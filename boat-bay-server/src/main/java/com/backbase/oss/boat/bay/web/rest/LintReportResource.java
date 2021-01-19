package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.repository.LintReportRepository;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
        return ResponseEntity.created(new URI("/api/lint-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lint-reports} : Updates an existing lintReport.
     *
     * @param lintReport the lintReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lintReport,
     * or with status {@code 400 (Bad Request)} if the lintReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lintReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lint-reports")
    public ResponseEntity<LintReport> updateLintReport(@RequestBody LintReport lintReport) throws URISyntaxException {
        log.debug("REST request to update LintReport : {}", lintReport);
        if (lintReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LintReport result = lintReportRepository.save(lintReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lintReport.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
