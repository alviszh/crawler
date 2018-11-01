package app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.tonghua.HousingFundTongHuaAccount;
import com.microservice.dao.entity.crawler.housing.tonghua.HousingFundTongHuaUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
@Component
public class HousingFundTongHuaParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		
		String url="http://www.thgjj.com/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='userName']");
		id_card.reset();
		id_card.setText("220519196903110565");
		
		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='passWord']");
		searchpwd.reset();
		searchpwd.setText("123456");
		
		HtmlElement button = page.getFirstByXPath("//*[@id='form1']/ul/li[3]/input");
		HtmlPage page2 = button.click();
		Thread.sleep(5000);
		//System.out.println(page2.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		String url1="http://www.thgjj.com/nethousing/topPage.action";
		WebClient webClient2 = page2.getWebClient();
		HtmlPage page3 = webClient2.getPage(url1);
		//System.out.println(page3.getWebResponse().getContentAsString());
		if(page3.getWebResponse().getContentAsString().contains("主页"))
		{
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setHtmlPage(page3);
			webParam.setUrl(url1);
			webParam.setWebClient(webClient2);
			return webParam;
		}
		return null;
	}

	public WebParam<HousingFundTongHuaUserInfo> crawlerUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url2="http://www.thgjj.com/nethousing/personalInformation_list.action";
		Page page4 = webClient.getPage(url2);
		//System.out.println(page4.getWebResponse().getContentAsString());
		
		WebParam<HousingFundTongHuaUserInfo> webParam = new WebParam<HousingFundTongHuaUserInfo>();
		if(page4.getWebResponse().getContentAsString().contains("查看个人信息"))
		{
			Document doc = Jsoup.parse(page4.getWebResponse().getContentAsString());
			HousingFundTongHuaUserInfo h = new HousingFundTongHuaUserInfo();
			Elements elementsByTag = doc.getElementsByTag("input");
			h.setComNum(elementsByTag.get(1).val());
			h.setCompany(elementsByTag.get(2).val());
			h.setPersonalNum(elementsByTag.get(3).val());
			h.setName(elementsByTag.get(4).val());
			h.setSex(elementsByTag.get(5).val());
			h.setIdCard(elementsByTag.get(6).val());
			h.setBirthday(elementsByTag.get(7).val());
			h.setDepartment(elementsByTag.get(8).val());
			h.setPersonalRatio(elementsByTag.get(9).val());
			h.setUnitRatio(elementsByTag.get(10).val());
			h.setGovemmentRatio(elementsByTag.get(11).val());
			h.setPersonalPay(elementsByTag.get(12).val());
			h.setUnitPay(elementsByTag.get(13).val());
			h.setGovementPay(elementsByTag.get(14).val());
			h.setMonthPay(elementsByTag.get(15).val());
			h.setBase(elementsByTag.get(16).val());
			h.setPersonalStatus(elementsByTag.get(17).val());
			h.setPersonalFee(elementsByTag.get(18).val());
			h.setOpenDate(elementsByTag.get(19).val());
			h.setTaskid(taskHousing.getTaskid());
			
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setUrl(url2);
			webParam.setWebClient(webClient);
			webParam.setHousingFundTongHuaUserInfo(h);
			return webParam;
		}
		return null;
	}

	public WebParam<HousingFundTongHuaAccount> crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String url="http://www.thgjj.com/nethousing/personaldetails_main.action";
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page4 = webClient.getPage(url);
		WebParam<HousingFundTongHuaAccount> webParam = new WebParam<HousingFundTongHuaAccount>();
		if(page4.getWebResponse().getContentAsString().contains("公积金明细查询"))
		{
			Document doc = Jsoup.parse(page4.getWebResponse().getContentAsString());
			HousingFundTongHuaAccount h =null;
			List<HousingFundTongHuaAccount> list = new ArrayList<HousingFundTongHuaAccount>();
//			System.out.println(doc);
			Elements elementsByTag = doc.getElementsByTag("form").get(1).getElementsByTag("tbody");
//			System.out.println(elementsByTag);
			for (int i = 0; i < elementsByTag.size(); i++) {
				Elements elementsByTag2 = elementsByTag.get(i).getElementsByTag("td");
				//System.out.println(elementsByTag2);
				for (int j = 0; j < elementsByTag2.size(); j=j+7) {
					h = new HousingFundTongHuaAccount();
					h.setDatea(elementsByTag2.get(j+1).text());
					h.setDescr(elementsByTag2.get(j+2).text());
					h.setPayDatea(elementsByTag2.get(j+3).text());
					h.setGetMoney(elementsByTag2.get(j+4).text());
					h.setCostMoney(elementsByTag2.get(j+5).text());
					h.setFee(elementsByTag2.get(j+6).text());
					h.setTaskid(taskHousing.getTaskid());
					list.add(h);
				}
			}
			webParam.setList(list);
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			return webParam;
		}
		return null;
	}
}
