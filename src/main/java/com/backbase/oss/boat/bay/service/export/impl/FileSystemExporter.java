package com.backbase.oss.boat.bay.service.export.impl;

import com.backbase.oss.boat.ExportException;
import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.service.export.ExportInfo;
import com.backbase.oss.boat.bay.service.export.ExportOptions;
import com.backbase.oss.boat.bay.service.export.ExportType;
import com.backbase.oss.boat.bay.service.export.Exporter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class FileSystemExporter implements Exporter {

    private final ObjectMapper objectMapper;


    public FileSystemExporter(JavaTimeModule javaTimeModule, Jdk8Module jdk8Module) {
        objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.registerModule(javaTimeModule);
        objectMapper.registerModule(jdk8Module);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.addMixIn(Portal.class, MixInPortal.class);
        objectMapper.addMixIn(Product.class, MixInProduct.class);
    }

    @Override
    public ExportInfo export(ExportOptions exportOptions) throws ExportException {
        Portal portal = exportOptions.getPortal();

        try {
            Path portalPath = Files.createDirectories(Path.of(exportOptions.getLocation(), portal.getName()));
            objectMapper.writeValue(new File(portalPath.toFile(), "portal.yaml"), portal);

            for (Product product : portal.getProducts()) {
                Path productPath = Files.createDirectories(portalPath.resolve(product.getKey()));
                objectMapper.writeValue(new File(productPath.toFile(), product.getName() + ".yaml"), product);
            }

            ExportInfo exportInfo = new ExportInfo();
            exportInfo.setLocation(portalPath.toString());
            return exportInfo;
        } catch (IOException e) {
            throw new ExportException("Failed to export", e);
        }


    }

    @Override
    public ExportType getExportType() {
        return ExportType.FILE_SYSTEM;
    }

    private abstract static class MixInPortal {

        @JsonIgnore
        abstract List<Product> getProducts();
    }

    private abstract static class MixInProduct {

        @JsonIgnore
        abstract Portal getPortal();

        @JsonIgnore
        abstract Set<Capability> getCapabilities();
    }
}
