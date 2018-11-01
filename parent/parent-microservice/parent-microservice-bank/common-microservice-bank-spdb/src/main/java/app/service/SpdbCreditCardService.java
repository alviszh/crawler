package app.service;

import app.commontracerlog.TracerLog;
import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.spdb.*;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.spdb.*;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.VirtualKeyBoard;
import org.openqa.selenium.*;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic", "com.microservice.dao.entity.crawler.bank.spdb"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic", "com.microservice.dao.repository.crawler.bank.spdb"})
public class SpdbCreditCardService {
    @Autowired
    private TracerLog tracer;
    @Autowired
    private WebDriverIEService webDriverIEService;
    @Autowired
    private TaskBankStatusService taskBankStatusService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private SpdbCreditCardCrawlerService spdbCreditCardCrawlerService;



    private WebDriver driver = null;

    String loginUrl = "https://cardsonline.spdbccc.com.cn/icard/icardlogin.do?_locale=zh_CN";
    private static final String LEN_MIN = "0";
    private static final String TIME_ADD = "0";
    private static final String STR_DEBUG = "a";

    /**
     * 登录
     * @param bankJsonBean
     * @param taskBank
     * @throws Exception
     */
    @Async
    public void login(BankJsonBean bankJsonBean,TaskBank taskBank) throws Exception {
        tracer.addTag("taskid", taskBank.getTaskid());
        tracer.addTag("loginname", bankJsonBean.getLoginName());
        tracer.addTag("cardtype", taskBank.getCardType());//DEBIT_CARD、CREDIT_CARD
        tracer.addTag("ip", taskBank.getCrawlerHost());
        System.out.println("bankJsonBean============="+bankJsonBean);

        String path = null;
        WebDriver webDriver = getLoginSPDBHtml();

        String windowHandle = webDriver.getWindowHandle();
        bankJsonBean.setWebdriverHandle(windowHandle);
        tracer.addTag("浦发银行信用卡网银登录，打开网页获取网页handler", windowHandle);

        String loginType = bankJsonBean.getLoginType();
        tracer.addTag("开始选择登录类型", loginType);
        if (loginType != null) {
            String idTpyeValue = "";
            if (loginType.equals(StatusCodeLogin.IDNUM)) { //15/18位身份证
                idTpyeValue = "01";
            } else if (loginType.equals(StatusCodeLogin.PASSPORT)) { //护照
                idTpyeValue = "09";
            } else if (loginType.equals(StatusCodeLogin.OFFICER_CARD)) {//军官证/警官证
                idTpyeValue = "03";
            } else if (loginType.equals(StatusCodeLogin.NICK_NAME)) {//信用卡用户昵称
                idTpyeValue = "00";
            } else {//其他证件
                idTpyeValue = "06";
            }
            WebElement idTypeOption = driver.findElement(By.cssSelector("#IdType option[value='" + idTpyeValue + "']"));
            idTypeOption.click();
            Thread.sleep(500L);
        }

        tracer.addTag("开始输入登录账号", "taskId: " + bankJsonBean.getLoginName());
        Thread.sleep(500L);
        webDriver.findElement(By.name("IdNo")).sendKeys(bankJsonBean.getLoginName());

        Thread.sleep(500L);
        webDriver.findElement(By.id("OPassword")).sendKeys(Keys.DOWN);
        VirtualKeyBoard.KeyPressEx(bankJsonBean.getPassword(), 50);//
        Thread.sleep(500L);

        //====================开始识别图片验证码==========================
        long starttimeCode = System.currentTimeMillis();
        String captchaImgPath = WebDriverUnit.saveImg(webDriver, By.id("CaptchaImg"));
        System.out.println("captchaImgPath---------------" + captchaImgPath);
        String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1006", LEN_MIN, TIME_ADD, STR_DEBUG,
                captchaImgPath);
        System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
        Gson gson = new GsonBuilder().create();
        String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
        code = code.toLowerCase();
        System.out.println("code ====>>" + code);
        //====================识别图片验证码结束==========================
        long endtimeCode = System.currentTimeMillis();
        tracer.addTag("识别图片验证码所用时间：",(endtimeCode-starttimeCode)+":ms");
        //输入图片验证码
        tracer.addTag("SpdbCreditCardService.login","图片验证码url："+ captchaImgPath +"，识别结果："+ code);
        tracer.addTag("开始输入图片验证码",code);
        driver.findElement(By.name("Token")).sendKeys(code);

