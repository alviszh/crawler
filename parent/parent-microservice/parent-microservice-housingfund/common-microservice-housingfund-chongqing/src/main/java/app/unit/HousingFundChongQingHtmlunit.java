package app.unit;

import java.net.URL;
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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingAccountInfo;
import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingPaydetails;
import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundChongQingParser;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundChongQingHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundChongQingHtmlunit.class);
	
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private HousingFundChongQingParser  housingFundChongQingParser;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;	
	public WebParam login(MessageLoginForHousing messageLoginForHousing,
			int count) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.cqgjj.cn/html/user/login.html";
		WebParam webParam = new WebParam();
		webClient.getOptions().setJavaScriptEnabled(true);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.cqgjj.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.cqgjj.cn/html/user/login.html");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage firstPage = webClient.getPage(webRequest);
		String loginUrl = "http://www.cqgjj.cn/Member/UserLogin.aspx?type=null";
		WebRequest webRequest2 = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		webRequest2.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Host", "www.cqgjj.cn");
		webRequest2.setAdditionalHeader("Referer", "http://www.cqgjj.cn/Member/UserLogin.aspx?type=null");
		webRequest2.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page = webClient.getPage(webRequest2);
		Thread.sleep(1500);
		if (200 == page.getWebResponse().getStatusCode()) {
			if (page.getWebResponse().getContentAsString().contains("txt_loginname")) {
				HtmlImage image = page.getFirstByXPath("//img[@id='imgCode']");
				String code = chaoJiYingOcrService.getVerifycode(image, "5000");
				tracer.addTag("verifyCode ==>", code);
				HtmlTextInput username = (HtmlTextInput) page.getFirstByXPath("//input[@id='txt_loginname']");
				HtmlPasswordInput password = (HtmlPasswordInput) page.getFirstByXPath("//input[@id='txt_pwd']");
				HtmlTextInput imageCode = (HtmlTextInput) page.getFirstByXPath("//input[@id='txt_code']");
				HtmlElement loginButton = (HtmlElement) page.getFirstByXPath("//input[@id='loginBtn']");

				username.setText(messageLoginForHousing.getNum());
				password.setText(messageLoginForHousing.getPassword());
				imageCode.setText(code);
				HtmlPage loginPage = loginButton.click();
				Thread.sleep(1500);
				webParam.setHtmlPage(loginPage);
				webParam.setWebClient(webClient);
				String html = loginPage.asXml();
				tracer.addTag("parser.crawler.login.loginPage", "<xmp>" + html + "</xmp>");
				if (html.contains("会员中心")) {
					webParam.setHtmlPage(loginPage);
					webParam.setWebClient(webClient);
					webParam.setHtml(html);
				} else {
					page = webClient.getPage(webRequest2);
					html = page.asXml();
					if (html.contains("会员中心")) {
						webParam.setWebClient(webClient);
						webParam.setHtmlPage(page);
						webParam.setHtml(html);
					} else {
						if (count < 3) {
							count++;
							tracer.addTag("parser.crawler.login.count" + count, "这是第" + count + "次登陆");
							Thread.sleep(2500);
							login(messageLoginForHousing, count);
						}
					}
				}
			}
		}
		return webParam;
	}
	
	public WebParam  getPaydetails(MessageLoginForHousing messageLoginForHousing,int count) throws Exception {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskHousing);
		String url = "http://www.cqgjj.cn/Member/gr/gjjmxcx.aspx";	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.cqgjj.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.cqgjj.cn/Member/gr/gjjmxcx.aspx");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page= webClient.getPage(webRequest);
		Thread.sleep(2500);
		List<HousingChongqingPaydetails> paydetails=new ArrayList<HousingChongqingPaydetails>();
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getPaydetails", "<xmp>"+html+"</xmp>");
		webParam.setHtml(html);
		webParam.setPage(page);
		if (html.contains("您的查询过于频繁，请输入验证码后继续")) {		
			HtmlImage image = page.getFirstByXPath("//img[@id='imgCode']");
			String code = chaoJiYingOcrService.getVerifycode(image, "5000");
			tracer.addTag("verifyCode ==>", code);
			HtmlTextInput imageCode= (HtmlTextInput)page.getFirstByXPath("//input[@id='ContentPlaceHolder1_TextBox_Code']");
			HtmlElement queryButton = (HtmlElement)page.getFirstByXPath("//input[@id='ContentPlaceHolder1_Button_Select']");
			imageCode.setText(code);
			page = queryButton.click();
			html=page.getWebResponse().getContentAsString();
			if (html.contains("listinfo") && html.contains("ContentPlaceHolder1_Button1")) {
				paydetails = housingFundChongQingParser.htmlPaydetailsParser(html, taskHousing);
				webParam.setPaydetails(paydetails);
				webParam.setHtml(html);
				webParam.setPage(page);
			}
		 }else if (html.contains("listinfo") && html.contains("ContentPlaceHolder1_Button1")) {
			paydetails=housingFundChongQingParser.htmlPaydetailsParser(html, taskHousing);
			webParam.setPaydetails(paydetails);
			webParam.setHtml(html);
			webParam.setPage(page);
		}else{
			page= webClient.getPage(webRequest);
			html=page.getWebResponse().getContentAsString();
            if (html.contains("listinfo") && html.contains("ContentPlaceHolder1_Button1")) {
            	webParam.setPaydetails(paydetails);
    			webParam.setHtml(html);
    			webParam.setPage(page);
			}else{
				if (count < 3) {
					count++;
					tracer.addTag("parser.crawler.getPaydetails.count" + count, "这是第" + count + "次获取公积金缴存明细信息");
					Thread.sleep(2500);
					getPaydetails(messageLoginForHousing, count);
				}
			}
		}		
		return webParam;
	}
	public WebParam  getUserInfo(MessageLoginForHousing messageLoginForHousing,int count) throws Exception {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskHousing);
		String url = "http://www.cqgjj.cn/Member/UserInfo/MyInformation.aspx";	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.cqgjj.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.cqgjj.cn/Member/UserInfo/MyInformation.aspx");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page=webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getUserInfo", "<xmp>"+html+"</xmp>");
		HousingChongqingUserInfo userInfo=new HousingChongqingUserInfo();
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		if (html.contains("listinfo")) {
			userInfo=housingFundChongQingParser.htmlUserInfoParser(html,taskHousing);
			webParam.setUserInfo(userInfo);
			webParam.setPage(page);
			webParam.setHtml(html);
		}else{
			page= webClient.getPage(webRequest);
			html=page.getWebResponse().getContentAsString();
			if (html.contains("listinfo")) {
				userInfo=housingFundChongQingParser.htmlUserInfoParser(html,taskHousing);
				webParam.setUserInfo(userInfo);
				webParam.setPage(page);
				webParam.setHtml(html);
			}else{
				if (count < 3) {
					count++;
					tracer.addTag("parser.crawler.getUserInfo.count" + count, "这是第" + count + "次获取用户信息");
					Thread.sleep(2500);
					getUserInfo(messageLoginForHousing, count);
				}
			}
		}		
		return webParam;
	}
	public WebParam  getAccountInfo(MessageLoginForHousing messageLoginForHousing, int count) throws Exception {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskHousing);
		String url = "http://www.cqgjj.cn/Member/gr/gjjyecx.aspx";	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.cqgjj.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.cqgjj.cn/Member/gr/gjjyecx.aspx");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");	
		HtmlPage page=webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getAccountInfo", "<xmp>"+html+"</xmp>");
		HousingChongqingAccountInfo accountInfo=new HousingChongqingAccountInfo();
		webParam.setUrl(url);
		if (html.contains("listinfo")) {
			accountInfo= housingFundChongQingParser.htmlAccountInfoParser(html, taskHousing);
			webParam.setAccountInfo(accountInfo);
			webParam.setPage(page);
			webParam.setHtml(html);
		}else{
			page= webClient.getPage(webRequest);
			html=page.getWebResponse().getContentAsString();			
			if (html.contains("listinfo")) {
				accountInfo= housingFundChongQingParser.htmlAccountInfoParser(html, taskHousing);
				webParam.setAccountInfo(accountInfo);
				webParam.setPage(page);
				webParam.setHtml(html);
			}else{
				if (count < 3) {
					count++;
					tracer.addTag("parser.crawler.getAccountInfo.count" + count, "这是第" + count + "次获取公积金余额信息");
					Thread.sleep(2500);
					getAccountInfo(messageLoginForHousing, count);
				}
			}
           
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
