package app.parser;

import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingEndowment;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingInjury;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingMaternity;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingMedical;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingUnemployment;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceNanNingParser {

	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		String url="http://222.216.5.212:8060/Cas/login?service=http://222.216.5.212:8081/siweb/userlogin.do?method=begin_dl";
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='username']");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getUsername());
		
		HtmlPasswordInput pwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
		pwd.reset();
		pwd.setText(insuranceRequestParameters.getPassword());
		
		HtmlElement button = (HtmlElement)page.getFirstByXPath("//input[@id='loginButton']");
		Page click = button.click();
//		System.out.println(click.getWebResponse().getContentAsString());
		String url1="http://222.216.5.212:8081/siweb/userlogin.do?method=begin_dl";
		HtmlPage page2 = webClient.getPage(url1);
//		System.out.println(page2.getWebResponse().getContentAsString());
		
		
		HtmlTextInput text1 = (HtmlTextInput)page2.getFirstByXPath("//input[@id='text1']");
		text1.reset();
		text1.setText(insuranceRequestParameters.getName());
		HtmlElement button1 = (HtmlElement)page2.getFirstByXPath("//input[@id='button1']");
		Page click1 = button1.click();
		
		System.out.println(click1.getWebResponse().getContentAsString());
		
		String url2="http://222.216.5.212:8081/siweb/emp_payinof_query.do?method=begin";
		Page page3 = webClient.getPage(url2);
		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		if(page3.getWebResponse().getContentAsString().contains("个人缴费明细查询条件"))
		{
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			return webParam;
		}
		return null;
	}

	public WebParam<InsuranceNanNingUserInfo> getUserInfo(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance) {
		String testhtml = taskInsurance.getTesthtml();
		InsuranceNanNingUserInfo i = new InsuranceNanNingUserInfo();
		
		int indexOf = testhtml.indexOf("psnname");
		int indexOf2 = testhtml.indexOf("var beginTime");
		String substring = testhtml.substring(indexOf, indexOf2);
		String replace = substring.replace("psnname = \"", "");
		String replaceAll = replace.replaceAll("\";","");
		
		i.setIDCard(insuranceRequestParameters.getUsername());
		i.setNum(insuranceRequestParameters.getName());
		i.setName(replaceAll);
		i.setTaskid(taskInsurance.getTaskid());
		WebParam<InsuranceNanNingUserInfo> webParam = new WebParam<InsuranceNanNingUserInfo>();
		webParam.setHtml(testhtml);
		webParam.setInsuranceNanNingUserInfo(i);
		return webParam;
	}

	public WebParam<InsuranceNanNingMaternity> getMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskInsurance);
		
		String url3="http://222.216.5.212:8081/siweb/rpc.do?method=doQuery";
	    String body1="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"\",pageNumber:1,pageSize:10,recordCount:0,rowSetName:\"nn_apply.web_v_ac20\",parameters:{\"gridid\":\"grid1\"},condition:\"WEB_V_AC20.AAE003 >='"+getDateBefore("yyyyMM", 3)+"' and WEB_V_AC20.AAE003 <='"+getTime("yyyyMM")+"' and WEB_V_AC20.AAE140 = '51' and [WEB_V_AC20_AAC001]='"+insuranceRequestParameters.getName()+"' and WEB_V_AC20.AAE114 ='1'\"}},parameters:{\"synCount\":\"true\"}}}";
		WebRequest webRequest2 = new WebRequest(new URL(url3), HttpMethod.POST);
		webRequest2.setRequestBody(body1);
		webRequest2.setAdditionalHeader("Accept", "*/*");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest2.setAdditionalHeader("ajaxRequest", "true");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Content-Type", "application/json");
		webRequest2.setAdditionalHeader("Host", "222.216.5.212:8081");
		webRequest2.setAdditionalHeader("Origin", "http://222.216.5.212:8081");
		webRequest2.setAdditionalHeader("Referer", "http://222.216.5.212:8081/siweb/emp_payinof_query.do?method=begin");
		webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page4 = webClient.getPage(webRequest2);
		System.out.println(page4.getWebResponse().getContentAsString());
		InsuranceNanNingMaternity j =null;
		
		WebParam<InsuranceNanNingMaternity> webParam = new WebParam<InsuranceNanNingMaternity>();
		if(page4.getWebResponse().getContentAsString().contains("primary"))
		{
			JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
			String string = fromObject.getString("body");
			System.out.println(string);
			JSONObject fromObject2 = JSONObject.fromObject(string);
			String string2 = fromObject2.getString("dataStores");
			JSONObject fromObject3 = JSONObject.fromObject(string2);
			String string3 = fromObject3.getString("");
			JSONObject fromObject4 = JSONObject.fromObject(string3);
			String string4 = fromObject4.getString("rowSet");
			JSONObject fromObject5 = JSONObject.fromObject(string4);
			String string5 = fromObject5.getString("primary");
			JSONArray fromObject6 = JSONArray.fromObject(string5);
			List<InsuranceNanNingMaternity> list = new ArrayList<InsuranceNanNingMaternity>();
			for (int i = 0; i < fromObject6.size(); i++) {
				j = new InsuranceNanNingMaternity();
				JSONObject fromObject7 = JSONObject.fromObject(fromObject6.get(i));
				System.out.println(fromObject7.getString("WEB_V_AC20_AAE216"));
				j.setWEB_V_AC20_AAC001(fromObject7.getString("WEB_V_AC20_AAC001"));
				j.setWEB_V_AC20_AAB004(fromObject7.getString("WEB_V_AC20_AAB004"));
				j.setWEB_V_AC20_AAE003(fromObject7.getString("WEB_V_AC20_AAE003"));
				j.setWEB_V_AC20_AAE140(fromObject7.getString("WEB_V_AC20_AAE140"));
				j.setWEB_V_AC20_AAE210(fromObject7.getString("WEB_V_AC20_AAE210"));
				j.setWEB_V_AC20_AAC130(fromObject7.getString("WEB_V_AC20_AAC130"));
				j.setWEB_V_AC20_AAB001(fromObject7.getString("WEB_V_AC20_AAB001"));
				j.setTaskid(taskInsurance.getTaskid());

				list.add(j);
			}
			System.out.println(list);
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url3);
			return webParam;
		}
		return null;
	}

	public WebParam<InsuranceNanNingInjury> getInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskInsurance);
		
		String url3="http://222.216.5.212:8081/siweb/rpc.do?method=doQuery";
		String body="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"\",pageNumber:1,pageSize:10,recordCount:0,rowSetName:\"nn_apply.web_v_ac20\",parameters:{\"gridid\":\"grid1\"},condition:\"WEB_V_AC20.AAE003 >='"+getDateBefore("yyyyMM", 3)+"' and WEB_V_AC20.AAE003 <='"+getTime("yyyyMM")+"' and WEB_V_AC20.AAE140 = '41' and [WEB_V_AC20_AAC001]='"+insuranceRequestParameters.getName()+"' and WEB_V_AC20.AAE114 ='1'\"}},parameters:{\"synCount\":\"true\"}}}";
		WebRequest webRequest2 = new WebRequest(new URL(url3), HttpMethod.POST);
		webRequest2.setRequestBody(body);
		webRequest2.setAdditionalHeader("Accept", "*/*");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest2.setAdditionalHeader("ajaxRequest", "true");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Content-Type", "application/json");
		webRequest2.setAdditionalHeader("Host", "222.216.5.212:8081");
		webRequest2.setAdditionalHeader("Origin", "http://222.216.5.212:8081");
		webRequest2.setAdditionalHeader("Referer", "http://222.216.5.212:8081/siweb/emp_payinof_query.do?method=begin");
		webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page4 = webClient.getPage(webRequest2);
		System.out.println(page4.getWebResponse().getContentAsString());
		InsuranceNanNingInjury j = null;
		
		
		WebParam<InsuranceNanNingInjury> webParam = new WebParam<InsuranceNanNingInjury>();
		if(page4.getWebResponse().getContentAsString().contains("primary"))
		{
		JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
		String string = fromObject.getString("body");
		System.out.println(string);
		JSONObject fromObject2 = JSONObject.fromObject(string);
		String string2 = fromObject2.getString("dataStores");
		JSONObject fromObject3 = JSONObject.fromObject(string2);
		String string3 = fromObject3.getString("");
		JSONObject fromObject4 = JSONObject.fromObject(string3);
		String string4 = fromObject4.getString("rowSet");
		JSONObject fromObject5 = JSONObject.fromObject(string4);
		String string5 = fromObject5.getString("primary");
		JSONArray fromObject6 = JSONArray.fromObject(string5);
		List<InsuranceNanNingInjury> list = new ArrayList<InsuranceNanNingInjury>();
		for (int i = 0; i < fromObject6.size(); i++) {
			j = new InsuranceNanNingInjury();
			JSONObject fromObject7 = JSONObject.fromObject(fromObject6.get(i));
			System.out.println(fromObject7.getString("WEB_V_AC20_AAE216"));
			j.setWEB_V_AC20_AAC001(fromObject7.getString("WEB_V_AC20_AAC001"));
			j.setWEB_V_AC20_AAB004(fromObject7.getString("WEB_V_AC20_AAB004"));
			j.setWEB_V_AC20_AAE003(fromObject7.getString("WEB_V_AC20_AAE003"));
			j.setWEB_V_AC20_AAE140(fromObject7.getString("WEB_V_AC20_AAE140"));
			j.setWEB_V_AC20_AAE210(fromObject7.getString("WEB_V_AC20_AAE210"));
			j.setWEB_V_AC20_AAC130(fromObject7.getString("WEB_V_AC20_AAC130"));
			j.setWEB_V_AC20_AAB001(fromObject7.getString("WEB_V_AC20_AAB001"));
			j.setTaskid(taskInsurance.getTaskid());

			list.add(j);
		}
		System.out.println(list);
		webParam.setHtml(page4.getWebResponse().getContentAsString());
		webParam.setList(list);
		webParam.setUrl(url3);
		return webParam;
	}
	return null;
	}

	public WebParam<InsuranceNanNingUnemployment> getUnemployment(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance) throws Exception {
		
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskInsurance);
		
		String url3="http://222.216.5.212:8081/siweb/rpc.do?method=doQuery";
		String body="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"\",pageNumber:1,pageSize:10,recordCount:0,rowSetName:\"nn_apply.web_v_ac20\",parameters:{\"gridid\":\"grid1\"},condition:\"WEB_V_AC20.AAE003 >='"+getDateBefore("yyyyMM", 3)+"' and WEB_V_AC20.AAE003 <='"+getTime("yyyyMM")+"' and WEB_V_AC20.AAE140 = '21' and [WEB_V_AC20_AAC001]='"+insuranceRequestParameters.getName()+"' and WEB_V_AC20.AAE114 ='1'\"}},parameters:{\"synCount\":\"true\"}}}";
		WebRequest webRequest2 = new WebRequest(new URL(url3), HttpMethod.POST);
		webRequest2.setRequestBody(body);
		webRequest2.setAdditionalHeader("Accept", "*/*");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest2.setAdditionalHeader("ajaxRequest", "true");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Content-Type", "application/json");
		webRequest2.setAdditionalHeader("Host", "222.216.5.212:8081");
		webRequest2.setAdditionalHeader("Origin", "http://222.216.5.212:8081");
		webRequest2.setAdditionalHeader("Referer", "http://222.216.5.212:8081/siweb/emp_payinof_query.do?method=begin");
		webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page4 = webClient.getPage(webRequest2);
		System.out.println(page4.getWebResponse().getContentAsString());
		InsuranceNanNingUnemployment j =null;
		
		WebParam<InsuranceNanNingUnemployment> webParam = new WebParam<InsuranceNanNingUnemployment>();
		if(page4.getWebResponse().getContentAsString().contains("primary"))
		{
		JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
		String string = fromObject.getString("body");
		System.out.println(string);
		JSONObject fromObject2 = JSONObject.fromObject(string);
		String string2 = fromObject2.getString("dataStores");
		JSONObject fromObject3 = JSONObject.fromObject(string2);
		String string3 = fromObject3.getString("");
		JSONObject fromObject4 = JSONObject.fromObject(string3);
		String string4 = fromObject4.getString("rowSet");
		JSONObject fromObject5 = JSONObject.fromObject(string4);
		String string5 = fromObject5.getString("primary");
		JSONArray fromObject6 = JSONArray.fromObject(string5);
		List<InsuranceNanNingUnemployment> list = new ArrayList<InsuranceNanNingUnemployment>();
		for (int i = 0; i < fromObject6.size(); i++) {
			j = new InsuranceNanNingUnemployment();
			JSONObject fromObject7 = JSONObject.fromObject(fromObject6.get(i));
			System.out.println(fromObject7.getString("WEB_V_AC20_AAE216"));
			j.setWEB_V_AC20_AAC001(fromObject7.getString("WEB_V_AC20_AAC001"));
			j.setWEB_V_AC20_AAB004(fromObject7.getString("WEB_V_AC20_AAB004"));
			j.setWEB_V_AC20_AAE003(fromObject7.getString("WEB_V_AC20_AAE003"));
			j.setWEB_V_AC20_AAE140(fromObject7.getString("WEB_V_AC20_AAE140"));
			j.setWEB_V_AC20_AAE210(fromObject7.getString("WEB_V_AC20_AAE210"));
			j.setWEB_V_AC20_AAC130(fromObject7.getString("WEB_V_AC20_AAC130"));
			j.setWEB_V_AC20_AAB001(fromObject7.getString("WEB_V_AC20_AAB001"));
			j.setTaskid(taskInsurance.getTaskid());

			list.add(j);
		}
		System.out.println(list);
		webParam.setHtml(page4.getWebResponse().getContentAsString());
		webParam.setList(list);
		webParam.setUrl(url3);
		return webParam;
	}
	return null;
	}

	
	//养老
	public WebParam<InsuranceNanNingEndowment> getEndowment(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance) throws Exception {
		
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskInsurance);
		
		//基本养老
		String url3="http://222.216.5.212:8081/siweb/rpc.do?method=doQuery";
		String body="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"\",pageNumber:1,pageSize:10,recordCount:0,rowSetName:\"nn_apply.web_v_ac20\",parameters:{\"gridid\":\"grid1\"},condition:\"WEB_V_AC20.AAE003 >='"+getDateBefore("yyyyMM", 3)+"' and WEB_V_AC20.AAE003 <='"+getTime("yyyyMM")+"' and WEB_V_AC20.AAE140 = '11' and [WEB_V_AC20_AAC001]='"+insuranceRequestParameters.getName()+"' and WEB_V_AC20.AAE114 ='1'\"}},parameters:{\"synCount\":\"true\"}}}";
		WebRequest webRequest2 = new WebRequest(new URL(url3), HttpMethod.POST);
		webRequest2.setRequestBody(body);
		webRequest2.setAdditionalHeader("Accept", "*/*");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest2.setAdditionalHeader("ajaxRequest", "true");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Content-Type", "application/json");
		webRequest2.setAdditionalHeader("Host", "222.216.5.212:8081");
		webRequest2.setAdditionalHeader("Origin", "http://222.216.5.212:8081");
		webRequest2.setAdditionalHeader("Referer", "http://222.216.5.212:8081/siweb/emp_payinof_query.do?method=begin");
		webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page4 = webClient.getPage(webRequest2);
		System.out.println(page4.getWebResponse().getContentAsString());
		InsuranceNanNingEndowment j = null;
		List<InsuranceNanNingEndowment> list = new ArrayList<InsuranceNanNingEndowment>();
		WebParam<InsuranceNanNingEndowment> webParam = new WebParam<InsuranceNanNingEndowment>();
		if(page4.getWebResponse().getContentAsString().contains("primary"))
		{
		JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
		String string = fromObject.getString("body");
		System.out.println(string);
		JSONObject fromObject2 = JSONObject.fromObject(string);
		String string2 = fromObject2.getString("dataStores");
		JSONObject fromObject3 = JSONObject.fromObject(string2);
		String string3 = fromObject3.getString("");
		JSONObject fromObject4 = JSONObject.fromObject(string3);
		String string4 = fromObject4.getString("rowSet");
		JSONObject fromObject5 = JSONObject.fromObject(string4);
		String string5 = fromObject5.getString("primary");
		JSONArray fromObject6 = JSONArray.fromObject(string5);
		int size = fromObject6.size();
		if(size>0)
		{
			for (int i = 0; i < fromObject6.size(); i++) {
				j = new InsuranceNanNingEndowment();
				JSONObject fromObject7 = JSONObject.fromObject(fromObject6.get(i));
				System.out.println(fromObject7.getString("WEB_V_AC20_AAE216"));
				j.setWEB_V_AC20_AAC001(fromObject7.getString("WEB_V_AC20_AAC001"));
				j.setWEB_V_AC20_AAB004(fromObject7.getString("WEB_V_AC20_AAB004"));
				j.setWEB_V_AC20_AAE003(fromObject7.getString("WEB_V_AC20_AAE003"));
				j.setWEB_V_AC20_AAE140(fromObject7.getString("WEB_V_AC20_AAE140"));
				j.setWEB_V_AC20_AAE210(fromObject7.getString("WEB_V_AC20_AAE210"));
				j.setWEB_V_AC20_AAC130(fromObject7.getString("WEB_V_AC20_AAC130"));
				j.setWEB_V_AC20_AAB001(fromObject7.getString("WEB_V_AC20_AAB001"));
				j.setTaskid(taskInsurance.getTaskid());

				list.add(j);
			}
			System.out.println(list);
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url3);
			return webParam;
		}
		else if(size==0)
		{
			//机关养老
			String url4="http://222.216.5.212:8081/siweb/rpc.do?method=doQuery";
			String body4="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"\",pageNumber:1,pageSize:10,recordCount:0,rowSetName:\"nn_apply.web_v_ac20\",parameters:{\"gridid\":\"grid1\"},condition:\"WEB_V_AC20.AAE003 >='"+getDateBefore("yyyyMM", 3)+"' and WEB_V_AC20.AAE003 <='"+getTime("yyyyMM")+"' and WEB_V_AC20.AAE140 = '12' and [WEB_V_AC20_AAC001]='"+insuranceRequestParameters.getName()+"' and WEB_V_AC20.AAE114 ='1'\"}},parameters:{\"synCount\":\"true\"}}}";
			WebRequest webRequest4 = new WebRequest(new URL(url4), HttpMethod.POST);
			webRequest4.setRequestBody(body4);
			webRequest4.setAdditionalHeader("Accept", "*/*");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest4.setAdditionalHeader("ajaxRequest", "true");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Content-Type", "application/json");
			webRequest4.setAdditionalHeader("Host", "222.216.5.212:8081");
			webRequest4.setAdditionalHeader("Origin", "http://222.216.5.212:8081");
			webRequest4.setAdditionalHeader("Referer", "http://222.216.5.212:8081/siweb/emp_payinof_query.do?method=begin");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
			webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			Page page44 = webClient.getPage(webRequest4);
			System.out.println(page44.getWebResponse().getContentAsString());
			
			if(page4.getWebResponse().getContentAsString().contains("primary"))
			{
				JSONObject from = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
				String str = from.getString("body");
				System.out.println(str);
				JSONObject from2 = JSONObject.fromObject(str);
				String str2 = from2.getString("dataStores");
				JSONObject from3 = JSONObject.fromObject(str2);
				String str3 = from3.getString("");
				JSONObject from4 = JSONObject.fromObject(str3);
				String str4 = from4.getString("rowSet");
				JSONObject from5 = JSONObject.fromObject(str4);
				String str5 = from5.getString("primary");
				JSONArray from6 = JSONArray.fromObject(str5);
				int size1 = from6.size();
				if(size1>0)
				{
					for (int i = 0; i < fromObject6.size(); i++) {
						j = new InsuranceNanNingEndowment();
						JSONObject fromObject7 = JSONObject.fromObject(fromObject6.get(i));
						System.out.println(fromObject7.getString("WEB_V_AC20_AAE216"));
						j.setWEB_V_AC20_AAC001(fromObject7.getString("WEB_V_AC20_AAC001"));
						j.setWEB_V_AC20_AAB004(fromObject7.getString("WEB_V_AC20_AAB004"));
						j.setWEB_V_AC20_AAE003(fromObject7.getString("WEB_V_AC20_AAE003"));
						j.setWEB_V_AC20_AAE140(fromObject7.getString("WEB_V_AC20_AAE140"));
						j.setWEB_V_AC20_AAE210(fromObject7.getString("WEB_V_AC20_AAE210"));
						j.setWEB_V_AC20_AAC130(fromObject7.getString("WEB_V_AC20_AAC130"));
						j.setWEB_V_AC20_AAB001(fromObject7.getString("WEB_V_AC20_AAB001"));
						j.setTaskid(taskInsurance.getTaskid());
						list.add(j);
					}
					System.out.println(list);
					webParam.setHtml(page4.getWebResponse().getContentAsString());
					webParam.setList(list);
					webParam.setUrl(url3);
					return webParam;
				}
			}
		}
	}
	return null;
	}

	public WebParam<InsuranceNanNingMedical> getMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskInsurance);
		
		String url3="http://222.216.5.212:8081/siweb/rpc.do?method=doQuery";
		String body="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"\",pageNumber:1,pageSize:1000,recordCount:0,rowSetName:\"nn_apply.web_v_ac20\",parameters:{\"gridid\":\"grid1\"},condition:\"WEB_V_AC20.AAE003 >='"+getDateBefore("yyyyMM", 3)+"' and WEB_V_AC20.AAE003 <='"+getTime("yyyyMM")+"' and WEB_V_AC20.AAE140 = '31' and [WEB_V_AC20_AAC001]='"+insuranceRequestParameters.getName()+"' and WEB_V_AC20.AAE114 ='1'\"}},parameters:{\"synCount\":\"true\"}}}";
		WebRequest webRequest2 = new WebRequest(new URL(url3), HttpMethod.POST);
		webRequest2.setRequestBody(body);
		webRequest2.setAdditionalHeader("Accept", "*/*");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest2.setAdditionalHeader("ajaxRequest", "true");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Content-Type", "application/json");
		webRequest2.setAdditionalHeader("Host", "222.216.5.212:8081");
		webRequest2.setAdditionalHeader("Origin", "http://222.216.5.212:8081");
		webRequest2.setAdditionalHeader("Referer", "http://222.216.5.212:8081/siweb/emp_payinof_query.do?method=begin");
		webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page4 = webClient.getPage(webRequest2);
		System.out.println(page4.getWebResponse().getContentAsString());
		InsuranceNanNingMedical j = null;
		
		WebParam<InsuranceNanNingMedical> webParam = new WebParam<InsuranceNanNingMedical>();
		if(page4.getWebResponse().getContentAsString().contains("primary"))
		{
		JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
		String string = fromObject.getString("body");
		System.out.println(string);
		JSONObject fromObject2 = JSONObject.fromObject(string);
		String string2 = fromObject2.getString("dataStores");
		JSONObject fromObject3 = JSONObject.fromObject(string2);
		String string3 = fromObject3.getString("");
		JSONObject fromObject4 = JSONObject.fromObject(string3);
		String string4 = fromObject4.getString("rowSet");
		JSONObject fromObject5 = JSONObject.fromObject(string4);
		String string5 = fromObject5.getString("primary");
		JSONArray fromObject6 = JSONArray.fromObject(string5);
		List<InsuranceNanNingMedical> list = new ArrayList<InsuranceNanNingMedical>();
		for (int i = 0; i < fromObject6.size(); i++) {
			j = new InsuranceNanNingMedical();			JSONObject fromObject7 = JSONObject.fromObject(fromObject6.get(i));
			System.out.println(fromObject7.getString("WEB_V_AC20_AAE216"));
			j.setWEB_V_AC20_AAC001(fromObject7.getString("WEB_V_AC20_AAC001"));
			j.setWEB_V_AC20_AAB004(fromObject7.getString("WEB_V_AC20_AAB004"));
			j.setWEB_V_AC20_AAE003(fromObject7.getString("WEB_V_AC20_AAE003"));
			j.setWEB_V_AC20_AAE140(fromObject7.getString("WEB_V_AC20_AAE140"));
			j.setWEB_V_AC20_AAE210(fromObject7.getString("WEB_V_AC20_AAE210"));
			j.setWEB_V_AC20_AAC130(fromObject7.getString("WEB_V_AC20_AAC130"));
			j.setWEB_V_AC20_AAB001(fromObject7.getString("WEB_V_AC20_AAB001"));
			j.setTaskid(taskInsurance.getTaskid());

			list.add(j);
		}
		System.out.println(list);
		webParam.setHtml(page4.getWebResponse().getContentAsString());
		webParam.setList(list);
		webParam.setUrl(url3);
		return webParam;
	}
	return null;
	}
	
	
	public WebClient addcookie(WebClient webclient, TaskInsurance taskInsurance) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}
		return webclient;
	}
	
	// 当前时间
	public String getTime(String fmt) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}

	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

}
