package app.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingInjury;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingMaternity;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingMedical;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingPension;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingUnemployment;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingUserInfo;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceBeijingParser {
	
	public static final Logger log = LoggerFactory.getLogger(InsuranceBeijingParser.class);
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
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance) throws Exception {
		String url = "http://www.bjrbj.gov.cn/csibiz/indinfo/login.jsp";
		
		WebParam webParam= new WebParam();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());		
		String loginUrl = "http://www.bjrbj.gov.cn/csibiz/indinfo/login_check?type=1&flag=3&j_username="+insuranceRequestParameters.getUsername()+"&j_password="+insuranceRequestParameters.getPassword()+"&safecode="+taskInsurance.getPid()+"&i_phone="+insuranceRequestParameters.getMessage()+"&x=47&y=26";
		HtmlPage adminPage = getHtml(loginUrl,webClient);
		
		webParam.setPage(adminPage);
		return webParam;
	}

	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public WebParam getUserInfo(TaskInsurance taskInsurance, WebClient webClient) throws Exception {
		
		WebParam webParam= new WebParam();
		String url = "http://www.bjrbj.gov.cn/csibiz/indinfo/search/ind/indNewInfoSearchAction";
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
		
		requestSettings.setAdditionalHeader("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
	    requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
	    requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
	    requestSettings.setAdditionalHeader("Connection", "keep-alive");
	    requestSettings.setAdditionalHeader("Host", "www.bjrbj.gov.cn");
	    requestSettings.setAdditionalHeader("Referer", "http://www.bjrbj.gov.cn/csibiz/indinfo/search/ind/ind_new_info_index.jsp");
	    requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
	    requestSettings.setAdditionalHeader("X-Prototype-Version", "1.5.1.1"); 
	    requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); 
	    
	    HtmlPage page = webClient.getPage(requestSettings);
	    int statusCode = page.getWebResponse().getStatusCode();
	    if(200 == statusCode){
	    	String html = page.getWebResponse().getContentAsString();
	    	tracer.addTag("parser.crawler.getUserinfo 个人信息","<xmp>"+html+"</xmp>");
	    	InsuranceBeijingUserInfo userInfo = htmlParser(html,taskInsurance);
	    	
	    	webParam.setCode(statusCode);
	    	webParam.setInsuranceBeijingUserInfo(userInfo);
//	    	webParam.setPage(page);
	    	webParam.setUrl(url);
	    	webParam.setHtml(html);
	    	return webParam;
	    	
	    }
		return null;
	}

	/**
	 * @Des 解析个人信息
	 * @param html
	 * @return
	 */
	private InsuranceBeijingUserInfo htmlParser(String html,TaskInsurance taskInsurance) {
		String companyName = null;
		String organizationCode = null;
		String insuranceRegisterNum = null;
		String county = null;
		
		InsuranceBeijingUserInfo insuranceBeijingUserInfo = new InsuranceBeijingUserInfo();
		Document doc = Jsoup.parse(html);
		String message = insuranceService.getMsgByKeyword(doc,"组织机构代码");
		String regex = "单位名称：(.*)?组织机构代码：(.*)?社会保险登记：(.*)?所属区县：(.*)?";
		Pattern p = Pattern.compile(regex);
		System.out.println("****************************个人信息"+message);
		Matcher m = p.matcher(message.replace("&nbsp;", ""));
		if(m.find()){
			companyName = m.group(1);
			organizationCode = m.group(2);
			insuranceRegisterNum = m.group(3);
			county = m.group(4);
		}
				
		String name = insuranceService.getNextLabelByKeyword(doc, "*姓 名");
		String idNum = insuranceService.getNextLabelByKeyword(doc, "*公民身份号码");
		String sex = insuranceService.getNextLabelByKeyword(doc, "性 别");
		String birthdate = insuranceService.getNextLabelByKeyword(doc, "出生日期");
		String nation = insuranceService.getNextLabelByKeyword(doc, "民 族");
		String country = insuranceService.getNextLabelByKeyword(doc, "国家/地区");
		String personalIdentity = insuranceService.getNextLabelByKeyword(doc, "个人身份");
		String workDate = insuranceService.getNextLabelByKeyword(doc, "参加工作日期");
		String category = insuranceService.getNextLabelByKeyword(doc, "户口性质");
		String hkadr = insuranceService.getNextLabelByKeyword(doc, "户口所在地地址");
		String hkadrCode = insuranceService.getNextLabelByKeyword(doc, "户口所在地邮政编码");
		String residenceAddres = insuranceService.getNextLabelByKeyword(doc, "居住地(联系)地址");
		String residenceAddresCode = insuranceService.getNextLabelByKeyword(doc, "居住地（联系）邮政编码");
		String statementsType = insuranceService.getNextLabelByKeyword(doc, "获取对账单方式");
		String education = insuranceService.getNextLabelByKeyword(doc, "文化程度");
		String insuredPhone = insuranceService.getNextLabelByKeyword(doc, "参保人电话");
		String averageSalary = insuranceService.getNextLabelByKeyword(doc, "申报月均工资收入（元）");
		String issuingBank = insuranceService.getNextLabelByKeyword(doc, "委托代发银行名称");
		String issuingBankAccount = insuranceService.getNextLabelByKeyword(doc, "委托代发银行账号");
		String payPersonalCategory = insuranceService.getNextLabelByKeyword(doc, "缴费人员类别");
		String insuredType = insuranceService.getNextLabelByKeyword(doc, "医疗参保人员类别");
		String specialDisease = insuranceService.getNextLabelByKeyword(doc, "是否患有特殊病");
		
		insuranceBeijingUserInfo.setAverageSalary(averageSalary);
		insuranceBeijingUserInfo.setBirthdate(birthdate);
		insuranceBeijingUserInfo.setCategory(category);
		insuranceBeijingUserInfo.setCompanyName(companyName);
		insuranceBeijingUserInfo.setCountry(country);
		insuranceBeijingUserInfo.setEducation(education);
		insuranceBeijingUserInfo.setHkadr(hkadr);
		insuranceBeijingUserInfo.setHkadrCode(hkadrCode);
		insuranceBeijingUserInfo.setIdNum(idNum);
		insuranceBeijingUserInfo.setInsuranceRegisterNum(insuranceRegisterNum);
		insuranceBeijingUserInfo.setInsuredPhone(insuredPhone);
		insuranceBeijingUserInfo.setInsuredType(insuredType);
		insuranceBeijingUserInfo.setIssuingBank(issuingBank);
		insuranceBeijingUserInfo.setIssuingBankAccount(issuingBankAccount);
		insuranceBeijingUserInfo.setName(name);
		insuranceBeijingUserInfo.setNation(nation);
		insuranceBeijingUserInfo.setOrganizationCode(organizationCode);
		insuranceBeijingUserInfo.setPayPersonalCategory(payPersonalCategory);
		insuranceBeijingUserInfo.setPersonalIdentity(personalIdentity);
		insuranceBeijingUserInfo.setResidenceAddres(residenceAddres);
		insuranceBeijingUserInfo.setResidenceAddresCode(residenceAddresCode);
		insuranceBeijingUserInfo.setSex(sex);
		insuranceBeijingUserInfo.setSpecialDisease(specialDisease);
		insuranceBeijingUserInfo.setStatementsType(statementsType);
		insuranceBeijingUserInfo.setWorkDate(workDate);
		insuranceBeijingUserInfo.setTaskid(taskInsurance.getTaskid());
		insuranceBeijingUserInfo.setCounty(county);
		return insuranceBeijingUserInfo;
	}

	/**
	 * @Des 获取养老保险
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceBeijingPension> getPension(TaskInsurance taskInsurance, Set<Cookie> cookies,String searchYear) {
		WebParam<InsuranceBeijingPension> webParam= new WebParam<InsuranceBeijingPension>();
		
		try {
			HtmlPage page = getHtml("oldage",searchYear,cookies);
			if(null != page){
				String html = page.getWebResponse().getContentAsString();
				List<InsuranceBeijingPension> pensions = parser(html,taskInsurance);
				webParam.setHtml(html);
				webParam.setList(pensions);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * @Des 解析养老保险
	 * @param html
	 * @param taskInsurance
	 * @return 
	 * @return
	 */
	private List<InsuranceBeijingPension> parser(String html, TaskInsurance taskInsurance) {
		List<InsuranceBeijingPension> pensions = new ArrayList<InsuranceBeijingPension>();
		List<Elements> list = getTdLabel(html);
		if(null != list){
			for(Elements tds : list){
				String payMonth = tds.get(0).text();
				String payType = tds.get(1).text();
				String payCardinality = tds.get(2).text();
				String payDepartment = tds.get(3).text();
				String payPerson = tds.get(4).text();
				String payDepartmentName = tds.get(5).text();
				
				InsuranceBeijingPension insuranceBeijingPension = new InsuranceBeijingPension();
				insuranceBeijingPension.setPayCardinality(payCardinality);
				insuranceBeijingPension.setPayDepartment(payDepartment);
				insuranceBeijingPension.setPayDepartmentName(payDepartmentName);
				insuranceBeijingPension.setPayMonth(payMonth);
				insuranceBeijingPension.setPayPerson(payPerson);
				insuranceBeijingPension.setPayType(payType);
				insuranceBeijingPension.setTaskid(taskInsurance.getTaskid());
				pensions.add(insuranceBeijingPension);
			}
		}
		return pensions;
	}
	
	public List<Elements> getTdLabel(String html){
		List<Elements> list = new ArrayList<Elements>();
		Document doc = Jsoup.parse(html);
		Elements trs = doc.select("tr");
		if(null != trs && trs.size()>0){
			for(int i=2;i<trs.size()-1;i++){
				Element tr = trs.get(i);
				Elements tds = tr.select("td");
				if(null != tds && tds.size()>0){
					list.add(tds);
				}
			}
		}
		return list;
	}

	public HtmlPage getHtml(String type, String searchYear, Set<Cookie> cookies) throws Exception{
		
		String url = "http://www.bjrbj.gov.cn/csibiz/indinfo/search/ind/indPaySearchAction!"+type+"?searchYear="+searchYear+"&time="+System.currentTimeMillis();
		
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
		
		requestSettings.setAdditionalHeader("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
	    requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
	    requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
	    requestSettings.setAdditionalHeader("Connection", "keep-alive");
//	    requestSettings.setAdditionalHeader("Content-Length", "0");
	    requestSettings.setAdditionalHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
	    requestSettings.setAdditionalHeader("Host", "www.bjrbj.gov.cn");
	    requestSettings.setAdditionalHeader("Referer", "http://www.bjrbj.gov.cn/csibiz/indinfo/search/ind/ind_pay_index.jsp");
	    requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
	    requestSettings.setAdditionalHeader("X-Prototype-Version", "1.5.1.1"); 
	    requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); 
	    
	    HtmlPage page = webClient.getPage(requestSettings);
	    int statusCode = page.getWebResponse().getStatusCode();
	    if(200 == statusCode){
	    	String html = page.getWebResponse().getContentAsString();
	    	tracer.addTag("getHtml "+type+" "+searchYear, "<xmp>"+html+"</xmp>");
	    	if(html.contains("没有找到符合条件的个人用户信息")){
	    		return null;
	    	}
	    	return page;
	    }		
		return null;		
	}

	/**
	 * @Des 获取失业信息
	 * @param taskInsurance
	 * @param cookies
	 * @param searchYear
	 * @return
	 */
	public WebParam<InsuranceBeijingUnemployment> getUnemployment(TaskInsurance taskInsurance, Set<Cookie> cookies,
			String searchYear) {
		WebParam<InsuranceBeijingUnemployment> webParam= new WebParam<InsuranceBeijingUnemployment>();
		
		try {
			HtmlPage page = getHtml("unemployment",searchYear,cookies);
			if(null != page){
				String html = page.getWebResponse().getContentAsString();
				List<InsuranceBeijingUnemployment> unemployments = parserUnemployment(html,taskInsurance);
				webParam.setHtml(html);
				webParam.setList(unemployments);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * @Des 解析失业保险
	 * @param html
	 * @param taskInsurance
	 * @return 
	 * @return
	 */
	private List<InsuranceBeijingUnemployment> parserUnemployment(String html, TaskInsurance taskInsurance) {
		List<InsuranceBeijingUnemployment> unemployments = new ArrayList<InsuranceBeijingUnemployment>();
		List<Elements> list = getTdLabel(html);
		if(null != list){
			for(Elements tds : list){
				String payMonth = tds.get(0).text();
				String payCardinality = tds.get(1).text();
				String payDepartment = tds.get(2).text();
				String payPerson = tds.get(3).text();
				
				InsuranceBeijingUnemployment insuranceBeijingUnemployment = new InsuranceBeijingUnemployment();
				insuranceBeijingUnemployment.setPayCardinality(payCardinality);
				insuranceBeijingUnemployment.setPayDepartment(payDepartment);
				insuranceBeijingUnemployment.setPayMonth(payMonth);
				insuranceBeijingUnemployment.setPayPerson(payPerson);
				insuranceBeijingUnemployment.setTaskid(taskInsurance.getTaskid());
				unemployments.add(insuranceBeijingUnemployment);
			}
		}
		return unemployments;
	}

	
	/**
	 * @Des 获取工伤保险信息
	 * @param taskInsurance
	 * @param webClient
	 * @param searchYear
	 * @return
	 */
	public WebParam<InsuranceBeijingInjury> getInjury(TaskInsurance taskInsurance, Set<Cookie> cookies,
			String searchYear) {
		WebParam<InsuranceBeijingInjury> webParam= new WebParam<InsuranceBeijingInjury>();
		
		try {
			HtmlPage page = getHtml("injuries",searchYear,cookies);
			if(null != page){
				String html = page.getWebResponse().getContentAsString();
				List<InsuranceBeijingInjury> injuries = parserInjury(html,taskInsurance);
				webParam.setHtml(html);
				webParam.setList(injuries);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @Des 解析工伤信息
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceBeijingInjury> parserInjury(String html, TaskInsurance taskInsurance) {
		List<InsuranceBeijingInjury> injuries = new ArrayList<InsuranceBeijingInjury>();
		List<Elements> list = getTdLabel(html);
		if(null != list){
			for(Elements tds : list){
				String payMonth = tds.get(0).text();
				String payCardinality = tds.get(1).text();
				String payDepartment = tds.get(2).text();
				
				InsuranceBeijingInjury insuranceBeijingInjury = new InsuranceBeijingInjury();
				insuranceBeijingInjury.setPayCardinality(payCardinality);
				insuranceBeijingInjury.setPayDepartment(payDepartment);
				insuranceBeijingInjury.setPayMonth(payMonth);
				insuranceBeijingInjury.setTaskid(taskInsurance.getTaskid());
				injuries.add(insuranceBeijingInjury);
			}
		}
		return injuries;
	}

	/**@Des 获取生育保险
	 * @param taskInsurance
	 * @param cookies
	 * @param searchYear
	 * @return
	 */
	public WebParam<InsuranceBeijingMaternity> getBear(TaskInsurance taskInsurance, Set<Cookie> cookies,
			String searchYear) {
		WebParam<InsuranceBeijingMaternity> webParam= new WebParam<InsuranceBeijingMaternity>();
		
		try {
			HtmlPage page = getHtml("maternity",searchYear,cookies);
			if(null != page){
				String html = page.getWebResponse().getContentAsString();
				List<InsuranceBeijingMaternity> bears = parserBear(html,taskInsurance);
				webParam.setHtml(html);
				webParam.setList(bears);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @Des 解析生育保险
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceBeijingMaternity> parserBear(String html, TaskInsurance taskInsurance) {
		List<InsuranceBeijingMaternity> maternitys = new ArrayList<InsuranceBeijingMaternity>();
		List<Elements> list = getTdLabel(html);
		if(null != list){
			for(Elements tds : list){
				String payMonth = tds.get(0).text();
				String payCardinality = tds.get(1).text();
				String payDepartment = tds.get(2).text();
				
				InsuranceBeijingMaternity insuranceBeijingMaternity = new InsuranceBeijingMaternity();
				insuranceBeijingMaternity.setPayCardinality(payCardinality);
				insuranceBeijingMaternity.setPayDepartment(payDepartment);
				insuranceBeijingMaternity.setPayMonth(payMonth);
				insuranceBeijingMaternity.setTaskid(taskInsurance.getTaskid());
				maternitys.add(insuranceBeijingMaternity);
			}
		}
		return maternitys;
	}

	/**
	 * @Des 获取医疗信息
	 * @param taskInsurance
	 * @param cookies
	 * @param searchYear
	 * @return
	 */
	public WebParam<InsuranceBeijingMedical> getMedical(TaskInsurance taskInsurance, Set<Cookie> cookies,
			String searchYear) {
		WebParam<InsuranceBeijingMedical> webParam= new WebParam<InsuranceBeijingMedical>();
		
		try {
			HtmlPage page = getHtml("medicalcare",searchYear,cookies);
			if(null != page){
				String html = page.getWebResponse().getContentAsString();
				List<InsuranceBeijingMedical> medicals = parserMedical(html,taskInsurance);
				webParam.setHtml(html);
				webParam.setList(medicals);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @Des 解析医疗保险
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceBeijingMedical> parserMedical(String html, TaskInsurance taskInsurance) {
		List<InsuranceBeijingMedical> medicals = new ArrayList<InsuranceBeijingMedical>();
		List<Elements> list = getTdLabel(html);
		if(null != list){
			for(Elements tds : list){
				String payMonth = tds.get(0).text();
				String payType = tds.get(1).text();
				String payCardinality = tds.get(2).text();
				String payDepartment = tds.get(3).text();
				String payPerson = tds.get(4).text();
				String payDepartmentName = tds.get(5).text();
				
				InsuranceBeijingMedical insuranceBeijingMedical = new InsuranceBeijingMedical();
				insuranceBeijingMedical.setPayCardinality(payCardinality);
				insuranceBeijingMedical.setPayDepartment(payDepartment);
				insuranceBeijingMedical.setPayDepartmentName(payDepartmentName);
				insuranceBeijingMedical.setPayMonth(payMonth);
				insuranceBeijingMedical.setPayPerson(payPerson);
				insuranceBeijingMedical.setPayType(payType);
				insuranceBeijingMedical.setTaskid(taskInsurance.getTaskid());
				medicals.add(insuranceBeijingMedical);
			}
		}
		return medicals;
	}

	/**
	 * @Des 校验
	 * @param insuranceRequestParameters
	 * @param taskInsurance
	 * @return
	 */
	public WebParam validate(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.bjrbj.gov.cn/csibiz/indinfo/login.jsp";
		HtmlPage html = getHtml(url,webClient);
		if(null != html){
			HtmlImage image = html.getFirstByXPath("//img[@id='indsafecode']");
			String code = null;
			try {
				code = chaoJiYingOcrService.getVerifycode(image, "1004");
			} catch (Exception e) {
				e.printStackTrace();
			}
			tracer.addTag("verifyCode ==>", code);
			
			String sendSmsUrl = "http://www.bjrbj.gov.cn/csibiz/indinfo/passwordSetAction!getTelSafeCode?"
					+ "idCode="+insuranceRequestParameters.getUsername()+"&logPass="+insuranceRequestParameters.getPassword()+"&safeCode="+code;
			UnexpectedPage smsPage = getHtml1(sendSmsUrl,webClient);
			tracer.addTag("parser.validate.smspage", smsPage.getWebResponse().getContentAsString());
			
			String content = smsPage.getWebResponse().getContentAsString();
			if(content.contains("附加码错误")){
				webParam.setCode(102);
			}else if(content.contains("请您完善您的注册手机号")){
				webParam.setCode(103);
			}else if(content.contains("密码输入错误")){
				webParam.setCode(101);
			}else if(content.contains("您的短信验证码还在有效期内")){
				webParam.setCode(199);
			}else if(content.contains("短信验证码已发送至您的手机")){
				webParam.setWebClient(webClient);
				webParam.setCode(200);
				webParam.setHtml(code);
			}else{
				webParam.setCode(104);
			}
			
		}
		return webParam;
	}
	
	public HtmlPage getHtml(String url,WebClient webClient){
		WebRequest webRequest;
		HtmlPage searchPage = null;
		try {
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			searchPage = webClient.getPage(webRequest);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return searchPage;	
	}
	
	public UnexpectedPage getHtml1(String url,WebClient webClient){
		WebRequest webRequest;
		UnexpectedPage searchPage = null;
		try {
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			searchPage = webClient.getPage(webRequest);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return searchPage;
		
	}

}
