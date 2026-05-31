package com.smartbanking.ui;

import com.smartbanking.model.User;
import com.smartbanking.service.AuthService;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginScreen {
    private final Stage stage;
    private final AuthService authService = new AuthService();

    public LoginScreen(Stage stage) {
        this.stage = stage;
    }

    public Parent getView() {
        VBox form = new VBox(14);
        form.getStyleClass().add("auth-panel");
        form.setMaxWidth(420);
        form.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Smart Banking");
        title.getStyleClass().add("auth-title");
        Label subtitle = new Label("Login to manage banking, expenses, budgets and goals.");
        subtitle.getStyleClass().add("muted");

        TextField email = new TextField();
        email.setPromptText("Email");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Button login = new Button("Login");
        login.getStyleClass().add("primary-button");
        login.setMaxWidth(Double.MAX_VALUE);

        Hyperlink register = new Hyperlink("Create new account");

        login.setOnAction(e -> {
            try {
                var user = authService.login(email.getText(), password.getText());
                if (user.isPresent()) openDashboard(user.get());
                else UIUtil.error("Invalid email or password.");
            } catch (Exception ex) {
                UIUtil.error("Database connection failed: " + ex.getMessage());
            }
        });
        register.setOnAction(e -> stage.getScene().setRoot(new RegisterScreen(stage).getView()));

        form.getChildren().addAll(title, subtitle, email, password, login, register);
        StackPane root = new StackPane(form);
        root.getStyleClass().add("auth-root");
        return root;
    }

    private void openDashboard(User user) {
        Scene scene = stage.getScene();
        scene.setRoot(new Dashboard(stage, user).getView());
    }
}
