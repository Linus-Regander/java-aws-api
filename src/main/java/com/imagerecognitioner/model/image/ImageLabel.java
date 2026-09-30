package com.imagerecognitioner.model.image;

import io.swagger.v3.oas.annotations.media.Schema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

/**
 * Model class representing a label assigned to an image by the image recognition process.
 * ImageLabel
 */
@DynamoDbBean
@Schema(description = "Label assigned to an image by Amazon Rekognition.")
public class ImageLabel {
    @Schema(description = "Name of the detected label.", example = "Landscape")
    private String label;
    @Schema(description = "Confidence score for the detected label.", example = "98.5")
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
