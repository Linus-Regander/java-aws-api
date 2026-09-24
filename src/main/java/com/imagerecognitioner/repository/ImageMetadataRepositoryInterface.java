package com.imagerecognitioner.repository;

import java.util.List;
import java.util.Optional;

import com.imagerecognitioner.model.image.ImageMetadata;

public interface ImageMetadataRepositoryInterface {
    ImageMetadata save(ImageMetadata imageMetadata);

    List<ImageMetadata> findAll();

    Optional<ImageMetadata> findById(String imageId);

    void deleteById(String imageId);
}