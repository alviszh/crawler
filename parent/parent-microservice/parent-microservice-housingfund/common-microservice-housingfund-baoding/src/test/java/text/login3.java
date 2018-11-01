package text;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by WSK on 2017/12/12.
 */
public class login3 {
	
	 public static void main(String[] args) throws Exception {
		 
		 	File imageFile = new File("F:\\file\\jcmxlist.png");
			BufferedImage grayImage = ImageHelper.convertImageToBinary(ImageIO.read(imageFile));
			ImageIO.write(grayImage, "jpg", new File("F:\\file\\b.jpg"));
	        String result=getfindOCR("F:\\file\\b.jpg",true);
	        System.out.println(result);
	    }
	
	
    /**
     *
     * @param srImage 图片路径
     * @param ZH_CN 是否使用中文训练库,true-是
     * @return 识别结果
     */
	private static String getfindOCR(String srImage, boolean ZH_CN) {
		try {
			System.out.println("start");
			double start=System.currentTimeMillis();
			File imageFile = new File(srImage);
			if (!imageFile.exists()) {
				return "图片不存在";
			}
			BufferedImage textImage = ImageIO.read(imageFile);
			Tesseract instance=Tesseract.getInstance();
			
			instance.setDatapath("./src/main/resources/tess4j/tessdata");//设置库
			if (ZH_CN)
				instance.setLanguage("chi_sim");//中文识别
			//instance.setDatapath("youdir");
			String result = null;
			result = instance.doOCR(textImage);
			double end=System.currentTimeMillis();
			System.out.println("耗时"+(end-start)/1000+" s");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "发生未知错误";
		}
	}
   
    
    
    private static String FindOCR(String srImage, boolean ZH_CN) {
        try {
            System.out.println("start");
            double start=System.currentTimeMillis();
            File imageFile = new File(srImage);
            if (!imageFile.exists()) {
                return "图片不存在";
            }
            BufferedImage textImage = ImageIO.read(imageFile);
            Tesseract instance=Tesseract.getInstance();
            instance.setDatapath("./src/main/resources/tess4j/tessdata");//设置库
            if (ZH_CN)
                instance.setLanguage("chi_sim");//中文识别
           // instance.setDatapath("youdir");
            String result = null;
            result = instance.doOCR(textImage);
            double end=System.currentTimeMillis();
            System.out.println("耗时"+(end-start)/1000+" s");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "发生未知错误";
        }
    }
}
