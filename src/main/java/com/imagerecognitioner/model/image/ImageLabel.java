package com.imagerecognitioner.model.image;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

/**
 * Model class representing a label assigned to an image by the image recognition process.
 * ImageLabel
 */
@DynamoDbBean
public class ImageLabel {
    private String label;
    private float confidence;

    public ImageLabel() {
    }

    public ImageLabel(String label, float confidence) {
        this.label = label;
        this.confidence = confidence;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }
}
