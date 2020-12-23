package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.LintRuleSet;
import com.backbase.oss.boat.bay.repository.LintRuleSetRepository;

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
 * Integration tests for the {@link LintRuleSetResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LintRuleSetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private LintRuleSetRepository lintRuleSetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLintRuleSetMockMvc;

    private LintRuleSet lintRuleSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LintRuleSet createEntity(EntityManager em) {
        LintRuleSet lintRuleSet = new LintRuleSet()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return lintRuleSet;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LintRuleSet createUpdatedEntity(EntityManager em) {
        LintRuleSet lintRuleSet = new LintRuleSet()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return lintRuleSet;
    }

    @BeforeEach
    public void initTest() {
        lintRuleSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createLintRuleSet() throws Exception {
        int databaseSizeBeforeCreate = lintRuleSetRepository.findAll().size();
        // Create the LintRuleSet
        restLintRuleSetMockMvc.perform(post("/api/lint-rule-sets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lintRuleSet)))
            .andExpect(status().isCreated());

        // Validate the LintRuleSet in the database
        List<LintRuleSet> lintRuleSetList = lintRuleSetRepository.findAll();
        assertThat(lintRuleSetList).hasSize(databaseSizeBeforeCreate + 1);
        LintRuleSet testLintRuleSet = lintRuleSetList.get(lintRuleSetList.size() - 1);
        assertThat(testLintRuleSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLintRuleSet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createLintRuleSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lintRuleSetRepository.findAll().size();

        // Create the LintRuleSet with an existing ID
        lintRuleSet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLintRuleSetMockMvc.perform(post("/api/lint-rule-sets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lintRuleSet)))
            .andExpect(status().isBadRequest());

        // Validate the LintRuleSet in the database
        List<LintRuleSet> lintRuleSetList = lintRuleSetRepository.findAll();
        assertThat(lintRuleSetList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lintRuleSetRepository.findAll().size();
        // set the field null
        lintRuleSet.setName(null);

        // Create the LintRuleSet, which fails.


        restLintRuleSetMockMvc.perform(post("/api/lint-rule-sets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lintRuleSet)))
            .andExpect(status().isBadRequest());

        List<LintRuleSet> lintRuleSetList = lintRuleSetRepository.findAll();
        assertThat(lintRuleSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLintRuleSets() throws Exception {
        // Initialize the database
        lintRuleSetRepository.saveAndFlush(lintRuleSet);

        // Get all the lintRuleSetList
        restLintRuleSetMockMvc.perform(get("/api/lint-rule-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lintRuleSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getLintRuleSet() throws Exception {
        // Initialize the database
        lintRuleSetRepository.saveAndFlush(lintRuleSet);

        // Get the lintRuleSet
        restLintRuleSetMockMvc.perform(get("/api/lint-rule-sets/{id}", lintRuleSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lintRuleSet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingLintRuleSet() throws Exception {
        // Get the lintRuleSet
        restLintRuleSetMockMvc.perform(get("/api/lint-rule-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLintRuleSet() throws Exception {
        // Initialize the database
        lintRuleSetRepository.saveAndFlush(lintRuleSet);

        int databaseSizeBeforeUpdate = lintRuleSetRepository.findAll().size();

        // Update the lintRuleSet
        LintRuleSet updatedLintRuleSet = lintRuleSetRepository.findById(lintRuleSet.getId()).get();
        // Disconnect from session so that the updates on updatedLintRuleSet are not directly saved in db
        em.detach(updatedLintRuleSet);
        updatedLintRuleSet
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restLintRuleSetMockMvc.perform(put("/api/lint-rule-sets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLintRuleSet)))
            .andExpect(status().isOk());

        // Validate the LintRuleSet in the database
        List<LintRuleSet> lintRuleSetList = lintRuleSetRepository.findAll();
        assertThat(lintRuleSetList).hasSize(databaseSizeBeforeUpdate);
        LintRuleSet testLintRuleSet = lintRuleSetList.get(lintRuleSetList.size() - 1);
        assertThat(testLintRuleSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLintRuleSet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingLintRuleSet() throws Exception {
        int databaseSizeBeforeUpdate = lintRuleSetRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLintRuleSetMockMvc.perform(put("/api/lint-rule-sets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lintRuleSet)))
            .andExpect(status().isBadRequest());

        // Validate the LintRuleSet in the database
        List<LintRuleSet> lintRuleSetList = lintRuleSetRepository.findAll();
        assertThat(lintRuleSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLintRuleSet() throws Exception {
        // Initialize the database
        lintRuleSetRepository.saveAndFlush(lintRuleSet);

        int databaseSizeBeforeDelete = lintRuleSetRepository.findAll().size();

        // Delete the lintRuleSet
        restLintRuleSetMockMvc.perform(delete("/api/lint-rule-sets/{id}", lintRuleSet.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LintRuleSet> lintRuleSetList = lintRuleSetRepository.findAll();
        assertThat(lintRuleSetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
