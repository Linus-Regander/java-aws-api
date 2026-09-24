package com.imagerecognitioner.model.image;

import java.net.URL;

/**
 * Represents an image in the system.
 */
public class ImageResponse {
    private URL presignedUrl;
    private ImageMetadata imageMetadata;

    /**
     * Gets the presigned URL for accessing the image in S3.
     * @return the presigned URL
     */
    public URL getPresignedUrl() {
        return presignedUrl;
    }

    /**
     * Sets the presigned URL for accessing the image in S3.
     * @param presignedUrl the presigned URL
     */
    public void setPresignedUrl(URL presignedUrl) {
        this.presignedUrl = presignedUrl;
    }

    /**
     * Gets the ImageMetadata object associated with the image.
     * @return the ImageMetadata object
     */
    public ImageMetadata getImageMetadata() {
        return imageMetadata;
    }

    /**
     * Sets the ImageMetadata object associated with the image.
     * @param imageMetadata the ImageMetadata object
     */
    public void setImageMetadata(ImageMetadata imageMetadata) {
        this.imageMetadata = imageMetadata;
    }
}
