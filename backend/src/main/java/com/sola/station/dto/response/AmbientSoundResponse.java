package com.sola.station.dto.response;

import com.sola.station.entity.AmbientSound;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AmbientSoundResponse {
    private UUID id;
    private String code;
    private String name;
    private String audioUrl;

    public static AmbientSoundResponse from(AmbientSound ambientSound) {
        return AmbientSoundResponse.builder()
                .id(ambientSound.getId())
                .code(ambientSound.getCode())
                .name(ambientSound.getName())
                .audioUrl(ambientSound.getAudioUrl())
                .build();
    }
}
