package app.parser;

import java.net.URL;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.jian.InsuranceJiAnUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceJiAnParser {
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
	public WebParam loginForPersion(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
	    String url = "http://jasi12333.xicp.net:58699/jxquery/person/logincl";		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);	
		String requestBody="name="+insuranceRequestParameters.getUsername()+"&password="+insuranceRequestParameters.getPassword()+"&errors=";
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");	
		webRequest.setAdditionalHeader("Host", "jasi12333.xicp.net:58699");
		webRequest.setRequestBody(requestBody);
		HtmlPage page = webClient.getPage(webRequest);
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效 		
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 		
		String html=page.asXml();
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setUrl(url);
		webParam.setPage(page);
		webParam.setHtml(html);	
		tracer.addTag("login","<xmp>"+html+"</xmp>");		
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
		String url = "http://jasi12333.xicp.net:58699/jxquery/person/person_jb.jsp";
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "jasi12333.xicp.net:58699");
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 
		HtmlPage page = webClient.getPage(webRequest);		
		int statusCode = page.getWebResponse().getStatusCode();
	    if(200 == statusCode){
	    	String html = page.asXml();
	      	tracer.addTag("getUserInfo 个人信息","<xmp>"+html+"</xmp>");
	      	InsuranceJiAnUserInfo userInfo = htmlParserForUserInfo(html,taskInsurance);	    		    
	    	webParam.setCode(statusCode);
	    	webParam.setInsuranceJiAnUserInfo(userInfo);    
	     	webParam.setPage(page);
	    	webParam.setUrl(url);
	    	webParam.setHtml(html);
	    	return webParam;	    	
	    }
		return webParam;
	}
	/**
	 * @Des 解析个人信息
	 * @param html
	 * @return
	 */
	private InsuranceJiAnUserInfo htmlParserForUserInfo(String html, TaskInsurance taskInsurance) {
		Document doc = Jsoup.parse(html,"utf-8");	
		String usernum = insuranceService.getNextLabelByKeyword(doc, "个人编号");
		String username = insuranceService.getNextLabelByKeyword(doc, "姓名");
		String idnum = insuranceService.getNextLabelByKeyword(doc, "身份证号");
		String gender = insuranceService.getNextLabelByKeyword(doc, "性别");
		String insureplace = insuranceService.getNextLabelByKeyword(doc, "参保所在地");
		String companyName = insuranceService.getNextLabelByKeyword(doc, "单位名称");
		String insuranceType = insuranceService.getNextLabelByKeyword(doc, "参加险种");
		String state = insuranceService.getNextLabelByKeyword(doc, "险种状态");
		InsuranceJiAnUserInfo userInfo = new InsuranceJiAnUserInfo();
		userInfo.setUsernum(usernum);
		userInfo.setUsername(username);
		userInfo.setIdnum(idnum);
		userInfo.setGender(gender);
		userInfo.setInsureplace(insureplace);
		userInfo.setInsuranceType(insuranceType);
		userInfo.setCompanyName(companyName);
		userInfo.setState(state);
		userInfo.setTaskid(taskInsurance.getTaskid());
		return userInfo;
	}
	
	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public WebParam loginForMedical(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
	    String url = "http://www.ja12333.cn/jayb/";		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "www.ja12333.cn");
		HtmlPage page = webClient.getPage(webRequest);
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效 
		int status = page.getWebResponse().getStatusCode();
		if(200 == status){			
			HtmlImage image = page.getFirstByXPath("//img[contains(@src,'/jayb/login/getRandomPictrueAction')]");
			String code = chaoJiYingOcrService.getVerifycode(image, "1004");
			tracer.addTag("verifyCode ==>", code);
			//登陆
			String loginUrl = "http://www.ja12333.cn/jayb/login/doAction.action";
			String requestBody="loginname="+insuranceRequestParameters.getUsername()+"&password="+insuranceRequestParameters.getPassword()+"&validateCode="+code;
			WebRequest loginWebRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
			loginWebRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			loginWebRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			loginWebRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			loginWebRequest.setAdditionalHeader("Connection", "keep-alive");
			loginWebRequest.setAdditionalHeader("Host", "www.sdwfhrss.gov.cn");
			loginWebRequest.setAdditionalHeader("Host", "www.ja12333.cn");
			loginWebRequest.setAdditionalHeader("Referer", "http://www.ja12333.cn");
			loginWebRequest.setRequestBody(requestBody);
			HtmlPage loginPage = webClient.getPage(loginWebRequest);
			webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
			webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 		
			Thread.sleep(1000);
			String html=loginPage.asXml();
			webParam.setCode(loginPage.getWebResponse().getStatusCode());
			webParam.setUrl(url);
			webParam.setPage(loginPage);
			webParam.setHtml(html);	
			tracer.addTag("login","<xmp>"+html+"</xmp>");			
		}	
		return webParam;			
	}
}
