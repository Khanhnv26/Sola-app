package com.sola.station.entity;

import com.sola.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "mood_stations")
@Entity
public class MoodStation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(nullable = false, unique = true, length = 140)
    private String slug;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "mood_tag", nullable = false, length = 40)
    private String moodTag;

    @Column(name = "default_focus_minutes", nullable = false)
    private int defaultFocusMinutes;

    @Column(name = "default_sleep_minutes")
    private Integer defaultSleepMinutes;

    @Column(name = "theme_key", nullable = false, length = 40)
    private String themeKey;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "is_featured", nullable = false)
    private boolean isFeatured;

    @Column(name = "is_hidden_by_admin", nullable = false)
    private boolean isHiddenByAdmin;

    @Column(name = "hidden_reason", length = 255)
    private String hiddenReason;

    @Column(name = "play_count", nullable = false)
    private long playCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

}
