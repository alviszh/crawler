package app.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.liuzhou.InsuranceLiuZhouInjury;
import com.microservice.dao.entity.crawler.insurance.liuzhou.InsuranceLiuZhouMaternity;
import com.microservice.dao.entity.crawler.insurance.liuzhou.InsuranceLiuZhouMedical;
import com.microservice.dao.entity.crawler.insurance.liuzhou.InsuranceLiuZhouPension;
import com.microservice.dao.entity.crawler.insurance.liuzhou.InsuranceLiuZhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.liuzhou.InsuranceLiuZhouUserInfo;
import com.microservice.dao.repository.crawler.insurance.liuzhou.InsuranceLiuZhouInjuryRepository;
import com.microservice.dao.repository.crawler.insurance.liuzhou.InsuranceLiuZhouMaternityRepository;
import com.microservice.dao.repository.crawler.insurance.liuzhou.InsuranceLiuZhouMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.liuzhou.InsuranceLiuZhouPensionRepository;
import com.microservice.dao.repository.crawler.insurance.liuzhou.InsuranceLiuZhouUnemploymentRepository;
import com.microservice.dao.repository.crawler.insurance.liuzhou.InsuranceLiuZhouUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceLiuZhouParser {
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private  ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceLiuZhouUserInfoRepository insuranceLiuZhouUserInfoRepository;
	@Autowired
	private InsuranceLiuZhouInjuryRepository insuranceLiuZhouInjuryRepository;
	@Autowired
	private InsuranceLiuZhouMedicalRepository insuranceLiuZhouMedicalRepository;
	@Autowired
	private InsuranceLiuZhouMaternityRepository insuranceLiuZhouMaternityRepository;
	@Autowired
	private InsuranceLiuZhouPensionRepository insuranceLiuZhouPensionRepository;
	@Autowired
	private InsuranceLiuZhouUnemploymentRepository insuranceLiuZhouUnemploymentRepository;
	@Autowired
	private TracerLog tracer;
	public HtmlPage login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		String url = "http://siquery.lzsrsj.com/SIQG/Grdl.aspx";
		WebClient webClient = WebCrawler.getInstance().getWebClient();		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
//		webClient.getOptions().setTimeout(50000);
		HtmlPage page = webClient.getPage(webRequest);
		HtmlTextInput username = page.getFirstByXPath("//*[@id='txtSfzh']");
		HtmlTextInput name = page.getFirstByXPath("//*[@id='txtXm']");
		HtmlPasswordInput password = page.getFirstByXPath("//*[@id='txtMm']");
    	HtmlImage image = page.getFirstByXPath("//*[@id='imgVerify']");
    	String code = chaoJiYingOcrService.getVerifycode(image, "1005");
    	HtmlTextInput vfcode = page.getFirstByXPath("//*[@id='txtYzm2']");
	    System.out.println(code);
	    username.setText(insuranceRequestParameters.getUsername());
	    name.setText(insuranceRequestParameters.getName());
	    password.setText(insuranceRequestParameters.getPassword());
	    vfcode.setText(code);
	    HtmlElement button = page.getFirstByXPath("//*[@id='btnLogin']");

		HtmlPage loggedPage = button.click();
//		System.out.println(loggedPage.getWebResponse().getContentAsString());
		String u = "http://siquery.lzsrsj.com/SIQG/grxx.aspx";
		WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.GET);
		HtmlPage page1 = webClient.getPage(webRequest1);
