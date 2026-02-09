create database money_db;

use money_db; 

-- Disable checks to prevent constraint errors during drop
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;

-- Users Table 
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

-- Accounts Table 
CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    holder_name VARCHAR(255) DEFAULT NULL,
    balance FLOAT NOT NULL DEFAULT 0.0,
    status ENUM('ACTIVE','LOCKED','CLOSED') NOT NULL,
    version INT DEFAULT 1,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id INT, 
    CONSTRAINT fk_user_account 
        FOREIGN KEY (user_id) REFERENCES users(id) 
        ON DELETE CASCADE
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO users (username, password, role) VALUES 
('user1', 'pwd', 'USER'),
('user2', 'pwd', 'USER'),
('user3', 'pwd', 'USER');

INSERT INTO accounts (holder_name, balance, status, version, last_updated, user_id) VALUES 
('Alice 1', 1500.00, 'ACTIVE', 1, NOW(), 1), 
('Alice 2',  5500.00, 'ACTIVE', 1, NOW(), 1), 
('Bob',   2700.00, 'ACTIVE', 1, NOW(), 2), 
('Charlie', 450.00, 'ACTIVE', 1, NOW(), 3), 
('David',    3200.00, 'ACTIVE', 1, NOW(), 3)

CREATE TABLE transaction_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fromAccountId INT NOT NULL,
    toAccountId INT NOT NULL,
    amount FLOAT NOT NULL,
    status ENUM('SUCCESS', 'FAILED') NOT NULL,
    failureReason VARCHAR(255) NULL,
    idempotencyKey INT NULL DEFAULT 0,
    createdOn TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY fromAccountId REFERENCES accounts(id),
    FOREIGN KEY toAccountId REFERENCES accounts(id),
);
