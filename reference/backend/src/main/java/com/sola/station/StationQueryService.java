package com.sola.station;

import com.sola.common.AppException;
import com.sola.common.ErrorCode;
import com.sola.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationQueryService {

    private static final Set<String> DIRECT_TAGS = Set.of("LOFI", "DEEP_WORK", "CODING", "PODCAST");

    private final AmbientSoundRepository ambientSoundRepository;
    private final MoodStationRepository moodStationRepository;
    private final StationSourceRepository stationSourceRepository;
    private final StationAmbientLayerRepository stationAmbientLayerRepository;

    public List<AmbientSoundResponse> listActiveAmbientSounds() {
        return ambientSoundRepository.findByIsActiveTrueOrderByCodeAsc().stream()
                .map(AmbientSoundResponse::from)
                .toList();
    }

    public List<StationSummaryResponse> listFeaturedStations() {
        List<MoodStation> stations = moodStationRepository
                .findByIsFeaturedTrueAndIsPublicTrueAndIsHiddenByAdminFalseOrderByPlayCountDescNameAsc();
        return mapToSummaries(stations);
    }

    public PageResponse<StationSummaryResponse> discoverStations(String category, String q, int page, int size) {
        Set<String> tags = resolveMoodTags(category);
        Specification<MoodStation> spec = buildDiscoverSpec(tags, q);
        Sort sort = Sort.by(
                Sort.Order.desc("isFeatured"),
                Sort.Order.desc("playCount"),
                Sort.Order.asc("name")
        );
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MoodStation> result = moodStationRepository.findAll(spec, pageable);
        List<StationSummaryResponse> summaries = mapToSummaries(result.getContent());
        return PageResponse.from(result, summaries);
    }

    public StationDetailResponse getPublicStationBySlug(String slug) {
        MoodStation station = moodStationRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.STATION_NOT_FOUND));
        if (!station.isPublic() || station.isHiddenByAdmin()) {
            throw new AppException(ErrorCode.STATION_NOT_FOUND);
        }

        List<UUID> ids = List.of(station.getId());
        List<AmbientLayerResponse> layers = loadLayersByStationId(ids).getOrDefault(station.getId(), List.of());
        List<StationSourceResponse> sources = loadSourcesByStationId(ids).getOrDefault(station.getId(), List.of());
        String primarySourceTitle = sources.isEmpty() ? null : sources.get(0).getTitle();

        return new StationDetailResponse(
                station.getId(),
                station.getSlug(),
                station.getName(),
                station.getDescription(),
                station.getMoodTag(),
                station.getDefaultFocusMinutes(),
                station.getDefaultSleepMinutes(),
                station.getThemeKey(),
                station.isFeatured(),
                station.getOwner() != null ? station.getOwner().getDisplayName() : null,
                primarySourceTitle,
                station.getPlayCount(),
                layers,
                sources
        );
    }

    Set<String> resolveMoodTags(String category) {
        if (category == null || category.isBlank()) {
            return Set.of();
        }
        String normalized = category.trim().toUpperCase(Locale.ROOT);
        if ("ALL".equals(normalized)) {
            return Set.of();
        }
        if ("FOCUS".equals(normalized)) {
            return Set.of("DEEP_WORK", "CODING");
        }
        if (DIRECT_TAGS.contains(normalized)) {
            return Set.of(normalized);
        }
        throw new AppException(ErrorCode.VALIDATION_ERROR, "Danh mục lọc không hợp lệ: " + category);
    }

    private Specification<MoodStation> buildDiscoverSpec(Set<String> tags, String q) {
        Specification<MoodStation> spec = (root, query, cb) -> cb.and(
                cb.isTrue(root.get("isPublic")),
                cb.isFalse(root.get("isHiddenByAdmin"))
        );

        if (!tags.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("moodTag").in(tags));
        }

        if (q != null && !q.isBlank()) {
            String pattern = "%" + q.trim().toLowerCase(Locale.ROOT) + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(cb.coalesce(root.get("description"), "")), pattern)
            ));
        }
        return spec;
    }

    private List<StationSummaryResponse> mapToSummaries(List<MoodStation> stations) {
        if (stations.isEmpty()) {
            return List.of();
        }

        List<UUID> stationIds = stations.stream().map(MoodStation::getId).toList();
        Map<UUID, List<AmbientLayerResponse>> layersMap = loadLayersByStationId(stationIds);
        Map<UUID, List<StationSourceResponse>> sourcesMap = loadSourcesByStationId(stationIds);

        return stations.stream()
                .map(station -> {
                    List<AmbientLayerResponse> layers = layersMap.getOrDefault(station.getId(), List.of());
                    List<StationSourceResponse> sources = sourcesMap.getOrDefault(station.getId(), List.of());
                    String primarySourceTitle = sources.isEmpty() ? null : sources.get(0).getTitle();
                    return new StationSummaryResponse(
                            station.getId(),
                            station.getSlug(),
                            station.getName(),
                            station.getDescription(),
                            station.getMoodTag(),
                            station.getDefaultFocusMinutes(),
                            station.getThemeKey(),
                            station.isFeatured(),
                            station.getOwner() != null ? station.getOwner().getDisplayName() : null,
                            primarySourceTitle,
                            station.getPlayCount(),
                            layers
                    );
                })
                .toList();
    }

    private Map<UUID, List<AmbientLayerResponse>> loadLayersByStationId(List<UUID> stationIds) {
        Map<UUID, List<AmbientLayerResponse>> map = new LinkedHashMap<>();
        for (StationAmbientLayer layer : stationAmbientLayerRepository.findByStationIds(stationIds)) {
            map.computeIfAbsent(layer.getId().getStationId(), k -> new ArrayList<>())
                    .add(AmbientLayerResponse.from(layer));
        }
        return map;
    }

    private Map<UUID, List<StationSourceResponse>> loadSourcesByStationId(List<UUID> stationIds) {
        Map<UUID, List<StationSourceResponse>> map = new LinkedHashMap<>();
        for (StationSource link : stationSourceRepository.findByStationIds(stationIds)) {
            map.computeIfAbsent(link.getId().getStationId(), k -> new ArrayList<>())
                    .add(StationSourceResponse.from(link));
        }
        return map;
    }
}
