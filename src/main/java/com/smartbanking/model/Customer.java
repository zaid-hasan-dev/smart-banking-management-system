package com.smartbanking.model;

public class Customer extends User {
    public Customer(int userId, String name, String email, String password, boolean active) {
        super(userId, name, email, password, "CUSTOMER", active);
    }

    @Override
    public boolean canAccessAdminPanel() {
        return false;
    }
}
