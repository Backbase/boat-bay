package com.backbase.oss.boat.bay.service.lint;

import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.events.RuleUpdatedEvent;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleRepository;
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

    private final BoatLintRuleRepository lintRuleRepository;

    public ApiValidator getApiValidatorFor(Portal portal) {
        Set<LintRule> enabledRules = getAllByPortalAndEnabled(portal);
        Set<String> ids = enabledRules.stream().map(LintRule::getRuleId).collect(Collectors.toSet());

        List<RuleDetails> collect = defaultRuleManager.getRules().stream().filter(ruleDetails -> ids.contains(ruleDetails.getRule().id())).collect(Collectors.toList());

        RulesManager portalRuleManager = new RulesManager(config, collect);


        DefaultContextFactory defaultContextFactory = new DefaultContextFactory();
        ContextRulesValidator contextRulesValidator = new ContextRulesValidator(portalRuleManager, defaultContextFactory);
        JsonRulesValidator jsonRulesValidator = new JsonRulesValidator(portalRuleManager);
        return new CompositeRulesValidator(contextRulesValidator, jsonRulesValidator);
    }

    public Set<LintRule> getAllByPortalAndEnabled(Portal portal) {
        return lintRuleRepository.findAllByPortalAndEnabled(portal, true);
    }

    public RulesPolicy getRulePolicy(Portal portal) {
        return new RulesPolicy(new ArrayList<>());
    }

    @CacheEvict(API_RULE_POLICY)
    public void evictPortalCache(Portal portal) {
        log.info("Portal API Rules Cache Evicted for: {}", portal.getName());
    }

    @Async
    @EventListener(RuleUpdatedEvent.class)
    public void lintRuleUpdatedEvent(RuleUpdatedEvent ruleUpdatedEvent) {
        evictPortalCache(ruleUpdatedEvent.getLintRule().getPortal());
    }
}
