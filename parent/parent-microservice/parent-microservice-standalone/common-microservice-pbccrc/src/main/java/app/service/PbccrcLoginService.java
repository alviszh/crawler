package app.service;

import app.bean.LoginResult;
import app.client.proxy.HttpProxyClient;
import app.commontracerlog.TracerLog;
import com.crawler.aws.json.HttpProxyBean;
import com.crawler.aws.json.HttpProxyRes;
import com.crawler.domain.json.Result;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.pbccrc.json.ReportData;
import com.crawler.pbccrc.json.StandaloneEnum;
import com.gargoylesoftware.htmlunit.WebClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 *  人行征信登录 （重试机制）
 */

@Component
public class PbccrcLoginService {

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private WebDriverIEService webDriverIEService;
    @Autowired
    private PbccrcV2Service pbccrcV2Service;
    @Autowired
    private HttpProxyClient httpProxyClient;
    @Autowired
    private AgentService agentService;
    @Autowired
    private CrawlerStatusStandaloneService crawlerStatusStandaloneService;

    @Value("${isHttpProxy}")
    String isHttpProxy; //1使用代理ip

    private WebDriver driver = null;

    String loginUrl = "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp";
    private static final String LEN_MIN = "0";
    private static final String TIME_ADD = "0";
    private static final String STR_DEBUG = "a";

    private HttpProxyRes httpProxyRes = null;
    private static int size = 1;
    private static int proxyIndex = 0;//获取代理IP重试次数

    @Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
    public LoginResult retryLogin(PbccrcJsonBean pbccrcJsonBean){
        System.out.println("proxyIndex:"+proxyIndex);
        tracerLog.qryKeyValue("owner", pbccrcJsonBean.getOwner());
        tracerLog.qryKeyValue("username", pbccrcJsonBean.getUsername());//账号

        LoginResult loginResult = new LoginResult();

        Gson gson = new GsonBuilder().create();
        Result<ReportData> reportResult = new Result<ReportData>();//V1结果
        String path = "";
        System.out.println("isHttpProxy===="+isHttpProxy);
        tracerLog.addTag("isHttpProxy", isHttpProxy);

        long starttime = System.currentTimeMillis();
        if (pbccrcJsonBean.isFirst()) { //是否是第一次打开页面（验证码错误直接重新输入）
            size = 1; //初始化
            if (isHttpProxy.equals("1")) { //使用HTTP代理
                try {
                    tracerLog.addTag("开始获取代理IP、端口", "start");
//                       httpProxyBean = getProxy();
                    httpProxyRes = getProxyClient("3", "", "false", proxyIndex);
                    System.out.println("httpProxyRes:" + httpProxyRes);
                    tracerLog.addTag("获取代理IP、端口httpProxyRes", httpProxyRes + "");
                } catch (Exception ex) {
                    System.out.println("获取代理IP、端口出错。");
                    tracerLog.qryKeyValue("httpProxyRes.Exception", "获取代理IP、端口出错");
                    tracerLog.addTag("httpProxyRes.Exception.e", ex.toString());
                }
            }

            getLoginPBCCRCHtml(httpProxyRes);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            tracerLog.addTag("开始输入登录账号", pbccrcJsonBean.getUsername());
            //键盘输入账号
            driver.findElement(By.id("loginname")).clear();
            driver.findElement(By.id("loginname")).sendKeys(pbccrcJsonBean.getUsername());
        } catch (NoSuchElementException ex) {
            tracerLog.qryKeyValue("CrawlerLoginImpl.login.Exception", "人行征信网站被屏蔽！！！");
            tracerLog.addTag("CrawlerLoginImpl.NoSuchElementException", ex.toString());
            System.out.println("CrawlerLoginImpl.login.Exception" + ex.toString());
            ReportData reportData = new ReportData("-2", "人行征信网站被屏蔽，请稍后再试！", null, null);
            reportResult.setData(reportData);

            if (driver != null) {
                //截图
                String loginerrorPath = WebDriverUnit.getPathBySystem("loginerror");
                try {
                    path = WebDriverUnit.saveScreenshotByPath(driver, loginerrorPath);
                } catch (Exception e) {
                    tracerLog.addTag("人行征信网站被屏蔽,截图失败", e.getMessage());
                }
                tracerLog.addTag("打开人行征信网站异常", "截图:" + path);
                System.out.println("打开人行征信网站异常，截图路径：" + path);

                //释放instance ip ，quit webdriver
                tracerLog.addTag("释放instance ip ，quit webdriver:", pbccrcJsonBean.getIp());
                agentService.releaseInstance(pbccrcJsonBean.getIp(), driver);
            }
            long endtime = System.currentTimeMillis();
            tracerLog.addTag("人行征信网站被屏蔽", (endtime - starttime) + ":ms");
            if (proxyIndex >= 2) {
                proxyIndex = 0; //初始化
            } else {
                proxyIndex++;
            }
            throw new RuntimeException(gson.toJson(reportResult));
        }
        proxyIndex = 0; //初始化

        try {
            tracerLog.addTag("开始输入登录密码，mappingId=", pbccrcJsonBean.getMapping_id());
            //输入密码
            driver.findElement(By.id("pass")).sendKeys(pbccrcJsonBean.getPassword());
            Thread.sleep(500);
//        VirtualKeyBoard.KeyPressEx(pbccrcJsonBean.getPassword(), 100);
            VK.KeyPress(pbccrcJsonBean.getPassword());
            //====================开始识别图片验证码==========================
            long starttimeCode = System.currentTimeMillis();
//        String captchaImgPath = WebDriverUnit.saveImg(driver, By.id("imgrc"));
            String captchaImgPath = WebDriverUnit.saveImg(driver, By.id("imgrc"), 17, 2, "png");
            System.out.println("captchaImgPath---------------" + captchaImgPath);
            String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1006", LEN_MIN, TIME_ADD, STR_DEBUG,
                    captchaImgPath);
            System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);

