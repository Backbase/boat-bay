package com.backbase.oss.boat.bay.service.exporter.impl;

import com.backbase.oss.boat.ExportException;
import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.PortalRepository;
import com.backbase.oss.boat.bay.service.export.ExportOptions;
import com.backbase.oss.boat.bay.service.export.ExportType;
import com.backbase.oss.boat.bay.service.export.impl.FileSystemExporter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = BoatBayApp.class)
class FileSystemExporterIT {

    @Autowired
    FileSystemExporter fileSystemExporter;

    @Autowired
    PortalRepository portalRepository;

    @Test
    void testFileSystemExport() throws ExportException {

        for (Portal portal : portalRepository.findAll()) {
            fileSystemExporter.export(new ExportOptions(portal, ExportType.FILE_SYSTEM, "/target/exporter"));
        }
    }

}
