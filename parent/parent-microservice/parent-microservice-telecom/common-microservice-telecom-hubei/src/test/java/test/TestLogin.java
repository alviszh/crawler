package test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {
//		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog"); 
//		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
//		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
       	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/web/login";
		WebRequest webRequestt = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage html1 = webClient.getPage(webRequestt);
		HtmlImage vc1 = (HtmlImage) html1.getFirstByXPath("//img[@id='imgCaptcha']");
		
		HtmlPage html = (HtmlPage) vc1.click();
		
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlTextInput vinput = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtCaptcha']");
		HtmlImage vc = (HtmlImage) html.getFirstByXPath("//img[@id='imgCaptcha']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");

		File file = new File("D:\\img\\tele.jpg"); 
		vc.saveAs(file); 

		@SuppressWarnings("resource") 
		Scanner scanner = new Scanner(System.in); 
		String input = scanner.next();
		
		username.setText("17707287540");
    	passwordInput.setText("081202");
    	vinput.setText(input);

		HtmlPage htmlpage = button.click();	
		
		String url1 = "http://hb.189.cn/pages/login/hbuserCenter.parser";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
		webClient.getPage(webRequest);
		
		String url2 = "http://hb.189.cn/ajaxServlet/getCityCodeAndIsLogin?method=getCityCodeAndIsLogin";
		WebRequest webRequest1 = new WebRequest(new URL(url2), HttpMethod.POST);
		Page page = webClient.getPage(webRequest1);
		System.out.println(page.getWebResponse().getContentAsString());
		
	}
	
	
	public static Page getVoice(WebClient webClient) throws Exception {
		String url="http://shop.xj.189.cn:8081/xjwt_webapp/billQueryController/getmonthQuery.do";		
				String telephone="099118999891510";
				String queryDate="201709";
				String markType="0";
				String deviceType="25";
		String data= "[{\"telephone\":\""+telephone+"\",\"queryDate\":\""+queryDate+"\",\"markType\":\""+markType+"\",\"deviceType\":\""+deviceType+"\"}";	 	
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Host", "shop.xj.189.cn:8081");
		webRequest.setAdditionalHeader("Origin", "http://shop.xj.189.cn:8081");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Referer", "http://shop.xj.189.cn:8081/xjwt_webapp/pcapp/service/billing/historybill/HistoryBill.jsp?fastcode=10000335&cityCode=xj"); 
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
		webRequest.setAdditionalHeader("Content-Type", "application/json"); 
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");			
		webRequest.setRequestBody(data);
		Page page = webClient.getPage(webRequest);
		System.out.println(page.getWebResponse().getContentAsString());
		return page;
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception, IOException {
		WebRequest webRequest;
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage searchPage = webClient.getPage(webRequest);
			return searchPage;

	}
	
	public Page gettmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setCharset(Charset.forName("UTF-8"));
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			
			return null;
		}

	}
}
