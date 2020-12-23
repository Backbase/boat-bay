package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.Upload;
import com.backbase.oss.boat.bay.repository.UploadRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.Upload}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UploadResource {

    private final Logger log = LoggerFactory.getLogger(UploadResource.class);

    private static final String ENTITY_NAME = "upload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UploadRepository uploadRepository;

    public UploadResource(UploadRepository uploadRepository) {
        this.uploadRepository = uploadRepository;
    }

    /**
     * {@code POST  /uploads} : Create a new upload.
     *
     * @param upload the upload to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new upload, or with status {@code 400 (Bad Request)} if the upload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/uploads")
    public ResponseEntity<Upload> createUpload(@RequestBody Upload upload) throws URISyntaxException {
        log.debug("REST request to save Upload : {}", upload);
        if (upload.getId() != null) {
            throw new BadRequestAlertException("A new upload cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Upload result = uploadRepository.save(upload);
        return ResponseEntity.created(new URI("/api/uploads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /uploads} : Updates an existing upload.
     *
     * @param upload the upload to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated upload,
     * or with status {@code 400 (Bad Request)} if the upload is not valid,
     * or with status {@code 500 (Internal Server Error)} if the upload couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/uploads")
    public ResponseEntity<Upload> updateUpload(@RequestBody Upload upload) throws URISyntaxException {
        log.debug("REST request to update Upload : {}", upload);
        if (upload.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Upload result = uploadRepository.save(upload);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, upload.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /uploads} : get all the uploads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uploads in body.
     */
    @GetMapping("/uploads")
    public List<Upload> getAllUploads() {
        log.debug("REST request to get all Uploads");
        return uploadRepository.findAll();
    }

    /**
     * {@code GET  /uploads/:id} : get the "id" upload.
     *
     * @param id the id of the upload to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the upload, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/uploads/{id}")
    public ResponseEntity<Upload> getUpload(@PathVariable Long id) {
        log.debug("REST request to get Upload : {}", id);
        Optional<Upload> upload = uploadRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(upload);
    }

    /**
     * {@code DELETE  /uploads/:id} : delete the "id" upload.
     *
     * @param id the id of the upload to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/uploads/{id}")
    public ResponseEntity<Void> deleteUpload(@PathVariable Long id) {
        log.debug("REST request to delete Upload : {}", id);
        uploadRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
