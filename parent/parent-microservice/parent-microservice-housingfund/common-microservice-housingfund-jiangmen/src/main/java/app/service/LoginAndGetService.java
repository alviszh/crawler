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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.jiangmen")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.jiangmen")
public class LoginAndGetService extends HousingBasicService{
	//中国大陆居民身份证
	public  Page loginByIDNUM(WebClient webClient,String url, String name, String password) throws Exception {
		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='imgCode']");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4005");

		String url1 = "http://zjj.jiangmen.gov.cn/gjj/gjjsearch.aspx";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "zjj.jiangmen.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://zjj.jiangmen.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://zjj.jiangmen.gov.cn/gjj/gjjsearch.aspx");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String requestBody = "__VIEWSTATE=G%2FIj7dPR%2BL0OqJHbJcBoWx9LqLoo0nNxlpg05BoG2OjJwKokDKuX%2Bv5rfFib1XsDULjV6QSdEAE0xy6UJ%2BsNDSMQ6%2FLN3yooB0k5ZdJJVI8mnPZyVfiDnouEqZ1%2FRprJrCYxcAg9k09dZsqH2P2qKYE%2Bu5I%3D&__EVENTVALIDATION=NHiEBp5qHXrZtyC1BS%2BJFXQjncNkkLYVzsP6L8d101aB%2FFNS%2F79PW%2B2w3F2j73lhbvKoNVrIiBuXY6iusYg90geezDLXWii6FI57Y9COO2lL1qE%2FxwyuHcbZBRLK%2F2o%2FZ2u3OYF9c%2BJR1Rwv%2BEMHuihVFBvJ7vHfbVet7Rkg8L4Y9peYt06oiuzqNTgY4nSKkZecQg6I5QwjwspK3YuGVN%2BeHRtCONZW4Unx489gtFpn5e8yiESNoPjZ7khQzz0bZA%2Ba3OK68juEb%2BGl6hEVscOdw%2FAVcTPcB7RZkQ%3D%3D&idCardNum="+name+"&fundAccount="+password+"&idType=1&txtCode="+valicodeStr+"&SearchBnt=%E6%9F%A5%E8%AF%A2";
		webRequest.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		return searchPage;
	}
	
	//中国大陆军官证
	public  Page loginByOFFICER_CARD(WebClient webClient,String url, String name, String password) throws Exception {
        HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='imgCode']");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4005");

		String url1 = "http://zjj.jiangmen.gov.cn/gjj/gjjsearch.aspx";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "zjj.jiangmen.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://zjj.jiangmen.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://zjj.jiangmen.gov.cn/gjj/gjjsearch.aspx");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String requestBody = "__VIEWSTATE=G%2FIj7dPR%2BL0OqJHbJcBoWx9LqLoo0nNxlpg05BoG2OjJwKokDKuX%2Bv5rfFib1XsDULjV6QSdEAE0xy6UJ%2BsNDSMQ6%2FLN3yooB0k5ZdJJVI8mnPZyVfiDnouEqZ1%2FRprJrCYxcAg9k09dZsqH2P2qKYE%2Bu5I%3D&__EVENTVALIDATION=NHiEBp5qHXrZtyC1BS%2BJFXQjncNkkLYVzsP6L8d101aB%2FFNS%2F79PW%2B2w3F2j73lhbvKoNVrIiBuXY6iusYg90geezDLXWii6FI57Y9COO2lL1qE%2FxwyuHcbZBRLK%2F2o%2FZ2u3OYF9c%2BJR1Rwv%2BEMHuihVFBvJ7vHfbVet7Rkg8L4Y9peYt06oiuzqNTgY4nSKkZecQg6I5QwjwspK3YuGVN%2BeHRtCONZW4Unx489gtFpn5e8yiESNoPjZ7khQzz0bZA%2Ba3OK68juEb%2BGl6hEVscOdw%2FAVcTPcB7RZkQ%3D%3D&idCardNum="+name+"&fundAccount="+password+"&idType=2&txtCode="+valicodeStr+"&SearchBnt=%E6%9F%A5%E8%AF%A2";
		webRequest.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		return searchPage;
	}
	
	//港澳居民身份证
	public  Page loginByACCOUNT_NUM(WebClient webClient,String url, String name, String password) throws Exception {
        HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='imgCode']");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4005");

		String url1 = "http://zjj.jiangmen.gov.cn/gjj/gjjsearch.aspx";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "zjj.jiangmen.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://zjj.jiangmen.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://zjj.jiangmen.gov.cn/gjj/gjjsearch.aspx");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String requestBody = "__VIEWSTATE=G%2FIj7dPR%2BL0OqJHbJcBoWx9LqLoo0nNxlpg05BoG2OjJwKokDKuX%2Bv5rfFib1XsDULjV6QSdEAE0xy6UJ%2BsNDSMQ6%2FLN3yooB0k5ZdJJVI8mnPZyVfiDnouEqZ1%2FRprJrCYxcAg9k09dZsqH2P2qKYE%2Bu5I%3D&__EVENTVALIDATION=NHiEBp5qHXrZtyC1BS%2BJFXQjncNkkLYVzsP6L8d101aB%2FFNS%2F79PW%2B2w3F2j73lhbvKoNVrIiBuXY6iusYg90geezDLXWii6FI57Y9COO2lL1qE%2FxwyuHcbZBRLK%2F2o%2FZ2u3OYF9c%2BJR1Rwv%2BEMHuihVFBvJ7vHfbVet7Rkg8L4Y9peYt06oiuzqNTgY4nSKkZecQg6I5QwjwspK3YuGVN%2BeHRtCONZW4Unx489gtFpn5e8yiESNoPjZ7khQzz0bZA%2Ba3OK68juEb%2BGl6hEVscOdw%2FAVcTPcB7RZkQ%3D%3D&idCardNum="+name+"&fundAccount="+password+"&idType=3&txtCode="+valicodeStr+"&SearchBnt=%E6%9F%A5%E8%AF%A2";
		webRequest.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		return searchPage;
	}
	
	//港澳居民回乡证
	public  Page loginByPASSPORT(WebClient webClient,String url, String name, String password) throws Exception {
        HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='imgCode']");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4005");

		String url1 = "http://zjj.jiangmen.gov.cn/gjj/gjjsearch.aspx";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "zjj.jiangmen.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://zjj.jiangmen.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://zjj.jiangmen.gov.cn/gjj/gjjsearch.aspx");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String requestBody = "__VIEWSTATE=G%2FIj7dPR%2BL0OqJHbJcBoWx9LqLoo0nNxlpg05BoG2OjJwKokDKuX%2Bv5rfFib1XsDULjV6QSdEAE0xy6UJ%2BsNDSMQ6%2FLN3yooB0k5ZdJJVI8mnPZyVfiDnouEqZ1%2FRprJrCYxcAg9k09dZsqH2P2qKYE%2Bu5I%3D&__EVENTVALIDATION=NHiEBp5qHXrZtyC1BS%2BJFXQjncNkkLYVzsP6L8d101aB%2FFNS%2F79PW%2B2w3F2j73lhbvKoNVrIiBuXY6iusYg90geezDLXWii6FI57Y9COO2lL1qE%2FxwyuHcbZBRLK%2F2o%2FZ2u3OYF9c%2BJR1Rwv%2BEMHuihVFBvJ7vHfbVet7Rkg8L4Y9peYt06oiuzqNTgY4nSKkZecQg6I5QwjwspK3YuGVN%2BeHRtCONZW4Unx489gtFpn5e8yiESNoPjZ7khQzz0bZA%2Ba3OK68juEb%2BGl6hEVscOdw%2FAVcTPcB7RZkQ%3D%3D&idCardNum="+name+"&fundAccount="+password+"&idType=4&txtCode="+valicodeStr+"&SearchBnt=%E6%9F%A5%E8%AF%A2";
		webRequest.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		return searchPage;
	}

}
