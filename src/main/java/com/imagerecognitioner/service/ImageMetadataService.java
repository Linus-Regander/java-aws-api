package com.imagerecognitioner.service;

import com.imagerecognitioner.exception.ImageMetadataNotFoundException;
import com.imagerecognitioner.model.ImageMetadata;
import com.imagerecognitioner.repository.ImageMetadataRepositoryInterface;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Service for managing ImageMetadata objects.
 * ImageMetadataService
 */
@Service
public class ImageMetadataService {
    private ImageMetadataRepositoryInterface imageMetadataRepository;

    public ImageMetadataService(ImageMetadataRepositoryInterface imageMetadataRepository) {
        this.imageMetadataRepository = imageMetadataRepository;
    }

    /**
     * Saves an ImageMetadata object.
     * @param imageMetadata the ImageMetadata object to save
     * @return the saved ImageMetadata object
     */
    public ImageMetadata save(ImageMetadata imageMetadata) {
        return imageMetadataRepository.save(imageMetadata);
    }

    /**
     * Retrieves all ImageMetadata objects.
     * @return a list of all ImageMetadata objects
     */
    public List<ImageMetadata> findAll() {
        return imageMetadataRepository.findAll();
    }

    /**
     * Retrieves an ImageMetadata object by its imageId.
     * @param imageId the ID of the ImageMetadata object to retrieve
     * @return the ImageMetadata object
     * @throws ImageMetadataNotFoundException if no ImageMetadata exists with the given imageId
     */
    public ImageMetadata findById(String imageId) {
        return imageMetadataRepository.findById(imageId)
                .orElseThrow(() -> new ImageMetadataNotFoundException(imageId));
    }

    /**
     * Deletes an ImageMetadata object by its imageId.
     * @param imageId the ID of the ImageMetadata object to delete
     * @throws ImageMetadataNotFoundException if no ImageMetadata exists with the given imageId
     */
    public void deleteById(String imageId) {
        if (imageMetadataRepository.findById(imageId).isEmpty()) {
            throw new ImageMetadataNotFoundException(imageId);
        }

        imageMetadataRepository.deleteById(imageId);
    }
}