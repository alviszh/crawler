package app.service;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import app.commontracerlog.TracerLog;

@Component
public class WebDriverChromeService {

    @Value("${webdriver.chrome.driver.path}")
    String driverPathChrome;
    @Value("${webdriver.firefox.driver.path}")
    String driverPathFirefox;

    @Autowired
    private TracerLog tracerLog;

    WebDriver driver;


    public WebDriver createChromeDriver() {

        System.out.println("launching chrome browser");
        System.setProperty("webdriver.chrome.driver", driverPathChrome);

        if (driverPathChrome == null) {
            tracerLog.addTag("WebDriverIEService initChrome RuntimeException", "webdriver.chrome.driver 初始化失败！");
            throw new RuntimeException("webdriver.chrome.driver 初始化失败！");
        }

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("disable-gpu");
        chromeOptions.addArguments("lang=zh_CN.UTF-8");
        chromeOptions.addArguments("test-type");
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        driver = new ChromeDriver(capabilities);

        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

        driver.manage().window().maximize();
        return driver;

    }
    
    public WebDriver createFirefoxDriver() {

        System.out.println("launching Firefox browser");
        System.setProperty("webdriver.Firefox.driver", driverPathFirefox);

        if (driverPathFirefox == null) {
            tracerLog.addTag("WebDriverIEService initFirefox RuntimeException", "webdriver.Firefox.driver 初始化失败！");
            throw new RuntimeException("webdriver.Firefox.driver 初始化失败！");
        }

        DesiredCapabilities capabilities = DesiredCapabilities.firefox();

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("disable-gpu");
        firefoxOptions.addArguments("lang=zh_CN.UTF-8");
        firefoxOptions.addArguments("test-type");
        capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS , firefoxOptions);

        driver = new FirefoxDriver(capabilities);

        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

        driver.manage().window().maximize();
        return driver;

    }

    public void clearWebDriver() {
        driver = null;
    }

    public void quitWebDriver() {
        driver.quit();
    }

    public void clickButtonByDomId(WebDriver webDriver, String domId) throws NoSuchWindowException {
        webDriver.findElement(By.id(domId)).click();
    }
}
