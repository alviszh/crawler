package app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.microservice.dao.entity.crawler.insurance.huizhou.InsuranceHuiZhouLostwork;
import com.microservice.dao.entity.crawler.insurance.huizhou.InsuranceHuiZhouMedical;
import com.microservice.dao.entity.crawler.insurance.huizhou.InsuranceHuiZhouUserInfo;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceHuiZhouParser extends AbstractChaoJiYingHandler{

	@Autowired
	private TracerLog tracer;
	
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, WebDriver driver) throws Exception{
		WebParam webParam = new WebParam();
		String baseUrl = "http://113.106.216.244:8003/web/ggfw/app/index.html#/ggfw/grbsxq";
		tracer.addTag("parser.login.url", baseUrl);
		driver.get(baseUrl);
//		driver.manage().window().maximize();
		WebDriverWait wait=new WebDriverWait(driver, 10);
		WebElement notice= wait.until(new ExpectedCondition<WebElement>() {  
            public WebElement apply(WebDriver driver) {  
                return driver.findElement(By.xpath("//span[@class='case_big']"));  
            } 
        });
		
		notice.click();
		Thread.sleep(2500);
		WebElement loginFrame= wait.until(new Function<WebDriver, WebElement>(){  
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
		LOGINID.sendKeys(insuranceRequestParameters.getUsername());
		
		WebElement PASSWORD1 = driver.findElement(By.name("PASSWORD1"));
		PASSWORD1.sendKeys(insuranceRequestParameters.getPassword());
		
		WebElement IMAGCHECK = driver.findElement(By.name("IMAGCHECK"));
		Thread.sleep(1000);
		String path = WebDriverUnit.saveImg(driver, By.id("authImg")); 
		tracer.addTag("parser.login.codeimg.path", path);
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1005", "0", "0", "a", path); 
		tracer.addTag("parser.login.codeimg.chaoJiYingResult", chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		tracer.addTag("parser.login.codeimg.code", code);
		
		WebElement button = driver.findElement(By.className("button"));		
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
		tracer.addTag("parser.login.loginedPage", loginedPageSource);
		
		Document doc = Jsoup.parse(loginedPageSource);
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

	public InsuranceHuiZhouUserInfo getUserInfo(String html, String taskid) {
		tracer.addTag("parser.login.getUserInfo.taskid", taskid);
		tracer.addTag("parser.login.getUserInfo.html", html);
		InsuranceHuiZhouUserInfo  userInfo=new InsuranceHuiZhouUserInfo();
		Document doc = Jsoup.parse(html,"UTF-8");
		html = doc.body().text();
		if(html.contains("message")){
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String flag = list1ArrayObjs.getString("flag");
			if ("1".equals(flag)) {
				String dataStr = list1ArrayObjs.getString("datas");
				JSONObject dateObjs = JSONObject.fromObject(dataStr);
				String user_ncm_gt = dateObjs.getString("ncm_gt_个人基本信息");
				if (null != user_ncm_gt) {
					JSONObject userDatas = JSONObject.fromObject(user_ncm_gt);
					if (null != userDatas) {
						String userParams = userDatas.getString("params");
						JSONObject userObject = JSONObject.fromObject(userParams);
						String institutionName = userObject.getString("所属机构");					
						String institutionNum = userObject.getString("社会保险登记证编码");
						String unitname = userObject.getString("单位名称");					
						String idnum = userObject.getString("证件号码");
						String username = userObject.getString("姓名");					
						String gender = userObject.getString("性别");
						
						String birthdate = userObject.getString("出生日期");					
						String personalState = userObject.getString("个人身份");

						String householdState = userObject.getString("户口性质");					
						String employmenState = userObject.getString("用工性质");
						String firstworkDate = userObject.getString("参加工作日期");					
						String workerType = userObject.getString("职工类别");
					
						String civilserviceType = userObject.getString("公务员类别");					
						String paymentType = userObject.getString("缴费人员类别");
						String persionFirst = userObject.getString("养老保险参保时间");					
						String persionmonthNum = userObject.getString("养老保险实际月数");
						
						String lostFirst = userObject.getString("失业保险参保时间");					
						String lostMonthNum = userObject.getString("失业保险实际月数");
						String contactPersion = userObject.getString("联系人");					
						String contactTel = userObject.getString("联系电话");
						
						String zipcode = userObject.getString("邮政编码");
						String householdSeat = userObject.getString("户口所在地");					
						String liveAddress = userObject.getString("居住地地址");
						userInfo=new InsuranceHuiZhouUserInfo( institutionName,  institutionNum,  unitname,  idnum,
								 username,  gender,  birthdate,  personalState,  householdState,
								 employmenState,  firstworkDate,  workerType,  civilserviceType,  paymentType,
								 persionFirst,  persionmonthNum,  lostFirst,  lostMonthNum,  contactPersion,
								 contactTel,  zipcode,  householdSeat,  liveAddress,  taskid);
				
					}
				}
			}
		}
		return userInfo;
	}

	
	public List<InsuranceHuiZhouMedical> getMedicalList(String html, String taskid) {
		tracer.addTag("parser.login.getMedicalList.taskid", taskid);
		tracer.addTag("parser.login.getMedicalList.html", html);
		Document doc = Jsoup.parse(html,"UTF-8");
		html = doc.body().text();
		List<InsuranceHuiZhouMedical>  medicalList=new ArrayList<InsuranceHuiZhouMedical>();
		if(html.contains("message")){
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String flag = list1ArrayObjs.getString("flag");
			if ("1".equals(flag)) {
				String dataStr = list1ArrayObjs.getString("datas");
				JSONObject dateObjs = JSONObject.fromObject(dataStr);
				String medical_ncm_gt = dateObjs.getString("ncm_glt_医疗保险参保历史");
				if (null != medical_ncm_gt) {
					JSONObject medicalDatas = JSONObject.fromObject(medical_ncm_gt);
					if (null != medicalDatas) {
						String medicalDataStr = medicalDatas.getString("dataset");
						JSONArray medicallistArray = JSONArray.fromObject(medicalDataStr);
						for (int i = 0; i < medicallistArray.size(); i++) {
							JSONObject listArrayObjs = JSONObject.fromObject(medicallistArray.get(i));
							String unitname = listArrayObjs.getString("单位名称");
							String beginmonth = listArrayObjs.getString("开始年月");
							String endmonth = listArrayObjs.getString("终止年月");
							String type = listArrayObjs.getString("险种类型");
							String paybase = listArrayObjs.getString("缴费基数");
							String personalPay = listArrayObjs.getString("个人缴费金额月");
							String unitpersionPay = listArrayObjs.getString("单位划入个人帐户金额月");
							String unitoverallPay = listArrayObjs.getString("单位划入统筹金额/月");
							String medicalPay = listArrayObjs.getString("医保个账金额月");
							InsuranceHuiZhouMedical medical = new InsuranceHuiZhouMedical();
							medical.setUnitname(unitname);
							medical.setBeginmonth(beginmonth);
							medical.setEndmonth(endmonth);
							medical.setType(type);
							medical.setPaybase(paybase);
							medical.setPersonalPay(personalPay);
							medical.setUnitpersionPay(unitpersionPay);
							medical.setUnitoverallPay(unitoverallPay);
							medical.setMedicalPay(medicalPay);
							medical.setTaskid(taskid);
							medicalList.add(medical);
						}
					}
				}
			}	
		}
		return medicalList;
	}
	public List<InsuranceHuiZhouLostwork> getLostworkList(String html, String taskid) {
		tracer.addTag("parser.login.getLostworkList.taskid", taskid);
		tracer.addTag("parser.login.getLostworkList.html", html);
		Document doc = Jsoup.parse(html,"UTF-8");
		html = doc.body().text();
		List<InsuranceHuiZhouLostwork> lostworkList=new ArrayList<InsuranceHuiZhouLostwork>();
		if(html.contains("message")){
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String flag = list1ArrayObjs.getString("flag");
			if ("1".equals(flag)) {
				String dataStr = list1ArrayObjs.getString("datas");
				JSONObject dateObjs = JSONObject.fromObject(dataStr);
				String lostwork_ncm_gt = dateObjs.getString("ncm_glt_失业保险参保历史");
				if (null != lostwork_ncm_gt) {
					JSONObject lostworkDatas = JSONObject.fromObject(lostwork_ncm_gt);
					if (null != lostworkDatas) {
						String lostworkDataStr = lostworkDatas.getString("dataset");
						JSONArray lostworklistArray = JSONArray.fromObject(lostworkDataStr);
						for (int i = 0; i < lostworklistArray.size(); i++) {
							JSONObject listArrayObjs = JSONObject.fromObject(lostworklistArray.get(i));
							String unitname = listArrayObjs.getString("单位名称");
							String beginmonth = listArrayObjs.getString("开始年月");
							String endmonth = listArrayObjs.getString("终止年月");
							String type = listArrayObjs.getString("用工性质");
							String paybase = listArrayObjs.getString("缴费基数");
							String personalPay = listArrayObjs.getString("个人缴费金额/月");
							String unitpersionPay = listArrayObjs.getString("单位缴费金额/月");
							String unitoverallPay = listArrayObjs.getString("单位划入统筹金额/月");
												
							InsuranceHuiZhouLostwork lostwork = new InsuranceHuiZhouLostwork();
							lostwork.setUnitname(unitname);
							lostwork.setBeginmonth(beginmonth);
							lostwork.setEndmonth(endmonth);
							lostwork.setType(type);
							lostwork.setPaybase(paybase);
							lostwork.setPersonalPay(personalPay);
							lostwork.setUnitpersionPay(unitpersionPay);
							lostwork.setUnitoverallPay(unitoverallPay);						
							lostwork.setTaskid(taskid);
							lostworkList.add(lostwork);
						}
					}
				}
			}			
		}
		return lostworkList;
	}

}
