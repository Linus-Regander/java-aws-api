package com.imagerecognitioner.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.imagerecognitioner.model.ImageMetadata;
import com.imagerecognitioner.model.ImageStatus;
import com.imagerecognitioner.model.Image;
import com.imagerecognitioner.cli.AwsProperties;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.net.URL;
import java.time.Duration;

/**
 * Service for managing images, including uploading to S3 and storing metadata in DynamoDB.
 * ImageService
 */
@Service
public class ImageService {
    private final ImageStorageService imageStorageService;
    private final ImageMetadataService imageMetadataService;
    private final String keyPrefix;

    public ImageService(ImageStorageService imageStorageService, ImageMetadataService imageMetadataService, AwsProperties awsProperties) {
        this.imageStorageService = imageStorageService;
        this.imageMetadataService = imageMetadataService;
        this.keyPrefix = awsProperties.getS3().getKeyPrefix();
    }

    /**
     * Selects image metadata from DynamoDB.
     * @param imageId the ID of the image metadata to retrieve
     * @return the ImageMetadata object
     */
    public ImageMetadata selectImageMetadata(String imageId) {
        return imageMetadataService.findById(imageId);
    }

    /**
     * Selects all image metadata from DynamoDB.
     * @return a list of all ImageMetadata objects
     */
    public List<ImageMetadata> selectAllImageMetadata() {
        return imageMetadataService.findAll();
    }

    /**
     * Publishes an image by uploading it to S3 and creating its metadata in DynamoDB.
     * @param file the image file to upload
     * @param owner the owner of the image
     * @return the metadata of the published image
     */
    public ImageMetadata publishImage(MultipartFile file, String owner) {
        String id = UUID.randomUUID().toString();
        String key = buildKey(owner, id, file.getOriginalFilename());

        imageStorageService.upload(key, file);

        try {
            return imageMetadataService.create(extractMetadata(file, owner, id, key));
        } catch (AwsServiceException | SdkClientException e) {
            imageStorageService.delete(key);
            
            throw e;
        }
    }

    /**
     * Replaces an existing image in S3 and updates its metadata in DynamoDB.
     * @param file the new image file to upload
     * @param imageId the ID of the existing image to replace
     * @return the updated metadata of the replaced image
     */
    public ImageMetadata replaceImage(MultipartFile file, String imageId) {
        ImageMetadata existingMetadata = imageMetadataService.findById(imageId);
        String key = existingMetadata.getS3Key();

        imageStorageService.replace(key, file);

        return imageMetadataService.update(imageId, extractMetadata(file, existingMetadata.getOwner(), imageId, key));
    }

    /**
     * Selects an image by retrieving its metadata from DynamoDB and generating a presigned URL for access from S3.
     * @param imageId the ID of the image to select
     * @param expiry the duration for which the presigned URL should be valid
     * @return an Image object containing the metadata and presigned URL
     */
    public Image selectImage(String imageId, Duration expiry) {
        ImageMetadata metadata = imageMetadataService.findById(imageId);

        String key = metadata.getS3Key();

        URL presignedUrl = imageStorageService.getPresignedUrl(key, expiry);

        Image image = new Image();

        image.setPresignedUrl(presignedUrl);
        image.setImageMetadata(metadata);

        return image;
    }

    /**
     * Updates the metadata of an existing image in DynamoDB.
     * @param imageId the ID of the existing image to update
     * @param imageMetadata the new metadata to update
     * @return the updated metadata of the image
     */
    public ImageMetadata updateMetadata(String imageId, ImageMetadata imageMetadata) {
        return imageMetadataService.update(imageId, imageMetadata);
    }

    /**
     * Deletes an image by removing it from S3 and deleting its metadata from DynamoDB.
     * @param imageId the ID of the image to delete
     */
    public void deleteImage(String imageId) {
        ImageMetadata existingMetadata = imageMetadataService.findById(imageId);
        String key = existingMetadata.getS3Key();

        imageStorageService.delete(key);
        imageMetadataService.deleteById(imageId);
    }

    private ImageMetadata extractMetadata(MultipartFile file, String owner, String id, String key) {
        ImageMetadata metadata = new ImageMetadata();

        metadata.setImageId(id);
        metadata.setS3Key(key);
        metadata.setFileName(file.getOriginalFilename());
        metadata.setContentType(file.getContentType());
        metadata.setSizeBytes(file.getSize());
        metadata.setOwner(owner);
        metadata.setCreatedAt(Instant.now());
        metadata.setImageStatus(ImageStatus.PENDING);

        return metadata;
    }

    private String buildKey(String owner, String id, String originalFilename) {
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        
        return keyPrefix + owner + "/" + id + extension;
    }
}
