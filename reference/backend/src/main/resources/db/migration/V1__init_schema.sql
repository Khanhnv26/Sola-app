CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1. Tài khoản người dùng (Khách nghe thử không lưu vào bảng này)
CREATE TABLE users (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name  VARCHAR(100) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    is_active     BOOLEAN      NOT NULL DEFAULT true,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- 2. Refresh Tokens (hỗ trợ xoay vòng token & đăng xuất thu hồi)
CREATE TABLE refresh_tokens (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash  VARCHAR(128) NOT NULL UNIQUE,
    expires_at  TIMESTAMPTZ NOT NULL,
    revoked_at  TIMESTAMPTZ,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);

-- 3. Nguồn phát thống nhất (Tuyển chọn công khai bởi Admin hoặc Riêng tư của User)
CREATE TABLE content_sources (
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    source_type          VARCHAR(20)  NOT NULL CHECK (source_type IN ('RADIO_STREAM', 'PODCAST_RSS')),
    visibility           VARCHAR(20)  NOT NULL DEFAULT 'USER_PRIVATE'
                         CHECK (visibility IN ('CURATED_PUBLIC', 'USER_PRIVATE')),
    owner_id             UUID         REFERENCES users(id) ON DELETE CASCADE,
    title                VARCHAR(255) NOT NULL,
    description          TEXT,
    cover_image_url      TEXT,
    source_url           TEXT         NOT NULL,
    stream_format        VARCHAR(20), -- MP3, AAC, HLS_M3U8, RSS_XML
    is_https             BOOLEAN      NOT NULL DEFAULT true,
    cors_compatible      BOOLEAN      NOT NULL DEFAULT false,
    category_tags        TEXT[]       NOT NULL DEFAULT '{}',

    -- Conditional GET phục vụ đồng bộ nguồn Podcast nhẹ băng thông
    etag                 VARCHAR(255),
    last_modified_header VARCHAR(255),
    last_synced_at       TIMESTAMPTZ,

    -- Theo dõi chất lượng kết nối + giãn cách kiểm tra lại (Exponential Backoff)
    health_status        VARCHAR(20)  NOT NULL DEFAULT 'UNKNOWN'
                         CHECK (health_status IN ('HEALTHY', 'DEGRADED', 'DOWN', 'UNKNOWN')),
    consecutive_failures INT          NOT NULL DEFAULT 0,
    last_checked_at      TIMESTAMPTZ,
    next_retry_at        TIMESTAMPTZ,
    last_latency_ms      INT,
    created_at           TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at           TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE UNIQUE INDEX uq_curated_source_url ON content_sources(source_url) WHERE visibility = 'CURATED_PUBLIC';
CREATE UNIQUE INDEX uq_user_private_source ON content_sources(owner_id, source_url) WHERE visibility = 'USER_PRIVATE';

-- 4. Lịch sử kiểm tra kết nối nguồn phát
CREATE TABLE source_health_logs (
    id            BIGSERIAL PRIMARY KEY,
    source_id     UUID        NOT NULL REFERENCES content_sources(id) ON DELETE CASCADE,
    checked_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    is_success    BOOLEAN     NOT NULL,
    http_status   INT,
    latency_ms    INT,
    error_code    VARCHAR(50), -- TIMEOUT, SSRF_BLOCKED, INVALID_XML, HTTP_ERROR, MIXED_CONTENT
    error_message TEXT
);
CREATE INDEX idx_health_logs_source_time ON source_health_logs(source_id, checked_at DESC);

-- 5. Các tập Podcast đồng bộ từ nguồn PODCAST_RSS
CREATE TABLE episodes (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    source_id        UUID         NOT NULL REFERENCES content_sources(id) ON DELETE CASCADE,
    guid             VARCHAR(500) NOT NULL,
    title            VARCHAR(500) NOT NULL,
    summary          TEXT,
    audio_url        TEXT         NOT NULL,
    image_url        TEXT,
    duration_seconds INT,
    published_at     TIMESTAMPTZ,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now(),
    UNIQUE (source_id, guid)
);
CREATE INDEX idx_episodes_source_pub ON episodes(source_id, published_at DESC);

-- 6. Thư viện lớp âm thanh nền mặc định
CREATE TABLE ambient_sounds (
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code      VARCHAR(50)  NOT NULL UNIQUE, -- RAIN, CAFE, DRONE, FIREPLACE
    name      VARCHAR(100) NOT NULL,
    audio_url TEXT,
    is_active BOOLEAN      NOT NULL DEFAULT true
);

-- 7. Kênh nhạc & Không gian nghe (Tuyển chọn, Cá nhân & Cộng đồng)
CREATE TABLE mood_stations (
    id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id              UUID         REFERENCES users(id) ON DELETE CASCADE,
    slug                  VARCHAR(140) NOT NULL UNIQUE,
    name                  VARCHAR(150) NOT NULL,
    description           TEXT,
    mood_tag              VARCHAR(40)  NOT NULL, -- LOFI, DEEP_WORK, CODING, PODCAST
    default_focus_minutes INT          NOT NULL DEFAULT 25,
    default_sleep_minutes INT,
    theme_key             VARCHAR(40)  NOT NULL DEFAULT 'solar',
    is_public             BOOLEAN      NOT NULL DEFAULT false,
    is_featured           BOOLEAN      NOT NULL DEFAULT false,
    is_hidden_by_admin    BOOLEAN      NOT NULL DEFAULT false,
    hidden_reason         VARCHAR(255),
    play_count            BIGINT       NOT NULL DEFAULT 0,
    created_at            TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at            TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE INDEX idx_stations_discovery ON mood_stations(is_featured, is_public, is_hidden_by_admin);

-- 8. Nguồn phát gắn vào Kênh
CREATE TABLE station_sources (
    station_id     UUID NOT NULL REFERENCES mood_stations(id) ON DELETE CASCADE,
    source_id      UUID NOT NULL REFERENCES content_sources(id) ON DELETE CASCADE,
    priority_order INT  NOT NULL DEFAULT 1,
    PRIMARY KEY (station_id, source_id)
);

-- 9. Tỷ lệ trộn âm thanh nền của từng Kênh
CREATE TABLE station_ambient_layers (
    station_id       UUID NOT NULL REFERENCES mood_stations(id) ON DELETE CASCADE,
    ambient_sound_id UUID NOT NULL REFERENCES ambient_sounds(id) ON DELETE CASCADE,
    volume_percent   INT  NOT NULL CHECK (volume_percent BETWEEN 0 AND 100),
    PRIMARY KEY (station_id, ambient_sound_id)
);

-- 10. Tiến độ nghe tập Podcast
CREATE TABLE playback_progress (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id          UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    episode_id       UUID        NOT NULL REFERENCES episodes(id) ON DELETE CASCADE,
    position_seconds INT         NOT NULL DEFAULT 0 CHECK (position_seconds >= 0),
    duration_seconds INT,
    status           VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS'
                     CHECK (status IN ('IN_PROGRESS', 'COMPLETED')),
    last_played_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (user_id, episode_id)
);

-- 11. Danh sách chờ phát Podcast
CREATE TABLE queue_items (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    episode_id UUID        NOT NULL REFERENCES episodes(id) ON DELETE CASCADE,
    sort_order INT         NOT NULL,
    added_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (user_id, episode_id)
);

-- 12. Nhật ký phiên nghe & Chuỗi ngày tập trung
CREATE TABLE listening_sessions (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id          UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    station_id       UUID        REFERENCES mood_stations(id) ON DELETE SET NULL,
    source_id        UUID        REFERENCES content_sources(id) ON DELETE SET NULL,
    episode_id       UUID        REFERENCES episodes(id) ON DELETE SET NULL,
    session_type     VARCHAR(20) NOT NULL CHECK (session_type IN ('RADIO', 'PODCAST', 'AMBIENT_ONLY')),
    focus_mode       BOOLEAN     NOT NULL DEFAULT false,
    listened_seconds INT         NOT NULL DEFAULT 0 CHECK (listened_seconds >= 0),
    started_at       TIMESTAMPTZ NOT NULL,
    ended_at         TIMESTAMPTZ NOT NULL,
    UNIQUE (user_id, started_at, ended_at)
);
CREATE INDEX idx_sessions_user_started ON listening_sessions(user_id, started_at DESC);
