package com.backbase.oss.boat.bay.web.views;

import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.dto.DashboardDto;
import com.backbase.oss.boat.bay.dto.PortalDto;
import com.backbase.oss.boat.bay.mapper.DashboardMapper;
import com.backbase.oss.boat.bay.repository.extended.BoatDashboardRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/view")
@Transactional
@RequiredArgsConstructor
public class DashboardView {

    private final BoatDashboardRepository dashboardRepository;
    private final BoatPortalRepository boatPortalRepository;

    private final DashboardMapper dashboardMapper;


    @GetMapping("/dashboard")
    public ResponseEntity<PortalDto> getDefaultPortal() {

        Dashboard dashboard = dashboardRepository.findAll().stream().findFirst().orElseThrow(() -> new IllegalStateException("No dashboard present"));

        Portal portal = dashboard.getDefaultPortal();
        // Only list products with capabilities...
        portal.setProducts(portal.getProducts().stream().filter(product -> !product.getCapabilities().isEmpty()).collect(Collectors.toSet()));

        PortalDto portalDto = dashboardMapper.mapPortal(portal);
        portalDto.setReleases(dashboardMapper.mapReleases(portal));
        portalDto.setCapabilities(dashboardMapper.mapCapabilities(portal));

        return ResponseEntity.ok(portalDto);
    }

}
