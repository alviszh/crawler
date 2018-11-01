package app.parser;

import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangInjury;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangMaternity;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangMedical;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangUnemployment;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangUserInfo;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.InsuranceService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceHeiLongJiangParser {

	@Value("${webdriver.ie.driver.path}")
	String driverPath;
	private final String LEN_MIN = "0";
	private final String TIME_ADD = "0";
	private final String STR_DEBUG = "a";
	WebDriver driver;
	
	@Value("${spring.application.name}") 
	String appName;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private AgentService agentService;
	
	
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();
		 
		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://www.renshenet.org.cn/sionline/loginControler";
		driver.get(baseUrl);
		driver.manage().window().maximize();
		String pageSource = driver.getPageSource();
		
		String windowHandle = driver.getWindowHandle();
		System.out.println(windowHandle);
		WebElement username = driver.findElement(By.name("iptUserId"));
		Thread.sleep(1000);
		username.click();
		Thread.sleep(1000);
		username.clear();
		Thread.sleep(1000);
		username.sendKeys(insuranceRequestParameters.getUsername());
		Thread.sleep(1000);
		driver.findElement(By.className("button")).click();
		
//		String password = "wangluqsklmyt16";
		WebElement passwordEncoder = driver.findElement(By.id("passwordEncoder"));
		passwordEncoder.click();
		VK.KeyPress(insuranceRequestParameters.getPassword());

		//driver.findElement(By.className("button")).click();
		
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);  
		WebElement eleTriggerFunc = wait.until(new Function<WebDriver, WebElement>() {  
             public WebElement apply(WebDriver driver) {  
               return driver.findElement(By.className("button")); 
             }  
		}); 
		eleTriggerFunc.click();
		
		Thread.sleep(50000);
		String html = driver.getPageSource();
		//System.out.println(html);
		WebParam webParam = new WebParam();
		webParam.setWebHandle(windowHandle);
		if (html.contains("我的首页")) {
			
			System.out.println("登录成功！");
			//System.out.println(driver.getPageSource());
			
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			for(org.openqa.selenium.Cookie cookie : cookies){
				Cookie cookieWebClient = new Cookie("www.renshenet.org.cn", cookie.getName(), cookie.getValue());
				System.out.println(cookieWebClient);
				webClient.getCookieManager().addCookie(cookieWebClient);
			}
			webParam.setWebClient(webClient);
			webParam.setHtml(html);
			return webParam;
		} else {
			System.out.println("登录失败！");
			//截图
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			System.out.println("------------=============="+path);
			tracer.addTag("action.screenShotByPath.DebitCard.login",path);
			agentService.releaseInstance(taskInsurance.getCrawlerHost(), driver);
		}
		return null;
		
	}

	public WebParam<InsuranceSZHeiLongJiangUserInfo> getUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception{
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskInsurance);
		WebParam<InsuranceSZHeiLongJiangUserInfo> webParam = new WebParam<InsuranceSZHeiLongJiangUserInfo>();
		String webdriverHandle =taskInsurance.getWebdriverHandle();//获取登录步骤的webdriverHandle 
