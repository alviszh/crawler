package test;

import java.io.File;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;

public class TestLogin {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String loginUrl= "http://www.suizhougjj.cn/szgjjwt/login.jsp";
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);	
    	webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.suizhougjj.cn");		
		HtmlPage page = webClient.getPage(webRequest);
		webClient.getOptions().setJavaScriptEnabled(true);  
		webClient.waitForBackgroundJavaScript(20000);
	  
		System.out.println(page.asXml());
		HtmlImage image =page.getFirstByXPath("//img[@id='i_codegr']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
	

//		HtmlTextInput username = (HtmlTextInput)page2.getFirstByXPath("//input[@id='username_per']");
//
//		
//		HtmlPasswordInput password = (HtmlPasswordInput)page2.getFirstByXPath("//input[@name='password']");	
//		HtmlTextInput code = (HtmlTextInput)page2.getFirstByXPath("//input[@id='captcha']");		
//		HtmlButton button = (HtmlButton)page2.getFirstByXPath("//button[@id='loginBtn']");
//		
//		System.out.println("username   :"+username.asXml());
//		System.out.println("password   :"+password.asXml());	
//		System.out.println("button   :"+button.asXml());
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
//		code.setText(input);
//		username.setText("321321198801050033");
//		password.setText("529557");
			
//		Page loggedPage = button.click();
//		Thread.sleep(1500);
//		System.out.println("登陆成功后的页面  ====》》"+loggedPage.getWebResponse().getContentAsString());
//		Set<Cookie> cookies = loggedPage.getWebClient().getCookieManager().getCookies();
//		for(Cookie cookie:cookies){
//			System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());
//		}
//		
		    String loginUrl4 = "http://www.suizhougjj.cn/szgjjwt/action/login";		 
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
			String requestBody="cardno=429001198803141237&WZMM=888888&code="+input+"&yhlbdm=01";
			webRequest4.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			webRequest4.setAdditionalHeader("Host", "www.suizhougjj.cn");
			webRequest4.setAdditionalHeader("Origin", "http://www.suizhougjj.cn");
			webRequest4.setAdditionalHeader("Referer", "http://www.suizhougjj.cn/szgjjwt/login.jsp");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");
			webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");			
			webRequest4.setRequestBody(requestBody);
			Page page4 = webClient.getPage(webRequest4);
			String html=page4.getWebResponse().getContentAsString();
			JSONObject object = JSONObject.fromObject(html);
			String loginMsg = object.getString("success");   
			if (loginMsg.equals("true")) {
				System.out.println("========"+loginMsg);
			}
			System.out.println(page4.getWebResponse().getContentAsString());
			//{"success":"true","sjhm":"158******3208"}
		    //{"success":"false","msg":"核心系统处理失败，失败原因：登录用户名或密码错误！"}
			//{"success":"false","msg":"验证码输入有误!"}
				
			
			
//		   String loginUrl5 = "http://www.suizhougjj.cn/szgjjwt/action/duanxin_yzservlet";		                     
//			WebRequest webRequest5= new WebRequest(new URL(loginUrl5), HttpMethod.POST);	
//		
//			String requestBody2="exeType=getdx&dtmyz=WT00004";
//			webRequest5.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//			webRequest5.setAdditionalHeader("Connection", "keep-alive");
//			webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//			webRequest5.setAdditionalHeader("Host", "www.suizhougjj.cn");
//			webRequest5.setAdditionalHeader("Origin", "http://www.suizhougjj.cn");
//			webRequest5.setAdditionalHeader("Referer", "http://www.suizhougjj.cn/szgjjwt/login.jsp");
//			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
//			webRequest5.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//			webRequest5.setRequestBody(requestBody2);
//			Page page5 = webClient.getPage(webRequest5);
//			System.out.println(page5.getWebResponse().getContentAsString());
//			
//           
//			
//			@SuppressWarnings("resource")
//			Scanner scanner2 = new Scanner(System.in);
//			String input2 = scanner2.next();
//			
//			
//			
//		    String loginUrl6 = "http://www.suizhougjj.cn/szgjjwt/action/duanxin_yzservlet";		                     
//			WebRequest webRequest6= new WebRequest(new URL(loginUrl6), HttpMethod.POST);	
//			
//			List<NameValuePair> paramsList6 = new ArrayList<NameValuePair>();
//			paramsList6 = new ArrayList<NameValuePair>();
//			paramsList6.add(new NameValuePair("exeType", "yzdx"));
//			paramsList6.add(new NameValuePair("dtmyz", "WT00004"));
//			paramsList6.add(new NameValuePair("dxyzm", input2));
//			String requestBody3="exeType=yzdx&dtmyz=WT00004&dxyzm="+input2;
//			webRequest6.setAdditionalHeader("Accept", "*/*");
//			webRequest6.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//			webRequest6.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//			webRequest6.setAdditionalHeader("Connection", "keep-alive");
//			webRequest6.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//			webRequest6.setAdditionalHeader("Host", "www.suizhougjj.cn");
//			webRequest6.setAdditionalHeader("Origin", "http://www.suizhougjj.cn");
//			webRequest6.setAdditionalHeader("Referer", "http://www.suizhougjj.cn/szgjjwt/login.jsp");
//			webRequest6.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
//			webRequest6.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//			webRequest6.setRequestBody(requestBody3);
//			Page page6 = webClient.getPage(webRequest6);
//			System.out.println(page6.getWebResponse().getContentAsString());
//			
	
				
		  HtmlPage page8=getHtml("http://www.suizhougjj.cn/szgjjwt/page/gr/grcx_jbxx.jsp",  webClient);
				
			System.out.println(page8.asXml());
			
			
		    String loginUrl7 = "http://www.suizhougjj.cn/szgjjwt/action/grcx_jb";		                     
			WebRequest webRequest7= new WebRequest(new URL(loginUrl7), HttpMethod.POST);	
			String requestBody7="kjny=201707-201806&pageNumber=1&pagesize=50";
			webRequest7.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest7.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest7.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest7.setAdditionalHeader("Connection", "keep-alive");
			webRequest7.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			webRequest7.setAdditionalHeader("Host", "www.suizhougjj.cn");
			webRequest7.setAdditionalHeader("Origin", "http://www.suizhougjj.cn");
			webRequest7.setAdditionalHeader("Referer", "http://www.suizhougjj.cn/szgjjwt/page/gr/grcx_jbxx.jsp");
			webRequest7.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest7.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
	
			webRequest7.setRequestBody(requestBody7);
			Page page7 = webClient.getPage(webRequest7);
			System.out.println(page7.getWebResponse().getContentAsString());
	
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
