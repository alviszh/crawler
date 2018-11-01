package app.htmlunit;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
import com.microservice.dao.entity.crawler.mobile.BasicUser;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangAddvalueItem;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangPayMonths;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangPointRecord;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangProductInfo;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangRealtimeFee;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangRechargeRecord;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangSmsRecord;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangUserInfo;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangVoiceRecord;
import com.microservice.dao.repository.crawler.mobile.BasicUserRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.TelecomXinJiangParser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class TelecomXinJiangHtmlUnit {

	public  final Logger log = LoggerFactory.getLogger(TelecomXinJiangHtmlUnit.class);
	@Autowired
	private TelecomXinJiangParser  telecomXinJiangParser;
	@Autowired
	private  BasicUserRepository basicUserRepository;
	@Autowired
	private  TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
    //获取用户信息
	public WebParam getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile,int count){
		WebParam webParam= new WebParam();		
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = addcookie(webClient,taskMobile);
			HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000795", webClient);			
			if (200 == htmlPage.getWebResponse().getStatusCode()) {
				String url = "http://shop.xj.189.cn:8081/xjwt_webapp/AdminLoginWebController/getNumber.do";
				Page page = gettmlPost(webClient, null, url);
				Thread.sleep(2000);
				int statusCode = page.getWebResponse().getStatusCode();
				webParam.setCode(statusCode);
				webParam.setUrl(url);
				String html = page.getWebResponse().getContentAsString();
				webParam.setHtml(html);
				tracer.addTag("getUserInfoHtml", html);
				String accountBalanceStr = getAccountBalance(messageLogin, taskMobile);
				tracer.addTag("accountBalanceStr", accountBalanceStr);
				String ownFee = "";
				TelecomXinjiangUserInfo userInfo=new TelecomXinjiangUserInfo();
				if (null != accountBalanceStr && accountBalanceStr.contains("qfmsg")) {
					JSONArray jsonArray = JSONArray.fromObject(accountBalanceStr);
					JSONObject accountBalanceObj = JSONObject.fromObject(jsonArray.get(0));
					ownFee = accountBalanceObj.getString("qfmsg");
				}
				 if (html != null && html.contains("custInfo")) {
					userInfo = telecomXinJiangParser.htmlUserInfoParser(html);					
					userInfo.setTaskid(taskMobile.getTaskid());
					userInfo.setOwnFee(ownFee);	
					webParam.setUserInfo(userInfo);
					tracer.addTag("getUserInfo.userInfo", userInfo.toString());	
				 } else {
					if (count <=2) {
						count++;
						tracer.addTag("parser.crawler.getUserInfo.count" + count, "这是第" + count + "次获取用户信息");
						Thread.sleep(2000);
						getUserInfo(messageLogin, taskMobile, count);
					}
				}								
		   }			
		} catch (Exception e) {
			tracer.addTag("getUserInfo.Exception:", e.getMessage());
			e.printStackTrace();
		}		
	   return webParam;
	}

	public WebParam getAccountInfo(MessageLogin messageLogin, TaskMobile taskMobile, int count) {
		WebParam webParam = new WebParam();
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = addcookie(webClient, taskMobile);
			HtmlPage htmlPage = getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000795", webClient);
			if (200 == htmlPage.getWebResponse().getStatusCode()) {
				String url = "http://shop.xj.189.cn:8081/xjwt_webapp/AdminLoginWebController/getNumber.do";
				Page page = gettmlPost(webClient, null, url);
				Thread.sleep(1000);
				int statusCode = page.getWebResponse().getStatusCode();
				webParam.setCode(statusCode);
				webParam.setUrl(url);
				String html = page.getWebResponse().getContentAsString();
				webParam.setHtml(html);
				tracer.addTag("getAccountInfo", html);
				TelecomXinjiangProductInfo productInfo = new TelecomXinjiangProductInfo();
				if (html != null && html.contains("custInfo")) {
					productInfo = telecomXinJiangParser.htmlProductInfoParser(html);
					productInfo.setTaskid(taskMobile.getTaskid());
					webParam.setProductInfo(productInfo);
				} else {
					if (count <=2) {
						count++;
						tracer.addTag("parser.crawler.getAccountInfo.count" + count, "这是第" + count + "次获取账户信息");
						Thread.sleep(2000);
						getAccountInfo(messageLogin, taskMobile, count);
					}
				}			
			}
		} catch (Exception e) {
			tracer.addTag("getAccountInfo.Exception", e.getMessage());
			e.printStackTrace();
		}
		return webParam;
	}
   //获取号码实时费用
   public WebParam getRealtimefee(MessageLogin messageLogin, TaskMobile taskMobile,int count)  {
		WebParam webParam= new WebParam();	
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
			webClient = addcookie(webClient,taskMobile);
			HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000795", webClient);
			if (200==htmlPage.getWebResponse().getStatusCode()) {
				String url="http://shop.xj.189.cn:8081/xjwt_webapp/ArrearsController/getNewBillTimePost.do";	
				List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
				paramsList = new ArrayList<NameValuePair>();
				paramsList.add(new NameValuePair("moblieNum", messageLogin.getName()));
				paramsList.add(new NameValuePair("deviceType", "2"));
				paramsList.add(new NameValuePair("AreaCode", taskMobile.getAreacode()));	
				WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				webRequest.setAdditionalHeader("Host", "shop.xj.189.cn:8081");
				webRequest.setAdditionalHeader("Origin", "http://shop.xj.189.cn:8081");
				webRequest.setAdditionalHeader("Referer", "http://shop.xj.189.cn:8081/xjwt_webapp/pcapp/service/billing/arrearsquery/arrearsQuery.jsp?funid=2&fastcode=20000800&cityCode=xj"); 
				webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
				webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"); 
				webRequest.setAdditionalHeader("Connection", "keep-alive"); 
				webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
				webRequest.setRequestParameters(paramsList);
				Page page = webClient.getPage(webRequest);
				Thread.sleep(2000);
			    int statusCode = page.getWebResponse().getStatusCode();
				webParam.setCode(statusCode);
		     	webParam.setUrl(url);
		    	String html = page.getWebResponse().getContentAsString();
				webParam.setHtml(html);
			 	tracer.addTag("getRealtimefee",html);
			 	List<TelecomXinjiangRealtimeFee>  realtimeFees=new ArrayList<TelecomXinjiangRealtimeFee>();
			 	if (html !=null && html.contains("shishiheji")) {
			 	    realtimeFees=telecomXinJiangParser.htmlRealtimeFeeParser(html,taskMobile);		  
			 	 	webParam.setRealtimeFees(realtimeFees);  		    					
			  	}else{
					if(count<=2){
						count++;
						tracer.addTag("parser.crawler.getRealtimefee.count"+count, "这是第"+count+"次重新尝试抓取号码实时费用");
						Thread.sleep(2000);
						getRealtimefee(messageLogin, taskMobile,count);
					}
				}		 
			}		
		} catch (Exception e) {
			tracer.addTag("getRealtimefee.Exception",e.getMessage());
		}			
	   return webParam;
	}
   @Async
   public WebParam getVoiceRecordByMonth(MessageLogin messageLogin, TaskMobile taskMobile,String month)  {		
		WebParam webParam= new WebParam();	
		try {			
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
			webClient.getOptions().setTimeout(60000);
			webClient = addcookie(webClient,taskMobile);
			HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000795", webClient);
			if (200==htmlPage.getWebResponse().getStatusCode()) {
				String url="http://shop.xj.189.cn:8081/xjwt_webapp/DetailBillQueryController/getHistoryMonth.do";		
				List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
				paramsList = new ArrayList<NameValuePair>();
				paramsList.add(new NameValuePair("number",messageLogin.getName()));
				paramsList.add(new NameValuePair("AreaCode", taskMobile.getAreacode()));	
				paramsList.add(new NameValuePair("ListType", "0"));
				paramsList.add(new NameValuePair("queryDate", month));
				paramsList.add(new NameValuePair("deviceType", "25"));			
				paramsList.add(new NameValuePair("PageIndex", "0"));		
				paramsList.add(new NameValuePair("PageSize", "20"));	
				paramsList.add(new NameValuePair("telephoneCode", ""));	
				paramsList.add(new NameValuePair("photoCode", ""));			
				WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
				webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
				webRequest.setAdditionalHeader("Connection", "keep-alive");
				webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				webRequest.setAdditionalHeader("Host", "shop.xj.189.cn:8081");
				webRequest.setAdditionalHeader("Origin", "http://shop.xj.189.cn:8081");
				webRequest.setAdditionalHeader("Referer", "http://shop.xj.189.cn:8081/xjwt_webapp/pcapp/service/billing/arrearsquery/arrearsQuery.jsp?funid=2&fastcode=20000800&cityCode=xj"); 
				webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
				webRequest.setAdditionalHeader("Connection", "keep-alive"); 
				webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
				webRequest.setRequestParameters(paramsList);
				Page page = webClient.getPage(webRequest);
				Thread.sleep(1000);
			    int statusCode = page.getWebResponse().getStatusCode();	
				webParam.setCode(statusCode);
		     	webParam.setUrl(url);
		    	String html = page.getWebResponse().getContentAsString();
				webParam.setHtml(html);
			 	tracer.addTag("getVoiceRecordByMonth"+month,html);			 			
				if (null != html && html.contains("maxPage")) {
					JSONArray jsonArray = JSONArray.fromObject(html);
					JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(0));
					if (list1ArrayObjs.toString().contains("maxPage")) {
						String maxPage = list1ArrayObjs.getString("maxPage");
						webParam.setMaxPage(maxPage);
					}
					List<TelecomXinjiangVoiceRecord> voiceRecords = telecomXinJiangParser.htmlVoiceRecordsParser(html,taskMobile);
					webParam.setVoiceRecords(voiceRecords);
				} 
			}
		} catch (Exception e) {
			tracer.addTag("getVoiceRecordByMonth.Exception"+month,e.getMessage());		
		}	
		
		return webParam;
	}
   
   
   
   //语言详单
   public WebParam getVoiceRecord(MessageLogin messageLogin, TaskMobile taskMobile,String month,String pageIndex,int count)  {
		WebParam webParam= new WebParam();		
		tracer.addTag("getVoiceRecord"+month+pageIndex,"start");	
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = addcookie(webClient,taskMobile);
			HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000795", webClient);		
			if (200==htmlPage.getWebResponse().getStatusCode()) {
				String url="http://shop.xj.189.cn:8081/xjwt_webapp/DetailBillQueryController/getHistoryMonth.do";		
				List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
				paramsList = new ArrayList<NameValuePair>();
				paramsList.add(new NameValuePair("number",messageLogin.getName()));
				paramsList.add(new NameValuePair("AreaCode", taskMobile.getAreacode()));	
				paramsList.add(new NameValuePair("ListType", "0"));
				paramsList.add(new NameValuePair("queryDate", month));
				paramsList.add(new NameValuePair("deviceType", "25"));			
				paramsList.add(new NameValuePair("PageIndex", pageIndex));		
				paramsList.add(new NameValuePair("PageSize", "20"));	
				paramsList.add(new NameValuePair("telephoneCode", ""));	
				paramsList.add(new NameValuePair("photoCode", ""));			
			    Page page = gettmlPost(webClient, paramsList, url);	
				Thread.sleep(3500);
			    int statusCode = page.getWebResponse().getStatusCode();	
				webParam.setCode(statusCode);
		     	webParam.setUrl(url);
		    	String html = page.getWebResponse().getContentAsString();
				webParam.setHtml(html);		 			
				if (null != html && html.contains("maxPage")) {
					JSONArray jsonArray = JSONArray.fromObject(html);
					JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(0));
					if (list1ArrayObjs.toString().contains("maxPage")) {
						String maxPage = list1ArrayObjs.getString("maxPage");
						webParam.setMaxPage(maxPage);
					}
					List<TelecomXinjiangVoiceRecord> voiceRecords = telecomXinJiangParser.htmlVoiceRecordsParser(html,taskMobile);
					webParam.setVoiceRecords(voiceRecords);
				} else {
					tracer.addTag("parser.crawler.getVoiceRecord.html" + count,html);
					if (count <=3) {
						count++;
						tracer.addTag("parser.crawler.getVoiceRecord.count" + count,
								"这是第" + count + "次重新尝试抓取第" + month + "语音详单数据");
						Thread.sleep(3500);
						getVoiceRecord(messageLogin, taskMobile, month, pageIndex, count);
					}
				}

			}
		} catch (Exception e) {
			tracer.addTag("getVoiceRecord.Exception"+month+pageIndex,e.getMessage());		
		}
		tracer.addTag("getVoiceRecord"+month+pageIndex,"end");	
	   return webParam;
	}
   
    //充值记录
   public WebParam getRechargeRecord(MessageLogin messageLogin, TaskMobile taskMobile,String account,String month,int count){
		WebParam webParam= new WebParam();	
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
			webClient.getOptions().setTimeout(50000);
			webClient = addcookie(webClient,taskMobile);
			HtmlPage htmlPage=getHtml("http://shop.xj.189.cn:8081/xjwt_webapp/pcapp/service/billingNew/czjl.jsp?fastcode=20000797", webClient);
			if (200==htmlPage.getWebResponse().getStatusCode()) {
				String url="http://shop.xj.189.cn:8081/xjwt_webapp/PayCostsRecordQueryControllerNew/payCostsRecordQuery.do";	
				tracer.addTag("getRechargeRecord参数",month+"月 "+" account="+account+" AreaCode="+taskMobile.getAreacode());	
				List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
				paramsList = new ArrayList<NameValuePair>();
				paramsList.add(new NameValuePair("date",month));
				paramsList.add(new NameValuePair("phone", account));
				paramsList.add(new NameValuePair("deviceType", "25"));			
				paramsList.add(new NameValuePair("AreaCode", taskMobile.getAreacode()));		
			    Page page = gettmlPost(webClient, paramsList, url);		 
				Thread.sleep(3500);
			    int statusCode = page.getWebResponse().getStatusCode();
				webParam.setCode(statusCode);
		     	webParam.setUrl(url);
		    	String html = page.getWebResponse().getContentAsString();
				webParam.setHtml(html);
			 	tracer.addTag("getRechargeRecord"+month,html);	
		 		if (null !=html &&html.contains("payLiushu")) {
		    		List<TelecomXinjiangRechargeRecord>  rechargeRecords=telecomXinJiangParser.htmlRechargeRecordsParser(html,taskMobile);	    
			    	webParam.setRechargeRecords(rechargeRecords); 
				}else{
					if(count<=2){
						count++;
						tracer.addTag("parser.crawler.getRechargeRecord.count"+count, "这是第"+count+"次重新尝试抓取"+month+"充值数据");
						Thread.sleep(2000);
						getRechargeRecord(messageLogin, taskMobile,account,month,count);
					}
				}	   	 	
		    				    	  
			}		
		} catch (Exception e) {
		 	tracer.addTag("getRechargeRecord.Exception"+month,e.getMessage());	
		}		
	   return webParam;
	}
   
   //获取当前积分
   public WebParam getPiont(MessageLogin messageLogin, TaskMobile taskMobile,int count)  {
		WebParam webParam= new WebParam();	
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
			webClient = addcookie(webClient,taskMobile);
			HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000795", webClient);
			if (200==htmlPage.getWebResponse().getStatusCode()) {
				String url="http://shop.xj.189.cn:8081/xjwt_webapp/IntegralDetailQuery/queryIntegralDetail.do";		
				Page page = gettmlPost(webClient, null, url);	
				Thread.sleep(1000);
			    int statusCode = page.getWebResponse().getStatusCode();
				webParam.setCode(statusCode);
		     	webParam.setUrl(url);
		    	String html = page.getWebResponse().getContentAsString();
		    	webParam.setHtml(html);	    	
			 	tracer.addTag("getPiontResponse",html);
		    	if (html.contains("resList")) {
		    		List<TelecomXinjiangPointRecord>  points=telecomXinJiangParser.htmlPointParser(html,taskMobile);
			    	webParam.setPoints(points);	
				}else{
					if(count<3){
						count++;
						tracer.addTag("parser.crawler.getPiont.count"+count, "这是第"+count+"次重新尝试抓取积分数据");
						Thread.sleep(2000);
						getPiont(messageLogin, taskMobile,count);
					}
				}	
		    	
			}		
		} catch (Exception e) {
		 	tracer.addTag("getPiont.Exception",e.getMessage());
		}
		
	   return webParam;
	}
   //获取账户余额
   public String  getAccountBalance(MessageLogin messageLogin, TaskMobile taskMobile){
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		try {
			webClient = addcookie(webClient,taskMobile);
			HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000795", webClient);	
			if(200==htmlPage.getWebResponse().getStatusCode()){
				String url="http://shop.xj.189.cn:8081/xjwt_webapp/ArrearsController/getArrearsQueryPost.do";		
				List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
				paramsList = new ArrayList<NameValuePair>();
				paramsList.add(new NameValuePair("moblieNum",messageLogin.getName()));
				paramsList.add(new NameValuePair("deviceType", "25"));			
				paramsList.add(new NameValuePair("AreaCode", taskMobile.getAreacode()));	
			    Page page =  gettmlPost(webClient, paramsList, url);
				Thread.sleep(1000);
			    tracer.addTag("getAccountBalanceHtml",page.getWebResponse().getContentAsString());
			    return page.getWebResponse().getContentAsString();
			}			
		} catch (Exception e) {
			 tracer.addTag("getAccountBalance.Exception",e.getMessage());
		}
		return null;
   }
   
   //短信详单
  public WebParam getSmsRecord(MessageLogin messageLogin, TaskMobile taskMobile,String month,String pageIndex,int count){
		WebParam webParam= new WebParam();	
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient.getOptions().setTimeout(50000);
			webClient = addcookie(webClient,taskMobile);
			HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000795", webClient);	
			if (200==htmlPage.getWebResponse().getStatusCode()) {
				String url="http://shop.xj.189.cn:8081/xjwt_webapp/DetailBillQueryController/getHistoryMonth.do";		
				List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
				paramsList = new ArrayList<NameValuePair>();
				paramsList.add(new NameValuePair("number",messageLogin.getName()));
				paramsList.add(new NameValuePair("AreaCode", taskMobile.getAreacode()));		
				paramsList.add(new NameValuePair("ListType", "6"));
				paramsList.add(new NameValuePair("queryDate", month));
				paramsList.add(new NameValuePair("deviceType", "25"));			
				paramsList.add(new NameValuePair("PageIndex", pageIndex));	
				paramsList.add(new NameValuePair("PageSize", "20"));	
				paramsList.add(new NameValuePair("telephoneCode", ""));	
				paramsList.add(new NameValuePair("photoCode", ""));	
			    Page page = gettmlPost(webClient, paramsList, url);	
				Thread.sleep(2500);
			    int statusCode = page.getWebResponse().getStatusCode();
				webParam.setCode(statusCode);
		     	webParam.setUrl(url);
		    	String html = page.getWebResponse().getContentAsString();
		    	tracer.addTag("getSmsRecord"+month+pageIndex,html);	
				webParam.setHtml(html);
				if (null !=html && html.contains("maxPage")) {
					JSONArray jsonArray = JSONArray.fromObject(html);  
					JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(0));
					if (list1ArrayObjs.toString().contains("maxPage")) {
						String maxPage=list1ArrayObjs.getString("maxPage");
						webParam.setMaxPage(maxPage);
					}	
					List<TelecomXinjiangSmsRecord>  smsRecords=telecomXinJiangParser.htmlSmsRecordsParser(html, taskMobile);    
			    	webParam.setSmsRecords(smsRecords); 		
				}else{
					if(count<=2){
						count++;
						tracer.addTag("parser.crawler.getSmsRecord.count"+count, "这是第"+count+"次重新尝试抓取第"+pageIndex+"页短信详单数据");
						Thread.sleep(2000);
						getSmsRecord(messageLogin, taskMobile,month,pageIndex,count);
					}
				}
			 			
			}		
		} catch (Exception e) {
		 	tracer.addTag("getSmsRecord"+month+pageIndex,e.getMessage());	
		}
		
	   return webParam;
	}
