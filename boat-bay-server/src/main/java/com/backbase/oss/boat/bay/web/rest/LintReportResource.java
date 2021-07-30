package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.repository.LintReportRepository;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.LintReport}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LintReportResource {

    private final Logger log = LoggerFactory.getLogger(LintReportResource.class);

    private static final String ENTITY_NAME = "lintReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LintReportRepository lintReportRepository;

    public LintReportResource(LintReportRepository lintReportRepository) {
        this.lintReportRepository = lintReportRepository;
    }

    /**
     * {@code POST  /lint-reports} : Create a new lintReport.
     *
     * @param lintReport the lintReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lintReport, or with status {@code 400 (Bad Request)} if the lintReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lint-reports")
    public ResponseEntity<LintReport> createLintReport(@RequestBody LintReport lintReport) throws URISyntaxException {
        log.debug("REST request to save LintReport : {}", lintReport);
        if (lintReport.getId() != null) {
            throw new BadRequestAlertException("A new lintReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LintReport result = lintReportRepository.save(lintReport);
        return ResponseEntity
            .created(new URI("/api/lint-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lint-reports/:id} : Updates an existing lintReport.
     *
     * @param id the id of the lintReport to save.
     * @param lintReport the lintReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lintReport,
     * or with status {@code 400 (Bad Request)} if the lintReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lintReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lint-reports/{id}")
    public ResponseEntity<LintReport> updateLintReport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LintReport lintReport
    ) throws URISyntaxException {
        log.debug("REST request to update LintReport : {}, {}", id, lintReport);
        if (lintReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lintReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lintReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LintReport result = lintReportRepository.save(lintReport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lintReport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lint-reports/:id} : Partial updates given fields of an existing lintReport, field will ignore if it is null
     *
     * @param id the id of the lintReport to save.
     * @param lintReport the lintReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lintReport,
     * or with status {@code 400 (Bad Request)} if the lintReport is not valid,
     * or with status {@code 404 (Not Found)} if the lintReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the lintReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lint-reports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LintReport> partialUpdateLintReport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LintReport lintReport
    ) throws URISyntaxException {
        log.debug("REST request to partial update LintReport partially : {}, {}", id, lintReport);
        if (lintReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lintReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lintReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LintReport> result = lintReportRepository
            .findById(lintReport.getId())
            .map(
                existingLintReport -> {
                    if (lintReport.getName() != null) {
                        existingLintReport.setName(lintReport.getName());
                    }
                    if (lintReport.getGrade() != null) {
                        existingLintReport.setGrade(lintReport.getGrade());
                    }
                    if (lintReport.getPassed() != null) {
                        existingLintReport.setPassed(lintReport.getPassed());
                    }
                    if (lintReport.getLintedOn() != null) {
                        existingLintReport.setLintedOn(lintReport.getLintedOn());
                    }

                    return existingLintReport;
                }
            )
            .map(lintReportRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lintReport.getId().toString())
        );
    }

    /**
     * {@code GET  /lint-reports} : get all the lintReports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lintReports in body.
     */
    @GetMapping("/lint-reports")
    public List<LintReport> getAllLintReports() {
        log.debug("REST request to get all LintReports");
        return lintReportRepository.findAll();
    }

    /**
     * {@code GET  /lint-reports/:id} : get the "id" lintReport.
     *
     * @param id the id of the lintReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lintReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lint-reports/{id}")
    public ResponseEntity<LintReport> getLintReport(@PathVariable Long id) {
        log.debug("REST request to get LintReport : {}", id);
        Optional<LintReport> lintReport = lintReportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lintReport);
    }

    /**
     * {@code DELETE  /lint-reports/:id} : delete the "id" lintReport.
     *
     * @param id the id of the lintReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lint-reports/{id}")
    public ResponseEntity<Void> deleteLintReport(@PathVariable Long id) {
        log.debug("REST request to delete LintReport : {}", id);
        lintReportRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
