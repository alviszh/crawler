package app.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import app.bean.WebParamHousing;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.anqing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.anqing")
public class LoginAndGetService extends HousingBasicService {

	@Value("${anqing.ip}")
	String anqingip;
	// 根据个人号登录
	public WebParamHousing<?> login(WebClient webClient, String name, String password) throws Exception {
		WebParamHousing<?> webParamHousing = new WebParamHousing<>();
		String url = "http://"
				+ anqingip.trim()
				+ ":8888/Default.aspx";
		HtmlPage htmlPage = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);
		HtmlForm form = htmlPage.getFirstByXPath("/html/body/table[3]/tbody/tr[1]/td[2]/form");

		HtmlInput nameInput = form.getFirstByXPath("//*[@id='loginName']");
		HtmlInput passInput = form.getFirstByXPath("//*[@id='loginPwd']");

		nameInput.setValueAttribute(name.trim());
		passInput.setValueAttribute(password.trim());

		HtmlLabel valiCodeImg = htmlPage.getFirstByXPath("//*[@id='autoRandom']");

		String valicodeStr = valiCodeImg.asText();

		HtmlInput verfiyInput = form.getFirstByXPath("//*[@id='marketCaptchaCode']");

		verfiyInput.setValueAttribute(valicodeStr);

		HtmlElement loginButton = (HtmlElement) form.getFirstByXPath("//*[@id='button']");
		htmlPage = loginButton.click();

		webParamHousing.setPage(htmlPage);
		webParamHousing.setStatusCode(htmlPage.getWebResponse().getStatusCode());
		webParamHousing.setWebClient(webClient);
		tracer.addTag("公积金登录   安庆login", "<xmp>" + htmlPage.getWebResponse().getContentAsString() + "</xmp>");

		return webParamHousing;

	}

	// 根据个人号登录
	public AsyncResult<WebParamHousing<?>> getAccountDetails(WebClient webClient, String pagenum) throws Exception {
		WebParamHousing<?> webParamHousing = new WebParamHousing<>();
		String url = "http://"
				+ anqingip.trim()
				+ ":8888/details.aspx?page="
				+ pagenum.trim();
		Page page =  LoginAndGetCommon.getHtml(url, webClient);
		webParamHousing.setPage(page);
		webParamHousing.setStatusCode(page.getWebResponse().getStatusCode());
		webParamHousing.setWebClient(webClient);
		tracer.addTag("公积金登录   安庆 getAccountDetails", "<xmp>" + page.getWebResponse().getContentAsString() + "</xmp>");

		return new AsyncResult<WebParamHousing<?>>(webParamHousing);

	}

}
