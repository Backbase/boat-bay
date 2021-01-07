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
import org.springframework.data.domain.Example;


/**
 * Walks over a directory structure and based on directory names and depths, import specs
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

        List<Spec> specs = new ArrayList<>();

        for (SourcePath p : source.getSourcePaths()) {
            Path scanPath = Path.of(p.getName());

            try {

                List<Portal> portalsScanned = Files.walk(scanPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.endsWith("portal.yaml"))
                    .map(path -> mapPortal(scanPath, path))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
                source.setPortal(portalsScanned.get(0));
                specs.addAll(portalsScanned.stream().flatMap(portal -> portal.getProducts().stream())
                    .flatMap(product -> product.getCapabilities().stream())
                    .flatMap(capability -> capability.getServiceDefinitions().stream())
                    .flatMap(serviceDefinition -> serviceDefinition.getSpecs().stream()).collect(Collectors.toList()));

            } catch (IOException e) {
                log.error("Failed to scan path: {}", scanPath, e);
            }
        }


        return new ScanResult(source, specs);
    }

    private Optional<Portal> mapPortal(Path scanPath, Path path) {

        Portal portal;

        try {

            File portalFile = new File(path.toString());
            portal = objectMapper.readValue(portalFile, Portal.class);

            portalRepository.save(portal);


            portal.setProducts(Files.walk(path.getParent())
                .filter(Files::isRegularFile)
                .filter(productPath -> productPath.endsWith("product.yaml"))
                .map(productPath -> mapProduct(scanPath, productPath, portal))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet()));

        } catch (IOException e) {
            return Optional.empty();
        }

        log.debug("scanned portal {}", portal);
        portalRepository.save(portal);
        return Optional.of(portal);
    }

    private Optional<Product> mapProduct(Path scanPath, Path path, Portal portal) {

        Product product;

        try {
            File productPath = new File(path.toString());
            product = objectMapper.readValue(productPath, Product.class);

            product.setPortal(portal);


            if (!productRepository.findById(product.getId()).equals(product)) {
                product.setId(null);
                productRepository.save(product);
                product.setId(productRepository.findOne(Example.of(product)).get().getId());
            }else {
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


        } catch (IOException e) {
            return Optional.empty();
        }

        log.debug("Scanned product {}", product);
        productRepository.save(product);
        return Optional.of(product);
    }

    private Optional<Capability> mapCapability(Path scanPath, Path path, Product product) {

        Capability capability;


        try {
            File capabilityFile = new File(path.toString());

            capability = objectMapper.readValue(capabilityFile, Capability.class);

            capability.setProduct(product);

            if (!capabilityRepository.existsById(capability.getId())) {
                capability.setId(null);
                capabilityRepository.save(capability);
                capability.setId(capabilityRepository.findOne(Example.of(capability)).get().getId());
            }else {
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

        } catch (IOException e) {
            return Optional.empty();
        }

        log.debug("Scanned capability {}", capability);
        capabilityRepository.save(capability);

        return Optional.of(capability);
    }

    private Optional<ServiceDefinition> mapService(Path scanPath, Path path, Capability capability) {

        ServiceDefinition service;

        try {
            File serviceFile = new File(path.toString());
            service = objectMapper.readValue(serviceFile, ServiceDefinition.class);


            service.setCapability(capability);
            serviceDefinitionRepository.save(service);


            if (!serviceDefinitionRepository.existsById(service.getId())) {
                service.setId(null);
                serviceDefinitionRepository.save(service);
                service.setId(serviceDefinitionRepository.findOne(Example.of(service)).get().getId());
            }else {
                serviceDefinitionRepository.save(service);
            }

            service.setSpecs(Files.walk(path.getParent())
                .filter(Files::isRegularFile)
                .filter(capPath -> capPath.endsWith("spec.yaml"))
                .map(specPath -> mapSpec(scanPath, specPath))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet()));

        } catch (IOException e) {
            return Optional.empty();
        }

        log.debug("Scanned service {}", service);
        serviceDefinitionRepository.save(service);
        return Optional.of(service);
    }

    @NotNull
    private Optional<Spec> mapSpec(Path scanPath, Path path) {

        Spec spec;

        try {

            spec = objectMapper.readValue(new File(path.toString()), Spec.class);

        } catch (IOException e) {
            return Optional.empty();
        }

        log.debug("Scanned spec {}", spec);

        specRepository.save(spec);

        return Optional.of(spec);
    }


    @Override
    public SourceType getSourceType() {
        return SourceType.FILE_SYSTEM;
    }


}
