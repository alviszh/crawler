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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.baotou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.baotou")
public class LoginAndGetService extends HousingBasicService{
//	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
//	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
//	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public  Page loginIDNUM(WebClient webClient,String url, String name, String password) throws Exception {
		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@class='send']/img");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "1004");

		String url1 = "http://www.btgjj.com/tools/submit_ajax.ashx?action=user_login&site=main";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "www.btgjj.com");
		webRequest.setAdditionalHeader("Origin", "http://www.btgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.btgjj.com/login.html");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		String requestBody = "txtUserName="+name+"&txtPassword="+password+"&txtCode="+valicodeStr+"";
		webRequest.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest);
//		String url2 = "http://www.btgjj.com/tools/submit_ajax.ashx?action=user_gjj_person&site=main";
//		WebRequest webRequest1 = new WebRequest(new URL(url2), HttpMethod.POST);
//		webRequest1.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//		webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest1.setAdditionalHeader("Cache-Control", "max-age=0");
//		webRequest1.setAdditionalHeader("Connection", "keep-alive");
//		webRequest1.setAdditionalHeader("Host", "www.btgjj.com");
//		webRequest1.setAdditionalHeader("Origin", "http://www.btgjj.com");
//		webRequest1.setAdditionalHeader("Referer", "http://www.btgjj.com/user/gjj/person.html");
//		webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		webRequest1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//		Page searchPage1 = webClient.getPage(webRequest1);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		//您输入的验证码与系统的不一致！
//		tracer.addTag("parser.crawler.login.page", "<xmp>"+searchPage.getWebResponse().getContentAsString()+"</xmp>");
//		if(searchPage.getWebResponse().getContentAsString().contains("您输入的验证码与系统的不一致")){
//			tracer.addTag(valicodeStr, "图片验证码错误，触发retry机制");
//			throw new RuntimeException("请输入正确的附加码");
//		}
		return searchPage;
	}
}
