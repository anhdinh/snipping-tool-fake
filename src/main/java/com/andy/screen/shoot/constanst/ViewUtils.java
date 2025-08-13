package com.andy.screen.shoot.constanst;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ViewUtils {
    public static <T> T openView(@NotNull AppView view) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewUtils.class.getResource("/com/andy/screen/shoot/"+view.getFxmlFile()));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(view.getTitle());
            stage.setScene(new Scene(root));
            stage.show();
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("ViewUtils#openView", e);
        }
    }
}
