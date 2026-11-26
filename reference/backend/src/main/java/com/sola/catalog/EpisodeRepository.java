package com.sola.catalog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface EpisodeRepository extends JpaRepository<Episode, UUID> {

    @Query(
            value = """
                    select e from Episode e
                    join fetch e.source s
                    where s.visibility = 'CURATED_PUBLIC'
                      and (lower(e.title) like :pattern or lower(s.title) like :pattern)
                    """,
            countQuery = """
                    select count(e) from Episode e
                    join e.source s
                    where s.visibility = 'CURATED_PUBLIC'
                      and (lower(e.title) like :pattern or lower(s.title) like :pattern)
                    """
    )
    Page<Episode> searchPublicEpisodes(@Param("pattern") String pattern, Pageable pageable);
}
