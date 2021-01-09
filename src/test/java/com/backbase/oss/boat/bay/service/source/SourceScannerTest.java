package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.ExportException;
import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.*;
import com.backbase.oss.boat.bay.service.export.ExportInfo;
import com.backbase.oss.boat.bay.service.export.ExportOptions;
import com.backbase.oss.boat.bay.service.export.ExportType;
import com.backbase.oss.boat.bay.service.export.impl.FileSystemExporter;
import com.backbase.oss.boat.bay.service.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.service.source.scanner.impl.FileSystemSourceScanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@SpringBootTest(classes = BoatBayApp.class)
public class SourceScannerTest {
    @Autowired
    PortalRepository portalRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CapabilityRepository capabilityRepository;

    @Autowired
    ServiceDefinitionRepository serviceDefinitionRepository;

    @Autowired
    SpecRepository specRepository;

    @Autowired
    ProductReleaseRepository productReleaseRepository;

    @Autowired
    SpecTypeRepository specTypeRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    FileSystemExporter fileSystemExporter;

    @Test
    public void fileSystemSourceScannerTest() throws ExportException {
        FileSystemSourceScanner scanner =new FileSystemSourceScanner(portalRepository,productRepository,capabilityRepository,serviceDefinitionRepository,specRepository,productReleaseRepository, specTypeRepository,tagRepository,new ObjectMapper(new YAMLFactory()));
        Source source = new Source();
        source.addSourcePath(new SourcePath().name("/Users/sophiej/Documents/Projects/opensauce/boat-bay/boat-bay-data/repo.backbase.com"));
        scanner.setSource(source);
        ScanResult result = scanner.scan();
        Portal portal = result.getSource().getPortal();

        ExportInfo export = fileSystemExporter.export(new ExportOptions(portal, ExportType.FILE_SYSTEM, "/Users/sophiej/Documents/Projects/opensauce/boat-bay/boat-bay/test-target/exporter"));

        assertThat(export.toString().equals(""));


    }

    @Test
    public void fileSystemSourceScannerGithubInputTest() throws ExportException {
        FileSystemSourceScanner scanner =new FileSystemSourceScanner(portalRepository,productRepository,capabilityRepository,serviceDefinitionRepository,specRepository,productReleaseRepository, specTypeRepository,tagRepository,new ObjectMapper(new YAMLFactory()));
        Source source = new Source();
        source.addSourcePath(new SourcePath().name("https://github.com/Backbase/boat-bay.git"));
        scanner.setSource(source);
        ScanResult result = scanner.scan();
        Portal portal = result.getSource().getPortal();

        ExportInfo export = fileSystemExporter.export(new ExportOptions(portal, ExportType.FILE_SYSTEM, "/Users/sophiej/Documents/Projects/opensauce/boat-bay/boat-bay/test-target/exporter"));

        assertThat(export.toString().equals(""));


    }
}
