package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.repository.SourcePathRepository;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.SourcePath}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SourcePathResource {

    private final Logger log = LoggerFactory.getLogger(SourcePathResource.class);

    private static final String ENTITY_NAME = "sourcePath";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourcePathRepository sourcePathRepository;

    public SourcePathResource(SourcePathRepository sourcePathRepository) {
        this.sourcePathRepository = sourcePathRepository;
    }

    /**
     * {@code POST  /source-paths} : Create a new sourcePath.
     *
     * @param sourcePath the sourcePath to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourcePath, or with status {@code 400 (Bad Request)} if the sourcePath has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/source-paths")
    public ResponseEntity<SourcePath> createSourcePath(@RequestBody SourcePath sourcePath) throws URISyntaxException {
        log.debug("REST request to save SourcePath : {}", sourcePath);
        if (sourcePath.getId() != null) {
            throw new BadRequestAlertException("A new sourcePath cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SourcePath result = sourcePathRepository.save(sourcePath);
        return ResponseEntity
            .created(new URI("/api/source-paths/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /source-paths/:id} : Updates an existing sourcePath.
     *
     * @param id the id of the sourcePath to save.
     * @param sourcePath the sourcePath to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourcePath,
     * or with status {@code 400 (Bad Request)} if the sourcePath is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourcePath couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/source-paths/{id}")
    public ResponseEntity<SourcePath> updateSourcePath(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SourcePath sourcePath
    ) throws URISyntaxException {
        log.debug("REST request to update SourcePath : {}, {}", id, sourcePath);
        if (sourcePath.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourcePath.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourcePathRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SourcePath result = sourcePathRepository.save(sourcePath);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourcePath.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /source-paths/:id} : Partial updates given fields of an existing sourcePath, field will ignore if it is null
     *
     * @param id the id of the sourcePath to save.
     * @param sourcePath the sourcePath to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourcePath,
     * or with status {@code 400 (Bad Request)} if the sourcePath is not valid,
     * or with status {@code 404 (Not Found)} if the sourcePath is not found,
     * or with status {@code 500 (Internal Server Error)} if the sourcePath couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/source-paths/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SourcePath> partialUpdateSourcePath(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SourcePath sourcePath
    ) throws URISyntaxException {
        log.debug("REST request to partial update SourcePath partially : {}, {}", id, sourcePath);
        if (sourcePath.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourcePath.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourcePathRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SourcePath> result = sourcePathRepository
            .findById(sourcePath.getId())
            .map(
                existingSourcePath -> {
                    if (sourcePath.getName() != null) {
                        existingSourcePath.setName(sourcePath.getName());
                    }

                    return existingSourcePath;
                }
            )
            .map(sourcePathRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourcePath.getId().toString())
        );
    }

    /**
     * {@code GET  /source-paths} : get all the sourcePaths.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sourcePaths in body.
     */
    @GetMapping("/source-paths")
    public List<SourcePath> getAllSourcePaths() {
        log.debug("REST request to get all SourcePaths");
        return sourcePathRepository.findAll();
    }

    /**
     * {@code GET  /source-paths/:id} : get the "id" sourcePath.
     *
     * @param id the id of the sourcePath to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourcePath, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/source-paths/{id}")
    public ResponseEntity<SourcePath> getSourcePath(@PathVariable Long id) {
        log.debug("REST request to get SourcePath : {}", id);
        Optional<SourcePath> sourcePath = sourcePathRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sourcePath);
    }

    /**
     * {@code DELETE  /source-paths/:id} : delete the "id" sourcePath.
     *
     * @param id the id of the sourcePath to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/source-paths/{id}")
    public ResponseEntity<Void> deleteSourcePath(@PathVariable Long id) {
        log.debug("REST request to delete SourcePath : {}", id);
        sourcePathRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
