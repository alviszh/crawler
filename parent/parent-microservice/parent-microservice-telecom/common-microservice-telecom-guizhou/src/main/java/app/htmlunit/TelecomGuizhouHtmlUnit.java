package app.htmlunit;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouAccount;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouCallrecord;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouPaymonth;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouPoint;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouRecharges;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouSmsrecord;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.TelecomGuizhouBasicParser;

@Component
public class TelecomGuizhouHtmlUnit {

	public  final Logger log = LoggerFactory.getLogger(TelecomGuizhouHtmlUnit.class);
	@Autowired
	private TelecomGuizhouBasicParser telecomGuizhouBasicParser;
	@Autowired
	private TracerLog tracer;
    //获取账户信息
	public WebParam getAccountInfo(MessageLogin messageLogin, TaskMobile taskMobile,int count) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskMobile);
		HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00320352", webClient);
		webParam.setWebClient(webClient);
		if (200==htmlPage.getWebResponse().getStatusCode()) {
			String url="http://service.gz.189.cn/web2.0/query2.php?action=getmoney";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "service.gz.189.cn");	
			webRequest.setAdditionalHeader("Referer", "http://service.gz.189.cn/web2.0/query2.php?action=bill");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36"); 
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
			Page page = webClient.getPage(webRequest);		
			Thread.sleep(1000);
		    int statusCode = page.getWebResponse().getStatusCode();
			webParam.setCode(statusCode);
	     	webParam.setUrl(url);
	    	String html = page.getWebResponse().getContentAsString();
			webParam.setHtml(html);
		 	tracer.addTagWrap("getAccountInfo",html);
		 	TelecomGuizhouAccount  accountInfo=new TelecomGuizhouAccount();
		    if (200==statusCode) {
		    	if (null !=html && html.contains("Balance")) {
		    	   	accountInfo=telecomGuizhouBasicParser.htmlAccountInfoParser(html,taskMobile);
		    	   	webParam.setAccount(accountInfo);
				}		    	
			}		
		}		
	   return webParam;
	}

   //语言详单
   public WebParam getVoiceRecord(MessageLogin messageLogin, TaskMobile taskMobile,String month) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient,taskMobile);
		HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00320352", webClient);
		webParam.setWebClient(webClient);
		if (200==htmlPage.getWebResponse().getStatusCode()) {
			String url="http://service.gz.189.cn/web/query.php?_=1505100713091&action=getAllCall&QueryMonthly="+month+"&QueryType=1&checkcode="+messageLogin.getSms_code();
			tracer.addTagWrap("getVoiceRecord"+month,url);		
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "service.gz.189.cn");	
			webRequest.setAdditionalHeader("Referer", "http://service.gz.189.cn/web/query.php?action=call&fastcode=00320353&cityCode=gz");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36"); 
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");			
			Page page = webClient.getPage(webRequest);
			Thread.sleep(1000);
		    int statusCode = page.getWebResponse().getStatusCode();
			webParam.setCode(statusCode);
	     	webParam.setUrl(url);
	    	String html = page.getWebResponse().getContentAsString();
			webParam.setHtml(html);
		 	tracer.addTagWrap("getVoiceRecord"+month,html);				
			if (null !=html && html.contains("CDMA_CALL_CDR")) {	
				List<TelecomGuizhouCallrecord>  callrecords=telecomGuizhouBasicParser.htmlCallrecordsParser(html,taskMobile);	 
				webParam.setCallrecords(callrecords);		    	
			}
		}		
	   return webParam;
	}
   
    //充值记录
   public WebParam getRechargeRecord(MessageLogin messageLogin, TaskMobile taskMobile,int count) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskMobile);
		HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00320352", webClient);
		if (200==htmlPage.getWebResponse().getStatusCode()) {
			String url="http://service.gz.189.cn/web2.0/query2.php?action=payrecord";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "service.gz.189.cn");	
			webRequest.setAdditionalHeader("Referer", "http://service.gz.189.cn/web2.0/query2.php?action=mybill");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36"); 
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");			
			Page page = webClient.getPage(webRequest);	
			Thread.sleep(1000);
		    int statusCode = page.getWebResponse().getStatusCode();		   
			webParam.setCode(statusCode);
	     	webParam.setUrl(url);
	    	String html = page.getWebResponse().getContentAsString();
			webParam.setHtml(html);
		 	tracer.addTagWrap("getRechargeRecord",html);
		    if (null !=html && html.contains("records")) {
		    	List<TelecomGuizhouRecharges>  recharges=telecomGuizhouBasicParser.htmlRechargeRecordsParser(html,taskMobile);	    		
	    		webParam.setRecharges(recharges);		    	
			}
		}		
	   return webParam;
	}
   //短信详单
  public WebParam getSmsRecord(MessageLogin messageLogin, TaskMobile taskMobile,String month) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskMobile);
		HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00320352", webClient);
		webParam.setWebClient(webClient);
		if (200==htmlPage.getWebResponse().getStatusCode()) {
			String url="http://service.gz.189.cn/web/query.php?_=1505093738415&action=getAllCall&QueryMonthly="+month+"&QueryType=2&checkcode"+messageLogin.getSms_code();			           
			tracer.addTagWrap("getSmsRecord"+month,url);		
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "service.gz.189.cn");	
			webRequest.setAdditionalHeader("Referer", "http://service.gz.189.cn/web/query.php?action=call&fastcode=00320353&cityCode=gz");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36"); 
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");			
			Page page = webClient.getPage(webRequest);
			Thread.sleep(1000);
		    int statusCode = page.getWebResponse().getStatusCode();
			webParam.setCode(statusCode);
	     	webParam.setUrl(url);
	    	String html = page.getWebResponse().getContentAsString();
	    	tracer.addTagWrap("getSmsRecord"+month,html);	
			webParam.setHtml(html);	
			if (null !=html && html.contains("CDMA_SMS_CDR")) {					
				List<TelecomGuizhouSmsrecord>  smsrecords=telecomGuizhouBasicParser.htmlSmsrecordsParser(html, taskMobile);   
				webParam.setSmsrecords(smsrecords); 
			}
		}		
	   return webParam;
	}
  //获取积分信息
  public WebParam getPoints(MessageLogin messageLogin, TaskMobile taskMobile,int count) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskMobile);
		HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00320352", webClient);
		if (200==htmlPage.getWebResponse().getStatusCode()) {
			String url="http://service.gz.189.cn/web2.0/score.php?action=score";	
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "service.gz.189.cn");	
			webRequest.setAdditionalHeader("Referer", "http://www.189.cn/login/sso/ecs.do?method=linkTo&platNo=10024&toStUrl=http://service.gz.189.cn/web/query.php?action=score&fastcode=00320359&cityCode=gz");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36"); 
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 		
			Page page = webClient.getPage(webRequest);	
			Thread.sleep(1000);
		    int statusCode = page.getWebResponse().getStatusCode();
			webParam.setCode(statusCode);
	     	webParam.setUrl(url);
	    	String html = page.getWebResponse().getContentAsString();
			webParam.setHtml(html);
		 	tracer.addTagWrap("getPoints",html);
		    if (html !=null && html.contains("details")) {
		    	TelecomGuizhouPoint point=telecomGuizhouBasicParser.htmlPointParser(html,taskMobile);	    
	    		webParam.setPoint(point);	
			}
		}		
	   return webParam;
	}   
  //获取月账单信息
  public WebParam getPaymonths(MessageLogin messageLogin, TaskMobile taskMobile,String phoneNum,String month) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskMobile);
		HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00320352", webClient);
		webParam.setWebClient(webClient);
		if (200==htmlPage.getWebResponse().getStatusCode()) {
			String url="http://service.gz.189.cn/web2.0/query2.php?action=getcheck";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("month", month));
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "service.gz.189.cn");	
			webRequest.setAdditionalHeader("Referer", "http://www.189.cn/login/sso/ecs.do?method=linkTo&platNo=10024&toStUrl=http://service.gz.189.cn/web/query.php?action=score&fastcode=00320359&cityCode=gz");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36"); 
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 	
			webRequest.setRequestParameters(paramsList);
			Page page = webClient.getPage(webRequest);	
			Thread.sleep(1000);	  
		    int statusCode = page.getWebResponse().getStatusCode();
			webParam.setCode(statusCode);
	     	webParam.setUrl(url);
	    	String html = page.getWebResponse().getContentAsString();
			webParam.setHtml(html);
		 	tracer.addTagWrap("getPaymonths"+month,html);
		    if (html !=null && html.contains("productInfo")) {
		    	List<TelecomGuizhouPaymonth>  paymonths=telecomGuizhouBasicParser.htmlPaymonthParser(html,taskMobile,month,phoneNum);	    
	    		webParam.setPaymonths(paymonths);		
			}
		}		
	   return webParam;
	}   
   
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
	}
  
	public Page gettmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			tracer.addTag("telecomguizhou ", e.getMessage());
			return null;
		}

	}

	public  Page gethtmlPostByWebRequest(WebClient webClient, WebRequest webRequest, String url) {
		try {
			webClient.getOptions().setJavaScriptEnabled(false);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomguizhou ", e.getMessage());
			return null;
		}

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
	public  Page getPage(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
			Page searchPage = webClient.getPage(webRequest);
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
