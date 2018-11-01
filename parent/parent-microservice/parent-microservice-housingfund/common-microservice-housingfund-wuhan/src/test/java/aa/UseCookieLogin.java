package aa;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
 
import java.io.File;
import java.io.FileReader;
 

import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.xvolks.jnative.exceptions.NativeException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

public class UseCookieLogin {

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static Robot robot;
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Cookies.addCookies();
        WindowsUtils.killByName("chrome.exe");
        WindowsUtils.getProgramFilesPath();
        WebDriver driver=DriverFactory.create();
        driver.get("https://whgjj.hkbchina.com/portal/pc/htmls/LoginContent/loginContent.html");
        
        try 
        {
        String path1 = WebDriverUnit.saveImg(driver, By.id("_tokenImg"));
		System.out.println("path---------------" + path1);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,
				path1); // 1005
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
        
        
            File file=new File("broswer.data");
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            String line;
            while((line=br.readLine())!= null)
            {
                StringTokenizer str=new StringTokenizer(line,";");
                while(str.hasMoreTokens())
                {
                    String name=str.nextToken();
                    String value=str.nextToken();
                    String domain=str.nextToken();
                    String path=str.nextToken();
                    Date expiry=null;
                    String dt;
                    if(!(dt=str.nextToken()).equals(null))
                    {
                        //expiry=new Date(dt);
                        System.out.println();
                    }
                    boolean isSecure=new Boolean(str.nextToken()).booleanValue();
                    Cookie ck=new Cookie(name,value,domain,path,expiry,isSecure);
                    System.out.println(ck);
                    driver.manage().addCookie(ck);
                }
            }
            
            driver.get("https://whgjj.hkbchina.com/portal/pc/login.html");
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

          
    		InputTab();
    		InputTab();
    		InputTab();
    		InputTab();
    		InputTab();
    		InputTab();
    		
    		String LoginId = "420107198709061535";
    		VirtualKeyBoard.KeyPressEx(LoginId, 50);
    		
    		robot = new Robot();
    		//密码
    		robot.keyPress(KeyEvent.VK_TAB);
    		//huangwei1987924
    		String password = "huangwei1987924";
    		VirtualKeyBoard.KeyPressEx(password, 50);
       
            
            }
        catch(Exception e)
        {
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