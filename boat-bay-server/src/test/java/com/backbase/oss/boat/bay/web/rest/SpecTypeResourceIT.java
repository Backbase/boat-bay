package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.SpecType;
import com.backbase.oss.boat.bay.repository.SpecTypeRepository;

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
 * Integration tests for the {@link SpecTypeResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SpecTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MATCH_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_MATCH_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    @Autowired
    private SpecTypeRepository specTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecTypeMockMvc;

    private SpecType specType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecType createEntity(EntityManager em) {
        SpecType specType = new SpecType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .matchSpEL(DEFAULT_MATCH_SP_EL)
            .icon(DEFAULT_ICON);
        return specType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecType createUpdatedEntity(EntityManager em) {
        SpecType specType = new SpecType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .matchSpEL(UPDATED_MATCH_SP_EL)
            .icon(UPDATED_ICON);
        return specType;
    }

    @BeforeEach
    public void initTest() {
        specType = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpecType() throws Exception {
        int databaseSizeBeforeCreate = specTypeRepository.findAll().size();
        // Create the SpecType
        restSpecTypeMockMvc.perform(post("/api/spec-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specType)))
            .andExpect(status().isCreated());

        // Validate the SpecType in the database
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SpecType testSpecType = specTypeList.get(specTypeList.size() - 1);
        assertThat(testSpecType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpecType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpecType.getMatchSpEL()).isEqualTo(DEFAULT_MATCH_SP_EL);
        assertThat(testSpecType.getIcon()).isEqualTo(DEFAULT_ICON);
    }

    @Test
    @Transactional
    public void createSpecTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = specTypeRepository.findAll().size();

        // Create the SpecType with an existing ID
        specType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecTypeMockMvc.perform(post("/api/spec-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specType)))
            .andExpect(status().isBadRequest());

        // Validate the SpecType in the database
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = specTypeRepository.findAll().size();
        // set the field null
        specType.setName(null);

        // Create the SpecType, which fails.


        restSpecTypeMockMvc.perform(post("/api/spec-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specType)))
            .andExpect(status().isBadRequest());

        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIconIsRequired() throws Exception {
        int databaseSizeBeforeTest = specTypeRepository.findAll().size();
        // set the field null
        specType.setIcon(null);

        // Create the SpecType, which fails.


        restSpecTypeMockMvc.perform(post("/api/spec-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specType)))
            .andExpect(status().isBadRequest());

        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSpecTypes() throws Exception {
        // Initialize the database
        specTypeRepository.saveAndFlush(specType);

        // Get all the specTypeList
        restSpecTypeMockMvc.perform(get("/api/spec-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].matchSpEL").value(hasItem(DEFAULT_MATCH_SP_EL)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)));
    }
    
    @Test
    @Transactional
    public void getSpecType() throws Exception {
        // Initialize the database
        specTypeRepository.saveAndFlush(specType);

        // Get the specType
        restSpecTypeMockMvc.perform(get("/api/spec-types/{id}", specType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.matchSpEL").value(DEFAULT_MATCH_SP_EL))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON));
    }
    @Test
    @Transactional
    public void getNonExistingSpecType() throws Exception {
        // Get the specType
        restSpecTypeMockMvc.perform(get("/api/spec-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpecType() throws Exception {
        // Initialize the database
        specTypeRepository.saveAndFlush(specType);

        int databaseSizeBeforeUpdate = specTypeRepository.findAll().size();

        // Update the specType
        SpecType updatedSpecType = specTypeRepository.findById(specType.getId()).get();
        // Disconnect from session so that the updates on updatedSpecType are not directly saved in db
        em.detach(updatedSpecType);
        updatedSpecType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .matchSpEL(UPDATED_MATCH_SP_EL)
            .icon(UPDATED_ICON);

        restSpecTypeMockMvc.perform(put("/api/spec-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpecType)))
            .andExpect(status().isOk());

        // Validate the SpecType in the database
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeUpdate);
        SpecType testSpecType = specTypeList.get(specTypeList.size() - 1);
        assertThat(testSpecType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpecType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecType.getMatchSpEL()).isEqualTo(UPDATED_MATCH_SP_EL);
        assertThat(testSpecType.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    @Transactional
    public void updateNonExistingSpecType() throws Exception {
        int databaseSizeBeforeUpdate = specTypeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecTypeMockMvc.perform(put("/api/spec-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specType)))
            .andExpect(status().isBadRequest());

        // Validate the SpecType in the database
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSpecType() throws Exception {
        // Initialize the database
        specTypeRepository.saveAndFlush(specType);

        int databaseSizeBeforeDelete = specTypeRepository.findAll().size();

        // Delete the specType
        restSpecTypeMockMvc.perform(delete("/api/spec-types/{id}", specType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
