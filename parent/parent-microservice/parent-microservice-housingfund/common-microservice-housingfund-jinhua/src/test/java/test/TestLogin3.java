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

public class TestLogin3 {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String loginUrl= "http://218.90.206.76:8080/jeesite/a/login";
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);	
    	webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "218.90.206.76:8080");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		HtmlPage page2 = webClient.getPage(webRequest);
		webClient.getOptions().setJavaScriptEnabled(true);  
		webClient.waitForBackgroundJavaScript(20000);
	  
		System.out.println(page2.asXml());
		HtmlImage image =page2.getFirstByXPath("//img[@class='mid validateCode']");
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
		    String loginUrl4 = "http://218.90.206.76:8080/jeesite/a/login";		                     
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
			String requestBody="username=321202199009030928&password=030928&validateCode="+input;
			webRequest4.setAdditionalHeader("Accept", "*/*");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest4.setAdditionalHeader("Host", "218.90.206.76:8080");
			webRequest4.setAdditionalHeader("Origin", "http://218.90.206.76:8080");
			webRequest4.setAdditionalHeader("Referer", "http://218.90.206.76:8080/jeesite/a/login");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest4.setRequestBody(requestBody);
			Page page4 = webClient.getPage(webRequest4);
			System.out.println(page4.getWebResponse().getContentAsString());
			//{"code":0,"message":"登入成功","url":"/"}
			 String url="http://218.90.206.76:8080/jeesite/a/sys/user/index";
             HtmlPage  jbxxPage= getHtml(url,webClient);
			 System.out.println(jbxxPage.asXml());
				
			
			
		    String loginUrl5 = "http://218.90.206.76:8080/jeesite/a/sys/user/jcnote";		                     
			WebRequest webRequest5= new WebRequest(new URL(loginUrl5), HttpMethod.POST);	
			String requestBody2="pageNo=1&pageSize=30&orderBy=&spcode=103517768&year=2017";
			webRequest5.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest5.setAdditionalHeader("Host", "218.90.206.76:8080");
			webRequest5.setAdditionalHeader("Origin", "http://218.90.206.76:8080");
			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest5.setRequestBody(requestBody2);
			Page page5 = webClient.getPage(webRequest5);
			System.out.println(page5.getWebResponse().getContentAsString());
			//{"code":0,"message":null,"dataset":{"total":1,"rows":[{"ROWNUM_":1,"GRZH":"029529557","DWZH":"0029667","DWMC":"徐州市外事服务有限责任公司宿迁分公司（8%）","SEX":"男","SJH":null,"DWDH":null,"KHRQ":"2014.12.19","TQLAST":"2018.01.15","BZ":null,"XM":"徐恺强","ZJHM":"321321198801050033","GRZHZT":"11","GRZHZTMC":"正常汇缴","HJNY":"2018.01","SFDK":"否","GRZHYE":6828.18,"YJE":408,"GZJS":2550,"ZGYJE":204,"DWYJE":204,"LMKYH":null,"LMKH":null,"DWJJL":8,"ZGJJL":8,"ORGCODE":"100","ORGNAME":"市本部","SFDY":"否","SFDJ":"否","SFAYDK":"否","JTZZ":"江苏省宿迁市宿城区洋北镇友爱村高宅组21号","BTBL":0,"QJNY":"无","JZNY":"2018.01","JBYH":"宿迁建行","DWDZ":"宿迁市软件与服务外包产业园水杉大道1号知浩楼527室"}]}}
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
