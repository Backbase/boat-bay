package com.backbase.oss.boat.bay.web.rest;

import static com.backbase.oss.boat.bay.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backbase.oss.boat.bay.IntegrationTest;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.Changes;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link SpecResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SpecResourceIT {

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

    private static final Changes DEFAULT_CHANGES = Changes.INVALID_VERSION;
    private static final Changes UPDATED_CHANGES = Changes.NOT_APPLICABLE;

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

    private static final String DEFAULT_MVN_GROUP_ID = "AAAAAAAAAA";
    private static final String UPDATED_MVN_GROUP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MVN_ARTIFACT_ID = "AAAAAAAAAA";
    private static final String UPDATED_MVN_ARTIFACT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MVN_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_MVN_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_MVN_CLASSIFIER = "AAAAAAAAAA";
    private static final String UPDATED_MVN_CLASSIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_MVN_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_MVN_EXTENSION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/specs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
            .changes(DEFAULT_CHANGES)
            .sourcePath(DEFAULT_SOURCE_PATH)
            .sourceName(DEFAULT_SOURCE_NAME)
            .sourceUrl(DEFAULT_SOURCE_URL)
            .sourceCreatedBy(DEFAULT_SOURCE_CREATED_BY)
            .sourceCreatedOn(DEFAULT_SOURCE_CREATED_ON)
            .sourceLastModifiedOn(DEFAULT_SOURCE_LAST_MODIFIED_ON)
            .sourceLastModifiedBy(DEFAULT_SOURCE_LAST_MODIFIED_BY)
            .mvnGroupId(DEFAULT_MVN_GROUP_ID)
            .mvnArtifactId(DEFAULT_MVN_ARTIFACT_ID)
            .mvnVersion(DEFAULT_MVN_VERSION)
            .mvnClassifier(DEFAULT_MVN_CLASSIFIER)
            .mvnExtension(DEFAULT_MVN_EXTENSION);
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
            .changes(UPDATED_CHANGES)
            .sourcePath(UPDATED_SOURCE_PATH)
            .sourceName(UPDATED_SOURCE_NAME)
            .sourceUrl(UPDATED_SOURCE_URL)
            .sourceCreatedBy(UPDATED_SOURCE_CREATED_BY)
            .sourceCreatedOn(UPDATED_SOURCE_CREATED_ON)
            .sourceLastModifiedOn(UPDATED_SOURCE_LAST_MODIFIED_ON)
            .sourceLastModifiedBy(UPDATED_SOURCE_LAST_MODIFIED_BY)
            .mvnGroupId(UPDATED_MVN_GROUP_ID)
            .mvnArtifactId(UPDATED_MVN_ARTIFACT_ID)
            .mvnVersion(UPDATED_MVN_VERSION)
            .mvnClassifier(UPDATED_MVN_CLASSIFIER)
            .mvnExtension(UPDATED_MVN_EXTENSION);
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
    void createSpec() throws Exception {
        int databaseSizeBeforeCreate = specRepository.findAll().size();
        // Create the Spec
        restSpecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spec)))
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
        assertThat(testSpec.getValid()).isEqualTo(DEFAULT_VALID);
        assertThat(testSpec.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testSpec.getParseError()).isEqualTo(DEFAULT_PARSE_ERROR);
        assertThat(testSpec.getExternalDocs()).isEqualTo(DEFAULT_EXTERNAL_DOCS);
        assertThat(testSpec.getHide()).isEqualTo(DEFAULT_HIDE);
        assertThat(testSpec.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testSpec.getChanges()).isEqualTo(DEFAULT_CHANGES);
        assertThat(testSpec.getSourcePath()).isEqualTo(DEFAULT_SOURCE_PATH);
        assertThat(testSpec.getSourceName()).isEqualTo(DEFAULT_SOURCE_NAME);
        assertThat(testSpec.getSourceUrl()).isEqualTo(DEFAULT_SOURCE_URL);
        assertThat(testSpec.getSourceCreatedBy()).isEqualTo(DEFAULT_SOURCE_CREATED_BY);
        assertThat(testSpec.getSourceCreatedOn()).isEqualTo(DEFAULT_SOURCE_CREATED_ON);
        assertThat(testSpec.getSourceLastModifiedOn()).isEqualTo(DEFAULT_SOURCE_LAST_MODIFIED_ON);
        assertThat(testSpec.getSourceLastModifiedBy()).isEqualTo(DEFAULT_SOURCE_LAST_MODIFIED_BY);
        assertThat(testSpec.getMvnGroupId()).isEqualTo(DEFAULT_MVN_GROUP_ID);
        assertThat(testSpec.getMvnArtifactId()).isEqualTo(DEFAULT_MVN_ARTIFACT_ID);
        assertThat(testSpec.getMvnVersion()).isEqualTo(DEFAULT_MVN_VERSION);
        assertThat(testSpec.getMvnClassifier()).isEqualTo(DEFAULT_MVN_CLASSIFIER);
        assertThat(testSpec.getMvnExtension()).isEqualTo(DEFAULT_MVN_EXTENSION);
    }

    @Test
    @Transactional
    void createSpecWithExistingId() throws Exception {
        // Create the Spec with an existing ID
        spec.setId(1L);

        int databaseSizeBeforeCreate = specRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setKey(null);

        // Create the Spec, which fails.

        restSpecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setName(null);

        // Create the Spec, which fails.

        restSpecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setVersion(null);

        // Create the Spec, which fails.

        restSpecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setCreatedOn(null);

        // Create the Spec, which fails.

        restSpecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setCreatedBy(null);

        // Create the Spec, which fails.

        restSpecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChecksumIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setChecksum(null);

        // Create the Spec, which fails.

        restSpecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFilenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setFilename(null);

        // Create the Spec, which fails.

        restSpecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValidIsRequired() throws Exception {
        int databaseSizeBeforeTest = specRepository.findAll().size();
        // set the field null
        spec.setValid(null);

        // Create the Spec, which fails.

        restSpecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isBadRequest());

        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecs() throws Exception {
        // Initialize the database
        specRepository.saveAndFlush(spec);

        // Get all the specList
        restSpecMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
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
            .andExpect(jsonPath("$.[*].changes").value(hasItem(DEFAULT_CHANGES.toString())))
            .andExpect(jsonPath("$.[*].sourcePath").value(hasItem(DEFAULT_SOURCE_PATH)))
            .andExpect(jsonPath("$.[*].sourceName").value(hasItem(DEFAULT_SOURCE_NAME)))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)))
            .andExpect(jsonPath("$.[*].sourceCreatedBy").value(hasItem(DEFAULT_SOURCE_CREATED_BY)))
            .andExpect(jsonPath("$.[*].sourceCreatedOn").value(hasItem(sameInstant(DEFAULT_SOURCE_CREATED_ON))))
            .andExpect(jsonPath("$.[*].sourceLastModifiedOn").value(hasItem(sameInstant(DEFAULT_SOURCE_LAST_MODIFIED_ON))))
            .andExpect(jsonPath("$.[*].sourceLastModifiedBy").value(hasItem(DEFAULT_SOURCE_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].mvnGroupId").value(hasItem(DEFAULT_MVN_GROUP_ID)))
            .andExpect(jsonPath("$.[*].mvnArtifactId").value(hasItem(DEFAULT_MVN_ARTIFACT_ID)))
            .andExpect(jsonPath("$.[*].mvnVersion").value(hasItem(DEFAULT_MVN_VERSION)))
            .andExpect(jsonPath("$.[*].mvnClassifier").value(hasItem(DEFAULT_MVN_CLASSIFIER)))
            .andExpect(jsonPath("$.[*].mvnExtension").value(hasItem(DEFAULT_MVN_EXTENSION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpecsWithEagerRelationshipsIsEnabled() throws Exception {
        when(specRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpecMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(specRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpecsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(specRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpecMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(specRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSpec() throws Exception {
        // Initialize the database
        specRepository.saveAndFlush(spec);

        // Get the spec
        restSpecMockMvc
            .perform(get(ENTITY_API_URL_ID, spec.getId()))
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
            .andExpect(jsonPath("$.changes").value(DEFAULT_CHANGES.toString()))
            .andExpect(jsonPath("$.sourcePath").value(DEFAULT_SOURCE_PATH))
            .andExpect(jsonPath("$.sourceName").value(DEFAULT_SOURCE_NAME))
            .andExpect(jsonPath("$.sourceUrl").value(DEFAULT_SOURCE_URL))
            .andExpect(jsonPath("$.sourceCreatedBy").value(DEFAULT_SOURCE_CREATED_BY))
            .andExpect(jsonPath("$.sourceCreatedOn").value(sameInstant(DEFAULT_SOURCE_CREATED_ON)))
            .andExpect(jsonPath("$.sourceLastModifiedOn").value(sameInstant(DEFAULT_SOURCE_LAST_MODIFIED_ON)))
            .andExpect(jsonPath("$.sourceLastModifiedBy").value(DEFAULT_SOURCE_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.mvnGroupId").value(DEFAULT_MVN_GROUP_ID))
            .andExpect(jsonPath("$.mvnArtifactId").value(DEFAULT_MVN_ARTIFACT_ID))
            .andExpect(jsonPath("$.mvnVersion").value(DEFAULT_MVN_VERSION))
            .andExpect(jsonPath("$.mvnClassifier").value(DEFAULT_MVN_CLASSIFIER))
            .andExpect(jsonPath("$.mvnExtension").value(DEFAULT_MVN_EXTENSION));
    }

    @Test
    @Transactional
    void getNonExistingSpec() throws Exception {
        // Get the spec
        restSpecMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSpec() throws Exception {
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
            .changes(UPDATED_CHANGES)
            .sourcePath(UPDATED_SOURCE_PATH)
            .sourceName(UPDATED_SOURCE_NAME)
            .sourceUrl(UPDATED_SOURCE_URL)
            .sourceCreatedBy(UPDATED_SOURCE_CREATED_BY)
            .sourceCreatedOn(UPDATED_SOURCE_CREATED_ON)
            .sourceLastModifiedOn(UPDATED_SOURCE_LAST_MODIFIED_ON)
            .sourceLastModifiedBy(UPDATED_SOURCE_LAST_MODIFIED_BY)
            .mvnGroupId(UPDATED_MVN_GROUP_ID)
            .mvnArtifactId(UPDATED_MVN_ARTIFACT_ID)
            .mvnVersion(UPDATED_MVN_VERSION)
            .mvnClassifier(UPDATED_MVN_CLASSIFIER)
            .mvnExtension(UPDATED_MVN_EXTENSION);

        restSpecMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpec.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpec))
            )
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
        assertThat(testSpec.getValid()).isEqualTo(UPDATED_VALID);
        assertThat(testSpec.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testSpec.getParseError()).isEqualTo(UPDATED_PARSE_ERROR);
        assertThat(testSpec.getExternalDocs()).isEqualTo(UPDATED_EXTERNAL_DOCS);
        assertThat(testSpec.getHide()).isEqualTo(UPDATED_HIDE);
        assertThat(testSpec.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testSpec.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testSpec.getSourcePath()).isEqualTo(UPDATED_SOURCE_PATH);
        assertThat(testSpec.getSourceName()).isEqualTo(UPDATED_SOURCE_NAME);
        assertThat(testSpec.getSourceUrl()).isEqualTo(UPDATED_SOURCE_URL);
        assertThat(testSpec.getSourceCreatedBy()).isEqualTo(UPDATED_SOURCE_CREATED_BY);
        assertThat(testSpec.getSourceCreatedOn()).isEqualTo(UPDATED_SOURCE_CREATED_ON);
        assertThat(testSpec.getSourceLastModifiedOn()).isEqualTo(UPDATED_SOURCE_LAST_MODIFIED_ON);
        assertThat(testSpec.getSourceLastModifiedBy()).isEqualTo(UPDATED_SOURCE_LAST_MODIFIED_BY);
        assertThat(testSpec.getMvnGroupId()).isEqualTo(UPDATED_MVN_GROUP_ID);
        assertThat(testSpec.getMvnArtifactId()).isEqualTo(UPDATED_MVN_ARTIFACT_ID);
        assertThat(testSpec.getMvnVersion()).isEqualTo(UPDATED_MVN_VERSION);
        assertThat(testSpec.getMvnClassifier()).isEqualTo(UPDATED_MVN_CLASSIFIER);
        assertThat(testSpec.getMvnExtension()).isEqualTo(UPDATED_MVN_EXTENSION);
    }

    @Test
    @Transactional
    void putNonExistingSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();
        spec.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spec.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spec))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();
        spec.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spec))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();
        spec.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecWithPatch() throws Exception {
        // Initialize the database
        specRepository.saveAndFlush(spec);

        int databaseSizeBeforeUpdate = specRepository.findAll().size();

        // Update the spec using partial update
        Spec partialUpdatedSpec = new Spec();
        partialUpdatedSpec.setId(spec.getId());

        partialUpdatedSpec
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .icon(UPDATED_ICON)
            .openApi(UPDATED_OPEN_API)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .checksum(UPDATED_CHECKSUM)
            .valid(UPDATED_VALID)
            .parseError(UPDATED_PARSE_ERROR)
            .externalDocs(UPDATED_EXTERNAL_DOCS)
            .hide(UPDATED_HIDE)
            .grade(UPDATED_GRADE)
            .sourceName(UPDATED_SOURCE_NAME)
            .sourceUrl(UPDATED_SOURCE_URL)
            .sourceCreatedOn(UPDATED_SOURCE_CREATED_ON)
            .sourceLastModifiedBy(UPDATED_SOURCE_LAST_MODIFIED_BY)
            .mvnArtifactId(UPDATED_MVN_ARTIFACT_ID)
            .mvnExtension(UPDATED_MVN_EXTENSION);

        restSpecMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpec.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpec))
            )
            .andExpect(status().isOk());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
        Spec testSpec = specList.get(specList.size() - 1);
        assertThat(testSpec.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSpec.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpec.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testSpec.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSpec.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testSpec.getOpenApi()).isEqualTo(UPDATED_OPEN_API);
        assertThat(testSpec.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpec.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testSpec.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpec.getChecksum()).isEqualTo(UPDATED_CHECKSUM);
        assertThat(testSpec.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testSpec.getValid()).isEqualTo(UPDATED_VALID);
        assertThat(testSpec.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testSpec.getParseError()).isEqualTo(UPDATED_PARSE_ERROR);
        assertThat(testSpec.getExternalDocs()).isEqualTo(UPDATED_EXTERNAL_DOCS);
        assertThat(testSpec.getHide()).isEqualTo(UPDATED_HIDE);
        assertThat(testSpec.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testSpec.getChanges()).isEqualTo(DEFAULT_CHANGES);
        assertThat(testSpec.getSourcePath()).isEqualTo(DEFAULT_SOURCE_PATH);
        assertThat(testSpec.getSourceName()).isEqualTo(UPDATED_SOURCE_NAME);
        assertThat(testSpec.getSourceUrl()).isEqualTo(UPDATED_SOURCE_URL);
        assertThat(testSpec.getSourceCreatedBy()).isEqualTo(DEFAULT_SOURCE_CREATED_BY);
        assertThat(testSpec.getSourceCreatedOn()).isEqualTo(UPDATED_SOURCE_CREATED_ON);
        assertThat(testSpec.getSourceLastModifiedOn()).isEqualTo(DEFAULT_SOURCE_LAST_MODIFIED_ON);
        assertThat(testSpec.getSourceLastModifiedBy()).isEqualTo(UPDATED_SOURCE_LAST_MODIFIED_BY);
        assertThat(testSpec.getMvnGroupId()).isEqualTo(DEFAULT_MVN_GROUP_ID);
        assertThat(testSpec.getMvnArtifactId()).isEqualTo(UPDATED_MVN_ARTIFACT_ID);
        assertThat(testSpec.getMvnVersion()).isEqualTo(DEFAULT_MVN_VERSION);
        assertThat(testSpec.getMvnClassifier()).isEqualTo(DEFAULT_MVN_CLASSIFIER);
        assertThat(testSpec.getMvnExtension()).isEqualTo(UPDATED_MVN_EXTENSION);
    }

    @Test
    @Transactional
    void fullUpdateSpecWithPatch() throws Exception {
        // Initialize the database
        specRepository.saveAndFlush(spec);

        int databaseSizeBeforeUpdate = specRepository.findAll().size();

        // Update the spec using partial update
        Spec partialUpdatedSpec = new Spec();
        partialUpdatedSpec.setId(spec.getId());

        partialUpdatedSpec
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
            .changes(UPDATED_CHANGES)
            .sourcePath(UPDATED_SOURCE_PATH)
            .sourceName(UPDATED_SOURCE_NAME)
            .sourceUrl(UPDATED_SOURCE_URL)
            .sourceCreatedBy(UPDATED_SOURCE_CREATED_BY)
            .sourceCreatedOn(UPDATED_SOURCE_CREATED_ON)
            .sourceLastModifiedOn(UPDATED_SOURCE_LAST_MODIFIED_ON)
            .sourceLastModifiedBy(UPDATED_SOURCE_LAST_MODIFIED_BY)
            .mvnGroupId(UPDATED_MVN_GROUP_ID)
            .mvnArtifactId(UPDATED_MVN_ARTIFACT_ID)
            .mvnVersion(UPDATED_MVN_VERSION)
            .mvnClassifier(UPDATED_MVN_CLASSIFIER)
            .mvnExtension(UPDATED_MVN_EXTENSION);

        restSpecMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpec.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpec))
            )
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
        assertThat(testSpec.getValid()).isEqualTo(UPDATED_VALID);
        assertThat(testSpec.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testSpec.getParseError()).isEqualTo(UPDATED_PARSE_ERROR);
        assertThat(testSpec.getExternalDocs()).isEqualTo(UPDATED_EXTERNAL_DOCS);
        assertThat(testSpec.getHide()).isEqualTo(UPDATED_HIDE);
        assertThat(testSpec.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testSpec.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testSpec.getSourcePath()).isEqualTo(UPDATED_SOURCE_PATH);
        assertThat(testSpec.getSourceName()).isEqualTo(UPDATED_SOURCE_NAME);
        assertThat(testSpec.getSourceUrl()).isEqualTo(UPDATED_SOURCE_URL);
        assertThat(testSpec.getSourceCreatedBy()).isEqualTo(UPDATED_SOURCE_CREATED_BY);
        assertThat(testSpec.getSourceCreatedOn()).isEqualTo(UPDATED_SOURCE_CREATED_ON);
        assertThat(testSpec.getSourceLastModifiedOn()).isEqualTo(UPDATED_SOURCE_LAST_MODIFIED_ON);
        assertThat(testSpec.getSourceLastModifiedBy()).isEqualTo(UPDATED_SOURCE_LAST_MODIFIED_BY);
        assertThat(testSpec.getMvnGroupId()).isEqualTo(UPDATED_MVN_GROUP_ID);
        assertThat(testSpec.getMvnArtifactId()).isEqualTo(UPDATED_MVN_ARTIFACT_ID);
        assertThat(testSpec.getMvnVersion()).isEqualTo(UPDATED_MVN_VERSION);
        assertThat(testSpec.getMvnClassifier()).isEqualTo(UPDATED_MVN_CLASSIFIER);
        assertThat(testSpec.getMvnExtension()).isEqualTo(UPDATED_MVN_EXTENSION);
    }

    @Test
    @Transactional
    void patchNonExistingSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();
        spec.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spec.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spec))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();
        spec.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spec))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();
        spec.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(spec)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpec() throws Exception {
        // Initialize the database
        specRepository.saveAndFlush(spec);

        int databaseSizeBeforeDelete = specRepository.findAll().size();

        // Delete the spec
        restSpecMockMvc
            .perform(delete(ENTITY_API_URL_ID, spec.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
