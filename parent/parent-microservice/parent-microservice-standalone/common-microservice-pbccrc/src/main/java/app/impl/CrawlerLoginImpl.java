package app.impl;

import app.client.proxy.HttpProxyClient;
import com.crawler.aws.json.HttpProxyRes;
import com.crawler.pbccrc.json.MessageResult;
import app.bean.PbcCreditReport;
import app.client.aws.AwsApiClient;
import app.commontracerlog.TracerLog;
import app.htmlparser.PbcCreditFeedParser;
import app.parser.PbccrcV2Parser;
import app.service.*;
import app.service.aop.ICrawlerLogin;
import com.crawler.domain.json.Result;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.pbccrc.json.*;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.pbccrc.PlainPbccrcJson;
import com.microservice.dao.repository.crawler.pbccrc.PlainPbccrcJsonRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.pbccrc"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.pbccrc"})
public class CrawlerLoginImpl implements ICrawlerLogin {
    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private WebDriverIEService webDriverIEService;
    @Autowired
    private PbcCreditFeedParser pbcCreditFeedParser;
    @Autowired
    private PbccrcV2Service pbccrcV2Service;
    @Autowired
    private PlainPbccrcJsonRepository plainPbccrcJsonRepository;
    @Autowired
    private AwsApiClient awsApiClient;
    @Autowired
    private HttpProxyClient httpProxyClient;
    @Autowired
    private AgentService agentService;
    @Autowired
    private PbccrcV2Parser pbccrcV2Parser;
    @Autowired
    private CrawlerStatusStandaloneService crawlerStatusStandaloneService;

    @Value("${isHttpProxy}")
    String isHttpProxy; //1使用代理ip

    private WebDriver driver = null;

    String loginUrl = "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp";
    private static final String LEN_MIN = "0";
    private static final String TIME_ADD = "0";
    private static final String STR_DEBUG = "a";

//    private HttpProxyBean httpProxyBean = null;
    private HttpProxyRes httpProxyRes = null;
    private static int size = 1;

    private static int proxySize = 0;  //获取代理Ip的标识（大于0 表示重新获取新的代理Ip）

    /**
     *
     * 登录
     * @param pbccrcJsonBean
    //     * @param version 版本（1：V1， 2：V2）
    //     * @param isFirst 是否是第一次打开页面（验证码错误重试直接重新输入）
     * @return
     * @throws Exception
     */
    @Override
    @Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
    public String login(PbccrcJsonBean pbccrcJsonBean){
        tracerLog.qryKeyValue("owner", pbccrcJsonBean.getOwner());
        tracerLog.qryKeyValue("username", pbccrcJsonBean.getUsername());//账号

        Gson gson = new GsonBuilder().create();
        Result<ReportData> reportResult = new Result<ReportData>();//V1结果
        Result<MessageResult> v2Result = new Result<MessageResult>();//V2结果
        String parserReportDataV2 = ""; //版本V2解析的结果
        String parserReportDataV1 = null;
        String path = "";
        System.out.println("isHttpProxy===="+isHttpProxy);
        tracerLog.addTag("isHttpProxy", isHttpProxy);

        long starttime = System.currentTimeMillis();
        if (pbccrcJsonBean.isFirst()) { //是否是第一次打开页面（验证码错误直接重新输入）
            size = 1; //初始化
            if (isHttpProxy.equals("1")) { //使用HTTP代理
                try {
                    proxySize ++; //标记获取代理iP次数
                    tracerLog.addTag("开始第"+ proxySize +"次获取代理IP、端口", "start");
                    if (proxySize <= 1 ) {
//                        httpProxyBean = getProxy();
                        httpProxyRes = getProxyClient("1", "");
                        System.out.println("httpProxyRes:" + httpProxyRes);
                        tracerLog.addTag("第"+ proxySize +"次获取代理IP、端口httpProxyRes", httpProxyRes + "");
                    } else {
                        httpProxyRes = getDelProxy("1", "");
                        System.out.println("httpProxyRes:" + httpProxyRes);
                        tracerLog.addTag("第"+ proxySize +"次获取代理IP、端口httpProxyRes", httpProxyRes + "");
                    }
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
            throw new RuntimeException(gson.toJson(reportResult));
//                return gson.toJson(reportResult);
        }

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
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(3, TimeUnit.SECONDS)
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
                tracerLog.addTag("CrawlerLoginImpl.login.Exception.e", e.toString());
                System.out.println("exception=" + e.toString());
            }
            long endtime = System.currentTimeMillis();
            tracerLog.addTag("登录完毕", (endtime - starttime) + ":ms");
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
                            String reportResultStr = login(pbccrcJsonBean);
//                        System.out.println("reportResultStr:"+reportResultStr);
                            return reportResultStr;
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
                    //保存状态
                    pbccrcV2Service.saveFlowStatus(pbccrcJsonBean.getMapping_id(), reportData.getMessage());

                    //释放instance ip ，quit webdriver
                    tracerLog.addTag("释放instance ip ，quit webdriver:", pbccrcJsonBean.getIp());
                    agentService.releaseInstance(pbccrcJsonBean.getIp(), driver);
                    System.out.println("登录失败：" + reportResult);
                    return gson.toJson(reportResult);
                } else {
                    //登录超时，mainFrame、errInfo都为null
                    tracerLog.qryKeyValue("登录超时", "mainFrame、errInfo都为null");
                    ReportData reportData = new ReportData("-1", "查询失败,请重试", null, null);
                    reportResult.setData(reportData);

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
                    return gson.toJson(reportResult);
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
                return gson.toJson(reportResult);
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

            return gson.toJson(reportResult);
        }
    }

