package app.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
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
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.cmbchina.CmbChinaDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.cmbchina.CmbChinaDebitCardUserInfo;
import com.microservice.dao.repository.crawler.bank.cmbchina.CmbChinaDebitCardTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.cmbchina.CmbChinaDebitCardUserInfoRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.bank.cmbchina"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.bank.cmbchina"})
public class CmbChinaService extends  WebDriverIEService implements ICrawlerLogin, ISms{
	
	@Autowired 
	private CmbChinaDebitCardTransFlowRepository cmbChinaDebitCardTransFlowRepository;
	
	@Autowired 
	private CmbChinaDebitCardUserInfoRepository cmbChinaDebitCardUserInfoRepository;
	
	
	@Autowired 
	private TracerLog tracerLog;
	
	@Autowired
	private ParserService parserService;
	
	@Autowired
	private TaskBankStatusService taskBankStatusService; 
	
	@Autowired
	private WebDriverIEService webDriverIEService;
	
	@Autowired
	private AgentService agentService;
	
	private WebDriver driver;

	static String GenIndex = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenIndex.aspx";

	static String genLoginVerifyM2 = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenLoginVerifyM2.aspx";
	
	static String LoginPage = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/Login.aspx";
	 
	private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
	
	/**
	 * @Des 输入验证码,开始短信验证
	 * @param  BankJsonBean
	 */
	@Async
	@Override
	public TaskBank verifySms(BankJsonBean bankJsonBean) {
		String webdriverHandle = bankJsonBean.getWebdriverHandle();//获取登录步骤的webdriverHandle 
		tracerLog.addTag("当前的 webdriverHandle：",driver.getWindowHandle()); 
		tracerLog.addTag("Task表中的 webdriverHandle：",webdriverHandle); 
		TaskBank taskBank = null;
		try {
			//判断数据库中的WindowHandle和当前游览器的WindowHandle是否一致，此判断非常重要！
			//登录后，需要短信验证码或爬取，需要基于登录环节的seleunim WindowHandle 继续下去，如果WindowHandle不匹配。则无法连贯继续下去。例如登录在机器A，爬取在机器B，
			if(webdriverHandle==null||!webdriverHandle.equals(driver.getWindowHandle())){
				tracerLog.addTag("RuntimeException","当前的webdriverHandle 和 数据库中的 webdriverHandle 不匹配");
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WEBDRIVER_ERROR.getPhase(), 
						BankStatusCode.BANK_WEBDRIVER_ERROR.getPhasestatus(), 
						BankStatusCode.BANK_WEBDRIVER_ERROR.getDescription(), 
						BankStatusCode.BANK_WEBDRIVER_ERROR.getError_code(),false,bankJsonBean.getTaskid());  
				return taskBank;
			}else{
				tracerLog.addTag("sendSMS","开始验证短信验证码"+bankJsonBean.toString());
				//短信验证码的input 输入框
				WebElement textSendCode = null;
				try {
					textSendCode = driver.findElement(By.id("txtSendCode"));  
				} catch (NoSuchElementException e) {
					String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracerLog.addTag("#textSendCode为空，未知的错误", "截图:" + path+" 当前页面URL:" + driver.getCurrentUrl()); 
				} 
				if(textSendCode==null){
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(), 
							BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(), 
							BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(), 
							BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(),false,bankJsonBean.getTaskid());  
					return taskBank;
				}else{
					textSendCode.clear();
					textSendCode.sendKeys(bankJsonBean.getVerification());
					Thread.sleep(500L);
					driver.findElement(By.id("btnVerifyCode")).click();
					Thread.sleep(1000L); 
					Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(6, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);  
					WebElement mainWorkArea = null;
					try {
						mainWorkArea = wait.until(new Function<WebDriver, WebElement>() {  
				             public WebElement apply(WebDriver driver) {   
				            	return driver.findElement(By.id("mainWorkArea")); //这是不需要短信验证码，直接进入主页面的div的 ID 
				             }  
						});  
			     	} catch (Exception e) {
			     		//详单主页面未出现    截图 
						String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
						tracerLog.addTag("#mainWorkArea 元素等待10秒未出现,异常:"+e.toString()+" ", "截图:" + path+" 当前页面URL:" + driver.getCurrentUrl()); 
						 
						//判断是否还停留在短信验证页面
						WebElement controlExplain = driver.findElement(By.className("control-explain"));
						if(controlExplain==null){ 
							tracerLog.addTag("#control-explain为空，未知的错误", "截图:" + path+" 当前页面URL:" + driver.getCurrentUrl()); 
							taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(), 
									BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(), 
									BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(), 
									BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(),false,bankJsonBean.getTaskid());  
							return taskBank;
						}else{
							//短信验证错误
							String errorfinfo = controlExplain.getText();//errorfinfo 一般是: "验证码校验失败，原因：请输入正确的验证码或重新申请验证码。ErrCode:02"
							taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(), 
									BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(), 
									errorfinfo, 
									BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(),false,bankJsonBean.getTaskid());  
							return taskBank;
						} 
			  		}
					
