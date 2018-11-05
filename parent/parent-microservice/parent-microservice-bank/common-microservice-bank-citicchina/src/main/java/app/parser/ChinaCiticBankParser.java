package app.parser;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardAccount;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardRegular;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardUserInfo;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.TaskBankStatusService;

@Component
public class ChinaCiticBankParser {
	
	@Value("${webdriver.ie.driver.path}")
	String driverPath;
	private final String LEN_MIN = "0";
	private final String TIME_ADD = "0";
	private final String STR_DEBUG = "a";
	WebDriver driver;
	
	@Autowired
	private TaskBankStatusService taskBankStatusService; 
	@Autowired
	TracerLog tracer;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private AgentService agentService;
	
	@Value("${spring.application.name}") 
	String appName;
	
	
	public WebParam login(BankJsonBean bankJsonBean, TaskBank taskBank) throws Exception {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.setProperty("webdriver.ie.driver", driverPath);
		WebDriver driver = new InternetExplorerDriver();
		driver = new InternetExplorerDriver(ieCapabilities);
		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://i.bank.ecitic.com/perbank6/signIn.do";
		driver.get(baseUrl);
		driver.manage().window().maximize();
		String pageSource = driver.getPageSource();

		// System.out.println("page----------------------------------------"+pageSource);
		WebParam webParam = new WebParam();
		webParam.setHtml(pageSource);

		//windowHandle
		String windowHandle = driver.getWindowHandle();
		bankJsonBean.setWebdriverHandle(windowHandle);
		webParam.setWebHandle(windowHandle);
				
		WebElement username = driver.findElement(By.name("logonNoCert"));
		Thread.sleep(1000);
		username.click();
		Thread.sleep(1000);
		username.clear();
		Thread.sleep(1000);
		username.sendKeys(bankJsonBean.getLoginName());
		Thread.sleep(1000);
//		InputTab();
//		VK.Tab();
//		VK.Tab();
		
		
		String password = bankJsonBean.getPassword();
//		VirtualKeyBoard.KeyPressEx(password, 800);
		driver.findElement(By.id("ocxEdit")).sendKeys(Keys.ENTER);
		VK.KeyPress(password);
		
		String string = driver.findElement(By.id("pinImg")).getAttribute("src");
		if(string != null)
		{
			//判断有没有验证码
			if(driver.findElement(By.id("pinImg")).getAttribute("src").contains("dynPassword.do"))
			{
				String path = WebDriverUnit.saveImg(driver, By.id("pinImg"));
				if("" != path)
				{
					String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1006", LEN_MIN, TIME_ADD, STR_DEBUG, path); // 1005
					Gson gson = new GsonBuilder().create();
					String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
					driver.findElement(By.className("loginInputVerity")).sendKeys(code);
					Thread.sleep(1000);
					try {
						Alert alt = driver.switchTo().alert(); 
						String text = alt.getText(); 
						if (text.contains("验证码错误")) { 
							System.out.println(text); 
							webParam.setHtml(text);
							//截图
							String path1 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
							System.out.println("------------=============="+path1);
							tracer.addTag("action.screenShotByPath.DebitCard.login7",path1);
							agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
							return webParam;
						} 
					} catch (Exception e) {
						tracer.addTag("action.image.success",code);
					}
				}
			}
		}
		
		
		WebElement button = driver.findElement(By.id("logonButton"));
		button.click();
		Thread.sleep(5000);
		String currentUrl = driver.getCurrentUrl();
		String pageSource2 = driver.getPageSource();
		if(pageSource2.contains("errorMsg1"))
		{
			String text = driver.findElement(By.className("errorMsg1")).getText();
			if(text!=null)
			{
				if(text.contains("交易失败"))
				{
					webParam.setHtml(text);
					//截图
					String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					System.out.println("------------=============="+path);
					tracer.addTag("action.screenShotByPath.DebitCard.login6",path);
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}
			}
		}
		else if (baseUrl.contains(currentUrl)) {
			System.out.println("登录失败！");
			//截图
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			System.out.println("------------=============="+path);
			tracer.addTag("action.screenShotByPath.DebitCard.login5",path);
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} 
		else if(driver.findElement(By.id("firstLogonMdyPwdID")).getText().contains("修改密码"))
		{
			//截图
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			System.out.println("------------=============="+path);
			tracer.addTag("action.screenShotByPath.DebitCard.login4",path);
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
		else if(driver.findElement(By.id("firstLogonMdyPwdID")).getText().contains("请输入登陆密码"))
		{
			//截图
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			System.out.println("------------=============="+path);
			tracer.addTag("action.screenShotByPath.DebitCard.login3",path);
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
		
		else if(driver.findElement(By.id("showCertOverDateId")).toString().contains("display: block;"))
		{
			String text2 = driver.findElement(By.id("showCertOverDateId")).getText();
			if(text2.contains("证书过期提示"))
			{
				webParam.setHtml(text2);
//				System.out.println(driver.getTitle()+"中信银行个人网银密码过期，请去官网重新设置");
				//截图
				String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				System.out.println("------------=============="+path);
				tracer.addTag("action.screenShotByPath.DebitCard.login2",path);
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			}
		}
		else {
			System.out.println("登录成功！");
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			for (org.openqa.selenium.Cookie cookie : cookies) {
				Cookie cookieWebClient = new Cookie("i.bank.ecitic.com", cookie.getName(), cookie.getValue());
				webClient.getCookieManager().addCookie(cookieWebClient);
			}

			String cookieJson = CommonUnit.transcookieToJson(webClient);
			taskBank.setCookies(cookieJson);
			taskBank.setLoginName(bankJsonBean.getLoginName());
			taskBankRepository.save(taskBank);
			webParam.setCode(200);
			webParam.setWebClient(webClient);
//			webParam.setUrl(bankJsonBean.getPassword());
			
			//截图
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			System.out.println("------------=============="+path);
			tracer.addTag("action.screenShotByPath.DebitCard.login1",path);
			
		}
		// 将开启的驱动关闭 为了内存考虑
		// driver.quit();
		
		return webParam;
	}

	//tab键
	public void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

	//enter键
	public void InputEnter() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Enter"));
		}
	}

