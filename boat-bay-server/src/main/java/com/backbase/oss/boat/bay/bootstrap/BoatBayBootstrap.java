package com.backbase.oss.boat.bay.bootstrap;

import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.domain.SpecType;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.exceptions.BootstrapException;
import com.backbase.oss.boat.bay.repository.ProductRepository;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import com.backbase.oss.boat.bay.repository.SpecTypeRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatDashboardRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatSourcePathRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.zalando.zally.core.RuleDetails;
import org.zalando.zally.core.RulesManager;
import org.zalando.zally.rule.api.Rule;
import org.zalando.zally.rule.api.RuleSet;

@Component
@Slf4j
@RequiredArgsConstructor
@DependsOn("liquibase")
public class BoatBayBootstrap {

    @Value("${backbase.bootstrap.file:''}")
    private File bootstrapFile;

    private final BoatPortalRepository portalRepository;
    private final ProductRepository productRepository;
    private final SourceRepository sourceRepository;
    private final BoatDashboardRepository dashboardRepository;
    private final SpecTypeRepository specTypeRepository;
    private final BoatSourcePathRepository sourcePathRepository;
    private final RulesManager rulesManager;
    private final BoatLintRuleRepository lintRuleRepository;

    private final JavaTimeModule javaTimeModule;
    private final Jdk8Module jdk8Module;

    private Map<String, RuleDetails> availableRules;

    @PostConstruct
    public void loadBootstrapFile() {

        if (bootstrapFile != null && bootstrapFile.exists()) {

            log.info("Loading bootstrap from: {}", bootstrapFile);
            ObjectMapper objectMapper = new ObjectMapper(YAMLFactory.builder().build());
            objectMapper.registerModule(javaTimeModule);
            objectMapper.registerModule(jdk8Module);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            try {
                Bootstrap bootstrap = objectMapper.readValue(bootstrapFile, Bootstrap.class);
                bootstrap.getPortals().forEach(portal -> {
                    Optional<Portal> existingPortal = portalRepository.findOne(Example.of(portal));
                    if (existingPortal.isEmpty()) {
                        log.info("Bootstrapping portal: {}", portal.getName());
                        Set<LintRule> lintRules = portal.getLintRules();
                        portal.setLintRules(null);
                        portalRepository.save(portal);
                        lintRules.stream().map(rule -> createLintRule(portal, rule)).forEach(lintRuleRepository::save);
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
            rulesManager.getRules()
                .forEach(rule -> availableRules.put(rule.getRule().id(), rule));
        }
        return availableRules;
    }

    private void bootstrapSource(Source source) throws BootstrapException {
        Portal portal = portalRepository.findOne(Example.of(source.getPortal())).orElseThrow(() -> new BootstrapException("Cannot create source with portal: " + source.getPortal() + " as it does not exist"));
        source.setPortal(portal);
        if (source.getProduct() == null) {
            throw new BootstrapException("You must define a product in the source");
        }
        source.getProduct().setPortal(portal);
        Product product = productRepository.findOne(Example.of(source.getProduct())).orElseGet(() -> productRepository.save(source.getProduct()));
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

}
