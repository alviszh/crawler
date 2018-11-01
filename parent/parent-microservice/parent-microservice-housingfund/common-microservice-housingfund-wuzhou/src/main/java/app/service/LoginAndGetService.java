package app.service;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wuzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wuzhou")
public class LoginAndGetService extends HousingBasicService{

	public  HtmlPage loginByIDNUM(WebClient webClient,String url, String name, String password) throws Exception {
		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
		HtmlTextInput name1 = htmlPage.getFirstByXPath("//*[@id='sfno']");
		HtmlPasswordInput password1 = htmlPage.getFirstByXPath("//*[@id='pwd']");
		HtmlTextInput code = htmlPage.getFirstByXPath("//*[@id='pSN1']");
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//table/tbody/tr[4]/td/img");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4004");
		
		name1.setText(name);
		password1.setText(password);
		code.setText(valicodeStr);
		HtmlElement button = htmlPage.getFirstByXPath("//table/tbody/tr[9]/td/input");
		HtmlPage htmlPage1 = button.click();
		//System.out.println(htmlPage1.asXml());
		
		return htmlPage1;
	}
}
