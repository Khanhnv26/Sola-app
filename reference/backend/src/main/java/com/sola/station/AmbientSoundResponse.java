package com.sola.station;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmbientSoundResponse {

    private UUID id;
    private String code;
    private String name;
    private String audioUrl;

    public static AmbientSoundResponse from(AmbientSound sound) {
        return AmbientSoundResponse.builder()
                .id(sound.getId())
                .code(sound.getCode())
                .name(sound.getName())
                .audioUrl(sound.getAudioUrl())
                .build();
    }
}
