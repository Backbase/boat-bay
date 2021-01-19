package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
        return ResponseEntity.created(new URI("/api/capabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /capabilities} : Updates an existing capability.
     *
     * @param capability the capability to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capability,
     * or with status {@code 400 (Bad Request)} if the capability is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capability couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/capabilities")
    public ResponseEntity<Capability> updateCapability(@Valid @RequestBody Capability capability) throws URISyntaxException {
        log.debug("REST request to update Capability : {}", capability);
        if (capability.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Capability result = capabilityRepository.save(capability);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, capability.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
