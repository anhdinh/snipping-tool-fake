package com.andy.screen.shoot.objects;

import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class WindowInfo {
    private final Stage stage;
    private final Object controller;

    public WindowInfo(Stage stage, Object controller) {
        this.stage = stage;
        this.controller = controller;
    }

}
