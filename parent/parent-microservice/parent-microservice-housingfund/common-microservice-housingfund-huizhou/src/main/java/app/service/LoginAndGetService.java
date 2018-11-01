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

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.huizhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.huizhou")
public class LoginAndGetService extends HousingBasicService{
	public  Page loginByIDNUM(WebClient webClient,String username, String countNumber, String num ,String phone) throws Exception {
		String url1 = "http://oa.hzgjj.cn:8083/vgsGjj-port/psnquery/psnqueryPsninfoAction.shtml;jsessionid=3BA02DA0F9DE1656EB8BCE497F4CF44C";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "oa.hzgjj.cn:8083");
		webRequest.setAdditionalHeader("Origin", "http://oa.hzgjj.cn:8083");
		webRequest.setAdditionalHeader("Referer", "http://oa.hzgjj.cn:8083/vgsGjj-port/psnquery/psnqueryPsninfoAction.shtml");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String requestBody = "method=login&perName="+URLEncoder.encode(username, "utf-8")+"&perAccount="+countNumber+"&perNo="+num+"&perMobile="+phone+"";
		webRequest.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest);
	    System.out.println(searchPage.getWebResponse().getContentAsString());
		return searchPage;
	}
}
