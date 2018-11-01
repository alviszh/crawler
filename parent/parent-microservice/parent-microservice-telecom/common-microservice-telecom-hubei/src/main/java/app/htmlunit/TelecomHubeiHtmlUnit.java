package app.htmlunit;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiAccount;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiCallrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiPaymonths;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiPointrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiRecharges;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiServices;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiSmsrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiUserinfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.TelecomHubeiParser;
import app.service.ChaoJiYingOcrService;

@Component
public class TelecomHubeiHtmlUnit {

	public  final Logger log = LoggerFactory.getLogger(TelecomHubeiHtmlUnit.class);
	@Autowired
	private TelecomHubeiParser telecomHubeiParser;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	//获取用户基本信息
	public WebParam getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile,int count) throws Exception {
		tracer.addTag("getUserInfo",messageLogin.getTask_id());
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		webClient = addcookie(webClient,taskMobile);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		String url = "http://hb.189.cn/pages/selfservice/custinfo/userinfo/userInfo.action";
	    WebRequest webRequest= new WebRequest(new URL(url),HttpMethod.GET);			    
	    webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
	    webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate"); 	
	    webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9"); 		
	    webRequest.setAdditionalHeader("Connection", "keep-alive");	
	    webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/hbuserCenter.action");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
		
		HtmlPage userInfoPage = webClient.getPage(webRequest);  	
		tracer.addTag("login getUserInfo","<xmp>"+userInfoPage.asXml()+"</xmp>");
		tracer.addTag("login getUserInfo",userInfoPage.getUrl().toString());
    	String currentPage = userInfoPage.getUrl().toString();	
    	int statusCode=userInfoPage.getWebResponse().getStatusCode();
	   	String html = userInfoPage.asXml();
	   	tracer.addTag("getUserInfo","<xmp>"+html+"</xmp>");
		webParam.setCode(statusCode);
     	webParam.setUrl(currentPage); 
		webParam.setHtml(html);
		tracer.addTag("statusCode",statusCode+"");
		if (200==statusCode) { 
			if (null != html && html.contains("yhnc1")) {
				tracer.addTag("success else","登陆成功");
				TelecomHubeiUserinfo userInfo = telecomHubeiParser.htmlUserInfoParser(html,taskMobile);
				tracer.addTag("userInfo",userInfo.toString());
				webParam.setUserinfo(userInfo);
				webParam.setWebClient(webClient);
			}else{
				tracer.addTag("爬取用户信息为空",taskMobile.getTaskid());
			}
		}
	
		return webParam;
	}
   //获取账户信息
	public WebParam getAccountInfo(MessageLogin messageLogin, TaskMobile taskMobile,int count) throws Exception {
		tracer.addTag("getAccountInfo",messageLogin.getTask_id());
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();			
		webClient = addcookie(webClient,taskMobile);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://hb.189.cn/queryFeesYue.action";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("cityname", taskMobile.getCity()));
		paramsList.add(new NameValuePair("username", messageLogin.getUsername()));
	    WebRequest webRequest= new WebRequest(new URL(url),HttpMethod.POST);		
	    webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/feesquery/feesyue_new.jsp"); 
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate"); 		
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"); 
		webRequest.setAdditionalHeader("Connection", "keep-alive"); 
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);  
		Thread.sleep(1500);
		int statusCode = page.getWebResponse().getStatusCode();
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("getAccountInfo", html);
		webParam.setCode(statusCode);
		webParam.setUrl(url);
		webParam.setHtml(html);
		if (200 == statusCode) {
			if (null != html && html.contains(messageLogin.getName())) {
				TelecomHubeiAccount accountInfo = telecomHubeiParser.htmlAccountInfoParser(html, taskMobile);
				webParam.setAccountinfo(accountInfo);
			} else {
				if (count < 3) {
					count++;
					tracer.addTag("parser.crawler.getAccountInfo.count" + count, "这是第" + count + "次获取账户信息");
					Thread.sleep(1500);
					getAccountInfo(messageLogin, taskMobile, count);
				}
			}
		}
		return webParam;	
	}

   //语言详单
   public WebParam getVoiceRecord(MessageLogin messageLogin, TaskMobile taskMobile,String month,int count) throws Exception {
	    tracer.addTag("getVoiceRecord",messageLogin.getTask_id());	
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();				
		webClient = addcookie(webClient,taskMobile);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url="http://hb.189.cn/feesquery_querylist.action";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("startMonth", month+"0000"));
		paramsList.add(new NameValuePair("type", "5"));
		paramsList.add(new NameValuePair("random", messageLogin.getSms_code()));	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);		
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate"); 
		webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp"); 
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
		webRequest.setAdditionalHeader("Connection", "keep-alive"); 
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 	
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
		webRequest.setRequestParameters(paramsList);
		HtmlPage page = webClient.getPage(webRequest);
		Thread.sleep(2500);
	    int statusCode = page.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.asXml();
		webParam.setHtml(html);
	 	tracer.addTag("getVoiceRecord"+month,"<xmp>"+html+"</xmp>");				
		if (null !=html && html.contains("xd01")) {	
			List<TelecomHubeiCallrecords>  callrecords=telecomHubeiParser.htmlCallrecordsParser(html,month,taskMobile);				 
			webParam.setCallrecords(callrecords);	
			Document doc = Jsoup.parse(html, "utf-8");			
			Element div= doc.getElementById("queryListCount");	
			if (null !=div) {
				String number=div.attr("value");
			 	tracer.addTag("getVoiceRecord number",number);		
			    webParam.setNumber(number);  	
			}	  
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getVoiceRecord.count" + count, "这是第" + count + "次获取语音详单信息");
				Thread.sleep(1000);
				getVoiceRecord(messageLogin, taskMobile,month, count);
			}
		}	
	   return webParam;
	}
 //语言详单
   public WebParam getOtherPageVoiceRecord(MessageLogin messageLogin, TaskMobile taskMobile,String month,int pageNumber,int count) throws Exception {
	    tracer.addTag("getOtherPageVoiceRecord",messageLogin.getTask_id());	
	    WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		webClient = addcookie(webClient,taskMobile);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url="http://hb.189.cn/feesquery_pageQuery.action";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("page", pageNumber+""));
		paramsList.add(new NameValuePair("showCount", "20"));			
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);		
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp"); 
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 
		webRequest.setAdditionalHeader("Connection", "keep-alive"); 
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
		webRequest.setRequestParameters(paramsList);
		HtmlPage page = webClient.getPage(webRequest);
		Thread.sleep(2500);
	    int statusCode = page.getWebResponse().getStatusCode();
	    tracer.addTag("getOtherPageVoiceRecord statusCode",statusCode+"");		
		webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.asXml();
		webParam.setHtml(html);
	 	tracer.addTag("getOtherPageVoiceRecord"+month+"第"+pageNumber+"页","<xmp>"+html+"</xmp>");				
		if (null !=html && html.contains("xd01")) {			
			List<TelecomHubeiCallrecords>  callrecords=telecomHubeiParser.htmlCallrecordsParser(html,month,taskMobile);	
			if (callrecords !=null && !callrecords.isEmpty()) {
				webParam.setCallrecords(callrecords);
			}				    			    		   
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getOtherPageVoiceRecord.count" + count, "这是第" + count + "次获取语音详单信息");
				Thread.sleep(1500);
				getOtherPageVoiceRecord(messageLogin, taskMobile,month,pageNumber, count);
			}
		}	
	    return webParam;   
	}
  
    //充值记录
   public WebParam getRechargeRecord(MessageLogin messageLogin, TaskMobile taskMobile,String month ,int count) throws Exception {
	    tracer.addTag("getRechargeRecord",messageLogin.getTask_id());	
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		webClient = addcookie(webClient,taskMobile);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url="http://hb.189.cn/pages/selfservice/qrybill/rechargeHistoryAjax/queryRH.action";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("accNbr","50:"+messageLogin.getName()));
		paramsList.add(new NameValuePair("billing_Cycle", month));			
		paramsList.add(new NameValuePair("queryType", "1"));
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/qrybill/rechargeHistory/queryRechargeHistory.action");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
		webRequest.setCharset(Charset.forName("UTF-8")); 		
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);
		Thread.sleep(2500);
	    int statusCode = page.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
		webParam.setHtml(html);
	 	tracer.addTag("getRechargeRecord html",html);
	    if (null !=html && html.contains("resultList")) {
	    	List<TelecomHubeiRecharges>  recharges=telecomHubeiParser.htmlRechargeRecordsParser(html,month,taskMobile);	
	     	webParam.setRecharges(recharges);    					    						    	
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getRechargeRecord.count" + count, "这是第" + count + "次充值信息");
				Thread.sleep(1500);
				getRechargeRecord(messageLogin, taskMobile,month, count);
			}
		}	
	    return webParam;	
	}  
   //积分记录
  public WebParam getPointRecord(MessageLogin messageLogin, TaskMobile taskMobile,String month ,int count) throws Exception {
	    tracer.addTag("getPointRecord",messageLogin.getTask_id());
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient = addcookie(webClient,taskMobile);
		String url="http://hb.189.cn/service/integral/qryResult.action";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("qryMonth",month));
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/service/integral/qryIndex.action");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
		webRequest.setCharset(Charset.forName("UTF-8")); 		
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);
		Thread.sleep(2500);
	    int statusCode = page.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
		webParam.setHtml(html);
	 	tracer.addTag("getPointRecord"+month,"<xmp>"+html+"</xmp>");
	    if (null !=html && html.contains("idtable")) {
	    	List<TelecomHubeiPointrecords>  pointrecords=telecomHubeiParser.htmlPointRecordsParser(html,month,taskMobile);	
	    	webParam.setPointrecords(pointrecords);	    
		}else{
			if (count < 1) {
				count++;
				tracer.addTag("parser.crawler.getPointRecord.count" + count, "这是第" + count + "次获取积分信息");
				Thread.sleep(1000);
				getPointRecord(messageLogin, taskMobile,month, count);
			}
		}	
	    return webParam;	
	}
   //短信详单
  public WebParam getSmsRecord(MessageLogin messageLogin, TaskMobile taskMobile,String month,int count) throws Exception {
	    tracer.addTag("getSmsRecord",messageLogin.getTask_id());
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient = addcookie(webClient,taskMobile);
		String url="http://hb.189.cn/feesquery_querylist.action";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);	
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("startMonth",month+"0000"));
		paramsList.add(new NameValuePair("type", "7"));
		paramsList.add(new NameValuePair("random", messageLogin.getSms_code()));							
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp"); 
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestParameters(paramsList);
		HtmlPage page = webClient.getPage(webRequest);
		Thread.sleep(2500);
	    int statusCode = page.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.asXml();
    	tracer.addTag("getSmsRecord"+month,"<xmp>"+html+"</xmp>");	
		webParam.setHtml(html);	
		if (null != html && html.contains("xd01")) {
			List<TelecomHubeiSmsrecords> smsrecords = telecomHubeiParser.htmlSmsrecordsParser(html, month, taskMobile);
			webParam.setSmsrecords(smsrecords);
			Document doc = Jsoup.parse(html, "utf-8");
			Element div = doc.getElementById("queryListCount");
			if (null != div) {
				String number = div.attr("value");
				tracer.addTag("getVoiceRecord number", number);
				webParam.setNumber(number);
			}
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getSmsRecord.count" + count, "这是第" + count + "次短信详单信息");
				Thread.sleep(1500);
				getSmsRecord(messageLogin, taskMobile,month, count);
			}
		}	
		return webParam;   
	}
  
  //短信详单
  public WebParam getOtherPageSmsRecord(MessageLogin messageLogin, TaskMobile taskMobile,String month,int pageNumber,int count) throws Exception {
	    tracer.addTag("getOtherPageSmsRecord",messageLogin.getTask_id());
	    WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient = addcookie(webClient,taskMobile);	
		String url="http://hb.189.cn/feesquery_pageQuery.action";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("page", pageNumber+""));
		paramsList.add(new NameValuePair("showCount", "20"));			
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp"); 
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 
		webRequest.setAdditionalHeader("Connection", "keep-alive"); 
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
		webRequest.setRequestParameters(paramsList);
		HtmlPage page = webClient.getPage(webRequest);
		Thread.sleep(2500);
	    int statusCode = page.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.asXml();
    	tracer.addTag("getOtherPageSmsRecord"+month+"第"+pageNumber+"页","<xmp>"+html+"</xmp>");	
		webParam.setHtml(html);	
		if (null !=html && html.contains("xd01")) {					
			List<TelecomHubeiSmsrecords> smsrecords=telecomHubeiParser.htmlSmsrecordsParser(html, month,taskMobile);   
			webParam.setSmsrecords(smsrecords);	    	 
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getOtherPageSmsRecord.count" + count, "这是第" + count + "次短信详单信息");
				Thread.sleep(1500);
				getOtherPageSmsRecord(messageLogin, taskMobile,month,pageNumber,count);
			}
		}		
		return webParam;  
	}
  //获取月账单信息
  public WebParam getPaymonths(MessageLogin messageLogin, TaskMobile taskMobile,String month,int count) throws Exception {
	    tracer.addTag("getPaymonths",messageLogin.getTask_id());
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient = addcookie(webClient,taskMobile);
		String url = "http://hb.189.cn/pages/selfservice/feesquery/newBOSSQueryCustBill.action?billbeanos.citycode="+taskMobile.getAreacode()+"&billbeanos.btime="
				+ month + "&billbeanos.accnbr="+messageLogin.getName()
				+ "&skipmethod.cityname="+URLEncoder.encode(taskMobile.getCity(), "GBK")+"&billbeanos.paymode=2";
		WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);  
		Thread.sleep(1500);
	    int statusCode = page.getWebResponse().getStatusCode();	
		webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
    	webParam.setHtml(html);
	 	tracer.addTag("getPaymonths"+month,"<xmp>"+html+"</xmp>");
		if (200==statusCode) {		
		    if (html.contains(messageLogin.getName()) && html.contains("lszd_month") && html.contains(messageLogin.getName())) {
		    	List<TelecomHubeiPaymonths>  paymonths=telecomHubeiParser.htmlPaymonthParser(html,taskMobile,month);	
		    	webParam.setPaymonths(paymonths);							    			
			}else{
				if (count < 3) {
					count++;
					tracer.addTag("parser.crawler.getPaymonths.count" + count, "这是第" + count + "次月账单信息");
					Thread.sleep(1000);
					getPaymonths(messageLogin, taskMobile,month,count);
				}
			}
		}		
	   return webParam;
	}   
   
  //获取套餐信息
  public WebParam getServiceInfo(MessageLogin messageLogin, TaskMobile taskMobile,int count) throws Exception {
	   tracer.addTag("getServiceInfo",messageLogin.getTask_id());
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskMobile);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url="http://hb.189.cn/co/qryOldBusiness.action";
	    WebRequest webRequest= new WebRequest(new URL(url),HttpMethod.POST);		
	    webRequest.setAdditionalHeader("Accept", "*/*");
	    webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");	
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/business/coo/functiondisplay/oldBussiness.jsp");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
		HtmlPage servicePage = webClient.getPage(webRequest);  		
		Thread.sleep(2500);
	    int statusCode = servicePage.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = servicePage.getWebResponse().getContentAsString();
		webParam.setHtml(html);
	 	tracer.addTag("getServiceInfo","<xmp>"+html+"</xmp>");
		if (200==statusCode) {
	    	if (null !=html && html.contains("hovergray")) {
	    		List<TelecomHubeiServices>  services=telecomHubeiParser.htmlServiceinfosParser(html,taskMobile);
	    		webParam.setServices(services); 
			}else{
				if (count < 3) {
					count++;
					tracer.addTag("parser.crawler.getServiceInfo.count" + count, "这是第" + count + "次获取套餐信息");
					Thread.sleep(1500);
					getServiceInfo(messageLogin, taskMobile, count);
				}
			}				
		}		
	   return webParam;
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
			tracer.addTag("telecomhubei ", e.getMessage());
			return null;
		}

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
	public  Page gethtmlPostByWebRequest(WebClient webClient, WebRequest webRequest, String url) {
		try {
			webClient.getOptions().setJavaScriptEnabled(false);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomhubei ", e.getMessage());
			return null;
		}

	}

	public  HtmlPage getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
			HtmlPage searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomhubei ", e.getMessage());
			return null;
		}

	}
	public  Page getPage(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setRedirectEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomhubei ", e.getMessage());
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
