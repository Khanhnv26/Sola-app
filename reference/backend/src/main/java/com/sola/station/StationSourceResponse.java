package com.sola.station;

import com.sola.catalog.ContentSource;

import java.util.UUID;

public record StationSourceResponse(
        UUID id,
        String title,
        String sourceType,
        String streamFormat,
        String sourceUrl,
        String coverImageUrl,
        String healthStatus,
        int priorityOrder
) {

    public static StationSourceResponse from(StationSource link) {
        ContentSource s = link.getSource();
        return new StationSourceResponse(
                s.getId(),
                s.getTitle(),
                s.getSourceType(),
                s.getStreamFormat(),
                s.getSourceUrl(),
                s.getCoverImageUrl(),
                s.getHealthStatus(),
                link.getPriorityOrder()
        );
    }
}
