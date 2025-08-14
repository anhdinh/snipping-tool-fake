package com.andy.screen.shoot.controller;

import com.andy.screen.shoot.constanst.AppView;
import com.andy.screen.shoot.core.SnippingTool;
import com.andy.screen.shoot.utils.*;
import com.andy.screen.shoot.event.AppEventBus;
import com.andy.screen.shoot.event.ScreenOverlayCloseEvent;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class MainController implements Initializable {
    @FXML
    public ScrollPane scrollPane;

    @FXML
    public Button imageToText;

    @FXML
    public Button readQr;

    @FXML
    private ImageView imageView;

    @FXML
    private Button newButton;

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

    public void onCutClick(ActionEvent actionEvent) {

    }

    public void onCopyClick(ActionEvent actionEvent) {
    }

    public void onPasteClick(ActionEvent actionEvent) {
    }

    public void onAboutClick(ActionEvent actionEvent) {
        AboutController aboutController = ViewUtils.openViewModal(AppView.ABOUT);
    }

    @Subscribe
    public void onOverlayClose(ScreenOverlayCloseEvent event){
        showFeatures();
        log.info("capture image success!");
    }


    @SneakyThrows
    public void onImageToText(ActionEvent actionEvent) {
        System.out.println("onImageToText");
        if(!OCRUtils.isTesseractInstalled()){
            ToastUtils.showError("Error", "Please install tesseract first!");
            return;
        }
        String text = OCRUtils.imageViewToText(imageView);
        if(StringUtils.isEmpty(text)){
            ToastUtils.showInfo("Info", "No text found!");
            return;
        }
        ClipboardUtils.copyToClipboard(text);
        ToastUtils.showInfo("Info", "Copied to clipboard!");
    }

    public void onReadQr(ActionEvent actionEvent) {
        log.info("onReadQr");
        var qr = QRUtils.readQR(imageView);
        if(qr==null){
            ToastUtils.showError("Read QR Code", "Not QR code found");
            return;
        }
        ClipboardUtils.copyToClipboard(qr);
        ToastUtils.showInfo("Read QR Code", "Copied to clipboard");

    }

    public void showFeatures(){
        scrollPane.setVisible(true);
        readQr.setVisible(true);
        imageToText.setVisible(true);
    }

    public void hideFeatures(){
        scrollPane.setVisible(false);
        readQr.setVisible(false);
        imageToText.setVisible(false);
    }

    public void onClickNewButton() {
        Stage primaryStage = (Stage) newButton.getScene().getWindow();
        Platform.runLater(() -> SnippingTool.startSnipping(primaryStage,imageView));
    }

}
