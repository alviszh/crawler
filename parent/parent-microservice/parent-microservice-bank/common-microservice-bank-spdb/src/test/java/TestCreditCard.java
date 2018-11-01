import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;
import com.module.ocr.utils.AbstractChaoJiYingHandler;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by zmy on 2017/12/5.
 */
public class TestCreditCard extends AbstractChaoJiYingHandler {

    static String driverPath = "G:\\IE\\32\\IEDriverServer.exe";
    private static final String LEN_MIN = "0";
    private static final String TIME_ADD = "0";
    private static final String STR_DEBUG = "a";

    public static void main(String[] args) throws Exception {
//        login();
//        parserTest();
        System.out.println(getBeforeMonth(0));;
    }
    //从当日期开始往前推n月
    public static String getBeforeMonth(int n){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -n);
        String beforeMonth = f.format(c.getTime());
        return beforeMonth;
    }

    public static void parserTest(){
        String html = "var pageCurrentRecord = new Array(\"BookedDate|20171020\",\"TurnoverSign|1\",\"RegisterDate|20171020\",\"TransJnlNo|203033\",\"ConsumeTime|15041193\");\n" +
                "var pagePrevRecord = new Array(\"BookedDate|\",\"BookedDate|\",\"TurnoverSign|\",\"RegisterDate|\",\"TransJnlNo|\",\"ConsumeTime|\");\n" +
                "function clickCommand(command){\n" +
                "document.form1.Command.value=command\n" +
                "if('firstPage' == command) return;\n" +
                "var valueArr = pageCurrentRecord; ";

        int i = html.indexOf("var pageCurrentRecord");
        int j = html.indexOf("\n", i);
        String pcr = html.substring(i, j);
        System.out.println(pcr);

        String bookedDate = pcr.substring(pcr.indexOf("BookedDate"), pcr.indexOf('"',pcr.indexOf("BookedDate")));
        String turnoverSign = pcr.substring(pcr.indexOf("TurnoverSign"), pcr.indexOf('"', pcr.indexOf("TurnoverSign")));
        String registerDate = pcr.substring(pcr.indexOf("RegisterDate"), pcr.indexOf('"',pcr.indexOf("RegisterDate")));
        String transJnlNo = pcr.substring(pcr.indexOf("TransJnlNo"), pcr.indexOf('"',pcr.indexOf("TransJnlNo")));
        String consumeTime = pcr.substring(pcr.indexOf("ConsumeTime"), pcr.indexOf('"',pcr.indexOf("ConsumeTime")));

//        String[] s = turnoverSign.split("|");
//        System.out.println("===="+ turnoverSign.substring(turnoverSign.indexOf("|")), turnoverSign.length());

//        System.out.println(turnoverSign.indexOf("|"));
        /*System.out.println(turnoverSign);
        System.out.println(bookedDate.substring(bookedDate.indexOf("|")+1, bookedDate.length()));
        System.out.println(bookedDate);
        System.out.println(bookedDate.split("\\|")[1]);*/
        System.out.println(pcr.substring(pcr.indexOf("TurnoverSign"), pcr.indexOf('"', pcr.indexOf("TurnoverSign"))).split("\\|")[1]);
        System.out.println(pcr.substring(pcr.indexOf("RegisterDate"), pcr.indexOf('"',pcr.indexOf("RegisterDate"))).split("\\|")[1]);
        System.out.println(pcr.substring(pcr.indexOf("TransJnlNo"), pcr.indexOf('"',pcr.indexOf("TransJnlNo"))).split("\\|")[1]);
        System.out.println(pcr.substring(pcr.indexOf("ConsumeTime"), pcr.indexOf('"',pcr.indexOf("ConsumeTime"))).split("\\|")[1]);

    }

    public static void login() throws Exception{
        DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

        System.out.println("launching IE browser");
        System.setProperty("webdriver.ie.driver", driverPath);

        WebDriver driver = new InternetExplorerDriver(ieCapabilities);

        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        String baseUrl = "https://cardsonline.spdbccc.com.cn/icard/icardlogin.do?_locale=zh_CN";
        driver.get(baseUrl);
        String currentUrl = driver.getCurrentUrl();
        System.out.println("currentUrl--11--" + currentUrl);
        Thread.sleep(1000L);
        driver.findElement(By.name("IdNo")).sendKeys("340223199602250010");

        Thread.sleep(1000L);
        driver.findElement(By.id("OPassword")).sendKeys(Keys.DOWN);
        VirtualKeyBoard.KeyPressEx("389869", 50);//
        Thread.sleep(1000L);

        String path = WebDriverUnit.saveImg(driver, By.id("CaptchaImg"));
        System.out.println("path---------------" + path);
        String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1006", LEN_MIN, TIME_ADD, STR_DEBUG,
                path);
        System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
        Gson gson = new GsonBuilder().create();
        String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
        System.out.println("code ====>>" + code);

        driver.findElement(By.name("Token")).sendKeys(code);

        //输入回车登录
        InputEnter();//

        Thread.sleep(5000);
        /*try {
            //错误信息
            Alert alert = driver.switchTo().alert();
            System.out.println("错误信息alert:"+alert.getText());

        } catch (Exception e) {
            System.out.println("没有alert弹框");
        }*/

        //https://cardsonline.spdbccc.com.cn/icard/login.do //输入验证码页面
        //等待10秒(每2秒轮训检查一遍)，如果还没有登录成功的标识input#MobilePasswd(跳转到输入短信验证码页面) 则表示登录失败
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
        WebElement mobilePasswd = null;

        //截图
//        String screenPath = WebDriverUnit.saveScreenshotByPath(driver, TestCreditCard.class);
        try {
            mobilePasswd = wait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(By.id("MobilePasswd"));
                }
            });

        } catch (Exception e) {
            String exStr = e.toString();
            System.out.println("#mobilePasswd 元素等待10秒未出现,异常:"+exStr+"," +
                    "截图:"
//                    + screenPath
                    +" 当前页面URL:" + driver.getCurrentUrl());

            //登录失败，获取错误信息
            String errInfo = "";
            String key = "Modal dialog present:";
            if (exStr.contains(key)) {
                if (exStr.contains("非常抱歉，目前我们无法完成您的请求")){
                    errInfo = "身份证号或者密码不正确";
                } else {
                    errInfo = exStr.substring(exStr.indexOf(key) + key.length()).trim();
                }
            } else {
                errInfo = "身份证号或者密码不正确";
            }
            System.out.println("登录失败，提示错误信息："+ errInfo);

        }

        if (mobilePasswd==null) {

        } else {
            System.out.println("登录成功");

        }

    }

    public static void InputEnter() throws IllegalAccessException, NativeException, Exception {
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
    }

    //将String保存到本地
    public static void savefile(String filePath, String fileTxt) throws Exception {
        File fp = new File(filePath);
        PrintWriter pfp = new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
    }
}
