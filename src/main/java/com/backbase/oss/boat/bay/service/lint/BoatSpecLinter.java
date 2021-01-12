package com.backbase.oss.boat.bay.service.lint;

import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.LintRuleViolation;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.events.SpecUpdatedEvent;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintReportRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleViolationRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.zally.core.ApiValidator;
import org.zalando.zally.core.Result;
import org.zalando.zally.core.RuleDetails;
import org.zalando.zally.core.RulesManager;
import org.zalando.zally.core.RulesPolicy;

@Component
@RequiredArgsConstructor
@Slf4j
public class BoatSpecLinter {

    private final BoatLintRuleValidatorFactory boatLintRuleValidatorFactory;
    private final BoatLintRuleViolationRepository boatLintRuleViolationRepository;
    private final BoatLintRuleRepository boatLintRuleRepository;
    private final BoatLintReportRepository lintReportRepository;
    private final RulesManager rulesManager;
    private final SpecRepository specRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);


    public void lint(Long id) {
        Optional<Spec> byId = specRepository.findById(id);
        byId.ifPresent(this::lint);
    }

    @Transactional
    public LintReport lint(Spec spec) {
        log.info("Linting Spec: {}", spec.getName());
        ApiValidator apiValidator = getApiValidator(spec);
        RulesPolicy rulesPolicy = getRulesPolicy(spec);
        List<Result> validate = apiValidator.validate(spec.getOpenApi(), rulesPolicy, null);

        LintReport lintReport = lintReportRepository.findBySpec(spec).orElse(new LintReport().spec(spec));

        // Collect new Violations
        Set<LintRuleViolation> violations = validate.stream()
            .map(result -> mapResult(lintReport, result))
            .collect(Collectors.toSet());
        lintReport.setName("Lint Report " + spec.getName() + "-" + spec.getVersion());
        lintReport.setGrade(calculateGrade(violations));
        lintReport.setLintedOn(Instant.now());
        lintReportRepository.save(lintReport);
        boatLintRuleViolationRepository.deleteByLintReport(lintReport);
        boatLintRuleViolationRepository.saveAll(violations);
        return lintReport;
    }

    public List<RuleDetails> getApplicableRuleDetails(Spec spec) {
        RulesPolicy rulesPolicy = getRulesPolicy(spec);
        return rulesManager.rules(rulesPolicy);
    }

    public RulesPolicy getRulesPolicy(Spec spec) {
        return boatLintRuleValidatorFactory.getRulePolicy(spec.getPortal());
    }

    public ApiValidator getApiValidator(Spec spec) {
        return boatLintRuleValidatorFactory.getApiValidatorFor(spec.getPortal());
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

    @EventListener(SpecUpdatedEvent.class)
    @Async
    public void handleSpecUpdated(SpecUpdatedEvent event) {
        Long id = event.getSpec().getId();
        executorService.submit(() -> lint(id));
    }

}
