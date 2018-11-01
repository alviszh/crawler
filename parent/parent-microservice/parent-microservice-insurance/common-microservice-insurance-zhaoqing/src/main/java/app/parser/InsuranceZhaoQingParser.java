package app.parser;

import java.net.URL;
import java.net.URLEncoder;
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
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingEndowment;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingInjury;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingMaternity;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingMedical;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingUnemployment;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
@Component
public class InsuranceZhaoQingParser {

	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://wsbs.zhaoqing.gov.cn/portal/jcfw2/insurance/insurance.action?areacode=441200&zhencode=&cuncode=";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='sbusername']");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getName());

		
		HtmlTextInput id_account = (HtmlTextInput)page.getFirstByXPath("//*[@id='sbidcard']");
		id_account.reset();
		id_account.setText(insuranceRequestParameters.getUsername());
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='mainForm']/div/div/div[2]/div/div/div[3]/div/input");
		Page page2 = firstByXPath.click();
//		System.out.println(page2.getWebResponse().getContentAsString());
		String json = page2.getWebResponse().getContentAsString();
		WebParam webParam = new WebParam();
		if(page2.getWebResponse().getContentAsString().contains("个人信息"))
		{
			int indexOf = json.indexOf("userid");
			int indexOf2 = json.indexOf("sbusername");
			String substring = json.substring(indexOf, indexOf2);
			int indexOf3 = substring.indexOf("value");
			String substring2 = substring.substring(indexOf3);
			String substring3 = substring2.substring(7, 15);
//			System.out.println(substring2.substring(7, 15));
			
			int indexOf4 = json.indexOf("基层公共服务平台");
			int indexOf5 = json.indexOf("切换");
			String substring4 = json.substring(indexOf4,indexOf5);
//			System.out.println(substring);
			int indexOf6 = substring4.indexOf("showarea");
			int indexOf7 = substring4.indexOf("style");
			String substring5 = substring4.substring(indexOf6,indexOf7);
			String substring6 = substring5.substring(10, 16);
//			System.out.println(substring5.substring(10, 16));
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			webParam.setWebClient(webClient);
			webParam.setWebHandle(substring3);
			webParam.setUrl(substring6);
			return webParam;
		}		
		return null;
	}

	//个人信息
	public WebParam<InsuranceZhaoQingUserInfo> crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance)throws Exception {
				String testhtml = taskInsurance.getTesthtml();
				WebParam<InsuranceZhaoQingUserInfo> webParam = new WebParam<InsuranceZhaoQingUserInfo>();
				if(testhtml.contains("个人信息"))
				{
					Document doc = Jsoup.parse(testhtml);
					Elements elementsByClass = doc.getElementsByClass("zmhdCon");
//					System.out.println(elementsByClass);
					InsuranceZhaoQingUserInfo i = new InsuranceZhaoQingUserInfo();
					String nextLabelByKeywordTwo = getNextLabelByKeywordTwo(elementsByClass, "姓名", "td");
//					System.out.println(nextLabelByKeywordTwo);
					i.setName(getNextLabelByKeywordTwo(elementsByClass, "姓名", "td"));
					i.setSex(getNextLabelByKeywordTwo(elementsByClass, "性别", "td"));
					i.setNational(getNextLabelByKeywordTwo(elementsByClass, "民族", "td"));
					i.setBirth(getNextLabelByKeywordTwo(elementsByClass, "出生日期", "td"));
					i.setCompany(getNextLabelByKeywordTwo(elementsByClass, "单位名称", "td"));
					i.setIDNum(getNextLabelByKeywordTwo(elementsByClass, "身份证号码", "td"));
					i.setTaskid(taskInsurance.getTaskid());
//					System.out.println(i);
					webParam.setHtml(testhtml);
					webParam.setInsuranceZhaoQingUserInfo(i);
					return webParam;
				}
				
			return null;
	}
	
	
	//养老
	public WebParam<InsuranceZhaoQingEndowment> crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance,int time) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url2="http://wsbs.zhaoqing.gov.cn/portal/jcfw2/insurance/insurance!findInsurance.action";
		WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.POST);
		String encode = URLEncoder.encode(insuranceRequestParameters.getName(), "UTF-8");
		webRequest.setRequestBody("type=4&userid="+taskInsurance.getWebdriverHandle()+"&sbusername="+encode+"&sbidcard="+insuranceRequestParameters.getUsername()+"&years="+getDateBefore("yyyy", time)+"&areacode="+taskInsurance.getCrawlerHost()+"&zhencode=&cuncode=");
		Page page3 = webClient.getPage(webRequest);
