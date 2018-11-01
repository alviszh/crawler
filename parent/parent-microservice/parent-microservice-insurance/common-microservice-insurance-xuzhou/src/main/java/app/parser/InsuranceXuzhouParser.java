package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.xuzhou.InsuranceXuzhouEndowmentAccount;
import com.microservice.dao.entity.crawler.insurance.xuzhou.InsuranceXuzhouMedicalAccount;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.service.ChaoJiYingOcrService;

@Component
public class InsuranceXuzhouParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters)throws Exception  {
		String url ="https://www.jsxzhrss.gov.cn//personal/login.jsp?model=ylloading";
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setTimeout(30000);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		int status = page.getWebResponse().getStatusCode();
		if(status == 200){
			HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("//input[@id='grbh']");
			HtmlTextInput password = (HtmlTextInput)page.getFirstByXPath("//input[@id='sfzhm']");
		
			username.setText(insuranceRequestParameters.getUsername());
			password.setText(insuranceRequestParameters.getPassword());
			
			HtmlElement button = (HtmlElement)page.getFirstByXPath("//img[@width='78']");
		
			HtmlPage loadPage = button.click();
			
			webParam.setCode(loadPage.getWebResponse().getStatusCode());
			webParam.setPage(loadPage);
			
			return webParam;
		}
		return null;
	}
	
	
	public WebParam getEndowmentAccount(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance)throws Exception  {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.jsxz.lss.gov.cn/ylzh.jsp?grbh="+insuranceRequestParameters.getUsername()+"&sfzhm="+insuranceRequestParameters.getPassword();
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(requestSettings);
		int statusCode = page.getWebResponse().getStatusCode();
		List<HtmlPage> pageList = new ArrayList<HtmlPage>();
		if(200 == statusCode){
			String html = page.getWebResponse().getContentAsString();
			if(html.contains("您未参加城镇职工保险,请确认身份证号码和个人代码")){
				tracer.addTag("InsuranceXuzhouiParser.getEndowmentAccount 养老保险个人账户信息,身份证或者个人代码错误，请核实" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				return null;
			}else{
	    	tracer.addTag("InsuranceXuzhouiParser.getEndowmentAccount 养老保险个人账户信息" + taskInsurance.getTaskid(),
					"<xmp>" + html + "</xmp>");
	    	pageList.add(page);
	    	InsuranceXuzhouEndowmentAccount infoList = htmlParserEndowmentAccount(html,taskInsurance);
    		if(null !=infoList){
    			webParam.setXuzhouEndowmentInfo(infoList);
    		}
    		if(null != pageList){
    			webParam.setHtmlPage(pageList);
    		}
    		return webParam;
		}
		}
		return null;
		
	}
	public WebParam getMedicalAccount(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance)throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://www.jsxzhrss.gov.cn//personal/yilzh.jsp?grbh="+insuranceRequestParameters.getUsername()+"&sfzhm="+insuranceRequestParameters.getPassword();
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(requestSettings);
		int statusCode = page.getWebResponse().getStatusCode();
		List<HtmlPage> pageList = new ArrayList<HtmlPage>();
		if(200 == statusCode){
			String html = page.getWebResponse().getContentAsString();
			if(html.contains("您未参加城镇职工保险,请确认身份证号码和个人代码")){
				tracer.addTag("InsuranceXuzhouiParser.getMedicalAccount 医疗保险个人账户信息,身份证或者个人代码错误，请核实" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				return null;
			}else{
			tracer.addTag("InsuranceXuzhouiParser.getMedicalAccount 医疗保险个人账户信息" + taskInsurance.getTaskid(),
					"<xmp>" + html + "</xmp>");
			webParam.setHtml(html);
			pageList.add(page);
			InsuranceXuzhouMedicalAccount infoList = htmlParserMedicalAccount(html,taskInsurance);
			if(null !=infoList){
    			webParam.setXuzhouMedicalInfo(infoList);
    		}
    		if(null != pageList){
    			webParam.setHtmlPage(pageList);
    		}
    		return webParam;
		}
		}	
		return null;
	}

	private InsuranceXuzhouMedicalAccount htmlParserMedicalAccount(String html, TaskInsurance taskInsurance) {
		Document endowmentAccountDoc = Jsoup.parse(html);
		String grdm = getNextLabelByKeyword(endowmentAccountDoc,"个人代码","td");
		String xm   =getNextLabelByKeyword(endowmentAccountDoc, "姓名","td");
		String shbzhm = getNextLabelByKeyword(endowmentAccountDoc,"社会保障号码","td");
		String dwdm  = getNextLabelByKeyword(endowmentAccountDoc,"单位代码","td");
		String dwmc  = getNextLabelByKeyword(endowmentAccountDoc,"单位名称","td");
		String dnyjfjs  = getNextLabelByKeyword(endowmentAccountDoc,"当年月缴费基数","td");
		String dyyhzje = getNextLabelByKeyword(endowmentAccountDoc,"当月应划账金额","td");
		String dysfdz  = getNextLabelByKeyword(endowmentAccountDoc,"当月是否到帐","td");
		String zhye = getNextLabelByKeyword(endowmentAccountDoc,"账户余额","td");
		String bnmzmxbqfbzlj = getNextLabelByKeyword(endowmentAccountDoc,"本年门诊慢性病起付标准累计","td");
		String bnmzmxbzflj = getNextLabelByKeyword(endowmentAccountDoc,"本年门诊慢性病支付累计","td");
		InsuranceXuzhouMedicalAccount  medicalAccount  = new InsuranceXuzhouMedicalAccount();
		medicalAccount.setPersonalCode(grdm);
		medicalAccount.setName(xm);
		medicalAccount.setPersonalFirstTime(shbzhm);
		medicalAccount.setUnitCode(dwdm);
		medicalAccount.setUnitName(dwmc);
		medicalAccount.setMonthlyPaymentBase(dnyjfjs);
		medicalAccount.setDebitAmount(dyyhzje);
		medicalAccount.setToAccountFlag(dysfdz);
		medicalAccount.setAccountBalance(zhye);
		medicalAccount.setPayStandard(bnmzmxbqfbzlj);
		medicalAccount.setPayCumulative(bnmzmxbzflj);
		medicalAccount.setTaskid(taskInsurance.getTaskid());
		return medicalAccount;
	}


	private InsuranceXuzhouEndowmentAccount htmlParserEndowmentAccount(String html, TaskInsurance taskInsurance) {
		Document endowmentAccountDoc = Jsoup.parse(html);
		String grdm = getNextLabelByKeyword(endowmentAccountDoc,"个人代码","td");
		String xm   =getNextLabelByKeyword(endowmentAccountDoc, "姓名","td");
		String shbzhm = getNextLabelByKeyword(endowmentAccountDoc,"社会保障号码","td");
		String dwdm  = getNextLabelByKeyword(endowmentAccountDoc,"单位代码","td");
		String dwmc  = getNextLabelByKeyword(endowmentAccountDoc,"单位名称","td");
		String grzhljcce  = getNextLabelByKeyword(endowmentAccountDoc,"截止至2017年6月末个人账户累计储存额","td");
		String dnyjfjs  = getNextLabelByKeyword(endowmentAccountDoc,"当年月缴费基数","td");
		String dnjfys  = getNextLabelByKeyword(endowmentAccountDoc,"当年缴费月数","td");
		String dysfdz  = getNextLabelByKeyword(endowmentAccountDoc,"当月是否到帐","td");
		InsuranceXuzhouEndowmentAccount  account  = new InsuranceXuzhouEndowmentAccount();
		account.setPersonalCode(grdm);
		account.setName(xm);
		account.setPersonalFirstTime(shbzhm);
		account.setUnitCode(dwdm);
		account.setUnitName(dwmc);
		account.setAccountStorageAmount(grzhljcce);
		account.setMonthlyPaymentBase(dnyjfjs);
		account.setPaymentMonths(dnjfys);
		account.setToAccountFlag(dysfdz);
		account.setTaskid(taskInsurance.getTaskid());
		return account;
	}

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


	

}
