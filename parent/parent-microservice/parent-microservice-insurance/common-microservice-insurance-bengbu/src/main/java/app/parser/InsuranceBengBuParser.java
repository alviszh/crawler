package app.parser;

import java.net.URL;
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
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuEndowment;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuInjury;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuMaternity;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuMedical;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuUnemployment;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class InsuranceBengBuParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://218.22.88.62:8000/wsbsbb/person/personSignUp/login.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='aac003']");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getName());
		
		HtmlTextInput pwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='aac002']");
		pwd.reset();
		pwd.setText(insuranceRequestParameters.getUsername());
		
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='aae099']");
		id_account.reset();
		id_account.setText(insuranceRequestParameters.getPassword());
		
		HtmlImage img = page.getFirstByXPath("//*[@id='image']");
		
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='verify']");
		
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		h.setText(verifycode);
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='loginimg']/img");
		Page page2 = firstByXPath.click();
		String url2="http://218.22.88.62:8000/wsbsbb/person/main.html";
		
		WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest);
//		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		if(page3.getWebResponse().getContentAsString().contains("姓名"))
		{
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setWebClient(webClient);
			webParam.setUrl(url2);
			return webParam;
		}		
		return null;
	}
	
	
	//个人信息
	public WebParam<InsuranceBengBuUserInfo> crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url3="http://218.22.88.62:8000/wsbsbb/person/myAccount.action";
		WebRequest webRequest = new WebRequest(new URL(url3), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest);
