package com.andy.screen.shoot.constanst;

import com.andy.screen.shoot.WindowInfo;
import com.andy.screen.shoot.WindowManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;

public class ViewUtils {
    public static <T> T openViewModal(AppView view) {
        try {
            if (WindowManager.WINDOWS.containsKey(view)) {
                WindowInfo info = WindowManager.WINDOWS.get(view);
                if (info.getStage().isShowing()) {
                    info.getStage().toFront();
                    return (T) info.getController();
                }
            }
            FXMLLoader loader = new FXMLLoader(ViewUtils.class.getResource("/com/andy/screen/shoot/" + view.getFxmlFile()));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(view.getTitle());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            T controller = loader.getController();
            WindowManager.WINDOWS.put(view, new WindowInfo(stage, controller));
            stage.setOnCloseRequest(event -> WindowManager.WINDOWS.remove(view));
            stage.showAndWait();
            return controller;

        } catch (IOException e) {
            throw new RuntimeException("ViewUtils#openViewModal", e);
        }
    }
}
