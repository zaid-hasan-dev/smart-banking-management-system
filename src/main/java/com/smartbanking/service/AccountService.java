package com.smartbanking.service;

import com.smartbanking.dao.AccountDAO;
import com.smartbanking.dao.TransactionDAO;
import com.smartbanking.database.DBConnection;
import com.smartbanking.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class AccountService {
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public boolean createAccount(int userId) throws SQLException {
        return accountDAO.createAccount(userId, "SB" + System.currentTimeMillis());
    }

    public Optional<Account> getAccount(int userId) throws SQLException {
        return accountDAO.findByUserId(userId);
    }

    public boolean deposit(Account account, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            BigDecimal newBalance = account.getBalance().add(amount);
            accountDAO.updateBalance(account.getAccountId(), newBalance, connection);
            transactionDAO.create(account.getAccountId(), "DEPOSIT", amount, "Cash deposit", connection);
            connection.commit();
            return true;
        }
    }

    public boolean withdraw(Account account, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || account.getBalance().compareTo(amount) < 0) return false;
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            BigDecimal newBalance = account.getBalance().subtract(amount);
            accountDAO.updateBalance(account.getAccountId(), newBalance, connection);
            transactionDAO.create(account.getAccountId(), "WITHDRAW", amount, "Cash withdraw", connection);
            connection.commit();
            return true;
        }
    }

    public boolean transfer(Account sender, String receiverAccountNumber, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || sender.getBalance().compareTo(amount) < 0) return false;
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            Optional<Account> receiverOpt = accountDAO.findByAccountNumber(receiverAccountNumber, connection);
            if (receiverOpt.isEmpty()) {
                connection.rollback();
                return false;
            }
            Account receiver = receiverOpt.get();
            accountDAO.updateBalance(sender.getAccountId(), sender.getBalance().subtract(amount), connection);
            accountDAO.updateBalance(receiver.getAccountId(), receiver.getBalance().add(amount), connection);
            transactionDAO.create(sender.getAccountId(), "TRANSFER", amount, "Transfer to " + receiverAccountNumber, connection);
            transactionDAO.create(receiver.getAccountId(), "TRANSFER", amount, "Transfer from " + sender.getAccountNumber(), connection);
            connection.commit();
            return true;
        }
    }
}
