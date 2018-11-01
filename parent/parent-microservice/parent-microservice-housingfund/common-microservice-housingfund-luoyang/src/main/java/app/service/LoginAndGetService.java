package app.service;

import java.net.URL;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.luoyang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.luoyang")
public class LoginAndGetService   extends HousingBasicService{

	 // 根据身份证号登录
	public  HtmlPage loginByIDNUM(WebClient webClient, String name, String password) throws Exception {
		String url = "http://www.lyzfgjj.com/login.do?r=0.7171368220006062&username="+name+"&password="+password+"&loginType=4&vertype=1";
		//WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage htmlpage = webClient.getPage(webRequest);
		String html = htmlpage.getWebResponse().getContentAsString();
		System.out.println(html);
		
		return htmlpage;
	}
	// 手机号
	public  HtmlPage loginByPHONE_NUM(WebClient webClient, String name, String password) throws Exception {
		String url = "http://www.lyzfgjj.com/login.do?r=0.7171368220006062&username="+name+"&password="+password+"&loginType=10&vertype=1";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage htmlpage = webClient.getPage(webRequest);
		String html = htmlpage.getWebResponse().getContentAsString();
		System.out.println(html);
		
		return htmlpage;
	}

	// 根据个人登记号登录,用户名
	public  HtmlPage loginByACCOUNT_NUM(WebClient webClient, String name, String password) throws Exception {
		String url = "http://www.lyzfgjj.com/login.do?r=0.7171368220006062&username="+name+"&password="+password+"&loginType=3&vertype=1";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage htmlpage = webClient.getPage(webRequest);
		String html = htmlpage.getWebResponse().getContentAsString();
		System.out.println(html);
		
		return htmlpage;
	}
}
