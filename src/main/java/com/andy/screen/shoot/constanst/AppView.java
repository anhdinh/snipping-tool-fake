package com.andy.screen.shoot.constanst;

import lombok.Getter;

@Getter
public enum AppView {
    MAIN("main-view.fxml", "Main"),
    ABOUT("about-view.fxml", "About"),
    SCREENSHOT("ScreenshotView.fxml", "Screenshot"),;

    private final String fxmlFile;

    private final String title;

    AppView(String fxmlFile, String title) {
        this.fxmlFile = fxmlFile;
        this.title = title;
    }

}
