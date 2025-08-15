package com.andy.screen.shoot.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OCRUtils {

    /**
     * Chuyển ImageView sang text bằng Tess4J.
     *
     * @param imageView ImageView
     * @return Text in ImageView
     * @throws TesseractException if OCR has errors
     */
    public static String imageViewToText(ImageView imageView) throws TesseractException {
        if (imageView == null || imageView.getImage() == null) {
            return "";
        }
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);

        // Thiết lập đường dẫn thư viện native (Linux)
        System.setProperty("jna.library.path", "/usr/lib/x86_64-linux-gnu");
        ITesseract tesseract = new Tesseract();
        // tessdata
        tesseract.setDatapath("/usr/share/tesseract-ocr/5/tessdata");
        tesseract.setLanguage("eng");
        return tesseract.doOCR(bufferedImage);
    }

    public static boolean isTesseractInstalled() {
        try {
            Process process = new ProcessBuilder("tesseract", "--version").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null && line.toLowerCase().contains("tesseract")) {
                return true;
            }
            process.waitFor();
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
