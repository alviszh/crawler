package app.parser;

import java.net.URL;
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
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingEndowment;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingInjury;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingMaternity;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingMedical;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingUnemployment;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class InsuranceMaoMingParser {

	
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://rsj.maoming.gov.cn:8999/PersonFlat/pages/login.jsp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='UserName']");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getUsername());
		
		HtmlTextInput pwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='LoginUserName']");
		pwd.reset();
		pwd.setText(insuranceRequestParameters.getName());
		
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='UserPassword']");
		id_account.reset();
		id_account.setText(insuranceRequestParameters.getPassword());
		
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='btnLogin']");
		Page page2 = firstByXPath.click();
		WebParam webParam = new WebParam();
		if(page2.getWebResponse().getContentAsString().contains("欢迎页"))
		{
//			System.out.println("-----------");
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			return webParam;
		}		
		return null;
	}

	//个人信息
	public WebParam<InsuranceMaoMingUserInfo> crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url3="http://rsj.maoming.gov.cn:8999/PersonFlat/person/cardManage!cardInfo.action";
		WebRequest webRequest = new WebRequest(new URL(url3), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest);
//		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam<InsuranceMaoMingUserInfo> webParam = new WebParam<InsuranceMaoMingUserInfo>();
		if(page3.getWebResponse().getContentAsString().contains("姓名"))
		{
			Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
			Elements elementById = doc.getElementById("form1").getElementsByTag("tbody");
			Element elementsByTag = elementById.get(0).getElementsByTag("tr").get(0);
//			System.out.println(elementsByTag);
			InsuranceMaoMingUserInfo i = new InsuranceMaoMingUserInfo();
			i.setCardNum(elementById.get(0).getElementsByTag("tr").get(0).getElementsByTag("input").get(0).val());
			i.setName(elementById.get(0).getElementsByTag("tr").get(0).getElementsByTag("input").get(1).val());
			i.setIDNum(elementById.get(0).getElementsByTag("tr").get(1).getElementsByTag("input").get(0).val());
			i.setBirth(elementById.get(0).getElementsByTag("tr").get(1).getElementsByTag("input").get(1).val());
			i.setSex(elementById.get(0).getElementsByTag("tr").get(2).getElementsByTag("input").get(0).val());
			i.setNational(elementById.get(0).getElementsByTag("tr").get(2).getElementsByTag("input").get(1).val());
			i.setStatus(elementById.get(0).getElementsByTag("tr").get(3).getElementsByTag("input").get(0).val());
			i.setPersonalNum(elementById.get(0).getElementsByTag("tr").get(3).getElementsByTag("input").get(1).val());
			i.setCompanyNum(elementById.get(0).getElementsByTag("tr").get(4).getElementsByTag("input").get(0).val());
			i.setCompany(elementById.get(0).getElementsByTag("tr").get(4).getElementsByTag("input").get(1).val());
			i.setPhone(elementById.get(0).getElementsByTag("tr").get(5).getElementsByTag("input").get(0).val());
			i.setNum(elementById.get(0).getElementsByTag("tr").get(5).getElementsByTag("input").get(1).val());
			i.setHomeAddr(elementById.get(0).getElementsByTag("tr").get(6).getElementsByTag("input").get(0).val());
			i.setAddr(elementById.get(0).getElementsByTag("tr").get(7).getElementsByTag("input").get(0).val());
			i.setTaskid(taskInsurance.getTaskid());
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(url3);
			webParam.setInsuranceMaoMingUserInfo(i);
			return webParam;
		}
		return null;
	}

	
	//养老
	public WebParam<InsuranceMaoMingEndowment> crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
        String url4="http://rsj.maoming.gov.cn:8999/PersonFlat/person/personPaymentInfo!paymentInfo.action";
		String a ="aae140=110&aae041="+getDateBefore("yyyy", 3)+"%E5%B9%B401%E6%9C%88&aae042="+getDateBefore1("yyyy")+"%E5%B9%B401%E6%9C%88&page=1&pagesize=200&codetable=%5B%7B%22type%22%3A%22aae140%22%2C%22data%22%3A%22aae140%22%2C%22display%22%3A%22aae140%22%7D%2C%7B%22type%22%3A%22aae078%22%2C%22data%22%3A%22aae078%22%2C%22display%22%3A%22aae078%22%7D%5D";
		WebRequest requestSettings = new WebRequest(new URL(url4), HttpMethod.POST);
		requestSettings.setRequestBody(a);
		requestSettings.setCharset(Charset.forName("UTF-8"));
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Host", "ggfw.mmrs.gov.cn:20001");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Origin", "http://ggfw.mmrs.gov.cn:20001");
		requestSettings.setAdditionalHeader("Referer", "http://ggfw.mmrs.gov.cn:20001/PersonFlat/person/personPaymentInfo!paymentInfo.action");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		
		Page page4 = webClient.getPage(requestSettings);
