package app.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import sun.misc.BASE64Encoder;
 
public class QrCodeUtil {
 
    public static String createQrCode(String url) {
        try {
            Map<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
            BufferedImage image = toBufferedImage(bitMatrix);
            ByteArrayOutputStream os = new ByteArrayOutputStream();//新建流。
            ImageIO.write(image, "png", os);//利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
            byte b[] = os.toByteArray();//从流中获取数据数组。
            String str = new BASE64Encoder().encode(b);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
 
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