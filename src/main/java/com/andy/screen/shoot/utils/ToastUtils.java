package com.andy.screen.shoot.utils;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class ToastUtils {


    public static void showInfo(String title, String text) {
        Notifications.create()
                .title(title)
                .text(text)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(3))
                .showInformation();
    }


    public static void showWarning(String title, String text) {
        Notifications.create()
                .title(title)
                .text(text)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(3))
                .showWarning();
    }


    public static void showError(String title, String text) {
        Notifications.create()
                .title(title)
                .text(text)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(3))
                .showError();
    }
}
