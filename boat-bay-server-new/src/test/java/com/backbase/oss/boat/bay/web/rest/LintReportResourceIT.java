package com.backbase.oss.boat.bay.web.rest;

import static com.backbase.oss.boat.bay.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backbase.oss.boat.bay.IntegrationTest;
import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.repository.LintReportRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LintReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LintReportResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GRADE = "AAAAAAAAAA";
    private static final String UPDATED_GRADE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PASSED = false;
    private static final Boolean UPDATED_PASSED = true;

    private static final ZonedDateTime DEFAULT_LINTED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LINTED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/lint-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
        LintReport lintReport = new LintReport().name(DEFAULT_NAME).grade(DEFAULT_GRADE).passed(DEFAULT_PASSED).lintedOn(DEFAULT_LINTED_ON);
        return lintReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LintReport createUpdatedEntity(EntityManager em) {
        LintReport lintReport = new LintReport().name(UPDATED_NAME).grade(UPDATED_GRADE).passed(UPDATED_PASSED).lintedOn(UPDATED_LINTED_ON);
        return lintReport;
    }

    @BeforeEach
    public void initTest() {
        lintReport = createEntity(em);
    }

    @Test
    @Transactional
    void createLintReport() throws Exception {
        int databaseSizeBeforeCreate = lintReportRepository.findAll().size();
        // Create the LintReport
        restLintReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lintReport)))
            .andExpect(status().isCreated());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeCreate + 1);
        LintReport testLintReport = lintReportList.get(lintReportList.size() - 1);
        assertThat(testLintReport.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLintReport.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testLintReport.getPassed()).isEqualTo(DEFAULT_PASSED);
        assertThat(testLintReport.getLintedOn()).isEqualTo(DEFAULT_LINTED_ON);
    }

    @Test
    @Transactional
    void createLintReportWithExistingId() throws Exception {
        // Create the LintReport with an existing ID
        lintReport.setId(1L);

        int databaseSizeBeforeCreate = lintReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLintReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lintReport)))
            .andExpect(status().isBadRequest());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLintReports() throws Exception {
        // Initialize the database
        lintReportRepository.saveAndFlush(lintReport);

        // Get all the lintReportList
        restLintReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lintReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)))
            .andExpect(jsonPath("$.[*].passed").value(hasItem(DEFAULT_PASSED.booleanValue())))
            .andExpect(jsonPath("$.[*].lintedOn").value(hasItem(sameInstant(DEFAULT_LINTED_ON))));
    }

    @Test
    @Transactional
    void getLintReport() throws Exception {
        // Initialize the database
        lintReportRepository.saveAndFlush(lintReport);

        // Get the lintReport
        restLintReportMockMvc
            .perform(get(ENTITY_API_URL_ID, lintReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lintReport.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE))
            .andExpect(jsonPath("$.passed").value(DEFAULT_PASSED.booleanValue()))
            .andExpect(jsonPath("$.lintedOn").value(sameInstant(DEFAULT_LINTED_ON)));
    }

    @Test
    @Transactional
    void getNonExistingLintReport() throws Exception {
        // Get the lintReport
        restLintReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLintReport() throws Exception {
        // Initialize the database
        lintReportRepository.saveAndFlush(lintReport);

        int databaseSizeBeforeUpdate = lintReportRepository.findAll().size();

        // Update the lintReport
        LintReport updatedLintReport = lintReportRepository.findById(lintReport.getId()).get();
        // Disconnect from session so that the updates on updatedLintReport are not directly saved in db
        em.detach(updatedLintReport);
        updatedLintReport.name(UPDATED_NAME).grade(UPDATED_GRADE).passed(UPDATED_PASSED).lintedOn(UPDATED_LINTED_ON);

        restLintReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLintReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLintReport))
            )
            .andExpect(status().isOk());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeUpdate);
        LintReport testLintReport = lintReportList.get(lintReportList.size() - 1);
        assertThat(testLintReport.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLintReport.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testLintReport.getPassed()).isEqualTo(UPDATED_PASSED);
        assertThat(testLintReport.getLintedOn()).isEqualTo(UPDATED_LINTED_ON);
    }

    @Test
    @Transactional
    void putNonExistingLintReport() throws Exception {
        int databaseSizeBeforeUpdate = lintReportRepository.findAll().size();
        lintReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLintReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lintReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lintReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLintReport() throws Exception {
        int databaseSizeBeforeUpdate = lintReportRepository.findAll().size();
        lintReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLintReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lintReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLintReport() throws Exception {
        int databaseSizeBeforeUpdate = lintReportRepository.findAll().size();
        lintReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLintReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lintReport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLintReportWithPatch() throws Exception {
        // Initialize the database
        lintReportRepository.saveAndFlush(lintReport);

        int databaseSizeBeforeUpdate = lintReportRepository.findAll().size();

        // Update the lintReport using partial update
        LintReport partialUpdatedLintReport = new LintReport();
        partialUpdatedLintReport.setId(lintReport.getId());

        partialUpdatedLintReport.passed(UPDATED_PASSED).lintedOn(UPDATED_LINTED_ON);

        restLintReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLintReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLintReport))
            )
            .andExpect(status().isOk());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeUpdate);
        LintReport testLintReport = lintReportList.get(lintReportList.size() - 1);
        assertThat(testLintReport.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLintReport.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testLintReport.getPassed()).isEqualTo(UPDATED_PASSED);
        assertThat(testLintReport.getLintedOn()).isEqualTo(UPDATED_LINTED_ON);
    }

    @Test
    @Transactional
    void fullUpdateLintReportWithPatch() throws Exception {
        // Initialize the database
        lintReportRepository.saveAndFlush(lintReport);

        int databaseSizeBeforeUpdate = lintReportRepository.findAll().size();

        // Update the lintReport using partial update
        LintReport partialUpdatedLintReport = new LintReport();
        partialUpdatedLintReport.setId(lintReport.getId());

        partialUpdatedLintReport.name(UPDATED_NAME).grade(UPDATED_GRADE).passed(UPDATED_PASSED).lintedOn(UPDATED_LINTED_ON);

        restLintReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLintReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLintReport))
            )
            .andExpect(status().isOk());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeUpdate);
        LintReport testLintReport = lintReportList.get(lintReportList.size() - 1);
        assertThat(testLintReport.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLintReport.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testLintReport.getPassed()).isEqualTo(UPDATED_PASSED);
        assertThat(testLintReport.getLintedOn()).isEqualTo(UPDATED_LINTED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingLintReport() throws Exception {
        int databaseSizeBeforeUpdate = lintReportRepository.findAll().size();
        lintReport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLintReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lintReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lintReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLintReport() throws Exception {
        int databaseSizeBeforeUpdate = lintReportRepository.findAll().size();
        lintReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLintReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lintReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLintReport() throws Exception {
        int databaseSizeBeforeUpdate = lintReportRepository.findAll().size();
        lintReport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLintReportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lintReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LintReport in the database
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLintReport() throws Exception {
        // Initialize the database
        lintReportRepository.saveAndFlush(lintReport);

        int databaseSizeBeforeDelete = lintReportRepository.findAll().size();

        // Delete the lintReport
        restLintReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, lintReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LintReport> lintReportList = lintReportRepository.findAll();
        assertThat(lintReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
