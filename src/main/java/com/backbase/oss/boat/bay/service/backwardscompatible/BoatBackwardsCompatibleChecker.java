package com.backbase.oss.boat.bay.service.backwardscompatible;

import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.extended.BoatSpecRepository;
import com.github.zafarkhaja.semver.Version;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.openapidiff.core.OpenApiCompare;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@DependsOn("boatBayBootstrap")
public class BoatBackwardsCompatibleChecker {

    private final BoatSpecRepository specRepository;


    @Scheduled(fixedRate = 3600000)
    public void checkSpecsToLint() {
        specRepository.findAllByBackwardsCompatibleIsNull().forEach(this::scheduleBackwardsCompatibleCheck);
    }

    @Async
    @Transactional
    public void scheduleBackwardsCompatibleCheck(Spec spec) {
        log.info("Scheduling linting of spec: {}", spec.getTitle());
        checkBackwardsCompatibility(spec);
    }

    public void checkBackwardsCompatibility(Spec spec) {
        List<Spec> specs = specRepository.findAllByNameAndServiceDefinitionAndVersionIsNotNull(spec.getName(), spec.getServiceDefinition());

        if (specs.size() == 1 && specs.get(0).getId().equals(spec.getId())) {
            setBackbwardsCompatible(spec, true);
        } else if (specs.size() > 1) {
            Optional<Spec> first = specs.stream()
                .filter(s -> !s.equals(spec))
                .min((o1, o2) -> {
                    Version v1 = Version.valueOf(o1.getVersion());
                    Version v2 = Version.valueOf(o2.getVersion());
                    return v1.compareTo(v2);
                });

            if(first.isPresent()) {
                String currentOpenAPI = spec.getOpenApi();
                String previousOpenAPI = first.get().getOpenApi();
                ChangedOpenApi diff = OpenApiCompare.fromContents(previousOpenAPI, currentOpenAPI);
                setBackbwardsCompatible(spec, diff.isCompatible());
                spec.setChanged(diff.isDifferent());
            }
        }


    }

    private void setBackbwardsCompatible(Spec spec, boolean backwardsCompatible) {
        spec.setBackwardsCompatible(backwardsCompatible);
        specRepository.save(spec);
    }


}
