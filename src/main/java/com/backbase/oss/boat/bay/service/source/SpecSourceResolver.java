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
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpecSourceResolver {

    private static final ExpressionParser parser = new SpelExpressionParser();

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
        if (!spec.isValid()) {
            spec.version("");
        }
        setProduct(spec, source);
        setCapability(spec, source);
        setServiceDefinition(spec, source);
        setSpecType(spec);
        log.info("Storing spec: {}", spec.getName());
        specRepository.save(spec);
    }

    private void setSpecType(Spec spec) {
        SpecType specType = specTypeRepository.findAll().stream()
            .filter(st -> match(st.getMatchSpEL(), spec))
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
            String key = parseName(source.getServiceNameSpEL(), spec, spec.getFilename().substring(0, spec.getFilename().lastIndexOf(".")));
            ServiceDefinition serviceDefinition = boatServiceRepository.findByCapabilityAndKey(spec.getCapability(), key)
                .orElseGet(() -> createServiceDefinition(spec, key));
            log.info("Assigning service: {} to spec: {}", serviceDefinition.getName(), spec.getName());
            spec.setServiceDefinition(serviceDefinition);
        }
    }

    private void setCapability(Spec spec, Source source) {
        if (spec.getCapability() == null || source.isOverwriteChanges()) {
            String key = parseName(source.getCapabilityKeySpEL(), spec, "unknown");
            Capability capability = capabilityRepository.findByProductAndKey(spec.getProduct(), key)
                .orElseGet(() -> createCapabilityForSpecWithKey(spec, key));
            log.info("Assigning capability: {} to spec: {}", capability.getName(), spec.getName());
            spec.setCapability(capability);
        }
    }

    private void setProduct(Spec spec, Source source) {
        if (spec.getProduct() == null || source.isOverwriteChanges()) {
            String key = parseName(source.getProductKeySpEL(), spec, "unknown");
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
        serviceDefinition.setName(serviceNameSpEL.map(exp -> parseName(exp, spec, key)).orElse(key));
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
        product.setName(productNameSpEL.map(exp -> parseName(exp, spec, key)).orElse(key));

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

        capability.setName(capabilityNameSpEL.map(exp -> parseName(exp, spec, key)).orElse(key));
        capability.setTitle(key);
        return capabilityRepository.save(capability);
    }

    public static boolean match(String spEL, Spec spec) {
        if (spEL == null) {
            return false;
        }
        Expression exp = parser.parseExpression(spEL);
        Boolean match;
        try {
            match = exp.getValue(spec, Boolean.class);
            return Objects.requireNonNullElse(match, false);
        } catch (EvaluationException | StringIndexOutOfBoundsException e) {
            log.warn("Expression: {} failed on: {}", spEL, spec);
            return false;
        }
    }

    public static String parseName(String spEL, Spec spec, String fallback) {
        if (spEL == null) {
            return fallback;
        }
        log.debug("Parsing SpEL: {} from spec:{}", spEL, spec.getName());
        Expression exp = parser.parseExpression(spEL);
        String name;
        try {
            name = exp.getValue(spec, String.class);
        } catch (EvaluationException | StringIndexOutOfBoundsException e) {
            log.warn("Expression: {} failed on: {}", spEL, spec.getName(), e);
            return fallback;
        }
        log.debug("Resolved: {}", name);
        return name;
    }

}
