package app.logintest;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.JOptionPane;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;

/**
 * 改版之后的登录测试类
 * @author sln
 *
 */
public class LoginTest {
	public static void main(String[] args) throws Exception {
		String url="http://old.jlgjj.gov.cn/website/website/trans/newperlogin.html";
		WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		WebClient webClient = WebCrawler.getInstance().getWebClient();	
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage hPage = webClient.getPage(webRequest);
		if(null!=hPage){
			String loginHtml=hPage.asXml();
			System.out.println("登录页面html为："+loginHtml);
			HtmlImage image = hPage.getFirstByXPath("//img[@id='image']");
			String imageName = "111.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 	
			url="http://old.jlgjj.gov.cn/website/website/trans/gjjquery.do?className=TRC020001"; 
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
			inputValue=URLEncoder.encode(inputValue, "utf-8");
			String requestBody="time=1524817674888"
					+ "&accnum="
					+ "&certinum=220202198605305140"
					+ "&password=530102&mark=2"
					+ "&txt=1&verify="+inputValue+"";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			Thread.sleep(1500);
			String html = page.getWebResponse().getContentAsString();
			System.out.println(html);
			String cookieString = CommonUnit.transcookieToJson(webClient);  //存储cookie
	 		System.out.println("存储起来的cookie是："+cookieString);
	 		String accnum = splitData(cookieString,"gjjaccnum\",\"value\":\"","\"}");
	 		System.out.println("登录成功从cookie中获取的公积金账号是："+accnum);
	 		if(html.contains("success")){
	 			System.out.println("登录成功");
	 		}else{
	 			if(html.contains("msg")){
	 				String errMsg = JSONObject.fromObject(html).getString("msg");
	 				System.out.println("获取的登录错误的信息是："+errMsg);
	 			}
	 		}
		}
	}
	public static String splitData(String str, String strStart, String strEnd) {  
		int i = str.indexOf(strStart); 
		int j = str.indexOf(strEnd, i); 
		String tempStr=str.substring(i+strStart.length(), j); 
        return tempStr;  
	}
}
