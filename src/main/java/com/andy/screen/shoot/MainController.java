package com.andy.screen.shoot;

import com.andy.screen.shoot.about.AboutController;
import com.andy.screen.shoot.constanst.AppView;
import com.andy.screen.shoot.constanst.ViewUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private Button newButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    protected void onNewButtonClick() {
        // Lấy Stage chính từ bất kỳ node nào trên Scene (ở đây là Button)
        Stage primaryStage = (Stage) newButton.getScene().getWindow();
        Platform.runLater(() -> SnippingTool.startSnipping(primaryStage,imageView));

    }


    @FXML
    private void onNewClick() {
        newAction();
    }

    @FXML
    private void onSaveClick() {
        System.out.println("Save menu clicked");
    }

    @FXML
    private void onExitClick() {
        System.exit(0);
    }

    public void onCutClick(ActionEvent actionEvent) {
    }

    public void onCopyClick(ActionEvent actionEvent) {
    }

    public void onPasteClick(ActionEvent actionEvent) {
    }

    public void onAboutClick(ActionEvent actionEvent) {
        AboutController aboutController = ViewUtils.openViewModal(AppView.ABOUT);
    }


    public void newAction() {
        String path = "~/Documents/.sceenshot".replaceFirst("^~", System.getProperty("user.home"));
        File folder = new File(path);
        File[] files = folder.listFiles((dir, name) ->
                name.matches("^Screenshot from \\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\\.png$"));
        if (files != null && files.length > 0) {
            File newest = Arrays.stream(files)
                    .max(Comparator.comparingLong(File::lastModified))
                    .orElse(files[0]);
            Image img = new Image(newest.toURI().toString());
            imageView.setImage(img);
            imageView.setVisible(true);
            Stage stage = (Stage) imageView.getScene().getWindow();
            double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
            double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

            double imgWidth = img.getWidth();
            double imgHeight = img.getHeight();

            double targetWidth = Math.min(imgWidth + 50, screenWidth * 0.9);
            double targetHeight = Math.min(imgHeight + 100, screenHeight * 0.9);

            stage.setWidth(targetWidth);
            stage.setHeight(targetHeight);
        } else {
            System.out.println("Không tìm thấy ảnh screenshot trong thư mục: " + path);
        }
    }
}
