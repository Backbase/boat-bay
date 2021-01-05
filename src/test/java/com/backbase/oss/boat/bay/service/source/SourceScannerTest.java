package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.service.source.scanner.impl.FileSystemSourceScanner;
import org.junit.jupiter.api.Test;

public class SourceScannerTest {

    @Test
    public void fileSystemSourceScannerTest(){
        FileSystemSourceScanner scanner =new FileSystemSourceScanner();
        Source source = new Source();
        source.setPath("/Users/sophiej/Documents/Projects/opensauce/boat-bay/boat-bay-data/Artifactory");
        scanner.setSource(source);
        scanner.scan();
    }
}
