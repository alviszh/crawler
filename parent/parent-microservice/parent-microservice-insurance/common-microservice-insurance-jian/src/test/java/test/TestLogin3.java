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

public class TestLogin3 {

	public static void main(String[] args) throws Exception {
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String loginUrl = "http://www.ja12333.cn/jayb/";		
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "www.ja12333.cn");
		HtmlPage page = webClient.getPage(webRequest);
		
 		System.out.println(page.asXml());
		
		HtmlImage image = page.getFirstByXPath("//img[contains(@src,'/jayb/login/getRandomPictrueAction')]");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);


//		HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("//input[@id='loginname']");
//		
//	
//		
//		HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
//		HtmlTextInput code = (HtmlTextInput)page.getFirstByXPath("//input[@id='validateCode']");
//		HtmlElement button = (HtmlElement)page.getFirstByXPath("//img[@id='btnlogin']");
//		System.out.println("username   :"+username.asXml());
//		System.out.println("password   :"+password.asXml());
//		System.out.println("code   :"+code.asXml());
//		System.out.println("button   :"+button.asXml());
//		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		
//	
//		username.setText("36080002340708");
//		password.setText("140428");
//    	webClient.getOptions().setJavaScriptEnabled(true);
//		//您输入的密码有误  密码错误
//		//出现错误！您的身份证信息未通过系统校验，请携带相关身份证明至中心服务大厅进行修改  账号错误
//		//出现错误！您输入的验证码有误，请核实！   验证码错误
//		//公积金查询  正确
//		HtmlPage loggedPage = button.click();
//		System.out.println("登陆成功后的页面  ====》》"+loggedPage.getWebResponse().getContentAsString());
//		Set<Cookie> cookies = loggedPage.getWebClient().getCookieManager().getCookies();
//		for(Cookie cookie:cookies){
//			System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());
//		}
		
		  String loginUrl4 = "http://www.ja12333.cn/jayb/login/doAction.action";
			
		   String requestBody4="loginname=36080002340708&password=140428&validateCode="+input;
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);
			webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Host", "www.ja12333.cn");
			webRequest4.setAdditionalHeader("Referer", "http://www.ja12333.cn");
			webRequest4.setRequestBody(requestBody4);
			HtmlPage page4 = webClient.getPage(webRequest4);
			System.out.println(page4.getWebResponse().getContentAsString());
