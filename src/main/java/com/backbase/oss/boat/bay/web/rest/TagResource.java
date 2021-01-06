package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.repository.TagRepository;
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
 * REST controller for managing {@link com.backbase.oss.boat.bay.domain.Tag}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TagResource {

    private final Logger log = LoggerFactory.getLogger(TagResource.class);

    private static final String ENTITY_NAME = "tag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TagRepository tagRepository;

    public TagResource(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * {@code POST  /tags} : Create a new tag.
     *
     * @param tag the tag to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tag, or with status {@code 400 (Bad Request)} if the tag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tags")
    public ResponseEntity<Tag> createTag(@Valid @RequestBody Tag tag) throws URISyntaxException {
        log.debug("REST request to save Tag : {}", tag);
        if (tag.getId() != null) {
            throw new BadRequestAlertException("A new tag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tag result = tagRepository.save(tag);
        return ResponseEntity.created(new URI("/api/tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tags} : Updates an existing tag.
     *
     * @param tag the tag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tag,
     * or with status {@code 400 (Bad Request)} if the tag is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tags")
    public ResponseEntity<Tag> updateTag(@Valid @RequestBody Tag tag) throws URISyntaxException {
        log.debug("REST request to update Tag : {}", tag);
        if (tag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Tag result = tagRepository.save(tag);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tag.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tags} : get all the tags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tags in body.
     */
    @GetMapping("/tags")
    public List<Tag> getAllTags() {
        log.debug("REST request to get all Tags");
        return tagRepository.findAll();
    }

    /**
     * {@code GET  /tags/:id} : get the "id" tag.
     *
     * @param id the id of the tag to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tag, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tags/{id}")
    public ResponseEntity<Tag> getTag(@PathVariable Long id) {
        log.debug("REST request to get Tag : {}", id);
        Optional<Tag> tag = tagRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tag);
    }

    /**
     * {@code DELETE  /tags/:id} : delete the "id" tag.
     *
     * @param id the id of the tag to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        log.debug("REST request to delete Tag : {}", id);
        tagRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
