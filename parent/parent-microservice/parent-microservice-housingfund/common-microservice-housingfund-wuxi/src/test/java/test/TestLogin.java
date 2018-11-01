package test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		
		String loginUrl = "http://58.215.195.18:10010/login_person.jsp";		
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "58.215.195.18:10010");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page = webClient.getPage(webRequest);
		
		System.out.println(page.asXml());
		
		HtmlImage image = page.getFirstByXPath("//img[@id='kaptcha-img1']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
		
		 Document doc = Jsoup.parse(page.asXml(), "utf-8"); 
		 Elements div= doc.getElementsByAttributeValue("class","login");
		 Element table=div.select("table").get(0);
		 String type=table.getElementById("logontype").getElementsByAttribute("selected").val();
			
		
		HtmlSelect logontype = (HtmlSelect)page.getFirstByXPath("//select[@id='logontype']");
		HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("//input[@id='loginname']");
		HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
		HtmlTextInput code = (HtmlTextInput)page.getFirstByXPath("//input[@name='_login_checkcode']");
		HtmlElement button = (HtmlElement)page.getFirstByXPath("//input[@name='image']");
		System.out.println("logontype   :"+logontype.asXml());
		System.out.println("username   :"+username.asXml());
		System.out.println("password   :"+password.asXml());
		System.out.println("code   :"+code.asXml());
		System.out.println("button   :"+button.asXml());
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		code.setText(input);
		logontype.setSelectedAttribute(type, true);
		
		username.setText("320324198905213720");
		password.setText("213720");
//		webClient.getOptions().setJavaScriptEnabled(true);
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
		
//		  String loginUrl4 = "http://58.215.195.18:10010/phoneNum.do?temp=0.2956186880276712";
//			
//			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.GET);
//			webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//			webRequest4.setAdditionalHeader("Connection", "keep-alive");
//			webRequest4.setAdditionalHeader("Host", "58.215.195.18:10010");
//			webRequest4.setAdditionalHeader("Referer", "http://58.215.195.18:10010/logon.do");
//			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
//			HtmlPage page4 = webClient.getPage(webRequest4);
//			 System.out.println(page4.getWebResponse().getContentAsString());
//				
//			if (page4.getWebResponse().getContentAsString().contains("个人信息")) {
//				 System.out.println(page4.getWebResponse().getContentAsString());
//				
//			}else{
//				 page4 = webClient.getPage(webRequest4);
//				 System.out.println();
//			}
		  
		    String loginUrl5 = "http://58.215.195.18:10010/zg_info.do";
			
			WebRequest webRequest5 = new WebRequest(new URL(loginUrl5), HttpMethod.GET);
			webRequest5.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Host", "58.215.195.18:10010");
			webRequest5.setAdditionalHeader("Referer", "http://58.215.195.18:10010/menu.do?zjlxCheck=null&temp=0.4599769082889047");
			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			HtmlPage page5 = loggedPage.getWebClient().getPage(webRequest5);
			if (page5.getWebResponse().getContentAsString().contains("listView")) {
                System.out.println(page5.getWebResponse().getContentAsString());
			}else{
				page5= loggedPage.getWebClient().getPage(webRequest5);
                System.out.println(page5.getWebResponse().getContentAsString());
			}		
			String loginUrl8 = "http://58.215.195.18:10010/mx_info.do";	
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("zjlx", "1"));
			paramsList.add(new NameValuePair("hjstatus", "%D5%FD%B3%A3%BB%E3%BD%C9"));
			paramsList.add(new NameValuePair("submit", "%B2%E9++%D1%AF"));
			WebRequest webRequest8 = new WebRequest(new URL(loginUrl8), HttpMethod.POST);
			webRequest8.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest8.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest8.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest8.setAdditionalHeader("Connection", "keep-alive");
			webRequest8.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest8.setAdditionalHeader("Host", "58.215.195.18:10010");
			webRequest8.setAdditionalHeader("Origin", "http://58.215.195.18:10010");
			webRequest8.setAdditionalHeader("Referer", "http://58.215.195.18:10010/mx_info.do?flag=1&temp=0.7225978488818365");
			webRequest8.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest8.setRequestParameters(paramsList);
			HtmlPage page8= loggedPage.getWebClient().getPage(webRequest8);
			System.out.println(page8.getWebResponse().getContentAsString());
			//zjlx=1&hjstatus=%D5%FD%B3%A3%BB%E3%BD%C9&submit=%B2%E9++%D1%AF
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
