package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingEndowment;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingInjury;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingMaternity;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingMedical;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingUnemployment;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class InsuranceShaoXingParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://202.101.185.218/inner/bsdt/nologin.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='id_grzjhm']");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getUsername());
		
		HtmlTextInput id_account = (HtmlTextInput)page.getFirstByXPath("//*[@id='id_grxm']");
		id_account.reset();
		id_account.setText(insuranceRequestParameters.getName());
		
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='yzmsrc']");
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1004");
		
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='id_loginyzm']");
		h.setText(verifycode);
		
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='user_main_0']/div[6]/a/img");
		Page page2 = firstByXPath.click();
		Page page3 = webClient.getPage("http://202.101.185.218/inner/bsdt/bsdt_sbxxcx.html?1520321171798637556358");
		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		if(page3.getWebResponse().getContentAsString().contains("欢迎登入"))
		{
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			return webParam;
		}
		return null;
	}

	public WebParam<InsuranceShaoXingUserInfo> getUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://202.101.185.218/web/sxrs/JMJbxx.action?_pageLines=0&_currPage=0";
		          
		            	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
	    requestSettings.setCharset(Charset.forName("UTF-8"));		    
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN");
		requestSettings.setAdditionalHeader("Host", "202.101.185.218");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		requestSettings.setAdditionalHeader("Referer", "http://202.101.185.218/inner/bsdt/bsdt_jbxx.html");
		
		Page page = webClient.getPage(requestSettings);
//		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<InsuranceShaoXingUserInfo> webParam = new WebParam<InsuranceShaoXingUserInfo>();

		if(page.getWebResponse().getContentAsString().contains("rowList"))
		{
			JSONObject fromObject = JSONObject.fromObject(page.getWebResponse().getContentAsString().substring(1, page.getWebResponse().getContentAsString().length()-1));
			String string = fromObject.getString("s0011Resdatas");
//			System.out.println(string);
			JSONObject fromObject2 = JSONObject.fromObject(string);
			String string2 = fromObject2.getString("rowList");
//			System.out.println(string2);
			String substring = string2.substring(1, string2.length()-1);
//			System.out.println(substring);
			JSONObject fromObject3 = JSONObject.fromObject(substring);
			String csrq = fromObject3.getString("csrq");
			InsuranceShaoXingUserInfo i = new InsuranceShaoXingUserInfo();
			i.setBirth(csrq);
			i.setCompanyNum(fromObject3.getString("dwdm"));
			i.setCompany(fromObject3.getString("dwmc"));
			i.setName(fromObject3.getString("xm"));
			i.setSex(fromObject3.getString("xb"));
			i.setIDNum(fromObject3.getString("zjhm"));
			i.setStatus(fromObject3.getString("zjlx"));
			i.setGrbh(fromObject3.getString("grbh"));
			i.setTaskid(taskInsurance.getTaskid());
			System.out.println(i);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setInsuranceShaoXingUserInfo(i);
			return webParam;
		}
		return null;
	}

	
	//养老
	public WebParam<InsuranceShaoXingEndowment> getEndowment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		String url="http://202.101.185.218/web/sxrs/ZgJfxx.action?&xzdm=01";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
	    requestSettings.setCharset(Charset.forName("UTF-8"));		    
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN");
		requestSettings.setAdditionalHeader("Host", "202.101.185.218");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		requestSettings.setAdditionalHeader("Referer", "http://202.101.185.218/inner/bsdt/bsdt_jbxx.html");
		Page page = webClient.getPage(requestSettings);
