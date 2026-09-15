package com.imagerecognitioner.controller;

import com.imagerecognitioner.model.ImageMetadata;
import com.imagerecognitioner.service.ImageMetadataService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing ImageMetadata objects via RESTful API endpoints.
 * ImageMetadataController
 */
@RestController
@RequestMapping("/api/images")
public class ImageMetadataController {
    private final ImageMetadataService imageMetadataService;

    public ImageMetadataController(ImageMetadataService imageMetadataService) {
        this.imageMetadataService = imageMetadataService;
    }

    /**
     * Creates a new ImageMetadata object.
     * @param imageMetadata the ImageMetadata object to create
     * @return the created ImageMetadata object
     */
    @PostMapping
    public ResponseEntity<ImageMetadata> createImageMetadata(@RequestBody ImageMetadata imageMetadata) {
        ImageMetadata saved = imageMetadataService.create(imageMetadata);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Updates an existing ImageMetadata object.
     * @param imageId the ID of the ImageMetadata object to update
     * @param imageMetadata the updated ImageMetadata object
     * @return the updated ImageMetadata object
     */
    @PutMapping("/{imageId}")
    public ResponseEntity<ImageMetadata> updateImageMetadata(@PathVariable String imageId, @RequestBody ImageMetadata imageMetadata) {
        ImageMetadata saved = imageMetadataService.update(imageId, imageMetadata);

        return ResponseEntity.status(HttpStatus.OK).body(saved);
    }

    /**
     * Retrieves all ImageMetadata objects.
     * @return a list of all ImageMetadata objects
     */
    @GetMapping
    public ResponseEntity<List<ImageMetadata>> getAllImageMetadata() {
        return ResponseEntity.ok(imageMetadataService.findAll());
    }

    /**
     * Retrieves an ImageMetadata object by its imageId.
     * @param imageId
     * @return the ImageMetadata object
     */
    @GetMapping("/{imageId}")
    public ResponseEntity<ImageMetadata> getImageMetadataById(@PathVariable String imageId) {
        return ResponseEntity.ok(imageMetadataService.findById(imageId));
    }

    /**
     * Deletes an ImageMetadata object by its imageId.
     * @param imageId
     * @return a ResponseEntity with no content
     */
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImageMetadata(@PathVariable String imageId) {
        imageMetadataService.deleteById(imageId);

        return ResponseEntity.noContent().build();
    }
}