//        if(webdriverHandle==null||!webdriverHandle.equals(driver.getWindowHandle())){
//			
//			System.out.println("======================+现在的++++++++++++++++++++++++++"+driver.getWindowHandle());
//			tracer.addTag("action.driver.ERROR", insuranceRequestParameters.getTaskId());
//			insuranceService.changeLoginStatusException(taskInsurance);
//			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
//			tracer.addTag("action.screenShotByPath.creditCard.driver",path);
//		}
//		else
//		{
		String url1="https://www.renshenet.org.cn/sionline/search01/init00.html";
		Page page2 = webClient.getPage(url1);
		Thread.sleep(1000);
		String url2="https://www.renshenet.org.cn/sionline/search01/init.html?parm=baseInfo";
		Page page3 = webClient.getPage(url2);
		Thread.sleep(1000);
		String usrUrl="https://www.renshenet.org.cn/sionline/search01/baseInfo.html?ywdm=search01&parm=baseInfo";
		Thread.sleep(2000);
		
		WebRequest  requestSettings = new WebRequest(new URL(usrUrl), HttpMethod.POST);
	    requestSettings.setCharset(Charset.forName("UTF-8"));		    
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN");
		requestSettings.setAdditionalHeader("Host", "www.renshenet.org.cn");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		requestSettings.setAdditionalHeader("Referer", "https://www.renshenet.org.cn/sionline/search01/init.html?parm=baseInfo");
		
		Page page = webClient.getPage(requestSettings);
		Thread.sleep(2000);
		System.out.println(page.getWebResponse().getContentAsString());
		if(null != page)
		{
			String string = page.getWebResponse().getContentAsString();
			
			if(string.contains("pageQuery"))
			{
				InsuranceSZHeiLongJiangUserInfo i = new InsuranceSZHeiLongJiangUserInfo();
				JSONObject fromObject = JSONObject.fromObject(string);
				
				i.setCompanyNum(fromObject.getString("bkc007"));
				//i.setCompany(fromObject.getString("aab004"));
				i.setPersonalNum(fromObject.getString("aac001"));
				i.setName(fromObject.getString("aac003"));
				i.setIdCard(fromObject.getString("aac002"));
				i.setSex(fromObject.getString("aac004"));
				i.setNational(fromObject.getString("aac004"));
				i.setBirth(fromObject.getString("aac006"));
				i.setBirthday(fromObject.getString("aac006Dossier"));
				i.setCulture(fromObject.getString("aac011"));
				i.setMarry(fromObject.getString("aac017"));
				i.setPersonal(fromObject.getString("aac012"));
				i.setJoinDate(fromObject.getString("aac007"));
				i.setHouseHold(fromObject.getString("aac009"));
				i.setInsuranceStatus(fromObject.getString("aac008"));
				i.setWorkStatus(fromObject.getString("aac016"));
				i.setProfession(fromObject.getString("aac014"));
				i.setUseWork(fromObject.getString("aac013"));
				i.setSpecial(fromObject.getString("aac019"));
				i.setChangeFloor(fromObject.getString("bae905"));
				i.setAdministration(fromObject.getString("aac020"));
				i.setPhone(fromObject.getString("aae005"));
				i.setAddr(fromObject.getString("aae006"));
				i.setCode(fromObject.getString("aae007"));
				i.setHomeland(fromObject.getString("aac010"));
				i.setCountryFloor(fromObject.getString("aac015"));
				i.setJudgeTime(fromObject.getString("bae904"));
				i.setSign(fromObject.getString("mergedMark"));
				i.setWorkerNum(fromObject.getString("bae903"));
				i.setTaskid(taskInsurance.getTaskid());
				//System.out.println(i);
				webParam.setHtml(string);
				webParam.setWebClient(webClient);
				webParam.setWebHandle(webdriverHandle);
				webParam.setInsuranceSZHeiLongJiangUserInfo(i);
				webParam.setUrl(usrUrl);
			    }
		    }
//		}
		agentService.releaseInstance(taskInsurance.getCrawlerHost(), driver);
        return webParam;
	}
	
	
	public WebClient addcookie(WebClient webclient, TaskInsurance taskInsurance) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}
		return webclient;
	}

	
	//医疗
	public WebParam<InsuranceSZHeiLongJiangMedical> getMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskInsurance);
		WebParam<InsuranceSZHeiLongJiangMedical> webParam = new WebParam<InsuranceSZHeiLongJiangMedical>();
		String webdriverHandle =taskInsurance.getWebdriverHandle();//获取登录步骤的webdriverHandle 
