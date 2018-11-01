package app.service;

import java.net.URL;
import java.net.URLEncoder;

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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.jian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.jian")
public class LoginAndGetService extends HousingBasicService{
	
	public  Page loginByIDNUM(WebClient webClient,String username, String num, String password ,String countNumber) throws Exception {
//		String url1 = "http://218.64.84.34:8089/gongjijin/loginpage.action;jsessionid=B62C4F15332DCAD117BC08C01759E51A";
//		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest.setAdditionalHeader("Host", "218.64.84.34:8089");
//		webRequest.setAdditionalHeader("Origin", "http://218.64.84.34:8089");
//		webRequest.setAdditionalHeader("Referer", "http://218.64.84.34:8089/gongjijin/dkxxlist.action");
//		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		String requestBody = "vwebPerson.per_name="+URLEncoder.encode(username, "utf-8")+"&vwebPerson.per_idcard="+num+"&vwebPerson.cust_code="+password+"&vwebPerson.per_code="+countNumber+"";
//		webRequest.setRequestBody(requestBody);
//		Page searchPage = webClient.getPage(webRequest);
////	System.out.println(searchPage.getWebResponse().getContentAsString());
		String url1 = "http://218.64.84.34:8089/gongjijin/dkxxlist.action";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
		HtmlPage htmlPage1 = webClient.getPage(webRequest);
		HtmlTextInput username1 = (HtmlTextInput)htmlPage1.getFirstByXPath("//*[@id='per_name']");
		HtmlTextInput num1 = (HtmlTextInput)htmlPage1.getFirstByXPath("//*[@id='per_idcard']");
		HtmlTextInput password1 = (HtmlTextInput)htmlPage1.getFirstByXPath("//*[@id='cust_code']");
		HtmlTextInput countNumber1 = (HtmlTextInput)htmlPage1.getFirstByXPath("//*[@id='per_code']");
		username1.setText(username);
		num1.setText(num);
		password1.setText(password);
		countNumber1.setText(countNumber);
		HtmlElement button = (HtmlElement)htmlPage1.getFirstByXPath("//*[@id='form_0']");
		HtmlPage htmlPage = button.click();
		return htmlPage;
	}
}
