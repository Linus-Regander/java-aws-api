package com.imagerecognitioner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagerecognitioner.model.Image;
import com.imagerecognitioner.model.ImageMetadata;
import com.imagerecognitioner.service.ImageService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web-layer tests for ImageController.
 * Only the controller is loaded (via @WebMvcTest); ImageService is mocked,
 * so no DynamoDB, S3, or real service logic is touched here.
 */
@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String IMAGE_ID = "img-123";

    private ImageMetadata sampleMetadata;

    @BeforeEach
    void setUp() {
        sampleMetadata = new ImageMetadata();
    }

    @Test
    void getImageMetadata_returnsOkAndMetadata() throws Exception {
        when(imageService.selectImageMetadata(IMAGE_ID)).thenReturn(sampleMetadata);

        mockMvc.perform(get("/api/images/{imageId}/metadata", IMAGE_ID))
            .andExpect(status().isOk());

        verify(imageService).selectImageMetadata(IMAGE_ID);
    }

    @Test
    void getAllImageMetadata_returnsOkAndList() throws Exception {
        when(imageService.selectAllImageMetadata()).thenReturn(List.of(sampleMetadata));

        mockMvc.perform(get("/api/images/metadata"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(1));

        verify(imageService).selectAllImageMetadata();
    }

    @Test
    void publishImage_returnsCreated() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file", "cat.jpg", MediaType.IMAGE_JPEG_VALUE, "fake-image-bytes".getBytes());

        when(imageService.publishImage(any(), eq("someOwner"))).thenReturn(sampleMetadata);

        mockMvc.perform(multipart("/api/images")
                .file(file)
                .param("owner", "someOwner"))
            .andExpect(status().isCreated());

        verify(imageService).publishImage(any(), eq("someOwner"));
    }

    @Test
    void updateImage_returnsOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file", "cat-updated.jpg", MediaType.IMAGE_JPEG_VALUE, "new-bytes".getBytes());

        when(imageService.replaceImage(any(), eq(IMAGE_ID))).thenReturn(sampleMetadata);

        // MockMvc's multipart() builds a POST by default, so switch it to PUT explicitly.
        mockMvc.perform(multipart("/api/images/{imageId}", IMAGE_ID)
                .file(file)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }))
            .andExpect(status().isOk());

        verify(imageService).replaceImage(any(), eq(IMAGE_ID));
    }


    @Test
    void updateImageMetadata_returnsOk() throws Exception {
        when(imageService.updateMetadata(eq(IMAGE_ID), any(ImageMetadata.class)))
            .thenReturn(sampleMetadata);

        mockMvc.perform(patch("/api/images/{imageId}/metadata", IMAGE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleMetadata)))
            .andExpect(status().isOk());

        verify(imageService).updateMetadata(eq(IMAGE_ID), any(ImageMetadata.class));
    }

    @Test
    void selectImage_withoutExpiry_returnsOk() throws Exception {
        Image image = new Image();
        // TODO: set real fields on Image, e.g. image.setMetadata(sampleMetadata); image.setUrl("https://...");

        when(imageService.selectImage(IMAGE_ID, null)).thenReturn(image);

        mockMvc.perform(get("/api/images/{imageId}", IMAGE_ID))
            .andExpect(status().isOk());

        verify(imageService).selectImage(IMAGE_ID, null);
    }

    @Test
    void selectImage_withExpiry_passesDurationToService() throws Exception {
        Image image = new Image();
        when(imageService.selectImage(eq(IMAGE_ID), eq(Duration.ofSeconds(300)))).thenReturn(image);

        mockMvc.perform(get("/api/images/{imageId}", IMAGE_ID)
                .param("expiry", "300"))
            .andExpect(status().isOk());

        verify(imageService).selectImage(IMAGE_ID, Duration.ofSeconds(300));
    }

    // ---------- DELETE /api/images/{imageId} ----------

    @Test
    void deleteImage_returnsNoContent() throws Exception {
        doNothing().when(imageService).deleteImage(IMAGE_ID);

        mockMvc.perform(delete("/api/images/{imageId}", IMAGE_ID))
            .andExpect(status().isNoContent());

        verify(imageService).deleteImage(IMAGE_ID);
    }
}