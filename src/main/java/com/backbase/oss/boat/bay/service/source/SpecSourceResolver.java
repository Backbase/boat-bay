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
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpecSourceResolver {

    private final ExpressionParser parser = new SpelExpressionParser();

    private final SpecRepository specRepository;
    private final BoatCapabilityRepository capabilityRepository;
    private final BoatServiceRepository boatServiceRepository;

    private final BoatSpecRepository boatSpecRepository;

    public void processSpecs(List<Spec> specs) {

        specs.forEach(spec -> {
//            scanResult.getSourceSpec().getx
            Source source = spec.getSource();

            String md5 = spec.getChecksum();
            if (md5 == null) {
                md5 = DigestUtils.md5DigestAsHex(spec.getOpenApi().getBytes(StandardCharsets.UTF_8));
            }

            Optional<Spec> existingSpec = boatSpecRepository.findByChecksumAndSource(md5, source);
            if (existingSpec.isPresent()) {
                log.info("Spec: {}  already exists for source: {}", existingSpec.get().getName(), source.getName());
            } else {
                if (spec.getCapability() == null && source.getCapabilityKeySpEL() != null) {
                    Optional<String> capabilityKey = parseName(source.getCapabilityKeySpEL(), spec);
                    capabilityKey.ifPresent(key -> {
                        Capability capability = capabilityRepository.findByPortalAndKey(spec.getPortal(), key)
                            .orElseGet(() -> createCapabilityForSpecWithKey(spec, key));
                        spec.setCapability(capability);
                    });
                }
                if (spec.getServiceDefinition() == null
                    && source.getServiceNameSpEL() != null
                    && spec.getCapability() != null) {
                    Optional<String> serviceKey = parseName(source.getServiceNameSpEL(), spec);
                    serviceKey.ifPresent(key -> {
                        ServiceDefinition serviceDefinition = boatServiceRepository.findByCapabilityAndKey(spec.getCapability(), key)
                            .orElseGet(() -> createServiceDefinition(spec, key));
                        spec.setServiceDefinition(serviceDefinition);
                    });
                }
                specRepository.save(spec);
            }
        });
    }

    private ServiceDefinition createServiceDefinition(Spec spec, String key) {
        Optional<String> serviceNameSpEL = Optional.ofNullable(spec.getSource().getServiceNameSpEL());

        ServiceDefinition serviceDefinition = new ServiceDefinition();
        serviceDefinition.setCapability(spec.getCapability());
        serviceDefinition.setKey(key);
        serviceDefinition.setCreatedBy(spec.getCreatedBy());
        serviceDefinition.setCreatedOn(Instant.now());
        serviceDefinition.setName(serviceNameSpEL.map(exp -> parseName(exp, spec).orElse(key)).orElse(key));
        return boatServiceRepository.save(serviceDefinition);
    }

    private Capability createCapabilityForSpecWithKey(Spec spec, String key) {
        Optional<String> capabilityNameSpEL = Optional.ofNullable(spec.getSource().getCapabilityNameSpEL());

        Capability capability = new Capability();
        capability.setPortal(spec.getPortal());
        capability.setKey(key);
        capability.setCreatedBy(spec.getSource().getName());
        capability.setCreatedOn(Instant.now());

        capability.setName(capabilityNameSpEL.map(exp -> parseName(exp, spec).orElse(key)).orElse(key));
        capability.setTitle(key);
        return capabilityRepository.save(capability);
    }

    private Optional<String> parseName(String spEL, Spec value) {
        Expression exp = parser.parseExpression(spEL);

        Object name = exp.getValue(value);
        if (value != null) {
            if (name instanceof String) {
                return Optional.of((String) name);
            } else {
                log.info("Invalid expression: {}", spEL);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

}
