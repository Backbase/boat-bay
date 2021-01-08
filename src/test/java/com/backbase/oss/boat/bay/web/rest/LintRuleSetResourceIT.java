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
import org.springframework.util.Base64Utils;
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

    private static final String DEFAULT_RULE_SET_ID = "AAAAAAAAAA";
    private static final String UPDATED_RULE_SET_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_URL = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_URL = "BBBBBBBBBB";

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
            .ruleSetId(DEFAULT_RULE_SET_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .externalUrl(DEFAULT_EXTERNAL_URL);
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
            .ruleSetId(UPDATED_RULE_SET_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .externalUrl(UPDATED_EXTERNAL_URL);
        return lintRuleSet;
    }

    @BeforeEach
    public void initTest() {
        lintRuleSet = createEntity(em);
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
            .andExpect(jsonPath("$.[*].ruleSetId").value(hasItem(DEFAULT_RULE_SET_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].externalUrl").value(hasItem(DEFAULT_EXTERNAL_URL)));
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
            .andExpect(jsonPath("$.ruleSetId").value(DEFAULT_RULE_SET_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.externalUrl").value(DEFAULT_EXTERNAL_URL));
    }
    @Test
    @Transactional
    public void getNonExistingLintRuleSet() throws Exception {
        // Get the lintRuleSet
        restLintRuleSetMockMvc.perform(get("/api/lint-rule-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
