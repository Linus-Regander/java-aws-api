package com.imagerecognitioner.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.imagerecognitioner.cli.AwsProperties;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.net.URL;
import java.time.Duration;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Service for managing image storage in AWS S3.
 * ImageStorageService
 */
@Service 
public class ImageStorageService {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public ImageStorageService(S3Client s3Client, S3Presigner s3Presigner, AwsProperties awsProperties) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = awsProperties.getS3().getBucketName();
    }

    /**
     * Uploads a file to the S3 bucket.
     * @param key the key under which to store the new object
     * @param file the file to upload
     * @return the key of the uploaded object
     */
    public String upload(String key, MultipartFile file) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(
                    file.getInputStream(), file.getSize()));

            return key;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read file for upload", e);
        }
    }

    /**
     * Replaces the object at an existing key with new file content.
     * @param key the key of the object to replace
     * @param newFile the new file content
     * @return the key of the replaced object
     */
    public String replace(String key, MultipartFile newFile) {
        return upload(key, newFile);
    }

    /**
     * Deletes an object from the S3 bucket.
     * @param key the key of the object to delete
     */
    public void delete(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
    }

        /**
     * Generates a time-limited pre-signed URL for downloading an object.
     * @param key the key of the object to generate a URL for
     * @param expiry how long the URL should remain valid
     * @return the pre-signed URL
     */
    public URL getPresignedUrl(String key, Duration expiry) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(expiry)
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }
}
