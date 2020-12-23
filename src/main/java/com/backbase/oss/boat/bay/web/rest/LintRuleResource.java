package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.repository.LintRuleRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public ResponseEntity<LintRule> createLintRule(@RequestBody LintRule lintRule) throws URISyntaxException {
        log.debug("REST request to save LintRule : {}", lintRule);
        if (lintRule.getId() != null) {
            throw new BadRequestAlertException("A new lintRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LintRule result = lintRuleRepository.save(lintRule);
        return ResponseEntity.created(new URI("/api/lint-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lint-rules} : Updates an existing lintRule.
     *
     * @param lintRule the lintRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lintRule,
     * or with status {@code 400 (Bad Request)} if the lintRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lintRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lint-rules")
    public ResponseEntity<LintRule> updateLintRule(@RequestBody LintRule lintRule) throws URISyntaxException {
        log.debug("REST request to update LintRule : {}", lintRule);
        if (lintRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LintRule result = lintRuleRepository.save(lintRule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lintRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lint-rules} : get all the lintRules.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lintRules in body.
     */
    @GetMapping("/lint-rules")
    public List<LintRule> getAllLintRules(@RequestParam(required = false) String filter) {
        if ("lintruleviolation-is-null".equals(filter)) {
            log.debug("REST request to get all LintRules where lintRuleViolation is null");
            return StreamSupport
                .stream(lintRuleRepository.findAll().spliterator(), false)
                .filter(lintRule -> lintRule.getLintRuleViolation() == null)
                .collect(Collectors.toList());
        }
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
