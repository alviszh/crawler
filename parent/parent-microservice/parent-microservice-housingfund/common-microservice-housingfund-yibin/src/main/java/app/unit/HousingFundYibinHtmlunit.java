package app.unit;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yibin.HousingYibinUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundYinbinParser;

@Component
public class HousingFundYibinHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundYibinHtmlunit.class);
	@Autowired
	private HousingFundYinbinParser  housingFundYinbinParser;
	@Autowired
	private TracerLog tracer;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	    webClient.getOptions().setThrowExceptionOnScriptError(false);  
		WebParam webParam = new WebParam();
		String url = "http://gjjcx.yibin.gov.cn/search.asp?s=login";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("sfz", messageLoginForHousing.getNum()));
		paramsList.add(new NameValuePair("gjj", messageLoginForHousing.getHosingFundNumber()));
		paramsList.add(new NameValuePair("pass", messageLoginForHousing.getPassword()));
		paramsList.add(new NameValuePair("B1", URLEncoder.encode("公积金查询", "gbk")));
		webRequest.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "gjjcx.yibin.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://gjjcx.yibin.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://gjjcx.yibin.gov.cn/search.asp");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setCharset(Charset.forName("GBK"));
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.login.html", html);
		webParam.setUrl(url);
		webParam.setPage(page);
		webParam.setHtml(html);
		String alertMsg=WebCrawler.getAlertMsg();
		webParam.setAlertMsg(alertMsg);
		webParam.setWebClient(webClient);
		HousingYibinUserInfo userInfo=new HousingYibinUserInfo();
		if (html.contains("tablejieguo")) {
			webParam.setLogin(true);
			userInfo=housingFundYinbinParser.htmlUserInfoParser(html, taskHousing);
			webParam.setUserInfo(userInfo);
		}		
		return webParam;
	}

	public  WebClient addcookie(WebClient webclient, TaskHousing taskHousing) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		 for(Cookie cookie : cookies){
			 webclient.getCookieManager().addCookie(cookie);
		  }
		return webclient;
	}
}
