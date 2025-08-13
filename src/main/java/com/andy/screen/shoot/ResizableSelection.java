package com.andy.screen.shoot;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ResizableSelection extends Pane {
    private final Rectangle rect;
    private final Circle topLeft, topRight, bottomLeft, bottomRight; // Corner handles
    // Edge handles are removed
    private double clickX, clickY;
    // Replaced by dragMode

    // Defines the state of the drag operation
    private enum DragMode { NONE, MOVE, RESIZE_N, RESIZE_S, RESIZE_W, RESIZE_E }
    private DragMode dragMode = DragMode.NONE;

    // The margin around the edges where resizing is possible
    private static final int RESIZE_MARGIN = 5;

    public ResizableSelection() {
        // SỬA LỖI 1: Đặt nền trong suốt để Pane có thể bắt sự kiện chuột trên toàn bộ diện tích của nó.
        // Điều này cho phép tạo vùng chọn mới bằng cách kéo thả ở bất kỳ đâu.
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));


        rect = new Rectangle(0, 0, 0, 0);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.BLUE);
        rect.setStrokeWidth(2);

        double handleSize = 15;
        topLeft = createHandle(handleSize, Cursor.NW_RESIZE);
        topRight = createHandle(handleSize, Cursor.NE_RESIZE);
        bottomLeft = createHandle(handleSize, Cursor.SW_RESIZE);
        bottomRight = createHandle(handleSize, Cursor.SE_RESIZE);

        // Edge handles are removed, resizing is now done by dragging the rectangle's edges.

        // Add only the rectangle and corner handles to the pane
        getChildren().addAll(rect, topLeft, topRight, bottomLeft, bottomRight);

        bindHandles();

        // SỬA LỖI 2: Cập nhật lại các lệnh gọi enableDrag để xử lý đúng các cạnh và góc
        // Kéo thả các điểm điều khiển ở góc (thay đổi cả X và Y)
        enableDrag(topLeft, true, true, true, true);
        enableDrag(topRight, true, true, false, true);
        enableDrag(bottomLeft, true, true, true, false);
        enableDrag(bottomRight, true, true, false, false);

        // Set up event handlers for moving and resizing the rectangle by its edges
        setupRectangleHandlers();
        
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
    }

    // SỬA LỖI 2: Cập nhật lại phương thức enableDrag để xử lý riêng biệt các trục X và Y
    private void enableDrag(Circle handle, boolean canResizeX, boolean canResizeY, boolean isLeft, boolean isTop) {
        handle.setOnMousePressed(MouseEvent::consume);
        handle.setOnMouseDragged(e -> {
            if (canResizeX) {
                double newX = e.getX();
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
            }

            if (canResizeY) {
                double newY = e.getY();
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
            }
            e.consume();
        });
    }

    private void setupRectangleHandlers() {
        // Change cursor when moving over the rectangle's edges
        rect.setOnMouseMoved(e -> {
            if (rect.getWidth() > 0 && rect.getHeight() > 0) {
                Cursor cursor = getCursorForPosition(e.getX(), e.getY());
                rect.setCursor(cursor);
            }
        });

        rect.setOnMousePressed(e -> {
            Cursor cursor = getCursorForPosition(e.getX(), e.getY());
            if (cursor == Cursor.MOVE) {
                dragMode = DragMode.MOVE;
                // Store the offset of the click from the rectangle's top-left corner
                clickX = e.getX() - rect.getX();
                clickY = e.getY() - rect.getY();
            } else if (cursor == Cursor.N_RESIZE) {
                dragMode = DragMode.RESIZE_N;
            } else if (cursor == Cursor.S_RESIZE) {
                dragMode = DragMode.RESIZE_S;
            } else if (cursor == Cursor.W_RESIZE) {
                dragMode = DragMode.RESIZE_W;
            } else if (cursor == Cursor.E_RESIZE) {
                dragMode = DragMode.RESIZE_E;
            } else {
                dragMode = DragMode.NONE;
            }
            e.consume();
        });

        rect.setOnMouseDragged(e -> {
            double newX = e.getX();
            double newY = e.getY();

            switch (dragMode) {
                case MOVE:
                    rect.setX(newX - clickX);
                    rect.setY(newY - clickY);
                    break;
                case RESIZE_N:
                    double deltaY = rect.getY() - newY;
                    double newHeight = rect.getHeight() + deltaY;
                    if (newHeight > 0) {
                        rect.setY(newY);
                        rect.setHeight(newHeight);
                    }
                    break;
                case RESIZE_S:
                    double newHeightS = newY - rect.getY();
                    if (newHeightS > 0) {
                        rect.setHeight(newHeightS);
                    }
                    break;
                case RESIZE_W:
                    double deltaX = rect.getX() - newX;
                    double newWidth = rect.getWidth() + deltaX;
                    if (newWidth > 0) {
                        rect.setX(newX);
                        rect.setWidth(newWidth);
                    }
                    break;
                case RESIZE_E:
                    double newWidthE = newX - rect.getX();
                    if (newWidthE > 0) {
                        rect.setWidth(newWidthE);
                    }
                    break;
                default:
                    break;
            }
            e.consume();
        });

        rect.setOnMouseReleased(e -> {
            dragMode = DragMode.NONE;
            rect.setCursor(Cursor.DEFAULT); // Reset cursor
            e.consume();
        });
    }

    private Cursor getCursorForPosition(double x, double y) {
        double rX = rect.getX();
        double rY = rect.getY();
        double rW = rect.getWidth();
        double rH = rect.getHeight();

        if (y >= rY && y <= rY + RESIZE_MARGIN) return Cursor.N_RESIZE;
        if (y >= rY + rH - RESIZE_MARGIN && y <= rY + rH) return Cursor.S_RESIZE;
        if (x >= rX && x <= rX + RESIZE_MARGIN) return Cursor.W_RESIZE;
        if (x >= rX + rW - RESIZE_MARGIN && x <= rX + rW) return Cursor.E_RESIZE;

        // If not on an edge, it's for moving.
        return Cursor.MOVE;
    }

    public Rectangle getRectangle() {
        return rect;
    }
}