					if(mainWorkArea!=null){ 
						String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
						tracerLog.addTag("#mainWorkArea 元素不为空，可以直接采集数据", "截图:" + path+" 当前页面URL:" + driver.getCurrentUrl()); 
						taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(), 
								BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(), 
								BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getDescription(), 
								BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getError_code(),false,bankJsonBean.getTaskid());  
						return taskBank; 
					}else{
						String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
						tracerLog.addTag("#mainWorkArea为空，未知的错误", "截图:" + path+" 当前页面URL:" + driver.getCurrentUrl()); 
						taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(), 
								BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(), 
								BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(), 
								BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(),false,bankJsonBean.getTaskid());  
						return taskBank; 
					} 
				} 
			} 
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CmbChinaService.verifySms---ERROR:", e.toString());
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(), false, bankJsonBean.getTaskid());
		}
		return taskBank;
	}
	
	/**
	 * @Des 打开招行登录页面
	 * @param  无
	 */
	public WebDriver openloginCmbChina(){ 
		driver = webDriverIEService.getNewWebDriver();
		System.out.println("WebDriverIEService loginCmbChina Msg 开始登陆招商银行登陆页");
		tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "开始登陆招商银行登陆页"); 
		try{
			driver = getPage(driver,LoginPage);
		}catch(NoSuchWindowException e){ 
			System.out.println("打开招行登录页面报错，尝试重新初始化游览器"+e.getMessage());
			tracerLog.addTag("打开招行登录页面报错，尝试重新初始化游览器", e.getMessage()); 
			driver = getPage(driver,LoginPage);
		} 
		tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "招商银行登陆页加载已完成,当前页面句柄"+driver.getWindowHandle());
		return driver;
	} 
 
	/**
	 * @Des 发送短信验证码
	 * @param  BankJsonBean
	 * @return TaskBank
	 */
	@Async
	@Override
	public TaskBank sendSms(BankJsonBean bankJsonBean) {
		String webdriverHandle = bankJsonBean.getWebdriverHandle();// 获取登录步骤的webdriverHandle
		tracerLog.addTag("当前的 webdriverHandle：", driver.getWindowHandle());
		tracerLog.addTag("Task表中的 webdriverHandle：", webdriverHandle);
		TaskBank taskBank = null;
		try {
			// 判断数据库中的WindowHandle和当前游览器的WindowHandle是否一致，此判断非常重要！
			// 登录后，需要短信验证码或爬取，需要基于登录环节的seleunim WindowHandle
			// 继续下去，如果WindowHandle不匹配。则无法连贯继续下去。例如登录在机器A，爬取在机器B，
			if (webdriverHandle == null || !webdriverHandle.equals(driver.getWindowHandle())) {
				tracerLog.addTag("RuntimeException", "当前的webdriverHandle 和 数据库中的 webdriverHandle 不匹配");
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WEBDRIVER_ERROR.getPhase(),
						BankStatusCode.BANK_WEBDRIVER_ERROR.getPhasestatus(),
						BankStatusCode.BANK_WEBDRIVER_ERROR.getDescription(),
						BankStatusCode.BANK_WEBDRIVER_ERROR.getError_code(), false, bankJsonBean.getTaskid());
				return taskBank;
			} else {
				tracerLog.addTag("sendSMS", "开始发送短信验证码" + bankJsonBean.toString());
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(6, TimeUnit.SECONDS)
						.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
				WebElement ele1 = wait.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.id("btnSendCode"));
					}
				});
				ele1.click();
				// 短信验证码已发送，请注意查收,等一秒钟在改状态，从页面点击到真发出短信有一定延迟
				Thread.sleep(1000L);

				String inputValue = ele1.getAttribute("value");
				tracerLog.addTag("inputValue", inputValue);
				/*
				 * if(inputValue.contains("")){
				 * 
				 * }
				 */
				String mobile = "";
				WebElement main = null;
				try {
					main = wait.until(new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driver) {
							return driver.findElement(By.id("lbMobileNo"));
						}
					});
				} catch (Exception e) {
					tracerLog.addTag("短信验证码发送手机号码未找到", "MSG:" + e.getMessage());
					e.printStackTrace();
				}
				if (main != null) {
					mobile = main.getText();
				}
				tracerLog.addTag(bankJsonBean.getTaskid() + "短信验证码发送手机号码mobile", mobile);

				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhase(),
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhasestatus(),
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getDescription() + "预留手机号[" + mobile + "]",
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getError_code(), false, bankJsonBean.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CmbChinaService.sendSms---ERROR:", e.toString());
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_ERROR.getPhase(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getPhasestatus(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getDescription(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getError_code(), false, bankJsonBean.getTaskid());
		}

		return taskBank;
	}
	
	
	/**
	 * @Des 开始爬取用户信息和账号流水信息（异步）
	 * @param bankJsonBean
	 */
	@Async
	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		
		String webdriverHandle = bankJsonBean.getWebdriverHandle();//获取登录步骤的webdriverHandle 
		tracerLog.addTag("当前的 webdriverHandle：",driver.getWindowHandle()); 
		tracerLog.addTag("Task表中的 webdriverHandle：",webdriverHandle); 
		TaskBank taskBank = null;
		try {
			//判断数据库中的WindowHandle和当前游览器的WindowHandle是否一致，此判断非常重要！
			//登录后，需要短信验证码或爬取，需要基于登录环节的seleunim WindowHandle 继续下去，如果WindowHandle不匹配。则无法连贯继续下去。例如登录在机器A，爬取在机器B，
			if(webdriverHandle==null||!webdriverHandle.equals(driver.getWindowHandle())){
				tracerLog.addTag("RuntimeException","当前的webdriverHandle 和 数据库中的 webdriverHandle 不匹配");
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WEBDRIVER_ERROR.getPhase(), 
						BankStatusCode.BANK_WEBDRIVER_ERROR.getPhasestatus(), 
						BankStatusCode.BANK_WEBDRIVER_ERROR.getDescription(), 
						BankStatusCode.BANK_WEBDRIVER_ERROR.getError_code(),true,bankJsonBean.getTaskid()); 
				//释放instance ip ，quit webdriver
				tracerLog.addTag("释放instance ip ，quit webdriver",taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				return taskBank;
				//throw new RuntimeException("当前的webdriverHandle 和 数据库中的 webdriverHandle 不匹配");
			} else{
				tracerLog.addTag("WindowHandle 匹配，获取数据开始","getDateCombo()"); 
				
				boolean cardType = false;
				String CardsInfoForType = driver.findElement(By.id("CardsInfoForType")).getAttribute("value");
				JSONObject jsonObj = JSONObject.fromObject(CardsInfoForType);
				if(jsonObj.has("A")){
					String type = jsonObj.getString("A");
					if("true".equals(type)){
						cardType = true;
					}
				}
				if (!cardType) {
					tracerLog.addTag(bankJsonBean.getTaskid() + "cardType", "卡类型有误"+CardsInfoForType);
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(),
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(),
							"对不起，您没有绑定一卡通，请登录官网绑定！",
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getError_code(), true, bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

					return taskBank;
				}
				
				

				JavascriptExecutor driver_js= ((JavascriptExecutor) driver); 
				Thread.sleep(1500L);
				driver_js.executeScript("CallFuncEx2('A','CBANK_DEBITCARD_ACCOUNTMANAGER','AccountQuery/am_QuerySubAccount.aspx','FORM',null)");
				Thread.sleep(1000L);
				driver.switchTo().frame("mainWorkArea");    
				String currentUrl3 = driver.getCurrentUrl();
				tracerLog.addTag("当前页面executeScript",currentUrl3); 
				//WebDriverWait wait = new WebDriverWait(driver, 5);
				//wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[onclick]")));
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);  
				WebElement eleTriggerFunc = wait.until(new Function<WebDriver, WebElement>() {  
		             public WebElement apply(WebDriver driver) {  
		               return driver.findElement(By.cssSelector("a[onclick]")); 
		             }  
				});    //需要等待
				String onclick = eleTriggerFunc.getAttribute("onclick");
				tracerLog.addTag("获取按钮",onclick); 
				Thread.sleep(1000L);
				long starttime = System.currentTimeMillis();
				try{ 
					driver_js.executeScript(onclick); 
					
				} catch (Exception e) {
					e.printStackTrace();
					String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracerLog.addTag("执行JavaScript错误，（请查看弹出页面是否被拦截）", "截图:" + path+" 当前页面URL:" + driver.getCurrentUrl()); 
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
							501,true,bankJsonBean.getTaskid()); 
					return taskBank;
				} 
				long endtime = System.currentTimeMillis();
				tracerLog.addTag("Javascript执行完毕",(endtime-starttime)+":ms");
				Thread.sleep(1000L);
				
				
				String dataHtml = "";
				String ClientNo = "";
				String currentWindow = driver.getWindowHandle(); 
				tracerLog.addTag("采集数据日志收集currentWindow",currentWindow);
		        Set<String> handles = driver.getWindowHandles(); 
		        tracerLog.addTag("采集数据日志收集handles总数："+handles.size(),handles.toString());
		        Iterator<String> it = handles.iterator();
		        while(it.hasNext()){
		            String handle = it.next(); 
		            if(currentWindow.equals(handle)) continue;
		            WebDriver window = driver.switchTo().window(handle);
		            //tracerLog.addTag("日志收集1：","title,url = "+window.getTitle()+","+window.getCurrentUrl());
		            //tracerLog.addTag("日志收集1.1：","<xmp>"+window.getPageSource()+"</xmp>");  
		            
		            ClientNo = Jsoup.parse(window.getPageSource()).select("#ClientNo").val();
		            
		            if("https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryTodayTrans.aspx".equals(window.getCurrentUrl())){
		           	JavascriptExecutor driver_window= ((JavascriptExecutor) window); 
		        		tracerLog.addTag("日志收集2：","triggerFunc driver_window JavaScript");
		        		driver_window.executeScript("triggerFunc('../AccountQuery/am_QueryHistoryTrans.aspx','FORM','_self')");
		        		Thread.sleep(2000L);
		        		String currentUrl4 = driver.getCurrentUrl();
			       		tracerLog.addTag("日志收集3：",currentUrl4); 
			       		String html = driver.getPageSource(); 
			       		//tracerLog.addTag("日志收集4：","<xmp>"+html+"</xmp>");  
			       		dataHtml = html;
		            }
		        }  
		       
		        String __VIEWSTATE = Jsoup.parse(dataHtml).select("#__VIEWSTATE").val();
		        String __EVENTVALIDATION = Jsoup.parse(dataHtml).select("#__EVENTVALIDATION").val();
		        
		        if(ClientNo==null||"".equals(ClientNo)){
		        	tracerLog.addTag("RuntimeException","关键参数 1 ClientNo 为空，无法获取数据");
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
							502,true,bankJsonBean.getTaskid()); 
					//释放instance ip ，quit webdriver
					tracerLog.addTag("释放instance ip ，quit webdriver",taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					return taskBank;
		        	//throw new RuntimeException("关键参数 1 ClientNo 为空，无法获取数据");
		        }
		        if(__VIEWSTATE==null||"".equals(__VIEWSTATE)){
		        	tracerLog.addTag("RuntimeException","关键参数 2 __VIEWSTATE 为空，无法获取数据");
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
							503,true,bankJsonBean.getTaskid()); 
					//释放instance ip ，quit webdriver 
					tracerLog.addTag("释放instance ip ，quit webdriver",taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					return taskBank;
		        	//throw new RuntimeException("关键参数 2 __VIEWSTATE 为空，无法获取数据");
		        }
		        if(__EVENTVALIDATION==null||"".equals(__EVENTVALIDATION)){
		        	tracerLog.addTag("RuntimeException","关键参数 3 __EVENTVALIDATION 为空，无法获取数据");
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
							504,true,bankJsonBean.getTaskid()); 
					//释放instance ip ，quit webdriver
					tracerLog.addTag("释放instance ip ，quit webdriver",taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					return taskBank;
		        	//throw new RuntimeException("关键参数 3 __EVENTVALIDATION 为空，无法获取数据");
		        }
		        
		        tracerLog.addTag("关键参数:ClientNo",ClientNo);
		        tracerLog.addTag("关键参数:__VIEWSTATE",__VIEWSTATE);
		        tracerLog.addTag("关键参数:__EVENTVALIDATION",__EVENTVALIDATION);
		        
		        String userinfohtml = getUserInfo(ClientNo);
		        if(userinfohtml.indexOf("无效html请求")==-1){
		        	tracerLog.addTag("开户信息html","<xmp>"+userinfohtml+"</xmp>"); 
		        	List<CmbChinaDebitCardUserInfo> transFlows = parserService.parserUserinfo(userinfohtml, bankJsonBean.getTaskid()); 
		        	cmbChinaDebitCardUserInfoRepository.saveAll(transFlows);
		        	int size = transFlows.size();
					if(size==0){
						//采集完成，但并没有数据
						taskBank = taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR.getError_code(), BankStatusCode.BANK_USERINFO_ERROR.getDescription(), bankJsonBean.getTaskid());
					}else{
						taskBank = taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_SUCCESS.getError_code(), BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), bankJsonBean.getTaskid());
					}  
					tracerLog.addTag("流水采集完成taskBank",taskBank.toString());  
		        }else{
		        	taskBank = taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR2.getError_code(), BankStatusCode.BANK_USERINFO_ERROR2.getDescription(), bankJsonBean.getTaskid());
		        	tracerLog.addTag("开户信息html获取失败",taskBank.toString());  
		        	tracerLog.addTag("开户信息html获取失败","<xmp>"+userinfohtml+"</xmp>");  
		        }
		        
		        
				String html = getData(ClientNo,__VIEWSTATE,__EVENTVALIDATION); 
				
				if(html.indexOf("无效html请求")==-1){
					tracerLog.addTag("流水原始html：","<xmp>"+html+"</xmp>");
					List<CmbChinaDebitCardTransFlow> transFlows = parserService.parserTransFlow(html, bankJsonBean.getTaskid()); 		
					List<CmbChinaDebitCardTransFlow> flows = cmbChinaDebitCardTransFlowRepository.saveAll(transFlows);
					int size = flows.size();
					if(size==0){
						taskBank = taskBankStatusService.updateTaskBankTransflow( BankStatusCode.BANK_TRANSFLOW_ERROR.getError_code(), BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), bankJsonBean.getTaskid());
					}else{
						taskBank = taskBankStatusService.updateTaskBankTransflow( BankStatusCode.BANK_TRANSFLOW_SUCCESS.getError_code(), BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
					}  
					tracerLog.addTag("流水采集完成taskBank",taskBank.toString());  
					//释放instance ip ，quit webdriver
					//tracerLog.addTag("释放instance ip ，quit webdriver",taskBank.getCrawlerHost());
					//agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					//return taskBank;
					
				}else{ 
					tracerLog.addTag("流水原始html获取失败","<xmp>"+html+"</xmp>"); 
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_TRANSFLOW_ERROR2.getPhase(), 
							BankStatusCode.BANK_TRANSFLOW_ERROR2.getPhasestatus(), 
							BankStatusCode.BANK_TRANSFLOW_ERROR2.getDescription(), 
							BankStatusCode.BANK_TRANSFLOW_ERROR2.getError_code(),true,bankJsonBean.getTaskid()); 
					//释放instance ip ，quit webdriver
					//tracerLog.addTag("释放instance ip ，quit webdriver",taskBank.getCrawlerHost());
					//agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}   
			} 
			//task 状态修改 finish = ture
			taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CmbChinaService.getDateCombo---ERROR:", e.toString());
			taskBankStatusService.updateTaskBankUserinfo(404, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
					bankJsonBean.getTaskid());
			taskBankStatusService.updateTaskBankTransflow(404, BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(),
					bankJsonBean.getTaskid());
			taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());
		}
		//释放instance ip ，quit webdriver
		tracerLog.addTag("释放instance ip ，quit webdriver",taskBank.getCrawlerHost());
		agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		
		return taskBank;
	}
	
	/**
	 * @Des 招商银行通过银行卡号登录
	 * @param bankJsonBean
	 */
	@Async
	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		tracerLog.addTag("loginCombo", "开始登陆招商银行" + bankJsonBean.getLoginName());
		TaskBank taskBank = null;
		try {
			// 打开IE游览器，访问招行的登录页面
			WebDriver webDriver = openloginCmbChina();

			String windowHandle = webDriver.getWindowHandle();
			bankJsonBean.setWebdriverHandle(windowHandle);
			tracerLog.addTag("登录招商银行，打开网页获取网页handler", windowHandle);
			
			driver.findElement(By.id("goPassWordLogin")).click();

			tracerLog.addTag("开始输入卡号", bankJsonBean.getLoginName());
			// 键盘输入卡号
			Thread.sleep(500L);
			VK.KeyPress(bankJsonBean.getLoginName());
			
			tracerLog.addTag("开始输入Tab", "Tab");
			// 键盘输入Tab，让游览器焦点切换到密码框
			Thread.sleep(500L);
			VK.Tab();

			tracerLog.addTag("开始输入密码", bankJsonBean.getPassword());
			// 键盘输入查询密码
			Thread.sleep(500L);
			VK.KeyPress(bankJsonBean.getPassword());

			tracerLog.addTag("开始点击登陆按钮", "#LoginBtn");
			// 点击游览器的登录按钮
			Thread.sleep(500L);
			webDriver.findElement(By.id("LoginBtn")).click();

			// 等待10秒(每2秒轮训检查一遍)，如果还没有登录成功的标识form#HomePage(无须短信验证) 则表示登录失败
			Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement btnVerifyCode = null;
			WebElement mainWorkArea = null;
			// 截图
			String path = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
			try {
				btnVerifyCode = wait.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.id("btnVerifyCode")); // 这是不需要短信验证码，直接进入主页面的div的
																			// ID
					}
				});

			} catch (Exception e) {
				tracerLog.addTag("#btnVerifyCode 元素等待10秒未出现,异常:" + e.toString() + " 开始判断是否不需要短信",
						"截图:" + path + " 当前页面URL:" + webDriver.getCurrentUrl());
			}
			// 开始判断是否不需要短信
			try {
				mainWorkArea = driver.findElement(By.id("mainWorkArea"));
			} catch (Exception e) {
				tracerLog.addTag("#mainWorkArea未出现,异常:" + e.toString() + " ",
						"截图:" + path + " 当前页面URL:" + webDriver.getCurrentUrl());
			}

			String currentPageURL = webDriver.getCurrentUrl();
			tracerLog.addTag("当前页面URL", currentPageURL);
			if (mainWorkArea == null && btnVerifyCode == null) {
				tracerLog.addTag("登录失败", currentPageURL);
				// 截图
				String pathE = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
				
				// 如果当前页面还是登陆页面，说明登陆不成功，从页面中获取登陆不成功的原因（密码错误、密码简单、。。。等等）
				if (LoginPage.equals(currentPageURL)) {
					tracerLog.addTag("登录失败.当前页面任是登录页面", currentPageURL);
					String errorfinfo = webDriver.findElement(By.id("errMsgSpan")).getText();
					if (errorfinfo != null && errorfinfo.length() > 0) {
						tracerLog.addTag("登录失败,错误信息", errorfinfo);
						taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
								BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
								BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), errorfinfo,
								BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
								windowHandle);
						tracerLog.addTag("登录失败", "登录失败截图:" + pathE);
						// 登录错误，释放资源
						// 释放instance ip ，quit webdriver
						tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
						agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

					} else {
						tracerLog.addTag("登录失败，且没有登录失败错误信息", "截图:" + path + " 当前页面URL:" + webDriver.getCurrentUrl());
						taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
								BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
								BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "登录失败，未知的错误",
								BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
								windowHandle);
						tracerLog.addTag("登录失败", "登录失败截图:" + pathE);
						// 释放instance ip ，quit webdriver
						tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
						agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					}
				}
			} else {
				tracerLog.addTag("登录成功", currentPageURL);
				tracerLog.addTag("登录成功 ，登录招商银行，登录成功网页handler", webDriver.getWindowHandle());
				// 截图
				String pathS = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
				
				if (currentPageURL.equals(GenIndex)) {
					tracerLog.addTag("logincmbchina", "GenIndex 无需进行短信，直接开始获取数据");
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid(),
							windowHandle);

					// cmbChinaService.getDateCombo();
					tracerLog.addTag("登录成功", "登录成功:" + currentPageURL);
					tracerLog.addTag("登录成功", "登录成功截图:" + pathS);

				} else if (currentPageURL.equals(genLoginVerifyM2)) {
					tracerLog.addTag("logincmbchina", "GenLoginVerifyM2 需进行短信，开始发送短信验证码");
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(), false, bankJsonBean.getTaskid(),
							windowHandle);

					// cmbChinaService.sendSMS();
					tracerLog.addTag("登录成功", "登录成功:" + currentPageURL);
					tracerLog.addTag("登录成功", "登录成功截图:" + pathS);

				} else {
					// TODO 设个地方需要用seleunim 截图并保持url和html，可能是未处理的情况（账号登录成功后，例如
					// 还未绑定银行卡、还未设置安全提问、还未认证等等）
					tracerLog.addTag("登录遇到了未知的链接", "未知的链接" + currentPageURL);
					tracerLog.addTag("登录遇到了未知的链接图片截图", pathS);
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "登录失败，请稍后再试！",
							BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(), "");
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "登录失败，请稍后再试！",
					BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(), "");
			tracerLog.addTag("登录遇到了未知报错", "MSG:" + e.getMessage());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
		return taskBank;

	}
	
	//https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryAccountInfo.aspx?ClientNo=4E73E482947E02B97112E11106658D03855697929254691900003044
	
	/**
	 * @Des seleunim已经获取到了爬取流水请求的关键性三个参数，接下来爬取流水就可以使用htmlunit来完成了(爬取开户信息)
	 * @param ClientNo
	 * @param __VIEWSTATE
	 * @param __EVENTVALIDATION
	 */ 
	public String getUserInfo(String ClientNo) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		String url = "https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryAccountInfo.aspx"; 
		tracerLog.addTag("进入开户信息数据",url); 
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);  
		requestSettings.setAdditionalHeader("Host", "pbsz.ebank.cmbchina.com");
		requestSettings.setAdditionalHeader("Referer", "https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryHistoryTrans.aspx");
		requestSettings.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
		requestSettings.setAdditionalHeader("DNT", "1"); 
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>()); 
		requestSettings.getRequestParameters().add(new NameValuePair("ClientNo", ClientNo)); 
		Page page = webClient.getPage(requestSettings);  
		String html = page.getWebResponse().getContentAsString();   
		return html;
	}
	
	
	/**
	 * @Des seleunim已经获取到了爬取流水请求的关键性三个参数，接下来爬取流水就可以使用htmlunit来完成了
	 * @param ClientNo
	 * @param __VIEWSTATE
	 * @param __EVENTVALIDATION
	 */ 
	public String getData(String ClientNo,String __VIEWSTATE,String __EVENTVALIDATION) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		String url = "https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryHistoryTrans.aspx"; 
		tracerLog.addTag("进入获取流水数据",url); 
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);  
		requestSettings.setAdditionalHeader("Host", "pbsz.ebank.cmbchina.com");
		requestSettings.setAdditionalHeader("Referer", "https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryHistoryTrans.aspx");
		requestSettings.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
		requestSettings.setAdditionalHeader("DNT", "1");
		
		//webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","WEBTRENDS_ID", "123.126.87.162-2358226944.30605561"));
		String endDate = df.format(new Date());
		String beginDate = "20000101";
		tracerLog.addTag("开始时间",beginDate);
		tracerLog.addTag("截止时间",endDate); 
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("__VIEWSTATE", __VIEWSTATE));
		requestSettings.getRequestParameters().add(new NameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));
		requestSettings.getRequestParameters().add(new NameValuePair("BeginDate", beginDate));
		requestSettings.getRequestParameters().add(new NameValuePair("EndDate", endDate));
		requestSettings.getRequestParameters().add(new NameValuePair("BtnOK", "查 询"));
		requestSettings.getRequestParameters().add(new NameValuePair("ClientNo", ClientNo));
		
		Page page = webClient.getPage(requestSettings);  
		String html = page.getWebResponse().getContentAsString();   
		return html;
	}
	
	/**
	 * @Des 系统退出，释放资源
	 * @param BankJsonBean 
	 */ 
	public TaskBank quit(BankJsonBean bankJsonBean){ 
		tracerLog.addTag("quit",bankJsonBean.toString()); 
		//关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试  , description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
		TaskBank taskBank = taskBankStatusService.systemClose(true,bankJsonBean.getTaskid());  
		//调用公用释放资源方法
		if(taskBank!=null){
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else{
			tracerLog.addTag("quit taskBank is null",""); 
		}
		return taskBank;
	} 

	/**
	 * @Des 返回当前webdriver的handle ID（测试用）
	 */ 
	public String getCurrentHandle(){
		if(driver==null){
			return null;
		}else{
			String currentHandle = driver.getWindowHandle(); 
			return currentHandle; 
		}
		
	}
	
	/**
	 * @Des 返回当前webdriver所有的handle ID（测试用）
	 */ 
	public Set<String> getHandles(){
		if(driver==null){
			return null;
		}else{
			Set<String> handles = driver.getWindowHandles(); 
			return handles;
		}
		 
	}

	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
