package test.webdriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.xvolks.jnative.exceptions.NativeException;

import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

public class TaobaoTest {

	static String driverPath = "D:\\software\\IEDriverServer_Win32\\IEDriverServer.exe";
	
	//static String driverPath = "C:\\Program Files\\Internet Explorer\\iexplore.exe";

	public static void main(String[] args) throws Exception {
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath );

		WebDriver driver = new InternetExplorerDriver();

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://login.taobao.com/member/login.jhtml"; 
		driver.get(baseUrl);
		String currentUrl = driver.getCurrentUrl();
		System.out.println("currentUrl--11--"+currentUrl); 
		String htmlsource = driver.getPageSource(); 
		System.out.println("htmlsource--11--"+htmlsource); 
		
		//driver.findElement(By.id("login-switch")).click();// J_Quick2Static
		
		driver.findElement(By.id("J_Quick2Static")).click();// 
		
		String[] accountNum = { "m", "d", "1", "9", "8", "4", "1", "0", "0", "2"};
		
		String[] password = { "M", "d", "8", "7", "3", "1", "5", "4", "5", "0"}; 

		Thread.sleep(1000L);

		Input(accountNum);//  

		InputTab(); // 

		Input(password);// 
		
		driver.findElement(By.id("J_SubmitStatic")).click();//�����¼�ύ��ť
		
		

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

	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

}
