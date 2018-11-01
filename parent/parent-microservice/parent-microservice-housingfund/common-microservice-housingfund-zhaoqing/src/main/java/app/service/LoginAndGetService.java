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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.zhaoqing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.zhaoqing")
public class LoginAndGetService extends HousingBasicService{

	public  HtmlPage loginByIDNUM(WebClient webClient,String url, String name, String password) throws Exception {
		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
		HtmlElement button = htmlPage.getFirstByXPath("//*[@id='tabs-1']/form/div[3]/div/img");
		htmlPage = button.click();
		
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='tabs-1']/form/div[3]/div/img");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4004");
		
		String url1 = "http://121.10.236.70:9080/wsyyt/per.login?accnum=20227906153&hi_value=0&perpwd=170013&vericode="+valicodeStr+"";
		
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
	
		HtmlPage searchPage = webClient.getPage(webRequest);
		//System.out.println(searchPage.asXml());
		
		return searchPage;
	}

}
