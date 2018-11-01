package app.unit;

import java.net.URL;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.binzhou.HousingBinZhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundBinZhouParser;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundBinZhouHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundBinZhouHtmlunit.class);
	
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private HousingFundBinZhouParser  housingFundBinZhouParser;
	@Autowired
	private TracerLog tracer;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int count) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam= new WebParam();
		String url = "http://www.bzgjj.cn/index.php";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "www.bzgjj.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.bzgjj.cn/userlogout.php");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		HtmlPage page = webClient.getPage(webRequest);
		webClient.getOptions().setJavaScriptEnabled(true);  
		webClient.waitForBackgroundJavaScript(20000);
	    tracer.addTag("parser.crawler.login.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		if(200 == page.getWebResponse().getStatusCode()){					
			if (page.getWebResponse().getContentAsString().contains("imageField")) {
				HtmlImage image = page.getFirstByXPath("//img[@name='imageField']");
				String code = chaoJiYingOcrService.getVerifycode(image, "1004");
				tracer.addTag("verifyCode ==>", code);
				//登陆
				String loginUrl = "http://www.bzgjj.cn/userlogin.php";		                     
				WebRequest webRequest2 = new WebRequest(new URL(loginUrl), HttpMethod.POST);	
				String requestBody="username="+messageLoginForHousing.getNum()+"&password="+messageLoginForHousing.getPassword()+"&checkcode="+code+"&url=index.php&action=check&Submit.x=54&Submit.y=11";
				webRequest2.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
				webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				webRequest2.setAdditionalHeader("Connection", "keep-alive");
				webRequest2.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
				webRequest2.setAdditionalHeader("Host", "www.bzgjj.cn");
				webRequest2.setAdditionalHeader("Origin", "http://www.bzgjj.cn");
				webRequest2.setAdditionalHeader("Referer", "http://www.bzgjj.cn/index.php");
				webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
				webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				webRequest2.setRequestBody(requestBody);
				HtmlPage loginPage = webClient.getPage(webRequest2);
				Thread.sleep(1000);
				String html=loginPage.asXml();	
				String msgAlert=WebCrawler.getAlertMsg();
				tracer.addTag("parser.crawler.login.loginPage", "<xmp>"+html+"</xmp>");
				webParam.setHtmlPage(loginPage);	
				webParam.setWebClient(webClient);
				webParam.setUrl(url);
				webParam.setMsgAlert(msgAlert);
				if (html.contains("欢迎您")) {
					webParam.setWebClient(webClient);
					webParam.setHtml(html);
					webParam.setHtmlPage(loginPage);
				}else{
					if (count < 3) {
						count++;
						tracer.addTag("parser.crawler.login.count" + count, "这是第" + count + "次登陆");
						Thread.sleep(1500);
						login(messageLoginForHousing, taskHousing, count);
					}					
				}
			}
		}
		return webParam;	
	}
	

	public WebParam  getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient=addcookie(webClient, taskHousing);
		WebParam webParam= new WebParam();	
	    String url = "http://www.bzgjj.cn/usermain2.php";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.bzgjj.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.bzgjj.cn/usermain.php");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page = webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getUserInfo", "<xmp>"+html+"</xmp>");
		HousingBinZhouUserInfo userInfo=new HousingBinZhouUserInfo();
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		if (html.contains("职工帐号")) {
			userInfo=housingFundBinZhouParser.htmlUserInfoParser(html,taskHousing);
			webParam.setUserInfo(userInfo);
			webParam.setPage(page);
			webParam.setHtml(html);
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
