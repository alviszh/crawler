/**
 * 
 */
package test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.module.ddxoft.VK;


/**
 * @author sln
 * @date 2018年8月14日上午11:36:24
 * @Description: 
 * 
 * 
 * 
 * 正确的账号和密码
 * 
 * _华夏信用卡：
 * 142301198507272315
 * zz99658!!
 * 
 * 华夏储蓄卡：
 * 420106198410028419
 * 12qwaszx
 */
public class LoginTest {
	WebDriver driver;
	static String driverPath="C:\\IEDriverServer_Win32\\IEDriverServer.exe";
	public static void main(String[] args) throws InterruptedException {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.setProperty("webdriver.ie.driver", driverPath );  
		if(driverPath==null){
			throw new RuntimeException("webdriver.ie.driver 初始化失败！");
		}
		WebDriver driver = new InternetExplorerDriver(ieCapabilities);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);//页面加载timeout 30秒 
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS); //JavaScript加载timeout 30秒
		String baseUrl = "https://sbank.hxb.com.cn/easybanking/jsp/indexComm.jsp";
		driver.get(baseUrl);  
		driver.manage().window().maximize();  //页面最大化
		Thread.sleep(2000L); 
		
		driver.findElement(By.id("divli2")).click();//把焦点定为到"证件号登录"
		Thread.sleep(1000); 
		VK.Tab();  //先定位到证件类型
		driver.findElement(By.id("idNo")).sendKeys("142301198507272315");
		Thread.sleep(1000);
		driver.findElement(By.id("passwd")).sendKeys(Keys.ENTER);	
		VK.KeyPress("zz99658!!");// 输入登录密码
		Thread.sleep(1000);
		
		WebElement loginButton = driver.findElement(By.name("qy_sut"));
		loginButton.click();
	}
}
