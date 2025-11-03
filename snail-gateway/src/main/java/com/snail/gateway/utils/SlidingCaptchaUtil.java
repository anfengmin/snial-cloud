package com.snail.gateway.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 滑动验证码工具类
 *
 * @author ruoyi
 */
public class SlidingCaptchaUtil {

    /**
     * 背景图宽度
     */
    private static final int BG_WIDTH = 300;

    /**
     * 背景图高度
     */
    private static final int BG_HEIGHT = 150;

    /**
     * 滑块宽度
     */
    private static final int SLIDER_WIDTH = 50;

    /**
     * 滑块高度
     */
    private static final int SLIDER_HEIGHT = 50;

    /**
     * 允许误差范围
     */
    private static final int TOLERANCE = 5;

    /**
     * 生成滑动验证码
     *
     * @return 包含背景图、滑块图和X坐标的数组 [backgroundImg, sliderImg, x]
     */
    public static SlidingCaptchaResult generate() throws IOException {
        // 随机生成滑块位置X坐标（确保滑块不超出背景图范围）
        int x = RandomUtil.randomInt(SLIDER_WIDTH, BG_WIDTH - SLIDER_WIDTH);
        int y = RandomUtil.randomInt(0, BG_HEIGHT - SLIDER_HEIGHT);

        // 创建背景图
        BufferedImage background = new BufferedImage(BG_WIDTH, BG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D bgGraphics = background.createGraphics();
        bgGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制背景（渐变或纯色背景）
        drawBackground(bgGraphics, BG_WIDTH, BG_HEIGHT);

        // 创建滑块图
        BufferedImage slider = new BufferedImage(SLIDER_WIDTH, SLIDER_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D sliderGraphics = slider.createGraphics();
        sliderGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 从背景图中提取滑块部分（在绘制缺口之前）
        for (int i = 0; i < SLIDER_WIDTH; i++) {
            for (int j = 0; j < SLIDER_HEIGHT; j++) {
                if (x + i < BG_WIDTH && y + j < BG_HEIGHT) {
                    slider.setRGB(i, j, background.getRGB(x + i, y + j));
                }
            }
        }

        // 在背景图上绘制滑块位置（缺口）
        drawSliderHole(bgGraphics, x, y, SLIDER_WIDTH, SLIDER_HEIGHT);

        // 添加滑块边框和阴影效果
        drawSliderStyle(sliderGraphics, SLIDER_WIDTH, SLIDER_HEIGHT);

        bgGraphics.dispose();
        sliderGraphics.dispose();

        // 转换为base64
        String backgroundBase64 = imageToBase64(background);
        String sliderBase64 = imageToBase64(slider);

        return new SlidingCaptchaResult(backgroundBase64, sliderBase64, x);
    }

    /**
     * 绘制背景
     */
    private static void drawBackground(Graphics2D g, int width, int height) {
        // 绘制渐变背景
        GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE, width, height, Color.LIGHT_GRAY);
        g.setPaint(gradient);
        g.fillRect(0, 0, width, height);

        // 添加一些干扰线
        g.setColor(new Color(200, 200, 200));
        for (int i = 0; i < 5; i++) {
            int x1 = RandomUtil.randomInt(0, width);
            int y1 = RandomUtil.randomInt(0, height);
            int x2 = RandomUtil.randomInt(0, width);
            int y2 = RandomUtil.randomInt(0, height);
            g.drawLine(x1, y1, x2, y2);
        }

        // 添加一些干扰点
        g.setColor(new Color(180, 180, 180));
        for (int i = 0; i < 20; i++) {
            int px = RandomUtil.randomInt(0, width);
            int py = RandomUtil.randomInt(0, height);
            g.fillOval(px, py, 3, 3);
        }
    }

    /**
     * 绘制滑块缺口
     */
    private static void drawSliderHole(Graphics2D g, int x, int y, int width, int height) {
        // 绘制缺口路径（圆角矩形）
        g.setColor(Color.WHITE);
        g.fillRoundRect(x, y, width, height, 10, 10);

        // 绘制缺口边框
        g.setColor(new Color(0, 150, 255));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(x, y, width, height, 10, 10);

        // 添加内部阴影效果
        g.setColor(new Color(0, 150, 255, 50));
        g.fillRoundRect(x + 2, y + 2, width - 4, height - 4, 8, 8);
    }

    /**
     * 绘制滑块样式
     */
    private static void drawSliderStyle(Graphics2D g, int width, int height) {
        // 绘制边框
        g.setColor(new Color(0, 150, 255));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(0, 0, width - 1, height - 1, 10, 10);

        // 添加阴影效果
        g.setColor(new Color(0, 0, 0, 100));
        g.drawRoundRect(1, 1, width - 1, height - 1, 10, 10);
    }

    /**
     * 图片转Base64
     */
    private static String imageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encode(imageBytes);
    }

    /**
     * 校验滑动位置
     *
     * @param correctX 正确的X坐标
     * @param userX    用户输入的X坐标
     * @return 是否验证通过
     */
    public static boolean verify(int correctX, int userX) {
        return Math.abs(correctX - userX) <= TOLERANCE;
    }

    /**
     * 滑动验证码结果
     */
    @Getter
    public static class SlidingCaptchaResult {
        private final String backgroundImg;
        private final String sliderImg;
        private final int x;

        public SlidingCaptchaResult(String backgroundImg, String sliderImg, int x) {
            this.backgroundImg = backgroundImg;
            this.sliderImg = sliderImg;
            this.x = x;
        }

    }
}

