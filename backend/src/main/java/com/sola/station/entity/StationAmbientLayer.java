package com.sola.station.entity;


import com.sola.station.entity.embedd.StationAmbientLayerId;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "station_ambient_layers")
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
