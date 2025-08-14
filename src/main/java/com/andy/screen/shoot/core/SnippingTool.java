package com.andy.screen.shoot.core;

import com.andy.screen.shoot.event.AppEventBus;
import com.andy.screen.shoot.event.ScreenOverlayCloseEvent;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
public class SnippingTool {

    public static void startSnipping(Stage primaryStage, ImageView targetImageView) {
        Platform.runLater(() -> {
            primaryStage.setIconified(true);
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(event -> captureAndShowOverlay(primaryStage, targetImageView));
            pause.play();
        });
    }

    private static void captureAndShowOverlay(Stage primaryStage, ImageView targetImageView) {
        try {
            BufferedImage screenCapture = new Robot().createScreenCapture(
                    new java.awt.Rectangle(Toolkit.getDefaultToolkit().getScreenSize())
            );
            Image fxImage = SwingFXUtils.toFXImage(screenCapture, null);
            showOverlay(primaryStage, fxImage, targetImageView);
        } catch (AWTException ex) {
            Platform.runLater(() -> primaryStage.setIconified(false));
        }
    }

    private static void showOverlay(Stage primaryStage, Image fxImage, ImageView targetImageView) {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        ImageView imageView = new ImageView(fxImage);
        imageView.setFitWidth(screenWidth);
        imageView.setFitHeight(screenHeight);

        double selWidth = 200;
        double selHeight = 150;
        double selX = (screenWidth - selWidth) / 2;
        double selY = (screenHeight - selHeight) / 2;
        ResizableSelection selection = new ResizableSelection(selX, selY, selWidth, selHeight);
        Rectangle selectionRect = selection.getRectangle();

        // Tạo overlay mờ xung quanh vùng chọn
        Color dimColor = Color.rgb(0, 0, 0, 0.5);
        Rectangle top = new Rectangle();
        Rectangle bottom = new Rectangle();
        Rectangle left = new Rectangle();
        Rectangle right = new Rectangle();

        top.setFill(dimColor);
        bottom.setFill(dimColor);
        left.setFill(dimColor);
        right.setFill(dimColor);

        left.xProperty().set(0);
        left.yProperty().set(0);
        left.widthProperty().bind(selectionRect.xProperty());
        left.heightProperty().set(screenHeight);

        right.xProperty().bind(selectionRect.xProperty().add(selectionRect.widthProperty()));
        right.yProperty().set(0);
        right.widthProperty().bind(Bindings.createDoubleBinding(
                () -> screenWidth - (selectionRect.getX() + selectionRect.getWidth()),
                selectionRect.xProperty(), selectionRect.widthProperty()
        ));
        right.heightProperty().set(screenHeight);

        top.xProperty().bind(selectionRect.xProperty());
        top.yProperty().set(0);
        top.widthProperty().bind(selectionRect.widthProperty());
        top.heightProperty().bind(selectionRect.yProperty());

        bottom.xProperty().bind(selectionRect.xProperty());
        bottom.yProperty().bind(selectionRect.yProperty().add(selectionRect.heightProperty()));
        bottom.widthProperty().bind(selectionRect.widthProperty());
        bottom.heightProperty().bind(Bindings.createDoubleBinding(
                () -> screenHeight - (selectionRect.getY() + selectionRect.getHeight()),
                selectionRect.yProperty(), selectionRect.heightProperty()
        ));

        Pane overlayRoot = new Pane(imageView, left, right, top, bottom, selection);
        Scene overlayScene = new Scene(overlayRoot, screenWidth, screenHeight, Color.TRANSPARENT);

        Stage overlayStage = new Stage();
        overlayStage.initStyle(StageStyle.TRANSPARENT);
        overlayStage.setAlwaysOnTop(true);
        overlayStage.setX(0);
        overlayStage.setY(0);
        overlayStage.setWidth(screenWidth);
        overlayStage.setHeight(screenHeight);
        overlayStage.setScene(overlayScene);


        // Key handlers
        overlayScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                overlayStage.close();
                primaryStage.setIconified(false);
                //fireOverlayCloseEvent();
            } else if (e.getCode() == KeyCode.ENTER) {
                cropAndShow(fxImage, selection.getRectangle(), targetImageView);
                overlayStage.close();
                primaryStage.setIconified(false);
                fireOverlayCloseEvent();
            }
        });

        overlayStage.show();
    }

    public static void fireOverlayCloseEvent(){
        AppEventBus.getInstance().post(new ScreenOverlayCloseEvent("close"));
    }

    private static void cropAndShow(Image fxImage, Rectangle fxRect, ImageView targetImageView) {
        try {
            // Convert JavaFX Rectangle to AWT Rectangle
            java.awt.Rectangle awtRect = new java.awt.Rectangle(
                    (int) fxRect.getX(),
                    (int) fxRect.getY(),
                    (int) fxRect.getWidth(),
                    (int) fxRect.getHeight()
            );

            // Convert FX Image -> BufferedImage
            BufferedImage buffered = SwingFXUtils.fromFXImage(fxImage, null);

            // Copy pixel data để tránh lỗi getSubimage trên Linux/HiDPI
            BufferedImage cropped = new BufferedImage(
                    awtRect.width,
                    awtRect.height,
                    buffered.getType()
            );
            cropped.getGraphics().drawImage(
                    buffered,
                    0, 0, awtRect.width, awtRect.height,
                    awtRect.x, awtRect.y, awtRect.x + awtRect.width, awtRect.y + awtRect.height,
                    null
            );

            // Lưu file crop
//            File outFile = new File("crop.png");
//            ImageIO.write(cropped, "png", outFile);
//            System.out.println("Saved crop.png");

            // Chuyển sang FX Image để hiển thị
            Image croppedFxImage = SwingFXUtils.toFXImage(cropped, null);

            if (targetImageView != null) {
                Platform.runLater(() -> {
                    targetImageView.setVisible(true);
                    targetImageView.setPreserveRatio(true);
                    targetImageView.setImage(croppedFxImage);
                    targetImageView.setFitWidth(croppedFxImage.getWidth());
                    targetImageView.setFitHeight(croppedFxImage.getHeight());
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
