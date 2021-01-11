package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.repository.PortalRepository;
import com.backbase.oss.boat.bay.repository.ProductRepository;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import com.backbase.oss.boat.bay.service.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.service.source.scanner.impl.JFrogSpecSourceScanner;
import java.time.LocalDate;
import java.util.Collections;
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

    @Autowired
    ProductRepository productRepository;


    Source source;

    @BeforeEach
    void setup() {
        Portal artifactory = new Portal()
            .key("artifactory")
            .name("Artifactory");


        portalRepository.save(artifactory);

        Product product =new Product();
        product.setPortal(artifactory);
        product.setKey("test");
        product.setName("test");
        productRepository.save(product);

        Source source = new Source()
            .name("Artifactory")
            .type(SourceType.JFROG)
            .username(System.getenv("ARTIFACTORY_USERNAME"))
            .password(System.getenv("ARTIFACTORY_PASSWORD"))
            .baseUrl(System.getenv("ARTIFACTORY_URL"))
            .product(product)
            .filterArtifactsName("banking-services-bom-*-api.zip")
            .filterArtifactsCreatedSince(LocalDate.parse("2021-01-01"))
            .productReleaseSpEL("info.name.replaceAll('banking-services-bom-', '').replaceAll('-api.zip', '')")
            .portal(artifactory);
        source.getSourcePaths().add(new SourcePath().name("/com/backbase/dbs/banking-services-bom"));
        this.source = sourceRepository.save(source);
    }
    @Test
    void testJFrog() {
        JFrogSpecSourceScanner scanner = new JFrogSpecSourceScanner();

        scanner.setSource(source);

        ScanResult scan = scanner.scan();
        System.out.println(scan);


    }
}
