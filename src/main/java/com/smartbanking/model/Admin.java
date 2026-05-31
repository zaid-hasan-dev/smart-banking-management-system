package com.smartbanking.model;

public class Admin extends User {
    public Admin(int userId, String name, String email, String password, boolean active) {
        super(userId, name, email, password, "ADMIN", active);
    }

    @Override
    public boolean canAccessAdminPanel() {
        return true;
    }
}
