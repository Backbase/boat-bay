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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.LintRule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LintRuleResource {

    private final Logger log = LoggerFactory.getLogger(LintRuleResource.class);

    private final LintRuleRepository lintRuleRepository;

    public LintRuleResource(LintRuleRepository lintRuleRepository) {
        this.lintRuleRepository = lintRuleRepository;
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
}
