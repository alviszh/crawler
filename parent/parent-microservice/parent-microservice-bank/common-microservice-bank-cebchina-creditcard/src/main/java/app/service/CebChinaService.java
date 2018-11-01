package app.service;

import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaCreditCardBilling;
import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaCreditCardConsumption;
import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaCreditCardConsumptionMonth;
import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaCreditCardUserInfo;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.cebchina.CebChinaCreditCardBillingRepository;
import com.microservice.dao.repository.crawler.bank.cebchina.CebChinaCreditCardConsumptionMonthRepository;
import com.microservice.dao.repository.crawler.bank.cebchina.CebChinaCreditCardConsumptionRepository;
import com.microservice.dao.repository.crawler.bank.cebchina.CebChinaCreditCardUserInfoRepository;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.parser.CebChinaParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic","com.microservice.dao.entity.crawler.bank.cebchina"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic","com.microservice.dao.repository.crawler.bank.cebchina"})
public class CebChinaService {
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private CebChinaParser cebChinaParser;
	@Autowired
	private AgentService agentService;
	@Autowired
	private WebDriverIEService webDriverIEService;
	@Autowired
	private CebChinaCreditCardUserInfoRepository cebChinaCreditCardUserInfoRepository;
	@Autowired
	private CebChinaCreditCardConsumptionMonthRepository cebChinaCreditCardConsumptionMonthRepository;
	@Autowired
	private CebChinaCreditCardConsumptionRepository cebChinaCreditCardConsumptionRepository;
	@Autowired
	private CebChinaCreditCardBillingRepository cebChinaCreditCardBillingRepository;
	String attr = null;
	private WebDriver driver;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	int i = 0;
	static String LoginPage  = "https://xyk.cebbank.com/mall/login?target=/mycard/home/home.htm";
	static String GenIndex = "https://xyk.cebbank.com/mycard/home/home.htm";
	/***
	 * 登录
	 * @param bankJsonBean
	 */

	public TaskBank loginByCardNum(BankJsonBean bankJsonBean) {

		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracerLog.output("crawler.bank.login.start", taskBank.getTaskid());
		try {
			//打开登录页
			driver = openloginCebChina();
			String windowHandle = driver.getWindowHandle();
			bankJsonBean.setWebdriverHandle(windowHandle);
			//输入帐号
			driver.findElement(By.id("userName")).sendKeys(bankJsonBean.getLoginName());//输入帐号 6226580034768784
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracerLog.output("登录页面", path);
			Thread.sleep(2000L);
			//获取图片验证码
			String path2 = WebDriverUnit.saveImg(driver, By.className("grid-mock"));
			System.out.println("path---------------" + path2);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,
					path2); // 1005
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);
			//输入验证码
			driver.findElement(By.id("yzmcode")).sendKeys(code);//验证码
			Thread.sleep(2000L);
			//发送短信
			WebElement findElement = driver.findElement(By.xpath("//button[@class='btn btn-small js-login-sendCode']"));//发送短信			
			findElement.click();
			Thread.sleep(2000L);

