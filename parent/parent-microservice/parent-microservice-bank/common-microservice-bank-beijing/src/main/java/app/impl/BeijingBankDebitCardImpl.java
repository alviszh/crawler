package app.impl;

import app.commontracerlog.TracerLog;
import app.parser.BeijingBankDebitCardParser;
import app.service.*;
import app.service.aop.ICrawlerLogin;
import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic", "com.microservice.dao.entity.crawler.bank.beijingbank"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic", "com.microservice.dao.repository.crawler.bank.beijingbank"})
public class BeijingBankDebitCardImpl  implements ICrawlerLogin {

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private WebDriverIEService webDriverIEService;
    @Autowired
    private TaskBankStatusService taskBankStatusService;
    @Autowired
    private TaskBankRepository taskBankRepository;
    @Autowired
    private AgentService agentService;
    @Autowired
    private BeijingBankDebitCardCrawlerService beijingBankDebitCardCrawlerService;
    @Autowired
    private BeijingBankDebitCardLoginService beijingBankDebitCardLoginService;

    private WebDriver driver = null;

    String loginUrl = "https://ebank.bankofbeijing.com.cn/bccbpb/accountLogon.jsp";
    String page = "";

    @Override
    public TaskBank login(BankJsonBean bankJsonBean){
        tracerLog.addTag("taskid", bankJsonBean.getTaskid());
        tracerLog.addTag("loginname", bankJsonBean.getLoginName());
        tracerLog.addTag("cardtype", bankJsonBean.getCardType());//DEBIT_CARD、CREDIT_CARD
        tracerLog.addTag("ip", bankJsonBean.getIp());
        System.out.println("bankJsonBean=============" + bankJsonBean);
        TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());