    /**
     * 获取报告数据
     * @param pbccrcJsonBean
     * @return
     */
    @Override
    public String getAllData(PbccrcJsonBean pbccrcJsonBean){
        int version = pbccrcJsonBean.getVersion();
        Gson gson = new GsonBuilder().create();
        Result<ReportData> reportResult = new Result<ReportData>();//V1结果
        Result<MessageResult> v2Result = new Result<MessageResult>();//V2结果
        String parserReportDataV2 = ""; //版本V2解析的结果
        String parserReportDataV1 = null;

        //保存json入库
        PlainPbccrcJson jsonResult = new PlainPbccrcJson();
        jsonResult.setMappingId(pbccrcJsonBean.getMapping_id());
        jsonResult.setOwner(pbccrcJsonBean.getOwner()); //保存owner值

        //根据授权码获取报告（简单解析结果）
        long starttime1 = System.currentTimeMillis();
        try {
            reportResult = getCeditReport(pbccrcJsonBean, jsonResult);
            if (reportResult.getData() == null || !"0".equals(reportResult.getData().getStatusCode())) {
                //释放instance ip ，quit webdriver
                tracerLog.addTag("释放instance ip ，quit webdriver:", pbccrcJsonBean.getIp());
                agentService.releaseInstance(pbccrcJsonBean.getIp(), driver);
                System.out.println("报告获取失败："+gson.toJson(reportResult));
                return gson.toJson(reportResult);
            }
            long endtime1 = System.currentTimeMillis();
            tracerLog.addTag("获取原始报告完毕",(endtime1-starttime1)+":ms");

            String pbcCreditFeedStr = gson.toJson(reportResult);
            jsonResult.setOriginal_json(pbcCreditFeedStr);//保存原始json
            String html = reportResult.getHtml();
            reportResult.setHtml(null);//tracer中不打印html源码日志
            tracerLog.addTag("原始json", gson.toJson(reportResult));
            reportResult.setHtml(html);

            PbcCreditReportFeed reportFeed = reportResult.getData().getReport();
            if (reportFeed != null) {
                ReportBase reportBase = reportFeed.getReportBase();
                if (reportBase != null) {
                    String reportId =reportBase.getReportId();//报告编号
                    String realName =reportBase .getRealname();//姓名
                    String certificateNum =reportBase.getCertificateNum();//身份证号码
                    tracerLog.qryKeyValue("reportId",reportId);
                    tracerLog.qryKeyValue("realName",realName);
                    tracerLog.qryKeyValue("certificateNum",certificateNum);
                }

                tracerLog.addTag("CrawlerLoginImpl.getReportResult.version", "版本：V"+version);

                //=====================解析原始json、入库（返回V2版本json）=======================
                long starttime2 = System.currentTimeMillis();
                PbcCreditReport pbcCreditReport = pbccrcV2Service.getParserReportData(reportResult.getData(), jsonResult);
                ObjectMapper mapper = new ObjectMapper();
                parserReportDataV2 =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pbcCreditReport);
                long endtime2 = System.currentTimeMillis();
                tracerLog.addTag("解析原始json、入库完毕",(endtime2-starttime2)+":ms");
                //=====================解析原始json、入库（返回V2版本json）=======================

                //chp3
                //=====================开始详细解析（返回V1版本json）=======================
                long starttime3 = System.currentTimeMillis();
                //                PbcCreditReportFeed reportFeed = reportResult.getData().getReport();
                //解析信用卡
                reportFeed = pbcCreditFeedParser.parseCreditCardDetails(reportFeed);
                //解析贷款明细（住房贷款 和 其他贷款）
                reportFeed = pbcCreditFeedParser.parseLoanDetails(reportFeed);
                //解析担保信息明细
                reportFeed = pbcCreditFeedParser.parseGuaranteeInfoDetails(reportFeed);
                //解析保证人代偿信息
                reportFeed = pbcCreditFeedParser.parseGuarantorCompensatoryinfoDetails(reportFeed);
                reportResult.getData().setReport(reportFeed);
                parserReportDataV1 = gson.toJson(reportResult);
                long endtime3 = System.currentTimeMillis();
                tracerLog.addTag("详细解析原始json（V1）完毕",(endtime3-starttime3)+":ms");
                //=====================结束详细解析（返回V1版本json）=======================

                tracerLog.addTag("准备入库解析后的json数据", "版本：V" + version);
                jsonResult.setJson_v1(parserReportDataV1);
                jsonResult.setJson_v2(parserReportDataV2);

                /*tracerLog.addTag("准备解析成【借么】的JSON格式","mappingId=" + pbccrcJsonBean.getMapping_id());
                String jiemo_json = pbccrcReportParser.getReportJiemoParser(pbcCreditReport);
                jsonResult.setJson_jiemo(jiemo_json);
                //                tracerLog.addTag("【借么】的JSON格式",jiemo_json);
                tracerLog.addTag("【借么】的JSON格式数据", "存入库中");*/

                tracerLog.qryKeyValue("开始入库解析后的json数据", "表名：plain_pbccrc_json");
                plainPbccrcJsonRepository.save(jsonResult);

                //采集、入库完成
                //释放instance ip ，quit webdriver
                tracerLog.addTag("释放instance ip ，quit webdriver:", pbccrcJsonBean.getIp());
                agentService.releaseInstance(pbccrcJsonBean.getIp(), driver);

                if (version == 1) { //chp3（V1）
                    //打印最后json（不带html）
                    reportResult.setHtml("");
                    tracerLog.addTag("getCreditV1.result===", gson.toJson(reportResult));
                    System.out.println("Message："+reportResult.getData().getMessage() + "，StatusCode："+ reportResult.getData().getStatusCode());
                    return parserReportDataV1;
                } else {
                    MessageResult messageResult = new MessageResult("0", "查询成功", parserReportDataV2);
                    v2Result.setData(messageResult);
                    crawlerStatusStandaloneService.updateTaskFinished(pbccrcJsonBean.getMapping_id());
                    tracerLog.addTag("getCreditV2.result===", gson.toJson(v2Result));
                    return gson.toJson(v2Result);//返回V2版本的格式
                }
            }
        } catch (Exception e) {
            tracerLog.qryKeyValue("CrawlerLoginImpl.getAllData.exception", "查询失败");
            reportResult = new Result<ReportData>();
            ReportData reportData = new ReportData("-1", "查询失败,请重试", null, null);
            reportResult.setData(reportData);
            tracerLog.addTag("CrawlerLoginImpl.getAllData.exception.e", e.toString());
            System.out.println("CrawlerLoginImpl.getAllData.exception="+ e.toString());
            //保存状态
            pbccrcV2Service.saveFlowStatus(pbccrcJsonBean.getMapping_id(), reportData.getMessage());
            //发送状态
//            sendMessageResult(pbccrcJsonBean, "5", "系统繁忙");
            crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_CRAWLER_ERROR.getPhase(),
                    StandaloneEnum.STANDALONE_CRAWLER_ERROR.getPhasestatus(),
                    StandaloneEnum.STANDALONE_CRAWLER_ERROR.getDescription(),
                    StandaloneEnum.STANDALONE_CRAWLER_ERROR.getCode(),
                    true, pbccrcJsonBean.getMapping_id());

