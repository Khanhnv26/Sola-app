package com.sola.station;

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
@Table(name = "station_ambient_layers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StationAmbientLayer {

    @EmbeddedId
    private StationAmbientLayerId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("stationId")
    @JoinColumn(name = "station_id", nullable = false)
    private MoodStation station;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ambientSoundId")
    @JoinColumn(name = "ambient_sound_id", nullable = false)
    private AmbientSound ambientSound;

    @Column(name = "volume_percent", nullable = false)
    private int volumePercent;
}
