package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangEndowment;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangInjury;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangMaternity;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangMedical;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangUnemployment;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
@Component
public class InsuranceXinXiangParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception{

		String url="https://www.haxx.lss.gov.cn/xxsshbz/userLogin/toLoginPage";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='userName']");
		id_card.reset();
		id_card.setText("410711198306271031");
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='password']");
		id_account.reset();
		id_account.setText("111111");
		
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='random']");
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "6004");
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='randnum']");
		h.setText(verifycode);
		
		
		HtmlElement firstByXPath = page.getFirstByXPath("/html/body/div[2]/div/div[2]/form/div/div[5]/button");
		Page page2 = firstByXPath.click();
//		System.out.println(page2.getWebResponse().getContentAsString());
		String url2="https://www.haxx.lss.gov.cn/xxsshbz/userLogin/index";
		WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest);
		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		if(page3.getWebResponse().getContentAsString().contains("个人养老保险基本信息"))
		{
			webParam.setUrl(url2);
			webParam.setWebClient(webClient);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			return webParam;
		}
		return null;
			
	}
	
	
	//个人信息
	public WebParam<InsuranceXinXiangUserInfo> crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url="https://www.haxx.lss.gov.cn/xxfshbz/insured/insuredBasinInfo";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		
		String url2="https://www.haxx.lss.gov.cn/xxsshbz/secialsecurity/socialSecurityBasicInfo?jspInfo=1";
		WebRequest webRequest1 = new WebRequest(new URL(url2), HttpMethod.GET);
		Page page1 = webClient.getPage(webRequest1);
		
		Document doc = Jsoup.parse(page1.getWebResponse().getContentAsString());
		Document doc1 = Jsoup.parse(page.getWebResponse().getContentAsString());
		WebParam<InsuranceXinXiangUserInfo> webParam = new WebParam<InsuranceXinXiangUserInfo>();
		if(page1.getWebResponse().getContentAsString().contains("个人基本信息")&&page.getWebResponse().getContentAsString().contains("个人基本信息"))
		{
			Elements elementsByClass = doc.getElementsByClass("cx_table");
			Elements elementsByClass1 = doc1.getElementsByClass("cx_table");
			
			InsuranceXinXiangUserInfo i = new InsuranceXinXiangUserInfo();
			String nextLabelByKeywordTwo = getNextLabelByKeywordTwo(elementsByClass,"姓名", "td");
			i.setTaskid(taskInsurance.getTaskid());
			i.setName(nextLabelByKeywordTwo);
			i.setPersonalPhone(getNextLabelByKeywordTwo(elementsByClass,"手机", "td"));
			i.setSex(getNextLabelByKeywordTwo(elementsByClass,"性别", "td"));
			i.setCardNum(getNextLabelByKeywordTwo(elementsByClass,"社保卡号", "td"));
			i.setKeeper(getNextLabelByKeywordTwo(elementsByClass,"监护人姓名", "td"));
			i.setIDNum(getNextLabelByKeywordTwo(elementsByClass,"身份证号", "td"));
			i.setCompany(getNextLabelByKeywordTwo(elementsByClass,"单位名称", "td"));
			i.setAddr(getNextLabelByKeywordTwo(elementsByClass,"通讯地址", "td"));
			i.setCommunity(getNextLabelByKeywordTwo(elementsByClass,"所在社区（村）", "td"));
			
			i.setPersonalNum(getNextLabelByKeywordTwo(elementsByClass1,"个人编号", "td"));
			i.setNational(getNextLabelByKeywordTwo(elementsByClass1,"民族", "td"));
			i.setBirth(getNextLabelByKeywordTwo(elementsByClass1,"出生日期", "td"));
			i.setStatus(getNextLabelByKeywordTwo(elementsByClass1,"单位状态", "td"));
			i.setJobStatus(getNextLabelByKeywordTwo(elementsByClass1,"行业风险类型", "td"));
			i.setCompanyNum(getNextLabelByKeywordTwo(elementsByClass1,"单位编号", "td"));
			i.setCompanyType(getNextLabelByKeywordTwo(elementsByClass1,"单位类型", "td"));
			i.setPayName(getNextLabelByKeywordTwo(elementsByClass1,"缴费单位专管员姓名", "td"));
			i.setPayPhone(getNextLabelByKeywordTwo(elementsByClass1,"缴费单位专管员电话", "td"));
			i.setJoinDate(getNextLabelByKeywordTwo(elementsByClass1,"参加工作时间", "td"));
			System.out.println(i);
			webParam.setHtml(page1.getWebResponse().getContentAsString()+page.getWebResponse().getContentAsString());
			webParam.setInsuranceXinXiangUserInfo(i);
			webParam.setUrl(url2);
			return webParam;
		}
		return null;
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容2
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
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


	public WebParam<InsuranceXinXiangEndowment> crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url="https://www.haxx.lss.gov.cn/xxsshbz/endowment/queryEndowmentPayInfo?pageNum=1&pageSize=1000";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		WebParam<InsuranceXinXiangEndowment> webParam = new WebParam<InsuranceXinXiangEndowment>();
		List<InsuranceXinXiangEndowment> list = new ArrayList<InsuranceXinXiangEndowment>();
		InsuranceXinXiangEndowment in = null;
		if(page.getWebResponse().getContentAsString().contains("养老缴费信息"))
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementsByClass = doc.getElementsByClass("cx_table").get(0).getElementsByTag("tr");
			for (int i = 1; i < elementsByClass.size()-3; i++) {
				in = new InsuranceXinXiangEndowment();
				Elements elementsByTag = elementsByClass.get(i).getElementsByTag("td");
				in.setEndDate(elementsByTag.get(0).text());
				in.setBase(elementsByTag.get(1).text());
				in.setPersonalPay(elementsByTag.get(2).text());
				in.setCompanyPay(elementsByTag.get(3).text());
				in.setCompanyAccount(elementsByTag.get(4).text());
				in.setAverage(elementsByTag.get(5).text());
				in.setPayMoney(elementsByTag.get(6).text());
				in.setStatus(elementsByTag.get(7).text());
				in.setPayFlag(elementsByTag.get(8).text());
				in.setGetDate(elementsByTag.get(9).text());
				in.setAccountFlag(elementsByTag.get(10).text());
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
				
			}
			System.out.println(list);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}


	public WebParam<InsuranceXinXiangMedical> crawlerMedical(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url="https://www.haxx.lss.gov.cn/xxsshbz/medical/queryMediacalPayInfo?pageNum=1&pageSize=1000";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		
		WebParam<InsuranceXinXiangMedical> webParam = new WebParam<InsuranceXinXiangMedical>();
		List<InsuranceXinXiangMedical> list = new ArrayList<InsuranceXinXiangMedical>();
		InsuranceXinXiangMedical in = null;
		if(page.getWebResponse().getContentAsString().contains("医疗缴费信息"))
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementsByClass = doc.getElementsByClass("cx_table").get(0).getElementsByTag("tr");
			for (int i = 1; i < elementsByClass.size()-3; i++) {
				in = new InsuranceXinXiangMedical();
				Elements elementsByTag = elementsByClass.get(i).getElementsByTag("td");
				in.setEndDate(elementsByTag.get(0).text());
				in.setBase(elementsByTag.get(1).text());
				in.setPersonalPay(elementsByTag.get(2).text());
				in.setCompanyPay(elementsByTag.get(3).text());
				in.setCompanyAccount(elementsByTag.get(4).text());
				in.setPayMoney(elementsByTag.get(5).text());
				in.setStatus(elementsByTag.get(6).text());
				in.setPayFlag(elementsByTag.get(7).text());
				in.setAccountFlag(elementsByTag.get(8).text());
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			System.out.println(list);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}


	public WebParam<InsuranceXinXiangUnemployment> crawlerUnemplyment(
			InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url="https://www.haxx.lss.gov.cn/xxsshbz/unemployment/queryUnemploymentPayInfo?pageNum=1&pageSize=1000";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		
		WebParam<InsuranceXinXiangUnemployment> webParam = new WebParam<InsuranceXinXiangUnemployment>();
		List<InsuranceXinXiangUnemployment> list = new ArrayList<InsuranceXinXiangUnemployment>();
		InsuranceXinXiangUnemployment in = null;
		if(page.getWebResponse().getContentAsString().contains("失业缴费欠费信息"))
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementsByClass = doc.getElementsByClass("cx_table").get(0).getElementsByTag("tr");
			for (int i = 1; i < elementsByClass.size()-3; i++) {
				in = new InsuranceXinXiangUnemployment();
				Elements elementsByTag = elementsByClass.get(i).getElementsByTag("td");
				in.setBase(elementsByTag.get(0).text());
				in.setPersonalPay(elementsByTag.get(1).text());
				in.setCompanyAccount(elementsByTag.get(2).text());
				in.setPayMoney(elementsByTag.get(3).text());
				in.setPayFlag(elementsByTag.get(4).text());
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			System.out.println(list);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}


	public WebParam<InsuranceXinXiangInjury> crawlerInjury(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url="https://www.haxx.lss.gov.cn/xxsshbz/employment/employmentInjuryBasicInfo";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		WebParam<InsuranceXinXiangInjury> webParam = new WebParam<InsuranceXinXiangInjury>();
		if(page.getWebResponse().getContentAsString().contains("暂无有效信息"))
		{
			return null;
		}
		else
		{
			webParam.setHtml(page.getWebResponse().getContentAsString());
			return webParam;
		}
	}


	public WebParam<InsuranceXinXiangMaternity> crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url="https://www.haxx.lss.gov.cn/xxsshbz/maternity/maternityBasicInfo";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		
		WebParam<InsuranceXinXiangMaternity> webParam = new WebParam<InsuranceXinXiangMaternity>();
		if(page.getWebResponse().getContentAsString().contains("暂无有效信息"))
		{
			return null;
		}
		else
		{
			webParam.setHtml(page.getWebResponse().getContentAsString());
			return webParam;
		}
	}

}
