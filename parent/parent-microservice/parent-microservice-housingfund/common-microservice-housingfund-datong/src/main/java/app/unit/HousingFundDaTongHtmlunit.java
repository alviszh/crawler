package app.unit;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.datong.HousingDaTongPaydetails;
import com.microservice.dao.entity.crawler.housing.datong.HousingDaTongUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundDaTongParser;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONObject;

@Component
public class HousingFundDaTongHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundDaTongHtmlunit.class);
	@Autowired
	private HousingFundDaTongParser housingFundDaTongParser;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int count) throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		String url = "http://121.30.239.174:90/netface/login.do";		
		HtmlPage page=getHtml(url,webClient);
		String html=page.getWebResponse().getContentAsString();
		System.out.println(html);
	    tracer.addTag("parser.crawler.login.page", "<xmp>"+html+"</xmp>");
		if(200 == page.getWebResponse().getStatusCode()){					
			if (page.getWebResponse().getContentAsString().contains("username")) {
				Document doc = Jsoup.parse(html, "utf-8"); 
				Element codeBoxDiv=doc.getElementById("codeBox");
				String dispaly=codeBoxDiv.attr("style");
				String code="";
				if (!dispaly.contains("none")) {
					HtmlImage image = page.getFirstByXPath("//img[@id='codeimg']");
				    code = chaoJiYingOcrService.getVerifycode(image, "1004");
					tracer.addTag("verifyCode ==>", code);
				}			
				//登陆
				String loginUrl = "http://121.30.239.174:90/netface/login.do?r=0.494040619643076";		                     
				WebRequest webRequest2 = new WebRequest(new URL(loginUrl), HttpMethod.POST);	
				String requestBody="";
				if (messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getACCOUNT_NUM())) {
				   requestBody="username="+messageLoginForHousing.getHosingFundNumber()+"&password="+messageLoginForHousing.getPassword()+"&loginType=3&vertcode="+code+"&bsr=chrome%2F64.0.3282.119&vertype=1";					
				}else{
				  requestBody="username="+messageLoginForHousing.getNum()+"&password="+messageLoginForHousing.getPassword()+"&loginType=4&vertcode="+code+"&bsr=chrome%2F64.0.3282.119&vertype=1";
				}      				
				webRequest2.setAdditionalHeader("Accept", "*/*");
				webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				webRequest2.setAdditionalHeader("Connection", "keep-alive");
				webRequest2.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
				webRequest2.setAdditionalHeader("Host", "121.30.239.174:90");
				webRequest2.setAdditionalHeader("Origin", "http://121.30.239.174:90");
				webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
				webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				webRequest2.setRequestBody(requestBody);
				Page loginPage = webClient.getPage(webRequest2);
				Thread.sleep(1500);
				String loginHtml=loginPage.getWebResponse().getContentAsString();
				tracer.addTag("parser.crawler.login.loginPage", "<xmp>"+loginHtml+"</xmp>");	
				webParam.setHtml(loginHtml);
				webParam.setWebClient(webClient);
				webParam.setUrl(loginUrl);
				JSONObject jsonObject = JSONObject.fromObject(loginHtml);
				String successStr = jsonObject.getString("success");				
				if (successStr.contains("true")) {
					webParam.setLogin(true);
					webParam.setHtml(loginHtml);
				}else{
					String msgStr = jsonObject.getString("msg");	
					webParam.setLogin(false);
					webParam.setMessage(msgStr);
					if (count < 3) {
						count++;
						tracer.addTag("parser.crawler.login.count" + count, "这是第" + count + "次登陆");
						Thread.sleep(2500);
						login(messageLoginForHousing, taskHousing, count);
					}					
				}
			}
		}
		return webParam;	
	}
	

	public WebParam  getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		webClient = addcookie(webClient,taskHousing);				
		String urlIndex="http://121.30.239.174:90/netface/index.do";
		HtmlPage pageIndex= getHtml(urlIndex,  webClient);
		tracer.addTag("parser.crawler.getUserInfo.pageIndex", "<xmp>"+pageIndex.getWebResponse().getContentAsString()+"</xmp>");		
	    String url = "http://121.30.239.174:90/netface/per/queryPerInfo.do";
	    WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "121.30.239.174:90");
		webRequest.setAdditionalHeader("Referer", "http://121.30.239.174:90/netface/index.do");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
	    Page page = webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getUserInfo", "<xmp>"+html+"</xmp>");
		HousingDaTongUserInfo userInfo=new HousingDaTongUserInfo();
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		if (html.contains("user_info_table")) {
			userInfo=housingFundDaTongParser.htmlUserInfoParser(html, taskHousing);
			webParam.setUserInfo(userInfo);
			webParam.setPage(page);
			webParam.setHtml(html);
		}
		return webParam;
	}
	
	public WebParam  getPaydetails(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		webClient = addcookie(webClient,taskHousing);		
	    String url = "http://121.30.239.174:90/netface/per/queryPerDeposit!queryPerByYear.do";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Date date = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dateNowStr = sdf.format(date);  
		String requestBody="dto%5B'startdate'%5D=2017-01-01&dto%5B'enddate'%5D="+dateNowStr+"&gridInfo%5B'dataList_limit'%5D=50&gridInfo%5B'dataList_start'%5D=0&";	
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "121.30.239.174:90");
		webRequest.setAdditionalHeader("Origin", "http://121.30.239.174:90");
		webRequest.setAdditionalHeader("Referer", "http://121.30.239.174:90/netface/per/queryPerDeposit.do");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestBody(requestBody);
	    Page page = webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getPaydetails", "<xmp>"+html+"</xmp>");
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		List<HousingDaTongPaydetails> paydetails=new ArrayList<HousingDaTongPaydetails>();
		   if (html.contains("dataList")) {
			    paydetails=housingFundDaTongParser.htmlPaydetailsParser(html, taskHousing);
				webParam.setPaydetails(paydetails);
				webParam.setHtml(html);
				webParam.setPage(page);
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

	public HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
