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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.xiamen.HousingXiamenPaydetails;
import com.microservice.dao.entity.crawler.housing.xiamen.HousingXiamenUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundXiamenParser;

@Component
public class HousingFundXiamenHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundXiamenHtmlunit.class);
	
	@Autowired
	private HousingFundXiamenParser  housingFundXiamenParser;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	public WebParam login(MessageLoginForHousing messageLoginForHousing,int count) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		String url = "http://wscx.xmgjj.gov.cn/xmgjjGR/login.shtml";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "wscx.xmgjj.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://wscx.xmgjj.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://wscx.xmgjj.gov.cn/xmgjjGR/login.jsp");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page = webClient.getPage(webRequest);
		tracer.addTag("parser.crawler.login.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		 if(200 == page.getWebResponse().getStatusCode()){					
			if (page.getWebResponse().getContentAsString().contains("username")) {
				HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("//input[@id='username']");
				HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
				HtmlElement loginButton = (HtmlElement)page.getFirstByXPath("//a[@class='btn btn-block btn-primary']");
				username.setText(messageLoginForHousing.getNum());
				password.setText(messageLoginForHousing.getPassword());		
				HtmlPage loginPage = loginButton.click();
				Thread.sleep(2500);
				String html=loginPage.asXml();	
				System.out.println(html);
				tracer.addTag("parser.crawler.login.loginPage", "<xmp>"+html+"</xmp>");
				webParam.setHtmlPage(loginPage);	
				webParam.setWebClient(webClient);
				webParam.setUrl(url);
				if (html.contains("个人中心")) {
					webParam.setWebClient(webClient);
					webParam.setHtml(html);
					webParam.setHtmlPage(loginPage);
					webParam.setLogin(true);
				}else{
					if (count < 3) {
						count++;
						tracer.addTag("parser.crawler.login.count" + count, "这是第" + count + "次登陆");
						Thread.sleep(1500);
						login(messageLoginForHousing, count);
					}					
				}
			}
		}
		return webParam;	
	}
	
	public WebParam  getPaydetails(MessageLoginForHousing messageLoginForHousing, String accountnum, int count) throws Exception {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskHousing);
		String url = "http://wscx.xmgjj.gov.cn/xmgjjGR/queryGrzhxxJson.shtml";	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Calendar c = Calendar.getInstance();
		String timeNow = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
		c.add(Calendar.YEAR, -1);
        String startDate = new SimpleDateFormat("yyyyMMdd").format(c.getTime()); 
    	tracer.addTag("parser.crawler.getPaydetails参数为 timeNow=", timeNow+" startDate="+startDate+" accountnum="+accountnum);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();		
		paramsList.add(new NameValuePair("custAcct", accountnum));
		paramsList.add(new NameValuePair("startDate", startDate));
		paramsList.add(new NameValuePair("endDate", timeNow));
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "wscx.xmgjj.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://wscx.xmgjj.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://wscx.xmgjj.gov.cn/xmgjjGR/grxx.php?custAcct=10036589067&startDate=20161018&endDate=20171018");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);
		Thread.sleep(2500);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getPaydetails", html);
		webParam.setHtml(html);
		webParam.setPage(page);	
		webParam.setUrl(url);
		List<HousingXiamenPaydetails> paydetails=new ArrayList<HousingXiamenPaydetails>();
	   if (html.contains("list")) {
		    paydetails=housingFundXiamenParser.htmlPaydetailsParser(html, taskHousing);
			webParam.setPaydetails(paydetails);
			webParam.setHtml(html);
			webParam.setPage(page);
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getPaydetails.count" + count, "这是第" + count + "次获取公积金缴存明细信息");
				Thread.sleep(1500);
				getPaydetails(messageLoginForHousing,accountnum, count);
			}			
		}		
		return webParam;
	}
	public WebParam  getUserInfo(MessageLoginForHousing messageLoginForHousing,int count) throws Exception {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());	 
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskHousing);		
	    String url = "http://wscx.xmgjj.gov.cn/xmgjjGR/queryZgzh.shtml";		
	    WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "wscx.xmgjj.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://wscx.xmgjj.gov.cn");				
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");	
		Page page = webClient.getPage(webRequest);
		Thread.sleep(2500);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getUserInfo", html);
		HousingXiamenUserInfo userInfo=new HousingXiamenUserInfo();
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		if (html.contains("list")) {
			userInfo=housingFundXiamenParser.htmlUserInfoParser(html,taskHousing);
			webParam.setUserInfo(userInfo);
			webParam.setPage(page);
			webParam.setHtml(html);
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getUserInfo.count" + count, "这是第" + count + "次获取用户信息");
				Thread.sleep(1500);
				getUserInfo(messageLoginForHousing,count);
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
