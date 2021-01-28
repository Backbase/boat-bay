package com.backbase.oss.boat.bay.web.views.dashboard.controller;

import com.backbase.oss.boat.ExportException;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.service.export.ExportOptions;
import com.backbase.oss.boat.bay.service.export.ExportType;
import com.backbase.oss.boat.bay.service.export.impl.FileSystemExporter;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.bay.service.model.BoatLintReport;
import com.backbase.oss.boat.bay.service.model.UploadRequestBody;
import com.backbase.oss.boat.bay.service.model.UploadSpec;
import com.backbase.oss.boat.bay.service.source.SpecSourceResolver;
import com.backbase.oss.boat.bay.service.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.service.source.scanner.SourceScannerOptions;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;
import com.backbase.oss.boat.bay.web.views.dashboard.mapper.BoatDashboardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

  private final SourceRepository sourceRepository;
  private final SpecSourceResolver specSourceResolver;
  private final FileSystemExporter fileSystemExporter;
  private final SpecRepository specRepository;
  private final BoatDashboardMapper lintReportMapper;
  private final BoatSpecLinter boatSpecLinter;


  @PutMapping("boat-maven-plugin/{sourceId}/upload")
  public ResponseEntity<List<BoatLintReport>> uploadSpec(@PathVariable String sourceId, @Valid @RequestBody UploadRequestBody requestBody) {

    Source source = sourceRepository.findById(Long.parseLong(sourceId)).orElseThrow(() -> new BadRequestAlertException("Invalid source, source Id does not exist", "SOURCE", "sourceIdInvalid"));

    List<UploadSpec> requestSpecs = requestBody.getSpecs();

    if (requestBody.getProjectId().isEmpty()
            || requestBody.getVersion().isEmpty()
            || requestBody.getArtifactId().isEmpty()) {
      throw new BadRequestAlertException("Invalid Request body missing attributes", "UploadSpec", "attributeempty");
    }


    List<Spec> specs = new ArrayList<>();


    for (UploadSpec uploadSpec : requestSpecs) {

      Spec spec = mapSpec(uploadSpec);

      log.debug("REST request to upload : {}", spec.getKey());

      if (spec.getFilename().isEmpty())
        spec.setFilename(requestBody.getArtifactId() + "-api-v" + requestBody.getVersion() + ".yaml");

      if (spec.getOpenApi().isEmpty()
              || spec.getKey().isEmpty())
        throw new BadRequestAlertException("Invalid spec with an empty api, key, or file name", "UploadSpec", "attributeempty");

      spec.setPortal(source.getPortal());
      spec.setProduct(source.getProduct());
      spec.setVersion(requestBody.getVersion().replaceAll("(\\.|-)(\\w{2,})+" , ""));
      spec.setSource(source);
      spec.setSourceName(spec.getFilename());
      spec.setCreatedBy("MavenPluginUpload");
      spec.setCreatedOn(ZonedDateTime.now());
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
    SourceScannerOptions scannerOptions = new SourceScannerOptions();
    ScanResult scanResult = new ScanResult(source,scannerOptions, specs);
    specSourceResolver.process(scanResult);

    String location = (requestBody.getLocation()).concat("/"+requestBody.getProjectId()).concat("/" + requestBody.getArtifactId()).concat("/" + source.getPortal().getName());

    ExportOptions exportSpec = new ExportOptions();
    exportSpec.setLocation(location);
    exportSpec.setPortal(source.getPortal());
    exportSpec.setExportType(ExportType.FILE_SYSTEM);
    try {
      fileSystemExporter.export(exportSpec);
    }catch (ExportException e) {
      e.printStackTrace();
    }


    List<Spec> specsProcessed = specRepository.findAll().stream().filter(spec -> spec.getSource().getId().equals(sourceId)).collect(Collectors.toList());
    List<BoatLintReport> lintReports = specsProcessed.stream().map(spec -> boatSpecLinter.lint(spec)).map(lintReport -> lintReportMapper.mapReport(lintReport)).collect(Collectors.toList());

    return ResponseEntity.ok(lintReports);
  }


  private Spec mapSpec(UploadSpec uploadSpec){

    Spec spec = new Spec();

    spec.setOpenApi(uploadSpec.getOpenApi());
    spec.setKey(uploadSpec.getKey());
    spec.setName(uploadSpec.getName());
    spec.setFilename(uploadSpec.getFileName());

    return spec;
  }
}
