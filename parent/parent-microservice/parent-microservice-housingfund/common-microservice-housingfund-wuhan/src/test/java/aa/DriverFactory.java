package aa;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;


public class DriverFactory {

    public static WebDriver create() {
        
        // TODO Auto-generated method stub
        String chromdriver="F:\\IEDriverServer_Win32\\IEDriverServer.exe";
        System.setProperty("webdriver.chrome.driver", chromdriver);
        ChromeOptions options = new ChromeOptions();
     
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("chrome.switches",
                Arrays.asList("--start-maximized"));
        options.addArguments("--test-type", "--start-maximized");
        WebDriver driver=new ChromeDriver(options);
        return driver;
    }

	

}