	// 个人信息
	public WebParam<CiticChinaDebitCardUserInfo> userInfocrawler(TaskBank taskBank, BankJsonBean bankJsonBean)
			throws Exception {
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskBank);
		
		String webdriverHandle =taskBank.getWebdriverHandle();//获取登录步骤的webdriverHandle 
		System.out.println("================+++获取登录步骤的webdriverHandle ++++++++++++++++++"+webdriverHandle);
		if(webdriverHandle==null||!webdriverHandle.equals(driver.getWindowHandle())){
			
			System.out.println("======================+现在的++++++++++++++++++++++++++"+driver.getWindowHandle());
			tracer.addTag("action.driver.ERROR", bankJsonBean.getTaskid());
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WEBDRIVER_ERROR.getPhase(), 
					BankStatusCode.BANK_WEBDRIVER_ERROR.getPhasestatus(), 
					BankStatusCode.BANK_WEBDRIVER_ERROR.getDescription(), 
					BankStatusCode.BANK_WEBDRIVER_ERROR.getError_code(),true,bankJsonBean.getTaskid()); 
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracer.addTag("action.screenShotByPath.creditCard.driver",path);
		}
		else{
			String urll = "https://i.bank.ecitic.com/perbank6/firstPageSearch.do?EMP_SID=" + taskBank.getError_message()+ "&searchtxt=个人信息";
			Page page2 = webClient.getPage(urll);
			Thread.sleep(1000);
			// System.out.println(page2.getWebResponse().getContentAsString());

			String userURL3 = "https://i.bank.ecitic.com/perbank6/pb8110_qryCstInfo.do?EMP_SID="+ taskBank.getError_message();
			Page page = webClient.getPage(userURL3);
			Thread.sleep(1000);
			// System.out.println(page.getWebResponse().getContentAsString());
			WebParam<CiticChinaDebitCardUserInfo> webParam = new WebParam<CiticChinaDebitCardUserInfo>();
			if (null != page) {
				int code2 = page.getWebResponse().getStatusCode();
				if (code2 == 200) {
					Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
					CiticChinaDebitCardUserInfo c = new CiticChinaDebitCardUserInfo();
					Elements elementById = doc.getElementById("form").getElementsByTag("td");
					if (null != elementById) {
						String name = getNextLabelByKeywordTwo(elementById, "姓名", "td");
						c.setName(name);
						String sex = getNextLabelByKeywordTwo(elementById, "性别", "td");
						c.setSex(sex);
						String datea = getNextLabelByKeywordTwo(elementById, "上次登陆时间", "td");
						c.setLastTime(datea);
						String phone = getNextLabelByKeywordTwo(elementById, "联系电话", "td");
						c.setPhone(phone);
						String Idcard = getNextLabelByKeywordTwo(elementById, "身份证号", "td");
						c.setIdcard(Idcard);
						String email = getNextLabelByKeywordTwo(elementById, "电子邮箱", "td");
						if(email.contains("未认证"))
						{
							c.setEmail(email);
						}else{
							c.setEmail("");
						}
						String num = getNextLabelByKeywordTwo(elementById, "认证手机号", "td");
						c.setNum(num);
						String username = getNextLabelByKeywordTwo(elementById, "用户名", "td");
						c.setUsername(username);
						String addr = getNextLabelByKeywordTwo(elementById, "地址", "td");
						c.setAddr(addr);
						String code = getNextLabelByKeywordTwo(elementById, "邮编", "td");
						c.setCode(code);
						c.setTaskid(taskBank.getTaskid());
						webParam.setCode(code2);
						webParam.setHtml(page.getWebResponse().getContentAsString());
						webParam.setUrl(userURL3);
						webParam.setCiticChinaDebitCardUserInfo(c);
						return webParam;
					}
				}
			}
			else
			{
				String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracer.addTag("action.screenShotByPath.DebitCard.userinfo",path);
			}
		}
		return null;
	}

	// 流水
	public WebParam<CiticChinaDebitCardAccount> accountcrawler(TaskBank taskBank, BankJsonBean bankJsonBean)
			throws Exception {
		WebParam<CiticChinaDebitCardAccount> webParam = new WebParam<CiticChinaDebitCardAccount>();
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskBank);

		String webdriverHandle =taskBank.getWebdriverHandle();//获取登录步骤的webdriverHandle 
		System.out.println("================+++获取登录步骤的webdriverHandle ++++++++++++++++++"+webdriverHandle);
		if(webdriverHandle==null||!webdriverHandle.equals(driver.getWindowHandle())){
			
			System.out.println("======================+现在的++++++++++++++++++++++++++"+driver.getWindowHandle());
			tracer.addTag("action.driver.ERROR", bankJsonBean.getTaskid());
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WEBDRIVER_ERROR.getPhase(), 
					BankStatusCode.BANK_WEBDRIVER_ERROR.getPhasestatus(), 
					BankStatusCode.BANK_WEBDRIVER_ERROR.getDescription(), 
					BankStatusCode.BANK_WEBDRIVER_ERROR.getError_code(),true,bankJsonBean.getTaskid()); 
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracer.addTag("action.screenShotByPath.creditCard.driver",path);
		}
			else{
				String url10 = "https://i.bank.ecitic.com/perbank6/trans_3063s.do?EMP_SID=" + taskBank.getError_message()+ "&accountNo=" + bankJsonBean.getLoginName() + "&selectSubAccount=null";
		        HtmlPage page10 = webClient.getPage(url10);
				System.out.println("--------------10--------------" + page10.getWebResponse().getContentAsString());
				Thread.sleep(2000);
				 webParam.setHtml(page10.getWebResponse().getContentAsString());//stdesseddt
//		        String url11 = "https://i.bank.ecitic.com/perbank6/pb1310_account_detail.do?EMP_SID="+ taskBank.getError_message() + "&payAcctxt=" + bankJsonBean.getLoginName()+ "&isubAccInfo.judFrozenSFlag=1&isubAccInfo.extendFlag=null&isubAccInfo.frozenSFlag=1&isubAccInfo.lossFlag=null&isubAccInfo.saveType=01&isubAccInfo.hostAccType=null&isubAccInfo.currencyType=001&isubAccInfo.savePeriod=null&isubAccInfo.recordState=1&beginDate="+getFirstDay("yyyyMMdd", 1)+"&endDate="+getFirstDay("yyyyMMdd", 0)+"&beginAmtText=&endAmtText=&accountNo=" + bankJsonBean.getLoginName()+ "&stdessbgdt="+getFirstDay("yyyyMMdd", 1)+"&stdesseddt="+getFirstDay("yyyyMMdd", 0)+"&stdesssbno=&CashFlag=&recordStart=1&recordNum=10000&std400chnn=&std400dcfg=&std400pgqf=N&std400pgtk=&std400pgts=&stdudfcyno=001&opFlag=0&stkessmnam=&largeAmount=&queryType=nearTenTab&targetPage=1&beginAmt=&endAmt=&recordSize=&queryDays=1&startPageFlag=&pageType=&beforePageMap=";
		        String url11 = "https://i.bank.ecitic.com/perbank6/pb1310_account_detail.do?EMP_SID="+ taskBank.getError_message() + "&payAcctxt=" + bankJsonBean.getLoginName()+ "&isubAccInfo.judFrozenSFlag=1&isubAccInfo.extendFlag=null&isubAccInfo.frozenSFlag=1&isubAccInfo.lossFlag=null&isubAccInfo.saveType=01&isubAccInfo.hostAccType=null&isubAccInfo.currencyType=001&isubAccInfo.savePeriod=null&isubAccInfo.interestRate=0.3000000&isubAccInfo.accrualEDate=00000000&isubAccInfo.recordState=1&beginDate="+getFirstDay("yyyyMMdd", 1)+"&endDate="+getFirstDay("yyyyMMdd", 0)+"&beginAmtText=&endAmtText=&accountNo=" + bankJsonBean.getLoginName()+ "&stdessbgdt="+getFirstDay("yyyyMMdd", 1)+"&stdesseddt="+getFirstDay("yyyyMMdd", 0)+"&stdesssbno=&CashFlag=&recordStart=1&std400chnn=&std400dcfg=&std400pgqf=N&std400pgtk=&std400pgts=&stdudfcyno=001&opFlag=0&stkessmnam=&largeAmount=&queryType=nearTenTab&targetPage=1&beginAmt=&endAmt=&recordSize=&queryDays=1&startPageFlag=&pageType=&beforePageMap=";
		        System.out.println(url11);
		        Page page11 = webClient.getPage(url11);
				Thread.sleep(2000);
				System.out.println("-------------------11-----------------" + page11.getWebResponse().getContentAsString());
				 webParam.setHtml(page11.getWebResponse().getContentAsString());
				String url9="https://i.bank.ecitic.com/perbank6/pb1310_account_detail_query.do?EMP_SID="+taskBank.getError_message();
				Page page9 = webClient.getPage(url9);
				Thread.sleep(2000);
				webParam.setHtml(page9.getWebResponse().getContentAsString());
				System.out.println("-------------------9-----------------" + page9.getWebResponse().getContentAsString());

			if (null != page11) {
				Document doc = Jsoup.parse(page11.getWebResponse().getContentAsString());
				CiticChinaDebitCardAccount c = null;
				Element elements = doc.getElementById("resultTable1");
				String text = null;
				if (null != elements) {
					Element elementsByTag3 = elements.getElementsByTag("tbody").get(0);
					if (elementsByTag3.text() == "") {
						return null;
					} else {
						if (null != page9) {
							String string = page9.getWebResponse().getContentAsString();
							Document doc1 = Jsoup.parse(string);
							Element elementById = doc1.getElementById("selected");
							text = elementById.text();
							System.out.println(text.substring(0, text.length() - 4));
						}
						Elements elementsByTag = elementsByTag3.getElementsByTag("tr");
						List<CiticChinaDebitCardAccount> list = new ArrayList<CiticChinaDebitCardAccount>();
						for (int i = 0; i < elementsByTag.size(); i++) {
							Elements elementsByTag2 = elementsByTag.get(i).getElementsByTag("td");
							for (int j = 1; j < elementsByTag2.size(); j = j + 10) {
								c = new CiticChinaDebitCardAccount();
								System.out.println(elementsByTag2);
								c.setCardNum(text.substring(0, text.length() - 4));
								c.setDatea(elementsByTag2.get(j + 1).text());
								c.setSetMoney(elementsByTag2.get(j + 2).text());
								c.setGetMoney(elementsByTag2.get(j + 3).text());
								c.setFee(elementsByTag2.get(j + 4).text());
								c.setAnotherOne(elementsByTag2.get(j + 5).text());
								c.setCompany(elementsByTag2.get(j + 6).text());
								c.setRemark(elementsByTag2.get(j + 7).text());
								c.setStatus(elementsByTag2.get(j + 8).text());
								c.setTaskid(taskBank.getTaskid());
							}
							list.add(c);
						}
						System.out.println(list);
						webParam.setHtml(page11.getWebResponse().getContentAsString());
						webParam.setList(list);
						webParam.setPage(page11);
						webParam.setUrl(url11);
						// driver.quit();
						// 业务实现完毕 释放ip
						// middleClient.releaseInstance(appName,
						// taskBank.getCrawlerHost());
						agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					}
				}
			}
		else
		{
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracer.addTag("action.screenShotByPath.DebitCard.account",path);
		}
		}
