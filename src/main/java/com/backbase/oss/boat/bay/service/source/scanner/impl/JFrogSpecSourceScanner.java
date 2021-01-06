package com.backbase.oss.boat.bay.service.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.service.source.scanner.SpecSourceScanner;
import com.backbase.oss.boat.bay.util.SpringExpressionUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.ItemHandle;
import org.jfrog.artifactory.client.model.File;
import org.jfrog.artifactory.client.model.RepoPath;


@Slf4j
public class JFrogSpecSourceScanner implements SpecSourceScanner {


    private Source source;

    private Artifactory artifactory;
    private String repository;
    private String baseUrl;
    private Set<SourcePath> paths = new LinkedHashSet<>();

    @Override
    public void setSource(Source source) {
        this.source = source;

        URI  uri = URI.create(source.getBaseUrl());
        this.baseUrl = uri.getScheme() + "://" + uri.getHost() + (uri.getPort() != -1 ? ":" + uri.getPort() : "");
        this.repository = uri.getPath().substring(1);
        source.getSourcePaths().forEach(paths::add);
    }

    @Override
    public Source getSource() {
        return source;
    }

    public List<Spec> scan() {
        log.info("Scanning Artifactory Source: {}", source.getName());
        return getArtifactory().searches()
            .repositories(repository)
            .artifactsByName(source.getFilter())
            .doSearch()
            .stream()
            .parallel()
            .map(this::getItem)
            .filter(this::isFileAndInSourcePaths)
            .map(this::createSpec)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    private Artifactory getArtifactory() {
        if (artifactory == null) {
            log.info("Setting up artifactory client with url: {} and username: {}", baseUrl, source.getUsername());
            artifactory = ArtifactoryClientBuilder.create()
                .setUrl(baseUrl)
                .setUsername(source.getUsername())
                .setPassword(source.getPassword())
                .build();
        }
        return artifactory;
    }

    private boolean isFileAndInSourcePaths(ItemHandle itemHandle) {
        boolean isFolder = itemHandle.isFolder();
        String path = itemHandle.info().getPath();
        if(isFolder) {
            log.debug("Skipping JFROG Item: {} as it's a folder", path);
            return false;
        }
        boolean isInSourcePaths = paths.stream().anyMatch(sourcePath -> path.startsWith(sourcePath.getName()));
        if(isInSourcePaths) {
            log.debug("Include item in scan: {}", path);
            return true;
        } else {
            log.debug("Exclude item in scan: {} as it's not in the source paths: [{}] ", path, paths.stream().map(SourcePath::getName).collect(Collectors.joining(",")));
            return false;
        }
    }

    private ItemHandle getItem(RepoPath repoPath) {
        return getArtifactory().repository(repository)
            .file(repoPath.getItemPath());
    }

    private Optional<Spec> createSpec(ItemHandle item) {
        try {
            Spec spec = getSpec(item);
            if (source.getSpecFilterSpEL() != null && SpringExpressionUtils.match(source.getSpecFilterSpEL(), spec)) {
                log.info("Spec: {} is ignored because it matches the spec filter expression: {}", item.info().getPath(), source.getSpecFilterSpEL());
                return Optional.empty();
            }
            log.info("Downloading OpenAPI for spec: {}", spec.getName());
            String openApiContents = download(item);
            spec.setOpenApi(openApiContents);
            return Optional.of(spec);
        } catch (IOException e) {
            log.error("Failed to download source spec: " + item);
            return Optional.empty();
        }
    }

    private Spec getSpec(ItemHandle item) {
        File file = item.info();
        Spec spec = new Spec();
        spec.setKey(item.info().getName());
        spec.setPortal(source.getPortal());
        spec.setCapability(source.getCapability());
        spec.setServiceDefinition(source.getServiceDefinition());
        spec.setSource(source);
        spec.setName(file.getName());
        spec.setCreatedBy(JFrogSpecSourceScanner.class.getSimpleName());
        spec.setCreatedOn(Instant.now());
        spec.setChecksum(file.getChecksums().getMd5());
        spec.setFilename(file.getName());
        spec.setSourcePath(file.getPath());
        spec.setSourceUrl(file.getDownloadUri());
        spec.setSourceName(file.getName());
        spec.setSourceCreatedBy(file.getCreatedBy());
        spec.setSourceCreatedOn(file.getCreated().toInstant());
        spec.setSourceLastModifiedBy(file.getModifiedBy());
        spec.setSourceLastModifiedOn(file.getLastModified().toInstant());
        return spec;
    }

    private String download(ItemHandle item) throws IOException {
        InputStream inputStream = getArtifactory().repository(repository).download(item.info().getPath()).doDownload();

        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, Charset.defaultCharset());

        return writer.toString();
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.JFROG;
    }

}
