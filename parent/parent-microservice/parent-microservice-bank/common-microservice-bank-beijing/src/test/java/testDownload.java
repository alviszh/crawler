import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by Administrator on 2018/6/4.
 */
public class testDownload {


    static String driverPath = "G:\\IE\\32\\IEDriverServer.exe";
    private static final String LEN_MIN = "0";
    private static final String TIME_ADD = "0";
    private static final String STR_DEBUG = "a";

    public static void main(String[] args) throws Exception {
        DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        //ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);


        System.out.println("launching IE browser");
        System.setProperty("webdriver.ie.driver", driverPath);

        WebDriver driver = new InternetExplorerDriver(ieCapabilities);
        driver.manage().window().maximize();//放大窗口

        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        String baseUrl = "https://ebank.bankofbeijing.com.cn/bccbpb/accountLogon.jsp";
        driver.get(baseUrl);
        String currentUrl = driver.getCurrentUrl();
        System.out.println("currentUrl--11--" + currentUrl);
        Thread.sleep(500L);//
        WebElement logonid = driver.findElement(By.id("logonid_temp"));

        Thread.sleep(1000);
        logonid.sendKeys("6214686001329643");

        Thread.sleep(500);
        VirtualKeyBoard.KeyPressEx("6214686001329643", 500);//
        Thread.sleep(1000L);
//        InputTab();
//        InputTab();
        //Thread.sleep(1000L);
        driver.findElement(By.id("password_temp")).sendKeys("869723");
        VirtualKeyBoard.KeyPressEx("869723", 500);
//        InputTab();

        String path = WebDriverUnit.saveImg(driver, By.id("verifyImg"));
        System.out.println("path---------------" + path);
        String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1004", LEN_MIN, TIME_ADD, STR_DEBUG,
                path);
        System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
        Gson gson = new GsonBuilder().create();
        String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
        System.out.println("code ====>>" + code);

        driver.findElement(By.name("verifyCode")).sendKeys(code.toLowerCase());
        Thread.sleep(1000);

        //模拟点击登录
        driver.findElement(By.id("button")).click();

        Thread.sleep(2000);
        //按回车进入主页面
        InputEnter();
        Thread.sleep(1000);

        String currentUr = driver.getCurrentUrl();
        System.out.println("currentUrl--" + currentUr);
        String page = driver.getPageSource();
        System.out.println("page--" + page);
//        savefile("G:\\beijinglogined.txt", driver.getPageSource());

        //获取请求参数
        Document doc = Jsoup.parse(page);
        String sessionId = doc.select("input[name=dse_sessionId]").first().val();
        System.out.println("sessionId====" + sessionId);

        //等待10秒，如果还没有登录成功的标识input#MobilePasswd(跳转到输入短信验证码页面) 则表示登录失败
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(org.openqa.selenium.NoSuchElementException.class);
        WebElement islogin = null;

        try {
            islogin = wait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(By.id("Page_content1"));
                }
            });

        } catch (Exception e) {
            System.out.println("登录失败！");
        }

        System.out.println("islogin====" + islogin);

        Set<Cookie> cookies = driver.manage().getCookies();
        WebClient webClient = WebCrawler.getInstance().getWebClient();//
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName() + "---------------" + cookie.getValue());
            webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("ebank.bankofbeijing.com.cn", cookie.getName(), cookie.getValue()));
        }

        //获取卡交易明细的请求参数
        String url = "https://ebank.bankofbeijing.com.cn/servlet/BCCBPB.Trans";
//        String body = "dse_sessionId=" + sessionId +
//                "&dse_parentContextName=" +
//                "&dse_operationName=tranDispatchOp" +
//                "&dse_pageId=8&menuCode=PB020103";
//        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//        webRequest.setAdditionalHeader("Accept","text/html, application/xhtml+xml, */*");
//        webRequest.setAdditionalHeader("Accept-Language","zh-CN");
//        webRequest.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded");
//        webRequest.setAdditionalHeader("Host","ebank.bankofbeijing.com.cn");
//        webRequest.setAdditionalHeader("Referer","https://ebank.bankofbeijing.com.cn/servlet/BCCBPB.Trans");
//        webRequest.setRequestBody(body);
//        Page page1 = webClient.getPage(webRequest);
//        System.out.println("========请求参数==============");
//        System.out.println(page1.getWebResponse().getContentAsString());
//        System.out.println("========================");


       // 获取交易记录（下载）
        String bodyd = "dse_sessionId=" + sessionId +
                "&dse_parentContextName=&dse_operationName=queryHistoryDetailOp&dse_pageId=16" +
                "&dse_processorState=&dse_processorId=&tranCode=Pb00503&tranFlag=2&topNum=010300&currType=01&saveType=01&accountNo=6214686001329643" +
                "&subAccount=001&sDate=20170604&eDate=20180604&beginPos=-1&openOrgCode=08901&showFlg=0";
        WebRequest webRequestd = new WebRequest(new URL(url), HttpMethod.POST);
        webRequestd.setAdditionalHeader("Accept","text/html, application/xhtml+xml, */*");
        webRequestd.setAdditionalHeader("Accept-Language","zh-CN");
        webRequestd.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded");
        webRequestd.setAdditionalHeader("Host","ebank.bankofbeijing.com.cn");
        webRequestd.setAdditionalHeader("Referer","https://ebank.bankofbeijing.com.cn/servlet/BCCBPB.Trans");
        webRequestd.setRequestBody(bodyd);
        Page pageDownloadHistory = webClient.getPage(webRequestd);
        System.out.println("=================pageDownloadHistory==================================");
        System.out.println(pageDownloadHistory.getWebResponse().getContentAsString());
        System.out.println("=====================pageDownloadHistory==============================");

        String body2 = "dse_sessionId=" + sessionId +
                "&dse_parentContextName=&dse_operationName=queryHistoryDetailOp&dse_pageId=23&dse_processorState=" +
                "&dse_processorId=&accountNo=6214686001329643" +
                "&openOrgBank=%B1%B1%BE%A9%D2%F8%D0%D0%CB%AB%D0%E3%D6%A7%D0%D0" +
                "&tranCode=Pb00155&showFlg=1";
        webRequestd.setRequestBody(body2);
        Page page2 = webClient.getPage(webRequestd);
        InputStream contentAsStream = page2.getWebResponse().getContentAsStream();
        //获取xls文件的存储路径
        String xlsFilePath = getXlsvFilePath("beijingbank", "6214686001329643");
        //将流水下载保存为xls文件
        saveXls(contentAsStream, xlsFilePath);


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
    public static void saveXls(InputStream inputStream, String path) throws Exception{
        OutputStream outputStream = new FileOutputStream(path);

        int byteCount = 0;

        byte[] bytes = new byte[1024];

        while ((byteCount = inputStream.read(bytes)) != -1)
        {
            outputStream.write(bytes, 0, byteCount);
        }
        inputStream.close();
        outputStream.close();
    }
    //获取存储xls文件的路径
    public static String getXlsvFilePath(String taskid, String cardNum) {
        //获取存放流水xls文件的路径
        String path = System.getProperty("user.dir")+"\\file\\";
        File parentDirFile = new File(path);
        parentDirFile.setReadable(true);
        parentDirFile.setWritable(true);
        if (!parentDirFile.exists()) {
            parentDirFile.mkdirs();
        }

        String csvPath = path+""+taskid+"_"+ cardNum +".xls";
        System.out.println("xlsPath:" + csvPath);
        return csvPath;
    }

}
