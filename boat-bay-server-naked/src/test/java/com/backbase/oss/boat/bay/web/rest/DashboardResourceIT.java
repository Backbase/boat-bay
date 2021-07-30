package com.backbase.oss.boat.bay.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backbase.oss.boat.bay.IntegrationTest;
import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.DashboardRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DashboardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DashboardResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_NAV_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_NAV_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dashboards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDashboardMockMvc;

    private Dashboard dashboard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dashboard createEntity(EntityManager em) {
        Dashboard dashboard = new Dashboard()
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE)
            .subTitle(DEFAULT_SUB_TITLE)
            .navTitle(DEFAULT_NAV_TITLE)
            .content(DEFAULT_CONTENT);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        dashboard.setDefaultPortal(portal);
        return dashboard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dashboard createUpdatedEntity(EntityManager em) {
        Dashboard dashboard = new Dashboard()
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .navTitle(UPDATED_NAV_TITLE)
            .content(UPDATED_CONTENT);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createUpdatedEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        dashboard.setDefaultPortal(portal);
        return dashboard;
    }

    @BeforeEach
    public void initTest() {
        dashboard = createEntity(em);
    }

    @Test
    @Transactional
    void createDashboard() throws Exception {
        int databaseSizeBeforeCreate = dashboardRepository.findAll().size();
        // Create the Dashboard
        restDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().isCreated());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeCreate + 1);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDashboard.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDashboard.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testDashboard.getNavTitle()).isEqualTo(DEFAULT_NAV_TITLE);
        assertThat(testDashboard.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void createDashboardWithExistingId() throws Exception {
        // Create the Dashboard with an existing ID
        dashboard.setId(1L);

        int databaseSizeBeforeCreate = dashboardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dashboardRepository.findAll().size();
        // set the field null
        dashboard.setName(null);

        // Create the Dashboard, which fails.

        restDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().isBadRequest());

        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDashboards() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dashboard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].navTitle").value(hasItem(DEFAULT_NAV_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    void getDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        // Get the dashboard
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL_ID, dashboard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dashboard.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.navTitle").value(DEFAULT_NAV_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDashboard() throws Exception {
        // Get the dashboard
        restDashboardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();

        // Update the dashboard
        Dashboard updatedDashboard = dashboardRepository.findById(dashboard.getId()).get();
        // Disconnect from session so that the updates on updatedDashboard are not directly saved in db
        em.detach(updatedDashboard);
        updatedDashboard
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .navTitle(UPDATED_NAV_TITLE)
            .content(UPDATED_CONTENT);

        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDashboard.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDashboard))
            )
            .andExpect(status().isOk());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDashboard.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDashboard.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testDashboard.getNavTitle()).isEqualTo(UPDATED_NAV_TITLE);
        assertThat(testDashboard.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void putNonExistingDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dashboard.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dashboard))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dashboard))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDashboardWithPatch() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();

        // Update the dashboard using partial update
        Dashboard partialUpdatedDashboard = new Dashboard();
        partialUpdatedDashboard.setId(dashboard.getId());

        partialUpdatedDashboard.name(UPDATED_NAME);

        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDashboard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDashboard))
            )
            .andExpect(status().isOk());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDashboard.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDashboard.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testDashboard.getNavTitle()).isEqualTo(DEFAULT_NAV_TITLE);
        assertThat(testDashboard.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void fullUpdateDashboardWithPatch() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();

        // Update the dashboard using partial update
        Dashboard partialUpdatedDashboard = new Dashboard();
        partialUpdatedDashboard.setId(dashboard.getId());

        partialUpdatedDashboard
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .navTitle(UPDATED_NAV_TITLE)
            .content(UPDATED_CONTENT);

        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDashboard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDashboard))
            )
            .andExpect(status().isOk());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDashboard.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDashboard.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testDashboard.getNavTitle()).isEqualTo(UPDATED_NAV_TITLE);
        assertThat(testDashboard.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void patchNonExistingDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dashboard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dashboard))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dashboard))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dashboard))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        int databaseSizeBeforeDelete = dashboardRepository.findAll().size();

        // Delete the dashboard
        restDashboardMockMvc
            .perform(delete(ENTITY_API_URL_ID, dashboard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
