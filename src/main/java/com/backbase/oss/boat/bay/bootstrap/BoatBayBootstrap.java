package com.backbase.oss.boat.bay.bootstrap;

import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SpecType;
import com.backbase.oss.boat.bay.exceptions.BootstrapException;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import com.backbase.oss.boat.bay.repository.SpecTypeRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatDashboardRepository;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty("backbase.bootstrap.file")
public class BoatBayBootstrap {

    @Value("${backbase.bootstrap.file}")
    private File bootstrapFile;

    private final BoatPortalRepository portalRepository;
    private final SourceRepository sourceRepository;
    private final BoatDashboardRepository dashboardRepository;
    private final SpecTypeRepository specTypeRepository;

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
                    log.info("Bootstrapping portal: {}-{}", portal.getName(), portal.getVersion());
                    portalRepository.save(portal);
                }
            });

            Dashboard dashboard = bootstrap.getDashboard();

            if (dashboard != null) {
                Optional<Dashboard> existingDashboard = dashboardRepository.findDashboardByName(dashboard.getName());
                if (existingDashboard.isEmpty()) {
                    Portal portal = portalRepository.findOne(Example.of(dashboard.getDefaultPortal())).orElseThrow(() -> new BootstrapException("Cannot create dashboard with portal: " + dashboard.getDefaultPortal() + " as it does not exist"));
                    dashboard.setDefaultPortal(portal);
                    log.info("Bootstrapping dashboard: {}", dashboard.getName());
                    dashboardRepository.save(dashboard);
                }
            }

            for (Source source : bootstrap.getSources()) {
                Optional<Source> existingSource = sourceRepository.findOne(Example.of(new Source().name(source.getName())));
                if (existingSource.isEmpty()) {
                    Portal portal = portalRepository.findOne(Example.of(source.getPortal())).orElseThrow(() -> new BootstrapException("Cannot create source with portal: " + source.getPortal() + " as it does not exist"));
                    source.setPortal(portal);
                    log.info("Bootstrapping source: {}", source.getName());
                    sourceRepository.save(source);
                }
            }

            if (bootstrap.getSpecTypes() != null) {
                bootstrap.getSpecTypes().forEach(specType -> {
                    Optional<SpecType> existingSpecType = specTypeRepository.findOne(Example.of(specType));
                    if (existingSpecType.isEmpty()) {
                        log.info("Bootstrapping Spec Type: {}", specType.getName());
                        specTypeRepository.save(specType);
                    }
                });
            }

        } catch (IOException e) {
            log.error("Failed to read bootstrap yaml file from location: {}", bootstrapFile, e);
        } catch (BootstrapException e) {
            log.error("Failed to bootstrap ", e);
        }
    }

    @Data
    public static class Bootstrap {
        private List<Portal> portals;
        private List<Source> sources;
        private Dashboard dashboard;
        private List<SpecType> specTypes;
    }

}
