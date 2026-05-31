package com.smartbanking.dao;

import com.smartbanking.database.DBConnection;
import com.smartbanking.model.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO implements CrudDAO<Expense> {
    @Override
    public boolean create(Expense expense) throws SQLException {
        String sql = "INSERT INTO expenses(user_id, category, amount, description, expense_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, expense.getUserId());
            statement.setString(2, expense.getCategory());
            statement.setBigDecimal(3, expense.getAmount());
            statement.setString(4, expense.getDescription());
            statement.setDate(5, Date.valueOf(expense.getExpenseDate()));
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Expense expense) throws SQLException {
        String sql = "UPDATE expenses SET category = ?, amount = ?, description = ?, expense_date = ? WHERE expense_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, expense.getCategory());
            statement.setBigDecimal(2, expense.getAmount());
            statement.setString(3, expense.getDescription());
            statement.setDate(4, Date.valueOf(expense.getExpenseDate()));
            statement.setInt(5, expense.getExpenseId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM expenses WHERE expense_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public List<Expense> findAllByUserId(int userId) throws SQLException {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE user_id = ? ORDER BY expense_date DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    expenses.add(mapExpense(rs));
                }
            }
        }
        return expenses;
    }

    public double totalByCategory(int userId, String category, int month, int year) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) total FROM expenses WHERE user_id = ? AND category = ? AND MONTH(expense_date) = ? AND YEAR(expense_date) = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, category);
            statement.setInt(3, month);
            statement.setInt(4, year);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getDouble("total") : 0;
            }
        }
    }

    private Expense mapExpense(ResultSet rs) throws SQLException {
        return new Expense(
                rs.getInt("expense_id"),
                rs.getInt("user_id"),
                rs.getString("category"),
                rs.getBigDecimal("amount"),
                rs.getString("description"),
                rs.getDate("expense_date").toLocalDate()
        );
    }
}
