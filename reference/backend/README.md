# Sola Backend Reference (Chặng 0–1)

> **Lưu ý quan trọng:** Hãy tự viết code trong thư mục [`backend/`](../../backend/README.md) trước, chạy thử trên cổng `8080`, rồi mới mở các file trong `reference/backend/` ra đối chiếu từng phần. Đừng copy nguyên xi vì sẽ mất cơ hội luyện JPA, Specification và xử lý lỗi chuẩn Spring Boot.

---

## Cách chạy bộ đối soát

1. Đảm bảo đã tạo database `sola_ref` trên PostgreSQL local (`localhost:5432`) theo hướng dẫn ở [../../README.md](../../README.md) (chạy [../../scripts/init-local-db.sql](../../scripts/init-local-db.sql) hoặc truyền `DB_USERNAME` / `DB_PASSWORD`).
2. Chạy server đối soát (cổng **`8081`**, dùng riêng DB **`sola_ref`** tách biệt hoàn toàn với `sola`):
   ```powershell
   cd reference/backend
   mvn spring-boot:run
   ```
3. Mở Swagger UI: http://localhost:8081/swagger-ui.html

---

## Bảng đối soát: File -> Điểm kỹ thuật cần kiểm tra

| File trong `reference/backend/` | Điểm cần đối soát khi bạn tự code ở `backend/` |
| :--- | :--- |
| `common/ApiResponse.java` | Khung JSON phản hồi thống nhất (`status`, `code`, `message`, `data`, `timestamp`) bằng Java `record`. |
| `common/PageResponse.java` | Bọc dữ liệu phân trang (`content`, `page`, `size`, `totalElements`, `totalPages`, `last`), không trả thẳng `Page` của Spring ra ngoài. |
| `common/GlobalExceptionHandler.java` | Bắt tập trung `AppException`, lỗi validate bean + tham số query (`HandlerMethodValidationException` của Spring Boot 3.2+ / Spring 6.1+), `404 NoResourceFoundException`, và `500` không lộ stack trace ra JSON. |
| `common/CorsConfig.java` | Cấu hình CORS cho `/api/**` nhận nguồn từ `application.properties`. |
| `catalog/ContentSource.java` | Ánh xạ cột `TEXT` (`columnDefinition = "text"`) và cột mảng PostgreSQL `text[]` bằng `@JdbcTypeCode(SqlTypes.ARRAY)` để pass `hibernate.ddl-auto: validate`. |
| `catalog/EpisodeRepository.java` | Mẹo truyền `pattern = "%"` khi `q` rỗng thay vì truyền `null` vào `LIKE` (tránh lỗi PostgreSQL *"could not determine data type of parameter"*), kèm `countQuery` tách riêng khi dùng `join fetch`. |
| `station/StationSource.java` & `StationAmbientLayer.java` | Khóa chính kép `@EmbeddedId` kết hợp `@MapsId` cho bảng trung gian có cột phụ (`priority_order`, `volume_percent`). |
| `station/MoodStationRepository.java` | Dùng `@EntityGraph(attributePaths = "owner")` lấy luôn người tạo kênh, tránh N+1 khi truy cập `station.getOwner().getDisplayName()` khi `spring.jpa.open-in-view: false`. |
| `station/StationQueryService.java` | **Chống N+1 bằng truy vấn theo lô:** gom toàn bộ `stationIds`, chỉ bắn 1 query lấy `layers` và 1 query lấy `sources` rồi ghép bằng `Map<UUID, List<...>>`. Ngân sách query: `/featured` `<= 3` câu SQL `SELECT`, `/discover` `<= 4` câu SQL `SELECT`. Dùng `Specification` cho bộ lọc động. |
| `station/PublicStationController.java` | Validate `@Min(0)` cho `page`, `@Min(1) @Max(50)` cho `size` bằng `@Validated`. |
| `station/StationSummaryResponse.java` & `StationDetailResponse.java` | Tách biệt hoàn toàn DTO (`record`) và Entity — không trả Entity ra Controller. |

---

## Lệnh `curl.exe` kiểm tra & kết quả mong đợi

*(Chạy trên PowerShell Windows với `curl.exe`, khi server đối soát đang chạy ở cổng `8081`)*

1. **Danh sách âm thanh nền đang bật (chỉ trả 3 mục, không có `FIREPLACE`):**
   ```powershell
   curl.exe -s http://localhost:8081/api/v1/public/ambient-sounds
   ```
   -> HTTP `200`, `code = "SUCCESS"`, `data` có 3 phần tử (`CAFE`, `DRONE`, `RAIN`).

2. **Kênh tuyển chọn nổi bật:**
   ```powershell
   curl.exe -s http://localhost:8081/api/v1/public/stations/featured
   ```
   -> HTTP `200`, `data` có 4 kênh (`Solar Morning`, `Golden Rain`, `Bright Study`, `Sớm Mai Acoustic`).

3. **Khám phá toàn bộ kênh công khai:**
   ```powershell
   curl.exe -s "http://localhost:8081/api/v1/public/stations/discover"
   ```
   -> HTTP `200`, `data.totalElements = 6`.

4. **Lọc theo nhóm `FOCUS` (gồm `DEEP_WORK` và `CODING`):**
   ```powershell
   curl.exe -s "http://localhost:8081/api/v1/public/stations/discover?category=FOCUS"
   ```
   -> HTTP `200`, `data.totalElements = 2` (`Golden Rain`, `Late Night Sprint`).

5. **Tìm kiếm theo từ khóa `q`:**
   ```powershell
   curl.exe -s "http://localhost:8081/api/v1/public/stations/discover?q=golden"
   ```
   -> HTTP `200`, `data.totalElements = 1` (`Golden Rain`).

6. **Kiểm tra lỗi `category` không hợp lệ:**
   ```powershell
   curl.exe -i -s "http://localhost:8081/api/v1/public/stations/discover?category=abc"
   ```
   -> HTTP `400`, `code = "VALIDATION_ERROR"`.

7. **Kiểm tra giới hạn `size` tối đa 50:**
   ```powershell
   curl.exe -i -s "http://localhost:8081/api/v1/public/stations/discover?size=500"
   ```
   -> HTTP `400`, `code = "VALIDATION_ERROR"`.

8. **Chi tiết một kênh theo `slug`:**
   ```powershell
   curl.exe -s "http://localhost:8081/api/v1/public/stations/solar-morning"
   ```
   -> HTTP `200`, `sources` có 1 nguồn (`Chillhop FM`), `ambientLayers` có 3 lớp (`RAIN 40%`, `CAFE 18%`, `DRONE 26%`).

9. **Kênh không tồn tại:**
   ```powershell
   curl.exe -i -s "http://localhost:8081/api/v1/public/stations/khong-ton-tai"
   ```
   -> HTTP `404`, `code = "STATION_NOT_FOUND"`.

10. **Danh sách tập Podcast công khai (không lộ tập từ nguồn `USER_PRIVATE`):**
    ```powershell
    curl.exe -s "http://localhost:8081/api/v1/public/episodes"
    ```
    -> HTTP `200`, `data.totalElements = 4` (không chứa *"Ghi chú riêng tuần 47"*).

11. **Tìm tập Podcast theo tên show hoặc tiêu đề:**
    ```powershell
    curl.exe -s "http://localhost:8081/api/v1/public/episodes?q=present"
    ```
    -> HTTP `200`, `data.totalElements = 2`.