//		System.out.println(page3.getWebResponse().getContentAsString());	
		if(page3.getWebResponse().getContentAsString().contains("养老保险缴费明细"))
		{
			Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
			if(page3.getWebResponse().getContentAsString().contains("没有查到相关数据哦"))
			{
				return null;
			}
			else if(page3.getWebResponse().getContentAsString().contains("查询异常"))
			{
				return null;
			}
			else if(page3.getWebResponse().getContentAsString().contains("hccx_tableCon"))
			{
				Elements elementsByClass = doc.getElementsByClass("hccx_tableCon").get(0).getElementsByTag("tr");
				InsuranceZhaoQingEndowment in = null;
				List<InsuranceZhaoQingEndowment> list = new ArrayList<InsuranceZhaoQingEndowment>();
				WebParam<InsuranceZhaoQingEndowment> webParam = new WebParam<InsuranceZhaoQingEndowment>();
				for (int i = 0; i < elementsByClass.size(); i++) {
					in = new InsuranceZhaoQingEndowment();
//					System.out.println(elementsByClass.get(i));
					in.setDatea(elementsByClass.get(i).getElementsByTag("td").get(0).text());
					in.setCompany(elementsByClass.get(i).getElementsByTag("td").get(1).text());
					in.setBase(elementsByClass.get(i).getElementsByTag("td").get(2).text());
					in.setCompanyPay(elementsByClass.get(i).getElementsByTag("td").get(3).text());
					in.setPersonlPay(elementsByClass.get(i).getElementsByTag("td").get(4).text());
					in.setSum(elementsByClass.get(i).getElementsByTag("td").get(5).text());
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
//				System.out.println(list);
				webParam.setList(list);
				webParam.setUrl(url2);
				webParam.setHtml(page3.getWebResponse().getContentAsString());
				return webParam;
			}
		}
		return null;
	}
	
	public String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
		Elements es = element.select(tag + ":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element1 = es.first();
			Element nextElement = element1.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

	//医疗
	public WebParam<InsuranceZhaoQingMedical> crawlerMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance,int time) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url2="http://wsbs.zhaoqing.gov.cn/portal/jcfw2/insurance/insurance!findInsurance.action";
		WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.POST);
		String encode = URLEncoder.encode(insuranceRequestParameters.getName(), "UTF-8");
		webRequest.setRequestBody("type=5&userid="+taskInsurance.getWebdriverHandle()+"&sbusername="+encode+"&sbidcard="+insuranceRequestParameters.getUsername()+"&years="+getDateBefore("yyyy", time)+"&areacode="+taskInsurance.getCrawlerHost()+"&zhencode=&cuncode=");
		Page page3 = webClient.getPage(webRequest);
