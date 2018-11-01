package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.ccbchina.CcbChinaCreditcardAccountTypeRepository;
import com.microservice.dao.repository.crawler.bank.ccbchina.CcbChinaCreditcardTransFlowRepository;
import com.module.jna.webdriver.WebDriverUnit;

import app.bean.WebData;
import app.commontracerlog.TracerLog;
import app.parser.CcbChinaCreditcardParser;
import app.service.aop.ICrawlerLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic","com.microservice.dao.entity.crawler.bank.ccbchina"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic","com.microservice.dao.repository.crawler.bank.ccbchina"})
public class CcbChinaCreditcardService implements ICrawlerLogin{
	
	@Autowired
	private WebDriverChromeService webDriverChromeService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private CcbChinaCreditcardParser ccbChinaCreditcardParser;
	@Autowired
	private CcbChinaCreditcardTransFlowRepository ccbChinaCreditcardTransFlowRepository;
	@Autowired
	private CcbChinaCreditcardAccountTypeRepository ccbChinaCreditcardAccountTypeRepository;
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private AgentService agentService;
	@Autowired
	private CcbChinaCreditcardLoginService ccbChinaCreditcardLoginService;
	
	private WebDriver driver;

	/**
	 * @Des 建行信用卡登录
	 * @param bankJsonBean
	 */
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracerLog.output("crawler.bank.login.creditcard.start", taskBank.getTaskid());
		
		try {
			driver = ccbChinaCreditcardLoginService.retryLogin(bankJsonBean);
			if(null != driver){
				//截图
				String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.output("登录后页面截图：", path);				
			}
		} catch (Exception e) {
			tracerLog.output("CcbChinaCreditcardService.login.getHtml", "error");
			tracerLog.output("login.getHtml.error", e.getMessage());
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(), 
					BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(), 
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(), 
					BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(),false,bankJsonBean.getTaskid());
			//释放机器，关闭driver进程
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
		
		String url = driver.getCurrentUrl();
		String html = driver.getPageSource();
//		tracerLog.output("卡号登录后页面：", "<xmp>"+html+"</xmp>");
		try{			
			if(url.contains("TXCODE")){
				if(html.contains("请输入正确的附加码")){
					taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(), 
							BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
							"系统繁忙，请稍后再试！", 
							BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(),false, bankJsonBean.getTaskid());
					//释放机器，关闭driver进程
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}else if(html.contains("我行对储蓄账户查询服务进行了优化升级，原有查询密码将无法继续使用")){
					taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(), 
							BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
							"当前查询密码无法继续使用，请您到建行官网重新设置查询密码后查询。", 
							BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(),false, bankJsonBean.getTaskid());
					//释放机器，关闭driver进程
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);	
				}else{				
					WebElement error = driver.findElement(By.cssSelector("p.fs_16 + p"));
					String errorMsg = error.getText();
					taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(), 
							BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
							errorMsg, 
							BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(),false, bankJsonBean.getTaskid());
					//释放机器，关闭driver进程
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}
			}else{
				tracerLog.output("登陆成功，存入cookie", "success");
				taskBankStatusService.transforCookie(driver, "accounts.ccb.com", taskBank, null);
				tracerLog.output("crawler.bank.login.creditcard.success", taskBank.getTaskid());
				
//				crawler(bankJsonBean);
			}
		}catch(NoSuchElementException e){
			tracerLog.output("获取错误信息失败", e.getMessage());
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(), 
					BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
					"系统繁忙，请稍后再试！", 
					BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(),false, bankJsonBean.getTaskid());
			//释放机器，关闭driver进程
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
//		if(html.contains("您输入的密码不正确")){
//			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(), 
//					BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
//					BankStatusCode.BANK_LOGIN_PWD_ERROR.getDescription(), 
//					BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(),false, bankJsonBean.getTaskid());
//			//释放机器，关闭driver进程
//			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//		}else if(html.contains("您输入的账户未设置查询密码")){
//			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(), 
//					BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), 
//					BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getDescription(), 
//					BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(),false,bankJsonBean.getTaskid());
//			//释放机器，关闭driver进程
//			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//		}else if(html.contains("请输入正确的附加码")){
//			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(), 
//					BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(), 
//					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(), 
//					BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(),false,bankJsonBean.getTaskid());
//			//释放机器，关闭driver进程
//			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//		}else if(html.contains("请输入附加码,如看不清可以点击图片刷新")){
//			
//		}else if(html.contains("记录或相关数据不存在")){
//			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(), 
//					BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(), 
//					"记录或相关数据不存在.", 
//					BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(),false,bankJsonBean.getTaskid());
//			//释放机器，关闭driver进程
//			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//		}else{
//			tracerLog.output("登陆成功，存入cookie", "success");
//			taskBankStatusService.transforCookie(driver, "accounts.ccb.com", taskBank, null);
//			tracerLog.output("crawler.bank.login.creditcard.success", taskBank.getTaskid());
//			
//			crawler(bankJsonBean);
//		}
		return taskBank;
	}

	/**
	 * @Des 数据爬取
	 * @param bankJsonBean
	 */
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		
		List<String> pageSources = new ArrayList<String>();
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		//获取每个月账单，用循环点击的方式：
		//点击账户明细查询按钮
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement accountSearch = wait.until(new Function<WebDriver, WebElement>() {  
			public WebElement apply(WebDriver driver) {   
			return driver.findElement(By.id("div_E13202"));  
			}  
		}); 
		accountSearch.click();
