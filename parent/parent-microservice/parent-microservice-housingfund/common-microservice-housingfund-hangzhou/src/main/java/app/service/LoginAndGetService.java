package app.service;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.hangzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.hangzhou")
public class LoginAndGetService   extends HousingBasicService{

	 // 根据身份证号登录
	public  HtmlPage loginByIDNUM(WebClient webClient,String url, String name, String password) throws Exception {
		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='IMG1']");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4004");

		String url1 = "http://www.hzgjj.gov.cn:8080/WebAccounts/userLogin.do?"
				+ "cust_type=2&flag=1&user_type=1&cust_no="+name+"&password="+password+"&validate_code="+valicodeStr+"";
		HtmlPage htmlPage1 = (HtmlPage) loginAndGetCommon.getHtml(url1, webClient);
		System.out.println(htmlPage1.asXml());
		
		return htmlPage1;
	}
	// 市民邮箱
	public  HtmlPage loginByCITIZEN_EMAIL(WebClient webClient,String url, String name, String password) throws Exception {
        HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='IMG1']");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4004");

		String url1 = "http://www.hzgjj.gov.cn:8080/WebAccounts/userLogin.do?"
				+ "cust_type=2&flag=1&user_type=4&cust_no="+name+"&password="+password+"&validate_code="+valicodeStr+"";
		HtmlPage htmlPage1 = (HtmlPage) loginAndGetCommon.getHtml(url1, webClient);
		System.out.println(htmlPage1.asXml());
		
		return htmlPage1;
	}

	// 根据个人登记号登录,用户名
	public  HtmlPage loginByACCOUNT_NUM(WebClient webClient,String url, String name, String password) throws Exception {
		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);

		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='IMG1']");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4004");

		String url1 = "http://www.hzgjj.gov.cn:8080/WebAccounts/userLogin.do?"
				+ "cust_type=2&flag=1&user_type=3&cust_no="+name+"&password="+password+"&validate_code="+valicodeStr+"";
		HtmlPage htmlPage1 = (HtmlPage) loginAndGetCommon.getHtml(url1, webClient);
		System.out.println(htmlPage1.asXml());
		
		return htmlPage1;
	}
}
