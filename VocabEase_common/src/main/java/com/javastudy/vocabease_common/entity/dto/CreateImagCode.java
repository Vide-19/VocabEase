package com.javastudy.vocabease_common.entity.dto;

import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;


public class CreateImagCode {
    private int width = 160;
    private int height = 160;
    private int codeCount = 4;
    private int lineCount = 20;
    private String code = null;
    @Getter
    private BufferedImage image = null;
    Random random = new Random();
    public CreateImagCode() {createImage();}
    public CreateImagCode(int width, int height) {
        this.width = width;
        this.height = height;
        createImage();
    }
    public CreateImagCode(int width, int height, int codeCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        createImage();
    }
    public CreateImagCode(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        createImage();
    }
    private void createImage() {
        int fontWidth = width / codeCount;
        int fontHeight = height - 5;
        int yCode = height - 8;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(getRandomColor(200, 250));
        g.fillRect(0, 0, width, height);
        Font font = new Font("Times New Roman", Font.BOLD, fontHeight);
        g.setFont(font);
        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width);
            int ye = ys + random.nextInt(height);
            g.setColor(getRandomColor(1, 250));
            g.drawLine(xs, ys, xe, ye);
        }
        float yawpRate = 0.01f;
        int arae = (int) (yawpRate * width * height);
        for (int i = 0; i < arae; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            image.setRGB(x, y, random.nextInt(255));
        }
        String str = randomStr(codeCount);
        this.code = str;
        for (int i = 0; i < codeCount; i++) {
            String strRandom = str.substring(i, i + 1);
            g.setColor(getRandomColor(1, 255));
            g.drawString(strRandom, i * fontWidth + 3, yCode);
        }
    }
    private Color getRandomColor(int fc, int bc) {
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
    private String randomStr(int n) {
        String str1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String str2 = "";
        int length = str1.length() - 1;
        double r;
        for (int i = 0; i < n; i++) {
            r = (Math.random() * length);
            str2 = str2 + str1.charAt((int) r);
        }
        return str2;
    }
    public void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }
    private Font getFont(int size) {
        Random random = new Random();
        Font font[] = new Font[5];
        font[0] = new Font("Times New Roman", Font.BOLD, size);
        font[1] = new Font("Wide Latin", Font.PLAIN, size);
        font[2] = new Font("Gill Sans Ultra Bold", Font.ITALIC, size);
        font[3] = new Font("Times New Roman", Font.BOLD, size);
        font[4] = new Font("Antique Olive Compact", Font.ITALIC, size);
        return font[random.nextInt(5)];
    }
    public void write(OutputStream os) throws IOException {
        ImageIO.write(image, "png", os);
        os.close();
    }
    public String getCode(){
        return code.toLowerCase();
    }

    private void shearX(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(2);
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);
        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }
    }
    private void shearY(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(40) + 10;
        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }
        }
    }
}
