package app.service;

import app.commontracerlog.TracerLog;
import app.parser.BeijingBankDebitCardParser;
import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.module.ddxoft.VK;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.VirtualKeyBoard;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 北京银行-储蓄卡登录接口（遇到图片验证码错误后，重试机制）
 * @author zmy
 *
 */
@Component
public class BeijingBankDebitCardLoginService {
    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private WebDriverIEService webDriverIEService;
    @Autowired
    private TaskBankStatusService taskBankStatusService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private BeijingBankDebitCardParser beijingBankDebitCardParser;

    private WebDriver driver;
    String loginUrl = "https://ebank.bankofbeijing.com.cn/bccbpb/accountLogon.jsp";
    private static final String LEN_MIN = "0";
    private static final String TIME_ADD = "0";
    private static final String STR_DEBUG = "a";

    /**
     * 登录，重试机制
     * @param bankJsonBean
     * @return WebDriver
     * @throws Exception
     */
    @Retryable(value={RuntimeException.class},maxAttempts=2,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
    //maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
    //backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
    public WebDriver retryLogin(BankJsonBean bankJsonBean,TaskBank taskBank) {
        String path = "";

        driver = getLoginBeijingHtml();
        if (driver == null) {
            return driver;
        }
        String windowHandle = driver.getWindowHandle();
        taskBank.setWebdriverHandle(windowHandle);

        tracerLog.addTag("开始输入登录账号",  bankJsonBean.getLoginName());

        //键盘输入账号
        driver.findElement(By.id("logonid_temp")).sendKeys(bankJsonBean.getLoginName());
        try {
            VK.KeyPress(bankJsonBean.getLoginName());
        } catch (Exception e) {
            tracerLog.addTag("使用winio输入账号出错",e.getMessage());
        }

        tracerLog.addTag("开始输入登录密码", "taskId: " + bankJsonBean.getTaskid());
        //输入密码
        driver.findElement(By.id("password_temp")).sendKeys(bankJsonBean.getPassword());
        try {
            VK.KeyPress(bankJsonBean.getPassword());

        } catch (Exception e) {
            tracerLog.addTag("使用winio输入密码出错", e.getMessage());
        }

        //====================开始识别图片验证码==========================
        long starttimeCode = System.currentTimeMillis();
        String captchaImgPath = null;
        try {
            captchaImgPath = WebDriverUnit.saveImg(driver, By.id("verifyImg"));
        } catch (Exception e) {
            tracerLog.addTag("保存图片验证码出错", e.getMessage());
        }
        System.out.println("captchaImgPath---------------" + captchaImgPath);
        String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1004", LEN_MIN, TIME_ADD, STR_DEBUG,
                captchaImgPath);
        System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
        Gson gson = new GsonBuilder().create();
        String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
        System.out.println("code ====>>" + code);
        tracerLog.addTag("BeijingBankDebitCardService.login", "图片验证码url：" + captchaImgPath + "，识别结果：" + code);
        //====================识别图片验证码结束==========================
        long endtimeCode = System.currentTimeMillis();
        tracerLog.addTag("识别图片验证码所用时间：",(endtimeCode-starttimeCode)+":ms");

        //输入图片验证码
        code = code.toLowerCase();
        tracerLog.addTag("开始输入图片验证码",code);
        driver.findElement(By.name("verifyCode")).sendKeys(code);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //模拟点击登录按键
        driver.findElement(By.id("button")).click();//登录

        //弹出错误框
        String errInfo = "";
        try {
            //错误信息
            Alert alert = driver.switchTo().alert();
            errInfo = alert.getText();
            System.out.println("错误信息alert:"+ errInfo);
            tracerLog.addTag("alert弹框errInfo: ", errInfo);

        } catch (Exception ex) {
            System.out.println("没有alert弹框");
        }
        boolean flag = false; //登录成功标识（网银小贴士页面）

        //等待10秒，如果还没有登录成功的标识div#Page_content(跳转到主页) 则表示登录失败
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(6, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
        WebElement login = null;
        try {
            login = wait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(By.id("Page_content")); //网银小贴士提示页面的div id="Page_content1"
                }
            });
        } catch (Exception e) {
            tracerLog.addTag("BeijingBankDebitCard.login.exception", "登录异常");
            try {
                path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
            } catch (Exception e1) {
                tracerLog.addTag("点击登录后，截图出错", e.getMessage());
            }
            String exStr = e.toString();
            tracerLog.addTag("登录异常", "#Page_content 元素等待6秒未出现,异常:" + exStr + ",截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());
            System.out.println("#Page_content 元素等待6秒未出现,异常:" + exStr + ",截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());
            String page = "";
            try {
                page = driver.getPageSource();
                if (page.contains("网银小贴示")) {
                    flag = true;//登录成功
                }
                System.out.println("page--" + page);
                tracerLog.addTag("登录异常页面：","flag="+flag+"，page="+ page);
            } catch (Exception ex) {
                String ext = ex.toString();
                tracerLog.addTag("driver.getPageSource.Exception", "登录失败");
                tracerLog.addTag("driver.getPageSource.Exception.ex", ex.toString());
                //登录失败，获取错误信息
                String key = "Modal dialog present:";
                if ("".equals(errInfo)) {
                    if (ext.contains(key)) {
                        if (ext.contains("验证码不能为空")) {
                            tracerLog.addTag(path, "验证码不能为空，触发retry机制");
                            driver.quit();
                            throw new RuntimeException("请输入正确的验证码");
                        }
                    }
                }
            }

            if (!flag) {//flag =false 登录失败
                if (errInfo.equals("") || page.contains("登录失败")) {
                    //登录失败，获取错误信息(错误页面)
                    errInfo = beijingBankDebitCardParser.getErrInfo(page);
                }
                tracerLog.addTag("login.creditCard.errInfo", "登录失败,错误信息：" + errInfo + "，taskid=" + taskBank.getTaskid());

                if (errInfo.contains("验证码")) {
                    tracerLog.addTag(path, "验证码错误，触发retry机制");
                    driver.quit();
                    throw new RuntimeException("请输入正确的验证码");
                }
                taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
                        BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(),
                        errInfo,
                        BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(), windowHandle);
                //登录错误，释放资源
                //释放instance ip ，quit webdriver
                /*tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
                agentService.releaseInstance(taskBank.getCrawlerHost(), driver);*/
            }
        }
        return driver;
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
            tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "北京银行登陆页加载已完成,当前页面句柄"+ driver.getWindowHandle());

        }catch(NoSuchWindowException e){
            System.out.println("打开北京银行登录页面报错，尝试重新初始化游览器"+e.getMessage());
            tracerLog.addTag("打开北京银行登录页面报错，尝试重新初始化游览器", e.getMessage());
//            driver  = webDriverIEService.getPage(driver, loginUrl);
            driver = null;
        }
        return driver;
    }
}
