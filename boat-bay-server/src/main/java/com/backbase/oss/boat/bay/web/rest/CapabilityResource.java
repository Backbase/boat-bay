package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.Capability}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CapabilityResource {

    private final Logger log = LoggerFactory.getLogger(CapabilityResource.class);

    private static final String ENTITY_NAME = "capability";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CapabilityRepository capabilityRepository;

    public CapabilityResource(CapabilityRepository capabilityRepository) {
        this.capabilityRepository = capabilityRepository;
    }

    /**
     * {@code POST  /capabilities} : Create a new capability.
     *
     * @param capability the capability to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new capability, or with status {@code 400 (Bad Request)} if the capability has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/capabilities")
    public ResponseEntity<Capability> createCapability(@Valid @RequestBody Capability capability) throws URISyntaxException {
        log.debug("REST request to save Capability : {}", capability);
        if (capability.getId() != null) {
            throw new BadRequestAlertException("A new capability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Capability result = capabilityRepository.save(capability);
        return ResponseEntity
            .created(new URI("/api/capabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /capabilities/:id} : Updates an existing capability.
     *
     * @param id the id of the capability to save.
     * @param capability the capability to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capability,
     * or with status {@code 400 (Bad Request)} if the capability is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capability couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/capabilities/{id}")
    public ResponseEntity<Capability> updateCapability(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Capability capability
    ) throws URISyntaxException {
        log.debug("REST request to update Capability : {}, {}", id, capability);
        if (capability.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capability.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Capability result = capabilityRepository.save(capability);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, capability.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /capabilities/:id} : Partial updates given fields of an existing capability, field will ignore if it is null
     *
     * @param id the id of the capability to save.
     * @param capability the capability to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capability,
     * or with status {@code 400 (Bad Request)} if the capability is not valid,
     * or with status {@code 404 (Not Found)} if the capability is not found,
     * or with status {@code 500 (Internal Server Error)} if the capability couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/capabilities/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Capability> partialUpdateCapability(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Capability capability
    ) throws URISyntaxException {
        log.debug("REST request to partial update Capability partially : {}, {}", id, capability);
        if (capability.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capability.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Capability> result = capabilityRepository
            .findById(capability.getId())
            .map(
                existingCapability -> {
                    if (capability.getKey() != null) {
                        existingCapability.setKey(capability.getKey());
                    }
                    if (capability.getName() != null) {
                        existingCapability.setName(capability.getName());
                    }
                    if (capability.getOrder() != null) {
                        existingCapability.setOrder(capability.getOrder());
                    }
                    if (capability.getSubTitle() != null) {
                        existingCapability.setSubTitle(capability.getSubTitle());
                    }
                    if (capability.getContent() != null) {
                        existingCapability.setContent(capability.getContent());
                    }
                    if (capability.getCreatedOn() != null) {
                        existingCapability.setCreatedOn(capability.getCreatedOn());
                    }
                    if (capability.getCreatedBy() != null) {
                        existingCapability.setCreatedBy(capability.getCreatedBy());
                    }
                    if (capability.getHide() != null) {
                        existingCapability.setHide(capability.getHide());
                    }

                    return existingCapability;
                }
            )
            .map(capabilityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, capability.getId().toString())
        );
    }

    /**
     * {@code GET  /capabilities} : get all the capabilities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of capabilities in body.
     */
    @GetMapping("/capabilities")
    public ResponseEntity<List<Capability>> getAllCapabilities(Pageable pageable) {
        log.debug("REST request to get a page of Capabilities");
        Page<Capability> page = capabilityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /capabilities/:id} : get the "id" capability.
     *
     * @param id the id of the capability to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the capability, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/capabilities/{id}")
    public ResponseEntity<Capability> getCapability(@PathVariable Long id) {
        log.debug("REST request to get Capability : {}", id);
        Optional<Capability> capability = capabilityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(capability);
    }

    /**
     * {@code DELETE  /capabilities/:id} : delete the "id" capability.
     *
     * @param id the id of the capability to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/capabilities/{id}")
    public ResponseEntity<Void> deleteCapability(@PathVariable Long id) {
        log.debug("REST request to delete Capability : {}", id);
        capabilityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
