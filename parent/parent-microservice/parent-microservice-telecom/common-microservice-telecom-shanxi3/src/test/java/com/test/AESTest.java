package com.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.StandardSocketOptions;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class AESTest  extends AbstractChaoJiYingHandler{
	
	static String phonenum = "18995154123";

	static String password = "795372";
	
	
	public static void main(String[] args) throws Exception {
		 
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		
//		/AESTest rsaTest = new AESTest();
		/*String str = rsaTest.passwordAES(password);
		System.out.println("password------------>"+str);*/
		//clicklogin();
		postlogin();
	}
	
	public static void postlogin() throws Exception{ 
		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
		HtmlPage page = webClient.getPage("http://login.189.cn/web/login"); 
		HtmlTextInput usernameInput = (HtmlTextInput) page.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) page.getFirstByXPath("//input[@id='txtPassword']"); 
		HtmlElement button = (HtmlElement) page.getFirstByXPath("//a[@id='loginbtn']");
		usernameInput.setText(phonenum);
		passwordInput.setText(password); 
		HtmlPage page2 = button.click();
		System.out.println("-----------------------------------------login   1---------------------------------------------");
		System.out.println(page2.asXml()); 
		if (page2.asXml().indexOf("登录失败") != -1) {
			HtmlImage valiCodeImg = page2.getFirstByXPath("//*[@id='imgCaptcha']");
			//HtmlTextInput valicodeStrinput = (HtmlTextInput) page2.getFirstByXPath("//*[@id='txtCaptcha']");
			//HtmlElement button2 = (HtmlElement) page2.getFirstByXPath("//a[@id='loginbtn']");
			if ((valiCodeImg.isDisplayed())) {
				System.out.println("Element is  displayed!");  
				String imageName ="D:\\img\\"+ UUID.randomUUID() + ".jpg";
				System.out.println("imageName-------"+imageName);
				File file = new File(imageName);
				valiCodeImg.saveAs(file);  
				String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", imageName); 
				Gson gson = new GsonBuilder().create();
				String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
				System.out.println("code---------"+code.toUpperCase());
				
				
				Set<Cookie> cookies = webClient.getCookieManager().getCookies();
				
				for(Cookie cookie : cookies){
					System.out.println("cookie  ====== "+cookie.getName()+" : "+cookie.getValue());
				}
				
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn",".ybtj.189.cn","031467AADD65C2F936BCA8E0C5094CC9"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","ECS_ReqInfo_login1","U2FsdGVkX18ub5YqXzG3vPb5dwzZqP%2FFrD0RugVibXrWRBAEUZtOC5ofPEGB0UG74VHBRaCJy%2F2GsQWYfvJr93GacX0CromOt8dQ9IcTRh0%3D"));
				//webClient.getCookieManager().addCookie(new Cookie("login.189.cn","EcsCaptchaKey","%2Bf0WSolHEoWVxnB%2B9ieWtxNqj%2BKPjPOLga5PFxbKSmwypnWfLdjcJw%3D%3D"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","EcsToLoginPara","zBPyOxHfMwekoO0uXkKvuYa0LkztAQUHt2wgHWyOSuvLhmRjXAjLIsx%2BIr63y6qxtXC7DENQrXtFBXtp1wvc1vAM9VLgHpF1w6EQbfCqozyM2DCJjIMgIMYHvkatseqA49PWU7553RU%3D"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","Hm_lpvt_79fae2027f43ca31186e567c6c8fe33e","1518255148"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","Hm_lvt_79fae2027f43ca31186e567c6c8fe33e","1518250758"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","SHOPID_COOKIEID","10009"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","UM_distinctid","15f0b88c00bff-06b132410c9ee9-5d153b16-15f900-15f0b88c00c892"));
				//webClient.getCookieManager().addCookie(new Cookie("login.189.cn","__qc_wId","604"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","_gscu_1758414200","065660565qd3ri76"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","_qddaz","QD.tkja1f.2rv92k.j8n0i0j8"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","aactgsh111220","18995154123"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","cityCode","nx"));
				//webClient.getCookieManager().addCookie(new Cookie("login.189.cn","code_v","20170913"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","fenxiId","e188eefe-8b68-4a06-9ec8-f31c2c4f3e9c"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","isLogin","non-logined"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","loginStatus","non-logined"));
				//webClient.getCookieManager().addCookie(new Cookie("login.189.cn","lvid","92767f1fc51fdff9b25895c1578116a1"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","nvid","1"));
				//webClient.getCookieManager().addCookie(new Cookie("login.189.cn","pgv_pvid","5341320050"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","s_cc","TRUE"));
				//webClient.getCookieManager().addCookie(new Cookie("login.189.cn","s_fid","12ABAAD03C80454E-21FDBB89D6AFC334")); 
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","s_sq","%5B%5BB%5D%5D"));
				//webClient.getCookieManager().addCookie(new Cookie("login.189.cn","svid","64EE0F7861A5C8E0"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","trkHmCity","0"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","trkHmClickCoords","868%2C453%2C682"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","trkHmCoords","0"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","trkHmLinks","0"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","trkHmPageName","%2Fnx%2F"));
				//webClient.getCookieManager().addCookie(new Cookie("login.189.cn","trkId","E7AB898A-149F-4D88-BF97-C12488B3586E"));
				webClient.getCookieManager().addCookie(new Cookie("login.189.cn","userId","201%7C100193920")); 
				
				WebRequest requestSettings = new WebRequest(new URL("http://login.189.cn/web/login"), HttpMethod.POST);
				requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
				requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
				requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
				requestSettings.setAdditionalHeader("Pragma", "no-cache"); 
				requestSettings.setAdditionalHeader("Host", "login.189.cn");
				requestSettings.setAdditionalHeader("Referer", "http://login.189.cn/web/login");
				requestSettings.setAdditionalHeader("Origin", "http://login.189.cn");  
			 
				requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
				requestSettings.getRequestParameters().add(new NameValuePair("Account", phonenum));
				requestSettings.getRequestParameters().add(new NameValuePair("UType", "204"));
				requestSettings.getRequestParameters().add(new NameValuePair("ProvinceID", "30"));
				requestSettings.getRequestParameters().add(new NameValuePair("AreaCode", "951"));
				requestSettings.getRequestParameters().add(new NameValuePair("CityNo", "951"));
				requestSettings.getRequestParameters().add(new NameValuePair("RandomFlag", "0"));
				requestSettings.getRequestParameters().add(new NameValuePair("Password", "t+03g3wrJ/8yinKZ2e7FFA=="));
				requestSettings.getRequestParameters().add(new NameValuePair("Captcha", code.toUpperCase())); 
				requestSettings.setCharset(Charset.forName("UTF-8"));
				Page searchPage = webClient.getPage(requestSettings); 
				
				
				System.out.println("searchPage------------"+searchPage.getWebResponse().getContentAsString());
				
				Set<Cookie> cookies2 = webClient.getCookieManager().getCookies();
				
				for(Cookie cookie : cookies2){
					System.out.println("cookie  ===222=== "+cookie.getName()+" : "+cookie.getValue());
				}
				
				List<NameValuePair> responseHeaders = searchPage.getWebResponse().getResponseHeaders();
				 
				for(NameValuePair nameValuePair : responseHeaders){
					System.out.println("nameValuePair  ===222=== "+nameValuePair.getName()+" : "+nameValuePair.getValue());
				}
				 
				//usernameInput.setText(phonenum);
				//passwordInput.setText(password);
				//valicodeStrinput.setText(code.toLowerCase().trim()); 

				//HtmlPage page3 = button2.click(); 
				//System.out.println("-----------------------------------------login   2---------------------------------------------");
				//System.out.println(page3.asXml()); 
			}else{
				System.out.println("Element is not displayed!"); 
			} 
			System.out.println("-----------------------------------------------------------------------------------------"); 
			
		} 
		
		Page indexdo = webClient.getPage("http://www.189.cn/login/index.do");
		
		System.out.println("------indexdo------"+indexdo.getWebResponse().getContentAsString());
		
		
	}
	
	
	
	public static void clicklogin() throws Exception{
		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
		HtmlPage page = webClient.getPage("http://login.189.cn/web/login");
		
		HtmlTextInput usernameInput = (HtmlTextInput) page.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) page.getFirstByXPath("//input[@id='txtPassword']"); 
		HtmlElement button = (HtmlElement) page.getFirstByXPath("//a[@id='loginbtn']");
		usernameInput.setText(phonenum);
		passwordInput.setText(password);
		
		HtmlPage page2 = button.click();
		System.out.println("-----------------------------------------login   1---------------------------------------------");
		System.out.println(page2.asXml());
		
		
		if (page2.asXml().indexOf("登录失败") != -1) {
			HtmlImage valiCodeImg = page2.getFirstByXPath("//*[@id='imgCaptcha']");
			HtmlTextInput valicodeStrinput = (HtmlTextInput) page2.getFirstByXPath("//*[@id='txtCaptcha']");
			HtmlElement button2 = (HtmlElement) page2.getFirstByXPath("//a[@id='loginbtn']");
			if ((valiCodeImg.isDisplayed())) {
				System.out.println("Element is  displayed!"); 
				
				String imageName ="D:\\img\\"+ UUID.randomUUID() + ".jpg";
				System.out.println("imageName-------"+imageName);
				File file = new File(imageName);
				valiCodeImg.saveAs(file);  
				String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", imageName); 
				Gson gson = new GsonBuilder().create();
				String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
				System.out.println("code---------"+code);
				 
				usernameInput.setText(phonenum);
				passwordInput.setText(password);
				valicodeStrinput.setText(code.toLowerCase().trim()); 

				HtmlPage page3 = button2.click();
				
				System.out.println("-----------------------------------------login   2---------------------------------------------");
				System.out.println(page3.asXml());
			
				  
			}else{
				System.out.println("Element is not displayed!"); 
			} 
			System.out.println("-----------------------------------------------------------------------------------------"); 
			
		} 
		HtmlPage htmlpage2 = button.click();
		System.out.println("------登录------" + htmlpage2.asXml());
		
		Page indexdo = webClient.getPage("http://www.189.cn/login/index.do");
		
		System.out.println("------indexdo------"+indexdo.getWebResponse().getContentAsString());
		
		
		/*WebRequest requestSettings = new WebRequest(new URL("http://login.189.cn/web/login"), HttpMethod.POST);
		requestSettings.setAdditionalHeader("Host", "login.189.cn");
		requestSettings.setAdditionalHeader("Referer", "http://login.189.cn/web/login");
		requestSettings.setAdditionalHeader("Origin", "http://login.189.cn");  
	
		
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("Account", phonenum));
		requestSettings.getRequestParameters().add(new NameValuePair("UType", "204"));
		requestSettings.getRequestParameters().add(new NameValuePair("ProvinceID", ""));
		requestSettings.getRequestParameters().add(new NameValuePair("AreaCode", ""));
		requestSettings.getRequestParameters().add(new NameValuePair("CityNo", ""));
		requestSettings.getRequestParameters().add(new NameValuePair("RandomFlag", "0"));
		requestSettings.getRequestParameters().add(new NameValuePair("Password", aespasword));
		requestSettings.getRequestParameters().add(new NameValuePair("Captcha", valicodeStr)); 
		
		Page searchPage = webClient.getPage(requestSettings); 
		
		
		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		*/
		
		
	}
	
	

	public String passwordAES(String phonenum) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("aes.js", Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("aesEncrypt",phonenum); 
		return data.toString(); 
	}
	
	public String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
}

}
