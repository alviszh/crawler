package app.service;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.zxing.Result;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoAlipayBankCardInfo;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoAlipayInfo;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoAlipayPaymentInfo;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoDeliverAddress;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoOrderInfo;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoUserInfo;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TaobaAlipayPaymentInfoRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TaobaoAlipayBankCardInfoRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TaobaoAlipayInfoRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TaobaoDeliverAddressRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TaobaoOrderInfoRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TaobaoUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.TaobaoParser;
import app.service.aop.aspect.AspectCrawler;
import sun.misc.BASE64Encoder;

@Component
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.e_commerce.basic",
        "com.microservice.dao.entity.crawler.e_commerce.taobao"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.e_commerce.basic",
        "com.microservice.dao.repository.crawler.e_commerce.taobao"})
public class TaobaoService {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private AgentService agentService;

    @Autowired
    private TracerLog tracerLog;
    
    @Autowired
	private QrcodeService qrcodeService;
    
    @Autowired
    private WebDriverChromeService chromeDriverService;

    @Autowired
    private E_CommerceTaskStatusService e_commerceTaskStatusService;

    @Autowired
    private E_CommerceTaskRepository e_commerceTaskRepository;

    @Autowired
    private TaobaoDeliverAddressRepository taobaoDeliverAddressRepository;

    @Autowired
    private TaobaAlipayPaymentInfoRepository taobaAlipayPaymentInfoRepository;

    @Autowired
    private TaobaoAlipayBankCardInfoRepository taobaoAlipayBankCardInfoRepository;

    @Autowired
    private TaobaoUserInfoRepository taobaoUserInfoRepository;

    @Autowired
    private TaobaoAlipayInfoRepository taobaoAlipayInfoRepository;

    @Autowired
    private TaobaoOrderInfoRepository taobaoOrderInfoRepository;

    @Autowired
    private ChaoJiYingOcrService chaoJiYingOcrService;

    @Autowired
    private AsyncGetDataService asyncGetDataService;
    
    @Autowired
    private AspectCrawler aspectCrawler;
    


    @Autowired
    private TaobaoParser taobaoParser;

    private static final String LOGIN_URL = "https://i.taobao.com/my_taobao.htm";

    private static final String ALIPAY_SMS_URL = "https://authgtj.alipay.com/login/checkSecurity.htm";

    private static final String TAOBAO_VERFIY_URL = "ssssssssssssssss";

    //WebClient webClient = WebCrawler.getInstance().getWebClient();
    private WebDriver webDriver;

    /**
     * @param e_commerceJsonBean
     * @Des 登录淘宝
     * @author douge
     */
    public E_CommerceTask login(E_CommerceTask task, E_CommerceJsonBean e_commerceJsonBean) throws AWTException {

        //tracerLog.output("crawler.taobao.start", e_commerceJsonBean.getTaskid());
        webDriver = chromeDriverService.createFirefoxDriver();
//        Set<Cookie> cookies = webDriver.manage().getCookies();
//        for (Cookie cookie : cookies) {
//            System.out.println(cookie.toString());
//            System.out.println(cookie.getName() + "::" + cookie.getValue());
//            System.out.println(cookie.getDomain());
//            System.out.println(cookie.getExpiry());
//            System.out.println(cookie.getPath());
//        }

        webDriver.get(LOGIN_URL);

        String windowHandle = webDriver.getWindowHandle();
        tracerLog.output("Taobao网登录，打开网页获取网页handler", windowHandle);

        //记录WebDriverHandle
        task.setTesthtml(e_commerceJsonBean.toString());
        task.setWebdriverHandle(windowHandle);
        task.setCrawlerHost(e_commerceJsonBean.getIp());
        task.setCrawlerPort(e_commerceJsonBean.getPort());
        e_commerceJsonBean.setWebdriverHandle(windowHandle);
        task.setWebsiteType("taobao");
        e_commerceTaskRepository.save(task);

        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebElement quick2StaticElement = webDriver.findElement(By.xpath("//*[@id=\"J_Quick2Static\"]"));
        quick2StaticElement.click();
        intermission(3, 0);

        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        intermission(2, 1);
//        userNameInput.sendKeys(Keys.TAB);
//        intermission(3, 1);
        WebElement passwdInput = webDriver.findElement(By.xpath("//*[@id=\"TPL_password_1\"]"));
        //webDriver.findElement(By.xpath("//*[@id=\"password-label\"]/i")).click();
        Actions actions = new Actions(webDriver);
        actions.moveToElement(passwdInput).perform();
        passwdInput.click();

        intermission(2, 0);
        sendKeys(e_commerceJsonBean.getPasswd());
        intermission(2, 0);
        //WebElement userNameInput = webDriver.findElement(By.xpath("//*[@id=\"TPL_username_1\"]"));
        WebElement userNameInput = webDriver.findElement(By.xpath("//*[@id=\"J_Form\"]/div[2]/label/i"));
        //intermission(3, 0);
        //webDriver.findElement(By.xpath("//*[@id=\"J_Form\"]/div[2]/label/i")).click();
        intermission(2, 0);
        Actions actions2 = new Actions(webDriver);
        actions2.moveToElement(userNameInput).perform();

        userNameInput.click();
        intermission(2, 1);
        sendKeys(e_commerceJsonBean.getUsername());
        intermission(2, 1);
        //userNameInput.sendKeys(Keys.CONTROL,);

//        try {
//            WebElement element = webDriver.findElement(By.xpath("//*[@id=\"nc_1_n1z\"]"));
//            if (element != null) {
//                Actions action = new Actions(webDriver);
//                action.moveToElement(element).perform();
//                //action.dragAndDropBy(element, element.getLocation().x, element.getLocation().y + 260);
////                for (int i = 1; i <= 260; i++) {
////                    action.clickAndHold(element).moveByOffset(element.getLocation().x + 1, element.getLocation().y).perform();
////                    Thread.sleep(10);
////                }
//                System.out.println(element.getLocation().x +"::"+element.getLocation().y);
//                System.out.println("");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            //closeWebDirver();
//        }

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);


//        WebElement loginButton = webDriver.findElement(By.xpath("//*[@id=\"J_SubmitStatic\"]"));
//        loginButton.click();

