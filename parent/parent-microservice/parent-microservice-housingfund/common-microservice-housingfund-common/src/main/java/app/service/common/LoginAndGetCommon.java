package app.service.common;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class LoginAndGetCommon extends AbstractChaoJiYingHandler{
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	public final Logger log = LoggerFactory.getLogger(LoginAndGetCommon.class);

	private static Gson gson = new Gson();

	public static String setcookieTojson(Set<Cookie> setcookie) {
		return gson.toJson(setcookie);
	}
	
	public static Page gethtmlWebRequest(WebClient webClient, WebRequest webRequest ) throws FailingHttpStatusCodeException, IOException {
		webClient.setJavaScriptTimeout(10000); 
		webClient.getOptions().setTimeout(10000); // 15->60 
		webClient.waitForBackgroundJavaScript(10000);
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
	
	
	public static WebClient addcookie(WebClient webclient, TaskHousing taskHousing) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}
		return webclient;
	}
	
	public static String getVerfiycodeBy(By by, WebDriver driver,Class clas) throws Exception {
		String path = WebDriverUnit.saveImg(driver, by);
		System.out.println("path---------------" + path);
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path);
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		return code;
	}

}
