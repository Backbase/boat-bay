package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.repository.SourcePathRepository;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
        return ResponseEntity.created(new URI("/api/source-paths/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /source-paths} : Updates an existing sourcePath.
     *
     * @param sourcePath the sourcePath to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourcePath,
     * or with status {@code 400 (Bad Request)} if the sourcePath is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourcePath couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/source-paths")
    public ResponseEntity<SourcePath> updateSourcePath(@RequestBody SourcePath sourcePath) throws URISyntaxException {
        log.debug("REST request to update SourcePath : {}", sourcePath);
        if (sourcePath.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SourcePath result = sourcePathRepository.save(sourcePath);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sourcePath.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
