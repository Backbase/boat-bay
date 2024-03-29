package com.backbase.oss.boat.bay.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backbase.oss.boat.bay.IntegrationTest;
import com.backbase.oss.boat.bay.domain.ZallyConfig;
import com.backbase.oss.boat.bay.repository.ZallyConfigRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ZallyConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ZallyConfigResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENTS = "AAAAAAAAAA";
    private static final String UPDATED_CONTENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/zally-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ZallyConfigRepository zallyConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restZallyConfigMockMvc;

    private ZallyConfig zallyConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZallyConfig createEntity(EntityManager em) {
        ZallyConfig zallyConfig = new ZallyConfig().name(DEFAULT_NAME).contents(DEFAULT_CONTENTS);
        return zallyConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZallyConfig createUpdatedEntity(EntityManager em) {
        ZallyConfig zallyConfig = new ZallyConfig().name(UPDATED_NAME).contents(UPDATED_CONTENTS);
        return zallyConfig;
    }

    @BeforeEach
    public void initTest() {
        zallyConfig = createEntity(em);
    }

    @Test
    @Transactional
    void createZallyConfig() throws Exception {
        int databaseSizeBeforeCreate = zallyConfigRepository.findAll().size();
        // Create the ZallyConfig
        restZallyConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zallyConfig)))
            .andExpect(status().isCreated());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeCreate + 1);
        ZallyConfig testZallyConfig = zallyConfigList.get(zallyConfigList.size() - 1);
        assertThat(testZallyConfig.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testZallyConfig.getContents()).isEqualTo(DEFAULT_CONTENTS);
    }

    @Test
    @Transactional
    void createZallyConfigWithExistingId() throws Exception {
        // Create the ZallyConfig with an existing ID
        zallyConfig.setId(1L);

        int databaseSizeBeforeCreate = zallyConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restZallyConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zallyConfig)))
            .andExpect(status().isBadRequest());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = zallyConfigRepository.findAll().size();
        // set the field null
        zallyConfig.setName(null);

        // Create the ZallyConfig, which fails.

        restZallyConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zallyConfig)))
            .andExpect(status().isBadRequest());

        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllZallyConfigs() throws Exception {
        // Initialize the database
        zallyConfigRepository.saveAndFlush(zallyConfig);

        // Get all the zallyConfigList
        restZallyConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zallyConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contents").value(hasItem(DEFAULT_CONTENTS.toString())));
    }

    @Test
    @Transactional
    void getZallyConfig() throws Exception {
        // Initialize the database
        zallyConfigRepository.saveAndFlush(zallyConfig);

        // Get the zallyConfig
        restZallyConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, zallyConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zallyConfig.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contents").value(DEFAULT_CONTENTS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingZallyConfig() throws Exception {
        // Get the zallyConfig
        restZallyConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewZallyConfig() throws Exception {
        // Initialize the database
        zallyConfigRepository.saveAndFlush(zallyConfig);

        int databaseSizeBeforeUpdate = zallyConfigRepository.findAll().size();

        // Update the zallyConfig
        ZallyConfig updatedZallyConfig = zallyConfigRepository.findById(zallyConfig.getId()).get();
        // Disconnect from session so that the updates on updatedZallyConfig are not directly saved in db
        em.detach(updatedZallyConfig);
        updatedZallyConfig.name(UPDATED_NAME).contents(UPDATED_CONTENTS);

        restZallyConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedZallyConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedZallyConfig))
            )
            .andExpect(status().isOk());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeUpdate);
        ZallyConfig testZallyConfig = zallyConfigList.get(zallyConfigList.size() - 1);
        assertThat(testZallyConfig.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testZallyConfig.getContents()).isEqualTo(UPDATED_CONTENTS);
    }

    @Test
    @Transactional
    void putNonExistingZallyConfig() throws Exception {
        int databaseSizeBeforeUpdate = zallyConfigRepository.findAll().size();
        zallyConfig.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZallyConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zallyConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zallyConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchZallyConfig() throws Exception {
        int databaseSizeBeforeUpdate = zallyConfigRepository.findAll().size();
        zallyConfig.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZallyConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zallyConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamZallyConfig() throws Exception {
        int databaseSizeBeforeUpdate = zallyConfigRepository.findAll().size();
        zallyConfig.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZallyConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zallyConfig)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateZallyConfigWithPatch() throws Exception {
        // Initialize the database
        zallyConfigRepository.saveAndFlush(zallyConfig);

        int databaseSizeBeforeUpdate = zallyConfigRepository.findAll().size();

        // Update the zallyConfig using partial update
        ZallyConfig partialUpdatedZallyConfig = new ZallyConfig();
        partialUpdatedZallyConfig.setId(zallyConfig.getId());

        partialUpdatedZallyConfig.name(UPDATED_NAME);

        restZallyConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZallyConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZallyConfig))
            )
            .andExpect(status().isOk());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeUpdate);
        ZallyConfig testZallyConfig = zallyConfigList.get(zallyConfigList.size() - 1);
        assertThat(testZallyConfig.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testZallyConfig.getContents()).isEqualTo(DEFAULT_CONTENTS);
    }

    @Test
    @Transactional
    void fullUpdateZallyConfigWithPatch() throws Exception {
        // Initialize the database
        zallyConfigRepository.saveAndFlush(zallyConfig);

        int databaseSizeBeforeUpdate = zallyConfigRepository.findAll().size();

        // Update the zallyConfig using partial update
        ZallyConfig partialUpdatedZallyConfig = new ZallyConfig();
        partialUpdatedZallyConfig.setId(zallyConfig.getId());

        partialUpdatedZallyConfig.name(UPDATED_NAME).contents(UPDATED_CONTENTS);

        restZallyConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZallyConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZallyConfig))
            )
            .andExpect(status().isOk());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeUpdate);
        ZallyConfig testZallyConfig = zallyConfigList.get(zallyConfigList.size() - 1);
        assertThat(testZallyConfig.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testZallyConfig.getContents()).isEqualTo(UPDATED_CONTENTS);
    }

    @Test
    @Transactional
    void patchNonExistingZallyConfig() throws Exception {
        int databaseSizeBeforeUpdate = zallyConfigRepository.findAll().size();
        zallyConfig.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZallyConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, zallyConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zallyConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchZallyConfig() throws Exception {
        int databaseSizeBeforeUpdate = zallyConfigRepository.findAll().size();
        zallyConfig.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZallyConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zallyConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamZallyConfig() throws Exception {
        int databaseSizeBeforeUpdate = zallyConfigRepository.findAll().size();
        zallyConfig.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZallyConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(zallyConfig))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteZallyConfig() throws Exception {
        // Initialize the database
        zallyConfigRepository.saveAndFlush(zallyConfig);

        int databaseSizeBeforeDelete = zallyConfigRepository.findAll().size();

        // Delete the zallyConfig
        restZallyConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, zallyConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
