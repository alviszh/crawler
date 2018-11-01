package app.service.common;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.CrawlerStatusMobileService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.shandong")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.shandong")
public class TelecomUnitCommomService {

	public static final Logger log = LoggerFactory.getLogger(TelecomUnitCommomService.class);


	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	
	@Autowired
	private TracerLog tracer;
	
	/*public MobileLogin login(MobileLogin mobileLogin) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/login";
		HtmlPage html = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText(mobileLogin.getName());
		passwordInput.setText(mobileLogin.getPassword());

		HtmlPage htmlpage = button.click();
		if (htmlpage.asXml().indexOf("登录失败") != -1) {
			mobileLogin.setStatusCodeRec(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE);
			return mobileLogin;
		}
		mobileLogin.setHtmlpage(htmlpage);
		return mobileLogin;
	}*/


	private void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	public HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}