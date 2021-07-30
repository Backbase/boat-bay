package com.backbase.oss.boat.bay.source.scanner;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class SourceScannerOptions {

    private Map<String, String> capabilityMappingOverrides = new HashMap<>();

    private MavenScannerOptions mavenScannerOptions;
}
