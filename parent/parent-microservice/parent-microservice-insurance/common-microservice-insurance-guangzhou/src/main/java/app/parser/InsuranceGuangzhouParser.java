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
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.guangzhou.GuangzhouMedicalInsurance;
import com.microservice.dao.entity.crawler.insurance.guangzhou.GuangzhouUserInfo;
import com.microservice.dao.entity.crawler.insurance.guangzhou.InsuranceGuangZhouGeneral;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceGuangzhouParser {
	
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	
	public static String getCurrentDate(){
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date date = new Date();
		String currentDate = f.format(date).substring(0,6);
		return currentDate;
	}

	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws Exception 
	 */
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		WebParam webParam = new WebParam();
		String url = "http://gzlss.hrssgz.gov.cn/cas/login";
		tracer.addTag("parser.login.Url", url);
		webParam.setUrl(url);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		try {
			HtmlPage page = webClient.getPage(webRequest);
			int status = page.getWebResponse().getStatusCode();
			tracer.addTag("parser.login.status", status+"");
			HtmlImage image = page.getFirstByXPath("//img[@id='validCodeimg']");
			if(null != image){
				HtmlTextInput loginName = (HtmlTextInput)page.getFirstByXPath("//input[@id='loginName']");
				HtmlPasswordInput loginPassword = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='loginPassword']");
				HtmlTextInput validateCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='validateCode']");
				HtmlElement usertype2 = (HtmlElement)page.getFirstByXPath("//input[@id='usertype2']");
				HtmlElement submitbt = (HtmlElement)page.getFirstByXPath("//input[@id='submitbt']");
				
				loginName.setText(insuranceRequestParameters.getUsername());
				loginPassword.setText(insuranceRequestParameters.getPassword());
				String verifycode = chaoJiYingOcrService.getVerifycode(image, "1004");
				validateCode.setText(verifycode);
				usertype2.click();
				HtmlPage page2 = submitbt.click();
				
				webParam.setPage(page2);
				webParam.setCode(page2.getWebResponse().getStatusCode());
				tracer.addTag("parser.login.webParam", webParam.toString());
				return webParam;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	
	/**
	 * @Des 解析获取-个人社保综合缴费历史-中的信息
	 * @param taskInsurance
	 * @param cookies
	 * @return 
	 */
	public WebParam getInsuranceGeneralInfo(TaskInsurance taskInsurance) throws Exception{
		WebParam webParam= new WebParam();
		List<InsuranceGuangZhouGeneral> insuranceGuangZhouGenerals = new ArrayList<InsuranceGuangZhouGeneral>();
		String url1 = "http://gzlss.hrssgz.gov.cn/gzlss_web/business/front/foundationcentre/getPersonPayHistoryInfoByPage.xhtml?querylog=true&businessocde=SBGRJFLSCX&visitterminal=PC";
		tracer.addTag("parser.getGeneral.url1", url1);
		webParam.setUrl(url1);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		tracer.addTag("parser.getGeneral.html ", "<xmp>"+searchPage.asXml()+"</xmp>");
		webParam.setPage(searchPage);
		Document doc = Jsoup.parse(searchPage.asXml());
		Element aac001 = doc.getElementById("aac001");
		if(null != aac001 && aac001.children().size() > 0){
			String aac001Str = aac001.child(0).val();
			String url = "http://gzlss.hrssgz.gov.cn/gzlss_web/business/front/foundationcentre/viewPage/viewPersonPayHistoryInfo.xhtml?aac001="+aac001Str+"&xzType=1&startStr=199601&endStr="+getCurrentDate()+"&querylog=true&businessocde=291QB-GRJFLS&visitterminal=PC";
			tracer.addTag("parser.getGeneral.url", url);
			webParam.setUrl(url);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest);
			tracer.addTag("parser.getGeneral.html个人社保综合缴费历史 ", "<xmp>"+page.asXml()+"</xmp>");
			webParam.setPage(page);
			Document document = Jsoup.parse(page.asXml());
			Elements data = document.select(".table_white_data");
			if(null != data && data.size() > 3){
				for (Element element : data) {
					if(element.toString().contains("分险种月数统计")){
						break;
					}
					InsuranceGuangZhouGeneral insuranceGuangZhouGeneral = new InsuranceGuangZhouGeneral();
					Elements allElements = element.getAllElements();
					List<String> list = new ArrayList<String>();
					for (int i = 1; i < allElements.size(); i++) {
						String text = allElements.get(i).text();
						if(null == text || "" == text.trim()){
							text = null;
						}
						list.add(text.trim());
					}
					insuranceGuangZhouGeneral.setPayStartDate(list.get(0));
					insuranceGuangZhouGeneral.setPayEndDate(list.get(1));
					insuranceGuangZhouGeneral.setMonthCum(list.get(2));
					insuranceGuangZhouGeneral.setPayBase(list.get(3));
					insuranceGuangZhouGeneral.setPensionCompanyPay(list.get(4));
					insuranceGuangZhouGeneral.setPensionPersonalPay(list.get(5));
					insuranceGuangZhouGeneral.setUnemploymentCompanyPay(list.get(6));
					insuranceGuangZhouGeneral.setUnemploymentPersonalPay(list.get(7));
					insuranceGuangZhouGeneral.setAccidentsPay(list.get(8));
					insuranceGuangZhouGeneral.setMaternityPay(list.get(9));
					insuranceGuangZhouGeneral.setOrganizationnum(list.get(10));
					insuranceGuangZhouGeneral.setOrganizationname(list.get(11));
					insuranceGuangZhouGeneral.setCheckmode(list.get(12));
					insuranceGuangZhouGeneral.setTaskid(taskInsurance.getTaskid());
					insuranceGuangZhouGenerals.add(insuranceGuangZhouGeneral);
				}
				webParam.setInsuranceGuangZhouGenerals(insuranceGuangZhouGenerals);
			}
		}
		tracer.addTag("parser.getGeneral.webParam", webParam.toString());
	    return webParam;
	}
	

	/**
	 * @Des 解析获取-个人基础信息查询-中的个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getUserInfoOne(TaskInsurance taskInsurance) throws Exception{
		WebParam webParam= new WebParam();
		String url = "http://gzlss.hrssgz.gov.cn/gzlss_web/business/front/foundationcentre/getPersonInfoSearch.xhtml?querylog=true&businessocde=291QB-GRJCXX&visitterminal=PC-MENU";
		tracer.addTag("parser.parser.getUserinfo.url", url);
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		tracer.addTag("parser.parser.getUserinfo.getUserInfoOne.html", "<xmp>"+searchPage.asXml()+"</xmp>");
		HtmlElement submitbt = (HtmlElement)searchPage.getFirstByXPath("//input[@id='queryBut']");
		if(null != submitbt){
			HtmlPage page = submitbt.click();
			Thread.sleep(2500);
			int statusCode = page.getWebResponse().getStatusCode();
			tracer.addTag("InsuranceGuangzhouParser.getUserInfoOne.statusCode", statusCode+"");
			if(200 == statusCode){
		    	String html = page.getWebResponse().getContentAsString();
		    	tracer.addTag("parser.parser.getUserinfo.html个人基础信息查询1", "<xmp>"+html+"</xmp>");
		    	GuangzhouUserInfo userInfo = htmlParserOne(html);
		    	if(null != userInfo){
		    		userInfo.setTaskid(taskInsurance.getTaskid());
			    	webParam.setGuangzhouUserInfo(userInfo);
		    	}
		    	webParam.setUrl(url);
		    	webParam.setPage(page);
		    	tracer.addTag("parser.parser.getUserinfo.getUserInfoOnewebParam", webParam.toString());
		    	
		    	return webParam;
		    }
		}
	    tracer.addTag("parser.parser.getUserinfo.getUserInfoOne.webParam", webParam.toString());
		return webParam;
	}
	
	
	/**
	 * @param userInfo 
	 * @Des 解析获取-人员非敏感资料变更-中的个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getUserInfoTwo(GuangzhouUserInfo userInfo, TaskInsurance taskInsurance) throws Exception{
		String url = "http://gzlss.hrssgz.gov.cn/gzlss_web/business/front/foundationcentre/selfService/toAddAndUpdateRYFMGZLBG.xhtml?bae622=GZ00990001707462391&isUpdate=firstSave&businessname=人员非敏感资料变更&businesscode=291QB-RYFMGZLBG";
		tracer.addTag("parser.parser.getUserinfo.getUserInfoTwo.url", url);
		
		WebParam webParam= new WebParam();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("parser.parser.getUserinfo.getUserInfoTwo.statusCode", statusCode+"");
		
	    if(200 == statusCode){
	    	String html = searchPage.getWebResponse().getContentAsString();
	    	tracer.addTag("parser.parser.getUserinfo.getUserInfoTwo.html个人基础信息查询2 人员非敏感资料变更", "<xmp>"+html+"</xmp>");
	    	if(!html.contains("第一次登录")){
	    		userInfo = htmlParserTwo(userInfo,html,taskInsurance);
		    	webParam.setUrl(url);
		    	webParam.setPage(searchPage);
		    	webParam.setCode(taskInsurance.getUserInfoStatus()+10);
		    	webParam.setGuangzhouUserInfo(userInfo);
	    	}
	    	tracer.addTag("parser.parser.getUserinfo.getUserInfoTwo.webParam", webParam.toString());
	    	return webParam;
	    }
	    
	    webParam.setCode(taskInsurance.getUserInfoStatus());
	    tracer.addTag("parser.parser.getUserinfo.getUserInfoTwo.webParam", webParam.toString());
	    return webParam;
		
	}
	
	/**
	 * @param userInfo 
	 * @Des 解析获取-个人医保缴费历史-中的信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam getMedicalInfo(TaskInsurance taskInsurance) throws Exception{
		WebParam webParam= new WebParam();
		String url1 = "http://gzlss.hrssgz.gov.cn/gzlss_web/business/front/foundationcentre/getHealthcarePersonPayHistorySumup.xhtml?querylog=true&businessocde=291QB_YBGRJFLSCX&visitterminal=PC-MENU";
		tracer.addTag("parser.parser.getMedical.url1", url1);
		webParam.setUrl(url1);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		HtmlElement aac001 = (HtmlElement)searchPage.getFirstByXPath("//select[@name='aac001']");
		if(null != aac001 && aac001.getChildElementCount() > 0){
			String url = "http://gzlss.hrssgz.gov.cn/gzlss_web/business/front/foundationcentre/getHealthcarePersonPayHistorySumup.xhtml?query=1&querylog=true&businessocde=291QB_YBGRJFLSCX&visitterminal=PC&aac001="+aac001.getFirstElementChild().getAttribute("value")+"&startStr=199601&endStr="+getCurrentDate();
			webParam.setUrl(url);
			tracer.addTag("parser.parser.getMedical.url", url);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest);
			webParam.setPage(page);
			int statusCode = page.getWebResponse().getStatusCode();
			tracer.addTag("parser.parser.getMedical.statusCode", statusCode+"");
	    	String html = page.asXml();
	    	tracer.addTag("parser.parser.getMedical. html个人医保缴费历史", "<xmp>"+html+"</xmp>");
	    	if(html.contains("基本资料")){
		    	List<GuangzhouMedicalInsurance> guangzhouMedicalInsurances = medicalHtmlParser(html,taskInsurance);
		    	if(null != guangzhouMedicalInsurances){
		    		webParam.setGuangzhouMedicalInsurances(guangzhouMedicalInsurances);
		    	}else{
		    	}
	    	}
	    	
	    	tracer.addTag("parser.parser.getMedical.webParam", webParam.toString());
		}
		return webParam;
	}
	
	
	/**
	 * @Des 解析获取-个人参保证明-中的个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam getUserInfoFour(GuangzhouUserInfo userInfo,TaskInsurance taskInsurance) throws Exception{
		String url = "http://gzlss.hrssgz.gov.cn/gzlss_web/business/front/foundationcentre/getPersonalCBZMDY.xhtml?searchFlag=1&querylog=true&businessocde=291QB-GRCBZMDY&visitterminal=PC";
		tracer.addTag("parser.parser.getUserinfo.getUserInfoFour.url", url);
		
		WebParam webParam= new WebParam();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("parser.parser.getUserinfo.getUserInfoFour.statusCode", statusCode+"");
	    if(200 == statusCode){
	    	String html = searchPage.getWebResponse().getContentAsString();
	    	tracer.addTag("个人基础信息查询4 个人参保证明html====》》", "<xmp>"+html+"</xmp>");
	    	userInfo = htmlParserFour(userInfo,html,taskInsurance);
	    	webParam.setUrl(url);
	    	webParam.setPage(searchPage);
	    	webParam.setGuangzhouUserInfo(userInfo);
	    	webParam.setCode(taskInsurance.getUserInfoStatus()+10);
	    	
	    	tracer.addTag("parser.parser.getUserinfo.getUserInfoFour.webParam", webParam.toString());
	    	return webParam;
	    }
	    
	    webParam.setCode(taskInsurance.getUserInfoStatus());
	    tracer.addTag("parser.parser.getUserinfo.getUserInfoFour.webParam", webParam.toString());
		return webParam;
	}
	
	/**
	 * @Des 解析-个人社保综合缴费历史-中的信息
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceGuangZhouGeneral> htmlParserGeneral(String html, TaskInsurance taskInsurance) {
		List<InsuranceGuangZhouGeneral> insuranceGuangZhouGenerals = new ArrayList<InsuranceGuangZhouGeneral>();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select(".table_white_data");
		if(null != elements){
			for (Element element : elements) {
				if(element.toString().contains("分险种月数统计")){
					break;
				}
				InsuranceGuangZhouGeneral insuranceGuangZhouGeneral = new InsuranceGuangZhouGeneral();
				Elements allElements = element.getAllElements();
				List<String> list = new ArrayList<String>();
				for (int i = 1; i < allElements.size(); i++) {
					String text = allElements.get(i).text();
					if(null == text || "" == text.trim()){
						text = null;
					}
					list.add(text.trim());
				}
				insuranceGuangZhouGeneral.setPayStartDate(list.get(0));
				insuranceGuangZhouGeneral.setPayEndDate(list.get(1));
				insuranceGuangZhouGeneral.setMonthCum(list.get(2));
				insuranceGuangZhouGeneral.setPayBase(list.get(3));
				insuranceGuangZhouGeneral.setPensionCompanyPay(list.get(4));
				insuranceGuangZhouGeneral.setPensionPersonalPay(list.get(5));
				insuranceGuangZhouGeneral.setUnemploymentCompanyPay(list.get(6));
				insuranceGuangZhouGeneral.setUnemploymentPersonalPay(list.get(7));
				insuranceGuangZhouGeneral.setAccidentsPay(list.get(8));
				insuranceGuangZhouGeneral.setMaternityPay(list.get(9));
				insuranceGuangZhouGeneral.setOrganizationnum(list.get(10));
				insuranceGuangZhouGeneral.setOrganizationname(list.get(11));
				insuranceGuangZhouGeneral.setCheckmode(list.get(12));
				insuranceGuangZhouGeneral.setTaskid(taskInsurance.getTaskid());
				insuranceGuangZhouGenerals.add(insuranceGuangZhouGeneral);
			}
			return insuranceGuangZhouGenerals;
		}
		
		return null;
	}
	
	/**
	 * @Des 解析-个人医保缴费历史-中的医保信息
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<GuangzhouMedicalInsurance> medicalHtmlParser(String html, TaskInsurance taskInsurance) {
		List<GuangzhouMedicalInsurance> guangzhouMedicalInsurances = new ArrayList<GuangzhouMedicalInsurance>();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("[temp=职工社会医疗保险]");
		Elements elements2 = doc.select("[temp=重大疾病医疗补助]");
		
		if(null != elements || null != elements2){
			//解析-职工社会医疗保险
			for (Element element : elements) {
				GuangzhouMedicalInsurance guangzhouMedicalInsurance = new GuangzhouMedicalInsurance();
				Elements allElements = element.select("td");
				List<String> list = new ArrayList<String>();  
				for (int i = 0; i < allElements.size(); i++) {
					Element td = allElements.get(i);
					String tdstr = td.toString();
					String text = null;
					if(tdstr.contains("script")){
						text = tdstr.substring(tdstr.indexOf("(")+1,tdstr.indexOf(")"));
					}else{
						text = td.text();
					}
					
					if(null == text){
						text = null;
					}
					list.add(text);
				}
				guangzhouMedicalInsurance.setOrganizationnum(list.get(0));
				guangzhouMedicalInsurance.setPayStartDate(list.get(1));
				guangzhouMedicalInsurance.setPayEndDate(list.get(2));
				guangzhouMedicalInsurance.setMonthCum(list.get(3));
				guangzhouMedicalInsurance.setTransferPay(list.get(4));
				guangzhouMedicalInsurance.setAppendPay(list.get(5));
				guangzhouMedicalInsurance.setOrganizationPay(list.get(6));
				guangzhouMedicalInsurance.setPersonalPay(list.get(7));
				guangzhouMedicalInsurance.setGovPay(list.get(8));
				guangzhouMedicalInsurance.setPayBase(list.get(9));
				guangzhouMedicalInsurance.setMedicalType("职工社会医疗保险");
				guangzhouMedicalInsurance.setTaskid(taskInsurance.getTaskid());
				guangzhouMedicalInsurances.add(guangzhouMedicalInsurance);
			}
			//解析-重大疾病医疗补助
			for (Element element : elements2) {
				GuangzhouMedicalInsurance guangzhouMedicalInsurance = new GuangzhouMedicalInsurance();
				Elements allElements = element.select("td");
				List<String> list = new ArrayList<String>();  
				for (int i = 0; i < allElements.size(); i++) {
					Element td = allElements.get(i);
					String tdstr = td.toString();
					String text = null;
					if(tdstr.contains("script")){
						text = tdstr.substring(tdstr.indexOf("(")+1,tdstr.indexOf(")"));
					}else{
						text = td.text();
					}
					
					if(null == text || "".equals(text.trim())){
						text = null;
					}
					list.add(text);
				}
				guangzhouMedicalInsurance.setOrganizationnum(list.get(0));
				guangzhouMedicalInsurance.setPayStartDate(list.get(1));
				guangzhouMedicalInsurance.setPayEndDate(list.get(2));
				guangzhouMedicalInsurance.setMonthCum(list.get(3));
				guangzhouMedicalInsurance.setTransferPay(list.get(4));
				guangzhouMedicalInsurance.setAppendPay(list.get(5));
				guangzhouMedicalInsurance.setOrganizationPay(list.get(6));
				guangzhouMedicalInsurance.setPersonalPay(list.get(7));
				guangzhouMedicalInsurance.setGovPay(list.get(8));
				guangzhouMedicalInsurance.setPayBase(list.get(9));
				guangzhouMedicalInsurance.setMedicalType("重大疾病医疗补助");
				guangzhouMedicalInsurance.setTaskid(taskInsurance.getTaskid());
				guangzhouMedicalInsurances.add(guangzhouMedicalInsurance);
			}
			return guangzhouMedicalInsurances;
		}
		return null;
	}


	/**
	 * @Des 解析-个人参保证明-中的个人信息
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private GuangzhouUserInfo htmlParserFour(GuangzhouUserInfo userInfo, String html, TaskInsurance taskInsurance) {
		Document doc = Jsoup.parse(html);
		
		String insuredIndate = insuranceService.getNextLabelByKeyword(doc, "生育保险");
		
		userInfo.setInsuredIndate(insuredIndate);
		userInfo.setInsuredArea("广州");
		
		return userInfo;
	}
	

	/**
	 * @param userInfo 
	 * @Des 解析-个人医保缴费历史-中的个人信息
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private GuangzhouUserInfo htmlParserThree(GuangzhouUserInfo userInfo, String html, TaskInsurance taskInsurance) {
		try {
			Document doc = Jsoup.parse(html);
			
			String currentOrganizationnum = insuranceService.getNextLabelByKeyword(doc, "现在单位编号");
			String currentOrganizationname = insuranceService.getNextLabelByKeyword(doc, "现在单位名称");
			String insuredStatus = insuranceService.getNextLabelByKeyword(doc, "参保状态");
			
			userInfo.setCurrentOrganizationnum(currentOrganizationnum);
			userInfo.setCurrentOrganizationname(currentOrganizationname);
			userInfo.setInsuredStatus(insuredStatus);
		
			return userInfo;
		} catch (Exception e) {
			return null;
		}
		
	}


	/**
	 * @param userInfo 
	 * @Des 解析-人员非敏感资料变更-中的个人信息
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private GuangzhouUserInfo htmlParserTwo(GuangzhouUserInfo userInfo, String html, TaskInsurance taskInsurance) {
		Document doc = Jsoup.parse(html);
		
		String personalnum  =  doc.select("#aac001").val();
		String name  =  doc.select("#aac003ss").val();
		String postcode  =  doc.select("#aae007").val();
		String address  =  doc.select("#bab306").val();
		String organizationname  =  doc.select("#aab069ss").val();
		String organizationnum  =  doc.select("#aab003ss").val();
		
		userInfo.setPersonalnum(personalnum);
		userInfo.setName(name);
		userInfo.setPostcode(postcode);
		userInfo.setAddress(address);
		userInfo.setOrganizationname(organizationname);
		userInfo.setOrganizationnum(organizationnum);
		
		return userInfo;
	}


	/**
	 * @Des 解析-个人基础信息查询-中的个人信息
	 * @param html
	 * @return
	 */
	private GuangzhouUserInfo htmlParserOne(String html) {
		if(html.contains("身份证号码")){
			GuangzhouUserInfo guangzhouUserInfo = new GuangzhouUserInfo();
			Document doc = Jsoup.parse(html);
			
			String idnum = insuranceService.getNextLabelByKeyword(doc, "身份证号码");
			String gender = insuranceService.getNextLabelByKeyword(doc, "性别");
			String birthday = insuranceService.getNextLabelByKeyword(doc, "出生日期");
			String retirestatus = insuranceService.getNextLabelByKeyword(doc, "离退休状态");
			String residenceArea = insuranceService.getNextLabelByKeyword(doc, "户口所在地");
			String residenceType = insuranceService.getNextLabelByKeyword(doc, "户口性质");
			String nation = insuranceService.getNextLabelByKeyword(doc, "民族");
			String phonenum = insuranceService.getNextLabelByKeyword(doc, "手机");
			String email = insuranceService.getNextLabelByKeyword(doc, "邮箱");
			
			guangzhouUserInfo.setIdnum(idnum);
			guangzhouUserInfo.setGender(gender);
			guangzhouUserInfo.setBirthday(birthday);
			guangzhouUserInfo.setRetirestatus(retirestatus);
			guangzhouUserInfo.setResidenceArea(residenceArea);
			guangzhouUserInfo.setResidenceType(residenceType);
			guangzhouUserInfo.setNation(nation);
			guangzhouUserInfo.setPhonenum(phonenum);
			guangzhouUserInfo.setEmail(email);
			
			return guangzhouUserInfo;
		}
		return null;
	}

	public WebParam sendSMS(TaskInsurance taskInsurance) throws Exception{
		tracer.addTag("parser.sendSMS.taskid", taskInsurance.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String url = "http://gzlss.hrssgz.gov.cn/gzlss_web/business/anon/LetterVistsSMSCode/getValidateCode.xhtml?buscode=LOGIN_BUSSINESS_CODE&rtime=1524449791211";
		webParam.setUrl(url);
		tracer.addTag("parser.sendSMS.url", url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		webParam.setPage2(page);
		tracer.addTag("parser.sendSMS.page", page.getWebResponse().getContentAsString());
		if(page.getWebResponse().getStatusCode() == 200){
			String html =page.getWebResponse().getContentAsString();
			if(html.equals("validityTime")){
				JsonParser parser = new JsonParser();
				JsonObject obj = (JsonObject) parser.parse(html);
				String flag = obj.get("flag").getAsString();
				if(flag.contains("1")){
					webParam.setCode(200);
					tracer.addTag("parser.sendSMS.success", "短信发送成功！请注意查收。");
				}else {
					webParam.setHtml("短信发送失败！");
					tracer.addTag("parser.sendSMS.fail", "短信发送失败！");
				}
			}else{
				webParam.setHtml("短信发送失败！");
				tracer.addTag("parser.sendSMS.fail2", "发送短信接口请求失败");
			}
			
		}else{
			webParam.setHtml("短信发送失败！");
			tracer.addTag("parser.sendSMS.fail3", "发送短信接口请求失败");
		}
		return webParam;
	}

	public WebParam checkSMS(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception{
		tracer.addTag("crawler.checkSMS.taskid", taskInsurance.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String url = "http://gzlss.hrssgz.gov.cn/gzlss_web/business/tomain/main.xhtml?buscode=LOGIN_BUSSINESS_CODE&code="+insuranceRequestParameters.getMessage();
		webParam.setUrl(url);
		tracer.addTag("crawler.checkSMS.url", url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		webParam.setPage(page);
		tracer.addTag("crawler.checkSMS.page", "<xmp>"+page.asXml()+"</xmp>");
		Document document = Jsoup.parse(page.asXml());
		Elements username = document.getElementsByAttributeValue("color", "blue");
		if(null != username && !username.text().trim().equals("")){
			webParam.setWebClient(webClient);
			tracer.addTag("crawler.checkSMS.success", "短信验证成功。");
		}else{
			webParam.setHtml("短信验证码错误！请您稍后重试。");
			tracer.addTag("crawler.checkSMS.fail", "短信验证失败。");
		}
		return webParam;
	}


	

}
