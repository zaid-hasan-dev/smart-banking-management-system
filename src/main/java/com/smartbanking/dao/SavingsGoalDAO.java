package com.smartbanking.dao;

import com.smartbanking.database.DBConnection;
import com.smartbanking.model.SavingsGoal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SavingsGoalDAO implements CrudDAO<SavingsGoal> {
    @Override
    public boolean create(SavingsGoal goal) throws SQLException {
        String sql = "INSERT INTO savings_goals(user_id, title, target_amount, saved_amount, deadline) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, goal.getUserId());
            statement.setString(2, goal.getTitle());
            statement.setBigDecimal(3, goal.getTargetAmount());
            statement.setBigDecimal(4, goal.getSavedAmount());
            statement.setDate(5, goal.getDeadline() == null ? null : Date.valueOf(goal.getDeadline()));
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(SavingsGoal goal) throws SQLException {
        String sql = "UPDATE savings_goals SET title = ?, target_amount = ?, saved_amount = ?, deadline = ? WHERE goal_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, goal.getTitle());
            statement.setBigDecimal(2, goal.getTargetAmount());
            statement.setBigDecimal(3, goal.getSavedAmount());
            statement.setDate(4, goal.getDeadline() == null ? null : Date.valueOf(goal.getDeadline()));
            statement.setInt(5, goal.getGoalId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM savings_goals WHERE goal_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public List<SavingsGoal> findAllByUserId(int userId) throws SQLException {
        List<SavingsGoal> goals = new ArrayList<>();
        String sql = "SELECT * FROM savings_goals WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Date deadline = rs.getDate("deadline");
                    goals.add(new SavingsGoal(rs.getInt("goal_id"), rs.getInt("user_id"), rs.getString("title"), rs.getBigDecimal("target_amount"), rs.getBigDecimal("saved_amount"), deadline == null ? null : deadline.toLocalDate()));
                }
            }
        }
        return goals;
    }
}
