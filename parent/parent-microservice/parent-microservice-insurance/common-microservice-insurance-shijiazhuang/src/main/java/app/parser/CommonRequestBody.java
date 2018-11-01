package app.parser;

import java.net.URL;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

import app.service.InsuranceService;

@Component
/**
 * 
 * @Description: 提取公共请求信息，用此请求方式来避免加载js错误程序无法执行爬取数据的问题
 * @author sln
 * @date 2017年8月5日
 */
public class CommonRequestBody {
	public static final Logger log = LoggerFactory.getLogger(CommonRequestBody.class);
	@Autowired
	private InsuranceService insuranceService;	
	//此方法供医疗和失业险调用
	public HtmlPage getMedicalOrLostWorkPage(String url,String requestpayload,String userName,Set<Cookie> cookies) throws Exception{
		String firstPart=userName.substring(0, 6);
		String secondPart=userName.substring(8,17);
		String userNameCut=firstPart+secondPart;
		String requestBody0=requestpayload.replaceAll("userNameCut", userNameCut);
		String requestBody=requestBody0.replaceAll("InsurNumLocal", userName);		
		WebClient webClient = insuranceService.getWebClient(cookies);	
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);		
		requestSettings.setAdditionalHeader("Host", "grsbcx.sjz12333.gov.cn");
	    requestSettings.setAdditionalHeader("Accept", "*/*");
	    requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
	    requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
	    requestSettings.setAdditionalHeader("Connection", "keep-alive");
	    requestSettings.setAdditionalHeader("Referer", "http://grsbcx.sjz12333.gov.cn/si/pages/default.jsp"); 
	    requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0"); 
	    requestSettings.setAdditionalHeader("ajaxRequest", "true"); 
	    requestSettings.setAdditionalHeader("Content-Type", "multipart/form-data"); 	    
	    requestSettings.setRequestBody(requestBody);
	    HtmlPage page = webClient.getPage(requestSettings);
	    return page;
	}
	//此方法供用户基本信息和社保公共信息调用
	public HtmlPage getUserOrGeneralPage(String url,String requestpayload,String userName,Set<Cookie> cookies) throws Exception{
		String requestBody=requestpayload.replaceAll("InsurNumLocal", userName);
		WebClient webClient = insuranceService.getWebClient(cookies);	
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);		
		requestSettings.setAdditionalHeader("Host", "grsbcx.sjz12333.gov.cn");
	    requestSettings.setAdditionalHeader("Accept", "*/*");
	    requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
	    requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
	    requestSettings.setAdditionalHeader("Connection", "keep-alive");
	    requestSettings.setAdditionalHeader("Referer", "http://grsbcx.sjz12333.gov.cn/si/pages/default.jsp"); 
	    requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0"); 
	    requestSettings.setAdditionalHeader("ajaxRequest", "true"); 
	    requestSettings.setAdditionalHeader("Content-Type", "multipart/form-data"); 	    
	    requestSettings.setRequestBody(requestBody);
	    HtmlPage page = webClient.getPage(requestSettings);
		return page;
	}
}
