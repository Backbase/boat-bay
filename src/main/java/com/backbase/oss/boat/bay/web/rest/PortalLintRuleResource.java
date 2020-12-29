package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.PortalLintRule;
import com.backbase.oss.boat.bay.repository.PortalLintRuleRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.PortalLintRule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PortalLintRuleResource {

    private final Logger log = LoggerFactory.getLogger(PortalLintRuleResource.class);

    private static final String ENTITY_NAME = "portalLintRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PortalLintRuleRepository portalLintRuleRepository;

    public PortalLintRuleResource(PortalLintRuleRepository portalLintRuleRepository) {
        this.portalLintRuleRepository = portalLintRuleRepository;
    }

    /**
     * {@code POST  /portal-lint-rules} : Create a new portalLintRule.
     *
     * @param portalLintRule the portalLintRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new portalLintRule, or with status {@code 400 (Bad Request)} if the portalLintRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/portal-lint-rules")
    public ResponseEntity<PortalLintRule> createPortalLintRule(@Valid @RequestBody PortalLintRule portalLintRule) throws URISyntaxException {
        log.debug("REST request to save PortalLintRule : {}", portalLintRule);
        if (portalLintRule.getId() != null) {
            throw new BadRequestAlertException("A new portalLintRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PortalLintRule result = portalLintRuleRepository.save(portalLintRule);
        return ResponseEntity.created(new URI("/api/portal-lint-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /portal-lint-rules} : Updates an existing portalLintRule.
     *
     * @param portalLintRule the portalLintRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portalLintRule,
     * or with status {@code 400 (Bad Request)} if the portalLintRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the portalLintRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/portal-lint-rules")
    public ResponseEntity<PortalLintRule> updatePortalLintRule(@Valid @RequestBody PortalLintRule portalLintRule) throws URISyntaxException {
        log.debug("REST request to update PortalLintRule : {}", portalLintRule);
        if (portalLintRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PortalLintRule result = portalLintRuleRepository.save(portalLintRule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, portalLintRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /portal-lint-rules} : get all the portalLintRules.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of portalLintRules in body.
     */
    @GetMapping("/portal-lint-rules")
    public List<PortalLintRule> getAllPortalLintRules(@RequestParam(required = false) String filter) {
        if ("portallintruleconfig-is-null".equals(filter)) {
            log.debug("REST request to get all PortalLintRules where portalLintRuleConfig is null");
            return StreamSupport
                .stream(portalLintRuleRepository.findAll().spliterator(), false)
                .filter(portalLintRule -> portalLintRule.getPortalLintRuleConfig() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all PortalLintRules");
        return portalLintRuleRepository.findAll();
    }

    /**
     * {@code GET  /portal-lint-rules/:id} : get the "id" portalLintRule.
     *
     * @param id the id of the portalLintRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the portalLintRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portal-lint-rules/{id}")
    public ResponseEntity<PortalLintRule> getPortalLintRule(@PathVariable Long id) {
        log.debug("REST request to get PortalLintRule : {}", id);
        Optional<PortalLintRule> portalLintRule = portalLintRuleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(portalLintRule);
    }

    /**
     * {@code DELETE  /portal-lint-rules/:id} : delete the "id" portalLintRule.
     *
     * @param id the id of the portalLintRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/portal-lint-rules/{id}")
    public ResponseEntity<Void> deletePortalLintRule(@PathVariable Long id) {
        log.debug("REST request to delete PortalLintRule : {}", id);
        portalLintRuleRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
