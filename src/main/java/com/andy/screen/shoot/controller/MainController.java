package com.andy.screen.shoot.controller;

import com.andy.screen.shoot.constanst.AppView;
import com.andy.screen.shoot.core.SnippingTool;
import com.andy.screen.shoot.effects.ShimmerOverlay;
import com.andy.screen.shoot.utils.*;
import com.andy.screen.shoot.event.AppEventBus;
import com.andy.screen.shoot.event.ScreenOverlayCloseEvent;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.util.Duration;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections.CollectionUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Log4j
public class MainController implements Initializable {
    @FXML
    public ScrollPane scrollPane;

    @FXML
    public Button imageToText;

    @FXML
    public Button readQr;


    public StackPane stackPaneImageContainer;

    public Button copyButton;

    @FXML
    private ImageView imageView;

    @FXML
    private Button newButton;

    private ShimmerOverlay effect;

    private Pane overlayPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("MainController#initialize");
        AppEventBus.getInstance().register(this);
        hideFeatures();
    }

    @FXML
    protected void onNewButtonClick() {
        newButton.setVisible(false);
        hideFeatures();
        onClickNewButton();
    }


    @FXML
    private void onNewClick() {
        onClickNewButton();
        if (overlayPane != null) {
            overlayPane.setVisible(false);
            overlayPane = null;
        }
    }

    @FXML
    private void onSaveClick() {
        System.out.println("Save menu clicked");
    }

    @FXML
    private void onExitClick() {
        AppEventBus.getInstance().unregister(this);
        System.exit(0);
    }

    public void onCutClick(ActionEvent actionEvent) throws InterruptedException {
        effect = new ShimmerOverlay(imageView);
        effect.start(() -> {
            effect.stop();
        },Duration.seconds(5));
    }

    public void onCopyClick(ActionEvent actionEvent) {
        ClipboardContent content = new ClipboardContent();
        content.putImage(imageView.getImage());
        Clipboard.getSystemClipboard().setContent(content);
        copyButton.setDisable(true);
        ToastUtils.showInfo("Info", "Copied to clipboard!");
    }

    public void onPasteClick(ActionEvent actionEvent) {
    }

    public void onAboutClick(ActionEvent actionEvent) {
        AboutController aboutController = ViewUtils.openViewModal(AppView.ABOUT);
    }

    @Subscribe
    public void onOverlayClose(ScreenOverlayCloseEvent event) {
        showFeatures();
        log.info("capture image success!");
    }


    @SneakyThrows
    public void onImageToText(ActionEvent actionEvent) {

        initOverLay();
        effect = new ShimmerOverlay(imageView);
        effect.start(() -> {
                    System.out.println("onImageToText");
                    if (!OCRUtils.isTesseractInstalled()) {
                        overlayPane.setVisible(false);
                        ToastUtils.showError("Error", "Please install tesseract first!");
                        return;
                    }

                    var listTexts = OCRUtils.imageViewToLines(imageView);
                    if (CollectionUtils.isEmpty(listTexts)) {
                        ToastUtils.showInfo("Info", "No text found!");
                        return;
                    }
                imageToText.setDisable(true);
                drawOCRFields(listTexts, overlayPane);
        //vẽ các texfield lên trên overlayPane ở đây
        effect.stop();
    },Duration.seconds(2));


    //TODO tạo lớp phủ và đính những texarea trên imageView chứa text ở đây
//        if(StringUtils.isEmpty(text)){
//            ToastUtils.showInfo("Info", "No text found!");
//            return;
//        }
//        ClipboardUtils.copyToClipboard(text);
//        ToastUtils.showInfo("Info", "Copied to clipboard!");


}

public void onReadQr(ActionEvent actionEvent) {
    log.info("onReadQr");
    readQr.setDisable(true);
    var qr = QRUtils.readQR(imageView);
    if (qr == null) {
        ToastUtils.showError("Read QR Code", "Not QR code found");
        return;
    }

    ClipboardUtils.copyToClipboard(qr);
    ToastUtils.showInfo("Read QR Code", "Copied to clipboard");


}

public void showFeatures() {
    scrollPane.setVisible(true);
    readQr.setVisible(true);
    imageToText.setVisible(true);
    copyButton.setVisible(true);
    copyButton.setDisable(false);
    imageToText.setDisable(false);
    readQr.setDisable(false);
}

public void hideFeatures() {
    scrollPane.setVisible(false);
    readQr.setVisible(false);
    imageToText.setVisible(false);
    copyButton.setVisible(false);
}

public void onClickNewButton() {
    Stage primaryStage = (Stage) newButton.getScene().getWindow();
    Platform.runLater(() -> SnippingTool.startSnipping(primaryStage, imageView));
}

public void initOverLay() {
    if (overlayPane != null && overlayPane.getChildren() != null) {
        overlayPane.getChildren().clear();
    }
    overlayPane = new Pane();
    overlayPane.getChildren().clear();
    overlayPane.setVisible(true);
    overlayPane.setStyle("-fx-background-color: rgba(255,255,255,0.05);");
    overlayPane.setMouseTransparent(false);
    overlayPane.prefWidthProperty().bind(imageView.fitWidthProperty());
    overlayPane.prefHeightProperty().bind(imageView.fitHeightProperty());
    stackPaneImageContainer.prefWidthProperty().bind(imageView.fitWidthProperty());
    stackPaneImageContainer.prefHeightProperty().bind(imageView.fitHeightProperty());
    stackPaneImageContainer.getChildren().add(overlayPane);

}

private void drawOCRFields(List<OCRUtils.OCRResult> results, Pane overlayPane) {
    overlayPane.getChildren().clear();

    for (OCRUtils.OCRResult r : results) {
        TextField tf = new TextField(r.getText());
        tf.setEditable(false);
        tf.setDisable(false);
        tf.setPrefWidth(r.getWidth());
        tf.setPrefHeight(r.getHeight());
        tf.setLayoutX(r.getX());
        tf.setLayoutY(r.getY());
        tf.setStyle("-fx-padding: 0 0 0 0;-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: transparent;");

        tf.setOnMouseClicked(e -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(r.getText());
            Clipboard.getSystemClipboard().setContent(content);
        });

        overlayPane.getChildren().add(tf);
    }
}
}




