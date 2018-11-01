package test;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.xvolks.jnative.exceptions.NativeException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class LoginTest2 extends AbstractChaoJiYingHandler{

	static String driverPath = "D:\\ChromeServer\\chromedriver.exe";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	// static String driverPath = "C:\\Program Files\\Internet
	// Explorer\\iexplore.exe";

	public static void main(String[] args) throws Exception {

		//VirtualKeyBoard.KeyPressEx("201710WAN",50);// 201710WAN
		
	    login();
		 
		//VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Delete"));//a1212.
		
		 
	}
	
	public static void login()  throws  Exception{

	
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);
		WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		//通过配置参数禁止data;的出现,否则输入正确信息也无法进入首页面 
		driver = new ChromeDriver(chromeOptions);
		//设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		String baseUrl = "http://login.189.cn";
		driver.get(baseUrl);
		
		Thread.sleep(1000);
	
		driver.findElement(By.id("txtAccount")).sendKeys("17786472180");
		
		InputTab();
	
		System.out.println(driver.findElement(By.id("txtShowPwd")));
		driver.findElement(By.id("txtShowPwd")).click();
		//输入密码
		driver.findElement(By.id("txtPassword")).sendKeys("73307533");
		InputTab();
		String htmlsource = driver.getPageSource();
		System.out.println(htmlsource);
//		Document doc = Jsoup.parse(htmlsource, "utf-8"); 
//		Element pathStr=doc.getElementById("imgCaptcha");
//		String path=pathStr.attr("src");
//		String pathstr = WebDriverUnit.saveScreenshotByPath(driver, "http://login.189.cn"+path); 
		String path=WebDriverUnit.saveImg(driver, By.id("imgCaptcha")); 
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code);
	

		driver.findElement(By.id("txtCaptcha")).sendKeys(code);    
		
		Thread.sleep(1000L);
		
		driver.findElement(By.id("loginbtn")).click();
		
		String loginClickHtml = driver.getPageSource(); //获取点击登录按钮后的html页面
		System.out.println(loginClickHtml);
	}
	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

	public static void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		for (String s : accountNum) {
			if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
			}
			Thread.sleep(500L);
		}
	}

}
