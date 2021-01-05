package com.backbase.oss.boat.bay.service.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.*;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.service.export.impl.FileSystemExporter;
import com.backbase.oss.boat.bay.service.source.scanner.SpecSourceScanner;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.parameters.P;

/**
 * Walks over a directory structure and based on directory names and depths, import specs
 *
 */
@Slf4j
public class FileSystemSourceScanner implements SpecSourceScanner {


    @Getter
    @Setter
    private Source source;

    private final ObjectMapper objectMapper;


    public FileSystemSourceScanner() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        Jdk8Module jdk8Module = new Jdk8Module();
        objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.registerModule(javaTimeModule);
        objectMapper.registerModule(jdk8Module);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    public List<Spec> scan() {

        Path scanPath = Path.of(source.getPath());

        try {
            Files.walk(scanPath)
                .filter(Files::isRegularFile)
                .filter(path -> path.endsWith("portal.yaml"))
                .map(path -> mapPortal(scanPath, path))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @NotNull
    private Optional<Spec> mapSpec(Path scanPath, Path path) {
        Path relativize = scanPath.relativize(path);

        Spec spec = new Spec();
        String openApi ;
        try {
           // openApi = Files.readString(path);
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            if(path.getFileName().toString().equals("spec.yaml")) {
                spec = objectMapper.readValue(new File(path.toString()), Spec.class);
            }
        } catch (IOException e) {
            return Optional.empty();
        }
        log.debug("Scanned spec {}", spec);
        return Optional.of(spec);
    }

    private Optional<ServiceDefinition> mapService(Path scanPath, Path path){
        File serviceFile = new File(path.toString());

        ServiceDefinition service= new ServiceDefinition();

        try{
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            if (serviceFile.exists()) {
                service = objectMapper.readValue(serviceFile, ServiceDefinition.class);
                service.setSpecs(Files.walk(path.getParent())
                    .filter(Files::isRegularFile)
                    .filter(capPath -> capPath.endsWith("spec.yaml"))
                    .map(specPath -> mapSpec(scanPath, specPath))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet()));
            }
        }catch (IOException e){
            return Optional.empty();
        }
        log.debug("Scanned service {}", service);
        return Optional.of(service);
    }

    private Optional<Capability> mapCapability(Path scanPath, Path path){
        Capability capability = new Capability();

        File capabilityFile = new File(path.toString());

        try {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            if (capabilityFile.exists()) {
                capability = objectMapper.readValue(capabilityFile, Capability.class);
                capability.setServiceDefinitions(
                    Files.walk(path.getParent())
                    .filter(Files::isRegularFile)
                    .filter(capPath -> capPath.endsWith("service.yaml"))
                    .map(servicePath -> mapService(scanPath, servicePath))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet()));

            }
        }catch (IOException e){
            return Optional.empty();
        }
        log.debug("Scanned capability {}", capability);
        return Optional.of(capability);
    }

    private Optional<Product> mapProduct(Path scanPath, Path path ){
        Product product = new Product();


        try {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            if (path.getFileName().toString().equals("product.yaml")) {
                product = objectMapper.readValue(new File(path.toString()), Product.class);
                product.setCapabilities(
                    Files.walk(path.getParent())
                    .filter(Files::isRegularFile)
                    .filter(capPath -> capPath.endsWith("capability.yaml"))
                    .map(capPath -> mapCapability(scanPath, capPath))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet()));
            }
        }catch (IOException e){
            return Optional.empty();
        }
        log.debug("Scanned product {}", product);
        return Optional.of(product);
    }

    private Optional<Portal> mapPortal(Path scanPath, Path path){
        Portal portal;
        try {
             ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
             File portalFile = new File(path.toString());
             portal = objectMapper.readValue(portalFile,Portal.class);
             portal.setProducts(Files.walk(path.getParent())
                 .filter(Files::isRegularFile)
                 .filter(productPath -> productPath.endsWith("product.yaml"))
                 .map(productPath -> mapProduct(scanPath, productPath))
                 .filter(Optional::isPresent)
                 .map(Optional::get)
                 .collect(Collectors.toSet()));
         }catch (IOException e){
            return Optional.empty();
        }

        log.debug("scanned portal {}",portal);
        return Optional.of(portal);
    }




    @Override
    public SourceType getSourceType() {
        return SourceType.FILE_SYSTEM;
    }


}
