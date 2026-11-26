package com.sola.station;

import java.util.List;
import java.util.UUID;

public record StationSummaryResponse(
        UUID id,
        String slug,
        String name,
        String description,
        String moodTag,
        int defaultFocusMinutes,
        String themeKey,
        boolean featured,
        String ownerDisplayName,
        String primarySourceTitle,
        long playCount,
        List<AmbientLayerResponse> ambientLayers
) {
}