//		try {
//			Thread.sleep(500l);
//		} catch (InterruptedException e2) {
//			e2.printStackTrace();
//		}
		//切换到对应的月份明细列表
		WebElement result1 = wait.until(new Function<WebDriver, WebElement>() {  
			public WebElement apply(WebDriver driver) {   
			return driver.findElement(By.id("result1"));  
			}  
		});
		driver.switchTo().frame("result1");
		//获取所有月份的明细账单
		List<WebElement> bills = driver.findElements(By.className("bill_search"));
		tracerLog.output("明细账单中的bills长度：", bills.size()+"");
		if(null == bills || bills.size() == 0){
			taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(), 
					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getError_code(),false,bankJsonBean.getTaskid());
			//释放机器，关闭driver进程
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}else{
			
			//获取当前页的句柄
			String handle = driver.getWindowHandle();
			//获取第一页
			bills.get(0).click();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
			pageSources.add(driver.getPageSource());
			driver.switchTo().window(handle);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			for(int i=1;i<bills.size();i++){
				try{
					//点击账户明细查询按钮
					accountSearch = driver.findElement(By.id("div_E13202"));
					accountSearch.click();					
					Thread.sleep(500l);
					//切换到对应的月份明细列表
					driver.switchTo().frame("result1");
					//获取所有月份的明细账单
					bills = driver.findElements(By.className("bill_search"));
					if(null != bills && bills.size()>0){
						//点击当前月份的明细按钮
						bills.get(i).click();
						Thread.sleep(500);
//						tracerLog.output("信用卡第"+i+"个明细：", "<xmp>"+driver.getPageSource()+"</xmp>");
						pageSources.add(driver.getPageSource());
						//切换到列表页
						driver.switchTo().window(handle);
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}					
					}else{
						taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(), 
								BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), 
								"此信用卡没有相关账单数据", 
								BankStatusCode.BANK_CRAWLER_SUCCESS.getError_code(),true,bankJsonBean.getTaskid());
						//释放机器，关闭driver进程
						agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
						break;
					}
					
				}catch(Exception e){			
					tracerLog.output("获取当前详情页出错 "+i+" :", e.getMessage());
					//切换到列表页
					driver.switchTo().window(handle);
					continue;				
				}			
			}
			//释放机器，关闭driver进程
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			tracerLog.output("关闭页面driver", "success");
			if(null != pageSources && pageSources.size()>0){
				WebData data = ccbChinaCreditcardParser.parser(pageSources,bankJsonBean);
				ccbChinaCreditcardTransFlowRepository.saveAll(data.getTransFlows());
				ccbChinaCreditcardAccountTypeRepository.saveAll(data.getAccountTypes());
				taskBankStatusService.updateTaskBankUserinfo(200,"正在爬取中。。用户信息爬取完毕",taskBank.getTaskid());
				taskBankStatusService.updateTaskBankTransflow(200,"",taskBank.getTaskid());
				taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(), 
						BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), 
						BankStatusCode.BANK_CRAWLER_SUCCESS.getDescription(), 
						BankStatusCode.BANK_CRAWLER_SUCCESS.getError_code(),true, 
						bankJsonBean.getTaskid());				
				
			}else{
				taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
						BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(), 
						BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getError_code(),false,bankJsonBean.getTaskid());
			}
		}
		return taskBank;
	}

	/**
	 * 建行信用卡获取登录页面
	 * @param bankJsonBean
	 * @return
	 * @throws Exception 
	 */
	private WebDriver getHtml(BankJsonBean bankJsonBean) throws Exception {
		
		String loginUrl = "http://accounts.ccb.com/tran/WCCMainB1L1?CCB_IBSVersion=V5&SERVLET_NAME=WCCMainB1L1&TXCODE=E3CX00";
		
		//打开建行登录页面
		driver = webDriverChromeService.getPageByChrome(loginUrl);
		
		// Get the location of element on the page 
		WebElement ele = driver.findElement(By.id("result1")); 
		Point point = ele.getLocation(); 
				
		driver.switchTo().frame(ele);
		
//		tracerLog.output("登录页面源码：", "<xmp>"+driver.getPageSource()+"</xmp>");
		//用户名输入框
		WebElement usernameInput = driver.findElement(By.name("ACC_NO_temp"));
		//密码输入框
		WebElement passwordInput = driver.findElement(By.name("LOGPASS"));			
		//图片验证码输入框
		WebElement imgInput = driver.findElement(By.name("PT_CONFIRM_PWD"));
		//点击登录按钮
		WebElement button = driver.findElement(By.className("btn"));
				
		//有数字键盘，改成用本地键盘录入。
		passwordInput.click();
		//使用键盘输入的按钮
		WebElement keyButton = driver.findElement(By.className("keyInfoBtn"));
		keyButton.click();
		//输入用户名
		usernameInput.sendKeys(bankJsonBean.getLoginName());
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//输入密码
		passwordInput.sendKeys(bankJsonBean.getPassword().trim());
		
		String code = null;
		try {
			String path = WebDriverUnit.saveImg(driver,By.id("fujiama"),point.getX(),point.getY());
			code = chaoJiYingOcrService.callChaoJiYingService(path, "1006");
			
			imgInput.sendKeys(code);
			button.click();
			Thread.sleep(2000L); 
			//判断是否需要发送短信验证码
			//等待10秒(每2秒轮训检查一遍)，如果还没有登录成功的标识btnConf1 则表示登录失败
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement errorMain = wait.until(new Function<WebDriver, WebElement>() {  
				public WebElement apply(WebDriver driver) {   
				return driver.findElement(By.id("error_main"));  
				}  
			});  
		} catch (Exception e) {
			tracerLog.output("getHtml.error", e.getMessage());
			return driver;
		}
		
		return driver;
	}



	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
