package com.smartbanking.dao;

import com.smartbanking.database.DBConnection;
import com.smartbanking.model.Account;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public class AccountDAO {
    public boolean createAccount(int userId, String accountNumber) throws SQLException {
        String sql = "INSERT INTO accounts(user_id, account_number, balance) VALUES (?, ?, 0.00)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, accountNumber);
            return statement.executeUpdate() > 0;
        }
    }

    public Optional<Account> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE user_id = ? LIMIT 1";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Account(rs.getInt("account_id"), rs.getInt("user_id"), rs.getString("account_number"), rs.getBigDecimal("balance")));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Account> findByAccountNumber(String accountNumber, Connection connection) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Account(rs.getInt("account_id"), rs.getInt("user_id"), rs.getString("account_number"), rs.getBigDecimal("balance")));
                }
            }
        }
        return Optional.empty();
    }

    public boolean updateBalance(int accountId, BigDecimal newBalance, Connection connection) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, newBalance);
            statement.setInt(2, accountId);
            return statement.executeUpdate() > 0;
        }
    }
}
