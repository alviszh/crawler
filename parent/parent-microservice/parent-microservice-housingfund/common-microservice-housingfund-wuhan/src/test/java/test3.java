

import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.catalina.util.URLEncoder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.jna.webdriver.WebDriverUnit;

import app.service.ChaoJiYingOcrService;

public class test3 {
	static String driverPath = "F:\\IEDriverServer_Win32\\IEDriverServer.exe";
	private static ChaoJiYingOcrService chaoJiYingOcrService;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	private static Robot robot;
	public static void main(String[] args) throws Exception {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.setProperty("webdriver.ie.driver", driverPath);
		WebDriver driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		driver = new InternetExplorerDriver(ieCapabilities);
		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

//		String url = "http://index.baidu.com/";
//		driver.get(url);
//		String pageSource = driver.getPageSource();
//		if(pageSource.indexOf("登录")!=-1){
//			driver.findElement(By.xpath("//a[@href = 'javascript:BID.popLogin();']")).click();
//
//			Thread.sleep(5000L);
//			driver.findElement(By.id("TANGRAM_12__userName")).clear();
//			driver.findElement(By.id("TANGRAM_12__userName")).sendKeys("liu299481122");
//			Thread.sleep(2000L);
//			driver.findElement(By.id("TANGRAM_12__password")).sendKeys("299481122");
//			Thread.sleep(2000L);
//			WebElement findElement = driver.findElement(By.id("TANGRAM_12__verifyCodeImg"));
//			if(findElement!=null){
//				String path = WebDriverUnit.saveImg(driver, By.id("TANGRAM_12__verifyCodeImg"));
//				System.out.println("path---------------" + path);
//				String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("5000", LEN_MIN, TIME_ADD, STR_DEBUG,
//						path); // 1005
//				System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
//				Gson gson = new GsonBuilder().create();
//				String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
//				System.out.println("code ====>>" + code);
//				driver.findElement(By.id("TANGRAM_12__verifyCode")).sendKeys(code);
//			}
//			Thread.sleep(2000L);
//			driver.findElement(By.id("TANGRAM_12__submit")).click();
//			Thread.sleep(5000L);
//			String html = driver.getPageSource();
//			System.out.println(html);
//
//			if(html.indexOf("您的帐号存在安全风险，为保障帐号安全，登录前需验证身份。")!=-1){
//				driver.findElement(By.id("TANGRAM__37__button_send_mobile")).click();
//				Thread.sleep(2000L);
//				//	String inputValue = JOptionPane.showInputDialog("请输入验证码……");
//				//	System.out.println(inputValue);
//				//	driver.findElement(By.id("TANGRAM__37__input_label_vcode")).sendKeys(inputValue);
//				Thread.sleep(10000L);
//				driver.findElement(By.id("TANGRAM__37__button_submit")).click();
//				Thread.sleep(5000L);
//			}
//
//		}
//		Thread.sleep(5000L);
//		driver.findElement(By.id("schword")).sendKeys("互联网金融");
//		Thread.sleep(2000L);
//		WebElement findElement = driver.findElement(By.id("searchWords"));
//		findElement.click();
		
//%BB%A5%C1%AA%CD%F8%BD%F0%C8%DA%2C%C8%CB%C8%CB%B4%FB%2C%D0%C5%BA%CD%B2%C6%B8%BB%2C%C4%E3%CE%D2%B4%FB
		String xpath = "/html/body/div[4]/div[3]/div[2]/div//a/img";
		List<String> list = new ArrayList<String>();
		//list.add("互联网金融");
		list.add("人人贷");
		String c = "";
		for (String string : list) {
			c += ","+string;
		}
		c = c.substring(1, c.length());
		System.out.println(c);
		
		URLEncoder u = new URLEncoder();
		String ue = u.encode(c, "gbk");
		System.out.println(ue);
		String url2 = "http://index.baidu.com/?"
				+ "tpl=trend"
				+ "&type=0"
				+ "&area=0"
				+ "&time=13"
				+ "&word="+c;
		driver.get(url2);
		Thread.sleep(5000L);
		String imageName = UUID.randomUUID().toString() + ".png";
		File file = new File("F:\\img\\" + imageName);
		System.out.println(imageName);
		//WebDriverUnit.saveImg(driver, By.id("trend"));
		
		
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement ele = driver.findElement(By.id("trend"));
		System.out.println(ele);
		Point point = ele.getLocation();
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		System.out.println("eleWidth-------" + eleWidth);
		System.out.println("eleHeight-------" + eleHeight);
		String path = getPathBySystem();
		// String path = "/home/seluser" + "/snapshot/";
		System.out.println("截图保存路径： " + path);
		File file1 = getImageCustomPath(path);
		BufferedImage eleScreenshot = fullImg.getSubimage(15+point.getX(), 100+point.getY(), 1234, 256);
		ImageIO.write(eleScreenshot, "png", screenshot);
		FileUtils.copyFile(screenshot, file);
		
		
		
		// x="20" y="130" width="1214" height="207.667" rx="0" ry="0" r="0"

	}
	
	public static String getPathBySystem() {

		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			String path = System.getProperty("user.dir") + "/snapshot/";
			return path;
		} else {
			String path = System.getProperty("user.home") + "/snapshot/";
			return path;
		}

	}
	
	public static File getImageCustomPath(String path) {
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;

	}
}
