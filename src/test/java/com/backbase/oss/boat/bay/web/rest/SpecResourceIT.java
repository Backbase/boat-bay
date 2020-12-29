package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
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
 * Integration tests for the {@link SpecResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SpecResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_OPEN_API = "AAAAAAAAAA";
    private static final String UPDATED_OPEN_API = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_CHECKSUM = "BBBBBBBBBB";

    private static final String DEFAULT_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_FILENAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VALID = false;
    private static final Boolean UPDATED_VALID = true;

    private static final String DEFAULT_PARSE_ERROR = "AAAAAAAAAA";
    private static final String UPDATED_PARSE_ERROR = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_SOURCE_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SOURCE_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SOURCE_LAST_MODIFIED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SOURCE_LAST_MODIFIED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SOURCE_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_LAST_MODIFIED_BY = "BBBBBBBBBB";

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
            .name(DEFAULT_NAME)
            .version(DEFAULT_VERSION)
            .title(DEFAULT_TITLE)
            .openApi(DEFAULT_OPEN_API)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .checksum(DEFAULT_CHECKSUM)
            .filename(DEFAULT_FILENAME)
            .valid(DEFAULT_VALID)
            .parseError(DEFAULT_PARSE_ERROR)
            .sourcePath(DEFAULT_SOURCE_PATH)
            .sourceName(DEFAULT_SOURCE_NAME)
            .sourceUrl(DEFAULT_SOURCE_URL)
            .sourceCreatedBy(DEFAULT_SOURCE_CREATED_BY)
            .sourceCreatedOn(DEFAULT_SOURCE_CREATED_ON)
            .sourceLastModifiedOn(DEFAULT_SOURCE_LAST_MODIFIED_ON)
            .sourceLastModifiedBy(DEFAULT_SOURCE_LAST_MODIFIED_BY);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        spec.setPortal(portal);
        // Add required entity
        Capability capability;
        if (TestUtil.findAll(em, Capability.class).isEmpty()) {
            capability = CapabilityResourceIT.createEntity(em);
            em.persist(capability);
            em.flush();
        } else {
            capability = TestUtil.findAll(em, Capability.class).get(0);
        }
        spec.setCapability(capability);
        // Add required entity
        ServiceDefinition serviceDefinition;
        if (TestUtil.findAll(em, ServiceDefinition.class).isEmpty()) {
            serviceDefinition = ServiceDefinitionResourceIT.createEntity(em);
            em.persist(serviceDefinition);
            em.flush();
        } else {
            serviceDefinition = TestUtil.findAll(em, ServiceDefinition.class).get(0);
        }
        spec.setServiceDefinition(serviceDefinition);
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
            .name(UPDATED_NAME)
            .version(UPDATED_VERSION)
            .title(UPDATED_TITLE)
            .openApi(UPDATED_OPEN_API)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .checksum(UPDATED_CHECKSUM)
            .filename(UPDATED_FILENAME)
            .valid(UPDATED_VALID)
            .parseError(UPDATED_PARSE_ERROR)
            .sourcePath(UPDATED_SOURCE_PATH)
            .sourceName(UPDATED_SOURCE_NAME)
            .sourceUrl(UPDATED_SOURCE_URL)
            .sourceCreatedBy(UPDATED_SOURCE_CREATED_BY)
            .sourceCreatedOn(UPDATED_SOURCE_CREATED_ON)
            .sourceLastModifiedOn(UPDATED_SOURCE_LAST_MODIFIED_ON)
            .sourceLastModifiedBy(UPDATED_SOURCE_LAST_MODIFIED_BY);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createUpdatedEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        spec.setPortal(portal);
        // Add required entity
        Capability capability;
        if (TestUtil.findAll(em, Capability.class).isEmpty()) {
            capability = CapabilityResourceIT.createUpdatedEntity(em);
            em.persist(capability);
            em.flush();
        } else {
            capability = TestUtil.findAll(em, Capability.class).get(0);
        }
        spec.setCapability(capability);
        // Add required entity
        ServiceDefinition serviceDefinition;
        if (TestUtil.findAll(em, ServiceDefinition.class).isEmpty()) {
            serviceDefinition = ServiceDefinitionResourceIT.createUpdatedEntity(em);
            em.persist(serviceDefinition);
            em.flush();
        } else {
            serviceDefinition = TestUtil.findAll(em, ServiceDefinition.class).get(0);
        }
        spec.setServiceDefinition(serviceDefinition);
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
        assertThat(testSpec.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpec.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testSpec.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSpec.getOpenApi()).isEqualTo(DEFAULT_OPEN_API);
        assertThat(testSpec.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testSpec.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpec.getChecksum()).isEqualTo(DEFAULT_CHECKSUM);
        assertThat(testSpec.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testSpec.isValid()).isEqualTo(DEFAULT_VALID);
        assertThat(testSpec.getParseError()).isEqualTo(DEFAULT_PARSE_ERROR);
        assertThat(testSpec.getSourcePath()).isEqualTo(DEFAULT_SOURCE_PATH);
        assertThat(testSpec.getSourceName()).isEqualTo(DEFAULT_SOURCE_NAME);
        assertThat(testSpec.getSourceUrl()).isEqualTo(DEFAULT_SOURCE_URL);
        assertThat(testSpec.getSourceCreatedBy()).isEqualTo(DEFAULT_SOURCE_CREATED_BY);
        assertThat(testSpec.getSourceCreatedOn()).isEqualTo(DEFAULT_SOURCE_CREATED_ON);
        assertThat(testSpec.getSourceLastModifiedOn()).isEqualTo(DEFAULT_SOURCE_LAST_MODIFIED_ON);
        assertThat(testSpec.getSourceLastModifiedBy()).isEqualTo(DEFAULT_SOURCE_LAST_MODIFIED_BY);
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
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setKey(null);

        // Create the Spec, which fails.


        restSpecMockMvc.perform(post("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setName(null);

        // Create the Spec, which fails.


        restSpecMockMvc.perform(post("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setVersion(null);

        // Create the Spec, which fails.


        restSpecMockMvc.perform(post("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setCreatedOn(null);

        // Create the Spec, which fails.


        restSpecMockMvc.perform(post("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setCreatedBy(null);

        // Create the Spec, which fails.


        restSpecMockMvc.perform(post("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChecksumIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setChecksum(null);

        // Create the Spec, which fails.


        restSpecMockMvc.perform(post("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFilenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setFilename(null);

        // Create the Spec, which fails.


        restSpecMockMvc.perform(post("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setValid(null);

        // Create the Spec, which fails.


        restSpecMockMvc.perform(post("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].openApi").value(hasItem(DEFAULT_OPEN_API.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].checksum").value(hasItem(DEFAULT_CHECKSUM)))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)))
            .andExpect(jsonPath("$.[*].valid").value(hasItem(DEFAULT_VALID.booleanValue())))
            .andExpect(jsonPath("$.[*].parseError").value(hasItem(DEFAULT_PARSE_ERROR.toString())))
            .andExpect(jsonPath("$.[*].sourcePath").value(hasItem(DEFAULT_SOURCE_PATH)))
            .andExpect(jsonPath("$.[*].sourceName").value(hasItem(DEFAULT_SOURCE_NAME)))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)))
            .andExpect(jsonPath("$.[*].sourceCreatedBy").value(hasItem(DEFAULT_SOURCE_CREATED_BY)))
            .andExpect(jsonPath("$.[*].sourceCreatedOn").value(hasItem(DEFAULT_SOURCE_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].sourceLastModifiedOn").value(hasItem(DEFAULT_SOURCE_LAST_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].sourceLastModifiedBy").value(hasItem(DEFAULT_SOURCE_LAST_MODIFIED_BY)));
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
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.openApi").value(DEFAULT_OPEN_API.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.checksum").value(DEFAULT_CHECKSUM))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME))
            .andExpect(jsonPath("$.valid").value(DEFAULT_VALID.booleanValue()))
            .andExpect(jsonPath("$.parseError").value(DEFAULT_PARSE_ERROR.toString()))
            .andExpect(jsonPath("$.sourcePath").value(DEFAULT_SOURCE_PATH))
            .andExpect(jsonPath("$.sourceName").value(DEFAULT_SOURCE_NAME))
            .andExpect(jsonPath("$.sourceUrl").value(DEFAULT_SOURCE_URL))
            .andExpect(jsonPath("$.sourceCreatedBy").value(DEFAULT_SOURCE_CREATED_BY))
            .andExpect(jsonPath("$.sourceCreatedOn").value(DEFAULT_SOURCE_CREATED_ON.toString()))
            .andExpect(jsonPath("$.sourceLastModifiedOn").value(DEFAULT_SOURCE_LAST_MODIFIED_ON.toString()))
            .andExpect(jsonPath("$.sourceLastModifiedBy").value(DEFAULT_SOURCE_LAST_MODIFIED_BY));
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
            .name(UPDATED_NAME)
            .version(UPDATED_VERSION)
            .title(UPDATED_TITLE)
            .openApi(UPDATED_OPEN_API)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .checksum(UPDATED_CHECKSUM)
            .filename(UPDATED_FILENAME)
            .valid(UPDATED_VALID)
            .parseError(UPDATED_PARSE_ERROR)
            .sourcePath(UPDATED_SOURCE_PATH)
            .sourceName(UPDATED_SOURCE_NAME)
            .sourceUrl(UPDATED_SOURCE_URL)
            .sourceCreatedBy(UPDATED_SOURCE_CREATED_BY)
            .sourceCreatedOn(UPDATED_SOURCE_CREATED_ON)
            .sourceLastModifiedOn(UPDATED_SOURCE_LAST_MODIFIED_ON)
            .sourceLastModifiedBy(UPDATED_SOURCE_LAST_MODIFIED_BY);

        restSpecMockMvc.perform(put("/api/specs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpec)))
            .andExpect(status().isOk());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
        Spec testSpec = specList.get(specList.size() - 1);
        assertThat(testSpec.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSpec.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpec.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testSpec.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSpec.getOpenApi()).isEqualTo(UPDATED_OPEN_API);
        assertThat(testSpec.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testSpec.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpec.getChecksum()).isEqualTo(UPDATED_CHECKSUM);
        assertThat(testSpec.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testSpec.isValid()).isEqualTo(UPDATED_VALID);
        assertThat(testSpec.getParseError()).isEqualTo(UPDATED_PARSE_ERROR);
        assertThat(testSpec.getSourcePath()).isEqualTo(UPDATED_SOURCE_PATH);
        assertThat(testSpec.getSourceName()).isEqualTo(UPDATED_SOURCE_NAME);
        assertThat(testSpec.getSourceUrl()).isEqualTo(UPDATED_SOURCE_URL);
        assertThat(testSpec.getSourceCreatedBy()).isEqualTo(UPDATED_SOURCE_CREATED_BY);
        assertThat(testSpec.getSourceCreatedOn()).isEqualTo(UPDATED_SOURCE_CREATED_ON);
        assertThat(testSpec.getSourceLastModifiedOn()).isEqualTo(UPDATED_SOURCE_LAST_MODIFIED_ON);
        assertThat(testSpec.getSourceLastModifiedBy()).isEqualTo(UPDATED_SOURCE_LAST_MODIFIED_BY);
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
