package com.sola.station;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MoodStationRepository extends JpaRepository<MoodStation, UUID>, JpaSpecificationExecutor<MoodStation> {

    @Override
    @EntityGraph(attributePaths = "owner")
    Page<MoodStation> findAll(Specification<MoodStation> spec, Pageable pageable);

    @EntityGraph(attributePaths = "owner")
    List<MoodStation> findByIsFeaturedTrueAndIsPublicTrueAndIsHiddenByAdminFalseOrderByPlayCountDescNameAsc();

    @EntityGraph(attributePaths = "owner")
    Optional<MoodStation> findBySlug(String slug);
}
