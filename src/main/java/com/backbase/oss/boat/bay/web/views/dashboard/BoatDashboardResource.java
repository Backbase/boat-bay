package com.backbase.oss.boat.bay.web.views.dashboard;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.repository.TagRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatCapabilityRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatDashboardRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintReportRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleViolationRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatProductRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatServiceRepository;
import com.backbase.oss.boat.bay.service.statistics.BoatStatisticsCollector;
import com.backbase.oss.boat.bay.web.views.lint.LintReportMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/boat/")
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoatDashboardResource {

    public static final String VIEWS = "views";

    private final BoatPortalRepository boatPortalRepository;
    private final BoatProductRepository boatProductRepository;
    private final BoatCapabilityRepository boatCapabilityRepository;
    private final BoatServiceRepository boatServiceRepository;
    private final BoatLintReportRepository boatLintReportRepository;
    private final BoatLintRuleViolationRepository boatLintRuleViolationRepository;
    private final BoatDashboardRepository dashboardRepository;

    private final BoatDashboardMapper dashboardMapper;
    private final LintReportMapper lintReportMapper;
    private final TagRepository tagRepository;

    private final BoatStatisticsCollector boatStatisticsCollector;

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
    public ResponseEntity<BoatProductDashboard> getProductDashboard(@PathVariable String projectKey, @PathVariable String productKey) {

        Product product = boatProductRepository.findByKeyAndPortalKey(productKey, projectKey )
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        BoatProductDashboard boatProduct = mapProduct(product, projectKey);

        return ResponseEntity.ok(boatProduct);
    }

    @NotNull
    private BoatPortalDashboard mapPortalDashboard(com.backbase.oss.boat.bay.domain.Product product) {

        BoatPortalDashboard portalDto = dashboardMapper.mapPortal(product.getPortal(), product);
        boatLintReportRepository.findDistinctFirstBySpecProductOrderByLintedOn(product)
            .ifPresent(lintReport -> portalDto.setLastLintReport(lintReportMapper.mapReportWithoutViolations(lintReport)));

        portalDto.setNumberOfCapabilities(boatCapabilityRepository.countByProduct(product));
        portalDto.setNumberOfServices(boatServiceRepository.countByCapabilityProduct(product));
        portalDto.setStatistics(boatStatisticsCollector.collect(product));
        return portalDto;
    }

    @NotNull
    private BoatProductDashboard mapProduct(Product product, String projectKey) {

        BoatProductDashboard boatProduct = dashboardMapper.mapBoatProduct(product);
        boatLintReportRepository.findDistinctFirstBySpecProductOrderByLintedOn(product)
            .ifPresent(lintReport -> boatProduct.setLastLintReport(lintReportMapper.mapReportWithoutViolations(lintReport)));
        boatProduct.setStatistics(boatStatisticsCollector.collect(product));

        List<BoatCapability> capabilities = product.getCapabilities().stream()
            .map(this::mapCapability)
            .collect(Collectors.toList());

        boatProduct.setCapabilities(capabilities);
        boatProduct.setPortalName(product.getPortal().getName());
        boatProduct.setPortalKey(projectKey);
        return boatProduct;
    }

    @NotNull
    private BoatCapability mapCapability(Capability capability) {

        BoatCapability boatCapability = dashboardMapper.mapBoatCapability(capability);
        boatLintReportRepository.findDistinctFirstBySpecServiceDefinitionCapability(capability)
            .ifPresent(lintReport -> boatCapability.setLastLintReport(lintReportMapper.mapReportWithoutViolations(lintReport)));
        boatCapability.setStatistics(boatStatisticsCollector.collect(capability));
        return boatCapability;
    }

}
