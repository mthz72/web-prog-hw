-- CSDL mau cho chuong 14 (Murach's Java Servlets and JSP) - ban MySQL, chay o may.
-- Chay: mysql -u root -p < db/create_murach_db.sql
--
-- JPA khong tu tao bang (persistence.xml dat schema-generation = none),
-- nen phai chay file nay truoc.
--
-- Ten bang la "useraccount" chu khong phai "User" nhu Week6: USER la tu khoa
-- cua PostgreSQL, ma ban deploy tren Render bat buoc dung PostgreSQL. Dat ten
-- trung tinh thi cung mot entity chay duoc ca hai engine.
-- Ban PostgreSQL cua file nay: db/create_render_db.sql

CREATE DATABASE IF NOT EXISTS murach
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE murach;

CREATE TABLE IF NOT EXISTS useraccount (
    email     VARCHAR(50) NOT NULL,
    firstname VARCHAR(50) NOT NULL,
    lastname  VARCHAR(50) NOT NULL,
    PRIMARY KEY (email)
);

INSERT IGNORE INTO useraccount (email, firstname, lastname) VALUES
    ('jsmith@gmail.com',  'John',   'Smith'),
    ('adavis@yahoo.com',  'Andrea', 'Davis'),
    ('bnguyen@gmail.com', 'Binh',   'Nguyen');

-- Tai khoan ma slide chuong 12 dung
CREATE USER IF NOT EXISTS 'murach_user'@'%' IDENTIFIED BY 'sesame';
GRANT ALL PRIVILEGES ON murach.* TO 'murach_user'@'%';
FLUSH PRIVILEGES;
