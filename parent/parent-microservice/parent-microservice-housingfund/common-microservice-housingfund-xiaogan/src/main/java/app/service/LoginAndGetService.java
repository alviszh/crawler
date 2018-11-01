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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xiaogan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xiaogan")
public class LoginAndGetService extends HousingBasicService{
	public  Page loginByIDNUM(WebClient webClient,String url, String num, String password) throws Exception {
//		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
////		System.out.println(htmlPage.getWebResponse().getContentAsString());
//		HtmlTextInput num1 = htmlPage.getFirstByXPath("//*[@id='wsbsGjjcx_cardno']");
//		HtmlPasswordInput password1 = htmlPage.getFirstByXPath("//*[@id='wsbsGjjcx_password']");
////		HtmlElement button = htmlPage.getFirstByXPath("//img[@alt='看不清楚请点击刷新验证码']");
//		HtmlTextInput code = htmlPage.getFirstByXPath("//*[@id='wsbsGjjcx_code']");
////////		System.out.println(htmlPage.getWebResponse().getContentAsString());
////		HtmlPage htmlPage1 = button.click();
//////		System.out.println(htmlPage1.getWebResponse().getContentAsString());
//		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//img[@id='mode_wsbsGjjcx_verificationCode']");
//////		System.out.println(valiCodeImg);
//		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4005");
//		num1.setText(num);
//		password1.setText(password);
//		code.setText(valicodeStr);
//		HtmlElement button1 = htmlPage.getFirstByXPath("//*[@id='wsbsGjjcx_button']");
//		HtmlPage htmlPage2 = button1.click();
//		System.out.println(htmlPage2.getWebResponse().getContentAsString());
		
//		String url1 = "http://www.xggjj.com/action/gjjcx";
//		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
//		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		webRequest.setAdditionalHeader("Host", "www.xggjj.com");
//		webRequest.setAdditionalHeader("Origin", "http://www.xggjj.com");
//		webRequest.setAdditionalHeader("Referer", "http://www.xggjj.com/page/wzzs/wzzs_center/wzzs_wzqtzs_center_sy_2.jsp?wsbsGjjcx=wsbsGjjcx");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//		String requestBody = "gjjcxCordno="+num+"&gjjcxPassword="+password+"&gjjcxCode="+valicodeStr+"&exeType=wsbsGjjcxSelect";
//		webRequest.setRequestBody(requestBody);
//		Page searchPage = webClient.getPage(webRequest);
//	    System.out.println(searchPage.getWebResponse().getContentAsString());
	    String url2 = "http://www.xggjj.com/action/gjjcx";
	    WebRequest webRequest1 = new WebRequest(new URL(url2), HttpMethod.POST);
	    webRequest1.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
	    webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
	    webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
	    webRequest1.setAdditionalHeader("Cache-Control", "max-age=0");
	    webRequest1.setAdditionalHeader("Connection", "keep-alive");
	    webRequest1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	    webRequest1.setAdditionalHeader("Host", "www.xggjj.com");
	    webRequest1.setAdditionalHeader("Origin", "http://www.xggjj.com");
	    webRequest1.setAdditionalHeader("Referer", "http://www.xggjj.com/page/wzzs/wzzs_center_mode/grcxindex.jsp");
	    webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
	    webRequest1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		String requestBody1 = "grid="+num+"&exeType=selGrjbxx";
		webRequest1.setRequestBody(requestBody1);
		Page searchPage1 = webClient.getPage(webRequest1);
		System.out.println(searchPage1.getWebResponse().getContentAsString());
//		return searchPage1;
		return searchPage1;
	}

}
