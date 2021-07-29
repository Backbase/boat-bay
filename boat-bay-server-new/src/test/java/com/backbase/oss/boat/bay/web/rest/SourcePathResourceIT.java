package com.backbase.oss.boat.bay.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backbase.oss.boat.bay.IntegrationTest;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.repository.SourcePathRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SourcePathResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SourcePathResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/source-paths";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourcePathMockMvc;

    private SourcePath sourcePath;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourcePath createEntity(EntityManager em) {
        SourcePath sourcePath = new SourcePath().name(DEFAULT_NAME);
        return sourcePath;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourcePath createUpdatedEntity(EntityManager em) {
        SourcePath sourcePath = new SourcePath().name(UPDATED_NAME);
        return sourcePath;
    }

    @BeforeEach
    public void initTest() {
        sourcePath = createEntity(em);
    }

    @Test
    @Transactional
    void createSourcePath() throws Exception {
        int databaseSizeBeforeCreate = sourcePathRepository.findAll().size();
        // Create the SourcePath
        restSourcePathMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourcePath)))
            .andExpect(status().isCreated());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeCreate + 1);
        SourcePath testSourcePath = sourcePathList.get(sourcePathList.size() - 1);
        assertThat(testSourcePath.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSourcePathWithExistingId() throws Exception {
        // Create the SourcePath with an existing ID
        sourcePath.setId(1L);

        int databaseSizeBeforeCreate = sourcePathRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourcePathMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourcePath)))
            .andExpect(status().isBadRequest());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSourcePaths() throws Exception {
        // Initialize the database
        sourcePathRepository.saveAndFlush(sourcePath);

        // Get all the sourcePathList
        restSourcePathMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourcePath.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSourcePath() throws Exception {
        // Initialize the database
        sourcePathRepository.saveAndFlush(sourcePath);

        // Get the sourcePath
        restSourcePathMockMvc
            .perform(get(ENTITY_API_URL_ID, sourcePath.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sourcePath.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSourcePath() throws Exception {
        // Get the sourcePath
        restSourcePathMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSourcePath() throws Exception {
        // Initialize the database
        sourcePathRepository.saveAndFlush(sourcePath);

        int databaseSizeBeforeUpdate = sourcePathRepository.findAll().size();

        // Update the sourcePath
        SourcePath updatedSourcePath = sourcePathRepository.findById(sourcePath.getId()).get();
        // Disconnect from session so that the updates on updatedSourcePath are not directly saved in db
        em.detach(updatedSourcePath);
        updatedSourcePath.name(UPDATED_NAME);

        restSourcePathMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSourcePath.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSourcePath))
            )
            .andExpect(status().isOk());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeUpdate);
        SourcePath testSourcePath = sourcePathList.get(sourcePathList.size() - 1);
        assertThat(testSourcePath.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSourcePath() throws Exception {
        int databaseSizeBeforeUpdate = sourcePathRepository.findAll().size();
        sourcePath.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourcePathMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourcePath.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourcePath))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSourcePath() throws Exception {
        int databaseSizeBeforeUpdate = sourcePathRepository.findAll().size();
        sourcePath.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourcePathMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourcePath))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSourcePath() throws Exception {
        int databaseSizeBeforeUpdate = sourcePathRepository.findAll().size();
        sourcePath.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourcePathMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourcePath)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSourcePathWithPatch() throws Exception {
        // Initialize the database
        sourcePathRepository.saveAndFlush(sourcePath);

        int databaseSizeBeforeUpdate = sourcePathRepository.findAll().size();

        // Update the sourcePath using partial update
        SourcePath partialUpdatedSourcePath = new SourcePath();
        partialUpdatedSourcePath.setId(sourcePath.getId());

        partialUpdatedSourcePath.name(UPDATED_NAME);

        restSourcePathMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourcePath.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSourcePath))
            )
            .andExpect(status().isOk());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeUpdate);
        SourcePath testSourcePath = sourcePathList.get(sourcePathList.size() - 1);
        assertThat(testSourcePath.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSourcePathWithPatch() throws Exception {
        // Initialize the database
        sourcePathRepository.saveAndFlush(sourcePath);

        int databaseSizeBeforeUpdate = sourcePathRepository.findAll().size();

        // Update the sourcePath using partial update
        SourcePath partialUpdatedSourcePath = new SourcePath();
        partialUpdatedSourcePath.setId(sourcePath.getId());

        partialUpdatedSourcePath.name(UPDATED_NAME);

        restSourcePathMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourcePath.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSourcePath))
            )
            .andExpect(status().isOk());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeUpdate);
        SourcePath testSourcePath = sourcePathList.get(sourcePathList.size() - 1);
        assertThat(testSourcePath.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSourcePath() throws Exception {
        int databaseSizeBeforeUpdate = sourcePathRepository.findAll().size();
        sourcePath.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourcePathMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sourcePath.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourcePath))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSourcePath() throws Exception {
        int databaseSizeBeforeUpdate = sourcePathRepository.findAll().size();
        sourcePath.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourcePathMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourcePath))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSourcePath() throws Exception {
        int databaseSizeBeforeUpdate = sourcePathRepository.findAll().size();
        sourcePath.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourcePathMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sourcePath))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSourcePath() throws Exception {
        // Initialize the database
        sourcePathRepository.saveAndFlush(sourcePath);

        int databaseSizeBeforeDelete = sourcePathRepository.findAll().size();

        // Delete the sourcePath
        restSourcePathMockMvc
            .perform(delete(ENTITY_API_URL_ID, sourcePath.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
