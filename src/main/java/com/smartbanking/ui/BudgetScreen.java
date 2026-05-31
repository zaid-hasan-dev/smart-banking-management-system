package com.smartbanking.ui;

import com.smartbanking.model.Budget;
import com.smartbanking.model.User;
import com.smartbanking.service.BudgetService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetScreen {
    private final User user;
    private final BudgetService service = new BudgetService();
    private final TableView<Budget> table = new TableView<>();
    private final Label remaining = new Label("Select a budget to see remaining amount.");

    public BudgetScreen(User user) {
        this.user = user;
    }

    public Parent getView() {
        VBox page = new VBox(16);
        page.setPadding(new Insets(24));
        page.getStyleClass().add("page");

        Label title = new Label("Budget Management");
        title.getStyleClass().add("page-title");
        ComboBox<String> category = new ComboBox<>(FXCollections.observableArrayList(UIUtil.CATEGORIES));
        category.setPromptText("Category");
        TextField limit = new TextField();
        limit.setPromptText("Monthly limit");
        Button save = new Button("Set Budget");
        HBox form = new HBox(10, category, limit, save);
        form.getStyleClass().add("toolbar");

        setupTable();
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> showRemaining(selected));
        save.setOnAction(e -> save(category.getValue(), limit.getText()));
        refresh();
        page.getChildren().addAll(title, form, remaining, table);
        return page;
    }

    private void setupTable() {
        TableColumn<Budget, String> category = new TableColumn<>("Category");
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Budget, BigDecimal> limit = new TableColumn<>("Limit");
        limit.setCellValueFactory(new PropertyValueFactory<>("monthlyLimit"));
        TableColumn<Budget, Integer> month = new TableColumn<>("Month");
        month.setCellValueFactory(new PropertyValueFactory<>("month"));
        TableColumn<Budget, Integer> year = new TableColumn<>("Year");
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        table.getColumns().setAll(category, limit, month, year);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void save(String category, String limit) {
        try {
            LocalDate now = LocalDate.now();
            service.save(new Budget(0, user.getUserId(), category, new BigDecimal(limit), now.getMonthValue(), now.getYear()));
            refresh();
        } catch (Exception e) {
            UIUtil.error(e.getMessage());
        }
    }

    private void showRemaining(Budget budget) {
        if (budget == null) return;
        try {
            double value = service.remaining(budget);
            remaining.setText("Remaining for " + budget.getCategory() + ": " + value);
            if (value < 0) UIUtil.warning("Budget exceeded for " + budget.getCategory());
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
