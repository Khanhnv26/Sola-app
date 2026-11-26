package com.sola.catalog.entity;


import com.sola.catalog.enums.HealthStatus;
import com.sola.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "content_sources")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class ContentSource {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "source_type", nullable = false, length = 20)
    private String sourceType;

    @Column(nullable = false, length = 20)
    private String visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url", columnDefinition = "TEXT")
    private String coverImageUrl;

    @Column(name = "source_url", nullable = false, columnDefinition = "TEXT")
    private String sourceUrl;

    @Column(name = "stream_format", length = 20)
    private String streamFormat;

    @Column(name = "is_https", nullable = false)
    private boolean isHttps;

    @Column(name = "cors_compatible", nullable = false)
    private boolean corsCompatible;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "category_tags", nullable = false, columnDefinition = "TEXT[]")
    private String[] categoryTags;

    @Column(length = 255)
    private String etag;

    @Column(name = "last_modified_header", length = 255)
    private String lastModifiedHeader;

    @Column(name = "last_synced_at")
    private OffsetDateTime lastSyncedAt;

    @Column(name = "health_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private HealthStatus healthStatus;

    @Column(name = "consecutive_failures", nullable = false)
    private int consecutiveFailures;

    @Column(name = "last_checked_at")
    private OffsetDateTime lastCheckedAt;

    @Column(name = "next_retry_at")
    private OffsetDateTime nextRetryAt;

    @Column(name = "last_latency_ms")
    private Integer lastLatencyMs;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
