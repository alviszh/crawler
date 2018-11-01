package org.common.microservice.housingfund.yiwu;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;

@Component
public class AppTest {
	
	
	public static void main(String[] args) throws Exception {
		String url = "http://122.226.76.37/Account/Logon?returnUrl=%2Fpsl";
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		HtmlPage page = webClient.getPage(webRequest);
		HtmlImage image = page.getFirstByXPath("//img[@onclick='getPic(this)']");
		String imageName = "111.jpg"; 
		File file = new File("D:\\img\\" + imageName); 
		image.saveAs(file);
		String inputuserjymtemp = JOptionPane.showInputDialog("请输入验证码……"); 
		WebRequest  requestSettings = new WebRequest(new URL("http://122.226.76.37/Account/Logon?returnUrl=%2Fpsl"), HttpMethod.POST); 
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("UserName", "330782199504073925"));
		requestSettings.getRequestParameters().add(new NameValuePair("Password", "417418"));
		requestSettings.getRequestParameters().add(new NameValuePair("Racha", inputuserjymtemp));
		requestSettings.setCharset(Charset.forName("UTF-8"));
//		requestSettings.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		requestSettings.setAdditionalHeader("Accept-Encoding","gzip, deflate");
//		requestSettings.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.9");
//		requestSettings.setAdditionalHeader("Cache-Control","max-age=0");
//		requestSettings.setAdditionalHeader("Connection","keep-alive");
//		requestSettings.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded");
//		requestSettings.setAdditionalHeader("Host","www.ztgjj.com");
//		requestSettings.setAdditionalHeader("Origin","http://www.ztgjj.com");
//		requestSettings.setAdditionalHeader("Referer","http://www.ztgjj.com/gjjcq.php");
//		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests","1");
//		requestSettings.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
		HtmlPage page2 = (HtmlPage)webClient.getPage(requestSettings); 
		System.out.println(page2.getWebResponse().getContentAsString());
	}

}