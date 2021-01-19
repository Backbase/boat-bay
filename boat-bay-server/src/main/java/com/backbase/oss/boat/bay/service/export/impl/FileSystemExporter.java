package com.backbase.oss.boat.bay.service.export.impl;

import com.backbase.oss.boat.ExportException;
import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.Tag;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@SuppressWarnings("java:S1610")
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
        objectMapper.addMixIn(Capability.class, MixInCapability.class);
        objectMapper.addMixIn(ServiceDefinition.class, MixInServiceDefinition.class);
        objectMapper.addMixIn(Spec.class, MixInSpec.class);
        objectMapper.addMixIn(ProductRelease.class, MixInProductRelease.class);
        objectMapper.addMixIn(Tag.class, MixIn.class);
    }

    @Override
    public ExportInfo export(ExportOptions exportOptions) throws ExportException {
        Portal portal = exportOptions.getPortal();
        try {
            Path portalPath = exportPortal(exportOptions, portal);
            ExportInfo exportInfo = new ExportInfo();
            exportInfo.setLocation(portalPath.toString());
            return exportInfo;
        } catch (IOException e) {
            throw new ExportException("Failed to export", e);
        }
    }

    @NotNull
    private Path exportPortal(ExportOptions exportOptions, Portal portal) throws IOException {
        Path portalPath = Files.createDirectories(Path.of(exportOptions.getLocation(), portal.getName()));
        objectMapper.writeValue(new File(portalPath.toFile(), "portal.yaml"), portal);

        for (Product product : portal.getProducts()) {
            exportProduct(portalPath, product);
        }
        return portalPath;
    }

    private void exportProduct(Path parent, Product product) throws IOException {
        Path path = Files.createDirectories(parent.resolve(product.getKey()));
        objectMapper.writeValue(new File(path.toFile(), "product.yaml"), product);

        for (Capability capability : product.getCapabilities()) {
            exportCapability(path, capability);
        }
    }

    private void exportCapability(Path parent, Capability capability) throws IOException {
        Path path = Files.createDirectories(parent.resolve(capability.getKey()));
        objectMapper.writeValue(new File(path.toFile(), "capability.yaml"), capability);

        for (ServiceDefinition serviceDefinition : capability.getServiceDefinitions()) {
            exportService(path, serviceDefinition);
        }
    }

    private void exportService(Path parent, ServiceDefinition serviceDefinition) throws IOException {
        Path path = Files.createDirectories(parent.resolve(serviceDefinition.getKey()));
        objectMapper.writeValue(new File(path.toFile(), "service.yaml"), serviceDefinition);

        for (Spec spec : serviceDefinition.getSpecs()) {
            exportSpec(path, spec);
        }

    }

    private void exportSpec(Path parent, Spec spec) throws IOException {
        Path path = Files.createDirectories(parent.resolve(spec.getKey() + "-" + spec.getVersion()));
        objectMapper.writeValue(new File(path.toFile(), "spec.yaml"), spec);
        Files.write(path.resolve(spec.getFilename()), spec.getOpenApi().getBytes(StandardCharsets.UTF_8));
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

    private abstract static class MixInCapability {

        @JsonIgnore
        abstract Product getProduct();

        @JsonIgnore
        abstract Set<ServiceDefinition> getServiceDefinitions();

    }

    private abstract static class MixInServiceDefinition {

        @JsonIgnore
        abstract Capability getCapability();

        @JsonIgnore
        abstract Set<Spec> getSpecs();

    }

    private abstract static class MixInSpec {

        @JsonIgnore
        abstract Portal getPortal();

        @JsonIgnore
        abstract Product getProduct();

        @JsonIgnore
        abstract Capability getCapability();

        @JsonIgnore
        abstract Source getSource();

        @JsonIgnore
        abstract ServiceDefinition getServiceDefinition();

        @JsonIgnore
        abstract Set<Spec> getSpecs();

        @JsonIgnore
        abstract String getOpenApi();

    }

    private abstract static class MixInProductRelease {

        @JsonIgnore
        abstract Portal getPortal();

        @JsonIgnore
        abstract Set<Spec> getSpecs();
    }

    private abstract static class MixInPortalLintRuleSet {

        @JsonIgnore
        abstract Portal getPortal();
    }


    private abstract static class MixIn {

        @JsonIgnore
        abstract Set<Spec> getSpecs();

    }
}
