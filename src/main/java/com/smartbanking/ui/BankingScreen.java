package com.smartbanking.ui;

import com.smartbanking.dao.TransactionDAO;
import com.smartbanking.model.Account;
import com.smartbanking.model.Transaction;
import com.smartbanking.model.User;
import com.smartbanking.service.AccountService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.math.BigDecimal;

public class BankingScreen {
    private final User user;
    private final AccountService accountService = new AccountService();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final Label accountInfo = new Label();
    private final TableView<Transaction> table = new TableView<>();
    private Account account;

    public BankingScreen(User user) {
        this.user = user;
    }

    public Parent getView() {
        VBox page = new VBox(16);
        page.setPadding(new Insets(24));
        page.getStyleClass().add("page");

        Label title = new Label("Banking");
        title.getStyleClass().add("page-title");
        accountInfo.getStyleClass().add("section-title");

        Button create = new Button("Create Account");
        TextField amount = new TextField();
        amount.setPromptText("Amount");
        TextField receiver = new TextField();
        receiver.setPromptText("Receiver account number");
        Button deposit = new Button("Deposit");
        Button withdraw = new Button("Withdraw");
        Button transfer = new Button("Transfer");

        HBox actions = new HBox(10, create, amount, deposit, withdraw, receiver, transfer);
        actions.getStyleClass().add("toolbar");

        create.setOnAction(e -> createAccount());
        deposit.setOnAction(e -> moneyAction(amount.getText(), "deposit", receiver.getText()));
        withdraw.setOnAction(e -> moneyAction(amount.getText(), "withdraw", receiver.getText()));
        transfer.setOnAction(e -> moneyAction(amount.getText(), "transfer", receiver.getText()));

        setupTable();
        refresh();
        page.getChildren().addAll(title, accountInfo, actions, table);
        return page;
    }

    private void setupTable() {
        TableColumn<Transaction, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        TableColumn<Transaction, String> type = new TableColumn<>("Type");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Transaction, BigDecimal> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Transaction, String> description = new TableColumn<>("Description");
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        table.getColumns().setAll(id, type, amount, description);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void createAccount() {
        try {
            if (account != null) {
                UIUtil.warning("You already have an account.");
                return;
            }
            accountService.createAccount(user.getUserId());
            refresh();
        } catch (Exception e) {
            UIUtil.error(e.getMessage());
        }
    }

    private void moneyAction(String amountText, String action, String receiver) {
        try {
            if (account == null) {
                UIUtil.warning("Create account first.");
                return;
            }
            BigDecimal value = new BigDecimal(amountText);
            boolean done = switch (action) {
                case "deposit" -> accountService.deposit(account, value);
                case "withdraw" -> accountService.withdraw(account, value);
                default -> accountService.transfer(account, receiver, value);
            };
            if (done) refresh();
            else UIUtil.warning("Operation failed. Check balance, amount or receiver account.");
        } catch (Exception e) {
            UIUtil.error("Invalid operation: " + e.getMessage());
        }
    }

    private void refresh() {
        try {
            account = accountService.getAccount(user.getUserId()).orElse(null);
            if (account == null) {
                accountInfo.setText("No bank account created yet.");
                table.setItems(FXCollections.observableArrayList());
            } else {
                accountInfo.setText("Account: " + account.getAccountNumber() + "    Balance: " + account.getBalance());
                table.setItems(FXCollections.observableArrayList(transactionDAO.findByAccountId(account.getAccountId())));
            }
        } catch (Exception e) {
            UIUtil.error(e.getMessage());
        }
    }
}
