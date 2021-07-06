package com.backbase.oss.boat.bay.web.views.dashboard.controller;

import com.backbase.oss.boat.bay.config.BoatCacheManager;
import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.repository.BoatCapabilityRepository;
import com.backbase.oss.boat.bay.repository.BoatDashboardRepository;
import com.backbase.oss.boat.bay.repository.BoatLintReportRepository;
import com.backbase.oss.boat.bay.repository.BoatLintRuleViolationRepository;
import com.backbase.oss.boat.bay.repository.BoatPortalRepository;
import com.backbase.oss.boat.bay.repository.BoatProductReleaseRepository;
import com.backbase.oss.boat.bay.repository.BoatProductRepository;
import com.backbase.oss.boat.bay.repository.BoatServiceRepository;
import com.backbase.oss.boat.bay.repository.BoatSpecQuerySpecs;
import com.backbase.oss.boat.bay.repository.BoatSpecRepository;
import com.backbase.oss.boat.bay.repository.BoatTagRepository;
import com.backbase.oss.boat.bay.repository.LintRuleRepository;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.bay.service.model.BoatCapability;
import com.backbase.oss.boat.bay.service.model.BoatLintReport;
import com.backbase.oss.boat.bay.service.model.BoatLintRule;
import com.backbase.oss.boat.bay.service.model.BoatProduct;
import com.backbase.oss.boat.bay.service.model.BoatProductRelease;
import com.backbase.oss.boat.bay.service.model.BoatService;
import com.backbase.oss.boat.bay.service.model.BoatSpec;
import com.backbase.oss.boat.bay.service.model.BoatViolation;
import com.backbase.oss.boat.bay.service.statistics.BoatStatisticsCollector;
import com.backbase.oss.boat.bay.web.views.dashboard.diff.DiffReportRenderer;
import com.backbase.oss.boat.bay.web.views.dashboard.mapper.BoatDashboardMapper;

import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatPortal;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatPortalDashboard;

