package app.service;

import java.net.URL;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.dalian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.dalian")
public class LoginAndGetService extends HousingBasicService{
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public  HtmlPage loginByCO_BRANDED_CARD(WebClient webClient,String url, String name, String password) throws Exception {
		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='imgid']");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "1004");

		String url1 = "https://bg.gjj.dl.gov.cn/person/logon/logon.act?usercode="+name+"&passwd="+password+"&validimage="+valicodeStr+"&_remember=";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		//System.out.println(searchPage.asXml());
		if(searchPage.getWebResponse().getContentAsString().contains("验证码错误")){
			tracer.addTag(valicodeStr, "图片验证码错误，触发retry机制");
			throw new RuntimeException("请输入正确的附加码");
		}else{
			return searchPage;
		}
		
	}
}
