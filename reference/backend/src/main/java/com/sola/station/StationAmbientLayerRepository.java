package com.sola.station;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface StationAmbientLayerRepository extends JpaRepository<StationAmbientLayer, StationAmbientLayerId> {

    @Query("""
            select l from StationAmbientLayer l
            join fetch l.ambientSound a
            where l.id.stationId in :stationIds
              and a.isActive = true
            order by a.code asc
            """)
    List<StationAmbientLayer> findByStationIds(@Param("stationIds") Collection<UUID> stationIds);
}
