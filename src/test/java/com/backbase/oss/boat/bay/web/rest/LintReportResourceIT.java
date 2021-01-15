package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.repository.LintReportRepository;

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
 * Integration tests for the {@link LintReportResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser

public class LintReportResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GRADE = "AAAAAAAAAA";
    private static final String UPDATED_GRADE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PASSED = false;
    private static final Boolean UPDATED_PASSED = true;

    private static final Instant DEFAULT_LINTED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LINTED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private LintReportRepository lintReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLintReportMockMvc;

    private LintReport lintReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LintReport createEntity(EntityManager em) {
        LintReport lintReport = new LintReport()
            .name(DEFAULT_NAME)
            .grade(DEFAULT_GRADE)
            .passed(DEFAULT_PASSED)
            .lintedOn(DEFAULT_LINTED_ON);
        return lintReport;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LintReport createUpdatedEntity(EntityManager em) {
        LintReport lintReport = new LintReport()
            .name(UPDATED_NAME)
            .grade(UPDATED_GRADE)
            .passed(UPDATED_PASSED)
            .lintedOn(UPDATED_LINTED_ON);
        return lintReport;
    }

    @BeforeEach
    public void initTest() {
        lintReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createLintReport() throws Exception {
        int databaseSizeBeforeCreate = lintReportRepository.findAll().size();
        // Create the LintReport
        restLintReportMockMvc.perform(post("/api/lint-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lintReport)))
            .andExpect(status().isCreated());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeCreate + 1);
        LintReport testLintReport = lintReportList.get(lintReportList.size() - 1);
        assertThat(testLintReport.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLintReport.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testLintReport.isPassed()).isEqualTo(DEFAULT_PASSED);
        assertThat(testLintReport.getLintedOn()).isEqualTo(DEFAULT_LINTED_ON);
    }

    @Test
    @Transactional
    public void createLintReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lintReportRepository.findAll().size();

        // Create the LintReport with an existing ID
        lintReport.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLintReportMockMvc.perform(post("/api/lint-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lintReport)))
            .andExpect(status().isBadRequest());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLintReports() throws Exception {
        // Initialize the database
        lintReportRepository.saveAndFlush(lintReport);

        // Get all the lintReportList
        restLintReportMockMvc.perform(get("/api/lint-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lintReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)))
            .andExpect(jsonPath("$.[*].passed").value(hasItem(DEFAULT_PASSED.booleanValue())))
            .andExpect(jsonPath("$.[*].lintedOn").value(hasItem(DEFAULT_LINTED_ON.toString())));
    }

    @Test
    @Transactional
    public void getLintReport() throws Exception {
        // Initialize the database
        lintReportRepository.saveAndFlush(lintReport);

        // Get the lintReport
        restLintReportMockMvc.perform(get("/api/lint-reports/{id}", lintReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lintReport.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE))
            .andExpect(jsonPath("$.passed").value(DEFAULT_PASSED.booleanValue()))
            .andExpect(jsonPath("$.lintedOn").value(DEFAULT_LINTED_ON.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingLintReport() throws Exception {
        // Get the lintReport
        restLintReportMockMvc.perform(get("/api/lint-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLintReport() throws Exception {
        // Initialize the database
        lintReportRepository.saveAndFlush(lintReport);

        int databaseSizeBeforeUpdate = lintReportRepository.findAll().size();

        // Update the lintReport
        LintReport updatedLintReport = lintReportRepository.findById(lintReport.getId()).get();
        // Disconnect from session so that the updates on updatedLintReport are not directly saved in db
        em.detach(updatedLintReport);
        updatedLintReport
            .name(UPDATED_NAME)
            .grade(UPDATED_GRADE)
            .passed(UPDATED_PASSED)
            .lintedOn(UPDATED_LINTED_ON);

        restLintReportMockMvc.perform(put("/api/lint-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLintReport)))
            .andExpect(status().isOk());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeUpdate);
        LintReport testLintReport = lintReportList.get(lintReportList.size() - 1);
        assertThat(testLintReport.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLintReport.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testLintReport.isPassed()).isEqualTo(UPDATED_PASSED);
        assertThat(testLintReport.getLintedOn()).isEqualTo(UPDATED_LINTED_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingLintReport() throws Exception {
        int databaseSizeBeforeUpdate = lintReportRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLintReportMockMvc.perform(put("/api/lint-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lintReport)))
            .andExpect(status().isBadRequest());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLintReport() throws Exception {
        // Initialize the database
        lintReportRepository.saveAndFlush(lintReport);

        int databaseSizeBeforeDelete = lintReportRepository.findAll().size();

        // Delete the lintReport
        restLintReportMockMvc.perform(delete("/api/lint-reports/{id}", lintReport.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
