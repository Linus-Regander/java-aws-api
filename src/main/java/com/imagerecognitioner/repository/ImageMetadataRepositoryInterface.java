package com.imagerecognitioner.repository;

import com.imagerecognitioner.model.ImageMetadata;

import java.util.List;
import java.util.Optional;

public interface ImageMetadataRepositoryInterface {
    ImageMetadata save(ImageMetadata imageMetadata);

    List<ImageMetadata> findAll();

    Optional<ImageMetadata> findById(String imageId);

    void deleteById(String imageId);
}