package com.backbase.oss.boat.bay.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.source.scanner.SourceScannerOptions;
import org.junit.jupiter.api.Test;

class MavenSpecSourceScannerTest {

    @Test
    public void test() {
        MavenSpecSourceScanner mavenSpecSourceScanner = new MavenSpecSourceScanner();

        Source source = new Source();


        SourceScannerOptions sourceScannerOptions = new SourceScannerOptions();
        source.setBaseUrl("https://artifacts.backbase.com/repo");
        source.setUsername("bartv");
        source.setPassword(System.getenv("M2_PASSWORD"));

        mavenSpecSourceScanner.setSource(source);
        mavenSpecSourceScanner.setScannerOptions(sourceScannerOptions);

        ScanResult scan = mavenSpecSourceScanner.scan();

    }

}
