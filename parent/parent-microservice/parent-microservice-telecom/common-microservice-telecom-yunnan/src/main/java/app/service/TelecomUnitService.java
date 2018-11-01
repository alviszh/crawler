package app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.service.common.TelecomBasicService;
import app.unit.TeleComCommonUnit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.yunnan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.yunnan")
public class TelecomUnitService extends TelecomBasicService {

	public WebClient getWebClientForTeleComYunNan(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);

		String url = "http://yn.189.cn/public/query_realnameInfo.jsp?NUM="
				+ messageLogin.getName().trim()
				+ "&AREA_CODE=&accNbrType=9";
		TeleComCommonUnit.getHtml(url, webClient);

		url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_new.jsp";
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", messageLogin.getName().trim()));
		paramsList.add(new NameValuePair("AREA_CODE", taskMobile.getAreacode().trim()));
		paramsList.add(new NameValuePair("PROD_NO", "4217"));
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "application/xml, text/xml, */*; q=0.01");
		webRequest.setAdditionalHeader("Referer", "http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");  
		webRequest.setAdditionalHeader("Host", "yn.189.cn"); 
		webRequest.setAdditionalHeader("Pragma", "no-cache"); 
		webRequest.setAdditionalHeader("Origin", "http://yn.189.cn"); 
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		webRequest.setRequestParameters(paramsList);
		TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		return webClient;

	}

	public WebClient addcookieForSMSAndCall(WebClient webClient) throws Exception {
		Map<String, String> cookieMaps = new HashMap<>();
		cookieMaps.put("Hm_lvt_79fae2027f43ca31186e567c6c8fe33e", "15,054,702,261,505,600,000");
		cookieMaps.put("Hm_lvt_99c2beeae1f4239c63480835d2892bf4", "1501581156");
		cookieMaps.put("Hm_lvt_ad041cdaa630664faeaf7ca3a6a45b89", "15,051,808,891,505,200,000");
		cookieMaps.put("UM_distinctid", "15d980fb0c21b8-0fd77255fd1164-474f0820-100200-15d980fb0c9557");
		cookieMaps.put("__utma", "218110873.589372428.1501581156.1501581156.1501581156.1");
		cookieMaps.put("__utmz", "218110873.1501581156.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");
		cookieMaps.put("_gscu_1758414200", "015599983snitv16");
		cookieMaps.put("cn_1260051947_dplus",
				"%7B%22distinct_id%22%3A%20%2215d980fb0c21b8-0fd77255fd1164-474f0820-100200-15d980fb0c9557%22%2C%22sp%22%3A%20%7B%22%24recent_outside_referrer%22%3A%20%22sso.telefen.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201505462008%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201505462008%7D%2C%22initial_view_time%22%3A%20%221501493821%22%2C%22initial_referrer%22%3A%20%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DC5gnI1MOIDBWo3OrGrMxnEjAAnhMBNOAHWIls2ffKuq-_zaY4GpCvvgm2_QRPn6J%26wd%3D%26eqid%3Dbf7d80a2000275f300000002597efec8%22%2C%22initial_referrer_domain%22%3A%20%22www.baidu.com%22%7D");
		cookieMaps.put("fenxiId", "8c07f0a0-3c66-46ec-b4b4-1ed04013c6a9");
		cookieMaps.put("i_vnum", "9");
		cookieMaps.put("ijg", "1.50518E+12");
		cookieMaps.put("pgv_pvi", "6499893248");
		cookieMaps.put("s_sq", "%5B%5BB%5D%5D");
		cookieMaps.put("trkHmCity", "0");
		cookieMaps.put("trkHmClickCoords", "0");
		cookieMaps.put("trkHmCoords", "0");
		cookieMaps.put("trkHmLinks", "0");
		cookieMaps.put("trkHmPageName", "%2Fyn%2F");

		Set<Cookie> cookiesWebClient = webClient.getCookieManager().getCookies();
		for (Map.Entry<String, String> entry : cookieMaps.entrySet()) {
			boolean istrue = false;
			for (Cookie cookie : cookiesWebClient) {
				if (entry.getKey().indexOf(cookie.getName()) != -1) {
					istrue = true;
					break;
				}
			}
			if (istrue) {
				continue;
			}
			webClient.getCookieManager().addCookie(new Cookie("yn.189.cn", entry.getKey(), entry.getValue()));
		}

		return webClient;

	}

}
