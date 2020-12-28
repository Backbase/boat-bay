package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.repository.ServiceDefinitionRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.ServiceDefinition}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ServiceDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(ServiceDefinitionResource.class);

    private static final String ENTITY_NAME = "serviceDefinition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceDefinitionRepository serviceDefinitionRepository;

    public ServiceDefinitionResource(ServiceDefinitionRepository serviceDefinitionRepository) {
        this.serviceDefinitionRepository = serviceDefinitionRepository;
    }

    /**
     * {@code POST  /service-definitions} : Create a new serviceDefinition.
     *
     * @param serviceDefinition the serviceDefinition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceDefinition, or with status {@code 400 (Bad Request)} if the serviceDefinition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-definitions")
    public ResponseEntity<ServiceDefinition> createServiceDefinition(@Valid @RequestBody ServiceDefinition serviceDefinition) throws URISyntaxException {
        log.debug("REST request to save ServiceDefinition : {}", serviceDefinition);
        if (serviceDefinition.getId() != null) {
            throw new BadRequestAlertException("A new serviceDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceDefinition result = serviceDefinitionRepository.save(serviceDefinition);
        return ResponseEntity.created(new URI("/api/service-definitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-definitions} : Updates an existing serviceDefinition.
     *
     * @param serviceDefinition the serviceDefinition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceDefinition,
     * or with status {@code 400 (Bad Request)} if the serviceDefinition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceDefinition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-definitions")
    public ResponseEntity<ServiceDefinition> updateServiceDefinition(@Valid @RequestBody ServiceDefinition serviceDefinition) throws URISyntaxException {
        log.debug("REST request to update ServiceDefinition : {}", serviceDefinition);
        if (serviceDefinition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceDefinition result = serviceDefinitionRepository.save(serviceDefinition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceDefinition.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /service-definitions} : get all the serviceDefinitions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceDefinitions in body.
     */
    @GetMapping("/service-definitions")
    public List<ServiceDefinition> getAllServiceDefinitions() {
        log.debug("REST request to get all ServiceDefinitions");
        return serviceDefinitionRepository.findAll();
    }

    /**
     * {@code GET  /service-definitions/:id} : get the "id" serviceDefinition.
     *
     * @param id the id of the serviceDefinition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceDefinition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-definitions/{id}")
    public ResponseEntity<ServiceDefinition> getServiceDefinition(@PathVariable Long id) {
        log.debug("REST request to get ServiceDefinition : {}", id);
        Optional<ServiceDefinition> serviceDefinition = serviceDefinitionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(serviceDefinition);
    }

    /**
     * {@code DELETE  /service-definitions/:id} : delete the "id" serviceDefinition.
     *
     * @param id the id of the serviceDefinition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-definitions/{id}")
    public ResponseEntity<Void> deleteServiceDefinition(@PathVariable Long id) {
        log.debug("REST request to delete ServiceDefinition : {}", id);
        serviceDefinitionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
