/**
 * 
 */
package app.test;

import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.module.ddxoft.VK;

import app.service.common.HxbChinaHelperService;

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
        System.out.println("driverPath====>>>"+driverPath);
		System.setProperty("webdriver.ie.driver", driverPath );  
		if(driverPath==null){
			throw new RuntimeException("webdriver.ie.driver 初始化失败！");
		}		 
		System.out.println("-------------getWebDriver() 初始化新的WebDriver---------");
		WebDriver driver = new InternetExplorerDriver(ieCapabilities);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);//页面加载timeout 30秒 
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS); //JavaScript加载timeout 30秒
		String baseUrl = "https://sbank.hxb.com.cn/easybanking/jsp/indexComm.jsp";
		driver.get(baseUrl);  
		driver.manage().window().maximize();  //页面最大化
		Thread.sleep(2000L); 
		try {
			String loginType = "IDNUM";
			if (loginType.equals("ACCOUNT_NUM")) { // 用户名方式登录（个人账户号）
				driver.findElement(By.id("divli1")).click();// 把焦点定为到"用户名登录"
				Thread.sleep(1000);
				VK.Tab(); // 定位到用户名输入框
				driver.findElement(By.id("alias")).sendKeys(""); // 此方式可以输入中文
				Thread.sleep(1000);
//				VK.Tab();
				driver.findElement(By.id("passwd")).sendKeys(Keys.ENTER);	
				VK.KeyPress("");// 输入登录密码
				Thread.sleep(1000);
			} else { // 证件号方式登录
				driver.findElement(By.id("divli2")).click();//把焦点定为到"证件号登录"
				Thread.sleep(1000); 
				VK.Tab();  //先定位到证件类型
				driver.findElement(By.id("idNo")).sendKeys("142301198507272315");
				Thread.sleep(1000);
//				VK.Tab();  //改成如下，就不会少输入密码位数了
				driver.findElement(By.id("passwd")).sendKeys(Keys.ENTER);	
				VK.KeyPress("zz99658!!");// 输入登录密码
				Thread.sleep(1000);
			}
			WebElement loginButton = driver.findElement(By.name("qy_sut"));
			Actions action = new Actions(driver);
			action.click(loginButton).perform();
			try {
				WebDriverWait wait = new WebDriverWait(driver, 5);
				Boolean isLogon = wait.until(ExpectedConditions.urlToBe(
						"https://sbank.hxb.com.cn/easybanking/PAccountWelcomePage/FormParedirectAction.do?actionType=entry"));
				if (isLogon == true) {
					System.out.println("登陆成功！！！");
				}
			} catch (Exception e) {
				String currentUrl = driver.getCurrentUrl();
				if (currentUrl.equals("https://sbank.hxb.com.cn/easybanking/login.do?")) { // 登录后进入的是修改密码的网页
					String pageSource = driver.getPageSource();
					if (pageSource.contains("MsgTable")) {
						Document doc = Jsoup.parse(pageSource);
						String errMsg = doc.getElementById("mess").text();
						System.out.println("登陆错误提示信息："+errMsg);
					} else {
						if (pageSource.contains("由于您的登录密码过于简单")) { // 需要修改登录密码
							System.out.println("由于您的登录密码过于简单，请先去官网修改初始密码");
						}
					}
				} else {
					System.out.println("输入相关用户名密码等登陆信息后，校验一定时间后没有进入首页面,故提示：登录异常，华夏银行系统繁忙，请稍后再试！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		HxbChinaHelperService.killProcess();
	}
}
