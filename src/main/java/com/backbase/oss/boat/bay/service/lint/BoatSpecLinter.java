package com.backbase.oss.boat.bay.service.lint;

import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.LintRuleViolation;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.events.SpecUpdatedEvent;
import com.backbase.oss.boat.bay.repository.LintReportRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleViolationRepository;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.zalando.zally.core.ApiValidator;
import org.zalando.zally.core.Result;
import org.zalando.zally.core.RulesPolicy;

@Component
@RequiredArgsConstructor
@Slf4j
public class BoatSpecLinter {

    private final BoatLintRuleValidatorFactory boatLintRuleValidatorFactory;
    private final BoatLintRuleViolationRepository boatLintRuleViolationRepository;
    private final BoatLintRuleRepository boatLintRuleRepository;
    private final LintReportRepository lintReportRepository;

    private void lint(Spec spec) {
        log.info("Linting Spec: {}", spec.getName());
        ApiValidator apiValidator = boatLintRuleValidatorFactory.getApiValidatorFor(spec.getPortal());
        RulesPolicy rulesPolicy = boatLintRuleValidatorFactory.getRulePolicy(spec.getPortal());
        List<Result> validate = apiValidator.validate(spec.getOpenApi(), rulesPolicy, null);

        LintReport lintReport = getLintReport(spec);


        // Reset Lint Report Violations
        boatLintRuleViolationRepository.deleteByLintReport(lintReport);

        // Collect new Violations
        Set<LintRuleViolation> violations = validate.stream()
            .map(result -> mapResult(lintReport, result))
            .collect(Collectors.toSet());

        lintReport.setName("Lint Report " + spec.getName() + "-" + spec.getVersion());
        lintReport.setGrade(calculateGrade(violations));
        lintReport.setLintedOn(Instant.now());
        lintReport.setLintRuleViolations(violations);

        lintReportRepository.save(lintReport);
    }

    private String calculateGrade(Set<LintRuleViolation> violations) {
        if (hasSeverity(violations, Severity.MUST)) {
            return "F";
        }
        if (hasSeverity(violations, Severity.SHOULD)) {
            return "D";
        }
        if (hasSeverity(violations, Severity.MAY)) {
            return "C";
        }
        if (hasSeverity(violations, Severity.HINT)) {
            return "B";
        }
        return "A";
    }

    private boolean hasSeverity(Set<LintRuleViolation> violations, Severity must) {
        return violations.stream()
            .anyMatch(violation -> violation.getSeverity().equals(must));
    }

    @NotNull
    private LintRuleViolation mapResult(LintReport lintReport, Result result) {
        LintRuleViolation lintRuleViolation = new LintRuleViolation();
        lintRuleViolation.lintReport(lintReport);
        boatLintRuleRepository.findByRuleId(result.getId()).ifPresent(lintRule -> {
            lintRuleViolation.setSeverity(lintRule.getSeverity());
            lintRuleViolation.setLintRule(lintRule);
        });
        lintRuleViolation.setName(result.getTitle());
        lintRuleViolation.setDescription(result.getDescription());
        lintRuleViolation.setLineStart(result.getLines().getStart());
        lintRuleViolation.setLineEnd(result.getLines().getLast());
        lintRuleViolation.setJsonPointer(result.getPointer().toString());
        lintRuleViolation.setUrl(result.getUrl().toString());
        return lintRuleViolation;
    }

    private LintReport getLintReport(Spec spec) {
        LintReport specLintReport = new LintReport()
            .spec(spec);

        LintReport newLintReport = lintReportRepository.findOne(Example.of(specLintReport)).orElseGet(() -> {
            return lintReportRepository.save(specLintReport);
        });
        return newLintReport;
    }


    @EventListener(SpecUpdatedEvent.class)
    @Async
    public void handleSpecUpdated(SpecUpdatedEvent event) {
        lint(event.getSpec());

    }


}
