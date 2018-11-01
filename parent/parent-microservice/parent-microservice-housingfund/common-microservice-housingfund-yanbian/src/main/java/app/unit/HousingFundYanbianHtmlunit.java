package app.unit;

import java.net.URL;
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
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yanbian.HousingYanbianPaydetails;
import com.microservice.dao.entity.crawler.housing.yanbian.HousingYanbianUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundYanbianParser;

@Component
public class HousingFundYanbianHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundYanbianHtmlunit.class);
	
	@Autowired
	private HousingFundYanbianParser  housingFundYanbianParser;
	@Autowired
	private TracerLog tracer;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam= new WebParam();
		String url = "http://www.ybzfgjj.com/admin/zhxx_userlogin.do";	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);		
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("SFZHM", messageLoginForHousing.getNum()));
		paramsList.add(new NameValuePair("MIMA", messageLoginForHousing.getPassword()));
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "www.ybzfgjj.com");
		webRequest.setAdditionalHeader("Origin", "http://www.ybzfgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.ybzfgjj.com/admin/zhxx_loginout.do");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setRequestParameters(paramsList);
		HtmlPage page = webClient.getPage(webRequest);
		Thread.sleep(1500);
		tracer.addTag("action.crawler.login.page", "<xmp>"+page.asXml()+"</xmp>");
		 if(200 == page.getWebResponse().getStatusCode()){					
				String html=page.asXml();	
				System.out.println(html);
				webParam.setUrl(url);
				webParam.setHtml(html);
				if (!html.contains("登陆没有成功请核对身份证号码和密码")) {
					webParam.setWebClient(webClient);
					webParam.setHtmlPage(page);
					webParam.setLogin(true);	
				}else{
					webParam.setLogin(false);
					webParam.setHtmlPage(page);
					webParam.setHtml(html);
				}
				
		}
		return webParam;	
	}
	
	public WebParam  getPaydetails(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int count) throws Exception {		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient,taskHousing);
		String url = "http://www.ybzfgjj.com/admin/zhxx_Grjjgjjmxb.do";			
		WebRequest webRequest= new WebRequest(new URL(url), HttpMethod.POST);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("start", "0"));
		paramsList.add(new NameValuePair("limit", "300"));
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "www.ybzfgjj.com");
		webRequest.setAdditionalHeader("Origin", "http://www.ybzfgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.ybzfgjj.com/admin/zhxx_userlogin.do");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);
		List<HousingYanbianPaydetails> paydetails=new ArrayList<HousingYanbianPaydetails>();
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("action.crawler.getPaydetails", "<xmp>"+html+"</xmp>");
		webParam.setHtml(html);
		webParam.setUrl(url);
	   if (html.contains("root")) {
		    paydetails=housingFundYanbianParser.htmlPaydetailsParser(html, taskHousing);
			webParam.setPaydetails(paydetails);
			webParam.setHtml(html);
			webParam.setPage(page);
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("action.crawler.getPaydetails.count" + count, "这是第" + count + "次获取公积金缴存明细信息");
				Thread.sleep(1500);
				getPaydetails(messageLoginForHousing, taskHousing, count);
			}			
		}		
		return webParam;
	}
	public WebParam  getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int count) throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient,taskHousing);		
	    String url = "http://www.ybzfgjj.com/admin/zhxx_pangdingGrzh.do?GRZH=094810000972";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.ybzfgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.ybzfgjj.com/admin/zhxx_userlogin.do");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");		
		Page page = webClient.getPage(webRequest);		
		Thread.sleep(1000);
		String uerUrl = "http://www.ybzfgjj.com/admin/zhxx_Grzhmx.do";	
		WebRequest webRequest2 = new WebRequest(new URL(uerUrl), HttpMethod.POST);
		webRequest2.setAdditionalHeader("Accept", "*/*");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Host", "www.ybzfgjj.com");
		webRequest2.setAdditionalHeader("Referer", "http://www.ybzfgjj.com/admin/zhxx_userlogin.do");
		webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");		
		Page page2 = webClient.getPage(webRequest2);
		Thread.sleep(1500);
		String html= page2.getWebResponse().getContentAsString();
		tracer.addTag("action.crawler.getUserInfo", html);
		HousingYanbianUserInfo userInfo=new HousingYanbianUserInfo();		
		webParam.setUrl(url);
		webParam.setHtml(html);
		webParam.setCode(page.getWebResponse().getStatusCode());
		if (null != html && html.contains("data")) {
			userInfo=housingFundYanbianParser.htmlUserInfoParser(html,taskHousing);
			webParam.setUserInfo(userInfo);
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("action.crawler.getUserInfo.count" + count, "这是第" + count + "次获取用户信息");
				Thread.sleep(1500);
				getUserInfo(messageLoginForHousing, taskHousing, count);
			}
		
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
