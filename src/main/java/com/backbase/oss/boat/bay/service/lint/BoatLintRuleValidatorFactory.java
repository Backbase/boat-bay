package com.backbase.oss.boat.bay.service.lint;

import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.PortalLintRule;
import com.backbase.oss.boat.bay.events.RuleUpdatedEvent;
import com.backbase.oss.boat.bay.repository.LintRuleRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalLintRuleRepository;
import com.typesafe.config.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.zalando.zally.core.ApiValidator;
import org.zalando.zally.core.CompositeRulesValidator;
import org.zalando.zally.core.ContextRulesValidator;
import org.zalando.zally.core.DefaultContextFactory;
import org.zalando.zally.core.JsonRulesValidator;
import org.zalando.zally.core.RuleDetails;
import org.zalando.zally.core.RulesManager;
import org.zalando.zally.core.RulesPolicy;

@Component
@RequiredArgsConstructor
@Slf4j
public class BoatLintRuleValidatorFactory {


    public static final String API_VALIDATORS = "apiValidators";
    public static final String API_RULE_POLICY = "apiRulePolicy";
    private final RulesManager defaultRuleManager;
    private final Config config;

    private final LintRuleRepository lintRuleRepository;
    private final BoatPortalLintRuleRepository portalLintRuleRepository;


    @Cacheable(API_RULE_POLICY)
    public ApiValidator getApiValidatorFor(Portal portal) {
        prepareLintRules(portal);
        Set<PortalLintRule> enabledRules = portalLintRuleRepository.findAllByPortalAndEnabled(portal, true);
        Set<String> ids = enabledRules.stream().map(PortalLintRule::getRuleId).collect(Collectors.toSet());

        List<RuleDetails> collect = defaultRuleManager.getRules().stream().filter(ruleDetails -> ids.contains(ruleDetails.getRule().id())).collect(Collectors.toList());

        RulesManager portalRuleManager = new RulesManager(config, collect);


        DefaultContextFactory defaultContextFactory = new DefaultContextFactory();
        ContextRulesValidator contextRulesValidator = new ContextRulesValidator(portalRuleManager, defaultContextFactory);
        JsonRulesValidator jsonRulesValidator = new JsonRulesValidator(portalRuleManager);
        return new CompositeRulesValidator(contextRulesValidator, jsonRulesValidator);
    }

    private synchronized void prepareLintRules(Portal portal) {
        if (!portalLintRuleRepository.existsAllByPortal(portal)) {
            portalLintRuleRepository.saveAll(lintRuleRepository.findAll().stream().map(lintRule ->
                new PortalLintRule()
                    .portal(portal)
                    .ruleId(lintRule.getRuleId())
                    .lintRule(lintRule)
                    .enabled(lintRule.isEnabled())).collect(Collectors.toSet())
            );
        }
    }

    public RulesPolicy getRulePolicy(Portal portal) {
        log.info("Created new Rule Policy for Portal: {}", portal.getName());
        return new RulesPolicy(new ArrayList<>());
    }

    @CacheEvict(API_RULE_POLICY)
    public void evictPortalCache(Portal portal) {
        log.info("Portal API Rules Cache Evicted for: {}", portal.getName());
    }

    @Async
    @EventListener(RuleUpdatedEvent.class)
    public void lintRuleUpdatedEvent(RuleUpdatedEvent ruleUpdatedEvent) {

        portalLintRuleRepository.findAllByLintRule(ruleUpdatedEvent.getLintRule())
            .forEach(portalLintRule -> {
                evictPortalCache(portalLintRule.getPortal());
                if (portalLintRule.isEnabled() && !ruleUpdatedEvent.getLintRule().isEnabled()) {
                    portalLintRule.setEnabled(false);
                    log.info("Disabled Rule: {} for portal: {} as global rule is disabled", portalLintRule.getRuleId(), portalLintRule.getPortal().getName());
                    portalLintRuleRepository.save(portalLintRule);
                }
            });
    }
}
