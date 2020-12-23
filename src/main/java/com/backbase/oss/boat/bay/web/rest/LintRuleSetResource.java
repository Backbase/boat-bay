package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.LintRuleSet;
import com.backbase.oss.boat.bay.repository.LintRuleSetRepository;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.LintRuleSet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LintRuleSetResource {

    private final Logger log = LoggerFactory.getLogger(LintRuleSetResource.class);

    private static final String ENTITY_NAME = "lintRuleSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LintRuleSetRepository lintRuleSetRepository;

    public LintRuleSetResource(LintRuleSetRepository lintRuleSetRepository) {
        this.lintRuleSetRepository = lintRuleSetRepository;
    }

    /**
     * {@code POST  /lint-rule-sets} : Create a new lintRuleSet.
     *
     * @param lintRuleSet the lintRuleSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lintRuleSet, or with status {@code 400 (Bad Request)} if the lintRuleSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lint-rule-sets")
    public ResponseEntity<LintRuleSet> createLintRuleSet(@Valid @RequestBody LintRuleSet lintRuleSet) throws URISyntaxException {
        log.debug("REST request to save LintRuleSet : {}", lintRuleSet);
        if (lintRuleSet.getId() != null) {
            throw new BadRequestAlertException("A new lintRuleSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LintRuleSet result = lintRuleSetRepository.save(lintRuleSet);
        return ResponseEntity.created(new URI("/api/lint-rule-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lint-rule-sets} : Updates an existing lintRuleSet.
     *
     * @param lintRuleSet the lintRuleSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lintRuleSet,
     * or with status {@code 400 (Bad Request)} if the lintRuleSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lintRuleSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lint-rule-sets")
    public ResponseEntity<LintRuleSet> updateLintRuleSet(@Valid @RequestBody LintRuleSet lintRuleSet) throws URISyntaxException {
        log.debug("REST request to update LintRuleSet : {}", lintRuleSet);
        if (lintRuleSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LintRuleSet result = lintRuleSetRepository.save(lintRuleSet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lintRuleSet.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lint-rule-sets} : get all the lintRuleSets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lintRuleSets in body.
     */
    @GetMapping("/lint-rule-sets")
    public List<LintRuleSet> getAllLintRuleSets() {
        log.debug("REST request to get all LintRuleSets");
        return lintRuleSetRepository.findAll();
    }

    /**
     * {@code GET  /lint-rule-sets/:id} : get the "id" lintRuleSet.
     *
     * @param id the id of the lintRuleSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lintRuleSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lint-rule-sets/{id}")
    public ResponseEntity<LintRuleSet> getLintRuleSet(@PathVariable Long id) {
        log.debug("REST request to get LintRuleSet : {}", id);
        Optional<LintRuleSet> lintRuleSet = lintRuleSetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lintRuleSet);
    }

    /**
     * {@code DELETE  /lint-rule-sets/:id} : delete the "id" lintRuleSet.
     *
     * @param id the id of the lintRuleSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lint-rule-sets/{id}")
    public ResponseEntity<Void> deleteLintRuleSet(@PathVariable Long id) {
        log.debug("REST request to delete LintRuleSet : {}", id);
        lintRuleSetRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
