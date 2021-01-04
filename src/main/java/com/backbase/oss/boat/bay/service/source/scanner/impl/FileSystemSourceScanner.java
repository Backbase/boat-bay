package com.backbase.oss.boat.bay.service.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.service.source.scanner.SpecSourceScanner;
import com.backbase.oss.boat.loader.OpenAPILoader;
import com.backbase.oss.boat.loader.OpenAPILoaderException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.ItemHandle;
import org.jfrog.artifactory.client.model.File;
import org.jfrog.artifactory.client.model.RepoPath;

/**
 * Walks over a directory structure and based on directory names and depths, import specs
 *
 */
@Slf4j
public class FileSystemSourceScanner implements SpecSourceScanner {

    @Getter
    @Setter
    private Source source;

    public List<Spec> scan() {

        Path scanPath = Path.of(source.getPath());
        try {
            Files.walk(scanPath)
                .filter(Files::isRegularFile)
                .filter(path -> path.endsWith("*.yaml"))
                .map(path -> mapSpec(scanPath, path))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @NotNull
    private Optional<Spec> mapSpec(Path scanPath, Path path){
        Path relativize = scanPath.relativize(path);

        Spec spec = new Spec();
        String openApi = null;
        try {
            openApi = Files.readString(path);
        } catch (IOException e) {
            return Optional.empty();
        }
        spec.setOpenApi(openApi);

        return Optional.of(spec);
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.FILE_SYSTEM;
    }


}
