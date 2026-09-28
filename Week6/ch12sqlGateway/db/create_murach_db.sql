-- CSDL mau cho chuong 12 (Murach's Java Servlets and JSP)
-- Chay: mysql -u root -p < db/create_murach_db.sql

CREATE DATABASE IF NOT EXISTS murach
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE murach;

CREATE TABLE IF NOT EXISTS User (
    Email     VARCHAR(50) NOT NULL,
    FirstName VARCHAR(50) NOT NULL,
    LastName  VARCHAR(50) NOT NULL,
    PRIMARY KEY (Email)
);

INSERT IGNORE INTO User (Email, FirstName, LastName) VALUES
    ('jsmith@gmail.com',  'John',  'Smith'),
    ('adavis@yahoo.com',  'Andrea', 'Davis'),
    ('bnguyen@gmail.com', 'Binh',  'Nguyen');

-- Tai khoan ma slide 26 dung
CREATE USER IF NOT EXISTS 'murach_user'@'%' IDENTIFIED BY 'sesame';
GRANT ALL PRIVILEGES ON murach.* TO 'murach_user'@'%';
FLUSH PRIVILEGES;
