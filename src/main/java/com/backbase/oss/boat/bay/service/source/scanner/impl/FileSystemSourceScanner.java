package com.backbase.oss.boat.bay.service.source.scanner.impl;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.service.source.scanner.SpecSourceScanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * Walks over a directory structure and based on directory names and depths, import specs
 */
@Slf4j
public class FileSystemSourceScanner implements SpecSourceScanner {

    @Getter
    @Setter
    private Source source;

    public List<Spec> scan() {

        List<Spec> specs = new ArrayList<>();
        for (SourcePath p : source.getSourcePaths()) {
            Path scanPath = Path.of(p.getName());
            try {
                specs.addAll(Files.walk(scanPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.endsWith("*.yaml"))
                    .map(path -> mapSpec(scanPath, path))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));
            } catch (IOException e) {
                log.error("Failed to scan path: {}", scanPath, e);
            }

        }
        return specs;
    }

    @NotNull
    private Optional<Spec> mapSpec(Path scanPath, Path path) {
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
