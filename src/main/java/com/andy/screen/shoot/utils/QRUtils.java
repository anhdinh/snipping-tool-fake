package com.andy.screen.shoot.utils;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import lombok.extern.log4j.Log4j;


import java.awt.image.BufferedImage;

@Log4j
public class QRUtils {

    /**
     * Read QR code from ImageView of JavaFX
     * @param imageView ImageView contains QR
     * @return  QR code, or null
     */
    public static String readQR(ImageView imageView) {
        if (imageView.getImage() == null) return null;

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            log.error("QRUtils#readQR",e);
            return null;
        }
    }
}
