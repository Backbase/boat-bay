package com.backbase.oss.boat.bay.bootstrap;

import com.backbase.oss.boat.bay.config.BoatBayConfigurationProperties;
import com.backbase.oss.boat.bay.domain.*;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.exceptions.BootstrapException;
import com.backbase.oss.boat.bay.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.zalando.zally.core.RuleDetails;
import org.zalando.zally.core.RulesManager;
import org.zalando.zally.rule.api.Rule;
import org.zalando.zally.rule.api.RuleSet;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(BoatBayConfigurationProperties.class)
@DependsOn("liquibase")
public class BoatBayBootstrap {

    private final BoatBayConfigurationProperties configuration;
    private final BoatPortalRepository portalRepository;
    private final ProductRepository productRepository;
    private final SourceRepository sourceRepository;
    private final BoatDashboardRepository dashboardRepository;
    private final SpecTypeRepository specTypeRepository;
    private final BoatSourcePathRepository sourcePathRepository;
    private final RulesManager rulesManager;
    private final BoatLintRuleRepository lintRuleRepository;

    private Map<String, RuleDetails> availableRules;

    private final PlatformTransactionManager txManager;

    private final BootstrapMapper mapper = Mappers.getMapper(BootstrapMapper.class);

    @PostConstruct
    public void loadBootstrapFile() {
        if (configuration.getBootstrap() != null) {

            BoatBayConfigurationProperties.Bootstrap bootstrap = configuration.getBootstrap();
            TransactionTemplate transactionTemplate = new TransactionTemplate(txManager);
            transactionTemplate.execute(
                new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(@NotNull TransactionStatus transactionStatus) {
                        try {
                            saveBootstrapConfiguration(bootstrap);
                            transactionStatus.flush();
                        } catch (BootstrapException e) {
                            log.error("Failed to bootstrap ", e);
                        }
                    }
                }
            );
        }
    }

    private void saveBootstrapConfiguration(BoatBayConfigurationProperties.Bootstrap bootstrap) throws BootstrapException {
        BoatBayConfigurationProperties.Bootstrap.Dashboard.Portal defaultPortal = bootstrap.getDashboard().getDefaultPortal();

        Optional<Portal> existingPortal = portalRepository.findByKey(defaultPortal.getKey());
        if (existingPortal.isEmpty()) {

            Portal newPortal = mapper.map(defaultPortal);
            newPortal.setCreatedBy("system");
            newPortal.setCreatedOn(ZonedDateTime.now());
            newPortal.setHide(false);

            log.info("Bootstrapping portal: {}", newPortal.getName());
            Set<LintRule> lintRules = newPortal.getLintRules();
            newPortal.setLintRules(null);
            portalRepository.save(newPortal);
            lintRules.stream().map(rule -> createLintRule(newPortal, rule)).forEach(lintRuleRepository::save);

            Product defaultProduct = mapper.map(defaultPortal.getDefaultProduct());
            defaultProduct.setCreatedBy("system");
            defaultProduct.setCreatedOn(ZonedDateTime.now());
            defaultProduct.setPortal(newPortal);
            defaultProduct.setHide(false);
            defaultProduct.setOrder(0);
            productRepository.save(defaultProduct);



            existingPortal = Optional.of(newPortal);
        }

        Dashboard dashboard = mapper.map(bootstrap.getDashboard());

        if (dashboard != null) {
            Optional<Dashboard> existingDashboard = dashboardRepository.findDashboardByName(dashboard.getName());
            if (existingDashboard.isEmpty()) {
                dashboard.setDefaultPortal(existingPortal.orElseThrow());
                log.info("Bootstrapping dashboard: {}", dashboard.getName());
                dashboardRepository.save(dashboard);
            }
        }

//        for (Source source : bootstrap.getSources()) {
//            Optional<Source> existingSource = sourceRepository.findOne(Example.of(new Source().name(source.getName())));
//            if (existingSource.isEmpty()) {
//                bootstrapSource(source);
//            }
//        }

        if (bootstrap.getSpecTypes() != null) {
            bootstrap
                .getSpecTypes()
                .stream()
                .map(mapper::map)
                .forEach(
                    specType -> {
                        Optional<SpecType> existingSpecType = specTypeRepository.findOne(Example.of(specType));
                        if (existingSpecType.isEmpty()) {
                            log.info("Bootstrapping Spec Type: {}", specType.getName());
                            specTypeRepository.save(specType);
                        }
                    }
                );
        }
    }

    private LintRule createLintRule(Portal portal, LintRule rule) {
        RuleDetails ruleDetails = getRule(rule.getRuleId());
        Rule zallyRule = ruleDetails.getRule();
        RuleSet ruleSet = ruleDetails.getRuleSet();

        LintRule newLintRule = new LintRule();
        newLintRule.setRuleSet(ruleSet.getId());
        newLintRule.setRuleId(zallyRule.id());
        newLintRule.setExternalUrl(ruleSet.url(zallyRule).toString());
        newLintRule.setTitle(zallyRule.title());
        newLintRule.setSeverity(Severity.valueOf(zallyRule.severity().name()));
        newLintRule.setEnabled(true);
        newLintRule.setSummary("");
        newLintRule.setDescription("");
        newLintRule.portal(portal);
        return newLintRule;
    }

    private RuleDetails getRule(String ruleId) {
        RuleDetails rule = getAvailableRules().get(ruleId);
        if (rule == null) {
            throw new IllegalStateException("Rule with Id: " + ruleId + " is not in the list of available rules");
        }
        return rule;
    }

    private Map<String, RuleDetails> getAvailableRules() {
        if (availableRules == null) {
            availableRules = new HashMap<>();
            rulesManager.getRules().forEach(rule -> availableRules.put(rule.getRule().id(), rule));
        }
        return availableRules;
    }

    private void bootstrapSource(Source source) throws BootstrapException {
        Portal portal = portalRepository
            .findOne(Example.of(source.getPortal()))
            .orElseThrow(() -> new BootstrapException("Cannot create source with portal: " + source.getPortal() + " as it does not exist"));
        source.setPortal(portal);
        if (source.getProduct() == null) {
            throw new BootstrapException("You must define a product in the source");
        }
        source.getProduct().setPortal(portal);
        Product product = productRepository
            .findOne(Example.of(source.getProduct()))
            .orElseGet(() -> productRepository.save(source.getProduct()));
        source.setProduct(product);
        log.info("Bootstrapping source: {}", source.getName());
        sourceRepository.save(source);
        sourcePathRepository.deleteAllBySource(source);

        for (SourcePath sourcePath : source.getSourcePaths()) {
            sourcePath.setSource(source);
            sourcePathRepository.save(sourcePath);
        }
    }

}
