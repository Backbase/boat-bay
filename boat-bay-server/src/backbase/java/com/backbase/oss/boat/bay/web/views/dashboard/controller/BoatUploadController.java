package com.backbase.oss.boat.bay.web.views.dashboard.controller;

import com.backbase.oss.boat.bay.api.BoatMavenPluginApi;
import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.model.BoatLintReport;
import com.backbase.oss.boat.bay.model.UploadRequestBody;
import com.backbase.oss.boat.bay.model.UploadSpec;
import com.backbase.oss.boat.bay.repository.*;
import com.backbase.oss.boat.bay.source.SpecSourceResolver;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.util.SpringExpressionUtils;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;
import com.backbase.oss.boat.bay.web.views.dashboard.mapper.BoatDashboardMapper;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoatUploadController implements BoatMavenPluginApi {

    private final BoatSourceRepository boatSourceRepository;
    private final SpecSourceResolver specSourceResolver;
    private final SpecRepository specRepository;
    private final CapabilityRepository capabilityRepository;
    private final BoatDashboardMapper lintReportMapper;
    private final BoatLintReportRepository boatLintReportRepository;
    private final BoatServiceRepository boatServiceRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private static final String ENTITY_NAME = "SPEC";
    private static final String SPEC_CREATOR = "MavenPluginUpload";

    @Override
    public ResponseEntity<List<BoatLintReport>> uploadSpec(String portalKey, String sourceKey, UploadRequestBody requestBody) {
        Source source = boatSourceRepository
            .findOne(Example.of(new Source().key(sourceKey)))
            .orElseThrow(() -> new BadRequestAlertException("Invalid source, source Key does not exist", "SOURCE", "sourceIdInvalid"));

        List<UploadSpec> requestSpecs = requestBody.getSpecs();

        List<Spec> specs = new ArrayList<>();

        log.info("Processing specs for upload from {}", sourceKey);
        for (UploadSpec uploadSpec : requestSpecs) {
            Spec spec = setUpSpec(mapSpec(uploadSpec), source, requestBody);

            log.info("REST request to upload : {}", spec.getName());

            if (spec.getOpenApi().isEmpty()) {
                throw new BadRequestAlertException("Invalid spec with an empty openapi", "UPLOAD_SPEC", "attributeempty");
            }

            Spec match = new Spec().key(spec.getKey()).name(spec.getName());
            Optional<Spec> duplicateKey = specRepository.findOne(Example.of(match));

            if (duplicateKey.isPresent()) {
                spec = getAndUpdateExistingSpec(requestBody, source, spec, duplicateKey);
            }

            if (spec.getCapability() == null && spec.getServiceDefinition() == null) {
                String serviceKey = SpringExpressionUtils.parseName(
                    source.getServiceKeySpEL(),
                    spec,
                    spec.getFilename().substring(0, spec.getFilename().lastIndexOf("."))
                );
                Optional<ServiceDefinition> serviceDefinition = boatServiceRepository.findByKey(serviceKey);
                // Existing service update
                if (serviceDefinition.isPresent()) {
                    Capability capability = serviceDefinition.get().getCapability();
                    spec.setCapability(capability);
                    if (source.getCapability() == null) {
                        source.setCapability(capability);
                    }
                } else {
                    // New Service and capability
                    Capability newCapability = new Capability()
                        .key(SpringExpressionUtils.parseName(source.getCapabilityKeySpEL(), requestBody, null))
                        .name(SpringExpressionUtils.parseName(source.getCapabilityNameSpEL(), requestBody, null))
                        .product(spec.getProduct());
                    capabilityRepository.saveAndFlush(newCapability);

                    ServiceDefinition newServiceDefinition = new ServiceDefinition()
                        .key(SpringExpressionUtils.parseName(source.getServiceKeySpEL(), requestBody, null))
                        .name(SpringExpressionUtils.parseName(source.getServiceNameSpEL(), requestBody, null))
                        .capability(newCapability);
                    boatServiceRepository.saveAndFlush(newServiceDefinition);

                    newCapability.addServiceDefinition(newServiceDefinition);
                    capabilityRepository.save(newCapability);
                    newServiceDefinition.addSpec(spec);
                    spec.setServiceDefinition(newServiceDefinition);
                    spec.setCapability(newCapability);
                    log.debug("Spec {} new, creating capability and service from artifactId", spec.getKey());
                }
            }

            specs.add(spec);
        }

        ScanResult scanResult = new ScanResult(source);
        specs.forEach(scanResult::addSpec);
        log.info("resolving specs {}", specs);
        specSourceResolver.process(scanResult);

        List<Spec> specsProcessed = specRepository
            .findAll()
            .stream()
            .filter(spec -> spec.getSource().getKey().equals(sourceKey))
            .collect(Collectors.toList());
        List<BoatLintReport> lintReports = specsProcessed
            .stream()
            .map(boatLintReportRepository::findBySpec)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(lintReportMapper::mapReport)
            .collect(Collectors.toList());

        log.info("linted{}", lintReports);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, "SOURCE", source.getId().toString()))
            .body(lintReports);
    }

    @NotNull
    private Spec getAndUpdateExistingSpec(UploadRequestBody requestBody, Source source, Spec spec, Optional<Spec> duplicateKey) {
        Spec existing = duplicateKey.get();

        if (!existing.getSource().equals(source)) {
            spec.setKey(existing.getKey().concat("-" + spec.getProduct().getKey()));
            log.info("Uploading new spec {}", spec.getKey());
        } else if (spec.getVersion() != null && !spec.getVersion().equals(existing.getVersion())) {
            spec.setKey(existing.getKey().concat("-" + spec.getVersion()));
            log.info("Uploading new version {} of spec {}", spec.getVersion(), spec.getKey());
        } else if (requestBody.getVersion().contains("SNAPSHOT")) {
            existing.setOpenApi(spec.getOpenApi());
            existing.setFilename(spec.getFilename());
            existing.setLintReport(null);
            spec = existing;
            log.info("Spec {} already uploaded, updating with changes and re-linting", spec.getKey());
        } else {
            throw new BadRequestAlertException(
                "This spec," +
                spec.getKey() +
                ", has already been uploaded, this upload is not from a" +
                " project under development and so will be rejected",
                "SPEC",
                "duplicateSpec"
            );
        }
        return spec;
    }

    private Spec mapSpec(UploadSpec uploadSpec) {
        Spec spec = new Spec();
        spec.setOpenApi(uploadSpec.getOpenApi());
        spec.setName(uploadSpec.getName());
        spec.setFilename(uploadSpec.getFileName());
        return spec;
    }

    private Spec setUpSpec(Spec spec, Source source, UploadRequestBody requestBody) {
        spec.setPortal(source.getPortal());
        spec.setProduct(source.getProduct());
        spec.setSource(source);
        spec.setSourceName(spec.getFilename());
        spec.setCreatedBy("MavenPluginUpload");
        spec.setCreatedOn(ZonedDateTime.now());
        spec.setKey(SpringExpressionUtils.parseName(source.getSpecKeySpEL(), spec, spec.getKey()));
        spec.setSourcePath(spec.getFilename());
        spec.setMvnGroupId(requestBody.getGroupId());
        spec.setMvnArtifactId(requestBody.getArtifactId());
        spec.setMvnVersion(requestBody.getVersion());
        return spec;
    }
}
