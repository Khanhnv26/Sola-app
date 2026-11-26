package com.sola.station;

import com.sola.common.ApiResponse;
import com.sola.common.PageResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicStationController {

    private final StationQueryService stationQueryService;

    @GetMapping("/ambient-sounds")
    public ApiResponse<List<AmbientSoundResponse>> listAmbientSounds() {
        return ApiResponse.ok(stationQueryService.listActiveAmbientSounds());
    }

    @GetMapping("/stations/featured")
    public ApiResponse<List<StationSummaryResponse>> listFeaturedStations() {
        return ApiResponse.ok(stationQueryService.listFeaturedStations());
    }

    @GetMapping("/stations/discover")
    public ApiResponse<PageResponse<StationSummaryResponse>> discoverStations(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "12") @Min(1) @Max(50) int size
    ) {
        return ApiResponse.ok(stationQueryService.discoverStations(category, q, page, size));
    }

    @GetMapping("/stations/{slug}")
    public ApiResponse<StationDetailResponse> getStationBySlug(@PathVariable String slug) {
        return ApiResponse.ok(stationQueryService.getPublicStationBySlug(slug));
    }
}