        //模拟tab按键
//        jNativeService.InputEnter();
        driver.findElement(By.id("button")).click();//提交

        //弹出错误框（只显示几秒钟）
        String errInfo = "";
        try {
            //错误信息
            Alert alert = driver.switchTo().alert();
            errInfo = alert.getText();
            System.out.println("错误信息alert:"+ errInfo);
            tracer.addTag("弹出错误框alert",errInfo);

        } catch (Exception e) {
            System.out.println("没有alert弹框");
        }

        //https://cardsonline.spdbccc.com.cn/icard/login.do //输入验证码页面
        //等待10秒，如果还没有登录成功的标识input#MobilePasswd(跳转到输入短信验证码页面) 则表示登录失败
        Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
        WebElement mobilePasswd = null;

        try {
            mobilePasswd = wait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(By.id("MobilePasswd"));
                }
            });

        } catch (Exception e) {
            path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
            String exStr = e.toString();
            tracer.addTag("creditCard.login.exception","登录异常");
            tracer.addTag("登录异常","#mobilePasswd 元素等待10秒未出现,异常:"+exStr + ",截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());
            System.out.println("#mobilePasswd 元素等待10秒未出现,异常:"+exStr+",截图:"+ path+" 当前页面URL:" + driver.getCurrentUrl());

            //登录失败，获取错误信息
            String key = "Modal dialog present:";
            if ("".equals(errInfo)) {
                if (exStr.contains(key)) {
                    if (exStr.contains("非常抱歉，目前我们无法完成您的请求")) {
                        errInfo = "账号或者密码不正确";
                    } else {
                        errInfo = exStr.substring(exStr.indexOf(key) + key.length(), exStr.indexOf("Build info")).trim();
                    }
                } else {
                    errInfo = "账号或者密码不正确";
                }
            } else {
                if (errInfo.contains("非常抱歉，目前我们无法完成您的请求")) {
                    errInfo = "账号或者密码不正确";
                }
            }
            tracer.addTag("creditCard.login.errInfo", "登录失败,错误信息："+ errInfo);
            System.out.println("登录失败，提示错误信息："+ errInfo+"###");

            if (errInfo.contains("验证码") || errInfo.contains("校验码")) {
                errInfo = "网络超时，请重试！";
            }

            taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
                    BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(),
                    errInfo,
                    BankStatusCode.BANK_LOGIN_ERROR.getError_code(),true,bankJsonBean.getTaskid(),windowHandle);
            //登录错误，释放资源
            //释放instance ip ，quit webdriver
            tracer.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
            agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
            return;
        }

        tracer.addTag("#mobilePasswd 元素的WebElement", mobilePasswd+"");
        //登录成功
        if (mobilePasswd!=null) {
            tracer.addTag("creditCard.login", "，欢迎来到浦发银行信用卡首页面,"+taskBank.getTaskid());
            System.out.println("登录成功,欢迎来到浦发银行信用卡首页面");
            Set<Cookie> cookies =  driver.manage().getCookies();
            WebClient webClient = WebCrawler.getInstance().getWebClient();//
            for(Cookie cookie:cookies){
                System.out.println(cookie.getName()+"---------------"+cookie.getValue());
                webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("cardsonline.spdbccc.com.cn",cookie.getName(),cookie.getValue()));
            }
            String cookieString = CommonUnit.transcookieToJson(webClient);  //存储cookie
            taskBank.setCookies(cookieString);

            taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(),
                    BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
                    BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(),
                    BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(),false,bankJsonBean.getTaskid(),windowHandle);

            String currentPageURL = webDriver.getCurrentUrl();
            //截图
            path = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
            tracer.addTag("登录成功", "截图:" + path+" 当前页面URL:" + currentPageURL);