//增值服务及已开通功能
  public WebParam getPayMonths(MessageLogin messageLogin, TaskMobile taskMobile,String month,int count)  {
		WebParam webParam= new WebParam();	
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
			webClient = addcookie(webClient,taskMobile);
			HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000795", webClient);	
			if (200==htmlPage.getWebResponse().getStatusCode()) {
				String url="http://shop.xj.189.cn:8081/xjwt_webapp/billQueryController/getmonthQuery.do";
				WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
				String markType="0";
				String deviceType="25";
				String data = "[{\"telephone\":\""+messageLogin.getName()+"\",\"queryDate\":\""+month+"\",\"markType\":"+markType+",\"deviceType\":\""+deviceType+"\"}]";		
				requestSettings.setAdditionalHeader("Accept", "text/plain, */*; q=0.01");
				requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate"); 
				requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8"); 
				requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
				requestSettings.setAdditionalHeader("Host", "shop.xj.189.cn:8081");
				requestSettings.setAdditionalHeader("Origin", "http://shop.xj.189.cn:8081");
				requestSettings.setAdditionalHeader("Referer", "http://shop.xj.189.cn:8081/xjwt_webapp/pcapp/service/billing/historybill/HistoryBill.jsp?fastcode=10000331&cityCode=xj"); 
				requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
				requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				requestSettings.setAdditionalHeader("Content-Type", "application/json"); 
				requestSettings.setAdditionalHeader("Connection", "keep-alive"); 
				requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
				requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");		
				requestSettings.setRequestBody(data);
				Page page = webClient.getPage(requestSettings);
			    int statusCode = page.getWebResponse().getStatusCode();
				webParam.setCode(statusCode);
		     	webParam.setUrl(url);
		    	String html = page.getWebResponse().getContentAsString();
				webParam.setHtml(html);
			 	tracer.addTag("getPayMonths"+month,html);
			    if (html.contains("two")) {
			    	List<TelecomXinjiangPayMonths>  payMonths=telecomXinJiangParser.htmlPayMonthsParser(html,month,taskMobile);    
			    	webParam.setPayMonths(payMonths);
				}else{
					if(count<=2){
						count++;
						tracer.addTag("parser.crawler.getPayMonths.count"+count, "这是第"+count+"次重新尝试抓取增值服务数据");
						Thread.sleep(2000);
						getAddvalueItem(messageLogin, taskMobile,count);
					}
				}
			 
			}		
		} catch (Exception e) {
			tracer.addTag("getPayMonths"+month,e.getMessage());
		}
		
	   return webParam;
	}   
  //增值服务及已开通功能
  public WebParam getAddvalueItem(MessageLogin messageLogin, TaskMobile taskMobile,int count)  {
		WebParam webParam= new WebParam();	
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
			webClient = addcookie(webClient,taskMobile);
			HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000795", webClient);	
			if (200==htmlPage.getWebResponse().getStatusCode()) {
				String url="http://shop.xj.189.cn:8081/xjwt_webapp/IsUseBussinesWebQueryController/getUsedBusinessPost.do";		
				List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
				paramsList = new ArrayList<NameValuePair>();
				paramsList.add(new NameValuePair("moblieNum",messageLogin.getName()));		
				paramsList.add(new NameValuePair("deviceType", "2"));	
				paramsList.add(new NameValuePair("AreaCode", taskMobile.getAreacode()));			
				WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
				webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest.setAdditionalHeader("Host", "shop.xj.189.cn:8081");
				webRequest.setAdditionalHeader("Origin", "http://shop.xj.189.cn:8081");
				webRequest.setAdditionalHeader("Referer", "http://shop.xj.189.cn:8081/xjwt_webapp/pcapp/service/billing/packageusecase/packageUseCaseQuery.jsp?funid=1&fastcode=10000333&cityCode=xj"); 
				webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
				webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"); 
				webRequest.setAdditionalHeader("Connection", "keep-alive"); 
				webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
				webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				webRequest.setRequestParameters(paramsList);
				Page page = webClient.getPage(webRequest);
			    int statusCode = page.getWebResponse().getStatusCode();
				webParam.setCode(statusCode);
		     	webParam.setUrl(url);
		    	String html = page.getWebResponse().getContentAsString();
				webParam.setHtml(html);
			 	tracer.addTag("getAddvalueItem",html);
			    if (html.contains("list1")) {
			    	List<TelecomXinjiangAddvalueItem>  addvalueItems=telecomXinJiangParser.htmladdvalueItemsParser(html,taskMobile);	    
			    	webParam.setAddvalueItems(addvalueItems);	
				}else{
					if(count<=3){
						count++;
						tracer.addTag("parser.crawler.getAddvalueItem.count"+count, "这是第"+count+"次重新尝试抓取增值服务数据");
						Thread.sleep(2000);
						getAddvalueItem(messageLogin, taskMobile,count);
					}
				}
			 
			}		
		} catch (Exception e) {
			tracer.addTag("getAddvalueItem.Exception",e.getMessage());
		}
		
	   return webParam;
	}   
	public  String getphonecode(MessageLogin mssageLogin,TaskMobile taskMobile) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient, taskMobile);
		String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000795";
		HtmlPage htmlpage = getHtml(url, webClient);
		if (200==htmlpage.getWebResponse().getStatusCode()) {
			url = "http://shop.xj.189.cn:8081/xjwt_webapp/SmsCodeServlet/getSmSInfo.do";	
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("phoneNumber", mssageLogin.getName()));
			paramsList.add(new NameValuePair("photoCode", "undefined"));
			paramsList.add(new NameValuePair("ywName", "realName_validate"));
		    Page page = gettmlPost(webClient, paramsList, url);		
		    String html=page.getWebResponse().getContentAsString();
			tracer.addTag("getphonecode",html);
			if ("1".equals(html)) {
				String cookies = CommonUnit.transcookieToJson(webClient);
	            taskMobile.setCookies(cookies);     
	            taskMobileRepository.save(taskMobile);
			}		
			return html;
		}		
		return null;
	}
	public  WebParam setphonecode(MessageLogin mssageLogin,TaskMobile taskMobile) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient = addcookie(webClient,taskMobile);				
		String url = "http://shop.xj.189.cn:8081/xjwt_webapp/pcapp/service/billing/detailbillquery/datailBillQuery.jsp?fastcode=10000332&fastcode=10000332&cityCode=xj";
		HtmlPage htmlpage = getHtml(url, webClient);	
		BasicUser basicUser = basicUserRepository.findById(mssageLogin.getUser_id());
		if (200==htmlpage.getWebResponse().getStatusCode()) {
			url = "http://shop.xj.189.cn:8081/xjwt_webapp/RealNameValidateController/optBusniesOrPhone.do";	
		    String idcard=basicUser.getIdnum();
		 	String sms_code=mssageLogin.getSms_code();
		 	String businessCode="10010008";
		 	String phoneNumber=mssageLogin.getName();
			String source="pc";
			String ywName="realName_validate";
			String deviceType="990001450";
			String passwordTc="";
			String grade="1";
			String data= "{\"idcard\":\""+idcard+"\",\"sms_code\":\""+sms_code+"\",\"businessCode\":\""+businessCode+"\",\"phoneNumber\":\""+phoneNumber+"\",\"source\":\""+source+"\",\"ywName\":\""+ywName+"\",\"deviceType\":\""+deviceType+"\",\"passwordTc\":\""+passwordTc+"\",\"grade\":\""+grade+"\"}";	 	
			tracer.addTag("setphonecode参数",data);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Host", "shop.xj.189.cn:8081");
			webRequest.setAdditionalHeader("Origin", "http://shop.xj.189.cn:8081");
			webRequest.setAdditionalHeader("Referer", "http://shop.xj.189.cn:8081/xjwt_webapp/pcapp/service/billing/detailbillquery/datailBillQuery.jsp?fastcode=10000332&cityCode=xj"); 
			webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
			webRequest.setAdditionalHeader("Content-Type", "application/json ; charset=UTF-8"); 
			webRequest.setAdditionalHeader("Connection", "keep-alive"); 
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest.setRequestBody(data);
			Page page = webClient.getPage(webRequest);	
			String html=page.getWebResponse().getContentAsString();
			tracer.addTag("setphonecode",html);
			webParam.setSmsResult(html);
			webParam.setWebClient(webClient);
			return webParam;
		}		
		return null;
	}
	public Page gettmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setCharset(Charset.forName("UTF-8"));
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			tracer.addTag("telecomxinjiang ", e.getMessage());
			return null;
		}

	}

	public  Page gethtmlPostByWebRequest(WebClient webClient, WebRequest webRequest, String url) {
		try {
			webClient.getOptions().setJavaScriptEnabled(false);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomxinjiang ", e.getMessage());
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
			tracer.addTag("telecomxinjiang ", e.getMessage());
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
