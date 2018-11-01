package Test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Testshi {

	 static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";
	public static void main(String[] args) throws Exception {
		
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();
		 
		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://i.bank.ecitic.com/perbank6/signIn.do";
		driver.get(baseUrl);
		
		driver.manage().window().maximize();
		
//		Thread.sleep(8000);
		String pageSource = driver.getPageSource();
//		Thread.sleep(8000);
		System.out.println("pageSource--------------------"+pageSource);
	}
}
