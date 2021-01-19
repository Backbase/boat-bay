package com.backbase.oss.boat.bay.web.utils;

import com.backbase.oss.boat.ExportException;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.service.export.ExportOptions;
import com.backbase.oss.boat.bay.service.export.ExportType;
import com.backbase.oss.boat.bay.service.export.impl.FileSystemExporter;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.bay.service.source.SpecSourceResolver;
import com.backbase.oss.boat.bay.service.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.web.rest.SpecResource;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;
import com.backbase.oss.boat.bay.web.views.lint.BoatLintReport;
import com.backbase.oss.boat.bay.web.views.lint.LintReportMapper;
import io.github.jhipster.web.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@Transactional
@RequiredArgsConstructor
public class UploadSpecs {

    private final Logger log = LoggerFactory.getLogger(SpecResource.class);
    private final SourceRepository sourceRepository;
    private final SpecSourceResolver specSourceResolver;
    private final FileSystemExporter fileSystemExporter;
    private final SpecRepository specRepository;
    private final BoatSpecLinter boatSpecLinter;
    private final LintReportMapper lintReportMapper;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private static final String ENTITY_NAME = "spec";
    private static final String SPEC_CREATOR= "MavenPluginUpload";



    @PutMapping("boat-maven-plugin/{sourceId}/upload")
    public ResponseEntity<List<BoatLintReport>> uploadSpec(@Valid @RequestBody UploadRequestBody requestBody, @PathVariable String sourceId) throws URISyntaxException, ExportException {

        Source source = sourceRepository.findById(Long.parseLong(sourceId)).orElseThrow(() -> new BadRequestAlertException("Invalid source, source Id does not exist", "SOURCE", "sourceIdInvalid"));

        List<UploadRequestBody.UploadSpec> requestSpecs = requestBody.getSpecs();

        if (requestBody.getProjectId().isEmpty()
            || requestBody.getVersion().isEmpty()
            || requestBody.getArtifactId().isEmpty()) {
            throw new BadRequestAlertException("Invalid Request body missing attributes", ENTITY_NAME, "attributeempty");
        }


       List<Spec> specs = new ArrayList<>();


        for (UploadRequestBody.UploadSpec uploadSpec : requestSpecs) {

            Spec spec = mapSpec(uploadSpec);

            log.debug("REST request to upload : {}", spec.getKey());

            if (spec.getFilename().isEmpty())
                spec.setFilename(requestBody.getArtifactId() + "-api-v" + requestBody.getVersion() + ".yaml");

            if (spec.getOpenApi().isEmpty()
                || spec.getKey().isEmpty())
                throw new BadRequestAlertException("Invalid spec with an empty api, key, or file name", ENTITY_NAME, "attributeempty");

            spec.setPortal(source.getPortal());
            spec.setProduct(source.getProduct());
            spec.setVersion(requestBody.getVersion().replaceAll("(\\.|-)(\\w{2,})+" , ""));
            spec.setSource(source);
            spec.setSourceName(spec.getFilename());
            spec.setCreatedBy(SPEC_CREATOR);
            spec.setCreatedOn(Instant.now());
            spec.setSourcePath("/" +
                requestBody.getProjectId().substring(
                    requestBody.getProjectId().lastIndexOf(".")+1)
                + "/" +
                spec.getFilename());

            Spec match = new Spec().key(spec.getKey()).name(spec.getName());

            Optional<Spec> duplicate = specRepository.findOne(Example.of(match));

            if (duplicate.isPresent()) {
                spec.capability(duplicate.get().getCapability());
                spec.serviceDefinition(duplicate.get().getServiceDefinition());
            }

        }

        ScanResult scanResult = new ScanResult(source, specs);
        specSourceResolver.process(scanResult);

        String location = (requestBody.getLocation()).concat("/"+requestBody.getProjectId()).concat("/" + requestBody.getArtifactId()).concat("/" + source.getPortal().getName());

        ExportOptions exportSpec = new ExportOptions();
        exportSpec.setLocation(location);
        exportSpec.setPortal(source.getPortal());
        exportSpec.setExportType(ExportType.FILE_SYSTEM);
        fileSystemExporter.export(exportSpec);

        List<Spec> specsProcessed = specRepository.findAll().stream().filter(spec -> spec.getSource().getId().equals(sourceId)).collect(Collectors.toList());
        List<BoatLintReport> lintReports = specsProcessed.stream().map(spec -> boatSpecLinter.lint(spec)).map(lintReport -> lintReportMapper.mapReport(lintReport)).collect(Collectors.toList());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, "SOURCE", source.getId().toString()))
            .body(lintReports);
    }


    private Spec mapSpec(UploadRequestBody.UploadSpec uploadSpec){

        Spec spec = new Spec();

        spec.setOpenApi(uploadSpec.getOpenApi());
        spec.setKey(uploadSpec.getKey());
        spec.setName(uploadSpec.getName());
        spec.setFilename(uploadSpec.getFilename());

        return spec;
    }

}
