package com.smartbanking.service;

import com.smartbanking.dao.ExpenseDAO;
import com.smartbanking.model.Expense;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private final ExpenseDAO expenseDAO = new ExpenseDAO();

    public Map<String, Double> expenseSummaryByCategory(int userId) throws SQLException {
        List<Expense> expenses = expenseDAO.findAllByUserId(userId);
        Map<String, Double> summary = new HashMap<>();
        for (Expense expense : expenses) {
            summary.merge(expense.getCategory(), expense.getAmount().doubleValue(), Double::sum);
        }
        return summary;
    }
}
