# Smart Personal Banking & Expense Management System

JavaFX desktop application with MySQL database and JDBC connectivity.

## Features

- User registration and secure login validation
- Customer and admin roles
- Bank account creation with automatic account number generation
- Deposit, withdraw, transfer, balance check and transaction history
- Expense CRUD with fixed categories
- Monthly and category-wise budgets with remaining budget calculation
- Savings goals with progress tracking
- Dashboard cards, sidebar navigation, tables and charts
- Admin user management

## Project Structure

```text
src/main/java/com/smartbanking/
├── model
├── dao
├── database
├── service
├── ui
└── Main.java
```

## Requirements

- JDK 17 or newer
- Maven
- MySQL Server

## Setup

1. Create the database by running `database.sql` in MySQL.
2. Update MySQL username/password in:

```text
src/main/java/com/smartbanking/database/DBConnection.java
```

3. Run the application:

```bash
mvn clean javafx:run
```

On this Windows machine, a portable Maven setup is also available. You can start the app with:

```bat
run-app.bat
```

If MySQL is stopped, start and initialize it with:

```bat
start-database.bat
```

## Default Admin Login

```text
Email: admin@smartbank.com
Password: admin123
```

## Notes

For a production version, replace plain password storage with BCrypt hashing.
