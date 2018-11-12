package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.guilin.InsuranceGuiLinMaternity;
import com.microservice.dao.entity.crawler.insurance.guilin.InsuranceGuiLinMedical;
import com.microservice.dao.entity.crawler.insurance.guilin.InsuranceGuiLinPension;
import com.microservice.dao.entity.crawler.insurance.guilin.InsuranceGuiLinUnemployment;
import com.microservice.dao.entity.crawler.insurance.guilin.InsuranceGuiLinUserInfo;
import com.microservice.dao.entity.crawler.insurance.guilin.InsuranceGuiLinuInjury;
import com.microservice.dao.repository.crawler.insurance.guilin.InsuranceGuiLinMaternityRepository;
import com.microservice.dao.repository.crawler.insurance.guilin.InsuranceGuiLinMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.guilin.InsuranceGuiLinPensionRepository;
import com.microservice.dao.repository.crawler.insurance.guilin.InsuranceGuiLinUnemploymentRepository;
import com.microservice.dao.repository.crawler.insurance.guilin.InsuranceGuiLinUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guilin.InsuranceGuiLinuInjuryRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceGuiLinParser {
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private  ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceGuiLinUserInfoRepository insuranceGuiLinUserInfoRepository;
	@Autowired
	private InsuranceGuiLinPensionRepository insuranceGuiLinPensionRepository;
	@Autowired
	private InsuranceGuiLinMaternityRepository insuranceGuiLinMaternityRepository;
	@Autowired
	private InsuranceGuiLinMedicalRepository insuranceGuiLinMedicalRepository;
	@Autowired
	private InsuranceGuiLinuInjuryRepository insuranceGuiLinuInjuryRepository;
	@Autowired
	private InsuranceGuiLinUnemploymentRepository insuranceGuiLinUnemploymentRepository;
	@Autowired
	private TracerLog tracer;
	
	public HtmlPage login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		String url = "http://219.159.250.149:7009/loginPerson.jsp";
		WebClient webClient = WebCrawler.getInstance().getWebClient();		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
//		webClient.getOptions().setTimeout(50000);
		HtmlPage page = webClient.getPage(webRequest);
		HtmlTextInput username = page.getFirstByXPath("//*[@id='c_username']");
		HtmlPasswordInput password = page.getFirstByXPath("//*[@id='c_password']");
		HtmlImage image = page.getFirstByXPath("//*[@id='codeimg']");
    	String code = chaoJiYingOcrService.getVerifycode(image, "1004");
    	HtmlTextInput vfcode = page.getFirstByXPath("//*[@id='checkCode']");
    	username.setText(insuranceRequestParameters.getUsername());
    	password.setText(insuranceRequestParameters.getPassword());
    	vfcode.setText(code);
    	HtmlElement button = page.getFirstByXPath("//*[@class='login2_loginBtn']");
    	HtmlPage page1 = button.click();
    	String u = "http://219.159.250.149:7009/indexAction.do?indexstyle=default";
    	WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.GET);	
    	HtmlPage page2 = webClient.getPage(webRequest1);
    	//System.out.println(page2.getWebResponse().getContentAsString());
    	webClient.close();
		return page2;

	}
	
	public void crawler(TaskInsurance taskInsurance,WebClient webClient) throws Exception {
		String url = "http://219.159.250.149:7009/yhwssb/query/queryInsuranceInfoAction!toSearchPersonInfo.do";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);	
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		//webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "219.159.250.149:7009");
		webRequest.setAdditionalHeader("Origin", "http://219.159.250.149:7009");
		webRequest.setAdditionalHeader("Referer", "http://219.159.250.149:7009/yhwssb/query/queryInsuranceInfoAction!toPersonDetailInfo.do?dto%5B%27lableSelect%27%5D=1&");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"); 
    	Page page = webClient.getPage(webRequest);
    	String html = page.getWebResponse().getContentAsString();
    	System.out.println(html);
    	tracer.addTag("个人信息html", html);
    	userInfo(html,taskInsurance.getTaskid());
    	
    	Calendar now = Calendar.getInstance();
		int endNian = now.get(Calendar.YEAR);
		List<String> list = new ArrayList<String>();
		while(true){
			String urlPay = "http://219.159.250.149:7009/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae002'%5D="+endNian+"&&&&";
			WebRequest webRequest1 = new WebRequest(new URL(urlPay), HttpMethod.POST);	
			webRequest1.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest1.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			webRequest1.setAdditionalHeader("Host", "219.159.250.149:7009");
			webRequest1.setAdditionalHeader("Origin", "http://219.159.250.149:7009");
			webRequest1.setAdditionalHeader("Referer", "http://219.159.250.149:7009/yhwssb/query/queryPersPayAction.do?___businessId=203");
			webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"); 
	    	Page page1 = webClient.getPage(webRequest1);
	    	String html1 = page1.getWebResponse().getContentAsString();
	    	System.out.println(html1);
	    	if(html1.contains("对不起，你查询的月份没有缴费信息，请核对输入时间是否准确")){
	    		break;
	    	}else{
	    		endNian = endNian-1;
	    		list.add(html1);
	    	}
	    	
		}
		tracer.addTag("流水html", list.toString());
		for(String html1:list ){
			pay(html1,taskInsurance.getTaskid());
		}
		//个人信息
    	insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getError_code(), taskInsurance);
		// 养老
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getError_code(), taskInsurance);
		// 生育
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getError_code(), taskInsurance);
		// 工商
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getError_code(), taskInsurance);
		// 医疗
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getError_code(), taskInsurance);
		// 失业
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getError_code(), taskInsurance);
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());		
		
	}
	
	public void userInfo(String html,String taskid){
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject object1 = object.get("fieldData").getAsJsonObject();
		JsonObject object2 = object1.get("info").getAsJsonObject();
		
		InsuranceGuiLinUserInfo userInfo = new InsuranceGuiLinUserInfo();
		String number = object2.get("aac001").toString().replaceAll("\"", "");						    //个人编号
		String name = object2.get("aac003").toString().replaceAll("\"", "");				            //姓名
		String idNum = object2.get("aac002").toString().replaceAll("\"", "");							//身份证号
		String nation = object2.get("aac005").toString().replaceAll("\"", "");                          //民族
		String sex = object2.get("aac004").toString().replaceAll("\"", "");								//性别
		String birth = object2.get("aac006").toString().replaceAll("\"", "");                           //出生日期
		String date = object2.get("aac007").toString().replaceAll("\"", "");                            //参工日期
		String type = object2.get("aac008").toString().replaceAll("\"", "");                            //人员状态
		String phone = object2.get("aae005").toString().replaceAll("\"", "");                           //联系方式
		String address = object2.get("aac010").toString().replaceAll("\"", "");                         //户口所在地
		userInfo.setNumber(number);
		userInfo.setName(name);
		userInfo.setIdNum(idNum);
		userInfo.setNation(nation);
		userInfo.setSex(sex);
		userInfo.setBirth(birth);
		userInfo.setDate(date);
		userInfo.setType(type);
		userInfo.setPhone(phone);
		userInfo.setAddress(address);
		userInfo.setTaskid(taskid);	
		insuranceGuiLinUserInfoRepository.save(userInfo);
	}
	
	public void pay(String html,String taskid){
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject object1 = object.get("lists").getAsJsonObject();
		JsonObject object2 = object1.get("dg_payment").getAsJsonObject();
		JsonArray accountCardList = object2.get("list").getAsJsonArray();
		if (accountCardList.size()>0){
			for (JsonElement acc : accountCardList) {
				JsonObject account = acc.getAsJsonObject();
				String type = account.get("aae140").toString().replaceAll("\"", "");                            //参保险种
				String year = account.get("aae003").toString().replaceAll("\"", "");                            //费款期号
				String base = account.get("aic020").toString().replaceAll("\"", "");                            //缴费工资
				String unitAmount = account.get("dwjnje").toString().replaceAll("\"", "");                      //单位缴费
				String personalAmount = account.get("grjnje").toString().replaceAll("\"", "");                  //个人缴费
				String company = account.get("aab069").toString().replaceAll("\"", "");						    //参保单位
				String sign = account.get("aae114").toString().replaceAll("\"", "");                            //缴费标志
				String paymentType = account.get("aaa115").toString().replaceAll("\"", "");				        //缴费类型
				String payMonth = account.get("aab191").toString().replaceAll("\"", ""); 
				
				int i = base.indexOf(".");
				if(i==0){
					base = "0"+base;
				}
				
				int j = unitAmount.indexOf(".");
				if(j==0){
					unitAmount = "0"+unitAmount;
				}
				
				int k = personalAmount.indexOf(".");
				if(k==0){
					personalAmount = "0"+personalAmount;
				}
				
				
				if(sign.contains("0")){
					sign = "未足额到账";
				}else if(sign.contains("1")){
					sign = "已足额到账";
				}else if(sign.contains("2")){
					sign = "中断";
				}else if(sign.contains("3")){
					sign = "在途";
				}
				
				if(paymentType.contains("10")){
					paymentType = "正常应缴";
				}else if(paymentType.contains("101")){
					paymentType = "老系统卡年费";
				}else if(paymentType.contains("102")){
					paymentType = "老系统当期统筹";
				}else if(paymentType.contains("103")){
					paymentType = "应缴未缴补收";
				}else if(paymentType.contains("104")){
					paymentType = "转入记视同";
				}else if(paymentType.contains("105")){
					paymentType = "转入";
				}else if(paymentType.contains("106")){
					paymentType = "仅补利息";
				}else if(paymentType.contains("108")){
					paymentType = "养老老系统96年1到6月结转利息";
				}else if(paymentType.contains("109")){
					paymentType = "养老建账前缴费认定";
				}else if(paymentType.contains("11")){
					paymentType = "正常应缴（失业代缴）";
				}else if(paymentType.contains("12")){
					paymentType = "正常应缴（基本医疗双基数增收）";
				}else if(paymentType.contains("130")){
					paymentType = "养老老系统封存";
				}else if(paymentType.contains("14")){
					paymentType = "退休划账户";
				}else if(paymentType.contains("15")){
					paymentType = "实缴划拨";
				}else if(paymentType.contains("20")){
					paymentType = "退收";
				}else if(paymentType.contains("21")){
					paymentType = "军转干参战人员退款";
				}else if(paymentType.contains("31")){
					paymentType = "中断或延迟年限补收";
				}else if(paymentType.contains("32")){
					paymentType = "一次性补收";
				}else if(paymentType.contains("33")){
					paymentType = "退休不收";
				}else if(paymentType.contains("34")){
					paymentType = "缴费基数调整补收";
				}else if(paymentType.contains("35")){
					paymentType = "缴费比例调整补收";
				}else if(paymentType.contains("36")){
					paymentType = "补收（失业代缴）";
				}else if(paymentType.contains("37")){
					paymentType = "一次性补收（基本医疗补缴视同年限）";
				}else if(paymentType.contains("40")){
					paymentType = "一次性缴费";
				}else if(paymentType.contains("41")){
					paymentType = "破产清偿人员一次性补收";
				}else if(paymentType.contains("51")){
					paymentType = "零星缴款";
				}else if(paymentType.contains("52")){
					paymentType = "待转金调整";
				}else if(paymentType.contains("53")){
					paymentType = "待转金分配";
				}else if(paymentType.contains("54")){
					paymentType = "退款进待转";
				}else if(paymentType.contains("60")){
					paymentType = "个人补欠费";
				}else if(paymentType.contains("61")){
					paymentType = "基本医疗个账补充核定";
				}else if(paymentType.contains("62")){
					paymentType = "医疗单建转统转统账核定";
				}else if(paymentType.contains("63")){
					paymentType = "单位历史陈欠核定";
				}else if(paymentType.contains("64")){
					paymentType = "基本医疗账户代扣大额";
				}else if(paymentType.contains("71")){
					paymentType = "退休社管费";
				}else if(paymentType.contains("72")){
					paymentType = "学校门诊费";
				}else if(paymentType.contains("88")){
					paymentType = "退款进统筹";
				}else if(paymentType.contains("89")){
					paymentType = "单位特殊核定";
				}else if(paymentType.contains("90")){
					paymentType = "转出";
				}else if(paymentType.contains("99")){
					paymentType = "单位历史陈欠";
				}
				
				if (type.contains("110")){
					type = "城镇企业职工基本养老保险";
					InsuranceGuiLinPension pension = new InsuranceGuiLinPension();
					pension.setType(type);
					pension.setYear(year);
					pension.setBase(base);
					pension.setUnitAmount(unitAmount);
					pension.setPersonalAmount(personalAmount);
					pension.setCompany(company);
					pension.setSign(sign);
					pension.setPaymentType(paymentType);
					pension.setPayMonth(payMonth);
					pension.setTaskid(taskid);
					insuranceGuiLinPensionRepository.save(pension);
				}
				if (type.contains("120")){
					type = "机关事业单位养老保险";
					InsuranceGuiLinPension pension = new InsuranceGuiLinPension();
					pension.setType(type);
					pension.setYear(year);
					pension.setBase(base);
					pension.setUnitAmount(unitAmount);
					pension.setPersonalAmount(personalAmount);
					pension.setCompany(company);
					pension.setSign(sign);
					pension.setPaymentType(paymentType);
					pension.setPayMonth(payMonth);
					pension.setTaskid(taskid);
					insuranceGuiLinPensionRepository.save(pension);
				}
				if (type.contains("210")){
					type = "失业保险";
					InsuranceGuiLinUnemployment unemployment = new InsuranceGuiLinUnemployment();
					unemployment.setType(type);
					unemployment.setYear(year);
					unemployment.setBase(base);
					unemployment.setUnitAmount(unitAmount);
					unemployment.setPersonalAmount(personalAmount);
					unemployment.setCompany(company);
					unemployment.setSign(sign);
					unemployment.setPaymentType(paymentType);
					unemployment.setPayMonth(payMonth);
					unemployment.setTaskid(taskid);
					insuranceGuiLinUnemploymentRepository.save(unemployment);
				}
				if (type.contains("310")){
					type = "城镇职工基本医疗保险";
					InsuranceGuiLinMedical medical = new InsuranceGuiLinMedical();
					medical.setType(type);
					medical.setYear(year);
					medical.setBase(base);
					medical.setUnitAmount(unitAmount);
					medical.setPersonalAmount(personalAmount);
					medical.setCompany(company);
					medical.setSign(sign);
					medical.setPaymentType(paymentType);
					medical.setPayMonth(payMonth);
					medical.setTaskid(taskid);
					insuranceGuiLinMedicalRepository.save(medical);
				}
				if (type.contains("390")){
					type = "城乡居民基本医疗保险";
					InsuranceGuiLinMedical medical = new InsuranceGuiLinMedical();
					medical.setType(type);
					medical.setYear(year);
					medical.setBase(base);
					medical.setUnitAmount(unitAmount);
					medical.setPersonalAmount(personalAmount);
					medical.setCompany(company);
					medical.setSign(sign);
					medical.setPaymentType(paymentType);
					medical.setPayMonth(payMonth);
					medical.setTaskid(taskid);
					insuranceGuiLinMedicalRepository.save(medical);
				}
				if (type.contains("410")){
					type = "工伤保险";
					InsuranceGuiLinuInjury injury = new InsuranceGuiLinuInjury();
					injury.setType(type);
					injury.setYear(year);
					injury.setBase(base);
					injury.setUnitAmount(unitAmount);
					injury.setPersonalAmount(personalAmount);
					injury.setCompany(company);
					injury.setSign(sign);
					injury.setPaymentType(paymentType);
					injury.setPayMonth(payMonth);
					injury.setTaskid(taskid);
					insuranceGuiLinuInjuryRepository.save(injury);
				}
				if (type.contains("510")){
					type = "生育保险";
					InsuranceGuiLinMaternity maternity = new InsuranceGuiLinMaternity();
					maternity.setType(type);
					maternity.setYear(year);
					maternity.setBase(base);
					maternity.setUnitAmount(unitAmount);
					maternity.setPersonalAmount(personalAmount);
					maternity.setCompany(company);
					maternity.setSign(sign);
					maternity.setPaymentType(paymentType);
					maternity.setPayMonth(payMonth);
					maternity.setTaskid(taskid);
					insuranceGuiLinMaternityRepository.save(maternity);
				}
			}
		}
	}
			
}
