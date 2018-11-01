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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.taizhou2.HousingFundTaiZhou2Account;
import com.microservice.dao.entity.crawler.housing.taizhou2.HousingFundTaiZhou2UserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
@Component
public class HousingFundTaiZhouParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String url="http://218.90.206.76:8080/jeesite/a/login";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='username']");
		id_card.reset();
		id_card.setText(messageLoginForHousing.getUsername());
		
		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
		searchpwd.reset();
		searchpwd.setText(messageLoginForHousing.getPassword());
		
		
        HtmlImage img = page.getFirstByXPath("//*[@id='loginForm']/div[1]/img");
		
		String inputValue = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//input[@name='validateCode']");
		identifying.reset();
		identifying.setText(inputValue);
		
		String url1="http://218.90.206.76:8080/jeesite/servlet/validateCodeServlet?validateCode="+inputValue;
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
		Page page1 = webClient.getPage(webRequest);
		String url2="http://218.90.206.76:8080/jeesite/a/login?username=321202199009030928&password=030928&validateCode="+inputValue;
		WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.POST);
		Page page2 = webClient.getPage(webRequest2);
		String url3="http://218.90.206.76:8080/jeesite/a";
		WebRequest webRequest3 = new WebRequest(new URL(url3), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest3);
		String url4="http://218.90.206.76:8080/jeesite/a/sys/user/index";
		WebRequest webRequest4 = new WebRequest(new URL(url4), HttpMethod.GET);
		Page page4 = webClient.getPage(webRequest4);
		System.out.println(page4.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		
		
		if(page4.getWebResponse().getContentAsString().contains("用户帐户列表"))
		{
			Document doc = Jsoup.parse(page4.getWebResponse().getContentAsString());
			Element elementById = doc.getElementById("contentTable");
//			System.out.println(elementById);
			Elements elementsByTag = elementById.getElementsByTag("tbody").get(0).getElementsByTag("tr");
//			System.out.println(elementsByTag);
			if(elementsByTag.size()>1)
			{
				taskHousing.setCrawlerHost("");
				for (int i = 0; i < elementsByTag.size(); i++) {
					Element element = elementsByTag.get(i).getElementsByTag("td").get(1);
					System.out.println(element);
					System.out.println(taskHousing.getCrawlerHost());
					taskHousing.setCrawlerHost(taskHousing.getCrawlerHost()+","+element.text());
					System.out.println(taskHousing.getCrawlerHost());
					taskHousingRepository.save(taskHousing);
				}
			}
			System.out.println(taskHousing.getCrawlerHost());
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setWebClient(webClient);
			webParam.setUrl(url4);
			return webParam;
		}
		return null;
	}
	
	//个人信息
	public WebParam<HousingFundTaiZhou2UserInfo> crawlerUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebParam<HousingFundTaiZhou2UserInfo> webParam = new WebParam<HousingFundTaiZhou2UserInfo>();
		String crawlerHost = taskHousing.getCrawlerHost();
		String substring = crawlerHost.substring(1);
		String[] split = substring.split(",");
		HousingFundTaiZhou2UserInfo h = null;
		List<HousingFundTaiZhou2UserInfo> list = new ArrayList<HousingFundTaiZhou2UserInfo>();
		for (int i = 0; i < split.length; i++) {
			h = new HousingFundTaiZhou2UserInfo();
			String url="http://218.90.206.76:8080/jeesite/a/sys/user/base?spaccount="+split[i];
			System.out.println(url);
			WebRequest webRequest2 = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest2);
			System.out.println(page.getWebResponse().getContentAsString());
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementsByTag = doc.getElementsByTag("table");
//			System.out.println(elementsByTag);
			Elements elementsByTag2 = elementsByTag.get(0).getElementsByTag("tr");
			Elements elementsByTag3 = elementsByTag.get(1).getElementsByTag("tr");
			System.out.println(elementsByTag3);
			Element element = elementsByTag2.get(0).getElementsByTag("td").get(1);
