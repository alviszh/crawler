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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.enshi")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.enshi")
public class LoginAndGetService extends HousingBasicService{
	public  Page loginByIDNUM(WebClient webClient, String num, String hosingFundNumber) throws Exception {
		String url1 = "http://223.75.50.75/ESGJJAPI/Handler.ashx?v_sfzh="+num+"&v_grzh="+hosingFundNumber+"&callback=callback&_=1523518373536";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
//		webRequest.setAdditionalHeader("Accept", "*/*");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Host", "223.75.50.75");
//		webRequest.setAdditionalHeader("Referer", "http://gjj.enshi.gov.cn/grzhcx.shtml?v_sfzh=&v_grzh=&x=26&y=11");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		String requestBody = "v_sfzh="+num+"&v_grzh="+hosingFundNumber+"&callback=callback&_=1523518373536";
//		webRequest.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest);
	    System.out.println(searchPage.getWebResponse().getContentAsString());
		return searchPage;
	}
}
