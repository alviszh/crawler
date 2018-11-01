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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.luzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.luzhou")
public class LoginAndGetService extends HousingBasicService{
	public  Page loginByIDNUM(WebClient webClient,String url, String name, String password) throws Exception {
		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='imgVERIFYCODE']/div[2]/div/img");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "1004");
		
		String urls = "http://www.sclzgjj.com/ispobs/loginController/grLoginVerify.do?YHLX=1&USERCODE="+name+"&USERPWD="+password+"&VERIFYCODE="+valicodeStr+"";
		WebRequest webRequest = new WebRequest(new URL(urls), HttpMethod.POST);
		Page searchPage = webClient.getPage(webRequest);

		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		return searchPage;
	}
}
