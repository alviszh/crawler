package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParam;
/***
 *   关键字查询
 **/
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.honesty.judicialwrit" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.honesty.judicialwrit" })
public class JudicialWritCrawlerService {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	public boolean getValidateCode(HttpProxyBean httpProxyBean ,int i){
		try {
		//	WebParam webParam = new WebParam();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://wenshu.court.gov.cn/Html_Pages/VisitRemind.html";
			HtmlPage page = getHtml(url, webClient, httpProxyBean);

			HtmlTextInput txtValidateCode = (HtmlTextInput) page.getElementById("txtValidateCode");
			HtmlImage validateCode = (HtmlImage)page.getElementByName("validateCode");
			HtmlButtonInput elementById = (HtmlButtonInput) page.getFirstByXPath("//input[@type='button']");
			String verifycode = chaoJiYingOcrService.getVerifycode(validateCode, "4004");
			txtValidateCode.setText(verifycode);
			elementById.click();
			String msg = WebCrawler.getAlertMsg();
			if(msg.equals("验证码错误!")){
				if(i<5){
					getValidateCode(httpProxyBean,i++);
				}
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url,HttpProxyBean httpProxyBean) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		if (httpProxyBean!=null) {
			webRequest.setProxyHost(httpProxyBean.getIp());
			webRequest.setProxyPort(Integer.parseInt(httpProxyBean.getPort()));
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
	public static HtmlPage getHtml(String url, WebClient webClient,HttpProxyBean httpProxyBean) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		if (httpProxyBean!=null) {
			webRequest.setProxyHost(httpProxyBean.getIp());
			webRequest.setProxyPort(Integer.parseInt(httpProxyBean.getPort()));
		}
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

}
