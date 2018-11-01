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
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zibo.InsuranceZiboInfo;
import com.microservice.dao.entity.crawler.insurance.zibo.InsuranceZiboUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;
import app.utils.CommonUtils;

@Component
public class InsuranceZiboParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private CommonUtils commonUtils;
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
		String url = "http://sdzb.hrss.gov.cn:8001/logonDialog.jsp";
		tracer.addTag("parser.login.Url", url);
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.setJavaScriptTimeout(1000);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		try {
			HtmlPage page = webClient.getPage(webRequest);
			tracer.addTag("parser.login.page", "<xmp>"+page.asXml()+"</xmp>");
			Thread.sleep(1000);
			int status = page.getWebResponse().getStatusCode();
			tracer.addTag("parser.login.status", status+"");
			if(200 == status){
				HtmlImage validateImage = page.getFirstByXPath("//img[@id='validatecode1']");
				HtmlElement logonBtn = (HtmlElement)page.getFirstByXPath("//img[@class='logonBtn']");
				String attribute = logonBtn.getAttribute("onclick");
				String appversion = attribute.substring(attribute.indexOf("(")+2, attribute.indexOf(",")-1);
				String verifycode = chaoJiYingOcrService.getVerifycode(validateImage, "1004");
				String md5Pwd = commonUtils.md5(insuranceRequestParameters.getPassword());
				
				WebRequest  requestSettings = new WebRequest(new URL("http://sdzb.hrss.gov.cn:8001/logon.do"), HttpMethod.GET);
				requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
				requestSettings.getRequestParameters().add(new NameValuePair("method", "doLogon"));
				requestSettings.getRequestParameters().add(new NameValuePair("_xmlString", "<?xml version='1.0' encoding='UTF-8'?><p><s userid='"+insuranceRequestParameters.getUsername()+"'/><s usermm='"+md5Pwd+"'/><s authcode='"+verifycode+"'/><s yxzjlx='A'/><s appversion='"+appversion+"'/></p>"));
				requestSettings.getRequestParameters().add(new NameValuePair("_random", "0.9454396308620945"));
				
				HtmlPage checkPage = webClient.getPage(requestSettings);
				tracer.addTag("parser.login.checkPage", "<xmp>"+checkPage.asXml()+"</xmp>");
				String checkMsg = checkPage.getWebResponse().getContentAsString();
				tracer.addTag("parser.login.checkMsg", checkMsg);
				if(checkMsg.contains("true")){
					HtmlPage loginPage = webClient.getPage(webRequest);
					tracer.addTag("parser.login.loginPage", "<xmp>"+loginPage.asXml()+"</xmp>");
					Thread.sleep(1000);
					status = loginPage.getWebResponse().getStatusCode();
					tracer.addTag("parser.login.status2", status+"");
					if(200 == status){
						HtmlTextInput loginName = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='yhmInput']");
						HtmlPasswordInput loginPassword = (HtmlPasswordInput)loginPage.getFirstByXPath("//input[@id='mmInput']");
						HtmlImage validatecode = loginPage.getFirstByXPath("//img[@id='validatecode1']");
						HtmlTextInput validateInput = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='validatecodevalue1']");
						HtmlElement submitbt = (HtmlElement)loginPage.getFirstByXPath("//img[@class='logonBtn']");
						
						loginName.setText(insuranceRequestParameters.getUsername());
						loginPassword.setText(insuranceRequestParameters.getPassword());
						validateInput.setText(chaoJiYingOcrService.getVerifycode(validatecode, "1004"));
						HtmlPage loginedPage = submitbt.click();
						tracer.addTag("parser.login.loginedPage", "<xmp>"+loginedPage.asXml()+"</xmp>");
						
						webParam.setPage(loginedPage);
						webParam.setUrl(loginedPage.getUrl().toString());
						tracer.addTag("parser.login.loginedPage.url", loginedPage.getUrl().toString());
						webParam.setCode(loginedPage.getWebResponse().getStatusCode());
					}
				}else{
					webParam.setPage(checkPage);
					webParam.setUrl(checkPage.getUrl().toString());
					webParam.setCode(checkPage.getWebResponse().getStatusCode());
				}
				
				return webParam;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	/**
	 * @Des 获取个人信息
	 * @return
	 * @throws Exception 
	 */
	public WebParam<InsuranceZiboUserInfo> getUserInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://sdzb.hrss.gov.cn:8001/hspUser.do";
		tracer.addTag("parser.getUserInfo.Url", url);
		
		WebParam<InsuranceZiboUserInfo> webParam = new WebParam<InsuranceZiboUserInfo>();
		webParam = commonUtils.getPostParameters(taskInsurance.getTaskid());
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String method = "fwdQueryPerInfo";
		HtmlPage userInfoPage = getHtmlByPost(url, webClient, method, webParam.getUsersessionuuid(), webParam.getLaneid());
		
		int statusCode = userInfoPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getUserInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = userInfoPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.getUserInfo.html", "<xmp>"+html+"</xmp>");
			List<InsuranceZiboUserInfo> insuranceFoshanUserInfos = new ArrayList<InsuranceZiboUserInfo>();
			InsuranceZiboUserInfo insuranceZiboUserInfo = userInfoHtmlParser(html);
			if(null != insuranceZiboUserInfo){
				insuranceZiboUserInfo.setTaskid(taskInsurance.getTaskid());
				insuranceFoshanUserInfos.add(insuranceZiboUserInfo);
				webParam.setList(insuranceFoshanUserInfos);
			}
			webParam.setPage(userInfoPage);
			webParam.setHtml(html);
			return webParam;
		}
		
		return webParam;
	}
	
	/**
	 * @Des 获取养老信息
	 * @return
	 * @throws Exception 
	 */
	public WebParam<InsuranceZiboInfo> getPensionInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://sdzb.hrss.gov.cn:8001/siAd.do";
		tracer.addTag("parser.getPensionInfo.Url", url);
		
		WebParam<InsuranceZiboInfo> webParam = new WebParam<InsuranceZiboInfo>();
		webParam = commonUtils.getPostParameters(taskInsurance.getTaskid());
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String method = "queryAgedPayHis";
		HtmlPage pensionInfoPage = getHtmlByPost(url, webClient, method, webParam.getUsersessionuuid(), webParam.getLaneid());
		
		int statusCode = pensionInfoPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getPensionInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = pensionInfoPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.getPensionInfo.html", "<xmp>"+html+"</xmp>");
			List<InsuranceZiboInfo> insuranceZiboInfos = insuranceInfoHtmlParser(taskInsurance, html, "pension");
			if(null != insuranceZiboInfos){
				webParam.setList(insuranceZiboInfos);
			}
			webParam.setPage(pensionInfoPage);
			webParam.setHtml(html);
			return webParam;
		}
		
		return webParam;
	}
	
	/**
	 * @Des 获取医保信息
	 * @return
	 * @throws Exception 
	 */
	public WebParam<InsuranceZiboInfo> getMedicalInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://sdzb.hrss.gov.cn:8001/siMedi.do";
		tracer.addTag("parser.getMedicalInfo.Url", url);
		
		WebParam<InsuranceZiboInfo> webParam = new WebParam<InsuranceZiboInfo>();
		webParam = commonUtils.getPostParameters(taskInsurance.getTaskid());
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String method = "queryMediPayHis";
		HtmlPage medicalInfoPage = getHtmlByPost(url, webClient, method, webParam.getUsersessionuuid(), webParam.getLaneid());
		
		int statusCode = medicalInfoPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getMedicalInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = medicalInfoPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.getMedicalInfo.html", "<xmp>"+html+"</xmp>");
			List<InsuranceZiboInfo> insuranceZiboInfos = insuranceInfoHtmlParser(taskInsurance, html, "medical");
			if(null != insuranceZiboInfos){
				webParam.setList(insuranceZiboInfos);
			}
			webParam.setPage(medicalInfoPage);
			webParam.setHtml(html);
			return webParam;
		}
		
		return webParam;
	}

	/**
	 * @Des 获取工伤信息
	 * @return
	 * @throws Exception 
	 */
	public WebParam<InsuranceZiboInfo> getInjuryInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://sdzb.hrss.gov.cn:8001/siHarm.do";
		tracer.addTag("parser.getInjuryInfo.Url", url);
		
		WebParam<InsuranceZiboInfo> webParam = new WebParam<InsuranceZiboInfo>();
		webParam = commonUtils.getPostParameters(taskInsurance.getTaskid());
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String method = "queryHarmPayHis";
		HtmlPage injuryInfoPage = getHtmlByPost(url, webClient, method, webParam.getUsersessionuuid(), webParam.getLaneid());
		
		int statusCode = injuryInfoPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getInjuryInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = injuryInfoPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.getInjuryInfo.html", "<xmp>"+html+"</xmp>");
			List<InsuranceZiboInfo> insuranceZiboInfos = insuranceInfoHtmlParser(taskInsurance, html, "injury");
			if(null != insuranceZiboInfos){
				webParam.setList(insuranceZiboInfos);
			}
			webParam.setPage(injuryInfoPage);
			webParam.setHtml(html);
			return webParam;
		}
		
		return webParam;
	}
	
	/**
	 * @Des 获取生育信息
	 * @return
	 * @throws Exception 
	 */
	public WebParam<InsuranceZiboInfo> getBearInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://sdzb.hrss.gov.cn:8001/siBirth.do";
		tracer.addTag("parser.getBearInfo.Url", url);
		
		WebParam<InsuranceZiboInfo> webParam = new WebParam<InsuranceZiboInfo>();
		webParam = commonUtils.getPostParameters(taskInsurance.getTaskid());
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String method = "queryBirthPayHis";
		HtmlPage bearInfoPage = getHtmlByPost(url, webClient, method, webParam.getUsersessionuuid(), webParam.getLaneid());
		
		int statusCode = bearInfoPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getBearInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = bearInfoPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.getBearInfo.html", "<xmp>"+html+"</xmp>");
			List<InsuranceZiboInfo> insuranceZiboInfos = insuranceInfoHtmlParser(taskInsurance, html, "bear");
			if(null != insuranceZiboInfos){
				webParam.setList(insuranceZiboInfos);
			}
			webParam.setPage(bearInfoPage);
			webParam.setHtml(html);
			return webParam;
		}
		
		return webParam;
	}
	
	
	/**
	 * @Des 获取失业信息
	 * @return
	 * @throws Exception 
	 */
	public WebParam<InsuranceZiboInfo> getUnemploymentInfo(TaskInsurance taskInsurance) throws Exception{
		String url = "http://sdzb.hrss.gov.cn:8001/siLost.do";
		tracer.addTag("parser.getUnemploymentInfo.Url", url);
		
		WebParam<InsuranceZiboInfo> webParam = new WebParam<InsuranceZiboInfo>();
		webParam = commonUtils.getPostParameters(taskInsurance.getTaskid());
		webParam.setUrl(url);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String method = "queryLostPayHis";
		HtmlPage unemploymentInfoPage = getHtmlByPost(url, webClient, method, webParam.getUsersessionuuid(), webParam.getLaneid());
		
		int statusCode = unemploymentInfoPage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
		tracer.addTag("parser.getUnemploymentInfo.statusCode", statusCode+"");
		if(200 == statusCode){
			String html = unemploymentInfoPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.getUnemploymentInfo.html", "<xmp>"+html+"</xmp>");
			List<InsuranceZiboInfo> insuranceZiboInfos = insuranceInfoHtmlParser(taskInsurance, html, "unemployment");
			if(null != insuranceZiboInfos){
				webParam.setList(insuranceZiboInfos);
			}
			webParam.setPage(unemploymentInfoPage);
			webParam.setHtml(html);
			return webParam;
		}
		
		return webParam;
	}
	
	
	/**
	 * @Des 解析个人信息页面
	 * @return
	 * @throws Exception 
	 */
	private InsuranceZiboUserInfo userInfoHtmlParser(String html) throws Exception{
		Document doc = Jsoup.parse(html);
		InsuranceZiboUserInfo insuranceZiboUserInfo = new InsuranceZiboUserInfo();
		
		String name = doc.getElementById("xm").val();
		String idNum = doc.getElementById("sfzhm").val();
		String gender = doc.getElementById("xbmc").val();
		String birthday = doc.getElementById("csrq").val();
		String nation = doc.getElementById("mzmc").val();
		String marriage = doc.getElementById("hyzkmc").val();
		String educationLevels = doc.getElementById("whcdmc").val();
		String residenceType = doc.getElementById("hkxzmc").val();
		String administrativeDuty = doc.getElementById("xzzwmc").val();
		String contact = doc.getElementById("lxr").val();
		String signupTel = doc.getElementById("lxdh").val();
		String postcode = doc.getElementById("yzbm").val();
		String familyAddress = doc.getElementById("jtzz").val();
		String postAddress = doc.getElementById("txdz").val();
		String residenceArea = doc.getElementById("hkszd").val();
		String organizationname = doc.getElementById("dwmc").val();
		
		insuranceZiboUserInfo.setName(name);
		insuranceZiboUserInfo.setIdNum(idNum);
		insuranceZiboUserInfo.setGender(gender);
		insuranceZiboUserInfo.setBirthday(birthday);
		insuranceZiboUserInfo.setNation(nation);
		insuranceZiboUserInfo.setMarriage(marriage);
		insuranceZiboUserInfo.setEducationLevels(educationLevels);
		insuranceZiboUserInfo.setResidenceType(residenceType);
		insuranceZiboUserInfo.setAdministrativeDuty(administrativeDuty);
		insuranceZiboUserInfo.setContact(contact);
		insuranceZiboUserInfo.setSignupTel(signupTel);
		insuranceZiboUserInfo.setPostcode(postcode);
		insuranceZiboUserInfo.setFamilyAddress(familyAddress);
		insuranceZiboUserInfo.setPostAddress(postAddress);
		insuranceZiboUserInfo.setResidenceArea(residenceArea);
		insuranceZiboUserInfo.setOrganizationname(organizationname);
		
		return insuranceZiboUserInfo;
	}
	
	/**
	 * @Des 社保信息页面解析
	 * @param html
	 * @param string
	 * @return
	 */
	private List<InsuranceZiboInfo> insuranceInfoHtmlParser(TaskInsurance taskInsurance, String html, String type) {
		Document doc = Jsoup.parse(html);
		List<InsuranceZiboInfo> insuranceZiboInfos = new ArrayList<InsuranceZiboInfo>();
		
		Elements elements = doc.select("[onmouseover=this.style.backgroundColor='#ffb5b5']");
		for (Element element : elements) {
			Elements inputs = element.select("input");
			List<String> str = new ArrayList<String>();
			InsuranceZiboInfo insuranceZiboInfo = new InsuranceZiboInfo();
			for (Element element2 : inputs) {
				String text = element2.val();
				str.add(text);
			}
			if(null != str && str.size() != 0){
				if(type.equals("pension")){
					insuranceZiboInfo.setInsuranceType(str.get(0));
					insuranceZiboInfo.setPayDate(str.get(1));
					insuranceZiboInfo.setPersonalPayBase(str.get(2));
					insuranceZiboInfo.setCompanyPay(str.get(3));
					insuranceZiboInfo.setPersonalPay(str.get(4));
				}else if(type.equals("unemployment")){
					insuranceZiboInfo.setInsuranceType(str.get(0));
					insuranceZiboInfo.setPayDate(str.get(1));
					insuranceZiboInfo.setPayBase(str.get(2));
					insuranceZiboInfo.setCompanyPay(str.get(3));
					insuranceZiboInfo.setPersonalPay(str.get(4));
				}else if(type.equals("medical")){
					insuranceZiboInfo.setInsuranceType(str.get(0));
					insuranceZiboInfo.setPayDate(str.get(1));
					insuranceZiboInfo.setPersonalPayBase(str.get(2));
					insuranceZiboInfo.setCompanyPayBase(str.get(3));
					insuranceZiboInfo.setCompanyPay(str.get(4));
					insuranceZiboInfo.setPersonalPay(str.get(5));
				}else if(type.equals("injury") || type.equals("bear")){
					insuranceZiboInfo.setInsuranceType(str.get(0));
					insuranceZiboInfo.setPayDate(str.get(1));
					insuranceZiboInfo.setPayBase(str.get(2));
					insuranceZiboInfo.setCompanyPay(str.get(3));
				}
				insuranceZiboInfo.setTaskid(taskInsurance.getTaskid());
				insuranceZiboInfo.setType(type);
				insuranceZiboInfos.add(insuranceZiboInfo);
			}
		}
		
		return insuranceZiboInfos;
	}

	/**
	 * @Des POST请求方法封装，以及添加参数
	 * @return
	 * @throws Exception 
	 */
	public static HtmlPage getHtmlByPost(String url,WebClient webClient,String method,String usersessionuuid,String laneid) throws Exception{
		
		URL urlAction = new URL(url);
		WebRequest  requestSettings = new WebRequest(urlAction, HttpMethod.POST);
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("method", method));
		requestSettings.getRequestParameters().add(new NameValuePair("_random", "0.07056168409558583"));
		requestSettings.getRequestParameters().add(new NameValuePair("__usersession_uuid", usersessionuuid));
		requestSettings.getRequestParameters().add(new NameValuePair("_laneID", laneid));
		
		HtmlPage searchPage = webClient.getPage(requestSettings);
		return searchPage;
	}



}
