package app.service.credit;

import java.net.URL;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;

import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.unit.CommonUnit;

/**
 * 
 * 项目名称：common-microservice-bank-boc 类名称：BocServiceCreditLoginAndGet 类描述：
 * 创建人：hyx 创建时间：2017年11月1日 上午11:17:01
 * 
 * @version
 */

@Component
@EnableAsync
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic",
		"com.microservice.dao.entity.crawler.bank.bocchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic",
		"com.microservice.dao.repository.crawler.bank.bocchina" })
public class BocServiceCreditLoginAndGet extends AbstractChaoJiYingHandler {

	public String getAccountSeq(String url,  WebDriver driver) throws Exception {
		
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("ebsnew.boc.cn", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60

		webClient.addRequestHeader("bfw-ctrl", "json");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20170907091405353&locale=zh&login=card&segment=1");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},\"request\":[{\"id\":6,\"method\":\"PsnAccBocnetQryLoginInfo\",\"conversationId\":null,\"params\":null}]}");
		Page searchPage = webClient.getPage(webRequest);
//		System.out.println(searchPage.getWebResponse().getContentAsString());
//		String acccountSeq = CommonUnit
//				.getSubUtilSimple(searchPage.getWebResponse().getContentAsString(), "accountSeq(.*?)accountType")
//				.replaceAll("\"", "").replaceAll(",", "").replaceAll(":", "").trim();
//		System.out.println("acccountSeq======" + acccountSeq);
		return searchPage.getWebResponse().getContentAsString();
	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-boc 类描述： 获取 银行流水的重要参数方法 创建人：hyx
	 * 创建时间：2017年11月1日 下午2:40:59
	 * 
	 * @version 返回值 为String
	 * 
	 * 
	 */
	public String getCountid(String url,WebClient webClient) throws Exception {
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60

		webClient.addRequestHeader("bfw-ctrl", "json");
		// webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20170907091405353&locale=zh&login=card&segment=1");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"}"
				+ ",\"request\":[{\"id\":17,\"method\":\"PsnAccBocnetCreateConversation\",\"conversationId\":null,\"params\":null}]}");
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		String counid = CommonUnit
				.getSubUtilSimple(searchPage.getWebResponse().getContentAsString(), "result(.*?)error")
				.replaceAll("\"", "").replaceAll(",", "").replaceAll(":", "").trim();
		System.out.println("counid======" + counid);
		return counid;
	}

	
	/**
	 * 
	 * 项目名称：common-microservice-bank-boc 所属包名：app.service 类描述： 创建人：hyx
	 * 创建时间：2017年11月1日
	 * 
	 * @version 1 返回值 AsyncResult<String>
	 */
	public String getAccountUserInfo(String url, WebClient webClient,String acccountSeq) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("bfw-ctrl", "json");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20170907091405353&locale=zh&login=card&segment=1");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},"
				+ "\"request\":[{\"id\""
				+ ":8"
				+ ",\"method\":\"PsnAccBocnetQueryGeneralInfo\",\"conversationId\":null,\"params\":{\"accountSeq\":\""
				+ acccountSeq.trim()
				+ "\",\"currency\":\"\"}}]}");
		Page searchPage = webClient.getPage(webRequest);
		System.out.println("getUserInfo == " + searchPage.getWebResponse().getContentAsString());
		return searchPage.getWebResponse().getContentAsString();
	}
	
	
	public  String getConversationId(String url, WebClient webClient,int id_countid) throws Exception {
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST); 
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("bfw-ctrl", "json");
		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("X-id", id_countid+"");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20171013030320567&locale=zh&login=card&segment=1");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		String body = "{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\",\"mac\":\"\",\"serial\":\"\"},\"request\":[{\"id\":"
				+ (id_countid-1)
				+ ",\"method\":\"PsnAccBocnetCreateConversation\",\"conversationId\":null,\"params\":null}]}";
		webRequest.setRequestBody(body);//"2017/11"
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		String conversationId = CommonUnit
				.getSubUtilSimple(searchPage.getWebResponse().getContentAsString(), "result(.*?)error")
				.replaceAll("\"", "").replaceAll(",", "").replaceAll(":", "").trim();
		System.out.println("conversationId======" + conversationId);
		return conversationId;

	}
	
	public  String getCreditInfo(String url, WebClient webClient, String countid,String acccountSeq,String date) throws Exception {
				
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST); 
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("bfw-ctrl", "json");
		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("X-id", "23");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20171013030320567&locale=zh&login=card&segment=1");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		String body = "{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},\"request\":"
				+ "[{\"id\":"
				+ "17"
				+ ",\"method\":\"PsnAccBocnetQryCrcdStatement\",\"conversationId\":\""
				+ countid.trim()
				+ "\",\"params\":{\"accountSeq\":\""
				+ acccountSeq.trim()
				+ "\",\"statementMonth\":\""
				+ date.trim()
				+ "\"}}]}";
		webRequest.setRequestBody(body);//"2017/11"
		Page searchPage = webClient.getPage(webRequest);
		return searchPage.getWebResponse().getContentAsString();
	}

	
	public  String getTranList(String url, WebClient webClient, String countid,String accountType,String creditcardId,String date,String id) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("bfw-ctrl", "json");
		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("X-id", "23");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20180620090404580&locale=zh&login=card&segment=1");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		String body = "{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},\"request\":[{\"id\":"
				+ id.trim()
				+ ",\"method\":\"PsnAccBocnetQryCrcdStatementDetail\",\"conversationId\":\""
				+ countid.trim()
				+ "\",\"params\":{\"creditcardId\":\""
				+ creditcardId.trim()
				+ "\",\"statementMonth\":\""
				+ date.trim()
				+ "\",\"accountType\":\""
				+ accountType.trim()
				+ "\",\"primary\":\"\",\"_refresh\":\"true\",\"lineNum\":\"1000\",\"pageNo\":\"1\"}}]}";
		webRequest.setRequestBody(body);//"2017/11/06"
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(body);
		System.out.println(date+"===="+searchPage.getWebResponse().getContentAsString());
		return searchPage.getWebResponse().getContentAsString();
	}

	
}