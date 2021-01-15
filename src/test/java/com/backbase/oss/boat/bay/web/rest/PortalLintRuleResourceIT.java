package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.PortalLintRule;
import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.PortalLintRuleRepository;

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
 * Integration tests for the {@link PortalLintRuleResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser

public class PortalLintRuleResourceIT {

    private static final String DEFAULT_RULE_ID = "AAAAAAAAAA";
    private static final String UPDATED_RULE_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private PortalLintRuleRepository portalLintRuleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPortalLintRuleMockMvc;

    private PortalLintRule portalLintRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PortalLintRule createEntity(EntityManager em) {
        PortalLintRule portalLintRule = new PortalLintRule()
            .ruleId(DEFAULT_RULE_ID)
            .enabled(DEFAULT_ENABLED);
        // Add required entity
        LintRule lintRule;
        if (TestUtil.findAll(em, LintRule.class).isEmpty()) {
            lintRule = LintRuleResourceIT.createEntity(em);
            em.persist(lintRule);
            em.flush();
        } else {
            lintRule = TestUtil.findAll(em, LintRule.class).get(0);
        }
        portalLintRule.setLintRule(lintRule);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        portalLintRule.setPortal(portal);
        return portalLintRule;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PortalLintRule createUpdatedEntity(EntityManager em) {
        PortalLintRule portalLintRule = new PortalLintRule()
            .ruleId(UPDATED_RULE_ID)
            .enabled(UPDATED_ENABLED);
        // Add required entity
        LintRule lintRule;
        if (TestUtil.findAll(em, LintRule.class).isEmpty()) {
            lintRule = LintRuleResourceIT.createUpdatedEntity(em);
            em.persist(lintRule);
            em.flush();
        } else {
            lintRule = TestUtil.findAll(em, LintRule.class).get(0);
        }
        portalLintRule.setLintRule(lintRule);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createUpdatedEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        portalLintRule.setPortal(portal);
        return portalLintRule;
    }

    @BeforeEach
    public void initTest() {
        portalLintRule = createEntity(em);
    }

    @Test
    @Transactional
    public void createPortalLintRule() throws Exception {
        int databaseSizeBeforeCreate = portalLintRuleRepository.findAll().size();
        // Create the PortalLintRule
        restPortalLintRuleMockMvc.perform(post("/api/portal-lint-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRule)))
            .andExpect(status().isCreated());

        // Validate the PortalLintRule in the database
        List<PortalLintRule> portalLintRuleList = portalLintRuleRepository.findAll();
        assertThat(portalLintRuleList).hasSize(databaseSizeBeforeCreate + 1);
        PortalLintRule testPortalLintRule = portalLintRuleList.get(portalLintRuleList.size() - 1);
        assertThat(testPortalLintRule.getRuleId()).isEqualTo(DEFAULT_RULE_ID);
        assertThat(testPortalLintRule.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createPortalLintRuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = portalLintRuleRepository.findAll().size();

        // Create the PortalLintRule with an existing ID
        portalLintRule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortalLintRuleMockMvc.perform(post("/api/portal-lint-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRule)))
            .andExpect(status().isBadRequest());

        // Validate the PortalLintRule in the database
        List<PortalLintRule> portalLintRuleList = portalLintRuleRepository.findAll();
        assertThat(portalLintRuleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRuleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = portalLintRuleRepository.findAll().size();
        // set the field null
        portalLintRule.setRuleId(null);

        // Create the PortalLintRule, which fails.


        restPortalLintRuleMockMvc.perform(post("/api/portal-lint-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRule)))
            .andExpect(status().isBadRequest());

        List<PortalLintRule> portalLintRuleList = portalLintRuleRepository.findAll();
        assertThat(portalLintRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = portalLintRuleRepository.findAll().size();
        // set the field null
        portalLintRule.setEnabled(null);

        // Create the PortalLintRule, which fails.


        restPortalLintRuleMockMvc.perform(post("/api/portal-lint-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRule)))
            .andExpect(status().isBadRequest());

        List<PortalLintRule> portalLintRuleList = portalLintRuleRepository.findAll();
        assertThat(portalLintRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPortalLintRules() throws Exception {
        // Initialize the database
        portalLintRuleRepository.saveAndFlush(portalLintRule);

        // Get all the portalLintRuleList
        restPortalLintRuleMockMvc.perform(get("/api/portal-lint-rules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portalLintRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getPortalLintRule() throws Exception {
        // Initialize the database
        portalLintRuleRepository.saveAndFlush(portalLintRule);

        // Get the portalLintRule
        restPortalLintRuleMockMvc.perform(get("/api/portal-lint-rules/{id}", portalLintRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(portalLintRule.getId().intValue()))
            .andExpect(jsonPath("$.ruleId").value(DEFAULT_RULE_ID))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPortalLintRule() throws Exception {
        // Get the portalLintRule
        restPortalLintRuleMockMvc.perform(get("/api/portal-lint-rules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePortalLintRule() throws Exception {
        // Initialize the database
        portalLintRuleRepository.saveAndFlush(portalLintRule);

        int databaseSizeBeforeUpdate = portalLintRuleRepository.findAll().size();

        // Update the portalLintRule
        PortalLintRule updatedPortalLintRule = portalLintRuleRepository.findById(portalLintRule.getId()).get();
        // Disconnect from session so that the updates on updatedPortalLintRule are not directly saved in db
        em.detach(updatedPortalLintRule);
        updatedPortalLintRule
            .ruleId(UPDATED_RULE_ID)
            .enabled(UPDATED_ENABLED);

        restPortalLintRuleMockMvc.perform(put("/api/portal-lint-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPortalLintRule)))
            .andExpect(status().isOk());

        // Validate the PortalLintRule in the database
        List<PortalLintRule> portalLintRuleList = portalLintRuleRepository.findAll();
        assertThat(portalLintRuleList).hasSize(databaseSizeBeforeUpdate);
        PortalLintRule testPortalLintRule = portalLintRuleList.get(portalLintRuleList.size() - 1);
        assertThat(testPortalLintRule.getRuleId()).isEqualTo(UPDATED_RULE_ID);
        assertThat(testPortalLintRule.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingPortalLintRule() throws Exception {
        int databaseSizeBeforeUpdate = portalLintRuleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortalLintRuleMockMvc.perform(put("/api/portal-lint-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portalLintRule)))
            .andExpect(status().isBadRequest());

        // Validate the PortalLintRule in the database
        List<PortalLintRule> portalLintRuleList = portalLintRuleRepository.findAll();
        assertThat(portalLintRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePortalLintRule() throws Exception {
        // Initialize the database
        portalLintRuleRepository.saveAndFlush(portalLintRule);

        int databaseSizeBeforeDelete = portalLintRuleRepository.findAll().size();

        // Delete the portalLintRule
        restPortalLintRuleMockMvc.perform(delete("/api/portal-lint-rules/{id}", portalLintRule.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PortalLintRule> portalLintRuleList = portalLintRuleRepository.findAll();
        assertThat(portalLintRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