//		driver.quit();
		//业务实现完毕    释放ip
//		middleClient.releaseInstance(appName, taskBank.getCrawlerHost());
		agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		return webParam;

	}

	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容2
	 * @param document
	 * @param keyword
	 * @return
	 */
	public String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
		Elements es = element.select(tag + ":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element1 = es.first();
			Element nextElement = element1.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

	// 获取当前时间
	public String getFirstDay(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

	public WebClient addcookie(WebClient webclient, TaskBank taskBank) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskBank.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}
		return webclient;
	}

	

	
	//定期信息
	public WebParam<CiticChinaDebitCardRegular> debitcardRegular(TaskBank taskBank, BankJsonBean bankJsonBean) throws Exception {
		WebParam<CiticChinaDebitCardRegular> webParam = new WebParam<CiticChinaDebitCardRegular>();
		String webdriverHandle =taskBank.getWebdriverHandle();//获取登录步骤的webdriverHandle 
        if(webdriverHandle==null||!webdriverHandle.equals(driver.getWindowHandle())){
			
			System.out.println("======================+现在的++++++++++++++++++++++++++"+driver.getWindowHandle());
			tracer.addTag("action.driver.ERROR", bankJsonBean.getTaskid());
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WEBDRIVER_ERROR.getPhase(), 
					BankStatusCode.BANK_WEBDRIVER_ERROR.getPhasestatus(), 
					BankStatusCode.BANK_WEBDRIVER_ERROR.getDescription(), 
					BankStatusCode.BANK_WEBDRIVER_ERROR.getError_code(),true,bankJsonBean.getTaskid()); 
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracer.addTag("action.screenShotByPath.creditCard.driver",path);
		}
		else
		{
			String url12="https://i.bank.ecitic.com/perbank6/pb1110_query_detail.do?EMP_SID="+ taskBank.getError_message()+"&accountNo="+bankJsonBean.getLoginName()+"&index=0ff0";
			WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
			WebClient webClient = addcookie(newWebClient, taskBank);
			HtmlPage htmlpage = webClient.getPage(url12);
			//获取姓名
			String contentAsString = htmlpage.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(contentAsString);
			
			//卡号
			Elements elements = doc.getElementsByClass("noMore");
			System.out.println(elements);
			String html = elements.html();
			System.out.println(html);
			String[] split = html.split("subQueryFuncNew");
			String[] split2 = split[1].split("value");
			String substring = split2[0].substring(2, 6);
			System.out.println(substring);
			
			
			Elements elementsByClass = doc.getElementsByClass("bg_white_color");
			//System.out.println(elementsByClass);
			Element element = elementsByClass.get(0);
			Elements elementsByTag2 = element.getElementsByTag("tr");
			//System.out.println(elementsByTag2.size());
			Elements elementsByTag = element.getElementsByTag("td");
			//System.out.println(elementsByTag);
			CiticChinaDebitCardRegular c = new CiticChinaDebitCardRegular();
			List<CiticChinaDebitCardRegular> list = new ArrayList<CiticChinaDebitCardRegular>();
			for (int i = 0; i < elementsByTag2.size(); i++) {
				c.setCardNum(bankJsonBean.getLoginName());
				c.setType(elementsByTag.get(i).text()+"");
				c.setCurrency(elementsByTag.get(i+1).text()+"");
				c.setRatio(elementsByTag.get(i+2).text()+"");
				c.setTime(elementsByTag.get(i+3).text()+"");
				c.setDoRatio(elementsByTag.get(i+4).text()+"");
				c.setStartDate(elementsByTag.get(i+5).text()+"");
				c.setEndDate(elementsByTag.get(i+6).text()+"");
				c.setUseFee(elementsByTag.get(i+7).text()+"");
				c.setAccountFee(elementsByTag.get(i+8).text()+"");
				c.setStatus(elementsByTag.get(i+9).text()+"");
				c.setTaskid(bankJsonBean.getTaskid());
				list.add(c);
			}
			webParam.setUrl(url12);
			webParam.setHtml(contentAsString);
			webParam.setCiticChinaDebitCardRegular(c);;
			webParam.setList(list);
			return webParam;
		}
		return null;
		
	}

	public TaskBank quit(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankStatusService.systemClose(true,bankJsonBean.getTaskid());  
		//调用公用释放资源方法
		if(taskBank!=null){
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else{
			tracer.addTag("quit taskBank is null",""); 
		}
		return taskBank;
	}
	
}
