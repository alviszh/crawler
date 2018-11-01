package app.unit;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.mianyang.HousingMianYangPaydetails;
import com.microservice.dao.entity.crawler.housing.mianyang.HousingMianYangUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundMianYangParser;
import app.service.ChaoJiYingOcrService;
import app.service.common.LoginAndGetCommon;
import net.sf.json.JSONObject;

@Component
public class HousingFundMianYangHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundMianYangHtmlunit.class);
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private HousingFundMianYangParser  housingFundMianYangParser;
	@Autowired
	private TracerLog tracer;

	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,
			int count) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		String loginUrl = "http://gjjobs.my.gov.cn/ispobs/";
		HtmlPage loginPage = (HtmlPage) LoginAndGetCommon.getHtml(loginUrl, webClient);
		tracer.addTag("parser.crawler.login.loginPage",
				"<xmp>" + loginPage.getWebResponse().getContentAsString() + "</xmp>");
		if (200 == loginPage.getWebResponse().getStatusCode()) {
			if (loginPage.getWebResponse().getContentAsString().contains("globalController/CreateVerifyImg.do")) {
				HtmlImage image = loginPage
						.getFirstByXPath("//img[contains(@src,'globalController/CreateVerifyImg.do')]");
				String code = chaoJiYingOcrService.getVerifycode(image, "5000");
				tracer.addTag("verifyCode ==>", code);
				String url = "http://gjjobs.my.gov.cn/ispobs/loginController/grLoginVerify.do";
				WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
				paramsList.add(new NameValuePair("YHLX", "1"));
				paramsList.add(new NameValuePair("USERCODE", messageLoginForHousing.getNum()));
				paramsList.add(new NameValuePair("USERPWD", MD5Util.getMD5(messageLoginForHousing.getPassword())));
				paramsList.add(new NameValuePair("VERIFYCODE", code));
				webRequest.setAdditionalHeader("Accept", "*/*");
				webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				webRequest.setAdditionalHeader("Connection", "keep-alive");
				webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
				webRequest.setAdditionalHeader("Host", "gjjobs.my.gov.cn");
				webRequest.setAdditionalHeader("Origin", "http://gjjobs.my.gov.cn");
				webRequest.setAdditionalHeader("Referer", "http://gjjobs.my.gov.cn/ispobs/");
				webRequest.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
				webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				webRequest.setRequestParameters(paramsList);
				Page page = webClient.getPage(webRequest);
				webParam.setWebClient(webClient);
				webParam.setUrl(url);
				webParam.setPage(page);
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("parser.crawler.login.html", html);
				if (html.contains("MSG")) {
					JSONObject jsonObjs = JSONObject.fromObject(html);
					String loginStr = jsonObjs.getString("MSG");
					if (loginStr.contains("登录成功")) {				
						webParam.setLogin(true);
						webParam.setText("登录成功");
					}else if(loginStr.contains("验证码错误")){
						webParam.setText("验证码错误");
						webParam.setLogin(false);
					}else if(loginStr.contains("证件号码不存在")){
						webParam.setText("证件号码不存在");
						webParam.setLogin(false);
					}else if(loginStr.contains("用户密码错误")){
						webParam.setText("用户密码错误");
						webParam.setLogin(false);
					}else{
						webParam.setText("登陆失败");
						webParam.setLogin(false);
					}					
				}
				if (!webParam.isLogin) {
					if (count < 3) {
						count++;
						tracer.addTag("parser.crawler.login.count" + count, "这是第" + count + "登陆");
						Thread.sleep(1500);
						login(messageLoginForHousing, taskHousing, count);
					}	
				}
			}
		}
		return webParam;
	}
	
	public WebParam  getPaydetails(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing, int count) throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient,taskHousing);
		String url = "http://gjjobs.my.gov.cn/ispobs/grQueryController/grzhmxcx.do";	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Calendar c = Calendar.getInstance();
		String timeNow = new SimpleDateFormat("yyyyMM").format(c.getTime());
		c.add(Calendar.YEAR, -1);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();	
		paramsList.add(new NameValuePair("KSNY", "201601"));
		paramsList.add(new NameValuePair("JSNY", timeNow));
		paramsList.add(new NameValuePair("orderbyname", "CALINTERDATE"));
		paramsList.add(new NameValuePair("isasc", "DESC"));
		paramsList.add(new NameValuePair("pagesize", "100"));
		paramsList.add(new NameValuePair("pageindex", "1"));
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "gjjobs.my.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://gjjobs.my.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://gjjobs.my.gov.cn/ispobs/grQueryController/grxxcx.do");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);
		Thread.sleep(1500);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getPaydetails", html);
		webParam.setHtml(html);
		webParam.setPage(page);	
		webParam.setUrl(url);
		List<HousingMianYangPaydetails> paydetails=new ArrayList<HousingMianYangPaydetails>();
	   if (html.contains("rows")) {
		    paydetails=housingFundMianYangParser.htmlPaydetailsParser(html, taskHousing);
			webParam.setPaydetails(paydetails);
			webParam.setHtml(html);
			webParam.setPage(page);
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getPaydetails.count" + count, "这是第" + count + "次获取公积金缴存明细信息");
				Thread.sleep(1500);
				getPaydetails(messageLoginForHousing, taskHousing, count);
			}			
		}		
		return webParam;
	}
	public WebParam  getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int count) throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient,taskHousing);		
	    String url = "http://gjjobs.my.gov.cn/ispobs/grQueryController/grzhxxcx.do";	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);			
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "gjjobs.my.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://gjjobs.my.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://gjjobs.my.gov.cn/ispobs/grQueryController/grxxcx.do");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page = webClient.getPage(webRequest);
		Thread.sleep(1500);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getUserInfo", html);
		HousingMianYangUserInfo userInfo=new HousingMianYangUserInfo();
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		if (html.contains("DATA")) {
			userInfo=housingFundMianYangParser.htmlUserInfoParser(html,taskHousing);
			webParam.setUserInfo(userInfo);
			webParam.setPage(page);
			webParam.setHtml(html);
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getUserInfo.count" + count, "这是第" + count + "次获取用户信息");
				Thread.sleep(1500);
				getUserInfo(messageLoginForHousing, taskHousing, count);
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
