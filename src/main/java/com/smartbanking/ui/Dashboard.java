package com.smartbanking.ui;

import com.smartbanking.model.Account;
import com.smartbanking.model.User;
import com.smartbanking.service.AccountService;
import com.smartbanking.service.ReportService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.Map;

public class Dashboard {
    private final Stage stage;
    private final User user;
    private final BorderPane root = new BorderPane();
    private final AccountService accountService = new AccountService();
    private final ReportService reportService = new ReportService();

    public Dashboard(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public Parent getView() {
        root.setLeft(sidebar());
        root.setCenter(home());
        return root;
    }

    private VBox sidebar() {
        VBox box = new VBox(10);
        box.getStyleClass().add("sidebar");
        box.setPrefWidth(230);

        Label brand = new Label("SmartBank");
        brand.getStyleClass().add("brand");
        Label profile = new Label(user.getName() + "\n" + user.getRole());
        profile.getStyleClass().add("profile");

        Button home = nav("Dashboard");
        Button banking = nav("Banking");
        Button expenses = nav("Expenses");
        Button budgets = nav("Budgets");
        Button goals = nav("Savings Goals");
        Button reports = nav("Reports");
        Button admin = nav("Admin");
        Button logout = nav("Logout");

        home.setOnAction(e -> root.setCenter(home()));
        banking.setOnAction(e -> root.setCenter(new BankingScreen(user).getView()));
        expenses.setOnAction(e -> root.setCenter(new ExpenseScreen(user).getView()));
        budgets.setOnAction(e -> root.setCenter(new BudgetScreen(user).getView()));
        goals.setOnAction(e -> root.setCenter(new SavingsGoalScreen(user).getView()));
        reports.setOnAction(e -> root.setCenter(new ReportScreen(user).getView()));
        admin.setOnAction(e -> root.setCenter(new AdminScreen(user).getView()));
        logout.setOnAction(e -> stage.getScene().setRoot(new LoginScreen(stage).getView()));

        box.getChildren().addAll(brand, profile, home, banking, expenses, budgets, goals, reports);
        if (user.canAccessAdminPanel()) box.getChildren().add(admin);
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        box.getChildren().addAll(spacer, logout);
        return box;
    }

    private Button nav(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("nav-button");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Parent home() {
        VBox page = new VBox(18);
        page.setPadding(new Insets(24));
        page.getStyleClass().add("page");

        Label title = new Label("Dashboard");
        title.getStyleClass().add("page-title");

        HBox cards = new HBox(14);
        cards.setAlignment(Pos.CENTER_LEFT);
        cards.getChildren().addAll(
                card("Account Balance", balanceText()),
                card("Monthly Expense", "View Reports"),
                card("Budget Status", "Track Limits"),
                card("Savings Goals", "Progress")
        );

        PieChart chart = new PieChart();
        chart.setTitle("Expense Category Summary");
        chart.getStyleClass().add("chart");
        try {
            Map<String, Double> summary = reportService.expenseSummaryByCategory(user.getUserId());
            summary.forEach((key, value) -> chart.getData().add(new PieChart.Data(key, value)));
        } catch (Exception ignored) {
            chart.getData().add(new PieChart.Data("No Data", 1));
        }

        page.getChildren().addAll(title, cards, chart);
        return page;
    }

    private VBox card(String label, String value) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setPrefWidth(190);
        Label l = new Label(label);
        l.getStyleClass().add("muted");
        Label v = new Label(value);
        v.getStyleClass().add("card-value");
        card.getChildren().addAll(l, v);
        return card;
    }

    private String balanceText() {
        try {
            return accountService.getAccount(user.getUserId()).map(Account::getBalance).orElse(BigDecimal.ZERO).toString();
        } catch (Exception e) {
            return "0.00";
        }
    }
}
