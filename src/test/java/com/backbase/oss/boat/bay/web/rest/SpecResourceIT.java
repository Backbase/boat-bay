package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.SpecRepository;

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
 * Integration tests for the {@link SpecResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SpecResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_OPEN_API_URL = "AAAAAAAAAA";
    private static final String UPDATED_OPEN_API_URL = "BBBBBBBBBB";

    private static final String DEFAULT_BOAT_DOC_URL = "AAAAAAAAAA";
    private static final String UPDATED_BOAT_DOC_URL = "BBBBBBBBBB";

    private static final String DEFAULT_OPEN_API = "AAAAAAAAAA";
    private static final String UPDATED_OPEN_API = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    @Autowired
    private SpecRepository specRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecMockMvc;

    private Spec spec;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spec createEntity(EntityManager em) {
        Spec spec = new Spec()
            .key(DEFAULT_KEY)
            .title(DEFAULT_TITLE)
            .openApiUrl(DEFAULT_OPEN_API_URL)
            .boatDocUrl(DEFAULT_BOAT_DOC_URL)
            .openApi(DEFAULT_OPEN_API)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY);
        return spec;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spec createUpdatedEntity(EntityManager em) {
        Spec spec = new Spec()
            .key(UPDATED_KEY)
            .title(UPDATED_TITLE)
            .openApiUrl(UPDATED_OPEN_API_URL)
            .boatDocUrl(UPDATED_BOAT_DOC_URL)
            .openApi(UPDATED_OPEN_API)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY);
        return spec;
    }

    @BeforeEach
    public void initTest() {
        spec = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpec() throws Exception {
        int databaseSizeBeforeCreate = specRepository.findAll().size();
        // Create the Spec
        restSpecMockMvc.perform(post("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isCreated());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeCreate + 1);
        Spec testSpec = specList.get(specList.size() - 1);
        assertThat(testSpec.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testSpec.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSpec.getOpenApiUrl()).isEqualTo(DEFAULT_OPEN_API_URL);
        assertThat(testSpec.getBoatDocUrl()).isEqualTo(DEFAULT_BOAT_DOC_URL);
        assertThat(testSpec.getOpenApi()).isEqualTo(DEFAULT_OPEN_API);
        assertThat(testSpec.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testSpec.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    public void createSpecWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = specRepository.findAll().size();

        // Create the Spec with an existing ID
        spec.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecMockMvc.perform(post("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSpecs() throws Exception {
        // Initialize the database
        specRepository.saveAndFlush(spec);

        // Get all the specList
        restSpecMockMvc.perform(get("/api/specs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spec.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].openApiUrl").value(hasItem(DEFAULT_OPEN_API_URL)))
            .andExpect(jsonPath("$.[*].boatDocUrl").value(hasItem(DEFAULT_BOAT_DOC_URL)))
            .andExpect(jsonPath("$.[*].openApi").value(hasItem(DEFAULT_OPEN_API)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }
    
    @Test
    @Transactional
    public void getSpec() throws Exception {
        // Initialize the database
        specRepository.saveAndFlush(spec);

        // Get the spec
        restSpecMockMvc.perform(get("/api/specs/{id}", spec.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spec.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.openApiUrl").value(DEFAULT_OPEN_API_URL))
            .andExpect(jsonPath("$.boatDocUrl").value(DEFAULT_BOAT_DOC_URL))
            .andExpect(jsonPath("$.openApi").value(DEFAULT_OPEN_API))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }
    @Test
    @Transactional
    public void getNonExistingSpec() throws Exception {
        // Get the spec
        restSpecMockMvc.perform(get("/api/specs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpec() throws Exception {
        // Initialize the database
        specRepository.saveAndFlush(spec);

        int databaseSizeBeforeUpdate = specRepository.findAll().size();

        // Update the spec
        Spec updatedSpec = specRepository.findById(spec.getId()).get();
        // Disconnect from session so that the updates on updatedSpec are not directly saved in db
        em.detach(updatedSpec);
        updatedSpec
            .key(UPDATED_KEY)
            .title(UPDATED_TITLE)
            .openApiUrl(UPDATED_OPEN_API_URL)
            .boatDocUrl(UPDATED_BOAT_DOC_URL)
            .openApi(UPDATED_OPEN_API)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY);

        restSpecMockMvc.perform(put("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpec)))
            .andExpect(status().isOk());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
        Spec testSpec = specList.get(specList.size() - 1);
        assertThat(testSpec.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSpec.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSpec.getOpenApiUrl()).isEqualTo(UPDATED_OPEN_API_URL);
        assertThat(testSpec.getBoatDocUrl()).isEqualTo(UPDATED_BOAT_DOC_URL);
        assertThat(testSpec.getOpenApi()).isEqualTo(UPDATED_OPEN_API);
        assertThat(testSpec.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testSpec.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecMockMvc.perform(put("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSpec() throws Exception {
        // Initialize the database
        specRepository.saveAndFlush(spec);

        int databaseSizeBeforeDelete = specRepository.findAll().size();

        // Delete the spec
        restSpecMockMvc.perform(delete("/api/specs/{id}", spec.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
