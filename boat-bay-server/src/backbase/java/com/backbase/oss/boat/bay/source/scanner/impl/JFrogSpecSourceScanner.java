package com.backbase.oss.boat.bay.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.source.scanner.SourceScannerOptions;
import com.backbase.oss.boat.bay.source.scanner.SpecSourceScanner;
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
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
    private SourceScannerOptions scannerOptions;

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
    public void setScannerOptions(SourceScannerOptions scannerOptions) {
        this.scannerOptions = scannerOptions;
    }

    @Override
    public Source getSource() {
        return source;
    }

    public ScanResult scan() {

        ScanResult scanResult = new ScanResult(source, scannerOptions);

        log.info("Scanning Artifactory Source: {}", source.getName());
        Searches searchQuery = getArtifactory().searches()
            .repositories(repository);
        if (source.getFilterArtifactsName() != null) {
            searchQuery = searchQuery.artifactsByName(source.getFilterArtifactsName());
        }

        List<RepoPath> repoPaths = searchQuery
            .doSearch();

        log.info("Found: {} items from artifactory", repoPaths.size());

        Comparator<ItemHandle> comparing = Comparator.comparing(ih -> ih.info().getLastModified());
        List<ItemHandle> items = repoPaths
            .stream()
            .map(this::getItem)
            .filter(this::isFileAndInSourcePaths)
            .filter(itemHandle -> isCreatedAfter(itemHandle, source))
            .sorted(comparing.reversed())
            .collect(Collectors.toList());

        if (source.getItemLimit() != null && !items.isEmpty()) {
            items = items.subList(0, Math.min(source.getItemLimit(), items.size()-1));
        }
        log.info("Processing  items: {}", items.size());

        items.forEach(itemHandle -> findSpecInZip(itemHandle, scanResult));
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

    private void findSpecInZip(ItemHandle item, ScanResult scanResult) {
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

            } else if (item.info().getName().endsWith(".zip")) {
                log.info("Downloading zip file: {}", item.info().getName());
                ZipInputStream zipInputStream = downloadZipFile(item);
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                Set<Spec> specsInZip = new HashSet<>();

                while (zipEntry != null) {
                    Optional<Spec> spec = findSpecInZip(item, zipInputStream, zipEntry);
                    spec.ifPresent(specsInZip::add);
                    zipEntry = zipInputStream.getNextEntry();
                }
                zipInputStream.close();

                if (!specsInZip.isEmpty()) {
                    String name = SpringExpressionUtils.parseName(source.getProductReleaseNameSpEL(), item, item.info().getName());
                    String key = SpringExpressionUtils.parseName(source.getProductReleaseKeySpEL(), item, item.info().getName());
                    String version = SpringExpressionUtils.parseName(source.getProductReleaseVersionSpEL(), item, item.info().getName());
                    ProductRelease productRelease = new ProductRelease();
                    productRelease.setKey(key);
                    productRelease.setName(name);
                    productRelease.setVersion(version);
                    productRelease.setSpecs(specsInZip);
                    productRelease.setProduct(source.getProduct());
                    productRelease.setReleaseDate(item.info().getLastModified().toInstant().atZone(ZoneId.systemDefault()));
                    log.info("Adding {} to release named: {}", specsInZip.size(), productRelease.getName());
                    scanResult.addProductRelease(productRelease);
                }


            } else {
                log.error("Item: {} not supported", item.info().getPath());
            }
        } catch (IOException e) {
            log.error("Failed to download source spec: " + item);
        }
    }

    private Optional<Spec> findSpecInZip(ItemHandle item, ZipInputStream stream, ZipEntry zipEntry) throws IOException {
        File file = item.info();

        if (!zipEntry.isDirectory() && zipEntry.getName().endsWith(".yaml")) {
            log.info("Creating spec from zip entry: {}", zipEntry.getName());
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
            spec.setCreatedOn(Instant.now().atZone(ZoneId.systemDefault()));
            spec.setOpenApi(openApi);

            spec.setFilename(filename);
            spec.setSourcePath(zipEntry.getName());
            spec.setSourceUrl(file.getDownloadUri());
            spec.setSourceName(filename);
            spec.setSourceCreatedBy(file.getCreatedBy());
            spec.setSourceCreatedOn(file.getCreated().toInstant().atZone(ZoneId.systemDefault()));
            spec.setSourceLastModifiedBy(file.getModifiedBy());
            spec.setSourceLastModifiedOn(file.getLastModified().toInstant().atZone(ZoneId.systemDefault()));
            return Optional.of(spec);
        } else {
            log.debug("Ignoring {}", zipEntry.getName());
        }
        return Optional.empty();

    }

    private String getOpenApiFromZipStream(ZipInputStream stream) throws IOException {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        int len;
        while ((len = stream.read(buffer)) > 0) {
            boas.write(buffer, 0, len);
        }
        boas.close();
        return boas.toString(Charset.defaultCharset());
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
        spec.setCreatedOn(Instant.now().atZone(ZoneId.systemDefault()));
        spec.setChecksum(file.getChecksums().getMd5());
        spec.setFilename(file.getName());
        spec.setSourcePath(file.getPath());
        spec.setSourceUrl(file.getDownloadUri());
        spec.setSourceName(file.getName());
        spec.setSourceCreatedBy(file.getCreatedBy());
        spec.setSourceCreatedOn(file.getCreated().toInstant().atZone(ZoneId.systemDefault()));
        spec.setSourceLastModifiedBy(file.getModifiedBy());
        spec.setSourceLastModifiedOn(file.getLastModified().toInstant().atZone(ZoneId.systemDefault()));
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
