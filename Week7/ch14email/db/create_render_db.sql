-- Bang cho PostgreSQL (dung khi deploy len Render).
--
-- Chay mot lan sau khi tao database tren Render:
--   psql "<External Database URL cua Render>" -f db/create_render_db.sql
-- Hoac dan noi dung file nay vao tab "Query" cua Render Postgres dashboard.
--
-- Luu y: ten bang la "useraccount" chu khong phai "User" nhu Week6, vi USER
-- la TU KHOA cua PostgreSQL. Ten cot de chu thuong het vi PostgreSQL tu ha
-- ten khong dat trong dau nhay kep ve chu thuong.
-- Entity murach.business.User anh xa sang bang nay bang @Table/@Column.

CREATE TABLE IF NOT EXISTS useraccount (
    email     VARCHAR(50) NOT NULL,
    firstname VARCHAR(50) NOT NULL,
    lastname  VARCHAR(50) NOT NULL,
    PRIMARY KEY (email)
);

INSERT INTO useraccount (email, firstname, lastname) VALUES
    ('jsmith@gmail.com',  'John',   'Smith'),
    ('adavis@yahoo.com',  'Andrea', 'Davis'),
    ('bnguyen@gmail.com', 'Binh',   'Nguyen')
ON CONFLICT (email) DO NOTHING;

-- Khong tao user/role o day: Render cap san tai khoan CSDL va truyen vao
-- ung dung qua bien moi truong DATABASE_URL.
