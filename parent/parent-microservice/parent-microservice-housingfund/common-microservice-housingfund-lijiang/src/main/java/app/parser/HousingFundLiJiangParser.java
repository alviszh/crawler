package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.lijiang.HousingFundLiJiangAccount;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundLiJiangParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		tracer.addTag("parser.login.start", taskHousing.getTaskid());
		String url="http://www.ljgjj.com/grgjjcx.asp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		HtmlPage page = webClient.getPage(url);		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@name='gjjzh']");
		id_card.reset();
		id_card.setText(messageLoginForHousing.getNum());//533222198910152014
//		System.out.println(messageLoginForHousing);
		HtmlTextInput id_account = (HtmlTextInput)page.getFirstByXPath("//input[@name='name']");
		id_account.reset();
		id_account.setText(messageLoginForHousing.getUsername());//杨宏敏
		
		
		HtmlTextInput searchpwd = (HtmlTextInput)page.getFirstByXPath("//input[@name='phone']");
		searchpwd.reset();
		searchpwd.setText(messageLoginForHousing.getPassword());//758926
		
		HtmlImage img = page.getFirstByXPath("/html/body/table[2]/tbody/tr[3]/td/table/tbody/tr/td/form/div/img");
		Page click = img.click();
		Page click1 = img.click();
		
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "4004");
		boolean isNum = verifycode.matches("[0-9]*"); 
		if(isNum==true)
		{
//			System.out.println(verifycode);
			HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("/html/body/table[2]/tbody/tr[3]/td/table/tbody/tr/td/form/div/input[4]");
			identifying.reset();
			identifying.setText(verifycode);
			
			HtmlElement button = page.getFirstByXPath("/html/body/table[2]/tbody/tr[3]/td/table/tbody/tr/td/form/div/input[5]");
			HtmlPage page2 = button.click();
			Thread.sleep(1000);
//			System.out.println(page2.getWebResponse().getContentAsString());
			if(null != page2)
			{
				int code = page2.getWebResponse().getStatusCode();
				if(code==200)
				{
					String string = page2.getWebResponse().getContentAsString();
					webParam.setCode(code);
					webParam.setHtml(string);
					webParam.setHtmlPage(page2);
					webParam.setUrl(verifycode);
					webParam.setWebClient(page2.getWebClient());
					return webParam;
				}
			}
		}
		return null;
	}
	
	
	public WebParam<HousingFundLiJiangAccount> crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebParam<HousingFundLiJiangAccount> webParam = new WebParam<HousingFundLiJiangAccount>();
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(taskHousing.getCookies());
//		for (Cookie cookie : cookies1) {
//			webClient.getCookieManager().addCookie(cookie);
//	    }
		String html = taskHousing.getCookies();
		if(html.contains("住房公积金个人业务明细"))
		{
			Document doc = Jsoup.parse(html);
			Elements elementsByTag = doc.getElementsByTag("tbody").get(1).getElementsByTag("tr");//("/html/body/table[2]/tbody");
			HousingFundLiJiangAccount h = null;
			List<HousingFundLiJiangAccount>  list = new ArrayList<HousingFundLiJiangAccount>();
			for (int i = 1; i < elementsByTag.size(); i++) {
				h = new HousingFundLiJiangAccount();
//				System.out.println(elementsByTag.get(i).getElementsByTag("td").text());
				h.setDatea(elementsByTag.get(i).getElementsByTag("td").get(0).text());
				h.setDescr(elementsByTag.get(i).getElementsByTag("td").get(1).text());
				h.setJf(elementsByTag.get(i).getElementsByTag("td").get(2).text());
				h.setDf(elementsByTag.get(i).getElementsByTag("td").get(3).text());
				h.setFee(elementsByTag.get(i).getElementsByTag("td").get(4).text());
				h.setTaskid(taskHousing.getTaskid());
				list.add(h);
			}
//			System.out.println(list);
			webParam.setHtml(html);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}

}
