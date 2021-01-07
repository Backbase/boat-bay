package com.backbase.oss.boat.bay.web.utils;

import com.backbase.oss.boat.ExportException;
import com.backbase.oss.boat.bay.repository.PortalRepository;
import com.backbase.oss.boat.bay.service.export.ExportInfo;
import com.backbase.oss.boat.bay.service.export.ExportOptions;
import com.backbase.oss.boat.bay.service.export.Exporter;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Transactional
@RequiredArgsConstructor
public class ExportResource {

    private final PortalRepository portalRepository;

    private final List<Exporter> exporterList;


    @PostMapping("/export")
    public ResponseEntity<ExportInfo> export(@Valid @RequestBody ExportOptions exportOptions) throws ExportException {

        exportOptions.setPortal(portalRepository
            .findOne(Example.of(exportOptions.getPortal()))
            .orElseThrow(() -> new IllegalArgumentException("Portal does not exist")));

        return ResponseEntity.ok(exporterList.stream()
            .filter(ex -> ex.getExportType().equals(exportOptions.getExportType()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No Exporter registered with that name"))
            .export(exportOptions));
    }

}