//        if(webdriverHandle==null||!webdriverHandle.equals(driver.getWindowHandle())){
//			
//			System.out.println("======================+现在的++++++++++++++++++++++++++"+driver.getWindowHandle());
//			tracer.addTag("action.driver.ERROR", insuranceRequestParameters.getTaskId());
//			insuranceService.changeLoginStatusException(taskInsurance);
//			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
//			tracer.addTag("action.screenShotByPath.creditCard.driver",path);
//		}
//		else
//		{
			String urlinit="https://www.renshenet.org.cn/sionline/search01/init03.html";
			Page page5 = webClient.getPage(urlinit);
			String urlwage="https://www.renshenet.org.cn/sionline/search01/init.html?parm=wagesInfo";
			Page page6 = webClient.getPage(urlwage);
			String urldetail="https://www.renshenet.org.cn/sionline/search01/init.html?parm=wagesInfo";
			Page page7 = webClient.getPage(urldetail);
			String urlyi="https://www.renshenet.org.cn/sionline/search01/accountDetails.html?ywdm=search01&parm=accountDetails";
            WebRequest  requestSettings1 = new WebRequest(new URL(urlyi), HttpMethod.POST);
		    requestSettings1.setCharset(Charset.forName("UTF-8"));		    
			requestSettings1.setAdditionalHeader("Accept", "*/*");
			requestSettings1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			requestSettings1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings1.setAdditionalHeader("Accept", "*/*");
			requestSettings1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			requestSettings1.setAdditionalHeader("Accept-Language", "zh-CN");
			requestSettings1.setAdditionalHeader("Host", "www.renshenet.org.cn");
			requestSettings1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			requestSettings1.setAdditionalHeader("Referer", "https://www.renshenet.org.cn/sionline/search01/init.html?parm=accountDetails");
			Page page4 = webClient.getPage(requestSettings1);
		    Thread.sleep(2000);
		    if(null != page4)
		    {
		    	String strings = page4.getWebResponse().getContentAsString();
		    	if(strings.contains("pageQuery"))
		    	{
		    		JSONObject fromObjectt = JSONObject.fromObject(strings);
			    	String string2 = fromObjectt.getString("pageQuery");
			    	JSONArray fromObject2 = JSONArray.fromObject(string2);
			    	
			    	List<InsuranceSZHeiLongJiangMedical> list = new ArrayList<InsuranceSZHeiLongJiangMedical>();
			    	for (int j = 0; j < fromObject2.size(); j++) {
			    		InsuranceSZHeiLongJiangMedical insuranceSZHeiLongJiangMedical = new InsuranceSZHeiLongJiangMedical();
						JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(j));
						insuranceSZHeiLongJiangMedical.setPayDate(fromObject3.getString("col1"));
						insuranceSZHeiLongJiangMedical.setPayType(fromObject3.getString("col2"));
						insuranceSZHeiLongJiangMedical.setPayBase(fromObject3.getString("col3"));
						insuranceSZHeiLongJiangMedical.setMustSum(fromObject3.getString("col4"));
						insuranceSZHeiLongJiangMedical.setMustPersonal(fromObject3.getString("col5"));
						insuranceSZHeiLongJiangMedical.setMustCompany(fromObject3.getString("col6"));
						insuranceSZHeiLongJiangMedical.setFactSum(fromObject3.getString("col7"));
						insuranceSZHeiLongJiangMedical.setFactPersonal(fromObject3.getString("col8"));
						insuranceSZHeiLongJiangMedical.setFactCompany(fromObject3.getString("col9"));
						insuranceSZHeiLongJiangMedical.setPayStatus(fromObject3.getString("col10"));
						insuranceSZHeiLongJiangMedical.setPeopleStatus(fromObject3.getString("col11"));
						insuranceSZHeiLongJiangMedical.setTaskid(taskInsurance.getTaskid());
						list.add(insuranceSZHeiLongJiangMedical);
					}
			    	webParam.setHtml(strings);
			    	webParam.setList(list);
			    	webParam.setUrl(urlyi);
			    	return webParam;
		    	}
		    }
//		}
        return null;
	}

	public WebParam<InsuranceSZHeiLongJiangUnemployment> getUnemployment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception{
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskInsurance);
		WebParam<InsuranceSZHeiLongJiangUnemployment> webParam = new WebParam<InsuranceSZHeiLongJiangUnemployment>();
		String webdriverHandle =taskInsurance.getWebdriverHandle();//获取登录步骤的webdriverHandle 
