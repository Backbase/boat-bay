package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.PortalLintRuleConfig;
import com.backbase.oss.boat.bay.repository.PortalLintRuleConfigRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.PortalLintRuleConfig}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PortalLintRuleConfigResource {

    private final Logger log = LoggerFactory.getLogger(PortalLintRuleConfigResource.class);

    private static final String ENTITY_NAME = "portalLintRuleConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PortalLintRuleConfigRepository portalLintRuleConfigRepository;

    public PortalLintRuleConfigResource(PortalLintRuleConfigRepository portalLintRuleConfigRepository) {
        this.portalLintRuleConfigRepository = portalLintRuleConfigRepository;
    }

    /**
     * {@code POST  /portal-lint-rule-configs} : Create a new portalLintRuleConfig.
     *
     * @param portalLintRuleConfig the portalLintRuleConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new portalLintRuleConfig, or with status {@code 400 (Bad Request)} if the portalLintRuleConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/portal-lint-rule-configs")
    public ResponseEntity<PortalLintRuleConfig> createPortalLintRuleConfig(@Valid @RequestBody PortalLintRuleConfig portalLintRuleConfig) throws URISyntaxException {
        log.debug("REST request to save PortalLintRuleConfig : {}", portalLintRuleConfig);
        if (portalLintRuleConfig.getId() != null) {
            throw new BadRequestAlertException("A new portalLintRuleConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PortalLintRuleConfig result = portalLintRuleConfigRepository.save(portalLintRuleConfig);
        return ResponseEntity.created(new URI("/api/portal-lint-rule-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /portal-lint-rule-configs} : Updates an existing portalLintRuleConfig.
     *
     * @param portalLintRuleConfig the portalLintRuleConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portalLintRuleConfig,
     * or with status {@code 400 (Bad Request)} if the portalLintRuleConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the portalLintRuleConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/portal-lint-rule-configs")
    public ResponseEntity<PortalLintRuleConfig> updatePortalLintRuleConfig(@Valid @RequestBody PortalLintRuleConfig portalLintRuleConfig) throws URISyntaxException {
        log.debug("REST request to update PortalLintRuleConfig : {}", portalLintRuleConfig);
        if (portalLintRuleConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PortalLintRuleConfig result = portalLintRuleConfigRepository.save(portalLintRuleConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, portalLintRuleConfig.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /portal-lint-rule-configs} : get all the portalLintRuleConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of portalLintRuleConfigs in body.
     */
    @GetMapping("/portal-lint-rule-configs")
    public List<PortalLintRuleConfig> getAllPortalLintRuleConfigs() {
        log.debug("REST request to get all PortalLintRuleConfigs");
        return portalLintRuleConfigRepository.findAll();
    }

    /**
     * {@code GET  /portal-lint-rule-configs/:id} : get the "id" portalLintRuleConfig.
     *
     * @param id the id of the portalLintRuleConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the portalLintRuleConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portal-lint-rule-configs/{id}")
    public ResponseEntity<PortalLintRuleConfig> getPortalLintRuleConfig(@PathVariable Long id) {
        log.debug("REST request to get PortalLintRuleConfig : {}", id);
        Optional<PortalLintRuleConfig> portalLintRuleConfig = portalLintRuleConfigRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(portalLintRuleConfig);
    }

    /**
     * {@code DELETE  /portal-lint-rule-configs/:id} : delete the "id" portalLintRuleConfig.
     *
     * @param id the id of the portalLintRuleConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/portal-lint-rule-configs/{id}")
    public ResponseEntity<Void> deletePortalLintRuleConfig(@PathVariable Long id) {
        log.debug("REST request to delete PortalLintRuleConfig : {}", id);
        portalLintRuleConfigRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
