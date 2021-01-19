package com.backbase.oss.boat.bay.web.views.dashboard;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.PortalLintRule;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.repository.PortalLintRuleRepository;
import com.backbase.oss.boat.bay.repository.TagRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatCapabilityRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatDashboardRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintReportRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleViolationRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatProductRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatServiceRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatSpecRepository;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.bay.service.statistics.BoatStatisticsCollector;
import com.backbase.oss.boat.bay.web.views.lint.BoatLintReport;
import com.backbase.oss.boat.bay.web.views.lint.BoatLintRule;
import com.backbase.oss.boat.bay.web.views.lint.BoatViolation;
import io.github.jhipster.web.util.PaginationUtil;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.zalando.zally.rule.api.Severity;


@RestController
@RequestMapping("/api/boat/")
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoatDashboardController {

    public static final String VIEWS = "views";

    private final BoatPortalRepository boatPortalRepository;
    private final BoatProductRepository boatProductRepository;
    private final BoatCapabilityRepository boatCapabilityRepository;
    private final BoatServiceRepository boatServiceRepository;
    private final BoatSpecRepository boatSpecRepository;
    private final BoatLintReportRepository boatLintReportRepository;
    private final BoatLintRuleViolationRepository boatLintRuleViolationRepository;
    private final PortalLintRuleRepository portalLintRuleRepository;
    private final BoatDashboardRepository dashboardRepository;

    private final BoatDashboardMapper dashboardMapper;
    private final TagRepository tagRepository;

    private final BoatStatisticsCollector boatStatisticsCollector;

    private final BoatSpecLinter boatSpecLinter;
    private Map<Severity, Integer> severityOrder;

    public List<String> getAllEnabledTags() {
        return tagRepository.findAll(Example.of(new Tag().hide(false))).stream()
            .map(Tag::getName)
            .collect(Collectors.toList());
    }

    @GetMapping("legacy-dashboard")
    @Cacheable(VIEWS)
    public ResponseEntity<BoatLegacyPortal> getDefaultPortal() {
        List<String> allEnabledTags = getAllEnabledTags();
        Dashboard dashboard = dashboardRepository.findAll()
            .stream()
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Portal portal = dashboard.getDefaultPortal();
        // Only list products with capabilities...
        portal.setProducts(portal.getProducts().stream()
            .filter(product -> !product.getCapabilities().isEmpty())
            .collect(Collectors.toSet()));
        BoatLegacyPortal portalDto = dashboardMapper.mapPortal(portal);
        portalDto.setReleases(dashboardMapper.mapReleases(portal));
        portalDto.setCapabilities(dashboardMapper.mapCapabilities(portal, allEnabledTags));
        return ResponseEntity.ok(portalDto);
    }

    @GetMapping("/portals")
    public ResponseEntity<List<BoatPortal>> getPortals() {

        List<BoatPortal> portals = boatPortalRepository.findAll().stream()
            .map(this::mapPortal)
            .collect(Collectors.toList());

        return ResponseEntity.ok(portals);
    }

    @GetMapping("/portals/{portalKey}/products")
    public ResponseEntity<List<BoatProduct>> getPortalProducts(@PathVariable String portalKey) {

        Portal portal = boatPortalRepository.findByKey(portalKey)
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        List<Product> products = boatProductRepository.findAllByPortal(portal);

        List<BoatProduct> boatProductDashboards = products.stream().map(
            this::mapProduct).collect(Collectors.toList());

        return ResponseEntity.ok(boatProductDashboards);
    }

    @GetMapping("/portals/{portalKey}/lint-rules")
    public ResponseEntity<List<BoatLintRule>> getPortalLintRules(@PathVariable String portalKey) {

        Portal portal = boatPortalRepository.findByKey(portalKey)
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        List<BoatLintRule> portalLintRules = portal.getPortalLintRules().stream().map(dashboardMapper::mapPortalLintRule).collect(Collectors.toList());
        return ResponseEntity.ok(portalLintRules);
    }

    @PostMapping("/portals/{portalKey}/lint-rules/{portalLintRuleId}")
    public ResponseEntity<Void> updatePortalLintRule(@PathVariable String portalKey, @PathVariable String portalLintRuleId,  @RequestBody BoatLintRule boatLintRule) {

        Portal portal = boatPortalRepository.findByKey(portalKey)
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        PortalLintRule portalLintRule = portalLintRuleRepository.findById(Long.valueOf(portalLintRuleId))
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        portalLintRule.setEnabled(boatLintRule.isEnabled());
        portalLintRuleRepository.save(portalLintRule);

        return ResponseEntity.accepted().build();
    }


    @GetMapping("/portals/{portalKey}/products/{productKey}/capabilities")
    public ResponseEntity<List<BoatCapability>> getPortalProducts(@PathVariable String portalKey, @PathVariable String productKey, Pageable pageable) {

        Product product = getProduct(portalKey, productKey);

        Page<Capability> capabilities = boatCapabilityRepository.findByProduct(product, pageable);
        Page<BoatCapability> page = capabilities.map(this::mapCapability);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/portals/{portalKey}/products/{productKey}/services")
    public ResponseEntity<List<BoatService>> getPortalServices(@PathVariable String portalKey, @PathVariable String productKey, Pageable pageable) {
        Product product = getProduct(portalKey, productKey);

        Page<ServiceDefinition> services = boatServiceRepository.findByCapabilityProduct(product, pageable);

        Page<BoatService> page = services.map(this::mapService);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/portals/{portalKey}/products/{productKey}/specs")
    public ResponseEntity<List<BoatSpec>> getPortalSpecs(@PathVariable String portalKey, @PathVariable String productKey, Pageable pageable) {
        Product product = getProduct(portalKey, productKey);

        Page<Spec> services = boatSpecRepository.findAllByCapabilityProduct(product, pageable);

        Page<BoatSpec> page = services.map(this::mapSpec);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    private Product getProduct(@PathVariable String portalKey, @PathVariable String productKey) {
        return boatProductRepository.findByKeyAndPortalKey(productKey, portalKey)
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    private BoatSpec mapSpec(Spec spec) {
        BoatSpec boatSpec = dashboardMapper.mapBoatSpec(spec);
        boatSpec.setStatistics(boatStatisticsCollector.collect(spec));
        return boatSpec;
    }


    @GetMapping("/portals/{portalKey}/products/{productKey}/specs/{specId}/lint-report")
    public ResponseEntity<BoatLintReport> getLintReportForSpec(@PathVariable String portalKey, @PathVariable String productKey, @PathVariable String specId) {
        Product product = getProduct(portalKey, productKey);

        Spec spec = boatSpecRepository.findById(Long.valueOf(specId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        LintReport specReport = Optional.ofNullable(spec.getLintReport()).orElseGet(() -> boatSpecLinter.lint(spec));


        boatSpecLinter.getApiValidator(spec);


        Map<Severity, Integer> severityIntegerMap = getSeverityOrder();

        BoatLintReport lintReport = dashboardMapper.mapReport(specReport);

        lintReport.getViolations()
            .sort(Comparator.comparing(boatViolation -> boatViolation.getRule().getRuleId()));

        lintReport.getViolations()
            .sort(Comparator.comparing(
                BoatViolation::getSeverity,
                Comparator.comparingInt(severityIntegerMap::get)));

        return ResponseEntity.ok(lintReport);
    }

    private Map<Severity, Integer> getSeverityOrder() {
        if(severityOrder == null) {
            severityOrder = new HashMap<>();
            for (int i = 0; i < Severity.values().length; i++) {
                severityOrder.put(Severity.values()[i], i);
            }
        }

        return severityOrder;

    }


    private BoatService mapService(ServiceDefinition serviceDefinition) {
        BoatService boatService = dashboardMapper.mapBoatService(serviceDefinition);
        boatService.setStatistics(boatStatisticsCollector.collect(serviceDefinition));
        return boatService;
    }


    @GetMapping("dashboard")
    @Cacheable(VIEWS)
    public ResponseEntity<List<BoatPortalDashboard>> getDashboard() {

        List<BoatPortalDashboard> portals = boatPortalRepository.findAll().stream()
            .flatMap(portal -> portal.getProducts().stream().map(this::mapPortalDashboard))
            .collect(Collectors.toList());

        return ResponseEntity.ok(portals);
    }

    @GetMapping("dashboard/{projectKey}/{productKey}")
    @Cacheable(VIEWS)
    public ResponseEntity<BoatProduct> getProductDashboard(@PathVariable String projectKey, @PathVariable String productKey) {


        Product product = getProduct(projectKey, productKey);

        BoatProduct boatProduct = mapProduct(product);

        return ResponseEntity.ok(boatProduct);
    }

    @NotNull
    private BoatPortal mapPortal(Portal portal) {
        return dashboardMapper.mapBoatPortal(portal);
    }

    @NotNull
    private BoatPortalDashboard mapPortalDashboard(com.backbase.oss.boat.bay.domain.Product product) {

        BoatPortalDashboard portalDto = dashboardMapper.mapPortal(product.getPortal(), product);
        boatLintReportRepository.findDistinctFirstBySpecProductOrderByLintedOn(product)
            .ifPresent(lintReport -> portalDto.setLastLintReport(dashboardMapper.mapReportWithoutViolations(lintReport)));

        portalDto.setNumberOfCapabilities(boatCapabilityRepository.countByProduct(product));
        portalDto.setNumberOfServices(boatServiceRepository.countByCapabilityProduct(product));
        portalDto.setStatistics(boatStatisticsCollector.collect(product));
        return portalDto;
    }

    @NotNull
    private BoatProduct mapProduct(Product product) {

        BoatProduct boatProduct = dashboardMapper.mapBoatProduct(product);
        boatLintReportRepository.findDistinctFirstBySpecProductOrderByLintedOn(product)
            .ifPresent(lintReport -> boatProduct.setLastLintReport(dashboardMapper.mapReportWithoutViolations(lintReport)));
        boatProduct.setStatistics(boatStatisticsCollector.collect(product));
        boatProduct.setPortalName(product.getPortal().getName());
        boatProduct.setPortalKey(product.getPortal().getKey());
        return boatProduct;
    }

    @NotNull
    private BoatCapability mapCapability(Capability capability) {

        BoatCapability boatCapability = dashboardMapper.mapBoatCapability(capability);
        boatLintReportRepository.findDistinctFirstBySpecServiceDefinitionCapability(capability)
            .ifPresent(lintReport -> boatCapability.setLastLintReport(dashboardMapper.mapReportWithoutViolations(lintReport)));
        boatCapability.setStatistics(boatStatisticsCollector.collect(capability));
        return boatCapability;
    }

}
