package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.ProductReleaseRepository;

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
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProductReleaseResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProductReleaseResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

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
            .name(DEFAULT_NAME);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        productRelease.setPortal(portal);
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
            .name(UPDATED_NAME);
        // Add required entity
        Portal portal;
        if (TestUtil.findAll(em, Portal.class).isEmpty()) {
            portal = PortalResourceIT.createUpdatedEntity(em);
            em.persist(portal);
            em.flush();
        } else {
            portal = TestUtil.findAll(em, Portal.class).get(0);
        }
        productRelease.setPortal(portal);
        return productRelease;
    }

    @BeforeEach
    public void initTest() {
        productRelease = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductRelease() throws Exception {
        int databaseSizeBeforeCreate = productReleaseRepository.findAll().size();
        // Create the ProductRelease
        restProductReleaseMockMvc.perform(post("/api/product-releases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productRelease)))
            .andExpect(status().isCreated());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeCreate + 1);
        ProductRelease testProductRelease = productReleaseList.get(productReleaseList.size() - 1);
        assertThat(testProductRelease.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testProductRelease.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createProductReleaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productReleaseRepository.findAll().size();

        // Create the ProductRelease with an existing ID
        productRelease.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductReleaseMockMvc.perform(post("/api/product-releases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productRelease)))
            .andExpect(status().isBadRequest());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = productReleaseRepository.findAll().size();
        // set the field null
        productRelease.setKey(null);

        // Create the ProductRelease, which fails.


        restProductReleaseMockMvc.perform(post("/api/product-releases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productRelease)))
            .andExpect(status().isBadRequest());

        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productReleaseRepository.findAll().size();
        // set the field null
        productRelease.setName(null);

        // Create the ProductRelease, which fails.


        restProductReleaseMockMvc.perform(post("/api/product-releases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productRelease)))
            .andExpect(status().isBadRequest());

        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductReleases() throws Exception {
        // Initialize the database
        productReleaseRepository.saveAndFlush(productRelease);

        // Get all the productReleaseList
        restProductReleaseMockMvc.perform(get("/api/product-releases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productRelease.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllProductReleasesWithEagerRelationshipsIsEnabled() throws Exception {
        when(productReleaseRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductReleaseMockMvc.perform(get("/api/product-releases?eagerload=true"))
            .andExpect(status().isOk());

        verify(productReleaseRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllProductReleasesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productReleaseRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductReleaseMockMvc.perform(get("/api/product-releases?eagerload=true"))
            .andExpect(status().isOk());

        verify(productReleaseRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getProductRelease() throws Exception {
        // Initialize the database
        productReleaseRepository.saveAndFlush(productRelease);

        // Get the productRelease
        restProductReleaseMockMvc.perform(get("/api/product-releases/{id}", productRelease.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productRelease.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingProductRelease() throws Exception {
        // Get the productRelease
        restProductReleaseMockMvc.perform(get("/api/product-releases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductRelease() throws Exception {
        // Initialize the database
        productReleaseRepository.saveAndFlush(productRelease);

        int databaseSizeBeforeUpdate = productReleaseRepository.findAll().size();

        // Update the productRelease
        ProductRelease updatedProductRelease = productReleaseRepository.findById(productRelease.getId()).get();
        // Disconnect from session so that the updates on updatedProductRelease are not directly saved in db
        em.detach(updatedProductRelease);
        updatedProductRelease
            .key(UPDATED_KEY)
            .name(UPDATED_NAME);

        restProductReleaseMockMvc.perform(put("/api/product-releases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductRelease)))
            .andExpect(status().isOk());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeUpdate);
        ProductRelease testProductRelease = productReleaseList.get(productReleaseList.size() - 1);
        assertThat(testProductRelease.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testProductRelease.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingProductRelease() throws Exception {
        int databaseSizeBeforeUpdate = productReleaseRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReleaseMockMvc.perform(put("/api/product-releases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productRelease)))
            .andExpect(status().isBadRequest());

        // Validate the ProductRelease in the database
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductRelease() throws Exception {
        // Initialize the database
        productReleaseRepository.saveAndFlush(productRelease);

        int databaseSizeBeforeDelete = productReleaseRepository.findAll().size();

        // Delete the productRelease
        restProductReleaseMockMvc.perform(delete("/api/product-releases/{id}", productRelease.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductRelease> productReleaseList = productReleaseRepository.findAll();
        assertThat(productReleaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
