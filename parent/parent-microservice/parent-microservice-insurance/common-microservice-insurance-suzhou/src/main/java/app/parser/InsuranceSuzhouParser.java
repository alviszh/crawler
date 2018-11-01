package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouBirth;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouEnterprisePension;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouTownsWorkers;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouUser;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouWorkInjury;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;
import net.sf.json.JSONObject;

@Component
public class InsuranceSuzhouParser {
	
	
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	

	public WebParam login(InsuranceRequestParameters insuranceRequestParameters,int i) throws Exception  {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		tracer.addTag("taskIdNum", taskInsurance.toString());
		String idnum = taskInsurance.getBasicUserInsurance().getIdnum();
		String url = "http://www.szsbzx.net.cn:9900/web/website/bsdt.parser";
					  //改版后 http://szsbzx.jsszhrss.gov.cn:9900/web/website/bsdt.action	
		try {
			WebParam webParam= new WebParam();
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setTimeout(30000);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);		
			HtmlPage searchPage = webClient.getPage(webRequest);		
			int status = searchPage.getWebResponse().getStatusCode();
			if(200 == status){
				HtmlImage image = searchPage.getFirstByXPath("//img[@id='randpic']");
				String code = chaoJiYingOcrService.getVerifycode(image, "4006");
				HtmlTextInput username = (HtmlTextInput)searchPage.getFirstByXPath("//input[@id='grbh']"); 
				HtmlTextInput password = (HtmlTextInput)searchPage.getFirstByXPath("//input[@id='sfzh']");
				HtmlTextInput verifyCode = (HtmlTextInput)searchPage.getFirstByXPath("//input[@id='yzcode']");
				HtmlElement button = (HtmlElement)searchPage.getFirstByXPath("//img[@onclick='subminLogin()']");			
				username.setText(insuranceRequestParameters.getUsername());
				password.setText(idnum);
				verifyCode.setText(code);
				searchPage = button.click();
				String alertMsg = WebCrawler.getAlertMsg();
				webParam.setCode(searchPage.getWebResponse().getStatusCode());
				webParam.setPage(searchPage);
				return webParam;		
			}	
		} catch (Exception e) {
			if(i<3){
				i++;
				login(insuranceRequestParameters,i);
			}else{
				tracer.addTag("loginError", "网络异常，暂时无法登陆。");
				return null;
				
			}
		}
			
