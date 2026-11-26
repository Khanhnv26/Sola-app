package com.sola.station.entity.embedd;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StationSourceId implements Serializable {

    @Column(name = "station_id")
    private UUID stationId;

    @Column(name = "source_id")
    private UUID sourceId;
}
