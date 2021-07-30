package com.backbase.oss.boat.bay.web.rest;

import static com.backbase.oss.boat.bay.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backbase.oss.boat.bay.IntegrationTest;
import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link CapabilityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CapabilityResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HIDE = false;
    private static final Boolean UPDATED_HIDE = true;

    private static final String ENTITY_API_URL = "/api/capabilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CapabilityRepository capabilityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCapabilityMockMvc;

    private Capability capability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Capability createEntity(EntityManager em) {
        Capability capability = new Capability()
            .key(DEFAULT_KEY)
            .name(DEFAULT_NAME)
            .order(DEFAULT_ORDER)
            .subTitle(DEFAULT_SUB_TITLE)
            .content(DEFAULT_CONTENT)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .hide(DEFAULT_HIDE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        capability.setProduct(product);
        return capability;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Capability createUpdatedEntity(EntityManager em) {
        Capability capability = new Capability()
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .order(UPDATED_ORDER)
            .subTitle(UPDATED_SUB_TITLE)
            .content(UPDATED_CONTENT)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .hide(UPDATED_HIDE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        capability.setProduct(product);
        return capability;
    }

    @BeforeEach
    public void initTest() {
        capability = createEntity(em);
    }

    @Test
    @Transactional
    void createCapability() throws Exception {
        int databaseSizeBeforeCreate = capabilityRepository.findAll().size();
        // Create the Capability
        restCapabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isCreated());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeCreate + 1);
        Capability testCapability = capabilityList.get(capabilityList.size() - 1);
        assertThat(testCapability.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testCapability.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCapability.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testCapability.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testCapability.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testCapability.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testCapability.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCapability.getHide()).isEqualTo(DEFAULT_HIDE);
    }

    @Test
    @Transactional
    void createCapabilityWithExistingId() throws Exception {
        // Create the Capability with an existing ID
        capability.setId(1L);

        int databaseSizeBeforeCreate = capabilityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCapabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isBadRequest());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = capabilityRepository.findAll().size();
        // set the field null
        capability.setKey(null);

        // Create the Capability, which fails.

        restCapabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isBadRequest());

        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = capabilityRepository.findAll().size();
        // set the field null
        capability.setName(null);

        // Create the Capability, which fails.

        restCapabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isBadRequest());

        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCapabilities() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        // Get all the capabilityList
        restCapabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(capability.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(sameInstant(DEFAULT_CREATED_ON))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].hide").value(hasItem(DEFAULT_HIDE.booleanValue())));
    }

    @Test
    @Transactional
    void getCapability() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        // Get the capability
        restCapabilityMockMvc
            .perform(get(ENTITY_API_URL_ID, capability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(capability.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createdOn").value(sameInstant(DEFAULT_CREATED_ON)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.hide").value(DEFAULT_HIDE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCapability() throws Exception {
        // Get the capability
        restCapabilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCapability() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();

        // Update the capability
        Capability updatedCapability = capabilityRepository.findById(capability.getId()).get();
        // Disconnect from session so that the updates on updatedCapability are not directly saved in db
        em.detach(updatedCapability);
        updatedCapability
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .order(UPDATED_ORDER)
            .subTitle(UPDATED_SUB_TITLE)
            .content(UPDATED_CONTENT)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .hide(UPDATED_HIDE);

        restCapabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCapability.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCapability))
            )
            .andExpect(status().isOk());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
        Capability testCapability = capabilityList.get(capabilityList.size() - 1);
        assertThat(testCapability.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCapability.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCapability.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testCapability.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testCapability.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testCapability.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testCapability.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCapability.getHide()).isEqualTo(UPDATED_HIDE);
    }

    @Test
    @Transactional
    void putNonExistingCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();
        capability.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, capability.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();
        capability.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();
        capability.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapabilityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCapabilityWithPatch() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();

        // Update the capability using partial update
        Capability partialUpdatedCapability = new Capability();
        partialUpdatedCapability.setId(capability.getId());

        partialUpdatedCapability.key(UPDATED_KEY).createdOn(UPDATED_CREATED_ON).hide(UPDATED_HIDE);

        restCapabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCapability))
            )
            .andExpect(status().isOk());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
        Capability testCapability = capabilityList.get(capabilityList.size() - 1);
        assertThat(testCapability.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCapability.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCapability.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testCapability.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testCapability.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testCapability.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testCapability.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCapability.getHide()).isEqualTo(UPDATED_HIDE);
    }

    @Test
    @Transactional
    void fullUpdateCapabilityWithPatch() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();

        // Update the capability using partial update
        Capability partialUpdatedCapability = new Capability();
        partialUpdatedCapability.setId(capability.getId());

        partialUpdatedCapability
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .order(UPDATED_ORDER)
            .subTitle(UPDATED_SUB_TITLE)
            .content(UPDATED_CONTENT)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .hide(UPDATED_HIDE);

        restCapabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCapability))
            )
            .andExpect(status().isOk());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
        Capability testCapability = capabilityList.get(capabilityList.size() - 1);
        assertThat(testCapability.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCapability.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCapability.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testCapability.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testCapability.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testCapability.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testCapability.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCapability.getHide()).isEqualTo(UPDATED_HIDE);
    }

    @Test
    @Transactional
    void patchNonExistingCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();
        capability.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, capability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();
        capability.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();
        capability.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapabilityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(capability))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCapability() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        int databaseSizeBeforeDelete = capabilityRepository.findAll().size();

        // Delete the capability
        restCapabilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, capability.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
