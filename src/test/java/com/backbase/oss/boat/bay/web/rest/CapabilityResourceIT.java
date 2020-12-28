package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CapabilityResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CapabilityResourceIT {

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

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    @Autowired
    private CapabilityRepository capabilityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCapabilityMockMvc;

    private Capability capability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Capability createEntity(EntityManager em) {
        Capability capability = new Capability()
            .key(DEFAULT_KEY)
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE)
            .subTitle(DEFAULT_SUB_TITLE)
            .navTitle(DEFAULT_NAV_TITLE)
            .content(DEFAULT_CONTENT)
            .version(DEFAULT_VERSION)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        capability.setPortal(portal);
        return capability;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Capability createUpdatedEntity(EntityManager em) {
        Capability capability = new Capability()
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .navTitle(UPDATED_NAV_TITLE)
            .content(UPDATED_CONTENT)
            .version(UPDATED_VERSION)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createUpdatedEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        capability.setPortal(portal);
        return capability;
    }

    @BeforeEach
    public void initTest() {
        capability = createEntity(em);
    }

    @Test
    @Transactional
    public void createCapability() throws Exception {
        int databaseSizeBeforeCreate = capabilityRepository.findAll().size();
        // Create the Capability
        restCapabilityMockMvc.perform(post("/api/capabilities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isCreated());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeCreate + 1);
        Capability testCapability = capabilityList.get(capabilityList.size() - 1);
        assertThat(testCapability.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testCapability.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCapability.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCapability.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testCapability.getNavTitle()).isEqualTo(DEFAULT_NAV_TITLE);
        assertThat(testCapability.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testCapability.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testCapability.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testCapability.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    public void createCapabilityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = capabilityRepository.findAll().size();

        // Create the Capability with an existing ID
        capability.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCapabilityMockMvc.perform(post("/api/capabilities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isBadRequest());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = capabilityRepository.findAll().size();
        // set the field null
        capability.setKey(null);

        // Create the Capability, which fails.


        restCapabilityMockMvc.perform(post("/api/capabilities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isBadRequest());

        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = capabilityRepository.findAll().size();
        // set the field null
        capability.setName(null);

        // Create the Capability, which fails.


        restCapabilityMockMvc.perform(post("/api/capabilities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isBadRequest());

        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCapabilities() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        // Get all the capabilityList
        restCapabilityMockMvc.perform(get("/api/capabilities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(capability.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].navTitle").value(hasItem(DEFAULT_NAV_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }
    
    @Test
    @Transactional
    public void getCapability() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        // Get the capability
        restCapabilityMockMvc.perform(get("/api/capabilities/{id}", capability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(capability.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.navTitle").value(DEFAULT_NAV_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }
    @Test
    @Transactional
    public void getNonExistingCapability() throws Exception {
        // Get the capability
        restCapabilityMockMvc.perform(get("/api/capabilities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCapability() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();

        // Update the capability
        Capability updatedCapability = capabilityRepository.findById(capability.getId()).get();
        // Disconnect from session so that the updates on updatedCapability are not directly saved in db
        em.detach(updatedCapability);
        updatedCapability
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .navTitle(UPDATED_NAV_TITLE)
            .content(UPDATED_CONTENT)
            .version(UPDATED_VERSION)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY);

        restCapabilityMockMvc.perform(put("/api/capabilities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCapability)))
            .andExpect(status().isOk());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
        Capability testCapability = capabilityList.get(capabilityList.size() - 1);
        assertThat(testCapability.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCapability.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCapability.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCapability.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testCapability.getNavTitle()).isEqualTo(UPDATED_NAV_TITLE);
        assertThat(testCapability.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testCapability.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testCapability.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testCapability.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapabilityMockMvc.perform(put("/api/capabilities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isBadRequest());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCapability() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        int databaseSizeBeforeDelete = capabilityRepository.findAll().size();

        // Delete the capability
        restCapabilityMockMvc.perform(delete("/api/capabilities/{id}", capability.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
