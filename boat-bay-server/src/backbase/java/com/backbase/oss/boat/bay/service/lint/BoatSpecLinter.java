package com.backbase.oss.boat.bay.service.lint;

import com.backbase.oss.boat.bay.config.BoatCacheManager;
import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.LintRuleViolation;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.repository.BoatLintReportRepository;
import com.backbase.oss.boat.bay.repository.BoatLintRuleRepository;
import com.backbase.oss.boat.bay.repository.BoatLintRuleViolationRepository;
import com.backbase.oss.boat.bay.repository.BoatSpecRepository;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.zally.core.ApiValidator;
import org.zalando.zally.core.Result;
import org.zalando.zally.core.RuleDetails;
import org.zalando.zally.core.RulesManager;
import org.zalando.zally.core.RulesPolicy;

@Component
@RequiredArgsConstructor
@Slf4j
@DependsOn("boatBayBootstrap")
public class BoatSpecLinter {

    private final BoatLintRuleValidatorFactory boatLintRuleValidatorFactory;
    private final BoatLintRuleViolationRepository boatLintRuleViolationRepository;

    private final BoatLintRuleRepository boatLintRuleRepository;
    private final BoatLintReportRepository lintReportRepository;
    private final RulesManager rulesManager;
    private final BoatSpecRepository specRepository;

    private final BoatCacheManager boatCacheManager;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void scheduleLintJob(Spec spec) {
        lint(spec);
    }

    public LintReport lint(Spec spec) {
        spec = specRepository.findById(spec.getId()).orElseThrow();

        log.info("Start Lint Spec: {}", spec.getName());
        if (spec.getValid() == null || spec.getServiceDefinition() == null) {
            log.warn("I should never ever exists");
        }

        ApiValidator apiValidator = getApiValidator(spec);
        RulesPolicy rulesPolicy = getRulesPolicy(spec);

        Map<String, LintRule> applicableRules = boatLintRuleValidatorFactory
            .getAllByPortalAndEnabled(spec.getPortal())
            .stream()
            .collect(Collectors.toMap(LintRule::getRuleId, Function.identity()));

        List<Result> validate = apiValidator.validate(spec.getOpenApi(), rulesPolicy, null);

        if (spec.getLintReport() != null) {
            spec = deleteExistingLintReport(spec);
        }

        LintReport lintReport = new LintReport();
        lintReport.setViolations(new HashSet<>());
        // Collect new Violations
        Set<LintRuleViolation> violations = new HashSet<>();
        for (Result result : validate) {
            LintRuleViolation lintRuleViolation = mapResult(lintReport, result, applicableRules);
            violations.add(lintRuleViolation);
        }

        String grade = calculateGrade(violations);
        lintReport.setSpec(spec);
        lintReport.setName(spec.getFilename());
        lintReport.setGrade(grade);
        lintReport.setLintedOn(ZonedDateTime.now());
        lintReport.setViolations(violations);
        lintReport = lintReportRepository.save(lintReport);
        boatLintRuleViolationRepository.saveAll(violations);

        spec.setLintReport(lintReport);

        spec.setGrade(grade);
        specRepository.saveAndFlush(spec);

        log.info("Finished Linting Spec: {}", spec.getName());
        return lintReport;
    }

    @NotNull
    @Transactional
    public Spec deleteExistingLintReport(Spec spec) {
        LintReport existingLintReport = spec.getLintReport();
        boatLintRuleViolationRepository.deleteByLintReport(existingLintReport);
        lintReportRepository.delete(existingLintReport);
        spec.setLintReport(null);
        spec = specRepository.saveAndFlush(spec);
        return spec;
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
        return violations.stream().anyMatch(violation -> violation.getSeverity().equals(must));
    }

    @NotNull
    private LintRuleViolation mapResult(LintReport lintReport, Result result, Map<String, LintRule> applicableRules) {
        LintRule lintRule = applicableRules.get(result.getId());

        LintRuleViolation lintRuleViolation = new LintRuleViolation();
        lintRuleViolation.lintReport(lintReport);
        lintRuleViolation.setLintRule(lintRule);
        lintRuleViolation.setSeverity(lintRule.getSeverity());
        lintRuleViolation.setName(result.getTitle());
        lintRuleViolation.setDescription(result.getDescription());
        lintRuleViolation.setLineStart(result.getLines().getStart());
        lintRuleViolation.setLineEnd(result.getLines().getLast());
        lintRuleViolation.setJsonPointer(result.getPointer().toString());
        lintRuleViolation.setUrl(result.getUrl().toString());
        return lintRuleViolation;
    }
}
