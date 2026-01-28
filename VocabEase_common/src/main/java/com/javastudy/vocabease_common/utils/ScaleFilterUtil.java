package com.javastudy.vocabease_common.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ScaleFilterUtil {
    private static final Logger logger = Logger.getLogger(ScaleFilterUtil.class.getName());
    /**
     * 生成指定尺寸的 JPEG 缩略图
     * @param file             源图片文件（必须是可读的本地图片）
     * @param thumbnailWidth   目标缩略图的**最大宽度**
     * @param thumbnailHeight  目标缩略图的**最大高度**（仅用于裁剪，不强制拉伸）
     * @param targetFile       生成的缩略图保存路径（会覆盖同名文件）
     * @return true 表示成功生成并保存；false 表示失败或源图太小无需处理
     */
    public static Boolean createThumbnail(File file, int thumbnailWidth, int thumbnailHeight, File targetFile) {
        try {
            // 1. 读取源图片为 BufferedImage 对象
            BufferedImage src = ImageIO.read(file);
            int sourceWidth = src.getWidth();
            int sourceHeight = src.getHeight();
            // 2. 【提前返回】如果源图宽度小于目标宽度，则不生成缩略图（避免放大失真）
            if (sourceWidth < thumbnailWidth)
                return false;
            int height;// 计算等比例缩放后的高度
            // 3. 按目标宽度等比缩放（保持原始宽高比）
            if (sourceWidth > thumbnailWidth)
                // 宽度超限，按比例缩小高度
                height = thumbnailWidth * sourceHeight / sourceWidth;
            else {
                // 宽度未超限（理论上不会进入，因上面已判断 sourceWidth < thumbnailWidth 返回）
                thumbnailWidth = sourceWidth;
                height = sourceHeight;
            }
            // 4. 创建目标画布（RGB 格式）
            BufferedImage dst = new BufferedImage(thumbnailWidth, height, BufferedImage.TYPE_INT_RGB);
            // 5. 使用平滑算法缩放图像
            Image scaleImage = src.getScaledInstance(thumbnailWidth, height, Image.SCALE_SMOOTH);
            // 6. 将缩放后的图像绘制到目标画布上
            Graphics2D g = dst.createGraphics();
            g.drawImage(scaleImage, 0, 0, thumbnailWidth, height, null);
            g.dispose();
            // 7. 【关键裁剪逻辑】如果缩放后高度仍超过目标最大高度，则从顶部裁剪
            int resultHeight = dst.getHeight();
            if (resultHeight > thumbnailHeight) {
                resultHeight = thumbnailHeight;
                // 裁剪区域：x=0, y=0, width=thumbnailWidth, height=resultHeight
                dst = dst.getSubimage(0, 0, thumbnailWidth, resultHeight);
            }
            // 8. 将最终图像以 JPEG 格式写入目标文件
            ImageIO.write(dst, "jpeg", targetFile);
            return true;
        } catch (IOException e) {
            logger.warning("生成缩略图失败");
        }
        return false;
    }

    public static void main(String[] args ) {
        createThumbnail(new File("C:\\Users\\Administrator\\Desktop\\test.jpg"), 400,
                200, new File("C:\\Users\\Administrator\\Desktop\\test.jpg"));
    }
}
