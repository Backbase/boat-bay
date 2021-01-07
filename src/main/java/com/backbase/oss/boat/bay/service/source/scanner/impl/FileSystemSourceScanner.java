package com.backbase.oss.boat.bay.service.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.*;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.repository.*;
import com.backbase.oss.boat.bay.service.source.scanner.SpecSourceScanner;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.backbase.oss.boat.bay.service.source.scanner.ScanResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;



/**
 * Walks over a directory structure and based on directory names and depths, import specs
 *
 */
@Slf4j
@RequiredArgsConstructor
public class FileSystemSourceScanner implements SpecSourceScanner {
    @Autowired
    private final PortalRepository portalRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final CapabilityRepository capabilityRepository;
    @Autowired
    private final ServiceDefinitionRepository serviceDefinitionRepository;
    @Autowired
    private final SpecRepository specRepository;
    @Getter
    @Setter
    private Source source;

    private final ObjectMapper objectMapper;


    public ScanResult scan() {
        //objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

       // Path scanPath = Path.of(source.getPath());
        List<Spec> specs = new ArrayList<>();
        for (SourcePath p : source.getSourcePaths()) {
            Path scanPath = Path.of(p.getName());
            try {
            List<Portal> portalsScanned =  Files.walk(scanPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.endsWith("portal.yaml"))
                    .map(path -> mapPortal(scanPath, path))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
                specs.addAll( portalsScanned.stream().flatMap(portal -> portal.getProducts().stream())
                    .flatMap(product -> product.getCapabilities().stream())
                    .flatMap(capability -> capability.getServiceDefinitions().stream())
                    .flatMap(serviceDefinition -> serviceDefinition.getSpecs().stream()).collect(Collectors.toList()));

            } catch (IOException e) {
                log.error("Failed to scan path: {}", scanPath, e);
            }
        }

        return new ScanResult(source, specs);
    }

    @NotNull
    private Optional<Spec> mapSpec(Path scanPath, Path path) {

        Spec spec = new Spec();
        String openApi ;
        try {
           // openApi = Files.readString(path);
            if(path.getFileName().toString().equals("spec.yaml")) {
                spec = objectMapper.readValue(new File(path.toString()), Spec.class);
            }
        } catch (IOException e) {
            return Optional.empty();
        }
        log.debug("Scanned spec {}", spec);
        if (!specRepository.existsById(spec.getId()))
            specRepository.save(spec);
        return Optional.of(spec);
    }

    private Optional<ServiceDefinition> mapService(Path scanPath, Path path, Capability capability){
        File serviceFile = new File(path.toString());

        ServiceDefinition service= new ServiceDefinition();

        try{
            if (serviceFile.exists()) {
                service = objectMapper.readValue(serviceFile, ServiceDefinition.class);
                if (!serviceDefinitionRepository.existsById(service.getId())) {
                    service.setCapability(capability);
                    serviceDefinitionRepository.save(service);
                }
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
            serviceDefinitionRepository.save(service);
        return Optional.of(service);
    }

    private Optional<Capability> mapCapability(Path scanPath, Path path, Product product){
        Capability capability;

        File capabilityFile = new File(path.toString());

        try {
            if (capabilityFile.exists()) {
                capability = objectMapper.readValue(capabilityFile, Capability.class);
                if (!capabilityRepository.existsById(capability.getId()) && productRepository.existsById(product.getId())) {
                    capability.setProduct(product);
                    capabilityRepository.save(capability);
                }
                capability.setServiceDefinitions(
                    Files.walk(path.getParent())
                    .filter(Files::isRegularFile)
                    .filter(capPath -> capPath.endsWith("service.yaml"))
                    .map(servicePath -> mapService(scanPath, servicePath, capability))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet()));

                log.debug("Scanned capability {}", capability);
                capabilityRepository.save(capability);

                return Optional.of(capability);
            }
        }catch (IOException e){
            return Optional.empty();
        }
        return Optional.empty();
    }

    private Optional<Product> mapProduct(Path scanPath, Path path , Portal portal){
        Product product;
        try {
            if (path.getFileName().toString().equals("product.yaml")) {
                File productPath = new File(path.toString());
                product = objectMapper.readValue(productPath,Product.class);

                if(!productRepository.existsById(product.getId())){
                    product.setPortal(portal);
                    productRepository.save(product);
                }

                product.setCapabilities(
                    Files.walk(path.getParent())
                    .filter(Files::isRegularFile)
                    .filter(capPath -> capPath.endsWith("capability.yaml"))
                    .map(capPath -> mapCapability(scanPath, capPath, product))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet()));

                log.debug("Scanned product {}", product);
                productRepository.save(product);
                return Optional.of(product);
            }

        }catch (IOException e){
            return Optional.empty();
        }

        return Optional.empty();
    }

    private Optional<Portal> mapPortal(Path scanPath, Path path){
        Portal portal;
        try {
             File portalFile = new File(path.toString());
             portal = objectMapper.readValue(portalFile,Portal.class);

            if(!portalRepository.existsById(portal.getId())) {
                portalRepository.save(portal);
            }

             portal.setProducts(Files.walk(path.getParent())
                 .filter(Files::isRegularFile)
                 .filter(productPath -> productPath.endsWith("product.yaml"))
                 .map(productPath -> mapProduct(scanPath, productPath, portal))
                 .filter(Optional::isPresent)
                 .map(Optional::get)
                 .collect(Collectors.toSet()));
         }catch (IOException e){
            return Optional.empty();
        }

        log.debug("scanned portal {}",portal);
        portalRepository.save(portal);
        return Optional.of(portal);
    }




    @Override
    public SourceType getSourceType() {
        return SourceType.FILE_SYSTEM;
    }


}
