package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanEndowment;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanMedical;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanUnemployment;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
@Component
public class InsuranceShaoGuanParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://www.e12345.gov.cn/portal/siportal/query/person_login.jsp";

				
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='gr_loginname']");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getUsername());
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='gr_password']");
		id_account.reset();
		id_account.setText(insuranceRequestParameters.getPassword());
		
		HtmlImage img = page.getFirstByXPath("//*[@id='gr_login_image']");
		
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='gr_login_code']");
		h.setText(verifycode);
		
		HtmlElement firstByXPath = page.getFirstByXPath("/html/body/table/tbody/tr[3]/td/table/tbody/tr[3]/td/table/tbody/tr[5]/td/div/input");
		HtmlPage page2 = firstByXPath.click();	
//		System.out.println(page2.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		if(page2.getWebResponse().getContentAsString().contains("网上办事大厅"))
		{
	         webParam.setHtml(page2.getWebResponse().getContentAsString());
	         webParam.setUrl(url);
	         webParam.setWebClient(webClient);
	         return webParam;
		}
		return null;
	}
	
	
	public WebParam<InsuranceShaoGuanUserInfo> getUserInfo(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url2="http://www.e12345.gov.cn/portal/siportal/query/person_jbxx.jsp";
		WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest);
//		System.out.println(page3.getWebResponse().getContentAsString());
		Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
