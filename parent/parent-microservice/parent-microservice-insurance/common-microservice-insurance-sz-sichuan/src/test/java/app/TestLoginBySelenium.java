package app;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class TestLoginBySelenium extends AbstractChaoJiYingHandler{
	
	private static String driverPath = "D:\\software\\IEDriverServer_Win32\\chromedriver.exe";
	private static WebDriver driver = null;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) {
		
		String url = "http://119.6.84.89:7001/scwssb/login.jsp";
		try {
			seleniumLogin(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void seleniumLogin(String url) throws Exception{
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath); 
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("disable-gpu"); 
		// 设置浏览器窗口打开大小 （非必须）
		//chromeOptions.addArguments("--window-size=1920,1080");
		driver = new ChromeDriver(chromeOptions);
		 
		driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		
		driver.manage().window().maximize();
		driver.get(url);
		
		Thread.sleep(5000);
		
		System.out.println("登录页面 ： "+driver.getPageSource());
		
		//用户名输入框
		WebElement username = driver.findElement(By.className("inputFocusBorder"));
		//密码输入框
		WebElement password = driver.findElement(By.xpath("//input[@type='password']"));
		//登录按钮
		WebElement button = driver.findElement(By.xpath("//a[@class='STYLE1']"));
		//图片验证码输入框
		WebElement img = driver.findElement(By.id("checkCode"));
	
		System.out.println("用户名输入框："+username.getText());
		System.out.println("密码输入框："+password.getText());
		System.out.println("登录按钮："+button.getText());
		System.out.println("图片验证码输入框："+img.getText());
		
		WebDriverWait wait = new WebDriverWait(driver, 30); 
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("codeimg")));
		String path = WebDriverUnit.saveImg(driver,By.id("codeimg"));
		
		System.out.println("path  : "+path);
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code); 
		
		username.sendKeys("Qinxu");
		password.sendKeys("q12345678");
		img.sendKeys(code);
		
		button.click();		
		
		Thread.sleep(4000);
		System.out.println("点击登录后页面源码  ： "+driver.getCurrentUrl());
		
//		WebElement messageButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("bl-firstname")));
//		messageButton.click();
//		
////		System.out.println("参保信息  ： "+driver.getPageSource());
////		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("参保状态")));
//		
//		Thread.sleep(5000);
//		
		String messageUrl = "http://119.6.84.89:7001/scwssb/g40Action.do?___businessId=01391304";
		
		getMessage(messageUrl);
		
		
	}

	private static void getMessage(String url) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setTimeout(50000);
		
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		for(org.openqa.selenium.Cookie cookie : cookiesDriver){
		
			System.out.println("cookie domain = "+cookie.getDomain()+"   cookie key = "+cookie.getName() + "   value= "+cookie.getValue());
			Cookie cookieWebClient = new Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "119.6.84.89:7001");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
//		webRequest.setAdditionalHeader("Referer", "http://119.6.84.89:7001/scwssb/welcome2.jsp");
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		Page searchPage = webClient.getPage(webRequest);

		System.out.println("statuscode = "+searchPage.getWebResponse().getStatusCode());
		System.out.println("参保信息 ： "+searchPage.getWebResponse().getContentAsString());
		
		String html = searchPage.getWebResponse().getContentAsString();
		Pattern r = Pattern.compile("list\\\":[^]]*");
		Matcher m = r.matcher(html);
		if(m.find()){
			System.out.println("需要提取的一段信息： "+m.group(0));			
			String json = m.group(0).replace("list\":[", "");
			System.out.println("处理字符格式后，转成json ："+json);
			
			Gson gson = new Gson();
			TestJson[] jsonBeans = gson.fromJson("["+json+"]", TestJson[].class);
			for(TestJson jsonBean : jsonBeans){
				System.out.println(jsonBean.toString());
			}
		}else{
			System.out.println("没有匹配到对应的信息。");
		}
	}

}
