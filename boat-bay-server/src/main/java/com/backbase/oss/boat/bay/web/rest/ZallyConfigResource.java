package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.ZallyConfig;
import com.backbase.oss.boat.bay.repository.ZallyConfigRepository;
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
        return ResponseEntity.created(new URI("/api/zally-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /zally-configs} : Updates an existing zallyConfig.
     *
     * @param zallyConfig the zallyConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zallyConfig,
     * or with status {@code 400 (Bad Request)} if the zallyConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the zallyConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/zally-configs")
    public ResponseEntity<ZallyConfig> updateZallyConfig(@Valid @RequestBody ZallyConfig zallyConfig) throws URISyntaxException {
        log.debug("REST request to update ZallyConfig : {}", zallyConfig);
        if (zallyConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ZallyConfig result = zallyConfigRepository.save(zallyConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, zallyConfig.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
