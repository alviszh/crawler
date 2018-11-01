package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.insurance.sz.guangxi.InsuranceSZGuangXiUserInfo;
import com.microservice.dao.entity.crawler.insurance.sz.guangxi.InsuranceSZGuangXibasictypes;
import com.microservice.dao.entity.crawler.insurance.sz.guangxi.InsuranceSZGuangXipaydetails;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.InsuranceService;

@Component
public class InsuranceSZGuangXiParser extends AbstractChaoJiYingHandler{
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, WebDriver driver){
		WebParam webParam = new WebParam();
		webParam.setCode(0);	
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		//driver.manage().window().maximize();
		String baseUrl = "https://gx12333.net/index/loginInit.html";
		tracer.addTag("parser.login.url", baseUrl);
		driver.get(baseUrl);
		
		WebDriverWait wait=new WebDriverWait(driver, 10);
		WebElement username= wait.until(new ExpectedCondition<WebElement>() {  
            public WebElement apply(WebDriver driver) {  
                return driver.findElement(By.id("aac002_t"));  
            } 
        });		
		WebElement password = driver.findElement(By.id("aac003_t"));
		WebElement imageCode = driver.findElement(By.id("yzm_t"));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		String path;
		try {
			path = WebDriverUnit.saveImg(driver, By.id("codeimg"));
			tracer.addTag("parser.login.codeimg.path", path);
			String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", "0", "0", "a", path); 
			tracer.addTag("parser.login.codeimg.chaoJiYingResult", chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
			tracer.addTag("parser.login.codeimg.code", code);

			WebElement button = driver.findElement(By.xpath("//button[@type='button']"));	
			username.sendKeys(insuranceRequestParameters.getUsername());
			password.sendKeys(insuranceRequestParameters.getPassword());
			imageCode.sendKeys(code);
			button.click();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	
		Alert alert = null;
		try {
			alert = wait.until(new Function<WebDriver, Alert>() {  
	            public Alert apply(WebDriver driver) {  
	                return driver.switchTo().alert(); 
	            }
	        });
		} catch (Exception e) {
			tracer.addTag("parser.login.alert.none", "无登陆成功提示弹框。");
		}
		
		if(null != alert){
			String alertText = alert.getText();
			tracer.addTag("parser.login.alert.text", alertText);
			alert.accept();
		}		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String loginedPageSource = driver.getPageSource();
		webParam.setHtml(loginedPageSource);
		tracer.addTag("parser.login.loginedPage", loginedPageSource);	
		if(loginedPageSource.contains("您好")){
			tracer.addTag("parser.login.loginSuccess", loginedPageSource);
			webParam.setCode(1);
			webParam.setDriver(driver);
			// 获取cookies
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
			Set<CookieJson> cookiesSet = new HashSet<CookieJson>();
			for (org.openqa.selenium.Cookie cookie : cookies) {
				CookieJson cookiejson = new CookieJson();
				cookiejson.setDomain(cookie.getDomain());
				cookiejson.setKey(cookie.getName());
				cookiejson.setValue(cookie.getValue());
				cookiesSet.add(cookiejson);
			}
			String cookieJson = new Gson().toJson(cookiesSet);
			// 借用个url传递cookie
			webParam.setCookies(cookieJson);
		}else{
			tracer.addTag("parser.login.loginSuccess", loginedPageSource);
		}
		return webParam;
	}
	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getUserInfoUnit(InsuranceRequestParameters insuranceRequestParameters, Set<Cookie> cookies) {	
		tracer.addTag("getUserInfoUnit",insuranceRequestParameters.getTaskId());
		WebParam webParam= new WebParam();	
		try {
			WebClient webClient = insuranceService.getWebClient(cookies);
			String url = "https://gx12333.net/wxService/queryBaseInfo.html";	
			Page	page = getHtml(url,webClient);
			int statusCode = page.getWebResponse().getStatusCode();
	    	String html = page.getWebResponse().getContentAsString();
	     	webParam.setCode(statusCode);    
	    	webParam.setUrl(url);
	    	webParam.setHtml(html); 	
	      	tracer.addTag("getUserInfoUnit个人信息","<xmp>"+html+"</xmp>");
		    if(200 == statusCode){
		    	if (html.contains("人员基本信息")) {
		    		InsuranceSZGuangXiUserInfo userInfo = getUserInfoParse(html,insuranceRequestParameters.getTaskId());	
		    		webParam.setUserInfo(userInfo); 
				}	         		    
		   
		    }
		} catch (Exception e) {
	     	tracer.addTag("getUserInfoUnit.Exception",e.toString());
			e.printStackTrace();
		}	
	
		return webParam;
	}

	public InsuranceSZGuangXiUserInfo getUserInfoParse(String html, String taskid) {
		tracer.addTag("parser.login.getUserInfo.taskid", taskid);
		tracer.addTag("parser.login.getUserInfo.html", html);
		Document doc = Jsoup.parse(html, "UTF-8");
		InsuranceSZGuangXiUserInfo  userInfo=new InsuranceSZGuangXiUserInfo();
		if (html.contains("人员基本信息")) {
			String username = getNextLabelByKeyword(doc, "姓名"); // 姓名
			String idnum = getNextLabelByKeyword(doc, "身份证号"); // 证件号码
			String gender = getNextLabelByKeyword(doc, "性别"); // 性别
			String nation = getNextLabelByKeyword(doc, "民族"); // 民族
			String householdAddress = getNextLabelByKeyword(doc, "户籍地址"); // 户籍地址
			String companyname = getNextLabelByKeyword(doc, "单位名称"); // 单位名称
			String firstworkDate = getNextLabelByKeyword(doc, "参加工作日期"); // 参加工作日期
			String identity = getNextLabelByKeyword(doc, "个人身份"); // 个人身份
			String insuranceState = getNextLabelByKeyword(doc, "参保状态"); // 参保状态
			String insuranceNum = getNextLabelByKeyword(doc, "社保卡号"); // 社保卡号
			userInfo = new InsuranceSZGuangXiUserInfo(username, idnum, gender, nation,
					householdAddress, companyname, firstworkDate, identity, insuranceState, insuranceNum, taskid);
		}
		return userInfo;
	}
	/**
	 * @param document
	 * @param keyword
	 * @return
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 */
	public String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("label:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

	/**
	 * @Des 获取缴纳保险类型信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getBasicinfoUnit(InsuranceRequestParameters insuranceRequestParameters, Set<Cookie> cookies) {	
		tracer.addTag("getBasicinfoUnit",insuranceRequestParameters.getTaskId());
		WebParam webParam= new WebParam();
		try {
			WebClient webClient = insuranceService.getWebClient(cookies);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setCssEnabled(false);
			String body="aae140=0";
			String url = "https://gx12333.net/wxService/queryInsuInfoAfter.html";
			WebRequest webRequest=  new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Host", "gx12333.net");
			webRequest.setAdditionalHeader("Origin", "ttps://gx12333.net");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
			webRequest.setRequestBody(body);
			Page page=webClient.getPage(webRequest);
			int statusCode = page.getWebResponse().getStatusCode();
	    	String html = page.getWebResponse().getContentAsString();
	     	webParam.setCode(statusCode);    
	    	webParam.setUrl(url);
	    	webParam.setHtml(html); 	
	      	tracer.addTag("getBasicinfoUnit保险类型","<xmp>"+html+"</xmp>");
		    if(200 == statusCode){
		    	if (html.contains("ylzps")) {
		    		List<InsuranceSZGuangXibasictypes>  basictypeList = getInsuranceTypesParse(html,insuranceRequestParameters.getTaskId());	
		    		webParam.setBasictypeList(basictypeList);
				}	         		    
		    }
		} catch (Exception e) {
	     	tracer.addTag("getBasicinfoUnit.Exception",e.toString());
			e.printStackTrace();
		}
	
		return webParam;
	}
	
	public List<InsuranceSZGuangXibasictypes> getInsuranceTypesParse(String html, String taskid) {
		tracer.addTag("parser.login.getInsuranceTypesParse.taskid", taskid);
		tracer.addTag("parser.login.getInsuranceTypesParse.html", html);
		List<InsuranceSZGuangXibasictypes> typesList = new ArrayList<InsuranceSZGuangXibasictypes>();
		if (html.contains("ylzps")) {
			Document doc = Jsoup.parse(html, "UTF-8");
			Element table = doc.getElementById("ylzps");
			if (null != table) {
				Elements trs = table.select("tbody").first().select("tr");
				int trs_size = trs.size();
				if (trs_size > 1) {
					for (int i = 0; i < trs.size(); i++) {
						Elements tds = trs.get(i).select("td");
						String companyname = tds.get(0).text();
						String type = tds.get(1).text();
						String state = tds.get(2).text();
						String beginmonth = tds.get(3).text();
						String endmonth = tds.get(4).text();
						InsuranceSZGuangXibasictypes types = new InsuranceSZGuangXibasictypes(companyname, type, state,
								beginmonth, endmonth, taskid);
						typesList.add(types);
					}
				}
			}
		}
		return typesList;
	}
	
	/**
	 * @Des 获取缴纳保险明细信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getPaydetailsUnit(InsuranceRequestParameters insuranceRequestParameters, Set<Cookie> cookies,String month) {	
		tracer.addTag("getPaydetailsUnit保险明细"+month,insuranceRequestParameters.getTaskId());
		WebParam webParam= new WebParam();
		try {
			WebClient webClient = insuranceService.getWebClient(cookies);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setCssEnabled(false);
			String url="https://gx12333.net/wxService/queryJFXX.html";	                    
		  	String body="aae140=&aae003="+month;
			WebRequest webRequest=  new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
		   	webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
	     	webRequest.setAdditionalHeader("Host", "gx12333.net");
	     	webRequest.setAdditionalHeader("Origin", "https://gx12333.net");
	     	webRequest.setAdditionalHeader("Referer", "ttps://gx12333.net/wxService/queryJFXXBe.html");	
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
			webRequest.setRequestBody(body);		
			Page page=webClient.getPage(webRequest);
			int statusCode = page.getWebResponse().getStatusCode();
	    	String html = page.getWebResponse().getContentAsString();
	     	tracer.addTag("getPaydetailsUnit返回结果html"+month,"<xmp>"+html+"</xmp>");
	     	webParam.setCode(statusCode);    
	    	webParam.setUrl(url);
	    	webParam.setHtml(html);  	
			if (200 == statusCode) {
				if (html.contains("ylzps")) {
					List<InsuranceSZGuangXipaydetails> paydetilsList = getPaydetailsParse(html,
							insuranceRequestParameters.getTaskId());
					webParam.setPaydetailsList(paydetilsList);
				}
			}
		} catch (Exception e) {
			tracer.addTag("getPaydetailsUnit.Exception"+month,e.toString());
			e.printStackTrace();
		}		
		return webParam;
	}
	
	
	public List<InsuranceSZGuangXipaydetails> getPaydetailsParse(String html, String taskid) {
		tracer.addTag("parser.login.getPaydetailsParse.taskid", taskid);
		tracer.addTag("parser.login.getPaydetailsParse.html", html);
		List<InsuranceSZGuangXipaydetails> paydetilsList=new ArrayList<InsuranceSZGuangXipaydetails>();
		if (html.contains("ylzps")) {
			Document doc = Jsoup.parse(html, "UTF-8");
			Element table = doc.getElementById("ylzps");
			if (null != table) {
				Elements trs = table.select("tbody").first().select("tr");
				int trs_size = trs.size();
				if (trs_size > 1) {
					for (int i = 0; i < trs.size(); i++) {
						Elements tds = trs.get(i).select("td");
						String companyname = tds.get(0).text();// 单位名称
						String paymonth = tds.get(1).text();// 费款所属年月
						String type = tds.get(2).text();// 险种类型
						String paybase = tds.get(3).text();// 缴费基数
						String persionpayAmount = tds.get(4).text();// 个人数
						String unitpayAmount = tds.get(0).text();// 单位数
						String personalPay = tds.get(1).text();// 划入个人账号金额
						String unitPay = tds.get(2).text();// 单位缴费标志
						String unitpaySign = tds.get(3).text();// 单位缴费标志
						String persionpaySign = tds.get(4).text();// 单位缴费标志
						InsuranceSZGuangXipaydetails paydetail = new InsuranceSZGuangXipaydetails(companyname, paymonth,
								type, paybase, persionpayAmount, unitpayAmount, personalPay, unitPay, unitpaySign,
								persionpaySign, taskid);
						paydetilsList.add(paydetail);
					}
				}
			}
		}
		return paydetilsList;
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
