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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.foshan.InsuranceFoshanInfo;
import com.microservice.dao.entity.crawler.insurance.foshan.InsuranceFoshanUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceFoshanParser {

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
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		String url = "http://www.fssi.gov.cn/";
		tracer.addTag("parser.login.Url", url);
		
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.setJavaScriptTimeout(2000);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		try {
			HtmlPage page = webClient.getPage(webRequest);
			tracer.addTag("parser.login.page", page.asXml());
			Thread.sleep(1000);
			int status = page.getWebResponse().getStatusCode();
			tracer.addTag("parser.login.status", status+"");
			
			if(200 == status){
				HtmlTextInput loginName = (HtmlTextInput)page.getFirstByXPath("//input[@id='sfzhForm1']");
				HtmlPasswordInput loginPassword = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='pswForm1']");
				HtmlElement submitbt = (HtmlElement)page.getFirstByXPath("//input[@value='提 交']");
				
				loginName.setText(insuranceRequestParameters.getUsername());
				loginPassword.setText(insuranceRequestParameters.getPassword());
				HtmlPage checkPage = submitbt.click();
				
				HtmlElement submit = (HtmlElement)checkPage.getFirstByXPath("//a[@class='buttonLink']");
				checkPage = submit.click();
				webParam.setPage(checkPage);
				webParam.setCode(checkPage.getWebResponse().getStatusCode());
				
				return webParam;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
			
		return null;
	}


	/**
	 * @Des 登录2
	 * @param webParam
	 * @return
	 */
	public WebParam loginTwo(WebParam webParam) throws Exception{
		
		HtmlTextInput validateCode = (HtmlTextInput)webParam.getPage().getFirstByXPath("//input[@name='imagecheck']");
		HtmlImage image = webParam.getPage().getFirstByXPath("//img[@height='20']");
		HtmlElement submit = (HtmlElement)webParam.getPage().getFirstByXPath("//a[@class='buttonLink']");
		
		validateCode.setText(chaoJiYingOcrService.getVerifycode(image, "1004"));
		HtmlPage validatePage = submit.click();
		
		if(200 == validatePage.getWebResponse().getStatusCode()){
			webParam.setPage(validatePage);
			webParam.setCode(validatePage.getWebResponse().getStatusCode());
			return webParam;
		}
		
		return null;
		
	}

	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @return
	 */
	public WebParam<InsuranceFoshanUserInfo> getUserInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://61.142.213.86/grwssb/parser/MainAction?menuid=grcx_grjbzlcx&ActionType=grcx_grjbzlcx&flag=true";
		tracer.addTag("parser.getUserInfo.Url", url);
		
		WebParam<InsuranceFoshanUserInfo> webParam = new WebParam<InsuranceFoshanUserInfo>();
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage userInfoPage = webClient.getPage(webRequest);
		
		int statusCode = userInfoPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getUserInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = userInfoPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.getUserInfo.html", "<xmp>"+html+"</xmp>");
			if(!html.contains("您没有登录或者连结超时，请重新登录!")){
				List<InsuranceFoshanUserInfo> insuranceFoshanUserInfos = new ArrayList<InsuranceFoshanUserInfo>();
				InsuranceFoshanUserInfo insuranceFoshanUserInfo = userInfoHtmlParser(html);
				if(null != insuranceFoshanUserInfo){
					insuranceFoshanUserInfo.setTaskid(taskInsurance.getTaskid());
					insuranceFoshanUserInfos.add(insuranceFoshanUserInfo);
					webParam.setList(insuranceFoshanUserInfos);
				}
				webParam.setPage(userInfoPage);
				webParam.setHtml(html);
			}else{
				webParam.setCode(100);
			}
			return webParam;
		}
		
		return webParam;
	}
	
	
	/**
	 * @Des 获取养老信息
	 * @param taskInsurance
	 * @return
	 */
	public WebParam<InsuranceFoshanInfo> getPensionInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://61.142.213.86/grwssb/parser/MainAction?menuid=grcx_ylbxjfcx&ActionType=grcx_ylbxjfcx&flag=true";
		tracer.addTag("parser.getPensionInfo.Url", url);
		
		WebParam<InsuranceFoshanInfo> webParam = new WebParam<InsuranceFoshanInfo>();
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage pensionPage = webClient.getPage(webRequest);
		
		int statusCode = pensionPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getPensionInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = pensionPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.getPensionInfo.html", "<xmp>"+html+"</xmp>");
			if(!html.contains("您没有登录或者连结超时，请重新登录!")){
				List<InsuranceFoshanInfo> insuranceFoshanInfos = insuranceInfoHtmlParser(taskInsurance,html,"pension");
				if(null != insuranceFoshanInfos){
					webParam.setList(insuranceFoshanInfos);
				}
				webParam.setPage(pensionPage);
				webParam.setHtml(html);
			}else{
				webParam.setCode(100);
			}
			return webParam;
		}
		
		return webParam;
	}
	
	
	/**
	 * @Des 获取生育信息
	 * @param taskInsurance
	 * @return
	 */
	public WebParam<InsuranceFoshanInfo> getBearInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://61.142.213.86/grwssb/parser/MainAction?menuid=grcx_syubxjfcx&ActionType=grcx_syubxjfcx&flag=true";
		tracer.addTag("parser.getBearInfo.Url", url);
		
		WebParam<InsuranceFoshanInfo> webParam = new WebParam<InsuranceFoshanInfo>();
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage bearPage = webClient.getPage(webRequest);
		
		int statusCode = bearPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getBearInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = bearPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.getBearInfo.html", "<xmp>"+html+"</xmp>");
			if(!html.contains("您没有登录或者连结超时，请重新登录!")){
				List<InsuranceFoshanInfo> insuranceFoshanInfos = insuranceInfoHtmlParser(taskInsurance,html,"bear");
				if(null != insuranceFoshanInfos){
					webParam.setList(insuranceFoshanInfos);
				}
				webParam.setPage(bearPage);
				webParam.setHtml(html);
			}else{
				webParam.setCode(100);
			}
			return webParam;
		}
		
		return webParam;
	}

	
	/**
	 * @Des 获取工伤信息
	 * @param taskInsurance
	 * @return
	 */
	public WebParam<InsuranceFoshanInfo> getInjuryInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://61.142.213.86/grwssb/parser/MainAction?menuid=grcx_gsbxjfcx&ActionType=grcx_gsbxjfcx&flag=true";
		tracer.addTag("parser.getInjuryInfo.Url", url);
		
		WebParam<InsuranceFoshanInfo> webParam = new WebParam<InsuranceFoshanInfo>();
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage injuryPage = webClient.getPage(webRequest);
		
		int statusCode = injuryPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getInjuryInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = injuryPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.getInjuryInfo.html", "<xmp>"+html+"</xmp>");
			if(!html.contains("您没有登录或者连结超时，请重新登录!")){
				List<InsuranceFoshanInfo> insuranceFoshanInfos = insuranceInfoHtmlParser(taskInsurance,html,"injury");
				if(null != insuranceFoshanInfos){
					webParam.setList(insuranceFoshanInfos);
				}
				webParam.setPage(injuryPage);
				webParam.setHtml(html);
			}else{
				webParam.setCode(100);
			}
			return webParam;
		}
		
		return webParam;
	}
	
	

	/**
	 * @Des 获取医疗信息
	 * @param taskInsurance
	 * @return
	 */
	public WebParam<InsuranceFoshanInfo> getMedicalInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://61.142.213.86/grwssb/parser/MainAction?menuid=grcx_yilbxjfcx&ActionType=grcx_yilbxjfcx&flag=true";
		tracer.addTag("parser.getMedicalInfo.Url", url);
		
		WebParam<InsuranceFoshanInfo> webParam = new WebParam<InsuranceFoshanInfo>();
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage medicalPage = webClient.getPage(webRequest);
		
		int statusCode = medicalPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getMedicalInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = medicalPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.getMedicalInfo.html", "<xmp>"+html+"</xmp>");
			if(!html.contains("您没有登录或者连结超时，请重新登录!")){
				List<InsuranceFoshanInfo> insuranceFoshanInfos = insuranceInfoHtmlParser(taskInsurance,html,"medical");
				if(null != insuranceFoshanInfos){
					webParam.setList(insuranceFoshanInfos);
				}
				webParam.setPage(medicalPage);
				webParam.setHtml(html);
			}else{
				webParam.setCode(100);
			}
			return webParam;
		}
		
		return webParam;
	}
	
	
	/**
	 * @Des 获取失业信息
	 * @param taskInsurance
	 * @return
	 */
	public WebParam<InsuranceFoshanInfo> getUnemploymentInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://61.142.213.86/grwssb/parser/MainAction?menuid=grcx_syebxjfcx&ActionType=grcx_syebxjfcx&flag=true";
		tracer.addTag("parser.getUnemploymentInfo.Url", url);
		
		WebParam<InsuranceFoshanInfo> webParam = new WebParam<InsuranceFoshanInfo>();
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage unemploymentPage = webClient.getPage(webRequest);
		
		int statusCode = unemploymentPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getUnemploymentInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = unemploymentPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.getUnemploymentInfo.html", "<xmp>"+html+"</xmp>");
			if(!html.contains("您没有登录或者连结超时，请重新登录!")){
				List<InsuranceFoshanInfo> insuranceFoshanInfos = insuranceInfoHtmlParser(taskInsurance,html,"unemployment");
				if(null != insuranceFoshanInfos){
					webParam.setList(insuranceFoshanInfos);
				}
				webParam.setPage(unemploymentPage);
				webParam.setHtml(html);
			}else{
				webParam.setCode(100);
			}
			return webParam;
		}
		
		return webParam;
	}
	
	

	/**
	 * @Des 社保信息页面解析
	 * @param html
	 * @param string
	 * @return
	 */
	private List<InsuranceFoshanInfo> insuranceInfoHtmlParser(TaskInsurance taskInsurance, String html, String type) {
		Document doc = Jsoup.parse(html);
		List<InsuranceFoshanInfo> insuranceFoshanInfos = new ArrayList<InsuranceFoshanInfo>();
		
		Elements elements = doc.select(".list_table_tbody_tr");
		for (Element element : elements) {
			Elements tds = element.select("td");
			List<String> str = new ArrayList<String>();
			InsuranceFoshanInfo insuranceFoshanInfo = new InsuranceFoshanInfo();
			for (Element element2 : tds) {
				String text = element2.text();
				str.add(text);
			}
			if(null != str && str.size() != 0){
				insuranceFoshanInfo.setPayStartEndTime(str.get(0));
				insuranceFoshanInfo.setOrganizationName(str.get(1));
				insuranceFoshanInfo.setInsuranceType(str.get(2));
				insuranceFoshanInfo.setPayBase(str.get(3));
				insuranceFoshanInfo.setPersonalPay(str.get(4));
				insuranceFoshanInfo.setCompanyPay(str.get(5));
				insuranceFoshanInfo.setMonthPaySum(str.get(6));
				insuranceFoshanInfo.setPayMonth(str.get(7));
				insuranceFoshanInfo.setPersonalPaySum(str.get(8));
				insuranceFoshanInfo.setCompanyPaySum(str.get(9));
				insuranceFoshanInfo.setTotal(str.get(10));
				if(type.equals("pension")){
					insuranceFoshanInfo.setPersonalPensionAccount(str.get(11));
				}
				insuranceFoshanInfo.setType(type);
				insuranceFoshanInfo.setTaskid(taskInsurance.getTaskid());
				insuranceFoshanInfos.add(insuranceFoshanInfo);
			}
		}
		Elements elements1 = doc.select(".list_table_tbody_tr1");
		for (Element element : elements1) {
			Elements tds = element.select("td");
			List<String> str = new ArrayList<String>();
			InsuranceFoshanInfo insuranceFoshanInfo = new InsuranceFoshanInfo();
			for (Element element2 : tds) {
				String text = element2.text();
				str.add(text);
			}
			if(null != str && str.size() != 0){
				insuranceFoshanInfo.setPayStartEndTime(str.get(0));
				insuranceFoshanInfo.setOrganizationName(str.get(1));
				insuranceFoshanInfo.setInsuranceType(str.get(2));
				insuranceFoshanInfo.setPayBase(str.get(3));
				insuranceFoshanInfo.setPersonalPay(str.get(4));
				insuranceFoshanInfo.setCompanyPay(str.get(5));
				insuranceFoshanInfo.setMonthPaySum(str.get(6));
				insuranceFoshanInfo.setPayMonth(str.get(7));
				insuranceFoshanInfo.setPersonalPaySum(str.get(8));
				insuranceFoshanInfo.setCompanyPaySum(str.get(9));
				insuranceFoshanInfo.setTotal(str.get(10));
				if(type.equals("pension")){
					insuranceFoshanInfo.setPersonalPensionAccount(str.get(11));
				}
				insuranceFoshanInfo.setType(type);
				insuranceFoshanInfo.setTaskid(taskInsurance.getTaskid());
				insuranceFoshanInfos.add(insuranceFoshanInfo);
			}
		}
		
		return insuranceFoshanInfos;
	}


	/**
	 * @param html
	 * @return
	 */
	private InsuranceFoshanUserInfo userInfoHtmlParser(String html) {
		Document doc = Jsoup.parse(html);
		InsuranceFoshanUserInfo insuranceFoshanUserInfo = new InsuranceFoshanUserInfo();
		
		String manageOrganization = insuranceService.getNextLabelByKeyword(doc, "管理的社保机构");
		String insuranceNum = insuranceService.getNextLabelByKeyword(doc, "社会保障号");
		String personalInsuranceNum = insuranceService.getNextLabelByKeyword(doc, "个人社保号");
		String name = insuranceService.getNextLabelByKeyword(doc, "姓名");
		String gender = insuranceService.getNextLabelByKeyword(doc, "性别");
		String birthday = insuranceService.getNextLabelByKeyword(doc, "出生日期");
		String organizationName = insuranceService.getNextLabelByKeyword(doc, "现单位名称");
		String pension = insuranceService.getNextLabelByKeyword(doc, "养老");
		String medical = insuranceService.getNextLabelByKeyword(doc, "医疗");
		String bear = insuranceService.getNextLabelByKeyword(doc, "生育");
		String injury = insuranceService.getNextLabelByKeyword(doc, "工伤");
		String unemployment = insuranceService.getNextLabelByKeyword(doc, "失业");
		String arrearageMonth = insuranceService.getNextLabelByKeyword(doc, "欠费月数");
		String pensionRealPayMonth = insuranceService.getNextLabelByKeyword(doc, "养老 实际缴费月数");
		String medicalRealPayMonth = insuranceService.getNextLabelByKeyword(doc, "医疗 实际缴费月数");
		String unemploymentRealPayMonth = insuranceService.getNextLabelByKeyword(doc, "失业 实际缴费月数");
		
		insuranceFoshanUserInfo.setManageOrganization(manageOrganization);
		insuranceFoshanUserInfo.setInsuranceNum(insuranceNum);
		insuranceFoshanUserInfo.setPersonalInsuranceNum(personalInsuranceNum);
		insuranceFoshanUserInfo.setName(name);
		insuranceFoshanUserInfo.setGender(gender);
		insuranceFoshanUserInfo.setBirthday(birthday);
		insuranceFoshanUserInfo.setOrganizationName(organizationName);
		insuranceFoshanUserInfo.setPension(pension);
		insuranceFoshanUserInfo.setMedical(medical);
		insuranceFoshanUserInfo.setBear(bear);
		insuranceFoshanUserInfo.setInjury(injury);
		insuranceFoshanUserInfo.setUnemployment(unemployment);
		insuranceFoshanUserInfo.setArrearageMonth(arrearageMonth);
		insuranceFoshanUserInfo.setPensionRealPayMonth(pensionRealPayMonth);
		insuranceFoshanUserInfo.setMedicalRealPayMonth(medicalRealPayMonth);
		insuranceFoshanUserInfo.setUnemploymentRealPayMonth(unemploymentRealPayMonth);
		
		return insuranceFoshanUserInfo;
	}


}
