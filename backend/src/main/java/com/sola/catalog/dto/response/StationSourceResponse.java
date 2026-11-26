package com.sola.catalog.dto.response;


import com.sola.catalog.entity.ContentSource;
import com.sola.station.entity.StationSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.UUID;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationSourceResponse {

    private UUID id;
    private String title;
    private String sourceType;
    private String streamFormat;
    private String sourceUrl;
    private String coverImageUrl;
    private String healthStatus;
    private int priorityOrder;

    public static StationSourceResponse from(StationSource link) {
        ContentSource s = link.getSource();
        return StationSourceResponse.builder()
                .id(s.getId())
                .title(s.getTitle())
                .sourceType(s.getSourceType())
                .streamFormat(s.getStreamFormat())
                .sourceUrl(s.getSourceUrl())
                .coverImageUrl(s.getCoverImageUrl())
                .healthStatus(String.valueOf(s.getHealthStatus()))
                .priorityOrder(link.getPriorityOrder())
                .build();
    }
}