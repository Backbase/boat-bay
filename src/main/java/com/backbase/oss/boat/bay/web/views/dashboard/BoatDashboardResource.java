package com.backbase.oss.boat.bay.web.views.dashboard;

import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.repository.TagRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatDashboardRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintReportRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleViolationRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalRepository;
import com.backbase.oss.boat.bay.web.views.lint.LintReportMapper;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/boat/")
@Transactional
@RequiredArgsConstructor
public class BoatDashboardResource {

    public static final String VIEWS = "views";

    private final BoatPortalRepository boatPortalRepository;
    private final BoatLintReportRepository boatLintReportRepository;
    private final BoatLintRuleViolationRepository boatLintRuleViolationRepository;
    private final BoatDashboardRepository dashboardRepository;

    private final BoatDashboardMapper dashboardMapper;
    private final LintReportMapper lintReportMapper;
    private final TagRepository tagRepository;


    public List<String> getAllEnabledTags() {
        return tagRepository.findAll(Example.of(new Tag().hide(false))).stream()
            .map(Tag::getName)
            .collect(Collectors.toList());
    }


    @GetMapping("legacy-dashboard")
    public ResponseEntity<BoatLegacyPortalDto> getDefaultPortal() {

        List<String> allEnabledTags = getAllEnabledTags();

        Dashboard dashboard = dashboardRepository.findAll().stream().findFirst().orElseThrow(() -> new IllegalStateException("No dashboard present"));

        Portal portal = dashboard.getDefaultPortal();
        // Only list products with capabilities...
        portal.setProducts(portal.getProducts().stream().filter(product -> !product.getCapabilities().isEmpty()).collect(Collectors.toSet()));


        BoatLegacyPortalDto portalDto = dashboardMapper.mapPortal(portal);
        portalDto.setReleases(dashboardMapper.mapReleases(portal));

        portalDto.setCapabilities(dashboardMapper.mapCapabilities(portal, allEnabledTags));

        return ResponseEntity.ok(portalDto);
    }


    @GetMapping("dashboard")
    public ResponseEntity<List<BoatPortalDto>> getPortals() {

        List<BoatPortalDto> portals = boatPortalRepository.findAll().stream()
            .flatMap(portal -> portal.getProducts().stream()
                .map(product -> enrichPortalDto(portal, product)))
            .collect(Collectors.toList());

        return ResponseEntity.ok(portals);
    }


    @NotNull
    private BoatPortalDto enrichPortalDto(com.backbase.oss.boat.bay.domain.Portal portal, com.backbase.oss.boat.bay.domain.Product product) {
        BoatPortalDto portalDto = dashboardMapper.mapPortal(portal, product);

        boatLintReportRepository.findDistinctFirstBySpec_ProductOrderByLintedOn(product)
            .ifPresent(lintReport -> portalDto.setLastLintReport(lintReportMapper.mapReportWithoutViolations(lintReport)));

        Map<Severity, Long> issues = new LinkedHashMap<>();
        Arrays.stream(Severity.values()).forEach(s -> {
            long l = boatLintRuleViolationRepository.countBySeverityAndLintReport_Spec_Product(s, product);
            issues.put(s, l);
        });

        portalDto.setIssues(issues);

        return portalDto;
    }


}
