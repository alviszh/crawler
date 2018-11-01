package org.common.microservice.eureka.china.unicomt.test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

public class ListTest {

	static String cookiefile = "C:\\Users\\Administrator\\Desktop\\cookie.xlsx";
	
	
	public static void main(String[] args) throws  Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		//URL url = new URL("http://bj.189.cn/iframe/feequery/billDetailQuery.action"); 
		//WebRequest requestSettings = new WebRequest(url, HttpMethod.POST);
		
		URL url = new URL("http://bj.189.cn/iframe/feequery/billDetailQuery.parser?requestFlag=synchronization&billDetailType=1&qryMonth=2017%E5%B9%B406%E6%9C%88&startTime=1&accNum=13366777357&endTime=31");
		WebRequest requestSettings = new WebRequest(url, HttpMethod.GET);
		
		/*

		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("requestFlag", "synchronization"));
		requestSettings.getRequestParameters().add(new NameValuePair("billDetailType", "1"));
		requestSettings.getRequestParameters().add(new NameValuePair("qryMonth", "2017年06月"));
		requestSettings.getRequestParameters().add(new NameValuePair("startTime", "1"));
		requestSettings.getRequestParameters().add(new NameValuePair("accNum", "13366777357"));
		requestSettings.getRequestParameters().add(new NameValuePair("endTime", "30"));
*/
	//	POIUnit.addCookie(webClient,cookiefile,"bj.189.cn");
		 
		requestSettings.setAdditionalHeader("Accept", "text/html, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,fr;q=0.6");
		requestSettings.setAdditionalHeader("Cache-Control", "no-cache");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		//requestSettings.setAdditionalHeader("Content-Length", "120");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Host", "bj.189.cn");
		requestSettings.setAdditionalHeader("Origin", "http://bj.189.cn");
		requestSettings.setAdditionalHeader("Pragma", "no-cache");  
		requestSettings.setAdditionalHeader("Referer", "http://bj.189.cn/iframe/feequery/detailBillIndex.parser?fastcode=01390638&cityCode=bj");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		

		Page page = webClient.getPage(requestSettings);

		System.out.println(page.getWebResponse().getContentAsString());

	}
	
	/*public static WebClient addcookie(WebClient webclient, TaskMobile taskMobile) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}

		return webclient;
	}*/

}
