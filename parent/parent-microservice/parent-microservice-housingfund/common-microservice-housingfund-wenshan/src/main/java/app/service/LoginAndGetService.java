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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wenshan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wenshan")
public class LoginAndGetService extends HousingBasicService{
	public  HtmlPage loginByIDNUM(WebClient webClient,String url, String name, String password) throws Exception {
		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='person_pic']");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "1004");
		System.out.println(valicodeStr);
		String urls = "https://www.wsgjj.com/WSYYT/auth";
		WebRequest webRequest = new WebRequest(new URL(urls), HttpMethod.POST);
		
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "www.wsgjj.com");
		webRequest.setAdditionalHeader("Origin", "https://www.wsgjj.com");
		webRequest.setAdditionalHeader("Referer", "https://www.wsgjj.com/WSYYT/index");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		String requestBody = "loginType=person&idNumber="+name+"&password="+password+"&verifyCode="+valicodeStr+"";
		webRequest.setRequestBody(requestBody);
		
		
		HtmlPage searchPage = webClient.getPage(webRequest);

//		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		return searchPage;
	}
}
