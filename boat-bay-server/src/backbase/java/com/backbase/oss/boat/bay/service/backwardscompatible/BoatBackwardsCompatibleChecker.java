package com.backbase.oss.boat.bay.service.backwardscompatible;

import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.Changes;
import com.backbase.oss.boat.bay.repository.BoatSpecRepository;
import com.backbase.oss.boat.bay.util.TagsDiff;
import com.github.zafarkhaja.semver.Version;
import io.swagger.v3.oas.models.PathItem;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.openapidiff.core.OpenApiCompare;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@DependsOn("boatBayBootstrap")
public class BoatBackwardsCompatibleChecker {

    private final BoatSpecRepository specRepository;

    public void checkBackwardsCompatibility(Spec input) {
        Spec spec = specRepository.findById(input.getId()).orElseThrow();

        log.info("Checking Changes for Spec: {}", spec.getName());
        List<Spec> specs = specRepository.findAllByKeyAndServiceDefinitionAndVersionIsNotNull(spec.getKey(), spec.getServiceDefinition());

        if (specs.size() == 1 && specs.get(0).getId().equals(spec.getId())) {
            spec.setChanges(Changes.NOT_APPLICABLE);
        } else if (specs.size() > 1) {
            Optional<Version> specVersion = parseVersion(spec);
            if (specVersion.isEmpty()) {
                spec.setChanges(Changes.INVALID_VERSION);
            } else {
                Optional<Spec> previousSpec = specs
                    .stream()
                    .filter(s -> !s.equals(spec))
                    .filter(this::validateSpecVersion)
                    .sorted(this::compareVersion)
                    .filter(s -> !parseVersion(s).orElseThrow().greaterThan(specVersion.get()))
                    .findFirst();

                if (previousSpec.isPresent()) {
                    Spec prevSpec = previousSpec.get();
                    String prevVersion = prevSpec.getVersion();
                    log.info(
                        "Previous version from: {} detected: {} in list: {}",
                        prevVersion,
                        specVersion.get(),
                        specs.stream().map(Spec::getVersion).collect(Collectors.joining(", "))
                    );

                    if (prevSpec.getSuccessor() == null) {
                        spec.setPreviousSpec(prevSpec);
                    } else {
                        log.info("Cannot have multiple previous specs");
                    }

                    String currentOpenAPI = spec.getOpenApi();
                    String previousOpenAPI = prevSpec.getOpenApi();
                    ChangedOpenApi diff = null;
                    try {
                        diff = OpenApiCompare.fromContents(previousOpenAPI, currentOpenAPI);
                        Map<String, Map<PathItem.HttpMethod, List<String>>> changedTags = TagsDiff.findMissingTags(diff);
                        if (diff.isUnchanged() && changedTags.isEmpty()) {
                            spec.setChanges(Changes.UNCHANGED);
                        } else if (diff.isCompatible() && changedTags.isEmpty()) {
                            spec.setChanges(Changes.COMPATIBLE);
                        } else if (diff.isIncompatible() || !changedTags.isEmpty()) {
                            spec.setChanges(Changes.BREAKING);
                        }
                    } catch (Exception e) {
                        spec.setParseError(e.getMessage());
                        spec.setChanges(Changes.ERROR_COMPARING);
                    }

                    log.info(
                        "Checking backwards compatibility between: {} and: {} result: {}",
                        specVersion.get(),
                        prevVersion,
                        spec.getChanges()
                    );
                }
            }
        }
        specRepository.saveAndFlush(spec);
    }

    private int compareVersion(Spec o1, Spec o2) {
        Optional<Version> v1 = parseVersion(o1);
        Optional<Version> v2 = parseVersion(o2);
        if (v1.isPresent() && v2.isPresent()) {
            return v2.get().compareTo(v1.get());
        } else {
            return -1;
        }
    }

    private boolean validateSpecVersion(Spec s) {
        try {
            Version.valueOf(s.getVersion());
            return true;
        } catch (Exception e) {
            log.debug(" Unable to parse version from: {}", s);
        }
        return false;
    }

    private Optional<Version> parseVersion(Spec spec) {
        try {
            return Optional.of(Version.valueOf(spec.getVersion()));
        } catch (Exception e) {
            log.debug(" Unable to parse version from: {}", spec.getVersion());
        }
        return Optional.empty();
    }
}
