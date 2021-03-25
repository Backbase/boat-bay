package com.backbase.oss.boat.bay.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.source.scanner.MavenScannerOptions;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.source.scanner.SourceScannerOptions;
import com.backbase.oss.boat.bay.source.scanner.SpecSourceScanner;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.internal.impl.DefaultRepositorySystem;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.version.Version;
import org.w3c.dom.CDATASection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Slf4j
public class MavenSpecSourceScanner implements SpecSourceScanner {


    private Source source;
    private SourceScannerOptions sourceScannerOptions;


    @Override
    public void setSource(Source source) {
        this.source = source;
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
        RepositorySystem repositorySystem = newRepositorySystem();
        RepositorySystemSession session = newRepositorySystemSession(repositorySystem);


        MavenScannerOptions mavenScannerOptions = sourceScannerOptions.getMavenScannerOptions();
        Artifact artifact = new DefaultArtifact(mavenScannerOptions.getMavenArtifactCoords());

        VersionRangeRequest rangeRequest = new VersionRangeRequest();
        rangeRequest.setArtifact(artifact);
        rangeRequest.setRepositories(newRepositories(repositorySystem, session));

        VersionRangeResult rangeResult = null;
        try {
            rangeResult = repositorySystem.resolveVersionRange(session, rangeRequest);

            List<Version> versions = rangeResult.getVersions();

            System.out.println("Available versions " + versions);

        } catch (VersionRangeResolutionException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public SourceType getSourceType() {
        return null;
    }


    public DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository localRepo = new LocalRepository(this.sourceScannerOptions.getMavenScannerOptions().getLocalRepoPath());
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

//        session.setTransferListener( new ConsoleTransferListener() );
//        session.setRepositoryListener( new ConsoleRepositoryListener() );

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );

        return session;
    }

    public List<RemoteRepository> newRepositories(RepositorySystem system, RepositorySystemSession session) {
        return new ArrayList<>(Collections.singletonList(newRepository()));
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
        /*
         * Aether's components implement org.eclipse.aether.spi.locator.Service to ease manual wiring and using the
         * prepopulated DefaultServiceLocator, we only need to register the repository connector and transporter
         * factories.
         */
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        locator.setErrorHandler(new DefaultServiceLocator.ErrorHandler() {
            @Override
            public void serviceCreationFailed(Class<?> type, Class<?> impl, Throwable exception) {
                log.error("Service creation failed for {} with implementation {}",
                    type, impl, exception);
            }
        });

        return locator.getService(RepositorySystem.class);
    }
}
