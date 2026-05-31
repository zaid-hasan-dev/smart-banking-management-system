package com.smartbanking.dao;

import com.smartbanking.database.DBConnection;
import com.smartbanking.model.Transaction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public boolean create(int accountId, String type, BigDecimal amount, String description, Connection connection) throws SQLException {
        String sql = "INSERT INTO transactions(account_id, type, amount, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            statement.setString(2, type);
            statement.setBigDecimal(3, amount);
            statement.setString(4, description);
            return statement.executeUpdate() > 0;
        }
    }

    public List<Transaction> findByAccountId(int accountId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    transactions.add(new Transaction(
                            rs.getInt("transaction_id"),
                            rs.getInt("account_id"),
                            rs.getString("type"),
                            rs.getBigDecimal("amount"),
                            rs.getString("description"),
                            rs.getTimestamp("transaction_date").toLocalDateTime()
                    ));
                }
            }
        }
        return transactions;
    }
}
