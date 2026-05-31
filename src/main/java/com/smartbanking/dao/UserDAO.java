package com.smartbanking.dao;

import com.smartbanking.database.DBConnection;
import com.smartbanking.model.Admin;
import com.smartbanking.model.Customer;
import com.smartbanking.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {
    public boolean register(String name, String email, String password) throws SQLException {
        String sql = "INSERT INTO users(name, email, password, role) VALUES (?, ?, ?, 'CUSTOMER')";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            return statement.executeUpdate() > 0;
        }
    }

    public Optional<User> findByEmailAndPassword(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND active = TRUE";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        }
        return users;
    }

    public boolean setActive(int userId, boolean active) throws SQLException {
        String sql = "UPDATE users SET active = ? WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, active);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        String role = rs.getString("role");
        if ("ADMIN".equals(role)) {
            return new Admin(rs.getInt("user_id"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getBoolean("active"));
        }
        return new Customer(rs.getInt("user_id"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getBoolean("active"));
    }
}
