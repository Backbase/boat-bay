package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.service.source.scanner.impl.JFrogSpecSourceScanner;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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

    @Test
    void testJFrog() {
        JFrogSpecSourceScanner source = new JFrogSpecSourceScanner();
        source.setSource(new Source()
            .username(System.getenv("ARTIFACTORY_USERNAME"))
            .password(System.getenv("ARTIFACTORY_PASSWORD"))
            .baseUrl(System.getenv("ARTIFACTORY_URL"))
            .path("specs")
            .filter("*.yaml")
            .portal(new Portal().key("artifactory")));

        List<Spec> scan = source.scan();

        specSourceResolver.processSpecs(scan);


    }
}