//		System.out.println(page1.getWebResponse().getContentAsString());
		
		return page1;
		
	}
	
	public void crawler(TaskInsurance taskInsurance,WebClient webClient,String html) throws Exception {
		userInfo(html,taskInsurance.getTaskid());
		List<String> list = pay(taskInsurance,webClient);
		
		for(String html1:list){
			tracer.addTag("流水html", html1);
			injury(html1,taskInsurance.getTaskid());
		}
		//个人信息
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getError_code(), taskInsurance);
		// 养老
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getError_code(), taskInsurance);
		// 生育
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getError_code(), taskInsurance);
		// 工商
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getError_code(), taskInsurance);
		// 医疗
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getError_code(), taskInsurance);
		// 失业
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getError_code(), taskInsurance);
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
	}
	
	public void userInfo(String html,String taskid){
		Document doc = Jsoup.parse(html);
	    Elements ele = doc.select("div.column32b span");
	    if(ele.size()>0){
	    	InsuranceLiuZhouUserInfo userinfo = new InsuranceLiuZhouUserInfo();
	    	String number = ele.get(0).text().trim();						//个人社保编号
    		String name = ele.get(1).text().trim();				            //姓名
    		String sex = ele.get(2).text().trim();								//性别
    		String idNum = ele.get(3).text().trim();							//身份证号
    		String address = ele.get(4).text().trim();                         //联系地址
    		String phone = ele.get(5).text().trim();                           //电话
    		String unitNumber = ele.get(6).text().trim();                      //当前单位编号
    		String unitName = ele.get(7).text().trim();                        //当前单位名称
    		String type = ele.get(8).text().trim();                            //人员状态
    		String pension = ele.get(9).text().trim();                         //城镇企业职工养老保险
    		String medical = ele.get(10).text().trim();                         //医疗保险
    		String unemployment = ele.get(11).text().trim();                    //失业保险
    		String injury = ele.get(12).text().trim();                          //工伤保险	
    		String birth = ele.get(13).text().trim();                           //生育保险
    		userinfo.setNumber(number);
    		userinfo.setName(name);
    		userinfo.setSex(sex);
    		userinfo.setIdNum(idNum);
    		userinfo.setAddress(address);
    		userinfo.setPhone(phone);
    		userinfo.setUnitNumber(unitNumber);
    		userinfo.setUnitName(unitName);
    		userinfo.setType(type);
    		userinfo.setPension(pension);
    		userinfo.setMedical(medical);
    		userinfo.setUnemployment(unemployment);
    		userinfo.setInjury(injury);
    		userinfo.setBirth(birth);
    		userinfo.setTaskid(taskid);
	    		
    		insuranceLiuZhouUserInfoRepository.save(userinfo);
	    }
	}
	public void injury(String html,String taskid){
		Document doc = Jsoup.parse(html);
	    Elements ele = doc.select("#ContentPlaceHolder1_grvGrjfmx  tr");
	    if(ele.size()>0){
	    	for(int i = 1;i<ele.size();i++){
	    		String number = ele.get(i).select("td").eq(0).text().trim();						 //个人编号
		    	String name = ele.get(i).select("td").eq(1).text().trim();				            //姓名
		    	String idNum = ele.get(i).select("td").eq(2).text().trim();							//身份证号
		    	String type = ele.get(i).select("td").eq(3).text().trim();                          //险种类型
		    	String year = ele.get(i).select("td").eq(4).text().trim();                          //台账年月
		    	String payMonth = ele.get(i).select("td").eq(5).text().trim();                      //费款所属期
		    	String base = ele.get(i).select("td").eq(6).text().trim();                          //缴费基数
		    	String unitAmount = ele.get(i).select("td").eq(7).text().trim();                    //单位应缴
		    	String personalAmount = ele.get(i).select("td").eq(8).text().trim();                //个人应缴
		    	String subtotal = ele.get(i).select("td").eq(9).text().trim();                      //小计
		    	String sign = ele.get(i).select("td").eq(10).text().trim();                         //缴费标志
		    	InsuranceLiuZhouInjury injury = new InsuranceLiuZhouInjury();
		    	InsuranceLiuZhouMedical medical= new InsuranceLiuZhouMedical();
		    	InsuranceLiuZhouMaternity maternity = new InsuranceLiuZhouMaternity();
		    	InsuranceLiuZhouPension pension = new InsuranceLiuZhouPension();
		    	InsuranceLiuZhouUnemployment unemployment = new InsuranceLiuZhouUnemployment();
		    	if(type.contains("基本养老保险")){
		    		pension.setNumber(number);
		    		pension.setName(name);
		    		pension.setIdNum(idNum);
		    		pension.setType(type);
		    		pension.setYear(year);
		    		pension.setPayMonth(payMonth);
		    		pension.setBase(base);
		    		pension.setUnitAmount(unitAmount);
		    		pension.setPersonalAmount(personalAmount);
		    		pension.setSubtotal(subtotal);
		    		pension.setSign(sign);
		    		pension.setTaskid(taskid);
		    		insuranceLiuZhouPensionRepository.save(pension);
		    	}
		    	if(type.contains("失业保险")){
		    		unemployment.setNumber(number);
		    		unemployment.setName(name);
		    		unemployment.setIdNum(idNum);
		    		unemployment.setType(type);
		    		unemployment.setYear(year);
		    		unemployment.setPayMonth(payMonth);
		    		unemployment.setBase(base);
		    		unemployment.setUnitAmount(unitAmount);
		    		unemployment.setPersonalAmount(personalAmount);
		    		unemployment.setSubtotal(subtotal);
		    		unemployment.setSign(sign);
		    		unemployment.setTaskid(taskid);
		    		insuranceLiuZhouUnemploymentRepository.save(unemployment);
		    	}
		    	if(type.contains("综合医疗保险")||type.contains("大额医疗保险")){
		    		medical.setNumber(number);
		    		medical.setName(name);
		    		medical.setIdNum(idNum);
		    		medical.setType(type);
		    		medical.setYear(year);
		    		medical.setPayMonth(payMonth);
		    		medical.setBase(base);
		    		medical.setUnitAmount(unitAmount);
		    		medical.setPersonalAmount(personalAmount);
		    		medical.setSubtotal(subtotal);
		    		medical.setSign(sign);
		    		medical.setTaskid(taskid);
		    		insuranceLiuZhouMedicalRepository.save(medical);
		    	}
		    	if(type.contains("工伤保险")){
		    		injury.setNumber(number);
		    		injury.setName(name);
		    		injury.setIdNum(idNum);
		    		injury.setType(type);
		    		injury.setYear(year);
		    		injury.setPayMonth(payMonth);
		    		injury.setBase(base);
		    		injury.setUnitAmount(unitAmount);
		    		injury.setPersonalAmount(personalAmount);
		    		injury.setSubtotal(subtotal);
		    		injury.setSign(sign);
		    		injury.setTaskid(taskid);
		    		insuranceLiuZhouInjuryRepository.save(injury);
		    	}
		    	if(type.contains("生育保险")){
		    		maternity.setNumber(number);
		    		maternity.setName(name);
		    		maternity.setIdNum(idNum);
		    		maternity.setType(type);
		    		maternity.setYear(year);
		    		maternity.setPayMonth(payMonth);
		    		maternity.setBase(base);
		    		maternity.setUnitAmount(unitAmount);
		    		maternity.setPersonalAmount(personalAmount);
		    		maternity.setSubtotal(subtotal);
		    		maternity.setSign(sign);
		    		maternity.setTaskid(taskid);
		    		insuranceLiuZhouMaternityRepository.save(maternity);
		    	}
	    	}
	    	
	    }
	}
	
	
	public List<String> pay(TaskInsurance taskInsurance,WebClient webClient) throws Exception{
		String url = "http://siquery.lzsrsj.com/SIQG/grjfmx.aspx";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		webClient.getOptions().setTimeout(50000);
		HtmlPage page = webClient.getPage(webRequest);
		String html = page.getWebResponse().getContentAsString();
		Document doc = Jsoup.parse(html);
	    Elements ele = doc.select("#ContentPlaceHolder1_txtKssj");
	    String timea = ele.attr("value").trim();
	    //System.out.println("timea:"+timea);
	    String begin = timea.substring(0, timea.indexOf("-"));
	    String ss = timea.substring(timea.indexOf("-"));
	    int i = Integer.parseInt(begin)-7;
	    String s = Integer.toString(i);
	    String beginTime = s+ss;
	    //System.out.println(beginTime);
	    HtmlTextInput be = page.getFirstByXPath("//*[@id='ContentPlaceHolder1_txtKssj']");
	    be.setText(beginTime);
	    HtmlElement button = page.getFirstByXPath("//*[@id='ContentPlaceHolder1_btnQuery']");
	    HtmlPage pageHtml = button.click();
	    //System.out.println(pageHtml.asXml());
	    String html1 = pageHtml.getWebResponse().getContentAsString();
		Document doc1 = Jsoup.parse(html1);
	    Elements ele1 = doc1.select("#ContentPlaceHolder1_anpPaging > div:nth-child(1)");
	    String a = ele1.text().trim();
	    String aa = a.substring(a.indexOf("共")+1, a.indexOf("页"));
	    int b = Integer.parseInt(aa);
	    List<String> list =new  ArrayList<String>();
	    for(int j = 0;j<b;j++){
	    	String html2 = null;
	    	if(j==0){
	    		list.add(html1);
	    	}else{
	    		HtmlElement button1 = pageHtml.getFirstByXPath("//*[@id='ContentPlaceHolder1_anpPaging']/div[2]/span[14]/a");
	    	    HtmlPage pageHtml1 = button1.click();	
	    	    html2 = pageHtml1.getWebResponse().getContentAsString();
	    	    list.add(html2);
	    	    pageHtml = pageHtml1;
	    	}
	    }
		return list;
	}
}