//	
//			 
        	  String loginUrl5 = "http://www.ja12333.cn/jayb/web/f50020103/getAc11PageAction.action";
				
        	String requestBody="nd0000=2017&pageSize=100&start=0";
			WebRequest webRequest5 = new WebRequest(new URL(loginUrl5), HttpMethod.POST);
			webRequest5.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			webRequest5.setAdditionalHeader("Host", "www.ja12333.cn");
			webRequest5.setAdditionalHeader("Referer", "http://www.ja12333.cn");
			webRequest5.setRequestBody(requestBody);
			HtmlPage page5 = webClient.getPage(webRequest5);
			System.out.println(page5.getWebResponse().getContentAsString());
	//		{"currentPage":"1","dir":"desc","pageSize":"100","result":[{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"88.58","aac125":"141.728","aae002":"201712","aae140m":"基本医疗保险","baa005":"265.74","bac003":"0.0","bae023":"2017-12-21 17:16:24"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201712","aae140m":"生育保险","baa005":"22.145","bac003":"0.0","bae023":"2017-12-21 17:16:24"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201712","aae140m":"工伤保险","baa005":"8.858","bac003":"0.0","bae023":"2017-12-21 17:16:24"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"88.58","aac125":"141.728","aae002":"201711","aae140m":"基本医疗保险","baa005":"265.74","bac003":"0.0","bae023":"2017-11-22 17:02:06"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201711","aae140m":"生育保险","baa005":"22.145","bac003":"0.0","bae023":"2017-11-22 17:02:06"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201711","aae140m":"工伤保险","baa005":"8.858","bac003":"0.0","bae023":"2017-11-22 17:02:06"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"88.58","aac125":"141.728","aae002":"201710","aae140m":"基本医疗保险","baa005":"265.74","bac003":"0.0","bae023":"2017-10-26 09:48:50"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201710","aae140m":"生育保险","baa005":"22.145","bac003":"0.0","bae023":"2017-10-26 09:48:50"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201710","aae140m":"工伤保险","baa005":"8.858","bac003":"0.0","bae023":"2017-10-26 09:48:50"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"88.58","aac125":"141.728","aae002":"201709","aae140m":"基本医疗保险","baa005":"265.74","bac003":"0.0","bae023":"2017-09-27 11:14:38"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201709","aae140m":"生育保险","baa005":"22.145","bac003":"0.0","bae023":"2017-09-27 11:14:38"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201709","aae140m":"工伤保险","baa005":"8.858","bac003":"0.0","bae023":"2017-09-27 11:14:38"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"88.58","aac125":"141.728","aae002":"201708","aae140m":"基本医疗保险","baa005":"265.74","bac003":"0.0","bae023":"2017-08-22 09:25:59"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201708","aae140m":"生育保险","baa005":"22.145","bac003":"0.0","bae023":"2017-08-22 09:25:59"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201708","aae140m":"工伤保险","baa005":"8.858","bac003":"0.0","bae023":"2017-08-22 09:25:59"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"88.58","aac125":"141.728","aae002":"201707","aae140m":"基本医疗保险","baa005":"265.74","bac003":"0.0","bae023":"2017-07-14 16:26:39"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201707","aae140m":"生育保险","baa005":"22.145","bac003":"0.0","bae023":"2017-07-14 16:26:39"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201707","aae140m":"工伤保险","baa005":"8.858","bac003":"0.0","bae023":"2017-07-14 16:26:39"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"88.58","aac125":"141.728","aae002":"201706","aae140m":"基本医疗保险","baa005":"265.74","bac003":"0.0","bae023":"2017-06-15 16:09:04"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201706","aae140m":"生育保险","baa005":"22.145","bac003":"0.0","bae023":"2017-06-15 16:09:04"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201706","aae140m":"工伤保险","baa005":"8.858","bac003":"0.0","bae023":"2017-06-15 16:09:04"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"88.58","aac125":"141.728","aae002":"201705","aae140m":"基本医疗保险","baa005":"265.74","bac003":"0.0","bae023":"2017-05-18 16:50:45"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201705","aae140m":"生育保险","baa005":"22.145","bac003":"0.0","bae023":"2017-05-18 16:50:45"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201705","aae140m":"工伤保险","baa005":"8.858","bac003":"0.0","bae023":"2017-05-18 16:50:45"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"88.58","aac125":"141.728","aae002":"201704","aae140m":"基本医疗保险","baa005":"265.74","bac003":"0.0","bae023":"2017-04-25 09:02:49"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201704","aae140m":"生育保险","baa005":"22.145","bac003":"0.0","bae023":"2017-04-25 09:02:49"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201704","aae140m":"工伤保险","baa005":"8.858","bac003":"0.0","bae023":"2017-04-25 09:02:49"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"88.58","aac125":"141.728","aae002":"201703","aae140m":"基本医疗保险","baa005":"265.74","bac003":"0.0","bae023":"2017-03-15 11:49:02"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201703","aae140m":"生育保险","baa005":"22.145","bac003":"0.0","bae023":"2017-03-15 11:49:02"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201703","aae140m":"工伤保险","baa005":"8.858","bac003":"0.0","bae023":"2017-03-15 11:49:02"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"88.58","aac125":"141.728","aae002":"201702","aae140m":"基本医疗保险","baa005":"265.74","bac003":"0.0","bae023":"2017-02-23 09:15:07"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201702","aae140m":"生育保险","baa005":"22.145","bac003":"0.0","bae023":"2017-02-23 09:15:07"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201702","aae140m":"工伤保险","baa005":"8.858","bac003":"0.0","bae023":"2017-02-23 09:15:07"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"88.58","aac125":"141.728","aae002":"201701","aae140m":"基本医疗保险","baa005":"265.74","bac003":"0.0","bae023":"2017-01-19 16:19:56"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201701","aae140m":"工伤保险","baa005":"8.858","bac003":"0.0","bae023":"2017-01-19 16:19:56"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201701","aae140m":"生育保险","baa005":"22.145","bac003":"0.0","bae023":"2017-01-19 16:19:56"},{"aab120":"4429.0","aac001":"0131326272","aac003":"肖志丹","aac123":"0.0","aac125":"0.0","aae002":"201701","aae140m":"大病医疗保险","baa005":"160.0","bac003":"0.0","bae023":"2017-01-19 16:19:56"}],"sort":"aae002","start":"0","totalPages":"1","totalProperty":"37"}

		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
