package app.test;

import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xvolks.jnative.exceptions.NativeException;

import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

/**
 * @description:
 * @author: sln 
 * @date: 2017年11月9日 上午11:03:35 
 */
public class Test {
	public static void main(String[] args) {
//		String str="20171109";
//		System.out.println(str.substring(0, 4));
//		System.out.println(str.substring(4,6));
//		System.out.println(str.substring(6,8));
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", "C:/IEDriverServer_Win32/IEDriverServer.exe");

		WebDriver driver = new InternetExplorerDriver(ieCapabilities);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);//页面加载timeout 30秒 
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS); //JavaScript加载timeout 30秒
		
		
		try {
			String baseUrl = "https://sbank.hxb.com.cn/easybanking/jsp/indexComm.jsp";
			driver.get(baseUrl);  
			driver.manage().window().maximize();  //页面最大化
			Thread.sleep(2000L); 
			
		    //证件号方式登录
			driver.findElement(By.id("divli2")).click();//把焦点定为到"证件号登录"
			Thread.sleep(2000); 
			InputTab();  //先定位到证件类型
			driver.findElement(By.id("idNo")).sendKeys("123456789123456789");
			Thread.sleep(2000);
			InputTab();
			VirtualKeyBoard.KeyPressEx("12qwaszx",60);// 输入登录密码
			Thread.sleep(1000);
		WebElement loginButton = driver.findElement(By.name("qy_sut"));
			Actions action=new Actions(driver);  
			action.click(loginButton).perform();
			try {
				WebDriverWait wait = new WebDriverWait(driver, 10);   
				Boolean isLogon=wait.until(ExpectedConditions.urlToBe("https://sbank.hxb.com.cn/easybanking/PAccountWelcomePage/FormParedirectAction.do?actionType=entry"));
				if(isLogon==true){
					System.out.println("登录成功，进入首页面");
				}	
			}catch (Exception e) {
				Thread.sleep(2000);
				
				String currentUrl = driver.getCurrentUrl();
				if(currentUrl.equals("https://sbank.hxb.com.cn/easybanking/login.do?")){   //登录后进入的是修改密码的网页
					String pageSource = driver.getPageSource();
					
					System.out.println("点击登录之后获取的页面信息是："+pageSource);
					if(pageSource.contains("MsgTable")){
						System.out.println("登录过程中出现错误");
						Document doc = Jsoup.parse(pageSource);
						String errMsg = doc.getElementById("mess").text();
						System.out.println("打印出来的 错误信息是："+errMsg);
					}else{
						if(pageSource.contains("由于您的登录密码过于简单")){  //需要修改登录密码
							System.out.println("需要调用修改密码接口");
						}
					}
				}else{
					System.out.println("登录超时，华夏银行系统繁忙，请稍后再试！"+e.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}
}
