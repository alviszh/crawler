package app.unit;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.xinxiang.HousingXinxiangPaydetails;
import com.microservice.dao.entity.crawler.housing.xinxiang.HousingXinxiangUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundXinxiangParser;
import net.sf.json.JSONObject;

@Component
public class HousingFundXinxiangHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundXinxiangHtmlunit.class);	
	@Autowired
	private HousingFundXinxiangParser  housingFundXinxiangParser;
	@Autowired
	private TracerLog tracer;
	public WebParam login(MessageLoginForHousing messageLoginForHousing,String loginType) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		WebParam webParam= new WebParam();	
		String url = "http://www.xxzfgjj.com/GJJCX/DoL2o2g2i2n2";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		String requestBody="LoginType="+loginType+"+&UserAccount="+messageLoginForHousing.getNum()+"&UserPassword="+messageLoginForHousing.getPassword()+"&UserName="+URLEncoder.encode(messageLoginForHousing.getUsername(), "UTF-8")+"&CXType=1&X-Requested-With=XMLHttpRequest";
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.xxzfgjj.com");
		webRequest.setAdditionalHeader("Origin", "http://www.xxzfgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.xxzfgjj.com/gjjcx/");
		webRequest.setRequestBody(requestBody);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false); 
		Page page = webClient.getPage(webRequest);		
		tracer.addTag("parser.crawler.login.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		 if(200 == page.getWebResponse().getStatusCode()){		
			 String html=page.getWebResponse().getContentAsString();
			if (html.contains("Result_Type")) {
				JSONObject list1ArrayObjs = JSONObject.fromObject(html);
				String resultType=list1ArrayObjs.getString("Result_Type");
				if ("2".equals(resultType)) {
					webParam.setWebClient(webClient);
					webParam.setUrl(url);
					webParam.setLogin(true);
				}
			}
		}
		return webParam;	
	}
	
	public WebParam  getPaydetails(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient=addcookie(webClient,  taskHousing);
		String url = "http://www.xxzfgjj.com/gjjcx/deposit/243418.html";	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.xxzfgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.xxzfgjj.com/gjjcx/deposit/241238.html");		
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false); 
		HtmlPage page = webClient.getPage(webRequest);
		Thread.sleep(1500);
		String html=page.asXml();
		System.out.println(html);
		tracer.addTag("parser.crawler.getPaydetails", html);
		webParam.setHtml(html);
		webParam.setPage(page);	
		webParam.setUrl(url);
		List<HousingXinxiangPaydetails> paydetails=new ArrayList<HousingXinxiangPaydetails>();
	   if (html!=null) {
		    paydetails=housingFundXinxiangParser.htmlPaydetailsParser(html, taskHousing);
			webParam.setPaydetails(paydetails);
			webParam.setHtml(html);
			webParam.setPage(page);
		}	
		return webParam;
	}
	public WebParam  getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient=addcookie(webClient,  taskHousing);
	    String url = "http://www.xxzfgjj.com/gjjcx/deposit";		
	    WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.xxzfgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.xxzfgjj.com/gjjcx/");		
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false); 
		Page page = webClient.getPage(webRequest);
		Thread.sleep(1500);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getUserInfo", html);
		HousingXinxiangUserInfo userInfo=new HousingXinxiangUserInfo();
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		if (html.contains("新乡市住房公积金个人信息查询")) {
			userInfo=housingFundXinxiangParser.htmlUserInfoParser(html,taskHousing);
			webParam.setUserInfo(userInfo);
			webParam.setPage(page);
			webParam.setHtml(html);
		}	
		return webParam;
	}
	
	public  WebClient addcookie(WebClient webclient, TaskHousing taskHousing) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		 for(Cookie cookie : cookies){
			 webclient.getCookieManager().addCookie(cookie);
		  }
		return webclient;
	}
}
