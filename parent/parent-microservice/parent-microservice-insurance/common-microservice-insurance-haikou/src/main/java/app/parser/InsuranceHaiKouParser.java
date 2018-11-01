package app.parser;

import java.net.URL;
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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouEndowment;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouInjury;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouMaternity;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouMedical;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouUnemployment;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class InsuranceHaiKouParser {

	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url="http://202.100.251.116:8880/uaa/api/person/idandmobile?client_id=acme&redirect_uri=http://202.100.251.116:8880/ehrss/si/person/ui/&response_type=code";
		HtmlPage page = webClient.getPage(url);		
		Thread.sleep(1000);
		WebParam webParam = new WebParam();
//		System.out.println(page.getWebResponse().getContentAsString());
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies) {
//			System.out.println("登录Page获取到的cookie是：" + cookie.toString());
			if(cookie.toString().contains("XSRF"))
			{
				String substring = cookie.toString().substring(11,47);
				System.out.println(substring);
				String url2="http://202.100.251.116:8880/uaa/api/person/idandmobile/login?idnumber="+insuranceRequestParameters.getUsername()+"&mobilenumber="+insuranceRequestParameters.getName()+"&password="+insuranceRequestParameters.getPassword()+"&_csrf="+substring;
				WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
				Page page2 = webClient.getPage(webRequest);
				System.out.println(page2.getWebResponse().getContentAsString());
				if(page2.getWebResponse().getContentAsString().contains("海南省人力资源和社会保障网上大厅"))
				{
					webParam.setHtml(page2.getWebResponse().getContentAsString());
					webParam.setUrl(url2);
					webParam.setWebClient(webClient);
				}
			}
		}
		return webParam;
	}

	public WebParam<InsuranceHaiKouUserInfo> crawelerUser(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url="http://202.100.251.116:8880/ehrss-si-person/api/rights/persons/4000006004928672";
		Page page = webClient.getPage(url);
//		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<InsuranceHaiKouUserInfo> webParam = new WebParam<InsuranceHaiKouUserInfo>();
		if(page.getWebResponse().getContentAsString().contains("name"))
		{
			InsuranceHaiKouUserInfo i= new InsuranceHaiKouUserInfo();
			JSONObject fromObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
			
			i.setIDNum(fromObject.getString("certificateType"));
			i.setName(fromObject.getString("name"));
			if(fromObject.getString("sex").contains("1"))
			{
				i.setSex("男");
			}
			else
			{
				i.setSex("女");
			}
			if(fromObject.getString("nation").contains("01"))
			{
				i.setNational("汉族");
			}
			else
			{
				i.setNational("非汉族");
			}
			i.setBirth(fromObject.getString("birthday"));
			i.setJoinDate(fromObject.getString("workdate"));
			i.setHomeLand(fromObject.getString("accountLocation"));
			i.setAddr(fromObject.getString("householdType"));
			i.setSchool(fromObject.getString("educationalBackground"));
			i.setCode(fromObject.getString("administrativePost"));
			i.setMarry(fromObject.getString("maritalStatus"));
			i.setTaskid(taskInsurance.getTaskid());
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setInsuranceHaiKouUserInfo(i);
			webParam.setUrl(url);
		}
		return webParam;	
	}

	
	//医疗
	public WebParam<InsuranceHaiKouMedical> crawelerMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance,int time) throws Exception {
		String url="http://202.100.251.116:8880/ehrss-si-person/api/rights/payment/4000006004928672?insurance=310&year="+getDateBefore("yyyy", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		WebParam<InsuranceHaiKouMedical> webParam = new WebParam<InsuranceHaiKouMedical>();
		Page page = webClient.getPage(url);
		if(page.getWebResponse().getContentAsString().contains("unitname"))
		{
			
			List<InsuranceHaiKouMedical> list = new ArrayList<InsuranceHaiKouMedical>();
			InsuranceHaiKouMedical in =null;
			JSONArray fromObject = JSONArray.fromObject(page.getWebResponse().getContentAsString());
			for (int i = 0; i < fromObject.size(); i++) {
				in = new InsuranceHaiKouMedical();
				JSONObject fromObject2 = JSONObject.fromObject(fromObject.get(i));
				in.setCompany(fromObject2.getString("unitname"));
				in.setStatus(fromObject2.getString("insurance"));
				in.setDatea(fromObject2.getString("costperiod"));
				in.setBase(fromObject2.getString("paymentbase"));
				in.setCompanyPay(fromObject2.getString("unitacttransfersum"));
				in.setPersonalPay(fromObject2.getString("personalacttranssum"));
				in.setOtherPay(fromObject2.getString("otheracttransfersum"));
				in.setSum(fromObject2.getString("acttransfersum"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
//			System.out.println(list);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setList(list);
		}
		return webParam;
	}
	
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

	//养老
	public WebParam<InsuranceHaiKouEndowment> crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance,int time) throws Exception {
		String url="http://202.100.251.116:8880/ehrss-si-person/api/rights/payment/4000006004928672?insurance=110&year="+getDateBefore("yyyy", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url);
		WebParam<InsuranceHaiKouEndowment> webParam = new WebParam<InsuranceHaiKouEndowment>();
		if(page.getWebResponse().getContentAsString().contains("unitname"))
		{
			List<InsuranceHaiKouEndowment> list = new ArrayList<InsuranceHaiKouEndowment>();
			InsuranceHaiKouEndowment in =null;
			JSONArray fromObject = JSONArray.fromObject(page.getWebResponse().getContentAsString());
			for (int i = 0; i < fromObject.size(); i++) {
				in = new InsuranceHaiKouEndowment();
				JSONObject fromObject2 = JSONObject.fromObject(fromObject.get(i));
				in.setCompany(fromObject2.getString("unitname"));
				in.setStatus(fromObject2.getString("insurance"));
				in.setDatea(fromObject2.getString("costperiod"));
				in.setBase(fromObject2.getString("paymentbase"));
				in.setCompanyPay(fromObject2.getString("unitacttransfersum"));
				in.setPersonalPay(fromObject2.getString("personalacttranssum"));
				in.setOtherPay(fromObject2.getString("otheracttransfersum"));
				in.setSum(fromObject2.getString("acttransfersum"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
//			System.out.println(list);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setList(list);
		}
		return webParam;
	}

	
	//工伤
	public WebParam<InsuranceHaiKouInjury> crawlerInjury(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance,int time) throws Exception {
		String url="http://202.100.251.116:8880/ehrss-si-person/api/rights/payment/4000006004928672?insurance=410&year="+getDateBefore("yyyy", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url);
		WebParam<InsuranceHaiKouInjury> webParam = new WebParam<InsuranceHaiKouInjury>();
		if(page.getWebResponse().getContentAsString().contains("unitname"))
		{
			List<InsuranceHaiKouInjury> list = new ArrayList<InsuranceHaiKouInjury>();
			InsuranceHaiKouInjury in =null;
			JSONArray fromObject = JSONArray.fromObject(page.getWebResponse().getContentAsString());
			for (int i = 0; i < fromObject.size(); i++) {
				in = new InsuranceHaiKouInjury();
				JSONObject fromObject2 = JSONObject.fromObject(fromObject.get(i));
				in.setCompany(fromObject2.getString("unitname"));
				in.setStatus(fromObject2.getString("insurance"));
				in.setDatea(fromObject2.getString("costperiod"));
				in.setBase(fromObject2.getString("paymentbase"));
				in.setCompanyPay(fromObject2.getString("unitacttransfersum"));
				in.setPersonalPay(fromObject2.getString("personalacttranssum"));
				in.setOtherPay(fromObject2.getString("otheracttransfersum"));
				in.setSum(fromObject2.getString("acttransfersum"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
//			System.out.println(list);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setList(list);
		}
		return webParam;
	}

	
	//失业
	public WebParam<InsuranceHaiKouUnemployment> crawlerUnemployment(
			InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance,int time) throws Exception {
		String url="http://202.100.251.116:8880/ehrss-si-person/api/rights/payment/4000006004928672?insurance=210&year="+getDateBefore("yyyy", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url);
		WebParam<InsuranceHaiKouUnemployment> webParam = new WebParam<InsuranceHaiKouUnemployment>();
		if(page.getWebResponse().getContentAsString().contains("unitname"))
		{
			List<InsuranceHaiKouUnemployment> list = new ArrayList<InsuranceHaiKouUnemployment>();
			InsuranceHaiKouUnemployment in =null;
			JSONArray fromObject = JSONArray.fromObject(page.getWebResponse().getContentAsString());
			for (int i = 0; i < fromObject.size(); i++) {
				in = new InsuranceHaiKouUnemployment();
				JSONObject fromObject2 = JSONObject.fromObject(fromObject.get(i));
				in.setCompany(fromObject2.getString("unitname"));
				in.setStatus(fromObject2.getString("insurance"));
				in.setDatea(fromObject2.getString("costperiod"));
				in.setBase(fromObject2.getString("paymentbase"));
				in.setCompanyPay(fromObject2.getString("unitacttransfersum"));
				in.setPersonalPay(fromObject2.getString("personalacttranssum"));
				in.setOtherPay(fromObject2.getString("otheracttransfersum"));
				in.setSum(fromObject2.getString("acttransfersum"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
//			System.out.println(list);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setList(list);
		}
		return webParam;
	}

	
	//生育
	public WebParam<InsuranceHaiKouMaternity> crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance,int time) throws Exception {
		String url="http://202.100.251.116:8880/ehrss-si-person/api/rights/payment/4000006004928672?insurance=510&year="+getDateBefore("yyyy", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url);
		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<InsuranceHaiKouMaternity> webParam = new WebParam<InsuranceHaiKouMaternity>();
		if(page.getWebResponse().getContentAsString().contains("unitname"))
		{
			List<InsuranceHaiKouMaternity> list = new ArrayList<InsuranceHaiKouMaternity>();
			InsuranceHaiKouMaternity in =null;
			JSONArray fromObject = JSONArray.fromObject(page.getWebResponse().getContentAsString());
			for (int i = 0; i < fromObject.size(); i++) {
				in = new InsuranceHaiKouMaternity();
				JSONObject fromObject2 = JSONObject.fromObject(fromObject.get(i));
				in.setCompany(fromObject2.getString("unitname"));
				in.setStatus(fromObject2.getString("insurance"));
				in.setDatea(fromObject2.getString("costperiod"));
				in.setBase(fromObject2.getString("paymentbase"));
				in.setCompanyPay(fromObject2.getString("unitacttransfersum"));
				in.setPersonalPay(fromObject2.getString("personalacttranssum"));
				in.setOtherPay(fromObject2.getString("otheracttransfersum"));
				in.setSum(fromObject2.getString("acttransfersum"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
//			System.out.println(list);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setList(list);
		}
		return webParam;
	}

}
