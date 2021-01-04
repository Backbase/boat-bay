package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.LintRuleViolation;
import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.repository.LintRuleViolationRepository;

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

import com.backbase.oss.boat.bay.domain.enumeration.Severity;
/**
 * Integration tests for the {@link LintRuleViolationResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LintRuleViolationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Severity DEFAULT_SEVERITY = Severity.MUST;
    private static final Severity UPDATED_SEVERITY = Severity.SHOULD;

    private static final Integer DEFAULT_LINE_START = 1;
    private static final Integer UPDATED_LINE_START = 2;

    private static final Integer DEFAULT_LINE_END = 1;
    private static final Integer UPDATED_LINE_END = 2;

    private static final String DEFAULT_JSON_POINTER = "AAAAAAAAAA";
    private static final String UPDATED_JSON_POINTER = "BBBBBBBBBB";

    @Autowired
    private LintRuleViolationRepository lintRuleViolationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLintRuleViolationMockMvc;

    private LintRuleViolation lintRuleViolation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LintRuleViolation createEntity(EntityManager em) {
        LintRuleViolation lintRuleViolation = new LintRuleViolation()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL)
            .severity(DEFAULT_SEVERITY)
            .lineStart(DEFAULT_LINE_START)
            .lineEnd(DEFAULT_LINE_END)
            .jsonPointer(DEFAULT_JSON_POINTER);
        // Add required entity
        LintReport lintReport;
        if (TestUtil.findAll(em, LintReport.class).isEmpty()) {
            lintReport = LintReportResourceIT.createEntity(em);
            em.persist(lintReport);
            em.flush();
        } else {
            lintReport = TestUtil.findAll(em, LintReport.class).get(0);
        }
        lintRuleViolation.setLintReport(lintReport);
        return lintRuleViolation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LintRuleViolation createUpdatedEntity(EntityManager em) {
        LintRuleViolation lintRuleViolation = new LintRuleViolation()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .severity(UPDATED_SEVERITY)
            .lineStart(UPDATED_LINE_START)
            .lineEnd(UPDATED_LINE_END)
            .jsonPointer(UPDATED_JSON_POINTER);
        // Add required entity
        LintReport lintReport;
        if (TestUtil.findAll(em, LintReport.class).isEmpty()) {
            lintReport = LintReportResourceIT.createUpdatedEntity(em);
            em.persist(lintReport);
            em.flush();
        } else {
            lintReport = TestUtil.findAll(em, LintReport.class).get(0);
        }
        lintRuleViolation.setLintReport(lintReport);
        return lintRuleViolation;
    }

    @BeforeEach
    public void initTest() {
        lintRuleViolation = createEntity(em);
    }

    @Test
    @Transactional
    public void createLintRuleViolation() throws Exception {
        int databaseSizeBeforeCreate = lintRuleViolationRepository.findAll().size();
        // Create the LintRuleViolation
        restLintRuleViolationMockMvc.perform(post("/api/lint-rule-violations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lintRuleViolation)))
            .andExpect(status().isCreated());

        // Validate the LintRuleViolation in the database
        List<LintRuleViolation> lintRuleViolationList = lintRuleViolationRepository.findAll();
        assertThat(lintRuleViolationList).hasSize(databaseSizeBeforeCreate + 1);
        LintRuleViolation testLintRuleViolation = lintRuleViolationList.get(lintRuleViolationList.size() - 1);
        assertThat(testLintRuleViolation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLintRuleViolation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLintRuleViolation.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testLintRuleViolation.getSeverity()).isEqualTo(DEFAULT_SEVERITY);
        assertThat(testLintRuleViolation.getLineStart()).isEqualTo(DEFAULT_LINE_START);
        assertThat(testLintRuleViolation.getLineEnd()).isEqualTo(DEFAULT_LINE_END);
        assertThat(testLintRuleViolation.getJsonPointer()).isEqualTo(DEFAULT_JSON_POINTER);
    }

    @Test
    @Transactional
    public void createLintRuleViolationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lintRuleViolationRepository.findAll().size();

        // Create the LintRuleViolation with an existing ID
        lintRuleViolation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLintRuleViolationMockMvc.perform(post("/api/lint-rule-violations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lintRuleViolation)))
            .andExpect(status().isBadRequest());

        // Validate the LintRuleViolation in the database
        List<LintRuleViolation> lintRuleViolationList = lintRuleViolationRepository.findAll();
        assertThat(lintRuleViolationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lintRuleViolationRepository.findAll().size();
        // set the field null
        lintRuleViolation.setName(null);

        // Create the LintRuleViolation, which fails.


        restLintRuleViolationMockMvc.perform(post("/api/lint-rule-violations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lintRuleViolation)))
            .andExpect(status().isBadRequest());

        List<LintRuleViolation> lintRuleViolationList = lintRuleViolationRepository.findAll();
        assertThat(lintRuleViolationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = lintRuleViolationRepository.findAll().size();
        // set the field null
        lintRuleViolation.setDescription(null);

        // Create the LintRuleViolation, which fails.


        restLintRuleViolationMockMvc.perform(post("/api/lint-rule-violations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lintRuleViolation)))
            .andExpect(status().isBadRequest());

        List<LintRuleViolation> lintRuleViolationList = lintRuleViolationRepository.findAll();
        assertThat(lintRuleViolationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLintRuleViolations() throws Exception {
        // Initialize the database
        lintRuleViolationRepository.saveAndFlush(lintRuleViolation);

        // Get all the lintRuleViolationList
        restLintRuleViolationMockMvc.perform(get("/api/lint-rule-violations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lintRuleViolation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].lineStart").value(hasItem(DEFAULT_LINE_START)))
            .andExpect(jsonPath("$.[*].lineEnd").value(hasItem(DEFAULT_LINE_END)))
            .andExpect(jsonPath("$.[*].jsonPointer").value(hasItem(DEFAULT_JSON_POINTER)));
    }
    
    @Test
    @Transactional
    public void getLintRuleViolation() throws Exception {
        // Initialize the database
        lintRuleViolationRepository.saveAndFlush(lintRuleViolation);

        // Get the lintRuleViolation
        restLintRuleViolationMockMvc.perform(get("/api/lint-rule-violations/{id}", lintRuleViolation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lintRuleViolation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.severity").value(DEFAULT_SEVERITY.toString()))
            .andExpect(jsonPath("$.lineStart").value(DEFAULT_LINE_START))
            .andExpect(jsonPath("$.lineEnd").value(DEFAULT_LINE_END))
            .andExpect(jsonPath("$.jsonPointer").value(DEFAULT_JSON_POINTER));
    }
    @Test
    @Transactional
    public void getNonExistingLintRuleViolation() throws Exception {
        // Get the lintRuleViolation
        restLintRuleViolationMockMvc.perform(get("/api/lint-rule-violations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLintRuleViolation() throws Exception {
        // Initialize the database
        lintRuleViolationRepository.saveAndFlush(lintRuleViolation);

        int databaseSizeBeforeUpdate = lintRuleViolationRepository.findAll().size();

        // Update the lintRuleViolation
        LintRuleViolation updatedLintRuleViolation = lintRuleViolationRepository.findById(lintRuleViolation.getId()).get();
        // Disconnect from session so that the updates on updatedLintRuleViolation are not directly saved in db
        em.detach(updatedLintRuleViolation);
        updatedLintRuleViolation
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .severity(UPDATED_SEVERITY)
            .lineStart(UPDATED_LINE_START)
            .lineEnd(UPDATED_LINE_END)
            .jsonPointer(UPDATED_JSON_POINTER);

        restLintRuleViolationMockMvc.perform(put("/api/lint-rule-violations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLintRuleViolation)))
            .andExpect(status().isOk());

        // Validate the LintRuleViolation in the database
        List<LintRuleViolation> lintRuleViolationList = lintRuleViolationRepository.findAll();
        assertThat(lintRuleViolationList).hasSize(databaseSizeBeforeUpdate);
        LintRuleViolation testLintRuleViolation = lintRuleViolationList.get(lintRuleViolationList.size() - 1);
        assertThat(testLintRuleViolation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLintRuleViolation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLintRuleViolation.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testLintRuleViolation.getSeverity()).isEqualTo(UPDATED_SEVERITY);
        assertThat(testLintRuleViolation.getLineStart()).isEqualTo(UPDATED_LINE_START);
        assertThat(testLintRuleViolation.getLineEnd()).isEqualTo(UPDATED_LINE_END);
        assertThat(testLintRuleViolation.getJsonPointer()).isEqualTo(UPDATED_JSON_POINTER);
    }

    @Test
    @Transactional
    public void updateNonExistingLintRuleViolation() throws Exception {
        int databaseSizeBeforeUpdate = lintRuleViolationRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLintRuleViolationMockMvc.perform(put("/api/lint-rule-violations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lintRuleViolation)))
            .andExpect(status().isBadRequest());

        // Validate the LintRuleViolation in the database
        List<LintRuleViolation> lintRuleViolationList = lintRuleViolationRepository.findAll();
        assertThat(lintRuleViolationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLintRuleViolation() throws Exception {
        // Initialize the database
        lintRuleViolationRepository.saveAndFlush(lintRuleViolation);

        int databaseSizeBeforeDelete = lintRuleViolationRepository.findAll().size();

        // Delete the lintRuleViolation
        restLintRuleViolationMockMvc.perform(delete("/api/lint-rule-violations/{id}", lintRuleViolation.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LintRuleViolation> lintRuleViolationList = lintRuleViolationRepository.findAll();
        assertThat(lintRuleViolationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
