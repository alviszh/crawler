package app.parser;

import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiEndowment;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiInjury;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiMaternity;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiMedical;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiUnemployment;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceSZShanXiParser {

	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance)
			throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String md5 = MD5(insuranceRequestParameters.getPassword());
		String md51 = MD5(insuranceRequestParameters.getPassword()+"MSYOS");
		System.out.println(md5.toLowerCase());
		System.out.println(md51.toLowerCase());
		String url = "http://sx.msyos.com/user/login.html?username="+insuranceRequestParameters.getUsername()+"&password=" + md5.toLowerCase()+ "&password_v1=" + md51.toLowerCase();
//		System.out.println(url);
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(requestSettings);
		
		System.out.println(page.getWebResponse().getContentAsString());
		WebParam webParam  = new WebParam();
		if (page.getWebResponse().getContentAsString().contains("userinfo")) 
		{
			JSONObject fromObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
			String token = fromObject.getString("token");
			webParam.setUrl(url);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setWebHandle(token);
			return webParam;
		}
		return null;
	}

	public String MD5(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(s.getBytes("utf-8"));
			return toHex(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String toHex(byte[] bytes) {

		final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
		StringBuilder ret = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
			ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
		}
		return ret.toString();
	}

	//过去时间
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

	//当前时间
	public String getDate(String fmt) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	
	
	//养老
	public WebParam<InsuranceSZShanXiEndowment> crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance,
			int i) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String cookies2 = taskInsurance.getCookies();
