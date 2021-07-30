package com.backbase.oss.boat.bay.source.scanner;

import lombok.Data;

@Data
public class MavenScannerOptions {

    private String localRepoPath;
    private String mavenArtifactCoords;
}
