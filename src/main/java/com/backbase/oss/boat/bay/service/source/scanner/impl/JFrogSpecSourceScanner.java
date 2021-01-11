package com.backbase.oss.boat.bay.service.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.service.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.service.source.scanner.SpecSourceScanner;
import com.backbase.oss.boat.bay.util.SpringExpressionUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import liquibase.pro.packaged.Z;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.ItemHandle;
import org.jfrog.artifactory.client.Searches;
import org.jfrog.artifactory.client.model.File;
import org.jfrog.artifactory.client.model.RepoPath;


@Slf4j
public class JFrogSpecSourceScanner implements SpecSourceScanner {


    private Source source;

    private Artifactory artifactory;
    private String repository;
    private String baseUrl;
    private final Set<SourcePath> paths = new LinkedHashSet<>();

    byte[] buffer = new byte[2048];

    @SuppressWarnings("UseBulkOperation")
    @Override
    public void setSource(Source source) {
        this.source = source;

        URI uri = URI.create(source.getBaseUrl());
        this.baseUrl = uri.getScheme() + "://" + uri.getHost() + (uri.getPort() != -1 ? ":" + uri.getPort() : "");
        this.repository = uri.getPath().substring(1);
        source.getSourcePaths().forEach(paths::add);
    }

    @Override
    public Source getSource() {
        return source;
    }

    public ScanResult scan() {

        ScanResult scanResult = new ScanResult();

        log.info("Scanning Artifactory Source: {}", source.getName());
        Searches searchQuery = getArtifactory().searches()
            .repositories(repository);
        if (source.getFilterArtifactsName() != null) {
            searchQuery = searchQuery.artifactsByName(source.getFilterArtifactsName());
        }

        List<RepoPath> repoPaths = searchQuery
            .doSearch();
        repoPaths
            .stream()
            .parallel()
            .map(this::getItem)
            .filter(this::isFileAndInSourcePaths)
            .filter(itemHandle -> isCreatedAfter(itemHandle, source))
            .forEach(itemHandle -> process(itemHandle, scanResult));
        return scanResult;
    }

    private boolean isCreatedAfter(ItemHandle itemHandle, Source source) {
        if (source.getFilterArtifactsCreatedSince() == null) {
            return true;
        }
        Date lastModified = itemHandle.info().getLastModified();
        LocalDate lastModifiedDate = lastModified.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return lastModifiedDate.isAfter(source.getFilterArtifactsCreatedSince());
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
        if (isFolder) {
            log.debug("Skipping JFROG Item: {} as it's a folder", path);
            return false;
        }
        boolean isInSourcePaths = paths.stream().anyMatch(sourcePath -> path.startsWith(sourcePath.getName()));
        if (isInSourcePaths) {
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

    private void process(ItemHandle item, ScanResult scanResult) {
        try {
            if (item.info().getName().endsWith(".yaml")) {

                Spec spec = getSpec(item);
                if (source.getSpecFilterSpEL() != null && SpringExpressionUtils.match(source.getSpecFilterSpEL(), spec)) {
                    log.info("Spec: {} is ignored because it matches the spec filter expression: {}", item.info().getPath(), source.getSpecFilterSpEL());
                    return;
                }
                log.info("Downloading OpenAPI for spec: {}", spec.getName());
                String openApiContents = downloadOpenApi(item);
                spec.setOpenApi(openApiContents);
                scanResult.getSpecs().add(spec);

            } else if (item.info().getName().endsWith(".zip")) {
                ZipInputStream zipInputStream = downloadZipFile(item);
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while(zipEntry != null) {
                    process(item, zipInputStream, zipEntry, scanResult);
                    zipEntry = zipInputStream.getNextEntry();
                }
            } else {
                log.error("Item: {} not supported", item.info().getPath());
            }
        } catch (IOException e) {
            log.error("Failed to download source spec: " + item);
        }
    }

    private void process(ItemHandle item, ZipInputStream stream, ZipEntry zipEntry, ScanResult scanResult) throws IOException {
        File file = item.info();

        if(!zipEntry.isDirectory() && zipEntry.getName().endsWith(".yaml")) {
            log.info("Trying to parse: {}", zipEntry.getName());
            String openApi = getOpenApiFromZipStream(stream);

            String filename = StringUtils.substringAfter(zipEntry.getName(), "/");

            Spec spec = new Spec();
            spec.setKey(filename);
            spec.setPortal(source.getPortal());
            spec.setCapability(source.getCapability());
            spec.setServiceDefinition(source.getServiceDefinition());
            spec.setSource(source);
            spec.setName(filename);
            spec.setCreatedBy(JFrogSpecSourceScanner.class.getSimpleName());
            spec.setCreatedOn(Instant.now());

            spec.setFilename(filename);
            spec.setSourcePath(zipEntry.getName());
            spec.setSourceUrl(file.getDownloadUri());
            spec.setSourceName(file.getName());
            spec.setSourceCreatedBy(file.getCreatedBy());
            spec.setSourceCreatedOn(file.getCreated().toInstant());
            spec.setSourceLastModifiedBy(file.getModifiedBy());
            spec.setSourceLastModifiedOn(file.getLastModified().toInstant());
            log.info(openApi);

            scanResult.getSpecs().add(spec);
        } else {
            log.info("Ignoring {}", zipEntry.getName());
        }

    }

    private String getOpenApiFromZipStream(ZipInputStream stream) throws IOException {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        int len;
        while((len = stream.read(buffer)) > 0) {
            boas.write(buffer,0, len);
        }
        boas.close();
        ;
        String openApi = boas.toString(Charset.defaultCharset());
        return openApi;
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

    private String downloadOpenApi(ItemHandle item) throws IOException {
        InputStream inputStream = getInputStream(item);

        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, Charset.defaultCharset());

        return writer.toString();
    }

    private InputStream getInputStream(ItemHandle item) throws IOException {
        return getArtifactory().repository(repository).download(item.info().getPath()).doDownload();
    }

    private ZipInputStream downloadZipFile(ItemHandle item) throws IOException {
        InputStream inputStream = getInputStream(item);
        return new ZipInputStream(inputStream);
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.JFROG;
    }

}
