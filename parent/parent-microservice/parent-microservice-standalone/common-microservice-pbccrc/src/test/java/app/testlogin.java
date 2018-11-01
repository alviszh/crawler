package app;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by Administrator on 2018/4/17.
 */
public class testlogin {
    static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";
    private static final String LEN_MIN = "0";
    private static final String TIME_ADD = "0";
    private static final String STR_DEBUG = "a";


    public static void main(String[] args) throws Exception {
        try {
            DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
            /*Proxy proxy = new Proxy();
            String PROXY = "54.222.228.255:3200";
            proxy.setHttpProxy(PROXY);
            proxy.setFtpProxy(PROXY);
            proxy.setSslProxy(PROXY);
            ieCapabilities.setCapability(CapabilityType.PROXY, proxy);*/
            ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

            System.out.println("launching IE browser");
            System.setProperty("webdriver.ie.driver", driverPath);
            WebDriver webDriver = new InternetExplorerDriver();
            webDriver = new InternetExplorerDriver(ieCapabilities);
            webDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            webDriver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
            String baseUrl = "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp";

            webDriver.get(baseUrl);
            Thread.sleep(2000);

            String username = "zengmy0318";
            String password = "zengmy0318";

            //键盘输入账号
            webDriver.findElement(By.id("loginname")).sendKeys(username);
            Thread.sleep(1000);

            //输入密码
            webDriver.findElement(By.id("pass")).sendKeys(password);
            Thread.sleep(1000);
//            VirtualKeyBoard.KeyPressEx(password, 100);
            VK.KeyPress(password);

//            String captchaImgPath = WebDriverUnit.saveImg(webDriver, By.id("imgrc"));
            String captchaImgPath = WebDriverUnit.saveImg(webDriver, By.id("imgrc"), 17,2, "png");
            System.out.println("captchaImgPath---------------" + captchaImgPath);
            String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1006", LEN_MIN, TIME_ADD, STR_DEBUG,
                    captchaImgPath);
            System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
            Gson gson = new GsonBuilder().create();
            String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
            code = code.toLowerCase();
            System.out.println("code ====>>" + code);

          /*  //输入图片验证码
            webDriver.findElement(By.name("_@IMGRC@_")).sendKeys(code);
            Thread.sleep(1000);

            //模拟点击登录
            webDriver.findElement(By.cssSelector("input[onclick='FormSubmit();'] ")).click();

            Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(10, TimeUnit.SECONDS)
                    .pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
            WebElement main = null;
            try {
                main = wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        return driver.findElement(By.className("erro_div3"));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("err==============");
            }
            if (main != null) {
                System.out.println("--------------------------------有" + main.getText());
            } else {
                System.out.println("--------------------------------无");
            }

            WebElement loginNameInfo = null;
            try {
                loginNameInfo = webDriver.findElement(By.id("loginNameInfo"));
            } catch (Exception e) {
                System.out.println("err====" + e.getMessage());
            }
            String errInfo = "";
            if (loginNameInfo != null) {
                String info = loginNameInfo.getText();
                if (info != null && !info.equals("")) {
                    errInfo = info;
                }
            }

            System.out.println(errInfo);*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

   /* public static void InputEnter() throws IllegalAccessException, NativeException, Exception {
        Thread.sleep(1000L);
        if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
            VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Enter"));
        }
    }

    public static void InputTab() throws IllegalAccessException, NativeException, Exception {
        Thread.sleep(1000L);
        if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
            VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
        }
    }

    public static void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
        Thread.sleep(1000L);
        for (String s : accountNum) {
            if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
                VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
            }
            Thread.sleep(500L);
        }
    }*/

    //将String保存到本地
    public static void savefile(String filePath, String fileTxt) throws Exception {
        File fp = new File(filePath);
        PrintWriter pfp = new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
    }

    //从当日期开始往前推n年
    public static String getBeforeYear(int n){
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -n);
        String beforeYear = f.format(c.getTime());
        return beforeYear;
    }
    public static Page getPage(String url, String body,WebClient webClient) throws Exception {
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);

        webRequest.setAdditionalHeader("Accept","text/html, application/xhtml+xml, */*");
        webRequest.setAdditionalHeader("Accept-Language","zh-CN");
        webRequest.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded");
        webRequest.setAdditionalHeader("Host","ebank.bankofbeijing.com.cn");
        webRequest.setAdditionalHeader("Referer","https://ebank.bankofbeijing.com.cn/servlet/BCCBPB.Trans");
        webRequest.setRequestBody(body);
        Page page = webClient.getPage(webRequest);
        return page;
    }
}
