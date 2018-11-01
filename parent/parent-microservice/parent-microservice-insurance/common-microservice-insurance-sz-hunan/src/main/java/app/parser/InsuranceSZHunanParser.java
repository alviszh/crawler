package app.parser;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanBear;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanInjury;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanMedical;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanPension;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanUnemployment;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceSZHunanParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	
	
	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws Exception 
	 */
	public WebParam login(TaskInsurance taskInsurance, InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("parser.login.parser.taskid", taskInsurance.getTaskid());
		String url = "http://sbk.hn12333.com:7002/PubQuery/person/";
		tracer.addTag("parser.login.parser.Url", url);
		WebParam webParam = new WebParam();
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient.setJavaScriptTimeout(2000);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webParam.setUrl(url);
			HtmlPage page = webClient.getPage(webRequest);
			tracer.addTag("parser.login.parser.loginPage", page.asXml());
			int status = page.getWebResponse().getStatusCode();
			tracer.addTag("parser.login.parser.status", status+"");
			if(200 == status){
				HtmlTextInput UserName = (HtmlTextInput)page.getFirstByXPath("//input[@id='UserName']");
				HtmlTextInput LoginUserName = (HtmlTextInput)page.getFirstByXPath("//input[@id='LoginUserName']");
				HtmlPasswordInput UserPassword = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='UserPassword']");
				HtmlImage image = page.getFirstByXPath("//img[@id='VerifyImage']");
				HtmlTextInput VerifyCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='VerifyCode']");
				HtmlElement btnLogin = (HtmlElement)page.getFirstByXPath("//button[@id='btnLogin']");
				
				UserName.setText(taskInsurance.getBasicUserInsurance().getIdnum());
				LoginUserName.setText(taskInsurance.getBasicUserInsurance().getName());
				UserPassword.setText("666666");
				String code = chaoJiYingOcrService.getVerifycode(image, "1004");
				VerifyCode.setText(code);
				HtmlPage loginedPage = btnLogin.click();
				
				HtmlElement message = (HtmlElement)loginedPage.getFirstByXPath("//span[@id='messageInfo']");
				if(null != message){
					String asText = message.asText();
					tracer.addTag("parser.login.parser.message", asText);
				}
				
				webParam.setHtmlPage(loginedPage);
				webParam.setCode(loginedPage.getWebResponse().getStatusCode());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.login.parser.error", e.toString());
			return null;
		} 
		return webParam;
	}



	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @return
	 */
	public WebParam<InsuranceSZHunanUserInfo> getUserInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://sbk.hn12333.com:7002/PubQuery/commbiz/commonInfoAction!queryPersonBaseInfo.action";
		tracer.addTag("parser.parser.getUserInfo.Url", url);
		
		WebParam<InsuranceSZHunanUserInfo> webParam = new WebParam<InsuranceSZHunanUserInfo>();
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage userInfoPage = webClient.getPage(webRequest);
		webParam.setHtmlPage(userInfoPage);
		int statusCode = userInfoPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getUserInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = userInfoPage.asXml();
			tracer.addTag("parser.getUserInfo.html", "<xmp>"+html+"</xmp>");
			Document document = Jsoup.parse(html);
			Element personInfo = document.getElementById("personInfo");
			if(null != personInfo){
				List<InsuranceSZHunanUserInfo> userInfos = new ArrayList<InsuranceSZHunanUserInfo>();
				InsuranceSZHunanUserInfo insuranceSZHunanUserInfo = new InsuranceSZHunanUserInfo();
			
				String idNum = getNextLabelByKeywordInUserInfo(document, "证件号码");
				String name = getNextLabelByKeywordInUserInfo(document, "姓名");
				String gender = getNextLabelByKeywordInUserInfo(document, "性别");
				String nation = getNextLabelByKeywordInUserInfo(document, "民族");
				String birthday = getNextLabelByKeywordInUserInfo(document, "出生日期");
				String residenceArea = getNextLabelByKeywordInUserInfo(document, "户口所在地址");
				String cardNum = getNextLabelByKeywordInUserInfo(document, "封面卡号");
				String cardStartDate = getNextLabelByKeywordInUserInfo(document, "发卡日期");
				String cardStatus = getNextLabelByKeywordInUserInfo(document, "卡状态");
				String cardExpiry = getNextLabelByKeywordInUserInfo(document, "卡有效期限");
				String organizationName = getNextLabelByKeywordInUserInfo(document, "社会保险经办机构编码");
				String bankNum = getNextLabelByKeywordInUserInfo(document, "银行编号");
				String contactTel = getNextLabelByKeywordInUserInfo(document, "联系电话");
				
				insuranceSZHunanUserInfo.setIdNum(idNum);
				insuranceSZHunanUserInfo.setName(name);
				insuranceSZHunanUserInfo.setGender(gender);
				insuranceSZHunanUserInfo.setNation(nation);
				insuranceSZHunanUserInfo.setBirthday(birthday);
				insuranceSZHunanUserInfo.setResidenceArea(residenceArea);
				insuranceSZHunanUserInfo.setCardNum(cardNum);
				insuranceSZHunanUserInfo.setCardStartDate(cardStartDate);
				insuranceSZHunanUserInfo.setCardStatus(cardStatus);
				insuranceSZHunanUserInfo.setCardExpiry(cardExpiry);
				insuranceSZHunanUserInfo.setOrganizationName(organizationName);
				insuranceSZHunanUserInfo.setBankNum(bankNum);
				insuranceSZHunanUserInfo.setContactTel(contactTel);
				
				if(null != insuranceSZHunanUserInfo){
					insuranceSZHunanUserInfo.setTaskid(taskInsurance.getTaskid());
					userInfos.add(insuranceSZHunanUserInfo);
					webParam.setList(userInfos);
				}
			}
		}
		
		return webParam;
	}
	
	/**
	 * @Des 获取社保信息
	 * @param taskInsurance
	 * @return
	 */
	public WebParam getInsuranceInfo(TaskInsurance taskInsurance, int i) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat("yyyy");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		int year = Integer.parseInt(date)-i;
		String url = "http://sbk.hn12333.com:7002/PubQuery/commbiz/commonInfoAction!queryPersonPayInfo.action?yer="+year;
		tracer.addTag("parser.parser.getInsuranceInfo.Url."+i, url);
		
		WebParam webParam = new WebParam();
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage userInfoPage = webClient.getPage(webRequest);
		webParam.setHtmlPage(userInfoPage);
		int statusCode = userInfoPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getUserInfo.statusCode."+i, statusCode+"");
		if(200 == statusCode){
			String html = userInfoPage.asXml();
			tracer.addTag("parser.parser.getInsuranceInfo.html."+i, "<xmp>"+html+"</xmp>");
			webParam = parserInsuranceInfo(html, webParam, taskInsurance.getTaskid());
		}
		return webParam;
	}
	
	
	/**
	 * @Des 解析社保信息
	 * @param html
	 * @param webParam
	 * @return
	 */
	public static WebParam parserInsuranceInfo(String html, WebParam webParam, String taskid) throws Exception{
		Document document = Jsoup.parse(html);
		Element divTabs = document.getElementById("divTabs");
		if(null != divTabs){
			//解析养老保险信息
			Element pensionDiv = document.getElementById("divTab1");
			Element tbody1 = pensionDiv.select("tbody").first();
			Elements trs1 = tbody1.select("tr");
			String className1 = trs1.get(0).className();
			if(!className1.equals("empty")){
				List<InsuranceSZHunanPension> pensions = new ArrayList<InsuranceSZHunanPension>();
				for (Element tr : trs1) {
					Elements tds = tr.children();
					List<String> str = new ArrayList<String>();
					InsuranceSZHunanPension pension = new InsuranceSZHunanPension();
					for (Element td : tds) {
						String text = td.text();
						str.add(text);
					}
					pension.setPayStartDate(str.get(0));
					pension.setPayEndDate(str.get(1));
					pension.setPayMonthSum(str.get(2));
					pension.setOrganizationName(str.get(3));
					pension.setPaySalary(str.get(4));
					pension.setPersonalPay(str.get(5));
					pension.setOrganizationPay(str.get(6));
					pension.setTaskid(taskid);
					pensions.add(pension);
				}
				webParam.setListPension(pensions);
			}
			//解析医疗保险信息
			Element medicalDiv = document.getElementById("divTab2");
			Element tbody2 = medicalDiv.select("tbody").first();
			Elements trs2 = tbody2.select("tr");
			String className2 = trs2.get(0).className();
			if(!className2.equals("empty")){
				List<InsuranceSZHunanMedical> medicals = new ArrayList<InsuranceSZHunanMedical>();
				for (Element tr : trs2) {
					Elements tds = tr.children();
					List<String> str = new ArrayList<String>();
					InsuranceSZHunanMedical medical = new InsuranceSZHunanMedical();
					for (Element td : tds) {
						String text = td.text();
						str.add(text);
					}
					medical.setPayDate(str.get(0));
					medical.setOrganizationName(str.get(1));
					medical.setPaySalary(str.get(2));
					medical.setPersonalPay(str.get(3));
					medical.setOrganizationPay(str.get(4));
					medical.setTaskid(taskid);
					medicals.add(medical);
				}
				webParam.setListMedical(medicals);
			}
			//解析工伤保险信息
			Element injuryDiv = document.getElementById("divTab3");
			Element tbody3 = injuryDiv.select("tbody").first();
			Elements trs3 = tbody3.select("tr");
			String className3 = trs3.get(0).className();
			if(!className3.equals("empty")){
				List<InsuranceSZHunanInjury> injuries = new ArrayList<InsuranceSZHunanInjury>();
				for (Element tr : trs3) {
					Elements tds = tr.children();
					List<String> str = new ArrayList<String>();
					InsuranceSZHunanInjury injury = new InsuranceSZHunanInjury();
					for (Element td : tds) {
						String text = td.text();
						str.add(text);
					}
					injury.setPayDate(str.get(0));
					injury.setOrganizationName(str.get(1));
					injury.setPaySalary(str.get(2));
					injury.setPersonalPay(str.get(3));
					injury.setOrganizationPay(str.get(4));
					injury.setTaskid(taskid);
					injuries.add(injury);
				}
				webParam.setListInjury(injuries);
			}
			//解析生育保险信息
			Element bearDiv = document.getElementById("divTab4");
			Element tbody4 = bearDiv.select("tbody").first();
			Elements trs4 = tbody4.select("tr");
			String className4 = trs4.get(0).className();
			if(!className4.equals("empty")){
				List<InsuranceSZHunanBear> bears = new ArrayList<InsuranceSZHunanBear>();
				for (Element tr : trs4) {
					Elements tds = tr.children();
					List<String> str = new ArrayList<String>();
					InsuranceSZHunanBear bear = new InsuranceSZHunanBear();
					for (Element td : tds) {
						String text = td.text();
						str.add(text);
					}
					bear.setPayDate(str.get(0));
					bear.setOrganizationName(str.get(1));
					bear.setPaySalary(str.get(2));
					bear.setPersonalPay(str.get(3));
					bear.setOrganizationPay(str.get(4));
					bear.setTaskid(taskid);
					bears.add(bear);
				}
				webParam.setListBear(bears);
			}
			//解析失业保险信息
			Element unemploymentDiv = document.getElementById("divTab5");
			Element tbody5 = unemploymentDiv.select("tbody").first();
			Elements trs5 = tbody5.select("tr");
			String className5 = trs5.get(0).className();
			if(!className5.equals("empty")){
				List<InsuranceSZHunanUnemployment> unemployments = new ArrayList<InsuranceSZHunanUnemployment>();
				for (Element tr : trs5) {
					Elements tds = tr.children();
					List<String> str = new ArrayList<String>();
					InsuranceSZHunanUnemployment unemployment = new InsuranceSZHunanUnemployment();
					for (Element td : tds) {
						String text = td.text();
						str.add(text);
					}
					unemployment.setPayDate(str.get(0));
					unemployment.setOrganizationName(str.get(1));
					unemployment.setPaySalary(str.get(2));
					unemployment.setPersonalPay(str.get(3));
					unemployment.setOrganizationPay(str.get(4));
					unemployment.setTaskid(taskid);
					unemployments.add(unemployment);
				}
				webParam.setListUnemployment(unemployments);
			}
		}
		return webParam;
	}
	
	public WebParam getPensionInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception{
		tracer.addTag("parser.parser.getInsuranceInfo.taskid", taskInsurance.getTaskid());
		WebParam webParam = new WebParam();
		String taskid = taskInsurance.getTaskid();
		
		String loginUrl = "http://www.hn12333.com:81/comm_front/query/bsjylbxgrzh_query.jsp";
		tracer.addTag("parser.parser.getPensionInfo.loginUrl", loginUrl);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		tracer.addTag("parser.parser.getPensionInfo.loginPage", "<xmp>"+page.asXml()+"</xmp>");
		
		HtmlTextInput sfzhm = (HtmlTextInput)page.getFirstByXPath("//input[@id='sfzhm']");
		HtmlTextInput xm = (HtmlTextInput)page.getFirstByXPath("//input[@id='xm']");
		HtmlPasswordInput mm = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='mm']");
		HtmlImage image = page.getFirstByXPath("//img[@id='pics']");
		HtmlTextInput yzm = (HtmlTextInput)page.getFirstByXPath("//input[@name='yzm']");
		HtmlButtonInput btnLogin = (HtmlButtonInput)page.getFirstByXPath("//input[@name='search']");
		
		sfzhm.setText(taskInsurance.getBasicUserInsurance().getIdnum());
		xm.setText(taskInsurance.getBasicUserInsurance().getName());
		mm.setText(insuranceRequestParameters.getPassword());
		yzm.setText(chaoJiYingOcrService.getVerifycode(image, "1004"));
		HtmlPage loginedPage = btnLogin.click();
		webParam.setHtmlPage(loginedPage);
		tracer.addTag("parser.parser.getPensionInfo.loginedPage", "<xmp>"+loginedPage.asXml()+"</xmp>");
		//判断是否登录成功
		if(loginedPage.asXml().contains("建账年月")){
			List<InsuranceSZHunanPension> pensions = new ArrayList<InsuranceSZHunanPension>();
			for (int i = 0; i < 10; i++) {
				String pensionUrl = "http://www.hn12333.com:81/comm_front/query/jfxx_zz_list.jsp?flag=next&page1="+i;
				tracer.addTag("parser.parser.getPensionInfo.pensionUrl."+i, pensionUrl);
				webRequest = new WebRequest(new URL(pensionUrl), HttpMethod.GET);
				HtmlPage pensionPage = webClient.getPage(webRequest);
				tracer.addTag("parser.parser.getPensionInfo.pensionPage."+i, "<xmp>"+pensionPage.asXml()+"</xmp>");
				if(null != pensionPage.getElementById("count")){
					Document doc = Jsoup.parse(pensionPage.asXml());
					Elements trs = doc.select("tr");
					for (int j = 1; j < trs.size(); j++) {
						Element tr = trs.get(j);
						Elements tds = tr.select("td");
						if(tds.size() == 5){
							String payMonth = tds.get(0).text();
							String payIdentity = tds.get(1).text();
							String status = tds.get(2).text();
							String paySalary = tds.get(3).text();
							String payMoney = tds.get(4).text();
							InsuranceSZHunanPension pension = new InsuranceSZHunanPension();
							
							pension.setTaskid(taskid);
							pension.setPayMonth(payMonth);
							pension.setPayIdentity(payIdentity);
							pension.setStatus(status);
							pension.setPaySalary(paySalary);
							pension.setPayMoney(payMoney);
							pensions.add(pension);
						}
					}
				}
				webParam.setListPension(pensions);
			}
		}
		return webParam;
	}
	
	/**
	 * @Des 获取 用户信息中 目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeywordInUserInfo(Document document, String keyword){
		Elements es = document.select("td:contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.child(0).val();
			}
		}
		return null;
	}

}
