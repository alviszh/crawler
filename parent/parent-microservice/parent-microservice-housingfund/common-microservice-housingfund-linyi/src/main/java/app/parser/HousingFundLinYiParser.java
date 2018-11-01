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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.linyi.HousingFundLinYiAccount;
import com.microservice.dao.entity.crawler.housing.linyi.HousingFundLinYiUserInfo;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundLinYiParser extends AbstractChaoJiYingHandler{

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebParam webParam = new WebParam();
		String url="http://www.lyzfgjj.gov.cn/abc/login.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@name='zfz']");
		id_card.reset();
		id_card.setText(messageLoginForHousing.getNum());
		
		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@name='zh']");
		searchpwd.reset();
		searchpwd.setText(messageLoginForHousing.getPassword());
		
		HtmlElement button = page.getFirstByXPath("//input[@class='loginbtn']");
		HtmlPage page2 = button.click();
		Thread.sleep(1000);
		WebClient webClient2 = page2.getWebClient();
		String mainUrl="http://www.lyzfgjj.gov.cn/abc/index.asp";
		Page page3 = webClient2.getPage(mainUrl);
		System.out.println(page3.getWebResponse().getContentAsString());
		webParam.setHtml(page3.getWebResponse().getContentAsString());
		webParam.setPage(page2);
		webParam.setWebClient(webClient2);
		return webParam;
	}

	
//	private String getOcrCode(String imgFilePath)  {
//		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, imgFilePath);
//		System.out.println("chaoJiYingResult ====>>"+chaoJiYingResult);
//		Gson gson = new GsonBuilder().create();
//		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
//		return code;
//	}
	
	
	public WebParam<HousingFundLinYiUserInfo> crawlerInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebParam<HousingFundLinYiUserInfo> webParam = new WebParam<HousingFundLinYiUserInfo>();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
		String url2="http://www.lyzfgjj.gov.cn/abc/index.asp";
		HtmlPage page3 = webClient.getPage(url2);
		
		HtmlImage image = page3.getFirstByXPath("//img[@src='GetCode.asp']"); 
		
		String verifycode = chaoJiYingOcrService.getVerifycode(image, "1902");
		
		HtmlTextInput elementById = (HtmlTextInput)page3.getElementByName("zh");
		elementById.setText(messageLoginForHousing.getPassword());
		
		HtmlTextInput pwd = (HtmlTextInput)page3.getElementByName("verifycode");
		pwd.setText(verifycode);
		
		HtmlElement elementByName = page3.getElementByName("submit2");
		Page page5 = elementByName.click();
		if(null != page5)
		{
			Document doc = Jsoup.parse(page5.getWebResponse().getContentAsString());
			System.out.println(page5.getWebResponse().getContentAsString());
			Elements elementsByTag = doc.getElementsByTag("tr").get(2).getElementsByTag("div");
			HousingFundLinYiUserInfo h = new HousingFundLinYiUserInfo();
			System.out.println(elementsByTag.text());
			h.setIdNum(elementsByTag.get(0).text());
			h.setFundNum(elementsByTag.get(1).text());
			h.setMonthPay(elementsByTag.get(2).text().substring(0, elementsByTag.get(2).text().length()-2));
			h.setFee(elementsByTag.get(3).text().substring(0, elementsByTag.get(3).text().length()-2));
			h.setPayDate(elementsByTag.get(4).text().substring(0, elementsByTag.get(4).text().length()-2));
			h.setTaskid(taskHousing.getTaskid());
			webParam.setHousingFundLinYiUserInfo(h);
			webParam.setHtml(page5.getWebResponse().getContentAsString());
			webParam.setUrl(url2);
			return webParam;
		}
		return null;
		
			
		
	}

	
	
	public WebParam<HousingFundLinYiAccount> crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		WebParam<HousingFundLinYiAccount> webParam = new WebParam<HousingFundLinYiAccount>();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url="http://www.lyzfgjj.gov.cn/abc/grzd.asp?zfz="+messageLoginForHousing.getNum()+"&zh="+messageLoginForHousing.getPassword()+"&x=grzd&verifycode=5524&submit2=%B2%E9%D1%AF";
		Page page = webClient.getPage(url);
		if(null != page)
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			
			Elements elementsByTag = doc.getElementsByTag("tr");
			HousingFundLinYiAccount h = null;
			List<HousingFundLinYiAccount> list = new ArrayList<HousingFundLinYiAccount>();
			for (int i = 2; i < elementsByTag.size()-1; i++) {
				if(elementsByTag.get(i).getElementsByTag("div").size()==6)
				{
					h = new HousingFundLinYiAccount();
					h.setIdNum(elementsByTag.get(i).getElementsByTag("div").get(0).text());
					h.setFundNum(elementsByTag.get(i).getElementsByTag("div").get(1).text());
					h.setDatea(elementsByTag.get(i).getElementsByTag("div").get(2).text());
					h.setDescc(elementsByTag.get(i).getElementsByTag("div").get(3).text());
					h.setMoney(elementsByTag.get(i).getElementsByTag("div").get(4).text());
					h.setInfo(elementsByTag.get(i).getElementsByTag("div").get(5).text());
					h.setTaskid(taskHousing.getTaskid());
				}
				list.add(h);
			}
			webParam.setUrl(url);
			webParam.setList(list);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			return webParam;
		}
		return null;
	}

}
