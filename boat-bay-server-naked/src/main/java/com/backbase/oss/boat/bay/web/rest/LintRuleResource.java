package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.repository.LintRuleRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.LintRule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LintRuleResource {

    private final Logger log = LoggerFactory.getLogger(LintRuleResource.class);

    private static final String ENTITY_NAME = "lintRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LintRuleRepository lintRuleRepository;

    public LintRuleResource(LintRuleRepository lintRuleRepository) {
        this.lintRuleRepository = lintRuleRepository;
    }

    /**
     * {@code POST  /lint-rules} : Create a new lintRule.
     *
     * @param lintRule the lintRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lintRule, or with status {@code 400 (Bad Request)} if the lintRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lint-rules")
    public ResponseEntity<LintRule> createLintRule(@Valid @RequestBody LintRule lintRule) throws URISyntaxException {
        log.debug("REST request to save LintRule : {}", lintRule);
        if (lintRule.getId() != null) {
            throw new BadRequestAlertException("A new lintRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LintRule result = lintRuleRepository.save(lintRule);
        return ResponseEntity
            .created(new URI("/api/lint-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lint-rules/:id} : Updates an existing lintRule.
     *
     * @param id the id of the lintRule to save.
     * @param lintRule the lintRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lintRule,
     * or with status {@code 400 (Bad Request)} if the lintRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lintRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lint-rules/{id}")
    public ResponseEntity<LintRule> updateLintRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LintRule lintRule
    ) throws URISyntaxException {
        log.debug("REST request to update LintRule : {}, {}", id, lintRule);
        if (lintRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lintRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lintRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LintRule result = lintRuleRepository.save(lintRule);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lintRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lint-rules/:id} : Partial updates given fields of an existing lintRule, field will ignore if it is null
     *
     * @param id the id of the lintRule to save.
     * @param lintRule the lintRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lintRule,
     * or with status {@code 400 (Bad Request)} if the lintRule is not valid,
     * or with status {@code 404 (Not Found)} if the lintRule is not found,
     * or with status {@code 500 (Internal Server Error)} if the lintRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lint-rules/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LintRule> partialUpdateLintRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LintRule lintRule
    ) throws URISyntaxException {
        log.debug("REST request to partial update LintRule partially : {}, {}", id, lintRule);
        if (lintRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lintRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lintRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LintRule> result = lintRuleRepository
            .findById(lintRule.getId())
            .map(
                existingLintRule -> {
                    if (lintRule.getRuleId() != null) {
                        existingLintRule.setRuleId(lintRule.getRuleId());
                    }
                    if (lintRule.getTitle() != null) {
                        existingLintRule.setTitle(lintRule.getTitle());
                    }
                    if (lintRule.getRuleSet() != null) {
                        existingLintRule.setRuleSet(lintRule.getRuleSet());
                    }
                    if (lintRule.getSummary() != null) {
                        existingLintRule.setSummary(lintRule.getSummary());
                    }
                    if (lintRule.getSeverity() != null) {
                        existingLintRule.setSeverity(lintRule.getSeverity());
                    }
                    if (lintRule.getDescription() != null) {
                        existingLintRule.setDescription(lintRule.getDescription());
                    }
                    if (lintRule.getExternalUrl() != null) {
                        existingLintRule.setExternalUrl(lintRule.getExternalUrl());
                    }
                    if (lintRule.getEnabled() != null) {
                        existingLintRule.setEnabled(lintRule.getEnabled());
                    }

                    return existingLintRule;
                }
            )
            .map(lintRuleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lintRule.getId().toString())
        );
    }

    /**
     * {@code GET  /lint-rules} : get all the lintRules.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lintRules in body.
     */
    @GetMapping("/lint-rules")
    public List<LintRule> getAllLintRules() {
        log.debug("REST request to get all LintRules");
        return lintRuleRepository.findAll();
    }

    /**
     * {@code GET  /lint-rules/:id} : get the "id" lintRule.
     *
     * @param id the id of the lintRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lintRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lint-rules/{id}")
    public ResponseEntity<LintRule> getLintRule(@PathVariable Long id) {
        log.debug("REST request to get LintRule : {}", id);
        Optional<LintRule> lintRule = lintRuleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lintRule);
    }

    /**
     * {@code DELETE  /lint-rules/:id} : delete the "id" lintRule.
     *
     * @param id the id of the lintRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lint-rules/{id}")
    public ResponseEntity<Void> deleteLintRule(@PathVariable Long id) {
        log.debug("REST request to delete LintRule : {}", id);
        lintRuleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
