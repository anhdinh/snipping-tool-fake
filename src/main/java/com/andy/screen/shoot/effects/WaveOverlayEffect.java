package com.andy.screen.shoot.effects;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class WaveOverlayEffect {

    private Canvas overlay;
    private AnimationTimer timer;
    private double offset = 0;
    private boolean running = false;

    /**
     * Tạo lớp phủ sóng trên ImageView
     */
    public void attachWave(ImageView imageView) {
        if (overlay != null) return; // tránh attach nhiều lần

        StackPane parent;
        if (imageView.getParent() instanceof StackPane) {
            parent = (StackPane) imageView.getParent();
        } else {
            // bọc ImageView vào StackPane
            parent = new StackPane(imageView);
        }

        overlay = new Canvas(imageView.getFitWidth(), imageView.getFitHeight());
        overlay.widthProperty().bind(imageView.fitWidthProperty());
        overlay.heightProperty().bind(imageView.fitHeightProperty());

        parent.getChildren().add(overlay);

        GraphicsContext gc = overlay.getGraphicsContext2D();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawWave(gc, overlay.getWidth(), overlay.getHeight());
            }
        };
    }

    private void drawWave(GraphicsContext gc, double width, double height) {
        gc.clearRect(0, 0, width, height);

        gc.setFill(Color.color(1, 1, 1, 0.2)); // lớp phủ trong suốt
        gc.beginPath();
        for (int x = 0; x < width; x++) {
            double y = height / 2 + 20 * Math.sin((x + offset) * 0.05);
            if (x == 0) gc.moveTo(x, y);
            else gc.lineTo(x, y);
        }
        gc.lineTo(width, height);
        gc.lineTo(0, height);
        gc.closePath();
        gc.fill();

        offset += 2; // tốc độ sóng
    }

    /** Bật hiệu ứng sóng */
    public void start() {
        if (timer != null && !running) {
            timer.start();
            running = true;
        }
    }

    /** Tắt hiệu ứng sóng */
    public void stop() {
        if (timer != null && running) {
            timer.stop();
            running = false;
            if (overlay != null) {
                overlay.getGraphicsContext2D().clearRect(0, 0, overlay.getWidth(), overlay.getHeight());
            }
        }
    }
}
