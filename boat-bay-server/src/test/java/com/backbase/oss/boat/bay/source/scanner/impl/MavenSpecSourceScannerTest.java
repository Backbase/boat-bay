package com.backbase.oss.boat.bay.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.source.scanner.MavenScannerOptions;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.source.scanner.SourceScannerOptions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class MavenSpecSourceScannerTest {

    @Test
    public void test() {
        MavenSpecSourceScanner mavenSpecSourceScanner = new MavenSpecSourceScanner();

        Source source = new Source();

        HashSet<SourcePath> sourcePaths = new HashSet<>();
        sourcePaths.add(new SourcePath().name("com.backbase.*:*:*:api:*"));
        sourcePaths.add(new SourcePath().name("com.backbase.*:*:yml:*:*"));
        sourcePaths.add(new SourcePath().name("com.backbase.*:*-spec:*:*:*"));

        source.setSourcePaths(sourcePaths);


        SourceScannerOptions sourceScannerOptions = new SourceScannerOptions();
        MavenScannerOptions mavenScannerOptions = new MavenScannerOptions();
        mavenScannerOptions.setLocalRepoPath("target/local-repo");
        mavenScannerOptions.setMavenArtifactCoords("com.backbase:backbase-bom:pom:[0,)");

        sourceScannerOptions.setMavenScannerOptions(mavenScannerOptions);
//        source.setName("repo");
//        source.setBaseUrl("https://artifacts.backbase.com/repo/");
//        source.setUsername("bartv");
//        source.setPassword("AP7a4xXwETivZxFe5U84DBZx9uBhJiaFfTSKbi");
        mavenSpecSourceScanner.setSource(source);
        mavenSpecSourceScanner.setScannerOptions(sourceScannerOptions);

        ScanResult scan = mavenSpecSourceScanner.scan();

        System.out.println(scan);



    }

}
