package com.backbase.oss.boat.bay.web.views.dashboard.controller;

import com.backbase.oss.boat.ExportException;
import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.BoatSourceRepository;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.service.export.ExportOptions;
import com.backbase.oss.boat.bay.service.export.ExportType;
import com.backbase.oss.boat.bay.service.export.impl.FileSystemExporter;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.bay.source.SpecSourceResolver;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.source.scanner.SourceScannerOptions;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatUploadRequestBody;
import com.backbase.oss.boat.bay.web.views.dashboard.mapper.BoatDashboardMapper;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatLintReport;
import io.github.jhipster.web.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boat/")
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoatUploadController {

    private final BoatSourceRepository boatSourceRepository;
    private final SpecSourceResolver specSourceResolver;
    private final FileSystemExporter fileSystemExporter;
    private final SpecRepository specRepository;
    private final BoatSpecLinter boatSpecLinter;
    private final BoatDashboardMapper lintReportMapper;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private static final String ENTITY_NAME = "spec";
    private static final String SPEC_CREATOR = "MavenPluginUpload";


    @PutMapping("boat-maven-plugin/{sourceKey}/upload")
    public ResponseEntity<List<BoatLintReport>> uploadSpec(@Valid @RequestBody BoatUploadRequestBody requestBody, @PathVariable String sourceKey) throws URISyntaxException, ExportException {

        Source source = boatSourceRepository.findOne(Example.of(new Source().key(sourceKey))).orElseThrow(() -> new BadRequestAlertException("Invalid source, source Id does not exist", "SOURCE", "sourceIdInvalid"));

        List<BoatUploadRequestBody.UploadSpec> requestSpecs = requestBody.getSpecs();

        if (requestBody.getProjectId().isEmpty()
            || requestBody.getVersion().isEmpty()
            || requestBody.getArtifactId().isEmpty()) {
            throw new BadRequestAlertException("Invalid Request body missing attributes", ENTITY_NAME, "attributeempty");
        }


        List<Spec> specs = new ArrayList<>();


        for (BoatUploadRequestBody.UploadSpec uploadSpec : requestSpecs) {

            Spec spec = mapSpec(uploadSpec);

            log.debug("REST request to upload : {}", spec.getKey());

            if (spec.getFilename().isEmpty())
                spec.setFilename(requestBody.getArtifactId() + "-api-v" + requestBody.getVersion() + ".yaml");

            if (spec.getOpenApi().isEmpty()
                || spec.getKey().isEmpty())
                throw new BadRequestAlertException("Invalid spec with an empty api, key, or file name", ENTITY_NAME, "attributeempty");

            spec.setPortal(source.getPortal());
            spec.setProduct(source.getProduct());
            spec.setVersion(requestBody.getVersion().replaceAll("([.\\-])(\\w{2,})+", ""));
            spec.setSource(source);
            spec.setSourceName(spec.getFilename());
            spec.setCreatedBy(SPEC_CREATOR);
            spec.setCreatedOn(ZonedDateTime.now());
            spec.setSourcePath("/" +
                requestBody.getProjectId().substring(
                    requestBody.getProjectId().lastIndexOf(".") + 1)
                + "/" +
                spec.getFilename());

            Spec match = new Spec().key(spec.getKey()).name(spec.getName());

            Optional<Spec> duplicate = specRepository.findOne(Example.of(match));

            if (duplicate.isPresent()) {
                spec.capability(duplicate.get().getCapability());
                spec.serviceDefinition(duplicate.get().getServiceDefinition());
            } else {
                spec.capability(new Capability().key(requestBody.getArtifactId()).name(requestBody.getArtifactId()));
            }

        }

        ScanResult scanResult = new ScanResult(source, new SourceScannerOptions(), specs);
        specSourceResolver.process(scanResult);

        String location = requestBody.getLocation();

        ExportOptions exportSpec = new ExportOptions();
        exportSpec.setLocation(location);
        exportSpec.setPortal(source.getPortal());
        exportSpec.setExportType(ExportType.FILE_SYSTEM);
        fileSystemExporter.export(exportSpec);

        List<Spec> specsProcessed = specRepository.findAll().stream().filter(spec -> false).collect(Collectors.toList());
        List<BoatLintReport> lintReports = specsProcessed.stream().map(boatSpecLinter::lint).map(lintReportMapper::mapReport).collect(Collectors.toList());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, "SOURCE", source.getId().toString()))
            .body(lintReports);
    }


    private Spec mapSpec(BoatUploadRequestBody.UploadSpec uploadSpec) {

        Spec spec = new Spec();

        spec.setOpenApi(uploadSpec.getOpenApi());
        spec.setKey(uploadSpec.getKey());
        spec.setName(uploadSpec.getName());
        spec.setFilename(uploadSpec.getFilename());

        return spec;
    }
}
