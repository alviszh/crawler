//package org.common.microservice.eureka.china.unicomt.test;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriverService;
//import org.openqa.selenium.remote.DesiredCapabilities;
//
//import com.gargoylesoftware.htmlunit.HttpMethod;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.WebRequest;
//import com.gargoylesoftware.htmlunit.html.HtmlElement;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
//import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
//import com.gargoylesoftware.htmlunit.util.Cookie;
//import com.module.htmlunit.WebCrawler;
//
//public class aaa {
//
//	public static void main(String[] args) throws Exception {
//		
//		/*WebClient webClient = 	login();
//		panjs2(webClient);*/
//		panjs();
//	}
//	
//	public static WebClient login() throws Exception{
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String url = "http://login.189.cn/login";
//		HtmlPage html = getHtml(url, webClient);
//		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
//		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
//		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
//		username.setText("18940105113");
//		passwordInput.setText("456234");
//
//		HtmlPage htmlpage2 = button.click();
//		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
//			System.out.println("=======失败==============");
//		}
//		return htmlpage2.getWebClient();
//	}
//
//	public static void panjs( ) {
//		
//		// 设置必要参数
//		DesiredCapabilities dcaps = new DesiredCapabilities();
//		// ssl证书支持
//		dcaps.setCapability("acceptSslCerts", true);
//		// 截屏支持
//		dcaps.setCapability("takesScreenshot", false);
//		// css搜索支持
//		 dcaps.setCapability("cssSelectorsEnabled", true);
//		// js支持
//		dcaps.setJavascriptEnabled(true);
//		// 驱动支持
//		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "E:\\phantomjs\\phantomjs.exe");
//		// 创建无界面浏览器对象
//		PhantomJSDriver driver = new PhantomJSDriver(dcaps);
//
//		driver.get("http://login.189.cn/login");
//		//System.out.println(driver.getPageSource());
//
//		WebElement userNameElement = driver.findElementByXPath("//input[@id='txtAccount']");
//		WebElement pwdElement = driver.findElementByXPath("//*[@id='txtShowPwd']");
//		
//		userNameElement.sendKeys("13366777357");
//		pwdElement.sendKeys("591414");
//		
//		// 获取登录按钮
//		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//		WebElement loginButton = driver.findElementByXPath("//a[@id='loginbtn']");
//
//	//	System.out.println(driver.getPageSource());
//		
//		loginButton.click();
//		/*// 设置线程休眠时间等待页面加载完成
//		try {
//			Thread.sleep(10000L);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		String windowHandle = driver.getWindowHandle();
//		driver.switchTo().window(windowHandle);
//		System.out.println(driver.getPageSource());*/
//		driver.get("http://www.189.cn/dqmh/my189/initMy189home.do");
//		
//		
//		driver.get("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01390638");
//		try {
//			Thread.sleep(20000L);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//System.out.println(driver.getPageSource());
//		
//		driver.get("http://bj.189.cn/iframe/feequery/detailBillIndex.action");
//		try {
//			Thread.sleep(20000L);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(driver.getPageSource());
//		
//		  Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();  
//		  for(org.openqa.selenium.Cookie cookie : cookies){
//			  System.out.println(cookie.getDomain()+":"+cookie.getName()+":"+cookie.getValue());
//		  }
//		/*WebElement callthremClieck = driver.findElementByXPath("//*[@id='01390638']");
//		callthremClieck.click();
//		try {
//			Thread.sleep(20000L);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("============================================");
//		System.out.println(driver.getPageSource());
//		String windowHandle = driver.getWindowHandle();
//		try {
//			Thread.sleep(20000L);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		driver.switchTo().window(windowHandle);
//		System.out.println("============================================");
//		System.out.println(driver.getPageSource());
//		*/
//	}
//
//	public static void panjs2(WebClient webClient){
//		
//		// 设置必要参数
//				DesiredCapabilities dcaps = new DesiredCapabilities();
//				// ssl证书支持
//				dcaps.setCapability("acceptSslCerts", true);
//				// 截屏支持
//				dcaps.setCapability("takesScreenshot", false);
//				// css搜索支持
//				 dcaps.setCapability("cssSelectorsEnabled", true);
//				// js支持
//				dcaps.setJavascriptEnabled(true);
//				// 驱动支持
//				dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "E:\\phantomjs\\phantomjs.exe");
//				// 创建无界面浏览器对象
//				PhantomJSDriver driver = new PhantomJSDriver(dcaps);
//				
//				Set<Cookie> cookies = webClient.getCookieManager().getCookies();
//				for (Cookie cookie : cookies) {
//					org.openqa.selenium.Cookie cookiesele = new org.openqa.selenium.Cookie( cookie.getName(),
//							cookie.getValue());
//					driver.manage().addCookie(cookiesele);
//				}
//				/*
//				 * File file=new File("E:/phantomjs");
//				 * System.setProperty("phantomjs.binary.path",file+"/phantomjs.exe");
//				 * WebDriver driver=new PhantomJSDriver();
//				 */
//				driver.get("http://www.189.cn/dqmh/my189/initMy189home.do");
//				System.out.println(driver.getPageSource());
//
//			
//	}
//	
//	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
//		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//		HtmlPage searchPage = webClient.getPage(webRequest);
//		return searchPage;
//	}
//}
