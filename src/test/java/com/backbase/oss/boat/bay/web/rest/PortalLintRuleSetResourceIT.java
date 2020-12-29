package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.PortalLintRuleSet;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.PortalLintRuleSetRepository;

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
 * Integration tests for the {@link PortalLintRuleSetResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PortalLintRuleSetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PortalLintRuleSetRepository portalLintRuleSetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPortalLintRuleSetMockMvc;

    private PortalLintRuleSet portalLintRuleSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PortalLintRuleSet createEntity(EntityManager em) {
        PortalLintRuleSet portalLintRuleSet = new PortalLintRuleSet()
            .name(DEFAULT_NAME);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        portalLintRuleSet.setPortal(portal);
        return portalLintRuleSet;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PortalLintRuleSet createUpdatedEntity(EntityManager em) {
        PortalLintRuleSet portalLintRuleSet = new PortalLintRuleSet()
            .name(UPDATED_NAME);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createUpdatedEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        portalLintRuleSet.setPortal(portal);
        return portalLintRuleSet;
    }

    @BeforeEach
    public void initTest() {
        portalLintRuleSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createPortalLintRuleSet() throws Exception {
        int databaseSizeBeforeCreate = portalLintRuleSetRepository.findAll().size();
        // Create the PortalLintRuleSet
        restPortalLintRuleSetMockMvc.perform(post("/api/portal-lint-rule-sets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRuleSet)))
            .andExpect(status().isCreated());

        // Validate the PortalLintRuleSet in the database
        List<PortalLintRuleSet> portalLintRuleSetList = portalLintRuleSetRepository.findAll();
        assertThat(portalLintRuleSetList).hasSize(databaseSizeBeforeCreate + 1);
        PortalLintRuleSet testPortalLintRuleSet = portalLintRuleSetList.get(portalLintRuleSetList.size() - 1);
        assertThat(testPortalLintRuleSet.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPortalLintRuleSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = portalLintRuleSetRepository.findAll().size();

        // Create the PortalLintRuleSet with an existing ID
        portalLintRuleSet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortalLintRuleSetMockMvc.perform(post("/api/portal-lint-rule-sets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRuleSet)))
            .andExpect(status().isBadRequest());

        // Validate the PortalLintRuleSet in the database
        List<PortalLintRuleSet> portalLintRuleSetList = portalLintRuleSetRepository.findAll();
        assertThat(portalLintRuleSetList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = portalLintRuleSetRepository.findAll().size();
        // set the field null
        portalLintRuleSet.setName(null);

        // Create the PortalLintRuleSet, which fails.


        restPortalLintRuleSetMockMvc.perform(post("/api/portal-lint-rule-sets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRuleSet)))
            .andExpect(status().isBadRequest());

        List<PortalLintRuleSet> portalLintRuleSetList = portalLintRuleSetRepository.findAll();
        assertThat(portalLintRuleSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPortalLintRuleSets() throws Exception {
        // Initialize the database
        portalLintRuleSetRepository.saveAndFlush(portalLintRuleSet);

        // Get all the portalLintRuleSetList
        restPortalLintRuleSetMockMvc.perform(get("/api/portal-lint-rule-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portalLintRuleSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getPortalLintRuleSet() throws Exception {
        // Initialize the database
        portalLintRuleSetRepository.saveAndFlush(portalLintRuleSet);

        // Get the portalLintRuleSet
        restPortalLintRuleSetMockMvc.perform(get("/api/portal-lint-rule-sets/{id}", portalLintRuleSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(portalLintRuleSet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingPortalLintRuleSet() throws Exception {
        // Get the portalLintRuleSet
        restPortalLintRuleSetMockMvc.perform(get("/api/portal-lint-rule-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePortalLintRuleSet() throws Exception {
        // Initialize the database
        portalLintRuleSetRepository.saveAndFlush(portalLintRuleSet);

        int databaseSizeBeforeUpdate = portalLintRuleSetRepository.findAll().size();

        // Update the portalLintRuleSet
        PortalLintRuleSet updatedPortalLintRuleSet = portalLintRuleSetRepository.findById(portalLintRuleSet.getId()).get();
        // Disconnect from session so that the updates on updatedPortalLintRuleSet are not directly saved in db
        em.detach(updatedPortalLintRuleSet);
        updatedPortalLintRuleSet
            .name(UPDATED_NAME);

        restPortalLintRuleSetMockMvc.perform(put("/api/portal-lint-rule-sets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPortalLintRuleSet)))
            .andExpect(status().isOk());

        // Validate the PortalLintRuleSet in the database
        List<PortalLintRuleSet> portalLintRuleSetList = portalLintRuleSetRepository.findAll();
        assertThat(portalLintRuleSetList).hasSize(databaseSizeBeforeUpdate);
        PortalLintRuleSet testPortalLintRuleSet = portalLintRuleSetList.get(portalLintRuleSetList.size() - 1);
        assertThat(testPortalLintRuleSet.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPortalLintRuleSet() throws Exception {
        int databaseSizeBeforeUpdate = portalLintRuleSetRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortalLintRuleSetMockMvc.perform(put("/api/portal-lint-rule-sets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRuleSet)))
            .andExpect(status().isBadRequest());

        // Validate the PortalLintRuleSet in the database
        List<PortalLintRuleSet> portalLintRuleSetList = portalLintRuleSetRepository.findAll();
        assertThat(portalLintRuleSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePortalLintRuleSet() throws Exception {
        // Initialize the database
        portalLintRuleSetRepository.saveAndFlush(portalLintRuleSet);

        int databaseSizeBeforeDelete = portalLintRuleSetRepository.findAll().size();

        // Delete the portalLintRuleSet
        restPortalLintRuleSetMockMvc.perform(delete("/api/portal-lint-rule-sets/{id}", portalLintRuleSet.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PortalLintRuleSet> portalLintRuleSetList = portalLintRuleSetRepository.findAll();
        assertThat(portalLintRuleSetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