//		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
//		for (Cookie cookie : cookies) {
//			webClient.getCookieManager().addCookie(cookie);
//	    }
		//企业职工养老
		String url2=" http://sx.msyos.com/baseInfo/querySbjfForJson.html?rownum=1000&startrow=1&startDate="+getDateBefore("yyyy-MM", i)+"&endDate="+getDate("yyyy-MM")+"&xzlx=110&token="+taskInsurance.getWebdriverHandle();
		WebRequest webRequest = new WebRequest(new URL(url2),HttpMethod.POST);
		Page page2 = webClient.getPage(webRequest);
		Thread.sleep(1000);
		System.out.println(page2.getWebResponse().getContentAsString());
		
		if(page2.getWebResponse().getContentAsString().contains("noInfo"))
		{
			//机关事业单位养老
			String url3="http://sx.msyos.com/baseInfo/querySbjfForJson.html?rownum=1000&startrow=1&startDate="+getDateBefore("yyyy-MM", i)+"&endDate="+getDate("yyyy-MM")+"&xzlx=120&token="+taskInsurance.getWebdriverHandle();
			WebRequest webRequest1 = new WebRequest(new URL(url3),HttpMethod.POST);
			Page page3 = webClient.getPage(webRequest1);
			Thread.sleep(1000);
			
			if(page3.getWebResponse().getContentAsString().contains("queryList"))
			{
				JSONObject fromObject2 = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
				String string = fromObject2.getString("queryList");
				JSONArray fromObject3 = JSONArray.fromObject(string.toUpperCase());
				InsuranceSZShanXiEndowment in =null;
				List<InsuranceSZShanXiEndowment> list = new ArrayList<InsuranceSZShanXiEndowment>();
				WebParam<InsuranceSZShanXiEndowment> webParam = new WebParam<InsuranceSZShanXiEndowment>();
				for (int j = 0; j < fromObject3.size(); j++) {
//					System.out.println(fromObject3.get(i));
					JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(j));
//					System.out.println(string2);
					in =new InsuranceSZShanXiEndowment();
					if(fromObject4.getString("AAE143").contains("1"))
					{
						in.setType("正常应缴记录");
					}
					else
					{
						in.setType("非正常应缴记录");
					}
					in.setStartDate(fromObject4.getString("AAE041"));
					in.setEndDate(fromObject4.getString("AAE042"));
					in.setPayMoney(fromObject4.getString("BCC006"));
					in.setPersonalPay(fromObject4.getString("AAC123"));
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				System.out.println(list);
				webParam.setHtml(page3.getWebResponse().getContentAsString());
				webParam.setUrl(url2);
				webParam.setList(list);
				return webParam;
			}
			else{
				return null;
			}
		}
		else if(page2.getWebResponse().getContentAsString().contains("queryList"))
		{
			JSONObject fromObject2 = JSONObject.fromObject(page2.getWebResponse().getContentAsString());
			String string = fromObject2.getString("queryList");
			JSONArray fromObject3 = JSONArray.fromObject(string.toUpperCase());
			InsuranceSZShanXiEndowment in =null;
			List<InsuranceSZShanXiEndowment> list = new ArrayList<InsuranceSZShanXiEndowment>();
			WebParam<InsuranceSZShanXiEndowment> webParam = new WebParam<InsuranceSZShanXiEndowment>();
			for (int j = 0; j < fromObject3.size(); j++) {
//				System.out.println(fromObject3.get(i));
				JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(j));
//				System.out.println(string2);
				in =new InsuranceSZShanXiEndowment();
				if(fromObject4.getString("AAE143").contains("1"))
				{
					in.setType("正常应缴记录");
				}
				else
				{
					in.setType("非正常应缴记录");
				}
				in.setStartDate(fromObject4.getString("AAE041"));
				in.setEndDate(fromObject4.getString("AAE042"));
				in.setPayMoney(fromObject4.getString("BCC006"));
				in.setPersonalPay(fromObject4.getString("AAC123"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			System.out.println(list);
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			webParam.setUrl(url2);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}

	
	//医疗
	public WebParam<InsuranceSZShanXiMedical> crawlerMedical(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance, int i) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String cookies2 = taskInsurance.getCookies();
//		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
//		for (Cookie cookie : cookies) {
//			webClient.getCookieManager().addCookie(cookie);
//	    }
		
		String url2="http://sx.msyos.com/baseInfo/querySbjfForJson.html?rownum=1000&startrow=1&startDate="+getDateBefore("yyyy-MM", i)+"&endDate="+getDate("yyyy-MM")+"&xzlx=390&token="+taskInsurance.getWebdriverHandle();
		WebRequest webRequest = new WebRequest(new URL(url2),HttpMethod.POST);
		Page page3 = webClient.getPage(webRequest);
		Thread.sleep(1000);
		System.out.println(page3.getWebResponse().getContentAsString());
		if(page3.getWebResponse().getContentAsString().contains("noInfo"))
		{
			String url4="http://sx.msyos.com/baseInfo/querySbjfForJson.html?rownum=1000&startrow=1&startDate="+getDateBefore("yyyy-MM", i)+"&endDate="+getDate("yyyy-MM")+"&xzlx=310&token="+taskInsurance.getWebdriverHandle();
			WebRequest webRequest1 = new WebRequest(new URL(url4),HttpMethod.POST);
			Page page4 = webClient.getPage(webRequest1);
			Thread.sleep(1000);
			if(page4.getWebResponse().getContentAsString().contains("queryList"))
			{
				JSONObject fromObject2 = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
				String string = fromObject2.getString("queryList");
				JSONArray fromObject3 = JSONArray.fromObject(string.toUpperCase());
				InsuranceSZShanXiMedical in =null;
				List<InsuranceSZShanXiMedical> list = new ArrayList<InsuranceSZShanXiMedical>();
				WebParam<InsuranceSZShanXiMedical> webParam = new WebParam<InsuranceSZShanXiMedical>();
				for (int j = 0; j < fromObject3.size(); j++) {
//					System.out.println(fromObject3.get(i));
					JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(j));
//					System.out.println(string2);
					in =new InsuranceSZShanXiMedical();
					if(fromObject4.getString("AAE143").contains("1"))
					{
						in.setType("正常应缴记录");
					}
					else
					{
						in.setType("非正常应缴记录");
					}
					in.setStartDate(fromObject4.getString("AAE041"));
					in.setEndDate(fromObject4.getString("AAE042"));
					in.setPayMoney(fromObject4.getString("BCC006"));
					in.setPersonalPay(fromObject4.getString("AAC123"));
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				System.out.println(list);
				webParam.setHtml(page3.getWebResponse().getContentAsString());
				webParam.setUrl(url2);
				webParam.setList(list);
				return webParam;
			}
			else
			{
				return null;
			}
		}
		else if(page3.getWebResponse().getContentAsString().contains("queryList"))
		{
			JSONObject fromObject2 = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
			String string = fromObject2.getString("queryList");
			JSONArray fromObject3 = JSONArray.fromObject(string.toUpperCase());
			InsuranceSZShanXiMedical in =null;
			List<InsuranceSZShanXiMedical> list = new ArrayList<InsuranceSZShanXiMedical>();
			WebParam<InsuranceSZShanXiMedical> webParam = new WebParam<InsuranceSZShanXiMedical>();
			for (int j = 0; j < fromObject3.size(); j++) {
//				System.out.println(fromObject3.get(i));
				JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(j));
//				System.out.println(string2);
				in =new InsuranceSZShanXiMedical();
				if(fromObject4.getString("AAE143").contains("1"))
				{
					in.setType("正常应缴记录");
				}
				else
				{
					in.setType("非正常应缴记录");
				}
				in.setStartDate(fromObject4.getString("AAE041"));
				in.setEndDate(fromObject4.getString("AAE042"));
				in.setPayMoney(fromObject4.getString("BCC006"));
				in.setPersonalPay(fromObject4.getString("AAC123"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			System.out.println(list);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(url2);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}

	
	//生育
	public WebParam<InsuranceSZShanXiMaternity> crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance, int i) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String cookies2 = taskInsurance.getCookies();
//		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
//		for (Cookie cookie : cookies) {
//			webClient.getCookieManager().addCookie(cookie);
//	    }
		
		String url2="http://sx.msyos.com/baseInfo/querySbjfForJson.html?rownum=1000&startrow=1&startDate="+getDateBefore("yyyy-MM", i)+"&endDate="+getDate("yyyy-MM")+"&xzlx=510&token="+taskInsurance.getWebdriverHandle();
		WebRequest webRequest = new WebRequest(new URL(url2),HttpMethod.POST);
		Page page3 = webClient.getPage(webRequest);
		Thread.sleep(1000);
		System.out.println(page3.getWebResponse().getContentAsString());
		if(page3.getWebResponse().getContentAsString().contains("queryList"))
		{
			JSONObject fromObject2 = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
			String string = fromObject2.getString("queryList");
			JSONArray fromObject3 = JSONArray.fromObject(string.toUpperCase());
			InsuranceSZShanXiMaternity in =null;
			List<InsuranceSZShanXiMaternity> list = new ArrayList<InsuranceSZShanXiMaternity>();
			WebParam<InsuranceSZShanXiMaternity> webParam = new WebParam<InsuranceSZShanXiMaternity>();
			for (int j = 0; j < fromObject3.size(); j++) {
//				System.out.println(fromObject3.get(i));
				JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(j));
//				System.out.println(string2);
				in =new InsuranceSZShanXiMaternity();
				if(fromObject4.getString("AAE143").contains("1"))
				{
					in.setType("正常应缴记录");
				}
				else
				{
					in.setType("非正常应缴记录");
				}
				in.setStartDate(fromObject4.getString("AAE041"));
				in.setEndDate(fromObject4.getString("AAE042"));
				in.setPayMoney(fromObject4.getString("BCC006"));
				in.setPersonalPay(fromObject4.getString("AAC123"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			System.out.println(list);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(url2);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}

	
	//工伤
	public WebParam<InsuranceSZShanXiInjury> crawlerInjury(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance, int i) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String cookies2 = taskInsurance.getCookies();
//		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
//		for (Cookie cookie : cookies) {
//			webClient.getCookieManager().addCookie(cookie);
//	    }
		String url2="http://sx.msyos.com/baseInfo/querySbjfForJson.html?rownum=1000&startrow=1&startDate="+getDateBefore("yyyy-MM", i)+"&endDate="+getDate("yyyy-MM")+"&xzlx=410&token="+taskInsurance.getWebdriverHandle();
		WebRequest webRequest = new WebRequest(new URL(url2),HttpMethod.POST);
		Page page3 = webClient.getPage(webRequest);
		Thread.sleep(1000);
		System.out.println(page3.getWebResponse().getContentAsString());
		if(page3.getWebResponse().getContentAsString().contains("queryList"))
		{
			JSONObject fromObject2 = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
			String string = fromObject2.getString("queryList");
			JSONArray fromObject3 = JSONArray.fromObject(string.toUpperCase());
			InsuranceSZShanXiInjury in =null;
			List<InsuranceSZShanXiInjury> list = new ArrayList<InsuranceSZShanXiInjury>();
			WebParam<InsuranceSZShanXiInjury> webParam = new WebParam<InsuranceSZShanXiInjury>();
			for (int j = 0; j < fromObject3.size(); j++) {
//				System.out.println(fromObject3.get(i));
				JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(j));
//				System.out.println(string2);
				in =new InsuranceSZShanXiInjury();
				
				if(fromObject4.getString("AAE143").contains("1"))
				{
					in.setType("正常应缴记录");
				}
				else
				{
					in.setType("非正常应缴记录");
				}
				in.setStartDate(fromObject4.getString("AAE041"));
				in.setEndDate(fromObject4.getString("AAE042"));
				in.setPayMoney(fromObject4.getString("BCC006"));
				in.setPersonalPay(fromObject4.getString("AAC123"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			System.out.println(list);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(url2);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}

	
	//失业
	public WebParam<InsuranceSZShanXiUnemployment> crawlerUnemployment(
			InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance, int i) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String cookies2 = taskInsurance.getCookies();
//		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
//		for (Cookie cookie : cookies) {
//			webClient.getCookieManager().addCookie(cookie);
//	    }
		String url2="http://sx.msyos.com/baseInfo/querySbjfForJson.html?rownum=1000&startrow=1&startDate="+getDateBefore("yyyy-MM", i)+"&endDate="+getDate("yyyy-MM")+"&xzlx=210&token="+taskInsurance.getWebdriverHandle();

		WebRequest webRequest = new WebRequest(new URL(url2),HttpMethod.POST);
		Page page3 = webClient.getPage(webRequest);
		Thread.sleep(1000);
		System.out.println(page3.getWebResponse().getContentAsString());
		if(page3.getWebResponse().getContentAsString().contains("queryList"))
		{
			JSONObject fromObject2 = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
			String string = fromObject2.getString("queryList");
			JSONArray fromObject3 = JSONArray.fromObject(string.toUpperCase());
			InsuranceSZShanXiUnemployment in =null;
			List<InsuranceSZShanXiUnemployment> list = new ArrayList<InsuranceSZShanXiUnemployment>();
			WebParam<InsuranceSZShanXiUnemployment> webParam = new WebParam<InsuranceSZShanXiUnemployment>();
			for (int j = 0; j < fromObject3.size(); j++) {
//				System.out.println(fromObject3.get(i));
				JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(j));
//				System.out.println(string2);
				in =new InsuranceSZShanXiUnemployment();
				if(fromObject4.getString("AAE143").contains("1"))
				{
					in.setType("正常应缴记录");
				}
				else
				{
					in.setType("非正常应缴记录");
				}
				in.setStartDate(fromObject4.getString("AAE041"));
				in.setEndDate(fromObject4.getString("AAE042"));
				in.setPayMoney(fromObject4.getString("BCC006"));
				in.setPersonalPay(fromObject4.getString("AAC123"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			System.out.println(list);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(url2);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}

	
	//个人信息
	public WebParam<InsuranceSZShanXiUserInfo> crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception{
		String testhtml = taskInsurance.getTesthtml();
		if(testhtml.contains("userinfo"))
		{
			InsuranceSZShanXiUserInfo i = new InsuranceSZShanXiUserInfo();
			JSONObject fromObject = JSONObject.fromObject(testhtml);
			String token = fromObject.getString("userinfo");
//			String replaceAll = token.replaceAll("\\", "");
			JSONObject fromObject2 = JSONObject.fromObject(token);
			i.setIDNum(fromObject2.getString("aac002"));
			i.setName(fromObject2.getString("aac003"));
			i.setBirth(fromObject2.getString("aac006"));
			i.setCardNum(fromObject2.getString("bcc002"));
			WebParam<InsuranceSZShanXiUserInfo> webParam = new WebParam<InsuranceSZShanXiUserInfo>();
			webParam.setHtml(testhtml);
			webParam.setInsuranceSZShanXiUserInfo(i);
			return webParam;
		}
		
		return null;
	}
	
}
