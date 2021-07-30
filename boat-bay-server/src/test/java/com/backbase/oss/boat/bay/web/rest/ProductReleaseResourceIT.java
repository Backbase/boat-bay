package com.backbase.oss.boat.bay.web.rest;

import static com.backbase.oss.boat.bay.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backbase.oss.boat.bay.IntegrationTest;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.repository.ProductReleaseRepository;
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

/**
 * Integration tests for the {@link ProductReleaseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductReleaseResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_RELEASE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RELEASE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_HIDE = false;
    private static final Boolean UPDATED_HIDE = true;

    private static final String ENTITY_API_URL = "/api/product-releases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductReleaseRepository productReleaseRepository;

    @Mock
    private ProductReleaseRepository productReleaseRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductReleaseMockMvc;

    private ProductRelease productRelease;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductRelease createEntity(EntityManager em) {
        ProductRelease productRelease = new ProductRelease()
            .key(DEFAULT_KEY)
            .name(DEFAULT_NAME)
            .version(DEFAULT_VERSION)
            .releaseDate(DEFAULT_RELEASE_DATE)
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
        productRelease.setProduct(product);
        return productRelease;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductRelease createUpdatedEntity(EntityManager em) {
        ProductRelease productRelease = new ProductRelease()
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .version(UPDATED_VERSION)
            .releaseDate(UPDATED_RELEASE_DATE)
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
        productRelease.setProduct(product);
        return productRelease;
    }

    @BeforeEach
    public void initTest() {
        productRelease = createEntity(em);
    }

    @Test
    @Transactional
    void createProductRelease() throws Exception {
        int databaseSizeBeforeCreate = productReleaseRepository.findAll().size();
        // Create the ProductRelease
        restProductReleaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productRelease))
            )
            .andExpect(status().isCreated());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeCreate + 1);
        ProductRelease testProductRelease = productReleaseList.get(productReleaseList.size() - 1);
        assertThat(testProductRelease.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testProductRelease.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProductRelease.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testProductRelease.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testProductRelease.getHide()).isEqualTo(DEFAULT_HIDE);
    }

    @Test
    @Transactional
    void createProductReleaseWithExistingId() throws Exception {
        // Create the ProductRelease with an existing ID
        productRelease.setId(1L);

        int databaseSizeBeforeCreate = productReleaseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductReleaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productRelease))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = productReleaseRepository.findAll().size();
        // set the field null
        productRelease.setKey(null);

        // Create the ProductRelease, which fails.

        restProductReleaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productRelease))
            )
            .andExpect(status().isBadRequest());

        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productReleaseRepository.findAll().size();
        // set the field null
        productRelease.setName(null);

        // Create the ProductRelease, which fails.

        restProductReleaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productRelease))
            )
            .andExpect(status().isBadRequest());

        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productReleaseRepository.findAll().size();
        // set the field null
        productRelease.setVersion(null);

        // Create the ProductRelease, which fails.

        restProductReleaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productRelease))
            )
            .andExpect(status().isBadRequest());

        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductReleases() throws Exception {
        // Initialize the database
        productReleaseRepository.saveAndFlush(productRelease);

        // Get all the productReleaseList
        restProductReleaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productRelease.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(sameInstant(DEFAULT_RELEASE_DATE))))
            .andExpect(jsonPath("$.[*].hide").value(hasItem(DEFAULT_HIDE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductReleasesWithEagerRelationshipsIsEnabled() throws Exception {
        when(productReleaseRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductReleaseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productReleaseRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductReleasesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productReleaseRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductReleaseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productReleaseRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProductRelease() throws Exception {
        // Initialize the database
        productReleaseRepository.saveAndFlush(productRelease);

        // Get the productRelease
        restProductReleaseMockMvc
            .perform(get(ENTITY_API_URL_ID, productRelease.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productRelease.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.releaseDate").value(sameInstant(DEFAULT_RELEASE_DATE)))
            .andExpect(jsonPath("$.hide").value(DEFAULT_HIDE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingProductRelease() throws Exception {
        // Get the productRelease
        restProductReleaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductRelease() throws Exception {
        // Initialize the database
        productReleaseRepository.saveAndFlush(productRelease);

        int databaseSizeBeforeUpdate = productReleaseRepository.findAll().size();

        // Update the productRelease
        ProductRelease updatedProductRelease = productReleaseRepository.findById(productRelease.getId()).get();
        // Disconnect from session so that the updates on updatedProductRelease are not directly saved in db
        em.detach(updatedProductRelease);
        updatedProductRelease
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .version(UPDATED_VERSION)
            .releaseDate(UPDATED_RELEASE_DATE)
            .hide(UPDATED_HIDE);

        restProductReleaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductRelease.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductRelease))
            )
            .andExpect(status().isOk());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeUpdate);
        ProductRelease testProductRelease = productReleaseList.get(productReleaseList.size() - 1);
        assertThat(testProductRelease.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testProductRelease.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductRelease.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testProductRelease.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testProductRelease.getHide()).isEqualTo(UPDATED_HIDE);
    }

    @Test
    @Transactional
    void putNonExistingProductRelease() throws Exception {
        int databaseSizeBeforeUpdate = productReleaseRepository.findAll().size();
        productRelease.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReleaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productRelease.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productRelease))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductRelease() throws Exception {
        int databaseSizeBeforeUpdate = productReleaseRepository.findAll().size();
        productRelease.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReleaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productRelease))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductRelease() throws Exception {
        int databaseSizeBeforeUpdate = productReleaseRepository.findAll().size();
        productRelease.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReleaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productRelease)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductReleaseWithPatch() throws Exception {
        // Initialize the database
        productReleaseRepository.saveAndFlush(productRelease);

        int databaseSizeBeforeUpdate = productReleaseRepository.findAll().size();

        // Update the productRelease using partial update
        ProductRelease partialUpdatedProductRelease = new ProductRelease();
        partialUpdatedProductRelease.setId(productRelease.getId());

        partialUpdatedProductRelease.name(UPDATED_NAME).version(UPDATED_VERSION).hide(UPDATED_HIDE);

        restProductReleaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductRelease.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductRelease))
            )
            .andExpect(status().isOk());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeUpdate);
        ProductRelease testProductRelease = productReleaseList.get(productReleaseList.size() - 1);
        assertThat(testProductRelease.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testProductRelease.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductRelease.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testProductRelease.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testProductRelease.getHide()).isEqualTo(UPDATED_HIDE);
    }

    @Test
    @Transactional
    void fullUpdateProductReleaseWithPatch() throws Exception {
        // Initialize the database
        productReleaseRepository.saveAndFlush(productRelease);

        int databaseSizeBeforeUpdate = productReleaseRepository.findAll().size();

        // Update the productRelease using partial update
        ProductRelease partialUpdatedProductRelease = new ProductRelease();
        partialUpdatedProductRelease.setId(productRelease.getId());

        partialUpdatedProductRelease
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .version(UPDATED_VERSION)
            .releaseDate(UPDATED_RELEASE_DATE)
            .hide(UPDATED_HIDE);

        restProductReleaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductRelease.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductRelease))
            )
            .andExpect(status().isOk());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeUpdate);
        ProductRelease testProductRelease = productReleaseList.get(productReleaseList.size() - 1);
        assertThat(testProductRelease.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testProductRelease.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductRelease.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testProductRelease.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testProductRelease.getHide()).isEqualTo(UPDATED_HIDE);
    }

    @Test
    @Transactional
    void patchNonExistingProductRelease() throws Exception {
        int databaseSizeBeforeUpdate = productReleaseRepository.findAll().size();
        productRelease.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReleaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productRelease.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productRelease))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductRelease() throws Exception {
        int databaseSizeBeforeUpdate = productReleaseRepository.findAll().size();
        productRelease.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReleaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productRelease))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductRelease() throws Exception {
        int databaseSizeBeforeUpdate = productReleaseRepository.findAll().size();
        productRelease.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReleaseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productRelease))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductRelease() throws Exception {
        // Initialize the database
        productReleaseRepository.saveAndFlush(productRelease);

        int databaseSizeBeforeDelete = productReleaseRepository.findAll().size();

        // Delete the productRelease
        restProductReleaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, productRelease.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
