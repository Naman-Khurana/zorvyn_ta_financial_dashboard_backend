CREATE DATABASE IF NOT EXISTS `zorvyn_ta_db`;
USE `zorvyn_ta_db`;


DROP TABLE IF EXISTS financial_records;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;


CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);


CREATE TABLE users (
	`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`email` VARCHAR(100) UNIQUE NOT NULL,
	`password` VARCHAR(255) NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `active` BOOLEAN DEFAULT TRUE,
	`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `role_id` INT NOT NULL ,
    CONSTRAINT fk_user_role
    FOREIGN KEY (role_id)
    REFERENCES roles(id)
    ON DELETE RESTRICT
	
);

CREATE TABLE financial_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(12,2) NOT NULL,
    type ENUM('INCOME', 'EXPENSE') NOT NULL,
    category VARCHAR(100),
    record_date DATE NOT NULL,
	desc VARCHAR(255),
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_record_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

INSERT INTO roles (name) VALUES
('ADMIN'),
('ANALYST'),
('VIEWER');

