-- Dữ liệu mẫu khởi tạo cho Sola (Chặng 0–1)

-- 1. Người dùng mẫu (mật khẩu mã hóa BCrypt tương thích Spring Security)
INSERT INTO users (email, password_hash, display_name, role) VALUES
    ('admin@sola.local', crypt('Admin@123', gen_salt('bf', 10)), 'Quản trị viên', 'ADMIN'),
    ('minh.nguyen@example.com', crypt('User@123', gen_salt('bf', 10)), 'Minh Nguyễn', 'USER'),
    ('lan.anh@example.com', crypt('User@123', gen_salt('bf', 10)), 'Lan Anh', 'USER');

-- 2. Thư viện âm thanh nền mặc định (FIREPLACE tạm tắt để test lọc is_active = true)
INSERT INTO ambient_sounds (code, name, audio_url, is_active) VALUES
    ('RAIN', 'Tiếng mưa nhẹ', '/sounds/rain.mp3', true),
    ('CAFE', 'Quán cafe ấm', '/sounds/cafe.mp3', true),
    ('DRONE', 'Âm nền dịu', '/sounds/drone.mp3', true),
    ('FIREPLACE', 'Lò sưởi', '/sounds/fireplace.mp3', false);

-- 3. Nguồn phát công khai (CURATED_PUBLIC)
INSERT INTO content_sources (
    source_type, visibility, owner_id, title, description, source_url, stream_format,
    is_https, cors_compatible, category_tags, health_status, consecutive_failures, last_checked_at, last_latency_ms
) VALUES
    ('RADIO_STREAM', 'CURATED_PUBLIC', NULL, 'Chillhop FM',
     'Nhạc lo-fi và chillhop nhẹ nhàng phát trực tiếp.',
     'https://streams.fluxfm.de/chillhop/mp3-320', 'MP3',
     true, true, ARRAY['LOFI', 'CHILL'], 'HEALTHY', 0, now() - interval '5 minutes', 38),

    ('RADIO_STREAM', 'CURATED_PUBLIC', NULL, 'Lofi Beats FM',
     'Giai điệu chậm đều giúp giữ tập trung sâu.',
     'https://stream.sola.example/lofi-beats.mp3', 'MP3',
     true, true, ARRAY['LOFI', 'DEEP_WORK'], 'HEALTHY', 0, now() - interval '5 minutes', 62),

    ('PODCAST_RSS', 'CURATED_PUBLIC', NULL, 'The Present Writer',
     'Podcast về chủ nghĩa tối giản, phát triển bản thân và thói quen tích cực.',
     'https://anchor.fm/s/1351475c/podcast/rss', 'RSS_XML',
     true, false, ARRAY['PODCAST', 'MINDFULNESS'], 'HEALTHY', 0, now() - interval '10 minutes', 108),

    ('RADIO_STREAM', 'CURATED_PUBLIC', NULL, 'Acoustic Dawn FM',
     'Guitar acoustic mộc mạc cho buổi sáng thư thái.',
     'https://stream.sola.example/acoustic-dawn.mp3', 'MP3',
     true, true, ARRAY['LOFI', 'ACOUSTIC'], 'HEALTHY', 0, now() - interval '5 minutes', 54),

    ('PODCAST_RSS', 'CURATED_PUBLIC', NULL, 'Radio Chậm Lại',
     'Những câu chuyện nhỏ và tản văn cuối ngày.',
     'https://feeds.sola.example/radio-cham-lai.xml', 'RSS_XML',
     true, false, ARRAY['PODCAST', 'STORY'], 'HEALTHY', 0, now() - interval '15 minutes', 95),

    ('RADIO_STREAM', 'CURATED_PUBLIC', NULL, 'Sola Synth FM',
     'Âm thanh synthwave êm dịu cho buổi làm việc yên tĩnh.',
     'https://stream.sola.example/synth.mp3', 'MP3',
     true, true, ARRAY['CODING', 'SYNTH'], 'HEALTHY', 0, now() - interval '5 minutes', 49),

    ('RADIO_STREAM', 'CURATED_PUBLIC', NULL, 'VOV Giao Thông Trực Tiếp',
     'Kênh phát thanh trực tiếp tổng hợp.',
     'https://stream.vov.example/live.m3u8', 'HLS_M3U8',
     true, false, ARRAY['RADIO'], 'DEGRADED', 1, now() - interval '5 minutes', 440),

    ('PODCAST_RSS', 'CURATED_PUBLIC', NULL, 'Góc Đọc Sách Đêm',
     'Kênh đọc sách đêm (nguồn mẫu đang gián đoạn để test giám sát).',
     'https://feeds.legacy.example/rss.xml', 'RSS_XML',
     true, false, ARRAY['PODCAST'], 'DOWN', 3, now() - interval '20 minutes', NULL);

