package app.parser;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.yibin.InsuranceYinbinUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.InsuranceService;

@Component
public class InsuranceYibinParser {
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
	    String url = "http://cx2.ybxww.cn:6655/login.asp";		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		String requestBody="sfzh="+insuranceRequestParameters.getUsername()+"&username="
		+URLEncoder.encode(insuranceRequestParameters.getName(), "UTF-8")+"&pass="+insuranceRequestParameters.getPassword();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "cx2.ybxww.cn:6655");
		webRequest.setAdditionalHeader("Origin", "http://cx2.ybxww.cn:6655");
		webRequest.setAdditionalHeader("Referer", "http://cx2.ybxww.cn:6655/login.asp");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setRequestBody(requestBody);
		HtmlPage page = webClient.getPage(webRequest);		
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效 
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 	
		String alertMsg = WebCrawler.getAlertMsg();
		webParam.setAlertMsg(alertMsg);
		int code = page.getWebResponse().getStatusCode();
		String html=page.asXml();
		webParam.setCode(code);
		webParam.setHtml(html);	
		webParam.setUrl(url);
		webParam.setPage(page);
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
		String url = "http://cx2.ybxww.cn:6655/hrss.asp?id=a1";
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "cx2.ybxww.cn:6655");
		webRequest.setAdditionalHeader("Referer", "http://cx2.ybxww.cn:6655/left.asp"); 
		HtmlPage page = webClient.getPage(webRequest);		
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 
		int statusCode = page.getWebResponse().getStatusCode();
	    if(200 == statusCode){
	    	String html = page.asXml();
	      	tracer.addTag("getUserInfo 个人信息","<xmp>"+html+"</xmp>");
	      	InsuranceYinbinUserInfo userInfo = htmlParserForUserInfo(html,taskInsurance);	    		    
	    	webParam.setCode(statusCode);
	    	webParam.setUserInfo(userInfo);
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
	private InsuranceYinbinUserInfo htmlParserForUserInfo(String html, TaskInsurance taskInsurance) {
		Document doc = Jsoup.parse(html);
		Element table = doc.select("table").last();
		InsuranceYinbinUserInfo userInfo = new InsuranceYinbinUserInfo();
		if (null !=table) {
			String useraccount =  table.select("td").get(1).text();
			String username = table.select("td").get(3).text();
			String sex = table.select("td").get(5).text();
			String idnum = table.select("td").get(7).text();
			String birthdate = table.select("td").get(9).text();
			String firstdate = table.select("td").get(11).text();	
			userInfo.setUseraccount(useraccount);
			userInfo.setUsername(username);
			userInfo.setSex(sex);
			userInfo.setIdnum(idnum);
			userInfo.setBirthdate(birthdate);
			userInfo.setFirstdate(firstdate);
			userInfo.setTaskid(taskInsurance.getTaskid());
		}
		
		return userInfo;
	}
}
