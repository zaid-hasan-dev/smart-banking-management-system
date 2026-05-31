package com.smartbanking.model;

import java.math.BigDecimal;

public class Budget {
    private int budgetId;
    private int userId;
    private String category;
    private BigDecimal monthlyLimit;
    private int month;
    private int year;

    public Budget(int budgetId, int userId, String category, BigDecimal monthlyLimit, int month, int year) {
        this.budgetId = budgetId;
        this.userId = userId;
        this.category = category;
        this.monthlyLimit = monthlyLimit;
        this.month = month;
        this.year = year;
    }

    public int getBudgetId() { return budgetId; }
    public int getUserId() { return userId; }
    public String getCategory() { return category; }
    public BigDecimal getMonthlyLimit() { return monthlyLimit; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
}