-- Nguồn phát riêng tư của Minh Nguyễn (không được phép lộ ra API công khai)
INSERT INTO content_sources (
    source_type, visibility, owner_id, title, description, source_url, stream_format,
    is_https, cors_compatible, category_tags, health_status
) VALUES
    ('PODCAST_RSS', 'USER_PRIVATE',
     (SELECT id FROM users WHERE email = 'minh.nguyen@example.com'),
     'Ghi chú & Podcast cá nhân',
     'Nguồn podcast cá nhân chỉ dành cho chủ sở hữu.',
     'https://kenh-rieng.example/feed', 'RSS_XML',
     true, false, ARRAY['PODCAST'], 'UNKNOWN');

-- 4. Tập Podcast mẫu
INSERT INTO episodes (source_id, guid, title, summary, audio_url, duration_seconds, published_at) VALUES
    ((SELECT id FROM content_sources WHERE source_url = 'https://anchor.fm/s/1351475c/podcast/rss'),
     'tpw-001', 'Tập Trung Giữa Thành Phố Ồn Ào',
     'Cách tạo khoảng lặng cho trí óc giữa nhịp sống bận rộn.',
     'https://media.sola.example/episodes/tpw-001.mp3', 1380, now() - interval '6 days'),

    ((SELECT id FROM content_sources WHERE source_url = 'https://anchor.fm/s/1351475c/podcast/rss'),
     'tpw-002', 'Giữ Nhịp Cân Bằng Mỗi Ngày',
     'Xây dựng thói quen nhỏ giúp duy trì sự bình tĩnh và bền bỉ.',
     'https://media.sola.example/episodes/tpw-002.mp3', 1920, now() - interval '4 days'),

    ((SELECT id FROM content_sources WHERE source_url = 'https://feeds.sola.example/radio-cham-lai.xml'),
     'rcl-001', 'Ngủ Ngon Sau Ngày Dài Làm Việc',
     'Thả lỏng suy nghĩ trước giờ nghỉ ngơi.',
     'https://media.sola.example/episodes/rcl-001.mp3', 1200, now() - interval '3 days'),

    ((SELECT id FROM content_sources WHERE source_url = 'https://feeds.sola.example/radio-cham-lai.xml'),
     'rcl-002', 'Nghệ Thuật Sắp Xếp Thời Gian Buổi Sáng',
     'Bắt đầu ngày mới nhẹ nhàng mà vẫn hiệu quả.',
     'https://media.sola.example/episodes/rcl-002.mp3', 1500, now() - interval '1 day'),

    ((SELECT id FROM content_sources WHERE source_url = 'https://kenh-rieng.example/feed'),
     'priv-001', 'Ghi chú riêng tuần 47',
     'Bản ghi âm nội bộ của Minh, không hiển thị công khai.',
     'https://media.sola.example/episodes/priv-001.mp3', 600, now() - interval '2 days');

-- 5. Kênh nhạc & Không gian nghe (6 kênh công khai)
INSERT INTO mood_stations (
    owner_id, slug, name, description, mood_tag, default_focus_minutes,
    theme_key, is_public, is_featured, is_hidden_by_admin, play_count
) VALUES
    (NULL, 'solar-morning', 'Solar Morning',
     'Nhịp lo-fi sáng rõ kết hợp tiếng mưa dịu nhẹ.',
     'LOFI', 25, 'solar', true, true, false, 1280),

    (NULL, 'golden-rain', 'Golden Rain',
     'Nhịp trầm ổn định cho giờ tập trung dài.',
     'DEEP_WORK', 50, 'solar', true, true, false, 1140),

    (NULL, 'bright-study', 'Bright Study',
     'Nghe podcast truyền cảm hứng cùng nền quán cafe.',
     'PODCAST', 30, 'solar', true, true, false, 960),

    (NULL, 'som-mai-acoustic', 'Sớm Mai Acoustic',
     'Guitar mộc nhẹ nhàng cho giờ đọc sách buổi sáng.',
     'LOFI', 35, 'solar', true, true, false, 820),

    ((SELECT id FROM users WHERE email = 'lan.anh@example.com'),
     'ca-phe-cuoi-chieu', 'Cà Phê Cuối Chiều',
     'Tản văn thư giãn cuối ngày với không khí quán quen.',
     'PODCAST', 40, 'solar', true, false, false, 540),

    ((SELECT id FROM users WHERE email = 'minh.nguyen@example.com'),
     'late-night-sprint', 'Late Night Sprint',
     'Kênh gợi ý cộng đồng cho những buổi làm việc yên tĩnh.',
     'CODING', 45, 'solar', true, false, false, 430);

