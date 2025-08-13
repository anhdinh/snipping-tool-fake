package com.andy.screen.shoot;

import javafx.stage.Stage;

public class WindowInfo {
    private final Stage stage;
    private final Object controller;

    public WindowInfo(Stage stage, Object controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public Stage getStage() {
        return stage;
    }

    public Object getController() {
        return controller;
    }
}
