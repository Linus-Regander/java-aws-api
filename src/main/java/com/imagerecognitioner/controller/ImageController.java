package com.imagerecognitioner.controller;

import com.imagerecognitioner.model.image.ImageResponse;
import com.imagerecognitioner.model.image.ImageMetadata;
import com.imagerecognitioner.service.ImageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.time.Duration;

/**
 * Controller for managing Image objects via RESTful API endpoints.
 * ImageController
 */
@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Retrieves metadata of an Image from DynamoDB.
     * @param imageId the ID of the ImageMetadata object to retrieve
     * @return an ImageMetadata object
     */
    @GetMapping("/{imageId}/metadata")
    public ResponseEntity<ImageMetadata> getImageMetadata(@PathVariable String imageId) {
        return ResponseEntity.ok(imageService.selectImageMetadata(imageId));
    }

    /**
     * Retrieves all ImageMetadata objects from DynamoDB.
     * @return a list of ImageMetadata objects
     */
    @GetMapping("/metadata")
    public ResponseEntity<List<ImageMetadata>> getAllImageMetadata() {
        return ResponseEntity.ok(imageService.selectAllImageMetadata());
    }

    /**
     * Publishes a new image to S3 and stores the metadata in DynamoDB.
     * @param file the image file to upload
     * @param owner the owner of the image
     * @return the created ImageMetadata object
     */
    @PostMapping
    public ResponseEntity<ImageMetadata> publishImage(@RequestParam("file") MultipartFile file, @RequestParam("owner") String owner, @RequestParam(value = "minConfidence", required = false) Float minConfidence) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.publishImage(file, owner, minConfidence));
    }

    /**
     * Updates an existing image object in S3 and its metadata in DynamoDB.
     * @param file the new image file to upload
     * @param imageId the ID of the ImageMetadata object to update
     * @return the updated ImageMetadata object
     */
    @PutMapping("/{imageId}")
    public ResponseEntity<ImageMetadata> updateImage(@RequestParam("file") MultipartFile file, @PathVariable String imageId, @RequestParam(value = "minConfidence", required = false) Float minConfidence) {
        return ResponseEntity.status(HttpStatus.OK).body(imageService.replaceImage(file, imageId, minConfidence));
    }

    /**
     * Updates the metadata of an existing image.
     * @param imageId the ID of the ImageMetadata object to update
     * @param imageMetadata the updated ImageMetadata object
     * @return the updated ImageMetadata object
     */
    @PatchMapping("/{imageId}/metadata")
    public ResponseEntity<ImageMetadata> updateImageMetadata(@PathVariable String imageId,@RequestBody ImageMetadata imageMetadata) {
        return ResponseEntity.ok(imageService.updateMetadata(imageId, imageMetadata));
    }

    /**
     * Retrieves the metadata and presigned URL for an image by its ID.
     * @param imageId the ID of the image to retrieve
     * @param expirySeconds the duration for which the presigned URL should be valid
     * @return the Image object containing the metadata and presigned URL
     */
    @GetMapping("/{imageId}")
    public ResponseEntity<ImageResponse> selectImage(@PathVariable String imageId, @RequestParam(value = "expiry", required = false) Long expirySeconds) {
        Duration expiry = expirySeconds != null ? Duration.ofSeconds(expirySeconds) : null;

        return ResponseEntity.ok(imageService.selectImage(imageId, expiry));
    }

    /**
     * Deletes an Image from S3 Bucket and the metadata from DynamoDB.
     * @param imageId the ID of the image to delete
     * @return a ResponseEntity with no content
     */
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable String imageId) {
        imageService.deleteImage(imageId);

        return ResponseEntity.noContent().build();
    }
}