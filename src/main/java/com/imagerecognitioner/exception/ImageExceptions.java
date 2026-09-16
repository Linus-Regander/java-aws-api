package com.imagerecognitioner.exception;

/**
 * Custom exceptions that are not caused by AWS or other internal processes.
 * ImageExceptions
 */
public final class ImageExceptions {
    private ImageExceptions() {}

    public static class ImageMetadataNotFoundException extends RuntimeException {
        public ImageMetadataNotFoundException(String imageId) {
            super("ImageMetadata not found with id: " + imageId);
        }
    }

    public static class InvalidImageException extends RuntimeException {
        public InvalidImageException(String message) {
            super(message);
        }
    }
}