        intermission(5, 0);


        WebElement title = webDriver.findElement(By.xpath("/html/head/title"));
        if (title.getText().contains("我的淘宝")) {
            //crawl(e_commerceJsonBean);
        } else if (title.getText().contains("淘宝网")) {
            throw new RuntimeException("登录名或密码错误！");
        } else {
            WebElement form = webDriver.findElement(By.xpath("//*[@id=\"J_Form\"]"));
            String text = form.getText();
            if (text.contains("请打开手机淘宝点击确认")) {
                webDriver.findElement(By.xpath("//*[@id=\"otherValidator\"]"));
            } else {
                webDriver.findElement(By.xpath("//*[@id=\"J_Form\"]/div[2]/a"));
            }

            webDriver.findElement(By.xpath("//*[@id=\"content\"]/div/ol/li[2]/a")).click();

            webDriver.findElement(By.xpath("//*[@id=\"J_GetCode\"]")).click();

            WebElement phone = webDriver.findElement(By.xpath("//*[@id=\"J_Form\"]/div[1]/div[1]"));
            task.setVerificationPhone(phone.getText().trim());//短信验证的手机号
            e_commerceTaskRepository.save(task);

            e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getPhase(),
                    E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getDescription(),
                    E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getError_code(), false, e_commerceJsonBean.getTaskid());

        }
        return task;
    }

    /**
     * @param e_commerceJsonBean
     * @Des 支付宝页面登录淘宝
     * @author douge
     */
    public E_CommerceTask login4Alipay(E_CommerceTask task, E_CommerceJsonBean e_commerceJsonBean) {
        webDriver = chromeDriverService.createFirefoxDriver();
        webDriver.get(LOGIN_URL);

        String windowHandle = webDriver.getWindowHandle();
        tracerLog.output("Taobao网登录，打开网页获取网页handler", windowHandle);

        //记录WebDriverHandle
        task.setTesthtml(e_commerceJsonBean.toString());
        task.setWebdriverHandle(windowHandle);
        task.setCrawlerHost(e_commerceJsonBean.getIp());
        task.setCrawlerPort(e_commerceJsonBean.getPort());
        task.setWebsiteType("taobao");
        e_commerceJsonBean.setWebdriverHandle(windowHandle);

        e_commerceTaskRepository.save(task);

        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        webDriver.findElement(By.xpath("//*[@id=\"J_Quick2Static\"]")).click();
        intermission(3, 0);

        webDriver.findElement(By.xpath("//*[@id=\"J_OtherLogin\"]/a[2]")).click();
        intermission(3, 0);

        webDriver.findElement(By.xpath("//*[@id=\"J-loginMethod-tabs\"]/li[2]")).click();
        intermission(3, 0);

        sendKeys(webDriver.findElement(By.xpath("//*[@id=\"J-input-user\"]")), e_commerceJsonBean.getUsername());
        intermission(2, 0);
        sendKeys(webDriver.findElement(By.xpath("//*[@id=\"password_rsainput\"]")), e_commerceJsonBean.getPasswd());
        intermission(2, 0);

        try {
            WebElement checkcodeImg = webDriver.findElement(By.xpath("//*[@id=\"J-checkcode-img\"]"));
            if (checkcodeImg != null) {
//                checkcodeImg.get
                String verifycode = getVerifycodeByChaoJiYing(checkcodeImg);
                sendKeys(webDriver.findElement(By.xpath("//*[@id=\"J-input-checkcode\"]")), verifycode);
            }
        } catch (Exception e) {
            System.out.println("alipay account login has no verifycode");
        }

        webDriver.findElement(By.xpath("//*[@id=\"J-login-btn\"]")).click();

        if (ALIPAY_SMS_URL.equals(webDriver.getCurrentUrl())) {
            try {
                WebElement element = webDriver.findElement(By.xpath("//*[@id=\"risk_qrcode_cnt\"]"));
                if (element != null) {
                	task = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR_GOTO_QRCODE.getPhase(),
                            E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR_GOTO_QRCODE.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR_GOTO_QRCODE.getDescription(),
                            E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR_GOTO_QRCODE.getError_code(), false, e_commerceJsonBean.getTaskid());
                	this.closeWebDirver(e_commerceJsonBean);
                    return task;
                }
            } catch (Exception e) {
                System.out.println("支付宝没有扫码验证， " + e.getMessage());
            }
            task = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getPhase(),
                    E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getDescription(),
                    E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getError_code(), false, e_commerceJsonBean.getTaskid());

        } else if (webDriver.getCurrentUrl().equals(LOGIN_URL)) {
            task = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
                    E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
                    E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, e_commerceJsonBean.getTaskid());
        } else {
        	task = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR_GOTO_QRCODE.getPhase(),
                    E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR_GOTO_QRCODE.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR_GOTO_QRCODE.getDescription(),
                    E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR_GOTO_QRCODE.getError_code(), false, e_commerceJsonBean.getTaskid());
        	this.closeWebDirver(e_commerceJsonBean);
            return task;
        }

        windowHandle = webDriver.getWindowHandle();
        tracerLog.output("Alipay账号登录，获取短信验证码页面handler", windowHandle);
        task.setWebdriverHandle(windowHandle);
        e_commerceTaskRepository.save(task);
        return task;

    }

    /**
     * @param e_commerceTask
     * @param e_commerceJsonBean
     * @return e_commerceTask
     * @Des 获取登录淘宝二维码
     * @author douge
     */
    public E_CommerceTask getTaobaoQRcode(E_CommerceTask e_commerceTask, E_CommerceJsonBean e_commerceJsonBean) throws IOException {
        try {
            webDriver = chromeDriverService.createFirefoxDriver();
            webDriver.get(LOGIN_URL);

            String windowHandle = webDriver.getWindowHandle();
            tracerLog.output("Taobao网登录，打开网页获取网页handler", windowHandle);

            //记录WebDriverHandle
            e_commerceTask.setTesthtml(e_commerceJsonBean.toString());
            e_commerceTask.setWebdriverHandle(windowHandle);
            e_commerceTask.setCrawlerHost(e_commerceJsonBean.getIp());
            e_commerceTask.setCrawlerPort(e_commerceJsonBean.getPort());
            e_commerceTask.setWebsiteType("taobao");
            e_commerceTaskRepository.save(e_commerceTask);
            
            WebDriverWait wait=new WebDriverWait(webDriver, 15);
			WebElement qrCodeImg= wait.until(new ExpectedCondition<WebElement>() {  
			            public WebElement apply(WebDriver driver) {  
			                return webDriver.findElement(By.id("J_QRCodeImg")); 
			            } 
			        });
			//将二维码的base64以及解析地址存入e_commerceTask
			e_commerceTask = encodeImgageToBase64ByWebElement(qrCodeImg, e_commerceTask);
            
            /*WebElement qrCodeImg = null;
            webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            try {
                qrCodeImg = webDriver.findElement(By.xpath("//*[@id=\"J_QRCodeImg\"]/img"));
            } catch (Exception e) {
                try {
                    webDriver.findElement(By.xpath("//*[@id=\"J_Quick2Static\"]")).click();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
            }

            qrCodeImg = webDriver.findElement(By.xpath("//*[@id=\"J_QRCodeImg\"]/img"));

            String base64Code = encodeImgageToBase64(new URL(qrCodeImg.getAttribute("src")));
            tracerLog.output("Taobao网二维码登录，二维码图片base64Code", base64Code);
            e_commerceTask.setTesthtml(base64Code);
            e_commerceJsonBean.setWebdriverHandle(windowHandle);*/
			e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhase(),
                    E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getDescription(),
                    E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getError_code(), false, e_commerceJsonBean.getTaskid());
            e_commerceTaskRepository.save(e_commerceTask);
        } catch (Exception e) {
            e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getPhase(),
                    E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getDescription(),
                    E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getError_code(), false, e_commerceJsonBean.getTaskid());

            tracerLog.output("crawler.bank.login.exception", e.getMessage());
            this.closeWebDirver(e_commerceJsonBean);

            e.printStackTrace();
        }
        return e_commerceTask;
    }

    /**
     * @param e_commerceJsonBean
     * @return e_commerceTask
     * @Des 淘宝二维码登录
     * @author douge
     */
    public E_CommerceTask checkQRcode(E_CommerceTask e_commerceTask, E_CommerceJsonBean e_commerceJsonBean) {
        String currentWebdriverHandle = webDriver.getWindowHandle();
        tracerLog.output("checkQRcode ::::::::::::::::::::::::::::::: currentWebdriverHandle ::::::::::::::::::::::::::::::: ", currentWebdriverHandle);

        if (currentWebdriverHandle.equals(e_commerceTask.getWebdriverHandle())) {

            System.out.println("+++++++++++++++++++++++++++++++++webDriver.getCurrentUrl()++++++++++++++++++++++++++++" + webDriver.getCurrentUrl());
            if (webDriver.getCurrentUrl().startsWith("https://login.taobao.com")) {
                WebElement element = webDriver.findElement(By.id("J_QRCodeLogin"));
                String clasValue = element.getAttribute("class");
                if(clasValue.contains("qrcode-login qrcode-login-ok")){
                	e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WATING_CONFIRM_QRCODE.getPhase(),
                            E_ComerceStatusCode.E_COMMERCE_WATING_CONFIRM_QRCODE.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WATING_CONFIRM_QRCODE.getDescription(),
                            E_ComerceStatusCode.E_COMMERCE_WATING_CONFIRM_QRCODE.getError_code(), false, e_commerceJsonBean.getTaskid());
                }else if(clasValue.contains("qrcode-login qrcode-login-error")){
            		e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WATING_QRCODE_TIMEOUT.getPhase(),
                            E_ComerceStatusCode.E_COMMERCE_WATING_QRCODE_TIMEOUT.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WATING_QRCODE_TIMEOUT.getDescription(),
                            E_ComerceStatusCode.E_COMMERCE_WATING_QRCODE_TIMEOUT.getError_code(), true, e_commerceJsonBean.getTaskid());
            	}else{
                	e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhase(),
                            E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getDescription(),
                            E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getError_code(), false, e_commerceJsonBean.getTaskid());
                }
                return e_commerceTask;
            }
            try {
                try {
                    WebElement iTaobao = webDriver.findElement(By.xpath("//*[@id=\"J_SiteNavLogin\"]/div[1]/div[2]/a[1]"));

                    if (iTaobao != null && iTaobao.getText() != null) {
                        E_CommerceTask task = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
                                E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
                                E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, e_commerceJsonBean.getTaskid());
                        //crawl(e_commerceJsonBean);
                        return task;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                WebElement qrCodeLogin = webDriver.findElement(By.xpath("//*[@id=\"J_QRCodeLogin\"]/div[4]/div/p"));
//                if (qrCodeLogin != null && qrCodeLogin.getText().trim().equals("请在手机上确认登录")) {
                E_CommerceTask task = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhase(),
                        E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getDescription(),
                        E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getError_code(), false, e_commerceJsonBean.getTaskid());
                //crawl(e_commerceJsonBean);
                return task;
//                } else {
//                    return e_commerceTask;
//                }
            } catch (Exception e) {
                tracerLog.output("taobao login error message:", e.getMessage());
                tracerLog.output("taobao login error message:", e.getStackTrace().toString());
                tracerLog.output("taobao login error html page:", webDriver.getPageSource());
                e.printStackTrace();
                throw new RuntimeException("login error:" + e.getMessage());
            }

        } else {
            E_CommerceTask task = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getPhase(),
                    E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getDescription(),
                    E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getError_code(), false, e_commerceJsonBean.getTaskid());
            return task;
        }
        //return e_commerceTask;
    }

    public static String encodeImgageToBase64(URL imageUrl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageUrl);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
    }
    
    /**
     * @param e_commerceTask
     * @param e_commerceJsonBean
     * @return e_commerceTask
     * @Des 获取登录支付宝二维码
     * @author wpy
     */
    public E_CommerceTask getAliQRcode(E_CommerceTask e_commerceTask, E_CommerceJsonBean e_commerceJsonBean) {
    	try {
    		String url = "https://auth.alipay.com/login/index.htm";
    		webDriver = chromeDriverService.createFirefoxDriver();
            webDriver.get(url);

            String windowHandle = webDriver.getWindowHandle();
            tracerLog.output("支付宝网登录，打开网页获取网页handler", windowHandle);

            //记录WebDriverHandle
            e_commerceTask.setTesthtml(e_commerceJsonBean.toString());
            e_commerceTask.setWebdriverHandle(windowHandle);
            e_commerceTask.setCrawlerHost(e_commerceJsonBean.getIp());
            e_commerceTask.setCrawlerPort(e_commerceJsonBean.getPort());
            e_commerceTask.setWebsiteType("aliQRcode");
            e_commerceTaskRepository.save(e_commerceTask);
            WebDriverWait wait=new WebDriverWait(webDriver, 15);
			WebElement qrCodeImg= wait.until(new ExpectedCondition<WebElement>() {  
			            public WebElement apply(WebDriver driver) {  
			                return webDriver.findElement(By.id("J-barcode-container")); 
			            } 
			        });
			//将二维码的base64以及解析地址存入e_commerceTask
			e_commerceTask = encodeImgageToBase64ByWebElement(qrCodeImg, e_commerceTask);
            e_commerceTaskRepository.save(e_commerceTask);
            e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhase(),
                    E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getDescription(),
                    E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getError_code(), false, e_commerceJsonBean.getTaskid());
        } catch (Exception e) {
            e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getPhase(),
                    E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getDescription(),
                    E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getError_code(), false, e_commerceJsonBean.getTaskid());

            tracerLog.output("crawler.taobao.taobaoservice.getAliQRcode.exception", e.getMessage());
            this.closeWebDirver(e_commerceJsonBean);

            e.printStackTrace();
        }
        return e_commerceTask;
	}
    
    /**
     * @param e_commerceTask
     * @param e_commerceJsonBean
     * @return e_commerceTask
     * @Des 支付宝二维码是否扫描登录
     * @author wpy
     */
    public E_CommerceTask checkAliQRcode(E_CommerceTask e_commerceTask, E_CommerceJsonBean e_commerceJsonBean) {
    	String currentWebdriverHandle = webDriver.getWindowHandle();
        tracerLog.output("checkQRcode ::::::::::::::::::::::::::::::: currentWebdriverHandle ::::::::::::::::::::::::::::::: ", currentWebdriverHandle);

        if (currentWebdriverHandle.equals(e_commerceTask.getWebdriverHandle())) {

            System.out.println("+++++++++++++++++++++++++++++++++webDriver.getCurrentUrl()++++++++++++++++++++++++++++" + webDriver.getCurrentUrl());
            if (webDriver.getCurrentUrl().startsWith("https://auth.alipay.com")) {
				try {
					WebElement ele = webDriver.findElement(By.id("J-barcode-container"));
					String clasValue = ele.getAttribute("class");
	            	if(clasValue.contains("barcode-container scanned")){
	            		e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WATING_CONFIRM_QRCODE.getPhase(),
	                            E_ComerceStatusCode.E_COMMERCE_WATING_CONFIRM_QRCODE.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WATING_CONFIRM_QRCODE.getDescription(),
	                            E_ComerceStatusCode.E_COMMERCE_WATING_CONFIRM_QRCODE.getError_code(), false, e_commerceJsonBean.getTaskid());
	            	}else if(clasValue.contains("barcode-container timeout")){
	            		e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WATING_QRCODE_TIMEOUT.getPhase(),
	                            E_ComerceStatusCode.E_COMMERCE_WATING_QRCODE_TIMEOUT.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WATING_QRCODE_TIMEOUT.getDescription(),
	                            E_ComerceStatusCode.E_COMMERCE_WATING_QRCODE_TIMEOUT.getError_code(), true, e_commerceJsonBean.getTaskid());
	            	}else{
	            		e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhase(),
	                            E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getDescription(),
	                            E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getError_code(), false, e_commerceJsonBean.getTaskid());
	            	}
				} catch (NoSuchElementException e) {
					e.printStackTrace();
					e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhase(),
                            E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getDescription(),
                            E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getError_code(), false, e_commerceJsonBean.getTaskid());
				}
                return e_commerceTask;
            }
            try {
            	WebDriverWait wait=new WebDriverWait(webDriver, 10);
    			WebElement globalUser= wait.until(new ExpectedCondition<WebElement>() {  
    			            public WebElement apply(WebDriver webDriver) {  
    			                return webDriver.findElement(By.id("globalUser"));  
    			            } 
    			        });
        		if(null != globalUser && !globalUser.getText().equals("")){
        			tracerLog.output("aliQRcode.login.success", "用户名为："+globalUser.getText()+"的用户已成功登陆！");
        			e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
                            E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
                            E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, e_commerceJsonBean.getTaskid());
        			//从支付宝跳转到淘宝网
        			webDriver.get("https://zht.alipay.com/taobaotrust.htm?target=http://www.taobao.com");
        		}else{
        			tracerLog.output("aliQRcode.login.doing.error", "用户未登录成功");
        			tracerLog.output("aliQRcode.login.doing.url", webDriver.getCurrentUrl());
        			e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getPhase(),
                            E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getDescription(),
                            E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getError_code(), false, e_commerceJsonBean.getTaskid());
        		}

            } catch (Exception e) {
                tracerLog.output("alipay login error message:", e.getMessage());
                tracerLog.output("alipay login error html page:", webDriver.getPageSource());
                e.printStackTrace();
                e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getPhase(),
                        E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getDescription(),
                        E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_ERROR.getError_code(), false, e_commerceJsonBean.getTaskid());
            }
            
            return e_commerceTask;

        } else {
            e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getPhase(),
                    E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getDescription(),
                    E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getError_code(), false, e_commerceJsonBean.getTaskid());
            return e_commerceTask;
        }
	}

	/**
     *
     */
    public void closeWebDirver(E_CommerceJsonBean jsonBean) {
        quit(jsonBean);
    }


    public E_CommerceTask verfiySMS(E_CommerceJsonBean e_commerceJsonBean) throws AWTException {

        if (webDriver.getWindowHandle().equals(e_commerceJsonBean.getWebdriverHandle())) {
            //
            if (webDriver.getCurrentUrl().trim().equals("https://auth.alipay.com/home/error.htm")) {
                return e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_ALIPAY_SYSTEM_ERROR.getPhase(),
                        E_ComerceStatusCode.E_COMMERCE_ALIPAY_SYSTEM_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_ALIPAY_SYSTEM_ERROR.getDescription(),
                        E_ComerceStatusCode.E_COMMERCE_ALIPAY_SYSTEM_ERROR.getError_code(), false, e_commerceJsonBean.getTaskid());
            }
            String inputXpah = "//*[@id=\"riskackcode\"]";
            String subimitXpath = "//*[@id=\"J-submit\"]/input";
            if (!ALIPAY_SMS_URL.equals(webDriver.getCurrentUrl())) {
                inputXpah = "//*[@id=\"J_Phone_Checkcode\"]";
                subimitXpath = "//*[@type=\"submit\"]";
            }
            WebElement input = webDriver.findElement(By.xpath(inputXpah));
//            Actions actions = new Actions(webDriver);
//            actions.moveToElement(input).perform();
//            actions.doubleClick();
            input.clear();
            this.intermission(1, 0);
            this.sendKeys(input, e_commerceJsonBean.getVerfiySMS());
            this.intermission(2, 0);
            webDriver.findElement(By.xpath(subimitXpath)).click();
            this.intermission(3, 0);

            E_CommerceTask e_commerceTask = null;

            //String titileText = webDriver.findElement(By.xpath("/html/head/title")).getText();


            try {
                WebElement iTaobao = webDriver.findElement(By.xpath("//*[@id=\"J_SiteNavLogin\"]/div[1]/div[2]/a[1]"));
                if (iTaobao != null && iTaobao.getText() != null) {
                    e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
                            E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
                            E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, e_commerceJsonBean.getTaskid());
                    //crawl(e_commerceJsonBean);
                } else {
                    e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhase(),
                            E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getDescription(),
                            E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getError_code(), false, e_commerceJsonBean.getTaskid());
                    tracerLog.output("taobao login for alipay errer!", webDriver.getPageSource());
                }
            } catch (Exception e) {
                tracerLog.output("taobao login error message:", e.getMessage());
                tracerLog.output("taobao login error message:", e.getStackTrace().toString());
                tracerLog.output("taobao login error html page:", webDriver.getPageSource());
                e.printStackTrace();
                throw new RuntimeException("login error:" + e.getMessage());
            }
            return e_commerceTask;


