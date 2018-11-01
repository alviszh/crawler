package app.service;

import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.exceptiondetail.EUtils;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.shanxi1")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.shanxi1")
public class TelecomUnitService {

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private EUtils eutils;

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	/**
	 * 实名认证
	 * @param mobileLogin
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public HtmlPage verification(TaskMobile taskMobile) throws Exception {

		WebClient webClient = addcookie(taskMobile.getCookies());
		HtmlPage htmlpage = null;
//		Page page = verification(webClient, taskMobile);
//		if (null != page) {
//			
//			String htmltext = page.getWebResponse().getContentAsString();
//
//			if (htmltext.contains("用户未登录") || htmltext.contains("未知错误")) {
				for (int i = 0; i < 3; i++) {
					tracer.addTag("TelecomUnitService.login 身份验证" + i, taskMobile.getTaskid() + "---次数:" + i);
					htmlpage = verificationName(webClient, taskMobile);
					
					if (null != htmlpage) {
						HtmlSpan loginSpan = htmlpage.querySelector("#zmzqrmessSpan");
						if (null == loginSpan) {
							break;
						} else {
							String asText = loginSpan.asText();
							tracer.addTag("TelecomUnitService.login 身份验证asText" + i, taskMobile.getTaskid() + "---返回值:" + asText);
							if (asText.contains("验证通过")) {
								break;
							} else if (asText.contains("验证码错误") || asText.contains("请填写4位验证码")) {
								
							} else if (asText.contains("请填写正确的姓名") || asText.contains("请填写正确的的证件号码")
									|| asText.contains("信息验证失败")) {
								break;
							}
						}
					}
				}
//			}
//		}
		return htmlpage;
	}

	/**
	 * 实名验证
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	private HtmlPage verificationName(WebClient webClient, TaskMobile taskMobile) throws Exception {

		try {
			webClient = addcookie(taskMobile.getCookies());
			
			String verificationurl = "http://www.189.cn/service/my189/costQuery/4G-flow.html?fastcode=01841174&cityCode=sx";
			HtmlPage searchPage = getHtml(verificationurl, webClient, taskMobile);
			webClient.waitForBackgroundJavaScript(30000);

			HtmlTextInput verificationusername = (HtmlTextInput) searchPage.getFirstByXPath("//input[@id='smzxmImput']");
			HtmlTextInput smzzjhmImput = (HtmlTextInput) searchPage.getFirstByXPath("//input[@id='smzzjhmImput']");
			HtmlTextInput smzyzmImput = (HtmlTextInput) searchPage.getFirstByXPath("//input[@id='smzyzmImput']");

			HtmlImage image = searchPage.getFirstByXPath("//img[@id='smzqrImg']");

			String code = "";
			try {
				code = chaoJiYingOcrService.getVerifycode(image, "1902");
			} catch (Exception e) {
				tracer.addTag("ERROR:TelecomUnitService.verificationName.code.ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
			}
			tracer.addTag("TelecomUnitService.verificationName.code", taskMobile.getTaskid() + "---超级鹰解析code:" + code);

			/**
			 * 姓名
			 */
			if (verificationusername == null) {
				tracer.addTag("TelecomUnitService.verificationName",
						taskMobile.getTaskid() + "username input text can not found");
				throw new Exception("username input text can not found");
			} else {
				verificationusername.reset();
				verificationusername.setText(taskMobile.getBasicUser().getName().trim());
				tracer.addTag("TelecomUnitService.verificationName",
						taskMobile.getTaskid() + "---getName:" + taskMobile.getBasicUser().getName());
			}

			/**
			 * 身份证
			 */
			if (smzzjhmImput == null) {
				tracer.addTag("TelecomUnitService.verificationName",
						taskMobile.getTaskid() + "smzzjhmImput input text can not found");
				throw new Exception("smzzjhmImput input text can not found");
			} else {
				smzzjhmImput.reset();
				smzzjhmImput.setText(taskMobile.getBasicUser().getIdnum().trim());
				tracer.addTag("TelecomUnitService.verificationName",
						taskMobile.getTaskid() + "---getIdnum:" + taskMobile.getBasicUser().getIdnum());
			}

			/**
			 * 验证码
			 */
			if (smzyzmImput == null) {
				tracer.addTag("TelecomUnitService.verificationName",
						taskMobile.getTaskid() + "username input text can not found");
				throw new Exception("username input text can not found");
			} else {
				smzyzmImput.reset();
				smzyzmImput.setText(code);
				tracer.addTag("TelecomUnitService.verificationName", taskMobile.getTaskid() + "---code:" + code);
			}

			HtmlAnchor loginButton = searchPage.querySelector("#smzqryzA");

			searchPage = loginButton.click();
			
			Thread.sleep(3000);

			return searchPage;

		} catch (Exception e) {
			tracer.addTag("TelecomUnitService.verificationName---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return null;
	}

	private Page verification(WebClient webClient, TaskMobile taskMobile) {

		try {

			String url = "http://www.189.cn/bss/billing/brandinfo.do?&callback=jQuery111202329755171438339_1503469426456&aim=1&_=1503469426457";

			tracer.addTag("TelecomUnitService.getPage---url:" + url + " ", taskMobile.getTaskid());

			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

			Page searchPage = webClient.getPage(webRequest);

			int statusCode = searchPage.getWebResponse().getStatusCode();
			tracer.addTag("TelecomUnitService.getPage.statusCode:" + statusCode, "---taskid:" + taskMobile.getTaskid());

			if (200 == statusCode) {
				String html = searchPage.getWebResponse().getContentAsString();
				tracer.addTag("TelecomUnitService.getPage---url:" + url + "---taskid:" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				return searchPage;
			}

		} catch (Exception e) {
			tracer.addTag("TelecomUnitService.verification---taskId---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return null;
	}

	public WebClient addcookie(String cookieString) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookieString);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}
	

	public HtmlPage getHtml(String url, WebClient webClient, TaskMobile taskMobile) throws Exception {
		tracer.addTag("TelecomUnitService.getHtml---url:" + url + " ", taskMobile.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("TelecomUnitService.getHtml.statusCode:" + statusCode, "---taskid:" + taskMobile.getTaskid());
		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("TelecomUnitService.getHtml---url:" + url + "---taskid:" + taskMobile.getTaskid(),
					"<xmp>" + html + "</xmp>");
			return searchPage;
		}
		return null;
	}

}