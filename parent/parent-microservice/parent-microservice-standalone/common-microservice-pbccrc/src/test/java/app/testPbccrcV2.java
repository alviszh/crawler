package app;

import app.bean.PbcCreditReport;
import app.htmlparser.PbcCreditFeedParser;
import app.parser.PbccrcV2Parser;
import app.service.PbccrcV2Service;
import com.crawler.pbccrc.json.PbcCreditReportFeed;
import com.crawler.pbccrc.json.ReportData;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class testPbccrcV2 {

    static String driverPath = "G:\\IE\\32\\IEDriverServer.exe";
    private static final String LEN_MIN = "0";
    private static final String TIME_ADD = "0";
    private static final String STR_DEBUG = "a";

    public static void main(String[] args) throws Exception {
        DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        //代理IP、端口
        Proxy proxy = new Proxy();
        String PROXY = "52.80.126.61:3200";
        proxy.setHttpProxy(PROXY);
        proxy.setFtpProxy(PROXY);
        proxy.setSslProxy(PROXY);
        ieCapabilities.setCapability(CapabilityType.PROXY, proxy);
        ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

        System.out.println("launching IE browser");
        System.setProperty("webdriver.ie.driver", driverPath);

        WebDriver driver = new InternetExplorerDriver(ieCapabilities);
        driver.manage().window().maximize();//放大窗口

        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        String baseUrl = "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp";
        driver.get(baseUrl);
        String currentUrl = driver.getCurrentUrl();
        System.out.println("currentUrl--11--" + currentUrl);
        Thread.sleep(1000L);//
        WebElement logonid = driver.findElement(By.id("loginname"));

        Thread.sleep(1000);
        logonid.sendKeys("lxg-197846");

        Thread.sleep(1000);

        driver.findElement(By.id("pass")).sendKeys("Lxg197846");
        VK.KeyPress("Lxg197846");
//        InputTab();

        String path = WebDriverUnit.saveImg(driver, By.id("imgrc"));
        System.out.println("path---------------" + path);
        String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1006", LEN_MIN, TIME_ADD, STR_DEBUG,
                path);
        System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
        Gson gson = new GsonBuilder().create();
        String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
        System.out.println("code ====>>" + code);

        driver.findElement(By.name("_@IMGRC@_")).sendKeys(code.toLowerCase());
        Thread.sleep(1000);

        //模拟点击登录
        driver.findElement(By.cssSelector("input[value='登录'] ")).click();

        /*InputTab();
        InputTab();

        //按回车进入主页面
        InputEnter();*/
        Thread.sleep(10000);

        String currentUr = driver.getCurrentUrl();
        System.out.println("currentUrl--" + currentUr);
//        savefile("G:\\beijinglogined.txt", driver.getPageSource());

        //等待10秒，如果还没有登录成功的标识input#MobilePasswd(跳转到输入短信验证码页面) 则表示登录失败
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(org.openqa.selenium.NoSuchElementException.class);
        WebElement islogin = null;

        try {
            islogin = wait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(By.id("mainFrame"));
                }
            });

        } catch (Exception e) {
            System.out.println("exception="+e.toString());
            System.out.println("登录失败！");
        }

        String page = driver.getPageSource();
        System.out.println("page--" + page);
        System.out.println("islogin====" +islogin);

        Set<Cookie> cookies =  driver.manage().getCookies();
        WebClient webClient = WebCrawler.getInstance().getWebClient();//
        for(Cookie cookie:cookies){
            System.out.println(cookie.getName()+"---------------"+cookie.getValue());
            webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("ipcrs.pbccrc.org.cn",cookie.getName(),cookie.getValue()));
        }


        //获取报告
        WebRequest request = new WebRequest(new URL("https://ipcrs.pbccrc.org.cn/simpleReport.do?method=viewReport"), HttpMethod.POST);
        request.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
        request.setAdditionalHeader("Referer", "https://ipcrs.pbccrc.org.cn/reportAction.do?method=queryReport");
        request.setAdditionalHeader("Accept-Language", "zh-CN");
        request.setAdditionalHeader("zh-CN", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
        request.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
        request.setAdditionalHeader("Host", "ipcrs.pbccrc.org.cn");
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new NameValuePair("reportformat", "21"));
        nameValuePairs.add(new NameValuePair("tradeCode", "cc4vhf")); //身份验证码
        request.setRequestParameters(nameValuePairs);
        //代理ip，端口
        request.setProxyHost("52.80.126.61");
        request.setProxyPort(3200);
        Page reportPage = webClient.getPage(request);
        String html = reportPage.getWebResponse().getContentAsString();
        System.out.println("reportPage====" + html);

        Document doc = Jsoup.parse(html);
        Elements selects = doc.select("span[class=erro_div1]");
        if (selects!=null && selects.size() > 0) {
            System.out.println("授权码错误！");
            System.out.println(selects.get(0).text());

        } else {
            PbcCreditFeedParser pbcCreditFeedParser = new PbcCreditFeedParser();
            PbcCreditReportFeed pbcCreditReportFeed = pbcCreditFeedParser.getPbcCreditFeed(html);
            System.out.println("pbcCreditReportFeed====" + pbcCreditReportFeed);

            ReportData reportData = new ReportData(null, null, pbcCreditReportFeed, null);
            System.out.println("reportData==" + reportData);
            PbccrcV2Service service = new PbccrcV2Service();
//        String report = service.getreport(reportData);


            PbccrcV2Parser parser = new PbccrcV2Parser();
            PbcCreditReport report = parser.parserReportData(reportData,null);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(report);
            System.out.println("report=====" + json);
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
