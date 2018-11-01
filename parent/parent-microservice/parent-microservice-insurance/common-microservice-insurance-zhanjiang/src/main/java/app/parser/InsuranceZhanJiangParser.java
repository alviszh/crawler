package app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.insurance.zhanjiang.InsuranceZhanJiangGeneralInfo;
import com.microservice.dao.entity.crawler.insurance.zhanjiang.InsuranceZhanJiangUserInfo;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceZhanJiangParser extends AbstractChaoJiYingHandler{

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, WebDriver driver) throws Exception{
		WebParam webParam = new WebParam();
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		String baseUrl = "http://219.132.4.6:6012/web/ggfw/app/index.html";
		tracer.addTag("parser.login.url", baseUrl);
		driver.get(baseUrl);
		
		WebDriverWait wait=new WebDriverWait(driver, 10);
		WebElement notice= wait.until(new ExpectedCondition<WebElement>() {  
		            public WebElement apply(WebDriver driver) {  
		                return driver.findElement(By.xpath("//a[@title='cancel']"));  
		            } 
		        });
		notice.click();
		
		WebElement loginFrame= wait.until(new ExpectedCondition<WebElement>() {  
            public WebElement apply(WebDriver driver) {  
                return driver.findElement(By.xpath("//a[@ng-click='login()']"));  
            } 
        });
		loginFrame.click();

		WebElement LOGINID= wait.until(new ExpectedCondition<WebElement>() {  
            public WebElement apply(WebDriver driver) {  
                return driver.findElement(By.name("LOGINID"));  
            } 
        });
		
		WebElement PASSWORD1 = driver.findElement(By.name("PASSWORD1"));
		WebElement IMAGCHECK = driver.findElement(By.name("IMAGCHECK"));
		
		String path = WebDriverUnit.saveImg(driver, By.id("authImg")); 
		tracer.addTag("parser.login.codeimg.path", path);
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", "0", "0", "a", path); 
		tracer.addTag("parser.login.codeimg.chaoJiYingResult", chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		tracer.addTag("parser.login.codeimg.code", code);

		WebElement button = driver.findElement(By.className("button"));
		
		LOGINID.sendKeys(insuranceRequestParameters.getUserIDNum());
		PASSWORD1.sendKeys(insuranceRequestParameters.getPassword());
		IMAGCHECK.sendKeys(code);
		button.click();
		
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
		
		Thread.sleep(1000);
		String loginedPageSource = driver.getPageSource();
		tracer.addTag("parser.login.loginedPage", driver.getPageSource());
		
		Document doc = Jsoup.parse(driver.getPageSource());
		String text = doc.body().select(".ng-binding").text();
		tracer.addTag("parser.login.loginedPage.text", text);
		String msg = text.substring(4);
		if(msg.contains("登录失败")){
			tracer.addTag("parser.login.loginFail.msg", msg);
			webParam.setHtml(msg);
		}else{
			tracer.addTag("parser.login.loginSuccess.msg", msg);
		}
		webParam.setDriver(driver);
		return webParam;
	}

	public WebParam getUserInfo(String html, String taskid) {
		tracer.addTag("parser.login.getUserInfo.taskid", taskid);
		tracer.addTag("parser.login.getUserInfo.html", html);
		WebParam webParam = new WebParam();
		Document doc = Jsoup.parse(html);
		html = doc.body().text();
		if(html.contains("message")){
			JsonParser parser = new JsonParser();
			JsonObject obj = (JsonObject)parser.parse(html);
			int flag = obj.get("flag").getAsInt();
			if(flag == 1){
				JsonObject datas = obj.get("datas").getAsJsonObject();
				JsonObject personInfo = datas.get("ncm_gt_个人基本资料").getAsJsonObject();
				if(null != personInfo){
					JsonObject params = personInfo.get("params").getAsJsonObject();
					if(null != params){
						String idNum = params.get("证件号码").getAsString();
						String userName = params.get("姓名").getAsString();
						String isSpecial = params.get("特殊人员与否").getAsString();
						String institutionName = params.get("社保机构").getAsString();
						String institutionNum = params.get("组织ID").getAsString();
						String relation = params.get("与户主关系").getAsString();
						String familyNum = params.get("单位编号").getAsString();
						String unitType = params.get("单位类型").getAsString();
						String specialType = params.get("特殊人员类别").getAsString();
						String gender = params.get("性别").getAsString();
						String hostName = params.get("单位名称").getAsString();
						String personalNum = params.get("个人顺序号").getAsString();
						String personalType = params.get("个人参保状态").getAsString();
						String payType = params.get("缴费方式").getAsString();
						String address = params.get("家庭户地址").getAsString();
						String birthday = params.get("出生日期").getAsString();
						
						InsuranceZhanJiangUserInfo userInfo = new InsuranceZhanJiangUserInfo();
						List<InsuranceZhanJiangUserInfo> userInfos = new ArrayList<InsuranceZhanJiangUserInfo>();
						
						userInfo.setTaskid(taskid);
						userInfo.setIdNum(idNum);
						userInfo.setUserName(userName);
						userInfo.setIsSpecial(isSpecial);
						userInfo.setInstitutionName(institutionName);
						userInfo.setInstitutionNum(institutionNum);
						userInfo.setRelation(relation);
						userInfo.setFamilyNum(familyNum);
						userInfo.setUnitType(unitType);
						userInfo.setSpecialType(specialType);
						userInfo.setGender(gender);
						userInfo.setHostName(hostName);
						userInfo.setPersonalNum(personalNum);
						userInfo.setPersonalType(personalType);
						userInfo.setPayType(payType);
						userInfo.setAddress(address);
						userInfo.setBirthday(birthday);
						
						userInfos.add(userInfo);
						webParam.setList(userInfos);
					}
				}
			}
		}
		return webParam;
	}

	public WebParam getGeneralInfo(String html, String taskid, int i) {
		tracer.addTag("parser.login.getGeneralInfo."+i+".taskid", taskid);
		tracer.addTag("parser.login.getGeneralInfo."+i+".html", html);
		WebParam webParam = new WebParam();
		Document doc = Jsoup.parse(html);
		html = doc.body().text();
		if(html.contains("message")){
			JsonParser parser = new JsonParser();
			JsonObject obj = (JsonObject)parser.parse(html);
			int flag = obj.get("flag").getAsInt();
			if(flag == 1){
				JsonObject datas = obj.get("datas").getAsJsonObject();
				JsonObject details = datas.get("ncm_glt_个人已缴历史明细").getAsJsonObject();
				JsonArray dataset = details.get("dataset").getAsJsonArray();
				if(null != dataset && dataset.size() > 0){
					List<InsuranceZhanJiangGeneralInfo> generalInfos = new ArrayList<InsuranceZhanJiangGeneralInfo>();
					for (JsonElement data : dataset) {
						JsonObject object = data.getAsJsonObject();
						String personalPay = object.get("个人缴费金额").getAsString();
						String accountMoney = object.get("划入账户金额").getAsString();
						String idNum = object.get("证件号码").getAsString();
						String payType = object.get("应缴类型").getAsString();
						String overallMoney = object.get("划入统筹金额").getAsString();
						String userName = object.get("姓名").getAsString();
						String payDate = object.get("缴费年月").getAsString();
						String checkDate = object.get("审核年月").getAsString();
						String insuranceType = object.get("险种类型").getAsString();
						String payBase = object.get("缴费基数").getAsString();
						String companyPay = object.get("单位缴费金额").getAsString();
						
						InsuranceZhanJiangGeneralInfo generalInfo = new InsuranceZhanJiangGeneralInfo();
						
						generalInfo.setTaskid(taskid);
						generalInfo.setPersonalPay(personalPay);
						generalInfo.setAccountMoney(accountMoney);
						generalInfo.setIdNum(idNum);
						generalInfo.setPayType(payType);
						generalInfo.setOverallMoney(overallMoney);
						generalInfo.setUserName(userName);
						generalInfo.setPayDate(payDate);
						generalInfo.setCheckDate(checkDate);
						generalInfo.setInsuranceType(insuranceType);
						generalInfo.setPayBase(payBase);
						generalInfo.setCompanyPay(companyPay);
						
						generalInfos.add(generalInfo);
					}
					webParam.setList(generalInfos);
				}
			}
		}
		return webParam;
	}
}
