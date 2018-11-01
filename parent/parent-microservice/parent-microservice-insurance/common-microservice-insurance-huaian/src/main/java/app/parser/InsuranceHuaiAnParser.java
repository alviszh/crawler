package app.parser;

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
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnEndowment;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnInjury;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnMaternity;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnMedical;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnUnemployment;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
@Component
public class InsuranceHuaiAnParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception{
		String url="http://218.2.15.138:8090/socialSecurity/loginsi.jsp";
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		System.out.println(page.getWebResponse().getContentAsString());
		
		HtmlTextInput id_card = (HtmlTextInput)page.getElementById("iCard");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getUsername());
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='password']");
		id_account.reset();
		id_account.setText(insuranceRequestParameters.getPassword());
		
		HtmlImage img = page.getFirstByXPath("//*[@id='kaptchaImage1']");
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		WebParam webParam = new WebParam();
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='kaptcha']");
		h.setText(verifycode);
		HtmlElement button = page.getFirstByXPath("//*[@id='loginForm']/div[6]/a");
		Page page2 = button.click();
		String url2="http://218.2.15.138:8090/socialSecurity/index.jsp";
		Page page3 = webClient.getPage(url2);
		System.out.println(page3.getWebResponse().getContentAsString());
		if(page3.getWebResponse().getContentAsString().contains("个人编号"))
		{
			webParam.setUrl(url2);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setWebClient(webClient);
			return webParam;
		}
		return null;
	}

	public WebParam<InsuranceHuaiAnUserInfo> crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		String url="http://218.2.15.138:8090/socialSecurity/GrcxAction.do?method=queryJbxx&_="+System.currentTimeMillis();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url);
