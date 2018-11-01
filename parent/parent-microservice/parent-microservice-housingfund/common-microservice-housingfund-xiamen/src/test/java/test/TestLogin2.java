package test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin2 {

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String loginUrl = "http://wscx.xmgjj.gov.cn/xmgjjGR/login.shtml";		
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "wscx.xmgjj.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://wscx.xmgjj.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://wscx.xmgjj.gov.cn/xmgjjGR/login.jsp");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page = webClient.getPage(webRequest);
		System.out.println(page.asXml());


		HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("//input[@id='username']");
		HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
		HtmlElement button = (HtmlElement)page.getFirstByXPath("//a[@class='btn btn-block btn-primary']");

		System.out.println("username   :"+username.asXml());
		System.out.println("password   :"+password.asXml());
		System.out.println("button   :"+button.asXml());
	
		username.setText("350825199203104128");
		password.setText("ling128937");
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
		
		  String loginUrl4 = "http://wscx.xmgjj.gov.cn/xmgjjGR/queryGrzhxxJson.shtml";
			
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("custAcct", "10036589067"));
			paramsList.add(new NameValuePair("startDate", "20161018"));
			paramsList.add(new NameValuePair("endDate", "20171018"));
			webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Host", "wscx.xmgjj.gov.cn");
			webRequest4.setAdditionalHeader("Origin", "http://wscx.xmgjj.gov.cn");
			webRequest4.setAdditionalHeader("Referer", "http://wscx.xmgjj.gov.cn/xmgjjGR/grxx.php?custAcct=10036589067&startDate=20161018&endDate=20171018");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest4.setRequestParameters(paramsList);
			Page page4 = webClient.getPage(webRequest4);
			 System.out.println(page4.getWebResponse().getContentAsString());				
			if (page4.getWebResponse().getContentAsString().contains("个人信息")) {			
			}else{
				 page4 = webClient.getPage(webRequest4);
				 System.out.println();
			}
		  
		    String loginUrl5 = "http://wscx.xmgjj.gov.cn/xmgjjGR/queryZgzh.shtml";			
			WebRequest webRequest5 = new WebRequest(new URL(loginUrl5), HttpMethod.POST);
			webRequest5.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Host", "wscx.xmgjj.gov.cn");
			webRequest5.setAdditionalHeader("Origin", "http://wscx.xmgjj.gov.cn");				
			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			Page page5 = loggedPage.getWebClient().getPage(webRequest5);
			System.out.println(page5.getWebResponse().getContentAsString());
			if (page5.getWebResponse().getContentAsString().contains("listView")) {
                System.out.println(page5.getWebResponse().getContentAsString());
			}else{
				page5= loggedPage.getWebClient().getPage(webRequest5);
                System.out.println(page5.getWebResponse().getContentAsString());
			}		
			
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
