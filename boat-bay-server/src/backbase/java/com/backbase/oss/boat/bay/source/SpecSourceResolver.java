package com.backbase.oss.boat.bay.source;

import com.backbase.oss.boat.bay.domain.*;
import com.backbase.oss.boat.bay.exceptions.InvalidSpecException;
import com.backbase.oss.boat.bay.repository.*;
import com.backbase.oss.boat.bay.service.backwardscompatible.BoatBackwardsCompatibleChecker;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.util.SpringExpressionUtils;
import com.backbase.oss.boat.loader.OpenAPILoader;
import com.backbase.oss.boat.loader.OpenAPILoaderException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.joda.time.Seconds;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
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
        log.info("Processing Scan Result from {} with {} specs", scan.getSource().getName(), scan.specCount());
        Instant start = Instant.now();
        Source source = scan.getSource();
        for (ProductRelease pr : scan.getProductReleases()) {
            processProductRelease(scan, source, pr);
        }
        List<Spec> processedSpecs = scan
            .getProductReleases()
            .stream()
            .flatMap(pr -> pr.getSpecs().stream())
            .filter(spec -> spec.getId() != null)
            .collect(Collectors.toList());

        checkSpecs(processedSpecs);
        Instant end = Instant.now();
        log.info(
            "Finished processing Scan Result from {} with {} specs in: {}s",
            scan.getSource().getName(),
            scan.specCount(),
            ChronoUnit.SECONDS.between(start, end)
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processProductRelease(ScanResult scan, Source source, ProductRelease pr) {
        for (Spec spec : pr.getSpecs()) {
            processSpec(spec);
        }

        productReleaseRepository
            .findByProductAndKey(source.getProduct(), pr.getKey())
            .orElseGet(() -> productReleaseRepository.saveAndFlush(pr));
    }

    private void checkSpecs(List<Spec> processedSpecs) {
        log.info("Checking Specs");
        for (Spec processedSpec : processedSpecs) {
            if (processedSpec.getLintReport() == null) {
                boatSpecLinter.lint(processedSpec);
            }
            if (processedSpec.getChanges() == null) {
                boatBackwardsCompatibleChecker.checkBackwardsCompatibility(processedSpec);
            }
        }
        log.info("Finished Checking Specs");
    }

    @SuppressWarnings("java:S5411")
    private void processSpec(Spec spec) {
        log.info("Processing spec: {}", spec.getName());

        Source source = spec.getSource();

        String md5 = spec.getChecksum();
        if (md5 == null) {
            md5 = DigestUtils.md5DigestAsHex(spec.getOpenApi().getBytes(StandardCharsets.UTF_8));
            spec.setChecksum(md5);
        }
        Optional<Spec> existingSpec = boatSpecRepository.findByChecksumAndSource(md5, source);

        boolean overwriteChanges = Boolean.TRUE.equals(source.getOverwriteChanges());
        if (existingSpec.isPresent() && !overwriteChanges) {
            log.info("Spec: {}  already exists for source: {}", existingSpec.get().getName(), source.getName());
            spec.setId(existingSpec.get().getId());
            return;
        } else if (existingSpec.isPresent()) {
            log.debug("Updating spec: {}", spec.getName());
            spec.setId(existingSpec.get().getId());
        } else {
            log.debug("Spec: {} is not yet in BOAT BAY. Creating new Spec", spec.getName());
        }
        OpenAPI openAPI;
        try {
            openAPI = OpenAPILoader.parse(spec.getOpenApi());
            setInformationFromSpec(spec, openAPI, source);
        } catch (OpenAPILoaderException | InvalidSpecException e) {
            String parseErrorMessage = e.getMessage();
            if (e instanceof OpenAPILoaderException) {
                if (((OpenAPILoaderException) e).getParseMessages() != null) {
                    parseErrorMessage += "\n" + String.join("\n\t" + ((OpenAPILoaderException) e).getParseMessages());
                }
                spec.setParseError(parseErrorMessage);
            } else {
                spec.setParseError(e.getMessage());
            }
            spec.setValid(false);
            spec.setVersion("");
            log.error("Failed to parse OpenAPI for item: {}", spec.getName());
        }

        setProduct(spec, source);
        setCapability(spec, source);
        setServiceDefinition(spec, source);
        setSpecType(spec);

        specRepository.saveAndFlush(spec);
        log.debug("Finished processing spec: {}", spec.getName());
    }

    private void setProduct(Spec spec, Source source) {
        Product product = source.getProduct();
        spec.setProduct(product);
        log.debug("Adding spec: {} to product: {}", spec.getName(), product.getName());
    }

    private void setInformationFromSpec(Spec spec, OpenAPI openAPI, Source source) throws InvalidSpecException {
        Info info = openAPI.getInfo();
        if (info == null) {
            throw new InvalidSpecException("Missing INFO block");
        }

        spec.setVersion(info.getVersion());
        spec.setTitle(info.getTitle());
        spec.setDescription(info.getDescription());
        spec.setValid(true);
        if (openAPI.getTags() != null) {
            Set<Tag> tags = openAPI.getTags().stream().map(this::getOrCreateTag).collect(Collectors.toSet());
            spec.setTags(tags);
        }

        if (info.getExtensions() != null && info.getExtensions().containsKey("x-icon")) {
            spec.setIcon(info.getExtensions().get("x-icon").toString());
        }

        spec.setKey(SpringExpressionUtils.parseName(source.getSpecKeySpEL(), spec, spec.getKey()));
    }

    @NotNull
    private Tag getOrCreateTag(io.swagger.v3.oas.models.tags.Tag tag) {
        return boatTagRepository
            .findByName(tag.getName())
            .orElseGet(() -> boatTagRepository.saveAndFlush(new Tag().name(tag.getName()).description(tag.getDescription()).hide(false)));
    }

    private void setSpecType(Spec spec) {
        SpecType specType = specTypeRepository
            .findAll()
            .stream()
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
        return specTypeRepository.findOne(Example.of(specType)).orElseGet(() -> createSpecType(specType));
    }

    @NotNull
    private SpecType createSpecType(SpecType specType) {
        return specTypeRepository.saveAndFlush(specType);
    }

    private void setServiceDefinition(Spec spec, Source source) {
        if (spec.getServiceDefinition() == null || source.getOverwriteChanges()) {
            String key = SpringExpressionUtils.parseName(
                source.getServiceNameSpEL(),
                spec,
                spec.getFilename().substring(0, spec.getFilename().lastIndexOf("."))
            );
            ServiceDefinition serviceDefinition = boatServiceRepository
                .findByCapabilityAndKey(spec.getCapability(), key)
                .orElseGet(() -> createServiceDefinition(spec, key));
            log.debug("Assigning service: {} to spec: {}", serviceDefinition.getName(), spec.getName());
            spec.setServiceDefinition(serviceDefinition);
        }
    }

    private void setCapability(Spec spec, Source source) {
        if (spec.getCapability() == null || source.getOverwriteChanges()) {
            String key = getCapabilityKey(spec, source);

            Capability capability = capabilityRepository
                .findByProductAndKey(spec.getProduct(), key)
                .orElseGet(() -> createCapabilityForSpecWithKey(spec, key));
            log.debug("Assigning capability: {} to spec: {}", capability.getName(), spec.getName());
            spec.setCapability(capability);
        }
    }

    private String getCapabilityKey(Spec spec, Source source) {
        return SpringExpressionUtils.parseName(source.getCapabilityKeySpEL(), spec, "unknown");
    }

    private ServiceDefinition createServiceDefinition(Spec spec, String key) {
        log.debug("Creating service: {} for spec: {}", key, spec.getName());
        Optional<String> serviceNameSpEL = Optional.ofNullable(spec.getSource().getServiceNameSpEL());

        ServiceDefinition serviceDefinition = new ServiceDefinition();
        serviceDefinition.setCapability(spec.getCapability());
        serviceDefinition.setKey(key);
        serviceDefinition.setCreatedBy(spec.getCreatedBy());
        serviceDefinition.setCreatedOn(ZonedDateTime.now());
        serviceDefinition.setName(
            StringUtils
                .capitalize(serviceNameSpEL.map(exp -> SpringExpressionUtils.parseName(exp, spec, key)).orElse(key))
                .replaceAll("-", " ")
        );
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
        capability.setName(
            StringUtils.capitalize(
                capabilityNameSpEL.map(exp -> SpringExpressionUtils.parseName(exp, spec, key)).orElse(key).replaceAll("-", " ")
            )
        );
        return capabilityRepository.saveAndFlush(capability);
    }
}
