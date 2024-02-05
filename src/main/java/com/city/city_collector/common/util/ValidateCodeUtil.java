package com.city.city_collector.common.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;

import com.city.city_collector.admin.city.util.ApplicationData;

import sun.misc.BASE64Encoder;

public class ValidateCodeUtil {
    private String rndCode;

    private String result;

    private int width = 160;

    private int height = 40;

    private static final Integer ADD = Integer.valueOf(0);

    private BufferedImage validateImage = null;

    public BufferedImage getValidateImage() {
        return this.validateImage;
    }

    public void setValidateImage(BufferedImage validateImage) {
        this.validateImage = validateImage;
    }

    public String getRndCode() {
        return this.rndCode;
    }

    public void setRndCode(String rndCode) {
        this.rndCode = rndCode;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void createRndImage() {
        createRndCode();

        int fontheight = this.height - 2;
        int x = this.width / 6;
        this.validateImage = new BufferedImage(this.width, this.height, 1);
        Graphics2D g = this.validateImage.createGraphics();
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, this.width, this.height);
        g.setFont(new Font("Arial", 2, fontheight));
        String c = "";
        for (int i = 0; i < this.rndCode.length(); i++) {
            c = this.rndCode.charAt(i) + "";
            g.setColor(getRndColor());
            g.drawString(c, i * x, this.height - 4);
        }
        g.setFont(new Font("Arial", 2, fontheight - 10));
        g.setColor(new Color(0, 0, 0));
        g.setComposite(AlphaComposite.getInstance(10, 0.2F));
        g.drawString(ApplicationData.getInstance().getConfig().getCname(), 20, this.height - 4);
    }

    private void createRndCode() {
        Random rn = new Random();
        Integer i1 = Integer.valueOf(rn.nextInt(99));
        Integer i2 = Integer.valueOf(3);
        if (i1.intValue() >= 10)
            i2 = Integer.valueOf(rn.nextInt(9));
        else {
            i2 = Integer.valueOf(rn.nextInt(99));
        }
        Integer i3 = Integer.valueOf(rn.nextInt(2));
        Integer rs = runCode(i1, i2, i3);
        if (i3.intValue() == 0)
            this.rndCode = (i1 + "+" + i2);
        else {
            this.rndCode = (i1 + "-" + i2);
        }
        this.rndCode += "=?";
        this.result = (rs + "");
    }

    private Integer runCode(Integer a, Integer b, Integer sy) {
        if (sy == ADD)
            return Integer.valueOf(a.intValue() + b.intValue());
        return Integer.valueOf(a.intValue() - b.intValue());
    }

    private Color getRndColor() {
        Random rn = new Random();
        return new Color(rn.nextInt(256), rn.nextInt(256), rn.nextInt(256));
    }

    public void write(OutputStream sos) throws IOException {
        ImageIO.write(this.validateImage, "png", sos);
        sos.close();
    }

    public String getBaseString() {
        if (this.validateImage == null) {
            return "";
        }
        BASE64Encoder encoder = new BASE64Encoder();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(this.validateImage, "png", bos);
            return "data:image/png;base64," + encoder.encode(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
