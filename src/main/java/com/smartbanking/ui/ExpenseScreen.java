package com.smartbanking.ui;

import com.smartbanking.model.Expense;
import com.smartbanking.model.User;
import com.smartbanking.service.ExpenseService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseScreen {
    private final User user;
    private final ExpenseService service = new ExpenseService();
    private final TableView<Expense> table = new TableView<>();

    public ExpenseScreen(User user) {
        this.user = user;
    }

    public Parent getView() {
        VBox page = new VBox(16);
        page.setPadding(new Insets(24));
        page.getStyleClass().add("page");

        Label title = new Label("Expenses");
        title.getStyleClass().add("page-title");
        ComboBox<String> category = new ComboBox<>(FXCollections.observableArrayList(UIUtil.CATEGORIES));
        category.setPromptText("Category");
        TextField amount = new TextField();
        amount.setPromptText("Amount");
        TextField description = new TextField();
        description.setPromptText("Description");
        DatePicker date = new DatePicker(LocalDate.now());
        Button add = new Button("Add / Update");
        Button delete = new Button("Delete Selected");

        HBox form = new HBox(10, category, amount, description, date, add, delete);
        form.getStyleClass().add("toolbar");

        setupTable();
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                category.setValue(selected.getCategory());
                amount.setText(selected.getAmount().toString());
                description.setText(selected.getDescription());
                date.setValue(selected.getExpenseDate());
            }
        });
        add.setOnAction(e -> save(category.getValue(), amount.getText(), description.getText(), date.getValue()));
        delete.setOnAction(e -> delete());
        refresh();
        page.getChildren().addAll(title, form, table);
        return page;
    }

    private void setupTable() {
        TableColumn<Expense, String> category = new TableColumn<>("Category");
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Expense, BigDecimal> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Expense, String> description = new TableColumn<>("Description");
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Expense, LocalDate> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));
        table.getColumns().setAll(category, amount, description, date);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void save(String category, String amount, String description, LocalDate date) {
        try {
            Expense selected = table.getSelectionModel().getSelectedItem();
            int id = selected == null ? 0 : selected.getExpenseId();
            service.save(new Expense(id, user.getUserId(), category, new BigDecimal(amount), description, date));
            refresh();
        } catch (Exception e) {
            UIUtil.error("Expense save failed: " + e.getMessage());
        }
    }

    private void delete() {
        try {
            Expense selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                service.delete(selected.getExpenseId());
                refresh();
            }
        } catch (Exception e) {
            UIUtil.error(e.getMessage());
        }
    }

    private void refresh() {
        try {
            table.setItems(FXCollections.observableArrayList(service.findAll(user.getUserId())));
        } catch (Exception e) {
            UIUtil.error(e.getMessage());
        }
    }
}