//		System.out.println(page3.getWebResponse().getContentAsString());	
		if(page3.getWebResponse().getContentAsString().contains("医疗保险缴费查询"))
		{
			Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
			if(page3.getWebResponse().getContentAsString().contains("没有查到相关数据哦"))
			{
				return null;
			}
			else if(page3.getWebResponse().getContentAsString().contains("查询异常"))
			{
				return null;
			}
			else if(page3.getWebResponse().getContentAsString().contains("hccx_tableCon"))
			{
				Elements elementsByClass = doc.getElementsByClass("hccx_tableCon").get(0).getElementsByTag("tr");
				InsuranceZhaoQingMedical in = null;
				List<InsuranceZhaoQingMedical> list = new ArrayList<InsuranceZhaoQingMedical>();
				WebParam<InsuranceZhaoQingMedical> webParam = new WebParam<InsuranceZhaoQingMedical>();
				for (int i = 0; i < elementsByClass.size(); i++) {
					in = new InsuranceZhaoQingMedical();
//					System.out.println(elementsByClass.get(i));
					in.setDatea(elementsByClass.get(i).getElementsByTag("td").get(0).text());
					in.setCompany(elementsByClass.get(i).getElementsByTag("td").get(1).text());
					in.setCompanyPay(elementsByClass.get(i).getElementsByTag("td").get(2).text());
					in.setPersonlPay(elementsByClass.get(i).getElementsByTag("td").get(3).text());
					in.setBase(elementsByClass.get(i).getElementsByTag("td").get(4).text());
					in.setCompanyMoney(elementsByClass.get(i).getElementsByTag("td").get(5).text());
					in.setJiGou(elementsByClass.get(i).getElementsByTag("td").get(6).text());
					in.setSum(elementsByClass.get(i).getElementsByTag("td").get(7).text());
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
//				System.out.println(list);
				webParam.setList(list);
				webParam.setUrl(url2);
				webParam.setHtml(page3.getWebResponse().getContentAsString());
				return webParam;
			}
		}
		return null;
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

	//生育
	public WebParam<InsuranceZhaoQingMaternity> crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance, int j) throws  Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url2="http://wsbs.zhaoqing.gov.cn/portal/jcfw2/insurance/insurance!findInsurance.action";
		WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.POST);
		String encode = URLEncoder.encode(insuranceRequestParameters.getName(), "UTF-8");
		webRequest.setRequestBody("type=8&userid="+taskInsurance.getWebdriverHandle()+"&sbusername="+encode+"&sbidcard="+insuranceRequestParameters.getUsername()+"&years="+getDateBefore("yyyy",j )+"&areacode="+taskInsurance.getCrawlerHost()+"&zhencode=&cuncode=");
		Page page3 = webClient.getPage(webRequest);
		System.out.println(page3.getWebResponse().getContentAsString());	
		if(page3.getWebResponse().getContentAsString().contains("生育保险缴费明细"))
		{
			InsuranceZhaoQingMaternity in=null;
			List<InsuranceZhaoQingMaternity> list = new ArrayList<InsuranceZhaoQingMaternity>();
			WebParam<InsuranceZhaoQingMaternity> webParam = new WebParam<InsuranceZhaoQingMaternity>();
			Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
			if(page3.getWebResponse().getContentAsString().contains("没有查到相关数据哦"))
			{
				return null;
			}
			else if(page3.getWebResponse().getContentAsString().contains("查询异常"))
			{
				return null;
			}
			else if(page3.getWebResponse().getContentAsString().contains("hccx_tableCon"))
			{
				Elements elementsByClass = doc.getElementsByClass("hccx_tableCon").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
				for (int i = 0; i < elementsByClass.size(); i++) {
					
//					System.out.println(elementsByClass.get(i).getElementsByTag("td").get(1));
					in = new InsuranceZhaoQingMaternity();
					in.setDatea(elementsByClass.get(i).getElementsByTag("td").get(0).text());
					in.setCompany(elementsByClass.get(i).getElementsByTag("td").get(1).text());
					in.setBase(elementsByClass.get(i).getElementsByTag("td").get(2).text());
					in.setCompanyPay(elementsByClass.get(i).getElementsByTag("td").get(3).text());
					in.setPersonalPay(elementsByClass.get(i).getElementsByTag("td").get(4).text());
					in.setSum(elementsByClass.get(i).getElementsByTag("td").get(5).text());
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				webParam.setHtml(page3.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(url2);
				return webParam;
			}
			
		}
		return null;
	}

	//工伤
	public WebParam<InsuranceZhaoQingInjury> crawlerInjury(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance, int j) throws  Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url2="http://wsbs.zhaoqing.gov.cn/portal/jcfw2/insurance/insurance!findInsurance.action";
		WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.POST);
		String encode = URLEncoder.encode(insuranceRequestParameters.getName(), "UTF-8");
		webRequest.setRequestBody("type=7&userid="+taskInsurance.getWebdriverHandle()+"&sbusername="+encode+"&sbidcard="+insuranceRequestParameters.getUsername()+"&years="+getDateBefore("yyyy", j)+"&areacode="+taskInsurance.getCrawlerHost()+"&zhencode=&cuncode=");
		Page page3 = webClient.getPage(webRequest);
		System.out.println(page3.getWebResponse().getContentAsString());	
		if(page3.getWebResponse().getContentAsString().contains("工伤保险缴费明细"))
		{
			InsuranceZhaoQingInjury in=null;
			List<InsuranceZhaoQingInjury> list = new ArrayList<InsuranceZhaoQingInjury>();
			WebParam<InsuranceZhaoQingInjury> webParam = new WebParam<InsuranceZhaoQingInjury>();
			Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
			if(page3.getWebResponse().getContentAsString().contains("没有查到相关数据哦"))
			{
				return null;
			}
			else if(page3.getWebResponse().getContentAsString().contains("查询异常"))
			{
				return null;
			}
			else if(page3.getWebResponse().getContentAsString().contains("hccx_tableCon"))
			{
				Elements elementsByClass = doc.getElementsByClass("hccx_tableCon").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
				for (int i = 0; i < elementsByClass.size(); i++) {
//					System.out.println(elementsByClass.get(i).getElementsByTag("td").get(1));
					in = new InsuranceZhaoQingInjury();
					in.setDatea(elementsByClass.get(i).getElementsByTag("td").get(0).text());
					in.setCompany(elementsByClass.get(i).getElementsByTag("td").get(1).text());
					in.setBase(elementsByClass.get(i).getElementsByTag("td").get(2).text());
					in.setCompanyPay(elementsByClass.get(i).getElementsByTag("td").get(3).text());
					in.setPersonalPay(elementsByClass.get(i).getElementsByTag("td").get(4).text());
					in.setSum(elementsByClass.get(i).getElementsByTag("td").get(5).text());
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				webParam.setHtml(page3.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(url2);
				return webParam;
			}
		}		
		return null;
	}

	//失业
	public WebParam<InsuranceZhaoQingUnemployment> crawlerUnemployment(
			InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance, int j) throws  Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url2="http://wsbs.zhaoqing.gov.cn/portal/jcfw2/insurance/insurance!findInsurance.action";
		WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.POST);
		String encode = URLEncoder.encode(insuranceRequestParameters.getName(), "UTF-8");
		webRequest.setRequestBody("type=6&userid="+taskInsurance.getWebdriverHandle()+"&sbusername="+encode+"&sbidcard="+insuranceRequestParameters.getUsername()+"&years="+getDateBefore("yyyy", j)+"&areacode="+taskInsurance.getCrawlerHost()+"&zhencode=&cuncode=");
		Page page3 = webClient.getPage(webRequest);
		System.out.println(page3.getWebResponse().getContentAsString());	
		if(page3.getWebResponse().getContentAsString().contains("失业保险缴费明细"))
		{
			InsuranceZhaoQingUnemployment in=null;
			List<InsuranceZhaoQingUnemployment> list = new ArrayList<InsuranceZhaoQingUnemployment>();
			WebParam<InsuranceZhaoQingUnemployment> webParam = new WebParam<InsuranceZhaoQingUnemployment>();
			Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
			if(page3.getWebResponse().getContentAsString().contains("没有查到相关数据哦"))
			{
				return null;
			}
			else if(page3.getWebResponse().getContentAsString().contains("查询异常"))
			{
				return null;
			}
			else if(page3.getWebResponse().getContentAsString().contains("hccx_tableCon"))
			{
				Elements elementsByClass = doc.getElementsByClass("hccx_tableCon").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
				for (int i = 0; i < elementsByClass.size(); i++) {
//					System.out.println(elementsByClass.get(i).getElementsByTag("td").get(1));
					in = new InsuranceZhaoQingUnemployment();
					in.setDatea(elementsByClass.get(i).getElementsByTag("td").get(0).text());
					in.setCompany(elementsByClass.get(i).getElementsByTag("td").get(1).text());
					in.setBase(elementsByClass.get(i).getElementsByTag("td").get(2).text());
					in.setCompanyPay(elementsByClass.get(i).getElementsByTag("td").get(3).text());
					in.setPersonalPay(elementsByClass.get(i).getElementsByTag("td").get(4).text());
					in.setSum(elementsByClass.get(i).getElementsByTag("td").get(5).text());
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				webParam.setHtml(page3.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(url2);
				return webParam;
			}
		}		
		return null;
	}


}
