package app.controller;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/winio")
public class CmbChinaController{ 
	
	
	private WebDriver driver;
	
	static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";

	static String GenIndex = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenIndex.aspx";

	static String genLoginVerifyM2 = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenLoginVerifyM2.aspx";

	static String LoginPage = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/Login.aspx";

	/**
	 * @Des POST 登录接口
	 * @param bankJsonBean
	 */ 
	@GetMapping(path = "/cmbchina")
	public String logincmbchina(String username,String password) throws Exception {
		System.out.println("username-------"+username);
		System.out.println("password-------"+password);
		// 打开招行页面输入用户名密码登录 (异步)
		WebDriver webDriver = openloginCmbChina(); 
		if(driverPath==null){
			System.out.println("WebDriverIEService initIE RuntimeException  webdriver.ie.driver 初始化失败！");
			throw new RuntimeException("webdriver.ie.driver 初始化失败！");
		}
		
		/*
		System.out.println("开始输入卡号 "+username); 
		//键盘输入卡号
		Thread.sleep(500L);
		VirtualKeyBoard.KeyPressEx(username,100);//  sleep 不易设置果断，否则可能造成没有输入上的情况（11位数只输入了10位）
		
		System.out.println("开始输入Tab"); 
		//键盘输入Tab，让游览器焦点切换到密码框
		Thread.sleep(500L);
		VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		
		System.out.println("开始输入密码"+password); 
		//键盘输入查询密码
		Thread.sleep(500L);
		VirtualKeyBoard.KeyPressEx(password,100);//   
		 
		System.out.println("开始点击登陆按钮"+"#LoginBtn");
		//点击游览器的登录按钮
		Thread.sleep(500L); 
		webDriver.findElement(By.id("LoginBtn")).click(); 
		
		*/
		return "123";
	} 
	
	/**
	 * @Des 打开招行登录页面
	 * @param  无
	 */
	public WebDriver openloginCmbChina(){ 
		driver = getNewWebDriver();
		System.out.println("WebDriverIEService loginCmbChina Msg 开始登陆招商银行登陆页"); 
		try{
			driver = getPage(driver,LoginPage);
		}catch(NoSuchWindowException e){ 
			System.out.println("打开招行登录页面报错，尝试重新初始化游览器"+e.getMessage()); 
			driver = getPage(driver,LoginPage);
		} 
		System.out.println("WebDriverIEService loginCmbChina Msg 招商银行登陆页加载已完成,当前页面句柄"+driver.getWindowHandle());
		return driver;
	} 
	
	public WebDriver getNewWebDriver(){ 
		 
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        
		System.setProperty("webdriver.ie.driver", driverPath );  
		if(driverPath==null){
			System.out.println("WebDriverIEService initIE RuntimeException webdriver.ie.driver 初始化失败！");
			throw new RuntimeException("webdriver.ie.driver 初始化失败！");
		}
		 
		System.out.println("WebDriverIEService initIE Msg webdriver.ie.driver 初始化");
		 
		System.out.println("-------------getWebDriver() 初始化新的WebDriver---------");
		//WebDriver driver = new EventFiringWebDriver(new InternetExplorerDriver(ieCapabilities)).register(new LogEventListener());
		WebDriver driver = new InternetExplorerDriver(ieCapabilities);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);//页面加载timeout 30秒 
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS); //JavaScript加载timeout 30秒
	 
		Set<String> handlers = driver.getWindowHandles();
		for(String handler:handlers){
			System.out.println("handler-------------"+handler);
		}
		return driver;
	}
	 
	public WebDriver getPage(WebDriver driver,String url) throws NoSuchWindowException{
		System.out.println("getPage:"+url); 
		//driver = getWebDriver(); 
		driver.get(url);
		return driver;
	}
	

}
