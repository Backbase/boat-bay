package com.backbase.oss.boat.bay.web.views.dashboard.controller;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.PortalLintRule;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
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
import com.backbase.oss.boat.bay.repository.extended.BoatProductReleaseRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatProductRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatServiceRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatSpecQuerySpecs;
import com.backbase.oss.boat.bay.repository.extended.BoatSpecRepository;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.bay.service.statistics.BoatStatisticsCollector;
import com.backbase.oss.boat.bay.web.views.dashboard.mapper.BoatDashboardMapper;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatCapability;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatLintReport;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatLintRule;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatPortal;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatPortalDashboard;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatProduct;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatProductRelease;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatService;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatSpec;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatViolation;
import io.github.jhipster.web.util.PaginationUtil;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final BoatProductReleaseRepository boatProductReleaseRepository;
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



    @GetMapping("/portals")
    public ResponseEntity<List<BoatPortal>> getPortals() {

        List<BoatPortal> portals = boatPortalRepository.findAll().stream()
            .map(this::mapPortal)
            .collect(Collectors.toList());

        return ResponseEntity.ok(portals);
    }

    @GetMapping("/portals/{portalKey}/products")
    public ResponseEntity<List<BoatProduct>> getPortalProducts(@PathVariable String portalKey) {

        Portal portal = getPortal(portalKey);

        List<Product> products = boatProductRepository.findAllByPortal(portal);

        List<BoatProduct> boatProductDashboards = products.stream().map(
            this::mapProduct).collect(Collectors.toList());

        return ResponseEntity.ok(boatProductDashboards);
    }

    @GetMapping("/portals/{portalKey}/lint-rules")
    public ResponseEntity<List<BoatLintRule>> getPortalLintRules(@PathVariable String portalKey) {

        Portal portal = getPortal(portalKey);

        List<BoatLintRule> portalLintRules = portal.getPortalLintRules().stream().map(dashboardMapper::mapPortalLintRule).collect(Collectors.toList());
        return ResponseEntity.ok(portalLintRules);
    }

    private Portal getPortal(@PathVariable String portalKey) {
        return boatPortalRepository.findByKey(portalKey)
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping("/portals/{portalKey}/lint-rules/{portalLintRuleId}")
    public ResponseEntity<Void> updatePortalLintRule(@PathVariable String portalKey, @PathVariable String portalLintRuleId,  @RequestBody BoatLintRule boatLintRule) {

        Portal portal = getPortal(portalKey);

        PortalLintRule portalLintRule = portalLintRuleRepository.findById(Long.valueOf(portalLintRuleId))
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        portalLintRule.setEnabled(boatLintRule.isEnabled());
        portalLintRuleRepository.save(portalLintRule);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/portals/{portalKey}/products/{productKey}/releases")
    public ResponseEntity<List<BoatProductRelease>> getProductReleases(@PathVariable String portalKey, @PathVariable String productKey) {

        Product product = getProduct(portalKey, productKey);

        List<ProductRelease> productRelease = boatProductReleaseRepository.findAllByProduct(product);
        List<BoatProductRelease> boatProductReleases = productRelease.stream().map(dashboardMapper::mapBoatProductRelease).collect(Collectors.toList());

        return ResponseEntity.ok(boatProductReleases);
    }

    @GetMapping("/portals/{portalKey}/products/{productKey}/releases/{releaseKey}/specs")
    public ResponseEntity<List<BoatSpec>> getProductReleaseSpecs(@PathVariable String portalKey, @PathVariable String productKey, @PathVariable String releaseKey) {

        Product product = getProduct(portalKey, productKey);
        ProductRelease productRelease = boatProductReleaseRepository.findByProductAndKey(product, releaseKey).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<BoatSpec> specs = productRelease.getSpecs().stream().map(this::mapSpec).collect(Collectors.toList());
        return ResponseEntity.ok(specs);
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

    private final BoatSpecQuerySpecs boatSpecQuerySpecs;

    @GetMapping("/portals/{portalKey}/products/{productKey}/specs")
    public ResponseEntity<List<BoatSpec>> getPortalSpecs(@PathVariable String portalKey, @PathVariable String productKey,
                                                         @RequestParam(required = false) String capabilityId,
                                                         @RequestParam(required = false) String productReleaseId,
                                                         @RequestParam(required = false) String serviceId,
                                                         @RequestParam(required = false) String grade,
                                                         @RequestParam(required = false) boolean backwardsCompatible,
                                                         @RequestParam(required = false) boolean changed,
                                                         Pageable pageable) {
        Product product = getProduct(portalKey, productKey);

        log.info("Should be the same results");
//        Page<Spec> services = boatSpecRepository.findAllByCapabilityProduct(product, pageable);
        Specification<Spec> specification = boatSpecQuerySpecs.hasProduct(product.getId());

        if(productReleaseId != null) {
            specification = specification.and(boatSpecQuerySpecs.inProductRelease(productReleaseId));
        }

        if(capabilityId != null) {
            specification = specification.and(boatSpecQuerySpecs.hasCapability(capabilityId));
        }

        Page<Spec> all = boatSpecRepository.findAll(specification, pageable);

        Page<BoatSpec> page = all.map(this::mapSpec);

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
    public ResponseEntity<BoatLintReport> getLintReportForSpec(@PathVariable String portalKey, @PathVariable String productKey, @PathVariable String specId, @RequestParam(required = false) Boolean refresh) {
        Product product = getProduct(portalKey, productKey);

        Spec spec = boatSpecRepository.findById(Long.valueOf(specId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LintReport specReport;
        if(refresh) {
            specReport = boatSpecLinter.lint(spec);
        } else {
            specReport = Optional.ofNullable(spec.getLintReport()).orElseGet(() -> boatSpecLinter.lint(spec));
        }

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
