package com.sola.station.repository;

import com.sola.station.entity.StationAmbientLayer;
import com.sola.station.entity.embedd.StationAmbientLayerId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
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
