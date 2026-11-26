package com.sola.station;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationSummaryResponse {

    private UUID id;
    private String slug;
    private String name;
    private String description;
    private String moodTag;
    private int defaultFocusMinutes;
    private String themeKey;
    private boolean featured;
    private String ownerDisplayName;
    private String primarySourceTitle;
    private long playCount;
    private List<AmbientLayerResponse> ambientLayers;
}
