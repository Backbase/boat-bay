package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.CapabilityServiceDefinition;
import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.repository.CapabilityServiceDefinitionRepository;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CapabilityServiceDefinitionResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CapabilityServiceDefinitionResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_NAV_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_NAV_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    @Autowired
    private CapabilityServiceDefinitionRepository capabilityServiceDefinitionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCapabilityServiceDefinitionMockMvc;

    private CapabilityServiceDefinition capabilityServiceDefinition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CapabilityServiceDefinition createEntity(EntityManager em) {
        CapabilityServiceDefinition capabilityServiceDefinition = new CapabilityServiceDefinition()
            .key(DEFAULT_KEY)
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE)
            .subTitle(DEFAULT_SUB_TITLE)
            .navTitle(DEFAULT_NAV_TITLE)
            .content(DEFAULT_CONTENT)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY);
        // Add required entity
        Capability capability;
        if (TestUtil.findAll(em, Capability.class).isEmpty()) {
            capability = CapabilityResourceIT.createEntity(em);
            em.persist(capability);
            em.flush();
        } else {
            capability = TestUtil.findAll(em, Capability.class).get(0);
        }
        capabilityServiceDefinition.setCapability(capability);
        return capabilityServiceDefinition;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CapabilityServiceDefinition createUpdatedEntity(EntityManager em) {
        CapabilityServiceDefinition capabilityServiceDefinition = new CapabilityServiceDefinition()
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .navTitle(UPDATED_NAV_TITLE)
            .content(UPDATED_CONTENT)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY);
        // Add required entity
        Capability capability;
        if (TestUtil.findAll(em, Capability.class).isEmpty()) {
            capability = CapabilityResourceIT.createUpdatedEntity(em);
            em.persist(capability);
            em.flush();
        } else {
            capability = TestUtil.findAll(em, Capability.class).get(0);
        }
        capabilityServiceDefinition.setCapability(capability);
        return capabilityServiceDefinition;
    }

    @BeforeEach
    public void initTest() {
        capabilityServiceDefinition = createEntity(em);
    }

    @Test
    @Transactional
    public void createCapabilityServiceDefinition() throws Exception {
        int databaseSizeBeforeCreate = capabilityServiceDefinitionRepository.findAll().size();
        // Create the CapabilityServiceDefinition
        restCapabilityServiceDefinitionMockMvc.perform(post("/api/capability-service-definitions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(capabilityServiceDefinition)))
            .andExpect(status().isCreated());

        // Validate the CapabilityServiceDefinition in the database
        List<CapabilityServiceDefinition> capabilityServiceDefinitionList = capabilityServiceDefinitionRepository.findAll();
        assertThat(capabilityServiceDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
        CapabilityServiceDefinition testCapabilityServiceDefinition = capabilityServiceDefinitionList.get(capabilityServiceDefinitionList.size() - 1);
        assertThat(testCapabilityServiceDefinition.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testCapabilityServiceDefinition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCapabilityServiceDefinition.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCapabilityServiceDefinition.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testCapabilityServiceDefinition.getNavTitle()).isEqualTo(DEFAULT_NAV_TITLE);
        assertThat(testCapabilityServiceDefinition.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testCapabilityServiceDefinition.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testCapabilityServiceDefinition.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    public void createCapabilityServiceDefinitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = capabilityServiceDefinitionRepository.findAll().size();

        // Create the CapabilityServiceDefinition with an existing ID
        capabilityServiceDefinition.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCapabilityServiceDefinitionMockMvc.perform(post("/api/capability-service-definitions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(capabilityServiceDefinition)))
            .andExpect(status().isBadRequest());

        // Validate the CapabilityServiceDefinition in the database
        List<CapabilityServiceDefinition> capabilityServiceDefinitionList = capabilityServiceDefinitionRepository.findAll();
        assertThat(capabilityServiceDefinitionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = capabilityServiceDefinitionRepository.findAll().size();
        // set the field null
        capabilityServiceDefinition.setKey(null);

        // Create the CapabilityServiceDefinition, which fails.


        restCapabilityServiceDefinitionMockMvc.perform(post("/api/capability-service-definitions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(capabilityServiceDefinition)))
            .andExpect(status().isBadRequest());

        List<CapabilityServiceDefinition> capabilityServiceDefinitionList = capabilityServiceDefinitionRepository.findAll();
        assertThat(capabilityServiceDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = capabilityServiceDefinitionRepository.findAll().size();
        // set the field null
        capabilityServiceDefinition.setName(null);

        // Create the CapabilityServiceDefinition, which fails.


        restCapabilityServiceDefinitionMockMvc.perform(post("/api/capability-service-definitions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(capabilityServiceDefinition)))
            .andExpect(status().isBadRequest());

        List<CapabilityServiceDefinition> capabilityServiceDefinitionList = capabilityServiceDefinitionRepository.findAll();
        assertThat(capabilityServiceDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCapabilityServiceDefinitions() throws Exception {
        // Initialize the database
        capabilityServiceDefinitionRepository.saveAndFlush(capabilityServiceDefinition);

        // Get all the capabilityServiceDefinitionList
        restCapabilityServiceDefinitionMockMvc.perform(get("/api/capability-service-definitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(capabilityServiceDefinition.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].navTitle").value(hasItem(DEFAULT_NAV_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }
    
    @Test
    @Transactional
    public void getCapabilityServiceDefinition() throws Exception {
        // Initialize the database
        capabilityServiceDefinitionRepository.saveAndFlush(capabilityServiceDefinition);

        // Get the capabilityServiceDefinition
        restCapabilityServiceDefinitionMockMvc.perform(get("/api/capability-service-definitions/{id}", capabilityServiceDefinition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(capabilityServiceDefinition.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.navTitle").value(DEFAULT_NAV_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }
    @Test
    @Transactional
    public void getNonExistingCapabilityServiceDefinition() throws Exception {
        // Get the capabilityServiceDefinition
        restCapabilityServiceDefinitionMockMvc.perform(get("/api/capability-service-definitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCapabilityServiceDefinition() throws Exception {
        // Initialize the database
        capabilityServiceDefinitionRepository.saveAndFlush(capabilityServiceDefinition);

        int databaseSizeBeforeUpdate = capabilityServiceDefinitionRepository.findAll().size();

        // Update the capabilityServiceDefinition
        CapabilityServiceDefinition updatedCapabilityServiceDefinition = capabilityServiceDefinitionRepository.findById(capabilityServiceDefinition.getId()).get();
        // Disconnect from session so that the updates on updatedCapabilityServiceDefinition are not directly saved in db
        em.detach(updatedCapabilityServiceDefinition);
        updatedCapabilityServiceDefinition
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .navTitle(UPDATED_NAV_TITLE)
            .content(UPDATED_CONTENT)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY);

        restCapabilityServiceDefinitionMockMvc.perform(put("/api/capability-service-definitions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCapabilityServiceDefinition)))
            .andExpect(status().isOk());

        // Validate the CapabilityServiceDefinition in the database
        List<CapabilityServiceDefinition> capabilityServiceDefinitionList = capabilityServiceDefinitionRepository.findAll();
        assertThat(capabilityServiceDefinitionList).hasSize(databaseSizeBeforeUpdate);
        CapabilityServiceDefinition testCapabilityServiceDefinition = capabilityServiceDefinitionList.get(capabilityServiceDefinitionList.size() - 1);
        assertThat(testCapabilityServiceDefinition.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCapabilityServiceDefinition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCapabilityServiceDefinition.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCapabilityServiceDefinition.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testCapabilityServiceDefinition.getNavTitle()).isEqualTo(UPDATED_NAV_TITLE);
        assertThat(testCapabilityServiceDefinition.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testCapabilityServiceDefinition.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testCapabilityServiceDefinition.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingCapabilityServiceDefinition() throws Exception {
        int databaseSizeBeforeUpdate = capabilityServiceDefinitionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapabilityServiceDefinitionMockMvc.perform(put("/api/capability-service-definitions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(capabilityServiceDefinition)))
            .andExpect(status().isBadRequest());

        // Validate the CapabilityServiceDefinition in the database
        List<CapabilityServiceDefinition> capabilityServiceDefinitionList = capabilityServiceDefinitionRepository.findAll();
        assertThat(capabilityServiceDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCapabilityServiceDefinition() throws Exception {
        // Initialize the database
        capabilityServiceDefinitionRepository.saveAndFlush(capabilityServiceDefinition);

        int databaseSizeBeforeDelete = capabilityServiceDefinitionRepository.findAll().size();

        // Delete the capabilityServiceDefinition
        restCapabilityServiceDefinitionMockMvc.perform(delete("/api/capability-service-definitions/{id}", capabilityServiceDefinition.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CapabilityServiceDefinition> capabilityServiceDefinitionList = capabilityServiceDefinitionRepository.findAll();
        assertThat(capabilityServiceDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
