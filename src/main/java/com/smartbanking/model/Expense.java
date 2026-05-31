package com.smartbanking.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
    private int expenseId;
    private int userId;
    private String category;
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;

    public Expense(int expenseId, int userId, String category, BigDecimal amount, String description, LocalDate expenseDate) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
    }

    public int getExpenseId() { return expenseId; }
    public int getUserId() { return userId; }
    public String getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getExpenseDate() { return expenseDate; }
}