            //释放instance ip ，quit webdriver
            tracerLog.addTag("释放instance ip ，quit webdriver:", pbccrcJsonBean.getIp());
            agentService.releaseInstance(pbccrcJsonBean.getIp(), driver);

            return gson.toJson(reportResult);
        }
        return "";
    }

    /**
     * 获取报告
     * @param pbccrcJsonBean
     * @param plainPbccrcJson
     * @return
     * @throws IOException
     */
    public Result<ReportData> getCeditReport(PbccrcJsonBean pbccrcJsonBean, PlainPbccrcJson plainPbccrcJson) throws IOException {
        tracerLog.qryKeyValue("tradeCode",pbccrcJsonBean.getTradecode());
        Result<ReportData> result = new Result<ReportData>();
        WebClient webClient = getWebClient(pbccrcJsonBean.getCookieStr());
        webClient.getOptions().setJavaScriptEnabled(false);

        //获取信用信息（授权码是否过期）
        WebRequest queryReportRequest = new WebRequest(new URL("https://ipcrs.pbccrc.org.cn/reportAction.do?method=queryReport"), HttpMethod.GET);
        if (httpProxyRes != null) {
            //代理ip，端口
            queryReportRequest.setProxyHost(httpProxyRes.getIp());
            queryReportRequest.setProxyPort(Integer.parseInt(httpProxyRes.getPort()));
        }
        Page queryReportResult = webClient.getPage(queryReportRequest);
        String queryReportHtml = queryReportResult.getWebResponse().getContentAsString();
//        System.out.println("queryReportHtml:"+queryReportHtml);
        boolean isOverdue = pbccrcV2Parser.isOverdueTradeCodeParser(queryReportHtml);
        tracerLog.qryKeyValue("授权码是否已过期", ""+isOverdue);
        System.out.println("isOverdue====" + isOverdue);
        if (isOverdue) {
            ReportData reportData = new ReportData("-1", "授权码已过期", null, null);
            result.setData(reportData);
            //保存状态
            pbccrcV2Service.saveFlowStatus(pbccrcJsonBean.getMapping_id(), reportData.getMessage());
            //发送状态
//            sendMessageResult(pbccrcJsonBean, "2", "授权码已过期");
            crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_TRADECODE_ERROR1.getPhase(),
                    StandaloneEnum.STANDALONE_TRADECODE_ERROR1.getPhasestatus(),
                    StandaloneEnum.STANDALONE_TRADECODE_ERROR1.getDescription(),
                    StandaloneEnum.STANDALONE_TRADECODE_ERROR1.getCode(),
                    false, pbccrcJsonBean.getMapping_id());
            return result;
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
        nameValuePairs.add(new NameValuePair("counttime", ""));
        nameValuePairs.add(new NameValuePair("reportformat", "21"));
        nameValuePairs.add(new NameValuePair("tradeCode", pbccrcJsonBean.getTradecode())); //身份验证码
        request.setRequestParameters(nameValuePairs);
        if (httpProxyRes != null) {
            //代理ip，端口
            request.setProxyHost(httpProxyRes.getIp());
            request.setProxyPort(Integer.parseInt(httpProxyRes.getPort()));
        }
        Page page = webClient.getPage(request);
        int statusCode = page.getWebResponse().getStatusCode();
        tracerLog.addTag("statusCode", statusCode+"");
        ReportData reportData = null;
        if (statusCode==200) {
            String html = page.getWebResponse().getContentAsString();
            Document htmlDoc = Jsoup.parse(html);
            String divHtml = htmlDoc.select("div[align=center]").toString();
            //保存源码
            tracerLog.addTag("html源码", divHtml);
            plainPbccrcJson.setHtml(html);
            if (!StringUtils.isEmpty(html)) {
                //获取授权码错误信息
                Document doc = Jsoup.parse(html);
                Elements selects = doc.select("span[class=erro_div1]");
                if (selects != null && selects.size() > 0) {
                    String errInfo = selects.get(0).text();
                    System.out.println(errInfo);
                    reportData = new ReportData("1", errInfo, null, null);
                    result.setData(reportData);
                    //发送状态
//                    sendMessageResult(pbccrcJsonBean, "3", errInfo);
                    crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_TRADECODE_ERROR2.getPhase(),
                            StandaloneEnum.STANDALONE_TRADECODE_ERROR2.getPhasestatus(),
                            errInfo,
                            StandaloneEnum.STANDALONE_TRADECODE_ERROR2.getCode(),
                            true, pbccrcJsonBean.getMapping_id());
                } else {
                    if (pbccrcJsonBean.isHtml()) {//判断是否需要源码
                        result.setHtml(html);
                    }
                    PbcCreditReportFeed pbcCreditReportFeed = pbcCreditFeedParser.getPbcCreditFeed(html);
                    reportData = new ReportData("0", "查询成功", pbcCreditReportFeed, null);
//                reportData.setLoggedCookiesJson(cookiesJson);
                    result.setData(reportData);
                    //发送状态
//                    sendMessageResult(pbccrcJsonBean, "1", "爬取成功");
                    crawlerStatusStandaloneService.updateTaskFinished(pbccrcJsonBean.getMapping_id());
                }
            } else {
                tracerLog.addTag("","");
                reportData = new ReportData("3", "系统繁忙,请重试", null, null);
                result.setData(reportData);
                //发送状态
//                sendMessageResult(pbccrcJsonBean, "5", "系统繁忙,请重试");
                crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_CRAWLER_ERROR2.getPhase(),
                        StandaloneEnum.STANDALONE_CRAWLER_ERROR2.getPhasestatus(),
                        StandaloneEnum.STANDALONE_CRAWLER_ERROR2.getDescription(),
                        StandaloneEnum.STANDALONE_CRAWLER_ERROR2.getCode(),
                        true, pbccrcJsonBean.getMapping_id());
            }
        } else {
            reportData = new ReportData("3", "系统繁忙,请重试", null, null);
            result.setData(reportData);
            //发送状态
//            sendMessageResult(pbccrcJsonBean, "5", "系统繁忙,请重试");
            crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_CRAWLER_ERROR2.getPhase(),
                    StandaloneEnum.STANDALONE_CRAWLER_ERROR2.getPhasestatus(),
                    StandaloneEnum.STANDALONE_CRAWLER_ERROR2.getDescription(),
                    StandaloneEnum.STANDALONE_CRAWLER_ERROR2.getCode(),
                    true, pbccrcJsonBean.getMapping_id());
        }
        //保存状态
        pbccrcV2Service.saveFlowStatus(pbccrcJsonBean.getMapping_id(), reportData.getMessage());
        return result;
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


    public static WebClient getWebClient(String cookiesJson) {
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        Set<com.gargoylesoftware.htmlunit.util.Cookie> cookies = CommonUnit.transferJsonToSet(cookiesJson);
        Iterator<com.gargoylesoftware.htmlunit.util.Cookie> i = cookies.iterator();
        while (i.hasNext()) {
            webClient.getCookieManager().addCookie(i.next());
        }
        return webClient;
    }

    //获取代理IP、端口
    /*public HttpProxyBean getProxy(){
        httpProxyBean = awsApiClient.getProxy();
        return httpProxyBean;
    }*/

    //获取代理IP、端口（极光代理）
    public HttpProxyRes getProxyClient(String num, String pro){
        httpProxyRes = httpProxyClient.getProxy(num,pro);
        return httpProxyRes;
    }

    //删除缓存代理IP并获取新的代理IP、端口
    public HttpProxyRes getDelProxy(String num, String pro){
        httpProxyRes = httpProxyClient.getDelProxy(num,pro);
        return httpProxyRes;
    }

}
