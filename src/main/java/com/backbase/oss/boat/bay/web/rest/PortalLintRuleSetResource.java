package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.PortalLintRuleSet;
import com.backbase.oss.boat.bay.repository.PortalLintRuleSetRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.PortalLintRuleSet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PortalLintRuleSetResource {

    private final Logger log = LoggerFactory.getLogger(PortalLintRuleSetResource.class);

    private static final String ENTITY_NAME = "portalLintRuleSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PortalLintRuleSetRepository portalLintRuleSetRepository;

    public PortalLintRuleSetResource(PortalLintRuleSetRepository portalLintRuleSetRepository) {
        this.portalLintRuleSetRepository = portalLintRuleSetRepository;
    }

    /**
     * {@code POST  /portal-lint-rule-sets} : Create a new portalLintRuleSet.
     *
     * @param portalLintRuleSet the portalLintRuleSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new portalLintRuleSet, or with status {@code 400 (Bad Request)} if the portalLintRuleSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/portal-lint-rule-sets")
    public ResponseEntity<PortalLintRuleSet> createPortalLintRuleSet(@Valid @RequestBody PortalLintRuleSet portalLintRuleSet) throws URISyntaxException {
        log.debug("REST request to save PortalLintRuleSet : {}", portalLintRuleSet);
        if (portalLintRuleSet.getId() != null) {
            throw new BadRequestAlertException("A new portalLintRuleSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PortalLintRuleSet result = portalLintRuleSetRepository.save(portalLintRuleSet);
        return ResponseEntity.created(new URI("/api/portal-lint-rule-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /portal-lint-rule-sets} : Updates an existing portalLintRuleSet.
     *
     * @param portalLintRuleSet the portalLintRuleSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portalLintRuleSet,
     * or with status {@code 400 (Bad Request)} if the portalLintRuleSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the portalLintRuleSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/portal-lint-rule-sets")
    public ResponseEntity<PortalLintRuleSet> updatePortalLintRuleSet(@Valid @RequestBody PortalLintRuleSet portalLintRuleSet) throws URISyntaxException {
        log.debug("REST request to update PortalLintRuleSet : {}", portalLintRuleSet);
        if (portalLintRuleSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PortalLintRuleSet result = portalLintRuleSetRepository.save(portalLintRuleSet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, portalLintRuleSet.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /portal-lint-rule-sets} : get all the portalLintRuleSets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of portalLintRuleSets in body.
     */
    @GetMapping("/portal-lint-rule-sets")
    public List<PortalLintRuleSet> getAllPortalLintRuleSets() {
        log.debug("REST request to get all PortalLintRuleSets");
        return portalLintRuleSetRepository.findAll();
    }

    /**
     * {@code GET  /portal-lint-rule-sets/:id} : get the "id" portalLintRuleSet.
     *
     * @param id the id of the portalLintRuleSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the portalLintRuleSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portal-lint-rule-sets/{id}")
    public ResponseEntity<PortalLintRuleSet> getPortalLintRuleSet(@PathVariable Long id) {
        log.debug("REST request to get PortalLintRuleSet : {}", id);
        Optional<PortalLintRuleSet> portalLintRuleSet = portalLintRuleSetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(portalLintRuleSet);
    }

    /**
     * {@code DELETE  /portal-lint-rule-sets/:id} : delete the "id" portalLintRuleSet.
     *
     * @param id the id of the portalLintRuleSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/portal-lint-rule-sets/{id}")
    public ResponseEntity<Void> deletePortalLintRuleSet(@PathVariable Long id) {
        log.debug("REST request to delete PortalLintRuleSet : {}", id);
        portalLintRuleSetRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
