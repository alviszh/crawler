package app.service;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.ValidationLoginDataObject;
import app.bean.ValidationLoginRoot;
import app.bean.WebParamTelecom;
import app.service.common.LoginAndGetCommon;
import app.service.common.TelecomBasicService;
import app.unit.TeleComCommonUnit;


@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hainan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hainan")
public class TelecomLoginService extends TelecomBasicService {

	public WebParamTelecom<?> loginHaiNan(MessageLogin messageLogin, TaskMobile taskMobile,int i) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParamTelecom<?> webParamTelecom = new WebParamTelecom<>();

		if (taskMobile.getCookies() == null || taskMobile.getCookies().isEmpty()) {

		} else {
			webClient = taskMobile.getClient(taskMobile.getCookies());
		}

		try {
			InetAddress ia = InetAddress.getLocalHost();

			String localname = ia.getHostName();
			String localip = ia.getHostAddress();
			tracerLog.addTag("本机名称是",  localname);
			tracerLog.addTag("本机的ip是 ：", localip);
			System.out.println("本机名称是：" + localname);
			System.out.println("本机的ip是 ：" + localip);
			String url = "https://uam.ct10000.com/ct10000uam/login?service=http%3A%2F%2Fwww.189.cn%3A80%2Flogin%2Fuam.do&returnURL=1&register=register2.0&UserIp="
					+ localip.trim();
			HtmlPage htmlPage = (HtmlPage) TeleComCommonUnit.getHtml(url, webClient);
			HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='checkImg']");
			String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "2007");
			tracerLog.addTag("验证码111",  valicodeStr);
			
			System.out.println("=====验证码111======================" + valicodeStr);
			if (checkChineseCharacter(valicodeStr)) {
				valicodeStr = (returnChineseCharacter(valicodeStr));
				tracerLog.addTag("验证码222", valicodeStr);
				System.out.println("=====验证码222======================" + valicodeStr);
			}

			Document doc = Jsoup.parse(htmlPage.asXml());
			String lt = doc.select("input[name=lt]").attr("value");

			url = "https://uam.ct10000.com/ct10000uam/login?service=http%3A%2F%2Fwww.189.cn%3A80%2Flogin%2Fuam.do&returnURL=1&register=register2.0"
					+ "&UserIp=" + localip.trim();
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("forbidpass", "null"));
			paramsList.add(new NameValuePair("forbidaccounts", "null"));
			paramsList.add(new NameValuePair("authtype", "c2000004"));
			paramsList.add(new NameValuePair("customFileld02", "22"));
			paramsList.add(new NameValuePair("areaname", "海南"));
			paramsList.add(new NameValuePair("username", messageLogin.getName().trim()));
			paramsList.add(new NameValuePair("customFileld01", "1"));
			paramsList.add(new NameValuePair("password", messageLogin.getPassword().trim()));
			paramsList.add(new NameValuePair("randomId", valicodeStr.trim()));
			paramsList.add(new NameValuePair("c2000004RmbMe", "on"));
			paramsList.add(new NameValuePair("lt", lt));
			paramsList.add(new NameValuePair("_eventId", "submit"));
			paramsList.add(new NameValuePair("open_no", "1"));
			Page page = TeleComCommonUnit.gethtmlPost(webClient, paramsList, url);
			url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02091576";
			// webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
			webParamTelecom.setWebClient(webClient);
			url = "http://www.189.cn/login/index.do";

			page = LoginAndGetCommon.getHtml(url, webClient);
			tracerLog.addTag("电信登录：", "<xmp>" + page.getWebResponse().getContentAsString() + "</xmp>");
			if (page.getWebResponse().getContentAsString().indexOf("登录失败") != -1) {
				webParamTelecom.setStatusCodeRec(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE);
				webParamTelecom.setWebClient(null);

				return webParamTelecom;
			}
			ValidationLoginDataObject dataObject = ValidationLogin(webClient);
			tracerLog.addTag("电信登录：", "<xmp>" + dataObject.toString() + "</xmp>");
			if (dataObject.getNickName() == null) {
				webParamTelecom.setStatusCodeRec(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE);
				webParamTelecom.setWebClient(null);
				i++;
				if(i<3){
					return loginHaiNan(messageLogin, taskMobile, i);
				}
				return webParamTelecom;
			}
			if (page.getWebResponse().getContentAsString().indexOf("密码过于简单") != -1) {
				webParamTelecom.setStatusCodeRec(StatusCodeRec.MESSAGE_LOGIN_ERROR_Seven);
				webParamTelecom.setWebClient(null);
				return webParamTelecom;
			}

			return webParamTelecom;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return webParamTelecom;

	}

	private ValidationLoginDataObject ValidationLogin(WebClient webClient) {

		try {
			String url = "http://www.189.cn/login/index.do";
			Page page = TeleComCommonUnit.getHtml(url, webClient);

			System.out.println("*************************************** index.do");
			System.out.println(page.getWebResponse().getContentAsString());

			ValidationLoginRoot jsonObject = gs.fromJson(page.getWebResponse().getContentAsString(),
					ValidationLoginRoot.class);

			return jsonObject.getDataObject();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String regEx = "[\u4e00-\u9fa5]+";

	public boolean checkChineseCharacter(String str) {
		if (str.getBytes().length == str.length()) {
			return false;
		}

		return true;
	}

	public String returnChineseCharacter(String str) {
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		String chiResult = "";
		while (m.find()) {
			chiResult += m.group();
		}
		return chiResult;
	}
}
