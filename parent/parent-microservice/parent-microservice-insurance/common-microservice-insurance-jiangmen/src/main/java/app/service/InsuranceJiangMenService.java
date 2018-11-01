package app.service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenInjury;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenMaternity;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenMedical;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenPersion;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenUnemployment;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.jiangmen.InsuranceJiangMenInjuryRepository;
import com.microservice.dao.repository.crawler.insurance.jiangmen.InsuranceJiangMenMaternityRepository;
import com.microservice.dao.repository.crawler.insurance.jiangmen.InsuranceJiangMenMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.jiangmen.InsuranceJiangMenPensionRepository;
import com.microservice.dao.repository.crawler.insurance.jiangmen.InsuranceJiangMenUnemploymentRepository;
import com.microservice.dao.repository.crawler.insurance.jiangmen.InsuranceJiangMenUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.InsuranceJMParse;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.jiangmen"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.jiangmen"})
public class InsuranceJiangMenService {
	public static final Logger log = LoggerFactory.getLogger(InsuranceJiangMenService.class);
	@Autowired
	private InsuranceJiangMenUserInfoRepository insuranceJiangMenUserInfoRepository;
	@Autowired
	private InsuranceJiangMenPensionRepository insuranceJiangMenPensionRepository;
	@Autowired
	private InsuranceJiangMenMedicalRepository insuranceJiangMenMedicalRepository;
	@Autowired
	private InsuranceJiangMenMaternityRepository insuranceJiangMenMaternityRepository;
	@Autowired
	private InsuranceJiangMenInjuryRepository insuranceJiangMenInjuryRepository;
	@Autowired
	private InsuranceJiangMenUnemploymentRepository insuranceJiangMenUnemploymentRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	protected TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
	@Async
	public  Future<String> getResult(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance, WebClient webClient) {
		String urPersion = "http://sbj.jiangmen.gov.cn/Servers/YLaoCombineSearch.aspx?ItemId=48";
		String urMedical = "http://sbj.jiangmen.gov.cn/Servers/YLiaoCombineSearch.aspx?ItemId=51";
		String urMaternity = "http://sbj.jiangmen.gov.cn/Servers/SyuCombineSearch.aspx?ItemId=61";
		String urInjury = "http://sbj.jiangmen.gov.cn/Servers/GsCombineSearch.aspx?ItemId=59";
		String urUnemployment = "http://sbj.jiangmen.gov.cn/Servers/SyeCombineSearch.aspx?ItemId=57";
		String user = "http://sbj.jiangmen.gov.cn/Servers/BaseInfoSearch.aspx?ItemId=74";
		Page page = null;
		//个人信息
		try {
			WebRequest webRequest = new WebRequest(new URL(user), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html = page.getWebResponse().getContentAsString();
		InsuranceJiangMenUserInfo userInfo = InsuranceJMParse.userinfo_parse(html);
		if(userInfo==null){
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getError_code(), taskInsurance);
		}else{
			userInfo.setTaskid(taskInsurance.getTaskid());
			insuranceJiangMenUserInfoRepository.save(userInfo);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getError_code(), taskInsurance);
		}
		

		//养老缴费
		try {
			WebRequest webRequest = new WebRequest(new URL(urPersion), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String htmlPersion = page.getWebResponse().getContentAsString();
		tracer.addTag("养老缴费html", htmlPersion);
		System.out.println(htmlPersion);
		List<InsuranceJiangMenPersion> persion= InsuranceJMParse.persion_parse(htmlPersion,taskInsurance);
		if(persion==null){
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getError_code(), taskInsurance);
		}else{
			insuranceJiangMenPensionRepository.saveAll(persion);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getError_code(), taskInsurance);
		}
			
		
		
		//医疗缴费
		try {
			WebRequest webRequest = new WebRequest(new URL(urMedical), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String htmlMedical = page.getWebResponse().getContentAsString();
		tracer.addTag("医疗缴费html", htmlMedical);
		System.out.println(htmlMedical);
		List<InsuranceJiangMenMedical> medical= InsuranceJMParse.medical_parse(htmlMedical,taskInsurance);
		if(medical==null){
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getError_code(), taskInsurance);
		}else{
			insuranceJiangMenMedicalRepository.saveAll(medical);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getError_code(), taskInsurance);
		}
			
	
		
		//生育缴费
		try {
			WebRequest webRequest = new WebRequest(new URL(urMaternity), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String htmlMaternity = page.getWebResponse().getContentAsString();
		tracer.addTag("生育缴费html", htmlMaternity);
		System.out.println(htmlMaternity);
		List<InsuranceJiangMenMaternity> maternity= InsuranceJMParse.maternity_parse(htmlMaternity,taskInsurance);
		if(maternity==null){
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getError_code(), taskInsurance);
		}else{
			insuranceJiangMenMaternityRepository.saveAll(maternity);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getError_code(), taskInsurance);
		}
			
		
		
		//工伤缴费
		try {
			WebRequest webRequest = new WebRequest(new URL(urInjury), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String htmlInjury = page.getWebResponse().getContentAsString();
		tracer.addTag("工伤缴费html", htmlInjury);
		System.out.println(htmlInjury);
		List<InsuranceJiangMenInjury> injury= InsuranceJMParse.injury_parse(htmlInjury,taskInsurance);
		if(injury==null){
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getError_code(), taskInsurance);
		}else{
			insuranceJiangMenInjuryRepository.saveAll(injury);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getError_code(), taskInsurance);
		}
			
		
		
		//失业缴费
		try {
			WebRequest webRequest = new WebRequest(new URL(urUnemployment), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String htmlUnemployment = page.getWebResponse().getContentAsString();
		tracer.addTag("失业缴费html", htmlUnemployment);
		System.out.println(htmlUnemployment);
		List<InsuranceJiangMenUnemployment> unemployment= InsuranceJMParse.unemployment_parse(htmlUnemployment,taskInsurance);
		if(unemployment==null){
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getError_code(), taskInsurance);
		}else{
			
			insuranceJiangMenUnemploymentRepository.saveAll(unemployment);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getError_code(), taskInsurance);
		}
			
		
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		return null;
	}
		

}
