package com.backbase.oss.boat.bay.web.views.dashboard.v1;

import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.repository.extended.BoatDashboardRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Transactional
@RequiredArgsConstructor
public class DashboardV1View {

    public static final String VIEWS = "views";
    private final BoatDashboardRepository dashboardRepository;
    private final BoatPortalRepository boatPortalRepository;

    private final DashboardMapper dashboardMapper;


    @GetMapping("/dashboard")
    @Cacheable(VIEWS)
    public ResponseEntity<LegacyPortalDto> getDefaultPortal() {

        Dashboard dashboard = dashboardRepository.findAll().stream().findFirst().orElseThrow(() -> new IllegalStateException("No dashboard present"));

        Portal portal = dashboard.getDefaultPortal();
        // Only list products with capabilities...
        portal.setProducts(portal.getProducts().stream().filter(product -> !product.getCapabilities().isEmpty()).collect(Collectors.toSet()));


        LegacyPortalDto portalDto = dashboardMapper.mapPortal(portal);
        portalDto.setReleases(dashboardMapper.mapReleases(portal));
        portalDto.setCapabilities(dashboardMapper.mapCapabilities(portal));

        return ResponseEntity.ok(portalDto);
    }

}
