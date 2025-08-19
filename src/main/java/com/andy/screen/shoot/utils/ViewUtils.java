package com.andy.screen.shoot.utils;

import com.andy.screen.shoot.objects.WindowInfo;
import com.andy.screen.shoot.core.WindowManager;
import com.andy.screen.shoot.constanst.AppView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;


import javax.management.Notification;
import java.io.IOException;

public class ViewUtils {
    public static <T> T openViewModal(AppView view) {
        try {
            // ... (phần code kiểm tra cửa sổ đã tồn tại)

            FXMLLoader loader = new FXMLLoader(ViewUtils.class.getResource("/com/andy/screen/shoot/" + view.getFxmlFile()));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(view.getTitle());

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);

            T controller = loader.getController();

            WindowManager.WINDOWS.put(view, new WindowInfo(stage, controller));

            stage.setOnCloseRequest(event -> WindowManager.WINDOWS.remove(view));

            // Dùng show() thay vì showAndWait() để cửa sổ được render
            stage.show();

            // Sử dụng Platform.runLater() để đảm bảo kích thước cửa sổ đã được tính toán
            Platform.runLater(() -> {
                Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
                double centerX = primaryScreenBounds.getMinX() + (primaryScreenBounds.getWidth() - stage.getWidth()) / 2;
                double centerY = primaryScreenBounds.getMinY() + (primaryScreenBounds.getHeight() - stage.getHeight()) / 2;
                stage.setX(centerX);
                stage.setY(centerY);
            });

            // Bạn có thể dùng showAndWait() ở đây nếu muốn đợi cửa sổ đóng
            // stage.showAndWait();

            return controller;
        } catch (IOException e) {
            throw new RuntimeException("ViewUtils#openViewModal", e);
        }
    }
}

