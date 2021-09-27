package com.backbase.oss.boat.bay.web.views.dashboard.controller;

import com.backbase.oss.boat.bay.api.DashboardApi;
import com.backbase.oss.boat.bay.config.BoatCacheManager;
import com.backbase.oss.boat.bay.domain.*;
import com.backbase.oss.boat.bay.model.*;
import com.backbase.oss.boat.bay.repository.*;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.bay.service.statistics.BoatStatisticsCollector;
import com.backbase.oss.boat.bay.util.TagsDiff;
import com.backbase.oss.boat.bay.web.views.dashboard.diff.DiffReportRenderer;
import com.backbase.oss.boat.bay.web.views.dashboard.mapper.BoatDashboardMapper;
import io.swagger.v3.oas.models.PathItem;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.openapitools.openapidiff.core.OpenApiCompare;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoatDashboardController implements DashboardApi {

    public static final String VIEWS = "views";
    public static final String TAGS = "tags";

    private final BoatPortalRepository boatPortalRepository;
    private final BoatProductRepository boatProductRepository;
    private final BoatCapabilityRepository boatCapabilityRepository;
    private final BoatServiceRepository boatServiceRepository;
    private final BoatSpecRepository boatSpecRepository;
    private final LintRuleRepository lintRuleRepository;
    private final BoatLintReportRepository boatLintReportRepository;
    private final BoatProductReleaseRepository boatProductReleaseRepository;
    private final BoatDashboardMapper dashboardMapper;
    private final BoatTagRepository tagRepository;

    private final BoatStatisticsCollector boatStatisticsCollector;
    private final BoatSpecQuerySpecs boatSpecQuerySpecs;
    private final BoatSpecLinter boatSpecLinter;
    private static Map<Severity, Integer> severityOrder;

    public List<String> getAllEnabledTags() {
        return tagRepository.findAll(Example.of(new Tag().hide(false))).stream().map(Tag::getName).collect(Collectors.toList());
    }

    public ResponseEntity<List<BoatPortal>> getPortals() {
        List<BoatPortal> portals = boatPortalRepository.findAll().stream().map(this::mapPortal).collect(Collectors.toList());

        return ResponseEntity.ok(portals);
    }

    public ResponseEntity<List<BoatLintRule>> getPortalLintRules(String portalKey) {
        Portal portal = getPortal(portalKey);

        List<BoatLintRule> portalLintRules = portal
            .getLintRules()
            .stream()
            .map(dashboardMapper::mapPortalLintRule)
            .collect(Collectors.toList());
        return ResponseEntity.ok(portalLintRules);
    }

    @Cacheable(value = BoatCacheManager.PORTAL_PRODUCT)
    public ResponseEntity<BoatProduct> getPortalProduct(String portalKey, String productKey) {
        Product product = getProduct(portalKey, productKey);
        return ResponseEntity.ok(mapProduct(product));
    }

    @Cacheable(value = BoatCacheManager.PORTAL_PRODUCT)
    public ResponseEntity<List<BoatProduct>> getPortalProducts(String portalKey, Integer page, Integer size, List<String> sort) {
        Portal portal = getPortal(portalKey);
        Page<Product> allByPortal = boatProductRepository.findAllByPortal(portal, getPageable(page, size, sort));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), allByPortal);
        List<BoatProduct> products = allByPortal.stream().map(this::mapProduct).collect(Collectors.toList());
        return ResponseEntity.ok().headers(headers).body(products);
    }

    @Override
    public ResponseEntity<List<BoatService>> getPortalServices(
        String portalKey,
        String productKey,
        Integer page,
        Integer size,
        List<String> sort
    ) {
        Product product = getProduct(portalKey, productKey);

        Page<ServiceDefinition> byCapabilityProduct = boatServiceRepository.findByCapabilityProduct(product, getPageable(page, size, sort));

        List<BoatService> services = byCapabilityProduct.stream().map(this::mapService).collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            byCapabilityProduct
        );
        return ResponseEntity.ok().headers(headers).body(services);
    }

    private Portal getPortal(String portalKey) {
        return boatPortalRepository.findByKey(portalKey).orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public ResponseEntity<Void> updatePortalLintRule(String portalKey, String lintRuleId, @RequestBody BoatLintRule boatLintRule) {
        Portal portal = getPortal(portalKey);

        LintRule lintRule = lintRuleRepository
            .findById(Long.valueOf(lintRuleId))
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        boatSpecRepository.findAll(Example.of(new Spec().portal(portal))).forEach(boatSpecLinter::scheduleLintJob);

        log.info("Updating lint rule: {}", lintRule);
        lintRule.setEnabled(boatLintRule.getEnabled());
        lintRuleRepository.save(lintRule);

        return ResponseEntity.accepted().build();
    }

    @Cacheable(value = BoatCacheManager.PRODUCT_RELEASES)
    public ResponseEntity<List<BoatProductRelease>> getProductReleases(String portalKey, String productKey) {
        Product product = getProduct(portalKey, productKey);

        List<ProductRelease> productRelease = boatProductReleaseRepository.findAllByProductOrderByReleaseDate(product);
        List<BoatProductRelease> boatProductReleases = productRelease
            .stream()
            .map(dashboardMapper::mapBoatProductRelease)
            .collect(Collectors.toList());

        return ResponseEntity.ok(boatProductReleases);
    }

    @Cacheable(value = BoatCacheManager.PRODUCT_SPECS)
    public ResponseEntity<List<BoatSpec>> getProductReleaseSpecs(String portalKey, String productKey, String releaseKey) {
        Product product = getProduct(portalKey, productKey);
        ProductRelease productRelease = boatProductReleaseRepository
            .findByProductAndKey(product, releaseKey)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<BoatSpec> specs = productRelease.getSpecs().stream().map(this::mapSpec).collect(Collectors.toList());
        return ResponseEntity.ok(specs);
    }

    @Cacheable(value = BoatCacheManager.PRODUCT_TAGS)
    public ResponseEntity<List<BoatTag>> getProductTags(String portalKey, String productKey) {
        Product product = getProduct(portalKey, productKey);
        List<Spec> specsForProduct = getSpecsForProduct(product);

        Map<Tag, Integer> tagCount = new HashMap<>();

        for (Spec spec : specsForProduct) {
            spec
                .getTags()
                .forEach(
                    tag -> {
                        int occurences = tagCount.getOrDefault(tag, 0) + 1;
                        tagCount.put(tag, occurences);
                    }
                );
        }
        List<BoatTag> tags = new ArrayList<>();
        tagCount.forEach(
            (tag, integer) -> {
                BoatTag boatTag = dashboardMapper.mapTag(tag);
                boatTag.setNumberOfOccurrences(integer);
                tags.add(boatTag);
            }
        );

        return ResponseEntity.ok(tags);
    }

    private List<Spec> getSpecsForProduct(Product product) {
        return boatSpecRepository.findAll(boatSpecQuerySpecs.hasProduct(product.getId()));
    }

    @Override
    @Cacheable(value = BoatCacheManager.PRODUCT_SPECS)
    public ResponseEntity<List<BoatSpec>> getPortalSpecs(
        String portalKey,
        String productKey,
        Integer page,
        Integer size,
        List<String> sort,
        List<String> capabilityId,
        String productReleaseId,
        List<String> serviceId,
        String grade,
        Boolean backwardsCompatible,
        Boolean changed
    ) {
        Product product = getProduct(portalKey, productKey);

        Specification<Spec> specification = boatSpecQuerySpecs.hasProduct(product.getId());

        if (productReleaseId != null) {
            specification = specification.and(boatSpecQuerySpecs.inProductRelease(productReleaseId));
        }

        if (capabilityId != null) {
            Specification<Spec> capabilitySpecification = boatSpecQuerySpecs.hasCapabilityKey(capabilityId.get(0));
            for (int i = 1; i < capabilityId.size(); i++) {
                capabilitySpecification = capabilitySpecification.or(boatSpecQuerySpecs.hasCapabilityKey(capabilityId.get(i)));
            }
            specification = specification.and(capabilitySpecification);
        }

        if (serviceId != null) {
            Specification<Spec> serviceDefinitionSpecification = boatSpecQuerySpecs.hasServiceDefinition(serviceId.get(0));
            for (int i = 1; i < serviceId.size(); i++) {
                serviceDefinitionSpecification =
                    serviceDefinitionSpecification.or(boatSpecQuerySpecs.hasServiceDefinition(serviceId.get(i)));
            }
            specification = specification.and(serviceDefinitionSpecification);
        }

        Page<Spec> all = boatSpecRepository.findAll(specification, getPageable(page, size, sort));
        Page<BoatSpec> boatSpecs = all.map(this::mapSpec);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), boatSpecs);
        return ResponseEntity.ok().headers(headers).body(boatSpecs.getContent());
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public ResponseEntity<BoatLintReport> getLintReportForSpec(String portalKey, String productKey, BigDecimal specId, Boolean refresh) {
        Product product = getProduct(portalKey, productKey);

        log.info("Get lint report for spec: {} in product: {}", specId, product.getName());

        Spec spec = boatSpecRepository.findById(specId.longValue()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LintReport specReport = boatSpecLinter.lint(spec);

        Map<Severity, Integer> severityIntegerMap = getSeverityOrder();

        BoatLintReport lintReport = dashboardMapper.mapReport(specReport);
        lintReport.setOpenApi(spec.getOpenApi());

        lintReport.getViolations().sort(Comparator.comparing(boatViolation -> boatViolation.getRule().getRuleId()));

        lintReport.getViolations().sort(Comparator.comparing(BoatViolation::getSeverity, Comparator.comparingInt(severityIntegerMap::get)));

        return ResponseEntity.ok(lintReport);
    }

    public ResponseEntity<Resource> downloadSpec(
        String portalKey,
        String productKey,
        String capabilityKey,
        String serviceKey,
        String specKey,
        String version
    ) {
        Spec spec = boatSpecRepository
            .findByPortalKeyAndProductKeyAndCapabilityKeyAndServiceDefinitionKeyAndKeyAndVersion(
                portalKey,
                productKey,
                capabilityKey,
                serviceKey,
                specKey,
                version
            )
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + spec.getFilename());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        ByteArrayResource openApiBody = new ByteArrayResource(spec.getOpenApi().getBytes(StandardCharsets.UTF_8));

        return ResponseEntity
            .ok()
            .headers(header)
            .contentLength(spec.getOpenApi().length())
            .contentType(MediaType.valueOf("application/vnd.oai.openapi"))
            .body(openApiBody);
    }

    @Override
    public ResponseEntity<BoatSpec> getSpec(
        String portalKey,
        String productKey,
        String capabilityKey,
        String serviceKey,
        String specKey,
        String version
    ) {
        Spec spec = boatSpecRepository
            .findByPortalKeyAndProductKeyAndCapabilityKeyAndServiceDefinitionKeyAndKeyAndVersion(
                portalKey,
                productKey,
                capabilityKey,
                serviceKey,
                specKey,
                version
            )
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        BoatSpec body = dashboardMapper.mapBoatSpec(spec);
        body.setOpenApi(spec.getOpenApi());

        return ResponseEntity.ok(body);
    }

    public ResponseEntity<String> getDiffReport(String portalKey, String productKey, Integer spec1Id, Integer spec2Id) {
        ChangedOpenApi changedOpenApi = getChangedOpenApi(portalKey, productKey, spec1Id, spec2Id);
        Map<String, Map<PathItem.HttpMethod, List<String>>> changedTags = TagsDiff.findMissingTags(changedOpenApi);
        DiffReportRenderer htmlRender = new DiffReportRenderer();

        return ResponseEntity.ok(htmlRender.render(changedOpenApi, changedTags));
    }

    @Override
    @Cacheable(value = BoatCacheManager.PRODUCT_CAPABILITIES)
    public ResponseEntity<List<BoatCapability>> getPortalCapabilities(
        String portalKey,
        String productKey,
        Integer page,
        Integer size,
        List<String> sort
    ) {
        Product product = getProduct(portalKey, productKey);

        Page<Capability> capabilities = boatCapabilityRepository.findByProduct(product, getPageable(page, size, sort));
        Page<BoatCapability> boatCapabilities = capabilities.map(this::mapCapability);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            boatCapabilities
        );
        return ResponseEntity.ok().headers(headers).body(boatCapabilities.getContent());
    }

    @NotNull
    private ChangedOpenApi getChangedOpenApi(String portalKey, String productKey, Integer spec1Id, Integer spec2Id) {
        Product product = getProduct(portalKey, productKey);

        Spec spec1 = boatSpecRepository.findById(Long.valueOf(spec1Id)).orElseThrow();
        Spec spec2 = boatSpecRepository.findById(Long.valueOf(spec2Id)).orElseThrow();

        log.info("Creating diff reports from specs: {} and {} in {}", spec1.getName(), spec2.getName(), product.getName());

        ChangedOpenApi changedOpenApi;
        try {
            changedOpenApi = OpenApiCompare.fromContents(spec1.getOpenApi(), spec2.getOpenApi());
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
        return changedOpenApi;
    }

    private Product getProduct(String portalKey, String productKey) {
        return boatProductRepository
            .findByKeyAndPortalKey(productKey, portalKey)
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    private BoatSpec mapSpec(Spec spec) {
        BoatSpec boatSpec = dashboardMapper.mapBoatSpec(spec);
        boatSpec.setStatistics(boatStatisticsCollector.collect(spec));
        return boatSpec;
    }

    private static Map<Severity, Integer> getSeverityOrder() {
        if (severityOrder == null) {
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
        BoatPortal boatPortal = dashboardMapper.mapBoatPortal(portal);
        boatPortal.setNumberOfCapabilities(boatCapabilityRepository.countByProductPortal(portal));
        boatPortal.setNumberOfServices(boatServiceRepository.countByCapabilityProductPortal(portal));
        boatLintReportRepository
            .findDistinctFirstBySpecProductPortalOrderByLintedOn(portal)
            .ifPresent(lintReport -> boatPortal.setLastLintReport(dashboardMapper.mapReportWithoutViolations(lintReport)));
        return boatPortal;
    }

    @NotNull
    private BoatProduct mapProduct(Product product) {
        BoatProduct boatProduct = dashboardMapper.mapBoatProduct(product);
        boatLintReportRepository
            .findDistinctFirstBySpecProductOrderByLintedOn(product)
            .ifPresent(lintReport -> boatProduct.setLastLintReport(dashboardMapper.mapReportWithoutViolations(lintReport)));
        boatProduct.setStatistics(boatStatisticsCollector.collect(product));
        boatProduct.setPortalName(product.getPortal().getName());
        boatProduct.setPortalKey(product.getPortal().getKey());
        return boatProduct;
    }

    @NotNull
    private BoatCapability mapCapability(Capability capability) {
        BoatCapability boatCapability = dashboardMapper.mapBoatCapability(capability);
        boatLintReportRepository
            .findDistinctFirstBySpecServiceDefinitionCapability(capability)
            .ifPresent(lintReport -> boatCapability.setLastLintReport(dashboardMapper.mapReportWithoutViolations(lintReport)));
        boatCapability.setStatistics(boatStatisticsCollector.collect(capability));
        return boatCapability;
    }

    private Pageable getPageable(Integer page, Integer size, List<String> sort) {
        if (page == null || size == null) {
            return PageRequest.of(1, 10);
        }
        if (sort == null) {
            return PageRequest.of(page, size);
        } else {
            return PageRequest.of(page, size, parseSort(sort));
        }
    }

    private Sort parseSort(List<String> sortParameters) {
        List<Sort.Order> orders = sortParameters
            .stream()
            .map(this::parseSortParameter)
            .filter(order -> !order.getProperty().equals("undefined"))
            .collect(Collectors.toList());

        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }

    @NotNull
    private Sort.Order parseSortParameter(String sortParameter) {
        String[] split = sortParameter.split(",");
        if (split.length == 1) {
            return new Sort.Order(Sort.Direction.ASC, split[0]);
        } else {
            return new Sort.Order(getSortDirection(split[1]), split[0]);
        }
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }
}
