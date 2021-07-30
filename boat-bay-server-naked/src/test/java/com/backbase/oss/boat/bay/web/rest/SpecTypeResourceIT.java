package com.backbase.oss.boat.bay.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backbase.oss.boat.bay.IntegrationTest;
import com.backbase.oss.boat.bay.domain.SpecType;
import com.backbase.oss.boat.bay.repository.SpecTypeRepository;
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
 * Integration tests for the {@link SpecTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MATCH_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_MATCH_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/spec-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
    void createSpecType() throws Exception {
        int databaseSizeBeforeCreate = specTypeRepository.findAll().size();
        // Create the SpecType
        restSpecTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specType)))
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
    void createSpecTypeWithExistingId() throws Exception {
        // Create the SpecType with an existing ID
        specType.setId(1L);

        int databaseSizeBeforeCreate = specTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specType)))
            .andExpect(status().isBadRequest());

        // Validate the SpecType in the database
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = specTypeRepository.findAll().size();
        // set the field null
        specType.setName(null);

        // Create the SpecType, which fails.

        restSpecTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specType)))
            .andExpect(status().isBadRequest());

        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIconIsRequired() throws Exception {
        int databaseSizeBeforeTest = specTypeRepository.findAll().size();
        // set the field null
        specType.setIcon(null);

        // Create the SpecType, which fails.

        restSpecTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specType)))
            .andExpect(status().isBadRequest());

        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecTypes() throws Exception {
        // Initialize the database
        specTypeRepository.saveAndFlush(specType);

        // Get all the specTypeList
        restSpecTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
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
    void getSpecType() throws Exception {
        // Initialize the database
        specTypeRepository.saveAndFlush(specType);

        // Get the specType
        restSpecTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, specType.getId()))
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
    void getNonExistingSpecType() throws Exception {
        // Get the specType
        restSpecTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSpecType() throws Exception {
        // Initialize the database
        specTypeRepository.saveAndFlush(specType);

        int databaseSizeBeforeUpdate = specTypeRepository.findAll().size();

        // Update the specType
        SpecType updatedSpecType = specTypeRepository.findById(specType.getId()).get();
        // Disconnect from session so that the updates on updatedSpecType are not directly saved in db
        em.detach(updatedSpecType);
        updatedSpecType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).matchSpEL(UPDATED_MATCH_SP_EL).icon(UPDATED_ICON);

        restSpecTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpecType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpecType))
            )
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
    void putNonExistingSpecType() throws Exception {
        int databaseSizeBeforeUpdate = specTypeRepository.findAll().size();
        specType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecType in the database
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecType() throws Exception {
        int databaseSizeBeforeUpdate = specTypeRepository.findAll().size();
        specType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecType in the database
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecType() throws Exception {
        int databaseSizeBeforeUpdate = specTypeRepository.findAll().size();
        specType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecType in the database
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecTypeWithPatch() throws Exception {
        // Initialize the database
        specTypeRepository.saveAndFlush(specType);

        int databaseSizeBeforeUpdate = specTypeRepository.findAll().size();

        // Update the specType using partial update
        SpecType partialUpdatedSpecType = new SpecType();
        partialUpdatedSpecType.setId(specType.getId());

        partialUpdatedSpecType.name(UPDATED_NAME).matchSpEL(UPDATED_MATCH_SP_EL).icon(UPDATED_ICON);

        restSpecTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecType))
            )
            .andExpect(status().isOk());

        // Validate the SpecType in the database
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeUpdate);
        SpecType testSpecType = specTypeList.get(specTypeList.size() - 1);
        assertThat(testSpecType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpecType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpecType.getMatchSpEL()).isEqualTo(UPDATED_MATCH_SP_EL);
        assertThat(testSpecType.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    @Transactional
    void fullUpdateSpecTypeWithPatch() throws Exception {
        // Initialize the database
        specTypeRepository.saveAndFlush(specType);

        int databaseSizeBeforeUpdate = specTypeRepository.findAll().size();

        // Update the specType using partial update
        SpecType partialUpdatedSpecType = new SpecType();
        partialUpdatedSpecType.setId(specType.getId());

        partialUpdatedSpecType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).matchSpEL(UPDATED_MATCH_SP_EL).icon(UPDATED_ICON);

        restSpecTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecType))
            )
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
    void patchNonExistingSpecType() throws Exception {
        int databaseSizeBeforeUpdate = specTypeRepository.findAll().size();
        specType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecType in the database
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecType() throws Exception {
        int databaseSizeBeforeUpdate = specTypeRepository.findAll().size();
        specType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecType in the database
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecType() throws Exception {
        int databaseSizeBeforeUpdate = specTypeRepository.findAll().size();
        specType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(specType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecType in the database
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecType() throws Exception {
        // Initialize the database
        specTypeRepository.saveAndFlush(specType);

        int databaseSizeBeforeDelete = specTypeRepository.findAll().size();

        // Delete the specType
        restSpecTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, specType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpecType> specTypeList = specTypeRepository.findAll();
        assertThat(specTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
