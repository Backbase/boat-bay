package com.backbase.oss.boat.bay.bootstrap;

import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.LintRuleSet;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.domain.SpecType;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.exceptions.BootstrapException;
import com.backbase.oss.boat.bay.repository.ProductRepository;
import com.backbase.oss.boat.bay.repository.SourcePathRepository;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import com.backbase.oss.boat.bay.repository.SpecTypeRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatDashboardRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleSetRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatSourcePathRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.zalando.zally.core.RuleDetails;
import org.zalando.zally.core.RulesManager;
import org.zalando.zally.rule.api.Rule;

@Component
@Slf4j
@RequiredArgsConstructor
@DependsOn("liquibase")
@ConditionalOnProperty("backbase.bootstrap.file")
public class BoatBayBootstrap {

    @Value("${backbase.bootstrap.file}")
    private File bootstrapFile;

    private final BoatPortalRepository portalRepository;
    private final ProductRepository productRepository;
    private final SourceRepository sourceRepository;
    private final BoatDashboardRepository dashboardRepository;
    private final SpecTypeRepository specTypeRepository;
    private final BoatSourcePathRepository sourcePathRepository;
    private final RulesManager rulesManager;
    private final BoatLintRuleSetRepository lintRuleSetRepository;
    private final BoatLintRuleRepository lintRuleRepository;

    @PostConstruct
    public void loadBootstrapFile() {
        log.info("Loading bootstrap from: {}", bootstrapFile);
        ObjectMapper objectMapper = new ObjectMapper(YAMLFactory.builder().build());
        try {
            Bootstrap bootstrap = objectMapper.readValue(bootstrapFile, Bootstrap.class);
            bootstrap.getPortals().forEach(portal -> {
                Optional<Portal> existingPortal = portalRepository.findOne(Example.of(portal));
                if (existingPortal.isEmpty()) {
                    log.info("Bootstrapping portal: {}", portal.getName());
                    portalRepository.save(portal);
                }
            });

            Dashboard dashboard = bootstrap.getDashboard();

            if (dashboard != null) {
                Optional<Dashboard> existingDashboard = dashboardRepository.findDashboardByName(dashboard.getName());
                if (existingDashboard.isEmpty()) {
                    Portal portal = portalRepository.findOne(Example.of(dashboard.getDefaultPortal())).orElseThrow(() -> new BootstrapException("Cannot create dashboard with portal: " + dashboard.getDefaultPortal() + " as it does not exist"));
                    dashboard.setDefaultPortal(portal);
                    log.info("Bootstrapping dashboard: {}", dashboard.getName());
                    dashboardRepository.save(dashboard);
                }
            }

            for (Source source : bootstrap.getSources()) {
                Optional<Source> existingSource = sourceRepository.findOne(Example.of(new Source().name(source.getName())));
                if (existingSource.isEmpty()) {
                    bootstrapSource(source);
                }
            }

            if (bootstrap.getSpecTypes() != null) {
                bootstrap.getSpecTypes().forEach(specType -> {
                    Optional<SpecType> existingSpecType = specTypeRepository.findOne(Example.of(specType));
                    if (existingSpecType.isEmpty()) {
                        log.info("Bootstrapping Spec Type: {}", specType.getName());
                        specTypeRepository.save(specType);
                    }
                });
            }

        } catch (IOException e) {
            log.error("Failed to read bootstrap yaml file from location: {}", bootstrapFile, e);
        } catch (BootstrapException e) {
            log.error("Failed to bootstrap ", e);
        }
    }

    private void bootstrapSource(Source source) throws BootstrapException {
        Portal portal = portalRepository.findOne(Example.of(source.getPortal())).orElseThrow(() -> new BootstrapException("Cannot create source with portal: " + source.getPortal() + " as it does not exist"));
        source.setPortal(portal);
        if(source.getProduct() == null) {
            throw new BootstrapException("You must define a product in the source");
        }
        source.getProduct().setPortal(portal);
        Product product  = productRepository.findOne(Example.of(source.getProduct())).orElseGet(() -> productRepository.save(source.getProduct()));
        source.setProduct(product);
        log.info("Bootstrapping source: {}", source.getName());
        sourceRepository.save(source);
        sourcePathRepository.deleteAllBySource(source);

        for (SourcePath sourcePath : source.getSourcePaths()) {
            sourcePath.setSource(source);
            sourcePathRepository.save(sourcePath);
        }
    }

    @Data
    public static class Bootstrap {
        private List<Portal> portals;
        private List<Source> sources;
        private Dashboard dashboard;
        private List<SpecType> specTypes;
    }

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
