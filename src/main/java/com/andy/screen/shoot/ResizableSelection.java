package com.andy.screen.shoot;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ResizableSelection extends Pane {
    private final Rectangle rect;
    private final Circle topLeft, topRight, bottomLeft, bottomRight;
    private final Circle top, bottom, left, right;
    private double clickX, clickY;
    private boolean dragging = false;

    public ResizableSelection() {
        rect = new Rectangle(0, 0, 0, 0);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.BLUE);
        rect.setStrokeWidth(2);

        double handleSize = 15;
        topLeft = createHandle(handleSize, Cursor.NW_RESIZE);
        topRight = createHandle(handleSize, Cursor.NE_RESIZE);
        bottomLeft = createHandle(handleSize, Cursor.SW_RESIZE);
        bottomRight = createHandle(handleSize, Cursor.SE_RESIZE);

        top = createHandle(handleSize, Cursor.N_RESIZE);
        bottom = createHandle(handleSize, Cursor.S_RESIZE);
        left = createHandle(handleSize, Cursor.W_RESIZE);
        right = createHandle(handleSize, Cursor.E_RESIZE);

        getChildren().addAll(rect, topLeft, topRight, bottomLeft, bottomRight,
                top, bottom, left, right);

        bindHandles();

        // Drag handles
        enableDrag(topLeft, true, true);
        enableDrag(topRight, false, true);
        enableDrag(bottomLeft, true, false);
        enableDrag(bottomRight, false, false);

        enableDrag(top, false, true);
        enableDrag(bottom, false, false);
        enableDrag(left, true, false);
        enableDrag(right, false, false);

        // Drag rectangle
        rect.setOnMousePressed(e -> {
            clickX = e.getX() - rect.getX();
            clickY = e.getY() - rect.getY();
            rect.setCursor(Cursor.MOVE);
            dragging = true;
            e.consume();
        });
        rect.setOnMouseDragged(e -> {
            if (!dragging) return;
            double newX = e.getX() - clickX;
            double newY = e.getY() - clickY;
            rect.setX(newX);
            rect.setY(newY);
            e.consume();
        });
        rect.setOnMouseReleased(e -> {
            dragging = false;
            e.consume();
        });
        
        // Drag to create new selection
        setOnMousePressed(this::startSelection);
        setOnMouseDragged(this::dragSelection);
    }

    public ResizableSelection(double x, double y, double width, double height) {
        this(); // gọi constructor mặc định để setup handles, drag...
        rect.setX(x);
        rect.setY(y);
        rect.setWidth(width);
        rect.setHeight(height);
    }

    private void startSelection(MouseEvent e) {
        rect.setX(e.getX());
        rect.setY(e.getY());
        rect.setWidth(0);
        rect.setHeight(0);
        rect.setVisible(true);
    }

    private void dragSelection(MouseEvent e) {
        double x = Math.min(e.getX(), rect.getX());
        double y = Math.min(e.getY(), rect.getY());
        double w = Math.abs(e.getX() - rect.getX());
        double h = Math.abs(e.getY() - rect.getY());
        rect.setX(x);
        rect.setY(y);
        rect.setWidth(w);
        rect.setHeight(h);
    }

    private Circle createHandle(double size, Cursor cursor) {
        Circle c = new Circle(size / 2, Color.RED);
        c.setCursor(cursor);
        return c;
    }

    private void bindHandles() {
        // Corners
        topLeft.centerXProperty().bind(rect.xProperty());
        topLeft.centerYProperty().bind(rect.yProperty());

        topRight.centerXProperty().bind(rect.xProperty().add(rect.widthProperty()));
        topRight.centerYProperty().bind(rect.yProperty());

        bottomLeft.centerXProperty().bind(rect.xProperty());
        bottomLeft.centerYProperty().bind(rect.yProperty().add(rect.heightProperty()));

        bottomRight.centerXProperty().bind(rect.xProperty().add(rect.widthProperty()));
        bottomRight.centerYProperty().bind(rect.yProperty().add(rect.heightProperty()));

        // Edges
        top.centerXProperty().bind(rect.xProperty().add(rect.widthProperty().divide(2)));
        top.centerYProperty().bind(rect.yProperty());

        bottom.centerXProperty().bind(rect.xProperty().add(rect.widthProperty().divide(2)));
        bottom.centerYProperty().bind(rect.yProperty().add(rect.heightProperty()));

        left.centerXProperty().bind(rect.xProperty());
        left.centerYProperty().bind(rect.yProperty().add(rect.heightProperty().divide(2)));

        right.centerXProperty().bind(rect.xProperty().add(rect.widthProperty()));
        right.centerYProperty().bind(rect.yProperty().add(rect.heightProperty().divide(2)));
    }

    private void enableDrag(Circle handle, boolean isLeft, boolean isTop) {
        handle.setOnMousePressed(MouseEvent::consume);
        handle.setOnMouseDragged(e -> {
            double newX = e.getX();
            double newY = e.getY();

            if (isLeft) {
                double deltaX = rect.getX() - newX;
                double newWidth = rect.getWidth() + deltaX;
                if (newWidth > 0) {
                    rect.setX(newX);
                    rect.setWidth(newWidth);
                }
            } else { // isRight
                double newWidth = newX - rect.getX();
                if (newWidth > 0) rect.setWidth(newWidth);
            }

            if (isTop) {
                double deltaY = rect.getY() - newY;
                double newHeight = rect.getHeight() + deltaY;
                if (newHeight > 0) {
                    rect.setY(newY);
                    rect.setHeight(newHeight);
                }
            } else { // isBottom
                double newHeight = newY - rect.getY();
                if (newHeight > 0) rect.setHeight(newHeight);
            }
            e.consume();
        });
    }

    public Rectangle getRectangle() {
        return rect;
    }
}