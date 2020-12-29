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

    private final LintRuleSetRepository lintRuleSetRepository;

    public LintRuleSetResource(LintRuleSetRepository lintRuleSetRepository) {
        this.lintRuleSetRepository = lintRuleSetRepository;
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
}
