package com.sola.catalog;

import com.sola.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EpisodeQueryService {

    private final EpisodeRepository episodeRepository;

    public PageResponse<EpisodeResponse> searchPublicEpisodes(String q, int page, int size) {
        String pattern = (q == null || q.isBlank())
                ? "%"
                : "%" + q.trim().toLowerCase(Locale.ROOT) + "%";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<Episode> result = episodeRepository.searchPublicEpisodes(pattern, pageable);
        List<EpisodeResponse> items = result.getContent().stream()
                .map(EpisodeResponse::from)
                .toList();
        return PageResponse.from(result, items);
    }
}
