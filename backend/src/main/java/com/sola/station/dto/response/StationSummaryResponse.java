package com.sola.station.dto.response;

import java.util.List;
import java.util.UUID;

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
