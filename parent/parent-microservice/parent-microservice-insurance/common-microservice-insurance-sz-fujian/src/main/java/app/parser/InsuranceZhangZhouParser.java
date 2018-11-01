package app.parser;

import java.net.URL;
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
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouEndowment;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouInjury;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouMaternity;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouMedical;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;

@Component
public class InsuranceZhangZhouParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;

	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance)
			throws Exception {
		String url = "http://www.fj12333.gov.cn:268/fwpt/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);

		HtmlTextInput id_card = (HtmlTextInput) page.getFirstByXPath("//*[@id='aac003']");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getName());

		HtmlTextInput id_account = (HtmlTextInput) page.getFirstByXPath("//*[@id='aac002']");
		id_account.reset();
		id_account.setText(insuranceRequestParameters.getUsername());

		HtmlPasswordInput id_account2 = (HtmlPasswordInput) page.getFirstByXPath("//*[@id='ysc002']");
		id_account2.reset();
		id_account2.setText(insuranceRequestParameters.getPassword());

		HtmlImage img = page.getFirstByXPath("//*[@id='checkImg']");
		// String imageName = "111.jpg";
		// File file = new File("D:\\img\\" + imageName);
		// img.saveAs(file);
		// String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		String inputValue = chaoJiYingOcrService.getVerifycode(img, "1902");

		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='randCode']");
		h.setText(inputValue);

		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='loginimg']/img");
		Page page2 = firstByXPath.click();
		WebParam webParam = new WebParam();
		System.out.println(page2.getWebResponse().getContentAsString());
		if (page2.getWebResponse().getContentAsString().contains("你好")) {
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			return webParam;
		}
		return null;
	}

	// 个人信息
	public WebParam<InsuranceZhangZhouUserInfo> crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		String url = "http://www.fj12333.gov.cn:268/fwpt/queryPersonInfo.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		// System.out.println(page.getWebResponse().getContentAsString());

		WebParam<InsuranceZhangZhouUserInfo> webParam = new WebParam<InsuranceZhangZhouUserInfo>();
		if (page.getWebResponse().getContentAsString().contains("基本信息")) {
			String replaceAll = page.getWebResponse().getContentAsString().replace("<!--", "").replaceAll("-->", "");
			Document doc = Jsoup.parse(replaceAll);
			InsuranceZhangZhouUserInfo i = new InsuranceZhangZhouUserInfo();
			Elements elementById = doc.getElementById("result").getElementsByTag("tbody").get(0).getElementsByTag("tr");
			String name = getNextLabelByKeywordTwo(elementById, "姓名", "td");
			i.setName(name);
			i.setIDNum(getNextLabelByKeywordTwo(elementById, "身份证号", "td"));
			i.setBirth(getNextLabelByKeywordTwo(elementById, "出生日期", "td"));
			i.setHomeLand(getNextLabelByKeywordTwo(elementById, "户口所在地址", "td"));
			i.setPhone(getNextLabelByKeywordTwo(elementById, "移动电话", "td"));
			i.setTelephone(getNextLabelByKeywordTwo(elementById, "联系电话", "td"));
			i.setAddr(getNextLabelByKeywordTwo(elementById, "通讯地址", "td"));
			i.setCode(getNextLabelByKeywordTwo(elementById, "邮政编码", "td"));
			i.setEmail(getNextLabelByKeywordTwo(elementById, "电子邮箱", "td"));
			i.setCardNum(getNextLabelByKeywordTwo(elementById, "社保卡号", "td"));
			i.setStatus(getNextLabelByKeywordTwo(elementById, "当前状态", "td"));
			i.setTaskid(taskInsurance.getTaskid());
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setInsuranceZhangZhouUserInfo(i);
			webParam.setUrl(url);
			return webParam;
		}
		return null;
	}

	// 养老
	public WebParam<InsuranceZhangZhouEndowment> crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance, int time) throws Exception {
		String url = "http://www.fj12333.gov.cn:268/fwpt/queryCorpPersionPerFund.do?aae140=110&aae041="
				+ getDateBefore("yyyyMM", time) + "&aae042=" + getDateBefore("yyyyMM", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		// System.out.println(page.getWebResponse().getContentAsString());

		WebParam<InsuranceZhangZhouEndowment> webParam = new WebParam<InsuranceZhangZhouEndowment>();
		if (page.getWebResponse().getContentAsString().contains("ylzps_row1")) {
			InsuranceZhangZhouEndowment in = null;
			List<InsuranceZhangZhouEndowment> list = new ArrayList<InsuranceZhangZhouEndowment>();
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementById = doc.getElementById("ylzps").getElementsByTag("tbody").get(0).getElementsByTag("tr");
			for (int i = 0; i < elementById.size(); i++) {
				in = new InsuranceZhangZhouEndowment();
				// System.out.println(elementById.get(i));
				in.setDatea(elementById.get(i).getElementsByTag("td").get(0).text());
				in.setCompany(elementById.get(i).getElementsByTag("td").get(1).text());
				in.setType(elementById.get(i).getElementsByTag("td").get(2).text());
				in.setPersonalPay(elementById.get(i).getElementsByTag("td").get(3).text());
				in.setCompanyPay(elementById.get(i).getElementsByTag("td").get(4).text());
				in.setMoney(elementById.get(i).getElementsByTag("td").get(5).text());
				in.setBase(elementById.get(i).getElementsByTag("td").get(6).text());
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			webParam.setList(list);
			webParam.setUrl(url);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			return webParam;
		}
		return null;
	}

	// 医疗
	public WebParam<InsuranceZhangZhouMedical> crawlerMedical(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance, int time) throws Exception {
		String url = "http://www.fj12333.gov.cn:268/fwpt/queryCorpPersionPerFund.do?aae140=310&aae041="
				+ getDateBefore("yyyyMM", time) + "&aae042=" + getDateBefore("yyyyMM", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		// System.out.println(page.getWebResponse().getContentAsString());

		WebParam<InsuranceZhangZhouMedical> webParam = new WebParam<InsuranceZhangZhouMedical>();
		if (page.getWebResponse().getContentAsString().contains("ylzps_row1")) {

			InsuranceZhangZhouMedical in = null;
			List<InsuranceZhangZhouMedical> list = new ArrayList<InsuranceZhangZhouMedical>();
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementById = doc.getElementById("ylzps").getElementsByTag("tbody").get(0).getElementsByTag("tr");
			for (int i = 0; i < elementById.size(); i++) {
				in = new InsuranceZhangZhouMedical();
				// System.out.println(elementById.get(i));
				in.setDatea(elementById.get(i).getElementsByTag("td").get(0).text());
				in.setCompany(elementById.get(i).getElementsByTag("td").get(1).text());
				in.setType(elementById.get(i).getElementsByTag("td").get(2).text());
				in.setPersonalPay(elementById.get(i).getElementsByTag("td").get(3).text());
				in.setCompanyPay(elementById.get(i).getElementsByTag("td").get(4).text());
				in.setMoney(elementById.get(i).getElementsByTag("td").get(5).text());
				in.setBase(elementById.get(i).getElementsByTag("td").get(6).text());
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			webParam.setList(list);
			webParam.setUrl(url);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			return webParam;

		}
		return null;
	}

	// 生育
	public WebParam<InsuranceZhangZhouMaternity> crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance, int time) throws Exception {
		String url = "http://www.fj12333.gov.cn:268/fwpt/queryCorpPersionPerFund.do?aae140=510&aae041="
				+ getDateBefore("yyyyMM", time) + "&aae042=" + getDateBefore("yyyyMM", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		// System.out.println(page.getWebResponse().getContentAsString());

		WebParam<InsuranceZhangZhouMaternity> webParam = new WebParam<InsuranceZhangZhouMaternity>();
		if (page.getWebResponse().getContentAsString().contains("ylzps_row1")) {
			InsuranceZhangZhouMaternity in = null;
			List<InsuranceZhangZhouMaternity> list = new ArrayList<InsuranceZhangZhouMaternity>();
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementById = doc.getElementById("ylzps").getElementsByTag("tbody").get(0).getElementsByTag("tr");
			for (int i = 0; i < elementById.size(); i++) {
				in = new InsuranceZhangZhouMaternity();
				in.setDatea(elementById.get(i).getElementsByTag("td").get(0).text());
				in.setCompanyPay(elementById.get(i).getElementsByTag("td").get(1).text());
				in.setBase(elementById.get(i).getElementsByTag("td").get(2).text());
				in.setCompanyRatio(elementById.get(i).getElementsByTag("td").get(3).text());
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url);
			return webParam;
		}
		return null;
	}

	// 工伤
	public WebParam<InsuranceZhangZhouInjury> crawlerInjury(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance, int time) throws Exception {
		String url = "http://www.fj12333.gov.cn:268/fwpt/queryCorpPersionPerFund.do?aae140=410&aae041="
				+ getDateBefore("yyyyMM", time) + "&aae042=" + getDateBefore("yyyyMM", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		// System.out.println(page.getWebResponse().getContentAsString());

		WebParam<InsuranceZhangZhouInjury> webParam = new WebParam<InsuranceZhangZhouInjury>();
		if (page.getWebResponse().getContentAsString().contains("ylzps_row1")) {
			InsuranceZhangZhouInjury in = null;
			List<InsuranceZhangZhouInjury> list = new ArrayList<InsuranceZhangZhouInjury>();
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementById = doc.getElementById("ylzps").getElementsByTag("tbody").get(0).getElementsByTag("tr");
			for (int i = 0; i < elementById.size(); i++) {
				in = new InsuranceZhangZhouInjury();
				in.setDatea(elementById.get(i).getElementsByTag("td").get(0).text());
				in.setCompanyPay(elementById.get(i).getElementsByTag("td").get(1).text());
				in.setBase(elementById.get(i).getElementsByTag("td").get(2).text());
				in.setCompanyRatio(elementById.get(i).getElementsByTag("td").get(3).text());
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url);
			return webParam;
		}
		return null;
	}

	// 失业
	public WebParam<InsuranceZhangZhouUnemployment> crawlerUnemployment(
			InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance, int time)
			throws Exception {
		String url = "http://www.fj12333.gov.cn:268/fwpt/queryCorpPersionPerFund.do?aae140=210&aae041="
				+ getDateBefore("yyyyMM", time) + "&aae042=" + getDateBefore("yyyyMM", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		// System.out.println(page.getWebResponse().getContentAsString());

		WebParam<InsuranceZhangZhouUnemployment> webParam = new WebParam<InsuranceZhangZhouUnemployment>();
		List<InsuranceZhangZhouUnemployment> list = new ArrayList<InsuranceZhangZhouUnemployment>();
		InsuranceZhangZhouUnemployment in = null;
		if(page.getWebResponse().getContentAsString().contains("ylzps_row1")){
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementById = doc.getElementById("ylzps").getElementsByTag("tbody").get(0).getElementsByTag("tr");
			for (int i = 0; i < elementById.size(); i++) {
				in = new InsuranceZhangZhouUnemployment();
				in.setStartDate(elementById.get(i).getElementsByTag("td").get(0).text());
				in.setEndDate(elementById.get(i).getElementsByTag("td").get(1).text());
				in.setMonthSum(elementById.get(i).getElementsByTag("td").get(2).text());
				in.setMonth(elementById.get(i).getElementsByTag("td").get(3).text());
				in.setNumber(elementById.get(i).getElementsByTag("td").get(4).text());
				in.setFlag(elementById.get(i).getElementsByTag("td").get(5).text());
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url);
			return webParam;
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

}