		return null;
	}


	//解析企业养老网页信息
	public WebParam getEnterprisePensionInfo(TaskInsurance taskInsurance, Set<Cookie> cookies,int i) throws Exception{
		WebParam webParam= new WebParam();	
		String url = "http://www.szsbzx.net.cn:9900/web/website/personQuery/personQueryAction?frameControlSubmitFunction=getPagesAjax&xz=2&pageIndex=1&nu=1&pageCount=300";
		WebClient webClient = insuranceService.getWebClient(cookies);
		List<Page> pageList = new ArrayList<Page>();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept","*/*");
		webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection","keep-alive");
		webRequest.setAdditionalHeader("Host","szsbzx.jsszhrss.gov.cn:9900");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
		webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
		webRequest.setAdditionalHeader("Referer", "http://szsbzx.jsszhrss.gov.cn:9900/web/website/personQuery/personQueryAction.parser");
		webRequest.setAdditionalHeader("Origin","http://szsbzx.jsszhrss.gov.cn:9900");
		
		/*webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest.getRequestParameters().add(new NameValuePair("xz", "2"));
		webRequest.getRequestParameters().add(new NameValuePair("pageIndex", "1"));
		webRequest.getRequestParameters().add(new NameValuePair("nu", "1"));
		webRequest.getRequestParameters().add(new NameValuePair("pageCount", "300"));
		*/
		try {
			Page page = webClient.getPage(webRequest);
			
			int statusCode = page.getWebResponse().getStatusCode();
			List<InsuranceSuzhouEnterprisePension> infoList  = new ArrayList<InsuranceSuzhouEnterprisePension>();
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceSuzhouiParser.getEnterprisePensionInfo 企业养老信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				pageList.add(page);
				infoList= htmlParseEnterprisePension(page,taskInsurance,infoList);
				if(null != infoList){
					webParam.setList(infoList);
				}
			}
			if(null != pageList){
				webParam.setHtmlPage(pageList);
			}
			return webParam;
		} catch (Exception e) {
			if(i<3){
				i++;
				getEnterprisePensionInfo(taskInsurance,cookies,i);
			}else{
				return null;
			}
		}
		return null;
	}

	private List<InsuranceSuzhouEnterprisePension> htmlParseEnterprisePension(Page page,TaskInsurance taskInsurance, List<InsuranceSuzhouEnterprisePension> infoList) {
		 JSONObject jsonObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
		Document enterprisePensionDoc = Jsoup.parse(jsonObject.getString("content1"));
		
		Elements enterprisePensionInfo = enterprisePensionDoc.select("tr[style=height:28px;background:#ffffff]");
		Elements enterprisePensionInfo2 = enterprisePensionDoc.select("tr[style=height:28px;background:#F5F5F5]");
		
		int num = 0;
		
		for(int i=num;i<enterprisePensionInfo.size();i++){
			Elements tds = enterprisePensionInfo.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null !=lists){
				InsuranceSuzhouEnterprisePension enterprisePension = new InsuranceSuzhouEnterprisePension();
				enterprisePension.setSettlementTime(lists.get(0));
				enterprisePension.setCompanyName(lists.get(1));
				enterprisePension.setPayType(lists.get(2));
				enterprisePension.setPayBase(lists.get(3));
				enterprisePension.setMonths(lists.get(4));
				enterprisePension.setCompanyPay(lists.get(5));
				enterprisePension.setPersonal(lists.get(6));
				enterprisePension.setAccount(lists.get(7));
				enterprisePension.setArrivalTime(lists.get(8));
				enterprisePension.setTaskid(taskInsurance.getTaskid());
				
				infoList.add(enterprisePension);
				
			}
		}
		for(int i=num;i<enterprisePensionInfo2.size();i++){
			Elements tds = enterprisePensionInfo2.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null !=lists){
				InsuranceSuzhouEnterprisePension enterprisePension = new InsuranceSuzhouEnterprisePension();
				enterprisePension.setSettlementTime(lists.get(0));
				enterprisePension.setCompanyName(lists.get(1));
				enterprisePension.setPayType(lists.get(2));
				enterprisePension.setPayBase(lists.get(3));
				enterprisePension.setMonths(lists.get(4));
				enterprisePension.setCompanyPay(lists.get(5));
				enterprisePension.setPersonal(lists.get(6));
				enterprisePension.setAccount(lists.get(7));
				enterprisePension.setArrivalTime(lists.get(8));
				enterprisePension.setTaskid(taskInsurance.getTaskid());
				
				infoList.add(enterprisePension);
				
			}
		}
		return infoList;
	}


	//解析城镇职工医保网页信息
	public WebParam getTownsWorkersInfo(TaskInsurance taskInsurance, Set<Cookie> cookies,int i)throws Exception {
		WebParam webParam= new WebParam();	
		String url = "http://www.szsbzx.net.cn:9900/web/website/personQuery/personQueryAction?frameControlSubmitFunction=getPagesAjax&xz=2&pageIndex=1&nu=4&pageCount=300";
			
		WebClient webClient = insuranceService.getWebClient(cookies);
		List<Page> pageList = new ArrayList<Page>();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept","*/*");
		webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection","keep-alive");
		webRequest.setAdditionalHeader("Host","www.szsbzx.net.cn:9900");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
		webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
		webRequest.setAdditionalHeader("Referer", "http://www.szsbzx.net.cn:9900/web/website/personQuery/personQueryAction.parser");
		webRequest.setAdditionalHeader("Origin","http://www.szsbzx.net.cn:9900");
		
		
		try {
			Page page = webClient.getPage(webRequest);
			
			int statusCode = page.getWebResponse().getStatusCode();
			List<InsuranceSuzhouTownsWorkers> infoList  = new ArrayList<InsuranceSuzhouTownsWorkers>();
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceSuzhouiParser.getTownsWorkersInfo 城镇职工医保信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				pageList.add(page);
				infoList= htmlParseTownsWorkers(page,taskInsurance,infoList);
				if(null != infoList){
					webParam.setList(infoList);
				}
			}
			if(null != pageList){
				webParam.setHtmlPage(pageList);
			}
			return webParam;
		} catch (Exception e) {
			if(i<3){
				i++;
				getTownsWorkersInfo(taskInsurance,cookies,i);
			 
			}else{
				return null;
			}
		}
		return null;
	}


	
	private List<InsuranceSuzhouTownsWorkers> htmlParseTownsWorkers(Page page, TaskInsurance taskInsurance,List<InsuranceSuzhouTownsWorkers> infoList) {
			JSONObject jsonObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
			Document enterprisePensionDoc = Jsoup.parse(jsonObject.getString("content4"));
			
			Elements enterprisePensionInfo = enterprisePensionDoc.select("tr[style=height:28px;background:#ffffff]");
			Elements enterprisePensionInfo2 = enterprisePensionDoc.select("tr[style=height:28px;background:#F5F5F5]");
			
			int num = 0;
			for(int i=num;i<enterprisePensionInfo.size();i++){
				Elements tds = enterprisePensionInfo.get(i).select("td");
				List<String> lists= new ArrayList<>();
				for (Element element : tds) {
					lists.add(element.text().trim());
				}
				if(null !=lists){
					InsuranceSuzhouTownsWorkers townsWorkers = new InsuranceSuzhouTownsWorkers();
					townsWorkers.setSettlementTime(lists.get(0));
					townsWorkers.setCompanyName(lists.get(1));
					townsWorkers.setSupplementType(lists.get(2));
					townsWorkers.setPayBase(lists.get(3));
					townsWorkers.setMonths(lists.get(4));
					townsWorkers.setCompanyMoney(lists.get(5));
					townsWorkers.setBasic(lists.get(6));
					townsWorkers.setMoneyByTimePersonalDate(lists.get(7));
					townsWorkers.setGrand(lists.get(8));
					townsWorkers.setAccount(lists.get(9));
					townsWorkers.setArrivalFlag(lists.get(10));
					townsWorkers.setArrivalTime(lists.get(11));
					townsWorkers.setTaskid(taskInsurance.getTaskid());
					infoList.add(townsWorkers);
					
				}
			
			}
			for(int i=num;i<enterprisePensionInfo2.size();i++){
				Elements tds = enterprisePensionInfo2.get(i).select("td");
				List<String> lists= new ArrayList<>();
				for (Element element : tds) {
					lists.add(element.text().trim());
				}
				if(null !=lists){
					InsuranceSuzhouTownsWorkers townsWorkers = new InsuranceSuzhouTownsWorkers();
					townsWorkers.setSettlementTime(lists.get(0));
					townsWorkers.setCompanyName(lists.get(1));
					townsWorkers.setSupplementType(lists.get(2));
					townsWorkers.setPayBase(lists.get(3));
					townsWorkers.setMonths(lists.get(4));
					townsWorkers.setCompanyMoney(lists.get(5));
					townsWorkers.setBasic(lists.get(6));
					townsWorkers.setMoneyByTimePersonalDate(lists.get(7));
					townsWorkers.setGrand(lists.get(8));
					townsWorkers.setAccount(lists.get(9));
					townsWorkers.setArrivalFlag(lists.get(10));
					townsWorkers.setArrivalTime(lists.get(11));
					townsWorkers.setTaskid(taskInsurance.getTaskid());
					infoList.add(townsWorkers);
				}
			
			}
			
			return infoList;
	}


	//解析工伤保险网页信息
	public WebParam getWorkInjuryInfo(TaskInsurance taskInsurance, Set<Cookie> cookies,int i)throws Exception  {
		WebParam webParam= new WebParam();	
		String url = "http://www.szsbzx.net.cn:9900/web/website/personQuery/personQueryAction?frameControlSubmitFunction=getPagesAjax&xz=2&pageIndex=1&nu=6&pageCount=300";
					  	
		WebClient webClient = insuranceService.getWebClient(cookies);
		List<Page> pageList = new ArrayList<Page>();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept","*/*");
		webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection","keep-alive");
		webRequest.setAdditionalHeader("Host","www.szsbzx.net.cn:9900");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
		webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
		webRequest.setAdditionalHeader("Referer", "http://www.szsbzx.net.cn:9900/web/website/personQuery/personQueryAction.parser");
		webRequest.setAdditionalHeader("Origin","http://www.szsbzx.net.cn:9900");
		
		
		try {
			Page page = webClient.getPage(webRequest);
			
			int statusCode = page.getWebResponse().getStatusCode();
			
			List<InsuranceSuzhouWorkInjury> infoList = new ArrayList<InsuranceSuzhouWorkInjury>();
			
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceSuzhouiParser.getTownsWorkersInfo 工伤保险信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				pageList.add(page);
				infoList= htmlParseWorkInjury(page,taskInsurance,infoList);
				if(null != infoList){
					webParam.setList(infoList);
				}
			}
			if(null != pageList){
				webParam.setHtmlPage(pageList);
			}
			return webParam;
		} catch (Exception e) {
			if(i<3){
				i++;
				getWorkInjuryInfo(taskInsurance,cookies,i);
			}else{
				return null;
			}
		}
			return null;
	}


	private List<InsuranceSuzhouWorkInjury> htmlParseWorkInjury(Page page, TaskInsurance taskInsurance,List<InsuranceSuzhouWorkInjury> infoList) {
		
		JSONObject jsonObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
		Document enterprisePensionDoc = Jsoup.parse(jsonObject.getString("content6"));
		
		Elements enterprisePensionInfo = enterprisePensionDoc.select("tr[style=height:28px;background:#ffffff]");
		Elements enterprisePensionInfo2 = enterprisePensionDoc.select("tr[style=height:28px;background:#F5F5F5]");
		int num = 0;
		for(int i=num;i<enterprisePensionInfo.size();i++){
			Elements tds = enterprisePensionInfo.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null !=lists){
				InsuranceSuzhouWorkInjury injury = new InsuranceSuzhouWorkInjury();
				injury.setSettlementTime(lists.get(0));
				injury.setCompanyName(lists.get(1));
				injury.setPayType(lists.get(2));
				injury.setPayBase(lists.get(3));
				injury.setMonths(lists.get(4));
				injury.setCompanyMoney(lists.get(5));
				injury.setArrivalFlag(lists.get(6));
				injury.setArrivalTime(lists.get(7));
				injury.setTaskid(taskInsurance.getTaskid());
				infoList.add(injury);
				}
		}
		for(int i=num;i<enterprisePensionInfo2.size();i++){
			Elements tds = enterprisePensionInfo2.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null !=lists){
				InsuranceSuzhouWorkInjury injury = new InsuranceSuzhouWorkInjury();
				injury.setSettlementTime(lists.get(0));
				injury.setCompanyName(lists.get(1));
				injury.setPayType(lists.get(2));
				injury.setPayBase(lists.get(3));
				injury.setMonths(lists.get(4));
				injury.setCompanyMoney(lists.get(5));
				injury.setArrivalFlag(lists.get(6));
				injury.setArrivalTime(lists.get(7));
				injury.setTaskid(taskInsurance.getTaskid());
				infoList.add(injury);
				}
		}
		
		return infoList;
	}


	//解析失业保险网页信息
	public WebParam getUnmploymentInfo(TaskInsurance taskInsurance, Set<Cookie> cookies,int i)throws Exception  {
		WebParam webParam= new WebParam();	
		String url = "http://www.szsbzx.net.cn:9900/web/website/personQuery/personQueryAction?frameControlSubmitFunction=getPagesAjax&xz=2&pageIndex=1&nu=8&pageCount=300";
		WebClient webClient = insuranceService.getWebClient(cookies);
		List<Page> pageList = new ArrayList<Page>();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		webRequest.setAdditionalHeader("Accept","*/*");
		webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection","keep-alive");
		webRequest.setAdditionalHeader("Host","www.szsbzx.net.cn:9900");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
		webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
		webRequest.setAdditionalHeader("Referer", "http://www.szsbzx.net.cn:9900/web/website/personQuery/personQueryAction.parser");
		webRequest.setAdditionalHeader("Origin","http://www.szsbzx.net.cn:9900");
		
		try {
			Page page = webClient.getPage(webRequest);
			int statusCode = page.getWebResponse().getStatusCode();
			
			List<InsuranceSuzhouUnemployment> infoList = new ArrayList<InsuranceSuzhouUnemployment>();
			
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceSuzhouiParser.getUnmploymentInfo 失业保险信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				pageList.add(page);
				infoList= htmlParseUnemployment(page,taskInsurance,infoList);
				if(null != infoList){
					webParam.setList(infoList);
				}
				if(null != pageList){
					webParam.setHtmlPage(pageList);
				}
				return webParam;
			}
		} catch (Exception e) {
			if(i<3){
				i++;
				getUnmploymentInfo(taskInsurance,cookies,i);
			}else{
				return null;
			}
		}	
		
		return null;
	
		
	}

	private List<InsuranceSuzhouUnemployment> htmlParseUnemployment(Page page, TaskInsurance taskInsurance,List<InsuranceSuzhouUnemployment> infoList) {
		JSONObject jsonObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
		Document enterprisePensionDoc = Jsoup.parse(jsonObject.getString("content8"));
		
		Elements enterprisePensionInfo = enterprisePensionDoc.select("tr[style=height:28px;background:#ffffff]");
		Elements enterprisePensionInfo2 = enterprisePensionDoc.select("tr[style=height:28px;background:#F5F5F5]");
		
		int num = 0;
		for(int i=num;i<enterprisePensionInfo.size();i++){
			Elements tds = enterprisePensionInfo.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null !=lists){
				InsuranceSuzhouUnemployment unemployment = new InsuranceSuzhouUnemployment();
				unemployment.setSettlementTime(lists.get(0));
				unemployment.setCompanyName(lists.get(1));
				unemployment.setPayType(lists.get(2));
				unemployment.setPayBase(lists.get(3));
				unemployment.setMonths(lists.get(4));
				unemployment.setCompanyPay(lists.get(5));
				unemployment.setPersonal(lists.get(6));
				unemployment.setArrivalFlag(lists.get(7));
				unemployment.setArrivalTime(lists.get(8));
				unemployment.setTaskid(taskInsurance.getTaskid());
				infoList.add(unemployment);
			}
		}
		for(int i=num;i<enterprisePensionInfo2.size();i++){
			Elements tds = enterprisePensionInfo2.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null !=lists){
				InsuranceSuzhouUnemployment unemployment = new InsuranceSuzhouUnemployment();
				unemployment.setSettlementTime(lists.get(0));
				unemployment.setCompanyName(lists.get(1));
				unemployment.setPayType(lists.get(2));
				unemployment.setPayBase(lists.get(3));
				unemployment.setMonths(lists.get(4));
				unemployment.setCompanyPay(lists.get(5));
				unemployment.setPersonal(lists.get(6));
				unemployment.setArrivalFlag(lists.get(7));
				unemployment.setArrivalTime(lists.get(8));
				unemployment.setTaskid(taskInsurance.getTaskid());
				infoList.add(unemployment);
			}
		}
		
		return infoList;
	}


	//解析生育保险网页信息
	public WebParam getBirthInfo(TaskInsurance taskInsurance, Set<Cookie> cookies,int i)throws Exception {

		WebParam webParam= new WebParam();	
		String url = "http://www.szsbzx.net.cn:9900/web/website/personQuery/personQueryAction?frameControlSubmitFunction=getPagesAjax&xz=2&pageIndex=1&nu=7&pageCount=300";
		WebClient webClient = insuranceService.getWebClient(cookies);
		List<Page> pageList = new ArrayList<Page>();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		webRequest.setAdditionalHeader("Accept","*/*");
		webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection","keep-alive");
		webRequest.setAdditionalHeader("Host","www.szsbzx.net.cn:9900");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
		webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
		webRequest.setAdditionalHeader("Referer", "http://www.szsbzx.net.cn:9900/web/website/personQuery/personQueryAction.parser");
		webRequest.setAdditionalHeader("Origin","http://www.szsbzx.net.cn:9900");
		
		try {
			Page page = webClient.getPage(webRequest);
			
			int statusCode = page.getWebResponse().getStatusCode();
			
			List<InsuranceSuzhouBirth> infoList = new ArrayList<InsuranceSuzhouBirth>();
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceSuzhouiParser.getBirthInfo 生育保险信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				pageList.add(page);
				infoList= htmlParseBirth(page,taskInsurance,infoList);
				if(null != infoList){
					webParam.setList(infoList);
				}
			}
			if(null != pageList){
				webParam.setHtmlPage(pageList);
			}
			return webParam;
		} catch (Exception e) {
			if(i<3){
				i++;
				getBirthInfo(taskInsurance,cookies,i);
			}else{
				return null;
			}
			
		}
		return null;
		
	}


	private List<InsuranceSuzhouBirth> htmlParseBirth(Page page, TaskInsurance taskInsurance,List<InsuranceSuzhouBirth> infoList) {
		JSONObject jsonObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
		Document enterprisePensionDoc = Jsoup.parse(jsonObject.getString("content7"));
		
		Elements enterprisePensionInfo = enterprisePensionDoc.select("tr[style=height:28px;background:#ffffff]");
		Elements enterprisePensionInfo2 = enterprisePensionDoc.select("tr[style=height:28px;background:#F5F5F5]");
		
		int num = 0;
		for(int i=num;i<enterprisePensionInfo.size();i++){
			Elements tds = enterprisePensionInfo.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null!=lists){
				InsuranceSuzhouBirth birth = new InsuranceSuzhouBirth();
				birth.setSettlementTime(lists.get(0));
				birth.setCompanyName(lists.get(1));
				birth.setPayType(lists.get(2));
				birth.setPayBase(lists.get(3));
				birth.setMonths(lists.get(4));
				birth.setCompanyPay(lists.get(5));
				birth.setPersonal(lists.get(6));
				birth.setArrivalFlag(lists.get(7));
				birth.setArrivalTime(lists.get(8));
				birth.setTaskid(taskInsurance.getTaskid());
				infoList.add(birth);
			}
		}
		for(int i=num;i<enterprisePensionInfo2.size();i++){
			Elements tds = enterprisePensionInfo2.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null!=lists){
				InsuranceSuzhouBirth birth = new InsuranceSuzhouBirth();
				birth.setSettlementTime(lists.get(0));
				birth.setCompanyName(lists.get(1));
				birth.setPayType(lists.get(2));
				birth.setPayBase(lists.get(3));
				birth.setMonths(lists.get(4));
				birth.setCompanyPay(lists.get(5));
				birth.setPersonal(lists.get(6));
				birth.setArrivalFlag(lists.get(7));
				birth.setArrivalTime(lists.get(8));
				birth.setTaskid(taskInsurance.getTaskid());
				infoList.add(birth);
			}
		}
		return infoList;
	}


	public WebParam getUserfo(TaskInsurance taskInsurance, Set<Cookie> cookies, int i)throws Exception {
		
		WebParam webParam= new WebParam();	
		String url = "http://www.szsbzx.net.cn:9900/web/website/personQuery/personQueryAction?frameControlSubmitFunction=getPagesAjax&xz=1&pageIndex=1&nu=1&pageCount=10";
		WebClient webClient = insuranceService.getWebClient(cookies);
		List<Page> pageList = new ArrayList<Page>();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		
		webRequest.setAdditionalHeader("Accept","*/*");
		webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection","keep-alive");
		webRequest.setAdditionalHeader("Host","www.szsbzx.net.cn:9900");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
		webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
		webRequest.setAdditionalHeader("Referer", "http://www.szsbzx.net.cn:9900/web/website/personQuery/personQueryAction.parser");
		webRequest.setAdditionalHeader("Origin","http://www.szsbzx.net.cn:9900");
		
		
		try {
			Page page = webClient.getPage(webRequest);
			
			int statusCode = page.getWebResponse().getStatusCode();
			
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceSuzhouiParser.getBirthInfo 生育保险信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				pageList.add(page);
				InsuranceSuzhouUser insuranceSuzhouUser = htmlParseUser(page,taskInsurance);
				if(null != insuranceSuzhouUser){
					webParam.setInsuranceSuzhouUser(insuranceSuzhouUser);
					webParam.setPage2(page);
			    	webParam.setUrl(url);
			    	webParam.setHtml(html);
				}
			}
			if(null != pageList){
				webParam.setHtmlPage(pageList);
			}
			return webParam;
		} catch (Exception e) {
			if(i<3){
				i++;
				getUserfo(taskInsurance,cookies,i);
			}else{
				return null;
			}
			
		}
		return null;
	}

	
	/** 
	* @Des 获取目标标签的下一个兄弟标签的内容 
	* @param document 
	* @param keyword 
	* @return 
	*/ 
	public static String getNextLabelByKeyword(Document document, String keyword, String tag){ 
		Elements es = document.select(tag+":contains("+keyword+")"); 
		if(null != es && es.size()>0){ 
		Element element = es.first(); 
		Element nextElement = element.nextElementSibling(); 
			if(null != nextElement){ 
				return nextElement.text(); 
			} 
		} 
		return null; 
	}

	private InsuranceSuzhouUser htmlParseUser(Page page, TaskInsurance taskInsurance) {
		JSONObject jsonObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
		Document enterprisePensionDoc = Jsoup.parse(jsonObject.getString("psMsgBar"));
		String pNum = getNextLabelByKeyword(enterprisePensionDoc,"个人编号","td");
		String pName=getNextLabelByKeyword(enterprisePensionDoc,"姓名","td");
		String pCard=getNextLabelByKeyword(enterprisePensionDoc,"身份证号","td");
		InsuranceSuzhouUser insuranceSuzhouUser =new InsuranceSuzhouUser();
		insuranceSuzhouUser.setCardNum(pCard);
		insuranceSuzhouUser.setName(pName);
		insuranceSuzhouUser.setPersonalNumber(pNum);
		insuranceSuzhouUser.setTaskid(taskInsurance.getTaskid());
		
		return insuranceSuzhouUser;
	}
	
	
}
