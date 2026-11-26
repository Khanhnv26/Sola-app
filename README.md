# Sola — Góc nghe Radio, Podcast & Âm thanh tập trung hằng ngày

> 🚧 **Trạng thái dự án: Đang phát triển (Work in Progress)**  
> Dự án đang được xây dựng theo lộ trình từng chặng: **tự tay thiết kế & code Backend (Java 21 / Spring Boot)** kết hợp **review & hoàn thiện Frontend (React / TypeScript)** từ bản thiết kế mẫu.

---

## Giới thiệu sản phẩm

**Sola** là ứng dụng web tạo không gian nghe **Radio trực tiếp, Podcast và âm thanh nền thiên nhiên** giúp bạn giữ nhịp tập trung êm dịu khi học tập hoặc làm việc mỗi ngày.

### Những trải nghiệm chính Sola hướng tới

- **Nghe thử tức thì không cần tài khoản (`GUEST`):**
  - Mở **Phòng nghe (`/studio`)** và phát ngay các kênh tuyển chọn (*Solar Morning*, *Golden Rain*, *Bright Study*, *Sớm Mai Acoustic*).
  - Tự phối âm lượng **3 lớp âm thanh nền** (*Tiếng mưa nhẹ*, *Quán cafe ấm*, *Âm nền dịu*).
  - Đồng hồ tập trung **25 / 50 phút**, **Hẹn tắt êm khi ngủ** (giảm dần âm lượng ở cuối phiên) và **Chế độ tĩnh lặng toàn màn hình (Zen)**.
  - Trình phát nhạc giữ kết nối xuyên suốt khi chuyển sang **Khám phá (`/discover`)**, tự lưu tiến độ nghe dở và cấu hình âm lượng vào `localStorage`.
- **Góc cá nhân & Đồng bộ đa thiết bị (`USER`):**
  - **Tự động gộp dữ liệu nghe thử (`Guest-to-User Sync`)** lên tài khoản ngay sau khi đăng nhập/đăng ký.
  - Theo dõi tổng phút nghe trong tuần, chuỗi ngày tập trung liên tục và biểu đồ **7 ngày qua** tại **Góc cá nhân (`/dashboard`)**.
  - Tạo, sửa, xóa **Kênh riêng** (chọn chủ đề, phối sẵn mức âm nền, chia sẻ cộng đồng hoặc giữ riêng tư) và thêm nguồn Radio/Podcast cá nhân được kiểm tra an toàn tự động ở Backend.
  - Quản lý danh sách chờ Podcast và nhớ chính xác vị trí giây đang nghe dở.
- **Giám sát nguồn phát & Kiểm duyệt (`ADMIN`):**
  - Theo dõi trạng thái nguồn phát (*Ổn định*, *Chập chờn*, *Mất kết nối*), xem lịch sử phản hồi và kiểm tra lại kết nối tại **Quản lý kênh (`/admin`)**.
  - Quản lý kho kênh tuyển chọn và ẩn/mở lại các kênh cộng đồng chia sẻ khi vi phạm hoặc hỏng đường dẫn.

### Định hướng giao diện (UI/UX)

- Phong cách **Soft-Bento** kết hợp **Liquid Glass Chrome**, hỗ trợ **Light / Dark Mode**, ngôn ngữ hiển thị tiếng Việt nhẹ nhàng, gần gũi.
- Kiến trúc **Dual-Shell**: tách biệt khung công khai (`/`, `/login`) và khung ứng dụng (`/studio`, `/discover`, `/dashboard`, `/admin`) có trình phát thu gọn dưới đáy.
- Xem trực tiếp thiết kế giao diện mẫu tại [docs/sola-mockup.html](docs/sola-mockup.html) và đặc tả đầy đủ tại [docs/sola-architecture-spec.md](docs/sola-architecture-spec.md).

---

## Công nghệ sử dụng

| Tầng hệ thống | Công nghệ |
| :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.5.7 (Modular Monolith), Spring Data JPA, Flyway, Springdoc OpenAPI (Swagger UI) |
| **Database** | PostgreSQL (Local Dev) · Neon PostgreSQL (Production) |
| **Frontend** | React, TypeScript, Vite, Tailwind CSS v4, React Router |
| **Audio (dự kiến)** | HTML5 `<audio>` / `HLS.js` (Radio & Podcast) + `Web Audio API` (trộn âm thanh nền) |
| **Bảo mật & Mở rộng (các chặng sau)** | Spring Security + JWT (Refresh Token Rotation), `SSRFGuard`, Rome RSS Parser (`Conditional GET`), Caffeine/Redis Cache |

