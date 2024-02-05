
package com.city.city_collector.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import sun.misc.BASE64Encoder;

/**
 * @author nb
 * @Description:
 */
public class ZxingUtil {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * 创建二维码图像
     *
     * @param str
     * @return
     * @author:nb
     */
    public static BufferedImage createQRCodeImage(String str) {
        try {
            BitMatrix byteMatrix = new MultiFormatWriter().encode(new String(str.getBytes(), "iso-8859-1"),
                    BarcodeFormat.QR_CODE, 200, 200);
            return toBufferedImage(byteMatrix);
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        } catch (WriterException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建二维码图片  base64
     *
     * @param str
     * @return
     * @author:nb
     */
    public static String createQRCodeStr(String str) {
        BufferedImage img = createQRCodeImage(str);
        if (img == null) return "";
        BASE64Encoder encoder = new BASE64Encoder();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", bos);
            return "data:image/png;base64," + encoder.encode(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
}
