package test.webdriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import test.winio.User32;
import test.winio.VKMapping;
import test.winio.VirtualKeyBoard;

public class CMBChinaTest2 {

	//static String driverPath = "D:\\software\\IEDriverServer_x64\\IEDriverServer.exe";
	
	static String driverPath = "D:\\software\\IEDriverServer_Win32\\IEDriverServer.exe";
	
	//static String driverPath = "C:\\Program Files\\Internet Explorer\\iexplore.exe";

	public static void main(String[] args) throws Exception {
		
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        //ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
       
        
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath );

		WebDriver driver = new InternetExplorerDriver();
		 driver = new InternetExplorerDriver(ieCapabilities);
		//超时30s
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/Login.aspx";
		// String baseUrl = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenLoginVerifyM2.aspx";
		driver.get(baseUrl);
		String currentUrl = driver.getCurrentUrl();
		System.out.println("currentUrl--11--"+currentUrl); 
		String htmlsource = driver.getPageSource(); 
		System.out.println("htmlsource--11--"+htmlsource); 
		
		//
		String[] accountNum = { "6", "2", "1", "4", "8", "3", "0", "1", "6", "1", "3", "0", "0", "9", "2", "5" };

		String[] password = { "3", "3", "1", "4", "8", "3" }; 

		Thread.sleep(1000L);

		Input(accountNum);// 输入账户

		InputTab(); // 输入 Tab 切换到密码框

		Input(password);// 输入密码
		
		driver.findElement(By.id("LoginBtn")).click();//点击登录提交按钮
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSendCode")));//等待发送短信按钮出现
		/*
		for (org.openqa.selenium.Cookie ck : driver.manage().getCookies()) {
			System.out.println(ck.getName() + ";" + ck.getValue() + ";" + ck.getDomain() + ";" + ck.getPath() + ";"+ ck.getExpiry() + ";" + ck.isSecure());
		}
		*/
		String currentUrl2 = driver.getCurrentUrl();
		System.out.println("currentUrl--22--"+currentUrl2); 
		String htmlsource2 = driver.getPageSource(); 
		System.out.println("htmlsource--22--"+htmlsource2);  
		
		String ClientNo = driver.findElement(By.id("ClientNo")).getAttribute("value") ; 
		
		String __VIEWSTATE = driver.findElement(By.id("__VIEWSTATE")).getAttribute("value") ; 
		
		String __EVENTVALIDATION = driver.findElement(By.id("__EVENTVALIDATION")).getAttribute("value") ; 
		
		System.out.println("ClientNo--开始爬取--"+ClientNo); 
		System.out.println("__VIEWSTATE--开始爬取--"+__VIEWSTATE);
		System.out.println("__EVENTVALIDATION--开始爬取--"+__EVENTVALIDATION);
		
		Thread.sleep(1500L);
		driver.findElement(By.id("btnSendCode")).click();//点击发送短信验证码按钮 
	    System.out.print("短信验证码.[Enter]:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine(); 
		System.out.println("code: " + code); 
		driver.findElement(By.id("txtSendCode")).sendKeys(code);   //输入短信验证码 
		
		Thread.sleep(1500L);
		driver.findElement(By.id("btnVerifyCode")).click();//点击提交按钮
		
		/*for (org.openqa.selenium.Cookie ck : driver.manage().getCookies()) {
			System.out.println(ck.getName() + ";" + ck.getValue() + ";" + ck.getDomain() + ";" + ck.getPath() + ";"+ ck.getExpiry() + ";" + ck.isSecure());
		}*/
		
	
		//getData(ClientNo,__VIEWSTATE,__EVENTVALIDATION);
		
		
		
	}
	
	public static void getData(String ClientNo,String __VIEWSTATE,String __EVENTVALIDATION) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		String url = "https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryHistoryTrans.aspx"; 
		System.out.println(url); 
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);  
		requestSettings.setAdditionalHeader("Host", "pbsz.ebank.cmbchina.com");
		requestSettings.setAdditionalHeader("Referer", "https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryHistoryTrans.aspx");
		requestSettings.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
		requestSettings.setAdditionalHeader("DNT", "1");
		
		//webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","WEBTRENDS_ID", "123.126.87.162-2358226944.30605561"));
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("__VIEWSTATE", __VIEWSTATE));
		requestSettings.getRequestParameters().add(new NameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));
		requestSettings.getRequestParameters().add(new NameValuePair("BeginDate", "20140808"));
		requestSettings.getRequestParameters().add(new NameValuePair("EndDate", "20170802"));
		requestSettings.getRequestParameters().add(new NameValuePair("BtnOK", "查 询"));
		requestSettings.getRequestParameters().add(new NameValuePair("ClientNo", ClientNo));
		
		Page page = webClient.getPage(requestSettings);  
		String html = page.getWebResponse().getContentAsString();  
		System.out.println("html ===="+html);		
	}

	public static void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		for (String s : accountNum) {
			if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Windows Internet Explorer")) {
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
			}
			Thread.sleep(500L);
		}
	}

	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Windows Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

}
