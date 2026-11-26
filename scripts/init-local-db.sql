-- Chạy script này một lần bằng tài khoản superuser (mặc định: postgres)
-- trong pgAdmin (Query Tool) hoặc psql.exe trên máy local của bạn.

CREATE USER sola WITH PASSWORD 'sola';
CREATE DATABASE sola OWNER sola;
CREATE DATABASE sola_ref OWNER sola;