---

## Tiến độ xây dựng (Roadmap)

- [x] **Chặng 0 — Nền tảng & Thiết kế:** Chốt đặc tả kiến trúc, mockup UI, Database Schema 12 bảng (`V1__init_schema.sql`), dữ liệu mẫu (`V2__seed_initial_data.sql`), dựng khung `frontend/` và `backend/`.
- [ ] **Chặng 1 — API Công khai & Phòng nghe Khách (Đang build):**
  - 5 API đọc công khai `/api/v1/public/**` (đã có bộ code đối soát ở `reference/backend/`, đang tự code ở `backend/`).
  - Giao diện Landing (`/`), Phòng nghe (`/studio`) và Khám phá (`/discover`) kết nối API thật.
- [ ] **Chặng 2 — Xác thực & Đồng bộ Khách:** Đăng ký, đăng nhập JWT + Refresh Token Rotation (`/api/v1/auth/**`) và luồng `Guest-to-User Sync`.
- [ ] **Chặng 3 — Góc cá nhân & Kênh riêng:** CRUD kênh cá nhân, lưu tiến độ Podcast, hàng đợi phát, nhật ký phiên nghe & biểu đồ 7 ngày (`/dashboard`).
- [ ] **Chặng 4 — Nguồn ngoài an toàn & Quản trị:** Bộ lọc `SSRFGuard`, đọc RSS Podcast qua `Rome` (`Conditional GET`), giám sát tín hiệu nguồn phát & kiểm duyệt (`/admin`).

---

## Cấu trúc Monorepo

```text
sola/
├── backend/              # Khung rỗng để TỰ CODE Backend (cổng 8080, DB: sola)
├── reference/backend/    # Bộ code Backend đối soát mẫu Chặng 0–1 (cổng 8081, DB: sola_ref)
├── frontend/             # Khung React + Vite + TS + Tailwind (cổng 5173)
├── scripts/              # Script SQL khởi tạo DB trên PostgreSQL local
├── docs/                 # Đặc tả kiến trúc (sola-architecture-spec.md) & UI Mockup (sola-mockup.html)
└── docker-compose.yml    # Tùy chọn nếu chạy bằng container
```

### Nguyên tắc làm việc trong repo
1. Tự code từng chặng trong [backend/](backend/README.md) (cổng `8080`, DB `sola`).
2. Dùng [reference/backend/](reference/backend/README.md) (cổng `8081`, DB `sola_ref`) để đối chiếu JSON trả về, cách thiết kế DTO, xử lý lỗi và tối ưu SQL (tránh N+1) sau khi tự viết xong.

---

## Hướng dẫn chạy ở Local (PostgreSQL cổng `5432`)

### 1. Khởi tạo 2 database `sola` và `sola_ref`

Chạy nội dung file [scripts/init-local-db.sql](scripts/init-local-db.sql) bằng quyền superuser (`postgres`) trong **pgAdmin** hoặc `psql.exe`:

```powershell
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -f d:\sola\scripts\init-local-db.sql
```

*(Hoặc nếu dùng trực tiếp tài khoản `postgres` sẵn có, hãy tạo 2 DB `sola` và `sola_ref` rồi truyền `$env:DB_USERNAME` / `$env:DB_PASSWORD` trước khi chạy `mvn spring-boot:run`.)*

### 2. Chạy Backend tự code (`8080`) hoặc Backend đối soát (`8081`)

```powershell
# Backend tự code (DB: sola)
cd backend
mvn spring-boot:run

# Backend đối soát Chặng 0–1 (DB: sola_ref)
cd reference/backend
mvn spring-boot:run
```

- Swagger UI đối soát: `http://localhost:8081/swagger-ui.html`

### 3. Chạy Frontend khung (`5173`)

Trên PowerShell Windows, dùng `npm.cmd`:

```powershell
cd frontend
npm.cmd install
npm.cmd run dev
```

*(Mặc định `frontend/vite.config.ts` proxy `/api` về `http://localhost:8080`; đổi sang `8081` nếu muốn chạy thử cùng `reference/backend`.)*

---

## Tài khoản dữ liệu mẫu (`V2__seed_initial_data.sql`)

| Vai trò | Email | Mật khẩu | Tên hiển thị |
| :--- | :--- | :--- | :--- |
| `ADMIN` | `admin@sola.local` | `Admin@123` | Quản trị viên |
| `USER` | `minh.nguyen@example.com` | `User@123` | Minh Nguyễn |
| `USER` | `lan.anh@example.com` | `User@123` | Lan Anh |
