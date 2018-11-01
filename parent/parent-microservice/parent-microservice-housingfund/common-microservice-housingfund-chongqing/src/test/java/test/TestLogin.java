package test;

import java.io.File;
import java.net.URL;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {

//		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
//		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
//		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		//webClient.getOptions().setJavaScriptEnabled(false);
		String loginUrl = "http://www.cqgjj.cn/html/user/login.html";
//		
//		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Host", "www.cqgjj.cn");
//		webRequest.setAdditionalHeader("Referer", "http://www.cqgjj.cn/html/user/login.html");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page = getHtml(loginUrl,webClient);
		System.out.println("登录页   =====》》"+page.asXml());
		
		String loginUrl2 = "http://www.cqgjj.cn/Member/UserLogin.aspx?type=null";	

		WebRequest webRequest2 = new WebRequest(new URL(loginUrl2), HttpMethod.GET);
		webRequest2.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Host", "www.cqgjj.cn");
		webRequest2.setAdditionalHeader("Referer", "http://www.cqgjj.cn/Member/UserLogin.aspx?type=null");
		webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page2 = webClient.getPage(webRequest2);
		
		System.out.println(page2.asXml());
		
	
		HtmlImage image = page2.getFirstByXPath("//img[@id='imgCode']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
		

		HtmlTextInput username = (HtmlTextInput)page2.getFirstByXPath("//input[@id='txt_loginname']");
		HtmlPasswordInput password = (HtmlPasswordInput)page2.getFirstByXPath("//input[@id='txt_pwd']");
		HtmlTextInput code = (HtmlTextInput)page2.getFirstByXPath("//input[@id='txt_code']");
		HtmlElement button = (HtmlElement)page2.getFirstByXPath("//input[@id='loginBtn']");
		
		System.out.println("username   :"+username.asXml());
		System.out.println("password   :"+password.asXml());
		System.out.println("code   :"+code.asXml());
		System.out.println("button   :"+button.asXml());
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		code.setText(input);
		username.setText("li_272143200");
		password.setText("xixi123");
		
//		webClient.getOptions().setJavaScriptEnabled(true);
		HtmlPage loggedPage = button.click();
		Thread.sleep(1500);
		System.out.println("登陆成功后的页面  ====》》"+loggedPage.getWebResponse().getContentAsString());

		Set<Cookie> cookies = loggedPage.getWebClient().getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());
		}
		
		  String loginUrl4 = "http://www.cqgjj.cn/Member/Index.aspx";
			
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.GET);
			webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Host", "www.cqgjj.cn");
			webRequest4.setAdditionalHeader("Referer", "http://www.cqgjj.cn/html/user/index.html");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			HtmlPage page4 = webClient.getPage(webRequest4);
			 System.out.println(page4.getWebResponse().getContentAsString());
			if (page4.getWebResponse().getContentAsString().contains("欢迎您登录")) {
				 System.out.println(page4.getWebResponse().getContentAsString());
				
			}else{
				 page4 = webClient.getPage(webRequest4);
				 System.out.println();
			}
		  
		    String loginUrl5 = "http://www.cqgjj.cn/Member/gr/gjjyecx.aspx";
			
			WebRequest webRequest5 = new WebRequest(new URL(loginUrl5), HttpMethod.GET);
			webRequest5.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Host", "www.cqgjj.cn");
			webRequest5.setAdditionalHeader("Referer", "http://www.cqgjj.cn/Member/gr/gjjyecx.aspx");
			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			HtmlPage page5 = loggedPage.getWebClient().getPage(webRequest5);
			if (page5.getWebResponse().getContentAsString().contains("listinfo")) {
                System.out.println(page5.getWebResponse().getContentAsString());
			}else{
				page5= loggedPage.getWebClient().getPage(webRequest5);
                System.out.println(page5.getWebResponse().getContentAsString());
			}
			
//			
			String loginUrl6 = "http://www.cqgjj.cn/Member/gr/gjjmxcx.aspx";	
			WebRequest webRequest6 = new WebRequest(new URL(loginUrl6), HttpMethod.GET);
			webRequest6.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest6.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest6.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest6.setAdditionalHeader("Connection", "keep-alive");
			webRequest6.setAdditionalHeader("Host", "www.cqgjj.cn");
			webRequest6.setAdditionalHeader("Referer", "http://www.cqgjj.cn/Member/gr/gjjmxcx.aspx");
			webRequest6.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			HtmlPage page6= loggedPage.getWebClient().getPage(webRequest6);
			if (page6.getWebResponse().getContentAsString().contains("您的查询过于频繁，请输入验证码后继续")) {
				HtmlTextInput code2 = (HtmlTextInput)page6.getFirstByXPath("//input[@id='ContentPlaceHolder1_TextBox_Code']");
				HtmlElement button2 = (HtmlElement)page6.getFirstByXPath("//input[@id='ContentPlaceHolder1_Button_Select']");
				String imageName2 = UUID.randomUUID() + ".jpg";
				File file2 = new File("D:\\img\\"+imageName2);
				//image.saveAs(file2);
				
				@SuppressWarnings("resource")
				Scanner scanner2 = new Scanner(System.in);
				String input2 = scanner2.next();
				code2.setText(input2);
				
				HtmlPage loggedPage2 = button2.click();
				System.out.println(loggedPage2.getWebResponse().getContentAsString());
			}else if (page6.getWebResponse().getContentAsString().contains("listinfo")) {
				 System.out.println(page6.getWebResponse().getContentAsString());
			}else{
				 page6= loggedPage.getWebClient().getPage(webRequest6);
                System.out.println(page6.getWebResponse().getContentAsString());
			}		
			String loginUrl8 = "http://www.cqgjj.cn/Member/UserInfo/MyInformation.aspx";	
			WebRequest webRequest8 = new WebRequest(new URL(loginUrl8), HttpMethod.GET);
			webRequest8.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest8.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest8.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest8.setAdditionalHeader("Connection", "keep-alive");
			webRequest8.setAdditionalHeader("Host", "www.cqgjj.cn");
			webRequest8.setAdditionalHeader("Referer", "http://www.cqgjj.cn/Member/UserInfo/MyInformation.aspx");
			webRequest8.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			HtmlPage page8= loggedPage.getWebClient().getPage(webRequest8);
			if (page8.getWebResponse().getContentAsString().contains("listinfo")) {
                System.out.println(page8.getWebResponse().getContentAsString());
			}else{
				page8= loggedPage.getWebClient().getPage(webRequest8);
                System.out.println(page8.getWebResponse().getContentAsString());
			}
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