//			System.out.println(element);
			h.setCompanyNum(element.text());
			h.setComapny(elementsByTag2.get(0).getElementsByTag("td").get(3).text());
			h.setPersonalNum(elementsByTag2.get(1).getElementsByTag("td").get(1).text());
			h.setName(elementsByTag2.get(1).getElementsByTag("td").get(3).text());
			h.setIDNum(elementsByTag2.get(2).getElementsByTag("td").get(1).text());
			h.setPhone(elementsByTag2.get(2).getElementsByTag("td").get(3).text());
			h.setOpenDate(elementsByTag2.get(3).getElementsByTag("td").get(1).text());
			h.setStatus(elementsByTag2.get(3).getElementsByTag("td").get(3).text());
			h.setAddr(elementsByTag2.get(4).getElementsByTag("td").get(1).text());
			h.setPayDate(elementsByTag3.get(0).getElementsByTag("td").get(1).text());
			h.setBase(elementsByTag3.get(0).getElementsByTag("td").get(3).text());
			h.setComapnyRadio(elementsByTag3.get(1).getElementsByTag("td").get(1).text());
			h.setPersonalRadio(elementsByTag3.get(1).getElementsByTag("td").get(3).text());
			h.setFundMonth(elementsByTag3.get(2).getElementsByTag("td").get(1).text());
			h.setFundFee(elementsByTag3.get(2).getElementsByTag("td").get(3).text());
			h.setBtRadio(elementsByTag3.get(3).getElementsByTag("td").get(1).text());
			h.setBtMonth(elementsByTag3.get(3).getElementsByTag("td").get(3).text());
			h.setBtFee(elementsByTag3.get(4).getElementsByTag("td").get(1).text());
			h.setFee(elementsByTag3.get(4).getElementsByTag("td").get(3).text());
			h.setTaskid(taskHousing.getTaskid());
			list.add(h);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url);
			return webParam;
		}
		return null;
		
	}

	public WebParam<HousingFundTaiZhou2Account> crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebParam<HousingFundTaiZhou2Account> webParam = new WebParam<HousingFundTaiZhou2Account>();
		String crawlerHost = taskHousing.getCrawlerHost();
		String substring = crawlerHost.substring(1);
		String[] split = substring.split(",");
		HousingFundTaiZhou2Account h = null;
		List<HousingFundTaiZhou2Account> list = new ArrayList<HousingFundTaiZhou2Account>();
		for (int i = 0; i < split.length; i++) {
			for (int a = 0; a < 7; a++) {
				String url="http://218.90.206.76:8080/jeesite/a/sys/user/jcnote?pageNo=1&pageSize=30&orderBy=&spcode="+split[i]+"&year="+getDateBefore("yyyy", a);
				System.out.println(url);
				WebRequest webRequest2 = new WebRequest(new URL(url), HttpMethod.GET);
				Page page = webClient.getPage(webRequest2);
				System.out.println(page.getWebResponse().getContentAsString());
				
				if(page.getWebResponse().getContentAsString().contains("职工缴存明细"))
				{
					Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
					
					Elements elementById = doc.getElementById("contentTable").getElementsByTag("tbody").get(0).getElementsByTag("tr");
					for (int j = 1; j < elementById.size(); j++) {
						h = new HousingFundTaiZhou2Account();
						System.out.println(elementById.get(i));
						h.setDatea(elementById.get(j).getElementsByTag("td").get(1).text());
						h.setType(elementById.get(j).getElementsByTag("td").get(2).text());
						h.setPayDate(elementById.get(j).getElementsByTag("td").get(3).text());
						h.setGetFund(elementById.get(j).getElementsByTag("td").get(4).text());
						h.setOutFund(elementById.get(j).getElementsByTag("td").get(5).text());
						h.setFundInterest(elementById.get(j).getElementsByTag("td").get(6).text());
						h.setFundFee(elementById.get(j).getElementsByTag("td").get(7).text());
						h.setGetSubsidy(elementById.get(j).getElementsByTag("td").get(8).text());
						h.setOutSubsidy(elementById.get(j).getElementsByTag("td").get(9).text());
						h.setSubsidyInterest(elementById.get(j).getElementsByTag("td").get(10).text());
						h.setSubsidyFee(elementById.get(j).getElementsByTag("td").get(11).text());
						h.setTaskid(taskHousing.getTaskid());
						list.add(h);
					}
				}
				System.out.println(list);
				webParam.setList(list);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setUrl(url);
			}
			
		}
		
		return webParam;
	}
	
	
	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

}