            String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
            tracerLog.addTag("CrawlerLoginImpl.login", "图片验证码url：" + captchaImgPath + "，识别结果：" + code);
            System.out.println("code ====>>" + code);
            //如果验证码识别结果为空,识别重试三次
            while (size < 3) {
                if ("".equals(code) || code == null) {
                    tracerLog.addTag("超级鹰识别验证码为空，次数：", "次数：" + size + "，mappingId=" + pbccrcJsonBean.getMapping_id());
                    chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1006", LEN_MIN, TIME_ADD, STR_DEBUG,
                            captchaImgPath);
                    code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
                    tracerLog.addTag("CrawlerLoginImpl.login", "图片验证码url：" + captchaImgPath + "，识别结果：" + code);
                    size++;
                    System.out.println("第" + size + "次识别，chaoJiYingResult---------------" + chaoJiYingResult);

                } else {
                    tracerLog.addTag("超级鹰识别验证码不为空", "次数：" + size + "，mappingId=" + pbccrcJsonBean.getMapping_id()
                            + "，识别结果：" + code);
                    System.out.println("超级鹰识别验证码不为空，第" + size + "次识别，识别结果：" + code);
                    size = 1;//初始化size
                    break;
                }
            }
            code = code.toLowerCase();

            //====================识别图片验证码结束==========================
            long endtimeCode = System.currentTimeMillis();
            tracerLog.addTag("识别图片验证码所用时间：", (endtimeCode - starttimeCode) + ":ms");

            //输入图片验证码
            tracerLog.addTag("CrawlerLoginImpl.login", "图片验证码url：" + captchaImgPath + "，识别结果：" + code);
            tracerLog.addTag("开始输入图片验证码", code.toLowerCase());
