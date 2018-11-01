package app.Impl;

import app.commontracerlog.TracerLog;
import app.service.*;
import app.service.aop.ICrawlerLogin;
import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic", "com.microservice.dao.entity.crawler.bank.spdb"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic", "com.microservice.dao.repository.crawler.bank.spdb"})
public class SpdbDebitCardImpl implements ICrawlerLogin{

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private WebDriverIEService webDriverIEService;
    @Autowired
    private JNativeService jNativeService;
    @Autowired
    private TaskBankStatusService taskBankStatusService;
    @Autowired
    private SpdbDebitCardCrawlerService spdbDebitCardCrawlerService;
    @Autowired
    private TaskBankRepository taskBankRepository;
    @Autowired
    private AgentService agentService;

    private WebDriver driver = null;
    private String path = null;

    //    static String loginUrl = "https://ebank.spdb.com.cn/nbper/prelogin.do";
    String loginUrl = "https://ebank.spdb.com.cn/nbper/popInnerLogin.do?Reserve=";

    @Override
    public TaskBank login(BankJsonBean bankJsonBean){
        tracerLog.qryKeyValue("taskid", bankJsonBean.getTaskid());
        tracerLog.qryKeyValue("loginname", bankJsonBean.getLoginName());
        tracerLog.qryKeyValue("cardtype", bankJsonBean.getCardType());//DEBIT_CARD、CREDIT_CARD

        TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
        try {
            tracerLog.qryKeyValue("ip", taskBank.getCrawlerHost());
            System.out.println("bankJsonBean=============" + bankJsonBean);

            driver = getLoginSPDBHtml();

            String windowHandle = driver.getWindowHandle();
            bankJsonBean.setWebdriverHandle(windowHandle);
            tracerLog.output("登录浦发银行，打开网页获取网页handler", windowHandle);

            tracerLog.qryKeyValue("开始输入登录账号", "" + bankJsonBean.getLoginName());

            //键盘输入账号
            driver.findElement(By.id("LoginId")).sendKeys(bankJsonBean.getLoginName());

            Thread.sleep(1000);
            tracerLog.qryKeyValue("开始输入登录密码", "taskId: " + bankJsonBean.getTaskid());
            driver.findElement(By.id("OPassword")).sendKeys(bankJsonBean.getPassword());
            //输入密码
//        jNativeService.Input(bankJsonBean.getPassword());
            VK.KeyPress(bankJsonBean.getPassword());

            Thread.sleep(1000);
            //登录
            driver.findElement(By.id("LoginButton")).click();

            Boolean isLogin = false;
            try {
                //等待10秒(每2秒轮训检查一遍)，如果还没有跳转到指定的url，则表示登录失败
                WebDriverWait wait = new WebDriverWait(driver, 15);
//            isLogin=wait.until(ExpectedConditions.urlToBe("https://ebank.spdb.com.cn/nbper/index.do#"));
                isLogin = wait.until(ExpectedConditions.urlContains("https://ebank.spdb.com.cn/nbper/index.do"));
                System.out.println("isLogin=" + isLogin);

            } catch (Exception e) {
                tracerLog.qryKeyValue("SpdbDebitCardImpl.login.Exception", "登录异常");
                tracerLog.output("SpdbDebitCardImpl.login.Exception.e", e.toString());
                String currentUrl = driver.getCurrentUrl();
                String pageSource = driver.getPageSource();
                System.out.println("登录异常，当前页面url: " + currentUrl);
                tracerLog.output("登录错误源码页", pageSource);

                path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
                tracerLog.output("登录异常", "截图:" + path + " 当前页面URL:" + currentUrl);
                System.out.println("获取登录页面异常：" + e.toString() + "，Taskid===" + taskBank.getTaskid());

                //https://ebank.spdb.com.cn/nbper/NoLoginPageDisplay.do?_viewReferer=topdefault%2CuserActivate%2FUserActivateInput
                if (pageSource.contains("本人已阅知并同意服务协议")) {
                    //模拟点击“本人已阅知并同意服务协议”
                    driver.findElement(By.id("agreementSubmit")).click();//提交
                    isLogin = true;
                } else if (currentUrl.contains("https://ebank.spdb.com.cn/nbper/NoLoginPageDisplay.do?")) { //短信验证码页面
                    isLogin = true;
                } else {
                    //登录错误信息
                    String errInfo = "";
                    try {
                        errInfo = driver.findElement(By.id("errInfo")).getText();
                        if (errInfo == null || "".equals(errInfo)) {
                            if (e.toString().contains("密码长度错误")) {
                                errInfo = "密码长度错误";
                            } else {
                                errInfo = "账号或者密码错误！";//锁屏的情况
                            }
                        }
                        System.out.println("登录失败,错误信息" + errInfo + "，" + taskBank.getTaskid());
                        tracerLog.output("SpdbDebitCardImpl.login.errInfo", "taskid=" + taskBank.getTaskid() + "，登录失败,错误信息：" + errInfo);

                    } catch (Exception ex) {
                        errInfo = "登录异常";
                        tracerLog.output("SpdbDebitCardImpl.login.errInfo.ex", "没有发现错误消息errInfo，taskid=" + taskBank.getTaskid() + ex.getMessage());
                    }

                    taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
                            BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(),
                            errInfo,
                            BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(), windowHandle);
                    //登录错误，释放资源
                    //释放instance ip ，quit webdriver
                    tracerLog.output("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
                    agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
                    return taskBank;
                }
            }

            System.out.println("======是否登录成功，isLogin=" + isLogin);
            tracerLog.output("是否登录成功", "isLogin=" + isLogin);
            //登录成功
            if (isLogin) {
                tracerLog.output("loginspdb", "登录成功，欢迎来到浦发银行首页面," + taskBank.getTaskid());
                System.out.println("登录成功,欢迎来到浦发银行首页面");
                Set<Cookie> cookies = driver.manage().getCookies();
                WebClient webClient = WebCrawler.getInstance().getWebClient();//
                for (Cookie cookie : cookies) {
                    System.out.println(cookie.getName() + "---------------" + cookie.getValue());
                    webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("ebank.spdb.com.cn", cookie.getName(), cookie.getValue()));
                }
            /*String cookieString = CommonUnit.transcookieToJson(webClient);  //存储cookie
            taskBank.setCookies(cookieString);*/

                taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
                        BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
                        BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
                        BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid(), windowHandle);

                String currentPageURL = driver.getCurrentUrl();
                //截图
                path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
                tracerLog.output("登录成功", "截图:" + path + " 当前页面URL:" + currentPageURL);
//            tracerLog.output("登录成功", " 当前页面URL:" + currentPageURL);
                System.out.println("登录成功，当前页面url: " + currentPageURL);

                Thread.sleep(2000);

            }
        } catch (Exception e) {
            tracerLog.qryKeyValue("SpdbDebitCardImpl.login.exception", "-1");
            tracerLog.output("SpdbDebitCardImpl.login.exception.e", e.toString());
            System.out.println("SpdbDebitCardImpl.login.exception="+ e.toString());
            taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
                    BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(),
                    BankStatusCode.BANK_LOGIN_ERROR.getDescription(),
                    BankStatusCode.BANK_LOGIN_ERROR.getError_code(),true,bankJsonBean.getTaskid());

            //释放instance ip ，quit webdriver
            tracerLog.qryKeyValue("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
            agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
        }
        return taskBank;
    }


    @Override
    public TaskBank getAllData(BankJsonBean bankJsonBean) {
        tracerLog.output("登录成功，开始准备爬取", "" + bankJsonBean.getTaskid());
        tracerLog.qryKeyValue("taskid", bankJsonBean.getTaskid());
        TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());

        WebClient webClient = WebCrawler.getInstance().getWebClient();//
        try {
            taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(),
                    BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(),
                    BankStatusCode.BANK_CRAWLER_DOING.getDescription(),
                    null, false, bankJsonBean.getTaskid());
            try {
                spdbDebitCardCrawlerService.getUserInfo(webClient, taskBank);//采集用户信息
//                spdbDebitCardCrawlerService.getTransFlow(webClient, taskBank);//采集流水信息、定期存款信息
                spdbDebitCardCrawlerService.getTransFlowXLS(webClient, taskBank);//采集流水信息、定期存款信息


                //爬取完成
                taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());

                //释放instance ip ，quit webdriver
                tracerLog.qryKeyValue("释放instance ip ，quit webdriver:", taskBank.getCrawlerHost());
                agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
            } catch (Exception e) {
                tracerLog.output("action.crawler.getUserInfo.e", taskBank.getTaskid() + "  " + e.toString());
                taskBankStatusService.updateTaskBankUserinfo(404, "银行官网系统繁忙，用户基本信息暂不支持数据采集，请稍后再试", taskBank.getTaskid());
                taskBankStatusService.updateTaskBankTransflow(404, "银行官网系统繁忙，流水信息暂不支持数据采集，请稍后再试", taskBank.getTaskid());
                taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());

                //截图
                path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
                tracerLog.output("采集异常", "截图:" + path );

                //释放instance ip ，quit webdriver
                tracerLog.qryKeyValue("释放instance ip ，quit webdriver:", taskBank.getCrawlerHost());
                agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
            }
        } catch (Exception e) {
            tracerLog.qryKeyValue("SpdbDebitCardImpl.getAllData.exception", "-1");
            tracerLog.output("SpdbDebitCardImpl.getAllData.exception.e", e.toString());
            System.out.println("SpdbDebitCardImpl.getAllData.exception="+ e.toString());
            taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_TIMEOUT_ERROR.getPhase(),
                    BankStatusCode.BANK_CRAWLER_TIMEOUT_ERROR.getPhasestatus(),
                    BankStatusCode.BANK_CRAWLER_TIMEOUT_ERROR.getDescription(),
                    BankStatusCode.BANK_CRAWLER_TIMEOUT_ERROR.getError_code(),true,bankJsonBean.getTaskid());

            //释放instance ip ，quit webdriver
            tracerLog.qryKeyValue("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
            agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
        }

        return null;
    }

    /**
     * 打开浦发银行登录页面
     * @return
     */
    public WebDriver getLoginSPDBHtml(){
        driver = webDriverIEService.getNewWebDriver();
        driver.manage().window().maximize();
        System.out.println("WebDriverIEService loginSpdb Msg 开始登录浦发银行的登录页");
        tracerLog.qryKeyValue("WebDriverIEService loginSpdb Msg", "开始登录浦发银行的登录页");
        try{
            driver = webDriverIEService.getPage(driver,loginUrl);
        }catch(NoSuchWindowException e){
            System.out.println("打开浦发登录页面报错，尝试重新初始化游览器"+e.getMessage());
            tracerLog.output("打开浦发登录页面报错，尝试重新初始化游览器", e.getMessage());
            driver  = webDriverIEService.getPage(driver, loginUrl);

        }
        tracerLog.output("WebDriverIEService loginCmbChina Msg", "浦发银行登陆页加载已完成,当前页面句柄"+ driver.getWindowHandle());

        return driver;
    }


    @Override
    public TaskBank getAllDataDone(String taskId) {
        return null;
    }
}