			String html = driver.getPageSource();
			if(html.indexOf("确定")!=-1){
				String html2 = driver.findElement(By.className("popup-dialog-message")).getText();
				if(html2.indexOf("您的登录动态密码已经发送到您的手机，请稍候勿重复发送!")!=-1){
					System.out.println("发送短信成功");
					driver.findElement(By.xpath("//button[@class='btn btn-success btn-small js-btn-cancel']")).click();
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhase(), 
							BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhasestatus(), 
							"您的登录动态密码已经发送到您的手机，请使用最新发送的短信验证码!", 
							BankStatusCode.BANK_WAIT_CODE_SUCCESS.getError_code(),false,bankJsonBean.getTaskid(),windowHandle);
					return taskBank;
				}else{
					tracerLog.output("登录失败,错误信息", html2);
					if(html2.equals("验证码不正确")){
						html2 = "网络延迟，请重新登录！";
						if(i<3){
							i=i+1;
							driver.quit();
							loginByCardNum(bankJsonBean);
						}
					}
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_SEND_CODE_ERROR.getPhase(), 
							BankStatusCode.BANK_SEND_CODE_ERROR.getPhasestatus(),
							html2,
							BankStatusCode.BANK_SEND_CODE_ERROR.getError_code(),true,bankJsonBean.getTaskid(),windowHandle);
					String path1 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracerLog.output("登录失败，且没有登录失败错误信息", path1);
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}
			}else{
				System.out.println("发送短信成功");
				taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhase(), 
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhasestatus(), 
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getDescription(), 
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getError_code(),false,bankJsonBean.getTaskid(),windowHandle);
			}

			return taskBank;
		} catch (Exception e) {
			// TODO: handle exception
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(), 
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(), 
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(), 
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(),false,bankJsonBean.getTaskid());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			return taskBank;
		}
	}

	/****
	 * 验证短信
	 * @param bankJsonBean
	 */
	public void setsmscode(BankJsonBean bankJsonBean) {

		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracerLog.output("crawler.bank.login.account.start", taskBank.getTaskid());
		try {
			driver.findElement(By.id("verification-code")).sendKeys(bankJsonBean.getVerification());
			Thread.sleep(2000L);
			driver.findElement(By.xpath("//input[@class='btn login-style-bt ']")).click();
			Thread.sleep(2000L);
			String path1 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracerLog.output("点击登录后", path1);
			String currentUrl = driver.getCurrentUrl();

			if(currentUrl.equals(GenIndex)){
				System.out.println("登录成功");
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(), 
						BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(), 
						BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getDescription(), 
						BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getError_code(),false,bankJsonBean.getTaskid());
				//信用卡信息解析
				try {
					List<CebChinaCreditCardUserInfo> userInfo = getUserInfo(bankJsonBean);
					if(userInfo!=null){
						cebChinaCreditCardUserInfoRepository.saveAll(userInfo);
						taskBankStatusService.updateTaskBankUserinfo(
								200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), bankJsonBean.getTaskid());
					}
					else{
						taskBankStatusService.updateTaskBankUserinfo(
								201, BankStatusCode.BANK_USERINFO_ERROR.getDescription(), bankJsonBean.getTaskid());
						String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
						tracerLog.output("信用卡信息错误信息", path);
					}
				} catch (Exception e) {
					taskBankStatusService.updateTaskBankUserinfo(
							404, BankStatusCode.BANK_USERINFO_ERROR2.getDescription(), bankJsonBean.getTaskid());
					String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracerLog.output("信用卡信息错误信息", path);
				}
				Thread.sleep(1000L);
				//信用卡未出账单解析
				try {
					List<CebChinaCreditCardConsumptionMonth> list = getConsumptionMonth(bankJsonBean);
					if(list.size()>0&&list!=null){
						cebChinaCreditCardConsumptionMonthRepository.saveAll(list);
						taskBank.setDescription("信用卡未出账单爬取成功");
						taskBank.setError_message("信用卡未出账单爬取成功");
					}else{
						taskBank.setDescription("信用卡未出账单爬取完成");
						taskBank.setError_code(201);
						taskBank.setError_message("信用卡未出账单爬取完成");
						String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
						tracerLog.output("信用卡未出账单错误信息", path);
					}
				} catch (Exception e) {
					taskBank.setDescription("信用卡未出账单解析完成");
					taskBank.setError_code(404);
					taskBank.setError_message("信用卡未出账单网页未进入");
					String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracerLog.output("信用卡未出账单失败错误信息", path);
				}
				Thread.sleep(1000L);
				//信用卡已出账单
				try {
					List<CebChinaCreditCardConsumption> list2 = getConsumption(bankJsonBean);
					if(list2.size()>0&&list2!=null){
						cebChinaCreditCardConsumptionRepository.saveAll(list2);
						taskBankStatusService.updateTaskBankTransflow(
								200,  BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
					}else{
						taskBankStatusService.updateTaskBankTransflow(
								201,  BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), bankJsonBean.getTaskid());
						String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
						tracerLog.output("信用卡已出账单错误信息", path);
					}
				} catch (Exception e) {
					taskBankStatusService.updateTaskBankTransflow(
							404,  BankStatusCode.BANK_TRANSFLOW_ERROR2.getDescription(), bankJsonBean.getTaskid());
					String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracerLog.output("信用卡已出账单错误信息", path);
				}

				//账单明细

				for(int i =0;i<6;i++){
					try {
						List<CebChinaCreditCardBilling> getbilling = getbilling(bankJsonBean, i);
						if(getbilling!=null){
							cebChinaCreditCardBillingRepository.saveAll(getbilling);
							taskBank.setDescription("信用卡账单明细爬取成功");
							taskBank.setError_message("信用卡账单明细爬取成功");
						}else{
							taskBank.setDescription("信用卡账单明细爬取完成");
							taskBank.setError_code(201);
							taskBank.setError_message("信用卡账单明细爬取完成");
							String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
							tracerLog.output("信用卡账单明细错误信息", path);
						}

					} catch (Exception e) {
						taskBank.setDescription("信用卡账单明细爬取完成");
						taskBank.setError_code(500);
						taskBank.setError_message("信用卡账单明细爬取完成");
						String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
						tracerLog.output("信用卡账单明细错误信息", path);
					}
				}
				taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

			}
			else{
				System.out.println("验证码错误，请重新登录");
				taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR3.getPhase(), 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR3.getPhasestatus(), 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR3.getDescription(), 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR3.getError_code(),false,bankJsonBean.getTaskid());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			}


		} catch (Exception e) {
			System.out.println("登录失败，请重新登录");
			taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(), 
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(), 
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(), 
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(),false,bankJsonBean.getTaskid());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
	}


	/**
	 * 登录页面
	 * @return
	 */
	public WebDriver openloginCebChina(){ 

		driver = webDriverIEService.getNewWebDriver();
		System.out.println("WebDriverIEService loginCmbChina Msg 开始登陆光大信用卡登陆页");
		tracerLog.output("WebDriverIEService loginCmbChina Msg", "开始登陆光大信用卡登陆页"); 
		try{
			driver = getPage(driver,LoginPage);
		}catch(NoSuchWindowException e){ 
			System.out.println("打开光大登录页面报错，尝试重新初始化游览器"+e.getMessage());
			tracerLog.output("打开光大登录页面报错，尝试重新初始化游览器", e.getMessage()); 
			driver = getPage(driver,LoginPage);
		}

		return driver;
	}
	/**
	 * 信用卡信息
	 * @param bankJsonBean
	 * @return
	 */
	/*public CebChinaCreditCardUserInfo getUserInfo(BankJsonBean bankJsonBean)throws Exception{
		CebChinaCreditCardUserInfo cebChinaCreditCardUserInfo = null;
		//信用卡信息
		String url = "https://xyk.cebbank.com/mycard/home/acclist.htm?num=5004706";
		//信用卡额度
		String url2 = "https://xyk.cebbank.com/mycard/home/getCreditLimit.htm?num=5004706";
		//还款详情
		String url3 = "https://xyk.cebbank.com/mycard/bill/billnearly-query.htm?num=5004706";
		//积分
		String url4 = "https://xyk.cebbank.com/mycard/point/total.htm";
		//当月欠款
		String url5 = "https://xyk.cebbank.com/mycard/home/accinfo.htm?cardno="+bankJsonBean.getLoginName();
		//剩余额度
		String url6 = "https://xyk.cebbank.com/mycard/home/getAvailability.htm?cardno="+bankJsonBean.getLoginName();

		String html7 = driver.getPageSource();
		driver.get(url);
		String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
		tracerLog.output("信用卡信息", path);
		String html = driver.getPageSource();
		Thread.sleep(2000L);

		driver.get(url2);
		String path2 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
		tracerLog.output("信用卡额度", path2);
		String html2 = driver.getPageSource();
		Thread.sleep(2000L);

		driver.get(url3);
		String path3 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
		tracerLog.output("还款详情", path3);
		String html3 = driver.getPageSource();
		Thread.sleep(2000L);

		driver.get(url4);
		String path4 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
		tracerLog.output("积分", path4);
		String html4 = driver.getPageSource();
		Thread.sleep(2000L);

		driver.get(url5);
		String path5 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
		tracerLog.output("当月欠款", path5);
		String html5 = driver.getPageSource();
		Thread.sleep(2000L);

		driver.get(url6);
		Thread.sleep(1000L);
		String path6 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
		tracerLog.output("剩余额度", path6);
		String html6 = driver.getPageSource();

		cebChinaCreditCardUserInfo = cebChinaParser.getUserInfo(html,html2,html3,html4,html5,html6,html7);

		return cebChinaCreditCardUserInfo;
	}*/
	/**
	 * 信用卡信息
	 * @param bankJsonBean
	 * @return
	 * @throws Exception
	 */
	public List<CebChinaCreditCardUserInfo> getUserInfo(BankJsonBean bankJsonBean)throws Exception{

		List<CebChinaCreditCardUserInfo> list = new ArrayList<CebChinaCreditCardUserInfo>();
		String html7 = driver.getPageSource();

		String url = "https://xyk.cebbank.com/mycard/bill/info.htm";
		driver.get(url);
		Thread.sleep(1000L);
		String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
		tracerLog.output("剩余额度", path);
		String html = driver.getPageSource();

		Document parse = Jsoup.parse(html);
		Element element = parse.getElementsByClass("tab").get(0);
		Elements elementsByTag = element.getElementsByTag("tr");
		for (int i = 1; i < elementsByTag.size(); i++) {
			String text = element.getElementsByTag("tr").get(i).getElementsByTag("td").get(0).text();
			if(text.indexOf("********")!=-1){
				attr = elementsByTag.get(i).attr("id");
				String numname = elementsByTag.get(i).getElementsByTag("td").get(1).text();
				String url2 = "https://xyk.cebbank.com/mycard/bill/info.htm?type=lb"
						+ "&cardno="+attr
						+ "&first=2"
						+ "&weihuanjine=";
				driver.get(url2);
				String path3 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracerLog.output("信用卡信息", path3);
				String html2 = driver.getPageSource();
				Thread.sleep(2000L);

				String url3 = "https://xyk.cebbank.com/mycard/point/total.htm";
				driver.get(url3);
				String path2 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracerLog.output("积分", path2);
				String html4 = driver.getPageSource();

				List<CebChinaCreditCardUserInfo> userInfo1 = cebChinaParser.getUserInfo1(html2,bankJsonBean.getTaskid(),html4,html7,numname);
				if(list!=null){
					for (CebChinaCreditCardUserInfo cebChinaCreditCardUserInfo : userInfo1) {
						list.add(cebChinaCreditCardUserInfo);
					}
				}
			}else{
				return list;
			}
		}
		return list;
	}




	/**
	 * 当月消费明细（未出账）
	 * @return
	 */
	public List<CebChinaCreditCardConsumptionMonth> getConsumptionMonth(BankJsonBean bankJsonBean)throws Exception{

		List<CebChinaCreditCardConsumptionMonth> list = null;
		if(attr!=null){
		String url = "https://xyk.cebbank.com/mycard/bill/unprintbill-query.htm?"
				+ "ACCT_NBR="+attr
				+ "&ORG=103";//103（人民币）
		driver.get(url);
		String html = driver.getPageSource();
		list = cebChinaParser.getgetConsumptionMonth(html,bankJsonBean.getTaskid());
		}
		return list;
	}
	/**
	 * 已出账信息
	 * @param bankJsonBean
	 * @return
	 */
	public List<CebChinaCreditCardConsumption> getConsumption(BankJsonBean bankJsonBean)throws Exception{
		List<CebChinaCreditCardConsumption> list = null;
		String url = "https://xyk.cebbank.com/mycard/bill/havingprintbill-query.htm";
		driver.get(url);
		String html = driver.getPageSource();
		//信用卡信息
		String url2 = "https://xyk.cebbank.com/mycard/home/acclist.htm?num=5004706";
		driver.get(url2);
		String html2 = driver.getPageSource();
		list = cebChinaParser.getConsumption(bankJsonBean.getTaskid(),html,html2);
		return list;
	}
	/**
	 * 每月账单明细
	 */
	public List<CebChinaCreditCardBilling> getbilling(BankJsonBean bankJsonBean,int i){
		LocalDate today = LocalDate.now();
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1).plusMonths(-i);
		String monthint = stardate.getMonthValue() + "";
		if(monthint.length()<2){
			monthint = "0" + monthint;
		}
		String month1 = stardate.getYear() + monthint;
		List<CebChinaCreditCardBilling> list = null;
		String url = "https://xyk.cebbank.com/mycard/bill/billquerydetail.htm?"
				+ "statementDate="+month1+"18";
		driver.get(url);
		try {
			Alert alt = driver.switchTo().alert();
			String text = alt.getText(); 
			if (text.contains("无账单记录")) { 
				System.out.println(text); 
				System.out.println("无"+month1+"账单记录"); 
			} 
		} catch (Exception e) { 
			System.out.println("有"+month1+"账单记录（没有alert弹框）");
			driver.findElement(By.xpath("//a[@title = '账单明细']")).click();
			String html2 = driver.getPageSource();
			list = cebChinaParser.getbilling(html2,month1,bankJsonBean.getTaskid());
		}
		return list;
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public WebDriver getPage(WebDriver driver,String url) throws NoSuchWindowException{
		System.out.println("getPage:"+url);
		//	tracer.addTag("getPage", url); 
		//driver = getWebDriver(); 
		driver.get(url);
		return driver;
	}

	/**
	 * @Des 系统退出，释放资源
	 * @param BankJsonBean 
	 */ 
	public TaskBank quit(BankJsonBean bankJsonBean){ 
		tracerLog.output("quit",bankJsonBean.toString()); 
		//关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试  , description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
		TaskBank taskBank = taskBankStatusService.systemClose(true,bankJsonBean.getTaskid());  
		//调用公用释放资源方法
		if(taskBank!=null){
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else{
			tracerLog.output("quit taskBank is null",""); 
		}
		return taskBank;
	} 


}