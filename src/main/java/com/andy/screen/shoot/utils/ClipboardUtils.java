package com.andy.screen.shoot.utils;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
public class ClipboardUtils {
    public static void copyToClipboard(String text) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }
}
