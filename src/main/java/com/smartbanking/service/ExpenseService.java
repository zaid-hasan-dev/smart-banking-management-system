package com.smartbanking.service;

import com.smartbanking.dao.ExpenseDAO;
import com.smartbanking.model.Expense;

import java.sql.SQLException;
import java.util.List;

public class ExpenseService {
    private final ExpenseDAO expenseDAO = new ExpenseDAO();

    public boolean save(Expense expense) throws SQLException {
        if (expense.getAmount().doubleValue() <= 0) return false;
        return expense.getExpenseId() == 0 ? expenseDAO.create(expense) : expenseDAO.update(expense);
    }

    public boolean delete(int expenseId) throws SQLException {
        return expenseDAO.delete(expenseId);
    }

    public List<Expense> findAll(int userId) throws SQLException {
        return expenseDAO.findAllByUserId(userId);
    }
}
