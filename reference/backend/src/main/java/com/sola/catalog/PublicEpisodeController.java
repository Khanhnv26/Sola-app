package com.sola.catalog;

import com.sola.common.ApiResponse;
import com.sola.common.PageResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/public/episodes")
@RequiredArgsConstructor
public class PublicEpisodeController {

    private final EpisodeQueryService episodeQueryService;

    @GetMapping
    public ApiResponse<PageResponse<EpisodeResponse>> listPublicEpisodes(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size
    ) {
        return ApiResponse.ok(episodeQueryService.searchPublicEpisodes(q, page, size));
    }
}
