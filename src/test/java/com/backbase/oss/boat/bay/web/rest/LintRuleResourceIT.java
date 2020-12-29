package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.LintRuleSet;
import com.backbase.oss.boat.bay.repository.LintRuleRepository;

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
 * Integration tests for the {@link LintRuleResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LintRuleResourceIT {

    private static final String DEFAULT_RULE_ID = "AAAAAAAAAA";
    private static final String UPDATED_RULE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

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
            .summary(DEFAULT_SUMMARY)
            .severity(DEFAULT_SEVERITY)
            .description(DEFAULT_DESCRIPTION)
            .externalUrl(DEFAULT_EXTERNAL_URL)
            .enabled(DEFAULT_ENABLED);
        // Add required entity
        LintRuleSet lintRuleSet;
        if (TestUtil.findAll(em, LintRuleSet.class).isEmpty()) {
            lintRuleSet = LintRuleSetResourceIT.createEntity(em);
            em.persist(lintRuleSet);
            em.flush();
        } else {
            lintRuleSet = TestUtil.findAll(em, LintRuleSet.class).get(0);
        }
        lintRule.setRuleSet(lintRuleSet);
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
            .summary(UPDATED_SUMMARY)
            .severity(UPDATED_SEVERITY)
            .description(UPDATED_DESCRIPTION)
            .externalUrl(UPDATED_EXTERNAL_URL)
            .enabled(UPDATED_ENABLED);
        // Add required entity
        LintRuleSet lintRuleSet;
        if (TestUtil.findAll(em, LintRuleSet.class).isEmpty()) {
            lintRuleSet = LintRuleSetResourceIT.createUpdatedEntity(em);
            em.persist(lintRuleSet);
            em.flush();
        } else {
            lintRuleSet = TestUtil.findAll(em, LintRuleSet.class).get(0);
        }
        lintRule.setRuleSet(lintRuleSet);
        return lintRule;
    }

    @BeforeEach
    public void initTest() {
        lintRule = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllLintRules() throws Exception {
        // Initialize the database
        lintRuleRepository.saveAndFlush(lintRule);

        // Get all the lintRuleList
        restLintRuleMockMvc.perform(get("/api/lint-rules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lintRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].externalUrl").value(hasItem(DEFAULT_EXTERNAL_URL)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getLintRule() throws Exception {
        // Initialize the database
        lintRuleRepository.saveAndFlush(lintRule);

        // Get the lintRule
        restLintRuleMockMvc.perform(get("/api/lint-rules/{id}", lintRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lintRule.getId().intValue()))
            .andExpect(jsonPath("$.ruleId").value(DEFAULT_RULE_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY))
            .andExpect(jsonPath("$.severity").value(DEFAULT_SEVERITY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.externalUrl").value(DEFAULT_EXTERNAL_URL))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingLintRule() throws Exception {
        // Get the lintRule
        restLintRuleMockMvc.perform(get("/api/lint-rules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