        String path = "";
        try {
            driver = beijingBankDebitCardLoginService.retryLogin(bankJsonBean,taskBank);
            System.out.println("driver===" + driver);
            System.out.println("driver.getWindowHandle===" + driver.getWindowHandle());
            if(null != driver){
                //截图
                try {
                    path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tracerLog.addTag("登录后页面截图：", path);
            }
        } catch (Exception e) {
            System.out.println("======BeijingBankDebitCardService.login.exception=====" + e.getMessage());
            tracerLog.addTag("BeijingBankDebitCardService.login.exception", "error");
            tracerLog.addTag("BeijingBankDebitCardService.login.exception.e", e.getMessage());
            taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(),
                    BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(),
                    BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
                    BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(),false,bankJsonBean.getTaskid());
            if(null != driver){
                //截图
                try {
                    path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                tracerLog.addTag("beijingBankDebitCardLoginService.retryLogin.Exception","登录失败后页面截图："+ path);
            }
            //释放机器，关闭driver进程
            agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
            return taskBank;
        }

        System.out.println("====description======" + taskBank.getDescription());
        String currentPageURL = driver.getCurrentUrl();
        page = driver.getPageSource();
        if (!page.contains("网银小贴示") && !page.contains("欢迎页")) {
            tracerLog.addTag("登录失败页面",page);
            if (page.contains("登录失败") || page.contains("失败原因")) {
                //登录错误，释放资源
                //释放instance ip ，quit webdriver
                tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
                agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
            } else {
                System.out.println("=========获取错误信息失败");
                tracerLog.addTag("获取错误信息失败", "taskid" + taskBank.getTaskid());
                taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(),
                        BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(),
                        BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
                        BankStatusCode.BANK_LOGIN_ERROR.getError_code(),false, bankJsonBean.getTaskid());
                //释放机器，关闭driver进程
                agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
            }
        } else {
            tracerLog.addTag("loginspdb", "登录成功，欢迎来到北京银行首页面," + taskBank.getTaskid());
            tracerLog.addTag("登录成功", "截图:" + path + " 当前页面URL:" + currentPageURL + "taskid：" + taskBank.getTaskid());
            tracerLog.addTag("登录成功页面，html", page);

            //登录成功,存储cookie
            Set<Cookie> cookies = driver.manage().getCookies();
            WebClient webClient = WebCrawler.getInstance().getWebClient();
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + "---------------" + cookie.getValue());
                webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("ebank.bankofbeijing.com.cn", cookie.getName(), cookie.getValue()));
            }
            String cookieString = CommonUnit.transcookieToJson(webClient);
            taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
                    BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
                    BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
                    BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid(),cookieString);
        }
        return taskBank;
    }

    /**
     * 爬取数据
     * @param bankJsonBean
     */
    @Override
    public TaskBank getAllData(BankJsonBean bankJsonBean) {
        tracerLog.addTag("BeijingBankDebitCardService.crawler", "开始准备爬取");
        TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
        tracerLog.addTag("taskid", bankJsonBean.getTaskid());
        taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(),
                BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(),
                BankStatusCode.BANK_CRAWLER_DOING.getDescription(),
                null, false, bankJsonBean.getTaskid());
        try {
            //获取请求参数
            Document doc = Jsoup.parse(page);
            Elements inputs = doc.select("input[name=dse_sessionId]");
            if (inputs != null && inputs.size() > 0) {
                String sessionId = doc.select("input[name=dse_sessionId]").first().val();
                System.out.println("sessionId====" + sessionId);

                beijingBankDebitCardCrawlerService.getAccountInfo(taskBank, sessionId, bankJsonBean.getLoginName());//采集账户信息

                WebClient webClient = WebCrawler.getInstance().getWebClient();
                beijingBankDebitCardCrawlerService.getTransflowXls(webClient, taskBank, sessionId);//采集交易明细
            } else {
                System.out.println("未获取到name为dse_sessionId的input框");
                tracerLog.addTag("获取请求参数失败", "未获取到name为dse_sessionId的input框");
            }

            //爬取完成
            taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());

            //释放instance ip ，quit webdriver
            tracerLog.addTag("释放instance ip ，quit webdriver:", taskBank.getCrawlerHost());
            agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
        } catch (Exception e) {
            tracerLog.addTag("crawler.getAccountInfo.exception", "用户信息、流水信息爬取失败");
            tracerLog.addTag("crawler.getAccountInfo.e", taskBank.getTaskid() + ", " + e.getMessage());
            taskBankStatusService.updateTaskBankUserinfo(404, "银行官网系统繁忙，用户基本信息暂不支持数据采集，请稍后再试", taskBank.getTaskid());
            taskBankStatusService.updateTaskBankTransflow(404, "银行官网系统繁忙，流水信息暂不支持数据采集，请稍后再试", taskBank.getTaskid());
            taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());

            //截图
            String path = "";
            try {
                path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
            } catch (Exception e1) {
                tracerLog.addTag("爬取异常中，截图出错", e.getMessage());
            }
            tracerLog.addTag("采集异常", "截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());

            //释放instance ip ，quit webdriver
            tracerLog.addTag("释放instance ip ，quit webdriver:", taskBank.getCrawlerHost());
            agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
        }
        return taskBank;
    }


    /**
     * 打开北京银行登录页面
     * @return
     */
    public WebDriver getLoginBeijingHtml(){
        driver = webDriverIEService.getNewWebDriver();
        driver.manage().window().maximize();
        System.out.println("WebDriverIEService loginBeijing Msg 开始登录北京银行");
        tracerLog.addTag("WebDriverIEService loginBeijing Msg", "开始登录北京银行");
        try{
            driver = webDriverIEService.getPage(driver,loginUrl);
        }catch(NoSuchWindowException e){
            System.out.println("打开北京银行登录页面报错，尝试重新初始化游览器"+e.getMessage());
            tracerLog.addTag("打开北京银行登录页面报错，尝试重新初始化游览器", e.getMessage());
            driver  = webDriverIEService.getPage(driver, loginUrl);

        }
        tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "北京银行登陆页加载已完成,当前页面句柄" + driver.getWindowHandle());

        return driver;
    }

    @Override
    public TaskBank getAllDataDone(String taskId) {
        return null;
    }
}
