package com.sola.station.entity;


import com.sola.catalog.entity.ContentSource;
import com.sola.station.entity.embedd.StationSourceId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "station_sources")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class StationSource {

    @EmbeddedId
    private StationSourceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("stationId")
    @JoinColumn(name = "station_id", nullable = false)
    private MoodStation station;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sourceId")
    @JoinColumn(name = "source_id", nullable = false)
    private ContentSource source;

    @Column(name = "priority_order", nullable = false)
    private int priorityOrder;
}
