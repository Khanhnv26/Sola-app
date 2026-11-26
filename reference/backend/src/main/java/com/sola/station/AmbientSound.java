package com.sola.station;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "ambient_sounds")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AmbientSound {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "audio_url", columnDefinition = "text")
    private String audioUrl;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
