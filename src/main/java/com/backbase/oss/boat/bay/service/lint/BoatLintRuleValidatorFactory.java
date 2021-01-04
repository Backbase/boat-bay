package com.backbase.oss.boat.bay.service.lint;

import com.backbase.oss.boat.bay.domain.Portal;
import com.typesafe.config.Config;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.zalando.zally.core.ApiValidator;
import org.zalando.zally.core.CompositeRulesValidator;
import org.zalando.zally.core.ContextRulesValidator;
import org.zalando.zally.core.DefaultContextFactory;
import org.zalando.zally.core.JsonRulesValidator;
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


    @Cacheable(API_VALIDATORS)
    public ApiValidator getApiValidatorFor(Portal portal) {
        DefaultContextFactory defaultContextFactory = new DefaultContextFactory();
        ContextRulesValidator contextRulesValidator = new ContextRulesValidator(defaultRuleManager, defaultContextFactory);
        JsonRulesValidator jsonRulesValidator = new JsonRulesValidator(defaultRuleManager);
        ApiValidator apiValidator = new CompositeRulesValidator(contextRulesValidator, jsonRulesValidator);
        log.info("Created new API Validator for Portal: {}", portal.getName());
        return apiValidator;
    }

    @Cacheable(API_RULE_POLICY)
    public RulesPolicy getRulePolicy(Portal portal) {
        log.info("Created new Rule Policy for Portal: {}", portal.getName());
        return new RulesPolicy(new ArrayList<>());

    }
}