//            tracer.addTag("登录成功", " 当前页面URL:" + currentPageURL);
            System.out.println("登录成功，当前页面url: " + currentPageURL);

        }

    }

    /**
     * 短信验证、爬取
     * @param bankJsonBean
     * @return
     * @throws Exception
     */
    @Async
    public TaskBank verfiySMSAndCrawler(BankJsonBean bankJsonBean) throws Exception {
        tracer.addTag("taskid", bankJsonBean.getTaskid());
        tracer.addTag("loginname", bankJsonBean.getLoginName());
        tracer.addTag("cardtype", bankJsonBean.getCardType());//DEBIT_CARD、CREDIT_CARD
        tracer.addTag("ip", bankJsonBean.getIp());
        System.out.println("bankJsonBean============="+bankJsonBean);
        TaskBank taskBank = null;
        String webdriverHandle = bankJsonBean.getWebdriverHandle();//获取登录步骤的webdriverHandle
        tracer.addTag("当前的 webdriverHandle：",driver.getWindowHandle());
        tracer.addTag("Task表中的 webdriverHandle：",webdriverHandle);
        //判断数据库中的WindowHandle和当前游览器的WindowHandle是否一致，此判断非常重要！
        //登录后，需要短信验证码或爬取，需要基于登录环节的seleunim WindowHandle 继续下去，如果WindowHandle不匹配。则无法连贯继续下去。例如登录在机器A，爬取在机器B，
        if(webdriverHandle==null||!webdriverHandle.equals(driver.getWindowHandle())){
            tracer.addTag("RuntimeException","当前的webdriverHandle 和 数据库中的 webdriverHandle 不匹配");
            taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WEBDRIVER_ERROR.getPhase(),
                    BankStatusCode.BANK_WEBDRIVER_ERROR.getPhasestatus(),
                    BankStatusCode.BANK_WEBDRIVER_ERROR.getDescription(),
                    BankStatusCode.BANK_WEBDRIVER_ERROR.getError_code(),false,bankJsonBean.getTaskid());
            return taskBank;
        } else {
            //获取cookies
            Set<Cookie> cookies =  driver.manage().getCookies();
            WebClient webClient = WebCrawler.getInstance().getWebClient();
            for(Cookie cookie:cookies){
                System.out.println(cookie.getName()+"---------------"+cookie.getValue());
                webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("cardsonline.spdbccc.com.cn",cookie.getName(),cookie.getValue()));
            }
            String cookieString = CommonUnit.transcookieToJson(webClient);  //存储cookie

            tracer.addTag("verfiySMS","开始验证短信验证码"+bankJsonBean.toString());
            //短信验证码的input 输入框
            WebElement mobilePasswd = null;
            try {
                mobilePasswd = driver.findElement(By.id("MobilePasswd"));
            } catch (NoSuchElementException e) {
                String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
                tracer.addTag("未找到验证码输入框#MobilePasswd，未知的错误", "截图:" + path+" 当前页面URL:" + driver.getCurrentUrl());
            }
            if(mobilePasswd==null){
                tracer.addTag("验证码输入框#MobilePasswd为空", "" + taskBank.getTaskid());
                taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
                        BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
                        BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
                        BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(),false,bankJsonBean.getTaskid());

                //释放instance ip ，quit webdriver
                tracer.addTag("释放instance ip ，quit webdriver:",taskBank.getCrawlerHost());
                agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
                return taskBank;
            }else{
                String pageSource = driver.getPageSource();
                System.out.println("======pageSource=" + pageSource);
                mobilePasswd.clear();
                mobilePasswd.sendKeys(bankJsonBean.getVerification());
                Thread.sleep(500L);
                driver.findElement(By.name("Button")).click();//提交
                Thread.sleep(1000L);

                //eg:  Modal dialog present: 设置专属于你的个性签名，一步到位安全到家
                String msgInfo = "";
                try {
                    //错误信息
                    Alert alert = driver.switchTo().alert();
                    msgInfo = alert.getText();
                    System.out.println("提示信息alert:"+ msgInfo);

                } catch (Exception e) {
                    System.out.println("没有alert弹框");
                }

                VirtualKeyBoard.KeyDown(13);
                Thread.sleep(2000L);

                //未弹框：验证码错误、跳转到登录成功的页面
                if ("".equals(msgInfo)) {
                    WebElement errorbox = null;
                    try {
                        errorbox = driver.findElement(By.id("errorbox"));
                    }catch (Exception e) {
                        String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
                        tracer.addTag("#errorbox未出现,异常:"+e.toString()+" ", "截图:" + path+" 当前页面URL:" + driver.getCurrentUrl());
                    }

                    if (errorbox != null) {
                        //短信验证失败（短信验证页面）
                        String errorfinfo = errorbox.getText();//errorfinfo
                        tracer.addTag("短信验证失败","错误消息：" + errorfinfo);
                        taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(),
                                BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(),
                                errorfinfo,
                                BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(),false,bankJsonBean.getTaskid());

                        //释放instance ip ，quit webdriver
                        tracer.addTag("释放instance ip ，quit webdriver:",taskBank.getCrawlerHost());
                        agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
                        return taskBank;
                    } else {
                        //进入没有提示框的主页面
                        System.out.println("进入没有提示框的主页面");
                        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(6, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
                        WebElement userInfoDiv = null;
                        try {
                            userInfoDiv = wait.until(new Function<WebDriver, WebElement>() {
                                public WebElement apply(WebDriver driver) {
                                    return driver.findElement(By.id("userInfo")); //这是不需要短信验证码，直接进入主页面的div的 ID
                                }
                            });
                        } catch (Exception e) {
                            //主页面#userInfo未出现    截图
                            String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
                            tracer.addTag("#userInfo 元素等待10秒未出现,异常:"+e.toString()+" ", "截图:" + path+" 当前页面URL:" + driver.getCurrentUrl());
                        }

                        if(userInfoDiv!=null){
                            String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
                            tracer.addTag("短信验证成功，#userInfo 元素不为空，可以直接采集数据", "截图:" + path+" 当前页面URL:" + driver.getCurrentUrl());
                            taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(),
                                    BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(),
                                    BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getDescription(),
                                    BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getError_code(),false,bankJsonBean.getTaskid());
                            return taskBank;
                        }else{
                            String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
                            tracer.addTag("短信验证失败，#userInfo为空，未知的错误", "截图:" + path+" 当前页面URL:" + driver.getCurrentUrl());
                            taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
                                    BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
                                    BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
                                    BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(),false,bankJsonBean.getTaskid());

                            //释放instance ip ，quit webdriver
                            tracer.addTag("释放instance ip ，quit webdriver:",taskBank.getCrawlerHost());
                            agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
                            return taskBank;
                        }
                    }
                } else {
//                    String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
//                    tracer.addTag("短信验证成功，弹出提示框", "提示："+msgInfo+"，截图:" + path+"，当前页面URL:" + driver.getCurrentUrl());
                    tracer.addTag("短信验证成功，弹出提示框", "提示："+msgInfo);
                    taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(),
                            BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(),
                            BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getDescription(),
                            BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getError_code(),false,bankJsonBean.getTaskid());

                    Thread.sleep(1000L);

                    //==========================开始爬取==================================
                    long starttimeCode = System.currentTimeMillis();
