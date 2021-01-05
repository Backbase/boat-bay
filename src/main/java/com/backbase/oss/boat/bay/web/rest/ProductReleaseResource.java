package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.repository.ProductReleaseRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.ProductRelease}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductReleaseResource {

    private final Logger log = LoggerFactory.getLogger(ProductReleaseResource.class);

    private static final String ENTITY_NAME = "productRelease";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductReleaseRepository productReleaseRepository;

    public ProductReleaseResource(ProductReleaseRepository productReleaseRepository) {
        this.productReleaseRepository = productReleaseRepository;
    }

    /**
     * {@code POST  /product-releases} : Create a new productRelease.
     *
     * @param productRelease the productRelease to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productRelease, or with status {@code 400 (Bad Request)} if the productRelease has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-releases")
    public ResponseEntity<ProductRelease> createProductRelease(@Valid @RequestBody ProductRelease productRelease) throws URISyntaxException {
        log.debug("REST request to save ProductRelease : {}", productRelease);
        if (productRelease.getId() != null) {
            throw new BadRequestAlertException("A new productRelease cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductRelease result = productReleaseRepository.save(productRelease);
        return ResponseEntity.created(new URI("/api/product-releases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-releases} : Updates an existing productRelease.
     *
     * @param productRelease the productRelease to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productRelease,
     * or with status {@code 400 (Bad Request)} if the productRelease is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productRelease couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-releases")
    public ResponseEntity<ProductRelease> updateProductRelease(@Valid @RequestBody ProductRelease productRelease) throws URISyntaxException {
        log.debug("REST request to update ProductRelease : {}", productRelease);
        if (productRelease.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductRelease result = productReleaseRepository.save(productRelease);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productRelease.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /product-releases} : get all the productReleases.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productReleases in body.
     */
    @GetMapping("/product-releases")
    public List<ProductRelease> getAllProductReleases(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ProductReleases");
        return productReleaseRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /product-releases/:id} : get the "id" productRelease.
     *
     * @param id the id of the productRelease to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productRelease, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-releases/{id}")
    public ResponseEntity<ProductRelease> getProductRelease(@PathVariable Long id) {
        log.debug("REST request to get ProductRelease : {}", id);
        Optional<ProductRelease> productRelease = productReleaseRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(productRelease);
    }

    /**
     * {@code DELETE  /product-releases/:id} : delete the "id" productRelease.
     *
     * @param id the id of the productRelease to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-releases/{id}")
    public ResponseEntity<Void> deleteProductRelease(@PathVariable Long id) {
        log.debug("REST request to delete ProductRelease : {}", id);
        productReleaseRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