//		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam<InsuranceBengBuUserInfo> webParam  =new WebParam<InsuranceBengBuUserInfo>();
		InsuranceBengBuUserInfo i = new InsuranceBengBuUserInfo();
		if(page3.getWebResponse().getContentAsString().contains("个人基本信息"))
		{
			Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
			Elements elementById = doc.getElementById("form1").getElementsByTag("tr");
			String val = elementById.get(0).getElementsByTag("td").get(1).getElementsByTag("input").val();
//			System.out.println(val);
			
			i.setPersonalNum(elementById.get(0).getElementsByTag("td").get(1).getElementsByTag("input").val());
			i.setCardNum(elementById.get(0).getElementsByTag("td").get(3).getElementsByTag("input").val());
			i.setCompanyNum(elementById.get(1).getElementsByTag("td").get(1).getElementsByTag("input").val());
			i.setIDNum(elementById.get(1).getElementsByTag("td").get(3).getElementsByTag("input").val());
			i.setName(elementById.get(2).getElementsByTag("td").get(1).getElementsByTag("input").val());
			i.setSex(elementById.get(2).getElementsByTag("td").get(3).getElementsByTag("input").val());
			i.setNational(elementById.get(3).getElementsByTag("td").get(1).getElementsByTag("input").val());
			i.setBirth(elementById.get(3).getElementsByTag("td").get(3).getElementsByTag("input").val());
			i.setDatea(elementById.get(4).getElementsByTag("td").get(1).getElementsByTag("input").val());
			i.setStatus(elementById.get(4).getElementsByTag("td").get(3).getElementsByTag("input").val());
			i.setConnectPerson(elementById.get(5).getElementsByTag("td").get(1).getElementsByTag("input").val());
			i.setCode(elementById.get(5).getElementsByTag("td").get(3).getElementsByTag("input").val());
			i.setPhone(elementById.get(6).getElementsByTag("td").get(1).getElementsByTag("input").val());
			i.setAddr(elementById.get(6).getElementsByTag("td").get(3).getElementsByTag("input").val());
			i.setTaskid(taskInsurance.getTaskid());
			System.out.println(i);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setInsuranceBengBuUserInfo(i);
			webParam.setUrl(url3);
			return webParam;
		}			
		return null;
	}

	//医疗
	public WebParam<InsuranceBengBuMedical> crawlerMedical(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url3="http://218.22.88.62:8000/wsbsbb/person/personSbjf/personSbjfListQuery.action?aae140=310&pageIndex=0&pageSize=1000";
		WebRequest webRequest = new WebRequest(new URL(url3), HttpMethod.POST);
		Page page3 = webClient.getPage(webRequest);
//		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam<InsuranceBengBuMedical> webParam  =new WebParam<InsuranceBengBuMedical>();
		InsuranceBengBuMedical in =null;
		if(page3.getWebResponse().getContentAsString().contains("aae037"))
		{
			JSONObject fromObject = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
//			System.out.println(fromObject);
			String string = fromObject.getString("data");
//			System.out.println(string);
			JSONArray fromObject2 = JSONArray.fromObject(string);
			List<InsuranceBengBuMedical> list =new ArrayList<InsuranceBengBuMedical>();
			for (int i = 0; i < fromObject2.size(); i++) {
//				System.out.println(fromObject2.get(i));
				in =new InsuranceBengBuMedical();
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
				in.setDatea(fromObject3.getString("aae037"));
				in.setInDate(fromObject3.getString("aae037"));
				in.setPersonalMoney(fromObject3.getString("aae082"));
				in.setCompanyMoney(fromObject3.getString("aae081"));
				in.setBase(fromObject3.getString("aac150"));
				in.setFlag(fromObject3.getString("aae925"));
				in.setSumMoney(fromObject3.getString("aae080"));
				in.setType(fromObject3.getString("aae140mc"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url3);
			return webParam;
		}			
		return null;
	}


	//养老
	public WebParam<InsuranceBengBuEndowment> crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url3="http://218.22.88.62:8000/wsbsbb/person/personSbjf/personSbjfListQuery.action?aae140=110&pageIndex=0&pageSize=1000";
		WebRequest webRequest = new WebRequest(new URL(url3), HttpMethod.POST);
		Page page3 = webClient.getPage(webRequest);
//		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam<InsuranceBengBuEndowment> webParam  =new WebParam<InsuranceBengBuEndowment>();
		InsuranceBengBuEndowment in =null;
		if(page3.getWebResponse().getContentAsString().contains("aae037"))
		{
			JSONObject fromObject = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
//			System.out.println(fromObject);
			String string = fromObject.getString("data");
//			System.out.println(string);
			JSONArray fromObject2 = JSONArray.fromObject(string);
			List<InsuranceBengBuEndowment> list =new ArrayList<InsuranceBengBuEndowment>();
			for (int i = 0; i < fromObject2.size(); i++) {
//				System.out.println(fromObject2.get(i));
				in =new InsuranceBengBuEndowment();
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
				in.setDatea(fromObject3.getString("aae037"));
				in.setInDate(fromObject3.getString("aae037"));
				in.setPersonalMoney(fromObject3.getString("aae082"));
				in.setCompanyMoney(fromObject3.getString("aae081"));
				in.setBase(fromObject3.getString("aac150"));
				in.setFlag(fromObject3.getString("aae925"));
				in.setSumMoney(fromObject3.getString("aae080"));
				in.setType(fromObject3.getString("aae140mc"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url3);
			return webParam;
		}			
		return null;
	}

	//失业
	public WebParam<InsuranceBengBuUnemployment> crawlerUnemployment(
			InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url3="http://218.22.88.62:8000/wsbsbb/person/personSbjf/personSbjfListQuery.action?aae140=210&pageIndex=0&pageSize=1000";
		WebRequest webRequest = new WebRequest(new URL(url3), HttpMethod.POST);
		Page page3 = webClient.getPage(webRequest);
//		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam<InsuranceBengBuUnemployment> webParam  =new WebParam<InsuranceBengBuUnemployment>();
		InsuranceBengBuUnemployment in =null;
		if(page3.getWebResponse().getContentAsString().contains("aae037"))
		{
			JSONObject fromObject = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
//			System.out.println(fromObject);
			String string = fromObject.getString("data");
//			System.out.println(string);
			JSONArray fromObject2 = JSONArray.fromObject(string);
			List<InsuranceBengBuUnemployment> list =new ArrayList<InsuranceBengBuUnemployment>();
			for (int i = 0; i < fromObject2.size(); i++) {
//				System.out.println(fromObject2.get(i));
				in =new InsuranceBengBuUnemployment();
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
				in.setDatea(fromObject3.getString("aae037"));
				in.setInDate(fromObject3.getString("aae037"));
				in.setPersonalMoney(fromObject3.getString("aae082"));
				in.setCompanyMoney(fromObject3.getString("aae081"));
				in.setBase(fromObject3.getString("aac150"));
				in.setFlag(fromObject3.getString("aae925"));
				in.setSumMoney(fromObject3.getString("aae080"));
				in.setType(fromObject3.getString("aae140mc"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url3);
			return webParam;
		}			
		return null;
	}


	//工伤
	public WebParam<InsuranceBengBuInjury> crawlerInjury(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url3="http://218.22.88.62:8000/wsbsbb/person/personSbjf/personSbjfListQuery.action?aae140=410&pageIndex=0&pageSize=1000";
		WebRequest webRequest = new WebRequest(new URL(url3), HttpMethod.POST);
		Page page3 = webClient.getPage(webRequest);
//		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam<InsuranceBengBuInjury> webParam  =new WebParam<InsuranceBengBuInjury>();
		InsuranceBengBuInjury in =null;
		if(page3.getWebResponse().getContentAsString().contains("aae037"))
		{
			JSONObject fromObject = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
//			System.out.println(fromObject);
			String string = fromObject.getString("data");
//			System.out.println(string);
			JSONArray fromObject2 = JSONArray.fromObject(string);
			List<InsuranceBengBuInjury> list =new ArrayList<InsuranceBengBuInjury>();
			for (int i = 0; i < fromObject2.size(); i++) {
//				System.out.println(fromObject2.get(i));
				in =new InsuranceBengBuInjury();
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
				in.setDatea(fromObject3.getString("aae037"));
				in.setInDate(fromObject3.getString("aae037"));
				in.setPersonalMoney(fromObject3.getString("aae082"));
				in.setCompanyMoney(fromObject3.getString("aae081"));
				in.setBase(fromObject3.getString("aac150"));
				in.setFlag(fromObject3.getString("aae925"));
				in.setSumMoney(fromObject3.getString("aae080"));
				in.setType(fromObject3.getString("aae140mc"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url3);
			return webParam;
		}			
		return null;
	}


	//生育
	public WebParam<InsuranceBengBuMaternity> crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url3="http://218.22.88.62:8000/wsbsbb/person/personSbjf/personSbjfListQuery.action?aae140=510&pageIndex=0&pageSize=1000";
		WebRequest webRequest = new WebRequest(new URL(url3), HttpMethod.POST);
		Page page3 = webClient.getPage(webRequest);
//		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam<InsuranceBengBuMaternity> webParam  =new WebParam<InsuranceBengBuMaternity>();
		InsuranceBengBuMaternity in =null;
		if(page3.getWebResponse().getContentAsString().contains("aae037"))
		{
			JSONObject fromObject = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
//			System.out.println(fromObject);
			String string = fromObject.getString("data");
//			System.out.println(string);
			JSONArray fromObject2 = JSONArray.fromObject(string);
			List<InsuranceBengBuMaternity> list =new ArrayList<InsuranceBengBuMaternity>();
			for (int i = 0; i < fromObject2.size(); i++) {
//				System.out.println(fromObject2.get(i));
				in =new InsuranceBengBuMaternity();
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
				in.setDatea(fromObject3.getString("aae037"));
				in.setInDate(fromObject3.getString("aae037"));
				in.setPersonalMoney(fromObject3.getString("aae082"));
				in.setCompanyMoney(fromObject3.getString("aae081"));
				in.setBase(fromObject3.getString("aac150"));
				in.setFlag(fromObject3.getString("aae925"));
				in.setSumMoney(fromObject3.getString("aae080"));
				in.setType(fromObject3.getString("aae140mc"));
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url3);
			return webParam;
		}			
		return null;
	}

}
