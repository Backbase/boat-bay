package com.backbase.oss.boat.bay.web.views.dashboard.controller;

import com.backbase.oss.boat.bay.api.RegisterApi;
import com.backbase.oss.boat.bay.config.BoatBayConfigurationProperties;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.model.RegisterProject;
import com.backbase.oss.boat.bay.model.RegisteredProject;
import com.backbase.oss.boat.bay.repository.BoatPortalRepository;
import com.backbase.oss.boat.bay.repository.BoatProductRepository;
import com.backbase.oss.boat.bay.repository.BoatSourceRepository;
import com.backbase.oss.boat.bay.source.SpecSourceResolver;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.source.scanner.impl.MavenSpecSourceScanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;


@RestController
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RegisterController implements RegisterApi {


    public static final String MAVEN = "_maven";
    private final BoatPortalRepository boatPortalRepository;
    private final BoatSourceRepository boatSourceRepository;
    private final SpecSourceResolver specSourceResolver;
    final BoatBayConfigurationProperties boatBayConfigurationProperties;

    final BoatPortalRepository portalRepository;
    final BoatProductRepository productRepository;


    @Override
    public ResponseEntity<RegisteredProject> registerProject(String portalKey, RegisterProject registerProject, HttpServletRequest httpServletRequest) {
        Portal portal = portalRepository.findByKey(portalKey).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Product product = productRepository.findByKeyAndPortal(registerProject.getKey(), portal)
            .orElseGet(() -> productRepository.save(new Product()
                .portal(portal)
                .key(registerProject.getKey())
                .name(registerProject.getName())));

        Source mavenSource = boatSourceRepository.findByKeyAndPortal(registerProject.getKey() + MAVEN, portal)
            .orElseGet(() -> setupNewMavenSource(registerProject, portal, product));

        Source uploadSource = boatSourceRepository.findByKeyAndPortal(registerProject.getKey(), portal)
                .orElseGet(() -> setupNewUploadSource(registerProject, portal, product));


        MavenSpecSourceScanner scanner = new MavenSpecSourceScanner();
        scanner.setSource(mavenSource);
        scanner.setConfigurationProperties(boatBayConfigurationProperties);
        ScanResult scan = scanner.scan();

        specSourceResolver.process(scan);

        URI url = URI.create(httpServletRequest.getRequestURL().toString());
        URI resolve = url.resolve("/api/boat/portals/" + portalKey + "/boat-maven-plugin/" + uploadSource.getKey() + "/upload");
        RegisteredProject registeredProject = new RegisteredProject();
        registeredProject.setDashboardUrl(boatBayConfigurationProperties.getBootstrap().getDashboard().getBaseUrl() + "/lint-reports/" + portalKey + "/" + registerProject.getKey() );
        registeredProject.setUploadUrl(resolve.toString());
        registeredProject.setNumberOfApis(Long.valueOf(scan.getProductReleases().stream().flatMap(p -> p.getSpecs().stream()).count()).intValue());

        return ResponseEntity.ok(registeredProject);
    }

    private Source setupNewUploadSource(RegisterProject registerProject, Portal portal, Product product) {
        Source uploadSource = new Source();
        uploadSource.setKey(registerProject.getKey());
        uploadSource.setName(registerProject.getName());
        uploadSource.setType(SourceType.BOAT_MAVEN_PLUGIN);
        uploadSource.setProduct(product);
        uploadSource.setPortal(portal);
        return boatSourceRepository.save(uploadSource);
    }

    @NotNull
    private Source setupNewMavenSource(RegisterProject registerProject, Portal portal, Product product) {
        Source mavenSource = new Source();
        mavenSource.setKey(registerProject.getKey() + MAVEN);
        mavenSource.setName(registerProject.getName());
        mavenSource.setType(SourceType.MAVEN);
        mavenSource.getSourcePaths().add(new SourcePath().name("*:*:*:api:*").source(mavenSource));
        mavenSource.setBillOfMaterialsCoords(registerProject.getBom().getGroupId() + ":" + registerProject.getBom().getArtifactId() + ":pom:" + getVersion(registerProject) + "");
        mavenSource.setCapabilityKeySpEL("mvnGroupId.subString(mvnGroupId.lastIndexOf('.')");
        mavenSource.setCapabilityNameSpEL("mvnGroupId.subString(mvnGroupId.lastIndexOf('.')");
        mavenSource.setCapabilityKeySpEL("mvnArtifactId");
        mavenSource.setCapabilityNameSpEL("mvnArtifactId.replaceAll('-', ' ')");
        mavenSource.setProduct(product);
        mavenSource.setPortal(portal);
        mavenSource.setActive(true);
        mavenSource.setCronExpression("0 */2 * * *");
        mavenSource.setRunOnStartup(true);



        return boatSourceRepository.save(mavenSource);
    }

    private String getVersion(RegisterProject registerProject) {
        return registerProject.getBom().getVersion();
    }


}
