package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.SpecType;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.repository.SpecRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.backbase.oss.boat.bay.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SpecResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@ExtendWith(MockitoExtension.class)
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

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_OPEN_API = "AAAAAAAAAA";
    private static final String UPDATED_OPEN_API = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_CHECKSUM = "BBBBBBBBBB";

    private static final String DEFAULT_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_FILENAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VALID = false;
    private static final Boolean UPDATED_VALID = true;

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String DEFAULT_PARSE_ERROR = "AAAAAAAAAA";
    private static final String UPDATED_PARSE_ERROR = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_DOCS = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_DOCS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HIDE = false;
    private static final Boolean UPDATED_HIDE = true;

    private static final String DEFAULT_GRADE = "AAAAAAAAAA";
    private static final String UPDATED_GRADE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_BACKWARDS_COMPATIBLE = false;
    private static final Boolean UPDATED_BACKWARDS_COMPATIBLE = true;

    private static final Boolean DEFAULT_CHANGED = false;
    private static final Boolean UPDATED_CHANGED = true;

    private static final String DEFAULT_SOURCE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_CREATED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SOURCE_CREATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SOURCE_CREATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_SOURCE_LAST_MODIFIED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SOURCE_LAST_MODIFIED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SOURCE_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private SpecRepository specRepository;

    @Mock
    private SpecRepository specRepositoryMock;

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
            .icon(DEFAULT_ICON)
            .openApi(DEFAULT_OPEN_API)
            .description(DEFAULT_DESCRIPTION)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .checksum(DEFAULT_CHECKSUM)
            .filename(DEFAULT_FILENAME)
            .valid(DEFAULT_VALID)
            .order(DEFAULT_ORDER)
            .parseError(DEFAULT_PARSE_ERROR)
            .externalDocs(DEFAULT_EXTERNAL_DOCS)
            .hide(DEFAULT_HIDE)
            .grade(DEFAULT_GRADE)
            .backwardsCompatible(DEFAULT_BACKWARDS_COMPATIBLE)
            .changed(DEFAULT_CHANGED)
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
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        spec.setProduct(product);
        // Add required entity
        SpecType specType;
        if (TestUtil.findAll(em, SpecType.class).isEmpty()) {
            specType = SpecTypeResourceIT.createEntity(em);
            em.persist(specType);
            em.flush();
        } else {
            specType = TestUtil.findAll(em, SpecType.class).get(0);
        }
        spec.setSpecType(specType);
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
            .icon(UPDATED_ICON)
            .openApi(UPDATED_OPEN_API)
            .description(UPDATED_DESCRIPTION)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .checksum(UPDATED_CHECKSUM)
            .filename(UPDATED_FILENAME)
            .valid(UPDATED_VALID)
            .order(UPDATED_ORDER)
            .parseError(UPDATED_PARSE_ERROR)
            .externalDocs(UPDATED_EXTERNAL_DOCS)
            .hide(UPDATED_HIDE)
            .grade(UPDATED_GRADE)
            .backwardsCompatible(UPDATED_BACKWARDS_COMPATIBLE)
            .changed(UPDATED_CHANGED)
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
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        spec.setProduct(product);
        // Add required entity
        SpecType specType;
        if (TestUtil.findAll(em, SpecType.class).isEmpty()) {
            specType = SpecTypeResourceIT.createUpdatedEntity(em);
            em.persist(specType);
            em.flush();
        } else {
            specType = TestUtil.findAll(em, SpecType.class).get(0);
        }
        spec.setSpecType(specType);
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
        assertThat(testSpec.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testSpec.getOpenApi()).isEqualTo(DEFAULT_OPEN_API);
        assertThat(testSpec.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpec.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testSpec.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpec.getChecksum()).isEqualTo(DEFAULT_CHECKSUM);
        assertThat(testSpec.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testSpec.isValid()).isEqualTo(DEFAULT_VALID);
        assertThat(testSpec.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testSpec.getParseError()).isEqualTo(DEFAULT_PARSE_ERROR);
        assertThat(testSpec.getExternalDocs()).isEqualTo(DEFAULT_EXTERNAL_DOCS);
        assertThat(testSpec.isHide()).isEqualTo(DEFAULT_HIDE);
        assertThat(testSpec.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testSpec.isBackwardsCompatible()).isEqualTo(DEFAULT_BACKWARDS_COMPATIBLE);
        assertThat(testSpec.isChanged()).isEqualTo(DEFAULT_CHANGED);
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
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].openApi").value(hasItem(DEFAULT_OPEN_API.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(sameInstant(DEFAULT_CREATED_ON))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].checksum").value(hasItem(DEFAULT_CHECKSUM)))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)))
            .andExpect(jsonPath("$.[*].valid").value(hasItem(DEFAULT_VALID.booleanValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].parseError").value(hasItem(DEFAULT_PARSE_ERROR.toString())))
            .andExpect(jsonPath("$.[*].externalDocs").value(hasItem(DEFAULT_EXTERNAL_DOCS)))
            .andExpect(jsonPath("$.[*].hide").value(hasItem(DEFAULT_HIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)))
            .andExpect(jsonPath("$.[*].backwardsCompatible").value(hasItem(DEFAULT_BACKWARDS_COMPATIBLE.booleanValue())))
            .andExpect(jsonPath("$.[*].changed").value(hasItem(DEFAULT_CHANGED.booleanValue())))
            .andExpect(jsonPath("$.[*].sourcePath").value(hasItem(DEFAULT_SOURCE_PATH)))
            .andExpect(jsonPath("$.[*].sourceName").value(hasItem(DEFAULT_SOURCE_NAME)))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)))
            .andExpect(jsonPath("$.[*].sourceCreatedBy").value(hasItem(DEFAULT_SOURCE_CREATED_BY)))
            .andExpect(jsonPath("$.[*].sourceCreatedOn").value(hasItem(sameInstant(DEFAULT_SOURCE_CREATED_ON))))
            .andExpect(jsonPath("$.[*].sourceLastModifiedOn").value(hasItem(sameInstant(DEFAULT_SOURCE_LAST_MODIFIED_ON))))
            .andExpect(jsonPath("$.[*].sourceLastModifiedBy").value(hasItem(DEFAULT_SOURCE_LAST_MODIFIED_BY)));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllSpecsWithEagerRelationshipsIsEnabled() throws Exception {
        when(specRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpecMockMvc.perform(get("/api/specs?eagerload=true"))
            .andExpect(status().isOk());

        verify(specRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllSpecsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(specRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpecMockMvc.perform(get("/api/specs?eagerload=true"))
            .andExpect(status().isOk());

        verify(specRepositoryMock, times(1)).findAllWithEagerRelationships(any());
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
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.openApi").value(DEFAULT_OPEN_API.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdOn").value(sameInstant(DEFAULT_CREATED_ON)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.checksum").value(DEFAULT_CHECKSUM))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME))
            .andExpect(jsonPath("$.valid").value(DEFAULT_VALID.booleanValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.parseError").value(DEFAULT_PARSE_ERROR.toString()))
            .andExpect(jsonPath("$.externalDocs").value(DEFAULT_EXTERNAL_DOCS))
            .andExpect(jsonPath("$.hide").value(DEFAULT_HIDE.booleanValue()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE))
            .andExpect(jsonPath("$.backwardsCompatible").value(DEFAULT_BACKWARDS_COMPATIBLE.booleanValue()))
            .andExpect(jsonPath("$.changed").value(DEFAULT_CHANGED.booleanValue()))
            .andExpect(jsonPath("$.sourcePath").value(DEFAULT_SOURCE_PATH))
            .andExpect(jsonPath("$.sourceName").value(DEFAULT_SOURCE_NAME))
            .andExpect(jsonPath("$.sourceUrl").value(DEFAULT_SOURCE_URL))
            .andExpect(jsonPath("$.sourceCreatedBy").value(DEFAULT_SOURCE_CREATED_BY))
            .andExpect(jsonPath("$.sourceCreatedOn").value(sameInstant(DEFAULT_SOURCE_CREATED_ON)))
            .andExpect(jsonPath("$.sourceLastModifiedOn").value(sameInstant(DEFAULT_SOURCE_LAST_MODIFIED_ON)))
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
            .icon(UPDATED_ICON)
            .openApi(UPDATED_OPEN_API)
            .description(UPDATED_DESCRIPTION)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .checksum(UPDATED_CHECKSUM)
            .filename(UPDATED_FILENAME)
            .valid(UPDATED_VALID)
            .order(UPDATED_ORDER)
            .parseError(UPDATED_PARSE_ERROR)
            .externalDocs(UPDATED_EXTERNAL_DOCS)
            .hide(UPDATED_HIDE)
            .grade(UPDATED_GRADE)
            .backwardsCompatible(UPDATED_BACKWARDS_COMPATIBLE)
            .changed(UPDATED_CHANGED)
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
        assertThat(testSpec.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testSpec.getOpenApi()).isEqualTo(UPDATED_OPEN_API);
        assertThat(testSpec.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpec.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testSpec.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpec.getChecksum()).isEqualTo(UPDATED_CHECKSUM);
        assertThat(testSpec.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testSpec.isValid()).isEqualTo(UPDATED_VALID);
        assertThat(testSpec.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testSpec.getParseError()).isEqualTo(UPDATED_PARSE_ERROR);
        assertThat(testSpec.getExternalDocs()).isEqualTo(UPDATED_EXTERNAL_DOCS);
        assertThat(testSpec.isHide()).isEqualTo(UPDATED_HIDE);
        assertThat(testSpec.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testSpec.isBackwardsCompatible()).isEqualTo(UPDATED_BACKWARDS_COMPATIBLE);
        assertThat(testSpec.isChanged()).isEqualTo(UPDATED_CHANGED);
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