//		System.out.println(page.getWebResponse().getContentAsString());
		InsuranceHuaiAnUserInfo i = new InsuranceHuaiAnUserInfo();
		WebParam<InsuranceHuaiAnUserInfo> webParam = new WebParam<InsuranceHuaiAnUserInfo>();
		if(page.getWebResponse().getContentAsString().contains("个人基本信息"))
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			i.setNum(getNextLabelByKeyword(doc, "个人编号"));
			i.setName(getNextLabelByKeyword(doc, "姓名"));
			i.setIdNum(getNextLabelByKeyword(doc, "卡号"));
			i.setSex(getNextLabelByKeyword(doc, "性别"));
			i.setBirth(getNextLabelByKeyword(doc, "出生日期"));
			i.setIdCard(getNextLabelByKeyword(doc, "身份证号"));
			i.setCompany(getNextLabelByKeyword(doc, "单位名称"));
			i.setType(getNextLabelByKeyword(doc, "单位类型"));
			i.setTc(getNextLabelByKeyword(doc, "统筹区"));
			i.setFloor(getNextLabelByKeyword(doc, "医疗人员待遇类别"));
			i.setDatea(getNextLabelByKeyword(doc, "参加工作日期"));
			i.setEndDatea(getNextLabelByKeyword(doc, "离退休日期"));
			i.setPhone(getNextLabelByKeyword(doc, "联系电话"));
			i.setAddr(getNextLabelByKeyword(doc, "通讯地址"));
			i.setTaskid(taskInsurance.getTaskid());
			webParam.setUrl(url);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setInsuranceHuaiAnUserInfo(i);
			return webParam;
		}
		return null;
	}

	public WebParam<InsuranceHuaiAnMedical> crawlerMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance,int time) throws Exception {
		String url4="http://218.2.15.138:8090/socialSecurity/GrcxAction.do?method=queryYjsjxx&xz=31&jsq="+getDateBefore("yyyy", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url4);
//		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<InsuranceHuaiAnMedical> webParam = new WebParam<InsuranceHuaiAnMedical>();
		if(page.getWebResponse().getContentAsString().contains("个人应缴实缴明细列表"))
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementsByTag = doc.getElementsByTag("tbody").get(1).getElementsByTag("td");
			System.out.println(elementsByTag);
			InsuranceHuaiAnMedical i1 = null;
			List<InsuranceHuaiAnMedical> list = new ArrayList<InsuranceHuaiAnMedical>();
			for (int j = 0; j < elementsByTag.size(); j=j+14) {
				i1 = new InsuranceHuaiAnMedical();
				Element element = elementsByTag.get(j);
				i1.setCompany(element.text());
				i1.setName(elementsByTag.get(j+1).text());
				i1.setStatus(elementsByTag.get(j+2).text());
				i1.setType(elementsByTag.get(j+3).text());
				i1.setPayType(elementsByTag.get(j+4).text());
				i1.setDatea(elementsByTag.get(j+5).text());
				i1.setActualDate(elementsByTag.get(j+6).text());
				i1.setBase(elementsByTag.get(j+7).text());
				i1.setPayFlag(elementsByTag.get(j+8).text());
				i1.setGetFlag(elementsByTag.get(j+9).text());
				i1.setPersonalPay(elementsByTag.get(j+10).text());
				i1.setCompanyPay(elementsByTag.get(j+11).text());
				i1.setPersonalBill(elementsByTag.get(j+12).text());
				i1.setCompanyBill(elementsByTag.get(j+13).text());
				list.add(i1);
			}
			System.out.println(list);
			webParam.setUrl(url4);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setList(list);
			return webParam;
			
		}
		return null;
	}

	public WebParam<InsuranceHuaiAnEndowment> crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance,int time) throws Exception {
		String url4="http://218.2.15.138:8090/socialSecurity/GrcxAction.do?method=queryYjsjxx&xz=11&jsq="+getDateBefore("yyyy", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url4);
//		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<InsuranceHuaiAnEndowment> webParam = new WebParam<InsuranceHuaiAnEndowment>();
		if(page.getWebResponse().getContentAsString().contains("个人应缴实缴明细列表"))
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementsByTag = doc.getElementsByTag("tbody").get(1).getElementsByTag("td");
			System.out.println(elementsByTag);
			InsuranceHuaiAnEndowment i1 = null;
			List<InsuranceHuaiAnEndowment> list = new ArrayList<InsuranceHuaiAnEndowment>();
			for (int j = 0; j < elementsByTag.size(); j=j+14) {
				i1 = new InsuranceHuaiAnEndowment();
				Element element = elementsByTag.get(j);
				i1.setCompany(element.text());
				i1.setName(elementsByTag.get(j+1).text());
				i1.setStatus(elementsByTag.get(j+2).text());
				i1.setType(elementsByTag.get(j+3).text());
				i1.setPayType(elementsByTag.get(j+4).text());
				i1.setDatea(elementsByTag.get(j+5).text());
				i1.setActualDate(elementsByTag.get(j+6).text());
				i1.setBase(elementsByTag.get(j+7).text());
				i1.setPayFlag(elementsByTag.get(j+8).text());
				i1.setGetFlag(elementsByTag.get(j+9).text());
				i1.setPersonalPay(elementsByTag.get(j+10).text());
				i1.setCompanyPay(elementsByTag.get(j+11).text());
				i1.setPersonalBill(elementsByTag.get(j+12).text());
				i1.setCompanyBill(elementsByTag.get(j+13).text());
				list.add(i1);
			}
			System.out.println(list);
			webParam.setUrl(url4);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setList(list);
			return webParam;
			
		}
		return null;
	}

	
	  /**
     * @param document
     * @param keyword
     * @return
     * @Des 获取目标标签的下一个兄弟标签的内容
     */
    public static String getNextLabelByKeyword(Document document, String keyword) {
        Elements es = document.select("label:contains(" + keyword + ")");
        if (null != es && es.size() > 0) {
            Element element = es.first();
            Element nextElement = element.nextElementSibling();
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

	public WebParam<InsuranceHuaiAnInjury> crawlerInjury(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance, int time) throws Exception {
		String url4="http://218.2.15.138:8090/socialSecurity/GrcxAction.do?method=queryYjsjxx&xz=41&jsq="+getDateBefore("yyyy", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url4);
//		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<InsuranceHuaiAnInjury> webParam = new WebParam<InsuranceHuaiAnInjury>();
		if(page.getWebResponse().getContentAsString().contains("个人应缴实缴明细列表"))
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementsByTag = doc.getElementsByTag("tbody").get(1).getElementsByTag("td");
			System.out.println(elementsByTag);
			InsuranceHuaiAnInjury i1 = null;
			List<InsuranceHuaiAnInjury> list = new ArrayList<InsuranceHuaiAnInjury>();
			for (int j = 0; j < elementsByTag.size(); j=j+14) {
				i1 = new InsuranceHuaiAnInjury();
				Element element = elementsByTag.get(j);
				i1.setCompany(element.text());
				i1.setName(elementsByTag.get(j+1).text());
				i1.setStatus(elementsByTag.get(j+2).text());
				i1.setType(elementsByTag.get(j+3).text());
				i1.setPayType(elementsByTag.get(j+4).text());
				i1.setDatea(elementsByTag.get(j+5).text());
				i1.setActualDate(elementsByTag.get(j+6).text());
				i1.setBase(elementsByTag.get(j+7).text());
				i1.setPayFlag(elementsByTag.get(j+8).text());
				i1.setGetFlag(elementsByTag.get(j+9).text());
				i1.setPersonalPay(elementsByTag.get(j+10).text());
				i1.setCompanyPay(elementsByTag.get(j+11).text());
				i1.setPersonalBill(elementsByTag.get(j+12).text());
				i1.setCompanyBill(elementsByTag.get(j+13).text());
				list.add(i1);
			}
			System.out.println(list);
			webParam.setUrl(url4);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setList(list);
			return webParam;
			
		}
		return null;
	}

	public WebParam<InsuranceHuaiAnUnemployment> crawlerUnemployment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance, int time) throws Exception {
		String url4="http://218.2.15.138:8090/socialSecurity/GrcxAction.do?method=queryYjsjxx&xz=21&jsq="+getDateBefore("yyyy", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url4);
//		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<InsuranceHuaiAnUnemployment> webParam = new WebParam<InsuranceHuaiAnUnemployment>();
		if(page.getWebResponse().getContentAsString().contains("个人应缴实缴明细列表"))
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementsByTag = doc.getElementsByTag("tbody").get(1).getElementsByTag("td");
			System.out.println(elementsByTag);
			InsuranceHuaiAnUnemployment i1 = null;
			List<InsuranceHuaiAnUnemployment> list = new ArrayList<InsuranceHuaiAnUnemployment>();
			for (int j = 0; j < elementsByTag.size(); j=j+14) {
				i1 = new InsuranceHuaiAnUnemployment();
				Element element = elementsByTag.get(j);
				i1.setCompany(element.text());
				i1.setName(elementsByTag.get(j+1).text());
				i1.setStatus(elementsByTag.get(j+2).text());
				i1.setType(elementsByTag.get(j+3).text());
				i1.setPayType(elementsByTag.get(j+4).text());
				i1.setDatea(elementsByTag.get(j+5).text());
				i1.setActualDate(elementsByTag.get(j+6).text());
				i1.setBase(elementsByTag.get(j+7).text());
				i1.setPayFlag(elementsByTag.get(j+8).text());
				i1.setGetFlag(elementsByTag.get(j+9).text());
				i1.setPersonalPay(elementsByTag.get(j+10).text());
				i1.setCompanyPay(elementsByTag.get(j+11).text());
				i1.setPersonalBill(elementsByTag.get(j+12).text());
				i1.setCompanyBill(elementsByTag.get(j+13).text());
				list.add(i1);
			}
			System.out.println(list);
			webParam.setUrl(url4);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setList(list);
			return webParam;
			
		}
		return null;
	}

	public WebParam<InsuranceHuaiAnMaternity> crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance, int time) throws Exception {
		String url4="http://218.2.15.138:8090/socialSecurity/GrcxAction.do?method=queryYjsjxx&xz=51&jsq="+getDateBefore("yyyy", time);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url4);
