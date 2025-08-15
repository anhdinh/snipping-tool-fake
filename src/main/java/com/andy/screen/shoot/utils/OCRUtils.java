package com.andy.screen.shoot.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import lombok.Getter;
import net.sourceforge.tess4j.*;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

    public static List<OCRResult> imageViewToLines(ImageView imageView){
        List<OCRResult> results = new ArrayList<>();

        if (imageView == null || imageView.getImage() == null) {
            return results;
        }

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);

        // Thiết lập đường dẫn thư viện native (Linux)
        System.setProperty("jna.library.path", "/usr/lib/x86_64-linux-gnu");

        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("/usr/share/tesseract-ocr/5/tessdata");
        tesseract.setLanguage("eng");

        // Lấy từng dòng (TEXTLINE)
        List<Word> lines = tesseract.getWords(bufferedImage, ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE);
        for (Word line : lines) {
            String text = line.getText();
            java.awt.Rectangle rect = line.getBoundingBox();
            results.add(new OCRResult(
                    text,
                    rect.getX(),
                    rect.getY(),
                    rect.getWidth(),
                    rect.getHeight()
            ));
        }

        return results;
    }

    @Getter
    public static class OCRResult {
        private final String text;
        private final double x, y, width, height;

        public OCRResult(String text, double x, double y, double width, double height) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return String.format("OCRResult{text='%s', x=%.1f, y=%.1f, w=%.1f, h=%.1f}",
                    text, x, y, width, height);
        }
    }
}
