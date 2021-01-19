package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.SpecType;
import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.repository.ProductReleaseRepository;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.repository.SpecTypeRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatCapabilityRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatProductRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatServiceRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatSpecRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatTagRepository;
import com.backbase.oss.boat.bay.service.backwardscompatible.BoatBackwardsCompatibleChecker;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.bay.service.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.util.SpringExpressionUtils;
import com.backbase.oss.boat.loader.OpenAPILoader;
import com.backbase.oss.boat.loader.OpenAPILoaderException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpecSourceResolver {


    private final SpecRepository specRepository;
    private final BoatProductRepository boatProductRepository;
    private final BoatCapabilityRepository capabilityRepository;
    private final BoatServiceRepository boatServiceRepository;

    private final BoatSpecRepository boatSpecRepository;
    private final SpecTypeRepository specTypeRepository;
    private final BoatTagRepository boatTagRepository;
    private final ProductReleaseRepository productReleaseRepository;

    private final BoatSpecLinter boatSpecLinter;
    private final BoatBackwardsCompatibleChecker boatBackwardsCompatibleChecker;

    public void process(ScanResult scan) {
        Source source = scan.getSource();
        List<Spec> processedSpecs = processSpecs(scan);
        processReleases(scan, source, processedSpecs);
        lintSpecs(processedSpecs, scan);
        checkBackwardsCompatibility(processedSpecs, scan);
    }

    private void checkBackwardsCompatibility(List<Spec> processedSpecs, ScanResult scan) {
        log.info("Checking backwards compatibility for {} specs from scan result from source: {}", processedSpecs.size(), scan.getSource().getName());
        processedSpecs.forEach(boatBackwardsCompatibleChecker::scheduleBackwardsCompatibleCheck);
    }

    private void lintSpecs(List<Spec> processedSpecs, ScanResult scan) {
        log.info("Linting {} specs from scan result from source: {}", processedSpecs.size(), scan.getSource().getName());
        processedSpecs.forEach(boatSpecLinter::scheduleLintJob);
    }

    @Transactional
    public void processReleases(ScanResult scan, Source source, List<Spec> processedSpecs) {
        log.info("Processing {} releases from scan result from source: {}", scan.getProductReleases().size(), scan.getSource().getName());
        if (scan.getProductReleases().isEmpty()) {

            ProductRelease latest = new ProductRelease().key("latest").portal(source.getPortal());
            ProductRelease productRelease = productReleaseRepository.findOne(Example.of(latest)).orElseGet(() -> productReleaseRepository.save(latest.name("Latest").key("latest")));

            processedSpecs.stream()
                .collect(Collectors.toMap(Spec::getKey, spec -> spec, this::compareVersion))
                .forEach((s, spec) -> productRelease.addSpec(spec));
            productReleaseRepository.save(productRelease);
        } else {
            scan.getProductReleases().forEach(pr -> {
                if (!productReleaseRepository.exists(Example.of(new ProductRelease().portal(source.getPortal()).key(pr.getKey())))) {
                    log.info("Create Product Release: {} for Portal: {}", pr.getName(), source.getPortal());
                    pr.setPortal(source.getPortal());
                    if (pr.getKey() == null && pr.getName() != null) {
                        pr.setKey(pr.getName().toLowerCase(Locale.ROOT));
                    }
                    productReleaseRepository.save(pr);
                }
            });
        }
    }

    @NotNull
    @Transactional
    public List<Spec> processSpecs(ScanResult scan) {
        log.info("Processing {} specs from scan result from source: {}", scan.getSpecs().size(), scan.getSource().getName());

        List<Spec> processedSpecs = scan.getSpecs().stream()
            .map(this::processSpec)
            .collect(Collectors.toList());
        return processedSpecs;
    }

    @NotNull
    private Spec compareVersion(Spec s1, Spec s2) {
        if (s1.getVersion().compareTo(s2.getVersion()) > 0) {
            return s1;
        } else {
            return s2;
        }
    }

    @SuppressWarnings("java:S5411")
    private Spec processSpec(Spec spec) {
        Source source = spec.getSource();

        String md5 = spec.getChecksum();
        if (md5 == null) {
            md5 = DigestUtils.md5DigestAsHex(spec.getOpenApi().getBytes(StandardCharsets.UTF_8));
            spec.setChecksum(md5);
        }
        Optional<Spec> existingSpec = boatSpecRepository.findByChecksumAndSource(md5, source);

        if (existingSpec.isPresent() && !source.isOverwriteChanges()) {
            log.info("Spec: {}  already exists for source: {}", existingSpec.get().getName(), source.getName());
            spec.setId(existingSpec.get().getId());
            return spec;
        } else if (existingSpec.isPresent() && source.isOverwriteChanges()) {
            log.info("Updating spec: {}", spec.getName());
            spec.setId(existingSpec.get().getId());
        } else {
            log.info("Spec: {} is not yet in BOAT BAY. Creating new Spec", spec.getName());
        }
        setInformationFromSpec(spec, source);
        setProduct(spec, source);
        setCapability(spec, source);
        setServiceDefinition(spec, source);
        setSpecType(spec);

        log.info("Storing spec: {}", spec.getName());
        return specRepository.save(spec);
    }

    private void setProduct(Spec spec, Source source) {
        Product product = source.getProduct();
        spec.setProduct(product);
        log.info("Adding spec: {} to product: {}", spec.getName(), product.getName());
    }

    private void setInformationFromSpec(Spec spec, Source source) {
        try {
            OpenAPI openAPI = OpenAPILoader.parse(spec.getOpenApi());

            Info info = openAPI.getInfo();
            spec.setVersion(info.getVersion());
            spec.setTitle(info.getTitle());
            spec.setDescription(info.getDescription());
            spec.setValid(true);
            if (openAPI.getTags() != null) {
                Set<Tag> tags = openAPI.getTags().stream()
                    .map(this::getOrCreateTag)
                    .collect(Collectors.toSet());
                spec.setTags(tags);
            }

            if (info.getExtensions() != null && info.getExtensions().containsKey("x-icon")) {
                spec.setIcon(info.getExtensions().get("x-icon").toString());
            }
        } catch (OpenAPILoaderException e) {
            String parseErrorMessage = e.getMessage();
            if (e.getParseMessages() != null) {
                parseErrorMessage += "\n" + String.join("\n\t" + e.getParseMessages());
            }
            spec.setParseError(parseErrorMessage);
            spec.setValid(false);
            spec.setVersion("");
            log.info("Failed to parse OpenAPI for item: {}", spec.getName());
        }

        spec.setKey(SpringExpressionUtils.parseName(source.getSpecKeySpEL(), spec, spec.getKey()));

    }

    @NotNull
    private Tag getOrCreateTag(io.swagger.v3.oas.models.tags.Tag tag) {
        return boatTagRepository.findByName(tag.getName())
            .orElseGet(() -> boatTagRepository.save(new Tag().name(tag.getName()).description(tag.getDescription()).hide(false)));
    }

    private void setSpecType(Spec spec) {
        SpecType specType = specTypeRepository.findAll().stream()
            .filter(st -> SpringExpressionUtils.match(st.getMatchSpEL(), spec))
            .findFirst()
            .orElseGet(this::genericSpecType);
        log.info("Assigning specType: {} to spec: {}", specType.getName(), spec.getName());
        spec.setSpecType(specType);
    }

    private SpecType genericSpecType() {
        SpecType specType = new SpecType();
        specType.setName("generic");
        specType.setIcon("api");
        return specTypeRepository.findOne(Example.of(specType))
            .orElseGet(() -> createSpecType(specType));

    }

    @NotNull
    private SpecType createSpecType(SpecType specType) {
        return specTypeRepository.save(specType);
    }

    private void setServiceDefinition(Spec spec, Source source) {
        if (spec.getServiceDefinition() == null || source.isOverwriteChanges()) {
            String key = SpringExpressionUtils.parseName(source.getServiceNameSpEL(), spec, spec.getFilename().substring(0, spec.getFilename().lastIndexOf(".")));
            ServiceDefinition serviceDefinition = boatServiceRepository.findByCapabilityAndKey(spec.getCapability(), key)
                .orElseGet(() -> createServiceDefinition(spec, key));
            log.info("Assigning service: {} to spec: {}", serviceDefinition.getName(), spec.getName());
            spec.setServiceDefinition(serviceDefinition);
        }
    }

    private void setCapability(Spec spec, Source source) {
        if (spec.getCapability() == null || source.isOverwriteChanges()) {
            String key = SpringExpressionUtils.parseName(source.getCapabilityKeySpEL(), spec, "unknown");
            Capability capability = capabilityRepository.findByProductAndKey(spec.getProduct(), key)
                .orElseGet(() -> createCapabilityForSpecWithKey(spec, key));
            log.info("Assigning capability: {} to spec: {}", capability.getName(), spec.getName());
            spec.setCapability(capability);
        }
    }

    private ServiceDefinition createServiceDefinition(Spec spec, String key) {
        log.info("Creating service: {} for spec: {}", key, spec.getName());
        Optional<String> serviceNameSpEL = Optional.ofNullable(spec.getSource().getServiceNameSpEL());

        ServiceDefinition serviceDefinition = new ServiceDefinition();
        serviceDefinition.setCapability(spec.getCapability());
        serviceDefinition.setKey(key);
        serviceDefinition.setCreatedBy(spec.getCreatedBy());
        serviceDefinition.setCreatedOn(Instant.now());
        serviceDefinition.setName(StringUtils.capitalize(serviceNameSpEL.map(exp -> SpringExpressionUtils.parseName(exp, spec, key)).orElse(key)).replaceAll("-", " "));
        serviceDefinition.setDescription(spec.getDescription());
        return boatServiceRepository.save(serviceDefinition);
    }

    private Capability createCapabilityForSpecWithKey(Spec spec, String key) {
        log.info("Creating capability: {} for spec: {}", key, spec.getName());
        Optional<String> capabilityNameSpEL = Optional.ofNullable(spec.getSource().getCapabilityNameSpEL());

        Capability capability = new Capability();

        capability.setProduct(spec.getProduct());
        capability.setKey(key);
        capability.setCreatedBy(spec.getSource().getName());
        capability.setCreatedOn(Instant.now());

        capability.setName(capabilityNameSpEL.map(exp -> SpringExpressionUtils.parseName(exp, spec, key)).orElse(key));
        return capabilityRepository.save(capability);
    }


}