//                    getData(cookieString, taskBank);
                    tracer.addTag("开始准备爬取", taskBank.getTaskid());
                    //改为开始爬取状态
                    taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(),
                            BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(),
                            BankStatusCode.BANK_CRAWLER_DOING.getDescription(),
                            null, false, taskBank.getTaskid());
                   /* //获取账单分期记录
                    getInstallments(cookieString, taskBank);

                    //获取账户基本信息
                    getGeneralInfo(cookieString, taskBank);

                    //账单信息、近期历史对账单明细
                    getBillDetail(cookieString, taskBank);*/
                    getData(cookieString, taskBank);
                    //==========================结束爬取==================================
                    long endtimeCode = System.currentTimeMillis();
                    tracer.addTag("爬取所用总时间：",(endtimeCode-starttimeCode)+":ms");
                    //释放instance ip ，quit webdriver
                    tracer.addTag("释放instance ip ，quit webdriver:",taskBank.getCrawlerHost());
                    agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

                    /*//修改taskbank中的finish字段
                    taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());*/

                    //释放instance ip ，quit webdriver
                    tracer.addTag("释放instance ip ，quit webdriver:",taskBank.getCrawlerHost());
                    agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
                    return taskBank;
                }

            }

        }
    }

    public void getData(String cookieString, TaskBank taskBank) throws Exception{
        Map<String, Future<String>> listfuture = new HashMap<String, Future<String>>();   //判断异步爬取是否完成
        tracer.addTag("spdbCreditCardService.crawler.getData.start", taskBank.getTaskid());

        try {
            //获取账单分期记录
            Future<String> future = spdbCreditCardCrawlerService.getInstallments(cookieString, taskBank);
            listfuture.put("getInstallments", future);

        } catch (Exception e) {
            tracer.addTag("crawler.getInstallments.exception", "账单分期信息采集异常");
            tracer.addTag("crawler.getInstallments.e", "银行官网系统繁忙，账单分期信息暂不支持数据采集，请稍后再试,"
                    +taskBank.getTaskid() + "  " + e.toString());
//            taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());

            //释放instance ip ，quit webdriver
            /*tracer.addTag("释放instance ip ，quit webdriver:",taskBank.getCrawlerHost());
            agentService.releaseInstance(taskBank.getCrawlerHost(), driver);*/
        }

        try {
            //获取账户基本信息
            Future<String> future = spdbCreditCardCrawlerService.getGeneralInfo(cookieString, taskBank);
            listfuture.put("getGeneralInfo", future);

        } catch (Exception e) {
            tracer.addTag("crawler.getGeneralInfo.one.exception", "账户基本信息采集异常");
            tracer.addTag("crawler.getGeneralInfo.one..e", taskBank.getTaskid() + "  " + e.toString());
            taskBankStatusService.updateTaskBankUserinfo(404,"银行官网系统繁忙，账户基本信息暂不支持数据采集，请稍后再试",taskBank.getTaskid());
            /*taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());

            //释放instance ip ，quit webdriver
            tracer.addTag("释放instance ip ，quit webdriver:",taskBank.getCrawlerHost());
            agentService.releaseInstance(taskBank.getCrawlerHost(), driver);*/
        }

        //账单信息、近期历史对账单明细
        try {
            Future<String> future = spdbCreditCardCrawlerService.getBillDetail(cookieString, taskBank);
            listfuture.put("getBillDetail", future);

        } catch (Exception e) {
            tracer.addTag("getBillDetail.exception","账单明细采集异常");
            tracer.addTag("crawler.getBillDetail.e", "银行官网系统繁忙，账单明细暂不支持数据采集，请稍后再试,"
                    + taskBank.getTaskid() + "  " + e.toString());
            taskBankStatusService.updateTaskBankTransflow(404, "银行官网系统繁忙，流水信息暂不支持数据采集，请稍后再试", taskBank.getTaskid());

            /*//释放instance ip ，quit webdriver
            tracer.addTag("释放instance ip ，quit webdriver:", taskBank.getCrawlerHost());
            agentService.releaseInstance(taskBank.getCrawlerHost(), driver);*/
        }

        //最终状态的更新
        try {
            while (true) {
                for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
                    if (entry.getValue().isDone()) { // 判断是否执行完毕
//                        tracer.addTag(taskBank.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
//                        tracer.addTag(taskBank.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
                        listfuture.remove(entry.getKey());
                        break;
                    }
                }
                if (listfuture.size() == 0) {
                    break;
                }
            }
            taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
        } catch (Exception e) {
            e.printStackTrace();
            tracer.addTag("listfuture--ERROR", taskBank.getTaskid() + "---ERROR:" + e);
        }
    }


    /**
         * 打开浦发银行信用卡登录页面
         * @return
         */
    public WebDriver getLoginSPDBHtml(){
        driver = webDriverIEService.getNewWebDriver();
        driver.manage().window().maximize();
        System.out.println("WebDriverIEService loginSpdb Msg 开始登录浦发银行信用卡的登录页");
        tracer.addTag("WebDriverIEService loginSpdb Msg", "开始登录浦发银行信用卡的登录页");
        try{
            driver = webDriverIEService.getPage(driver,loginUrl);
        }catch(NoSuchWindowException e){
            System.out.println("打开浦发登录页面报错，尝试重新初始化游览器"+e.getMessage());
            tracer.addTag("打开浦发登录页面报错，尝试重新初始化游览器", e.getMessage());
            driver  = webDriverIEService.getPage(driver, loginUrl);

        }
        tracer.addTag("WebDriverIEService loginCmbChina Msg", "浦发银行信用卡登陆页加载已完成,当前页面句柄"+ driver.getWindowHandle());

        return driver;
    }

    /**
     * @Des 系统退出，释放资源
     * @param bankJsonBean
     */
    public TaskBank quit(BankJsonBean bankJsonBean){
        //关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试  , description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
        TaskBank taskBank = taskBankStatusService.systemClose(true,bankJsonBean.getTaskid());
        //调用公用释放资源方法
        agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
        return taskBank;
    }

}
