package test;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String loginUrl = "https://gx12333.net/index/loginInit.html";		
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "gx12333.net");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page = webClient.getPage(webRequest);
		
		System.out.println(page.asXml());
		
		HtmlImage image = page.getFirstByXPath("//img[@id='codeimg']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
	
		HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("//input[@id='aac002_t']");
		HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='aac003_t']");
		HtmlTextInput code = (HtmlTextInput)page.getFirstByXPath("//input[@name='yzm_t']");
		HtmlElement button = (HtmlElement)page.getFirstByXPath("//button[@type='button']");
//		System.out.println("username   :"+username.asXml());
//		System.out.println("password   :"+password.asXml());
//		System.out.println("code   :"+code.asXml());
//		System.out.println("button   :"+button.asXml());
//		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		code.setText(input);
//
		username.setText("450121199206201847");
		password.setText("123456");
		webClient.getOptions().setJavaScriptEnabled(true);
		//您输入的密码有误  密码错误
		//出现错误！您的身份证信息未通过系统校验，请携带相关身份证明至中心服务大厅进行修改  账号错误
		//出现错误！您输入的验证码有误，请核实！   验证码错误
		//公积金查询  正确
		HtmlPage loggedPage = button.click();
		System.out.println("登陆成功后的页面  ====》》"+loggedPage.getWebResponse().getContentAsString());
		
		
		Set<Cookie> cookies = loggedPage.getWebClient().getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());
		}
		
		    String loginUrl4 = "https://www.sdwfhrss.gov.cn/rsjwz/self/rzlogin";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("username", "G19040754"));
			paramsList.add(new NameValuePair("password", "gl666124"));
			paramsList.add(new NameValuePair("yzm", input));
			paramsList.add(new NameValuePair("button", ""));
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);
			webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Host", "www.sdwfhrss.gov.cn");
			webRequest4.setAdditionalHeader("Origin", "https://www.sdwfhrss.gov.cn");
			webRequest4.setAdditionalHeader("Referer", "https://www.sdwfhrss.gov.cn/rsjwz/self/login");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest4.setRequestParameters(paramsList);
			HtmlPage page4 = webClient.getPage(webRequest4);
			System.out.println(page4.getWebResponse().getContentAsString());
	
			HtmlPage pageInfo=getHtml("https://www.sdwfhrss.gov.cn/rsjwz/self/cbgk",webClient);
			System.out.println(pageInfo.asXml());
			
			//https://www.sdwfhrss.gov.cn/rsjwz/self/cblb?time=2017
		    String loginUrl5 = "https://www.sdwfhrss.gov.cn/rsjwz/self/yljfls";
		
			WebRequest webRequest5 = new WebRequest(new URL(loginUrl5), HttpMethod.GET);
			webRequest5.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Host", "www.sdwfhrss.gov.cn");
			webRequest5.setAdditionalHeader("Referer", "https://www.sdwfhrss.gov.cn/rsjwz/self/yljfls");
			webRequest5.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			HtmlPage page5 = webClient.getPage(webRequest5);
			System.out.println(page5.asXml());
			
			
			
			System.out.println(getHtml("https://www.sdwfhrss.gov.cn/rsjwz/self/cblb?time=2016",webClient).asXml());
			
			String medUrl="https://www.sdwfhrss.gov.cn/rsjwz/self/zgyl?time=2017";
			HtmlPage medPage= getHtml(medUrl,webClient);
			System.out.println(medPage.asXml());
		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
