package app.service;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.jiangmen"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.jiangmen"})
public class LoginAndGetService {
	@Autowired
	private  ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public  Page loginByIDNUM(WebClient webClient,String url, String username, String password, String message) throws Exception {
		
		WebRequest webRequest1 = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage page = webClient.getPage(webRequest1);

		HtmlImage valiCodeImg = page.getFirstByXPath("//*[@id='vimg2']");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4004");

		
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		Elements ele = doc.select("#__VIEWSTATE");
		Elements ele1 = doc.select("#__EVENTVALIDATION");
		Elements ele2 = doc.select("#__VIEWSTATEGENERATOR");
		String __VIEWSTATE = null;
		String __EVENTVALIDATION = null;
		String __VIEWSTATEGENERATOR = null;
		if(ele.size()>0){
			__VIEWSTATE = ele.attr("value").trim();	
		}
		if(ele1.size()>0){
			__EVENTVALIDATION = ele1.attr("value").trim();	
		}
		if(ele2.size()>0){
			__VIEWSTATEGENERATOR = ele2.attr("value").trim();	
		}
		String encodeURL=URLEncoder.encode( __VIEWSTATE, "UTF-8" );
		String encodeURL1=URLEncoder.encode( __EVENTVALIDATION, "UTF-8" );
		String url1 = "http://sbj.jiangmen.gov.cn/WsSever.aspx?mode=log";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "sbj.jiangmen.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://sbj.jiangmen.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://sbj.jiangmen.gov.cn/WsSever.aspx?mode=log");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] textByte = username.getBytes("UTF-8");
		byte[] textByte1 = password.getBytes("UTF-8");
		byte[] textByte2 = message.getBytes("UTF-8");
		String encodedText = encoder.encodeToString(textByte);
		String encodedText1 = encoder.encodeToString(textByte1);
		String encodedText2 = encoder.encodeToString(textByte2);
		String requestBody = "__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE="+encodeURL+"&__VIEWSTATEGENERATOR="+__VIEWSTATEGENERATOR+"&__EVENTVALIDATION="+encodeURL1+""
				+ "&txtPSIDCD=&txtPWD=&txtLast6=&txtCommentCode3=&IDNo="+encodedText+"&BtnSummit=%E6%8F%90+%E4%BA%A4&PsCxKl="+encodedText1+"&sbcard="+encodedText2+""
				+ "&txtCommentCode="+valicodeStr+"&OrganizationNo=&UtCxKl=&DJZNo1=&DJZNo2=&bze=&txtCommentCode2=&hidPscode="+username+"&hidCodeCount=1&hidIdcard="+username+"";
		webRequest.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest);
		
//		String url2 = "http://sbj.jiangmen.gov.cn/Servers/BaseInfoSearch.aspx?ItemId=74";
//		WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.GET);
//		Page searchPage1 = webClient.getPage(webRequest2);
//	    System.err.println(searchPage.getWebResponse().getContentAsString());
	    if(searchPage.getWebResponse().getContentAsString().contains("验证码错误，请重新填写验证码！")){
			tracer.addTag(valicodeStr, "图片验证码错误，触发retry机制");
			throw new RuntimeException("请输入正确的附加码");
		}
		return searchPage;
	}

}
