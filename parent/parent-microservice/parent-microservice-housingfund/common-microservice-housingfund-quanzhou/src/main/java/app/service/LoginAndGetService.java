package app.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.jna.webdriver.WebDriverUnit;

import app.bean.WebParamHousing;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.quanzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.quanzhou")
public class LoginAndGetService extends HousingBasicService{
	@Value("${datasource.driverPath}")
	public String driverPath;
	@Value("${imagePath}")
	public String OCR_FILE_PATH;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public String getVerfiycode(By by, WebDriver driver) throws Exception {
		String path =saveImg(driver, by);
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1004", LEN_MIN, TIME_ADD, STR_DEBUG, path);
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		return code;
	}
	//linux下创建文件夹
	public  File getLinuxImageLocalPath(){ 
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
	//根据dom节点截取图片
	public  String saveImg(WebDriver driver, By selector) throws Exception{
		File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		BufferedImage  fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page 
		WebElement ele = driver.findElement(selector); 
		Point point = ele.getLocation(); 
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight(); 
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------"+point.getX());
		System.out.println("point.getY()-------"+point.getY());
		System.out.println("eleWidth-------"+eleWidth);
		System.out.println("eleHeight-------"+eleHeight);
		BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(),eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot); 
		File file = getLinuxImageLocalPath();		
		FileUtils.copyFile(screenshot, file); 
		return file.getAbsolutePath();
	}	
	// 根据身份证号登录
	public WebParamHousing loginByIDCard(String num, String password)
			throws Exception {
		WebParamHousing<Object> webParamHousing = new WebParamHousing();
		ChromeDriver driver = intiChrome();
		//driver.manage().window().maximize();
		String url = "http://www.qzgjj.com/PAFundQuery/Index";
		//driver.manage().window().maximize();

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		driver.get(url);
		driver.findElement(By.id("t_user")).sendKeys(num);
		Thread.sleep(1000);
		driver.findElement(By.id("t_pass")).sendKeys(password);
		Thread.sleep(1000);
		String code = getVerfiycode(By.id("img_CheckCode_Layout"), driver);
		driver.findElement(By.id("Tb_CheckCode_Layout")).sendKeys(code);
		Thread.sleep(1000);
		driver.findElement(By.id("btn_Login")).click();
		Thread.sleep(1000*5);
//		String htmlsource2 = driver.getPageSource();
		try{  
			 Alert alt = driver.switchTo().alert();
		     alt.accept();  
	           
	     }catch (Exception Ex)  {  
	            
	     }   
		String html = driver.getPageSource();
		//图片滑动验证码
		if (html.contains("账户明细")){
			Thread.sleep(1000);
//			String html = driver.getPageSource();
			Document doc = Jsoup.parse(html);
			Elements ele = doc.select("div.verify-sub-block");
			String style = ele.attr("style").trim();
//			System.out.println(style);
			String s = style.substring(style.lastIndexOf(":")).trim();
			String ss = s.substring(s.indexOf("-")+1,s.indexOf("px")).trim();
			System.out.println(ss);
			
			float a = Float.parseFloat(ss);
			Actions action = new Actions(driver);
			// 获取滑动滑块的标签元素
			WebElement source = driver.findElement(By.xpath("//*[@class='verify-sub-block']"));
			// 确保每次拖动的像素不同，故而使用随机数
			action.clickAndHold(source).moveByOffset((int)a, 0);
			Thread.sleep(2000);
			// 拖动完释放鼠标
			action.moveToElement(source).release();
			Thread.sleep(2000);
			// 组织完这些一系列的步骤，然后开始真实执行操作
			Action actions = action.build();
			actions.perform();
		}
		
		
		String htmlsource3 = driver.getPageSource();
		String cookieJson = "";
		// 获取cookies
		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
		Set<CookieJson> cookiesSet = new HashSet<CookieJson>();
		for (org.openqa.selenium.Cookie cookie : cookies) {
			CookieJson cookiejson = new CookieJson();
			cookiejson.setDomain(cookie.getDomain());
			cookiejson.setKey(cookie.getName());
			cookiejson.setValue(cookie.getValue());
			cookiesSet.add(cookiejson);
		}
		cookieJson = new Gson().toJson(cookiesSet);
		//String htmlsource2 = driver.getPageSource();
		webParamHousing.setHtml(htmlsource3);
		webParamHousing.setPagnum(cookieJson);
		driver.quit();
		return webParamHousing;
		
	}		
	public  ChromeDriver intiChrome() throws Exception {
//		String driverPath = "/opt/selenium/chromedriver-2.31";
		System.setProperty("webdriver.chrome.driver", driverPath);
		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		// chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1980,1068");
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}
	
	//发送短信
//	public WebParamHousing loginByCard(WebClient webClient)
//			throws Exception {
//		WebParamHousing<Object> webParamHousing = new WebParamHousing();
//		String url = "http://www.qzgjj.com/PAFundQuery/Index";
//		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//		webClient.setJavaScriptTimeout(50000); 
//		webClient.getOptions().setTimeout(50000); // 15->60
//		//webClient.getOptions().setJavaScriptEnabled(false);
//		
//		HtmlPage htmlPage = webClient.getPage(webRequest);
//		HtmlElement button = (HtmlElement)htmlPage.getFirstByXPath("//input[@id='btn_GetMsgCode']");
//		HtmlPage htmlPages =  button.click();
//		webParamHousing.setStatusCode(htmlPages.getWebResponse().getStatusCode());
//		webParamHousing.setWebClient(webClient);
//		webParamHousing.setPage(htmlPages);
//		webParamHousing.setHtml(htmlPages.getWebResponse().getContentAsString());
//		return webParamHousing;
//		
//	}
//	
//	//短信验证登陆
//	public WebParamHousing loginCard(WebClient webClient,String sms_code,Page page)
//			throws Exception {
//		WebParamHousing<Object> webParamHousing = new WebParamHousing();
//		String url1 = "http://www.qzgjj.com/PAFundQuery";
//		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		webRequest.setAdditionalHeader("Cache-Control","max-age=0");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		//webRequest.setAdditionalHeader("Content-Length", "322");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest.setAdditionalHeader("Host", "www.qzgjj.com");
//		webRequest.setAdditionalHeader("Origin", "http://www.qzgjj.com");;
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"); 
//		HtmlPage htmlPage1 = webClient.getPage(webRequest);
//		HtmlElement button = (HtmlElement)htmlPage1.getFirstByXPath("//input[@value='查  询']");
//		HtmlTextInput code = (HtmlTextInput)htmlPage1.getFirstByXPath("//input[@id='Tb_MsgVCode']");
//        code.setText(sms_code.trim());
//        HtmlPage htmlPage = button.click();
//		webParamHousing.setStatusCode(htmlPage.getWebResponse().getStatusCode());
//		webParamHousing.setWebClient(webClient);
//		webParamHousing.setPage(htmlPage);
//		webParamHousing.setHtml(htmlPage.getWebResponse().getContentAsString());
//		return webParamHousing;
//		
//		
//	}

}
