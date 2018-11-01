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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.suqian.HousingSuQianPaydetails;
import com.microservice.dao.entity.crawler.housing.suqian.HousingSuQianUserInfo;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundSuQianParser;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundSuQianHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundSuQianHtmlunit.class);
	@Autowired
	private HousingFundSuQianParser housingFundSuQianParser;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	public WebParam login(WebClient webClient,MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int count) throws Exception {
		WebParam webParam= new WebParam();
		String url = "http://218.93.210.170:7001/login?type=1";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
    	webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "218.93.210.170:7001");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		HtmlPage page = webClient.getPage(webRequest);
		webClient.getOptions().setJavaScriptEnabled(true);  
		webClient.waitForBackgroundJavaScript(20000);
	    tracer.addTag("parser.crawler.login.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		if(200 == page.getWebResponse().getStatusCode()){					
			if (page.getWebResponse().getContentAsString().contains("captcha_img1")) {
				HtmlImage image = page.getFirstByXPath("//img[@id='captcha_img1']");
				String code = chaoJiYingOcrService.getVerifycode(image, "1004");
				tracer.addTag("verifyCode ==>", code);
				//登陆
				String loginUrl = "http://218.93.210.170:7001/login";		                     
				WebRequest webRequest2 = new WebRequest(new URL(loginUrl), HttpMethod.POST);	
				String requestBody="username="+messageLoginForHousing.getNum()+"&password="+messageLoginForHousing.getPassword()+"&captcha="+code+"&usertype=2";
				webRequest2.setAdditionalHeader("Accept", "*/*");
				webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				webRequest2.setAdditionalHeader("Connection", "keep-alive");
				webRequest2.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
				webRequest2.setAdditionalHeader("Host", "222.175.23.30");
				webRequest2.setAdditionalHeader("Origin", "http://222.175.23.30");
				webRequest2.setAdditionalHeader("Referer", "http://222.175.23.30/hfmis_wt/login");
				webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
				webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				webRequest2.setRequestBody(requestBody);
				Page loginPage = webClient.getPage(webRequest2);
				Thread.sleep(1500);
				String html=loginPage.getWebResponse().getContentAsString();
				tracer.addTag("parser.crawler.login.loginPage", "<xmp>"+html+"</xmp>");	
				webParam.setHtml(html);
				webParam.setWebClient(webClient);
				webParam.setUrl(url);
				String jbxxUrl="http://218.93.210.170:7001/personal/jbxx";	        
				if (html.contains("登入成功")) {
					webParam.setLogin(true);
					webParam.setWebClient(webClient);
					webParam.setHtml(html);
				    HtmlPage jbxxPage= getHtml(jbxxUrl,webClient);
				    String jbxxHtml=jbxxPage.asXml();
				    String grzh=housingFundSuQianParser.htmlForGzhParser(jbxxHtml);
				    webParam.setGrzh(grzh);
				}else{
					if (count < 3) {
						count++;
						tracer.addTag("parser.crawler.login.count" + count, "这是第" + count + "次登陆");
						Thread.sleep(2500);
						login(webClient,messageLoginForHousing, taskHousing, count);
					}					
				}
			}
		}
		return webParam;	
	}
	
	public WebParam  getUserInfo(WebClient webClient,MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,String grzh,int count) throws Exception {
		WebParam webParam= new WebParam();
		webClient = addcookie(webClient,taskHousing);		
	    String url = "http://218.93.210.170:7001/common/invoke/21B007";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		String requestBody="grzh="+grzh;	
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "218.93.210.170:7001");
		webRequest.setAdditionalHeader("Origin", "http://218.93.210.170:7001");
		webRequest.setAdditionalHeader("Referer", "http://218.93.210.170:7001/personal/jbxx");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestBody(requestBody);
	    Page page = webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getUserInfo", "<xmp>"+html+"</xmp>");
		HousingSuQianUserInfo userInfo=new HousingSuQianUserInfo();
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		if (html.contains("dataset")) {
			userInfo=housingFundSuQianParser.htmlUserInfoParser(html, taskHousing);
			webParam.setUserInfo(userInfo);
			webParam.setPage(page);
			webParam.setHtml(html);
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getPaydetails.count" + count, "这是第" + count + "次获取公积金缴存明细信息");
				Thread.sleep(1500);
				getUserInfo(webClient,messageLoginForHousing, taskHousing, grzh,count);
			}	
		}
		return webParam;
	}
	
	public WebParam  getPaydetails(WebClient webClient,MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,String grzh,int count) throws Exception {
		WebParam webParam= new WebParam();
		webClient = addcookie(webClient,taskHousing);		
	    String url = "http://218.93.210.170:7001/common/invoke/21C002";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		String requestBody="filterscount=0&groupscount=0&pagenum=0&pagesize=100&recordstartindex=0&recordendindex=20&grzh="+grzh;	
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "218.93.210.170:7001");
		webRequest.setAdditionalHeader("Origin", "http://218.93.210.170:7001");
		webRequest.setAdditionalHeader("Referer", "http://218.93.210.170:7001/personal/jcmx");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestBody(requestBody);
	    Page page = webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		System.out.println(html);
		tracer.addTag("parser.crawler.getPaydetails", "<xmp>"+html+"</xmp>");
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		List<HousingSuQianPaydetails> paydetails=new ArrayList<HousingSuQianPaydetails>();
		   if (html.contains("dataset")) {
			    paydetails=housingFundSuQianParser.htmlPaydetailsParser(html, taskHousing);
				webParam.setPaydetails(paydetails);
				webParam.setHtml(html);
				webParam.setPage(page);
			}else{
				if (count < 3) {
					count++;
					tracer.addTag("parser.crawler.getPaydetails.count" + count, "这是第" + count + "次获取公积金缴存明细信息");
					Thread.sleep(1500);
					getPaydetails(webClient,messageLoginForHousing, taskHousing, grzh,count);
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

	public HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
