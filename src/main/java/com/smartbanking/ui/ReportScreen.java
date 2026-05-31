package com.smartbanking.ui;

import com.smartbanking.model.User;
import com.smartbanking.service.ReportService;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ReportScreen {
    private final User user;
    private final ReportService service = new ReportService();

    public ReportScreen(User user) {
        this.user = user;
    }

    public Parent getView() {
        VBox page = new VBox(16);
        page.setPadding(new Insets(24));
        page.getStyleClass().add("page");

        Label title = new Label("Reports & Analytics");
        title.getStyleClass().add("page-title");
        PieChart pie = new PieChart();
        pie.setTitle("Expense Category Summary");
        BarChart<String, Number> bar = new BarChart<>(new CategoryAxis(), new NumberAxis());
        bar.setTitle("Category Spending");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expenses");

        try {
            service.expenseSummaryByCategory(user.getUserId()).forEach((category, total) -> {
                pie.getData().add(new PieChart.Data(category, total));
                series.getData().add(new XYChart.Data<>(category, total));
            });
        } catch (Exception e) {
            UIUtil.error(e.getMessage());
        }
        bar.getData().add(series);
        HBox charts = new HBox(18, pie, bar);
        page.getChildren().addAll(title, charts);
        return page;
    }
}
