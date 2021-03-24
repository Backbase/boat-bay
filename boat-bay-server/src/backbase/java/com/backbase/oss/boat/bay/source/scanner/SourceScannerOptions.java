package com.backbase.oss.boat.bay.source.scanner;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class SourceScannerOptions {

    private Map<String, String> capabilityMappingOverrides = new HashMap<>();

    private MavenScannerOptions mavenScannerOptions;



    @Data
    public static class MavenScannerOptions {

        private String id;
        private String url;
        private String username;
        private String password;

        private String groupId;
        private String artifactId;
        private String versionRange;
        private String includeGroupIds;
        private String includeArtifactIds;
        private String includeClassifiers;
        private String includeProfileNames;


    }

}
