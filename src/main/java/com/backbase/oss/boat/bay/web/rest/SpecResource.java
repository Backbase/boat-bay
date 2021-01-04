package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.SpecRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        return ResponseEntity.created(new URI("/api/specs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specs} : Updates an existing spec.
     *
     * @param spec the spec to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spec,
     * or with status {@code 400 (Bad Request)} if the spec is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spec couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specs")
    public ResponseEntity<Spec> updateSpec(@Valid @RequestBody Spec spec) throws URISyntaxException {
        log.debug("REST request to update Spec : {}", spec);
        if (spec.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Spec result = specRepository.save(spec);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spec.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /specs} : get all the specs.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specs in body.
     */
    @GetMapping("/specs")
    public List<Spec> getAllSpecs(@RequestParam(required = false) String filter) {
        if ("lintreport-is-null".equals(filter)) {
            log.debug("REST request to get all Specs where lintReport is null");
            return StreamSupport
                .stream(specRepository.findAll().spliterator(), false)
                .filter(spec -> spec.getLintReport() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Specs");
        return specRepository.findAll();
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
        Optional<Spec> spec = specRepository.findById(id);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
