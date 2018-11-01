package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingCompany;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingFirst;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceChongqingParser {
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
	@SuppressWarnings("rawtypes")
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
	    String url = "http://ggfw.cqhrss.gov.cn/ggfw/index1.jsp?code=000";		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		webRequest.setCharset(Charset.forName("UTF-8"));
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("Host", "ggfw.cqhrss.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://ggfw.cqhrss.gov.cn/ggfw/index1.jsp");
	
		HtmlPage page = webClient.getPage(webRequest);
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效 
		int status = page.getWebResponse().getStatusCode();
		if(200 == status){			
			HtmlImage image = page.getFirstByXPath("//img[@id='yzmimg']");
			String code = chaoJiYingOcrService.getVerifycode(image, "1004");
			tracer.addTag("verifyCode ==>", code);
			HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("//input[@id='sfzh']"); 
			HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
			HtmlTextInput verifyCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='validateCode']");
			HtmlElement loginButton = (HtmlElement)page.getFirstByXPath("//input[@id='loginBtn']");			
			if (loginButton == null) {
				tracer.addTag("InsuranceChongqingParser.login",
						insuranceRequestParameters.getTaskId() + "login button can not found : null");
				throw new Exception("login button can not found : null");
			} else {
				username.setText(insuranceRequestParameters.getUsername());
				password.setText(insuranceRequestParameters.getPassword());
				verifyCode.setText(code);								
				HtmlPage loginPage = loginButton.click();
				Thread.sleep(1500);
				String html=loginPage.getWebResponse().getContentAsString();
				String alertMsg = WebCrawler.getAlertMsg();
				webParam.setAlertMsg(alertMsg);
				webParam.setCode(loginPage.getWebResponse().getStatusCode());
				webParam.setUrl(url);
				webParam.setPage(loginPage);
				webParam.setHtml(html);	
				tracer.addTagWrap("login",html);
				Document doc = Jsoup.parse(html);			
				Element basicInfo_table=doc.getElementById("basicInfoTable");
				if (null !=basicInfo_table) {									
					webParam.setCode(1001);						
					return webParam;
				}else{
					if(null != alertMsg && alertMsg.contains("密码验证失败")){  						
						webParam.setCode(1002);						
						return webParam;
				  }else if(null != alertMsg && alertMsg.contains("验证码错误")){
						webParam.setCode(1003);						
						return webParam;
				  }else{
					   webParam.setCode(1004);						
					   return webParam;
				  }					
				}				
			}
		}
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
	public WebParam getUserInfo(TaskInsurance taskInsurance, Set<Cookie> cookies) throws Exception {	
		WebParam webParam= new WebParam();
		String url = "http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_main.do?code=000";
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "ggfw.cqhrss.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_main.do?code=000"); 
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 
		HtmlPage page = webClient.getPage(webRequest);		
		int statusCode = page.getWebResponse().getStatusCode();
		Thread.sleep(1000);
	    if(200 == statusCode){
	    	String html = page.asXml();
	    	System.out.println(html);
	      	tracer.addTag("getUserInfo 个人信息","<xmp>"+html+"</xmp>");
	    	InsuranceChongqingUserInfo userInfo = htmlParserForUserInfo(html,taskInsurance);	    	
	    	List<InsuranceChongqingCompany> insuranceChongqingCompanyList=htmlParserForChongqingCompany(html,taskInsurance);
	    	List<InsuranceChongqingFirst> insuranceChongqingFirstList=htmlParserForChongqingFirst(html,taskInsurance);
	    	webParam.setCode(statusCode);
	    	webParam.setInsuranceChongqingUserInfo(userInfo);	    
	    	webParam.setCompanyList(insuranceChongqingCompanyList);
	    	webParam.setFirstList(insuranceChongqingFirstList);
	    	webParam.setPage(page);
	    	webParam.setUrl(url);
	    	webParam.setHtml(html);	
	    }
		return webParam;
	}
	/**
	 * @Des 解析个人信息
	 * @param html
	 * @return
	 */
	private InsuranceChongqingUserInfo htmlParserForUserInfo(String html, TaskInsurance taskInsurance) {
		Document doc = Jsoup.parse(html,"utf-8");	
		Element table = doc.getElementById("basicInfoTable");
		InsuranceChongqingUserInfo userInfo = new InsuranceChongqingUserInfo();
		if (null !=table) {
			String name = insuranceService.getNextLabelByKeyword(doc, "姓名");
			String sex = insuranceService.getNextLabelByKeyword(doc, "性别");
			String idNum = insuranceService.getNextLabelByKeyword(doc, "身份证号");
			String birthdate = insuranceService.getNextLabelByKeyword(doc, "出生日期");
			String personNumber = insuranceService.getNextLabelByKeyword(doc, "个人编号");
			String nation = insuranceService.getNextLabelByKeyword(doc, "民族");
			String companyNum = insuranceService.getNextLabelByKeyword(doc, "所在单位编号");
			String category = insuranceService.getNextLabelByKeyword(doc, "户口性质");	
			userInfo.setName(name);
			userInfo.setSex(sex);
			userInfo.setIdNum(idNum);
			userInfo.setBirthdate(birthdate);
			userInfo.setPersonNumber(personNumber);
			userInfo.setNation(nation);
			userInfo.setCompanyNum(companyNum);
			userInfo.setCategory(category);
			userInfo.setTaskid(taskInsurance.getTaskid());
		}		
		return userInfo;
	}
	/**
	 * @Des 参保单位信息
	 * @param html
	 * @return
	 */
	private List<InsuranceChongqingCompany> htmlParserForChongqingCompany(String html,TaskInsurance taskInsurance) {
		Document doc = Jsoup.parse(html);
		Element insurance_first_table=doc.getElementById("chooseItemTable");
		List<InsuranceChongqingCompany> companyList=new ArrayList<InsuranceChongqingCompany>();
		if (null != insurance_first_table) {
			Elements insurance_first_tbody=insurance_first_table.select("tbody");
			if (null !=insurance_first_tbody) {
				Elements insurance_first_trs=insurance_first_tbody.select("tr");
				if (insurance_first_trs.size()>=2) {
					for(int i = 1; i < insurance_first_trs.size(); i++){						
						Elements insurance_first_tds = insurance_first_trs.get(i).select("td");
						String num=insurance_first_tds.get(1).text();
						String companyName=insurance_first_tds.get(2).text();
						String personNumber=insurance_first_tds.get(3).text();
						String idNum=insurance_first_tds.get(4).text();
						String name=insurance_first_tds.get(5).text();
						String sex=insurance_first_tds.get(6).text();
						
						InsuranceChongqingCompany  company=new InsuranceChongqingCompany();
						company.setNum(num);
						company.setCompanyName(companyName);
						company.setPersonNumber(personNumber);
						company.setIdNum(idNum);
						company.setName(name);
						company.setSex(sex);
						company.setTaskid(taskInsurance.getTaskid());
						companyList.add(company);
					}
				}
			}			
		}		
		return companyList;
	}
	/**
	 * @Des 参保情况信息
	 * @param html
	 * @return
	 */
	private List<InsuranceChongqingFirst> htmlParserForChongqingFirst(String html,TaskInsurance taskInsurance) {
		Document doc = Jsoup.parse(html);
		Element  insurance_first_table=doc.getElementById("chooseItemTable").nextElementSibling();
		List<InsuranceChongqingFirst> firstList=new ArrayList<InsuranceChongqingFirst>();
		if (null !=insurance_first_table) {
			Elements insurance_first_tbody=insurance_first_table.select("tbody");
			if (null !=insurance_first_tbody) {
				Elements types=insurance_first_tbody.select("tr").get(1).select("td");
				Elements firstDates=insurance_first_tbody.select("tr").get(2).select("td");
				Elements insuranceStates=insurance_first_tbody.select("tr").get(3).select("td");							
				for (int i = 1; i < types.size(); i++) {		
					String type=types.get(i).text();
					String firstDate =firstDates.get(i).text();
					String insuranceState=insuranceStates.get(i).text();
					InsuranceChongqingFirst  first=new InsuranceChongqingFirst();
					first.setTaskid(taskInsurance.getTaskid());
					first.setType(type);					
					first.setInsuranceState(insuranceState);
					first.setFirstDate(firstDate);
					firstList.add(first);
				}				
			}	
		}
		return firstList;
	}
}
