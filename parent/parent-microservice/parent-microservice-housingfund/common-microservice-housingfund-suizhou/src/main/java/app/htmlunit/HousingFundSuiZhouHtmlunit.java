package app.htmlunit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import com.microservice.dao.entity.crawler.housing.suizhou.HousingSuiZhouPaydetails;
import com.microservice.dao.entity.crawler.housing.suizhou.HousingSuiZhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundSuiZhouParser;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONObject;

@Component
public class HousingFundSuiZhouHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundSuiZhouHtmlunit.class);
	@Autowired
	private HousingFundSuiZhouParser housingFundSuiZhouParser;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int count) throws Exception {
		WebParam webParam= new WebParam();
		String loginUrl= "http://www.suizhougjj.cn/szgjjwt/login.jsp";
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);	
    	webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.suizhougjj.cn");		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(webRequest);
		webClient.getOptions().setJavaScriptEnabled(true);  
		webClient.waitForBackgroundJavaScript(20000);
	    tracer.addTag("parser.crawler.login.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		if(200 == page.getWebResponse().getStatusCode()){					
			if (page.getWebResponse().getContentAsString().contains("i_codegr")) {
				HtmlImage image = page.getFirstByXPath("//img[@id='i_codegr']");
				String code = chaoJiYingOcrService.getVerifycode(image, "1004");
				tracer.addTag("verifyCode ==>", code);
				//登陆
				String url = "http://www.suizhougjj.cn/szgjjwt/action/login";
				WebRequest webRequest2 = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody = "cardno=" + messageLoginForHousing.getNum() + "&WZMM="
						+ messageLoginForHousing.getPassword() + "&code=" + code + "&yhlbdm=01";
				webRequest2.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
				webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				webRequest2.setAdditionalHeader("Connection", "keep-alive");
				webRequest2.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				webRequest2.setAdditionalHeader("Host", "www.suizhougjj.cn");
				webRequest2.setAdditionalHeader("Origin", "http://www.suizhougjj.cn");
				webRequest2.setAdditionalHeader("Referer", "http://www.suizhougjj.cn/szgjjwt/login.jsp");
				webRequest2.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");
				webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				webRequest2.setRequestBody(requestBody);
				Page loginPage = webClient.getPage(webRequest2);
				Thread.sleep(1000);
				String html=loginPage.getWebResponse().getContentAsString();
				tracer.addTag("parser.crawler.login.loginPage", "<xmp>"+html+"</xmp>");	
				webParam.setHtml(html);
				webParam.setWebClient(webClient);
				webParam.setUrl(url);
				JSONObject object = JSONObject.fromObject(html);
				String loginStr= object.getString("success");   
				List<String> datalist = new ArrayList<>();
				if (loginStr.equals("true")) {
					webParam.setLogin(true);
					HtmlPage page2 = getHtml("http://www.suizhougjj.cn/szgjjwt/page/gr/grcx_jbxx.jsp", webClient);
					String html2 = page2.asXml();
					tracer.addTag("parser.crawler.login.page2", "<xmp>" + html2 + "</xmp>");
					if (html2.contains("kjny")) {
						Document doc = Jsoup.parse(html2);
						Element optionDiv = doc.getElementById("kjny");
						Elements option = optionDiv.getElementsByTag("option");
						for (Element element : option) {
							String text = element.text();
							String dateText = housingFundSuiZhouParser.dateListParser(text);
							datalist.add(dateText);
						}
					}
					webParam.setDatalist(datalist);
				}else{
					String loginMsg=object.getString("msg");
					webParam.setLogin(false);
					webParam.setLoginMsg(loginMsg);
					if (count < 3) {
							count++;
							tracer.addTag("parser.crawler.login.count" + count, "这是第" + count + "次登陆");
							Thread.sleep(1500);
							login(messageLoginForHousing, taskHousing, count);
						}	
					}
			
			}
		}
		return webParam;	
	}
	
	
	public WebParam  getPaydetails(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,String dataText) throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient,taskHousing);		
	    String url = "http://www.suizhougjj.cn/szgjjwt/action/grcx_jb";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		String requestBody="kjny="+dataText+"&pageNumber=1&pagesize=50";
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "www.suizhougjj.cn");
		webRequest.setAdditionalHeader("Origin", "http://www.suizhougjj.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.suizhougjj.cn/szgjjwt/page/gr/grcx_jbxx.jsp");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestBody(requestBody);
	    Page page = webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		System.out.println(html);
		tracer.addTag("parser.crawler.getPaydetails", "<xmp>"+html+"</xmp>");
		webParam.setUrl(url);
		webParam.setHtml(html);
		List<HousingSuiZhouPaydetails> paydetails=new ArrayList<HousingSuiZhouPaydetails>();
		HousingSuiZhouUserInfo userInfo=new HousingSuiZhouUserInfo();		
	    if (html.contains("datalist")) {
		    paydetails=housingFundSuiZhouParser.htmlPaydetailsParser(html, taskHousing);
			webParam.setPaydetails(paydetails);		
			userInfo=housingFundSuiZhouParser.htmlUserInfoParser(html, taskHousing);
			webParam.setUserInfo(userInfo);
			webParam.setPage(page);
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
