package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Upload;
import com.backbase.oss.boat.bay.repository.UploadRepository;

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
 * Integration tests for the {@link UploadResource} REST controller.
 */
@SpringBootTest(classes = BoatBayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class UploadResourceIT {

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PROCESSED = false;
    private static final Boolean UPDATED_PROCESSED = true;

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR = "AAAAAAAAAA";
    private static final String UPDATED_ERROR = "BBBBBBBBBB";

    @Autowired
    private UploadRepository uploadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUploadMockMvc;

    private Upload upload;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Upload createEntity(EntityManager em) {
        Upload upload = new Upload()
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .fileName(DEFAULT_FILE_NAME)
            .processed(DEFAULT_PROCESSED)
            .action(DEFAULT_ACTION)
            .error(DEFAULT_ERROR);
        return upload;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Upload createUpdatedEntity(EntityManager em) {
        Upload upload = new Upload()
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .processed(UPDATED_PROCESSED)
            .action(UPDATED_ACTION)
            .error(UPDATED_ERROR);
        return upload;
    }

    @BeforeEach
    public void initTest() {
        upload = createEntity(em);
    }

    @Test
    @Transactional
    public void createUpload() throws Exception {
        int databaseSizeBeforeCreate = uploadRepository.findAll().size();
        // Create the Upload
        restUploadMockMvc.perform(post("/api/uploads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(upload)))
            .andExpect(status().isCreated());

        // Validate the Upload in the database
        List<Upload> uploadList = uploadRepository.findAll();
        assertThat(uploadList).hasSize(databaseSizeBeforeCreate + 1);
        Upload testUpload = uploadList.get(uploadList.size() - 1);
        assertThat(testUpload.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testUpload.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUpload.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testUpload.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testUpload.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testUpload.isProcessed()).isEqualTo(DEFAULT_PROCESSED);
        assertThat(testUpload.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testUpload.getError()).isEqualTo(DEFAULT_ERROR);
    }

    @Test
    @Transactional
    public void createUploadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = uploadRepository.findAll().size();

        // Create the Upload with an existing ID
        upload.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUploadMockMvc.perform(post("/api/uploads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(upload)))
            .andExpect(status().isBadRequest());

        // Validate the Upload in the database
        List<Upload> uploadList = uploadRepository.findAll();
        assertThat(uploadList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUploads() throws Exception {
        // Initialize the database
        uploadRepository.saveAndFlush(upload);

        // Get all the uploadList
        restUploadMockMvc.perform(get("/api/uploads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(upload.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].processed").value(hasItem(DEFAULT_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].error").value(hasItem(DEFAULT_ERROR)));
    }
    
    @Test
    @Transactional
    public void getUpload() throws Exception {
        // Initialize the database
        uploadRepository.saveAndFlush(upload);

        // Get the upload
        restUploadMockMvc.perform(get("/api/uploads/{id}", upload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(upload.getId().intValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.processed").value(DEFAULT_PROCESSED.booleanValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.error").value(DEFAULT_ERROR));
    }
    @Test
    @Transactional
    public void getNonExistingUpload() throws Exception {
        // Get the upload
        restUploadMockMvc.perform(get("/api/uploads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUpload() throws Exception {
        // Initialize the database
        uploadRepository.saveAndFlush(upload);

        int databaseSizeBeforeUpdate = uploadRepository.findAll().size();

        // Update the upload
        Upload updatedUpload = uploadRepository.findById(upload.getId()).get();
        // Disconnect from session so that the updates on updatedUpload are not directly saved in db
        em.detach(updatedUpload);
        updatedUpload
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .processed(UPDATED_PROCESSED)
            .action(UPDATED_ACTION)
            .error(UPDATED_ERROR);

        restUploadMockMvc.perform(put("/api/uploads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedUpload)))
            .andExpect(status().isOk());

        // Validate the Upload in the database
        List<Upload> uploadList = uploadRepository.findAll();
        assertThat(uploadList).hasSize(databaseSizeBeforeUpdate);
        Upload testUpload = uploadList.get(uploadList.size() - 1);
        assertThat(testUpload.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testUpload.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUpload.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testUpload.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testUpload.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testUpload.isProcessed()).isEqualTo(UPDATED_PROCESSED);
        assertThat(testUpload.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testUpload.getError()).isEqualTo(UPDATED_ERROR);
    }

    @Test
    @Transactional
    public void updateNonExistingUpload() throws Exception {
        int databaseSizeBeforeUpdate = uploadRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUploadMockMvc.perform(put("/api/uploads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(upload)))
            .andExpect(status().isBadRequest());

        // Validate the Upload in the database
        List<Upload> uploadList = uploadRepository.findAll();
        assertThat(uploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUpload() throws Exception {
        // Initialize the database
        uploadRepository.saveAndFlush(upload);

        int databaseSizeBeforeDelete = uploadRepository.findAll().size();

        // Delete the upload
        restUploadMockMvc.perform(delete("/api/uploads/{id}", upload.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Upload> uploadList = uploadRepository.findAll();
        assertThat(uploadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
