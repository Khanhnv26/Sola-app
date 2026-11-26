package com.sola.station;

import com.sola.identity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "mood_stations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(columnDefinition = "text")
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

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
