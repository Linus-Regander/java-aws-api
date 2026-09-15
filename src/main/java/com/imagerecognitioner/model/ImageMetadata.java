package com.imagerecognitioner.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;


/**
 * Model class representing metadata for an image stored in DynamoDB.
 * ImageMetadata
 */

@DynamoDbBean
public class ImageMetadata {
    private String imageId;
    private String fileName;
    private String contentType;
    private Long sizeBytes;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Gets the ID of the image.
     * @return the image ID
     */
    @DynamoDbPartitionKey
    public String getImageId() {
        return imageId;
    }

    /**
     * Sets the ID of the image.
     * @param imageId the image ID
     */
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    /**
     * Gets the file name of the image.
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name of the image.
     * @param fileName the file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the content type of the image.
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the content type of the image.
     * @param contentType the content type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Gets the size of the image in bytes.
     * @return the size in bytes
     */
    public Long getSizeBytes() {
        return sizeBytes;
    }

    /**
     * Sets the size of the image in bytes.
     * @param sizeBytes the size in bytes
     */
    public void setSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    /**
     * Gets the creation timestamp of the image metadata.
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the image metadata.
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last updated timestamp of the image metadata.
     * @return the last updated timestamp
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last updated timestamp of the image metadata.
     * @param updatedAt the last updated timestamp
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}