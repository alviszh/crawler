package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.ganzhou.InsuranceGanZhouBasicinfo;
import com.microservice.dao.entity.crawler.insurance.ganzhou.InsuranceGanZhouPersion;
import com.microservice.dao.entity.crawler.insurance.ganzhou.InsuranceGanZhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.InsuranceService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceGanZhouParser {
	@Autowired
	private InsuranceService insuranceService;
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
	    String url = "http://www.gzyb.com.cn:8080/GZYBWeb/login.action";		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);	
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("loginForm.userName", insuranceRequestParameters.getUsername()));
		paramsList.add(new NameValuePair("loginForm.userPass", insuranceRequestParameters.getPassword()));
		paramsList.add(new NameValuePair("loginForm.userType", "1"));	
		paramsList.add(new NameValuePair("login", ""));	
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.gzyb.com.cn:8080");
		webRequest.setRequestParameters(paramsList);
		HtmlPage page = webClient.getPage(webRequest);
		String html=page.getWebResponse().getContentAsString();
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效 		
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 	
		webParam.setWebClient(webClient);
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setUrl(url);
		webParam.setPage(page);
		webParam.setHtml(html);	
		tracer.addTag("login page","<xmp>"+html+"</xmp>");		
		return webParam;			
	}
	
	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getUserInfo(InsuranceRequestParameters insuranceRequestParameters, Set<Cookie> cookies) throws Exception {	
		WebParam webParam= new WebParam();
		WebClient webClient = insuranceService.getWebClient(cookies);
		String url = "http://www.gzyb.com.cn:8080/GZYBWeb/touch/getInfo.action";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("akc020", insuranceRequestParameters.getUsername()));
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);		
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "www.gzyb.com.cn:8080");
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);		
		int statusCode = page.getWebResponse().getStatusCode();
    	String html = page.getWebResponse().getContentAsString();
      	tracer.addTag("getUserInfo 个人信息","<xmp>"+html+"</xmp>");
	    if(200 == statusCode){
	    	if (html.contains("aac002")) {
	    	 	InsuranceGanZhouUserInfo userInfo = htmlParserForUserInfo(html,insuranceRequestParameters.getTaskId());	
	    		webParam.setInsuranceGanZhouUserInfo(userInfo);  
			}	         		    
	    	webParam.setCode(statusCode);    
	    	webParam.setUrl(url);
	    	webParam.setHtml(html);
	    	return webParam;	    	
	    }
		return webParam;
	}
	/**
	 * @Des 解析个人信息
	 * @param html
	 * @return
	 */
	private InsuranceGanZhouUserInfo htmlParserForUserInfo(String html, String taskid) {

		JSONObject list1ArrayObjs = JSONObject.fromObject(html);
		String useraccount = list1ArrayObjs.getString("aac001");
		String companyname = list1ArrayObjs.getString("aab004");
		String username = list1ArrayObjs.getString("aac003");
		String idnum = list1ArrayObjs.getString("aac002");
		String state = list1ArrayObjs.getString("aac008x");
		String sex = list1ArrayObjs.getString("aac004x");
		String nation = list1ArrayObjs.getString("aac005x");
		String birthdate = list1ArrayObjs.getString("aac006");
		String medicalCategory = list1ArrayObjs.getString("akc021x");
		String firstdate = list1ArrayObjs.getString("aae036");
		String retiredate = list1ArrayObjs.getString("aic162");
		String balance = list1ArrayObjs.getString("bkc014");
		InsuranceGanZhouUserInfo userInfo = new InsuranceGanZhouUserInfo();
		userInfo.setUseraccount(useraccount);
		userInfo.setCompanyname(companyname);
		userInfo.setUsername(username);
		userInfo.setIdnum(idnum);
		userInfo.setState(state);
		userInfo.setSex(sex);
		userInfo.setBirthdate(birthdate);
		userInfo.setMedicalCategory(medicalCategory);
		userInfo.setFirstdate(firstdate);
		userInfo.setRetiredate(retiredate);
		userInfo.setBalance(balance);
		userInfo.setNation(nation);
		userInfo.setTaskid(taskid);
		return userInfo;
	}
	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getBasicinfo(InsuranceRequestParameters insuranceRequestParameters, Set<Cookie> cookies,String useraccount) throws Exception {	
		WebParam webParam= new WebParam();
		String url = "http://www.gzyb.com.cn:8080/GZYBWeb/enterprise/personInsured-getPersonInsuredInfo.action";
		WebClient webClient = insuranceService.getWebClient(cookies);
		List<NameValuePair> paramsList= new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();		
		paramsList.add(new NameValuePair("aac001", useraccount));		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);		
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.gzyb.com.cn:8080");
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);		
		int statusCode = page.getWebResponse().getStatusCode();
    	String html = page.getWebResponse().getContentAsString();
      	tracer.addTag("getUserInfo 个人信息","<xmp>"+html+"</xmp>");
      	if (html.contains("rows")) {
      		List<InsuranceGanZhouBasicinfo> basicinfoList = htmlParserForBasicinfo(html,insuranceRequestParameters.getTaskId());	
      		webParam.setBasicinfoList(basicinfoList);
		}          		    
    	webParam.setCode(statusCode);    
    	webParam.setUrl(url);
    	webParam.setHtml(html);	       	
		return webParam;
	}
	private List<InsuranceGanZhouBasicinfo> htmlParserForBasicinfo(String html, String taskid) {

		JSONObject list1ArrayObjs = JSONObject.fromObject(html);
		String listObjsStr = list1ArrayObjs.getString("rows");
		List<InsuranceGanZhouBasicinfo> basicinfoList=new ArrayList<InsuranceGanZhouBasicinfo>();
		if (null != listObjsStr) {
			JSONArray listArray = JSONArray.fromObject(listObjsStr);
			for (int i = 0; i < listArray.size(); i++) {				
				JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
				String useraccount = listArrayObjs.getString("aac001");// 个人编号
				String type = listArrayObjs.getString("aae140x");//险种类型
			
				String state = listArrayObjs.getString("aac031x");// 个参保状态
				String paytype = listArrayObjs.getString("aaa040x");// 缴费类型
				String paybase = listArrayObjs.getString("aae180");//缴费基数
				String startdate = listArrayObjs.getString("aac030");//参保日期			
				InsuranceGanZhouBasicinfo basicinfo = new InsuranceGanZhouBasicinfo(useraccount, type, state, paytype,
						paybase, startdate, taskid);
				basicinfoList.add(basicinfo);
			}
		}
		return basicinfoList;
	}
	/**
	 * @Des 获取养老信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getForPersion(InsuranceRequestParameters insuranceRequestParameters) throws Exception {	
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		String url = "http://111.75.255.90:8080/wlkjappy/open/social";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("IDCARD", insuranceRequestParameters.getUsername()));		
		paramsList.add(new NameValuePair("ISWORK", "true"));	
		paramsList.add(new NameValuePair("LOCAL", insuranceRequestParameters.getArea()));	
		paramsList.add(new NameValuePair("YEAR", "2016"));	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);		
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "111.75.255.90:8080");
		webRequest.setRequestParameters(paramsList);
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 
		Page page = webClient.getPage(webRequest);		
		int statusCode = page.getWebResponse().getStatusCode();
	   	String html = page.getWebResponse().getContentAsString();
      	tracer.addTag("getUserInfo 个人信息","<xmp>"+html+"</xmp>");
	    if(200 == statusCode){	 
	      	if (html.contains("IDCARD")) {
	      		InsuranceGanZhouPersion persion = htmlParserForPersion(html,insuranceRequestParameters.getTaskId());	
	      	  	webParam.setInsuranceGanZhouPersion(persion);
			}	      	    		    
	    	webParam.setCode(statusCode);	    
	    	webParam.setUrl(url);
	    	webParam.setHtml(html);
	    	return webParam;	    	
	    }
		return webParam;
	}
	/**
	 * @Des 解析养老信息
	 * @param html
	 * @return
	 */
	private InsuranceGanZhouPersion htmlParserForPersion(String html, String taskid) {

		JSONObject list1ArrayObjs = JSONObject.fromObject(html);		
		String username = list1ArrayObjs.getString("NAME");//姓名
		String state = list1ArrayObjs.getString("STATUS");//缴费状态
		String idnum = list1ArrayObjs.getString("IDCARD");//身份证号
		String paymonth = list1ArrayObjs.getString("MONTH");//缴费总月数
		String companyname = list1ArrayObjs.getString("COMPANYNAME");//企业名称
		String area = list1ArrayObjs.getString("LOCAL");//缴费地区
		String companyamount = list1ArrayObjs.getString("MONEY");//缴费总额	
		InsuranceGanZhouPersion persion = new InsuranceGanZhouPersion();
		persion.setUsername(username);
		persion.setState(state);
		persion.setArea(area);
		persion.setPaymonth(paymonth);
		persion.setIdnum(idnum);
		persion.setCompanyname(companyname);
		persion.setCompanyamount(companyamount);
		persion.setTaskid(taskid);
		return persion;
	}
}
