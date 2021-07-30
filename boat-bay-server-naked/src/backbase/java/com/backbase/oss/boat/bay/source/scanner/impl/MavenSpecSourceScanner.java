package com.backbase.oss.boat.bay.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.source.scanner.MavenScannerOptions;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.source.scanner.SourceScannerOptions;
import com.backbase.oss.boat.bay.source.scanner.SpecSourceScanner;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.DefaultDependencyNode;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.*;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.version.Version;

@Slf4j
public class MavenSpecSourceScanner implements SpecSourceScanner {

    private Source source;
    private SourceScannerOptions sourceScannerOptions;
    private RepositorySystem repositorySystem;
    private RepositorySystemSession session;
    private List<RemoteRepository> repositories;
    private final Set<SourcePath> paths = new LinkedHashSet<>();

    @Override
    public void setSource(Source source) {
        this.source = source;

        paths.addAll(source.getSourcePaths());
    }

    @Override
    public void setScannerOptions(SourceScannerOptions sourceScannerOptions) {
        this.sourceScannerOptions = sourceScannerOptions;
    }

    @Override
    public Source getSource() {
        return source;
    }

    @Override
    public ScanResult scan() {
        log.info("Running Maven Source Scanner for source: {}", source.getName());

        ScanResult scanResult = new ScanResult(source, sourceScannerOptions);

        repositorySystem = newRepositorySystem();
        session = newRepositorySystemSession(repositorySystem);

        MavenScannerOptions mavenScannerOptions = sourceScannerOptions.getMavenScannerOptions();
        Artifact artifact = new DefaultArtifact(mavenScannerOptions.getMavenArtifactCoords());

        VersionRangeRequest rangeRequest = new VersionRangeRequest();
        rangeRequest.setArtifact(artifact);
        repositories = newRepositories(repositorySystem, session);
        rangeRequest.setRepositories(repositories);

        VersionRangeResult rangeResult = null;
        try {
            log.info("Resolving versions for artifact: {} in: {}", mavenScannerOptions.getMavenArtifactCoords(), repositories);
            rangeResult = repositorySystem.resolveVersionRange(session, rangeRequest);

            List<Version> versions = rangeResult.getVersions();
            log.info("Available versions: {}", StringUtils.join(versions, ","));
            Map<String, Spec> resolvedSpecs = new HashMap<>();
            versions.forEach(v -> processVersion(scanResult, artifact, v, resolvedSpecs));
        } catch (VersionRangeResolutionException e) {
            e.printStackTrace();
        }

        return scanResult;
    }

    private void processVersion(ScanResult scanResult, Artifact artifact, Version version, Map<String, Spec> resolvedSpecs) {
        if (artifact.getExtension().equals("pom")) {
            processBOM(scanResult, artifact, version, resolvedSpecs);
        } else {
            throw new NotImplementedException("Not yet implemented");
        }
    }

