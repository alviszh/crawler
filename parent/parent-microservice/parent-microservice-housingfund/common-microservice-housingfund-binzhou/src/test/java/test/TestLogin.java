package test;

import java.io.File;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String loginUrl= "http://www.bzgjj.cn/index.php";
          
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);	
    	webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "www.bzgjj.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.bzgjj.cn/userlogout.php");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		HtmlPage page2 = webClient.getPage(webRequest);
		webClient.getOptions().setJavaScriptEnabled(true);  
		webClient.waitForBackgroundJavaScript(20000);
	  
		System.out.println(page2.asXml());
		HtmlImage image =page2.getFirstByXPath("//img[@name='imageField']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
	

//		HtmlTextInput username = (HtmlTextInput)page2.getFirstByXPath("//input[@name='username']");
//		HtmlPasswordInput password = (HtmlPasswordInput)page2.getFirstByXPath("//input[@name='password']");	
//		HtmlTextInput code = (HtmlTextInput)page2.getFirstByXPath("//input[@name='checkcode']");
//		
//		HtmlTextInput button = (HtmlTextInput)page2.getFirstByXPath("//input[@name='Submit']");
		
//		System.out.println("username   :"+username.asXml());
//		System.out.println("password   :"+password.asXml());	
//		System.out.println("button   :"+button.asXml());
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
//		code.setText(input);
//		username.setText("zhaotongtong");
//		password.setText("ztt123456");
//				
//		Page loggedPage = button.click();
//		Thread.sleep(1500);
//		System.out.println("登陆成功后的页面  ====》》"+loggedPage.getWebResponse().getContentAsString());
////
//		Set<Cookie> cookies = loggedPage.getWebClient().getCookieManager().getCookies();
//		for(Cookie cookie:cookies){
//			System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());
//		}
//		
		    String loginUrl4 = "http://www.bzgjj.cn/userlogin.php";		                     
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
			String requestBody="username=zhaotongtong&password=ztt123456&checkcode="+input+"&url=index.php&action=check&Submit.x=36&Submit.y=18";
			webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest4.setAdditionalHeader("Host", "www.bzgjj.cn");
			webRequest4.setAdditionalHeader("Origin", "http://www.bzgjj.cn");
			webRequest4.setAdditionalHeader("Referer", "http://www.bzgjj.cn/index.php");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest4.setRequestBody(requestBody);
			HtmlPage page4 = webClient.getPage(webRequest4);
			System.out.println(page4.asXml());
	
			HtmlPage page=getHtml("http://www.bzgjj.cn/usermain2.php",webClient);
			System.out.println(page.asXml());		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
