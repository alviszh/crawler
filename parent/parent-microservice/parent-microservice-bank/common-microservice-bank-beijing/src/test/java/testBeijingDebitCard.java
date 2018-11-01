import app.parser.BeijingBankDebitCardParser;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.spdb.SpdbDebitCardTransFlow;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by zmy on 2018/3/9.
 */
public class testBeijingDebitCard {

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
        Thread.sleep(1000L);//
        WebElement logonid = driver.findElement(By.id("logonid_temp"));

        Thread.sleep(1000);
        logonid.sendKeys("6214686001329643");

        Thread.sleep(1000);
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
        System.out.println("sessionId===="+sessionId);

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

        System.out.println("islogin====" +islogin);

        Set<Cookie> cookies =  driver.manage().getCookies();
        WebClient webClient = WebCrawler.getInstance().getWebClient();//
        for(Cookie cookie:cookies){
            System.out.println(cookie.getName()+"---------------"+cookie.getValue());
            webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("ebank.bankofbeijing.com.cn",cookie.getName(),cookie.getValue()));
        }

        //账户信息
        String url = "https://ebank.bankofbeijing.com.cn/servlet/BCCBPB.Trans";

//        WebRequest preQHWebRequest = new WebRequest(new URL(url), HttpMethod.POST);
//        preQHWebRequest.setAdditionalHeader("Accept","text/html, application/xhtml+xml, */*");
//        preQHWebRequest.setAdditionalHeader("Accept-Language","zh-CN");
//        preQHWebRequest.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded");
//        preQHWebRequest.setAdditionalHeader("Host","ebank.bankofbeijing.com.cn");
//        preQHWebRequest.setAdditionalHeader("Referer","https://ebank.bankofbeijing.com.cn/servlet/BCCBPB.Trans");

        /*String body = "dse_sessionId=" + sessionId +
                "&dse_parentContextName=" +
                "&dse_operationName=queryAccountBalanceOp" +
                "&dse_pageId=9" +
//                "&dse_processorState=" +
//                "&dse_processorId=" +
                "&tranCode=Pb00501" +
                "&tranFlag=0" +
//                "&topNum=010100" +
//                "&currType=" +
//                "&saveType=" +
                "&accountNo=6214686001329643";
//                + "&subAccount=&sDate=&eDate=&Alais=";
        preQHWebRequest.setRequestBody(body);
        Page preQHPage = webClient.getPage(preQHWebRequest);
        int preQHStatusCode = preQHPage.getWebResponse().getStatusCode();
        if (preQHStatusCode == 200) {
            String html = preQHPage.getWebResponse().getContentAsString();
            System.out.println("=================preQHHtml=" + html);

            BeijingBankDebitCardParser parser = new BeijingBankDebitCardParser();
            TaskBank taskBank =  new TaskBank();
            taskBank.setTaskid("11");
            parser.accountInfoParser(taskBank, html);

            //获取账户的详情信息
            String accountBody = "dse_sessionId=" + sessionId +
                    "&dse_parentContextName=" +
                    "&dse_operationName=queryBaseInfoOp" +
                    "&dse_pageId=10" +
//                    "&dse_processorState=" +
//                    "&dse_processorId=" +
                    "&tranCode=" + "Pb00502" +
                    "&topNum=" + "010100" +
                    "&tranFlag=" + "0" +
                    "&currType=" + "01" +
//                    "&saveType=" + "20" +
                    "&saveType=" + "01" +
                    "&accountNo=" + "6214686001329643" +
//                    "&subAccount=" + "002" +60
                    "&subAccount=" + "001" +
                    "&sDate=&eDate=" +
                    "&addFlag=" + "2" +
                    "&account=" + "6214686001329643" +
                    "&tranStep=" + "0";
            preQHWebRequest.setRequestBody(accountBody);
            Page accountPage = webClient.getPage(preQHWebRequest);
            System.out.println("=========账户详细信息：" + accountPage.getWebResponse().getContentAsString());
            String accounthtml = accountPage.getWebResponse().getContentAsString();
            parser.accountInfoDetailParser(accounthtml);

        }*/
        //分账户列表
        String url1 = "https://ebank.bankofbeijing.com.cn/servlet/BCCBPB.Trans?dse_sessionId=" + sessionId +
                "&dse_parentContextName=" +
                "&dse_operationName=selectAccMsgOp" +
                "&dse_pageId=11" +
//                "&dse_processorState=&dse_processorId=" +
                "&accountNo=6214686001329643&tranFlag=0";
        WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
        Page result = webClient.getPage(webRequest);
        String acctMsgHtml = result.getWebResponse().getContentAsString();
        System.out.println("============result:"+ acctMsgHtml);

        String body = "dse_sessionId=" + sessionId +
                "&dse_parentContextName=" +
                "&dse_operationName=tranDispatchOp" +
                "&dse_pageId=8&menuCode=PB020103"
//                "&menuSwitch=2" +
//                "&replyPage=null"
                ;
        Page page1 = getPage(url, body, webClient);
        String html = page1.getWebResponse().getContentAsString();
        System.out.println("====参数==="+ html);

        BeijingBankDebitCardParser parser = new BeijingBankDebitCardParser();
        Map<String, String> transFlowBase = parser.getTransFlowBase(html,"selectAccMsgOp", sessionId);
        List<String> transflowBody = parser.getTransflowBody(acctMsgHtml, transFlowBase);

        /*String body2 = "dse_sessionId=" + sessionId +
                "&dse_parentContextName=" +
                "&dse_operationName=queryHistoryDetailOp" +
                "&dse_pageId=9" +
//                "&dse_processorState=" +
//                "&dse_processorId=" +
                "&tranCode=Pb00503" +
                "&tranFlag=2" +
                "&topNum=010300" +
                "&currType=01" +
                "&saveType=01" +
                "&accountNo=6214686001329643" +
                "&subAccount=001" +
                "&sDate=20170320" +
                "&eDate=20180320" +
                "&beginPos=-1" +
//                "&openOrgCode=08901" +
                "&showFlg=0";*/

        Thread.sleep(1000);
        for (int i = 0; i < transflowBody.size(); i++) {
            Thread.sleep(5000);
            if (transflowBody.get(i).contains("subAccount=001")) {
                System.out.println("tranBody=" + transflowBody.get(i));
            Page page2 = getPage(url, transflowBody.get(i), webClient);
//            System.out.println("====明细===" + page2.getWebResponse().getContentAsString());

                //下一页（根据beginPos）
                String detailBody = "dse_sessionId=" + sessionId +
                        "&dse_parentContextName=&dse_operationName=queryHistoryDetailOp&dse_pageId=12&dse_processorState=" +
                        "&dse_processorId=&tranCode=Pb00501&tranFlag=2&topNum=010300&currType=&saveType=&accountNo=6214686001329643" +
                        "&sDate=20170801&eDate=20180321&beginPos=20&subAccount=001&openOrgCode=00000";
                Page page3 = getPage(url, detailBody, webClient);
                System.out.println("######################");
                System.out.println(page3.getWebResponse().getContentAsString());
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&");
                TaskBank taskBank =  new TaskBank();
                taskBank.setTaskid("beijingbank");
                parser.transflowDetailParser(taskBank,page3.getWebResponse().getContentAsString());

            }

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
