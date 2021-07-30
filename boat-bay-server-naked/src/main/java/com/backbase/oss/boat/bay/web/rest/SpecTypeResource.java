package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.SpecType;
import com.backbase.oss.boat.bay.repository.SpecTypeRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

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
        return ResponseEntity
            .created(new URI("/api/spec-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spec-types/:id} : Updates an existing specType.
     *
     * @param id the id of the specType to save.
     * @param specType the specType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specType,
     * or with status {@code 400 (Bad Request)} if the specType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spec-types/{id}")
    public ResponseEntity<SpecType> updateSpecType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecType specType
    ) throws URISyntaxException {
        log.debug("REST request to update SpecType : {}, {}", id, specType);
        if (specType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpecType result = specTypeRepository.save(specType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /spec-types/:id} : Partial updates given fields of an existing specType, field will ignore if it is null
     *
     * @param id the id of the specType to save.
     * @param specType the specType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specType,
     * or with status {@code 400 (Bad Request)} if the specType is not valid,
     * or with status {@code 404 (Not Found)} if the specType is not found,
     * or with status {@code 500 (Internal Server Error)} if the specType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/spec-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SpecType> partialUpdateSpecType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecType specType
    ) throws URISyntaxException {
        log.debug("REST request to partial update SpecType partially : {}, {}", id, specType);
        if (specType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecType> result = specTypeRepository
            .findById(specType.getId())
            .map(
                existingSpecType -> {
                    if (specType.getName() != null) {
                        existingSpecType.setName(specType.getName());
                    }
                    if (specType.getDescription() != null) {
                        existingSpecType.setDescription(specType.getDescription());
                    }
                    if (specType.getMatchSpEL() != null) {
                        existingSpecType.setMatchSpEL(specType.getMatchSpEL());
                    }
                    if (specType.getIcon() != null) {
                        existingSpecType.setIcon(specType.getIcon());
                    }

                    return existingSpecType;
                }
            )
            .map(specTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specType.getId().toString())
        );
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
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
