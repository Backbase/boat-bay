package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.repository.DashboardRepository;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.Dashboard}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    private static final String ENTITY_NAME = "dashboard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DashboardRepository dashboardRepository;

    public DashboardResource(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    /**
     * {@code POST  /dashboards} : Create a new dashboard.
     *
     * @param dashboard the dashboard to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dashboard, or with status {@code 400 (Bad Request)} if the dashboard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dashboards")
    public ResponseEntity<Dashboard> createDashboard(@Valid @RequestBody Dashboard dashboard) throws URISyntaxException {
        log.debug("REST request to save Dashboard : {}", dashboard);
        if (dashboard.getId() != null) {
            throw new BadRequestAlertException("A new dashboard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dashboard result = dashboardRepository.save(dashboard);
        return ResponseEntity
            .created(new URI("/api/dashboards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dashboards/:id} : Updates an existing dashboard.
     *
     * @param id the id of the dashboard to save.
     * @param dashboard the dashboard to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dashboard,
     * or with status {@code 400 (Bad Request)} if the dashboard is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dashboard couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dashboards/{id}")
    public ResponseEntity<Dashboard> updateDashboard(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Dashboard dashboard
    ) throws URISyntaxException {
        log.debug("REST request to update Dashboard : {}, {}", id, dashboard);
        if (dashboard.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dashboard.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dashboardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dashboard result = dashboardRepository.save(dashboard);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dashboard.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dashboards/:id} : Partial updates given fields of an existing dashboard, field will ignore if it is null
     *
     * @param id the id of the dashboard to save.
     * @param dashboard the dashboard to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dashboard,
     * or with status {@code 400 (Bad Request)} if the dashboard is not valid,
     * or with status {@code 404 (Not Found)} if the dashboard is not found,
     * or with status {@code 500 (Internal Server Error)} if the dashboard couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dashboards/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Dashboard> partialUpdateDashboard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Dashboard dashboard
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dashboard partially : {}, {}", id, dashboard);
        if (dashboard.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dashboard.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dashboardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dashboard> result = dashboardRepository
            .findById(dashboard.getId())
            .map(
                existingDashboard -> {
                    if (dashboard.getName() != null) {
                        existingDashboard.setName(dashboard.getName());
                    }
                    if (dashboard.getTitle() != null) {
                        existingDashboard.setTitle(dashboard.getTitle());
                    }
                    if (dashboard.getSubTitle() != null) {
                        existingDashboard.setSubTitle(dashboard.getSubTitle());
                    }
                    if (dashboard.getNavTitle() != null) {
                        existingDashboard.setNavTitle(dashboard.getNavTitle());
                    }
                    if (dashboard.getContent() != null) {
                        existingDashboard.setContent(dashboard.getContent());
                    }

                    return existingDashboard;
                }
            )
            .map(dashboardRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dashboard.getId().toString())
        );
    }

    /**
     * {@code GET  /dashboards} : get all the dashboards.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dashboards in body.
     */
    @GetMapping("/dashboards")
    public List<Dashboard> getAllDashboards() {
        log.debug("REST request to get all Dashboards");
        return dashboardRepository.findAll();
    }

    /**
     * {@code GET  /dashboards/:id} : get the "id" dashboard.
     *
     * @param id the id of the dashboard to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dashboard, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dashboards/{id}")
    public ResponseEntity<Dashboard> getDashboard(@PathVariable Long id) {
        log.debug("REST request to get Dashboard : {}", id);
        Optional<Dashboard> dashboard = dashboardRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dashboard);
    }

    /**
     * {@code DELETE  /dashboards/:id} : delete the "id" dashboard.
     *
     * @param id the id of the dashboard to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dashboards/{id}")
    public ResponseEntity<Void> deleteDashboard(@PathVariable Long id) {
        log.debug("REST request to delete Dashboard : {}", id);
        dashboardRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
