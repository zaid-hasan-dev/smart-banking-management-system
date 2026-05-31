package com.smartbanking.dao;

import com.smartbanking.database.DBConnection;
import com.smartbanking.model.Budget;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO implements CrudDAO<Budget> {
    @Override
    public boolean create(Budget budget) throws SQLException {
        String sql = """
                INSERT INTO budgets(user_id, category, monthly_limit, month, year)
                VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE monthly_limit = VALUES(monthly_limit)
                """;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, budget.getUserId());
            statement.setString(2, budget.getCategory());
            statement.setBigDecimal(3, budget.getMonthlyLimit());
            statement.setInt(4, budget.getMonth());
            statement.setInt(5, budget.getYear());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Budget budget) throws SQLException {
        return create(budget);
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM budgets WHERE budget_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public List<Budget> findAllByUserId(int userId) throws SQLException {
        List<Budget> budgets = new ArrayList<>();
        String sql = "SELECT * FROM budgets WHERE user_id = ? ORDER BY year DESC, month DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    budgets.add(new Budget(rs.getInt("budget_id"), rs.getInt("user_id"), rs.getString("category"), rs.getBigDecimal("monthly_limit"), rs.getInt("month"), rs.getInt("year")));
                }
            }
        }
        return budgets;
    }
}
