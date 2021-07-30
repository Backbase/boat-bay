package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.repository.SourceRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.Source}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SourceResource {

    private final Logger log = LoggerFactory.getLogger(SourceResource.class);

    private static final String ENTITY_NAME = "source";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourceRepository sourceRepository;

    public SourceResource(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    /**
     * {@code POST  /sources} : Create a new source.
     *
     * @param source the source to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new source, or with status {@code 400 (Bad Request)} if the source has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sources")
    public ResponseEntity<Source> createSource(@Valid @RequestBody Source source) throws URISyntaxException {
        log.debug("REST request to save Source : {}", source);
        if (source.getId() != null) {
            throw new BadRequestAlertException("A new source cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Source result = sourceRepository.save(source);
        return ResponseEntity
            .created(new URI("/api/sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sources/:id} : Updates an existing source.
     *
     * @param id the id of the source to save.
     * @param source the source to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated source,
     * or with status {@code 400 (Bad Request)} if the source is not valid,
     * or with status {@code 500 (Internal Server Error)} if the source couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sources/{id}")
    public ResponseEntity<Source> updateSource(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Source source
    ) throws URISyntaxException {
        log.debug("REST request to update Source : {}, {}", id, source);
        if (source.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, source.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Source result = sourceRepository.save(source);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, source.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sources/:id} : Partial updates given fields of an existing source, field will ignore if it is null
     *
     * @param id the id of the source to save.
     * @param source the source to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated source,
     * or with status {@code 400 (Bad Request)} if the source is not valid,
     * or with status {@code 404 (Not Found)} if the source is not found,
     * or with status {@code 500 (Internal Server Error)} if the source couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sources/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Source> partialUpdateSource(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Source source
    ) throws URISyntaxException {
        log.debug("REST request to partial update Source partially : {}, {}", id, source);
        if (source.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, source.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Source> result = sourceRepository
            .findById(source.getId())
            .map(
                existingSource -> {
                    if (source.getName() != null) {
                        existingSource.setName(source.getName());
                    }
                    if (source.getKey() != null) {
                        existingSource.setKey(source.getKey());
                    }
                    if (source.getType() != null) {
                        existingSource.setType(source.getType());
                    }
                    if (source.getBaseUrl() != null) {
                        existingSource.setBaseUrl(source.getBaseUrl());
                    }
                    if (source.getActive() != null) {
                        existingSource.setActive(source.getActive());
                    }
                    if (source.getFilterArtifactsName() != null) {
                        existingSource.setFilterArtifactsName(source.getFilterArtifactsName());
                    }
                    if (source.getFilterArtifactsCreatedSince() != null) {
                        existingSource.setFilterArtifactsCreatedSince(source.getFilterArtifactsCreatedSince());
                    }
                    if (source.getUsername() != null) {
                        existingSource.setUsername(source.getUsername());
                    }
                    if (source.getPassword() != null) {
                        existingSource.setPassword(source.getPassword());
                    }
                    if (source.getCronExpression() != null) {
                        existingSource.setCronExpression(source.getCronExpression());
                    }
                    if (source.getRunOnStartup() != null) {
                        existingSource.setRunOnStartup(source.getRunOnStartup());
                    }
                    if (source.getSpecFilterSpEL() != null) {
                        existingSource.setSpecFilterSpEL(source.getSpecFilterSpEL());
                    }
                    if (source.getCapabilityKeySpEL() != null) {
                        existingSource.setCapabilityKeySpEL(source.getCapabilityKeySpEL());
                    }
                    if (source.getCapabilityNameSpEL() != null) {
                        existingSource.setCapabilityNameSpEL(source.getCapabilityNameSpEL());
                    }
                    if (source.getServiceKeySpEL() != null) {
                        existingSource.setServiceKeySpEL(source.getServiceKeySpEL());
                    }
                    if (source.getServiceNameSpEL() != null) {
                        existingSource.setServiceNameSpEL(source.getServiceNameSpEL());
                    }
                    if (source.getSpecKeySpEL() != null) {
                        existingSource.setSpecKeySpEL(source.getSpecKeySpEL());
                    }
                    if (source.getVersionSpEL() != null) {
                        existingSource.setVersionSpEL(source.getVersionSpEL());
                    }
                    if (source.getProductReleaseNameSpEL() != null) {
                        existingSource.setProductReleaseNameSpEL(source.getProductReleaseNameSpEL());
                    }
                    if (source.getProductReleaseVersionSpEL() != null) {
                        existingSource.setProductReleaseVersionSpEL(source.getProductReleaseVersionSpEL());
                    }
                    if (source.getProductReleaseKeySpEL() != null) {
                        existingSource.setProductReleaseKeySpEL(source.getProductReleaseKeySpEL());
                    }
                    if (source.getItemLimit() != null) {
                        existingSource.setItemLimit(source.getItemLimit());
                    }
                    if (source.getOverwriteChanges() != null) {
                        existingSource.setOverwriteChanges(source.getOverwriteChanges());
                    }
                    if (source.getOptions() != null) {
                        existingSource.setOptions(source.getOptions());
                    }

                    return existingSource;
                }
            )
            .map(sourceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, source.getId().toString())
        );
    }

    /**
     * {@code GET  /sources} : get all the sources.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sources in body.
     */
    @GetMapping("/sources")
    public List<Source> getAllSources() {
        log.debug("REST request to get all Sources");
        return sourceRepository.findAll();
    }

    /**
     * {@code GET  /sources/:id} : get the "id" source.
     *
     * @param id the id of the source to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the source, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sources/{id}")
    public ResponseEntity<Source> getSource(@PathVariable Long id) {
        log.debug("REST request to get Source : {}", id);
        Optional<Source> source = sourceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(source);
    }

    /**
     * {@code DELETE  /sources/:id} : delete the "id" source.
     *
     * @param id the id of the source to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sources/{id}")
    public ResponseEntity<Void> deleteSource(@PathVariable Long id) {
        log.debug("REST request to delete Source : {}", id);
        sourceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
