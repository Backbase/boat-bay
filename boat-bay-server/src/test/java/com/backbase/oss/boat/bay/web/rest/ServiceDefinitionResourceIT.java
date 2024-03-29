package com.backbase.oss.boat.bay.web.rest;

import static com.backbase.oss.boat.bay.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backbase.oss.boat.bay.IntegrationTest;
import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.repository.ServiceDefinitionRepository;
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
 * Integration tests for the {@link ServiceDefinitionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceDefinitionResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HIDE = false;
    private static final Boolean UPDATED_HIDE = true;

    private static final String ENTITY_API_URL = "/api/service-definitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServiceDefinitionRepository serviceDefinitionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceDefinitionMockMvc;

    private ServiceDefinition serviceDefinition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceDefinition createEntity(EntityManager em) {
        ServiceDefinition serviceDefinition = new ServiceDefinition()
            .key(DEFAULT_KEY)
            .name(DEFAULT_NAME)
            .order(DEFAULT_ORDER)
            .subTitle(DEFAULT_SUB_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .icon(DEFAULT_ICON)
            .color(DEFAULT_COLOR)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .hide(DEFAULT_HIDE);
        // Add required entity
        Capability capability;
        if (TestUtil.findAll(em, Capability.class).isEmpty()) {
            capability = CapabilityResourceIT.createEntity(em);
            em.persist(capability);
            em.flush();
        } else {
            capability = TestUtil.findAll(em, Capability.class).get(0);
        }
        serviceDefinition.setCapability(capability);
        return serviceDefinition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceDefinition createUpdatedEntity(EntityManager em) {
        ServiceDefinition serviceDefinition = new ServiceDefinition()
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .order(UPDATED_ORDER)
            .subTitle(UPDATED_SUB_TITLE)
            .description(UPDATED_DESCRIPTION)
            .icon(UPDATED_ICON)
            .color(UPDATED_COLOR)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .hide(UPDATED_HIDE);
        // Add required entity
        Capability capability;
        if (TestUtil.findAll(em, Capability.class).isEmpty()) {
            capability = CapabilityResourceIT.createUpdatedEntity(em);
            em.persist(capability);
            em.flush();
        } else {
            capability = TestUtil.findAll(em, Capability.class).get(0);
        }
        serviceDefinition.setCapability(capability);
        return serviceDefinition;
    }

    @BeforeEach
    public void initTest() {
        serviceDefinition = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceDefinition() throws Exception {
        int databaseSizeBeforeCreate = serviceDefinitionRepository.findAll().size();
        // Create the ServiceDefinition
        restServiceDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceDefinition))
            )
            .andExpect(status().isCreated());

        // Validate the ServiceDefinition in the database
        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceDefinition testServiceDefinition = serviceDefinitionList.get(serviceDefinitionList.size() - 1);
        assertThat(testServiceDefinition.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testServiceDefinition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testServiceDefinition.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testServiceDefinition.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testServiceDefinition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testServiceDefinition.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testServiceDefinition.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testServiceDefinition.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testServiceDefinition.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testServiceDefinition.getHide()).isEqualTo(DEFAULT_HIDE);
    }

    @Test
    @Transactional
    void createServiceDefinitionWithExistingId() throws Exception {
        // Create the ServiceDefinition with an existing ID
        serviceDefinition.setId(1L);

        int databaseSizeBeforeCreate = serviceDefinitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceDefinition in the database
        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceDefinitionRepository.findAll().size();
        // set the field null
        serviceDefinition.setKey(null);

        // Create the ServiceDefinition, which fails.

        restServiceDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceDefinition))
            )
            .andExpect(status().isBadRequest());

        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceDefinitionRepository.findAll().size();
        // set the field null
        serviceDefinition.setName(null);

        // Create the ServiceDefinition, which fails.

        restServiceDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceDefinition))
            )
            .andExpect(status().isBadRequest());

        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceDefinitions() throws Exception {
        // Initialize the database
        serviceDefinitionRepository.saveAndFlush(serviceDefinition);

        // Get all the serviceDefinitionList
        restServiceDefinitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceDefinition.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(sameInstant(DEFAULT_CREATED_ON))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].hide").value(hasItem(DEFAULT_HIDE.booleanValue())));
    }

    @Test
    @Transactional
    void getServiceDefinition() throws Exception {
        // Initialize the database
        serviceDefinitionRepository.saveAndFlush(serviceDefinition);

        // Get the serviceDefinition
        restServiceDefinitionMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceDefinition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceDefinition.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.createdOn").value(sameInstant(DEFAULT_CREATED_ON)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.hide").value(DEFAULT_HIDE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingServiceDefinition() throws Exception {
        // Get the serviceDefinition
        restServiceDefinitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewServiceDefinition() throws Exception {
        // Initialize the database
        serviceDefinitionRepository.saveAndFlush(serviceDefinition);

        int databaseSizeBeforeUpdate = serviceDefinitionRepository.findAll().size();

        // Update the serviceDefinition
        ServiceDefinition updatedServiceDefinition = serviceDefinitionRepository.findById(serviceDefinition.getId()).get();
        // Disconnect from session so that the updates on updatedServiceDefinition are not directly saved in db
        em.detach(updatedServiceDefinition);
        updatedServiceDefinition
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .order(UPDATED_ORDER)
            .subTitle(UPDATED_SUB_TITLE)
            .description(UPDATED_DESCRIPTION)
            .icon(UPDATED_ICON)
            .color(UPDATED_COLOR)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .hide(UPDATED_HIDE);

        restServiceDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceDefinition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedServiceDefinition))
            )
            .andExpect(status().isOk());

        // Validate the ServiceDefinition in the database
        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeUpdate);
        ServiceDefinition testServiceDefinition = serviceDefinitionList.get(serviceDefinitionList.size() - 1);
        assertThat(testServiceDefinition.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testServiceDefinition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServiceDefinition.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testServiceDefinition.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testServiceDefinition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testServiceDefinition.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testServiceDefinition.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testServiceDefinition.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testServiceDefinition.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testServiceDefinition.getHide()).isEqualTo(UPDATED_HIDE);
    }

    @Test
    @Transactional
    void putNonExistingServiceDefinition() throws Exception {
        int databaseSizeBeforeUpdate = serviceDefinitionRepository.findAll().size();
        serviceDefinition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceDefinition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceDefinition in the database
        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceDefinition() throws Exception {
        int databaseSizeBeforeUpdate = serviceDefinitionRepository.findAll().size();
        serviceDefinition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceDefinition in the database
        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceDefinition() throws Exception {
        int databaseSizeBeforeUpdate = serviceDefinitionRepository.findAll().size();
        serviceDefinition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceDefinition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceDefinition in the database
        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceDefinitionWithPatch() throws Exception {
        // Initialize the database
        serviceDefinitionRepository.saveAndFlush(serviceDefinition);

        int databaseSizeBeforeUpdate = serviceDefinitionRepository.findAll().size();

        // Update the serviceDefinition using partial update
        ServiceDefinition partialUpdatedServiceDefinition = new ServiceDefinition();
        partialUpdatedServiceDefinition.setId(serviceDefinition.getId());

        partialUpdatedServiceDefinition
            .order(UPDATED_ORDER)
            .description(UPDATED_DESCRIPTION)
            .icon(UPDATED_ICON)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .hide(UPDATED_HIDE);

        restServiceDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceDefinition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceDefinition))
            )
            .andExpect(status().isOk());

        // Validate the ServiceDefinition in the database
        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeUpdate);
        ServiceDefinition testServiceDefinition = serviceDefinitionList.get(serviceDefinitionList.size() - 1);
        assertThat(testServiceDefinition.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testServiceDefinition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testServiceDefinition.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testServiceDefinition.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testServiceDefinition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testServiceDefinition.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testServiceDefinition.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testServiceDefinition.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testServiceDefinition.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testServiceDefinition.getHide()).isEqualTo(UPDATED_HIDE);
    }

    @Test
    @Transactional
    void fullUpdateServiceDefinitionWithPatch() throws Exception {
        // Initialize the database
        serviceDefinitionRepository.saveAndFlush(serviceDefinition);

        int databaseSizeBeforeUpdate = serviceDefinitionRepository.findAll().size();

        // Update the serviceDefinition using partial update
        ServiceDefinition partialUpdatedServiceDefinition = new ServiceDefinition();
        partialUpdatedServiceDefinition.setId(serviceDefinition.getId());

        partialUpdatedServiceDefinition
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .order(UPDATED_ORDER)
            .subTitle(UPDATED_SUB_TITLE)
            .description(UPDATED_DESCRIPTION)
            .icon(UPDATED_ICON)
            .color(UPDATED_COLOR)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .hide(UPDATED_HIDE);

        restServiceDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceDefinition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceDefinition))
            )
            .andExpect(status().isOk());

        // Validate the ServiceDefinition in the database
        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeUpdate);
        ServiceDefinition testServiceDefinition = serviceDefinitionList.get(serviceDefinitionList.size() - 1);
        assertThat(testServiceDefinition.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testServiceDefinition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServiceDefinition.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testServiceDefinition.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testServiceDefinition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testServiceDefinition.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testServiceDefinition.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testServiceDefinition.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testServiceDefinition.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testServiceDefinition.getHide()).isEqualTo(UPDATED_HIDE);
    }

    @Test
    @Transactional
    void patchNonExistingServiceDefinition() throws Exception {
        int databaseSizeBeforeUpdate = serviceDefinitionRepository.findAll().size();
        serviceDefinition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceDefinition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceDefinition in the database
        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceDefinition() throws Exception {
        int databaseSizeBeforeUpdate = serviceDefinitionRepository.findAll().size();
        serviceDefinition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceDefinition in the database
        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceDefinition() throws Exception {
        int databaseSizeBeforeUpdate = serviceDefinitionRepository.findAll().size();
        serviceDefinition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceDefinition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceDefinition in the database
        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceDefinition() throws Exception {
        // Initialize the database
        serviceDefinitionRepository.saveAndFlush(serviceDefinition);

        int databaseSizeBeforeDelete = serviceDefinitionRepository.findAll().size();

        // Delete the serviceDefinition
        restServiceDefinitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceDefinition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceDefinition> serviceDefinitionList = serviceDefinitionRepository.findAll();
        assertThat(serviceDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
