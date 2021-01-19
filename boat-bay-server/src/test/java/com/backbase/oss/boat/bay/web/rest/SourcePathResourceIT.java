package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.repository.SourcePathRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SourcePathResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SourcePathResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

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
        SourcePath sourcePath = new SourcePath()
            .name(DEFAULT_NAME);
        return sourcePath;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourcePath createUpdatedEntity(EntityManager em) {
        SourcePath sourcePath = new SourcePath()
            .name(UPDATED_NAME);
        return sourcePath;
    }

    @BeforeEach
    public void initTest() {
        sourcePath = createEntity(em);
    }

    @Test
    @Transactional
    public void createSourcePath() throws Exception {
        int databaseSizeBeforeCreate = sourcePathRepository.findAll().size();
        // Create the SourcePath
        restSourcePathMockMvc.perform(post("/api/source-paths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sourcePath)))
            .andExpect(status().isCreated());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeCreate + 1);
        SourcePath testSourcePath = sourcePathList.get(sourcePathList.size() - 1);
        assertThat(testSourcePath.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSourcePathWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourcePathRepository.findAll().size();

        // Create the SourcePath with an existing ID
        sourcePath.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourcePathMockMvc.perform(post("/api/source-paths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sourcePath)))
            .andExpect(status().isBadRequest());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSourcePaths() throws Exception {
        // Initialize the database
        sourcePathRepository.saveAndFlush(sourcePath);

        // Get all the sourcePathList
        restSourcePathMockMvc.perform(get("/api/source-paths?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourcePath.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getSourcePath() throws Exception {
        // Initialize the database
        sourcePathRepository.saveAndFlush(sourcePath);

        // Get the sourcePath
        restSourcePathMockMvc.perform(get("/api/source-paths/{id}", sourcePath.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sourcePath.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingSourcePath() throws Exception {
        // Get the sourcePath
        restSourcePathMockMvc.perform(get("/api/source-paths/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSourcePath() throws Exception {
        // Initialize the database
        sourcePathRepository.saveAndFlush(sourcePath);

        int databaseSizeBeforeUpdate = sourcePathRepository.findAll().size();

        // Update the sourcePath
        SourcePath updatedSourcePath = sourcePathRepository.findById(sourcePath.getId()).get();
        // Disconnect from session so that the updates on updatedSourcePath are not directly saved in db
        em.detach(updatedSourcePath);
        updatedSourcePath
            .name(UPDATED_NAME);

        restSourcePathMockMvc.perform(put("/api/source-paths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSourcePath)))
            .andExpect(status().isOk());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeUpdate);
        SourcePath testSourcePath = sourcePathList.get(sourcePathList.size() - 1);
        assertThat(testSourcePath.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSourcePath() throws Exception {
        int databaseSizeBeforeUpdate = sourcePathRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourcePathMockMvc.perform(put("/api/source-paths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sourcePath)))
            .andExpect(status().isBadRequest());

        // Validate the SourcePath in the database
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSourcePath() throws Exception {
        // Initialize the database
        sourcePathRepository.saveAndFlush(sourcePath);

        int databaseSizeBeforeDelete = sourcePathRepository.findAll().size();

        // Delete the sourcePath
        restSourcePathMockMvc.perform(delete("/api/source-paths/{id}", sourcePath.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SourcePath> sourcePathList = sourcePathRepository.findAll();
        assertThat(sourcePathList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
