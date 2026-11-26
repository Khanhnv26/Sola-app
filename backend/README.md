# Sola Backend — Checklist Tự Code (Bước 0 & Bước 1)

Đây là không gian **bạn tự viết code** (chạy cổng `8080`, kết nối PostgreSQL local `localhost:5432`, DB `sola`).
Hiện dự án đã dựng sẵn `pom.xml`, cấu hình `application.properties`, `SolaApplication.java` và Flyway migration (`V1`, `V2`).

---

## Bước 0: Hạ tầng dùng chung (`com.sola.common`)

- [ ] Tạo `ErrorCode` (enum chứa `HttpStatus` + `message` tiếng Việt mặc định: `SUCCESS`, `VALIDATION_ERROR`, `NOT_FOUND`, `STATION_NOT_FOUND`, `INTERNAL_ERROR`).
- [ ] Tạo `ApiResponse<T>` (record gồm `status`, `code`, `message`, `data`, `timestamp`) theo chuẩn mục 7 của `../docs/sola-architecture-spec.md`.
- [ ] Tạo `PageResponse<T>` (record gồm `content`, `page`, `size`, `totalElements`, `totalPages`, `last`).
- [ ] Tạo `AppException` kế thừa `RuntimeException` mang theo `ErrorCode`.
- [ ] Tạo `GlobalExceptionHandler` (`@RestControllerAdvice`) bắt:
  - `AppException`
  - Lỗi validate (`MethodArgumentNotValidException`, `ConstraintViolationException`, `HandlerMethodValidationException`, `MethodArgumentTypeMismatchException`) -> trả `400`
  - `NoResourceFoundException` -> trả `404`
  - `Exception` -> log stack trace và trả `500` với thông điệp chung chung (không lộ chi tiết nội bộ).
- [ ] Tạo `CorsConfig` cho phép `http://localhost:5173` gọi `/api/**`.
- [ ] Tạo `OpenApiConfig` cấu hình thông tin Swagger UI.

---

## Bước 1: 5 API Công khai (`/api/v1/public/**`)

### 1.1. Ánh xạ Entity khớp tuyệt đối với `V1__init_schema.sql`
*(Lưu ý `spring.jpa.hibernate.ddl-auto: validate` đang bật — sai kiểu cột ứng dụng sẽ không khởi động được)*

- [ ] `com.sola.identity.User` -> bảng `users` (tối thiểu các cột đọc được cho quan hệ `owner`).
- [ ] `com.sola.catalog.ContentSource` -> bảng `content_sources` (chú ý cột `TEXT` dùng `columnDefinition = "text"`, cột `category_tags` kiểu `text[]` ánh xạ bằng `@JdbcTypeCode(SqlTypes.ARRAY)`).
- [ ] `com.sola.catalog.Episode` -> bảng `episodes` (`@ManyToOne(fetch = FetchType.LAZY)` tới `ContentSource`).
- [ ] `com.sola.station.AmbientSound` -> bảng `ambient_sounds`.
- [ ] `com.sola.station.MoodStation` -> bảng `mood_stations`.
- [ ] `com.sola.station.StationSource` & `StationSourceId` -> bảng `station_sources` (khóa chính kép `@EmbeddedId` + `@MapsId`).
- [ ] `com.sola.station.StationAmbientLayer` & `StationAmbientLayerId` -> bảng `station_ambient_layers` (`@EmbeddedId` + `@MapsId`).

### 1.2. Repository, Service & DTO tách biệt khỏi Entity
- [ ] `GET /api/v1/public/ambient-sounds`: chỉ lấy các âm thanh có `is_active = true`, sắp theo `code` tăng dần.
- [ ] `GET /api/v1/public/stations/featured`: lấy kênh có `is_featured = true`, `is_public = true`, `is_hidden_by_admin = false`. **Chú ý chống lỗi N+1 Query** (gom danh sách `stationId` để query theo lô `layers` và `sources` — tổng số câu SQL `SELECT` phải `<= 3`).
- [ ] `GET /api/v1/public/stations/discover?category=&q=&page=0&size=12`:
  - Validate `page >= 0` và `1 <= size <= 50`.
  - Ánh xạ `category`: `ALL`/rỗng -> tất cả; `FOCUS` -> `DEEP_WORK` hoặc `CODING`; tag khác hợp lệ (`LOFI`, `DEEP_WORK`, `CODING`, `PODCAST`); sai tag -> trả `400`.
  - Dùng JPA `Specification` lọc động theo `category` và `q` (tìm theo tên hoặc mô tả, không phân biệt hoa thường), sắp xếp `isFeatured desc, playCount desc, name asc`.
- [ ] `GET /api/v1/public/stations/{slug}`: trả chi tiết kênh (gồm danh sách nguồn phát và lớp âm nền). Nếu không thấy hoặc kênh bị ẩn/riêng tư -> trả `404` (`STATION_NOT_FOUND`).
- [ ] `GET /api/v1/public/episodes?q=&page=0&size=20`: chỉ trả tập của nguồn `CURATED_PUBLIC`, không làm rò rỉ tập của nguồn `USER_PRIVATE`. Sắp theo `published_at desc`.

---

## Kiểm tra & Đối soát

1. Tạo sẵn database `sola` trên PostgreSQL local theo hướng dẫn ở [../README.md](../README.md) (chạy [../scripts/init-local-db.sql](../scripts/init-local-db.sql) hoặc truyền `DB_USERNAME` / `DB_PASSWORD`).
2. Chạy `mvn spring-boot:run` trong thư mục `backend/`.
3. Mở Swagger UI tại `http://localhost:8080/swagger-ui.html` để tự test từng endpoint.
4. Sau khi chạy thông, đối chiếu code, số lượng câu SQL trong log và kết quả JSON với `../reference/backend/` (chạy cổng `8081`). Xem hướng dẫn chi tiết ở [../reference/backend/README.md](../reference/backend/README.md).
