package com.smartbanking.ui;

import com.smartbanking.dao.UserDAO;
import com.smartbanking.model.User;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class AdminScreen {
    private final User currentUser;
    private final UserDAO userDAO = new UserDAO();
    private final TableView<User> table = new TableView<>();

    public AdminScreen(User currentUser) {
        this.currentUser = currentUser;
    }

    public Parent getView() {
        VBox page = new VBox(16);
        page.setPadding(new Insets(24));
        page.getStyleClass().add("page");

        Label title = new Label("Admin Panel");
        title.getStyleClass().add("page-title");
        Button disable = new Button("Disable Selected User");
        Button enable = new Button("Enable Selected User");
        HBox actions = new HBox(10, disable, enable);
        actions.getStyleClass().add("toolbar");

        setupTable();
        disable.setOnAction(e -> setActive(false));
        enable.setOnAction(e -> setActive(true));
        refresh();
        page.getChildren().addAll(title, actions, table);
        return page;
    }

    private void setupTable() {
        TableColumn<User, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("userId"));
        TableColumn<User, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<User, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<User, String> role = new TableColumn<>("Role");
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        TableColumn<User, Boolean> active = new TableColumn<>("Active");
        active.setCellValueFactory(new PropertyValueFactory<>("active"));
        table.getColumns().setAll(id, name, email, role, active);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setActive(boolean active) {
        try {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            if (selected.getUserId() == currentUser.getUserId()) {
                UIUtil.warning("You cannot disable your own admin account.");
                return;
            }
            userDAO.setActive(selected.getUserId(), active);
            refresh();
        } catch (Exception e) {
            UIUtil.error(e.getMessage());
        }
    }

    private void refresh() {
        try {
            table.setItems(FXCollections.observableArrayList(userDAO.findAll()));
        } catch (Exception e) {
            UIUtil.error(e.getMessage());
        }
    }
}
