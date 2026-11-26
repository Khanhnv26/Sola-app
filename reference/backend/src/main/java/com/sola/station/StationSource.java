package com.sola.station;

import com.sola.catalog.ContentSource;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "station_sources")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
