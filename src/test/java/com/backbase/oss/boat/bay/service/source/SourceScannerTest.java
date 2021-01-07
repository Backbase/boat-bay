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
    FileSystemExporter fileSystemExporter;

    @Test
    public void fileSystemSourceScannerTest() throws ExportException {
        FileSystemSourceScanner scanner =new FileSystemSourceScanner(portalRepository,productRepository,capabilityRepository,serviceDefinitionRepository,specRepository,new ObjectMapper(new YAMLFactory()));
        Source source = new Source();
        source.addSourcePath(new SourcePath().name("/Users/sophiej/Documents/Projects/opensauce/boat-bay/boat-bay-data/Artifactory"));
        scanner.setSource(source);
        List<Spec> specs = scanner.scan();
        Portal portal = portalRepository.getOne(specs.get(0).getPortal().getId());

        ExportInfo export = fileSystemExporter.export(new ExportOptions(portal, ExportType.FILE_SYSTEM, "/target/exporter"));

        assertThat(export.toString().equals(""));


    }
}
