package com.backbase.oss.boat.bay.bootstrap;

import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.repository.PortalRepository;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@DependsOn("liquibase")
public class BoatBayBootstrap {

    @Value("${backbase.bootstrap.file}")
    private File bootstrapFile;

    private final BoatPortalRepository portalRepository;
    private final SourceRepository sourceRepository;

    @EventListener({ContextRefreshedEvent.class})
    @Async
    public void loadBootstrapFile() {
        log.info("Loading bootstrap from: {}", bootstrapFile);
        ObjectMapper objectMapper = new ObjectMapper(YAMLFactory.builder().build());
        try {
            Bootstrap bootstrap = objectMapper.readValue(bootstrapFile, Bootstrap.class);
            bootstrap.getPortals().forEach(portal -> {
                Optional<Portal> existingPortal = portalRepository.findOne(Example.of(portal));
                if (existingPortal.isEmpty()) {
                    portalRepository.save(portal);
                }
            });

            bootstrap.getSources().forEach(source -> {
                Optional<Source> existingSource = sourceRepository.findOne(Example.of(source));
                if (existingSource.isEmpty()) {
                    source.setPortal(portalRepository.findByKey(source.getPortal().getKey()));
                    sourceRepository.save(source);
                }
            });

        } catch (IOException e) {
            log.error("Failed to read bootstrap yaml file from location: {}", bootstrapFile);
        }
    }

    @Data
    public static class Bootstrap {

        private List<Portal> portals;
        private List<Source> sources;
    }

}
