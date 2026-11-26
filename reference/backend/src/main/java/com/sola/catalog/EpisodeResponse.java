package com.sola.catalog;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EpisodeResponse(
        UUID id,
        String title,
        String summary,
        String audioUrl,
        String imageUrl,
        Integer durationSeconds,
        OffsetDateTime publishedAt,
        UUID sourceId,
        String showTitle
) {

    public static EpisodeResponse from(Episode episode) {
        return new EpisodeResponse(
                episode.getId(),
                episode.getTitle(),
                episode.getSummary(),
                episode.getAudioUrl(),
                episode.getImageUrl(),
                episode.getDurationSeconds(),
                episode.getPublishedAt(),
                episode.getSource().getId(),
                episode.getSource().getTitle()
        );
    }
}
