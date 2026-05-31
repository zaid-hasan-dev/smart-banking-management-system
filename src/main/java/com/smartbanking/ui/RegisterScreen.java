package com.smartbanking.ui;

import com.smartbanking.service.AuthService;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterScreen {
    private final Stage stage;
    private final AuthService authService = new AuthService();

    public RegisterScreen(Stage stage) {
        this.stage = stage;
    }

    public Parent getView() {
        VBox form = new VBox(14);
        form.getStyleClass().add("auth-panel");
        form.setMaxWidth(420);
        form.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Create Account");
        title.getStyleClass().add("auth-title");
        TextField name = new TextField();
        name.setPromptText("Full name");
        TextField email = new TextField();
        email.setPromptText("Email");
        PasswordField password = new PasswordField();
        password.setPromptText("Password minimum 6 characters");

        Button register = new Button("Register");
        register.getStyleClass().add("primary-button");
        register.setMaxWidth(Double.MAX_VALUE);
        Hyperlink login = new Hyperlink("Back to login");

        register.setOnAction(e -> {
            try {
                if (authService.register(name.getText(), email.getText(), password.getText())) {
                    UIUtil.info("Registration successful. Please login.");
                    stage.getScene().setRoot(new LoginScreen(stage).getView());
                } else {
                    UIUtil.warning("Enter valid name, email and password.");
                }
            } catch (Exception ex) {
                UIUtil.error("Registration failed: " + ex.getMessage());
            }
        });
        login.setOnAction(e -> stage.getScene().setRoot(new LoginScreen(stage).getView()));

        form.getChildren().addAll(title, name, email, password, register, login);
        StackPane root = new StackPane(form);
        root.getStyleClass().add("auth-root");
        return root;
    }
}
