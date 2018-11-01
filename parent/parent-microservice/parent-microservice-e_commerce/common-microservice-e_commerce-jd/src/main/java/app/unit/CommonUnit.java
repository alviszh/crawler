package app.unit;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

/**
 * @Des
 * @author hyx
 * @date 2017年11月1日 上午10:54:55 Administrator
 */
public class CommonUnit extends AbstractChaoJiYingHandler{

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.unit 类描述： 将图片转化为 创建人：hyx
	 * Base64编码返回 创建时间：2017年12月11日
	 * 
	 * @version 1 返回值 String
	 */
	
    public static String encodeImgageToBase64(URL imageUrl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageUrl);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
        
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
	
	public static String getVerfiycodeBy(By by, WebDriver driver,Class<?> clas) throws Exception {
		String path = WebDriverUnit.saveImg(driver, by);
		System.out.println("path---------------" + path);
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path);
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		return code;
	}

}