package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.CapabilityServiceDefinition;
import com.backbase.oss.boat.bay.repository.CapabilityServiceDefinitionRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.CapabilityServiceDefinition}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CapabilityServiceDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(CapabilityServiceDefinitionResource.class);

    private static final String ENTITY_NAME = "capabilityServiceDefinition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CapabilityServiceDefinitionRepository capabilityServiceDefinitionRepository;

    public CapabilityServiceDefinitionResource(CapabilityServiceDefinitionRepository capabilityServiceDefinitionRepository) {
        this.capabilityServiceDefinitionRepository = capabilityServiceDefinitionRepository;
    }

    /**
     * {@code POST  /capability-service-definitions} : Create a new capabilityServiceDefinition.
     *
     * @param capabilityServiceDefinition the capabilityServiceDefinition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new capabilityServiceDefinition, or with status {@code 400 (Bad Request)} if the capabilityServiceDefinition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/capability-service-definitions")
    public ResponseEntity<CapabilityServiceDefinition> createCapabilityServiceDefinition(@Valid @RequestBody CapabilityServiceDefinition capabilityServiceDefinition) throws URISyntaxException {
        log.debug("REST request to save CapabilityServiceDefinition : {}", capabilityServiceDefinition);
        if (capabilityServiceDefinition.getId() != null) {
            throw new BadRequestAlertException("A new capabilityServiceDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CapabilityServiceDefinition result = capabilityServiceDefinitionRepository.save(capabilityServiceDefinition);
        return ResponseEntity.created(new URI("/api/capability-service-definitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /capability-service-definitions} : Updates an existing capabilityServiceDefinition.
     *
     * @param capabilityServiceDefinition the capabilityServiceDefinition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capabilityServiceDefinition,
     * or with status {@code 400 (Bad Request)} if the capabilityServiceDefinition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capabilityServiceDefinition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/capability-service-definitions")
    public ResponseEntity<CapabilityServiceDefinition> updateCapabilityServiceDefinition(@Valid @RequestBody CapabilityServiceDefinition capabilityServiceDefinition) throws URISyntaxException {
        log.debug("REST request to update CapabilityServiceDefinition : {}", capabilityServiceDefinition);
        if (capabilityServiceDefinition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CapabilityServiceDefinition result = capabilityServiceDefinitionRepository.save(capabilityServiceDefinition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, capabilityServiceDefinition.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /capability-service-definitions} : get all the capabilityServiceDefinitions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of capabilityServiceDefinitions in body.
     */
    @GetMapping("/capability-service-definitions")
    public List<CapabilityServiceDefinition> getAllCapabilityServiceDefinitions() {
        log.debug("REST request to get all CapabilityServiceDefinitions");
        return capabilityServiceDefinitionRepository.findAll();
    }

    /**
     * {@code GET  /capability-service-definitions/:id} : get the "id" capabilityServiceDefinition.
     *
     * @param id the id of the capabilityServiceDefinition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the capabilityServiceDefinition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/capability-service-definitions/{id}")
    public ResponseEntity<CapabilityServiceDefinition> getCapabilityServiceDefinition(@PathVariable Long id) {
        log.debug("REST request to get CapabilityServiceDefinition : {}", id);
        Optional<CapabilityServiceDefinition> capabilityServiceDefinition = capabilityServiceDefinitionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(capabilityServiceDefinition);
    }

    /**
     * {@code DELETE  /capability-service-definitions/:id} : delete the "id" capabilityServiceDefinition.
     *
     * @param id the id of the capabilityServiceDefinition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/capability-service-definitions/{id}")
    public ResponseEntity<Void> deleteCapabilityServiceDefinition(@PathVariable Long id) {
        log.debug("REST request to delete CapabilityServiceDefinition : {}", id);
        capabilityServiceDefinitionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
