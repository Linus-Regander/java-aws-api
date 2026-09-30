package com.imagerecognitioner.model.image;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum representing the status of an image in the image recognition process.
 * ImageStatus
 */
@Schema(description = "Processing status of an image.")
public enum ImageStatus {
    PENDING,
    PROCESSED,
    FAILED
}