//		System.out.println(page.getWebResponse().getContentAsString());
		String json = page.getWebResponse().getContentAsString();
		WebParam<InsuranceShaoXingEndowment> webParam = new WebParam<InsuranceShaoXingEndowment>();
		List<InsuranceShaoXingEndowment> list = new ArrayList<InsuranceShaoXingEndowment>();
		if(json.contains("rowList"))
		{
			JSONObject fromObject = JSONObject.fromObject(json.substring(1,json.length()-1));
			String string = fromObject.getString("s0012Resdatas");
//			System.out.println(string);
			JSONObject fromObject2 = JSONObject.fromObject(string);
			String string3 = fromObject2.getString("pageCount");
			int parseInt = Integer.parseInt(string3);
			for (int i = 1; i < parseInt+1; i++) {
				String url1="http://202.101.185.218/web/sxrs/ZgJfxx.action?_pageLines=10&_currPage="+i+"&xzdm=01";
				WebRequest  requestSettings1 = new WebRequest(new URL(url1), HttpMethod.POST);
			    requestSettings1.setCharset(Charset.forName("UTF-8"));		    
				requestSettings1.setAdditionalHeader("Accept", "*/*");
				requestSettings1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
				requestSettings1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				requestSettings1.setAdditionalHeader("Accept", "*/*");
				requestSettings1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				requestSettings1.setAdditionalHeader("Accept-Language", "zh-CN");
				requestSettings1.setAdditionalHeader("Host", "202.101.185.218");
				requestSettings1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
				requestSettings1.setAdditionalHeader("Referer", "http://202.101.185.218/inner/bsdt/bsdt_jbxx.html");
				Page page1 = webClient.getPage(requestSettings1);
				String json1 = page1.getWebResponse().getContentAsString();
				JSONObject fromObject1 = JSONObject.fromObject(json1.substring(1,json1.length()-1));
				String string11 = fromObject1.getString("s0012Resdatas");
//				System.out.println(string);
				JSONObject fromObject22 = JSONObject.fromObject(string11);
				String string22 = fromObject22.getString("rowList");
//				System.out.println(string2);
				JSONArray fromObject3 = JSONArray.fromObject(string22);
				
				InsuranceShaoXingEndowment in =null;
				for (int ii = 0; ii < fromObject3.size(); ii++) {
//					System.out.println(fromObject3.get(i));
					in = new InsuranceShaoXingEndowment();
					JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(ii));
					in.setStatus(fromObject4.getString("xzdm"));
					in.setBase(fromObject4.getString("jfdc"));
					in.setGetMoney(fromObject4.getString("dzqk"));
					in.setPayCompany(fromObject4.getString("dwyj"));
					in.setPayPersonal(fromObject4.getString("gryj"));
					in.setTime(fromObject4.getString("yjny"));
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				System.out.println(list);
				webParam.setHtml(json);
				webParam.setList(list);
				webParam.setUrl(url);
			}
			
			
			return webParam;
		}
		
		return null;
	}

	public WebParam<InsuranceShaoXingMedical> getMedical(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		String url="http://202.101.185.218/web/sxrs/ZgJfxx.action?xzdm=07";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
	    requestSettings.setCharset(Charset.forName("UTF-8"));		    
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN");
		requestSettings.setAdditionalHeader("Host", "202.101.185.218");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		requestSettings.setAdditionalHeader("Referer", "http://202.101.185.218/inner/bsdt/bsdt_jbxx.html");
		Page page = webClient.getPage(requestSettings);
//		System.out.println(page.getWebResponse().getContentAsString());
		String json = page.getWebResponse().getContentAsString();
		WebParam<InsuranceShaoXingMedical> webParam = new WebParam<InsuranceShaoXingMedical>();
		List<InsuranceShaoXingMedical> list = new ArrayList<InsuranceShaoXingMedical>();
		if(json.contains("rowList"))
		{
			JSONObject fromObject = JSONObject.fromObject(json.substring(1,json.length()-1));
			String string = fromObject.getString("s0012Resdatas");
//			System.out.println(string);
			JSONObject fromObject2 = JSONObject.fromObject(string);
			String string3 = fromObject2.getString("pageCount");
			int parseInt = Integer.parseInt(string3);
			for (int i = 1; i < parseInt+1; i++) {
				String url1="http://202.101.185.218/web/sxrs/ZgJfxx.action?_pageLines=10&_currPage="+i+"&xzdm=07";
				WebRequest  requestSettings1 = new WebRequest(new URL(url1), HttpMethod.POST);
			    requestSettings1.setCharset(Charset.forName("UTF-8"));		    
				requestSettings1.setAdditionalHeader("Accept", "*/*");
				requestSettings1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
				requestSettings1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				requestSettings1.setAdditionalHeader("Accept", "*/*");
				requestSettings1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				requestSettings1.setAdditionalHeader("Accept-Language", "zh-CN");
				requestSettings1.setAdditionalHeader("Host", "202.101.185.218");
				requestSettings1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
				requestSettings1.setAdditionalHeader("Referer", "http://202.101.185.218/inner/bsdt/bsdt_jbxx.html");
				Page page1 = webClient.getPage(requestSettings1);
				String json1 = page1.getWebResponse().getContentAsString();
				JSONObject fromObject1 = JSONObject.fromObject(json1.substring(1,json1.length()-1));
				String string11 = fromObject1.getString("s0012Resdatas");
//				System.out.println(string);
				JSONObject fromObject22 = JSONObject.fromObject(string11);
				String string22 = fromObject22.getString("rowList");
//				System.out.println(string2);
				JSONArray fromObject3 = JSONArray.fromObject(string22);
				
				InsuranceShaoXingMedical in =null;
				for (int ii = 0; ii < fromObject3.size(); ii++) {
//					System.out.println(fromObject3.get(i));
					in = new InsuranceShaoXingMedical();
					JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(ii));
					in.setStatus(fromObject4.getString("xzdm"));
					in.setBase(fromObject4.getString("jfdc"));
					in.setGetMoney(fromObject4.getString("dzqk"));
					in.setPayCompany(fromObject4.getString("dwyj"));
					in.setPayPersonal(fromObject4.getString("gryj"));
					in.setTime(fromObject4.getString("yjny"));
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				System.out.println(list);
				webParam.setHtml(json);
				webParam.setList(list);
				webParam.setUrl(url);
			}
			return webParam;
		}
		
		return null;
	}

	//失业
	public WebParam<InsuranceShaoXingUnemployment> getUnemployment(
			InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://202.101.185.218/web/sxrs/ZgJfxx.action?xzdm=06";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
	    requestSettings.setCharset(Charset.forName("UTF-8"));		    
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN");
		requestSettings.setAdditionalHeader("Host", "202.101.185.218");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		requestSettings.setAdditionalHeader("Referer", "http://202.101.185.218/inner/bsdt/bsdt_jbxx.html");
		Page page = webClient.getPage(requestSettings);
//		System.out.println(page.getWebResponse().getContentAsString());
		String json = page.getWebResponse().getContentAsString();
		WebParam<InsuranceShaoXingUnemployment> webParam = new WebParam<InsuranceShaoXingUnemployment>();
		List<InsuranceShaoXingUnemployment> list = new ArrayList<InsuranceShaoXingUnemployment>();
		if(json.contains("rowList"))
		{
			JSONObject fromObject = JSONObject.fromObject(json.substring(1,json.length()-1));
			String string = fromObject.getString("s0012Resdatas");
//			System.out.println(string);
			JSONObject fromObject2 = JSONObject.fromObject(string);
			String string3 = fromObject2.getString("pageCount");
			int parseInt = Integer.parseInt(string3);
			for (int i = 1; i < parseInt+1; i++) {
				String url1="http://202.101.185.218/web/sxrs/ZgJfxx.action?_pageLines=10&_currPage="+i+"&xzdm=06";
				WebRequest  requestSettings1 = new WebRequest(new URL(url1), HttpMethod.POST);
			    requestSettings1.setCharset(Charset.forName("UTF-8"));		    
				requestSettings1.setAdditionalHeader("Accept", "*/*");
				requestSettings1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
				requestSettings1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				requestSettings1.setAdditionalHeader("Accept", "*/*");
				requestSettings1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				requestSettings1.setAdditionalHeader("Accept-Language", "zh-CN");
				requestSettings1.setAdditionalHeader("Host", "202.101.185.218");
				requestSettings1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
				requestSettings1.setAdditionalHeader("Referer", "http://202.101.185.218/inner/bsdt/bsdt_jbxx.html");
				Page page1 = webClient.getPage(requestSettings1);
				String json1 = page1.getWebResponse().getContentAsString();
				JSONObject fromObject1 = JSONObject.fromObject(json1.substring(1,json1.length()-1));
				String string11 = fromObject1.getString("s0012Resdatas");
//				System.out.println(string);
				JSONObject fromObject22 = JSONObject.fromObject(string11);
				String string22 = fromObject22.getString("rowList");
//				System.out.println(string2);
				JSONArray fromObject3 = JSONArray.fromObject(string22);
				InsuranceShaoXingUnemployment in =null;
				for (int ii = 0; ii < fromObject3.size(); ii++) {
//					System.out.println(fromObject3.get(i));
					in = new InsuranceShaoXingUnemployment();
					JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(ii));
					in.setStatus(fromObject4.getString("xzdm"));
					in.setBase(fromObject4.getString("jfdc"));
					in.setGetMoney(fromObject4.getString("dzqk"));
					in.setPayCompany(fromObject4.getString("dwyj"));
					in.setPayPersonal(fromObject4.getString("gryj"));
					in.setTime(fromObject4.getString("yjny"));
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				System.out.println(list);
				webParam.setHtml(json);
				webParam.setList(list);
				webParam.setUrl(url);
		   }
			return webParam;
		}
		return null;
	}

	public WebParam<InsuranceShaoXingInjury> getInjury(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		String url="http://202.101.185.218/web/sxrs/ZgJfxx.action?xzdm=04";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
	    requestSettings.setCharset(Charset.forName("UTF-8"));		    
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN");
		requestSettings.setAdditionalHeader("Host", "202.101.185.218");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		requestSettings.setAdditionalHeader("Referer", "http://202.101.185.218/inner/bsdt/bsdt_jbxx.html");
		Page page = webClient.getPage(requestSettings);
//		System.out.println(page.getWebResponse().getContentAsString());
		String json = page.getWebResponse().getContentAsString();
		WebParam<InsuranceShaoXingInjury> webParam = new WebParam<InsuranceShaoXingInjury>();
		List<InsuranceShaoXingInjury> list = new ArrayList<InsuranceShaoXingInjury>();
		if(json.contains("rowList"))
		{
			JSONObject fromObject = JSONObject.fromObject(json.substring(1,json.length()-1));
			String string = fromObject.getString("s0012Resdatas");
//			System.out.println(string);
			JSONObject fromObject2 = JSONObject.fromObject(string);
			String string3 = fromObject2.getString("pageCount");
			int parseInt = Integer.parseInt(string3);
			for (int i = 1; i < parseInt+1; i++) {
				String url1="http://202.101.185.218/web/sxrs/ZgJfxx.action?_pageLines=10&_currPage="+i+"&xzdm=04";
				WebRequest  requestSettings1 = new WebRequest(new URL(url1), HttpMethod.POST);
			    requestSettings1.setCharset(Charset.forName("UTF-8"));		    
				requestSettings1.setAdditionalHeader("Accept", "*/*");
				requestSettings1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
				requestSettings1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				requestSettings1.setAdditionalHeader("Accept", "*/*");
				requestSettings1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				requestSettings1.setAdditionalHeader("Accept-Language", "zh-CN");
				requestSettings1.setAdditionalHeader("Host", "202.101.185.218");
				requestSettings1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
				requestSettings1.setAdditionalHeader("Referer", "http://202.101.185.218/inner/bsdt/bsdt_jbxx.html");
				Page page1 = webClient.getPage(requestSettings1);
				String json1 = page1.getWebResponse().getContentAsString();
				JSONObject fromObject1 = JSONObject.fromObject(json1.substring(1,json1.length()-1));
				String string11 = fromObject1.getString("s0012Resdatas");
//				System.out.println(string);
				JSONObject fromObject22 = JSONObject.fromObject(string11);
				String string22 = fromObject22.getString("rowList");
//				System.out.println(string2);
				JSONArray fromObject3 = JSONArray.fromObject(string22);
				InsuranceShaoXingInjury in =null;
				for (int ii = 0; ii < fromObject3.size(); ii++) {
//					System.out.println(fromObject3.get(i));
					in = new InsuranceShaoXingInjury();
					JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(ii));
					in.setStatus(fromObject4.getString("xzdm"));
					in.setBase(fromObject4.getString("jfdc"));
					in.setGetMoney(fromObject4.getString("dzqk"));
					in.setPayCompany(fromObject4.getString("dwyj"));
					in.setPayPersonal(fromObject4.getString("gryj"));
					in.setTime(fromObject4.getString("yjny"));
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				System.out.println(list);
				webParam.setHtml(json);
				webParam.setList(list);
				webParam.setUrl(url);
			}
			return webParam;
		}		
		return null;
	}

	//生育
	public WebParam<InsuranceShaoXingMaternity> getMaternity(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		String url="http://202.101.185.218/web/sxrs/ZgJfxx.action?xzdm=05";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
	    requestSettings.setCharset(Charset.forName("UTF-8"));		    
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Accept", "*/*");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN");
		requestSettings.setAdditionalHeader("Host", "202.101.185.218");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		requestSettings.setAdditionalHeader("Referer", "http://202.101.185.218/inner/bsdt/bsdt_jbxx.html");
		Page page = webClient.getPage(requestSettings);
//		System.out.println(page.getWebResponse().getContentAsString());
		String json = page.getWebResponse().getContentAsString();
		WebParam<InsuranceShaoXingMaternity> webParam = new WebParam<InsuranceShaoXingMaternity>();
		List<InsuranceShaoXingMaternity> list = new ArrayList<InsuranceShaoXingMaternity>();
		if(json.contains("rowList"))
		{
			JSONObject fromObject = JSONObject.fromObject(json.substring(1,json.length()-1));
			String string = fromObject.getString("s0012Resdatas");
//			System.out.println(string);
			JSONObject fromObject2 = JSONObject.fromObject(string);
			String string3 = fromObject2.getString("pageCount");
			int parseInt = Integer.parseInt(string3);
			for (int i = 1; i < parseInt+1; i++) {
				String url2="http://202.101.185.218/web/sxrs/ZgJfxx.action?_pageLines=10&_currPage="+i+"&xzdm=05";
				WebRequest  requestSettings1 = new WebRequest(new URL(url2), HttpMethod.POST);
			    requestSettings1.setCharset(Charset.forName("UTF-8"));		    
				requestSettings1.setAdditionalHeader("Accept", "*/*");
				requestSettings1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
				requestSettings1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				requestSettings1.setAdditionalHeader("Accept", "*/*");
				requestSettings1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				requestSettings1.setAdditionalHeader("Accept-Language", "zh-CN");
				requestSettings1.setAdditionalHeader("Host", "202.101.185.218");
				requestSettings1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
				requestSettings1.setAdditionalHeader("Referer", "http://202.101.185.218/inner/bsdt/bsdt_jbxx.html");
				Page page1 = webClient.getPage(requestSettings1);
				String json1 = page1.getWebResponse().getContentAsString();
				JSONObject fromObject1 = JSONObject.fromObject(json1.substring(1,json1.length()-1));
				String string11 = fromObject1.getString("s0012Resdatas");
//				System.out.println(string);
				JSONObject fromObject22 = JSONObject.fromObject(string11);
				String string22 = fromObject22.getString("rowList");
				JSONArray fromObject3 = JSONArray.fromObject(string22);
				InsuranceShaoXingMaternity in =null;
				for (int ii = 0; ii < fromObject3.size(); ii++) {
//					System.out.println(fromObject3.get(i));
					in = new InsuranceShaoXingMaternity();
					JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(ii));
					in.setStatus(fromObject4.getString("xzdm"));
					in.setBase(fromObject4.getString("jfdc"));
					in.setGetMoney(fromObject4.getString("dzqk"));
					in.setPayCompany(fromObject4.getString("dwyj"));
					in.setPayPersonal(fromObject4.getString("gryj"));
					in.setTime(fromObject4.getString("yjny"));
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				System.out.println(list);
			}
			webParam.setHtml(json);
			webParam.setList(list);
			webParam.setUrl(url);
			return webParam;
		}
		return null;
	}
}
