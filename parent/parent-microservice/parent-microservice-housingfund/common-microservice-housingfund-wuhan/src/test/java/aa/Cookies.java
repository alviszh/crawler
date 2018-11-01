package aa;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.xvolks.jnative.exceptions.NativeException;

import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import org.openqa.selenium.Cookie;

public class Cookies {
    /**
     * @author Young
     * 
     */
	private static Robot robot;
	
    public static void addCookies() {

        WebDriver driver = DriverFactory.create();
        driver.get("https://whgjj.hkbchina.com/portal/pc/login.html");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
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
		
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File file = new File("broswer.data");
        try {
            // delete file if exists
            file.delete();
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Cookie ck : driver.manage().getCookies()) {
                bw.write(ck.getName() + ";" + ck.getValue() + ";"
                        + ck.getDomain() + ";" + ck.getPath() + ";"
                        + ck.getExpiry() + ";" + ck.isSecure());
                bw.newLine();
                System.out.println(ck);
            }
            bw.flush();
            bw.close();
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("cookie write to file");
        }
    }
    
    public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}
}