import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatTag;
import io.github.jhipster.web.util.PaginationUtil;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.openapitools.openapidiff.core.OpenApiCompare;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public static final String TAGS = "tags";

    private final BoatPortalRepository boatPortalRepository;
    private final BoatProductRepository boatProductRepository;
    private final BoatCapabilityRepository boatCapabilityRepository;
    private final BoatServiceRepository boatServiceRepository;
    private final BoatSpecRepository boatSpecRepository;
    private final LintRuleRepository lintRuleRepository;
    private final BoatLintReportRepository boatLintReportRepository;
    private final BoatLintRuleViolationRepository boatLintRuleViolationRepository;
    private final BoatProductReleaseRepository boatProductReleaseRepository;
    private final BoatDashboardRepository dashboardRepository;
    private final BoatDashboardMapper dashboardMapper;
    private final BoatTagRepository tagRepository;

    private final BoatStatisticsCollector boatStatisticsCollector;
    private final BoatSpecQuerySpecs boatSpecQuerySpecs;
    private final BoatSpecLinter boatSpecLinter;
    private Map<Severity, Integer> severityOrder;

    public List<String> getAllEnabledTags() {
        return tagRepository.findAll(Example.of(new Tag().hide(false))).stream()
            .map(Tag::getName)
            .collect(Collectors.toList());
    }

    @GetMapping("dashboard")
    @Cacheable(BoatCacheManager.PORTAL)
    @Deprecated
    public ResponseEntity<List<BoatPortalDashboard>> getDashboard() {

        List<BoatPortalDashboard> portals = boatPortalRepository.findAll().stream()
            .flatMap(portal -> portal.getProducts().stream().map(this::mapPortalDashboard))
            .collect(Collectors.toList());

        return ResponseEntity.ok(portals);
    }

    @GetMapping("dashboard/{projectKey}/{productKey}")
    @Cacheable(BoatCacheManager.PORTAL_PRODUCT)
    @Deprecated
    public ResponseEntity<BoatProduct> getProductDashboard(@PathVariable String projectKey, @PathVariable String productKey) {

        Product product = getProduct(projectKey, productKey);
        BoatProduct boatProduct = mapProduct(product);

        return ResponseEntity.ok(boatProduct);
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

        List<BoatLintRule> portalLintRules = portal.getLintRules().stream().map(dashboardMapper::mapPortalLintRule).collect(Collectors.toList());
        return ResponseEntity.ok(portalLintRules);
    }

    private Portal getPortal(@PathVariable String portalKey) {
        return boatPortalRepository.findByKey(portalKey)
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping("/portals/{portalKey}/lint-rules/{lintRuleId}")
    public ResponseEntity<Void> updatePortalLintRule(
        @PathVariable String portalKey,
        @PathVariable String lintRuleId,
        @RequestBody BoatLintRule boatLintRule) {

        Portal portal = getPortal(portalKey);

        LintRule lintRule = lintRuleRepository.findById(Long.valueOf(lintRuleId))
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        boatSpecRepository.findAll(Example.of(new Spec().portal(portal))).forEach(boatSpecLinter::scheduleLintJob);

        log.info("Updating lint rule: {}", lintRule);
        lintRule.setEnabled(boatLintRule.getEnabled());
        lintRuleRepository.save(lintRule);

        return ResponseEntity.accepted().build();
    }

    @Cacheable(BoatCacheManager.PRODUCT_RELEASES)
    @GetMapping("/portals/{portalKey}/products/{productKey}/releases")
    public ResponseEntity<List<BoatProductRelease>> getProductReleases(@PathVariable String portalKey, @PathVariable String productKey) {

        Product product = getProduct(portalKey, productKey);

        List<ProductRelease> productRelease = boatProductReleaseRepository.findAllByProductOrderByReleaseDate(product);
        List<BoatProductRelease> boatProductReleases = productRelease.stream().map(dashboardMapper::mapBoatProductRelease).collect(Collectors.toList());

        return ResponseEntity.ok(boatProductReleases);
    }

    @Cacheable(BoatCacheManager.PRODUCT_SPECS)
    @GetMapping("/portals/{portalKey}/products/{productKey}/releases/{releaseKey}/specs")
    public ResponseEntity<List<BoatSpec>> getProductReleaseSpecs(@PathVariable String portalKey, @PathVariable String productKey, @PathVariable String releaseKey) {

        Product product = getProduct(portalKey, productKey);
        ProductRelease productRelease = boatProductReleaseRepository.findByProductAndKey(product, releaseKey).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<BoatSpec> specs = productRelease.getSpecs().stream()
            .map(this::mapSpec)
            .collect(Collectors.toList());
        return ResponseEntity.ok(specs);
    }

    @GetMapping("/portals/{portalKey}/products/{productKey}/tags")
    @Cacheable(BoatCacheManager.PRODUCT_TAGS)
    public ResponseEntity<List<BoatTag>> getProductTags(@PathVariable String portalKey, @PathVariable String productKey) {

        Product product = getProduct(portalKey, productKey);
        List<Spec> specsForProduct = getSpecsForProduct(product);

        Map<Tag, Integer> tagCount = new HashMap<>();

        for (Spec spec : specsForProduct) {

            spec.getTags().forEach(tag ->  {
                int occurences = tagCount.getOrDefault(tag, 0) + 1;
                tagCount.put(tag, occurences);
            });

        }
        List<BoatTag> tags = new ArrayList<>();
        tagCount.forEach((tag, integer) -> {
            BoatTag boatTag = dashboardMapper.mapTag(tag);
            boatTag.setNumberOfOccurrences(integer);
            tags.add(boatTag);

        });

        return ResponseEntity.ok(tags);
    }

    private List<Spec> getSpecsForProduct(Product product) {
        return boatSpecRepository.findAll(boatSpecQuerySpecs.hasProduct(product.getId()));
    }


    @Cacheable(BoatCacheManager.PRODUCT_CAPABILITIES)
    @GetMapping("/portals/{portalKey}/products/{productKey}/capabilities")
    public ResponseEntity<List<BoatCapability>> getPortalProducts(@PathVariable String portalKey, @PathVariable String productKey, Pageable pageable) {

        Product product = getProduct(portalKey, productKey);

        Page<Capability> capabilities = boatCapabilityRepository.findByProduct(product, pageable);
        Page<BoatCapability> page = capabilities.map(this::mapCapability);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @Cacheable(BoatCacheManager.PRODUCT_SERVICES)
    @GetMapping("/portals/{portalKey}/products/{productKey}/capabilities/{capabilityKey}/services")
    public ResponseEntity<List<BoatService>> getPortalServices(
        @PathVariable String portalKey,
        @PathVariable String productKey,
        @PathVariable String capabilityKey) {
        Product product = getProduct(portalKey, productKey);

        Capability capability = boatCapabilityRepository.findByProductAndKey(product, capabilityKey).orElseThrow();

        List<BoatService> services = capability.getServiceDefinitions().stream().map(this::mapService).collect(Collectors.toList());

        return ResponseEntity.ok(services);
    }


    @Cacheable(BoatCacheManager.PRODUCT_SERVICES)
    @GetMapping("/portals/{portalKey}/products/{productKey}/services")
    public ResponseEntity<List<BoatService>> getPortalServices(@PathVariable String portalKey, @PathVariable String productKey, Pageable pageable) {
        Product product = getProduct(portalKey, productKey);

        Page<ServiceDefinition> services = boatServiceRepository.findByCapabilityProduct(product, pageable);

        Page<BoatService> page = services.map(this::mapService);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @Cacheable(BoatCacheManager.PRODUCT_SPECS)
    @GetMapping("/portals/{portalKey}/products/{productKey}/specs")
    public ResponseEntity<List<BoatSpec>> getPortalSpecs(@PathVariable String portalKey, @PathVariable String productKey,
                                                         @RequestParam(required = false) String[] capability,
                                                         @RequestParam(required = false) String[] service,
                                                         @RequestParam(required = false) String release,

                                                         @RequestParam(required = false) String grade,
                                                         @RequestParam(required = false) boolean backwardsCompatible,
                                                         @RequestParam(required = false) boolean changed,
                                                         Pageable pageable) {
        Product product = getProduct(portalKey, productKey);

        Specification<Spec> specification = boatSpecQuerySpecs.hasProduct(product.getId());

        if(release != null) {
            specification = specification.and(boatSpecQuerySpecs.inProductRelease(release));
        }

        if(capability != null) {
            Specification<Spec> capabilitySpecification = boatSpecQuerySpecs.hasCapabilityKey(capability[0]);
            for(int i = 1; i < capability.length; i++) {
                capabilitySpecification = capabilitySpecification.or(boatSpecQuerySpecs.hasCapabilityKey(capability[i]));
            }
            specification = specification.and(capabilitySpecification);
        }

        if(service != null) {
            Specification<Spec> serviceDefinitionSpecification = boatSpecQuerySpecs.hasServiceDefinition(service[0]);
            for(int i = 1; i < service.length; i++) {
                serviceDefinitionSpecification = serviceDefinitionSpecification.or(boatSpecQuerySpecs.hasServiceDefinition(service[i]));
            }
            specification = specification.and(serviceDefinitionSpecification);
        }

        Page<Spec> all = boatSpecRepository.findAll(specification, pageable);
        Page<BoatSpec> page = all.map(this::mapSpec);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @GetMapping("/portals/{portalKey}/products/{productKey}/specs/{specId}/lint-report")
    public ResponseEntity<BoatLintReport> getLintReportForSpec(@PathVariable String portalKey,
                                                               @PathVariable String productKey,
                                                               @PathVariable String specId,
                                                               @RequestParam(required = false) Boolean refresh) {
        Product product = getProduct(portalKey, productKey);

        log.info("Get lint report for spec: {} in product: {}", specId, product.getName());

        Spec spec = boatSpecRepository.findById(Long.valueOf(specId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LintReport specReport;
//        if(refresh != null && refresh) {
            specReport = boatSpecLinter.lint(spec);

//        } else {
//            specReport = Optional.ofNullable(spec.getLintReport()).orElseGet(() -> boatSpecLinter.lint(spec));
//        }

        Map<Severity, Integer> severityIntegerMap = getSeverityOrder();

        BoatLintReport lintReport = dashboardMapper.mapReport(specReport);
        lintReport.setOpenApi(spec.getOpenApi());

        lintReport.getViolations()
            .sort(Comparator.comparing(boatViolation -> boatViolation.getRule().getRuleId()));

        lintReport.getViolations()
            .sort(Comparator.comparing(
                BoatViolation::getSeverity,
                Comparator.comparingInt(severityIntegerMap::get)));

        return ResponseEntity.ok(lintReport);
    }

    @GetMapping("/portals/{portalKey}/products/{productKey}/capabilities/{capabilityKey}/services/{serviceKey}/specs/{specKey}/{version}/download")
    public ResponseEntity<Resource> getSpecAsOpenAPI(@PathVariable String portalKey,
                                                     @PathVariable String productKey,
                                                     @PathVariable String capabilityKey,
                                                     @PathVariable String serviceKey,
                                                     @PathVariable String specKey,
                                                     @PathVariable String version) {

        Spec spec = boatSpecRepository.findByPortalKeyAndProductKeyAndCapabilityKeyAndServiceDefinitionKeyAndKeyAndVersion(
            portalKey,productKey,capabilityKey,serviceKey,specKey,version).orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + spec.getFilename());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        ByteArrayResource openApiBody = new ByteArrayResource(spec.getOpenApi().getBytes(StandardCharsets.UTF_8));

        return ResponseEntity.ok()
            .headers(header)
            .contentLength(spec.getOpenApi().length())
            .contentType(MediaType.valueOf("application/vnd.oai.openapi"))
            .body(openApiBody);

    }

    @GetMapping("/portals/{portalKey}/products/{productKey}/capabilities/{capabilityKey}/services/{serviceKey}/specs/{specKey}/{version}")
    public ResponseEntity<BoatSpec> downloadSpec(@PathVariable String portalKey,
                                                     @PathVariable String productKey,
                                                     @PathVariable String capabilityKey,
                                                     @PathVariable String serviceKey,
                                                     @PathVariable String specKey,
                                                     @PathVariable String version) {

        Spec spec = boatSpecRepository.findByPortalKeyAndProductKeyAndCapabilityKeyAndServiceDefinitionKeyAndKeyAndVersion(
            portalKey,productKey,capabilityKey,serviceKey,specKey,version).orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        BoatSpec body = dashboardMapper.mapBoatSpec(spec);
        body.setOpenApi(spec.getOpenApi());

        return ResponseEntity.ok(body);

    }

    @GetMapping("/portals/{portalKey}/products/{productKey}/diff-report")
    public ResponseEntity<ChangedOpenApi> getLintReportForSpec(@PathVariable String portalKey,
                                                               @PathVariable String productKey,
                                                               @RequestParam String spec1Id,
                                                               @RequestParam String spec2Id) {
        ChangedOpenApi changedOpenApi = getChangedOpenApi(portalKey, productKey, spec1Id, spec2Id);


        return ResponseEntity.ok(changedOpenApi);
    }

    @GetMapping(value = "/portals/{portalKey}/products/{productKey}/diff-report.html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getLintReportForSpecAsHtml(@PathVariable String portalKey,
                                                               @PathVariable String productKey,
                                                               @RequestParam String spec1Id,
                                                               @RequestParam String spec2Id) {
        ChangedOpenApi changedOpenApi = getChangedOpenApi(portalKey, productKey, spec1Id, spec2Id);
        DiffReportRenderer htmlRender = new DiffReportRenderer();

        return ResponseEntity.ok(htmlRender.render(changedOpenApi));
    }

    @NotNull
    private ChangedOpenApi getChangedOpenApi(@PathVariable String portalKey, @PathVariable String productKey, @RequestParam String spec1Id, @RequestParam String spec2Id) {
        Product product = getProduct(portalKey, productKey);

        Spec spec1 = boatSpecRepository.findById(Long.valueOf(spec1Id)).orElseThrow();
        Spec spec2 = boatSpecRepository.findById(Long.valueOf(spec2Id)).orElseThrow();

        log.info("Creating diff reports from specs: {} and {} in {}", spec1.getName(), spec2.getName(), product.getName());

        ChangedOpenApi changedOpenApi = null;
        try {
            changedOpenApi = OpenApiCompare.fromContents(spec1.getOpenApi(), spec2.getOpenApi());
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
        return changedOpenApi;
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
