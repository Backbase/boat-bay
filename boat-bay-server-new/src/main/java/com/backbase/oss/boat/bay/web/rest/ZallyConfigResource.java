package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.ZallyConfig;
import com.backbase.oss.boat.bay.repository.ZallyConfigRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.ZallyConfig}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ZallyConfigResource {

    private final Logger log = LoggerFactory.getLogger(ZallyConfigResource.class);

    private static final String ENTITY_NAME = "zallyConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ZallyConfigRepository zallyConfigRepository;

    public ZallyConfigResource(ZallyConfigRepository zallyConfigRepository) {
        this.zallyConfigRepository = zallyConfigRepository;
    }

    /**
     * {@code POST  /zally-configs} : Create a new zallyConfig.
     *
     * @param zallyConfig the zallyConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new zallyConfig, or with status {@code 400 (Bad Request)} if the zallyConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/zally-configs")
    public ResponseEntity<ZallyConfig> createZallyConfig(@Valid @RequestBody ZallyConfig zallyConfig) throws URISyntaxException {
        log.debug("REST request to save ZallyConfig : {}", zallyConfig);
        if (zallyConfig.getId() != null) {
            throw new BadRequestAlertException("A new zallyConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ZallyConfig result = zallyConfigRepository.save(zallyConfig);
        return ResponseEntity
            .created(new URI("/api/zally-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /zally-configs/:id} : Updates an existing zallyConfig.
     *
     * @param id the id of the zallyConfig to save.
     * @param zallyConfig the zallyConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zallyConfig,
     * or with status {@code 400 (Bad Request)} if the zallyConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the zallyConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/zally-configs/{id}")
    public ResponseEntity<ZallyConfig> updateZallyConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ZallyConfig zallyConfig
    ) throws URISyntaxException {
        log.debug("REST request to update ZallyConfig : {}, {}", id, zallyConfig);
        if (zallyConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zallyConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zallyConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ZallyConfig result = zallyConfigRepository.save(zallyConfig);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, zallyConfig.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /zally-configs/:id} : Partial updates given fields of an existing zallyConfig, field will ignore if it is null
     *
     * @param id the id of the zallyConfig to save.
     * @param zallyConfig the zallyConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zallyConfig,
     * or with status {@code 400 (Bad Request)} if the zallyConfig is not valid,
     * or with status {@code 404 (Not Found)} if the zallyConfig is not found,
     * or with status {@code 500 (Internal Server Error)} if the zallyConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/zally-configs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ZallyConfig> partialUpdateZallyConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ZallyConfig zallyConfig
    ) throws URISyntaxException {
        log.debug("REST request to partial update ZallyConfig partially : {}, {}", id, zallyConfig);
        if (zallyConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zallyConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zallyConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ZallyConfig> result = zallyConfigRepository
            .findById(zallyConfig.getId())
            .map(
                existingZallyConfig -> {
                    if (zallyConfig.getName() != null) {
                        existingZallyConfig.setName(zallyConfig.getName());
                    }
                    if (zallyConfig.getContents() != null) {
                        existingZallyConfig.setContents(zallyConfig.getContents());
                    }

                    return existingZallyConfig;
                }
            )
            .map(zallyConfigRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, zallyConfig.getId().toString())
        );
    }

    /**
     * {@code GET  /zally-configs} : get all the zallyConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of zallyConfigs in body.
     */
    @GetMapping("/zally-configs")
    public List<ZallyConfig> getAllZallyConfigs() {
        log.debug("REST request to get all ZallyConfigs");
        return zallyConfigRepository.findAll();
    }

    /**
     * {@code GET  /zally-configs/:id} : get the "id" zallyConfig.
     *
     * @param id the id of the zallyConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the zallyConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/zally-configs/{id}")
    public ResponseEntity<ZallyConfig> getZallyConfig(@PathVariable Long id) {
        log.debug("REST request to get ZallyConfig : {}", id);
        Optional<ZallyConfig> zallyConfig = zallyConfigRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(zallyConfig);
    }

    /**
     * {@code DELETE  /zally-configs/:id} : delete the "id" zallyConfig.
     *
     * @param id the id of the zallyConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/zally-configs/{id}")
    public ResponseEntity<Void> deleteZallyConfig(@PathVariable Long id) {
        log.debug("REST request to delete ZallyConfig : {}", id);
        zallyConfigRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
