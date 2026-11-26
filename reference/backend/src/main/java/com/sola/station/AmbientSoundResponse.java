package com.sola.station;

import java.util.UUID;

public record AmbientSoundResponse(
        UUID id,
        String code,
        String name,
        String audioUrl
) {

    public static AmbientSoundResponse from(AmbientSound sound) {
        return new AmbientSoundResponse(
                sound.getId(),
                sound.getCode(),
                sound.getName(),
                sound.getAudioUrl()
        );
    }
}
