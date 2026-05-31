package com.smartbanking.service;

import com.smartbanking.dao.SavingsGoalDAO;
import com.smartbanking.model.SavingsGoal;

import java.sql.SQLException;
import java.util.List;

public class SavingsGoalService {
    private final SavingsGoalDAO savingsGoalDAO = new SavingsGoalDAO();

    public boolean save(SavingsGoal goal) throws SQLException {
        if (goal.getTargetAmount().doubleValue() <= 0 || goal.getSavedAmount().doubleValue() < 0) return false;
        return goal.getGoalId() == 0 ? savingsGoalDAO.create(goal) : savingsGoalDAO.update(goal);
    }

    public boolean delete(int goalId) throws SQLException {
        return savingsGoalDAO.delete(goalId);
    }

    public List<SavingsGoal> findAll(int userId) throws SQLException {
        return savingsGoalDAO.findAllByUserId(userId);
    }
}
