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
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;


import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;


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
    @Autowired
    private final ProductReleaseRepository productReleaseRepository;
    @Autowired
    private final SpecTypeRepository specTypeRepository;
    @Autowired
    private final TagRepository tagRepository;

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
            Path scanPath;

            try {
                    scanPath= Path.of(p.getName());


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
                log.error("Failed to scan path: {}", p.getName(), e);
            }
        }

        return new ScanResult(source, specs);

    }

    private Path scanGitRepo(String path) throws IOException {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        Repository existingRepo = repositoryBuilder.setGitDir(new File("/path/to/repo/.git"))
            .readEnvironment() // scan environment GIT_* variables
            .findGitDir() // scan up the file system tree
            .setMustExist(true)
            .build();
//        Git.open(new File("/path/to/repo/.git"))
//            .checkout();
//        Repository repository = git.getRepository();
        ObjectId lastCommitId = existingRepo.resolve(Constants.HEAD);

        // a RevWalk allows to walk over commits based on some filtering that is defined
        try (RevWalk revWalk = new RevWalk(existingRepo)) {
            RevCommit commit = revWalk.parseCommit(lastCommitId);
            // and using commit's tree find the path
            RevTree tree = commit.getTree();
            System.out.println("Having tree: " + tree);

            // now try to find a specific file
            try (TreeWalk treeWalk = new TreeWalk(existingRepo)) {
                treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                treeWalk.setFilter(PathFilter.create("portal.yaml"));
                if (!treeWalk.next()) {
                    throw new IllegalStateException("Did not find expected file 'portal.yaml'");
                }

                ObjectId objectId = treeWalk.getObjectId(0);
                ObjectLoader loader = existingRepo.open(objectId);

                Portal portal = objectMapper.readValue(loader.openStream(), Portal.class);

                // and then one can the loader to read the file
                //loader.copyTo();
            }

            revWalk.dispose();
        }

        return null;
    }

    private Optional<Portal> mapPortal(Path scanPath, Path path) {

        Portal portal;
        List<ProductRelease> productReleases = new ArrayList<>();

        try {

            File portalFile = new File(path.toString());

            portal = objectMapper.readValue(portalFile, Portal.class);
            productReleases.addAll(portal.getProductReleases());
            portal.setProductReleases(new HashSet<>());
            portalRepository.save(portal);

            for (ProductRelease p : productReleases){

                p.setPortal(portal);
                productReleaseRepository.save(p);
                portal.addProductRelease(p);

            }
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
                .map(specPath -> mapSpec(scanPath, specPath, service))
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
    private Optional<Spec> mapSpec(Path scanPath, Path path, ServiceDefinition serviceDefinition) {

        Spec spec;

        try {

            spec = objectMapper.readValue(new File(path.toString()), Spec.class);


            spec.setId(null);
            specRepository.findOne(Example.of(spec)).ifPresent(s -> spec.setId(s.getId()));

            SpecType specType = spec.getSpecType();
            specType.setId(null);
            specTypeRepository.findOne(Example.of(specType)).ifPresent(s -> specType.setId(s.getId()));
            specTypeRepository.save(specType);
            specType.setId(specTypeRepository.findOne(Example.of(specType)).get().getId());


            spec.setSpecType(specType);
            spec.setServiceDefinition(serviceDefinition);
            spec.setCapability(serviceDefinition.getCapability());
            spec.setProduct(serviceDefinition.getCapability().getProduct());
            spec.setPortal(serviceDefinition.getCapability().getProduct().getPortal());
            String openapi = Files.readString(path.getParent().resolve(spec.getFilename()));
            spec.setOpenApi(openapi);


            List<Tag> tags = new ArrayList<>();
            tags.addAll(spec.getTags());

            for (Tag t : tags){
                spec.removeTag(t);

                t.setId(null);
                tagRepository.findOne(Example.of(t)).ifPresent(tag -> t.setId(tag.getId()));
                tagRepository.save(t);
                t.setId(tagRepository.findOne(Example.of(t)).get().getId());

                spec.addTag(t);

            }


        } catch (IOException e) {
            return Optional.empty();
        }

        log.debug("Scanned spec {}", spec);
        specRepository.save(spec);
        spec.setId(specRepository.findOne(Example.of(spec)).get().getId());


        return Optional.of(spec);
    }

//    private void save(JpaRepository repo, Object entity ){
//
//
//    }


    @Override
    public SourceType getSourceType() {
        return SourceType.FILE_SYSTEM;
    }
    private static class SimpleProgressMonitor implements ProgressMonitor {
        @Override
        public void start(int totalTasks) {
            System.out.println("Starting work on " + totalTasks + " tasks");
        }

        @Override
        public void beginTask(String title, int totalWork) {
            System.out.println("Start " + title + ": " + totalWork);
        }

        @Override
        public void update(int completed) {
            System.out.print(completed + "-");
        }

        @Override
        public void endTask() {
            System.out.println("Done");
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }


}
