CREATE DATABASE IF NOT EXISTS smart_banking_system;
USE smart_banking_system;

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('CUSTOMER', 'ADMIN') DEFAULT 'CUSTOMER',
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    account_number VARCHAR(30) UNIQUE NOT NULL,
    balance DECIMAL(12,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    type ENUM('DEPOSIT', 'WITHDRAW', 'TRANSFER') NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    description VARCHAR(255),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS expenses (
    expense_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    category ENUM('Food','Transport','Shopping','Bills','Education','Entertainment') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    description VARCHAR(255),
    expense_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS budgets (
    budget_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    monthly_limit DECIMAL(10,2) NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    UNIQUE KEY unique_budget (user_id, category, month, year),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS savings_goals (
    goal_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    target_amount DECIMAL(10,2) NOT NULL,
    saved_amount DECIMAL(10,2) DEFAULT 0.00,
    deadline DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

INSERT INTO users (name, email, password, role)
SELECT 'Admin', 'admin@smartbank.com', 'admin123', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@smartbank.com');

INSERT INTO users (name, email, password, role)
SELECT 'Ali Khan', 'ali@example.com', 'ali12345', 'CUSTOMER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'ali@example.com');

INSERT INTO users (name, email, password, role)
SELECT 'Sara Ahmed', 'sara@example.com', 'sara12345', 'CUSTOMER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'sara@example.com');

INSERT INTO accounts (user_id, account_number, balance)
SELECT user_id, 'SB1000001', 85000.00 FROM users
WHERE email = 'ali@example.com'
AND NOT EXISTS (SELECT 1 FROM accounts WHERE account_number = 'SB1000001');

INSERT INTO accounts (user_id, account_number, balance)
SELECT user_id, 'SB1000002', 125000.00 FROM users
WHERE email = 'sara@example.com'
AND NOT EXISTS (SELECT 1 FROM accounts WHERE account_number = 'SB1000002');

INSERT INTO transactions (account_id, type, amount, description)
SELECT account_id, 'DEPOSIT', 100000.00, 'Initial deposit' FROM accounts
WHERE account_number = 'SB1000001'
AND NOT EXISTS (SELECT 1 FROM transactions WHERE account_id = accounts.account_id AND description = 'Initial deposit');

INSERT INTO transactions (account_id, type, amount, description)
SELECT account_id, 'WITHDRAW', 15000.00, 'ATM withdrawal' FROM accounts
WHERE account_number = 'SB1000001'
AND NOT EXISTS (SELECT 1 FROM transactions WHERE account_id = accounts.account_id AND description = 'ATM withdrawal');

INSERT INTO transactions (account_id, type, amount, description)
SELECT account_id, 'DEPOSIT', 125000.00, 'Salary deposit' FROM accounts
WHERE account_number = 'SB1000002'
AND NOT EXISTS (SELECT 1 FROM transactions WHERE account_id = accounts.account_id AND description = 'Salary deposit');

INSERT INTO expenses (user_id, category, amount, description, expense_date)
SELECT user_id, 'Food', 3200.00, 'Groceries and lunch', CURDATE() FROM users
WHERE email = 'ali@example.com'
AND NOT EXISTS (SELECT 1 FROM expenses WHERE user_id = users.user_id AND description = 'Groceries and lunch');

INSERT INTO expenses (user_id, category, amount, description, expense_date)
SELECT user_id, 'Transport', 1800.00, 'Fuel and ride booking', CURDATE() FROM users
WHERE email = 'ali@example.com'
AND NOT EXISTS (SELECT 1 FROM expenses WHERE user_id = users.user_id AND description = 'Fuel and ride booking');

INSERT INTO expenses (user_id, category, amount, description, expense_date)
SELECT user_id, 'Bills', 9500.00, 'Electricity bill', CURDATE() FROM users
WHERE email = 'ali@example.com'
AND NOT EXISTS (SELECT 1 FROM expenses WHERE user_id = users.user_id AND description = 'Electricity bill');

INSERT INTO expenses (user_id, category, amount, description, expense_date)
SELECT user_id, 'Shopping', 7600.00, 'Clothes shopping', CURDATE() FROM users
WHERE email = 'sara@example.com'
AND NOT EXISTS (SELECT 1 FROM expenses WHERE user_id = users.user_id AND description = 'Clothes shopping');

INSERT INTO expenses (user_id, category, amount, description, expense_date)
SELECT user_id, 'Education', 12000.00, 'Course fee', CURDATE() FROM users
WHERE email = 'sara@example.com'
AND NOT EXISTS (SELECT 1 FROM expenses WHERE user_id = users.user_id AND description = 'Course fee');

INSERT INTO budgets (user_id, category, monthly_limit, month, year)
SELECT user_id, 'Food', 15000.00, MONTH(CURDATE()), YEAR(CURDATE()) FROM users
WHERE email = 'ali@example.com'
ON DUPLICATE KEY UPDATE monthly_limit = VALUES(monthly_limit);

INSERT INTO budgets (user_id, category, monthly_limit, month, year)
SELECT user_id, 'Transport', 8000.00, MONTH(CURDATE()), YEAR(CURDATE()) FROM users
WHERE email = 'ali@example.com'
ON DUPLICATE KEY UPDATE monthly_limit = VALUES(monthly_limit);

INSERT INTO budgets (user_id, category, monthly_limit, month, year)
SELECT user_id, 'Shopping', 20000.00, MONTH(CURDATE()), YEAR(CURDATE()) FROM users
WHERE email = 'sara@example.com'
ON DUPLICATE KEY UPDATE monthly_limit = VALUES(monthly_limit);

INSERT INTO savings_goals (user_id, title, target_amount, saved_amount, deadline)
SELECT user_id, 'Emergency Fund', 200000.00, 55000.00, DATE_ADD(CURDATE(), INTERVAL 8 MONTH) FROM users
WHERE email = 'ali@example.com'
AND NOT EXISTS (SELECT 1 FROM savings_goals WHERE user_id = users.user_id AND title = 'Emergency Fund');

INSERT INTO savings_goals (user_id, title, target_amount, saved_amount, deadline)
SELECT user_id, 'Laptop Upgrade', 180000.00, 75000.00, DATE_ADD(CURDATE(), INTERVAL 5 MONTH) FROM users
WHERE email = 'sara@example.com'
AND NOT EXISTS (SELECT 1 FROM savings_goals WHERE user_id = users.user_id AND title = 'Laptop Upgrade');
