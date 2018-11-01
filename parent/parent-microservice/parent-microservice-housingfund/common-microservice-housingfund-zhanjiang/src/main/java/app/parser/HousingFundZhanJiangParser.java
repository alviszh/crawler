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

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhanjiang.HousingFundZhanJiangAccount;
import com.microservice.dao.entity.crawler.housing.zhanjiang.HousingFundZhanJiangUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
@Component
public class HousingFundZhanJiangParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String url="https://www.zjzfgjj.gov.cn/Modules/GJJQuery/GJJLogin.aspx";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput searchpwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='MainContent_GjjLogin1_txtzh']");
		searchpwd.setText(messageLoginForHousing.getNum());
		
		HtmlPasswordInput searchpwd1 = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='MainContent_GjjLogin1_txtPassword']");
		searchpwd1.setText(messageLoginForHousing.getPassword());
		
        HtmlImage img = page.getFirstByXPath("//*[@id='MainContent_GjjLogin1_txtAuthCode']");
		
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//*[@id='MainContent_GjjLogin1_txtAuthCodeEqual']");
		identifying.reset();
		identifying.setText(verifycode); 
		
		HtmlElement button = page.getFirstByXPath("//*[@id='MainContent_GjjLogin1_StaticBtnSubmit']");
		HtmlPage page2 = button.click();
