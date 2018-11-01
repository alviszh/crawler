package app.parser;

import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

import app.bean.RequestParam;
import app.controller.CcbChinaController;

@Component
public class CcbChinaLoginParser {
	
	public static final Logger log = LoggerFactory.getLogger(CcbChinaController.class);
	
	/**
	 * @Des 获得重要参数
	 * @param html
	 * @return
	 */
	public RequestParam getParam(String html,WebDriver driver) {
		
		log.info("获得重要参数:getParam");
		
		RequestParam requestParam = new RequestParam();
		Document doc = Jsoup.parse(html);
		Element form = doc.getElementById("form4Switch");
		
		Element skeyInput = form.select("[name=SKEY]").first();
		String skey = skeyInput.attr("value");
		Element branchidInput = form.select("[name=BRANCHID]").first();
		String branchid = branchidInput.attr("value");
		Element useridInput = form.select("[name=USERID]").first();
		String userid = useridInput.attr("value");
		
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("ibsbjstar.ccb.com.cn", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);
		
		requestParam.setCookies(cookieJson);
		requestParam.setBranchid(branchid);
		requestParam.setSkey(skey);
		requestParam.setUserid(userid);
		return requestParam;
	}
	

}
