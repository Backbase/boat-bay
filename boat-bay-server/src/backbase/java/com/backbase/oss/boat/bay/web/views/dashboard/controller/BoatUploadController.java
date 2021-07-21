package com.backbase.oss.boat.bay.web.views.dashboard.controller;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.BoatLintReportRepository;
import com.backbase.oss.boat.bay.repository.BoatSourceRepository;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;
import com.backbase.oss.boat.bay.repository.ServiceDefinitionRepository;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.service.api.ApiBoatBayUpload;
import com.backbase.oss.boat.bay.service.model.BoatLintReport;
import com.backbase.oss.boat.bay.service.model.UploadRequestBody;
import com.backbase.oss.boat.bay.service.model.UploadSpec;
import com.backbase.oss.boat.bay.source.SpecSourceResolver;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.source.scanner.SourceScannerOptions;
import com.backbase.oss.boat.bay.util.SpringExpressionUtils;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;
import com.backbase.oss.boat.bay.web.views.dashboard.mapper.BoatDashboardMapper;
import com.backbase.oss.boat.loader.OpenAPILoader;
import com.backbase.oss.boat.loader.OpenAPILoaderException;
import io.github.jhipster.web.util.HeaderUtil;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoatUploadController implements ApiBoatBayUpload {

    private final BoatSourceRepository boatSourceRepository;
    private final SpecSourceResolver specSourceResolver;
    private final SpecRepository specRepository;
    private final CapabilityRepository capabilityRepository;
    private final BoatDashboardMapper lintReportMapper;
    private final BoatLintReportRepository boatLintReportRepository;
    private final ServiceDefinitionRepository serviceDefinitionRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private static final String ENTITY_NAME = "SPEC";
    private static final String SPEC_CREATOR = "MavenPluginUpload";

    public ResponseEntity<List<BoatLintReport>> uploadSpec(@PathVariable String sourceKey, @Valid @RequestBody UploadRequestBody requestBody)  {

        Source source = boatSourceRepository.findOne(Example.of(new Source().key(sourceKey))).orElseThrow(() -> new BadRequestAlertException("Invalid source, source Id does not exist", "SOURCE", "sourceIdInvalid"));

        List<UploadSpec> requestSpecs = requestBody.getSpecs();

        List<Spec> specs = new ArrayList<>();

        if (requestBody.getProjectId().isEmpty() || requestBody.getVersion().isEmpty()
            || requestBody.getArtifactId().isEmpty()) {
            throw new BadRequestAlertException("Invalid Request body missing attributes", "UPLOAD_REQUEST_BODY",
                "attributeEmpty");
        }

        log.info("Processing specs for upload from {}", sourceKey);
        for (UploadSpec uploadSpec : requestSpecs) {
            Spec spec = mapSpec(uploadSpec);

            log.info("REST request to upload : {}", spec.getKey());

            spec = setUpSpec(spec,source,requestBody);

            if (spec.getOpenApi().isEmpty()) {
                throw new BadRequestAlertException("Invalid spec with an empty openapi", "UPLOAD_SPEC",
                    "attributeempty");
            }

            Spec match = new Spec().key(spec.getKey()).name(spec.getName());
            Optional<Spec> duplicateKey = specRepository.findOne(Example.of(match));

            if (duplicateKey.isPresent()) {
                Spec existing = duplicateKey.get();

                if (!existing.getSource().equals(source)) {
                    spec.setKey(existing.getKey().concat("-" + spec.getProduct().getKey()));
                    log.info("Uploading new spec {}",spec.getKey());
                } else if (!spec.getVersion().equals(existing.getVersion())) {
                    spec.setKey(existing.getKey().concat("-" + spec.getVersion()));
                    log.info("Uploading new version {} of spec {}",spec.getVersion(), spec.getKey());
                } else if(requestBody.getVersion().contains("SNAPSHOT")){
                    existing.setOpenApi(spec.getOpenApi());
                    existing.setFilename(spec.getFilename());
                    existing.setLintReport(null);
                    spec = existing;
                    log.info("Spec {} already uploaded, updating with changes and re-linting",
                        spec.getKey());
                }else {
                    throw new BadRequestAlertException("This spec,"+spec.getKey()+", has already been uploaded, this upload is not from a"
                        + " project under development and so will be rejected", "SPEC",
                        "duplicateSpec");
                }
            }

            if (spec.getCapability() == null && spec.getServiceDefinition() == null) {

                Capability capability = new Capability()
                    .key(SpringExpressionUtils.parseName(source.getCapabilityKeySpEL(), requestBody, null))
                    .name(SpringExpressionUtils.parseName(source.getCapabilityNameSpEL(), requestBody, null))
                    .product(spec.getProduct());
                capabilityRepository.saveAndFlush(capability);

                ServiceDefinition serviceDefinition = new ServiceDefinition()
                    .key(SpringExpressionUtils.parseName(source.getServiceKeySpEL(), requestBody, null))
                    .name(SpringExpressionUtils.parseName(source.getServiceNameSpEL(), requestBody, null))
                    .capability(capability);
                serviceDefinitionRepository.saveAndFlush(serviceDefinition);

                capability.addServiceDefinition(serviceDefinition);
                capabilityRepository.save(capability);
                serviceDefinition.addSpec(spec);
                spec.setServiceDefinition(serviceDefinition);
                spec.setCapability(capability);
                log.debug("Spec {} new, creating capability and service from artifactId", spec.getKey());
            }

            specs.add(spec);

        }

        ScanResult scanResult = new ScanResult(source, new SourceScannerOptions());
        specs.stream().forEach(scanResult::addSpec);
        log.info("resolving specs {}", specs);
        specSourceResolver.process(scanResult);

        List<Spec> specsProcessed = specRepository.findAll().stream()
            .filter(spec -> spec.getSource().getKey().equals(sourceKey)).collect(Collectors.toList());
        List<BoatLintReport> lintReports = specsProcessed.stream()
            .map(spec -> boatLintReportRepository.findBySpec(spec)).filter(Optional::isPresent).map(Optional::get)
            .map(lintReportMapper::mapReport).collect(Collectors.toList());

        log.info("linted{}", lintReports);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, "SOURCE", source.getId().toString()))
            .body(lintReports);
    }


    private Spec mapSpec(UploadSpec uploadSpec) {
        Spec spec = new Spec();
        spec.setOpenApi(uploadSpec.getOpenApi());
        spec.setName(uploadSpec.getName());
        spec.setVersion(uploadSpec.getVersion());
        spec.setFilename(uploadSpec.getFileName());
        return spec;
    }

    private Spec setUpSpec(Spec spec, Source source, UploadRequestBody requestBody)  {
        spec.setPortal(source.getPortal());
        spec.setProduct(source.getProduct());
        spec.setSource(source);
        spec.setSourceName(spec.getFilename());
        spec.setCreatedBy("MavenPluginUpload");
        spec.setCreatedOn(ZonedDateTime.now());
        spec.setKey(SpringExpressionUtils.parseName(source.getSpecKeySpEL(), spec, spec.getKey()));
        if (spec.getKey().equals(null)){
            log.info("Filename of spec doesn't fit format specified in SpecKeySpEL set in source, you may later "
                + "encounter some issues");
        }
        spec.setSourcePath("/" +
            requestBody.getProjectId().substring(
                requestBody.getProjectId().lastIndexOf(".") + 1)
            + "/" +
            spec.getFilename());
        return spec;
    }


}
