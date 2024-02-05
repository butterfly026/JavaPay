package com.city.city_collector.common.util;

import com.alibaba.simpleimage.ImageFormat;
import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.ImageWrapper;
import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.render.ReadRender;
import com.alibaba.simpleimage.render.ScaleParameter;
import com.alibaba.simpleimage.render.ScaleRender;
import com.alibaba.simpleimage.render.WatermarkParameter;
import com.alibaba.simpleimage.render.WatermarkRender;
import com.alibaba.simpleimage.render.WriteRender;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * 图片缩放处理类
 *
 * @author nb
 */
public class SimpleImageUtil {
    public static void scaleImage(File srcFile, File destFile, int destWidth, int destHeight, String fileType)
            throws SimpleImageException, FileNotFoundException {
        WriteRender wr = null;
        InputStream inStream = null;
        try {
            inStream = new FileInputStream(srcFile);
            ImageRender ir = new ReadRender(inStream);
            ScaleParameter scaleParam = new ScaleParameter(destWidth, destHeight);
            ImageRender sr = new ScaleRender(ir, scaleParam);
            wr = new WriteRender(sr, destFile,
                    fileType == null ? ImageFormat.JPEG : ImageFormat.getImageFormat(fileType));
            wr.render();
        } finally {
            IOUtils.closeQuietly(inStream);
            if (wr != null)
                try {
                    wr.dispose();
                } catch (SimpleImageException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void addWartermarkImage(File srcFile, File destFile, BufferedImage image, int x, int y)
            throws SimpleImageException, IOException {
        InputStream in = null;
        ImageRender sr = null;
        WriteRender wr = null;
        try {
            in = new FileInputStream(srcFile);
            ImageRender ir = new ReadRender(in, true);

            WatermarkParameter wmarkparam = new WatermarkParameter(new ImageWrapper(image), 0.7F, x, y);
            sr = new WatermarkRender(ir, wmarkparam);

            wr = new WriteRender(sr, destFile);
            wr.render();
        } finally {
            if (sr != null) {
                sr.dispose();
            }
            if (wr != null) {
                wr.dispose();
            }
            IOUtils.closeQuietly(in);
        }
    }
}
