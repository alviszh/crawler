package app.htmlunit;

import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouBasicinfo;
import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouRecords;
import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceHuzhouParser;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;
import net.sf.json.JSONObject;

@Component
public class InsuranceHuzhouHtmlunit {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceHuzhouParser insuranceHuzhouParser;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
	    String url = "http://www.hzlss.gov.cn:9092/huzhounet/";		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "www.hzlss.gov.cn:9092");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		HtmlPage page = webClient.getPage(webRequest);
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效 
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 		
		int status = page.getWebResponse().getStatusCode();
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setUrl(url);
		if(200 == status){			
			HtmlImage image = page.getFirstByXPath("//img[@id='ImageCode']");
			String code = chaoJiYingOcrService.getVerifycode(image, "1006");
			tracer.addTag("verifyCode ==>", code);
			//登陆
			String loginUrl = "http://www.hzlss.gov.cn:9092/huzhounet/checkLoginpersonal.do";
			String requestBody = "uname=" + insuranceRequestParameters.getUsername() + "&password="
					+ MD5Util.getMD5(insuranceRequestParameters.getPassword()).toLowerCase() + "&truename="
					+ URLEncoder.encode(insuranceRequestParameters.getName(), "UTF-8") + "&imgCode=" + code
					+ "&uaertype=1";
			WebRequest loginWebRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
			loginWebRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			loginWebRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			loginWebRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			loginWebRequest.setAdditionalHeader("Connection", "keep-alive");
			loginWebRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			loginWebRequest.setAdditionalHeader("Host", "www.hzlss.gov.cn:9092");
			loginWebRequest.setAdditionalHeader("Origin", "http://www.hzlss.gov.cn:9092");
			loginWebRequest.setAdditionalHeader("Referer", "http://www.hzlss.gov.cn:9092/huzhounet/login.do");
			loginWebRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			loginWebRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			loginWebRequest.setRequestBody(requestBody);
			Page loginPage = webClient.getPage(loginWebRequest);	
			Thread.sleep(1500);
			String html=loginPage.getWebResponse().getContentAsString();
			tracer.addTagWrap("login",html);		
			JSONObject loginObjs = JSONObject.fromObject(html);
			String errMsg=loginObjs.getString("errMsg");
			webParam.setWebClient(webClient);
			webParam.setErrMsg(errMsg);
			webParam.setHtml(html);						
		}
		return webParam;	
	}
	
	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getUserInfo(TaskInsurance taskInsurance, Set<Cookie> cookies) throws Exception {	
		WebParam webParam= new WebParam();
		String url = "http://www.hzlss.gov.cn:9092/huzhounet/personal/home.do";
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.hzlss.gov.cn:9092");
		webRequest.setAdditionalHeader("Referer", "http://www.hzlss.gov.cn:9092/huzhounet/personal/WelcomeMe"); 
		HtmlPage page = webClient.getPage(webRequest);		
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 		
		int statusCode = page.getWebResponse().getStatusCode();
	    if(200 == statusCode){
	    	String html = page.asXml();
	      	tracer.addTag("getUserInfo 个人信息","<xmp>"+html+"</xmp>");
	      	InsuranceHuzhouUserInfo userInfo = insuranceHuzhouParser.htmlParserForUserInfo(html,taskInsurance);	    		    
	    	webParam.setCode(statusCode);
	    	webParam.setUserInfo(userInfo);
	     	webParam.setPage(page);
	    	webParam.setUrl(url);
	    	webParam.setHtml(html);
	    }
		return webParam;
	}

	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getBasicinfo(TaskInsurance taskInsurance, Set<Cookie> cookies) throws Exception {	
		WebParam webParam= new WebParam();
		String url = "http://www.hzlss.gov.cn:9092/huzhounet/personal/InsuranceListFind";
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);	
		String requestBody = "currentActive=1&pagedata.pageSize=0&pagedata.totalRows=0&pagedata.pageSum=0&pagedata.currentPage=0&pagedata.pageStart=0&pagedata.pageEnd=0&pagedata.prev=1&pagedata.next=1&currentPage=1";
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");		
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.hzlss.gov.cn:9092");
		webRequest.setAdditionalHeader("Origin", "http://www.hzlss.gov.cn:9092"); 
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);		
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 		
		int statusCode = page.getWebResponse().getStatusCode();
    	String html = page.getWebResponse().getContentAsString();
     	webParam.setHtml(html);
    	webParam.setUrl(url);
      	webParam.setCode(statusCode);
      	tracer.addTag("getInsuranceInfo 社保情况信息",html);	
	    if(200 == statusCode){
	      	List<InsuranceHuzhouBasicinfo> basicinfoList = insuranceHuzhouParser.htmlParserForBasicinfo(html, taskInsurance);		    	  
	    	webParam.setBasicinfoList(basicinfoList);
	    }
		return webParam;
	}
	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getInsuranceRecords(TaskInsurance taskInsurance,String type,Set<Cookie> cookies) throws Exception {	
		WebParam webParam= new WebParam();
		String url = "http://www.hzlss.gov.cn:9092/huzhounet/personal/PaymentDetailsFind";
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);	
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");  
		Calendar c = Calendar.getInstance();  
		String endTime=format.format(c.getTime());//开始设置十年之前
		c.add(Calendar.YEAR, -10); //年份减10 
		String startTime=format.format(c.getTime());
		String requestBody = "currentActive=2&ace021=" + URLEncoder.encode(type, "UTF-8")
				+ "&ace023a="+startTime+"&ace023b="+endTime+"&pagedata.pageSize=0&pagedata.totalRows=0"
				+ "&pagedata.pageSum=0&pagedata.currentPage=0&pagedata.pageStart=0&pagedata.pageEnd=0"
				+ "&pagedata.prev=1&pagedata.next=1&currentPage=1";
		tracer.addTag("getInsuranceRecords type="+type+" requestBody=",requestBody);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");		
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.hzlss.gov.cn:9092");
		webRequest.setAdditionalHeader("Origin", "http://www.hzlss.gov.cn:9092"); 
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);		
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 		
		Thread.sleep(1500);
		int statusCode = page.getWebResponse().getStatusCode();
		String html = page.getWebResponse().getContentAsString();
		webParam.setCode(statusCode);
    	webParam.setUrl(url);
    	webParam.setHtml(html);
      	tracer.addTagWrap("getInsuranceRecords type="+type,html);
	    if(200 == statusCode){
	      	if (null !=html && html.contains("page")) {
	      		JSONObject list1ArrayObjs = JSONObject.fromObject(html);
				String pageInfo=list1ArrayObjs.getString("page");
				JSONObject pageInfoObjs = JSONObject.fromObject(pageInfo);
				String currentPage=pageInfoObjs.getString("currentPage");
				String next=pageInfoObjs.getString("next");
				String pageEnd=pageInfoObjs.getString("pageEnd");
				String pageSize=pageInfoObjs.getString("pageSize");
				String pageStart=pageInfoObjs.getString("pageStart");
				String pageSum=pageInfoObjs.getString("pageSum");
				String prev=pageInfoObjs.getString("prev");
				String totalRows=pageInfoObjs.getString("totalRows");
				webParam.setCurrentPage(currentPage);
				webParam.setNext(next);
				webParam.setPageEnd(pageEnd);
				webParam.setPageSize(pageSize);
				webParam.setPageStart(pageStart);
				webParam.setPageSum(pageSum);
				webParam.setPrev(prev);
				webParam.setTotalRows(totalRows);
			}
	      	List<InsuranceHuzhouRecords> recordList = insuranceHuzhouParser.htmlParserForRecordsList(html, taskInsurance);		    
	    	webParam.setRecordList(recordList);
	    }
		return webParam;
	}
	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getInsuranceRecordsOtherPage(WebParam webParam,TaskInsurance taskInsurance,String type,int pageCount,Set<Cookie> cookies) throws Exception {	
		String url = "http://www.hzlss.gov.cn:9092/huzhounet/personal/PaymentDetailsFind";
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);	
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");  
		Calendar c = Calendar.getInstance();  
		String endTime=format.format(c.getTime());//开始设置十年之前
		c.add(Calendar.YEAR, -10); //年份减10
		String startTime=format.format(c.getTime());
		String requestBody = "currentActive=2&ace021=" + URLEncoder.encode(type, "UTF-8") + "&ace023a=" + startTime
				+ "&ace023b=" + endTime + "&pagedata.pageSize=" + webParam.getPageSize() + "&pagedata.totalRows="
				+ webParam.getTotalRows() + "&pagedata.pageSum=" + webParam.getPageSum() + "&pagedata.currentPage="
				+ webParam.getCurrentPage() + "&pagedata.pageStart=" + webParam.getPageStart() + "&pagedata.pageEnd="
				+ webParam.getPageEnd() + "&pagedata.prev=" + webParam.getPrev() + "&pagedata.next="
				+ webParam.getNext() + "&currentPage=" + pageCount;
		tracer.addTag("getInsuranceRecordsOtherPage type="+type+" 页数:"+pageCount+"  requestBody=",requestBody);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");		
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.hzlss.gov.cn:9092");
		webRequest.setAdditionalHeader("Origin", "http://www.hzlss.gov.cn:9092"); 
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);		
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 		
		Thread.sleep(1500);
		int statusCode = page.getWebResponse().getStatusCode();
		String html = page.getWebResponse().getContentAsString();
      	tracer.addTagWrap("getInsuranceRecordsOtherPage type="+type,html);
      	webParam.setCode(statusCode);    	
      	webParam.setUrl(url);
      	webParam.setHtml(html);   
	    if(200 == statusCode){
	    	if (null !=html && html.contains("page")) {
	      		JSONObject list1ArrayObjs = JSONObject.fromObject(html);
				String pageInfo=list1ArrayObjs.getString("page");
				JSONObject pageInfoObjs = JSONObject.fromObject(pageInfo);
				String currentPage=pageInfoObjs.getString("currentPage");
				String next=pageInfoObjs.getString("next");
				String pageEnd=pageInfoObjs.getString("pageEnd");
				String pageSize=pageInfoObjs.getString("pageSize");
				String pageStart=pageInfoObjs.getString("pageStart");
				String pageSum=pageInfoObjs.getString("pageSum");
				String prev=pageInfoObjs.getString("prev");
				String totalRows=pageInfoObjs.getString("totalRows");
				webParam.setCurrentPage(currentPage);
				webParam.setNext(next);
				webParam.setPageEnd(pageEnd);
				webParam.setPageSize(pageSize);
				webParam.setPageStart(pageStart);
				webParam.setPageSum(pageSum);
				webParam.setPrev(prev);
				webParam.setTotalRows(totalRows);
			}
	    	if (null != html & html.contains("data")) {
	    	 	List<InsuranceHuzhouRecords> recordList = insuranceHuzhouParser.htmlParserForRecordsList(html, taskInsurance);	
	    	 	webParam.setRecordList(recordList);
	    	}     	    	    	
	    }
		return webParam;
	}
}
