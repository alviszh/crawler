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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecom;
import app.commontracerlog.TracerLog;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.sichuan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.sichuan")
public class TelecomUnitCommonService {

	@Autowired 
	private TracerLog tracer;

	public WebParamTelecom<MessageLogin> login(MessageLogin messageLogin) throws Exception{
		WebParamTelecom<MessageLogin> webParamTelecom = new WebParamTelecom<>();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/web/login";
		HtmlPage html = getHtml(url,webClient);
		HtmlTextInput username =(HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText(messageLogin.getName());
		passwordInput.setText(messageLogin.getPassword());

		HtmlPage htmlpage = button.click();
		String asXml = htmlpage.asXml();
		System.out.println(asXml);
		if(htmlpage.asXml().indexOf("登录失败") != -1){
			webParamTelecom.setPage(null);
			return webParamTelecom;
		}
		webParamTelecom.setPage(htmlpage);
		return webParamTelecom;
	}
	public HtmlPage getHtml(String url, WebClient webClient) throws Exception{

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);

		return page;

	}
}
