package com.backbase.oss.boat.bay.source.scanner.impl;

import com.backbase.oss.boat.bay.config.BoatBayConfigurationProperties;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.io.xpp3.SettingsXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
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
    private RepositorySystem repositorySystem;
    private RepositorySystemSession session;
    private List<RemoteRepository> repositories;
    private BoatBayConfigurationProperties boatBayConfigurationProperties;
    private final Set<SourcePath> paths = new LinkedHashSet<>();

    @Override
    public void setConfigurationProperties(BoatBayConfigurationProperties properties) {
        this.boatBayConfigurationProperties = properties;
    }

    @Override
    public void setSource(Source source) {
        this.source = source;
        paths.addAll(source.getSourcePaths());
    }

    @Override
    public Source getSource() {
        return source;
    }

    @Override
    public ScanResult scan() {
        log.info("Running Maven Source Scanner for source: {}", source.getName());

        ScanResult scanResult = new ScanResult(source);

        SettingsXpp3Reader settingsXpp3Reader = new SettingsXpp3Reader();
        Settings mavenSettings;
        try {
            mavenSettings = settingsXpp3Reader.read(new FileInputStream(boatBayConfigurationProperties.getMavenSettingsFile()));
        } catch (IOException | XmlPullParserException e) {
            throw new IllegalStateException(
                "Cannot read maven settings file from: " + boatBayConfigurationProperties.getMavenSettingsFile()
            );
        }

        repositorySystem = newRepositorySystem();
        session = newRepositorySystemSession(mavenSettings, repositorySystem);

        Artifact artifact = new DefaultArtifact(source.getBillOfMaterialsCoords());

        VersionRangeRequest rangeRequest = new VersionRangeRequest();
        rangeRequest.setArtifact(artifact);
        repositories = readMavenRepositories(mavenSettings, repositorySystem, session);
        rangeRequest.setRepositories(repositories);

        VersionRangeResult rangeResult = null;
        try {
            log.info("Resolving versions for artifact: {} in: {}", source.getBillOfMaterialsCoords(), repositories);
            rangeResult = repositorySystem.resolveVersionRange(session, rangeRequest);

            List<Version> versions = filterOutCandidateRelease(rangeResult.getVersions());
            log.info("Available versions: {}", StringUtils.join(versions, ","));
            Map<String, Spec> resolvedSpecs = new HashMap<>();
            versions.forEach(v -> processVersion(scanResult, artifact, v, resolvedSpecs));
        } catch (VersionRangeResolutionException e) {
            e.printStackTrace();
        }

        return scanResult;
    }

    private List<Version> filterOutCandidateRelease(List<Version> versions) {
        List<Version> result = new ArrayList<>();
        versions.forEach(
            version -> {
                // Filter out rc,cr,b1,snapshot and likewise release except for LTS
                if (!version.toString().matches(".*[a-zA-Z]+.*") || version.toString().endsWith("LTS")) {
                    result.add(version);
                }
            }
        );
        return result;
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
            ExtendedPatternDependencyFilter dependencyFilter = new ExtendedPatternDependencyFilter(
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
            List<ArtifactResult> artifactResults = new ArrayList<>();
            artifactRequests.forEach(
                artifactRequest -> {
                    try {
                        artifactResults.add(repositorySystem.resolveArtifact(session, artifactRequest));
                    } catch (ArtifactResolutionException e) {
                        log.warn("Spec {} not found ", artifactRequest.toString());
                    }
                }
            );
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
        } catch (ArtifactDescriptorException e) {
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

    public DefaultRepositorySystemSession newRepositorySystemSession(Settings settings, RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        String localRepositoryLocation = settings.getLocalRepository() != null
            ? settings.getLocalRepository()
            : System.getenv("HOME") + "/.m2/repository";
        log.info("Setting up maven repository location: {}", localRepositoryLocation);
        LocalRepository localRepository = new LocalRepository(localRepositoryLocation);

        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepository));
        session.setChecksumPolicy(RepositoryPolicy.CHECKSUM_POLICY_IGNORE);

        //        session.setTransferListener( new ConsoleTransferListener() );
        //        session.setRepositoryListener( new ConsoleRepositoryListener() );

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );

        return session;
    }

    public List<RemoteRepository> readMavenRepositories(Settings settings, RepositorySystem system, RepositorySystemSession session) {
        return settings
            .getProfiles()
            .stream()
            .flatMap(profile -> profile.getRepositories().stream())
            .map(
                repository -> {
                    Server server = settings.getServer(repository.getId());
                    AuthenticationBuilder authenticationBuilder = new AuthenticationBuilder();
                    if (server != null) {
                        authenticationBuilder.addPassword(server.getPassword());
                        authenticationBuilder.addUsername(server.getUsername());
                    }
                    return new RemoteRepository.Builder(repository.getName(), "default", repository.getUrl())
                        .setAuthentication(authenticationBuilder.build())
                        .build();
                }
            )
            .collect(Collectors.toList());
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