//            if (titileText.contains("我的淘宝")) {
//                e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.VALIDATE_CODE_SUCCESS.getPhase(),
//                        E_ComerceStatusCode.VALIDATE_CODE_SUCCESS.getPhasestatus(), E_ComerceStatusCode.VALIDATE_CODE_SUCCESS.getDescription(),
//                        E_ComerceStatusCode.VALIDATE_CODE_SUCCESS.getError_code(), false, e_commerceJsonBean.getTaskid());
//                //crawler(e_commerceJsonBean);
//            } else {
//                e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.VALIDATE_CODE_ERROR1.getPhase(),
//                        E_ComerceStatusCode.VALIDATE_CODE_ERROR1.getPhasestatus(), E_ComerceStatusCode.VALIDATE_CODE_ERROR1.getDescription(),
//                        E_ComerceStatusCode.VALIDATE_CODE_ERROR1.getError_code(), false, e_commerceJsonBean.getTaskid());
//            }
//            return e_commerceTask;
        } else {
            E_CommerceTask e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getPhase(),
                    E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getDescription(),
                    E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getError_code(), false, e_commerceJsonBean.getTaskid());
            return e_commerceTask;
        }

    }

    /**
     * @param
     * @param e_commerceJsonBean
     * @Des 爬取
     */
    @Async
    public E_CommerceTask crawl(E_CommerceJsonBean e_commerceJsonBean, E_CommerceTask e_commerceTask) {

        if (webDriver.getWindowHandle().equals(e_commerceTask.getWebdriverHandle())) {
            try {
                tracerLog.output("crawler.taobao.crawler.start", e_commerceJsonBean.getTaskid());

                //我的淘宝页面 头像用户名登录名 a标签点击
//                WebElement aElement = webDriver.findElement(By.xpath("//*[@id=\"J_Col_Main\"]/div/div[1]/div/div[1]/div[1]/div/div[1]/a"));
//                String loginName = aElement.getText().trim();
//                aElement.click();
//                this.intermission(2, 0);

                //得到所有窗口的句柄
//                Set<String> handles = webDriver.getWindowHandles();
//
//                String currentHandle = e_commerceJsonBean.getWebdriverHandle();
//
//                for (String handle : handles) {
//                    if (!currentHandle.equals(handle)) {
//                        webDriver = webDriver.switchTo().window(handle);
//                        webDriver.close();
//                    }
//                }
//                webDriver.switchTo().window(currentHandle);
                //用户信息
                //webDriver.findElement(By.xpath("//*[@id=\"baseInfoSet\"]/a")).click();//

                webDriver.get("https://i.taobao.com/user/baseInfoSet.htm");
                this.intermission(2, 0);
                TaobaoUserInfo userInfo = null;
                try {
                    userInfo = taobaoParser.parserTaobaoUserInfo(webDriver);
                } catch (Exception e) {
                    tracerLog.output("TAOBAO CRAWL ERROR , USER INFO PARSER ERROR!", e_commerceJsonBean.toString());
                    outputErrorMessage(e);
                }
                if (userInfo != null) {
                    e_commerceTask.setUserinfoStatus(200);
                    userInfo.setTaskid(e_commerceTask.getTaskid());
                    //userInfo.setLoginName(loginName);
                    taobaoUserInfoRepository.save(userInfo);
                } else {
                    e_commerceTask.setUserinfoStatus(201);//解析之后没有数据标记状态为-1
                }

                //收货地址点击
                //webDriver.findElement(By.xpath("//*[@id=\"newDeliverAddress\"]/a")).click();//
                webDriver.get("https://member1.taobao.com/member/fresh/deliver_address.htm");
                this.intermission(2, 0);

                List<TaobaoDeliverAddress> addressList = null;
                try {
                    addressList = taobaoParser.parserTaobaoDeliverAddress(webDriver);
                } catch (Exception e) {
                    tracerLog.output("TAOBAO CRAWL ERROR , Address PARSER ERROR!", e_commerceJsonBean.toString());
                    outputErrorMessage(e);
                }

                if (addressList != null && addressList.size() > 0) {
                    e_commerceTask.setAddressInfoStatus(200);
                    for (TaobaoDeliverAddress address : addressList) {
                        address.setTaskid(e_commerceTask.getTaskid());
                    }
                    taobaoDeliverAddressRepository.saveAll(addressList);
                } else {
                    e_commerceTask.setAddressInfoStatus(201);//解析之后没有数据标记状态为-1
                }


                webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                //支付宝信息点击
                //webDriver.findElement(By.xpath("//*[@id=\"newAccountManagement\"]/a")).click();//
                webDriver.get("https://member1.taobao.com/member/fresh/account_management.htm");

                this.intermission(3, 0);
                //进入支付宝  点击
                webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                webDriver.findElement(By.xpath("//*[@id=\"main-content\"]/div/div[2]/div/div[2]/p/a[1]")).click();
                this.intermission(3, 0);
                //webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

                //解析支付宝基本信息
                TaobaoAlipayInfo alipayInfo = taobaoParser.parserAlipayInfo(webDriver);
                if (alipayInfo != null) {
                    e_commerceTask.setAlipayInfoStatus(200);
                    alipayInfo.setTaskid(e_commerceTask.getTaskid());
                    taobaoAlipayInfoRepository.save(alipayInfo);
                } else {
                    tracerLog.output("TAOBAO CRAWL ERROR , ALIPAY INFO PARSER ERROR!", e_commerceJsonBean.toString());
                    e_commerceTask.setAlipayInfoStatus(201);//解析之后没有数据标记状态为-1
                }

                //银行卡
                //this.intermission(2, 0);
                //webDriver.findElement(By.xpath("//*[@id=\"J-assets-other-zht\"]/ul/li[1]/span/a")).click();
                webDriver.get("https://zht.alipay.com/asset/bankList.htm");
                webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                this.intermission(1, 0);
                List<TaobaoAlipayBankCardInfo> bankCardList = taobaoParser.parserAlipayBankCard(webDriver);
                if (bankCardList != null && bankCardList.size() > 0) {
                    e_commerceTask.setBankCardInfoStatus(200);
                    for (TaobaoAlipayBankCardInfo bankCardInfo : bankCardList) {
                        bankCardInfo.setTaskid(e_commerceTask.getTaskid());
                    }
                    taobaoAlipayBankCardInfoRepository.saveAll(bankCardList);
                } else {
                    tracerLog.output("TAOBAO CRAWL ERROR , ALIPAY BANK CARD INFO PARSER ERROR!", e_commerceJsonBean.toString());
                    e_commerceTask.setBankCardInfoStatus(201);//解析之后没有数据标记状态为-1
                }

                //支付宝查看所有交易记录 点击
                //webDriver.findElement(By.xpath("//*[@id=\"J-trend-consume\"]/div/div[2]/div[3]/a[2]")).click();
                webDriver.get("https://consumeprod.alipay.com/record/standard.htm");
                webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                List<TaobaoAlipayPaymentInfo> paymentInfoList = taobaoParser.parserAlipayPaymentInfo(webDriver);
                if (paymentInfoList != null && paymentInfoList.size() > 0) {
                    e_commerceTask.setAlipayPaymentInfoStatus(200);
                    for (TaobaoAlipayPaymentInfo paymentInfo : paymentInfoList) {
                        paymentInfo.setTaskid(e_commerceTask.getTaskid());
                    }
                    taobaAlipayPaymentInfoRepository.saveAll(paymentInfoList);
                } else {
                    tracerLog.output("TAOBAO CRAWL ERROR , ALIPAY PANMENT INFO PARSER ERROR!", e_commerceJsonBean.toString());
                    e_commerceTask.setAlipayPaymentInfoStatus(201);//解析之后没有数据标记状态为-1
                }
                //最后抓订单
                webDriver.get("https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm");
                webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                List<TaobaoOrderInfo> orderInfoList = taobaoParser.parserTaobaoOrderInfo4(webDriver);
                if (orderInfoList != null && orderInfoList.size() > 0) {
                    e_commerceTask.setOrderInfoStatus(200);
                    for (TaobaoOrderInfo orderInfo : orderInfoList) {
                        orderInfo.setTaskid(e_commerceTask.getTaskid());
                    }
                    taobaoOrderInfoRepository.saveAll(orderInfoList);
                } else {
                    tracerLog.output("TAOBAO CRAWL ERROR , ALIPAY INFO PARSER ERROR!", e_commerceJsonBean.toString());
                    e_commerceTask.setOrderInfoStatus(201);//解析之后没有数据标记状态为-1
                }
                quit(e_commerceJsonBean);
                return changeCrawlerStatusSuccess(e_commerceTask);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getPhase(),
                    E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getDescription(),
                    E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getError_code(), false, e_commerceJsonBean.getTaskid());
        }

        return e_commerceTask;
    }
    
    
    public E_CommerceTask crawler(E_CommerceJsonBean e_commerceJsonBean, E_CommerceTask e_commerceTask){
    	if (webDriver.getWindowHandle().equals(e_commerceTask.getWebdriverHandle())) {
    		//获取淘宝订单列表,并采集订单信息
    		WebClient webClient = getWebClientByWebDriver("buyertrade.taobao.com");
    		Set<org.openqa.selenium.Cookie> cookiesDriver = webDriver.manage().getCookies();
    		asyncGetDataService.getOrderInfo(e_commerceTask, webClient, cookiesDriver);
    		this.intermission(2, 0);
    		//获取淘宝用户信息
    		WebClient webClient2 = getWebClientByWebDriver("i.taobao.com");
    		asyncGetDataService.getUserInfo(e_commerceTask, webClient2);
    		this.intermission(2, 0);
    		//获取收货地址信息
    		String addressInfoUrl = "https://member1.taobao.com/member/fresh/deliver_address.htm";
    		tracerLog.output("crawler.getdata.addressInfoUrl", addressInfoUrl);
    		webDriver.get(addressInfoUrl);
    		this.intermission(2, 0);
    		asyncGetDataService.getAddressInfo(e_commerceTask, webDriver.getPageSource());
    		this.intermission(2, 0);
    		//切换到支付宝网站
    		webDriver.get("https://my.alipay.com/portal/i.htm");
    		//获取支付宝个人信息
    		getAliPayInfo(e_commerceTask);
    		//获取支付宝绑定银行卡
    		WebClient webClient3 = getWebClientByWebDriver("zht.alipay.com");
    		asyncGetDataService.getAliBankInfo(e_commerceTask, webClient3);
    		//获取支付宝交易流水（沿用原方法）
    		webDriver.get("https://consumeprod.alipay.com/record/standard.htm");
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            List<TaobaoAlipayPaymentInfo> paymentInfoList = taobaoParser.parserAlipayPaymentInfo(webDriver);
            if (paymentInfoList != null && paymentInfoList.size() > 0) {
                for (TaobaoAlipayPaymentInfo paymentInfo : paymentInfoList) {
                    paymentInfo.setTaskid(e_commerceTask.getTaskid());
                }
                taobaAlipayPaymentInfoRepository.saveAll(paymentInfoList);
				tracerLog.output("crawler.getAlipayPaymentInfo.saveList", "支付宝交易流水信息入库");
				e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ALIPAYMENT_SUCCESS.getPhase()
						, E_ComerceStatusCode.E_COMMERCE_ALIPAYMENT_SUCCESS.getPhasestatus()
						, E_ComerceStatusCode.E_COMMERCE_ALIPAYMENT_SUCCESS.getDescription()
						, E_ComerceStatusCode.E_COMMERCE_ALIPAYMENT_SUCCESS.getError_code()
						, "alipayment", 200, e_commerceTask.getTaskid());
            } else {
                tracerLog.output("TAOBAO CRAWL ERROR , ALIPAY PANMENT INFO PARSER ERROR!", e_commerceJsonBean.toString());
                tracerLog.output("crawler.getAlipayPaymentInfo.saveList.nodata", "支付宝交易流水信息无数据");
				e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ALIPAYMENT_ERROR.getPhase()
						, E_ComerceStatusCode.E_COMMERCE_ALIPAYMENT_ERROR.getPhasestatus()
						, E_ComerceStatusCode.E_COMMERCE_ALIPAYMENT_ERROR.getDescription()
						, E_ComerceStatusCode.E_COMMERCE_ALIPAYMENT_ERROR.getError_code()
						, "alipayment", 201, e_commerceTask.getTaskid());
            }
            
            //爬取完成，修改状态CRAWLER为SUCCESS
            e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getPhase());
            e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getPhasestatus());
            e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getDescription());
            e_commerceTask.setFinished(true);
            e_commerceTaskRepository.save(e_commerceTask);
            //调用quit方法
            e_commerceTask = quit(e_commerceJsonBean);
	    } else {
	        e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getPhase(),
	                E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getDescription(),
	                E_ComerceStatusCode.E_COMMERCE_WEBDRIVER_ERROR.getError_code(), false, e_commerceJsonBean.getTaskid());
	    }
    	return e_commerceTask;
    }
    
    public void getAliPayInfo(E_CommerceTask e_commerceTask) {
    	webDriver.findElement(By.id("showAccountAmount")).click();
    	this.intermission(1, 0);
		webDriver.findElement(By.id("showYuebaoAmount")).click();
		this.intermission(1, 0);
		webDriver.findElement(By.id("showHuabeiAmount")).click();
		this.intermission(1, 0);
		String aliyue = webDriver.findElement(By.id("account-amount-container")).getText();
		String yuebaoyue = webDriver.findElement(By.id("J-assets-mfund-amount")).getText();
		String keyongHuabei = webDriver.findElement(By.id("available-amount-container")).getText();
		String huabeiedu = webDriver.findElement(By.id("credit-amount-container")).getText();
		String userName = webDriver.findElement(By.id("globalUser")).getText();
		String accountName = webDriver.findElement(By.id("J-userInfo-account-userEmail")).getText();
		String shouyi = webDriver.findElement(By.id("J-income-num")).getText();
		tracerLog.output("支付宝用户基本信息part1", "aliyue:"+aliyue
				+ " yuebaoyue:"+yuebaoyue
				+ " keyongHuabei:"+keyongHuabei
				+ " huabeiedu:"+huabeiedu
				+ " userName:"+userName
				+ " accountName:"+accountName
				+ " shouyi:"+shouyi);
		TaobaoAlipayInfo alipayInfo = new TaobaoAlipayInfo();
		alipayInfo.setTaskid(e_commerceTask.getTaskid());
		alipayInfo.setAccountBalance(aliyue);
		alipayInfo.setYu_e_bao(yuebaoyue);
		alipayInfo.setYu_e_baoAccumulatedEarnings(shouyi);
		alipayInfo.setHuabeiAvailableCredit(keyongHuabei);
		alipayInfo.setHuabeiTotalCredit(huabeiedu);
		alipayInfo.setAccount(accountName);
		alipayInfo.setUserName(userName);
		//获取支付宝用户基本信息part2
		WebClient webClient = getWebClientByWebDriver("custweb.alipay.com");
		asyncGetDataService.getAliPayInfo(e_commerceTask, webClient, alipayInfo);
    }

    public E_CommerceTask changeCrawlerStatusSuccess(E_CommerceTask e_commerceTask) {

        tracerLog.output("taobao changeCrawlerStatusSuccess start", e_commerceTask.getTaskid());

        if (null != e_commerceTask.getUserinfoStatus()
                && null != e_commerceTask.getOrderInfoStatus()
                && null != e_commerceTask.getAddressInfoStatus()
                && null != e_commerceTask.getBankCardInfoStatus()
                && null != e_commerceTask.getAlipayPaymentInfoStatus()
                && null != e_commerceTask.getAlipayInfoStatus()) {
            e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getDescription());
            e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getPhase());
            e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getPhasestatus());
            e_commerceTask.setFinished(true);

            e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
            tracerLog.output("changeCrawlerStatusSuccess success", e_commerceTask.getTaskid());
        } else {
            tracerLog.output("changeCrawlerStatusSuccess fail", e_commerceTask.toString());
        }
        return e_commerceTask;
    }


    /**
     * @param e_commerceJsonBean
     * @Des 系统退出，释放资源
     */

    public E_CommerceTask quit(E_CommerceJsonBean e_commerceJsonBean) {
        tracerLog.output("taobao quit", e_commerceJsonBean.toString());
        tracerLog.output("taobao=========================================================quit", e_commerceJsonBean.toString());
        //关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试  , description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
        E_CommerceTask task = e_commerceTaskStatusService.systemClose(true, e_commerceJsonBean.getTaskid());
        //调用公用释放资源方法
        if (task != null) {
            agentService.releaseInstance(task.getCrawlerHost(), this.webDriver);
        } else {
            tracerLog.output("quit taobaoTask is null", "");
        }
        aspectCrawler.afterGetAllDataDone(task);
        return task;
    }

    private void sendKeys(String key) throws AWTException {
//        char[] chars = key.toCharArray();
//        for (char c : chars) {
//            intermission(1, 0);
//            inputElement.sendKeys(String.valueOf(c));
//        }


        setClipboardData(key);
//        // 模拟Ctrl+V，进行粘贴
        Robot robot = new Robot();

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void setClipboardData(String str) {
        try {
            StringSelection stringSelection = new StringSelection(str);
            Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(stringSelection, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            e.printStackTrace();
        }
    }

    private void sendKeys(WebElement inputElement, String key) {
        char[] chars = key.toCharArray();
        for (char c : chars) {
            intermission(1, 0);
            inputElement.sendKeys(String.valueOf(c));
        }
    }


    //间歇
    private void intermission(int time1, int tim2) {
        Random r = new Random();
        int d = (r.nextInt(time1) + tim2) * 1000;
        time1 = Math.abs(d) + Math.abs(r.nextInt(1000));
        try {
            //System.out.println("sleep " + time1);
            Thread.sleep(time1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String outputErrorMessage(Exception e) {
        e.printStackTrace();
        StackTraceElement[] stackTraceArr = e.getStackTrace();
        StringBuffer errorMessage = new StringBuffer();
        for (StackTraceElement stackTraceElement : stackTraceArr) {
            errorMessage.append(stackTraceElement.toString()).append("/r/n");
        }
        return errorMessage.toString();
    }

    private String getVerifycodeByChaoJiYing(WebElement element) throws Exception {
        File srcFile = ((TakesScreenshot) this.webDriver).getScreenshotAs(OutputType.FILE); //讲截取的图片以文件的形式返回
        BufferedImage fullImg = ImageIO.read(srcFile);

        Point point = element.getLocation();
        int eleWidth = element.getSize().getWidth();
        int eleHeight = element.getSize().getHeight();

        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
        ImageIO.write(eleScreenshot, "png", srcFile);

        String userHome = System.getProperty("user.home");

        File imgHomeFile = new File(userHome + File.separatorChar + "img");
        if (!imgHomeFile.exists()) {
            imgHomeFile.mkdirs();
        }
        File file = new File(imgHomeFile.getAbsolutePath() + File.separatorChar + UUID.randomUUID() + ".png");

        FileUtils.copyFile(srcFile, file);

        String verifyCode = chaoJiYingOcrService.getVerifycodeByChaoJiYing("1902", file.getAbsolutePath());

        return verifyCode;
    }
    
    private E_CommerceTask encodeImgageToBase64ByWebElement(WebElement element, E_CommerceTask e_CommerceTask) throws Exception{
    	File qrcode = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
    	byte[] data = null;
    	try {
            Point p = element.getLocation();
            int width = element.getSize().getWidth();
            int height = element.getSize().getHeight();
            BufferedImage img = ImageIO.read(qrcode);
            BufferedImage dest = img.getSubimage(p.getX(), p.getY(),
                    width, height);
            ImageIO.write(dest, "png", qrcode);
            Thread.sleep(1000);
            Result result = qrcodeService.getQRresult(dest);
            tracerLog.addTag("crawler.taobaoService.qrcode.result", result.toString());
            tracerLog.addTag("crawler.taobaoService.qrcode.url", result.getText());
            e_CommerceTask.setQrUrl(result.getText());
            FileInputStream inputStream = new FileInputStream(qrcode);
			// 读取图片字节数组
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		String base64 = encoder.encode(data);
		tracerLog.output("二维码登录，二维码图片base64Code", base64);
		e_CommerceTask.setBaseCode(base64);
		
		return e_CommerceTask;
	}
    
    public WebClient getWebClientByWebDriver(String host) {
		Set<org.openqa.selenium.Cookie> cookiesDriver = webDriver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie(host, cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		return webClient;
	}

}