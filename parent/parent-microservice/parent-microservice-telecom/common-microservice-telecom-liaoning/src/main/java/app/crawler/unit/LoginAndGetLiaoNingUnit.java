package app.crawler.unit;

import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LoginAndGetLiaoNingUnit {

	public static final Logger log = LoggerFactory.getLogger(LoginAndGetLiaoNingUnit.class);
	
	

	public static WebClient readyForCallThrem(WebClient webClient, int i) {
		String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01630716";
		
		Page page = getHtml(url, webClient);
		
		url = "http://ln.189.cn/group/bill/bill_billlist.do?fastcode=01650723&amp;cityCode=ln";
		page = getHtml(url, webClient);
		
		return null;//page.getWebClient();
	}
	public static Page getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}}
	// 获取用户通话详单 带分页信息
	public static String getCallThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			String stardate, String enddate, String month, int i,String month1) {

		webClient = addcookie(webClient, taskMobile);
		System.out.println(stardate + ":" + enddate + ":" + month );
		String url = "http://ln.189.cn/queryVoiceMsgAction.action?"
				+ "inventoryVo.accNbr="+messageLogin.getName()
				+ "&inventoryVo.begDate="+month+"-"+stardate.trim()
				+ "&inventoryVo.endDate="+month+"-"+enddate.trim()
				+ "&inventoryVo.family=8"
				+ "&inventoryVo.getFlag=3"
				+ "&inventoryVo.feeDate="+month1
				;
		
		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = getHtml(url, webClient);

			String html = page.getWebResponse().getContentAsString();
			System.out.println(month1+"通话记录");
			return html;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
	public static String getpayThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			LocalDate stardate, LocalDate enddate, String month, int i,String month1) {

		webClient = addcookie(webClient, taskMobile);
		System.out.println(stardate + ":" + enddate + ":" + month );
		String url = "http://ln.189.cn/queryRecharge.action?"
				+ "queryType=8"
				+ "&queryNbr="+messageLogin.getName()
				+ "&beginDate="+stardate
				+ "&endDate="+enddate;
		
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
	public static HashMap getphoneBillThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			LocalDate stardate, LocalDate enddate, String month, int i,String month1) {

		webClient = addcookie(webClient, taskMobile);
		System.out.println(stardate + ":" + enddate + ":" + month );
		String url = "http://ln.189.cn/chargeQuery/chargeQuery_queryCustBill.action?"
				+ "billingCycleId="+month1
				+"&accNbr="+messageLogin.getName()
				+"&queryFlag=1&productId=8";
		
		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = getHtml(url, webClient);

			String html = page.getWebResponse().getContentAsString();

			HashMap<Object,Object> hashMap = new HashMap<>();
			hashMap.put("html", html);
			hashMap.put("month", month1);
			return hashMap;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
	
	//
		public static String getSMSThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
				String stardate, String enddate, String month, int i,String month1) {

			webClient = addcookie(webClient, taskMobile);
			System.out.println(stardate + ":" + enddate + ":" + month );
			String url = "http://ln.189.cn/mobileInventoryAction.action?"
					+ "inventoryVo.accNbr="+messageLogin.getName()
					+ "&inventoryVo.begDate="+month+"-"+stardate.trim()
					+ "&inventoryVo.endDate="+month+"-"+enddate.trim()
					+ "&inventoryVo.family=8"
					+ "&inventoryVo.getFlag=3"
					+ "&inventoryVo.feeDate="+month1;
					
			
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
}
