package app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.luzhou.InsuranceLuZhouEndowment;
import com.microservice.dao.entity.crawler.insurance.luzhou.InsuranceLuZhouMedical;
import com.microservice.dao.entity.crawler.insurance.luzhou.InsuranceLuZhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
@Component
public class InsuranceLuZhouParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	//登陆
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://www.sclzsi.cn/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='username']");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getUsername());
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='password']");
		id_account.reset();
		id_account.setText(insuranceRequestParameters.getPassword());
		
		HtmlImage img = page.getFirstByXPath("//*[@id='randImg']");
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='codes']");
		h.setText(verifycode);
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='loginBody']/div/div[2]/div[3]/div[5]/input");
		Page page2 = firstByXPath.click();
		Page page3 = webClient.getPage("http://www.sclzsi.cn/sys/index.do");
		WebParam webParam = new WebParam();
		if(page3.getWebResponse().getContentAsString().contains("欢迎进入泸州社保网上服务大厅"))
		{
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			return webParam;
		}
		return null;
	}

	//个人信息
	public WebParam<InsuranceLuZhouUserInfo> getUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://www.sclzsi.cn/user/center.do";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page4 = webClient.getPage(url);
		System.out.println(page4.getWebResponse().getContentAsString());
		WebParam<InsuranceLuZhouUserInfo> webParam = new WebParam<InsuranceLuZhouUserInfo>();
		if(page4.getWebResponse().getContentAsString().contains("个人社保信息"))
		{
			Document doc = Jsoup.parse(page4.getWebResponse().getContentAsString());
			Elements elementsByClass = doc.getElementsByClass("detail").get(0).getElementsByClass("item");
			InsuranceLuZhouUserInfo in = new InsuranceLuZhouUserInfo();
			in.setName(elementsByClass.get(0).text());
			in.setIDNum(elementsByClass.get(1).text());
			in.setPhone(elementsByClass.get(2).text());
			
			Elements elementsByClass1 = doc.getElementsByClass("detail").get(0).getElementsByClass("item-info");
			in.setCompany(elementsByClass1.get(0).text());
			in.setCanbaoCompany(elementsByClass1.get(1).text());
			in.setEndowment(elementsByClass1.get(2).text());
			in.setUnemployment(elementsByClass1.get(3).text());
			in.setMedical(elementsByClass1.get(4).text());
			in.setAddMedical(elementsByClass1.get(5).text());
			in.setInjury(elementsByClass1.get(6).text());
			in.setMaternity(elementsByClass1.get(7).text());
			in.setTaskid(taskInsurance.getTaskid());
			System.out.println(in);
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setInsuranceLuZhouUserInfo(in);
			webParam.setUrl(url);
			return webParam;
		}
		return null;
	}

	//医疗
	public WebParam<InsuranceLuZhouMedical> getMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://www.sclzsi.cn/query/ml/lnjfqk.do";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page4 = webClient.getPage(url);
		System.out.println(page4.getWebResponse().getContentAsString());	
		WebParam<InsuranceLuZhouMedical> webParam = new WebParam<InsuranceLuZhouMedical>();
		if(page4.getWebResponse().getContentAsString().contains("个人账户余额"))
		{
			Document doc = Jsoup.parse(page4.getWebResponse().getContentAsString());
			Elements elementsByClass = doc.getElementsByClass("row-bottom");
			List<InsuranceLuZhouMedical> list = new ArrayList<InsuranceLuZhouMedical>();
			InsuranceLuZhouMedical in = null;
			for (int i = 1; i < elementsByClass.size(); i++) {
				Elements element = elementsByClass.get(i).getElementsByTag("span");
					in = new InsuranceLuZhouMedical();
					in.setTime(element.get(1).text());
					in.setMonth(element.get(2).text());
					in.setMoney(element.get(3).text());
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
			}
//			System.out.println(list);
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setList(list);
			return webParam;
		}		
		return null;
	}

	//养老
	public WebParam<InsuranceLuZhouEndowment> getEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://www.sclzsi.cn/query/inquire.do";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page4 = webClient.getPage(url);
		System.out.println(page4.getWebResponse().getContentAsString());	
		WebParam<InsuranceLuZhouEndowment> webParam = new WebParam<InsuranceLuZhouEndowment>();
		if(page4.getWebResponse().getContentAsString().contains("个人账户查询"))
		{
			Document doc = Jsoup.parse(page4.getWebResponse().getContentAsString());
			Elements elementsByClass = doc.getElementsByClass("row-bottom");
			List<InsuranceLuZhouEndowment> list = new ArrayList<InsuranceLuZhouEndowment>();
			InsuranceLuZhouEndowment in = null;
			for (int i = 1; i < elementsByClass.size(); i++) {
				Elements element = elementsByClass.get(i).getElementsByTag("span");
					in = new InsuranceLuZhouEndowment();
					in.setTime(element.get(1).text());
					in.setMonth(element.get(2).text());
					in.setMoney(element.get(3).text());
					in.setFee(element.get(4).text());
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
			}
//			System.out.println(list);
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setList(list);
			return webParam;
		}		
		return null;
	}

}
