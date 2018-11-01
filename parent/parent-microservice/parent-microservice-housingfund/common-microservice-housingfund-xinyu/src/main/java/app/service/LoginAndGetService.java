package app.service;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import app.service.common.HousingBasicService;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xinyu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xinyu")
public class LoginAndGetService extends HousingBasicService{
	//市直身份证
	public  HtmlPage loginByIDNUM(WebClient webClient, String name, String password) throws Exception {
//		String keyWord = URLEncoder.encode(password, "UTF-8");
//		System.out.println(keyWord);
		String url = "http://www.xygjj.gov.cn/c102396/cxiframe.shtml";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage page = webClient.getPage(webRequest);
		
		HtmlElement button = (HtmlElement)page.getFirstByXPath("//*[@id='radiobutton2']");
		page =  button.click();
		HtmlTextInput name1 = page.getFirstByXPath("//*[@id='Idcard']");
		HtmlTextInput password1 = page.getFirstByXPath("//input[@id='Name']");
		name1.setText(name);
		password1.setText(password);
		HtmlElement button1 = (HtmlElement)page.getFirstByXPath("//input[@value='开始查询 ']");
		HtmlPage htmlPages =  button1.click();
		System.out.println(htmlPages.getWebResponse().getContentAsString());
		
		return htmlPages;
	}
	
	// 市直公积金账号
	public  HtmlPage loginByACCOUNT_NUM(WebClient webClient, String name, String password) throws Exception {
		String url = "http://www.xygjj.gov.cn/c102396/cxiframe.shtml";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage page = webClient.getPage(webRequest);
		
//		HtmlElement button = (HtmlElement)page.getFirstByXPath("//*[@id='radiobutton2']");
//		page =  button.click();
		HtmlTextInput name1 = page.getFirstByXPath("//*[@id='Num']");
		HtmlTextInput password1 = page.getFirstByXPath("//input[@id='Name']");
		name1.setText(name);
		password1.setText(password);
		HtmlElement button1 = (HtmlElement)page.getFirstByXPath("//input[@value='开始查询 ']");
		HtmlPage htmlPages =  button1.click();
		System.out.println(htmlPages.getWebResponse().getContentAsString());
		return htmlPages;
	}
	
	//分宜县身份证
	public  HtmlPage loginByIDNUM1(WebClient webClient, String name, String password) throws Exception {
		String url = "http://www.xygjj.gov.cn/c102396/cxiframe.shtml";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage page = webClient.getPage(webRequest);
		
		HtmlElement button2 = (HtmlElement)page.getFirstByXPath("//*[@id='channelid']/option[2]");
		page =  button2.click();
		HtmlElement button = (HtmlElement)page.getFirstByXPath("//*[@id='radiobutton2']");
		page =  button.click();
		HtmlTextInput name1 = page.getFirstByXPath("//*[@id='Idcard']");
		HtmlTextInput password1 = page.getFirstByXPath("//input[@id='Name']");
		name1.setText(name);
		password1.setText(password);
		HtmlElement button1 = (HtmlElement)page.getFirstByXPath("//input[@value='开始查询 ']");
		HtmlPage htmlPages =  button1.click();
		System.out.println(htmlPages.getWebResponse().getContentAsString());
		
		return htmlPages;
	}
	
	// 分宜县公积金账号
	public  HtmlPage loginByACCOUNT_NUM1(WebClient webClient, String name, String password) throws Exception {
		String url = "http://www.xygjj.gov.cn/c102396/cxiframe.shtml";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage page = webClient.getPage(webRequest);
		
		HtmlElement button = (HtmlElement)page.getFirstByXPath("//*[@id='channelid']/option[2]");
		page =  button.click();
		HtmlTextInput name1 = page.getFirstByXPath("//*[@id='Num']");
		HtmlTextInput password1 = page.getFirstByXPath("//input[@id='Name']");
		name1.setText(name);
		password1.setText(password);
		HtmlElement button1 = (HtmlElement)page.getFirstByXPath("//input[@value='开始查询 ']");
		HtmlPage htmlPages =  button1.click();
		System.out.println(htmlPages.getWebResponse().getContentAsString());
		return htmlPages;
	}	
}
