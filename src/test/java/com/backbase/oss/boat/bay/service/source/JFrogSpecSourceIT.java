package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.repository.PortalRepository;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import com.backbase.oss.boat.bay.service.source.scanner.impl.JFrogSpecSourceScanner;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest(classes = BoatBayApp.class)
@Transactional
class JFrogSpecSourceIT {

    @Autowired
    SpecSourceResolver specSourceResolver;

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    PortalRepository portalRepository;

    Source source;

    @BeforeEach
    void setup() {
        Portal artifactory = new Portal()
            .key("artifactory")
            .name("Artifactory");
        portalRepository.save(artifactory);

        Source portal = new Source()
            .name("Artifactory")
            .type(SourceType.JFROG)
            .username(System.getenv("ARTIFACTORY_USERNAME"))
            .password(System.getenv("ARTIFACTORY_PASSWORD"))
            .baseUrl(System.getenv("ARTIFACTORY_URL"))
            .path("specs")
            .filter("*.yaml")
            .portal(artifactory);
        source = sourceRepository.save(portal);
    }
    @Test
    void testJFrog() {
        JFrogSpecSourceScanner scanner = new JFrogSpecSourceScanner();

        scanner.setSource(source);

        List<Spec> scan = scanner.scan();

        specSourceResolver.processSpecs(scan);


    }
}
