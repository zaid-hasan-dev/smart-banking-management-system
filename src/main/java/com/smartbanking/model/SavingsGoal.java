package com.smartbanking.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingsGoal {
    private int goalId;
    private int userId;
    private String title;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;
    private LocalDate deadline;

    public SavingsGoal(int goalId, int userId, String title, BigDecimal targetAmount, BigDecimal savedAmount, LocalDate deadline) {
        this.goalId = goalId;
        this.userId = userId;
        this.title = title;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
        this.deadline = deadline;
    }

    public int getGoalId() { return goalId; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public BigDecimal getTargetAmount() { return targetAmount; }
    public BigDecimal getSavedAmount() { return savedAmount; }
    public LocalDate getDeadline() { return deadline; }
    public double getProgress() {
        if (targetAmount.doubleValue() <= 0) return 0;
        return savedAmount.doubleValue() / targetAmount.doubleValue();
    }
}
