package com.andy.screen.shoot;

import com.andy.screen.shoot.about.AboutController;
import com.andy.screen.shoot.constanst.AppView;
import com.andy.screen.shoot.constanst.ViewUtils;
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
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public ScrollPane scrollPane;

    @FXML
    private ImageView imageView;

    @FXML
    private Button newButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("register");
        AppEventBus.getInstance().register(this);
        System.out.println("register done");
    }

    @FXML
    protected void onNewButtonClick() {
        newButton.setVisible(false);
        newAction();
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
        System.out.println(event.getData());
    }


    public void newAction() {
        Stage primaryStage = (Stage) newButton.getScene().getWindow();
        Platform.runLater(() -> SnippingTool.startSnipping(primaryStage,imageView));
    }
}
