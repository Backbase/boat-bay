package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.ZallyConfig;
import com.backbase.oss.boat.bay.repository.ZallyConfigRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ZallyConfigResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ZallyConfigResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENTS = "AAAAAAAAAA";
    private static final String UPDATED_CONTENTS = "BBBBBBBBBB";

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
        ZallyConfig zallyConfig = new ZallyConfig()
            .name(DEFAULT_NAME)
            .contents(DEFAULT_CONTENTS);
        return zallyConfig;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZallyConfig createUpdatedEntity(EntityManager em) {
        ZallyConfig zallyConfig = new ZallyConfig()
            .name(UPDATED_NAME)
            .contents(UPDATED_CONTENTS);
        return zallyConfig;
    }

    @BeforeEach
    public void initTest() {
        zallyConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createZallyConfig() throws Exception {
        int databaseSizeBeforeCreate = zallyConfigRepository.findAll().size();
        // Create the ZallyConfig
        restZallyConfigMockMvc.perform(post("/api/zally-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zallyConfig)))
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
    public void createZallyConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = zallyConfigRepository.findAll().size();

        // Create the ZallyConfig with an existing ID
        zallyConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restZallyConfigMockMvc.perform(post("/api/zally-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zallyConfig)))
            .andExpect(status().isBadRequest());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = zallyConfigRepository.findAll().size();
        // set the field null
        zallyConfig.setName(null);

        // Create the ZallyConfig, which fails.


        restZallyConfigMockMvc.perform(post("/api/zally-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zallyConfig)))
            .andExpect(status().isBadRequest());

        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllZallyConfigs() throws Exception {
        // Initialize the database
        zallyConfigRepository.saveAndFlush(zallyConfig);

        // Get all the zallyConfigList
        restZallyConfigMockMvc.perform(get("/api/zally-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zallyConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contents").value(hasItem(DEFAULT_CONTENTS.toString())));
    }
    
    @Test
    @Transactional
    public void getZallyConfig() throws Exception {
        // Initialize the database
        zallyConfigRepository.saveAndFlush(zallyConfig);

        // Get the zallyConfig
        restZallyConfigMockMvc.perform(get("/api/zally-configs/{id}", zallyConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zallyConfig.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contents").value(DEFAULT_CONTENTS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingZallyConfig() throws Exception {
        // Get the zallyConfig
        restZallyConfigMockMvc.perform(get("/api/zally-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateZallyConfig() throws Exception {
        // Initialize the database
        zallyConfigRepository.saveAndFlush(zallyConfig);

        int databaseSizeBeforeUpdate = zallyConfigRepository.findAll().size();

        // Update the zallyConfig
        ZallyConfig updatedZallyConfig = zallyConfigRepository.findById(zallyConfig.getId()).get();
        // Disconnect from session so that the updates on updatedZallyConfig are not directly saved in db
        em.detach(updatedZallyConfig);
        updatedZallyConfig
            .name(UPDATED_NAME)
            .contents(UPDATED_CONTENTS);

        restZallyConfigMockMvc.perform(put("/api/zally-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedZallyConfig)))
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
    public void updateNonExistingZallyConfig() throws Exception {
        int databaseSizeBeforeUpdate = zallyConfigRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZallyConfigMockMvc.perform(put("/api/zally-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zallyConfig)))
            .andExpect(status().isBadRequest());

        // Validate the ZallyConfig in the database
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteZallyConfig() throws Exception {
        // Initialize the database
        zallyConfigRepository.saveAndFlush(zallyConfig);

        int databaseSizeBeforeDelete = zallyConfigRepository.findAll().size();

        // Delete the zallyConfig
        restZallyConfigMockMvc.perform(delete("/api/zally-configs/{id}", zallyConfig.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ZallyConfig> zallyConfigList = zallyConfigRepository.findAll();
        assertThat(zallyConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
