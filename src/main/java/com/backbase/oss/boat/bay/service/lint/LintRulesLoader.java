package com.backbase.oss.boat.bay.service.lint;

import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.LintRuleSet;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleSetRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.zalando.zally.core.RuleDetails;
import org.zalando.zally.core.RulesManager;
import org.zalando.zally.rule.api.Rule;

@Component
@RequiredArgsConstructor
@Slf4j
@DependsOn("liquibase")
public class LintRulesLoader {

    private final RulesManager rulesManager;
    private final BoatLintRuleSetRepository lintRuleSetRepository;
    private final BoatLintRuleRepository lintRuleRepository;

    @EventListener({ContextRefreshedEvent.class})
    @Async
    public void setupDefaultRules() {
        log.info("Loading Default BOAT Linting Rules");

        rulesManager.getRules().stream()
            .collect(Collectors.groupingBy(RuleDetails::getRuleSet))
            .forEach((ruleSet, rules) -> {

                LintRuleSet lintRuleSet = lintRuleSetRepository.findByRuleSetId(ruleSet.getId())
                    .map(rs -> {
                        log.debug("Rule Set: {} already exists.", rs.getName());
                        return rs;
                    })
                    .orElseGet(() -> {
                        LintRuleSet newLintRuleSet = new LintRuleSet();
                        newLintRuleSet.setRuleSetId(ruleSet.getId());
                        newLintRuleSet.setExternalUrl(ruleSet.getUrl().toString());
                        newLintRuleSet.setName(ruleSet.getId());
                        log.debug("Created new Rule Set: {}", ruleSet.getId());
                        return lintRuleSetRepository.save(newLintRuleSet);
                    });
                log.info("Loaded Rule Set: {}", lintRuleSet.getName());


                rules.forEach(ruleDetails -> {
                    Rule rule = ruleDetails.getRule();
                    LintRule lintRule = lintRuleRepository.findByRuleId(rule.id())
                        .map(lr -> {
                            log.debug("Rule {} already exists", lr.getTitle());
                            return lr;
                        })
                        .orElseGet(() -> {
                            LintRule newLintRule = new LintRule();
                            newLintRule.setRuleSet(lintRuleSet);
                            newLintRule.setRuleId(rule.id());
                            newLintRule.setExternalUrl(ruleSet.url(rule).toString());
                            newLintRule.setTitle(rule.title());
                            newLintRule.setSeverity(Severity.valueOf(rule.severity().name()));
                            newLintRule.setEnabled(true);
                            newLintRule.setSummary("");
                            newLintRule.setDescription("");
                            log.debug("Created new Rule: {} for Rule Set: {}", newLintRule.getRuleId(), ruleSet.getId());
                            return lintRuleRepository.save(newLintRule);
                        });
                });


            });
        log.info("Finished Default BOAT Linting Rules");
    }
}