//        if(webdriverHandle==null||!webdriverHandle.equals(driver.getWindowHandle())){
//			
//			System.out.println("======================+现在的++++++++++++++++++++++++++"+driver.getWindowHandle());
//			tracer.addTag("action.driver.ERROR", insuranceRequestParameters.getTaskId());
//			insuranceService.changeLoginStatusException(taskInsurance);
//			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
//			tracer.addTag("action.screenShotByPath.creditCard.driver",path);
//		}
//		else
//		{
		    String url="https://www.renshenet.org.cn/sionline/search01/unemployed02.html?ywdm=search01&parm=unemployed02";
		    WebRequest  requestSettings1 = new WebRequest(new URL(url), HttpMethod.POST);
		    
		    requestSettings1.setCharset(Charset.forName("UTF-8"));		    
			requestSettings1.setAdditionalHeader("Accept", "*/*");
			requestSettings1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			requestSettings1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings1.setAdditionalHeader("Accept", "*/*");
			requestSettings1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			requestSettings1.setAdditionalHeader("Accept-Language", "zh-CN");
			requestSettings1.setAdditionalHeader("Host", "www.renshenet.org.cn");
			requestSettings1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			requestSettings1.setAdditionalHeader("Referer", "https://www.renshenet.org.cn/sionline/search01/init.html?parm=accountDetails");
			Page page4 = webClient.getPage(requestSettings1);
		    Thread.sleep(2000);
		    if(null != page4)
		    {
		    	String strings = page4.getWebResponse().getContentAsString();
		    	if(strings.contains("pageQuery"))
		    	{
		    		JSONObject fromObjectt = JSONObject.fromObject(strings);
			    	String string2 = fromObjectt.getString("pageQuery");
			    	JSONArray fromObject2 = JSONArray.fromObject(string2);
			    	//System.out.println(fromObject2);
			    	List<InsuranceSZHeiLongJiangUnemployment> list = new ArrayList<InsuranceSZHeiLongJiangUnemployment>();
			    	for (int j = 0; j < fromObject2.size(); j++) {
			    		InsuranceSZHeiLongJiangUnemployment insuranceSZHeiLongJiangUnemployment = new InsuranceSZHeiLongJiangUnemployment();
						JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(j));
						insuranceSZHeiLongJiangUnemployment.setPayDate(fromObject3.getString("col1"));
						insuranceSZHeiLongJiangUnemployment.setPayType(fromObject3.getString("col2"));
						insuranceSZHeiLongJiangUnemployment.setPayBase(fromObject3.getString("col3"));
						insuranceSZHeiLongJiangUnemployment.setMustSum(fromObject3.getString("col4"));
						insuranceSZHeiLongJiangUnemployment.setMustPersonal(fromObject3.getString("col5"));
						insuranceSZHeiLongJiangUnemployment.setMustCompany(fromObject3.getString("col6"));
						insuranceSZHeiLongJiangUnemployment.setFactSum(fromObject3.getString("col7"));
						insuranceSZHeiLongJiangUnemployment.setFactPersonal(fromObject3.getString("col8"));
						insuranceSZHeiLongJiangUnemployment.setFactCompany(fromObject3.getString("col9"));
						insuranceSZHeiLongJiangUnemployment.setPayStatus(fromObject3.getString("col10"));
						//insuranceSZHeiLongJiangUnemployment.setPeopleStatus(fromObject3.getString("col11"));
						insuranceSZHeiLongJiangUnemployment.setTaskid(taskInsurance.getTaskid());
						list.add(insuranceSZHeiLongJiangUnemployment);
					}
			    	webParam.setHtml(strings);
			    	webParam.setList(list);
			    	webParam.setUrl(url);
			    	return webParam;
		    	}
		    }
//		}
		return null;
	}

	
	//工伤
	public WebParam<InsuranceSZHeiLongJiangInjury> getInjury(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskInsurance);
		WebParam<InsuranceSZHeiLongJiangInjury> webParam = new WebParam<InsuranceSZHeiLongJiangInjury>();
		String webdriverHandle =taskInsurance.getWebdriverHandle();//获取登录步骤的webdriverHandle 
