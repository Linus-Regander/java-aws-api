package com.imagerecognitioner.model.image;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.util.List;

/**
 * Model class representing metadata for an image stored in DynamoDB.
 * ImageMetadata
 */

@DynamoDbBean
public class ImageMetadata {
    private String imageId;
    private String fileName;
    private String contentType;
    private String s3Key;
    private String s3Bucket;
    private String owner;
    private Long sizeBytes;
    private Instant createdAt;
    private Instant updatedAt;
    private ImageStatus imageStatus;
    private List<ImageLabel> labels;

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
     * Gets the S3 key for the image.
     * @return the S3 key
     */
    public String getS3Key() {
        return s3Key;
    }

    /**
     * Sets the S3 key for the image.
     * @param s3Key the S3 key
     */
    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    /**
     * Gets the S3 bucket name for the image.
     * @return the S3 bucket name
     */
    public String getS3Bucket() {
        return s3Bucket;
    }

    /**
     * Sets the S3 bucket name for the image.
     * @param s3Bucket the S3 bucket name
     */
    public void setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    /**
     * Gets the owner of the image.
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the image.
     * @param owner the owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
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

    /**
     * Gets the status of the image.
     * @return the image status
     */
    public ImageStatus getImageStatus() {
        return imageStatus;
    }

    /**
     * Sets the status of the image.
     * @param imageStatus the image status
     */
    public void setImageStatus(ImageStatus imageStatus) {
        this.imageStatus = imageStatus;
    }

    /**
     * Gets the list of labels associated with the image.
     * @return the list of image labels
     */
    public List<ImageLabel> getLabels() {
        return labels;
    }

    /**
     * Sets the list of labels associated with the image.
     * @param labels the list of image labels
     */
    public void setLabels(List<ImageLabel> labels) {
        this.labels = labels;
    }
}