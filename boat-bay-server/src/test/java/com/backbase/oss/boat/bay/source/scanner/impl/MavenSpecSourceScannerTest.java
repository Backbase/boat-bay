package com.backbase.oss.boat.bay.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.source.scanner.MavenScannerOptions;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.source.scanner.SourceScannerOptions;
import org.junit.jupiter.api.Test;

class MavenSpecSourceScannerTest {

    @Test
    public void test() {
        MavenSpecSourceScanner mavenSpecSourceScanner = new MavenSpecSourceScanner();

        Source source = new Source();


        SourceScannerOptions sourceScannerOptions = new SourceScannerOptions();
        MavenScannerOptions mavenScannerOptions = new MavenScannerOptions();
        mavenScannerOptions.setLocalRepoPath("target/local-repo");
        mavenScannerOptions.setMavenArtifactCoords("com.backbase:backbase-bom:pom:[0,)");
        mavenScannerOptions.setIncludeGroupIds("com.backbase");
        mavenScannerOptions.setIncludeClassifiers("api");
        mavenScannerOptions.setIncludeProfileNames("*");


        sourceScannerOptions.setMavenScannerOptions(mavenScannerOptions);
        source.setBaseUrl("https://artifacts.backbase.com/repo/");
        source.setUsername("bartv");
        source.setPassword("AP8rdgKcR5wwSHMizhDtcHnayDU");

        mavenSpecSourceScanner.setSource(source);
        mavenSpecSourceScanner.setScannerOptions(sourceScannerOptions);

        ScanResult scan = mavenSpecSourceScanner.scan();



    }

}
