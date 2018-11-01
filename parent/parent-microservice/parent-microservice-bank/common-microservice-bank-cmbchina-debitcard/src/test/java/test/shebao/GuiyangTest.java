package test.shebao;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

public class GuiyangTest {

	static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	private static Robot robot;

	public static void main(String[] args) throws Exception {
		// 锁定用IE作为程序的工具
		/*
		 * DesiredCapabilities ieCapabilities =
		 * DesiredCapabilities.internetExplorer();
		 * ieCapabilities.setJavascriptEnabled(true);
		 * //ieCapabilities.setVersion("7"); //系统属性的名称：webdriver.ie.driver
		 * //系统属性的值：D:\\IEDriverServer_Win32\\IEDriverServer.exe
		 * //设置成为系统的全局变量！可以在项目的任何一个地方 通过System.getProperty("变量");来获得;
		 * System.setProperty("webdriver.ie.driver", driverPath);
		 * //新建一个IE浏览器的实例化 WebDriver driver = new InternetExplorerDriver();
		 * 
		 * driver = new InternetExplorerDriver(ieCapabilities);
		 */

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();

		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "http://118.112.188.109/nethall/login.jsp";
		driver.get(baseUrl);
		
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement login_person = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("login_person"));
			}
		});  
		
		System.out.println("获取到login_person---->"+login_person.getText());
		
		Thread.sleep(2000L);//这里需要休息2秒，不然点击事件可能无法弹出登录框
		
		login_person.click(); 
		/*
		
		JavascriptExecutor driver_js= ((JavascriptExecutor) driver);  
		System.out.println("执行JavaScript");
		driver_js.executeScript("showLoginPersonnel()"); 
		*/
		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("c_username"));
			}
		});
		System.out.println("获取到username---->"+username.getText());
		
		 
		
		username.click();
		username.clear();
		username.sendKeys("120103198304271124");

		//String[] accountNum = { "1", "2", "0", "4", "8", "3" };
		
		WebElement ele = driver.findElement(By.id("checkCodeC")); //这里选择验证码的dom坐标来获取 密码的坐标。实际上是因为原来密码的坐标是一个隐藏的，
		Point point = ele.getLocation(); 
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight(); 
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------"+point.getX());
		System.out.println("point.getY()-------"+point.getY());
		System.out.println("eleWidth-------"+eleWidth);
		System.out.println("eleHeight-------"+eleHeight);
		
		int x = point.getX()+eleWidth;
		int y = point.getY()+eleHeight;
		System.out.println("x------"+x);
		System.out.println("y------"+y);
		try {
			robot = new Robot();// 创建Robot对象
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		robot.mouseMove(x, y);  
        //按下和释放鼠标左键，选定工程  
        robot.mousePress(KeyEvent.BUTTON1_MASK);  
        robot.mouseRelease(KeyEvent.BUTTON1_MASK);  
		
		 
		String password = "500600";
        VirtualKeyBoard.KeyPressEx(password,50);
		
		
        String path = WebDriverUnit.saveImg(driver, By.id("codeimgC")); 
		System.out.println("path---------------"+path); 
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1005", LEN_MIN, TIME_ADD, STR_DEBUG, path); // 1005 1~5位英文数字
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code); 
		
		driver.findElement(By.id("checkCodeC")).sendKeys(code);
		
        driver.findElement(By.id("loginBtnC")).click();
        
        Wait<WebDriver> wait3 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement ele1 = wait3.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("input_div_password_79445"));
			}
		});
        
        Thread.sleep(8000L);
        
        String currentUrl = driver.getCurrentUrl();
		System.out.println("currentUrl--11--"+currentUrl);
		String htmlsource = driver.getPageSource();
		System.out.println("htmlsource--11--"+htmlsource);
		
        
        Set<Cookie> cookies =  driver.manage().getCookies(); 
        WebClient webClient = WebCrawler.getInstance().getWebClient();//   
		for(Cookie cookie:cookies){
			System.out.println(cookie.getName()+"-------cookies--------"+cookie.getValue());
			webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("118.112.188.109",cookie.getName(),cookie.getValue()));
		}
		/* 
		 
		String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do"; 
		System.out.println(url); 
		
		
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);    
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Host", "118.112.188.109");
		requestSettings.setAdditionalHeader("Referer", "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do");
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("dto['aae140']", "01"));
		requestSettings.getRequestParameters().add(new NameValuePair("dto['aae140_desc']", "城镇职工基本养老保险"));
		requestSettings.getRequestParameters().add(new NameValuePair("dto['aae002']", "2017"));  
		requestSettings.getRequestParameters().add(new NameValuePair("gridInfo['payment_list_start']", "0"));*/
		
		 
		String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae140'%5D=01&dto%5B'aae140_desc'%5D=%E5%9F%8E%E9%95%87%E8%81%8C%E5%B7%A5%E5%9F%BA%E6%9C%AC%E5%85%BB%E8%80%81%E4%BF%9D%E9%99%A9&dto%5B'aae002'%5D=2017&gridInfo%5B'payment_list_limit'%5D=100&gridInfo%5B'payment_list_start'%5D=0&"; 
		System.out.println(url); 
		
		
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);    
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Host", "118.112.188.109");
		requestSettings.setAdditionalHeader("Referer", "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do"); 
		
		Page page = webClient.getPage(requestSettings);  
		String html = page.getWebResponse().getContentAsString();  
		System.out.println("html ===="+html);	
		
 
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