    private void processBOM(ScanResult scanResult, Artifact artifact, Version version, Map<String, Spec> resolvedSpecs) {
        try {
            Artifact searchArtifact = artifact.setVersion(version.toString());

            log.info("Processing BOM Maven Artifact: {} for available spec dependencies", searchArtifact);
            ArtifactDescriptorRequest descriptorRequest = new ArtifactDescriptorRequest();
            descriptorRequest.setArtifact(searchArtifact);
            descriptorRequest.setRepositories(repositories);

            ArtifactDescriptorResult descriptorResult = repositorySystem.readArtifactDescriptor(session, descriptorRequest);

            DependencyFilter dependencyFilter = new ExtendedPatternDependencyFilter(
                paths.stream().map(SourcePath::getName).collect(Collectors.toList())
            );

            List<DependencyNode> parents = Collections.emptyList();
            List<ArtifactRequest> artifactRequests = Stream
                .concat(descriptorResult.getDependencies().stream(), descriptorResult.getManagedDependencies().stream())
                .map(DefaultDependencyNode::new)
                .filter(dependency -> dependencyFilter.accept(dependency, parents))
                .map(this::createArtifactDownloadRequest)
                .collect(Collectors.toList());

            log.info("Found {} specs in BOM: {} ", artifactRequests.size(), searchArtifact.getVersion());

            List<ArtifactResult> artifactResults = repositorySystem.resolveArtifacts(session, artifactRequests);
            Set<Spec> specsInBom = new HashSet<>();
            artifactResults.forEach(
                artifactResult -> {
                    File file = artifactResult.getArtifact().getFile();
                    if (file.exists()) {
                        if (file.getName().endsWith(".zip") || file.getName().endsWith(".jar")) {
                            try {
                                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
                                ZipEntry zipEntry = zipInputStream.getNextEntry();

                                while (zipEntry != null) {
                                    Optional<Spec> spec;
                                    String resolvedSpecKey = artifactResult.getArtifact().toString() + "/" + zipEntry.getName();
                                    if (resolvedSpecs.containsKey(resolvedSpecKey)) {
                                        spec = Optional.of(resolvedSpecs.get(resolvedSpecKey));
                                        log.info(
                                            "Spec already resolved: {}. Adding to release: {}",
                                            spec.get().getFilename(),
                                            searchArtifact.getVersion()
                                        );
                                    } else {
                                        spec = findSpecInZip(artifactResult, zipInputStream, zipEntry);
                                        if (spec.isPresent()) {
                                            resolvedSpecs.put(resolvedSpecKey, spec.get());
                                            log.info(
                                                "Spec: {} resolved from {}. Adding to release: {}",
                                                spec.get().getFilename(),
                                                artifactResult.getArtifact(),
                                                searchArtifact.getVersion()
                                            );
                                        }
                                    }
                                    spec.ifPresent(specsInBom::add);

                                    zipEntry = zipInputStream.getNextEntry();
                                }
                                zipInputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            );

            if (!specsInBom.isEmpty()) {
                ProductRelease productRelease = new ProductRelease();
                productRelease.setKey(version.toString());
                productRelease.setName(version.toString());
                productRelease.setVersion(version.toString());
                productRelease.setSpecs(specsInBom);
                productRelease.setProduct(source.getProduct());
                productRelease.setReleaseDate(ZonedDateTime.now());
                log.info("Adding {} to release named: {}", specsInBom.size(), productRelease.getName());
                scanResult.addProductRelease(productRelease);
            }
        } catch (ArtifactDescriptorException | ArtifactResolutionException e) {
            e.printStackTrace();
        }
    }

    private Optional<Spec> findSpecInZip(ArtifactResult artifactResult, ZipInputStream stream, ZipEntry zipEntry) throws IOException {
        Artifact artifact = artifactResult.getArtifact();

        if (!zipEntry.isDirectory() && zipEntry.getName().endsWith(".yaml")) {
            String openApi = getOpenApiFromZipStream(stream);
            if (openApi.startsWith("openapi:")) {
                log.info("Creating spec from zip entry: {} in artifact: {}", zipEntry.getName(), artifactResult.getArtifact());

                String filename = zipEntry.getName().contains("/")
                    ? StringUtils.substringAfter(zipEntry.getName(), "/")
                    : zipEntry.getName();

                Spec spec = new Spec();
                spec.setKey(filename);
                spec.setPortal(source.getPortal());
                spec.setCapability(source.getCapability());
                spec.setServiceDefinition(source.getServiceDefinition());
                spec.setSource(source);
                spec.setName(filename);
                spec.setCreatedBy(MavenSpecSourceScanner.class.getSimpleName());
                spec.setCreatedOn(Instant.now().atZone(ZoneId.systemDefault()));
                spec.setOpenApi(openApi);

                spec.setFilename(filename);
                spec.setSourcePath(zipEntry.getName());
                spec.setSourceUrl(
                    artifact.getGroupId() +
                    ":" +
                    artifact.getArtifactId() +
                    ":" +
                    artifact.getClassifier() +
                    ":" +
                    artifact.getExtension() +
                    ":" +
                    artifact.getVersion()
                );
                spec.setSourceName(filename);
                spec.setMvnGroupId(artifact.getGroupId());
                spec.setMvnArtifactId(artifact.getArtifactId());
                spec.setMvnExtension(artifact.getExtension());
                spec.setMvnVersion(artifact.getVersion());
                spec.setMvnClassifier(artifact.getClassifier());
                //            spec.setSourceCreatedBy(file.getCreatedBy());
                //            spec.setSourceCreatedOn(file.getCreated().toInstant().atZone(ZoneId.systemDefault()));
                //            spec.setSourceLastModifiedBy(file.getModifiedBy());
                //            spec.setSourceLastModifiedOn(file.getLastModified().toInstant().atZone(ZoneId.systemDefault()));
                return Optional.of(spec);
            } else {
                log.info("File: {} is not an openapi spec: ", zipEntry.getName());
            }
        } else {
            log.debug("Ignoring {}", zipEntry.getName());
        }
        return Optional.empty();
    }

    byte[] buffer = new byte[2048];

    private String getOpenApiFromZipStream(ZipInputStream stream) throws IOException {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        int len;
        while ((len = stream.read(buffer)) > 0) {
            boas.write(buffer, 0, len);
        }
        boas.close();
        return boas.toString(Charset.defaultCharset());
    }

    private ArtifactRequest createArtifactDownloadRequest(DefaultDependencyNode dependency) {
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(dependency.getArtifact());
        artifactRequest.setRepositories(repositories);
        artifactRequest.setDependencyNode(dependency);
        return artifactRequest;
    }

    @Override
    public SourceType getSourceType() {
        return null;
    }

    public DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository localRepo = new LocalRepository(this.sourceScannerOptions.getMavenScannerOptions().getLocalRepoPath());
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
        session.setChecksumPolicy(RepositoryPolicy.CHECKSUM_POLICY_IGNORE);

        //        session.setTransferListener( new ConsoleTransferListener() );
        //        session.setRepositoryListener( new ConsoleRepositoryListener() );

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );

        return session;
    }

    public List<RemoteRepository> newRepositories(RepositorySystem system, RepositorySystemSession session) {
        return Collections.singletonList(newRepository());
    }

    private RemoteRepository newRepository() {
        AuthenticationBuilder authenticationBuilder = new AuthenticationBuilder();
        authenticationBuilder.addUsername(source.getUsername());
        authenticationBuilder.addPassword(source.getPassword());

        return new RemoteRepository.Builder(source.getName(), "default", source.getBaseUrl())
            .setAuthentication(authenticationBuilder.build())
            .build();
    }

    public RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        locator.setErrorHandler(
            new DefaultServiceLocator.ErrorHandler() {
                @Override
                public void serviceCreationFailed(Class<?> type, Class<?> impl, Throwable exception) {
                    log.error("Service creation failed for {} with implementation {}", type, impl, exception);
                }
            }
        );

        return locator.getService(RepositorySystem.class);
    }
}
