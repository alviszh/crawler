package app.parser;

import java.net.URL;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.microservice.dao.entity.crawler.insurance.wuzhou.InsuranceWuZhouInjury;
import com.microservice.dao.entity.crawler.insurance.wuzhou.InsuranceWuZhouMaternity;
import com.microservice.dao.entity.crawler.insurance.wuzhou.InsuranceWuZhouMedical;
import com.microservice.dao.entity.crawler.insurance.wuzhou.InsuranceWuZhouPension;
import com.microservice.dao.entity.crawler.insurance.wuzhou.InsuranceWuZhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.wuzhou.InsuranceWuZhouUserInfo;
import com.microservice.dao.repository.crawler.insurance.wuzhou.InsuranceWuZhouInjuryRepository;
import com.microservice.dao.repository.crawler.insurance.wuzhou.InsuranceWuZhouMaternityRepository;
import com.microservice.dao.repository.crawler.insurance.wuzhou.InsuranceWuZhouMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.wuzhou.InsuranceWuZhouPensionRepository;
import com.microservice.dao.repository.crawler.insurance.wuzhou.InsuranceWuZhouUnemploymentRepository;
import com.microservice.dao.repository.crawler.insurance.wuzhou.InsuranceWuZhouUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceWuZhouParser {
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private  ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceWuZhouUserInfoRepository insuranceWuZhouUserInfoRepository;
	@Autowired
	private InsuranceWuZhouInjuryRepository insuranceWuZhouInjuryRepository;
	@Autowired
	private InsuranceWuZhouMaternityRepository insuranceWuZhouMaternityRepository;
	@Autowired
	private InsuranceWuZhouMedicalRepository insuranceWuZhouMedicalRepository;
	@Autowired
	private InsuranceWuZhouPensionRepository insuranceWuZhouPensionRepository;
	@Autowired
	private InsuranceWuZhouUnemploymentRepository insuranceWuZhouUnemploymentRepository;
	@Autowired
	private TracerLog tracer;
	public HtmlPage login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		String url = "http://www.gxwz.si.gov.cn:7009/#";
		WebClient webClient = WebCrawler.getInstance().getWebClient();		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		webClient.getOptions().setTimeout(50000);
		HtmlPage page = webClient.getPage(webRequest);
		HtmlTextInput username = page.getFirstByXPath("//*[@id='c_username']");
		HtmlPasswordInput password = page.getFirstByXPath("//*[@id='c_password']");
		HtmlImage image = page.getFirstByXPath("//*[@id='codeimg']");
    	String code = chaoJiYingOcrService.getVerifycode(image, "1004");
    	HtmlTextInput vfcode = page.getFirstByXPath("//*[@id='c_checkCode']");
    	username.setText(insuranceRequestParameters.getUsername());
    	password.setText(insuranceRequestParameters.getPassword());
    	vfcode.setText(code);
    	HtmlElement button = page.getFirstByXPath("//*[@id='login_loginButton']/span");
    	HtmlPage page1 = button.click();
    	String u = "http://www.gxwz.si.gov.cn:7009/indexAction.do?indexstyle=default";
    	WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.GET);	
    	HtmlPage page2 = webClient.getPage(webRequest1);
    	//System.out.println(page2.getWebResponse().getContentAsString());
    	webClient.close();
		return page2;

	}
	
	public void crawler(TaskInsurance taskInsurance,WebClient webClient) throws Exception {
		String url = "http://www.gxwz.si.gov.cn:7009/yhwssb/query/queryInsuranceInfoAction!toSearchPersonInfo.do";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);	
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		//webRequest.setAdditionalHeader("Content-Length", "322");;
		webRequest.setAdditionalHeader("Host", "www.gxwz.si.gov.cn:7009");
		webRequest.setAdditionalHeader("Origin", "http://www.gxwz.si.gov.cn:7009");
		webRequest.setAdditionalHeader("Referer", "http://www.gxwz.si.gov.cn:7009/yhwssb/query/queryInsuranceInfoAction!toPersonDetailInfo.do?dto%5B%27lableSelect%27%5D=1&");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"); 
    	Page page = webClient.getPage(webRequest);
    	String html = page.getWebResponse().getContentAsString();
    	tracer.addTag("个人信息html", html);
    	System.out.println(html);
		userInfo(html,taskInsurance.getTaskid());

		Calendar now = Calendar.getInstance();
		int endNian = now.get(Calendar.YEAR);
		int beginNian = endNian-1;
		int yue = now.get(Calendar.MONTH) +1;
		String yu = null;
		if (yue>9){
			yu = String.valueOf(yue);
		}else{
			yu = "0"+yue;
		}
		String endTime = endNian + yu;
		String beginTime = beginNian +"01";
		String urlPay = "http://www.gxwz.si.gov.cn:7009/yhwssb/query/queryPersPayAction!queryPayment.do?"
				+ "gridInfo%5B'dg_payment_limit'%5D=1000&gridInfo%5B'dg_payment_start'%5D=0&gridInfo%5B'dg_payment_selected'%5D=%5B%5D"
				+ "&gridInfo%5B'dg_payment_modified'%5D=%5B%5D&gridInfo%5B'dg_payment_removed'%5D=%5B%5D&gridInfo%5B'dg_payment_added'%5D=%5B%5D"
				+ "&dto%5B'aae041'%5D="+beginTime+"&dto%5B'aae042'%5D="+endTime+"&dto%5B'aae140'%5D=";
		WebRequest webRequest1 = new WebRequest(new URL(urlPay), HttpMethod.POST);	
		webRequest1.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest1.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest1.setAdditionalHeader("Host", "www.gxwz.si.gov.cn:7009");
		webRequest1.setAdditionalHeader("Origin", "http://www.gxwz.si.gov.cn:7009");
		webRequest1.setAdditionalHeader("Referer", "http://www.gxwz.si.gov.cn:7009/yhwssb/query/queryPersPayAction.do?___businessId=203");
		webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"); 
    	Page page1 = webClient.getPage(webRequest1);
    	String html1 = page1.getWebResponse().getContentAsString();
    	tracer.addTag("流水信息html", html1);
    	System.out.println(html1);
    	pay(html1,taskInsurance.getTaskid());
    	
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
    	insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getDescription(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getError_code(), taskInsurance);
		// 工商
    	insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getError_code(), taskInsurance);
		// 医疗
    	insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getDescription(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getError_code(), taskInsurance);
		// 失业
    	insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getDescription(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getError_code(), taskInsurance);
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());		
	}
	
	public void userInfo(String html,String taskid){
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject object1 = object.get("fieldData").getAsJsonObject();
		JsonObject object2 = object1.get("info").getAsJsonObject();
		//System.out.println("object2:"+object2);
		InsuranceWuZhouUserInfo userInfo = new InsuranceWuZhouUserInfo();
		
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
		insuranceWuZhouUserInfoRepository.save(userInfo);
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
				String unitContriRatio = account.get("dwjfbl").toString().replaceAll("\"", "");                 //单位缴费比例
				String unitAmount = account.get("dwjnje").toString().replaceAll("\"", "");                      //单位缴费
				String personalContriRatio = account.get("grjfbl").toString().replaceAll("\"", "");				//个人缴费比例
				String personalAmount = account.get("grjnje").toString().replaceAll("\"", "");                  //个人缴费
				String company = account.get("aab069").toString().replaceAll("\"", "");						    //参保单位
				String sign = account.get("aae114").toString().replaceAll("\"", "");                            //缴费标志
				String paymentType = account.get("aaa115").toString().replaceAll("\"", "");				        //缴费类型
				String payMonth = account.get("aab191").toString().replaceAll("\"", "");                        //到账日期
				
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
				}else if(paymentType.contains("105")){
					paymentType = "转入";
				}else if(paymentType.contains("106")){
					paymentType = "仅补利息";
				}else if(paymentType.contains("107")){
					paymentType = "异地安置退费";
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
				}else if(paymentType.contains("16")){
					paymentType = "个账代扣大病";
				}else if(paymentType.contains("17")){
					paymentType = "滞纳金分开征收";
				}else if(paymentType.contains("18")){
					paymentType = "退回个人账户代缴大病医疗保险";
				}else if(paymentType.contains("19")){
					paymentType = "追回退休划账户";
				}else if(paymentType.contains("20")){
					paymentType = "退收";
				}else if(paymentType.contains("21")){
					paymentType = "军转干参战人员退款";
				}else if(paymentType.contains("22")){
					paymentType = "灵活就业缴费暂时性封存";
				}else if(paymentType.contains("31")){
					paymentType = "补中断";
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
					paymentType = "单建转统账";
				}else if(paymentType.contains("63")){
					paymentType = "单位历史陈欠核定";
				}else if(paymentType.contains("64")){
					paymentType = "支款给商业保险公司";
				}else if(paymentType.contains("65")){
					paymentType = "财政承担核定";
				}else if(paymentType.contains("66")){
					paymentType = "被征地农民补贴申请";
				}else if(paymentType.contains("67")){
					paymentType = "居民学校统筹划";
				}else if(paymentType.contains("68")){
					paymentType = "个人账户追回";
				}else if(paymentType.contains("69")){
					paymentType = "老干部医疗补助";
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
					type = "基本养老保险";
					InsuranceWuZhouPension pension = new InsuranceWuZhouPension();
					pension.setType(type);
					pension.setYear(year);
					pension.setBase(base);
					pension.setUnitContriRatio(unitContriRatio);
					pension.setUnitAmount(unitAmount);
					pension.setPersonalContriRatio(personalContriRatio);
					pension.setPersonalAmount(personalAmount);
					pension.setCompany(company);
					pension.setSign(sign);
					pension.setPaymentType(paymentType);
					pension.setPayMonth(payMonth);
					pension.setTaskid(taskid);
					insuranceWuZhouPensionRepository.save(pension);
				}
				if (type.contains("210")){
					type = "失业保险";
					InsuranceWuZhouUnemployment unemployment = new InsuranceWuZhouUnemployment();
					unemployment.setType(type);
					unemployment.setYear(year);
					unemployment.setBase(base);
					unemployment.setUnitContriRatio(unitContriRatio);
					unemployment.setUnitAmount(unitAmount);
					unemployment.setPersonalContriRatio(personalContriRatio);
					unemployment.setPersonalAmount(personalAmount);
					unemployment.setCompany(company);
					unemployment.setSign(sign);
					unemployment.setPaymentType(paymentType);
					unemployment.setPayMonth(payMonth);
					unemployment.setTaskid(taskid);
					insuranceWuZhouUnemploymentRepository.save(unemployment);
				}
				if (type.contains("310")){
					type = "基本医疗保险";
					InsuranceWuZhouMedical medical = new InsuranceWuZhouMedical();
					medical.setType(type);
					medical.setYear(year);
					medical.setBase(base);
					medical.setUnitContriRatio(unitContriRatio);
					medical.setUnitAmount(unitAmount);
					medical.setPersonalContriRatio(personalContriRatio);
					medical.setPersonalAmount(personalAmount);
					medical.setCompany(company);
					medical.setSign(sign);
					medical.setPaymentType(paymentType);
					medical.setPayMonth(payMonth);
					medical.setTaskid(taskid);
					insuranceWuZhouMedicalRepository.save(medical);
				}
				if (type.contains("410")){
					type = "工伤保险";
					InsuranceWuZhouInjury injury = new InsuranceWuZhouInjury();
					injury.setType(type);
					injury.setYear(year);
					injury.setBase(base);
					injury.setUnitContriRatio(unitContriRatio);
					injury.setUnitAmount(unitAmount);
					injury.setPersonalContriRatio(personalContriRatio);
					injury.setPersonalAmount(personalAmount);
					injury.setCompany(company);
					injury.setSign(sign);
					injury.setPaymentType(paymentType);
					injury.setPayMonth(payMonth);
					injury.setTaskid(taskid);
					insuranceWuZhouInjuryRepository.save(injury);
				}
				if (type.contains("510")){
					type = "生育保险";
					InsuranceWuZhouMaternity maternity = new InsuranceWuZhouMaternity();
					maternity.setType(type);
					maternity.setYear(year);
					maternity.setBase(base);
					maternity.setUnitContriRatio(unitContriRatio);
					maternity.setUnitAmount(unitAmount);
					maternity.setPersonalContriRatio(personalContriRatio);
					maternity.setPersonalAmount(personalAmount);
					maternity.setCompany(company);
					maternity.setSign(sign);
					maternity.setPaymentType(paymentType);
					maternity.setPayMonth(payMonth);
					maternity.setTaskid(taskid);
					insuranceWuZhouMaternityRepository.save(maternity);
				}
				
				
				
				
				
			}
		}
	}
}
