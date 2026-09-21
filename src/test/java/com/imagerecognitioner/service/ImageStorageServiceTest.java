package com.imagerecognitioner.service;

import com.imagerecognitioner.cli.AwsProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageStorageServiceTest {

    private static final String BUCKET = "image-bucket";

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private PresignedGetObjectRequest presignedRequest;

    private ImageStorageService storageService;

    @BeforeEach
    void setUp() {
        AwsProperties properties = new AwsProperties();
        properties.getS3().setBucketName(BUCKET);
        storageService = new ImageStorageService(s3Client, s3Presigner, properties);
    }

    @Test
    void upload_handlesSuccessfulAndUnreadableFiles() throws Exception {
        Map<String, StorageTestCase> testCases = Map.of(
            "successful upload", () -> {
                MultipartFile file = new MockMultipartFile("file", "photo.jpg", "image/jpeg", "bytes".getBytes());

                assertEquals("images/photo.jpg", storageService.upload("images/photo.jpg", file));

                ArgumentCaptor<PutObjectRequest> request = ArgumentCaptor.forClass(PutObjectRequest.class);
                verify(s3Client).putObject(request.capture(), any(RequestBody.class));
                assertEquals(BUCKET, request.getValue().bucket());
                assertEquals("images/photo.jpg", request.getValue().key());
                assertEquals("image/jpeg", request.getValue().contentType());
                reset(s3Client);
            },
            "file cannot be read", () -> {
                when(multipartFile.getInputStream()).thenThrow(new IOException("read failed"));
                when(multipartFile.getSize()).thenReturn(5L);

                assertThrows(java.io.UncheckedIOException.class,
                    () -> storageService.upload("images/broken.jpg", multipartFile));
                verify(s3Client, never()).putObject(any(PutObjectRequest.class), any(RequestBody.class));
                reset(s3Client, multipartFile);
            });

        runTestCases(testCases);
    }

    @Test
    void replace_uploadsTheNewFileAtTheExistingKey() {
        MultipartFile file = new MockMultipartFile("file", "updated.jpg", "image/jpeg", "bytes".getBytes());

        assertEquals("images/photo.jpg", storageService.replace("images/photo.jpg", file));

        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void delete_removesEachRequestedObject() {
        Map<String, StorageTestCase> testCases = Map.of(
            "known object", () -> deleteObject("images/photo.jpg"),
            "another object", () -> deleteObject("images/other.png"));

        runTestCases(testCases);
    }

    @Test
    void getPresignedUrl_usesEachKeyAndExpiry() throws Exception {
        URL url = new URL("https://example.test/photo.jpg");

        Map<String, StorageTestCase> testCases = Map.of(
            "short expiry", () -> getUrl("images/photo.jpg", Duration.ofMinutes(5), url),
            "long expiry", () -> getUrl("images/photo.jpg", Duration.ofHours(1), url));

        runTestCases(testCases);
    }

    private void deleteObject(String key) {
        storageService.delete(key);

        ArgumentCaptor<DeleteObjectRequest> request = ArgumentCaptor.forClass(DeleteObjectRequest.class);
        verify(s3Client).deleteObject(request.capture());
        assertEquals(BUCKET, request.getValue().bucket());
        assertEquals(key, request.getValue().key());
        reset(s3Client);
    }

    private void getUrl(String key, Duration expiry, URL expectedUrl) {
        reset(s3Presigner, presignedRequest);
        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presignedRequest);
        when(presignedRequest.url()).thenReturn(expectedUrl);

        assertSame(expectedUrl, storageService.getPresignedUrl(key, expiry));

        ArgumentCaptor<GetObjectPresignRequest> request = ArgumentCaptor.forClass(GetObjectPresignRequest.class);
        verify(s3Presigner).presignGetObject(request.capture());
        assertEquals(expiry, request.getValue().signatureDuration());
        assertEquals(BUCKET, request.getValue().getObjectRequest().bucket());
        assertEquals(key, request.getValue().getObjectRequest().key());
    }

    private void runTestCases(Map<String, StorageTestCase> testCases) {
        for (Map.Entry<String, StorageTestCase> testCase : testCases.entrySet()) {
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
    private interface StorageTestCase {
        void run() throws Exception;
    }
}
