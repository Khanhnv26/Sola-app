package com.sola.station;

public record AmbientLayerResponse(
        String code,
        String name,
        String audioUrl,
        int volumePercent
) {

    public static AmbientLayerResponse from(StationAmbientLayer layer) {
        AmbientSound sound = layer.getAmbientSound();
        return new AmbientLayerResponse(
                sound.getCode(),
                sound.getName(),
                sound.getAudioUrl(),
                layer.getVolumePercent()
        );
    }
}
