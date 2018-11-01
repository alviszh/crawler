package app.test;

import java.io.File;
import java.net.URL;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
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

public class Teststr {
	
	 
	public static void main(String[] args) throws Exception {
		
//		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		String url = "http://www.bjrbj.gov.cn/csibiz/indinfo/login.jsp";
		HtmlPage html = getHtml(url,webClient);
		System.out.println("搜索页源代码================================================>>"+html.asXml());
		
		HtmlImage image = html.getFirstByXPath("//img[@id='indsafecode']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
		
		HtmlTextInput username = (HtmlTextInput)html.getFirstByXPath("//input[@id='i_username']"); 
		HtmlPasswordInput password = (HtmlPasswordInput)html.getFirstByXPath("//input[@id='i_password']");
		HtmlTextInput code = (HtmlTextInput)html.getFirstByXPath("//input[@id='i_safecode']");
		HtmlTextInput sms = html.getFirstByXPath("//input[@calss='required']");
		HtmlElement button = (HtmlElement)html.getFirstByXPath("//input[@type='image']");
		
		username.setText("130406199110233017");
		password.setText("Zhang1314");
//		String aaa = "121746668009";
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		code.setText(input);
		
		String sendSmsUrl = "http://www.bjrbj.gov.cn/csibiz/indinfo/passwordSetAction!getTelSafeCode?idCode=130406199110233017&logPass=Zhang1314&safeCode="+code;
		HtmlPage smsPage = getHtml(sendSmsUrl,webClient);
		System.out.println(smsPage.asXml());
		
		@SuppressWarnings("resource")
		Scanner scanner1 = new Scanner(System.in);
		String input1 = scanner1.next();
		sms.setText(input1);
		
		
		
		HtmlPage loggedPage = button.click();
		System.out.println("点击后的页面"+loggedPage.getWebResponse().getContentAsString());
		String bbb = "http://www.bjrbj.gov.cn/csibiz/indinfo/search/ind/indPaySearchAction!oldage?searchYear=2017&time=1496732955382";
		WebRequest webRequest = new WebRequest(new URL(bbb), HttpMethod.GET);
//		addCookies(webClient,cookies);
		HtmlPage searchPage = webClient.getPage(webRequest);
		System.out.println("2017年社保缴费======================>>"+searchPage.asXml());
		
				
	
	}
	
	
	
	public static HtmlPage getHtml(String url,WebClient webClient) throws Exception{
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
		
	}
	
	@SuppressWarnings("unused")
	private static void addCookies(WebClient webClient, Set<Cookie> cookies) {
		for (Cookie cookie : cookies) {
			if (StringUtils.isEmpty(cookie.getName())) {
				System.out.println("详情："+cookie.getName()+":"+cookie.getValue());
				continue;
			}
			webClient.getCookieManager().addCookie(cookie);
		}
	}


}
