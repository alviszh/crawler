package app.htmlunit;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chenzhou.HousingChenZhouPaydetails;
import com.microservice.dao.entity.crawler.housing.chenzhou.HousingChenZhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundChenZhouParser;
import net.sf.json.JSONObject;

@Component
public class HousingFundChenZhouHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundChenZhouHtmlunit.class);
	@Autowired
	private HousingFundChenZhouParser housingFundChenZhouParser;
	@Autowired
	private TracerLog tracer;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		String url = "http://cx.czzfgjj.cn/pfsystem/login";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);	
		
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("username", messageLoginForHousing.getNum()));
		paramsList.add(new NameValuePair("mobile", messageLoginForHousing.getTelephone()));
		paramsList.add(new NameValuePair("password", messageLoginForHousing.getPassword()));
		
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "cx.czzfgjj.cn");
		webRequest.setAdditionalHeader("Origin", "http://cx.czzfgjj.cn");
		webRequest.setAdditionalHeader("Referer", "http://cx.czzfgjj.cn/pfsystem/login");
		webRequest.setRequestParameters(paramsList);		
		Page page = webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		webParam.setHtml(html);
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setUrl(url);
		webParam.setWebClient(webClient);
	    tracer.addTag("parser.crawler.login.page", "<xmp>"+html+"</xmp>");
	    JSONObject list1ArrayObjs = JSONObject.fromObject(html);
		String codeStr = list1ArrayObjs.getString("code");
		String msg=list1ArrayObjs.getString("msg");
		webParam.setText(msg);
		if ("200".equals(codeStr)) {
			webParam.setLogin(true);		
		}else{
			webParam.setLogin(false);
		}
		return webParam;	
	}
	public WebParam  getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient= addcookie(webClient, taskHousing);
	    String url = "http://cx.czzfgjj.cn/pfsystem/pf/jiaocun/my";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "cx.czzfgjj.cn");
		webRequest.setAdditionalHeader("Origin", "http://cx.czzfgjj.cn/pfsystem/admin");
		Page page = webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getUserInfo", "<xmp>"+html+"</xmp>");
		HousingChenZhouUserInfo userInfo=new HousingChenZhouUserInfo();
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setPage(page);
		webParam.setHtml(html);
		if (html.contains("个人缴存信息")) {
			userInfo=housingFundChenZhouParser.htmlUserInfoParser(html, taskHousing);
			webParam.setUserInfo(userInfo);		
		}
		return webParam;
	}
	
	public WebParam  getPaydetails(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient= addcookie(webClient, taskHousing);
	    String url = "http://cx.czzfgjj.cn/pfsystem/pf/jiaocun/detail/query";	
	    //获取十年前的当前日期
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar c = Calendar.getInstance();  
		String tbxEnd=format.format(c.getTime());
		c.add(Calendar.YEAR, -10); //年份减3  
		String startDate=format.format(c.getTime());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("spcode", ""));
		paramsList.add(new NameValuePair("startdate", startDate));
		paramsList.add(new NameValuePair("page", "1"));
		paramsList.add(new NameValuePair("rows", "300"));
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
	
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "cx.czzfgjj.cn");
		webRequest.setAdditionalHeader("Origin", "http://cx.czzfgjj.cn");
		webRequest.setAdditionalHeader("Referer", "http://cx.czzfgjj.cn/pfsystem/pf/jiaocun/detail");
		webRequest.setRequestParameters(paramsList);
	    Page page = webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getPaydetails", "<xmp>"+html+"</xmp>");
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setHtml(html);
		webParam.setPage(page);
		List<HousingChenZhouPaydetails> paydetails=new ArrayList<HousingChenZhouPaydetails>();
		   if (html.contains("rows")) {
			    paydetails=housingFundChenZhouParser.htmlPaydetailsParser(html, taskHousing);
				webParam.setPaydetails(paydetails);			
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
