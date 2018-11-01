package app.service.common;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wuhan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wuhan")
public class HousingfundCommonUnit {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	public void login(MessageLogin mobileLogin) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		String url = "https://whgjj.hkbchina.com/portal/pc/htmls/LoginContent/loginContent.html";
		 
		HtmlPage page = getHtml(url, webClient);
		
		HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("[//input[@id='LoginId']]");//帐号
		HtmlTextInput img = page.getFirstByXPath("[//input[@id='_vTokenName']]");//输入验证码
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='_tokenImg']");//图片验证码
		
		String img1 = chaoJiYingOcrService.getVerifycode(image, "1902");
		System.out.println(img1);
		username.setText(mobileLogin.getName());
		
	}
	public HtmlPage getHtml(String url, WebClient webClient) throws Exception{

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);

		return page;

	}
}
