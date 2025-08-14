package com.andy.screen.shoot.effects;


import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;

public class WaveOverlayGradientEffect {

    private Canvas overlay;
    private AnimationTimer timer;
    private double offset = 0;
    private boolean running = false;

    /**
     * Gắn lớp phủ sóng với gradient shimmer vào ImageView
     */
    public void attachWave(ImageView imageView) {
        if (overlay != null) return; // tránh attach nhiều lần

        StackPane parent;
        if (imageView.getParent() instanceof StackPane) {
            parent = (StackPane) imageView.getParent();
        } else {
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

        // Gradient ánh sáng chạy
        LinearGradient gradient = new LinearGradient(
                (offset % width) / width, 0,
                ((offset % width) + 100) / width, 0,
                true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.color(1, 1, 1, 0)),
                new Stop(0.5, Color.color(1, 1, 1, 0.3)),
                new Stop(1, Color.color(1, 1, 1, 0))
        );

        gc.setFill(gradient);
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

        offset += 3; // tốc độ di chuyển của shimmer
    }

    /** Bật hiệu ứng sóng gradient */
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