//        driver.findElement(By.name("_@IMGRC@_")).clear();
            driver.findElement(By.name("_@IMGRC@_")).sendKeys(code.toLowerCase());
            Thread.sleep(500);

            //模拟点击登录
            driver.findElement(By.cssSelector("input[onclick='FormSubmit();'] ")).click();

            //等待10秒，如果还没有登录成功的标识div#mainFrame(跳转到登录成功页面) 则表示登录失败
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(6, TimeUnit.SECONDS)
                    .pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
            WebElement mainFrame = null;

            try {
                mainFrame = wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        return driver.findElement(By.id("mainFrame")); //进入主页面
                    }
                });

            } catch (Exception e) {
                tracerLog.qryKeyValue("CrawlerLoginImpl.login.Exception", "进入主页面异常");
                tracerLog.addTag("CrawlerLoginImpl.login.mainFrame.Exception.e", e.toString());
                System.out.println("exception=" + e.toString());
            }
            long endtime = System.currentTimeMillis();
            tracerLog.addTag("登录完毕", (endtime - starttime) + ":ms，mainFrame=" + mainFrame );
            System.out.println("mainFrame:" + mainFrame);

            if (mainFrame == null) {//登录失败
                //获取登录错误信息
                String errInfo = getLoginErrInfo();
                tracerLog.addTag("CrawlerLoginImpl.login.errInfo", errInfo);
                System.out.println("CrawlerLoginImpl.login.errInfo:" + errInfo);

                //登录失败
                String statusCode = "2";
                if (errInfo != null && !errInfo.equals("")) {
                    //验证码识别错误，重试三次
                    if (errInfo.contains("验证码输入错误")) {
                        tracerLog.addTag("超级鹰识别验证码错误次数：", "次数：" + size + "，mappingId=" + pbccrcJsonBean.getMapping_id());
                        size++;
                        if (size <= 3) {
                            tracerLog.qryKeyValue("超级鹰识别错误", "尝试重新登录");
                            pbccrcJsonBean.setIsFirst(false);
//                            String reportResultStr = retryLogin(pbccrcJsonBean, proxyIndex);
                            loginResult.setDriver(driver);
                            loginResult.setHttpProxyRes(httpProxyRes);
                            loginResult = retryLogin(pbccrcJsonBean);
//                        System.out.println("reportResultStr:"+reportResultStr);
                            return loginResult;
                        } else {
                            errInfo = "查询失败,请重试"; //验证码重试三次还是错误
                            statusCode = "-1";
                            size = 1;
                            tracerLog.addTag("验证码重试三次还是错误", "提示：" + errInfo);
                        }
                    }
                    if (errInfo.contains("验证码不能为空或空格")) {
                        errInfo = "查询失败,请重试"; //验证码为空，直接提示查询失败
                        statusCode = "-1";
                        tracerLog.addTag("验证码不能为空或空格", "提示：" + errInfo);
                    }

                    ReportData reportData = new ReportData(statusCode, errInfo, null, null);
                    reportResult.setData(reportData);
                    //发送状态
                    if (!statusCode.equals("-1")) {
//                        sendMessageResult(pbccrcJsonBean, "4", "用户名或密码错误");
                        crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_PASSWORD_ERROR.getPhase(),
                                StandaloneEnum.STANDALONE_PASSWORD_ERROR.getPhasestatus(),
                                StandaloneEnum.STANDALONE_PASSWORD_ERROR.getDescription(),
                                StandaloneEnum.STANDALONE_PASSWORD_ERROR.getCode(),
                                true, pbccrcJsonBean.getMapping_id());
                    } else {
//                        sendMessageResult(pbccrcJsonBean, "6", "登录失败"); 查询失败，提示“网络超时”
                        crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_LOGIN_ERROR2.getPhase(),
                                StandaloneEnum.STANDALONE_LOGIN_ERROR2.getPhasestatus(),
                                StandaloneEnum.STANDALONE_LOGIN_ERROR2.getDescription(),
                                StandaloneEnum.STANDALONE_LOGIN_ERROR2.getCode(),
                                true, pbccrcJsonBean.getMapping_id());
                    }
                    //截图
                    String loginerrorPath = WebDriverUnit.getPathBySystem("loginerror");
                    try {
                        path = WebDriverUnit.saveScreenshotByPath(driver, loginerrorPath);
                        tracerLog.addTag("登录异常", "截图:" + path);
                    } catch (Exception e1) {
                        tracerLog.addTag("登录异常,截图失败", e1.getMessage());
                    }
                    //保存状态
                    pbccrcV2Service.saveFlowStatus(pbccrcJsonBean.getMapping_id(), reportData.getMessage());

                    //释放instance ip ，quit webdriver
                    tracerLog.addTag("释放instance ip ，quit webdriver:", pbccrcJsonBean.getIp());
                    agentService.releaseInstance(pbccrcJsonBean.getIp(), driver);
                    System.out.println("登录失败：" + reportResult);


                    loginResult.setDriver(driver);
                    loginResult.setHttpProxyRes(httpProxyRes);
                    loginResult.setResultJson(gson.toJson(reportResult));
                    return loginResult;
                } else {
                    //登录超时，mainFrame、errInfo都为null
                    tracerLog.qryKeyValue("登录超时", "mainFrame、errInfo都为null");
                    ReportData reportData = new ReportData("-1", "查询失败,请重试", null, null);
                    reportResult.setData(reportData);

                    //截图
                    String loginerrorPath = WebDriverUnit.getPathBySystem("loginerror");
                    try {
                        path = WebDriverUnit.saveScreenshotByPath(driver, loginerrorPath);
                        tracerLog.addTag("登录异常", "截图:" + path);
                    } catch (Exception e1) {
                        tracerLog.addTag("登录异常,截图失败", e1.getMessage());
                    }
                    //保存状态
                    pbccrcV2Service.saveFlowStatus(pbccrcJsonBean.getMapping_id(), reportData.getMessage());
                    //发送状态
//                    sendMessageResult(pbccrcJsonBean, "6", "登录失败"); 网络超时
                    crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_LOGIN_ERROR2.getPhase(),
                            StandaloneEnum.STANDALONE_LOGIN_ERROR2.getPhasestatus(),
                            StandaloneEnum.STANDALONE_LOGIN_ERROR2.getDescription(),
                            StandaloneEnum.STANDALONE_LOGIN_ERROR2.getCode(),
                            true, pbccrcJsonBean.getMapping_id());

                    //释放instance ip ，quit webdriver
                    tracerLog.addTag("释放instance ip ，quit webdriver:", pbccrcJsonBean.getIp());
                    agentService.releaseInstance(pbccrcJsonBean.getIp(), driver);
                    System.out.println("登录失败：" + reportResult);
