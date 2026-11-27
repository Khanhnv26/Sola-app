package com.sola.station.repository;

import com.sola.station.entity.MoodStation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MoodStationRepository extends JpaRepository<MoodStation, UUID>, JpaSpecificationExecutor<MoodStation> {

    @Override
    @EntityGraph(attributePaths = "owner")
    Page<MoodStation> findAll(Specification<MoodStation> spec, Pageable pageable);


    @EntityGraph(attributePaths = "owner")
    @Query("SELECT m FROM MoodStation m WHERE m.isFeatured = true AND m.isPublic = true " +
            "AND m.isHiddenByAdmin = false " +
            "ORDER BY m.playCount DESC, m.name asc")
    List<MoodStation> getMoodStationByFeaturedAndPublic();

    @EntityGraph(attributePaths = "owner")
    Optional<MoodStation> findBySlug(String slug);


}
