package com.sola.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeResponse {

    private UUID id;
    private String title;
    private String summary;
    private String audioUrl;
    private String imageUrl;
    private Integer durationSeconds;
    private OffsetDateTime publishedAt;
    private UUID sourceId;
    private String showTitle;

    public static EpisodeResponse from(Episode episode) {
        return EpisodeResponse.builder()
                .id(episode.getId())
                .title(episode.getTitle())
                .summary(episode.getSummary())
                .audioUrl(episode.getAudioUrl())
                .imageUrl(episode.getImageUrl())
                .durationSeconds(episode.getDurationSeconds())
                .publishedAt(episode.getPublishedAt())
                .sourceId(episode.getSource().getId())
                .showTitle(episode.getSource().getTitle())
                .build();
    }
}
