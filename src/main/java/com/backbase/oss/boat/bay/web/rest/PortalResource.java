package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.PortalRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.Portal}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PortalResource {

    private final Logger log = LoggerFactory.getLogger(PortalResource.class);

    private static final String ENTITY_NAME = "portal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PortalRepository portalRepository;

    public PortalResource(PortalRepository portalRepository) {
        this.portalRepository = portalRepository;
    }

    /**
     * {@code POST  /portals} : Create a new portal.
     *
     * @param portal the portal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new portal, or with status {@code 400 (Bad Request)} if the portal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/portals")
    public ResponseEntity<Portal> createPortal(@Valid @RequestBody Portal portal) throws URISyntaxException {
        log.debug("REST request to save Portal : {}", portal);
        if (portal.getId() != null) {
            throw new BadRequestAlertException("A new portal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Portal result = portalRepository.save(portal);
        return ResponseEntity.created(new URI("/api/portals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /portals} : Updates an existing portal.
     *
     * @param portal the portal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portal,
     * or with status {@code 400 (Bad Request)} if the portal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the portal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/portals")
    public ResponseEntity<Portal> updatePortal(@Valid @RequestBody Portal portal) throws URISyntaxException {
        log.debug("REST request to update Portal : {}", portal);
        if (portal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Portal result = portalRepository.save(portal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, portal.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /portals} : get all the portals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of portals in body.
     */
    @GetMapping("/portals")
    public List<Portal> getAllPortals() {
        log.debug("REST request to get all Portals");
        return portalRepository.findAll();
    }

    /**
     * {@code GET  /portals/:id} : get the "id" portal.
     *
     * @param id the id of the portal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the portal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portals/{id}")
    public ResponseEntity<Portal> getPortal(@PathVariable Long id) {
        log.debug("REST request to get Portal : {}", id);
        Optional<Portal> portal = portalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(portal);
    }

    /**
     * {@code DELETE  /portals/:id} : delete the "id" portal.
     *
     * @param id the id of the portal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/portals/{id}")
    public ResponseEntity<Void> deletePortal(@PathVariable Long id) {
        log.debug("REST request to delete Portal : {}", id);
        portalRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
