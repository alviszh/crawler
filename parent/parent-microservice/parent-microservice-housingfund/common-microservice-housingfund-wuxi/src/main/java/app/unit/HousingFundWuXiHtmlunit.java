package app.unit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wuxi.HousingWuxiPaydetails;
import com.microservice.dao.entity.crawler.housing.wuxi.HousingWuxiUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundWuXiParser;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundWuXiHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundWuXiHtmlunit.class);
	
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private HousingFundWuXiParser  housingFundWuXiParser;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	public WebParam login(WebClient webClient,MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int count) throws Exception {
		WebParam webParam= new WebParam();
		String url = "http://58.215.195.18:10010/login_person.jsp";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "58.215.195.18:10010");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page = webClient.getPage(webRequest);
		tracer.addTag("parser.crawler.login.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		 //获取登陆方式
		 String type="";
		 if (page.asXml().contains("logontype")) {
			 Document doc = Jsoup.parse(page.asXml(), "utf-8"); 
			 Elements div= doc.getElementsByAttributeValue("class","login");
			 Element table=div.select("table").get(0);
		     type=table.getElementById("logontype").getElementsByAttribute("selected").val();			
		}
		 tracer.addTag("type ==>", type);
		 if(200 == page.getWebResponse().getStatusCode()){					
			if (page.getWebResponse().getContentAsString().contains("loginname")) {
				HtmlImage image = page.getFirstByXPath("//img[@id='kaptcha-img1']");
				String code = chaoJiYingOcrService.getVerifycode(image, "1004");
				tracer.addTag("verifyCode ==>", code);
				HtmlSelect logontype = (HtmlSelect)page.getFirstByXPath("//select[@id='logontype']");
				HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("//input[@id='loginname']");
				HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
				HtmlTextInput imageCode =(HtmlTextInput)page.getFirstByXPath("//input[@name='_login_checkcode']");
				HtmlElement loginButton = (HtmlElement)page.getFirstByXPath("//input[@name='image']");

				logontype.setSelectedAttribute(type, true);
				if(messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getIDNUM())){
					username.setText(messageLoginForHousing.getNum());
				}else if(messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getACCOUNT_NUM())){
					username.setText(messageLoginForHousing.getHosingFundNumber());
				}	
				password.setText(messageLoginForHousing.getPassword());
				imageCode.setText(code);								
				HtmlPage loginPage = loginButton.click();
				Thread.sleep(1500);
				String html=loginPage.asXml();	
				tracer.addTag("parser.crawler.login.loginPage", "<xmp>"+html+"</xmp>");
				webParam.setHtmlPage(loginPage);	
				webParam.setWebClient(webClient);
				webParam.setUrl(url);
				if (html.contains("公积金查询")) {
					webParam.setWebClient(webClient);
					webParam.setHtml(html);
					webParam.setHtmlPage(loginPage);
				}else{
					if (count < 3) {
						count++;
						tracer.addTag("parser.crawler.login.count" + count, "这是第" + count + "次登陆");
						Thread.sleep(2500);
						login(webClient,messageLoginForHousing, taskHousing, count);
					}					
				}
			}
		}
		return webParam;	
	}
	
	public WebParam  getPaydetails(MessageLoginForHousing messageLoginForHousing,int count) throws Exception {
		WebParam webParam= new WebParam();
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskHousing);
		String url = "http://58.215.195.18:10010/mx_info.do";	
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("zjlx", "1"));
		paramsList.add(new NameValuePair("hjstatus", "%D5%FD%B3%A3%BB%E3%BD%C9"));
		paramsList.add(new NameValuePair("submit", "%B2%E9++%D1%AF"));
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "58.215.195.18:10010");
		webRequest.setAdditionalHeader("Origin", "http://58.215.195.18:10010");
		webRequest.setAdditionalHeader("Referer", "http://58.215.195.18:10010/mx_info.do?flag=1&temp=0.7225978488818365");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setRequestParameters(paramsList);
		HtmlPage page= webClient.getPage(webRequest);
		List<HousingWuxiPaydetails> paydetails=new ArrayList<HousingWuxiPaydetails>();
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getPaydetails", "<xmp>"+html+"</xmp>");
		webParam.setHtml(html);
		webParam.setPage(page);
	   if (html.contains("scrollDiv")) {
		    paydetails=housingFundWuXiParser.htmlPaydetailsParser(html, taskHousing);
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
		return webParam;
	}
	public WebParam  getUserInfo(MessageLoginForHousing messageLoginForHousing,int count) throws Exception {
		WebParam webParam= new WebParam();
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		webClient = addcookie(webClient,taskHousing);		
	    String url = "http://58.215.195.18:10010/zg_info.do";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "58.215.195.18:10010");
		webRequest.setAdditionalHeader("Referer", "http://58.215.195.18:10010/menu.do?zjlxCheck=null&temp=0.4599769082889047");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page = webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getUserInfo", "<xmp>"+html+"</xmp>");
		HousingWuxiUserInfo userInfo=new HousingWuxiUserInfo();
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		if (html.contains("listView")) {
			userInfo=housingFundWuXiParser.htmlUserInfoParser(html,taskHousing);
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
