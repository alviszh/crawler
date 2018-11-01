package app.service.common;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

public class LoginAndGetCommon {

	public final Logger log = LoggerFactory.getLogger(LoginAndGetCommon.class);

	public Page gethtmlPostByWebRequest(WebClient webClient, WebRequest webRequest, String url) throws Exception, IOException {

		webClient.getOptions().setJavaScriptEnabled(false);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

	public static Page getHtml(String url, WebClient webClient) throws Exception, IOException {
		WebRequest webRequest;
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;

	}

	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws Exception {

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

	public static WebClient addcookie(WebClient webclient, TaskMobile taskMobile) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}

		return webclient;
	}

}
