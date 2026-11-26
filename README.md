# Sola Monorepo

Không gian nghe Radio, Podcast và âm thanh tập trung hằng ngày.

## Cấu trúc thư mục

```text
sola/
├── backend/              # Khung rỗng để bạn TỰ CODE Backend (port 8080, DB: sola)
├── reference/backend/    # Bộ code Backend đối soát mẫu cho Chặng 0–1 (port 8081, DB: sola_ref)
├── frontend/             # Khung React + Vite + TS + Tailwind (port 5173, chờ AI dựng UI)
├── scripts/              # Script khởi tạo database trên PostgreSQL local
├── docs/                 # Tài liệu đặc tả kiến trúc & HTML mockup gốc
└── docker-compose.yml    # Tùy chọn (chỉ dùng nếu sau này muốn chạy container/Redis)
```

## Quy tắc học & làm việc: Tự code trước, đối soát sau

1. **Đọc tài liệu trước:** Xem mục `1–7` trong [docs/sola-architecture-spec.md](docs/sola-architecture-spec.md) và checklist trong [backend/README.md](backend/README.md).
2. **Tự code trong `backend/`:** Implement từng file theo checklist ở `backend/` (chạy trên cổng `8080`, DB `sola`). Không copy nguyên xi từ `reference/backend/`.
3. **Chỉ mở `reference/backend/` để đối soát:** Khi tự code xong (hoặc khi bị kẹt ở một lỗi cụ thể), bạn chạy song song `reference/backend/` ở cổng `8081`, so sánh JSON trả về, số câu SQL Hibernate sinh ra và đối chiếu từng điểm lưu ý trong [reference/backend/README.md](reference/backend/README.md).
4. **Phạm vi đối soát hiện tại:** **Chặng 0 + Chặng 1** (khung `common`, migration `V1` + `V2`, và 5 API công khai `/api/v1/public/**`). Các chặng tiếp theo (Auth JWT, Playback & Dashboard, SSRFGuard & Admin) sẽ bổ sung sau khi bạn hoàn thành Chặng 1.

## Cách khởi chạy với PostgreSQL Local (cổng `5432`)

### 1. Tạo 2 database `sola` và `sola_ref` trên PostgreSQL local

Bạn mở **pgAdmin** (hoặc dùng `psql.exe`) và chọn **1 trong 2 cách**:

- **Cách A — Dùng user riêng `sola` (chạy 1 lần, không cần cấu hình thêm khi chạy app):**
  Chạy nội dung file [scripts/init-local-db.sql](scripts/init-local-db.sql) bằng quyền superuser (`postgres`):
  ```sql
  CREATE USER sola WITH PASSWORD 'sola';
  CREATE DATABASE sola OWNER sola;
  CREATE DATABASE sola_ref OWNER sola;
  ```
  Hoặc bằng PowerShell:
  ```powershell
  & "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -f d:\sola\scripts\init-local-db.sql
  ```

- **Cách B — Dùng luôn tài khoản `postgres` sẵn có của bạn:**
  Chỉ cần tạo 2 database `sola` và `sola_ref` trong pgAdmin, rồi truyền biến môi trường `DB_USERNAME` và `DB_PASSWORD` khi chạy backend:
  ```powershell
  $env:DB_USERNAME="postgres"; $env:DB_PASSWORD="<mat_khau_cua_ban>"; mvn spring-boot:run
  ```

*(Flyway sẽ tự động chạy `V1__init_schema.sql` và `V2__seed_initial_data.sql` ngay lần đầu bạn khởi động Backend.)*

### 2. Chạy Backend của bạn (`backend/`, cổng `8080`)

```powershell
cd d:\sola\backend
mvn spring-boot:run
```

### 3. Chạy Backend đối soát (`reference/backend/`, cổng `8081`)

```powershell
cd d:\sola\reference\backend
mvn spring-boot:run
```

- Swagger UI đối soát: http://localhost:8081/swagger-ui.html

### 4. Chạy Frontend khung (`frontend/`, cổng `5173`)

Trên PowerShell Windows, dùng `npm.cmd`:

```powershell
cd d:\sola\frontend
npm.cmd install
npm.cmd run dev
```

*(Mặc định `vite.config.ts` proxy `/api` về `http://localhost:8080`. Nếu muốn test Frontend với bộ đối soát, đổi target trong `frontend/vite.config.ts` sang `8081`.)*

## Tài khoản dữ liệu mẫu (`V2__seed_initial_data.sql`)

| Vai trò | Email | Mật khẩu | Tên hiển thị |
| :--- | :--- | :--- | :--- |
| `ADMIN` | `admin@sola.local` | `Admin@123` | Quản trị viên |
| `USER` | `minh.nguyen@example.com` | `User@123` | Minh Nguyễn |
| `USER` | `lan.anh@example.com` | `User@123` | Lan Anh |
