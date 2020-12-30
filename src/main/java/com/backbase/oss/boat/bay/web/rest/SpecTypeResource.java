package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.SpecType;
import com.backbase.oss.boat.bay.repository.SpecTypeRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.SpecType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SpecTypeResource {

    private final Logger log = LoggerFactory.getLogger(SpecTypeResource.class);

    private static final String ENTITY_NAME = "specType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecTypeRepository specTypeRepository;

    public SpecTypeResource(SpecTypeRepository specTypeRepository) {
        this.specTypeRepository = specTypeRepository;
    }

    /**
     * {@code POST  /spec-types} : Create a new specType.
     *
     * @param specType the specType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specType, or with status {@code 400 (Bad Request)} if the specType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spec-types")
    public ResponseEntity<SpecType> createSpecType(@Valid @RequestBody SpecType specType) throws URISyntaxException {
        log.debug("REST request to save SpecType : {}", specType);
        if (specType.getId() != null) {
            throw new BadRequestAlertException("A new specType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecType result = specTypeRepository.save(specType);
        return ResponseEntity.created(new URI("/api/spec-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spec-types} : Updates an existing specType.
     *
     * @param specType the specType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specType,
     * or with status {@code 400 (Bad Request)} if the specType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spec-types")
    public ResponseEntity<SpecType> updateSpecType(@Valid @RequestBody SpecType specType) throws URISyntaxException {
        log.debug("REST request to update SpecType : {}", specType);
        if (specType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SpecType result = specTypeRepository.save(specType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /spec-types} : get all the specTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specTypes in body.
     */
    @GetMapping("/spec-types")
    public List<SpecType> getAllSpecTypes() {
        log.debug("REST request to get all SpecTypes");
        return specTypeRepository.findAll();
    }

    /**
     * {@code GET  /spec-types/:id} : get the "id" specType.
     *
     * @param id the id of the specType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spec-types/{id}")
    public ResponseEntity<SpecType> getSpecType(@PathVariable Long id) {
        log.debug("REST request to get SpecType : {}", id);
        Optional<SpecType> specType = specTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(specType);
    }

    /**
     * {@code DELETE  /spec-types/:id} : delete the "id" specType.
     *
     * @param id the id of the specType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spec-types/{id}")
    public ResponseEntity<Void> deleteSpecType(@PathVariable Long id) {
        log.debug("REST request to delete SpecType : {}", id);
        specTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
