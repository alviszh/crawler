package app.service.common;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecom;
import app.service.ChaoJiYingOcrService;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.guangdong")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.guangdong")
public class TelecomUnitCommonService {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	public WebParamTelecom login(MessageLogin messageLogin) throws Exception{
		WebParamTelecom webParamTelecom = new WebParamTelecom<>();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://gd.189.cn/common/newLogin/newLogin.htm";
		HtmlPage page = getHtml(url, webClient);
		Thread.sleep(2000);
		HtmlTextInput account = (HtmlTextInput)page.getFirstByXPath("//input[@id='account']");
		HtmlSelect mobilePassword = (HtmlSelect)page.getFirstByXPath("//select[@id='mobilePassword']");
		HtmlPasswordInput password =(HtmlPasswordInput) page.getFirstByXPath("//input[@id='password']");
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='loginCodeImage']");
		HtmlTextInput loginCodeRand =(HtmlTextInput) page.getFirstByXPath("//input[@id='loginCodeRand']");
		String img = chaoJiYingOcrService.getVerifycode(image, "1902");
		Thread.sleep(1000);
		HtmlElement button = (HtmlElement)page.getFirstByXPath("//input[@id='t_login']");
		Thread.sleep(1000);
		
		
		account.setText(messageLogin.getName());
		password.setText(messageLogin.getPassword());
		loginCodeRand.setText(img);
		mobilePassword.setTextContent("客户密码");
		
		HtmlPage htmlpage = button.click();
		Thread.sleep(1000);
		String url1 = "https://gd.189.cn/common/getIsLogin.jsp?";
		HtmlPage page2 = getHtml(url1, webClient);
		
		String s = page2.getWebResponse().getContentAsString();
		int statusCode = page2.getWebResponse().getStatusCode();
		System.out.println(s);
		System.out.println(statusCode);
		if(s.equals("\r\n\r\nloginInfo=0")){
			System.out.println("登陆失败");
			return null;
		}
		webParamTelecom.setWebClient(webClient);
		webParamTelecom.setPage(htmlpage);
		return webParamTelecom;
		
	}

	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception{

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);

		return page;

	}
	
}
