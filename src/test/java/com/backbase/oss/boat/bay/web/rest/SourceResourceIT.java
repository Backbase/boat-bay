package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.SourceRepository;

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

import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
/**
 * Integration tests for the {@link SourceResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SourceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final SourceType DEFAULT_TYPE = SourceType.GIT;
    private static final SourceType UPDATED_TYPE = SourceType.JFROG;

    private static final String DEFAULT_BASE_URL = "AAAAAAAAAA";
    private static final String UPDATED_BASE_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FILTER = "AAAAAAAAAA";
    private static final String UPDATED_FILTER = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_CRON_EXPRESSION = "AAAAAAAAAA";
    private static final String UPDATED_CRON_EXPRESSION = "BBBBBBBBBB";

    private static final String DEFAULT_CAPABILITY_KEY_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_CAPABILITY_KEY_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_CAPABILITY_NAME_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_CAPABILITY_NAME_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_KEY_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_KEY_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_NAME_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_SP_EL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OVERWRITE_CHANGES = false;
    private static final Boolean UPDATED_OVERWRITE_CHANGES = true;

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourceMockMvc;

    private Source source;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Source createEntity(EntityManager em) {
        Source source = new Source()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .baseUrl(DEFAULT_BASE_URL)
            .active(DEFAULT_ACTIVE)
            .path(DEFAULT_PATH)
            .filter(DEFAULT_FILTER)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .cronExpression(DEFAULT_CRON_EXPRESSION)
            .capabilityKeySpEL(DEFAULT_CAPABILITY_KEY_SP_EL)
            .capabilityNameSpEL(DEFAULT_CAPABILITY_NAME_SP_EL)
            .serviceKeySpEL(DEFAULT_SERVICE_KEY_SP_EL)
            .serviceNameSpEL(DEFAULT_SERVICE_NAME_SP_EL)
            .versionSpEL(DEFAULT_VERSION_SP_EL)
            .overwriteChanges(DEFAULT_OVERWRITE_CHANGES);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        source.setPortal(portal);
        return source;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Source createUpdatedEntity(EntityManager em) {
        Source source = new Source()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .baseUrl(UPDATED_BASE_URL)
            .active(UPDATED_ACTIVE)
            .path(UPDATED_PATH)
            .filter(UPDATED_FILTER)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .cronExpression(UPDATED_CRON_EXPRESSION)
            .capabilityKeySpEL(UPDATED_CAPABILITY_KEY_SP_EL)
            .capabilityNameSpEL(UPDATED_CAPABILITY_NAME_SP_EL)
            .serviceKeySpEL(UPDATED_SERVICE_KEY_SP_EL)
            .serviceNameSpEL(UPDATED_SERVICE_NAME_SP_EL)
            .versionSpEL(UPDATED_VERSION_SP_EL)
            .overwriteChanges(UPDATED_OVERWRITE_CHANGES);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createUpdatedEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        source.setPortal(portal);
        return source;
    }

    @BeforeEach
    public void initTest() {
        source = createEntity(em);
    }

    @Test
    @Transactional
    public void createSource() throws Exception {
        int databaseSizeBeforeCreate = sourceRepository.findAll().size();
        // Create the Source
        restSourceMockMvc.perform(post("/api/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isCreated());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeCreate + 1);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSource.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSource.getBaseUrl()).isEqualTo(DEFAULT_BASE_URL);
        assertThat(testSource.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testSource.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testSource.getFilter()).isEqualTo(DEFAULT_FILTER);
        assertThat(testSource.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testSource.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testSource.getCronExpression()).isEqualTo(DEFAULT_CRON_EXPRESSION);
        assertThat(testSource.getCapabilityKeySpEL()).isEqualTo(DEFAULT_CAPABILITY_KEY_SP_EL);
        assertThat(testSource.getCapabilityNameSpEL()).isEqualTo(DEFAULT_CAPABILITY_NAME_SP_EL);
        assertThat(testSource.getServiceKeySpEL()).isEqualTo(DEFAULT_SERVICE_KEY_SP_EL);
        assertThat(testSource.getServiceNameSpEL()).isEqualTo(DEFAULT_SERVICE_NAME_SP_EL);
        assertThat(testSource.getVersionSpEL()).isEqualTo(DEFAULT_VERSION_SP_EL);
        assertThat(testSource.isOverwriteChanges()).isEqualTo(DEFAULT_OVERWRITE_CHANGES);
    }

    @Test
    @Transactional
    public void createSourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourceRepository.findAll().size();

        // Create the Source with an existing ID
        source.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceMockMvc.perform(post("/api/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceRepository.findAll().size();
        // set the field null
        source.setName(null);

        // Create the Source, which fails.


        restSourceMockMvc.perform(post("/api/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceRepository.findAll().size();
        // set the field null
        source.setType(null);

        // Create the Source, which fails.


        restSourceMockMvc.perform(post("/api/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBaseUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceRepository.findAll().size();
        // set the field null
        source.setBaseUrl(null);

        // Create the Source, which fails.


        restSourceMockMvc.perform(post("/api/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSources() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList
        restSourceMockMvc.perform(get("/api/sources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(source.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].baseUrl").value(hasItem(DEFAULT_BASE_URL)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].filter").value(hasItem(DEFAULT_FILTER)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].cronExpression").value(hasItem(DEFAULT_CRON_EXPRESSION)))
            .andExpect(jsonPath("$.[*].capabilityKeySpEL").value(hasItem(DEFAULT_CAPABILITY_KEY_SP_EL)))
            .andExpect(jsonPath("$.[*].capabilityNameSpEL").value(hasItem(DEFAULT_CAPABILITY_NAME_SP_EL)))
            .andExpect(jsonPath("$.[*].serviceKeySpEL").value(hasItem(DEFAULT_SERVICE_KEY_SP_EL)))
            .andExpect(jsonPath("$.[*].serviceNameSpEL").value(hasItem(DEFAULT_SERVICE_NAME_SP_EL)))
            .andExpect(jsonPath("$.[*].versionSpEL").value(hasItem(DEFAULT_VERSION_SP_EL)))
            .andExpect(jsonPath("$.[*].overwriteChanges").value(hasItem(DEFAULT_OVERWRITE_CHANGES.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get the source
        restSourceMockMvc.perform(get("/api/sources/{id}", source.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(source.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.baseUrl").value(DEFAULT_BASE_URL))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.filter").value(DEFAULT_FILTER))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.cronExpression").value(DEFAULT_CRON_EXPRESSION))
            .andExpect(jsonPath("$.capabilityKeySpEL").value(DEFAULT_CAPABILITY_KEY_SP_EL))
            .andExpect(jsonPath("$.capabilityNameSpEL").value(DEFAULT_CAPABILITY_NAME_SP_EL))
            .andExpect(jsonPath("$.serviceKeySpEL").value(DEFAULT_SERVICE_KEY_SP_EL))
            .andExpect(jsonPath("$.serviceNameSpEL").value(DEFAULT_SERVICE_NAME_SP_EL))
            .andExpect(jsonPath("$.versionSpEL").value(DEFAULT_VERSION_SP_EL))
            .andExpect(jsonPath("$.overwriteChanges").value(DEFAULT_OVERWRITE_CHANGES.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingSource() throws Exception {
        // Get the source
        restSourceMockMvc.perform(get("/api/sources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Update the source
        Source updatedSource = sourceRepository.findById(source.getId()).get();
        // Disconnect from session so that the updates on updatedSource are not directly saved in db
        em.detach(updatedSource);
        updatedSource
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .baseUrl(UPDATED_BASE_URL)
            .active(UPDATED_ACTIVE)
            .path(UPDATED_PATH)
            .filter(UPDATED_FILTER)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .cronExpression(UPDATED_CRON_EXPRESSION)
            .capabilityKeySpEL(UPDATED_CAPABILITY_KEY_SP_EL)
            .capabilityNameSpEL(UPDATED_CAPABILITY_NAME_SP_EL)
            .serviceKeySpEL(UPDATED_SERVICE_KEY_SP_EL)
            .serviceNameSpEL(UPDATED_SERVICE_NAME_SP_EL)
            .versionSpEL(UPDATED_VERSION_SP_EL)
            .overwriteChanges(UPDATED_OVERWRITE_CHANGES);

        restSourceMockMvc.perform(put("/api/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSource)))
            .andExpect(status().isOk());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSource.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSource.getBaseUrl()).isEqualTo(UPDATED_BASE_URL);
        assertThat(testSource.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testSource.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testSource.getFilter()).isEqualTo(UPDATED_FILTER);
        assertThat(testSource.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testSource.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSource.getCronExpression()).isEqualTo(UPDATED_CRON_EXPRESSION);
        assertThat(testSource.getCapabilityKeySpEL()).isEqualTo(UPDATED_CAPABILITY_KEY_SP_EL);
        assertThat(testSource.getCapabilityNameSpEL()).isEqualTo(UPDATED_CAPABILITY_NAME_SP_EL);
        assertThat(testSource.getServiceKeySpEL()).isEqualTo(UPDATED_SERVICE_KEY_SP_EL);
        assertThat(testSource.getServiceNameSpEL()).isEqualTo(UPDATED_SERVICE_NAME_SP_EL);
        assertThat(testSource.getVersionSpEL()).isEqualTo(UPDATED_VERSION_SP_EL);
        assertThat(testSource.isOverwriteChanges()).isEqualTo(UPDATED_OVERWRITE_CHANGES);
    }

    @Test
    @Transactional
    public void updateNonExistingSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceMockMvc.perform(put("/api/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeDelete = sourceRepository.findAll().size();

        // Delete the source
        restSourceMockMvc.perform(delete("/api/sources/{id}", source.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
