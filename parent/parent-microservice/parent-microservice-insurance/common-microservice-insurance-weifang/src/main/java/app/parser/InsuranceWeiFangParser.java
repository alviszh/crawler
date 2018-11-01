package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceWeiFangParser {
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
	    String url = "https://www.sdwfhrss.gov.cn/rsjwz/self/login";		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "www.sdwfhrss.gov.cn");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page = webClient.getPage(webRequest);
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效 
		int status = page.getWebResponse().getStatusCode();
		if(200 == status){			
			HtmlImage image = page.getFirstByXPath("//img[@id='login1ValidCodeImg']");
			String code = chaoJiYingOcrService.getVerifycode(image, "1004");
			tracer.addTag("verifyCode ==>", code);
			//登陆
			String loginUrl = "https://www.sdwfhrss.gov.cn/rsjwz/self/rzlogin";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("username",insuranceRequestParameters.getUsername()));
			paramsList.add(new NameValuePair("password", insuranceRequestParameters.getPassword()));
			paramsList.add(new NameValuePair("yzm", code));
			paramsList.add(new NameValuePair("button", ""));
			WebRequest loginWebRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
			loginWebRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			loginWebRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			loginWebRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			loginWebRequest.setAdditionalHeader("Connection", "keep-alive");
			loginWebRequest.setAdditionalHeader("Host", "www.sdwfhrss.gov.cn");
			loginWebRequest.setAdditionalHeader("Origin", "https://www.sdwfhrss.gov.cn");
			loginWebRequest.setAdditionalHeader("Referer", "https://www.sdwfhrss.gov.cn/rsjwz/self/login");
			loginWebRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			loginWebRequest.setRequestParameters(paramsList);
			HtmlPage loginPage = webClient.getPage(loginWebRequest);
			webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
			webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 		
			Thread.sleep(1500);
			String html=loginPage.asXml();
			webParam.setCode(loginPage.getWebResponse().getStatusCode());
			webParam.setUrl(url);
			webParam.setPage(loginPage);
			webParam.setHtml(html);	
			tracer.addTag("login","<xmp>"+html+"</xmp>");		
			return webParam;			
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
		String url = "https://www.sdwfhrss.gov.cn/rsjwz/self/cbgk";
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.sdwfhrss.gov.cn");
		webRequest.setAdditionalHeader("Referer", "https://www.sdwfhrss.gov.cn/rsjwz/self/search"); 
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 
		HtmlPage page = webClient.getPage(webRequest);		
		int statusCode = page.getWebResponse().getStatusCode();
	    if(200 == statusCode){
	    	String html = page.asXml();
	      	tracer.addTag("getUserInfo 个人信息","<xmp>"+html+"</xmp>");
	      	InsuranceWeiFangUserInfo userInfo = htmlParserForUserInfo(html,taskInsurance);	    		    
	    	webParam.setCode(statusCode);
	    	webParam.setInsuranceWeiFangUserInfo(userInfo);    
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
	private InsuranceWeiFangUserInfo htmlParserForUserInfo(String html, TaskInsurance taskInsurance) {
		Document doc = Jsoup.parse(html);
		String username =  doc.select("table").select("td").get(1).text();
		String personalType = insuranceService.getNextLabelByKeyword(doc, "人员类别");
		String institution = insuranceService.getNextLabelByKeyword(doc, "经办机构");
		String companyName = insuranceService.getNextLabelByKeyword(doc, "单位名称");
		InsuranceWeiFangUserInfo userInfo = new InsuranceWeiFangUserInfo();
		userInfo.setUsername(username);
		userInfo.setPersonalType(personalType);
		userInfo.setInstitution(institution);
		userInfo.setCompanyName(companyName);
		return userInfo;
	}
}
