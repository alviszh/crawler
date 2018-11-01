package app.htmlunit;

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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yangzhou.HousingYangzhouHtml;
import com.microservice.dao.entity.crawler.housing.yangzhou.HousingYangzhouPaydetails;
import com.microservice.dao.entity.crawler.housing.yangzhou.HousingYangzhouUserInfo;
import com.microservice.dao.repository.crawler.housing.yangzhou.HousingYangzhouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.yangzhou.HousingYangzhouPaydetailsRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundYangzuouParser;

@Component
public class HousingFundYangzhouHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundYangzhouHtmlunit.class);
	@Autowired
	private HousingFundYangzuouParser housingFundYangzuouParser;
	@Autowired
	private HousingYangzhouPaydetailsRepository housingYangzhouPaydetailsRepository;
	@Autowired
	private HousingYangzhouHtmlRepository housingYangzhouHtmlRepository;
	@Autowired
	private TracerLog tracer;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam= new WebParam();
		String url = "http://58.220.193.178:8880/yw/login2.asp?username="+messageLoginForHousing.getNum()+"&password="+messageLoginForHousing.getPassword();	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "58.220.193.178:8880");
		webRequest.setAdditionalHeader("Referer", "http://58.220.193.178:8880/yw/login2.asp");
		HtmlPage loginPage = webClient.getPage(webRequest);
		Thread.sleep(1500);
		tracer.addTag("action.crawler.login.page", "<xmp>"+loginPage.asXml()+"</xmp>");
		 if(200 == loginPage.getWebResponse().getStatusCode()){					
				String url2="http://58.220.193.178:8880/yw/main_p.asp";
				HtmlPage page=getHtml(url2,webClient);
				String html=page.asXml();
				String alertMsg=WebCrawler.getAlertMsg();
				if (html.contains("扬州市住房公积金管理中心网上业务管理系统")) {
					webParam.setWebClient(webClient);
					webParam.setHtml(html);
					webParam.setAlertMsg(alertMsg);
					webParam.setHtmlPage(page);
					webParam.setLogin(true);	
				}
		}
		return webParam;	
	}
	
	public WebParam  getPaydetails(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,String year) throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient,taskHousing);
		String url = "http://58.220.193.178:8880/yw/detail_p.asp?year="+year;			
		WebRequest webRequest= new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");		
		webRequest.setAdditionalHeader("Host", "58.220.193.178:8880");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		HtmlPage page = webClient.getPage(webRequest);
		List<HousingYangzhouPaydetails> paydetails=new ArrayList<HousingYangzhouPaydetails>();
		String html=page.asXml();
		tracer.addTag("action.crawler.getPaydetails", "<xmp>"+html+"</xmp>");
		webParam.setHtml(html);
		webParam.setUrl(url);
	   if (html.contains("缴存明细账")) {
		    paydetails=housingFundYangzuouParser.htmlPaydetailsParser(html, taskHousing);
			webParam.setPaydetails(paydetails);
			webParam.setHtml(html);
			webParam.setPage(page);			
		}	
		return webParam;
	}
	
	public void  getPaydetailsforPageTwo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,String year) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient,taskHousing);
		String url = "http://58.220.193.178:8880/yw/detail_p.asp?year="+year+"&PageNo=2";			
		WebRequest webRequest= new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "58.220.193.178:8880");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		HtmlPage page = webClient.getPage(webRequest);
		List<HousingYangzhouPaydetails> paydetails=new ArrayList<HousingYangzhouPaydetails>();
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("HousingFundYangzhouHtmlunit.getPaydetailsforPageTwo---缴费信息第二页请求" + taskHousing.getTaskid(),
				"<xmp>" + html + "</xmp>");
	   if (html.contains("缴存明细账")) {
		    paydetails=housingFundYangzuouParser.htmlPaydetailsParser(html, taskHousing);
		    housingYangzhouPaydetailsRepository.saveAll(paydetails);
			tracer.addTag("getPaydetailsforPageTwo.data---缴费信息第二页请求返回数据" + taskHousing.getTaskid(),paydetails.toString());
			HousingYangzhouHtml housingYangzhouHtml = new HousingYangzhouHtml(taskHousing.getTaskid(),
					"paydetails",2,url, html);
			housingYangzhouHtmlRepository.save(housingYangzhouHtml);
		}		
	}
	public WebParam  getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient,taskHousing);	
		WebParam webParam= new WebParam();
	    String url = "http://58.220.193.178:8880/yw/main_p.asp";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "58.220.193.178:8880");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");		
		HtmlPage page = webClient.getPage(webRequest);		
		String html=page.asXml();
		tracer.addTag("action.crawler.getUserInfo", page.asXml());
		webParam.setUrl(url);
		webParam.setHtml(html);
		webParam.setCode(page.getWebResponse().getStatusCode());
		if (null != html && html.contains("个人基本信息")) {
			HousingYangzhouUserInfo	userInfo=housingFundYangzuouParser.htmlUserInfoParser(html,taskHousing);
			webParam.setUserInfo(userInfo);
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
