package app.service;

import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.TaskStatusCode;
import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.bean.WebParamE;
import app.commontracerlog.TracerLog;
import app.parser.SuNingParser;

@Component
@EnableAsync
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.e_commerce.suning" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.e_commerce.suning" })
public class SuNingService {

	private static WebDriver driver;
	
	@Autowired
    private E_CommerceTaskStatusService e_commerceTaskStatusService;
	@Autowired
    private E_CommerceTaskRepository e_commerceTaskRepository;
	@Autowired
    private WebDriverChromeService chromeDriverService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private AsyncGetData asyncGetData;
	@Autowired
	private SuNingParser suNingParser;
	@Autowired
	private TracerLog tracerLog;
	
	public E_CommerceTask sendSMS(E_CommerceJsonBean e_CommerceJsonBean, E_CommerceTask ecommerceTask) {
		tracerLog.output("crawler.e_commerce.SuNingService.sendSMS.taskid", e_CommerceJsonBean.getTaskid());
		try {
			WebParamE webParamE = suNingParser.sendSMS(e_CommerceJsonBean);
			if(null != webParamE.getHtml()){
				if(webParamE.getHtml().contains("成功")){
					ecommerceTask = e_commerceTaskStatusService.changeStatus(TaskStatusCode.WAIT_CODE_SUCCESS.getPhase(), TaskStatusCode.WAIT_CODE_SUCCESS.getPhasestatus(), TaskStatusCode.WAIT_CODE_SUCCESS.getDescription(),
						TaskStatusCode.WAIT_CODE_SUCCESS.getError_code(), false, e_CommerceJsonBean.getTaskid());
				}else{
					String html = webParamE.getHtml();
					int i = html.indexOf("msg");
					int j = html.indexOf(",", i);
					String msg = html.substring(i+6, j-1);
					ecommerceTask = e_commerceTaskStatusService.changeStatus(TaskStatusCode.SEND_CODE_ERROR.getPhase(), TaskStatusCode.SEND_CODE_ERROR.getPhasestatus(), msg,
							TaskStatusCode.SEND_CODE_ERROR.getError_code(), false, e_CommerceJsonBean.getTaskid());
				}
			}else{
				ecommerceTask = e_commerceTaskStatusService.changeStatus(TaskStatusCode.SEND_CODE_ERROR.getPhase(), TaskStatusCode.SEND_CODE_ERROR.getPhasestatus(), TaskStatusCode.SEND_CODE_ERROR.getDescription(),
						TaskStatusCode.SEND_CODE_ERROR.getError_code(), false, e_CommerceJsonBean.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			ecommerceTask = e_commerceTaskStatusService.changeStatus(TaskStatusCode.SEND_CODE_ERROR.getPhase(), TaskStatusCode.SEND_CODE_ERROR.getPhasestatus(), TaskStatusCode.SEND_CODE_ERROR.getDescription(),
					TaskStatusCode.SEND_CODE_ERROR.getError_code(), false, e_CommerceJsonBean.getTaskid());
		}
		
		return ecommerceTask;
	}

	public E_CommerceTask login(E_CommerceJsonBean e_CommerceJsonBean, E_CommerceTask ecommerceTask) {
		tracerLog.output("crawler.e_commerce.SuNingService.login.taskid", e_CommerceJsonBean.getTaskid());
		tracerLog.output("crawler.e_commerce.SuNingService.login.loginType", e_CommerceJsonBean.getLogintype());
		//记录ip port
        ecommerceTask.setCrawlerHost(e_CommerceJsonBean.getIp());
        ecommerceTask.setCrawlerPort(e_CommerceJsonBean.getPort());
        e_commerceTaskRepository.save(ecommerceTask);
		//创建chromeDriver
		driver = chromeDriverService.createFirefoxDriver();
		try {
			WebParamE webParamE = login(e_CommerceJsonBean);
			if(null != webParamE.getHtml()){
				ecommerceTask = e_commerceTaskStatusService.changeStatus(TaskStatusCode.LOGIN_PWD_ERROR.getPhase(), TaskStatusCode.LOGIN_PWD_ERROR.getPhasestatus(), webParamE.getHtml(),
						TaskStatusCode.LOGIN_PWD_ERROR.getError_code(), false, e_CommerceJsonBean.getTaskid());
				//释放资源并退出浏览器
				quitDriver(e_CommerceJsonBean);
			}else if(null != webParamE.getDriver()){
				e_commerceTaskStatusService.loingSucess(ecommerceTask);
//				driver = webParamE.getDriver();
				getData(ecommerceTask, e_CommerceJsonBean);
			}else{
				ecommerceTask = e_commerceTaskStatusService.changeStatus(TaskStatusCode.LOGIN_ABNORMAL_ERROR.getPhase(), TaskStatusCode.LOGIN_ABNORMAL_ERROR.getPhasestatus(), TaskStatusCode.LOGIN_ABNORMAL_ERROR.getDescription(),
						TaskStatusCode.LOGIN_ABNORMAL_ERROR.getError_code(), false, e_CommerceJsonBean.getTaskid());
				//释放资源并退出浏览器
				quitDriver(e_CommerceJsonBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ecommerceTask = e_commerceTaskStatusService.changeStatus(TaskStatusCode.LOGIN_ABNORMAL_ERROR.getPhase(), TaskStatusCode.LOGIN_ABNORMAL_ERROR.getPhasestatus(), "网络波动异常，请您稍后重试！",
					TaskStatusCode.LOGIN_ABNORMAL_ERROR.getError_code(), false, e_CommerceJsonBean.getTaskid());
			//释放资源并退出浏览器
			quitDriver(e_CommerceJsonBean);
		}
		return ecommerceTask;
	}
	
	public WebParamE login(E_CommerceJsonBean e_CommerceJsonBean) throws Exception{
		WebParamE webParamE = new WebParamE();
		
		String baseUrl = "https://passport.suning.com/ids/login?loginTheme=wap_new";
		webParamE.setUrl(baseUrl);
		tracerLog.output("crawler.SuNingParser.login.url", baseUrl);
		driver.get(baseUrl);
		Thread.sleep(2000);
		WebElement slide = null;
		try {
			slide = driver.findElement(By.id("WAP_login_password_slide"));
		} catch (NoSuchElementException e) {
			tracerLog.output("#dt_notice为空，未知的错误", "当前页面URL:" + driver.getCurrentUrl()+"当前页面" + driver.getPageSource()); 
			//截图 
			String shotpath = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracerLog.output("dt_notice为空，未知的错误Screenshot", "截图:" + shotpath);
			return webParamE;
		}
		if(null != slide){
			String attribute = slide.getAttribute("style");
			//根据属性判断是否有滑块验证,如果有滑块验证则刷新页面
			if(!attribute.contains("display: none;")){
			tracerLog.output("have 滑动", "123");
				driver.navigate().refresh();
				Thread.sleep(1000);
			}
			//判断登录方式
			if(e_CommerceJsonBean.getLogintype().contains("sn_num")){			//账号密码登录
				tracerLog.output("账号登录", "123");
				WebElement numLogin = driver.findElement(By.name("WAP_login_message_paslog"));
				numLogin.click();
				WebElement username = driver.findElement(By.id("username"));
				WebElement password = driver.findElement(By.id("password"));
				tracerLog.output("开始输入", "账号密码");
				username.sendKeys(e_CommerceJsonBean.getUsername());
				Thread.sleep(1000);
				password.sendKeys(e_CommerceJsonBean.getPasswd());
				Thread.sleep(1000);
			}else if(e_CommerceJsonBean.getLogintype().contains("sn_phone")){	//短信登录
				tracerLog.output("短信登录", "123");
				WebElement smsLogin = driver.findElement(By.name("WAP_login_password_meslog"));
				smsLogin.click();
				Thread.sleep(500);
				WebElement phoneNum = driver.findElement(By.id("phoneNum"));
				WebElement phoneCode = driver.findElement(By.id("phoneCode"));
				tracerLog.output("开始输入", "手机号，短信验证码");
				phoneNum.sendKeys(e_CommerceJsonBean.getUsername());
				Thread.sleep(1000);
				phoneCode.sendKeys(e_CommerceJsonBean.getVerfiySMS());
				Thread.sleep(1000);
			}
			
			WebDriverWait wait=new WebDriverWait(driver, 10);
			WebElement loginBtn= wait.until(new ExpectedCondition<WebElement>() {  
			            public WebElement apply(WebDriver driver) {  
			                return driver.findElement(By.name("WAP_login_password_logsubmit"));  
			            } 
			        });
			//截图 
			String shotpath = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracerLog.output("点击登录按钮前Screenshot", "截图:" + shotpath);
			tracerLog.output("开始点击", "登录按钮");
			tracerLog.output("登录按钮==》loginBtn", loginBtn.getAttribute("href"));
			Thread.sleep(1000);
//			JavascriptExecutor js = (JavascriptExecutor)driver;
			try{
				loginBtn.click();
				//截图 
				String shotpath1 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracerLog.output("点击登录按钮的Screenshot", "截图:" + shotpath1);
//				js.executeScript("arguments[0].click();", loginBtn);
			}catch(Exception e){
				//截图 
				String shotpath1 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracerLog.output("点击登录按钮出现异常Screenshot", "截图:" + shotpath1);
				tracerLog.output("点击异常", "++++++++++++++++++++++");
				e.printStackTrace();
				tracerLog.output("点击异常", "++++++++++++++++++++++");
			}
			
			Thread.sleep(1000);
			tracerLog.output("点击完成", "登录按钮");
			tracerLog.output("crawler.SuNingParser.logined.page", driver.getPageSource());
			tracerLog.output("crawler.SuNingParser.logined.url", driver.getCurrentUrl());
			tracerLog.output("crawler.SuNingParser.logined.WindowHandle", driver.getWindowHandle());
			//截图 
			String shotpath1 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracerLog.output("点击登录按钮后Screenshot", "截图:" + shotpath1);
			Document doc = Jsoup.parse(driver.getPageSource());
			Elements eles = doc.select(".alert-msg");
			Elements wbox = doc.select(".search-content.wbox");
			String msg = eles.text();
			tracerLog.output("crawler.SuNingParser.logined.eles", eles.toString());
			tracerLog.output("crawler.SuNingParser.logined.wbox", wbox.toString());
			if(null != msg && !msg.equals("")){
				if(msg.contains("您还没有注册苏宁易购")){
					msg = "您输入的账号还没有注册苏宁易购,请确认账号正确无误后重试！";
				}
				webParamE.setHtml(msg);
				tracerLog.output("crawler.SuNingParser.login.fail", msg);
			}else if(driver.getCurrentUrl().contains("https://m.suning.com/") && null != wbox && wbox.size() > 0){
				webParamE.setDriver(driver);
				tracerLog.output("crawler.SuNingParser.login.success", "登陆成功");
			}else if(driver.getCurrentUrl().contains("https://passport.suning.com/ids/login?loginTheme=wap_new") && driver.getPageSource().contains("请设置密码成为苏宁易购会员")){
				webParamE.setHtml("您好，请您自行到苏宁官网设置密码成为苏宁易购会员后重试！");
				tracerLog.output("crawler.SuNingParser.login.noVIP", "用户不是苏宁易购会员，无法登录并爬取信息。");
			}else{
				webParamE.setHtml("登录异常，请您稍后重试。");
				tracerLog.output("crawler.SuNingParser.login.fail2", "未进入到登陆成功页面。");
			}
		}
		
		return webParamE;
	}
	
	public E_CommerceTask getData(E_CommerceTask ecommerceTask, E_CommerceJsonBean e_CommerceJsonBean) throws Exception{
		tracerLog.output("crawler is begin", ecommerceTask.getTaskid());
		ecommerceTask = e_commerceTaskStatusService.changeStatus(TaskStatusCode.CRAWLER_DOING.getPhase(), TaskStatusCode.CRAWLER_DOING.getPhasestatus(), TaskStatusCode.CRAWLER_DOING.getDescription(),
				TaskStatusCode.CRAWLER_DOING.getError_code(), false, ecommerceTask.getTaskid());
		ecommerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_ERROR.getPhase(), E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_ERROR.getDescription(),
				E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_ERROR.getError_code(), "privilegeinfo", 201, ecommerceTask.getTaskid());
		//爬取订单详情
		WebClient webClient = transforCookie(driver, "order.suning.com");
		tracerLog.output("开始爬取订单详情", ecommerceTask.getTaskid());
		asyncGetData.getOrderList(ecommerceTask, webClient, driver);
		//收货地址信息
		WebClient webClient11 = transforCookie(driver, "my.suning.com");
		tracerLog.output("开始爬取收货地址信息", ecommerceTask.getTaskid());
		asyncGetData.getAddress(ecommerceTask, webClient11, driver);
		//pc网页版苏宁首页
		tracerLog.output("开始跳转页面前", ecommerceTask.getTaskid());
		Thread.sleep(3000);
		tracerLog.output("开始跳转页面后", ecommerceTask.getTaskid());
		String indexUrl = "https://my.suning.com/";
		driver.get(indexUrl);
		//爬取用户信息以及收货地址信息
		WebClient webClient1 = transforCookie(driver, "my.suning.com");
		tracerLog.output("开始爬取用户信息", ecommerceTask.getTaskid());
		asyncGetData.getUserInfo(ecommerceTask, webClient1, driver);
		//pc网页版易付宝首页
		Thread.sleep(1000);
		String pay = "https://pay.suning.com/epp-epw/useraccount/compatible-login!login.action";
		driver.get(pay);
		//爬取账户认证信息以及绑定银行卡信息
		WebClient webClient2 = transforCookie(driver, "pay.suning.com");
		tracerLog.output("开始爬取账户认证信息", ecommerceTask.getTaskid());
		asyncGetData.getAccountInfo(ecommerceTask, webClient2, driver);
		tracerLog.output("开始爬取绑定银行卡信息", ecommerceTask.getTaskid());
		asyncGetData.getBankCardInfo(ecommerceTask, webClient2, driver);
		
		//判断是否爬取完成修改finish字段
		e_commerceTaskStatusService.changeFinish(ecommerceTask.getTaskid());
		//释放资源并退出浏览器
		quitDriver(e_CommerceJsonBean);
		return ecommerceTask;
	}
	
	//根据不同的domian获取不同的webclient
	public WebClient transforCookie(WebDriver driver, String domain) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
        
        for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
            Cookie cookieWebClient = new Cookie(domain, cookie.getName(), cookie.getValue());
            webClient.getCookieManager().addCookie(cookieWebClient);
        }
		
		return webClient;
	}
	
	public E_CommerceTask quitDriver(E_CommerceJsonBean e_CommerceJsonBean){
		tracerLog.output("quit", e_CommerceJsonBean.toString()); 
		//截图 
		try {
			String shotpath = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracerLog.output("调用quit方法时的Screenshot", "截图:" + shotpath);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("调用quit方法时截图报错", e.toString());
		}
		//关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试  , description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
		E_CommerceTask ecommerceTask = e_commerceTaskStatusService.systemClose(true, e_CommerceJsonBean.getTaskid());  
		//调用公用释放资源方法
		if(ecommerceTask != null){
			agentService.releaseInstance(ecommerceTask.getCrawlerHost(), driver);
		} else{
			tracerLog.output("quit ecommerceTask is null",""); 
		}
		return ecommerceTask;
	}
	
}
