package com.imagerecognitioner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagerecognitioner.model.image.ImageResponse;
import com.imagerecognitioner.model.image.ImageMetadata;
import com.imagerecognitioner.service.ImageService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for ImageController.
 * ImageService is mocked and MockMvc is configured with the controller directly,
 * so no Spring context, DynamoDB, S3, or real service logic is touched here.
 */
@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    private ObjectMapper objectMapper;

    private static final String IMAGE_ID = "img-123";

    private ImageMetadata sampleMetadata;

    @BeforeEach
    void setUp() {
        sampleMetadata = new ImageMetadata();
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    void getImageMetadata_handlesEachImageId() throws Exception {
        Map<String, ControllerTestCase> testCases = Map.of(
            "known image", () -> {
                when(imageService.selectImageMetadata(IMAGE_ID)).thenReturn(sampleMetadata);

                mockMvc.perform(get("/api/images/{imageId}/metadata", IMAGE_ID))
                    .andExpect(status().isOk());

                verify(imageService).selectImageMetadata(IMAGE_ID);
            },
            "another image", () -> {
                String anotherImageId = "img-456";
                when(imageService.selectImageMetadata(anotherImageId)).thenReturn(sampleMetadata);

                mockMvc.perform(get("/api/images/{imageId}/metadata", anotherImageId))
                    .andExpect(status().isOk());

                verify(imageService).selectImageMetadata(anotherImageId);
            });

        runTestCases(testCases);
    }

    @Test
    void getAllImageMetadata_returnsEachConfiguredResult() throws Exception {
        Map<String, ControllerTestCase> testCases = Map.of(
            "one metadata item", () -> {
                when(imageService.selectAllImageMetadata()).thenReturn(List.of(sampleMetadata));

                mockMvc.perform(get("/api/images/metadata"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1));

                verify(imageService).selectAllImageMetadata();
            },
            "no metadata items", () -> {
                when(imageService.selectAllImageMetadata()).thenReturn(List.of());

                mockMvc.perform(get("/api/images/metadata"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));

                verify(imageService).selectAllImageMetadata();
            });

        runTestCases(testCases);
    }

    @Test
    void publishImage_returnsCreatedForEachUpload() throws Exception {
        Map<String, ControllerTestCase> testCases = Map.of(
            "jpeg upload", () -> publishImage("cat.jpg", "someOwner"),
            "png upload", () -> publishImage("landscape.png", "anotherOwner"));

        runTestCases(testCases);
    }

    @Test
    void updateImage_returnsOkForEachReplacement() throws Exception {
        Map<String, ControllerTestCase> testCases = Map.of(
            "replace jpeg", () -> replaceImage("cat-updated.jpg", IMAGE_ID),
            "replace png", () -> replaceImage("landscape-updated.png", "img-456"));

        runTestCases(testCases);
    }

    @Test
    void updateImageMetadata_returnsOkForEachMetadataUpdate() throws Exception {
        Map<String, ControllerTestCase> testCases = Map.of(
            "pending metadata", () -> updateImageMetadata(IMAGE_ID, sampleMetadata),
            "metadata for another image", () -> updateImageMetadata("img-456", sampleMetadata));

        runTestCases(testCases);
    }

    @Test
    void selectImage_passesEachExpiryOption() throws Exception {
        Map<String, ControllerTestCase> testCases = Map.of(
            "without expiry", () -> selectImage(null),
            "five minute expiry", () -> selectImage(300L),
            "one hour expiry", () -> selectImage(3600L));

        runTestCases(testCases);
    }

    @Test
    void deleteImage_returnsNoContentForEachImageId() throws Exception {
        Map<String, ControllerTestCase> testCases = Map.of(
            "known image", () -> deleteImage(IMAGE_ID),
            "another image", () -> deleteImage("img-456"));

        runTestCases(testCases);
    }

    private void publishImage(String fileName, String owner) throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file", fileName, MediaType.IMAGE_JPEG_VALUE, "fake-image-bytes".getBytes());
        when(imageService.publishImage(any(), eq(owner), any())).thenReturn(sampleMetadata);

        mockMvc.perform(multipart("/api/images")
                .file(file)
                .param("owner", owner))
            .andExpect(status().isCreated());

        verify(imageService).publishImage(any(), eq(owner), isNull());
    }

    private void replaceImage(String fileName, String imageId) throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file", fileName, MediaType.IMAGE_JPEG_VALUE, "new-bytes".getBytes());
        when(imageService.replaceImage(any(), eq(imageId), any())).thenReturn(sampleMetadata);

        mockMvc.perform(multipart("/api/images/{imageId}", imageId)
                .file(file)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }))
            .andExpect(status().isOk());

        verify(imageService).replaceImage(any(), eq(imageId), isNull());
    }

    private void updateImageMetadata(String imageId, ImageMetadata metadata) throws Exception {
        when(imageService.updateMetadata(eq(imageId), any(ImageMetadata.class))).thenReturn(metadata);

        mockMvc.perform(patch("/api/images/{imageId}/metadata", imageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(metadata)))
            .andExpect(status().isOk());

        verify(imageService).updateMetadata(eq(imageId), any(ImageMetadata.class));
    }

    private void selectImage(Long expirySeconds) throws Exception {
        ImageResponse image = new ImageResponse();
        Duration expiry = expirySeconds == null ? null : Duration.ofSeconds(expirySeconds);
        when(imageService.selectImage(IMAGE_ID, expiry)).thenReturn(image);

        MockHttpServletRequestBuilder request = get("/api/images/{imageId}", IMAGE_ID);
        if (expirySeconds != null) {
            request.param("expiry", expirySeconds.toString());
        }

        mockMvc.perform(request)
            .andExpect(status().isOk());

        verify(imageService).selectImage(IMAGE_ID, expiry);
    }

    private void deleteImage(String imageId) throws Exception {
        doNothing().when(imageService).deleteImage(imageId);

        mockMvc.perform(delete("/api/images/{imageId}", imageId))
            .andExpect(status().isNoContent());

        verify(imageService).deleteImage(imageId);
    }

    private void runTestCases(Map<String, ControllerTestCase> testCases) throws Exception {
        for (Map.Entry<String, ControllerTestCase> testCase : testCases.entrySet()) {
            reset(imageService);
            try {
                testCase.getValue().run();
            } catch (AssertionError assertionError) {
                throw new AssertionError("Test case failed: " + testCase.getKey(), assertionError);
            } catch (Exception exception) {
                throw new AssertionError("Test case failed: " + testCase.getKey(), exception);
            }
        }
    }

    @FunctionalInterface
    private interface ControllerTestCase {
        void run() throws Exception;
    }
}