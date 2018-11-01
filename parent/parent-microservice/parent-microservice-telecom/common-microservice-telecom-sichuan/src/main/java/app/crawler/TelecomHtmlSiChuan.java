package app.crawler;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;



@Component
public class TelecomHtmlSiChuan {

	
	public static String getConsumptionPoints(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			String string, String string2, String month, int i, String month1) {
		webClient = addcookie(webClient, taskMobile);
		
		String url = "http://sc.189.cn/service/jf/getPointItems.jsp?"
				+ "syyyyMM=" + month1
				+ "&eyyyyMM=" + month1;
		
		try {
			webClient.setJavaScriptTimeout(20000);
			webClient.getOptions().setTimeout(20000); // 15->60
			Page page = getHtml(url, webClient);
			String html = page.getWebResponse().getContentAsString();
			System.out.println(html);
			return html;
		} catch (Exception e) {
			return null;
		}
		
	}
	public static Page getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		}
	public static WebClient addcookie(WebClient webclient, TaskMobile taskMobile) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}

		return webclient;
	}
	public static String getCallThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			String stardate, String enddate, String month, int i, String month1){
		try {
		webClient = addcookie(webClient, taskMobile);
		String str = messageLogin.getSms_code();
		byte[] encodeBase64 = Base64.encodeBase64(str.getBytes("UTF-8"));
		String url = "http://sc.189.cn/service/billDetail/detailQuery.jsp?"
				+ "startTime="+ month + "-" + stardate.trim()
				+ "&endTime="+ month + "-"+ enddate.trim()
				+ "&qryType=21"
				+ "&randomCode="+new String(encodeBase64);
		
			//MTI0NTc4
			
			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = getHtml(url, webClient);

			String html = page.getWebResponse().getContentAsString();

			System.out.println(html);
			return html;
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		return null;
	}
	public static String getPayMent(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			String stardate, String enddate, String month, int i, String month1) {
		webClient = addcookie(webClient, taskMobile);
		String url = "http://sc.189.cn/service/pay/getData.jsp?"
				+ "startDate="+ month + "-" + stardate.trim()
				+ "&endDate="+ month + "-"+ enddate.trim();
		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = getHtml(url, webClient);

			String html = page.getWebResponse().getContentAsString();

			return html;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
		
		
	}
	public static Map<String,String> getPhoneBill(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			String string, String string2, String month, int i, String month1) {
		String url = "http://sc.189.cn/ajax/accbillajax.jsp?yyyyMM="+month1;
		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = getHtml(url, webClient);

			String html = page.getWebResponse().getContentAsString();

			Map<String,String> map = new HashMap<>();
			map.put("html", html);
			map.put("month", month1);
			
			return map;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
	public static String getBillDetail(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			String stardate, String enddate, String month, int i, String month1)throws Exception  {
		
		webClient = addcookie(webClient, taskMobile);
		String str = messageLogin.getSms_code();
		byte[] encodeBase64 = Base64.encodeBase64(str.getBytes("UTF-8"));
		String url = "http://sc.189.cn/service/billDetail/detailQuery.jsp?"
				+ "startTime="+ month + "-" + stardate.trim()
				+ "&endTime="+ month + "-"+ enddate.trim()
				+ "&qryType=24"
				+ "&randomCode="+new String(encodeBase64);
		
			//MTI0NTc4
			
			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = getHtml(url, webClient);

			String html = page.getWebResponse().getContentAsString();

			System.out.println(html);
			return html;
		
	}

}
