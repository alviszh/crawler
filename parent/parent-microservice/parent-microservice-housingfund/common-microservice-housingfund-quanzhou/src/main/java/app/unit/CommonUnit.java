//package app.unit;
//
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.util.Map;
//import java.util.UUID;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.imageio.ImageIO;
//
//import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.By;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.Point;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.springframework.beans.factory.annotation.Value;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.module.jna.webdriver.WebDriverUnit;
//import com.module.ocr.utils.AbstractChaoJiYingHandler;
//
///**
// * @Des
// * @author hyx
// * @date 2017年11月1日 上午10:54:55
// * Administrator
// */
//public class CommonUnit extends AbstractChaoJiYingHandler {
//	@Value("${imagePath}")
//	public static String OCR_FILE_PATH;
//	private static final String LEN_MIN = "0";
//	private static final String TIME_ADD = "0";
//	private static final String STR_DEBUG = "a";
//
//	
//	public static String getVerfiycode(By by, WebDriver driver) throws Exception {
//		String path =saveImg(driver, by);
//		System.out.println("path---------------" + path);
//		String chaoJiYingResult = getVerifycodeByChaoJiYing("1004", LEN_MIN, TIME_ADD, STR_DEBUG, path);
//		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
//		Gson gson = new GsonBuilder().create();
//		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
//		System.out.println("code ====>>" + code);
//		return code;
//	}
//	//linux下创建文件夹
//	public static File getLinuxImageLocalPath(){ 
//		File parentDirFile = new File(OCR_FILE_PATH);
//		parentDirFile.setReadable(true); //
//		parentDirFile.setWritable(true); // 
//		if (!parentDirFile.exists()) {
//			parentDirFile.mkdirs();
//		} 
//		String imageName = UUID.randomUUID().toString() + ".png";
//		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
//		codeImageFile.setReadable(true); //
//		codeImageFile.setWritable(true); // 
//		return codeImageFile;
//	}
//	//根据dom节点截取图片
//		public static String saveImg(WebDriver driver, By selector) throws Exception{
//			File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//			BufferedImage  fullImg = ImageIO.read(screenshot);
//			// Get the location of element on the page 
//			WebElement ele = driver.findElement(selector); 
//			Point point = ele.getLocation(); 
//			// Get width and height of the element
//			int eleWidth = ele.getSize().getWidth();
//			int eleHeight = ele.getSize().getHeight(); 
//			// Crop the entire page screenshot to get only element screenshot
//			System.out.println("point.getX()-------"+point.getX());
//			System.out.println("point.getY()-------"+point.getY());
//			System.out.println("eleWidth-------"+eleWidth);
//			System.out.println("eleHeight-------"+eleHeight);
//			BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(),eleWidth, eleHeight);
//			ImageIO.write(eleScreenshot, "png", screenshot); 
//			File file = getLinuxImageLocalPath();		
//			FileUtils.copyFile(screenshot, file); 
//			return file.getAbsolutePath();
//		}
//		public static File getImageLocalPath(){ 
//			File parentDirFile = new File(OCR_FILE_PATH);
//			parentDirFile.setReadable(true); //
//			parentDirFile.setWritable(true); // 
//			if (!parentDirFile.exists()) {
//				parentDirFile.mkdirs();
//			} 
//			String imageName = UUID.randomUUID().toString() + ".png";
//			File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
//			codeImageFile.setReadable(true); //
//			codeImageFile.setWritable(true); // 
//			return codeImageFile;
//			
//		}
//	
//	/**
//	 * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样
//	 * 
//	 * @param soap
//	 * @param rgex
//	 * @return
//	 */
//	public static String getSubUtilSimple(String soap, String rgex) {
//		Pattern pattern = Pattern.compile(rgex);// 匹配的模式
//		Matcher m = pattern.matcher(soap);
//		while (m.find()) {
//			return m.group(1);
//		}
//		return "";
//	}
//
//}