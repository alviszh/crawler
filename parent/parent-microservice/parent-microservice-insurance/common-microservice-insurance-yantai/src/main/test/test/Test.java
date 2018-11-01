package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

public class Test {
	public static String driverPath = "D:\\IEDriverServer_Win32\\8.15\\chromedriver.exe";
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", driverPath);
		try {
			ChromeOptions chromeOptions = new ChromeOptions();
			ChromeDriver driver = new ChromeDriver(chromeOptions);

			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			String baseUrl = "http://ytrsj.gov.cn:8081/hsp/logonDialog_withF.jsp";
			driver.get(baseUrl);

			Thread.sleep(1000);

			// 个人公积金账户
			driver.findElement(By.id("yhmInput")).sendKeys("37068619910213653X");
			Thread.sleep(1000);
			// 身份证号
			driver.findElement(By.id("mmInput")).sendKeys("wang6200");
			Thread.sleep(1000);
			// 验证码
			String path = saveImg(driver, By.xpath(("//*[@id=\"logoninfo\"]/div/table/tbody/tr[5]/td[2]")));

			System.out.println("path---------------" + path);

			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("5201", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 505
							// 1~5位英文数字
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);

			driver.findElement(By.id("authcode_result")).sendKeys(code);
			Thread.sleep(1000);
			// 模拟登录
			driver.findElement(By.className("logonBtn")).click();

			Thread.sleep(3000);
			
			String pageSource = driver.getPageSource();
			System.out.println("源代码"+pageSource);
			
			
			String currentUrl = driver.getCurrentUrl();
			// http://ytrsj.gov.cn:8081/hsp/mainFrame.jsp?&__usersession_uuid=USERSESSION_94a53302_367c_4ea1_b5f5_0983e87ac942&_width=1034&_height=708
			String[] split = currentUrl.split("__usersession_uuid=");
			String[] split2 = split[1].split("&");
			System.out.println("首页地址-----" + currentUrl);
			System.out.println("入参__usersession_uuid-----" + split2[0]);

			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

			WebClient webClient = WebCrawler.getInstance().getWebClient();//

			for (org.openqa.selenium.Cookie cookie : cookies) {
				System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("ytrsj.gov.cn",
						cookie.getName(), cookie.getValue()));
			}

			String loginurl = "http://ytrsj.gov.cn:8081/hsp/siHarm.do";
			WebRequest requestSettings = new WebRequest(new URL(loginurl),
					HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("method", "queryHarmPayHis"));
			requestSettings.getRequestParameters().add(new NameValuePair("year", "2018"));
			requestSettings.getRequestParameters().add(new NameValuePair("crypto_007562a8203b1676eba5533bf1864235e31e6cbe-af74-469a-8db3-68bbdd4ca06b", "crypto_2fe77d45434587148c70265d78d0dfb305928383cd3cf21f3db2e60ab11c893aa69676e0-b819-4e87-ba14-d93d0aaa707c"));
			requestSettings.getRequestParameters().add(new NameValuePair("__usersession_uuid", split2[0]));
			requestSettings.getRequestParameters().add(new NameValuePair("_laneID", "af26b486-9e5c-4534-8990-ae267b9303ac"));
			HtmlPage loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			
			System.out.println("信息-------" + contentAsString);

			Document doc = Jsoup.parse(contentAsString);
			Elements elementById = doc.getElementsByAttributeValue("class","defaultTableClass");
			Elements elementsByAttribute = elementById.get(0).getElementsByTag("tr");
			for (int i = 1; i < elementsByAttribute.size(); i++) {
				Element element = elementsByAttribute.get(i);
				Elements elementsByTag = element.getElementsByTag("td");
				//险种
				String name = elementsByTag.get(0).getElementsByTag("input").val();
				System.out.println("险种-----"+name);
				//年月
				String yearMonth = elementsByTag.get(1).getElementsByTag("input").val();
				System.out.println("年月-----"+yearMonth);
				//缴费基数
				String pay_cardinal = elementsByTag.get(2).getElementsByTag("input").val();
				System.out.println("缴费基数-----"+pay_cardinal);
				//个人交
				String personal_pay = elementsByTag.get(4).getElementsByTag("input").val();
				System.out.println("个人交-----"+personal_pay);
				//单位编号
				String unit_no = elementsByTag.get(5).getElementsByTag("input").val();
				System.out.println("单位编号-----"+unit_no);
				//单位名称
				String unit_name = elementsByTag.get(6).getElementsByTag("input").val();
				System.out.println("单位名称-----"+unit_name);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
		File parentDirFile = new File("D:\\img");
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File("D:\\img" + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
		return codeImageFile;

	}
}
