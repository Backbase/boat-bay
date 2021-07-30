package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.Spec}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SpecResource {

    private final Logger log = LoggerFactory.getLogger(SpecResource.class);

    private static final String ENTITY_NAME = "spec";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecRepository specRepository;

    public SpecResource(SpecRepository specRepository) {
        this.specRepository = specRepository;
    }

    /**
     * {@code POST  /specs} : Create a new spec.
     *
     * @param spec the spec to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spec, or with status {@code 400 (Bad Request)} if the spec has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/specs")
    public ResponseEntity<Spec> createSpec(@Valid @RequestBody Spec spec) throws URISyntaxException {
        log.debug("REST request to save Spec : {}", spec);
        if (spec.getId() != null) {
            throw new BadRequestAlertException("A new spec cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Spec result = specRepository.save(spec);
        return ResponseEntity
            .created(new URI("/api/specs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specs/:id} : Updates an existing spec.
     *
     * @param id the id of the spec to save.
     * @param spec the spec to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spec,
     * or with status {@code 400 (Bad Request)} if the spec is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spec couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specs/{id}")
    public ResponseEntity<Spec> updateSpec(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Spec spec)
        throws URISyntaxException {
        log.debug("REST request to update Spec : {}, {}", id, spec);
        if (spec.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spec.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Spec result = specRepository.save(spec);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spec.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /specs/:id} : Partial updates given fields of an existing spec, field will ignore if it is null
     *
     * @param id the id of the spec to save.
     * @param spec the spec to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spec,
     * or with status {@code 400 (Bad Request)} if the spec is not valid,
     * or with status {@code 404 (Not Found)} if the spec is not found,
     * or with status {@code 500 (Internal Server Error)} if the spec couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/specs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Spec> partialUpdateSpec(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Spec spec
    ) throws URISyntaxException {
        log.debug("REST request to partial update Spec partially : {}, {}", id, spec);
        if (spec.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spec.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Spec> result = specRepository
            .findById(spec.getId())
            .map(
                existingSpec -> {
                    if (spec.getKey() != null) {
                        existingSpec.setKey(spec.getKey());
                    }
                    if (spec.getName() != null) {
                        existingSpec.setName(spec.getName());
                    }
                    if (spec.getVersion() != null) {
                        existingSpec.setVersion(spec.getVersion());
                    }
                    if (spec.getTitle() != null) {
                        existingSpec.setTitle(spec.getTitle());
                    }
                    if (spec.getIcon() != null) {
                        existingSpec.setIcon(spec.getIcon());
                    }
                    if (spec.getOpenApi() != null) {
                        existingSpec.setOpenApi(spec.getOpenApi());
                    }
                    if (spec.getDescription() != null) {
                        existingSpec.setDescription(spec.getDescription());
                    }
                    if (spec.getCreatedOn() != null) {
                        existingSpec.setCreatedOn(spec.getCreatedOn());
                    }
                    if (spec.getCreatedBy() != null) {
                        existingSpec.setCreatedBy(spec.getCreatedBy());
                    }
                    if (spec.getChecksum() != null) {
                        existingSpec.setChecksum(spec.getChecksum());
                    }
                    if (spec.getFilename() != null) {
                        existingSpec.setFilename(spec.getFilename());
                    }
                    if (spec.getValid() != null) {
                        existingSpec.setValid(spec.getValid());
                    }
                    if (spec.getOrder() != null) {
                        existingSpec.setOrder(spec.getOrder());
                    }
                    if (spec.getParseError() != null) {
                        existingSpec.setParseError(spec.getParseError());
                    }
                    if (spec.getExternalDocs() != null) {
                        existingSpec.setExternalDocs(spec.getExternalDocs());
                    }
                    if (spec.getHide() != null) {
                        existingSpec.setHide(spec.getHide());
                    }
                    if (spec.getGrade() != null) {
                        existingSpec.setGrade(spec.getGrade());
                    }
                    if (spec.getChanges() != null) {
                        existingSpec.setChanges(spec.getChanges());
                    }
                    if (spec.getSourcePath() != null) {
                        existingSpec.setSourcePath(spec.getSourcePath());
                    }
                    if (spec.getSourceName() != null) {
                        existingSpec.setSourceName(spec.getSourceName());
                    }
                    if (spec.getSourceUrl() != null) {
                        existingSpec.setSourceUrl(spec.getSourceUrl());
                    }
                    if (spec.getSourceCreatedBy() != null) {
                        existingSpec.setSourceCreatedBy(spec.getSourceCreatedBy());
                    }
                    if (spec.getSourceCreatedOn() != null) {
                        existingSpec.setSourceCreatedOn(spec.getSourceCreatedOn());
                    }
                    if (spec.getSourceLastModifiedOn() != null) {
                        existingSpec.setSourceLastModifiedOn(spec.getSourceLastModifiedOn());
                    }
                    if (spec.getSourceLastModifiedBy() != null) {
                        existingSpec.setSourceLastModifiedBy(spec.getSourceLastModifiedBy());
                    }
                    if (spec.getMvnGroupId() != null) {
                        existingSpec.setMvnGroupId(spec.getMvnGroupId());
                    }
                    if (spec.getMvnArtifactId() != null) {
                        existingSpec.setMvnArtifactId(spec.getMvnArtifactId());
                    }
                    if (spec.getMvnVersion() != null) {
                        existingSpec.setMvnVersion(spec.getMvnVersion());
                    }
                    if (spec.getMvnClassifier() != null) {
                        existingSpec.setMvnClassifier(spec.getMvnClassifier());
                    }
                    if (spec.getMvnExtension() != null) {
                        existingSpec.setMvnExtension(spec.getMvnExtension());
                    }

                    return existingSpec;
                }
            )
            .map(specRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spec.getId().toString())
        );
    }

    /**
     * {@code GET  /specs} : get all the specs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specs in body.
     */
    @GetMapping("/specs")
    public ResponseEntity<List<Spec>> getAllSpecs(
        Pageable pageable,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        if ("lintreport-is-null".equals(filter)) {
            log.debug("REST request to get all Specs where lintReport is null");
            return new ResponseEntity<>(
                StreamSupport
                    .stream(specRepository.findAll().spliterator(), false)
                    .filter(spec -> spec.getLintReport() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK
            );
        }

        if ("successor-is-null".equals(filter)) {
            log.debug("REST request to get all Specs where successor is null");
            return new ResponseEntity<>(
                StreamSupport
                    .stream(specRepository.findAll().spliterator(), false)
                    .filter(spec -> spec.getSuccessor() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK
            );
        }
        log.debug("REST request to get a page of Specs");
        Page<Spec> page;
        if (eagerload) {
            page = specRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = specRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /specs/:id} : get the "id" spec.
     *
     * @param id the id of the spec to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spec, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specs/{id}")
    public ResponseEntity<Spec> getSpec(@PathVariable Long id) {
        log.debug("REST request to get Spec : {}", id);
        Optional<Spec> spec = specRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(spec);
    }

    /**
     * {@code DELETE  /specs/:id} : delete the "id" spec.
     *
     * @param id the id of the spec to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/specs/{id}")
    public ResponseEntity<Void> deleteSpec(@PathVariable Long id) {
        log.debug("REST request to delete Spec : {}", id);
        specRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
