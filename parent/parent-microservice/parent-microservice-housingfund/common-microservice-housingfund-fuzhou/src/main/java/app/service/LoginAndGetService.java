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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.fuzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.fuzhou")
public class LoginAndGetService extends HousingBasicService{

	public  Page loginIDNUM(WebClient webClient,String url, String name, String password) throws Exception {
		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='imgCode']");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "1006");

		String url1 = "http://www.fzgjj.com.cn:7002/fznetface//login.do?loginid="+name+"&pwd="+password+"&logintype=2&codeimg="+valicodeStr+"&ip=123.126.87.168";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		return searchPage;
	}

}