//                    return gson.toJson(reportResult);
                    loginResult.setDriver(driver);
                    loginResult.setHttpProxyRes(httpProxyRes);
                    loginResult.setResultJson(gson.toJson(reportResult));
                    return loginResult;
                }

            } else { //登录成功
                //保存状态
                pbccrcV2Service.saveFlowStatus(pbccrcJsonBean.getMapping_id(), "登录成功");
                //发送状态
//                sendMessageResult(pbccrcJsonBean, "0", "登录成功");
                crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_LOGIN_SUCCESS.getPhase(),
                        StandaloneEnum.STANDALONE_LOGIN_SUCCESS.getPhasestatus(),
                        StandaloneEnum.STANDALONE_LOGIN_SUCCESS.getDescription(),
                        StandaloneEnum.STANDALONE_LOGIN_SUCCESS.getCode(),
                        false, pbccrcJsonBean.getMapping_id());

                Set<Cookie> cookies = driver.manage().getCookies();
                WebClient webClient = WebCrawler.getInstance().getWebClient();//
                for (org.openqa.selenium.Cookie cookie : cookies) {
                    System.out.println(cookie.getName() + "------cookie---------" + cookie.getValue());
                    webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("ipcrs.pbccrc.org.cn", cookie.getName(), cookie.getValue()));
                }
                String cookieStr = CommonUnit.transcookieToJson(webClient);  //存储cookie
                pbccrcJsonBean.setCookieStr(cookieStr);

                /*String aa = null;
                System.out.println("异常=====" +aa.toString());*/

                ReportData reportData = new ReportData("5", "登录成功", null, null);
                reportResult.setData(reportData);
//                return gson.toJson(reportResult);

                loginResult.setDriver(driver);
                loginResult.setHttpProxyRes(httpProxyRes);
                loginResult.setResultJson(gson.toJson(reportResult));
                return loginResult;
            }

        } catch (Exception e) {
            tracerLog.qryKeyValue("CrawlerLoginImpl.login.exception", "登录异常");
            reportResult = new Result<ReportData>();
            ReportData reportData = new ReportData("6", "登录失败,请重试", null, null);
            reportResult.setData(reportData);
            tracerLog.addTag("CrawlerLoginImpl.login.exception.e", e.toString());
            System.out.println("CrawlerLoginImpl.login.exception="+ e.toString());
            //截图
            String loginerrorPath = WebDriverUnit.getPathBySystem("loginerror");
            try {
                path = WebDriverUnit.saveScreenshotByPath(driver, loginerrorPath);
                tracerLog.addTag("登录异常", "截图:" + path);
            } catch (Exception e1) {
                tracerLog.addTag("登录异常,截图失败", e1.getMessage());
            }
            //保存状态
            pbccrcV2Service.saveFlowStatus(pbccrcJsonBean.getMapping_id(), reportData.getMessage());
            //发送状态
//            sendMessageResult(pbccrcJsonBean, "6", "登录失败,请重试");
            crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_LOGIN_ERROR3.getPhase(),
                    StandaloneEnum.STANDALONE_LOGIN_ERROR3.getPhasestatus(),
                    StandaloneEnum.STANDALONE_LOGIN_ERROR3.getDescription(),
                    StandaloneEnum.STANDALONE_LOGIN_ERROR3.getCode(),
                    true, pbccrcJsonBean.getMapping_id());

            //释放instance ip ，quit webdriver
            tracerLog.addTag("释放instance ip ，quit webdriver:", pbccrcJsonBean.getIp());
            agentService.releaseInstance(pbccrcJsonBean.getIp(), driver);