//		System.out.println(page2.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		if(page2.getWebResponse().getContentAsString().contains("欢迎光临湛江市住房公积金管理中心官方网站"))
		{
			
//			HtmlForm firstByXPath = page2.getFirstByXPath("//*[@id='form1']");
//			System.out.println(firstByXPath);
//			String[] split = firstByXPath.asXml().split("form1");
//			String[] split2 = split[0].split("id");
//			String substring = split2[1].substring(1, 33);
//			System.out.println(substring);
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			return webParam;
		}
		return null;
	}

	public WebParam<HousingFundZhanJiangUserInfo> crawlerUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		String loginMessageJson = taskHousing.getLoginMessageJson();
		Document parse = Jsoup.parse(loginMessageJson);
		if(loginMessageJson.contains("MainContent_tabConainer_tabAccInfo"))
		{
			Elements elementById = parse.getElementById("MainContent_tabConainer_tabAccInfo").getElementsByTag("tr");
//			System.out.println(elementById);
			HousingFundZhanJiangUserInfo h = new HousingFundZhanJiangUserInfo();
			
			
			String name = getNextLabelByKeywordTwo(elementById, "姓名", "th");
			h.setName(name);
			String fundNum = getNextLabelByKeywordTwo(elementById, "公积金账号", "th");
			h.setFundNum(fundNum);
			String openDate = getNextLabelByKeywordTwo(elementById, "开户日期", "th");
			h.setOpenDate(openDate);
			String companyNum = getNextLabelByKeywordTwo(elementById, "单位账号", "th");
			h.setCompanyNum(companyNum);
			String company = getNextLabelByKeywordTwo(elementById, "单位名称", "th");
			h.setCompany(company);
			String status = getNextLabelByKeywordTwo(elementById, "公积金状态", "th");
			h.setStatus(status);
			String cardStatus = getNextLabelByKeywordTwo(elementById, "注册证件类型", "th");
			h.setCardStatus(cardStatus);
			String iDNum = getNextLabelByKeywordTwo(elementById, "注册证件号码", "th");
			h.setIDNum(iDNum);
			String base = getNextLabelByKeywordTwo(elementById, "缴存基数", "th");
			h.setBase(base);
			String companyPay = getNextLabelByKeywordTwo(elementById, "单位月缴额", "th");
			h.setCompanyPay(companyPay);
			String personalPay = getNextLabelByKeywordTwo(elementById, "个人月缴额", "th");
			h.setPersonalPay(personalPay);
			String monthPay = getNextLabelByKeywordTwo(elementById, "财政月缴额", "th");
			h.setMonthPay(monthPay);
			String birth = getNextLabelByKeywordTwo(elementById, "出生年月", "th");
			h.setBirth(birth);
			String sex = getNextLabelByKeywordTwo(elementById, "性别", "th");
			h.setSex(sex);
			String endDate = getNextLabelByKeywordTwo(elementById, "缴存截至年月", "th");
			h.setEndDate(endDate);
			String phone = getNextLabelByKeywordTwo(elementById, "手机号码", "th");
			h.setPhone(phone);
			String addr = getNextLabelByKeywordTwo(elementById, "通讯地址", "th");
			h.setAddr(addr);
			String num = getNextLabelByKeywordTwo(elementById, "固定电话", "th");
			h.setNum(num);
			
			h.setTaskid(taskHousing.getTaskid());
//			System.out.println(h);
			WebParam<HousingFundZhanJiangUserInfo> webParam = new WebParam<HousingFundZhanJiangUserInfo>();
			webParam.setHousingZhanJiangUserInfo(h);
			webParam.setHtml(elementById.html());
			return webParam;
		}
		return null;
	}

	public WebParam<HousingFundZhanJiangAccount> crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int time) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url3="https://www.zjzfgjj.gov.cn/Modules/GJJQuery/GJJDetail1.aspx?id="+taskHousing.getWebdriverHandle()+"&type=1";
		WebRequest webReque = new WebRequest(new URL(url3), HttpMethod.POST);
		Page page8 = webClient.getPage(webReque);
		
		
		System.out.println(page8.getWebResponse().getContentAsString());
		String url2="https://www.zjzfgjj.gov.cn/Modules/GJJQuery/GJJDetail1.aspx?act=person&com="+taskHousing.getWebdriverHandle()+"&t="+System.currentTimeMillis();
		String url4="https://www.zjzfgjj.gov.cn/Modules/GJJQuery/GJJDetail1.aspx?year="+getDateBefore("yyyy", time)+"&iscom=true&com="+taskHousing.getWebdriverHandle();
		
		WebRequest webRequest5 = new WebRequest(new URL(url4), HttpMethod.POST);
		Page page5 = webClient.getPage(webRequest5);
		Thread.sleep(10000);
		WebRequest webRequest7 = new WebRequest(new URL(url2), HttpMethod.POST);
		Page page7 = webClient.getPage(webRequest7);
		Thread.sleep(10000);
		System.out.println(page7.getWebResponse().getContentAsString());

		System.out.println(page5.getWebResponse().getContentAsString()+page7.getWebResponse().getContentAsString());
		HousingFundZhanJiangAccount h = null;
		List<HousingFundZhanJiangAccount> list = new ArrayList<HousingFundZhanJiangAccount>();
		Document parse = Jsoup.parse(page7.getWebResponse().getContentAsString());
		WebParam<HousingFundZhanJiangAccount> webParam = new WebParam<HousingFundZhanJiangAccount>();
		if(page7.getWebResponse().getContentAsString().contains("发生日期"))
		{
			Elements elementsByTag = parse.getElementsByTag("table").get(1).getElementsByTag("tr");
			for (int i = 1; i < elementsByTag.size(); i++) {
				h = new HousingFundZhanJiangAccount();
//				System.out.println(elementsByTag.get(i));	
				h.setDatea(elementsByTag.get(i).getElementsByTag("td").get(0).text());
				h.setDesrc(elementsByTag.get(i).getElementsByTag("td").get(1).text());
				h.setGetMoney(elementsByTag.get(i).getElementsByTag("td").get(2).text());
				h.setOutMoney(elementsByTag.get(i).getElementsByTag("td").get(3).text());
				h.setFee(elementsByTag.get(i).getElementsByTag("td").get(4).text());
				h.setTaskid(taskHousing.getTaskid());
				list.add(h);
			}
			webParam.setHtml(page7.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url4);
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
	public  String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
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