//        if(webdriverHandle==null||!webdriverHandle.equals(driver.getWindowHandle())){
//			
//			System.out.println("======================+现在的++++++++++++++++++++++++++"+driver.getWindowHandle());
//			tracer.addTag("action.driver.ERROR", insuranceRequestParameters.getTaskId());
//			insuranceService.changeLoginStatusException(taskInsurance);
//			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
//			tracer.addTag("action.screenShotByPath.creditCard.driver",path);
//		}
//		else
//		{
		    String url="https://www.renshenet.org.cn/sionline/search01/injury01.html?ywdm=search01&parm=injury01";
		    WebRequest  requestSettings1 = new WebRequest(new URL(url), HttpMethod.POST);
		    requestSettings1.setCharset(Charset.forName("UTF-8"));		    
			requestSettings1.setAdditionalHeader("Accept", "*/*");
			requestSettings1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			requestSettings1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings1.setAdditionalHeader("Accept", "*/*");
			requestSettings1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			requestSettings1.setAdditionalHeader("Accept-Language", "zh-CN");
			requestSettings1.setAdditionalHeader("Host", "www.renshenet.org.cn");
			requestSettings1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			requestSettings1.setAdditionalHeader("Referer", "https://www.renshenet.org.cn/sionline/search01/init.html?parm=accountDetails");
			Page page4 = webClient.getPage(requestSettings1);
		    Thread.sleep(2000);
		    if(null != page4)
		    {
		    	String strings = page4.getWebResponse().getContentAsString();
		    	if(strings.contains("pageQuery"))
		    	{
		    		JSONObject fromObjectt = JSONObject.fromObject(strings);
			    	String string2 = fromObjectt.getString("pageQuery");
			    	JSONArray fromObject2 = JSONArray.fromObject(string2);
			    	//System.out.println(fromObject2);
			    	List<InsuranceSZHeiLongJiangInjury> list = new ArrayList<InsuranceSZHeiLongJiangInjury>();
			    	for (int j = 0; j < fromObject2.size(); j++) {
			    		InsuranceSZHeiLongJiangInjury insuranceSZHeiLongJiangInjury = new InsuranceSZHeiLongJiangInjury();
						JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(j));
						insuranceSZHeiLongJiangInjury.setPayDate(fromObject3.getString("col0"));
						insuranceSZHeiLongJiangInjury.setPayType(fromObject3.getString("col1"));
						insuranceSZHeiLongJiangInjury.setPayBase(fromObject3.getString("col2"));
						insuranceSZHeiLongJiangInjury.setMustSum(fromObject3.getString("col3"));
						insuranceSZHeiLongJiangInjury.setMustPersonal(fromObject3.getString("col4"));
						insuranceSZHeiLongJiangInjury.setMustCompany(fromObject3.getString("col5"));
						insuranceSZHeiLongJiangInjury.setFactSum(fromObject3.getString("col6"));
						insuranceSZHeiLongJiangInjury.setFactPersonal(fromObject3.getString("col7"));
						insuranceSZHeiLongJiangInjury.setFactCompany(fromObject3.getString("col8"));
						insuranceSZHeiLongJiangInjury.setPayStatus(fromObject3.getString("col9"));
						//insuranceSZHeiLongJiangInjury.setPeopleStatus(fromObject3.getString("col11"));
						insuranceSZHeiLongJiangInjury.setTaskid(taskInsurance.getTaskid());
						list.add(insuranceSZHeiLongJiangInjury);
					}
			    	webParam.setHtml(strings);
			    	webParam.setList(list);
			    	webParam.setUrl(url);
			    	return webParam;
		    	}
		    }
//		}
		return null;
	}

	//生育
	public WebParam<InsuranceSZHeiLongJiangMaternity> getMaternity(
			InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskInsurance);
		WebParam<InsuranceSZHeiLongJiangMaternity> webParam = new WebParam<InsuranceSZHeiLongJiangMaternity>();
		String webdriverHandle =taskInsurance.getWebdriverHandle();//获取登录步骤的webdriverHandle 
