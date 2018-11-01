package app.service.common;

import java.net.URL;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.bean.ValidationLoginDataObject;
import app.bean.ValidationLoginRoot;
import app.bean.WebParamTelecom;
import app.crawler.telecom.htmlparse.TelecomParseCommon;
import app.unit.TeleComCommonUnit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.common")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.common")
public class TelecomUnitCommomService extends TelecomBasicService  {

	public static final Logger log = LoggerFactory.getLogger(TelecomUnitCommomService.class);

	
	public WebParamTelecom<?> login(MessageLogin messageLogin) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/web/login";
		HtmlPage htmlpage = (HtmlPage) TeleComCommonUnit.getHtml(url, webClient);
		
		
		HtmlTextInput username = (HtmlTextInput) htmlpage.getFirstByXPath("//input[@id='txtAccount']");
		HtmlElement htmlElement = (HtmlTextInput)htmlpage.getFirstByXPath("//input[@id='txtShowPwd']");
		htmlElement.click();
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) htmlpage.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) htmlpage.getFirstByXPath("//a[@id='loginbtn']");
		username.setText(messageLogin.getName().trim());
		passwordInput.setText(messageLogin.getPassword().trim());

		htmlpage = button.click();
		
		WebParamTelecom<?> webParamTelecom =  new WebParamTelecom<>();;

		if (htmlpage.asXml().indexOf("登录失败") != -1) {

			HtmlImage valiCodeImg = htmlpage.getFirstByXPath("//*[@id='imgCaptcha']");
			if ((valiCodeImg.isDisplayed())) {
				htmlpage = loginByImage(htmlpage, messageLogin);
				if (htmlpage.asXml().indexOf("验证码不正确") != -1) {
					htmlpage = loginByImage(htmlpage, messageLogin);
					if (htmlpage.asXml().indexOf("验证码不正确") != -1) {
						htmlpage = loginByImage(htmlpage, messageLogin);
					}

				}
				if (htmlpage.asXml().indexOf("登录失败") != -1) {
					 webParamTelecom = TelecomParseCommon.loginerror_Parser(htmlpage.asXml());
					 webParamTelecom.setStatusCodeEnum(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE);
					 webParamTelecom.setStatusCodeRec(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE);
					return webParamTelecom;
				}
			} else {
				 webParamTelecom = TelecomParseCommon.loginerror_Parser(htmlpage.asXml());
				 webParamTelecom.setStatusCodeEnum(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE);
				 webParamTelecom.setStatusCodeRec(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE);
				return webParamTelecom;
			}
		}
		
		
		url = "http://www.189.cn/dqmh/my189/initMy189home.do";
		LoginAndGetCommon.getHtml(url, webClient);
		//System.out.println(page.asXml());
		ValidationLoginDataObject dataObject = ValidationLogin(webClient);
		tracerLog.addTag("电信登录：", "<xmp>" + dataObject.toString() + "</xmp>");
	
	
		if (dataObject.getNickName() == null) {
			
			 webParamTelecom = TelecomParseCommon.loginerror_Parser(htmlpage.asXml());
			
			 webParamTelecom.setStatusCodeEnum(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE);

			 webParamTelecom.setStatusCodeRec(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE);
			return webParamTelecom;
		}
	
		webParamTelecom.setWebClient(webClient);
		webParamTelecom.setPage(htmlpage);
		return webParamTelecom;
	}

	private HtmlPage loginByImage(HtmlPage html, MessageLogin messageLogin) throws Exception {
		HtmlImage valiCodeImg = html.getFirstByXPath("//*[@id='imgCaptcha']");
		System.out.println("Element is  displayed!");
		tracerLog.addTag("电信登录 验证码", "<xmp>" + "Element is  displayed!" + "</xmp>");
		String valicodeStr = null;
		try{
			valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");

		}catch(Exception e){
			e.printStackTrace();
			valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");

		}
		tracerLog.addTag("电信登录 验证码内容", "<xmp>" + valicodeStr + "</xmp>");

		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlTextInput valicodeStrinput = (HtmlTextInput) html.getFirstByXPath("//*[@id='txtCaptcha']");

		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");

		username.setText(messageLogin.getName().trim());
		passwordInput.setText(messageLogin.getPassword().trim());
		valicodeStrinput.setText(valicodeStr.toLowerCase().trim());

		HtmlPage htmlpage2 = button.click();
		return htmlpage2;
	}

	private ValidationLoginDataObject ValidationLogin(WebClient webClient) {

		try {
			String url = "http://www.189.cn/login/index.do";
			Page page = getHtml(url, webClient);

			ValidationLoginRoot jsonObject = gs.fromJson(page.getWebResponse().getContentAsString(),
					ValidationLoginRoot.class);

			return jsonObject.getDataObject();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "www.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.189.cn/html/login/right.html");
		webRequest.setAdditionalHeader("Origin", "http://www.czgjj.com");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}