//		System.out.println(doc.getElementsByTag("table").get(8).getElementsByTag("td"));
		String elementsByClass1 = doc.getElementsByTag("table").get(8).getElementsByTag("td").text();
		int indexOf3 = elementsByClass1.indexOf("/");
		int indexOf4 = elementsByClass1.indexOf("第");
		String substring3 = elementsByClass1.substring(indexOf3, indexOf4);
		String[] split = substring3.split("共");
		String[] substring4 = split[1].split("页");
		System.out.println(substring4[0]);
		
		Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        Matcher isNum = pattern.matcher(substring4[0].trim());
        if (!isNum.matches()) {
//            System.out.println("false");
            taskInsurance.setCrawlerHost("false");
            taskInsuranceRepository.save(taskInsurance);
        }
        else
        {
//            System.out.println(substring4[0]);
            taskInsurance.setCrawlerHost(substring4[0].trim());
            taskInsuranceRepository.save(taskInsurance);
        }
		WebParam<InsuranceShaoGuanUserInfo> webParam = new WebParam<InsuranceShaoGuanUserInfo>();
		if(page3.getWebResponse().getContentAsString().contains("参保信息查询"))
		{
//			Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
			Element elementsByClass = doc.getElementsByClass("tableblue").get(0).getElementsByTag("tbody").get(0);
			InsuranceShaoGuanUserInfo i = new InsuranceShaoGuanUserInfo();
			i.setCompany(elementsByClass.getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text());
			i.setIDNum(elementsByClass.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text());
			i.setName(elementsByClass.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text());
			i.setStatus(elementsByClass.getElementsByTag("tr").get(3).getElementsByTag("td").get(1).text());
			i.setMoneyType(elementsByClass.getElementsByTag("tr").get(4).getElementsByTag("td").get(1).text());
			i.setJoinDate(elementsByClass.getElementsByTag("tr").get(5).getElementsByTag("td").get(1).text());
			i.setYongGong(elementsByClass.getElementsByTag("tr").get(6).getElementsByTag("td").get(1).text());
			i.setPersonal(elementsByClass.getElementsByTag("tr").get(7).getElementsByTag("td").get(1).text());
			i.setLastThree(elementsByClass.getElementsByTag("tr").get(8).getElementsByTag("td").get(1).text());
			i.setSf(elementsByClass.getElementsByTag("tr").get(9).getElementsByTag("td").get(1).text());
			i.setNetPoint(elementsByClass.getElementsByTag("tr").get(10).getElementsByTag("td").get(1).text());
			i.setPhone(elementsByClass.getElementsByTag("tr").get(11).getElementsByTag("td").get(1).text());
			i.setAddr(elementsByClass.getElementsByTag("tr").get(12).getElementsByTag("td").get(1).text());
			i.setTaskid(taskInsurance.getTaskid());
//			System.out.println(i);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setInsuranceShaoGuanUserInfo(i);
			webParam.setUrl(url2);
			return webParam;
		}
		return null;
	}


	public WebParam<InsuranceShaoGuanEndowment> getEndowment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance,int time) throws Exception {
		
		String url="http://www.e12345.gov.cn/portal/siportal/query/person_jfls.jsp?PageNo="+time;
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		WebParam<InsuranceShaoGuanEndowment> webParam = new WebParam<InsuranceShaoGuanEndowment>();
		if(page.getWebResponse().getContentAsString().contains("养老参保历史情况查询"))
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			InsuranceShaoGuanEndowment in = null;
			List<InsuranceShaoGuanEndowment> list = new ArrayList<InsuranceShaoGuanEndowment>();
			Elements elementsByTag = doc.getElementsByClass("tableblue").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
			for (int i = 1; i < elementsByTag.size(); i++) {
				in = new InsuranceShaoGuanEndowment();
//				System.out.println(elementsByTag.get(i).getElementsByTag("td").text());
				in.setCompanyNum(elementsByTag.get(i).getElementsByTag("td").get(0).text());
				in.setCompany(elementsByTag.get(i).getElementsByTag("td").get(1).text());
				in.setDatea(elementsByTag.get(i).getElementsByTag("td").get(2).text());
				in.setSf(elementsByTag.get(i).getElementsByTag("td").get(3).text());
				in.setBase(elementsByTag.get(i).getElementsByTag("td").get(4).text());
				in.setCompanyRatio(elementsByTag.get(i).getElementsByTag("td").get(5).text());
				in.setCompanyPay(elementsByTag.get(i).getElementsByTag("td").get(6).text());
				in.setPersonalRatio(elementsByTag.get(i).getElementsByTag("td").get(7).text());
				in.setPersoanlPay(elementsByTag.get(i).getElementsByTag("td").get(8).text());
				in.setCompanySend(elementsByTag.get(i).getElementsByTag("td").get(9).text());
				in.setPaySum(elementsByTag.get(i).getElementsByTag("td").get(10).text());
				in.setSendDate(elementsByTag.get(i).getElementsByTag("td").get(11).text());
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


	public WebParam<InsuranceShaoGuanUnemployment> getUnemployment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url2="http://www.e12345.gov.cn/portal/siportal/query/person_jbxx.jsp";
		WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest);
		if(page3.getWebResponse().getContentAsString().contains("个人缴费历史"))
		{
			Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
			InsuranceShaoGuanUnemployment in = null;
			Elements elementsByClass = doc.getElementsByClass("tableblue").get(2).getElementsByTag("tbody").get(0).getElementsByTag("tr");
			List<InsuranceShaoGuanUnemployment> list = new ArrayList<InsuranceShaoGuanUnemployment>();
			WebParam<InsuranceShaoGuanUnemployment> webParam = new WebParam<InsuranceShaoGuanUnemployment>();
			for (int i = 2; i < elementsByClass.size(); i++) {
//				System.out.println(elementsByClass.get(i).getElementsByTag("td"));
				Elements elementsByTag = elementsByClass.get(i).getElementsByTag("td");
//					System.out.println(elementsByTag.get(j).text());
					in = new InsuranceShaoGuanUnemployment();
					in.setCompanyNum(elementsByTag.get(0).text());
					in.setMonth(elementsByTag.get(1).text());
					in.setPaySum(elementsByTag.get(2).text());
					in.setCompanyPay(elementsByTag.get(3).text());
					in.setPersonalPay(elementsByTag.get(4).text());
					in.setEndowmentSum(elementsByTag.get(11).text());
					in.setEndowmentCompany(elementsByTag.get(12).text());
					in.setEndowmentPersonal(elementsByTag.get(13).text());
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
			}
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(url2);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}


	public WebParam<InsuranceShaoGuanMedical> getMedical(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance,int time) throws Exception {
		String url="http://www.e12345.gov.cn/portal/siportal/query/person_ybjfls.jsp?PageNo="+time;
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		WebParam<InsuranceShaoGuanMedical> webParam = new WebParam<InsuranceShaoGuanMedical>();
		if(page.getWebResponse().getContentAsString().contains("医疗参保历史情况查询"))
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			InsuranceShaoGuanMedical in = null;
			List<InsuranceShaoGuanMedical> list = new ArrayList<InsuranceShaoGuanMedical>();
			Elements elementsByTag = doc.getElementsByClass("tableblue").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
			for (int i = 1; i < elementsByTag.size(); i++) {
				in = new InsuranceShaoGuanMedical();
//				System.out.println(elementsByTag.get(i).getElementsByTag("td").text());
				in.setCompanyNum(elementsByTag.get(i).getElementsByTag("td").get(0).text());
				in.setCompany(elementsByTag.get(i).getElementsByTag("td").get(1).text());
				in.setDatea(elementsByTag.get(i).getElementsByTag("td").get(2).text());
				in.setSf(elementsByTag.get(i).getElementsByTag("td").get(3).text());
				in.setBase(elementsByTag.get(i).getElementsByTag("td").get(4).text());
				in.setCompanyRatio(elementsByTag.get(i).getElementsByTag("td").get(5).text());
				in.setCompanyPay(elementsByTag.get(i).getElementsByTag("td").get(6).text());
				in.setPersonalRatio(elementsByTag.get(i).getElementsByTag("td").get(7).text());
				in.setPersoanlPay(elementsByTag.get(i).getElementsByTag("td").get(8).text());
				in.setCompanySend(elementsByTag.get(i).getElementsByTag("td").get(9).text());
				in.setPaySum(elementsByTag.get(i).getElementsByTag("td").get(10).text());
				in.setSendDate(elementsByTag.get(i).getElementsByTag("td").get(11).text());
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

}
