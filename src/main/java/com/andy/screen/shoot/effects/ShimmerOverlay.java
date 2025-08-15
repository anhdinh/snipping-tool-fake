package com.andy.screen.shoot.effects;


import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ShimmerOverlay {

    private final Node targetNode;
    private final ColorInput shimmerEffect;
    private final Timeline timeline;

    public ShimmerOverlay(Node node) {
        this.targetNode = node;

        shimmerEffect = new ColorInput(0, 0, node.getBoundsInParent().getWidth(),
                node.getBoundsInParent().getHeight(), Color.TRANSPARENT);

        Blend blend = new Blend();
        blend.setMode(BlendMode.SRC_OVER);
        blend.setTopInput(shimmerEffect);
        node.setEffect(blend);

        timeline = createShimmerTimeline();
    }

    private Timeline createShimmerTimeline() {
        Timeline tl = new Timeline();
        tl.setCycleCount(Animation.INDEFINITE);

        KeyFrame startFrame = new KeyFrame(Duration.ZERO,
                new KeyValue(shimmerEffect.paintProperty(), Color.web("#ffffff00")));

        KeyFrame midFrame = new KeyFrame(Duration.seconds(0.5),
                new KeyValue(shimmerEffect.paintProperty(), Color.web("#ffffffff")));

        KeyFrame endFrame = new KeyFrame(Duration.seconds(1),
                new KeyValue(shimmerEffect.paintProperty(), Color.web("#ffffff00")));

        tl.getKeyFrames().addAll(startFrame, midFrame, endFrame);
        return tl;
    }

    public void start(Runnable afterFinished,Duration duration) {
        if (timeline.getStatus() != Animation.Status.RUNNING) {
            timeline.play();

            // Đảm bảo chạy ít nhất 2 giây
            PauseTransition pause = new PauseTransition(duration);
            pause.setOnFinished(e -> {
                if (afterFinished != null) {
                    afterFinished.run(); // Chạy hành động tiếp theo
                }
            });
            pause.play();
        }
    }

    public void start(Runnable afterFinishedn) {
        if (timeline.getStatus() != Animation.Status.RUNNING) {
            timeline.play();

            // Đảm bảo chạy ít nhất 2 giây
            PauseTransition pause = new PauseTransition(Duration.seconds(0));
            pause.setOnFinished(e -> {
                if (afterFinishedn != null) {
                    afterFinishedn.run(); // Chạy hành động tiếp theo
                }
            });
            pause.play();
        }
    }

    public void stop() {
        if (timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.stop();
            shimmerEffect.setPaint(Color.TRANSPARENT);
        }
    }
}


