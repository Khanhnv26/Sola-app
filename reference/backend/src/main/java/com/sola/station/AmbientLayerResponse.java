package com.sola.station;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmbientLayerResponse {

    private String code;
    private String name;
    private String audioUrl;
    private int volumePercent;

    public static AmbientLayerResponse from(StationAmbientLayer layer) {
        AmbientSound sound = layer.getAmbientSound();
        return AmbientLayerResponse.builder()
                .code(sound.getCode())
                .name(sound.getName())
                .audioUrl(sound.getAudioUrl())
                .volumePercent(layer.getVolumePercent())
                .build();
    }
}
