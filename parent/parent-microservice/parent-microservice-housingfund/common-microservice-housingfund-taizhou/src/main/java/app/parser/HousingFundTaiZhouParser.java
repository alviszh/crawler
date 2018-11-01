package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.taizhou.HousingFundTaiZhouAccount;
import com.microservice.dao.entity.crawler.housing.taizhou.HousingFundTaiZhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
@Component
public class HousingFundTaiZhouParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String url="https://puser.zjzwfw.gov.cn/sso/usp.do?action=ssoLogin&servicecode=njdh";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='sfzh']");
		id_card.reset();
		id_card.setText(messageLoginForHousing.getUsername());
		
		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='pswd']");
		searchpwd.reset();
		searchpwd.setText(messageLoginForHousing.getPassword());
		
		
        HtmlImage img = page.getFirstByXPath("//*[@id='yzm']");
		
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//input[@name='code']");
		identifying.reset();
		identifying.setText(verifycode);
		
		HtmlElement button = page.getFirstByXPath("//input[@id='add_submit']");
		HtmlPage page2 = button.click();
		WebParam webParam = new WebParam();
		System.out.println(page2.getWebResponse().getContentAsString());
		if(page2.getWebResponse().getContentAsString().contains("欢迎访问台州市住房公积金网站"))
		{
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			return webParam;
		}
		return null;
	}
	public WebParam<HousingFundTaiZhouUserInfo> crawler(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)  throws Exception{
		String cookies = taskHousing.getCookies();
		Document doc = Jsoup.parse(cookies);
		WebParam<HousingFundTaiZhouUserInfo> webParam = new WebParam<HousingFundTaiZhouUserInfo>();
		if(cookies.contains("归集情况"))
		{
			Elements elementsByClass = doc.getElementsByClass("con_body").get(0).getElementsByTag("tr").get(3).getElementsByTag("td");
			Elements elementsByClass1 = doc.getElementsByClass("con_body").get(0).getElementsByTag("tr").get(4).getElementsByTag("td");
			System.out.println(elementsByClass1);

			HousingFundTaiZhouUserInfo h = null;
					
					
			for (int i = 0; i < elementsByClass.size(); i=i+8) {
				h = new HousingFundTaiZhouUserInfo();
				h.setNum(elementsByClass.get(i).text());
				h.setName(elementsByClass.get(i+1).text());
				h.setIdCard(elementsByClass.get(i+2).text());
				h.setFee(elementsByClass.get(i+3).text());
				h.setMonthPay(elementsByClass.get(i+4).text());
				h.setStatus(elementsByClass.get(i+5).text());
				h.setPayDate(elementsByClass.get(i+6).text());
				h.setBank(elementsByClass.get(i+7).text());
				h.setCompany(elementsByClass1.text().substring(5));
				h.setTaskid(taskHousing.getTaskid());
			}
			
			System.out.println(h);
			webParam.setHousingFundTaiZhouUserInfo(h);
			webParam.setHtml(cookies);
			return webParam;
		}
		return null;
	}
	public WebParam<HousingFundTaiZhouAccount> crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		Document doc = Jsoup.parse(cookies);
		WebParam<HousingFundTaiZhouAccount> webParam = new WebParam<HousingFundTaiZhouAccount>();
		if(cookies.contains("归集明细"))
		{
			Elements elementsByClass2 = doc.getElementsByClass("custom_list").get(1).getElementsByTag("td");
			System.out.println(elementsByClass2);
			HousingFundTaiZhouAccount h1= null;
			List<HousingFundTaiZhouAccount> list = new ArrayList<HousingFundTaiZhouAccount>();
			for (int i = 0; i < elementsByClass2.size(); i=i+5) {
				h1 = new HousingFundTaiZhouAccount();
				h1.setType(elementsByClass2.get(i).text());
				h1.setNum(elementsByClass2.get(i+1).text());
				h1.setFee(elementsByClass2.get(i+2).text());
				h1.setDatea(elementsByClass2.get(i+3).text());
				h1.setMoney(elementsByClass2.get(i+4).text());
				h1.setTaskid(taskHousing.getTaskid());
				list.add(h1);
			}
			System.out.println(list);
			webParam.setHtml(cookies);
			webParam.setList(list);
			return webParam;
		}
			
		
		return null;
	}

}
