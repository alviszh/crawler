package app.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.crawler.cmcc.domain.json.LoginAuthJson;
import com.crawler.cmcc.domain.json.WebCmccParam;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;
import app.commontracerlog.TracerLog;

@Component
public class CmccLoginParser {

	public static final Logger log = LoggerFactory.getLogger(CmccLoginParser.class);
	private String welcomeUrl = "http://shop.10086.cn/i/v1/auth/getArtifact";
	@Autowired
	private TracerLog tracer;
	
	public WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	public String result;

	/**
	 * @Description: 是否发送随机短信
	 * @param mobile
	 * @return Boolean
	 * @throws Exception
	 */
	public String sendSMS(String mobileNum) {

		tracer.addTag("CmccLoginParser sendSMS", mobileNum);
		
		
		
		

		loadSendflag();
		webClient = loadToken(mobileNum);
		try {
			URL smsAction = new URL("https://login.10086.cn/sendRandomCodeAction.action");
			WebRequest  requestSettings = new WebRequest(smsAction, HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("userName", mobileNum));
			requestSettings.getRequestParameters().add(new NameValuePair("type", "01"));
			requestSettings.getRequestParameters().add(new NameValuePair("channelID", "12034"));

			requestSettings.setAdditionalHeader("Host", "login.10086.cn");
			requestSettings.setAdditionalHeader("Origin", "https://login.10086.cn");
			requestSettings.setAdditionalHeader("Referer", "https://login.10086.cn/login.html");
			requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
			requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8");
			requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			requestSettings.setAdditionalHeader("Xa-before", result);
			
			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();
			tracer.addTag("sendSMS 登录短信发送", html);
			return html;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 2018年10月8日，中国移动改版，第一次登录短信请求体中多了一个参数Xa-before。。此方法就是获得此参数的
	 * @param webClient
	 * @return
	 */
	private WebClient loadToken(String mobileNum) {
		String url = "https://login.10086.cn/loadToken.action";
		WebRequest requestSettings = null;
		try {
			requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} 
		
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8");
		requestSettings.setAdditionalHeader("Host", "login.10086.cn");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Origin", "https://login.10086.cn");
		requestSettings.setAdditionalHeader("Referer", "https://login.10086.cn/login.html");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("userName", mobileNum));
		
		Page page;
		try {
			page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();
			tracer.addTag("loadToken.html", html);
			JSONObject json = new JSONObject(html);
			result = (String) json.get("result");
			
			tracer.addTag("Xa-before", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return webClient;
	}

	/**
	 * @Description: 登录认证
	 * @param messageLogin
	 * @return LoginAuthJson
	 */
	public WebCmccParam loginAuthentication(MessageLogin messageLogin) {

		tracer.addTag("CmccLoginParser loginAuthentication", messageLogin.getTask_id());
		String authenticationUrl = "https://login.10086.cn/login.htm";
		WebCmccParam webCmccParam = new WebCmccParam();

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try {
			WebRequest requestSettings = new WebRequest(new URL(authenticationUrl), HttpMethod.GET);

			requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
			requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,fr;q=0.6");
			requestSettings.setAdditionalHeader("Connection", "keep-alive");
			requestSettings.setAdditionalHeader("Host", "login.10086.cn");
			requestSettings.setAdditionalHeader("Referer",
					"https://login.10086.cn/login.html?channelID=12003&backUrl=http://shop.10086.cn/i/");
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("accountType", "01"));
			requestSettings.getRequestParameters().add(new NameValuePair("account", messageLogin.getName()));
			requestSettings.getRequestParameters().add(new NameValuePair("password", messageLogin.getPassword()));
			requestSettings.getRequestParameters().add(new NameValuePair("pwdType", "01"));
			requestSettings.getRequestParameters().add(new NameValuePair("smsPwd", messageLogin.getSms_code()));
			requestSettings.getRequestParameters().add(new NameValuePair("inputCode", ""));
			requestSettings.getRequestParameters().add(new NameValuePair("backUrl", "http://shop.10086.cn/i/"));
			requestSettings.getRequestParameters().add(new NameValuePair("rememberMe", "0"));
			requestSettings.getRequestParameters().add(new NameValuePair("channelID", "12003"));
			requestSettings.getRequestParameters().add(new NameValuePair("protocol", "https:"));
			requestSettings.getRequestParameters().add(new NameValuePair("timestamp", "" + System.currentTimeMillis()));

			Page page = webClient.getPage(requestSettings);
			String json = page.getWebResponse().getContentAsString();
			tracer.addTag("loginAuthentication 登录验证信息", json);
			Gson gson = new Gson();
			LoginAuthJson laj = gson.fromJson(json, LoginAuthJson.class);

			webCmccParam.setLoginAuthJson(laj);
			webCmccParam.setWebClient(webClient);

			return webCmccParam;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @Description: 登录
	 * @return HtmlPage
	 * @throws Exception
	 */
	public WebClient getLoginResult(WebClient webClient, String artifac) {

		tracer.addTag("CmccLoginParser getLoginResult", artifac);

		WebRequest welcomeRequestSettings;
		try {
			welcomeRequestSettings = new WebRequest(new URL(welcomeUrl), HttpMethod.GET);
			welcomeRequestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			welcomeRequestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			welcomeRequestSettings.getRequestParameters().add(new NameValuePair("backUrl", "http://shop.10086.cn/i/"));
			welcomeRequestSettings.getRequestParameters().add(new NameValuePair("artifact", artifac));

			webClient.getPage(welcomeRequestSettings);
			return webClient;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//发送短信前 ，获取cookie中的sendflag值
	public WebClient loadSendflag() {
		String url = "https://login.10086.cn/loadSendflag.htm?timestamp=";
		WebRequest requestSettings = null;
		try {
			requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}  
		requestSettings.setAdditionalHeader("Host", "login.10086.cn");
		requestSettings.setAdditionalHeader("Referer", "https://login.10086.cn/login.html");
		requestSettings.setAdditionalHeader("Accept", "image/webp,image/*,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch, br");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Cache-Control", "no-cache");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
	
		try {
			Page page = webClient.getPage(requestSettings);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return webClient;
	}
	
	//发送短信前获取cookie中的CaptchaCode
	public WebClient captchazh(WebClient webClient) {
		
		String url = "https://login.10086.cn/captchazh.htm?type=12";	
		WebRequest requestSettings;
		try {
			requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
			requestSettings.setAdditionalHeader("Accept", "image/webp,image/*,*/*;q=0.8");
			requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch, br");
			requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			requestSettings.setAdditionalHeader("Host", "login.10086.cn");
			requestSettings.setAdditionalHeader("Referer", "https://login.10086.cn/login.html");
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			
			Page page = webClient.getPage(requestSettings); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return webClient;
		
	}

}
