package com.smartbanking.ui;

import javafx.scene.control.Alert;

final class UIUtil {
    static final String[] CATEGORIES = {"Food", "Transport", "Shopping", "Bills", "Education", "Entertainment"};

    private UIUtil() {
    }

    static void info(String message) {
        alert(Alert.AlertType.INFORMATION, "Success", message);
    }

    static void error(String message) {
        alert(Alert.AlertType.ERROR, "Error", message);
    }

    static void warning(String message) {
        alert(Alert.AlertType.WARNING, "Warning", message);
    }

    private static void alert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
