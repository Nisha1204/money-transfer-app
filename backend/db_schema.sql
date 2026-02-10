-- 1. Setup Database
CREATE DATABASE IF NOT EXISTS money_db;
USE money_db;

-- 2. Drop existing tables in reverse order of dependency
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS transaction_logs;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

---

-- 3. Create Tables

-- Users Table (No foreign keys)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

-- Accounts Table (Depends on users)
CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    holder_name VARCHAR(255) DEFAULT NULL,
    -- Using DECIMAL for financial accuracy to match BigDecimal in Java
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00, 
    status VARCHAR(20) NOT NULL, -- To match the AccountStatus Enum
    version INT DEFAULT 1,
    last_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id INT,
    CONSTRAINT fk_user_account 
        FOREIGN KEY (user_id) REFERENCES users(id) 
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- Transaction Logs Table (Depends on accounts)
CREATE TABLE transaction_logs (
    -- Change to VARCHAR if you want to use UUIDs, or stay BIGINT for auto-increment
    id VARCHAR(36) PRIMARY KEY,
    fromAccountId BIGINT NOT NULL,
    toAccountId BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    failureReason VARCHAR(255) NULL,
    idempotencyKey VARCHAR(255) NULL,
    createdOn DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_from_account FOREIGN KEY (fromAccountId) REFERENCES accounts(id),
    CONSTRAINT fk_to_account FOREIGN KEY (toAccountId) REFERENCES accounts(id)
) ENGINE=InnoDB;

---

-- 4. Insert Seed Data

INSERT INTO users (username, password, role) VALUES 
('user1', '$2a$10$xyz...', 'ROLE_USER'), -- Assuming encoded passwords later
('user2', '$2a$10$abc...', 'ROLE_USER'),
('user3', '$2a$10$def...', 'ROLE_USER');

INSERT INTO accounts (holder_name, balance, status, version, last_updated, user_id) VALUES 
('Alice 1', 1500.00, 'ACTIVE', 1, NOW(), 1), 
('Alice 2', 5500.00, 'ACTIVE', 1, NOW(), 1), 
('Bob',     2700.00, 'ACTIVE', 1, NOW(), 2), 
('Charlie', 450.00, 'ACTIVE', 1, NOW(), 3), 
('David',   3200.00, 'ACTIVE', 1, NOW(), 3);