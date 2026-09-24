package com.imagerecognitioner.model.moderation;

import com.imagerecognitioner.model.image.ImageLabel;

import java.util.List;

/**
 * Result of a content moderation check against an image.
 * ModerationResult
 */
public class ModerationResult {
    private final boolean flagged;
    private final List<ImageLabel> labels;

    public ModerationResult(boolean flagged, List<ImageLabel> labels) {
        this.flagged = flagged;
        this.labels = labels;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public List<ImageLabel> getLabels() {
        return labels;
    }
}