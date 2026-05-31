package com.smartbanking.service;

import com.smartbanking.dao.BudgetDAO;
import com.smartbanking.dao.ExpenseDAO;
import com.smartbanking.model.Budget;

import java.sql.SQLException;
import java.util.List;

public class BudgetService {
    private final BudgetDAO budgetDAO = new BudgetDAO();
    private final ExpenseDAO expenseDAO = new ExpenseDAO();

    public boolean save(Budget budget) throws SQLException {
        return budget.getMonthlyLimit().doubleValue() > 0 && budgetDAO.create(budget);
    }

    public List<Budget> findAll(int userId) throws SQLException {
        return budgetDAO.findAllByUserId(userId);
    }

    public double remaining(Budget budget) throws SQLException {
        double spent = expenseDAO.totalByCategory(budget.getUserId(), budget.getCategory(), budget.getMonth(), budget.getYear());
        return budget.getMonthlyLimit().doubleValue() - spent;
    }
}
