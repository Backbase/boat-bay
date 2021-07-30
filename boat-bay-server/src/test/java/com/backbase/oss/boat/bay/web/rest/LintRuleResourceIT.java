package com.backbase.oss.boat.bay.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backbase.oss.boat.bay.IntegrationTest;
import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.repository.LintRuleRepository;
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
 * Integration tests for the {@link LintRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LintRuleResourceIT {

    private static final String DEFAULT_RULE_ID = "AAAAAAAAAA";
    private static final String UPDATED_RULE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_RULE_SET = "AAAAAAAAAA";
    private static final String UPDATED_RULE_SET = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final Severity DEFAULT_SEVERITY = Severity.MUST;
    private static final Severity UPDATED_SEVERITY = Severity.SHOULD;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_URL = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/lint-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LintRuleRepository lintRuleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLintRuleMockMvc;

    private LintRule lintRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LintRule createEntity(EntityManager em) {
        LintRule lintRule = new LintRule()
            .ruleId(DEFAULT_RULE_ID)
            .title(DEFAULT_TITLE)
            .ruleSet(DEFAULT_RULE_SET)
            .summary(DEFAULT_SUMMARY)
            .severity(DEFAULT_SEVERITY)
            .description(DEFAULT_DESCRIPTION)
            .externalUrl(DEFAULT_EXTERNAL_URL)
            .enabled(DEFAULT_ENABLED);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        lintRule.setPortal(portal);
        return lintRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LintRule createUpdatedEntity(EntityManager em) {
        LintRule lintRule = new LintRule()
            .ruleId(UPDATED_RULE_ID)
            .title(UPDATED_TITLE)
            .ruleSet(UPDATED_RULE_SET)
            .summary(UPDATED_SUMMARY)
            .severity(UPDATED_SEVERITY)
            .description(UPDATED_DESCRIPTION)
            .externalUrl(UPDATED_EXTERNAL_URL)
            .enabled(UPDATED_ENABLED);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createUpdatedEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        lintRule.setPortal(portal);
        return lintRule;
    }

    @BeforeEach
    public void initTest() {
        lintRule = createEntity(em);
    }

    @Test
    @Transactional
    void createLintRule() throws Exception {
        int databaseSizeBeforeCreate = lintRuleRepository.findAll().size();
        // Create the LintRule
        restLintRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lintRule)))
            .andExpect(status().isCreated());

        // Validate the LintRule in the database
        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeCreate + 1);
        LintRule testLintRule = lintRuleList.get(lintRuleList.size() - 1);
        assertThat(testLintRule.getRuleId()).isEqualTo(DEFAULT_RULE_ID);
        assertThat(testLintRule.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLintRule.getRuleSet()).isEqualTo(DEFAULT_RULE_SET);
        assertThat(testLintRule.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testLintRule.getSeverity()).isEqualTo(DEFAULT_SEVERITY);
        assertThat(testLintRule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLintRule.getExternalUrl()).isEqualTo(DEFAULT_EXTERNAL_URL);
        assertThat(testLintRule.getEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    void createLintRuleWithExistingId() throws Exception {
        // Create the LintRule with an existing ID
        lintRule.setId(1L);

        int databaseSizeBeforeCreate = lintRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLintRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lintRule)))
            .andExpect(status().isBadRequest());

        // Validate the LintRule in the database
        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRuleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = lintRuleRepository.findAll().size();
        // set the field null
        lintRule.setRuleId(null);

        // Create the LintRule, which fails.

        restLintRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lintRule)))
            .andExpect(status().isBadRequest());

        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = lintRuleRepository.findAll().size();
        // set the field null
        lintRule.setTitle(null);

        // Create the LintRule, which fails.

        restLintRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lintRule)))
            .andExpect(status().isBadRequest());

        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeverityIsRequired() throws Exception {
        int databaseSizeBeforeTest = lintRuleRepository.findAll().size();
        // set the field null
        lintRule.setSeverity(null);

        // Create the LintRule, which fails.

        restLintRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lintRule)))
            .andExpect(status().isBadRequest());

        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = lintRuleRepository.findAll().size();
        // set the field null
        lintRule.setEnabled(null);

        // Create the LintRule, which fails.

        restLintRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lintRule)))
            .andExpect(status().isBadRequest());

        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLintRules() throws Exception {
        // Initialize the database
        lintRuleRepository.saveAndFlush(lintRule);

        // Get all the lintRuleList
        restLintRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lintRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].ruleSet").value(hasItem(DEFAULT_RULE_SET)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].externalUrl").value(hasItem(DEFAULT_EXTERNAL_URL)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getLintRule() throws Exception {
        // Initialize the database
        lintRuleRepository.saveAndFlush(lintRule);

        // Get the lintRule
        restLintRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, lintRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lintRule.getId().intValue()))
            .andExpect(jsonPath("$.ruleId").value(DEFAULT_RULE_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.ruleSet").value(DEFAULT_RULE_SET))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY))
            .andExpect(jsonPath("$.severity").value(DEFAULT_SEVERITY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.externalUrl").value(DEFAULT_EXTERNAL_URL))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingLintRule() throws Exception {
        // Get the lintRule
        restLintRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLintRule() throws Exception {
        // Initialize the database
        lintRuleRepository.saveAndFlush(lintRule);

        int databaseSizeBeforeUpdate = lintRuleRepository.findAll().size();

        // Update the lintRule
        LintRule updatedLintRule = lintRuleRepository.findById(lintRule.getId()).get();
        // Disconnect from session so that the updates on updatedLintRule are not directly saved in db
        em.detach(updatedLintRule);
        updatedLintRule
            .ruleId(UPDATED_RULE_ID)
            .title(UPDATED_TITLE)
            .ruleSet(UPDATED_RULE_SET)
            .summary(UPDATED_SUMMARY)
            .severity(UPDATED_SEVERITY)
            .description(UPDATED_DESCRIPTION)
            .externalUrl(UPDATED_EXTERNAL_URL)
            .enabled(UPDATED_ENABLED);

        restLintRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLintRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLintRule))
            )
            .andExpect(status().isOk());

        // Validate the LintRule in the database
        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeUpdate);
        LintRule testLintRule = lintRuleList.get(lintRuleList.size() - 1);
        assertThat(testLintRule.getRuleId()).isEqualTo(UPDATED_RULE_ID);
        assertThat(testLintRule.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLintRule.getRuleSet()).isEqualTo(UPDATED_RULE_SET);
        assertThat(testLintRule.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testLintRule.getSeverity()).isEqualTo(UPDATED_SEVERITY);
        assertThat(testLintRule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLintRule.getExternalUrl()).isEqualTo(UPDATED_EXTERNAL_URL);
        assertThat(testLintRule.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void putNonExistingLintRule() throws Exception {
        int databaseSizeBeforeUpdate = lintRuleRepository.findAll().size();
        lintRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLintRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lintRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lintRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the LintRule in the database
        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLintRule() throws Exception {
        int databaseSizeBeforeUpdate = lintRuleRepository.findAll().size();
        lintRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLintRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lintRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the LintRule in the database
        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLintRule() throws Exception {
        int databaseSizeBeforeUpdate = lintRuleRepository.findAll().size();
        lintRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLintRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lintRule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LintRule in the database
        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLintRuleWithPatch() throws Exception {
        // Initialize the database
        lintRuleRepository.saveAndFlush(lintRule);

        int databaseSizeBeforeUpdate = lintRuleRepository.findAll().size();

        // Update the lintRule using partial update
        LintRule partialUpdatedLintRule = new LintRule();
        partialUpdatedLintRule.setId(lintRule.getId());

        partialUpdatedLintRule.summary(UPDATED_SUMMARY).severity(UPDATED_SEVERITY);

        restLintRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLintRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLintRule))
            )
            .andExpect(status().isOk());

        // Validate the LintRule in the database
        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeUpdate);
        LintRule testLintRule = lintRuleList.get(lintRuleList.size() - 1);
        assertThat(testLintRule.getRuleId()).isEqualTo(DEFAULT_RULE_ID);
        assertThat(testLintRule.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLintRule.getRuleSet()).isEqualTo(DEFAULT_RULE_SET);
        assertThat(testLintRule.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testLintRule.getSeverity()).isEqualTo(UPDATED_SEVERITY);
        assertThat(testLintRule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLintRule.getExternalUrl()).isEqualTo(DEFAULT_EXTERNAL_URL);
        assertThat(testLintRule.getEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    void fullUpdateLintRuleWithPatch() throws Exception {
        // Initialize the database
        lintRuleRepository.saveAndFlush(lintRule);

        int databaseSizeBeforeUpdate = lintRuleRepository.findAll().size();

        // Update the lintRule using partial update
        LintRule partialUpdatedLintRule = new LintRule();
        partialUpdatedLintRule.setId(lintRule.getId());

        partialUpdatedLintRule
            .ruleId(UPDATED_RULE_ID)
            .title(UPDATED_TITLE)
            .ruleSet(UPDATED_RULE_SET)
            .summary(UPDATED_SUMMARY)
            .severity(UPDATED_SEVERITY)
            .description(UPDATED_DESCRIPTION)
            .externalUrl(UPDATED_EXTERNAL_URL)
            .enabled(UPDATED_ENABLED);

        restLintRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLintRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLintRule))
            )
            .andExpect(status().isOk());

        // Validate the LintRule in the database
        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeUpdate);
        LintRule testLintRule = lintRuleList.get(lintRuleList.size() - 1);
        assertThat(testLintRule.getRuleId()).isEqualTo(UPDATED_RULE_ID);
        assertThat(testLintRule.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLintRule.getRuleSet()).isEqualTo(UPDATED_RULE_SET);
        assertThat(testLintRule.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testLintRule.getSeverity()).isEqualTo(UPDATED_SEVERITY);
        assertThat(testLintRule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLintRule.getExternalUrl()).isEqualTo(UPDATED_EXTERNAL_URL);
        assertThat(testLintRule.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void patchNonExistingLintRule() throws Exception {
        int databaseSizeBeforeUpdate = lintRuleRepository.findAll().size();
        lintRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLintRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lintRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lintRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the LintRule in the database
        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLintRule() throws Exception {
        int databaseSizeBeforeUpdate = lintRuleRepository.findAll().size();
        lintRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLintRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lintRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the LintRule in the database
        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLintRule() throws Exception {
        int databaseSizeBeforeUpdate = lintRuleRepository.findAll().size();
        lintRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLintRuleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lintRule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LintRule in the database
        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLintRule() throws Exception {
        // Initialize the database
        lintRuleRepository.saveAndFlush(lintRule);

        int databaseSizeBeforeDelete = lintRuleRepository.findAll().size();

        // Delete the lintRule
        restLintRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, lintRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LintRule> lintRuleList = lintRuleRepository.findAll();
        assertThat(lintRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