//		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<InsuranceHuaiAnMaternity> webParam = new WebParam<InsuranceHuaiAnMaternity>();
		if(page.getWebResponse().getContentAsString().contains("个人应缴实缴明细列表"))
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements elementsByTag = doc.getElementsByTag("tbody").get(1).getElementsByTag("td");
			System.out.println(elementsByTag);
			InsuranceHuaiAnMaternity i1 = null;
			List<InsuranceHuaiAnMaternity> list = new ArrayList<InsuranceHuaiAnMaternity>();
			for (int j = 0; j < elementsByTag.size(); j=j+14) {
				i1 = new InsuranceHuaiAnMaternity();
				Element element = elementsByTag.get(j);
				i1.setCompany(element.text());
				i1.setName(elementsByTag.get(j+1).text());
				i1.setStatus(elementsByTag.get(j+2).text());
				i1.setType(elementsByTag.get(j+3).text());
				i1.setPayType(elementsByTag.get(j+4).text());
				i1.setDatea(elementsByTag.get(j+5).text());
				i1.setActualDate(elementsByTag.get(j+6).text());
				i1.setBase(elementsByTag.get(j+7).text());
				i1.setPayFlag(elementsByTag.get(j+8).text());
				i1.setGetFlag(elementsByTag.get(j+9).text());
				i1.setPersonalPay(elementsByTag.get(j+10).text());
				i1.setCompanyPay(elementsByTag.get(j+11).text());
				i1.setPersonalBill(elementsByTag.get(j+12).text());
				i1.setCompanyBill(elementsByTag.get(j+13).text());
				list.add(i1);
			}
			System.out.println(list);
			webParam.setUrl(url4);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setList(list);
			return webParam;
			
		}
		return null;
	}

}
