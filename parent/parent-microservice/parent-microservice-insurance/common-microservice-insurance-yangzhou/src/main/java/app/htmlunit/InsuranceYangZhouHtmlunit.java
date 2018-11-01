package app.htmlunit;

import java.net.URL;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.yangzhou.InsuranceYangZhouMedical;
import com.microservice.dao.entity.crawler.insurance.yangzhou.InsuranceYangZhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceYangZhouParser;
import app.service.InsuranceService;

@Component
public class InsuranceYangZhouHtmlunit {
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceYangZhouParser insuranceYangZhouParser;
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
		// 登陆		
		WebParam  webParam=new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.jsyz.si.gov.cn/shebao/front/web";
		String requestBody="method=anotherlogin&userName="+insuranceRequestParameters.getUsername()+"&psd="+insuranceRequestParameters.getPassword()+"&B3=";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "www.jsyz.si.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://www.jsyz.si.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.jsyz.si.gov.cn/shebao/index.jsp");
		webRequest.setRequestBody(requestBody);
	    HtmlPage page = webClient.getPage(webRequest);
		Thread.sleep(1000);
		String alertMsg=WebCrawler.getAlertMsg();
		String html = page.getWebResponse().getContentAsString();	
		tracer.addTag("login", "<xmp>" + html + "</xmp>");
		webParam.setHtml(html);
		webParam.setUrl(url);
		webParam.setPage(page);
		webParam.setAlertMsg(alertMsg);	
		return webParam;
	}
	
	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @param username
	 * @return
	 * @throws Exception 
	 */
	public WebParam getUserInfo(TaskInsurance taskInsurance, Set<Cookie> cookies,String username) throws Exception {	
		WebParam webParam= new WebParam();
		String url = "http://www.jsyz.si.gov.cn/shebao/front/web?method=identity&userCode="+username;
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.jsyz.si.gov.cn");
		HtmlPage page = webClient.getPage(webRequest);		
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 		
		int statusCode = page.getWebResponse().getStatusCode();
		String html = page.asXml();
		tracer.addTag("getUserInfo 个人信息","<xmp>"+html+"</xmp>");
	  	webParam.setCode(statusCode);
	  	webParam.setUrl(url);
    	webParam.setHtml(html);
	    if(html !=null && html.contains("人员身份查询")){	      
	      	InsuranceYangZhouUserInfo userInfo = insuranceYangZhouParser.htmlParserForUserInfo(html, taskInsurance);	  
	    	webParam.setUserInfo(userInfo);
	    }
		return webParam;
	}

	/**
	 * @Des 获取医疗信息
	 * @param taskInsurance
	 * @param cookies
	 * @param username
	 * @return
	 * @throws Exception 
	 */
	public WebParam getMedicalList(TaskInsurance taskInsurance,Set<Cookie> cookies,String username) throws Exception {	
		WebParam webParam= new WebParam();
		String url = "http://www.jsyz.si.gov.cn/shebao/front/web?method=useraccount&userCode="+username;
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");		
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.jsyz.si.gov.cn");
		HtmlPage page = webClient.getPage(webRequest);		
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 		
		Thread.sleep(1500);
		int statusCode = page.getWebResponse().getStatusCode();
		String html = page.getWebResponse().getContentAsString();
		webParam.setCode(statusCode);
    	webParam.setUrl(url);
    	webParam.setHtml(html);
      	tracer.addTag("getMedicalList",html);
    	if (html !=null && html.contains("实时账户余额")) {
    		List<InsuranceYangZhouMedical> medicalList = insuranceYangZhouParser.htmlParserForMedicalList(html, taskInsurance);   
	    	webParam.setMedicalList(medicalList);
		}
		return webParam;
	}
	
}
