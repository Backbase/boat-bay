package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.SpecType;
import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.repository.SpecTypeRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatCapabilityRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatProductReleaseRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatProductRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatServiceRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatSpecRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatTagRepository;
import com.backbase.oss.boat.bay.service.backwardscompatible.BoatBackwardsCompatibleChecker;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.bay.service.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.service.source.scanner.SourceScannerOptions;
import com.backbase.oss.boat.bay.util.SpringExpressionUtils;
import com.backbase.oss.boat.loader.OpenAPILoader;
import com.backbase.oss.boat.loader.OpenAPILoaderException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpecSourceResolver {


    public static final String LATEST = "latest";
    private final SpecRepository specRepository;
    private final BoatProductRepository boatProductRepository;
    private final BoatCapabilityRepository capabilityRepository;
    private final BoatServiceRepository boatServiceRepository;

    private final BoatSpecRepository boatSpecRepository;
    private final SpecTypeRepository specTypeRepository;
    private final BoatTagRepository boatTagRepository;
    private final BoatProductReleaseRepository productReleaseRepository;

    private final BoatSpecLinter boatSpecLinter;
    private final BoatBackwardsCompatibleChecker boatBackwardsCompatibleChecker;

    public void process(ScanResult scan) {
        log.info("Processing Scan Result: {} from source: {}", scan.getSpecs().size() , scan.getSource().getName());
        Source source = scan.getSource();
        List<Spec> processedSpecs = processSpecs(scan);
        processReleases(scan, source, processedSpecs);
        checkSpecs(processedSpecs, scan);
        log.info("Finished Processing Scan Result: {} from source: {}", scan.getSpecs().size() , scan.getSource().getName());
    }

    public void checkSpecs(List<Spec> processedSpecs, ScanResult scan){
        log.info("Checking Specs");
        for (Spec processedSpec : processedSpecs) {


            if(processedSpec.getLintReport() == null) {
                boatSpecLinter.lint(processedSpec);
            }
            if(processedSpec.getChanges() == null) {
                boatBackwardsCompatibleChecker.checkBackwardsCompatibility(processedSpec);
            }
        }
        log.info("Finished Checking Specs");
    }

    @Transactional
    public void processReleases(ScanResult scan, Source source, List<Spec> processedSpecs) {
        log.info("Processing {} releases from scan result from source: {}", scan.getProductReleases().size(), scan.getSource().getName());
        if (scan.getProductReleases().isEmpty()) {

            ProductRelease productRelease = productReleaseRepository.findByProductAndKey(source.getProduct(), LATEST)
                .orElseGet(() -> createLatestProductRelease(source.getProduct()));

            processedSpecs.stream()
                .collect(Collectors.toMap(Spec::getKey, spec -> spec, this::compareVersion))
                .forEach((s, spec) -> productRelease.addSpec(spec));

            productReleaseRepository.saveAndFlush(productRelease);
        } else {
            scan.getProductReleases().forEach(pr -> {
                ProductRelease productRelease = productReleaseRepository.findByProductAndKey(source.getProduct(), pr.getKey())
                    .orElseGet(() -> productReleaseRepository.saveAndFlush(pr));
                log.info("Processed release: {}", productRelease.getName());
            });
        }
        log.info("Finished processing {} releases from scan result from source: {}", scan.getProductReleases().size(), scan.getSource().getName());
    }

    @NotNull
    private ProductRelease createLatestProductRelease(Product product) {
        ProductRelease newProductRelease = new ProductRelease();
        newProductRelease.setKey(LATEST.toLowerCase(Locale.ROOT));
        newProductRelease.setName(LATEST);
        newProductRelease.setVersion(LATEST);
        newProductRelease.setReleaseDate(ZonedDateTime.now());

        newProductRelease.setProduct(product);
        return productReleaseRepository.saveAndFlush(newProductRelease);
    }

    @NotNull
    @Transactional(isolation = Isolation.DEFAULT, propagation =  Propagation.MANDATORY)
    public List<Spec> processSpecs(ScanResult scan) {
        log.info("Processing {} specs from scan result from source: {}", scan.getSpecs().size(), scan.getSource().getName());

        List<Spec> list = new ArrayList<>();
        for (Spec spec : scan.getSpecs()) {
            Spec processSpec = processSpec(spec, scan.getScannerOptions());
            list.add(processSpec);
        }
        return list;
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Spec processSpec(Spec spec, SourceScannerOptions scannerOptions) {
        log.info("Processing spec: {}", spec.getName());

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
            log.debug("Updating spec: {}", spec.getName());
            spec.setId(existingSpec.get().getId());
        } else {
            log.debug("Spec: {} is not yet in BOAT BAY. Creating new Spec", spec.getName());
        }
        setInformationFromSpec(spec, source);
        setProduct(spec, source);
        setCapability(spec, source, scannerOptions);
        setServiceDefinition(spec, source);
        setSpecType(spec);

        Spec newSpec = specRepository.saveAndFlush(spec);
        log.debug("Finished processing spec: {}", newSpec.getName());
        return newSpec;
    }

    private void setProduct(Spec spec, Source source) {
        Product product = source.getProduct();
        spec.setProduct(product);
        log.debug("Adding spec: {} to product: {}", spec.getName(), product.getName());
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
            log.error("Failed to parse OpenAPI for item: {}", spec.getName());
        }

        spec.setKey(SpringExpressionUtils.parseName(source.getSpecKeySpEL(), spec, spec.getKey()));

    }

    @NotNull
    private Tag getOrCreateTag(io.swagger.v3.oas.models.tags.Tag tag) {
        return boatTagRepository.findByName(tag.getName())
            .orElseGet(() -> boatTagRepository.saveAndFlush(new Tag().name(tag.getName()).description(tag.getDescription()).hide(false)));
    }

    private void setSpecType(Spec spec) {
        SpecType specType = specTypeRepository.findAll().stream()
            .filter(st -> SpringExpressionUtils.match(st.getMatchSpEL(), spec))
            .findFirst()
            .orElseGet(this::genericSpecType);
        log.debug("Assigning specType: {} to spec: {}", specType.getName(), spec.getName());
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
        return specTypeRepository.saveAndFlush(specType);
    }

    private void setServiceDefinition(Spec spec, Source source) {
        if (spec.getServiceDefinition() == null || source.isOverwriteChanges()) {
            String key = SpringExpressionUtils.parseName(source.getServiceNameSpEL(), spec, spec.getFilename().substring(0, spec.getFilename().lastIndexOf(".")));
            ServiceDefinition serviceDefinition = boatServiceRepository.findByCapabilityAndKey(spec.getCapability(), key)
                .orElseGet(() -> createServiceDefinition(spec, key));
            log.debug("Assigning service: {} to spec: {}", serviceDefinition.getName(), spec.getName());
            spec.setServiceDefinition(serviceDefinition);
        }
    }

    private void setCapability(Spec spec, Source source, SourceScannerOptions scannerOptions) {
        if (spec.getCapability() == null || source.isOverwriteChanges()) {
            String key = getCapabilityKey(spec, source, scannerOptions);

            Capability capability = capabilityRepository.findByProductAndKey(spec.getProduct(), key)
                .orElseGet(() -> createCapabilityForSpecWithKey(spec, key));
            log.debug("Assigning capability: {} to spec: {}", capability.getName(), spec.getName());
            spec.setCapability(capability);
        }
    }

    private String getCapabilityKey(Spec spec, Source source, SourceScannerOptions scannerOptions) {
        String key = SpringExpressionUtils.parseName(source.getCapabilityKeySpEL(), spec, "unknown");

        if(scannerOptions != null && scannerOptions.getCapabilityMappingOverrides().containsKey(key)) {
            String override = scannerOptions.getCapabilityMappingOverrides().get(key);
            log.debug("Overriding capability key: {} with configuration override: {}", key, override);
            key = override;
        }
        return key;
    }

    private ServiceDefinition createServiceDefinition(Spec spec, String key) {
        log.debug("Creating service: {} for spec: {}", key, spec.getName());
        Optional<String> serviceNameSpEL = Optional.ofNullable(spec.getSource().getServiceNameSpEL());

        ServiceDefinition serviceDefinition = new ServiceDefinition();
        serviceDefinition.setCapability(spec.getCapability());
        serviceDefinition.setKey(key);
        serviceDefinition.setCreatedBy(spec.getCreatedBy());
        serviceDefinition.setCreatedOn(ZonedDateTime.now());
        serviceDefinition.setName(StringUtils.capitalize(serviceNameSpEL.map(exp -> SpringExpressionUtils.parseName(exp, spec, key)).orElse(key)).replaceAll("-", " "));
        serviceDefinition.setDescription(spec.getDescription());
        return boatServiceRepository.saveAndFlush(serviceDefinition);
    }

    private Capability createCapabilityForSpecWithKey(Spec spec, String key) {
        log.debug("Creating capability: {} for spec: {}", key, spec.getName());
        Optional<String> capabilityNameSpEL = Optional.ofNullable(spec.getSource().getCapabilityNameSpEL());
        Capability capability = new Capability();
        capability.setProduct(spec.getProduct());
        capability.setKey(key);
        capability.setCreatedBy(spec.getSource().getName());
        capability.setCreatedOn(ZonedDateTime.now());
        capability.setName(StringUtils.capitalize(capabilityNameSpEL.map(exp -> SpringExpressionUtils.parseName(exp, spec, key)).orElse(key).replaceAll("-", " ")));
        return capabilityRepository.saveAndFlush(capability);
    }


}
