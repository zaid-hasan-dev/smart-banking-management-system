package com.smartbanking.service;

import com.smartbanking.dao.UserDAO;
import com.smartbanking.model.User;

import java.sql.SQLException;
import java.util.Optional;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public boolean register(String name, String email, String password) throws SQLException {
        if (name.isBlank() || !email.contains("@") || password.length() < 6) {
            return false;
        }
        return userDAO.register(name.trim(), email.trim().toLowerCase(), password);
    }

    public Optional<User> login(String email, String password) throws SQLException {
        if (email.isBlank() || password.isBlank()) {
            return Optional.empty();
        }
        return userDAO.findByEmailAndPassword(email.trim().toLowerCase(), password);
    }
}
