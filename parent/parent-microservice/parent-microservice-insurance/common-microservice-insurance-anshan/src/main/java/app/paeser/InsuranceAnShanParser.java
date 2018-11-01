package app.paeser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.anshan.InsuranceAnShanMedical;
import com.microservice.dao.entity.crawler.insurance.anshan.InsuranceAnShanPension;
import com.microservice.dao.entity.crawler.insurance.anshan.InsuranceAnShanUserInfo;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.anshan.InsuranceAnShanMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.anshan.InsuranceAnShanUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.anshan.InsuranceAnShangPensionRepository;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.anshan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.anshan"})
public class InsuranceAnShanParser {
	@Autowired
	private  ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceAnShangPensionRepository insuranceAnShangPensionRepository;
	@Autowired
	private InsuranceAnShanMedicalRepository insuranceAnShanMedicalRepository;
	@Autowired
	private InsuranceAnShanUserInfoRepository insuranceAnShanUserInfoRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws FailingHttpStatusCodeException 
	 * @throws Exception 
	 */
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance) throws Exception {
		pension(insuranceRequestParameters,taskInsurance);
		
		
		return null;
	}

	public void pension(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getWebClient();	
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setTimeout(50000); 
		String urlPension = "http://www.asshbx.gov.cn/asweb/cxyl.jsp?yincang=%D2%BD%C1%C6&subid="+insuranceRequestParameters.getUsername()+"&PASSWORD="+insuranceRequestParameters.getUserIDNum()+"&Submit=%D2%BD%C1%C6";
		WebRequest webRequest1 = new WebRequest(new URL(urlPension), HttpMethod.POST);	
		HtmlPage page1 = webClient.getPage(webRequest1);
		String html = page1.getWebResponse().getContentAsString();
		Document doc = Jsoup.parse(html);
		Elements element = doc.select(" form > table > tbody > tr > td > span");
		String str = null;
		if (element.size()>0){
			//System.out.println(element.size());
			str = element.get(2).text().trim();
			
		}
		System.out.println("str"+str);
		if (str.contains("个人电脑编号")){
			int statusCode = page1.getWebResponse().getStatusCode();
			taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance,page1);
			taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
			List<InsuranceAnShanPension> insuranceAnShanPension = htmlPersionInfoParser(html,taskInsurance);
			if(insuranceAnShanPension==null||insuranceAnShanPension.equals("")){
				
			}else{
			    insuranceAnShangPensionRepository.saveAll(insuranceAnShanPension);
			    insuranceService.changeCrawlerStatus("【养老保险信息】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
			    		statusCode, taskInsurance);
			    String idCard = str.substring(str.lastIndexOf("：")+1);
			    System.out.println(idCard);
			    medical(insuranceRequestParameters,taskInsurance,idCard);
			}
		}else{
			taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
		}
		
	}
	public void medical(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance,String idCard) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getWebClient();	
		//webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setTimeout(50000); 
		System.out.println("11111111111111");
		//String urlMedical = "http://www.asshbx.gov.cn/asweb/cxyil.jsp?yincang=%D2%BD%C1%C6&subid=&PASSWORD="+insuranceRequestParameters.getUsername()+"&Submit=%D2%BD%C1%C6";	
		String urlMedical = "http://www.asshbx.gov.cn/asweb/cxlog2.jsp";
		WebRequest webRequest1 = new WebRequest(new URL(urlMedical), HttpMethod.GET);
		HtmlPage page1 = webClient.getPage(webRequest1);
		HtmlTextInput userIDNum = (HtmlTextInput)page1.getFirstByXPath("//input[@name='subid']"); 
		HtmlTextInput username1 = (HtmlTextInput)page1.getFirstByXPath("//input[@name='PASSWORD']"); 
		HtmlElement loginButton1 = (HtmlElement)page1.getFirstByXPath("//input[@name='Submit']");	
		userIDNum.setText(insuranceRequestParameters.getUsername());
		username1.setText(insuranceRequestParameters.getUserIDNum());
		HtmlPage loginPage1 = loginButton1.click();
		String html1 = loginPage1.getWebResponse().getContentAsString();
		int statusCode = page1.getWebResponse().getStatusCode();
		List<InsuranceAnShanMedical> insuranceAnShanMedical = htmlMedicalInfoParser(html1,taskInsurance);
		if(insuranceAnShanMedical==null||insuranceAnShanMedical.equals("")){
			
		}else{
			insuranceAnShanMedicalRepository.saveAll(insuranceAnShanMedical);
		    insuranceService.changeCrawlerStatus("【医疗保险信息】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
		    		statusCode, taskInsurance);
		}
		
		InsuranceAnShanUserInfo insuranceAnShanUserInfo = htmlUserInfoParser(html1,taskInsurance,idCard);
        if(insuranceAnShanMedical==null||insuranceAnShanMedical.equals("")){
			
		}else{
			insuranceAnShanUserInfoRepository.save(insuranceAnShanUserInfo);
		    insuranceService.changeCrawlerStatus("【个人信息】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
		    		statusCode, taskInsurance);
		}
        taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getDescription());
        taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhase());
        taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhasestatus());
        taskInsurance.setFinished(true);
        taskInsurance.setGongshangStatus(200);
        taskInsurance.setShengyuStatus(200);
        taskInsurance.setShiyeStatus(200);
        taskInsuranceRepository.save(taskInsurance);
		
	}
	

	private InsuranceAnShanUserInfo htmlUserInfoParser (String html,TaskInsurance taskInsurance,String idCard) throws Exception{
		Document doc = Jsoup.parse(html);
		Elements element = doc.select(" form > table > tbody > tr > td > span");
		if (element.size()>0){
			//System.out.println(element.size());
			String str = element.get(2).text().trim();
			String str1 = str.substring(str.indexOf("：")+1).trim();
			String personal = str1.substring(0,str1.indexOf("单")).trim();
			String str3 = str1.substring(str1.indexOf("：")+1).trim();
			String companyName = str3.substring(0, str3.indexOf("个人电脑")-1).trim();
			String str5 = str3.substring(str3.indexOf("：")+1).trim();
			String company = str5.substring(0, str5.indexOf("姓名")-1).trim();
			String name = str5.substring(str5.indexOf("：")+1).trim();
			System.out.println(personal);
			System.out.println(companyName);
			System.out.println(company);
			System.out.println(name);
			InsuranceAnShanUserInfo insuranceAnShanUserInfo = new InsuranceAnShanUserInfo();
			insuranceAnShanUserInfo.setPersonal(personal);
			insuranceAnShanUserInfo.setCompanyName(companyName);
			insuranceAnShanUserInfo.setCompany(company);
			insuranceAnShanUserInfo.setName(name);
			insuranceAnShanUserInfo.setTaskid(taskInsurance.getTaskid());
			insuranceAnShanUserInfo.setIdCard(idCard);
			return insuranceAnShanUserInfo;
		}
		return null;
		
	}
	/**
	 * @Des 根据获取的获取养老保险缴费详情页面解析具体信息
	 * @param html
	 * @return
	 */
	private List<InsuranceAnShanPension>  htmlPersionInfoParser(String html,TaskInsurance taskInsurance) throws Exception{
		List<InsuranceAnShanPension> pensions = new ArrayList<InsuranceAnShanPension>();
		Document doc = Jsoup.parse(html);
		Elements element = doc.select(" table > tbody > tr:nth-child(2) > td > table > tbody tr");
		if (element.size()>0){
			for(int k = 2;k<(element.size()-1);k++){
				String payMonth = element.get(k).select("td").eq(0).text().trim();					//年度
				String payCardinality = element.get(k).select("td").eq(1).text().trim();			//缴费基数
				String payNumber = element.get(k).select("td").eq(2).text().trim();					//实交月数
				String payDepartment= element.get(k).select("td").eq(3).text().trim();				//单位缴费
				String payPerson= element.get(k).select("td").eq(4).text().trim();					//个人缴费
				InsuranceAnShanPension insuranceAnShanPension = new InsuranceAnShanPension();
				insuranceAnShanPension.setPayMonth(payMonth);
				insuranceAnShanPension.setPayCardinality(payCardinality);
				insuranceAnShanPension.setPayNumber(payNumber);
				insuranceAnShanPension.setPayDepartment(payDepartment);
				insuranceAnShanPension.setPayPerson(payPerson);
				insuranceAnShanPension.setTaskid(taskInsurance.getTaskid());
				pensions.add(insuranceAnShanPension);
			}
			return pensions;
		}
		
		return null;
	}
	 /**
	 * @Des 根据获取的获取医疗保险缴费数据解析
	 * @param html  zcx
	 * @return
	 */		
	private List<InsuranceAnShanMedical>  htmlMedicalInfoParser(String html,TaskInsurance taskInsurance)throws Exception {
		List<InsuranceAnShanMedical> medical = new ArrayList<InsuranceAnShanMedical>();
		Document doc = Jsoup.parse(html);
		Elements element = doc.select("table > tbody > tr:nth-child(2) > td > table > tbody tr");
		if (element.size()>0){
			//System.out.println(element.size());
			for(int k = 2;k<element.size();k++){
			    String payMonth = element.get(k).select("td").eq(0).text().trim();					//年度
				String carryover = element.get(k).select("td").eq(1).text().trim();					//上年结转金额
				String payPerson = element.get(k).select("td").eq(2).text().trim();					//本年帐户个人缴费部分本金
				String payDepartment = element.get(k).select("td").eq(3).text().trim();				//本年帐户单位缴费划拨部分本金
				String injection = element.get(k).select("td").eq(4).text().trim();                   //本年帐户注入金额
				String cumulative = element.get(k).select("td").eq(5).text().trim();                  //帐户支付累计金额
				String balance = element.get(k).select("td").eq(6).text().trim();                     //帐户结余金额
				InsuranceAnShanMedical insuranceAnShanMedical = new InsuranceAnShanMedical();
				insuranceAnShanMedical.setPayMonth(payMonth);
				insuranceAnShanMedical.setCarryover(carryover);
				insuranceAnShanMedical.setPayPerson(payPerson);
				insuranceAnShanMedical.setPayDepartment(payDepartment);
				insuranceAnShanMedical.setInjection(injection);
				insuranceAnShanMedical.setCumulative(cumulative);
				insuranceAnShanMedical.setBalance(balance);
				insuranceAnShanMedical.setTaskid(taskInsurance.getTaskid());
				
				medical.add(insuranceAnShanMedical);
			}
			return medical;
		}
		return null;
		
	}	
}
