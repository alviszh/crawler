package app.util;

import com.gargoylesoftware.htmlunit.Page;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ImageUtils {

    private static final int IMG_GAP_RGB = 205205193;

    public static File saveImg(Page image) throws IOException {
        InputStream contentAsStream = image.getWebResponse().getContentAsStream();
        String userHome = System.getProperty("user.home");
        File imgHomeFile = new File(userHome + File.separatorChar + "img");
        if (!imgHomeFile.exists()) {
            imgHomeFile.mkdirs();
        }
        File imgFile = new File(imgHomeFile.getAbsolutePath() + File.separatorChar + UUID.randomUUID() + ".png");
        FileOutputStream fos = new FileOutputStream(imgFile);

        byte[] read = new byte[1024];
        int len = 0;
        while ((len = contentAsStream.read(read)) != -1) {
            fos.write(read, 0, len);
        }
        contentAsStream.close();
        fos.flush();
        fos.close();
        return imgFile;
    }

    public static int getMoveOffset(File parentImageFile, File childImageFile) throws IOException {

        BufferedImage parentBufferedImage = ImageIO.read(parentImageFile);
        BufferedImage childBufferedImage = ImageIO.read(childImageFile);
        int parentImgWidth = parentBufferedImage.getWidth();
        int parentImgHeight = parentBufferedImage.getHeight();
        int childImgWidth = childBufferedImage.getWidth();

        int tmpRGB = 0;
        int tapXPoint = 0;//图片缺口的x坐标值
        int count = 0;

        for (int yPoint = parentImgHeight - 1; yPoint >= 0; yPoint--) { //父级图片的高度遍历
            for (int xPoint = 0; xPoint < parentImgWidth; xPoint++) {//父级图片的长度遍历
                int rgb = parentBufferedImage.getRGB(xPoint, yPoint);
                if (rgb == tmpRGB) {
                    ++count;
                    if (count >= (childImgWidth - 1)) {
                        return tapXPoint;
                    }
                } else {
                    tmpRGB = rgb;
                    count = 0;
                    tapXPoint = xPoint;
                }
            }
        }
        return Integer.MIN_VALUE;
    }
}
