package com.backbase.oss.boat.bay.service.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.service.source.scanner.SpecSourceScanner;
import com.backbase.oss.boat.loader.OpenAPILoader;
import com.backbase.oss.boat.loader.OpenAPILoaderException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.ItemHandle;
import org.jfrog.artifactory.client.model.File;
import org.jfrog.artifactory.client.model.RepoPath;


@Slf4j
public class JFrogSpecSourceScanner implements SpecSourceScanner {

    @Getter
    @Setter
    private Source source;

    private Artifactory artifactory;

    public List<Spec> scan() {
        log.info("Scanning Artifactory Source: {} in repo: {}", source.getBaseUrl(), source.getPath());
        return getArtifactory().searches()
            .repositories(source.getPath())
            .artifactsByName(source.getFilter())
            .doSearch()
            .stream()
            .parallel()
            .map(this::getItem)
            .filter(this::isFile)
            .map(this::createSpec)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    private Artifactory getArtifactory() {
        if (artifactory == null) {
            log.info("Setting up artifactory client with url: {} and username: {}", source.getBaseUrl(), source.getUsername());
            artifactory = ArtifactoryClientBuilder.create()
                .setUrl(source.getBaseUrl())
                .setUsername(source.getUsername())
                .setPassword(source.getPassword())
                .build();
        }
        return artifactory;
    }

    private boolean isFile(ItemHandle itemHandle) {
        return !itemHandle.isFolder();
    }

    private ItemHandle getItem(RepoPath repoPath) {
        return getArtifactory().repository(source.getPath())
            .file(repoPath.getItemPath());
    }

    private Optional<Spec> createSpec(ItemHandle item) {
        try {
            String openApiContents = download(item);
            Spec spec = getSpec(item, openApiContents);
            return Optional.of(spec);
        } catch (IOException e) {
            log.error("Failed to download source spec: " + item);
            return Optional.empty();
        }
    }

    private Spec getSpec(ItemHandle item, String openApiContents) {
        String version = null;
        String title = null;
        Boolean valid;
        String parseErrorMessage = null;
        String tags;

        try {
            OpenAPI openAPI = OpenAPILoader.parse(openApiContents);

            Info info = openAPI.getInfo();
            version = info.getVersion();
            title = info.getTitle();
            valid = Boolean.TRUE;
        } catch (OpenAPILoaderException e) {
            parseErrorMessage = e.getMessage();
            if(e.getParseMessages()!=null) {
                parseErrorMessage += "\n" + String.join("\n\t" + e.getParseMessages());
            }

            valid = Boolean.FALSE;
            version = "";
            log.info("Failed to parse OpenAPI for item: {}", item.info().getPath());
        }

        File file = item.info();

        Spec spec = new Spec();
        spec.setKey(source.getPath());
        spec.setPortal(source.getPortal());
        spec.setCapability(source.getCapability());
        spec.setServiceDefinition(source.getServiceDefinition());
        spec.setSource(source);
        spec.setName(file.getName());
        spec.setVersion(version);
        spec.setTitle(title);
        spec.setOpenApi(openApiContents);
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
        spec.setParseError(parseErrorMessage);
        spec.setValid(valid);
        return spec;
    }

    private String download(ItemHandle item) throws IOException {
        InputStream inputStream = getArtifactory().repository(source.getPath()).download(item.info().getPath()).doDownload();

        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, Charset.defaultCharset());

        return writer.toString();
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.JFROG;
    }

}
