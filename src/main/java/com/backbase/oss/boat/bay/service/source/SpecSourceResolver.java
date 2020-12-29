package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatCapabilityRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatServiceRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatSpecRepository;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final BoatCapabilityRepository capabilityRepository;
    private final BoatServiceRepository boatServiceRepository;

    private final BoatSpecRepository boatSpecRepository;

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
        setCapability(spec, source);
        setServiceDefinition(spec, source);
        log.info("Storing spec: {}", spec.getName());
        specRepository.save(spec);
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
            Capability capability = capabilityRepository.findByPortalAndKey(spec.getPortal(), key)
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
        serviceDefinition.setName(serviceNameSpEL.map(exp -> parseName(exp, spec, key)).orElse(key));
        return boatServiceRepository.save(serviceDefinition);
    }

    private Capability createCapabilityForSpecWithKey(Spec spec, String key) {
        log.info("Creating capability: {} for spec: {}", key, spec.getName());
        Optional<String> capabilityNameSpEL = Optional.ofNullable(spec.getSource().getCapabilityNameSpEL());

        Capability capability = new Capability();
        capability.setPortal(spec.getPortal());
        capability.setKey(key);
        capability.setCreatedBy(spec.getSource().getName());
        capability.setCreatedOn(Instant.now());

        capability.setName(capabilityNameSpEL.map(exp -> parseName(exp, spec, key)).orElse(key));
        capability.setTitle(key);
        return capabilityRepository.save(capability);
    }

    public static String parseName(String spEL, Spec spec, String fallback) {
        if (spEL == null) {
            return fallback;
        }
        log.info("Parsing SpEL: {} from spec:{}", spEL, spec.getName());
        Expression exp = parser.parseExpression(spEL);
        Object name;
        try {
            name = exp.getValue(spec);
        } catch (EvaluationException | StringIndexOutOfBoundsException e) {
            log.warn("Expression: {} failed on: {}", spEL, spec);
            return fallback;
        }
        if (name instanceof String) {
            log.info("Resolved: {}", name);
            return (String) name;
        } else {
            log.info("Invalid expression: {}", spEL);
            return fallback;
        }
    }

}
