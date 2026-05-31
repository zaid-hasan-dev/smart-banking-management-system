package com.smartbanking.ui;

import com.smartbanking.model.SavingsGoal;
import com.smartbanking.model.User;
import com.smartbanking.service.SavingsGoalService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingsGoalScreen {
    private final User user;
    private final SavingsGoalService service = new SavingsGoalService();
    private final TableView<SavingsGoal> table = new TableView<>();

    public SavingsGoalScreen(User user) {
        this.user = user;
    }

    public Parent getView() {
        VBox page = new VBox(16);
        page.setPadding(new Insets(24));
        page.getStyleClass().add("page");
        Label title = new Label("Savings Goals");
        title.getStyleClass().add("page-title");

        TextField name = new TextField();
        name.setPromptText("Goal title");
        TextField target = new TextField();
        target.setPromptText("Target amount");
        TextField saved = new TextField();
        saved.setPromptText("Saved amount");
        DatePicker deadline = new DatePicker(LocalDate.now().plusMonths(6));
        Button save = new Button("Save Goal");
        Button delete = new Button("Delete Selected");
        HBox form = new HBox(10, name, target, saved, deadline, save, delete);
        form.getStyleClass().add("toolbar");

        setupTable();
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                name.setText(selected.getTitle());
                target.setText(selected.getTargetAmount().toString());
                saved.setText(selected.getSavedAmount().toString());
                deadline.setValue(selected.getDeadline());
            }
        });
        save.setOnAction(e -> save(name.getText(), target.getText(), saved.getText(), deadline.getValue()));
        delete.setOnAction(e -> delete());
        refresh();
        page.getChildren().addAll(title, form, table);
        return page;
    }

    private void setupTable() {
        TableColumn<SavingsGoal, String> title = new TableColumn<>("Title");
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<SavingsGoal, BigDecimal> target = new TableColumn<>("Target");
        target.setCellValueFactory(new PropertyValueFactory<>("targetAmount"));
        TableColumn<SavingsGoal, BigDecimal> saved = new TableColumn<>("Saved");
        saved.setCellValueFactory(new PropertyValueFactory<>("savedAmount"));
        TableColumn<SavingsGoal, Double> progress = new TableColumn<>("Progress");
        progress.setCellValueFactory(new PropertyValueFactory<>("progress"));
        table.getColumns().setAll(title, target, saved, progress);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void save(String title, String target, String saved, LocalDate deadline) {
        try {
            SavingsGoal selected = table.getSelectionModel().getSelectedItem();
            int id = selected == null ? 0 : selected.getGoalId();
            service.save(new SavingsGoal(id, user.getUserId(), title, new BigDecimal(target), new BigDecimal(saved), deadline));
            refresh();
        } catch (Exception e) {
            UIUtil.error(e.getMessage());
        }
    }

    private void delete() {
        try {
            SavingsGoal selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                service.delete(selected.getGoalId());
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