//        if(webdriverHandle==null||!webdriverHandle.equals(driver.getWindowHandle())){
//			
//			System.out.println("======================+现在的++++++++++++++++++++++++++"+driver.getWindowHandle());
//			tracer.addTag("action.driver.ERROR", insuranceRequestParameters.getTaskId());
//			insuranceService.changeLoginStatusException(taskInsurance);
//			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
//			tracer.addTag("action.screenShotByPath.creditCard.driver",path);
//		}
//		else
//		{
		    String url="https://www.renshenet.org.cn/sionline/search01/maternity01.html?ywdm=search01&parm=maternity01";
		    WebRequest  requestSettings1 = new WebRequest(new URL(url), HttpMethod.POST);
		    requestSettings1.setCharset(Charset.forName("UTF-8"));		    
			requestSettings1.setAdditionalHeader("Accept", "*/*");
			requestSettings1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			requestSettings1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings1.setAdditionalHeader("Accept", "*/*");
			requestSettings1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			requestSettings1.setAdditionalHeader("Accept-Language", "zh-CN");
			requestSettings1.setAdditionalHeader("Host", "www.renshenet.org.cn");
			requestSettings1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			requestSettings1.setAdditionalHeader("Referer", "https://www.renshenet.org.cn/sionline/search01/init.html?parm=accountDetails");
			Page page4 = webClient.getPage(requestSettings1);
		    Thread.sleep(2000);
		    if(null != page4)
		    {
		    	String strings = page4.getWebResponse().getContentAsString();
		    	System.out.println(strings.length());
		    	if(strings.contains("pageQuery"))
		    	{
		    		JSONObject fromObjectt = JSONObject.fromObject(strings);
			    	String string2 = fromObjectt.getString("pageQuery");
			    	System.out.println(string2.length());
			    	if(string2.length() != 2)
			    	{
			    		JSONArray fromObject2 = JSONArray.fromObject(string2);
				    	//System.out.println(fromObject2);
				    	List<InsuranceSZHeiLongJiangMaternity> list = new ArrayList<InsuranceSZHeiLongJiangMaternity>();
				    	for (int j = 0; j < fromObject2.size(); j++) {
				    		InsuranceSZHeiLongJiangMaternity insuranceSZHeiLongJiangMaternity = new InsuranceSZHeiLongJiangMaternity();
							JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(j));
							insuranceSZHeiLongJiangMaternity.setPayDate(fromObject3.getString("col1"));
							insuranceSZHeiLongJiangMaternity.setPayType(fromObject3.getString("col2"));
							insuranceSZHeiLongJiangMaternity.setPayBase(fromObject3.getString("col3"));
							insuranceSZHeiLongJiangMaternity.setMustSum(fromObject3.getString("col4"));
							insuranceSZHeiLongJiangMaternity.setMustPersonal(fromObject3.getString("col5"));
							insuranceSZHeiLongJiangMaternity.setMustCompany(fromObject3.getString("col6"));
							insuranceSZHeiLongJiangMaternity.setFactSum(fromObject3.getString("col7"));
							insuranceSZHeiLongJiangMaternity.setFactPersonal(fromObject3.getString("col8"));
							insuranceSZHeiLongJiangMaternity.setFactCompany(fromObject3.getString("col9"));
							insuranceSZHeiLongJiangMaternity.setPayStatus(fromObject3.getString("col10"));
							//insuranceSZHeiLongJiangMaternity.setPeopleStatus(fromObject3.getString("col11"));
							insuranceSZHeiLongJiangMaternity.setTaskid(taskInsurance.getTaskid());
							list.add(insuranceSZHeiLongJiangMaternity);
						}
				    	webParam.setHtml(strings);
				    	webParam.setList(list);
				    	webParam.setUrl(url);
				    	return webParam;
			    	}
		    	}
		    }
//		}
		return null;
	}

}
