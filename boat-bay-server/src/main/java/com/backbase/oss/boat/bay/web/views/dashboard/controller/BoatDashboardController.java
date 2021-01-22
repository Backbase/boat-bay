package com.backbase.oss.boat.bay.web.views.dashboard.controller;

import com.backbase.oss.boat.ExportException;
import com.backbase.oss.boat.bay.domain.*;
import com.backbase.oss.boat.bay.repository.LintRuleRepository;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import com.backbase.oss.boat.bay.repository.SpecRepository;
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
import com.backbase.oss.boat.bay.service.api.ApiApi;
import com.backbase.oss.boat.bay.service.export.ExportOptions;
import com.backbase.oss.boat.bay.service.export.ExportType;
import com.backbase.oss.boat.bay.service.export.impl.FileSystemExporter;
import com.backbase.oss.boat.bay.repository.extended.BoatTagRepository;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.bay.service.model.*;
import com.backbase.oss.boat.bay.service.source.SpecSourceResolver;
import com.backbase.oss.boat.bay.service.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.service.statistics.BoatStatisticsCollector;
import com.backbase.oss.boat.bay.web.views.dashboard.config.BoatCacheManager;
import com.backbase.oss.boat.bay.web.rest.SpecResource;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;

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
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatTag;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatViolation;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.zalando.zally.rule.api.Severity;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/boat/")
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoatDashboardController implements ApiApi {

    public static final String VIEWS = "views";
    public static final String TAGS = "tags";
    /////////////////////////////////////////////
    // change to boat repos
    private final SourceRepository sourceRepository;
    private final SpecSourceResolver specSourceResolver;
    private final FileSystemExporter fileSystemExporter;
    private final SpecRepository specRepository;
    private final BoatDashboardMapper lintReportMapper;
    ///////////////////////////////////////////////////

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

        List<BoatSpec> specs = productRelease.getSpecs().stream().map(this::mapSpec).collect(Collectors.toList());
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
    public ResponseEntity<List<BoatCapability>> getPortalCapabilities(@PathVariable String portalKey, @PathVariable String productKey, Pageable pageable) {

        Product product = getProduct(portalKey, productKey);

        Page<Capability> capabilities = boatCapabilityRepository.findByProduct(product, pageable);
        Page<BoatCapability> page = capabilities.map(this::mapCapability);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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



    private final BoatSpecQuerySpecs boatSpecQuerySpecs;

    @Cacheable(BoatCacheManager.PRODUCT_SPECS)
    @GetMapping("/portals/{portalKey}/products/{productKey}/specs")
    public ResponseEntity<List<BoatSpec>> getPortalSpecs(@PathVariable String productKey, @PathVariable String portalKey,
                                                         Pageable pageable,
                                                         @RequestParam(required = false) String capabilityId,
                                                         @RequestParam(required = false) String productReleaseId,
                                                         @RequestParam(required = false) String serviceId,
                                                         @RequestParam(required = false) String grade,
                                                         @RequestParam(required = false) Boolean backwardsCompatible,
                                                         @RequestParam(required = false) Boolean changed ) {
        Product product = getProduct(portalKey, productKey);

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


    @GetMapping("/portals/{portalKey}/products/{productKey}/specs/{specId}/lint-report")
    public ResponseEntity<BoatLintReport> getLintReportForSpec(@PathVariable String portalKey, @PathVariable String productKey, @PathVariable String specId, @RequestParam(required = false) Boolean refresh) {
        Product product = getProduct(portalKey, productKey);

        log.info("Get lint report for spec: {} in product: {}", specId, product.getName());

        Spec spec = boatSpecRepository.findById(Long.valueOf(specId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LintReport specReport;
        if(refresh != null && refresh) {
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

    @PutMapping("boat-maven-plugin/{sourceId}/upload")
    public ResponseEntity<List<BoatLintReport>> uploadSpec(@PathVariable String sourceId, @Valid @RequestBody UploadRequestBody requestBody) {

        Source source = sourceRepository.findById(Long.parseLong(sourceId)).orElseThrow(() -> new BadRequestAlertException("Invalid source, source Id does not exist", "SOURCE", "sourceIdInvalid"));

        List<UploadSpec> requestSpecs = requestBody.getSpecs();

        if (requestBody.getProjectId().isEmpty()
                || requestBody.getVersion().isEmpty()
                || requestBody.getArtifactId().isEmpty()) {
            throw new BadRequestAlertException("Invalid Request body missing attributes", "UploadSpec", "attributeempty");
        }


        List<Spec> specs = new ArrayList<>();


        for (UploadSpec uploadSpec : requestSpecs) {

            Spec spec = mapSpec(uploadSpec);

            log.debug("REST request to upload : {}", spec.getKey());

            if (spec.getFilename().isEmpty())
                spec.setFilename(requestBody.getArtifactId() + "-api-v" + requestBody.getVersion() + ".yaml");

            if (spec.getOpenApi().isEmpty()
                    || spec.getKey().isEmpty())
                throw new BadRequestAlertException("Invalid spec with an empty api, key, or file name", "UploadSpec", "attributeempty");

            spec.setPortal(source.getPortal());
            spec.setProduct(source.getProduct());
            spec.setVersion(requestBody.getVersion().replaceAll("(\\.|-)(\\w{2,})+" , ""));
            spec.setSource(source);
            spec.setSourceName(spec.getFilename());
            spec.setCreatedBy("MavenPluginUpload");
            spec.setCreatedOn(ZonedDateTime.now());
            spec.setSourcePath("/" +
                    requestBody.getProjectId().substring(
                            requestBody.getProjectId().lastIndexOf(".")+1)
                    + "/" +
                    spec.getFilename());

            Spec match = new Spec().key(spec.getKey()).name(spec.getName());

            Optional<Spec> duplicate = specRepository.findOne(Example.of(match));

            if (duplicate.isPresent()) {
                spec.capability(duplicate.get().getCapability());
                spec.serviceDefinition(duplicate.get().getServiceDefinition());
            }

        }

        ScanResult scanResult = new ScanResult(source, specs);
        specSourceResolver.process(scanResult);

        String location = (requestBody.getLocation()).concat("/"+requestBody.getProjectId()).concat("/" + requestBody.getArtifactId()).concat("/" + source.getPortal().getName());

        ExportOptions exportSpec = new ExportOptions();
        exportSpec.setLocation(location);
        exportSpec.setPortal(source.getPortal());
        exportSpec.setExportType(ExportType.FILE_SYSTEM);
        try {
            fileSystemExporter.export(exportSpec);
        }catch (ExportException e) {
            e.printStackTrace();
        }


        List<Spec> specsProcessed = specRepository.findAll().stream().filter(spec -> spec.getSource().getId().equals(sourceId)).collect(Collectors.toList());
        List<BoatLintReport> lintReports = specsProcessed.stream().map(spec -> boatSpecLinter.lint(spec)).map(lintReport -> lintReportMapper.mapReport(lintReport)).collect(Collectors.toList());

        return ResponseEntity.ok(lintReports);
    }


    private Spec mapSpec(UploadSpec uploadSpec){

        Spec spec = new Spec();

        spec.setOpenApi(uploadSpec.getOpenApi());
        spec.setKey(uploadSpec.getKey());
        spec.setName(uploadSpec.getName());
        spec.setFilename(uploadSpec.getFileName());

        return spec;
    }

}
