package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

public class ShenZhenTest {
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	public static String OCR_FILE_PATH = "E:\\home\\img";
	public static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", driverPath);

		ChromeOptions chromeOptions = new ChromeOptions();
		ChromeDriver driver = new ChromeDriver(chromeOptions);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "http://app.szzfgjj.com:7001/pages/wy_yecx.jsp";
		driver.get(baseUrl);
		driver.get(baseUrl);
		try {

			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
		}

		// 个人公积金账户
		driver.findElement(By.id("accnum2")).sendKeys("20580631843");
		try {

			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 身份证号 无此记录
		driver.findElement(By.id("certinum2")).sendKeys("450324198409081916");
		try {

			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		String path = null;
		try {

			// 验证码
			path = saveImg(driver, By.id("imgyzm"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("4004", LEN_MIN, TIME_ADD, STR_DEBUG, path); // 505
																														// 1~5位英文数字
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);

		driver.findElement(By.id("verify")).sendKeys("qwer");
		try {

			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 模拟登录
		driver.findElement(By.id("but_2")).click();

		try {

			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			Alert alt = driver.switchTo().alert();
			String text = alt.getText();
			System.out.println(text);
		} catch (Exception e) {
			System.out.println("进来了");
		}

		String pageSource = driver.getPageSource();
		System.out.println(pageSource);
	}

	// 根据dom节点截取图片
	public static String saveImg(WebDriver driver, By selector) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement ele = driver.findElement(selector);
		Point point = ele.getLocation();
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		System.out.println("eleWidth-------" + eleWidth);
		System.out.println("eleHeight-------" + eleHeight);
		BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot);
		File file = getImageLocalPath();
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}

	public static File getImageLocalPath() {
		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
		return codeImageFile;

	}
}