//		System.out.println(page4.getWebResponse().getContentAsString());
		InsuranceMaoMingEndowment in = null;
		WebParam<InsuranceMaoMingEndowment> webParam = new WebParam<InsuranceMaoMingEndowment>();
		if(page4.getWebResponse().getContentAsString().contains("rows"))
		{
			JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
			String string = fromObject.getString("rows");
//			System.out.println(string);
			JSONArray fromObject2 = JSONArray.fromObject(string);
			
			List<InsuranceMaoMingEndowment> list = new ArrayList<InsuranceMaoMingEndowment>();
			for (int i = 0; i < fromObject2.size(); i++) {
				in = new InsuranceMaoMingEndowment();
//				System.out.println(fromObject2.get(i));
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
				in.setCompany(fromObject3.getString("aab069"));
				in.setStatus(fromObject3.getString("aae140"));
				in.setDatea(fromObject3.getString("aae079"));
				in.setBase(fromObject3.getString("aae180"));
				in.setPersonalpPay(fromObject3.getString("aae022"));
				in.setCompanyPay(fromObject3.getString("aae020"));
				in.setCompanyMoney(fromObject3.getString("aae056"));
				in.setSum(fromObject3.getString("aae058"));
				in.setFlag(fromObject3.getString("aae078"));
				in.setGetDate(fromObject3.getString("aae002"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			System.out.println(list);
			webParam.setUrl(url4);
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setList(list);
			return webParam;
		}
		
		return null;
	}

	
	//医疗
	public WebParam<InsuranceMaoMingMedical> crawlerMedical(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
        String url4="http://rsj.maoming.gov.cn:8999/PersonFlat/person/personPaymentInfo!paymentInfo.action";
		String a ="aae140=310&aae041="+getDateBefore("yyyy", 3)+"%E5%B9%B401%E6%9C%88&aae042="+getDateBefore1("yyyy")+"%E5%B9%B401%E6%9C%88&page=1&pagesize=200&codetable=%5B%7B%22type%22%3A%22aae140%22%2C%22data%22%3A%22aae140%22%2C%22display%22%3A%22aae140%22%7D%2C%7B%22type%22%3A%22aae078%22%2C%22data%22%3A%22aae078%22%2C%22display%22%3A%22aae078%22%7D%5D";
		WebRequest requestSettings = new WebRequest(new URL(url4), HttpMethod.POST);
		requestSettings.setRequestBody(a);
		requestSettings.setCharset(Charset.forName("UTF-8"));
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Host", "ggfw.mmrs.gov.cn:20001");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Origin", "http://ggfw.mmrs.gov.cn:20001");
		requestSettings.setAdditionalHeader("Referer", "http://ggfw.mmrs.gov.cn:20001/PersonFlat/person/personPaymentInfo!paymentInfo.action");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		
		Page page4 = webClient.getPage(requestSettings);
//		System.out.println(page4.getWebResponse().getContentAsString());
		InsuranceMaoMingMedical in = null;
		WebParam<InsuranceMaoMingMedical> webParam = new WebParam<InsuranceMaoMingMedical>();
		if(page4.getWebResponse().getContentAsString().contains("rows"))
		{
			JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
			String string = fromObject.getString("rows");
//			System.out.println(string);
			JSONArray fromObject2 = JSONArray.fromObject(string);
			
			List<InsuranceMaoMingMedical> list = new ArrayList<InsuranceMaoMingMedical>();
			for (int i = 0; i < fromObject2.size(); i++) {
				in = new InsuranceMaoMingMedical();
//				System.out.println(fromObject2.get(i));
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
				in.setCompany(fromObject3.getString("aab069"));
				in.setStatus(fromObject3.getString("aae140"));
				in.setDatea(fromObject3.getString("aae079"));
				in.setBase(fromObject3.getString("aae180"));
				in.setPersonalpPay(fromObject3.getString("aae022"));
				in.setCompanyPay(fromObject3.getString("aae020"));
				in.setCompanyMoney(fromObject3.getString("aae056"));
				in.setSum(fromObject3.getString("aae058"));
				in.setFlag(fromObject3.getString("aae078"));
				in.setGetDate(fromObject3.getString("aae002"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			System.out.println(list);
			webParam.setUrl(url4);
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setList(list);
			return webParam;
		}
		
		return null;
	}

	
	//失业
	public WebParam<InsuranceMaoMingUnemployment> crawlerUnemployment(
			InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
        String url4="http://rsj.maoming.gov.cn:8999/PersonFlat/person/personPaymentInfo!paymentInfo.action";
		String a ="aae140=210&aae041="+getDateBefore("yyyy", 3)+"%E5%B9%B401%E6%9C%88&aae042="+getDateBefore1("yyyy")+"%E5%B9%B401%E6%9C%88&page=1&pagesize=200&codetable=%5B%7B%22type%22%3A%22aae140%22%2C%22data%22%3A%22aae140%22%2C%22display%22%3A%22aae140%22%7D%2C%7B%22type%22%3A%22aae078%22%2C%22data%22%3A%22aae078%22%2C%22display%22%3A%22aae078%22%7D%5D";
		WebRequest requestSettings = new WebRequest(new URL(url4), HttpMethod.POST);
		requestSettings.setRequestBody(a);
		requestSettings.setCharset(Charset.forName("UTF-8"));
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Host", "ggfw.mmrs.gov.cn:20001");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Origin", "http://ggfw.mmrs.gov.cn:20001");
		requestSettings.setAdditionalHeader("Referer", "http://ggfw.mmrs.gov.cn:20001/PersonFlat/person/personPaymentInfo!paymentInfo.action");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		
		Page page4 = webClient.getPage(requestSettings);
//		System.out.println(page4.getWebResponse().getContentAsString());
		InsuranceMaoMingUnemployment in = null;
		WebParam<InsuranceMaoMingUnemployment> webParam = new WebParam<InsuranceMaoMingUnemployment>();
		if(page4.getWebResponse().getContentAsString().contains("rows"))
		{
			JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
			String string = fromObject.getString("rows");
//			System.out.println(string);
			JSONArray fromObject2 = JSONArray.fromObject(string);
			
			List<InsuranceMaoMingUnemployment> list = new ArrayList<InsuranceMaoMingUnemployment>();
			for (int i = 0; i < fromObject2.size(); i++) {
				in = new InsuranceMaoMingUnemployment();
//				System.out.println(fromObject2.get(i));
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
				in.setCompany(fromObject3.getString("aab069"));
				in.setStatus(fromObject3.getString("aae140"));
				in.setDatea(fromObject3.getString("aae079"));
				in.setBase(fromObject3.getString("aae180"));
				in.setPersonalpPay(fromObject3.getString("aae022"));
				in.setCompanyPay(fromObject3.getString("aae020"));
				in.setCompanyMoney(fromObject3.getString("aae056"));
				in.setSum(fromObject3.getString("aae058"));
				in.setFlag(fromObject3.getString("aae078"));
				in.setGetDate(fromObject3.getString("aae002"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			System.out.println(list);
			webParam.setUrl(url4);
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setList(list);
			return webParam;
		}
		
		return null;
	}

	//工伤
	public WebParam<InsuranceMaoMingInjury> crawlerInjury(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
        String url4="http://rsj.maoming.gov.cn:8999/PersonFlat/person/personPaymentInfo!paymentInfo.action";
		String a ="aae140=410&aae041="+getDateBefore("yyyy", 3)+"%E5%B9%B401%E6%9C%88&aae042="+getDateBefore1("yyyy")+"%E5%B9%B401%E6%9C%88&page=1&pagesize=200&codetable=%5B%7B%22type%22%3A%22aae140%22%2C%22data%22%3A%22aae140%22%2C%22display%22%3A%22aae140%22%7D%2C%7B%22type%22%3A%22aae078%22%2C%22data%22%3A%22aae078%22%2C%22display%22%3A%22aae078%22%7D%5D";
		WebRequest requestSettings = new WebRequest(new URL(url4), HttpMethod.POST);
		requestSettings.setRequestBody(a);
		requestSettings.setCharset(Charset.forName("UTF-8"));
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Host", "ggfw.mmrs.gov.cn:20001");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Origin", "http://ggfw.mmrs.gov.cn:20001");
		requestSettings.setAdditionalHeader("Referer", "http://ggfw.mmrs.gov.cn:20001/PersonFlat/person/personPaymentInfo!paymentInfo.action");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		
		Page page4 = webClient.getPage(requestSettings);
//		System.out.println(page4.getWebResponse().getContentAsString());
		InsuranceMaoMingInjury in = null;
		WebParam<InsuranceMaoMingInjury> webParam = new WebParam<InsuranceMaoMingInjury>();
		if(page4.getWebResponse().getContentAsString().contains("rows"))
		{
			JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
			String string = fromObject.getString("rows");
//			System.out.println(string);
			JSONArray fromObject2 = JSONArray.fromObject(string);
			
			List<InsuranceMaoMingInjury> list = new ArrayList<InsuranceMaoMingInjury>();
			for (int i = 0; i < fromObject2.size(); i++) {
				in = new InsuranceMaoMingInjury();
//				System.out.println(fromObject2.get(i));
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
				in.setCompany(fromObject3.getString("aab069"));
				in.setStatus(fromObject3.getString("aae140"));
				in.setDatea(fromObject3.getString("aae079"));
				in.setBase(fromObject3.getString("aae180"));
				in.setPersonalpPay(fromObject3.getString("aae022"));
				in.setCompanyPay(fromObject3.getString("aae020"));
				in.setCompanyMoney(fromObject3.getString("aae056"));
				in.setSum(fromObject3.getString("aae058"));
				in.setFlag(fromObject3.getString("aae078"));
				in.setGetDate(fromObject3.getString("aae002"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			System.out.println(list);
			webParam.setUrl(url4);
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setList(list);
			return webParam;
		}
		
		return null;
	}

	//生育
	public WebParam<InsuranceMaoMingMaternity> crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
        String url4="http://rsj.maoming.gov.cn:8999/PersonFlat/person/personPaymentInfo!paymentInfo.action";
		String a ="aae140=510&aae041="+getDateBefore("yyyy", 3)+"%E5%B9%B401%E6%9C%88&aae042="+getDateBefore1("yyyy")+"%E5%B9%B401%E6%9C%88&page=1&pagesize=200&codetable=%5B%7B%22type%22%3A%22aae140%22%2C%22data%22%3A%22aae140%22%2C%22display%22%3A%22aae140%22%7D%2C%7B%22type%22%3A%22aae078%22%2C%22data%22%3A%22aae078%22%2C%22display%22%3A%22aae078%22%7D%5D";
		WebRequest requestSettings = new WebRequest(new URL(url4), HttpMethod.POST);
		requestSettings.setRequestBody(a);
		requestSettings.setCharset(Charset.forName("UTF-8"));
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Host", "ggfw.mmrs.gov.cn:20001");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Origin", "http://ggfw.mmrs.gov.cn:20001");
		requestSettings.setAdditionalHeader("Referer", "http://ggfw.mmrs.gov.cn:20001/PersonFlat/person/personPaymentInfo!paymentInfo.action");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		
		Page page4 = webClient.getPage(requestSettings);
//		System.out.println(page4.getWebResponse().getContentAsString());
		InsuranceMaoMingMaternity in = null;
		WebParam<InsuranceMaoMingMaternity> webParam = new WebParam<InsuranceMaoMingMaternity>();
		if(page4.getWebResponse().getContentAsString().contains("rows"))
		{
			JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
			String string = fromObject.getString("rows");
//			System.out.println(string);
			JSONArray fromObject2 = JSONArray.fromObject(string);
			
			List<InsuranceMaoMingMaternity> list = new ArrayList<InsuranceMaoMingMaternity>();
			for (int i = 0; i < fromObject2.size(); i++) {
				in = new InsuranceMaoMingMaternity();
//				System.out.println(fromObject2.get(i));
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
				in.setCompany(fromObject3.getString("aab069"));
				in.setStatus(fromObject3.getString("aae140"));
				in.setDatea(fromObject3.getString("aae079"));
				in.setBase(fromObject3.getString("aae180"));
				in.setPersonalpPay(fromObject3.getString("aae022"));
				in.setCompanyPay(fromObject3.getString("aae020"));
				in.setCompanyMoney(fromObject3.getString("aae056"));
				in.setSum(fromObject3.getString("aae058"));
				in.setFlag(fromObject3.getString("aae078"));
				in.setGetDate(fromObject3.getString("aae002"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			System.out.println(list);
			webParam.setUrl(url4);
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setList(list);
			return webParam;
		}
		
		return null;
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
	
	//当前时间
	public String getDateBefore1(String fmt) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
}
