package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.SpecType;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.repository.SpecTypeRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatCapabilityRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatProductRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatServiceRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatSpecRepository;
import com.backbase.oss.boat.bay.util.SpringExpressionUtils;
import com.backbase.oss.boat.loader.OpenAPILoader;
import com.backbase.oss.boat.loader.OpenAPILoaderException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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


    public void processSpecs(List<Spec> specs) {
        specs.forEach(this::processSpec);
    }

    @SuppressWarnings("java:S5411")
    private void processSpec(Spec spec) {
        Source source = spec.getSource();


        String md5 = spec.getChecksum();
        if (md5 == null) {
            md5 = DigestUtils.md5DigestAsHex(spec.getOpenApi().getBytes(StandardCharsets.UTF_8));
        }
        Optional<Spec> existingSpec = boatSpecRepository.findByChecksumAndSource(md5, source);

        if (existingSpec.isPresent() && !source.isOverwriteChanges()) {
            log.info("Spec: {}  already exists for source: {}", existingSpec.get().getName(), source.getName());
            return;
        } else if (existingSpec.isPresent() && source.isOverwriteChanges()) {
            log.info("Updating spec: {}", spec.getName());
            spec.setId(existingSpec.get().getId());
        } else {
            log.info("Spec: {} is not yet in BOAT BAY. Creating new Spec", spec.getName());
        }
        setProduct(spec, source);
        setCapability(spec, source);
        setServiceDefinition(spec, source);
        setSpecType(spec);
        setInformationFromSpec(spec);

        log.info("Storing spec: {}", spec.getName());
        specRepository.save(spec);
    }

    private void setInformationFromSpec(Spec spec) {
        try {
            OpenAPI openAPI = OpenAPILoader.parse(spec.getOpenApi());

            Info info = openAPI.getInfo();
            spec.setVersion(info.getVersion());
            spec.setTitle(info.getTitle());
            spec.setDescription(info.getDescription());
            spec.setValid(true);
            if (openAPI.getTags() != null) {
                spec.setTagsCsv(openAPI.getTags().stream().map(Tag::getName).collect(Collectors.joining(",")));
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

    private void setProduct(Spec spec, Source source) {
        if (spec.getProduct() == null || source.isOverwriteChanges()) {
            String key = SpringExpressionUtils.parseName(source.getProductKeySpEL(), spec, "unknown");
            Product product = boatProductRepository.findByPortalAndKey(spec.getPortal(), key)
                .orElseGet(() -> createProductWithKey(spec, key));
            log.info("Assigning product: {} to spec: {}", product.getName(), spec.getName());
            spec.setProduct(product);
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
        serviceDefinition.setName(serviceNameSpEL.map(exp -> SpringExpressionUtils.parseName(exp, spec, key)).orElse(key));
        return boatServiceRepository.save(serviceDefinition);
    }

    private Product createProductWithKey(Spec spec, String key) {
        log.info("Creating product: {} with spec: {}", key, spec.getName());

        Optional<String> productNameSpEL = Optional.of(spec.getSource().getProductNameSpEL());
        Product product = new Product();
        product.setKey(key);
        product.setPortal(spec.getPortal());
        product.setCreatedBy(spec.getCreatedBy());
        product.setCreatedOn(Instant.now());
        product.setName(productNameSpEL.map(exp -> SpringExpressionUtils.parseName(exp, spec, key)).orElse(key));

        return boatProductRepository.save(product);

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
        capability.setTitle(key);
        return capabilityRepository.save(capability);
    }

}
