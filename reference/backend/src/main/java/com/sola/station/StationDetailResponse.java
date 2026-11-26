package com.sola.station;

import java.util.List;
import java.util.UUID;

public record StationDetailResponse(
        UUID id,
        String slug,
        String name,
        String description,
        String moodTag,
        int defaultFocusMinutes,
        Integer defaultSleepMinutes,
        String themeKey,
        boolean featured,
        String ownerDisplayName,
        String primarySourceTitle,
        long playCount,
        List<AmbientLayerResponse> ambientLayers,
        List<StationSourceResponse> sources
) {
}
