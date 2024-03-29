package com.backbase.oss.boat.bay.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backbase.oss.boat.bay.IntegrationTest;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SourceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final SourceType DEFAULT_TYPE = SourceType.BOAT_MAVEN_PLUGIN;
    private static final SourceType UPDATED_TYPE = SourceType.MAVEN;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_FILTER_ARTIFACTS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILTER_ARTIFACTS_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FILTER_ARTIFACTS_CREATED_SINCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FILTER_ARTIFACTS_CREATED_SINCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CRON_EXPRESSION = "AAAAAAAAAA";
    private static final String UPDATED_CRON_EXPRESSION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RUN_ON_STARTUP = false;
    private static final Boolean UPDATED_RUN_ON_STARTUP = true;

    private static final String DEFAULT_SPEC_FILTER_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_SPEC_FILTER_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_CAPABILITY_KEY_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_CAPABILITY_KEY_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_CAPABILITY_NAME_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_CAPABILITY_NAME_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_KEY_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_KEY_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_NAME_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_SPEC_KEY_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_SPEC_KEY_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_RELEASE_NAME_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_RELEASE_NAME_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_RELEASE_VERSION_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_RELEASE_VERSION_SP_EL = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_RELEASE_KEY_SP_EL = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_RELEASE_KEY_SP_EL = "BBBBBBBBBB";

    private static final Integer DEFAULT_ITEM_LIMIT = 1;
    private static final Integer UPDATED_ITEM_LIMIT = 2;

    private static final Boolean DEFAULT_OVERWRITE_CHANGES = false;
    private static final Boolean UPDATED_OVERWRITE_CHANGES = true;

    private static final String DEFAULT_BILL_OF_MATERIALS_COORDS = "AAAAAAAAAA";
    private static final String UPDATED_BILL_OF_MATERIALS_COORDS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourceMockMvc;

    private Source source;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Source createEntity(EntityManager em) {
        Source source = new Source()
            .name(DEFAULT_NAME)
            .key(DEFAULT_KEY)
            .type(DEFAULT_TYPE)
            .active(DEFAULT_ACTIVE)
            .filterArtifactsName(DEFAULT_FILTER_ARTIFACTS_NAME)
            .filterArtifactsCreatedSince(DEFAULT_FILTER_ARTIFACTS_CREATED_SINCE)
            .cronExpression(DEFAULT_CRON_EXPRESSION)
            .runOnStartup(DEFAULT_RUN_ON_STARTUP)
            .specFilterSpEL(DEFAULT_SPEC_FILTER_SP_EL)
            .capabilityKeySpEL(DEFAULT_CAPABILITY_KEY_SP_EL)
            .capabilityNameSpEL(DEFAULT_CAPABILITY_NAME_SP_EL)
            .serviceKeySpEL(DEFAULT_SERVICE_KEY_SP_EL)
            .serviceNameSpEL(DEFAULT_SERVICE_NAME_SP_EL)
            .specKeySpEL(DEFAULT_SPEC_KEY_SP_EL)
            .versionSpEL(DEFAULT_VERSION_SP_EL)
            .productReleaseNameSpEL(DEFAULT_PRODUCT_RELEASE_NAME_SP_EL)
            .productReleaseVersionSpEL(DEFAULT_PRODUCT_RELEASE_VERSION_SP_EL)
            .productReleaseKeySpEL(DEFAULT_PRODUCT_RELEASE_KEY_SP_EL)
            .itemLimit(DEFAULT_ITEM_LIMIT)
            .overwriteChanges(DEFAULT_OVERWRITE_CHANGES)
            .billOfMaterialsCoords(DEFAULT_BILL_OF_MATERIALS_COORDS);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        source.setPortal(portal);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        source.setProduct(product);
        return source;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Source createUpdatedEntity(EntityManager em) {
        Source source = new Source()
            .name(UPDATED_NAME)
            .key(UPDATED_KEY)
            .type(UPDATED_TYPE)
            .active(UPDATED_ACTIVE)
            .filterArtifactsName(UPDATED_FILTER_ARTIFACTS_NAME)
            .filterArtifactsCreatedSince(UPDATED_FILTER_ARTIFACTS_CREATED_SINCE)
            .cronExpression(UPDATED_CRON_EXPRESSION)
            .runOnStartup(UPDATED_RUN_ON_STARTUP)
            .specFilterSpEL(UPDATED_SPEC_FILTER_SP_EL)
            .capabilityKeySpEL(UPDATED_CAPABILITY_KEY_SP_EL)
            .capabilityNameSpEL(UPDATED_CAPABILITY_NAME_SP_EL)
            .serviceKeySpEL(UPDATED_SERVICE_KEY_SP_EL)
            .serviceNameSpEL(UPDATED_SERVICE_NAME_SP_EL)
            .specKeySpEL(UPDATED_SPEC_KEY_SP_EL)
            .versionSpEL(UPDATED_VERSION_SP_EL)
            .productReleaseNameSpEL(UPDATED_PRODUCT_RELEASE_NAME_SP_EL)
            .productReleaseVersionSpEL(UPDATED_PRODUCT_RELEASE_VERSION_SP_EL)
            .productReleaseKeySpEL(UPDATED_PRODUCT_RELEASE_KEY_SP_EL)
            .itemLimit(UPDATED_ITEM_LIMIT)
            .overwriteChanges(UPDATED_OVERWRITE_CHANGES)
            .billOfMaterialsCoords(UPDATED_BILL_OF_MATERIALS_COORDS);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createUpdatedEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        source.setPortal(portal);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        source.setProduct(product);
        return source;
    }

    @BeforeEach
    public void initTest() {
        source = createEntity(em);
    }

    @Test
    @Transactional
    void createSource() throws Exception {
        int databaseSizeBeforeCreate = sourceRepository.findAll().size();
        // Create the Source
        restSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isCreated());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeCreate + 1);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSource.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testSource.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSource.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testSource.getFilterArtifactsName()).isEqualTo(DEFAULT_FILTER_ARTIFACTS_NAME);
        assertThat(testSource.getFilterArtifactsCreatedSince()).isEqualTo(DEFAULT_FILTER_ARTIFACTS_CREATED_SINCE);
        assertThat(testSource.getCronExpression()).isEqualTo(DEFAULT_CRON_EXPRESSION);
        assertThat(testSource.getRunOnStartup()).isEqualTo(DEFAULT_RUN_ON_STARTUP);
        assertThat(testSource.getSpecFilterSpEL()).isEqualTo(DEFAULT_SPEC_FILTER_SP_EL);
        assertThat(testSource.getCapabilityKeySpEL()).isEqualTo(DEFAULT_CAPABILITY_KEY_SP_EL);
        assertThat(testSource.getCapabilityNameSpEL()).isEqualTo(DEFAULT_CAPABILITY_NAME_SP_EL);
        assertThat(testSource.getServiceKeySpEL()).isEqualTo(DEFAULT_SERVICE_KEY_SP_EL);
        assertThat(testSource.getServiceNameSpEL()).isEqualTo(DEFAULT_SERVICE_NAME_SP_EL);
        assertThat(testSource.getSpecKeySpEL()).isEqualTo(DEFAULT_SPEC_KEY_SP_EL);
        assertThat(testSource.getVersionSpEL()).isEqualTo(DEFAULT_VERSION_SP_EL);
        assertThat(testSource.getProductReleaseNameSpEL()).isEqualTo(DEFAULT_PRODUCT_RELEASE_NAME_SP_EL);
        assertThat(testSource.getProductReleaseVersionSpEL()).isEqualTo(DEFAULT_PRODUCT_RELEASE_VERSION_SP_EL);
        assertThat(testSource.getProductReleaseKeySpEL()).isEqualTo(DEFAULT_PRODUCT_RELEASE_KEY_SP_EL);
        assertThat(testSource.getItemLimit()).isEqualTo(DEFAULT_ITEM_LIMIT);
        assertThat(testSource.getOverwriteChanges()).isEqualTo(DEFAULT_OVERWRITE_CHANGES);
        assertThat(testSource.getBillOfMaterialsCoords()).isEqualTo(DEFAULT_BILL_OF_MATERIALS_COORDS);
    }

    @Test
    @Transactional
    void createSourceWithExistingId() throws Exception {
        // Create the Source with an existing ID
        source.setId(1L);

        int databaseSizeBeforeCreate = sourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceRepository.findAll().size();
        // set the field null
        source.setName(null);

        // Create the Source, which fails.

        restSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceRepository.findAll().size();
        // set the field null
        source.setKey(null);

        // Create the Source, which fails.

        restSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceRepository.findAll().size();
        // set the field null
        source.setType(null);

        // Create the Source, which fails.

        restSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSources() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList
        restSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(source.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].filterArtifactsName").value(hasItem(DEFAULT_FILTER_ARTIFACTS_NAME)))
            .andExpect(jsonPath("$.[*].filterArtifactsCreatedSince").value(hasItem(DEFAULT_FILTER_ARTIFACTS_CREATED_SINCE.toString())))
            .andExpect(jsonPath("$.[*].cronExpression").value(hasItem(DEFAULT_CRON_EXPRESSION)))
            .andExpect(jsonPath("$.[*].runOnStartup").value(hasItem(DEFAULT_RUN_ON_STARTUP.booleanValue())))
            .andExpect(jsonPath("$.[*].specFilterSpEL").value(hasItem(DEFAULT_SPEC_FILTER_SP_EL)))
            .andExpect(jsonPath("$.[*].capabilityKeySpEL").value(hasItem(DEFAULT_CAPABILITY_KEY_SP_EL)))
            .andExpect(jsonPath("$.[*].capabilityNameSpEL").value(hasItem(DEFAULT_CAPABILITY_NAME_SP_EL)))
            .andExpect(jsonPath("$.[*].serviceKeySpEL").value(hasItem(DEFAULT_SERVICE_KEY_SP_EL)))
            .andExpect(jsonPath("$.[*].serviceNameSpEL").value(hasItem(DEFAULT_SERVICE_NAME_SP_EL)))
            .andExpect(jsonPath("$.[*].specKeySpEL").value(hasItem(DEFAULT_SPEC_KEY_SP_EL)))
            .andExpect(jsonPath("$.[*].versionSpEL").value(hasItem(DEFAULT_VERSION_SP_EL)))
            .andExpect(jsonPath("$.[*].productReleaseNameSpEL").value(hasItem(DEFAULT_PRODUCT_RELEASE_NAME_SP_EL)))
            .andExpect(jsonPath("$.[*].productReleaseVersionSpEL").value(hasItem(DEFAULT_PRODUCT_RELEASE_VERSION_SP_EL)))
            .andExpect(jsonPath("$.[*].productReleaseKeySpEL").value(hasItem(DEFAULT_PRODUCT_RELEASE_KEY_SP_EL)))
            .andExpect(jsonPath("$.[*].itemLimit").value(hasItem(DEFAULT_ITEM_LIMIT)))
            .andExpect(jsonPath("$.[*].overwriteChanges").value(hasItem(DEFAULT_OVERWRITE_CHANGES.booleanValue())))
            .andExpect(jsonPath("$.[*].billOfMaterialsCoords").value(hasItem(DEFAULT_BILL_OF_MATERIALS_COORDS)));
    }

    @Test
    @Transactional
    void getSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get the source
        restSourceMockMvc
            .perform(get(ENTITY_API_URL_ID, source.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(source.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.filterArtifactsName").value(DEFAULT_FILTER_ARTIFACTS_NAME))
            .andExpect(jsonPath("$.filterArtifactsCreatedSince").value(DEFAULT_FILTER_ARTIFACTS_CREATED_SINCE.toString()))
            .andExpect(jsonPath("$.cronExpression").value(DEFAULT_CRON_EXPRESSION))
            .andExpect(jsonPath("$.runOnStartup").value(DEFAULT_RUN_ON_STARTUP.booleanValue()))
            .andExpect(jsonPath("$.specFilterSpEL").value(DEFAULT_SPEC_FILTER_SP_EL))
            .andExpect(jsonPath("$.capabilityKeySpEL").value(DEFAULT_CAPABILITY_KEY_SP_EL))
            .andExpect(jsonPath("$.capabilityNameSpEL").value(DEFAULT_CAPABILITY_NAME_SP_EL))
            .andExpect(jsonPath("$.serviceKeySpEL").value(DEFAULT_SERVICE_KEY_SP_EL))
            .andExpect(jsonPath("$.serviceNameSpEL").value(DEFAULT_SERVICE_NAME_SP_EL))
            .andExpect(jsonPath("$.specKeySpEL").value(DEFAULT_SPEC_KEY_SP_EL))
            .andExpect(jsonPath("$.versionSpEL").value(DEFAULT_VERSION_SP_EL))
            .andExpect(jsonPath("$.productReleaseNameSpEL").value(DEFAULT_PRODUCT_RELEASE_NAME_SP_EL))
            .andExpect(jsonPath("$.productReleaseVersionSpEL").value(DEFAULT_PRODUCT_RELEASE_VERSION_SP_EL))
            .andExpect(jsonPath("$.productReleaseKeySpEL").value(DEFAULT_PRODUCT_RELEASE_KEY_SP_EL))
            .andExpect(jsonPath("$.itemLimit").value(DEFAULT_ITEM_LIMIT))
            .andExpect(jsonPath("$.overwriteChanges").value(DEFAULT_OVERWRITE_CHANGES.booleanValue()))
            .andExpect(jsonPath("$.billOfMaterialsCoords").value(DEFAULT_BILL_OF_MATERIALS_COORDS));
    }

    @Test
    @Transactional
    void getNonExistingSource() throws Exception {
        // Get the source
        restSourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Update the source
        Source updatedSource = sourceRepository.findById(source.getId()).get();
        // Disconnect from session so that the updates on updatedSource are not directly saved in db
        em.detach(updatedSource);
        updatedSource
            .name(UPDATED_NAME)
            .key(UPDATED_KEY)
            .type(UPDATED_TYPE)
            .active(UPDATED_ACTIVE)
            .filterArtifactsName(UPDATED_FILTER_ARTIFACTS_NAME)
            .filterArtifactsCreatedSince(UPDATED_FILTER_ARTIFACTS_CREATED_SINCE)
            .cronExpression(UPDATED_CRON_EXPRESSION)
            .runOnStartup(UPDATED_RUN_ON_STARTUP)
            .specFilterSpEL(UPDATED_SPEC_FILTER_SP_EL)
            .capabilityKeySpEL(UPDATED_CAPABILITY_KEY_SP_EL)
            .capabilityNameSpEL(UPDATED_CAPABILITY_NAME_SP_EL)
            .serviceKeySpEL(UPDATED_SERVICE_KEY_SP_EL)
            .serviceNameSpEL(UPDATED_SERVICE_NAME_SP_EL)
            .specKeySpEL(UPDATED_SPEC_KEY_SP_EL)
            .versionSpEL(UPDATED_VERSION_SP_EL)
            .productReleaseNameSpEL(UPDATED_PRODUCT_RELEASE_NAME_SP_EL)
            .productReleaseVersionSpEL(UPDATED_PRODUCT_RELEASE_VERSION_SP_EL)
            .productReleaseKeySpEL(UPDATED_PRODUCT_RELEASE_KEY_SP_EL)
            .itemLimit(UPDATED_ITEM_LIMIT)
            .overwriteChanges(UPDATED_OVERWRITE_CHANGES)
            .billOfMaterialsCoords(UPDATED_BILL_OF_MATERIALS_COORDS);

        restSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSource))
            )
            .andExpect(status().isOk());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSource.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSource.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSource.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testSource.getFilterArtifactsName()).isEqualTo(UPDATED_FILTER_ARTIFACTS_NAME);
        assertThat(testSource.getFilterArtifactsCreatedSince()).isEqualTo(UPDATED_FILTER_ARTIFACTS_CREATED_SINCE);
        assertThat(testSource.getCronExpression()).isEqualTo(UPDATED_CRON_EXPRESSION);
        assertThat(testSource.getRunOnStartup()).isEqualTo(UPDATED_RUN_ON_STARTUP);
        assertThat(testSource.getSpecFilterSpEL()).isEqualTo(UPDATED_SPEC_FILTER_SP_EL);
        assertThat(testSource.getCapabilityKeySpEL()).isEqualTo(UPDATED_CAPABILITY_KEY_SP_EL);
        assertThat(testSource.getCapabilityNameSpEL()).isEqualTo(UPDATED_CAPABILITY_NAME_SP_EL);
        assertThat(testSource.getServiceKeySpEL()).isEqualTo(UPDATED_SERVICE_KEY_SP_EL);
        assertThat(testSource.getServiceNameSpEL()).isEqualTo(UPDATED_SERVICE_NAME_SP_EL);
        assertThat(testSource.getSpecKeySpEL()).isEqualTo(UPDATED_SPEC_KEY_SP_EL);
        assertThat(testSource.getVersionSpEL()).isEqualTo(UPDATED_VERSION_SP_EL);
        assertThat(testSource.getProductReleaseNameSpEL()).isEqualTo(UPDATED_PRODUCT_RELEASE_NAME_SP_EL);
        assertThat(testSource.getProductReleaseVersionSpEL()).isEqualTo(UPDATED_PRODUCT_RELEASE_VERSION_SP_EL);
        assertThat(testSource.getProductReleaseKeySpEL()).isEqualTo(UPDATED_PRODUCT_RELEASE_KEY_SP_EL);
        assertThat(testSource.getItemLimit()).isEqualTo(UPDATED_ITEM_LIMIT);
        assertThat(testSource.getOverwriteChanges()).isEqualTo(UPDATED_OVERWRITE_CHANGES);
        assertThat(testSource.getBillOfMaterialsCoords()).isEqualTo(UPDATED_BILL_OF_MATERIALS_COORDS);
    }

    @Test
    @Transactional
    void putNonExistingSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();
        source.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, source.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(source))
            )
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();
        source.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(source))
            )
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();
        source.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSourceWithPatch() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Update the source using partial update
        Source partialUpdatedSource = new Source();
        partialUpdatedSource.setId(source.getId());

        partialUpdatedSource
            .key(UPDATED_KEY)
            .type(UPDATED_TYPE)
            .active(UPDATED_ACTIVE)
            .cronExpression(UPDATED_CRON_EXPRESSION)
            .specFilterSpEL(UPDATED_SPEC_FILTER_SP_EL)
            .serviceKeySpEL(UPDATED_SERVICE_KEY_SP_EL)
            .serviceNameSpEL(UPDATED_SERVICE_NAME_SP_EL)
            .versionSpEL(UPDATED_VERSION_SP_EL)
            .productReleaseKeySpEL(UPDATED_PRODUCT_RELEASE_KEY_SP_EL)
            .itemLimit(UPDATED_ITEM_LIMIT)
            .billOfMaterialsCoords(UPDATED_BILL_OF_MATERIALS_COORDS);

        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSource))
            )
            .andExpect(status().isOk());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSource.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSource.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSource.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testSource.getFilterArtifactsName()).isEqualTo(DEFAULT_FILTER_ARTIFACTS_NAME);
        assertThat(testSource.getFilterArtifactsCreatedSince()).isEqualTo(DEFAULT_FILTER_ARTIFACTS_CREATED_SINCE);
        assertThat(testSource.getCronExpression()).isEqualTo(UPDATED_CRON_EXPRESSION);
        assertThat(testSource.getRunOnStartup()).isEqualTo(DEFAULT_RUN_ON_STARTUP);
        assertThat(testSource.getSpecFilterSpEL()).isEqualTo(UPDATED_SPEC_FILTER_SP_EL);
        assertThat(testSource.getCapabilityKeySpEL()).isEqualTo(DEFAULT_CAPABILITY_KEY_SP_EL);
        assertThat(testSource.getCapabilityNameSpEL()).isEqualTo(DEFAULT_CAPABILITY_NAME_SP_EL);
        assertThat(testSource.getServiceKeySpEL()).isEqualTo(UPDATED_SERVICE_KEY_SP_EL);
        assertThat(testSource.getServiceNameSpEL()).isEqualTo(UPDATED_SERVICE_NAME_SP_EL);
        assertThat(testSource.getSpecKeySpEL()).isEqualTo(DEFAULT_SPEC_KEY_SP_EL);
        assertThat(testSource.getVersionSpEL()).isEqualTo(UPDATED_VERSION_SP_EL);
        assertThat(testSource.getProductReleaseNameSpEL()).isEqualTo(DEFAULT_PRODUCT_RELEASE_NAME_SP_EL);
        assertThat(testSource.getProductReleaseVersionSpEL()).isEqualTo(DEFAULT_PRODUCT_RELEASE_VERSION_SP_EL);
        assertThat(testSource.getProductReleaseKeySpEL()).isEqualTo(UPDATED_PRODUCT_RELEASE_KEY_SP_EL);
        assertThat(testSource.getItemLimit()).isEqualTo(UPDATED_ITEM_LIMIT);
        assertThat(testSource.getOverwriteChanges()).isEqualTo(DEFAULT_OVERWRITE_CHANGES);
        assertThat(testSource.getBillOfMaterialsCoords()).isEqualTo(UPDATED_BILL_OF_MATERIALS_COORDS);
    }

    @Test
    @Transactional
    void fullUpdateSourceWithPatch() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Update the source using partial update
        Source partialUpdatedSource = new Source();
        partialUpdatedSource.setId(source.getId());

        partialUpdatedSource
            .name(UPDATED_NAME)
            .key(UPDATED_KEY)
            .type(UPDATED_TYPE)
            .active(UPDATED_ACTIVE)
            .filterArtifactsName(UPDATED_FILTER_ARTIFACTS_NAME)
            .filterArtifactsCreatedSince(UPDATED_FILTER_ARTIFACTS_CREATED_SINCE)
            .cronExpression(UPDATED_CRON_EXPRESSION)
            .runOnStartup(UPDATED_RUN_ON_STARTUP)
            .specFilterSpEL(UPDATED_SPEC_FILTER_SP_EL)
            .capabilityKeySpEL(UPDATED_CAPABILITY_KEY_SP_EL)
            .capabilityNameSpEL(UPDATED_CAPABILITY_NAME_SP_EL)
            .serviceKeySpEL(UPDATED_SERVICE_KEY_SP_EL)
            .serviceNameSpEL(UPDATED_SERVICE_NAME_SP_EL)
            .specKeySpEL(UPDATED_SPEC_KEY_SP_EL)
            .versionSpEL(UPDATED_VERSION_SP_EL)
            .productReleaseNameSpEL(UPDATED_PRODUCT_RELEASE_NAME_SP_EL)
            .productReleaseVersionSpEL(UPDATED_PRODUCT_RELEASE_VERSION_SP_EL)
            .productReleaseKeySpEL(UPDATED_PRODUCT_RELEASE_KEY_SP_EL)
            .itemLimit(UPDATED_ITEM_LIMIT)
            .overwriteChanges(UPDATED_OVERWRITE_CHANGES)
            .billOfMaterialsCoords(UPDATED_BILL_OF_MATERIALS_COORDS);

        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSource))
            )
            .andExpect(status().isOk());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSource.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSource.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSource.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testSource.getFilterArtifactsName()).isEqualTo(UPDATED_FILTER_ARTIFACTS_NAME);
        assertThat(testSource.getFilterArtifactsCreatedSince()).isEqualTo(UPDATED_FILTER_ARTIFACTS_CREATED_SINCE);
        assertThat(testSource.getCronExpression()).isEqualTo(UPDATED_CRON_EXPRESSION);
        assertThat(testSource.getRunOnStartup()).isEqualTo(UPDATED_RUN_ON_STARTUP);
        assertThat(testSource.getSpecFilterSpEL()).isEqualTo(UPDATED_SPEC_FILTER_SP_EL);
        assertThat(testSource.getCapabilityKeySpEL()).isEqualTo(UPDATED_CAPABILITY_KEY_SP_EL);
        assertThat(testSource.getCapabilityNameSpEL()).isEqualTo(UPDATED_CAPABILITY_NAME_SP_EL);
        assertThat(testSource.getServiceKeySpEL()).isEqualTo(UPDATED_SERVICE_KEY_SP_EL);
        assertThat(testSource.getServiceNameSpEL()).isEqualTo(UPDATED_SERVICE_NAME_SP_EL);
        assertThat(testSource.getSpecKeySpEL()).isEqualTo(UPDATED_SPEC_KEY_SP_EL);
        assertThat(testSource.getVersionSpEL()).isEqualTo(UPDATED_VERSION_SP_EL);
        assertThat(testSource.getProductReleaseNameSpEL()).isEqualTo(UPDATED_PRODUCT_RELEASE_NAME_SP_EL);
        assertThat(testSource.getProductReleaseVersionSpEL()).isEqualTo(UPDATED_PRODUCT_RELEASE_VERSION_SP_EL);
        assertThat(testSource.getProductReleaseKeySpEL()).isEqualTo(UPDATED_PRODUCT_RELEASE_KEY_SP_EL);
        assertThat(testSource.getItemLimit()).isEqualTo(UPDATED_ITEM_LIMIT);
        assertThat(testSource.getOverwriteChanges()).isEqualTo(UPDATED_OVERWRITE_CHANGES);
        assertThat(testSource.getBillOfMaterialsCoords()).isEqualTo(UPDATED_BILL_OF_MATERIALS_COORDS);
    }

    @Test
    @Transactional
    void patchNonExistingSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();
        source.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, source.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(source))
            )
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();
        source.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(source))
            )
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();
        source.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeDelete = sourceRepository.findAll().size();

        // Delete the source
        restSourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, source.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
