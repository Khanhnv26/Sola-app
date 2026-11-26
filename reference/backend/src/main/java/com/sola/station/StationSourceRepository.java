package com.sola.station;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface StationSourceRepository extends JpaRepository<StationSource, StationSourceId> {

    @Query("""
            select ss from StationSource ss
            join fetch ss.source s
            where ss.id.stationId in :stationIds
            order by ss.priorityOrder asc
            """)
    List<StationSource> findByStationIds(@Param("stationIds") Collection<UUID> stationIds);
}
