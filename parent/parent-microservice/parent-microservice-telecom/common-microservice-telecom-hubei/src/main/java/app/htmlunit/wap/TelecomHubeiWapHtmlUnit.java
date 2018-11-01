package app.htmlunit.wap;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapCallrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapPaymonths;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapPointrecord;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapRecharges;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapUserinfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WapParam;
import app.parser.TelecomHubeiWapParser;


@Component
public class TelecomHubeiWapHtmlUnit {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomHubeiWapParser telecomHubeiWapParser;
	@Autowired
	private TelecomHubeiWapCommon telecomHubeiWapCommon;
	//获取用户基本信息
	public WapParam getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile,int count) throws Exception {
		tracer.addTag("getUserInfo",messageLogin.getTask_id());
		WapParam wapParam= new WapParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		webClient = telecomHubeiWapCommon.addcookie(webClient, taskMobile);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://waphb.189.cn/info/queryUserInfo.htm";
	    WebRequest webRequest= new WebRequest(new URL(url),HttpMethod.POST);			    
	    webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate"); 		
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9"); 
	    webRequest.setAdditionalHeader("Connection", "keep-alive");	
	    webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://waphb.189.cn/service/userinfo.jsp");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");		
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page userInfoPage = webClient.getPage(webRequest);  	
        Thread.sleep(1500);
		String html = userInfoPage.getWebResponse().getContentAsString();
		System.out.println("getUserInfo=="+html);
		tracer.addTag("getUserInfo.result",html);
    	int statusCode=userInfoPage.getWebResponse().getStatusCode();
	   	wapParam.setCode(statusCode);
	   	wapParam.setUrl(url); 
	   	wapParam.setHtml(html);
	   	if (200==statusCode) {
	   		if (null != html && html.contains("serv")) {
				tracer.addTag("success else","登陆成功");
				tracer.addTag("getUserInfo.html",html);
				TelecomHubeiWapUserinfo userInfo = telecomHubeiWapParser.htmlUserInfoParser(html,taskMobile);			
				wapParam.setUserinfo(userInfo);
				wapParam.setWebClient(webClient);
			}else{
				tracer.addTag("爬取用户信息为空,结果html"+html,taskMobile.getTaskid());
			}	
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getUserInfo.count" + count, "这是第" + count + "次获取用户");
				Thread.sleep(1000);
				getUserInfo(messageLogin, taskMobile, count);
			}
		}
		
	   return wapParam;
	}
   //语言详单
   public WapParam getVoiceRecord(MessageLogin messageLogin, TaskMobile taskMobile,String month,int count) throws Exception {
	    tracer.addTag("getVoiceRecord",messageLogin.getTask_id());	
		WapParam wapParam= new WapParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient = telecomHubeiWapCommon.addcookie(webClient, taskMobile);
		String url="http://waphb.189.cn/billQuery/custBillDetail.htm";
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
        requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("queryType", "14"));
		requestSettings.getRequestParameters().add(new NameValuePair("billCycle", month));
		requestSettings.getRequestParameters().add(new NameValuePair("servId", "19262371823"));
		requestSettings.getRequestParameters().add(new NameValuePair("latnId", "1001"));  
		requestSettings.getRequestParameters().add(new NameValuePair("paymentMode", "1"));  
	
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate"); 	
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9"); 
		requestSettings.setAdditionalHeader("Connection", "keep-alive"); 
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"); 
		requestSettings.setAdditionalHeader("Host", "waphb.189.cn");
		requestSettings.setAdditionalHeader("Origin", "http://waphb.189.cn");
		requestSettings.setAdditionalHeader("Referer", "http://waphb.189.cn/service/custBillDetail.jsp");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");				
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");		
		Page page = webClient.getPage(requestSettings);
		Thread.sleep(1500);
	    int statusCode = page.getWebResponse().getStatusCode();
	    wapParam.setCode(statusCode);
	    wapParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
    	wapParam.setHtml(html);
	 	tracer.addTag("getVoiceRecord"+month,html);	
	 	if (200==statusCode) {
	 		if (null !=html && html.contains("billDetail")) {	
	 			tracer.addTag(month+"月getVoiceRecord.html",html);
				List<TelecomHubeiWapCallrecords>  callrecords=telecomHubeiWapParser.htmlCallrecordsParser(html,month,taskMobile);				 
				wapParam.setCallrecords(callrecords);		 
			}else{
				tracer.addTag(month+"月getVoiceRecord结果为空",html);
			}
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getVoiceRecord.count" + count, "这是第" + count + "次获取通话详单信息");
				Thread.sleep(1000);
				getVoiceRecord(messageLogin, taskMobile,month, count);
			}
		}		
	   return wapParam;
	}
    //充值记录
   public WapParam getRechargeRecord(MessageLogin messageLogin, TaskMobile taskMobile,String month ,int count) throws Exception {
	    tracer.addTag("getRechargeRecord",messageLogin.getTask_id());	
		WapParam wapParam= new WapParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		webClient = telecomHubeiWapCommon.addcookie(webClient, taskMobile);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url="http://waphb.189.cn/billQuery/paymentQueryOnestyle.htm";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();	
		paramsList.add(new NameValuePair("billCycle", month));			
		paramsList.add(new NameValuePair("task_remark", null));
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
		Thread.sleep(1500);
	    int statusCode = page.getWebResponse().getStatusCode();
	    wapParam.setCode(statusCode);
	    wapParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
    	wapParam.setHtml(html);
	 	tracer.addTag("getRechargeRecord html",html);
	 	if (200==statusCode) {
	 		 if (null !=html && html.contains("paymentRecordList")) {
	 			tracer.addTag(month+"月 getRechargeRecord.html",html);
	 	    	List<TelecomHubeiWapRecharges>  recharges=telecomHubeiWapParser.htmlRechargeRecordsParser(html,month,taskMobile);	
	 	    	wapParam.setRecharges(recharges);    					    						    	
	 		}else{
	 			tracer.addTag(month+"月 getVoiceRecord结果为空",html);
	 		}
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getRechargeRecord.count" + count, "这是第" + count + "次获取充值信息");
				Thread.sleep(1500);
				getRechargeRecord(messageLogin, taskMobile,month, count);
			}
		}
	    return wapParam;	
	}  
   //积分记录
  public WapParam getPointRecord(MessageLogin messageLogin, TaskMobile taskMobile,int count) throws Exception {
	    tracer.addTag("getPointRecord",messageLogin.getTask_id());
		WapParam wapParam= new WapParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient = telecomHubeiWapCommon.addcookie(webClient, taskMobile);
		String url="http://waphb.189.cn/service/scorequery.jsp";	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "waphb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://waphb.189.cn/service/nav.jsp?tag=1");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
		webRequest.setCharset(Charset.forName("UTF-8")); 		
		Page page = webClient.getPage(webRequest);
		Thread.sleep(1500);
	    int statusCode = page.getWebResponse().getStatusCode();
	    wapParam.setCode(statusCode);
	    wapParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
    	wapParam.setHtml(html);
	 	tracer.addTag("getPointRecord",html);
	 	if (200==statusCode) {
	 		 if (null !=html && html.contains("pointNow")) {
	 			tracer.addTag("getPointRecord.html",html);
	 	    	TelecomHubeiWapPointrecord  pointrecord=telecomHubeiWapParser.htmlPointRecordParser(html,taskMobile);	
	 	    	wapParam.setPointrecord(pointrecord);
	 		}else{
	 			tracer.addTag("getPointRecord结果为空",html);
	 		}
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getPointRecord.count" + count, "这是第" + count + "次获取积分信息");
				Thread.sleep(1000);
				getPointRecord(messageLogin, taskMobile, count);
			}
		}
	    return wapParam;	
	}
 
  //获取月账单信息
  public WapParam getPaymonths(MessageLogin messageLogin, TaskMobile taskMobile,String month,int count) throws Exception {
	    tracer.addTag("getPaymonths",messageLogin.getTask_id());
		WapParam webParam= new WapParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = telecomHubeiWapCommon.addcookie(webClient, taskMobile);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://waphb.189.cn/billQuery/billQueryList.htm";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("billCycle", month));			
		paramsList.add(new NameValuePair("task_remark", null));
		WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "waphb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://waphb.189.cn"); 
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Mobile Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);  
		Thread.sleep(1500);
	    int statusCode = page.getWebResponse().getStatusCode();	
		webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
    	webParam.setHtml(html);
	 	tracer.addTag("getPaymonths"+month,html);
		if (200==statusCode) {		
		    if (html.contains(messageLogin.getName()) && html.contains("itemInforList")) {
		    	List<TelecomHubeiWapPaymonths>  paymonths=telecomHubeiWapParser.htmlPaymonthParser(html,taskMobile,month);	
		    	webParam.setPaymonths(paymonths);							    			
			}else{
				if (count < 3) {
					count++;
					tracer.addTag("parser.crawler.getPaymonths.count" + count, "这是第" + count + "次获取月账单信息");
					Thread.sleep(1000);
					getPaymonths(messageLogin, taskMobile,month,count);
				}
			}
		}		
	   return webParam;
	}   

}
