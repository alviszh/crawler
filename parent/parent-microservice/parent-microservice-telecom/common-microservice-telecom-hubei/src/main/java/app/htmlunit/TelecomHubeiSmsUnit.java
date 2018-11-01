package app.htmlunit;

import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
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
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class TelecomHubeiSmsUnit {

	public  final Logger log = LoggerFactory.getLogger(TelecomHubeiSmsUnit.class);
	@Autowired
	private TracerLog tracer;
	//发送验证码
	public  WebParam getphonecode(MessageLogin mssageLogin,TaskMobile taskMobile,String areaCode) throws Exception {
		WebParam webParam=new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient = addcookie(webClient, taskMobile);
		String preUrl = "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp";
		Page page2 = webClient.getPage(preUrl);
		tracer.addTag("getphonecode.detailList.page", page2.getWebResponse().getContentAsString());
		String smsUrl="http://hb.189.cn/feesquery_PhoneIsDX.action";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("productNumber", mssageLogin.getName()));		
		paramsList.add(new NameValuePair("cityCode", areaCode));	
		paramsList.add(new NameValuePair("sentType", "C"));	
		paramsList.add(new NameValuePair("ip", "0"));		
		tracer.addTag("getphonecode 参数为 productNumber=",mssageLogin.getName()+" cityCode="+areaCode);	
		WebRequest webRequest = new WebRequest(new URL(smsUrl), HttpMethod.POST);				
		webRequest.setAdditionalHeader("Accept", "text/plain, */*");
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
		Page page = webClient.getPage(webRequest);				
		Thread.sleep(1000);
		int statusCode=page.getWebResponse().getStatusCode();
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("getphonecode",html);	
		webParam.setUrl(smsUrl);
		webParam.setHtml(html);		
		if (200==statusCode) {
			webParam.setWebClient(webClient);			
			webParam.setSmsResult(html);
			return webParam;
		}			
		return null;
	}
	//手机验证码验证
	public  WebParam setphonecode(MessageLogin mssageLogin,TaskMobile taskMobile) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient = addcookie(webClient,taskMobile);
		String url = "http://hb.189.cn/feesquery_checkCDMAFindWeb.action";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("random", mssageLogin.getSms_code()));
		paramsList.add(new NameValuePair("sentType", "C"));	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");	
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp"); 
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");			
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);	
		Thread.sleep(2500);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("setphonecode",html);
		webParam.setUrl(url);
		webParam.setHtml(html);
		if (200==page.getWebResponse().getStatusCode()) {		
			webParam.setWebClient(webClient);
			webParam.setSmsResult(html);
			return webParam;	
		}	
		return null;
	}
	//获取城市信息
	public WebParam getCityCode(MessageLogin messageLogin, TaskMobile taskMobile, int count) throws Exception {
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient = addcookie(webClient, taskMobile);
		String url = "http://hb.189.cn/ajaxServlet/getCityCodeAndIsLogin";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("method", "getCityCodeAndIsLogin"));
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/ajaxServlet/getCityCodeAndIsLogin?method=getCityCodeAndIsLogin");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setCharset(Charset.forName("UTF-8"));
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("getCityCode html",  html);
		String areaCode = "";
		String isLogin = "";
		if (null != html && html.contains("CITYCODE")) {
			JSONArray listArray = JSONArray.fromObject(html);
			JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(0));
			areaCode = listArrayObjs.getString("CITYCODE");
			webParam.setAreaCode(areaCode);
			isLogin = listArrayObjs.getString("ISLOGIN");
			webParam.setIsLogin(isLogin);
		} else {
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getCityCode.count" + count, "这是第" + count + "次获取城市信息");
				Thread.sleep(1000);
				getCityCode(messageLogin, taskMobile, count);
			}
		}
		return webParam;
	}
	//获取当前月份
	private String getLocalMonth(){
		LocalDate today = LocalDate.now();// 本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(0);
		String monthint = stardate.getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() + monthint;
		return  month;		
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
			tracer.addTag("telecomhlj ", e.getMessage());
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
