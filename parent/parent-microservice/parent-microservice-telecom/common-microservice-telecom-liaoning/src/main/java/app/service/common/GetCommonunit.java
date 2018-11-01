package app.service.common;

import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.crawler.unit.LoginAndGetLiaoNingUnit;

@Component
public class GetCommonunit {

	public final Logger log = LoggerFactory.getLogger(GetCommonunit.class);
	
	@Autowired
	private TracerLog tracer;
	
	public Page gethtmlPostByWebRequest(WebClient webClient,WebRequest webRequest, String url){
		
		try {
			webClient.getOptions().setJavaScriptEnabled(false);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomhlj", e.getMessage());
			return null;
		}
	}
	public HtmlPage getHtml(String url,WebClient webClient){
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url),HttpMethod.GET);
			HtmlPage searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomhlj", e.getMessage());
			return null;
		}
	}
	public WebClient addcookie(WebClient webClient, TaskMobile taskMobile) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}
	public static String getCallThrem(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile,
			int i) {
		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if (i == 0) {
			enddate = today;
		}
		String monthint = stardate.getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() +"-"+ monthint;
		String month1 =  stardate.getYear() + monthint;
		// 本月的最后一天
		return LoginAndGetLiaoNingUnit.getCallThrem(webClientCookies, messageLogin, taskMobile, stardate.getDayOfMonth() + "",
				enddate.getDayOfMonth() + "", month,0,month1);
	}
	public static String getpayThrem(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile,
			int i) {
		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if (i == 0) {
			enddate = today;
		}
		String monthint = stardate.getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() +"-"+ monthint;
		String month1 =  stardate.getYear() + monthint;
		// 本月的最后一天
		return LoginAndGetLiaoNingUnit.getpayThrem(webClientCookies, messageLogin, taskMobile, stardate,
				enddate, month,0,month1);
	}
	
	
	public static String getSMSThrem(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile,
			int i) {
		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if (i == 0) {
			enddate = today;
		}
		String monthint = stardate.getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() +"-"+ monthint;
		String month1 =  stardate.getYear() + monthint;
		// 本月的最后一天
		return LoginAndGetLiaoNingUnit.getSMSThrem(webClientCookies, messageLogin, taskMobile, stardate.getDayOfMonth() + "",
				enddate.getDayOfMonth() + "", month,0,month1);
	}
	
	public static HashMap getphoneBillThrem(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile,
			int i) {
		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if (i == 0) {
			enddate = today;
		}
		String monthint = stardate.getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() +"-"+ monthint;
		String month1 =  stardate.getYear() + monthint;
		// 本月的最后一天
		 HashMap hashmap = LoginAndGetLiaoNingUnit.getphoneBillThrem(webClientCookies, messageLogin, taskMobile, stardate,
				enddate, month,0,month1);
		 return hashmap;
	}

}
