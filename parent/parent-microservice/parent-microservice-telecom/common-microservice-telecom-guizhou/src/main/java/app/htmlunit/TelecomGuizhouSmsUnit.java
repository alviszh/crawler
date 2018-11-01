package app.htmlunit;

import java.net.URL;
import java.time.LocalDate;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;

@Component
public class TelecomGuizhouSmsUnit {

	public  final Logger log = LoggerFactory.getLogger(TelecomGuizhouSmsUnit.class);
	@Autowired
	private TracerLog tracer;  
	//发送验证码
	public  WebParam getphonecode(MessageLogin mssageLogin,TaskMobile taskMobile) throws Exception {
		tracer.addTag("getphonecode", mssageLogin.getTask_id());
		WebParam webParam=new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient, taskMobile);
		String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00320352";
		HtmlPage htmlpage = getHtml(url, webClient);
		tracer.addTag("parser.telecom.getphonecode.htmlpage",htmlpage.asXml());
		String url2="http://service.gz.189.cn/web/query.php?action=call";
		HtmlPage htmlpage2 = getHtml(url2, webClient);
		tracer.addTag("parser.telecom.getphonecode.htmlpage2",htmlpage2.asXml());
		if (200==htmlpage2.getWebResponse().getStatusCode()) {
			String smsUrl="http://service.gz.189.cn/web/query.php?action=postsms";
			WebRequest webRequest = new WebRequest(new URL(smsUrl), HttpMethod.POST);	
			webRequest.setAdditionalHeader("Accept", "text/plain, */*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
			webRequest.setAdditionalHeader("Host", "service.gz.189.cn");
			webRequest.setAdditionalHeader("Origin", "http://service.gz.189.cn");
			webRequest.setAdditionalHeader("Referer", "http://service.gz.189.cn/web/query.php?parser=call&fastcode=00320353&cityCode=gz");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			Page page = webClient.getPage(webRequest);	
			Thread.sleep(2500);
			String html=page.getWebResponse().getContentAsString();
			tracer.addTag("getphonecode",html);	
			webParam.setWebClient(webClient);
			webParam.setUrl(smsUrl);
			webParam.setSmsResult(html);
			return webParam;
		}		
		return null;
	}
	//手机验证码验证
	public WebParam setphonecode(MessageLogin mssageLogin,TaskMobile taskMobile) throws Exception {
		tracer.addTag("TelecomGuizhouSmsUnit.setphonecode",taskMobile.getTaskid());
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskMobile);
		String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00320352";
		HtmlPage htmlpage = getHtml(url, webClient);	
		if (200==htmlpage.getWebResponse().getStatusCode()) { 
		url = "http://service.gz.189.cn/web/query.php?_=1505093738415&action=getAllCall&QueryMonthly="+getLocalMonth()+"&QueryType=1&checkcode="+mssageLogin.getSms_code();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "service.gz.189.cn");	
		webRequest.setAdditionalHeader("Referer", "http://service.gz.189.cn/web/query.php?parser=call&fastcode=00320353&cityCode=gz");
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*"); 
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webClient.setJavaScriptTimeout(60000);
		webClient.getOptions().setTimeout(60000); // 15->60
		Page page = webClient.getPage(webRequest);		
		Thread.sleep(2000);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("setphonecode手机验证码验证结果",html);
		webParam.setWebClient(webClient);
		webParam.setSmsResult(html);
		return webParam;
		}
		return null;
	}
	
	//获取当前月份
	private String getLocalMonth(){
		LocalDate today = LocalDate.now();// 本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(0);
		String monthint = stardate.getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() + monthint;
		return  month;		
	}
	public  HtmlPage getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
			HtmlPage searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomguizhou ", e.getMessage());
			return null;
		}

	}
	public  WebClient addcookie(WebClient webclient, TaskMobile taskMobile) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		 for(Cookie cookie : cookies){
			 webclient.getCookieManager().addCookie(cookie);
		  }
		return webclient;
	}
}