-- 6. Gắn nguồn phát vào từng kênh
INSERT INTO station_sources (station_id, source_id, priority_order) VALUES
    ((SELECT id FROM mood_stations WHERE slug = 'solar-morning'),
     (SELECT id FROM content_sources WHERE source_url = 'https://streams.fluxfm.de/chillhop/mp3-320'), 1),
    ((SELECT id FROM mood_stations WHERE slug = 'golden-rain'),
     (SELECT id FROM content_sources WHERE source_url = 'https://stream.sola.example/lofi-beats.mp3'), 1),
    ((SELECT id FROM mood_stations WHERE slug = 'bright-study'),
     (SELECT id FROM content_sources WHERE source_url = 'https://anchor.fm/s/1351475c/podcast/rss'), 1),
    ((SELECT id FROM mood_stations WHERE slug = 'som-mai-acoustic'),
     (SELECT id FROM content_sources WHERE source_url = 'https://stream.sola.example/acoustic-dawn.mp3'), 1),
    ((SELECT id FROM mood_stations WHERE slug = 'ca-phe-cuoi-chieu'),
     (SELECT id FROM content_sources WHERE source_url = 'https://feeds.sola.example/radio-cham-lai.xml'), 1),
    ((SELECT id FROM mood_stations WHERE slug = 'late-night-sprint'),
     (SELECT id FROM content_sources WHERE source_url = 'https://stream.sola.example/synth.mp3'), 1);

-- 7. Tỷ lệ trộn 3 lớp âm thanh nền mặc định cho từng kênh
INSERT INTO station_ambient_layers (station_id, ambient_sound_id, volume_percent) VALUES
    ((SELECT id FROM mood_stations WHERE slug = 'solar-morning'), (SELECT id FROM ambient_sounds WHERE code = 'RAIN'), 40),
    ((SELECT id FROM mood_stations WHERE slug = 'solar-morning'), (SELECT id FROM ambient_sounds WHERE code = 'CAFE'), 18),
    ((SELECT id FROM mood_stations WHERE slug = 'solar-morning'), (SELECT id FROM ambient_sounds WHERE code = 'DRONE'), 26),

    ((SELECT id FROM mood_stations WHERE slug = 'golden-rain'), (SELECT id FROM ambient_sounds WHERE code = 'RAIN'), 56),
    ((SELECT id FROM mood_stations WHERE slug = 'golden-rain'), (SELECT id FROM ambient_sounds WHERE code = 'CAFE'), 10),
    ((SELECT id FROM mood_stations WHERE slug = 'golden-rain'), (SELECT id FROM ambient_sounds WHERE code = 'DRONE'), 30),

    ((SELECT id FROM mood_stations WHERE slug = 'bright-study'), (SELECT id FROM ambient_sounds WHERE code = 'RAIN'), 16),
    ((SELECT id FROM mood_stations WHERE slug = 'bright-study'), (SELECT id FROM ambient_sounds WHERE code = 'CAFE'), 30),
    ((SELECT id FROM mood_stations WHERE slug = 'bright-study'), (SELECT id FROM ambient_sounds WHERE code = 'DRONE'), 14),

    ((SELECT id FROM mood_stations WHERE slug = 'som-mai-acoustic'), (SELECT id FROM ambient_sounds WHERE code = 'RAIN'), 20),
    ((SELECT id FROM mood_stations WHERE slug = 'som-mai-acoustic'), (SELECT id FROM ambient_sounds WHERE code = 'CAFE'), 24),
    ((SELECT id FROM mood_stations WHERE slug = 'som-mai-acoustic'), (SELECT id FROM ambient_sounds WHERE code = 'DRONE'), 18),

    ((SELECT id FROM mood_stations WHERE slug = 'ca-phe-cuoi-chieu'), (SELECT id FROM ambient_sounds WHERE code = 'RAIN'), 14),
    ((SELECT id FROM mood_stations WHERE slug = 'ca-phe-cuoi-chieu'), (SELECT id FROM ambient_sounds WHERE code = 'CAFE'), 42),
    ((SELECT id FROM mood_stations WHERE slug = 'ca-phe-cuoi-chieu'), (SELECT id FROM ambient_sounds WHERE code = 'DRONE'), 16),

    ((SELECT id FROM mood_stations WHERE slug = 'late-night-sprint'), (SELECT id FROM ambient_sounds WHERE code = 'RAIN'), 22),
    ((SELECT id FROM mood_stations WHERE slug = 'late-night-sprint'), (SELECT id FROM ambient_sounds WHERE code = 'CAFE'), 8),
    ((SELECT id FROM mood_stations WHERE slug = 'late-night-sprint'), (SELECT id FROM ambient_sounds WHERE code = 'DRONE'), 38);