//            return gson.toJson(reportResult);
            loginResult.setDriver(driver);
            loginResult.setHttpProxyRes(httpProxyRes);
            loginResult.setResultJson(gson.toJson(reportResult));
            return loginResult;
        }
    }

    /**
     * 打开人行征信登录页面
     * @return
     */
    public WebDriver getLoginPBCCRCHtml(HttpProxyRes httpProxyRes){
        driver = webDriverIEService.getNewWebDriver(httpProxyRes);
//        driver.manage().window().maximize();
        System.out.println("WebDriverIEService loginSpdb Msg 开始登录人行征信的登录页");
        tracerLog.qryKeyValue("WebDriverIEService loginSpdb Msg", "开始登录人行征信的登录页");
        try{
            driver = webDriverIEService.getPage(driver,loginUrl);
        }catch(NoSuchWindowException e){
            tracerLog.qryKeyValue("getLoginPBCCRCHtml.NoSuchWindowException", "打开人行征信登录页面报错");
            System.out.println("打开人行征信登录页面报错，尝试重新初始化游览器"+e.getMessage());
            tracerLog.addTag("打开人行征信登录页面报错，尝试重新初始化游览器", e.getMessage());
            driver  = webDriverIEService.getPage(driver, loginUrl);

        }
        tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "人行征信登陆页加载已完成,当前页面句柄" + driver.getWindowHandle());

        return driver;
    }

    public  String getLoginErrInfo() {
        String errInfo = null;
        WebElement erro_div3 = null;
        WebElement loginNameInfo = null;
        WebElement passwordInfo = null;
        WebElement imageCodeInfo = null;
        try {
            erro_div3 = driver.findElement(By.className("erro_div3"));
        } catch (Exception e) {
            tracerLog.addTag("erro_div3元素未出现,异常:" + e.toString() + " ",
                    " 当前页面URL:" + driver.getCurrentUrl());
        }
        try {
            loginNameInfo = driver.findElement(By.id("loginNameInfo"));
        } catch (Exception e) {
            tracerLog.addTag("loginNameInfo元素未出现,异常:" + e.toString() + " ",
                    " 当前页面URL:" + driver.getCurrentUrl());
        }
        try {
            passwordInfo = driver.findElement(By.id("passwordInfo"));
        } catch (Exception e) {
            tracerLog.addTag("passwordInfo元素未出现,异常:" + e.toString() + " ",
                    " 当前页面URL:" + driver.getCurrentUrl());
        }
        try {
            imageCodeInfo = driver.findElement(By.id("imageCodeInfo"));
        } catch (Exception e) {
            tracerLog.addTag("imageCodeInfo元素未出现,异常:" + e.toString() + " ",
                    " 当前页面URL:" + driver.getCurrentUrl());
        }

        if (erro_div3 != null) {
            String info = erro_div3.getText();
            if (info != null && !info.equals("")) {
                errInfo = info;
            }
        }
        if (loginNameInfo != null) {
            String info = loginNameInfo.getText();
            if (info != null && !info.equals("")) {
                errInfo = info;
            }
        }
        if (passwordInfo != null) {
            String info = passwordInfo.getText();
            if (info != null && !info.equals("")) {
                errInfo = info;
            }
        }
        if (imageCodeInfo != null) {
            String info = imageCodeInfo.getText();
            if (info != null && !info.equals("")) {
                errInfo = info;
            }
        }
        return errInfo;
    }

    //获取代理IP、端口（极光代理）
    public HttpProxyRes getProxyClient(String num, String pro, String useCache, int index){
        tracerLog.addTag("准备获取原始代理IP ，  index=", index+"'");
        if (index <= 0 ) {
            httpProxyRes = httpProxyClient.getProxy(num,pro,useCache);
            tracerLog.addTag("获取原始代理IP：httpProxyRes=", httpProxyRes + "");
        }
        if (httpProxyRes != null) {
            List<HttpProxyBean> httpProxyBeanSet = httpProxyRes.getHttpProxyBeanSet();
            HttpProxyBean httpProxyBean = httpProxyBeanSet.get(index);//根据重试次数获取不同的代理
            tracerLog.addTag("根据Index获取代理：httpProxyBean=", httpProxyBean + "");
            /*if (index <= 1) {
                httpProxyRes.setIp("10.167.202.1");
                httpProxyRes.setPort(httpProxyBean.getPort());
            } else {*/
                httpProxyRes.setIp(httpProxyBean.getIp());
                httpProxyRes.setPort(httpProxyBean.getPort());
//            }
        }
        System.out.println("httpProxyRes="+httpProxyRes);
        return httpProxyRes;
    }

}
