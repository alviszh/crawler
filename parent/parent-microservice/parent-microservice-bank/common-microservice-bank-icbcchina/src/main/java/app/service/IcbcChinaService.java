package app.service;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.icbcchina.IcbcChinaDebitCardHtml;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.ddxoft.VK;
import com.module.jna.webdriver.WebDriverUnit;

import app.common.AccNumList;
import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.IcbcChinaParser;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;
import app.service.aop.impl.CrawlerImpl;


@Component
@EnableAsync
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic", "com.microservice.dao.entity.crawler.bank.icbcchina"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic", "com.microservice.dao.repository.crawler.bank.icbcchina"})
public class IcbcChinaService implements ICrawlerLogin, ISms{
	
	private WebDriver driver;
	
	@Autowired
	private AgentService agentService;
	@Autowired
	private WebDriverIEService webDriverIEService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private IcbcChinaParser icbcChinaParser;
	@Autowired
	private GetDataService getDataService;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private IcbcChinaLoginService icbcChinaLoginService;
	@Autowired
	private CrawlerImpl crawlerImpl;
	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 工行登录
	 * @param bankJsonBean
	 * @author 王培阳
	 * @throws Exception 
	 */
	@Override
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracer.addTag("crawler.bank.login.start", taskBank.getTaskid());
		try {
			driver = icbcChinaLoginService.retryLogin(bankJsonBean);
			if(driver == null){
				tracer.addTag("crawler.bank.login.driverisNull", "123");
				//登录过程中出现异常
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(),BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
			}
			String windowHandle = driver.getWindowHandle();
			bankJsonBean.setWebdriverHandle(windowHandle);
			tracer.addTag("登录工商银行，打开网页获取网页handler",windowHandle);
			
			String currentUrl = driver.getCurrentUrl();
			List<WebElement> eles = driver.findElements(By.xpath("//span[starts-with(@id, 'ebdp-pc4promote-menu-level1-text-')]"));
//			tracer.addTag("crawler.bank.login.sms.eles", eles.get(0).toString());
			if(null != eles && eles.size() > 0){			//需要短信验证&& eles.toString().contains("短信认证")
				//将状态改为需要短信验证码
				taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(),BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(),BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(), false, bankJsonBean.getTaskid(), windowHandle);
				tracer.addTag("crawler.bank.login.sms.yes", "登陆成功，需要发送短信验证");
				
				//截图 
				String shotPath = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracer.addTag("需要短信认证Screenshot", "截图:" + shotPath);
				Thread.sleep(1000);
				//调取发送短信
//				getSMSCode(bankJsonBean);
				return taskBank;
			}else if(currentUrl.contains("https://mybank.icbc.com.cn/icbc/newperbank/perbank3/frame/frame_index.jsp")){
				/*//为防止获取到的driver.getPagesource()为空白页。加入wait方法
				WebDriverWait wait = new WebDriverWait(driver, 20);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("input")));*/
				Thread.sleep(3000);
				String errorText = icbcChinaParser.isLogin(driver.getPageSource());
				if(null != errorText && errorText.length() > 0){			//登陆失败，errorText中为失败提示信息
					tracer.addTag("crawler.bank.login.fail", errorText);
					if(errorText.contains("密码错误次数超限,请次日再试或点击")){
						errorText = errorText.substring(0, 19);
					}else if(errorText.contains("验证码输入错误或已超时失效")){
						errorText = "系统超时，请稍后再试";
					}
					taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(),BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
							errorText,BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(), true, bankJsonBean.getTaskid(), windowHandle);
					//退出
					quitDriver(bankJsonBean);
				}else{														//登陆成功
					//截图 
					String shotPath = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracer.addTag("登陆成功Screenshot", "截图:" + shotPath);
					tracer.addTag("crawler.bank.login.success", "登陆成功");
					//将状态改为不需要短信验证码
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid(), windowHandle);
					tracer.addTag("crawler.bank.login.sms.no", "登陆成功，不需要短信验证");
					String sessionId = icbcChinaParser.getSessionId(driver.getPageSource());
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					taskBank.setParam(sessionId);
					taskBankRepository.save(taskBank);
					Thread.sleep(1000);
					//判断卡的类型（储蓄卡，信用卡）调用不同的爬取方法
					/*if(bankJsonBean.getCardType().contains("DEBIT_CARD")){
						//开始爬取
						getData(bankJsonBean);
					}else if(bankJsonBean.getCardType().contains("CREDIT_CARD")){
						getCreditData(bankJsonBean);
					}*/
					return taskBank;
				}
			}else if(driver.getCurrentUrl().contains("frame_index_no_credible.jsp") || driver.getPageSource().contains("您预留的验证信息为空")){
				// 模拟Esc按键
//				jNativeService.InputEsc();
				tracer.addTag("crawler.bank.login.fail.验证信息为空", "短信验证成功，但是，您预留的验证信息为空。");
				tracer.addTag("crawler.bank.login.fail.验证信息为空.page", "<xmp>"+driver.getPageSource()+"</xmp>");
				taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR2.getPhase(),BankStatusCode.BANK_LOGIN_ERROR2.getPhasestatus(),
						"尊敬的用户，您预留的验证信息为空，请您自行到工商银行官网完整您的信息！",BankStatusCode.BANK_LOGIN_ERROR2.getError_code(), true, bankJsonBean.getTaskid(), windowHandle);
				//截图 
				String shotPath = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracer.addTag("预留的验证信息为空Screenshot", "截图:" + shotPath);
				//退出
				quitDriver(bankJsonBean);
			}else if(currentUrl.contains("https://mybank.icbc.com.cn/icbc/newperbank/perbank3/frame/frame_guide.jsp")){
				//进入到提示页面，将其关闭后自动跳到登陆成功主页
				tracer.addTag("crawler.bank.login.guidePage", driver.getCurrentUrl());
				Thread.sleep(3000);
				//通过执行js来关闭guide页面
				String js = "document.form.submit();";
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript(js);
				Thread.sleep(1500);
				String errorText = icbcChinaParser.isLogin(driver.getPageSource());
				if(null != errorText && errorText.length() > 0){			//登陆失败，errorText中为失败提示信息
					tracer.addTag("crawler.bank.login.fail", errorText);
					taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(),BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
							errorText,BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(), false, bankJsonBean.getTaskid(), windowHandle);
					//退出
					quitDriver(bankJsonBean);
				}else{														//登陆成功
					tracer.addTag("crawler.bank.login.success", "登陆成功");
					//将状态改为不需要短信验证码
					taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid(), windowHandle);
					tracer.addTag("crawler.bank.login.sms.no", "登陆成功，不需要短信验证");
					String sessionId = icbcChinaParser.getSessionId(driver.getPageSource());
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					taskBank.setParam(sessionId);
					taskBankRepository.save(taskBank);
					Thread.sleep(1000);
					//判断卡的类型（储蓄卡，信用卡）调用不同的爬取方法
					if(bankJsonBean.getCardType().contains("DEBIT_CARD")){
						//开始爬取
						getData(bankJsonBean);
					}else if(bankJsonBean.getCardType().contains("CREDIT_CARD")){
						getCreditData(bankJsonBean);
					}
				}
			}else {
				//截图 
				String shotPath = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracer.addTag("登录到未知页面Screenshot", "截图:" + shotPath);
				
				tracer.addTag("crawler.bank.login.unknownError", "登录到未知页面");
				IcbcChinaDebitCardHtml debitCardHtml = new IcbcChinaDebitCardHtml();
				debitCardHtml.setUrl(driver.getCurrentUrl());
				debitCardHtml.setPagenumber(1);
				debitCardHtml.setType("unknownError");
				debitCardHtml.setHtml(driver.getPageSource());
				debitCardHtml.setTaskid(bankJsonBean.getTaskid());
				tracer.addTag("crawler.bank.login.page.unknownError", "<xmp>"+driver.getPageSource()+"</xmp>");
				
				taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR2.getPhase(),BankStatusCode.BANK_LOGIN_ERROR2.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_ERROR2.getDescription(),BankStatusCode.BANK_LOGIN_ERROR2.getError_code(), true, bankJsonBean.getTaskid(), windowHandle);
				//退出
				quitDriver(bankJsonBean);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			tracer.addTag("crawler.bank.login.RuntimeException", e.toString());
			//登录过程中验证码错误
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
					"系统超时，请稍后再试",BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
			//退出
			quitDriver(bankJsonBean);
		}catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.bank.login.Exception", e.toString());
			//登录过程中出现异常
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(),BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
			//退出
			quitDriver(bankJsonBean);
		}
		return null;
	}

	public void getData(BankJsonBean bankJsonBean){
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracer.addTag("crawler.bank.crawler.start", taskBank.getTaskid());
		//改为开始爬取状态
		taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(), 
				BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(), 
				BankStatusCode.BANK_CRAWLER_DOING.getDescription(), 
				null, false, bankJsonBean.getTaskid());
		//获取个人信息
		getDataService.getUserInfo(taskBank);
		//獲取定期存款信息
		getDataService.getTimeDeposit(taskBank);
		//获取银行流水
		getDataService.getTransflow(taskBank);
		
		//修改taskbank中的finish字段
		taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
		//退出
		quitDriver(bankJsonBean);
	}
	
	public void getCreditData(BankJsonBean bankJsonBean){
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracer.addTag("crawler.bank.credit.crawler.start", taskBank.getTaskid());
		//改为开始爬取状态
		taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(), 
				BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(), 
				BankStatusCode.BANK_CRAWLER_DOING.getDescription(), 
				null, false, bankJsonBean.getTaskid());
		//先获取 要爬取的卡号及其他信息
		List<AccNumList> numLists = null;
		try {
			numLists = icbcChinaParser.getAccNumList(taskBank);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.bank.credit.crawler.getAccNumList.Exception", e.getMessage());
		}
		if(null != numLists && numLists.size() > 0){
			for (AccNumList accNumList : numLists) {
				//筛选出信用卡
				if(null == accNumList.getAcctNo0() || accNumList.getAcctNo0().length() == 0){
					//获取该卡的基本信息
					getDataService.getCreditUserInfo(taskBank, accNumList);
					//获取该卡分期信息
					getDataService.getPayStages(taskBank, accNumList);
					//获取该卡月账单，及流水
					getDataService.getMonthBill(taskBank, accNumList);
				}
			}
		}
		//修改taskbank中的finish字段
		taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
		//退出
		quitDriver(bankJsonBean);
		crawlerImpl.getAllDataDone(taskBank.getTaskid());
	}

	@Override
	public TaskBank sendSms(BankJsonBean bankJsonBean) {
		try {
			TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			tracer.addTag("crawler.bank.getSMSCode.start", taskBank.getTaskid());
			//修改状态为 开始发送短信
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_DONING.getPhase(),BankStatusCode.BANK_SEND_CODE_DONING.getPhasestatus(),
					BankStatusCode.BANK_SEND_CODE_DONING.getDescription(),BankStatusCode.BANK_SEND_CODE_DONING.getError_code(), false, bankJsonBean.getTaskid());
			//刷新发送短信页面
			driver.navigate().refresh();
			//模拟 I键 按键3次  为了确保第一次登陆的账户需要安装安全控件，按i键可同意安装
			VK.KeyPress("i");
			Thread.sleep(1000);
			VK.KeyPress("i");
			Thread.sleep(1000);
			VK.KeyPress("i");
			Thread.sleep(1000);
			//获取认证标签集合
			List<WebElement> eles = driver.findElements(By.xpath("//span[starts-with(@id, 'ebdp-pc4promote-menu-level1-text-')]"));
			//创建新集合，存储 短信认证 标签
			List<WebElement> elesNew = new ArrayList<WebElement>();
			for (WebElement ele : eles) {
				if(ele.getText().contains("短信认证")){
					elesNew.add(ele);
				}
			}
			//点击‘短信认证’是页面切换到短信认证的标签页
			elesNew.get(0).click();
			//切换到发短信的frame
			driver.switchTo().frame("integratemainFrame");
			//获取并点击 “发送”按钮
			driver.findElement(By.id("sendSMSCode")).click();
			//截图 
			String shotPath = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracer.addTag("短信发送按钮点击后Screenshot", "截图:" + shotPath);
			Thread.sleep(1000);
			//判断是否发送成功
			String text = driver.findElement(By.id("SendMsgTraceNum")).getText();
			
			if(null != text && text.length() > 0){							//发送成功
				WebParam webParam = icbcChinaParser.getSMSCode(driver.getPageSource());
				tracer.addTag("crawler.bank.getSMSCode.success", webParam.getHtml());
				taskBankStatusService.changeStatus(BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhase(),BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhasestatus(),
						webParam.getHtml(),BankStatusCode.BANK_WAIT_CODE_SUCCESS.getError_code(), false, bankJsonBean.getTaskid());
			}else{
				tracer.addTag("crawler.bank.getSMSCode.fail", "短信码发送失败");
				taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_ERROR.getPhase(),BankStatusCode.BANK_SEND_CODE_ERROR.getPhasestatus(),
						BankStatusCode.BANK_SEND_CODE_ERROR.getDescription(),BankStatusCode.BANK_SEND_CODE_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.bank.getSMSCode.fail2", "短信码发送失败"+e.toString());
			taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_ERROR.getPhase(),BankStatusCode.BANK_SEND_CODE_ERROR.getPhasestatus(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getDescription(),BankStatusCode.BANK_SEND_CODE_ERROR.getError_code(), false, bankJsonBean.getTaskid());
		}
		return null;
	}

	@Async
	@Override
	public TaskBank verifySms(BankJsonBean bankJsonBean) {
		String webdriverHandle = bankJsonBean.getWebdriverHandle();//获取登录步骤的webdriverHandle 
		tracer.addTag("当前的 webdriverHandle：",driver.getWindowHandle()); 
		tracer.addTag("Task表中的 webdriverHandle：",webdriverHandle); 
		//判断数据库中的WindowHandle和当前游览器的WindowHandle是否一致，此判断非常重要！
		//登录后，需要短信验证码或爬取，需要基于登录环节的seleunim WindowHandle 继续下去，如果WindowHandle不匹配。则无法连贯继续下去。例如登录在机器A，爬取在机器B，
		if(webdriverHandle==null||!webdriverHandle.equals(driver.getWindowHandle())){
			tracer.addTag("RuntimeException","当前的webdriverHandle 和 数据库中的 webdriverHandle 不匹配");
			taskBankStatusService.changeStatus(BankStatusCode.BANK_WEBDRIVER_ERROR.getPhase(), 
					BankStatusCode.BANK_WEBDRIVER_ERROR.getPhasestatus(), 
					BankStatusCode.BANK_WEBDRIVER_ERROR.getDescription(), 
					BankStatusCode.BANK_WEBDRIVER_ERROR.getError_code(),false,bankJsonBean.getTaskid());  
		}else {
			try {
				WebElement codeInput = driver.findElement(By.name("userSubmitSignVerifyCode"));
				//输入短信验证码
				codeInput.sendKeys(bankJsonBean.getVerification());
				//driver切换到网页最外层
				driver.switchTo().defaultContent();
				System.out.println("罪孽深重的driver源码++++++"+driver.getPageSource());
				//获取保存图片验证码，并识别
				String path = WebDriverUnit.saveImg(driver, "integratemainFrame", By.id("VerifyimageFrame"));
				String code = chaoJiYingOcrService.callChaoJiYingService(path, "1902");
				//模拟tab按键
				VK.Tab();
				//输入图片验证码
				VK.KeyPress(code);
				//点击‘确定’按钮提交
				driver.findElement(By.id("queding")).click();
				
				Thread.sleep(1000);
				String pageSource = driver.getPageSource();
				if(pageSource.contains("认证通过")){
					//认证通过，点击“点击继续”按钮，跳转到登录成功后的工商银行页面
					driver.findElement(By.id("queding")).click();
					tracer.addTag("crawler.bank.setSMSCode.success", "短信验证成功");
					taskBankStatusService.changeStatus(BankStatusCode.	BANK_VALIDATE_CODE_SUCCESS.getPhase(),BankStatusCode.	BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(),
							BankStatusCode.	BANK_VALIDATE_CODE_SUCCESS.getDescription(),BankStatusCode.	BANK_VALIDATE_CODE_SUCCESS.getError_code(), false, bankJsonBean.getTaskid());
					Thread.sleep(1000);
					//模拟Enter按键3次
					VK.Enter();
					Thread.sleep(500);
					VK.Enter();
					Thread.sleep(500);
					VK.Enter();
					Thread.sleep(2000);
					String currentUrl = driver.getCurrentUrl();
					//进入导航页
					if(currentUrl.contains("https://mybank.icbc.com.cn/icbc/newperbank/perbank3/frame/frame_guide.jsp")){
						//进入到提示页面，将其关闭后自动跳到登陆成功主页
						tracer.addTag("crawler.bank.login.guidePage", driver.getCurrentUrl());
						Thread.sleep(3000);
						//通过执行js来关闭guide页面
						String js = "document.form.submit();";
						JavascriptExecutor jse = (JavascriptExecutor) driver;
						jse.executeScript(js);
						Thread.sleep(1500);
					}else if(driver.getCurrentUrl().contains("frame_index_no_credible.jsp") || driver.getPageSource().contains("您预留的验证信息为空")){
						/*// 模拟Esc按键
						jNativeService.InputEsc();*/
						tracer.addTag("crawler.bank.setSMSCode.fail3", "短信验证成功，但是，您预留的验证信息为空。");
						tracer.addTag("crawler.bank.setSMSCode.fail3.page", "<xmp>"+driver.getPageSource()+"</xmp>");
						//截图 
						String shotPath = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
						tracer.addTag("预留的验证信息为空Screenshot", "截图:" + shotPath);
						taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(),BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(),
								"尊敬的用户，您预留的验证信息为空，请您自行到工商银行官网完整您的信息！",BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(), true, bankJsonBean.getTaskid());
						//退出
						quitDriver(bankJsonBean);
						return null;
					}
					//寻找有sessionID的元素
					WebDriverWait wait=new WebDriverWait(driver, 10);
					Boolean isSessionId = wait.until(new ExpectedCondition<Boolean>() {  
					            public Boolean apply(WebDriver driver) {  
					                return driver.getPageSource().contains("dse_sessionId");  
					            } 
					        });
					if(isSessionId){
						//存放sessionId
						String sessionId = icbcChinaParser.getSessionId(driver.getPageSource());
						TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
						taskBank.setParam(sessionId);
						taskBankRepository.save(taskBank);
						//判断卡的类型（储蓄卡，信用卡）调用不同的爬取方法
						if(bankJsonBean.getCardType().contains("DEBIT_CARD")){
							//开始爬取
							getData(bankJsonBean);
						}else if(bankJsonBean.getCardType().contains("CREDIT_CARD")){
							getCreditData(bankJsonBean);
						}
					}else{
						tracer.addTag("crawler.bank.setSMSCode.fail3", "短信验证失败,找不到sessionId");
						tracer.addTag("crawler.bank.setSMSCode.fail3.page", "<xmp>"+driver.getPageSource()+"</xmp>");
						taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(),BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(),
								BankStatusCode.BANK_VALIDATE_CODE_ERROR.getDescription(),BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(), true, bankJsonBean.getTaskid());
						//退出
						quitDriver(bankJsonBean);
					}
				}else if(pageSource.contains("交易失败")){
					tracer.addTag("crawler.bank.setSMSCode.fail", "短信验证失败");
					tracer.addTag("crawler.bank.setSMSCode.fail.page", "<xmp>"+driver.getPageSource()+"</xmp>");
					taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(),BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR.getDescription(),BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(), true, bankJsonBean.getTaskid());
					//退出
					quitDriver(bankJsonBean);
				}else{
					tracer.addTag("crawler.bank.setSMSCode.fail2", "短信验证失败");
					tracer.addTag("crawler.bank.setSMSCode.fail2.page", "<xmp>"+driver.getPageSource()+"</xmp>");
					taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(), true, bankJsonBean.getTaskid());
					//退出
					quitDriver(bankJsonBean);
				}
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("crawler.bank.setSMSCode.Exception", "短信验证失败"+e.toString());
				tracer.addTag("crawler.bank.setSMSCode.Exception.page", "<xmp>"+driver.getPageSource()+"</xmp>");
				taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
						BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(), true, bankJsonBean.getTaskid());
				//退出
				quitDriver(bankJsonBean);
			}
		}
		return null;
	}
	
	public TaskBank quitDriver(BankJsonBean bankJsonBean) {
		tracer.addTag("quit",bankJsonBean.toString()); 
		//关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试  , description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
//		TaskBank taskBank = taskBankStatusService.systemClose(true,bankJsonBean.getTaskid());  
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		if(taskBank!=null){
			taskBank.setFinished(true);
			taskBankRepository.save(taskBank);
			//调用公用释放资源方法
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else{
			tracer.addTag("quit taskBank is null",""); 
		}
		return taskBank;
	}

	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
