package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.PortalRepository;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PortalResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PortalResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_NAV_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_NAV_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HIDE = false;
    private static final Boolean UPDATED_HIDE = true;

    @Autowired
    private PortalRepository portalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPortalMockMvc;

    private Portal portal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Portal createEntity(EntityManager em) {
        Portal portal = new Portal()
            .key(DEFAULT_KEY)
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE)
            .subTitle(DEFAULT_SUB_TITLE)
            .navTitle(DEFAULT_NAV_TITLE)
            .logoUrl(DEFAULT_LOGO_URL)
            .logoLink(DEFAULT_LOGO_LINK)
            .content(DEFAULT_CONTENT)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .hide(DEFAULT_HIDE);
        return portal;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Portal createUpdatedEntity(EntityManager em) {
        Portal portal = new Portal()
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .navTitle(UPDATED_NAV_TITLE)
            .logoUrl(UPDATED_LOGO_URL)
            .logoLink(UPDATED_LOGO_LINK)
            .content(UPDATED_CONTENT)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .hide(UPDATED_HIDE);
        return portal;
    }

    @BeforeEach
    public void initTest() {
        portal = createEntity(em);
    }

    @Test
    @Transactional
    public void createPortal() throws Exception {
        int databaseSizeBeforeCreate = portalRepository.findAll().size();
        // Create the Portal
        restPortalMockMvc.perform(post("/api/portals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portal)))
            .andExpect(status().isCreated());

        // Validate the Portal in the database
        List<Portal> portalList = portalRepository.findAll();
        assertThat(portalList).hasSize(databaseSizeBeforeCreate + 1);
        Portal testPortal = portalList.get(portalList.size() - 1);
        assertThat(testPortal.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testPortal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPortal.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPortal.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testPortal.getNavTitle()).isEqualTo(DEFAULT_NAV_TITLE);
        assertThat(testPortal.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);
        assertThat(testPortal.getLogoLink()).isEqualTo(DEFAULT_LOGO_LINK);
        assertThat(testPortal.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testPortal.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testPortal.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPortal.isHide()).isEqualTo(DEFAULT_HIDE);
    }

    @Test
    @Transactional
    public void createPortalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = portalRepository.findAll().size();

        // Create the Portal with an existing ID
        portal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortalMockMvc.perform(post("/api/portals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portal)))
            .andExpect(status().isBadRequest());

        // Validate the Portal in the database
        List<Portal> portalList = portalRepository.findAll();
        assertThat(portalList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = portalRepository.findAll().size();
        // set the field null
        portal.setKey(null);

        // Create the Portal, which fails.


        restPortalMockMvc.perform(post("/api/portals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portal)))
            .andExpect(status().isBadRequest());

        List<Portal> portalList = portalRepository.findAll();
        assertThat(portalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = portalRepository.findAll().size();
        // set the field null
        portal.setName(null);

        // Create the Portal, which fails.


        restPortalMockMvc.perform(post("/api/portals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portal)))
            .andExpect(status().isBadRequest());

        List<Portal> portalList = portalRepository.findAll();
        assertThat(portalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPortals() throws Exception {
        // Initialize the database
        portalRepository.saveAndFlush(portal);

        // Get all the portalList
        restPortalMockMvc.perform(get("/api/portals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portal.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].navTitle").value(hasItem(DEFAULT_NAV_TITLE)))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].logoLink").value(hasItem(DEFAULT_LOGO_LINK)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].hide").value(hasItem(DEFAULT_HIDE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPortal() throws Exception {
        // Initialize the database
        portalRepository.saveAndFlush(portal);

        // Get the portal
        restPortalMockMvc.perform(get("/api/portals/{id}", portal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(portal.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.navTitle").value(DEFAULT_NAV_TITLE))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL))
            .andExpect(jsonPath("$.logoLink").value(DEFAULT_LOGO_LINK))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.hide").value(DEFAULT_HIDE.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPortal() throws Exception {
        // Get the portal
        restPortalMockMvc.perform(get("/api/portals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePortal() throws Exception {
        // Initialize the database
        portalRepository.saveAndFlush(portal);

        int databaseSizeBeforeUpdate = portalRepository.findAll().size();

        // Update the portal
        Portal updatedPortal = portalRepository.findById(portal.getId()).get();
        // Disconnect from session so that the updates on updatedPortal are not directly saved in db
        em.detach(updatedPortal);
        updatedPortal
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .navTitle(UPDATED_NAV_TITLE)
            .logoUrl(UPDATED_LOGO_URL)
            .logoLink(UPDATED_LOGO_LINK)
            .content(UPDATED_CONTENT)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .hide(UPDATED_HIDE);

        restPortalMockMvc.perform(put("/api/portals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPortal)))
            .andExpect(status().isOk());

        // Validate the Portal in the database
        List<Portal> portalList = portalRepository.findAll();
        assertThat(portalList).hasSize(databaseSizeBeforeUpdate);
        Portal testPortal = portalList.get(portalList.size() - 1);
        assertThat(testPortal.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testPortal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPortal.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPortal.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testPortal.getNavTitle()).isEqualTo(UPDATED_NAV_TITLE);
        assertThat(testPortal.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testPortal.getLogoLink()).isEqualTo(UPDATED_LOGO_LINK);
        assertThat(testPortal.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testPortal.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testPortal.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPortal.isHide()).isEqualTo(UPDATED_HIDE);
    }

    @Test
    @Transactional
    public void updateNonExistingPortal() throws Exception {
        int databaseSizeBeforeUpdate = portalRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortalMockMvc.perform(put("/api/portals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portal)))
            .andExpect(status().isBadRequest());

        // Validate the Portal in the database
        List<Portal> portalList = portalRepository.findAll();
        assertThat(portalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePortal() throws Exception {
        // Initialize the database
        portalRepository.saveAndFlush(portal);

        int databaseSizeBeforeDelete = portalRepository.findAll().size();

        // Delete the portal
        restPortalMockMvc.perform(delete("/api/portals/{id}", portal.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Portal> portalList = portalRepository.findAll();
        assertThat(portalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
