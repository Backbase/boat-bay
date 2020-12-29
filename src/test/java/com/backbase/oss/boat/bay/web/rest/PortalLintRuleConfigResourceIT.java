package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.PortalLintRuleConfig;
import com.backbase.oss.boat.bay.repository.PortalLintRuleConfigRepository;

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
 * Integration tests for the {@link PortalLintRuleConfigResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PortalLintRuleConfigResourceIT {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private PortalLintRuleConfigRepository portalLintRuleConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPortalLintRuleConfigMockMvc;

    private PortalLintRuleConfig portalLintRuleConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PortalLintRuleConfig createEntity(EntityManager em) {
        PortalLintRuleConfig portalLintRuleConfig = new PortalLintRuleConfig()
            .path(DEFAULT_PATH)
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE);
        return portalLintRuleConfig;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PortalLintRuleConfig createUpdatedEntity(EntityManager em) {
        PortalLintRuleConfig portalLintRuleConfig = new PortalLintRuleConfig()
            .path(UPDATED_PATH)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE);
        return portalLintRuleConfig;
    }

    @BeforeEach
    public void initTest() {
        portalLintRuleConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createPortalLintRuleConfig() throws Exception {
        int databaseSizeBeforeCreate = portalLintRuleConfigRepository.findAll().size();
        // Create the PortalLintRuleConfig
        restPortalLintRuleConfigMockMvc.perform(post("/api/portal-lint-rule-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRuleConfig)))
            .andExpect(status().isCreated());

        // Validate the PortalLintRuleConfig in the database
        List<PortalLintRuleConfig> portalLintRuleConfigList = portalLintRuleConfigRepository.findAll();
        assertThat(portalLintRuleConfigList).hasSize(databaseSizeBeforeCreate + 1);
        PortalLintRuleConfig testPortalLintRuleConfig = portalLintRuleConfigList.get(portalLintRuleConfigList.size() - 1);
        assertThat(testPortalLintRuleConfig.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testPortalLintRuleConfig.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPortalLintRuleConfig.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createPortalLintRuleConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = portalLintRuleConfigRepository.findAll().size();

        // Create the PortalLintRuleConfig with an existing ID
        portalLintRuleConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortalLintRuleConfigMockMvc.perform(post("/api/portal-lint-rule-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRuleConfig)))
            .andExpect(status().isBadRequest());

        // Validate the PortalLintRuleConfig in the database
        List<PortalLintRuleConfig> portalLintRuleConfigList = portalLintRuleConfigRepository.findAll();
        assertThat(portalLintRuleConfigList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = portalLintRuleConfigRepository.findAll().size();
        // set the field null
        portalLintRuleConfig.setPath(null);

        // Create the PortalLintRuleConfig, which fails.


        restPortalLintRuleConfigMockMvc.perform(post("/api/portal-lint-rule-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRuleConfig)))
            .andExpect(status().isBadRequest());

        List<PortalLintRuleConfig> portalLintRuleConfigList = portalLintRuleConfigRepository.findAll();
        assertThat(portalLintRuleConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = portalLintRuleConfigRepository.findAll().size();
        // set the field null
        portalLintRuleConfig.setType(null);

        // Create the PortalLintRuleConfig, which fails.


        restPortalLintRuleConfigMockMvc.perform(post("/api/portal-lint-rule-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRuleConfig)))
            .andExpect(status().isBadRequest());

        List<PortalLintRuleConfig> portalLintRuleConfigList = portalLintRuleConfigRepository.findAll();
        assertThat(portalLintRuleConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPortalLintRuleConfigs() throws Exception {
        // Initialize the database
        portalLintRuleConfigRepository.saveAndFlush(portalLintRuleConfig);

        // Get all the portalLintRuleConfigList
        restPortalLintRuleConfigMockMvc.perform(get("/api/portal-lint-rule-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portalLintRuleConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }
    
    @Test
    @Transactional
    public void getPortalLintRuleConfig() throws Exception {
        // Initialize the database
        portalLintRuleConfigRepository.saveAndFlush(portalLintRuleConfig);

        // Get the portalLintRuleConfig
        restPortalLintRuleConfigMockMvc.perform(get("/api/portal-lint-rule-configs/{id}", portalLintRuleConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(portalLintRuleConfig.getId().intValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingPortalLintRuleConfig() throws Exception {
        // Get the portalLintRuleConfig
        restPortalLintRuleConfigMockMvc.perform(get("/api/portal-lint-rule-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePortalLintRuleConfig() throws Exception {
        // Initialize the database
        portalLintRuleConfigRepository.saveAndFlush(portalLintRuleConfig);

        int databaseSizeBeforeUpdate = portalLintRuleConfigRepository.findAll().size();

        // Update the portalLintRuleConfig
        PortalLintRuleConfig updatedPortalLintRuleConfig = portalLintRuleConfigRepository.findById(portalLintRuleConfig.getId()).get();
        // Disconnect from session so that the updates on updatedPortalLintRuleConfig are not directly saved in db
        em.detach(updatedPortalLintRuleConfig);
        updatedPortalLintRuleConfig
            .path(UPDATED_PATH)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE);

        restPortalLintRuleConfigMockMvc.perform(put("/api/portal-lint-rule-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPortalLintRuleConfig)))
            .andExpect(status().isOk());

        // Validate the PortalLintRuleConfig in the database
        List<PortalLintRuleConfig> portalLintRuleConfigList = portalLintRuleConfigRepository.findAll();
        assertThat(portalLintRuleConfigList).hasSize(databaseSizeBeforeUpdate);
        PortalLintRuleConfig testPortalLintRuleConfig = portalLintRuleConfigList.get(portalLintRuleConfigList.size() - 1);
        assertThat(testPortalLintRuleConfig.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testPortalLintRuleConfig.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPortalLintRuleConfig.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingPortalLintRuleConfig() throws Exception {
        int databaseSizeBeforeUpdate = portalLintRuleConfigRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortalLintRuleConfigMockMvc.perform(put("/api/portal-lint-rule-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRuleConfig)))
            .andExpect(status().isBadRequest());

        // Validate the PortalLintRuleConfig in the database
        List<PortalLintRuleConfig> portalLintRuleConfigList = portalLintRuleConfigRepository.findAll();
        assertThat(portalLintRuleConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePortalLintRuleConfig() throws Exception {
        // Initialize the database
        portalLintRuleConfigRepository.saveAndFlush(portalLintRuleConfig);

        int databaseSizeBeforeDelete = portalLintRuleConfigRepository.findAll().size();

        // Delete the portalLintRuleConfig
        restPortalLintRuleConfigMockMvc.perform(delete("/api/portal-lint-rule-configs/{id}", portalLintRuleConfig.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PortalLintRuleConfig> portalLintRuleConfigList = portalLintRuleConfigRepository.findAll();
        assertThat(portalLintRuleConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
