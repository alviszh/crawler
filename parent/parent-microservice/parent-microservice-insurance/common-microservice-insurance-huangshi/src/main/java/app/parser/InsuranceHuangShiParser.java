package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.huangshi.InsuranceHuangShiPayinfo;
import com.microservice.dao.entity.crawler.insurance.huangshi.InsuranceHuangShiUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceHuangShiParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		tracer.addTag("crawler.login.parser.taskid", insuranceRequestParameters.getTaskId());
		WebParam webParam = new WebParam();
		int area = 1;
		if(insuranceRequestParameters.getCity().equals("黄石")){
			area = 1;
		}else if(insuranceRequestParameters.getCity().equals("大冶")){
			area = 2;
		}else if(insuranceRequestParameters.getCity().equals("阳新")){
			area = 3;
		}
		String loginUrl = "http://wsfw.hs12333.gov.cn/login.ered?reqCode=queryPerson&cardno="+insuranceRequestParameters.getUserIDNum()+"&area="+area;
		tracer.addTag("crawler.login.parser.loginUrl", loginUrl);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		Page loginPage = webClient.getPage(webRequest);
		tracer.addTag("crawler.login.parser.loginPage", loginPage.getWebResponse().getContentAsString());
		if(loginPage.getWebResponse().getContentAsString().contains("success")){
			JsonParser parser = new JsonParser();
			JsonObject obj = (JsonObject)parser.parse(loginPage.getWebResponse().getContentAsString());
			boolean success = obj.get("success").getAsBoolean();
			String personIds = obj.get("personIds").getAsString();
			if(!success){
				webParam.setHtml(personIds);
			}else if(personIds.trim().equals("")){
				webParam.setHtml("不存在该身份证对应的参保人资料。");
			}else{
				String loginUrl2 = "http://wsfw.hs12333.gov.cn/login.ered?reqCode=login&cardno="+insuranceRequestParameters.getUserIDNum()+"&password="+insuranceRequestParameters.getPassword()+"&persionId="+personIds+"&area="+area;
				tracer.addTag("crawler.login.parser.loginUrl2", loginUrl2);
				webRequest = new WebRequest(new URL(loginUrl2), HttpMethod.GET);
				HtmlPage loginPage2 = webClient.getPage(webRequest);
				tracer.addTag("crawler.login.parser.loginPage2", loginPage2.getWebResponse().getContentAsString());
				JsonObject object = (JsonObject)parser.parse(loginPage2.getWebResponse().getContentAsString());
				boolean b = object.get("success").getAsBoolean();
				if(b){
					tracer.addTag("crawler.login.parser.success", "登陆成功！");
					webParam.setHtmlPage(loginPage2);
				}else{
					String msg = object.get("msg").getAsString();
					webParam.setHtml(msg);
				}
			}
		}else{
			webParam.setHtml("网络异常，请您稍后重试！");
		}
		return webParam;
	}
	public WebParam getUserInfo(TaskInsurance taskInsurance) throws Exception{
		tracer.addTag("crawler.parser.getUserInfo.taskid", taskInsurance.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String userinfoUrl = "http://wsfw.hs12333.gov.cn/person.ered?reqCode=Init1";
		webParam.setUrl(userinfoUrl);
		tracer.addTag("crawler.parser.getUserInfo.userinfoUrl", userinfoUrl);
		WebRequest webRequest = new WebRequest(new URL(userinfoUrl), HttpMethod.GET);
		HtmlPage userinfoPage = webClient.getPage(webRequest);
		webParam.setHtml(userinfoPage.asXml());
		tracer.addTag("crawler.parser.getUserInfo.userinfoPage", "<xmp>"+userinfoPage.asXml()+"</xmp>");
		Document doc = Jsoup.parse(userinfoPage.asXml());
		Element tb = doc.getElementById("tb");
		if(null != tb && tb.select("tr").size() > 1){
			List<InsuranceHuangShiUserInfo> userInfos = new ArrayList<InsuranceHuangShiUserInfo>();
			InsuranceHuangShiUserInfo userInfo = new InsuranceHuangShiUserInfo();
			
			String personalNum = getNextLabelByKeyword(tb, "td", "个人编号");
			String idNum = getNextLabelByKeyword(tb, "td", "公民身份号码");
			String oldNum = getNextLabelByKeyword(tb, "td", "原编号");
			String name = getNextLabelByKeyword(tb, "td", "姓名");
			String gender = getNextLabelByKeyword(tb, "td", "性别");
			String birthday = getNextLabelByKeyword(tb, "td", "出生日期");
			String workDate = getNextLabelByKeyword(tb, "td", "参加工作日期");
			String residentType = getNextLabelByKeyword(tb, "td", "户口性质");
			String companyNum = getNextLabelByKeyword(tb, "td", "单位编号");
			String companyName = getNextLabelByKeyword(tb, "td", "单位名称");
			String workStatus = getNextLabelByKeyword(tb, "td", "人员状态");
			String specialPeopleType = getNextLabelByKeyword(tb, "td", "特殊人群类别");
			
			userInfo.setTaskid(taskInsurance.getTaskid());
			userInfo.setPersonalNum(personalNum);
			userInfo.setIdNum(idNum);
			userInfo.setOldNum(oldNum);
			userInfo.setName(name);
			userInfo.setGender(gender);
			userInfo.setBirthday(birthday);
			userInfo.setWorkDate(workDate);
			userInfo.setResidentType(residentType);
			userInfo.setCompanyNum(companyNum);
			userInfo.setCompanyName(companyName);
			userInfo.setWorkStatus(workStatus);
			userInfo.setSpecialPeopleType(specialPeopleType);
			userInfos.add(userInfo);
			webParam.setList(userInfos);
		}
		return webParam;
	}
	
	public WebParam getPensionInfo(TaskInsurance taskInsurance) throws Exception{
		tracer.addTag("crawler.parser.getPensionInfo.taskid", taskInsurance.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String pensionInfoUrl = "http://wsfw.hs12333.gov.cn/cost.ered?reqCode=findList&start=0&limit=500&OID=c42907ec3cf2462f931624745ce378cf&type=11";
		webParam.setUrl(pensionInfoUrl);
		tracer.addTag("crawler.parser.getPensionInfo.pensionInfoUrl", pensionInfoUrl);
		
		WebRequest webRequest = new WebRequest(new URL(pensionInfoUrl), HttpMethod.GET);
		Page pensionInfoPage = webClient.getPage(webRequest);
		
		webParam.setHtml(pensionInfoPage.getWebResponse().getContentAsString());
		tracer.addTag("crawler.parser.getPensionInfo.pensionInfoPage", pensionInfoPage.getWebResponse().getContentAsString());
		
		List<InsuranceHuangShiPayinfo> payinfos = new ArrayList<>();
		if(pensionInfoPage.getWebResponse().getContentAsString().contains("TOTALCOUNT")){
			payinfos = getPayInfo(payinfos, pensionInfoPage.getWebResponse().getContentAsString(), taskInsurance.getTaskid());
		}
		webParam.setList(payinfos);
		return webParam;
	}
	
	public WebParam getMedicalInfo(TaskInsurance taskInsurance) throws Exception{
		tracer.addTag("crawler.parser.getMedicalInfo.taskid", taskInsurance.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String medicalInfoUrl = "http://wsfw.hs12333.gov.cn/cost.ered?reqCode=findList&start=0&limit=500&OID=c42907ec3cf2462f931624745ce378cf&type=31";
		String medicalInfoUrl2 = "http://wsfw.hs12333.gov.cn/cost.ered?reqCode=findList&start=0&limit=500&OID=c42907ec3cf2462f931624745ce378cf&type=32";
		webParam.setUrl(medicalInfoUrl+medicalInfoUrl2);
		tracer.addTag("crawler.parser.getMedicalInfo.medicalInfoUrl", medicalInfoUrl);
		tracer.addTag("crawler.parser.getMedicalInfo.medicalInfoUrl2", medicalInfoUrl2);
		
		WebRequest webRequest = new WebRequest(new URL(medicalInfoUrl), HttpMethod.GET);
		Page medicalInfoPage = webClient.getPage(webRequest);
		
		webRequest = new WebRequest(new URL(medicalInfoUrl2), HttpMethod.GET);
		Page medicalInfoPage2 = webClient.getPage(webRequest);
		
		webParam.setHtml(medicalInfoPage.getWebResponse().getContentAsString()+medicalInfoPage2.getWebResponse().getContentAsString());
		tracer.addTag("crawler.parser.getMedicalInfo.medicalInfoPage", medicalInfoPage.getWebResponse().getContentAsString());
		tracer.addTag("crawler.parser.getMedicalInfo.medicalInfoPage2", medicalInfoPage2.getWebResponse().getContentAsString());
		
		List<InsuranceHuangShiPayinfo> payinfos = new ArrayList<>();
		if(medicalInfoPage.getWebResponse().getContentAsString().contains("TOTALCOUNT")){
			payinfos = getPayInfo(payinfos, medicalInfoPage.getWebResponse().getContentAsString(), taskInsurance.getTaskid());
		}
		if(medicalInfoPage2.getWebResponse().getContentAsString().contains("TOTALCOUNT")){
			payinfos = getPayInfo(payinfos, medicalInfoPage2.getWebResponse().getContentAsString(), taskInsurance.getTaskid());
		}
		webParam.setList(payinfos);
		return webParam;
	}
	
	public WebParam getBearInfo(TaskInsurance taskInsurance) throws Exception{
		tracer.addTag("crawler.parser.getBearInfo.taskid", taskInsurance.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String bearInfoUrl = "http://wsfw.hs12333.gov.cn/cost.ered?reqCode=findList&start=0&limit=500&OID=c42907ec3cf2462f931624745ce378cf&type=51";
		webParam.setUrl(bearInfoUrl);
		tracer.addTag("crawler.parser.getBearInfo.bearInfoUrl", bearInfoUrl);
		
		WebRequest webRequest = new WebRequest(new URL(bearInfoUrl), HttpMethod.GET);
		Page bearInfoPage = webClient.getPage(webRequest);
		
		webParam.setHtml(bearInfoPage.getWebResponse().getContentAsString());
		tracer.addTag("crawler.parser.getBearInfo.bearInfoPage", bearInfoPage.getWebResponse().getContentAsString());
		
		List<InsuranceHuangShiPayinfo> payinfos = new ArrayList<>();
		if(bearInfoPage.getWebResponse().getContentAsString().contains("TOTALCOUNT")){
			payinfos = getPayInfo(payinfos, bearInfoPage.getWebResponse().getContentAsString(), taskInsurance.getTaskid());
		}
		webParam.setList(payinfos);
		return webParam;
	}

	public WebParam getInjuryInfo(TaskInsurance taskInsurance) throws Exception{
		tracer.addTag("crawler.parser.getInjuryInfo.taskid", taskInsurance.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String injuryInfoUrl = "http://wsfw.hs12333.gov.cn/cost.ered?reqCode=findList&start=0&limit=500&OID=c42907ec3cf2462f931624745ce378cf&type=41";
		webParam.setUrl(injuryInfoUrl);
		tracer.addTag("crawler.parser.getInjuryInfo.injuryInfoUrl", injuryInfoUrl);
		
		WebRequest webRequest = new WebRequest(new URL(injuryInfoUrl), HttpMethod.GET);
		Page injuryInfoPage = webClient.getPage(webRequest);
		
		webParam.setHtml(injuryInfoPage.getWebResponse().getContentAsString());
		tracer.addTag("crawler.parser.getInjuryInfo.injuryInfoPage", injuryInfoPage.getWebResponse().getContentAsString());
		
		List<InsuranceHuangShiPayinfo> payinfos = new ArrayList<>();
		if(injuryInfoPage.getWebResponse().getContentAsString().contains("TOTALCOUNT")){
			payinfos = getPayInfo(payinfos, injuryInfoPage.getWebResponse().getContentAsString(), taskInsurance.getTaskid());
		}
		webParam.setList(payinfos);
		return webParam;
	}
	
	public WebParam getUnemploymentInfo(TaskInsurance taskInsurance) throws Exception{
		tracer.addTag("crawler.parser.getUnemploymentInfo.taskid", taskInsurance.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String unemploymentInfoUrl = "http://wsfw.hs12333.gov.cn/cost.ered?reqCode=findList&start=0&limit=500&OID=c42907ec3cf2462f931624745ce378cf&type=21";
		webParam.setUrl(unemploymentInfoUrl);
		tracer.addTag("crawler.parser.getUnemploymentInfo.unemploymentInfoUrl", unemploymentInfoUrl);
		
		WebRequest webRequest = new WebRequest(new URL(unemploymentInfoUrl), HttpMethod.GET);
		Page unemploymentInfoPage = webClient.getPage(webRequest);
		
		webParam.setHtml(unemploymentInfoPage.getWebResponse().getContentAsString());
		tracer.addTag("crawler.parser.getUnemploymentInfo.unemploymentInfoPage", unemploymentInfoPage.getWebResponse().getContentAsString());
		
		List<InsuranceHuangShiPayinfo> payinfos = new ArrayList<>();
		if(unemploymentInfoPage.getWebResponse().getContentAsString().contains("TOTALCOUNT")){
			payinfos = getPayInfo(payinfos, unemploymentInfoPage.getWebResponse().getContentAsString(), taskInsurance.getTaskid());
		}
		webParam.setList(payinfos);
		return webParam;
	}
	
	public List<InsuranceHuangShiPayinfo> getPayInfo(List<InsuranceHuangShiPayinfo> payinfos, String html, String taskid){
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject)parser.parse(html);
		JsonArray root = obj.get("ROOT").getAsJsonArray();
		if(null != root && root.size() > 0){
			for (JsonElement jsonElement : root) {
				InsuranceHuangShiPayinfo payinfo = new InsuranceHuangShiPayinfo();
				
				JsonObject object = jsonElement.getAsJsonObject();
				String companyName = object.get("3").getAsString();
				String insuranceType = object.get("4").getAsString();
				String accountDate = object.get("5").getAsString();
				String feeBelongsDate = object.get("6").getAsString();
				String payBase = object.get("7").getAsString();
				String feeType = object.get("8").getAsString();
				String feeSource = object.get("9").getAsString();
				String payType = object.get("10").getAsString();
				String thisPay = object.get("11").getAsString();
				String toPersonalAccountMoney = object.get("12").getAsString();
				String payMark = object.get("13").getAsString();
				String arriveDate = object.get("14").getAsString();
				String debitMark = object.get("16").getAsString();
				String debitDate = object.get("15").getAsString();
				String personalStatus = object.get("17").getAsString();
				String medicalPaymentGrade = object.get("18").getAsString();
				
				payinfo.setTaskid(taskid);
				payinfo.setCompanyName(companyName);
				payinfo.setInsuranceType(insuranceType);
				payinfo.setAccountDate(accountDate);
				payinfo.setFeeBelongsDate(feeBelongsDate);
				payinfo.setPayBase(payBase);
				payinfo.setFeeType(feeType);
				payinfo.setFeeSource(feeSource);
				payinfo.setPayType(payType);
				payinfo.setThisPay(thisPay);
				payinfo.setToPersonalAccountMoney(toPersonalAccountMoney);
				payinfo.setPayMark(payMark);
				payinfo.setArriveDate(arriveDate);
				payinfo.setDebitMark(debitMark);
				payinfo.setDebitDate(debitDate);
				payinfo.setPersonalStatus(personalStatus);
				payinfo.setMedicalPaymentGrade(medicalPaymentGrade);
				payinfos.add(payinfo);
			}
		}
		return payinfos;
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public String getNextLabelByKeyword(Element document, String tag, String keyword){
		Elements es = document.select(tag+":contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}
}
