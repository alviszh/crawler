package test.webdriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xvolks.jnative.exceptions.NativeException;

import test.winio.User32;
import test.winio.VKMapping;
import test.winio.VirtualKeyBoard;

public class CMBChinaTest {

	//static String driverPath = "D:\\software\\IEDriverServer_x64\\IEDriverServer.exe";
	
	static String driverPath = "D:\\software\\IEDriverServer_Win32\\IEDriverServer.exe";
	
	//static String driverPath = "C:\\Program Files\\Internet Explorer\\iexplore.exe";

	public static void main(String[] args) throws Exception {
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath );

		WebDriver driver = new InternetExplorerDriver();
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
		
		String currentUrl2 = driver.getCurrentUrl();
		System.out.println("currentUrl--22--"+currentUrl2); 
		String htmlsource2 = driver.getPageSource(); 
		System.out.println("htmlsource--22--"+htmlsource2); 
		driver.findElement(By.id("btnSendCode")).click();//点击发送短信验证码按钮 
	    System.out.print("短信验证码.[Enter]:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine(); 
		System.out.println("code: " + code); 
		driver.findElement(By.id("txtSendCode")).sendKeys(code);   //输入短信验证码 
		driver.findElement(By.id("btnVerifyCode")).click();//点击提交按钮
		
		String currentUrl3 = driver.getCurrentUrl();
		System.out.println("currentUrl--33--"+currentUrl3); 
		String htmlsource3 = driver.getPageSource(); 
		System.out.println("htmlsource--33--"+htmlsource3); 
		
		
		
		for (Cookie ck : driver.manage().getCookies()) {
			System.out.println(ck.getName() + ";" + ck.getValue() + ";" + ck.getDomain() + ";" + ck.getPath() + ";"+ ck.getExpiry() + ";" + ck.isSecure());
		}
		

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
