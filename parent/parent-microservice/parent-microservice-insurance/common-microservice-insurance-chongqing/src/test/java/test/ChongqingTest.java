package test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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

public class ChongqingTest {
	
	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
		String loginUrl = "http://ggfw.cqhrss.gov.cn/ggfw/index1.jsp";
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "ggfw.cqhrss.gov.cn");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");

	
		HtmlPage page = webClient.getPage(webRequest);
		System.out.println("登录页   =====》》"+page.getWebResponse().getContentAsString());
		
		HtmlImage image = page.getFirstByXPath("//img[@id='yzmimg']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
		
		
		HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("//input[@id='sfzh']");
		HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
		HtmlTextInput code = (HtmlTextInput)page.getFirstByXPath("//input[@id='validateCode']");
		HtmlElement button = (HtmlElement)page.getFirstByXPath("//input[@id='loginBtn']");
		
		System.out.println("username   :"+username.asXml());
		System.out.println("password   :"+password.asXml());
		System.out.println("code   :"+code.asXml());
		System.out.println("button   :"+button.asXml());
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		code.setText(input);
		username.setText("500383198508234169");
		password.setText("234169");
		
//		webClient.getOptions().setJavaScriptEnabled(true);
		HtmlPage loggedPage = button.click();
		System.out.println("登陆成功后的页面  ====》》"+loggedPage.getWebResponse().getContentAsString());
		Set<Cookie> cookies = loggedPage.getWebClient().getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());
		}
		
	
		getUserInfo(webClient);		
		
	}
private static void getUserInfo(WebClient webClient) throws Exception {
		
		//String str = "{header:{\"code\": -100, \"message\": {\"title\": \"\", \"detail\": \"\"}},body:{dataStores:{contentStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"contentStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.content\",attributes:{\"AAC002\": [\"130181198812163921\", \"12\"], \"AAE135\": [\"130181881216392\", \"12\"]}},xzStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"xzStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.xzxx\",attributes:{\"AAC002\": [\"130181198812163921\", \"12\"], \"AAE135\": [\"130181881216392\", \"12\"]}},sbkxxStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"sbkxxStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.sbkxx\",attributes:{\"AAC002\": [\"130181198812163921\", \"12\"], \"AAE135\": [\"130181881216392\", \"12\"]}},grqyjlStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"grqyjlStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.grqyjlyj\",attributes:{\"AAE135\": [\"130181198812163921\", \"12\"]}}},parameters:{\"BUSINESS_ID\": \"UCI314\", \"BUSINESS_REQUEST_ID\": \"REQ-IC-Q-098-60\", \"CUSTOMVPDPARA\": \"\", \"PAGE_ID\": \"\"}}}";
	
		String url = "http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_query.do?code=015";
		
		//String aaa = "{header:{\"code\": -100, \"message\": {\"title\": \"\", \"detail\": \"\"}},body:{dataStores:{contentStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"contentStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.content\",attributes:{\"AAC002\": [\"130181198812163921\", \"12\"], \"AAE135\": [\"\", \"12\"]}},xzStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"xzStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.xzxx\",attributes:{\"AAC002\": [\"130181198812163921\", \"12\"], \"AAE135\": [\"\", \"12\"]}},sbkxxStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"sbkxxStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.sbkxx\",attributes:{\"AAC002\": [\"130181198812163921\", \"12\"], \"AAE135\": [\"\", \"12\"]}},grqyjlStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"grqyjlStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.grqyjlyj\",attributes:{\"AAE135\": [\"130181198812163921\", \"12\"]}}},parameters:{\"BUSINESS_ID\": \"\", \"BUSINESS_REQUEST_ID\": \"\", \"CUSTOMVPDPARA\": \"\", \"PAGE_ID\": \"\"}}}";
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Host", "ggfw.cqhrss.gov.cn");
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Referer", "http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_main.do?code=015"); 
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0"); 
		webRequest.setAdditionalHeader("ajaxRequest", "true"); 
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"); 
	    webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
	    
	    HtmlPage  page=webClient.getPage(webRequest);
	    HtmlSelect year = (HtmlSelect)page.getFirstByXPath("//select[@name='year']");
	    year.setSelectedAttribute("2015", true);
	    
	    HtmlElement queryButton=(HtmlElement)page.getFirstByXPath("//button[@id='queryBtn']");
	    HtmlPage  queryPage=queryButton.click();
		int status = page.getWebResponse().getStatusCode();
		System.out.println("========================");
		System.out.println(queryPage.asXml());
		System.out.println(status);
		
	}
}
