package com.sola.station.dto.response;

import com.sola.station.entity.StationAmbientLayer;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AmbientLayerResponse {
    private String code;
    private String name;
    private String audioUrl;
    private int volumePercent;

    public static AmbientLayerResponse fromEntity(StationAmbientLayer entity) {
        return AmbientLayerResponse.builder()
                .code(entity.getAmbientSound().getCode())
                .name(entity.getAmbientSound().getName())
                .audioUrl(entity.getAmbientSound().getAudioUrl())
                .volumePercent(entity.getVolumePercent())
                .build();
    }
}
