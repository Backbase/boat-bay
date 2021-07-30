package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.repository.ServiceDefinitionRepository;
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
    public ResponseEntity<ServiceDefinition> createServiceDefinition(@Valid @RequestBody ServiceDefinition serviceDefinition)
        throws URISyntaxException {
        log.debug("REST request to save ServiceDefinition : {}", serviceDefinition);
        if (serviceDefinition.getId() != null) {
            throw new BadRequestAlertException("A new serviceDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceDefinition result = serviceDefinitionRepository.save(serviceDefinition);
        return ResponseEntity
            .created(new URI("/api/service-definitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-definitions/:id} : Updates an existing serviceDefinition.
     *
     * @param id the id of the serviceDefinition to save.
     * @param serviceDefinition the serviceDefinition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceDefinition,
     * or with status {@code 400 (Bad Request)} if the serviceDefinition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceDefinition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-definitions/{id}")
    public ResponseEntity<ServiceDefinition> updateServiceDefinition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceDefinition serviceDefinition
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceDefinition : {}, {}", id, serviceDefinition);
        if (serviceDefinition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceDefinition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceDefinitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServiceDefinition result = serviceDefinitionRepository.save(serviceDefinition);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceDefinition.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-definitions/:id} : Partial updates given fields of an existing serviceDefinition, field will ignore if it is null
     *
     * @param id the id of the serviceDefinition to save.
     * @param serviceDefinition the serviceDefinition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceDefinition,
     * or with status {@code 400 (Bad Request)} if the serviceDefinition is not valid,
     * or with status {@code 404 (Not Found)} if the serviceDefinition is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceDefinition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-definitions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ServiceDefinition> partialUpdateServiceDefinition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceDefinition serviceDefinition
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceDefinition partially : {}, {}", id, serviceDefinition);
        if (serviceDefinition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceDefinition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceDefinitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceDefinition> result = serviceDefinitionRepository
            .findById(serviceDefinition.getId())
            .map(
                existingServiceDefinition -> {
                    if (serviceDefinition.getKey() != null) {
                        existingServiceDefinition.setKey(serviceDefinition.getKey());
                    }
                    if (serviceDefinition.getName() != null) {
                        existingServiceDefinition.setName(serviceDefinition.getName());
                    }
                    if (serviceDefinition.getOrder() != null) {
                        existingServiceDefinition.setOrder(serviceDefinition.getOrder());
                    }
                    if (serviceDefinition.getSubTitle() != null) {
                        existingServiceDefinition.setSubTitle(serviceDefinition.getSubTitle());
                    }
                    if (serviceDefinition.getDescription() != null) {
                        existingServiceDefinition.setDescription(serviceDefinition.getDescription());
                    }
                    if (serviceDefinition.getIcon() != null) {
                        existingServiceDefinition.setIcon(serviceDefinition.getIcon());
                    }
                    if (serviceDefinition.getColor() != null) {
                        existingServiceDefinition.setColor(serviceDefinition.getColor());
                    }
                    if (serviceDefinition.getCreatedOn() != null) {
                        existingServiceDefinition.setCreatedOn(serviceDefinition.getCreatedOn());
                    }
                    if (serviceDefinition.getCreatedBy() != null) {
                        existingServiceDefinition.setCreatedBy(serviceDefinition.getCreatedBy());
                    }
                    if (serviceDefinition.getHide() != null) {
                        existingServiceDefinition.setHide(serviceDefinition.getHide());
                    }

                    return existingServiceDefinition;
                }
            )
            .map(serviceDefinitionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceDefinition.getId().toString())
        );
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
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
