package com.imagerecognitioner.exception;

public class ImageMetadataNotFoundException extends RuntimeException {
    public ImageMetadataNotFoundException(String imageId) {
        super("ImageMetadata not found with id: " + imageId);
    }
}