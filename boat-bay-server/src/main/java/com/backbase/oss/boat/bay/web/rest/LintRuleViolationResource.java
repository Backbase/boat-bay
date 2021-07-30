package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.LintRuleViolation;
import com.backbase.oss.boat.bay.repository.LintRuleViolationRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.LintRuleViolation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LintRuleViolationResource {

    private final Logger log = LoggerFactory.getLogger(LintRuleViolationResource.class);

    private static final String ENTITY_NAME = "lintRuleViolation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LintRuleViolationRepository lintRuleViolationRepository;

    public LintRuleViolationResource(LintRuleViolationRepository lintRuleViolationRepository) {
        this.lintRuleViolationRepository = lintRuleViolationRepository;
    }

    /**
     * {@code POST  /lint-rule-violations} : Create a new lintRuleViolation.
     *
     * @param lintRuleViolation the lintRuleViolation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lintRuleViolation, or with status {@code 400 (Bad Request)} if the lintRuleViolation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lint-rule-violations")
    public ResponseEntity<LintRuleViolation> createLintRuleViolation(@Valid @RequestBody LintRuleViolation lintRuleViolation)
        throws URISyntaxException {
        log.debug("REST request to save LintRuleViolation : {}", lintRuleViolation);
        if (lintRuleViolation.getId() != null) {
            throw new BadRequestAlertException("A new lintRuleViolation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LintRuleViolation result = lintRuleViolationRepository.save(lintRuleViolation);
        return ResponseEntity
            .created(new URI("/api/lint-rule-violations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lint-rule-violations/:id} : Updates an existing lintRuleViolation.
     *
     * @param id the id of the lintRuleViolation to save.
     * @param lintRuleViolation the lintRuleViolation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lintRuleViolation,
     * or with status {@code 400 (Bad Request)} if the lintRuleViolation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lintRuleViolation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lint-rule-violations/{id}")
    public ResponseEntity<LintRuleViolation> updateLintRuleViolation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LintRuleViolation lintRuleViolation
    ) throws URISyntaxException {
        log.debug("REST request to update LintRuleViolation : {}, {}", id, lintRuleViolation);
        if (lintRuleViolation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lintRuleViolation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lintRuleViolationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LintRuleViolation result = lintRuleViolationRepository.save(lintRuleViolation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lintRuleViolation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lint-rule-violations/:id} : Partial updates given fields of an existing lintRuleViolation, field will ignore if it is null
     *
     * @param id the id of the lintRuleViolation to save.
     * @param lintRuleViolation the lintRuleViolation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lintRuleViolation,
     * or with status {@code 400 (Bad Request)} if the lintRuleViolation is not valid,
     * or with status {@code 404 (Not Found)} if the lintRuleViolation is not found,
     * or with status {@code 500 (Internal Server Error)} if the lintRuleViolation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lint-rule-violations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LintRuleViolation> partialUpdateLintRuleViolation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LintRuleViolation lintRuleViolation
    ) throws URISyntaxException {
        log.debug("REST request to partial update LintRuleViolation partially : {}, {}", id, lintRuleViolation);
        if (lintRuleViolation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lintRuleViolation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lintRuleViolationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LintRuleViolation> result = lintRuleViolationRepository
            .findById(lintRuleViolation.getId())
            .map(
                existingLintRuleViolation -> {
                    if (lintRuleViolation.getName() != null) {
                        existingLintRuleViolation.setName(lintRuleViolation.getName());
                    }
                    if (lintRuleViolation.getDescription() != null) {
                        existingLintRuleViolation.setDescription(lintRuleViolation.getDescription());
                    }
                    if (lintRuleViolation.getUrl() != null) {
                        existingLintRuleViolation.setUrl(lintRuleViolation.getUrl());
                    }
                    if (lintRuleViolation.getSeverity() != null) {
                        existingLintRuleViolation.setSeverity(lintRuleViolation.getSeverity());
                    }
                    if (lintRuleViolation.getLineStart() != null) {
                        existingLintRuleViolation.setLineStart(lintRuleViolation.getLineStart());
                    }
                    if (lintRuleViolation.getLineEnd() != null) {
                        existingLintRuleViolation.setLineEnd(lintRuleViolation.getLineEnd());
                    }
                    if (lintRuleViolation.getJsonPointer() != null) {
                        existingLintRuleViolation.setJsonPointer(lintRuleViolation.getJsonPointer());
                    }

                    return existingLintRuleViolation;
                }
            )
            .map(lintRuleViolationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lintRuleViolation.getId().toString())
        );
    }

    /**
     * {@code GET  /lint-rule-violations} : get all the lintRuleViolations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lintRuleViolations in body.
     */
    @GetMapping("/lint-rule-violations")
    public ResponseEntity<List<LintRuleViolation>> getAllLintRuleViolations(Pageable pageable) {
        log.debug("REST request to get a page of LintRuleViolations");
        Page<LintRuleViolation> page = lintRuleViolationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lint-rule-violations/:id} : get the "id" lintRuleViolation.
     *
     * @param id the id of the lintRuleViolation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lintRuleViolation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lint-rule-violations/{id}")
    public ResponseEntity<LintRuleViolation> getLintRuleViolation(@PathVariable Long id) {
        log.debug("REST request to get LintRuleViolation : {}", id);
        Optional<LintRuleViolation> lintRuleViolation = lintRuleViolationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lintRuleViolation);
    }

    /**
     * {@code DELETE  /lint-rule-violations/:id} : delete the "id" lintRuleViolation.
     *
     * @param id the id of the lintRuleViolation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lint-rule-violations/{id}")
    public ResponseEntity<Void> deleteLintRuleViolation(@PathVariable Long id) {
        log.debug("REST request to delete LintRuleViolation : {}", id);
        lintRuleViolationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
