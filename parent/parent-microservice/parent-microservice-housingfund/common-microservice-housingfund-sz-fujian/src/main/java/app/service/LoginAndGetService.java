package app.service;

import java.net.URL;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.sz.fujian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.sz.fujian")
public class LoginAndGetService extends HousingBasicService{

	public  Page loginIDNUM(WebClient webClient,String url, String name, String password) throws Exception {
		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
		HtmlElement botton1 = htmlPage.getFirstByXPath("//*[@id='type']/option[1]");
		HtmlPage htmlPage1 = botton1.click();
		HtmlTextInput name1 = htmlPage1.getFirstByXPath("//*[@id='username']");
		HtmlPasswordInput password1 = htmlPage1.getFirstByXPath("//*[@id='password']");
		name1.setText(name);
		password1.setText(password);
		
		HtmlElement botton = htmlPage1.getFirstByXPath("//*[@id='personalLoginbtn']");
		
		Page searchPage = botton.click();
		String url1 ="http://www.fjszgjj.com/gjj/accountInfo/queryInfogrxx";
		WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);

		HtmlPage searchPage2 = webClient.getPage(webRequest1);
//		System.out.println(searchPage2.getWebResponse().getContentAsString());
		
		return searchPage2;
	}
	
	public  Page loginCO_BRANDED_CARD(WebClient webClient,String url, String name, String password) throws Exception {
		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
        HtmlElement botton1 = htmlPage.getFirstByXPath("//*[@id='type']/option[2]");
		HtmlPage htmlPage1 = botton1.click();
		HtmlTextInput name1 = htmlPage1.getFirstByXPath("//*[@id='username']");
		HtmlPasswordInput password1 = htmlPage1.getFirstByXPath("//*[@id='password']");
		name1.setText(name);
		password1.setText(password);
		
		HtmlElement botton = htmlPage1.getFirstByXPath("//*[@id='personalLoginbtn']");
		
		Page searchPage = botton.click();
		String url1 ="http://www.fjszgjj.com/gjj/accountInfo/queryInfogrxx";
		WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);

		HtmlPage searchPage2 = webClient.getPage(webRequest1);
//		System.out.println(searchPage2.getWebResponse().getContentAsString());
		
		return searchPage2;
	}

}
