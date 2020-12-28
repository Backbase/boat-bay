package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;
import com.backbase.oss.boat.bay.repository.CapabilityServiceDefinitionRepository;
import com.backbase.oss.boat.bay.repository.PortalRepository;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatSpecRepository;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpecSourceResolver {

    private final SpecRepository specRepository;
    private final CapabilityServiceDefinitionRepository capabilityServiceDefinitionRepository;
    private final CapabilityRepository capabilityRepository;
    private final PortalRepository portalRepository;

    private final BoatSpecRepository boatSpecRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void processSpecs(List<Spec> specs) {

        specs.forEach(spec -> {
//            scanResult.getSourceSpec().getx
            Source source = spec.getSource();

            String md5 = spec.getChecksum();
            if(md5 == null) {
                md5 = DigestUtils.md5DigestAsHex(spec.getOpenApi().getBytes(StandardCharsets.UTF_8));
            }

            Optional<Spec> existingSpec = boatSpecRepository.findByChecksumAndSource(md5, source);
            if(existingSpec.isPresent()) {
                log.info("Spec: {}  already exists for source: {}", existingSpec.get().getName(), source.getName());
            } else {






                specRepository.save(spec);
            }
        });
    }

}
