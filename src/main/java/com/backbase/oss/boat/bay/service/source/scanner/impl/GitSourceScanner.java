package com.backbase.oss.boat.bay.service.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.repository.*;
import com.backbase.oss.boat.bay.service.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.service.source.scanner.SpecSourceScanner;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class GitSourceScanner implements SpecSourceScanner{
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
try {


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
} catch (CorruptObjectException e) {
    e.printStackTrace();
} catch (JsonParseException e) {
    e.printStackTrace();
} catch (AmbiguousObjectException e) {
    e.printStackTrace();
} catch (IncorrectObjectTypeException e) {
    e.printStackTrace();
} catch (JsonMappingException e) {
    e.printStackTrace();
} catch (MissingObjectException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}

        return null;
    }
    @Override
    public void setSource(Source source) {

    }

    @Override
    public Source getSource() {
        return null;
    }



    @Override
    public SourceType getSourceType() {
        return null;
    }
}
