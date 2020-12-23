package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.LintRuleViolation;
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

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Severity DEFAULT_SEVERITY = Severity.MUST;
    private static final Severity UPDATED_SEVERITY = Severity.SHOULD;

    private static final Integer DEFAULT_LINE_START = 1;
    private static final Integer UPDATED_LINE_START = 2;

    private static final Integer DEFAULT_LIND_END = 1;
    private static final Integer UPDATED_LIND_END = 2;

    private static final Integer DEFAULT_COLUMN_START = 1;
    private static final Integer UPDATED_COLUMN_START = 2;

    private static final Integer DEFAULT_COLUMN_END = 1;
    private static final Integer UPDATED_COLUMN_END = 2;

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
            .description(DEFAULT_DESCRIPTION)
            .severity(DEFAULT_SEVERITY)
            .lineStart(DEFAULT_LINE_START)
            .lindEnd(DEFAULT_LIND_END)
            .columnStart(DEFAULT_COLUMN_START)
            .columnEnd(DEFAULT_COLUMN_END)
            .jsonPointer(DEFAULT_JSON_POINTER);
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
            .description(UPDATED_DESCRIPTION)
            .severity(UPDATED_SEVERITY)
            .lineStart(UPDATED_LINE_START)
            .lindEnd(UPDATED_LIND_END)
            .columnStart(UPDATED_COLUMN_START)
            .columnEnd(UPDATED_COLUMN_END)
            .jsonPointer(UPDATED_JSON_POINTER);
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
        assertThat(testLintRuleViolation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLintRuleViolation.getSeverity()).isEqualTo(DEFAULT_SEVERITY);
        assertThat(testLintRuleViolation.getLineStart()).isEqualTo(DEFAULT_LINE_START);
        assertThat(testLintRuleViolation.getLindEnd()).isEqualTo(DEFAULT_LIND_END);
        assertThat(testLintRuleViolation.getColumnStart()).isEqualTo(DEFAULT_COLUMN_START);
        assertThat(testLintRuleViolation.getColumnEnd()).isEqualTo(DEFAULT_COLUMN_END);
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
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].lineStart").value(hasItem(DEFAULT_LINE_START)))
            .andExpect(jsonPath("$.[*].lindEnd").value(hasItem(DEFAULT_LIND_END)))
            .andExpect(jsonPath("$.[*].columnStart").value(hasItem(DEFAULT_COLUMN_START)))
            .andExpect(jsonPath("$.[*].columnEnd").value(hasItem(DEFAULT_COLUMN_END)))
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
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.severity").value(DEFAULT_SEVERITY.toString()))
            .andExpect(jsonPath("$.lineStart").value(DEFAULT_LINE_START))
            .andExpect(jsonPath("$.lindEnd").value(DEFAULT_LIND_END))
            .andExpect(jsonPath("$.columnStart").value(DEFAULT_COLUMN_START))
            .andExpect(jsonPath("$.columnEnd").value(DEFAULT_COLUMN_END))
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
            .description(UPDATED_DESCRIPTION)
            .severity(UPDATED_SEVERITY)
            .lineStart(UPDATED_LINE_START)
            .lindEnd(UPDATED_LIND_END)
            .columnStart(UPDATED_COLUMN_START)
            .columnEnd(UPDATED_COLUMN_END)
            .jsonPointer(UPDATED_JSON_POINTER);

        restLintRuleViolationMockMvc.perform(put("/api/lint-rule-violations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLintRuleViolation)))
            .andExpect(status().isOk());

        // Validate the LintRuleViolation in the database
        List<LintRuleViolation> lintRuleViolationList = lintRuleViolationRepository.findAll();
        assertThat(lintRuleViolationList).hasSize(databaseSizeBeforeUpdate);
        LintRuleViolation testLintRuleViolation = lintRuleViolationList.get(lintRuleViolationList.size() - 1);
        assertThat(testLintRuleViolation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLintRuleViolation.getSeverity()).isEqualTo(UPDATED_SEVERITY);
        assertThat(testLintRuleViolation.getLineStart()).isEqualTo(UPDATED_LINE_START);
        assertThat(testLintRuleViolation.getLindEnd()).isEqualTo(UPDATED_LIND_END);
        assertThat(testLintRuleViolation.getColumnStart()).isEqualTo(UPDATED_COLUMN_START);
        assertThat(testLintRuleViolation.getColumnEnd()).isEqualTo(UPDATED_COLUMN_END);
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
