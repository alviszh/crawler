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
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.taian.InsuranceTaiAnChargeDetail;
import com.microservice.dao.entity.crawler.insurance.taian.InsuranceTaiAnHtml;
import com.microservice.dao.entity.crawler.insurance.taian.InsuranceTaiAnUserInfo;
import com.microservice.dao.repository.crawler.insurance.taian.InsuranceTaiAnChargeDetailRepository;
import com.microservice.dao.repository.crawler.insurance.taian.InsuranceTaiAnHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.taian.InsuranceTaiAnUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceTaiAnParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.taian"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.taian"})
public class InsuranceTaiAnCrawlerService {
	public static final Logger log = LoggerFactory.getLogger(InsuranceTaiAnCrawlerService.class);
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceTaiAnHtmlRepository insuranceTaiAnHtmlRepository;
	@Autowired
	private InsuranceTaiAnParser insuranceTaiAnParser;
	@Autowired
	private InsuranceTaiAnUserInfoRepository insuranceTaiAnUserInfoRepository;
	@Autowired
	private InsuranceTaiAnChargeDetailRepository insuranceTaiAnChargeDetailRepository;
	@Autowired
	private TracerLog tracer;
	
	@Async
	public Future<String> getUserInfo(TaskInsurance taskInsurance, String usersession_uuid)  {
		try {
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			String url="http://124.130.146.14:8082/hsp/hspUser.do";
			String requestBody="method=fwdQueryPerInfo"
					+ "&_random=0.8820611540536378"
					+ "&__usersession_uuid="+usersession_uuid.trim()+""
					+ "&_laneID=a5c60318-8192-4f41-be5d-9766c7306cf0";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				InsuranceTaiAnHtml insuranceTaiAnHtml = new InsuranceTaiAnHtml();
				insuranceTaiAnHtml.setTaskid(taskInsurance.getTaskid());
				insuranceTaiAnHtml.setType("用户信息源码页");
				insuranceTaiAnHtml.setPagenumber(1);
				insuranceTaiAnHtml.setUrl(url);
				insuranceTaiAnHtml.setHtml(html);
				insuranceTaiAnHtmlRepository.save(insuranceTaiAnHtml);
				tracer.addTag("action.crawler.getUserinfo.html", "个人信息源码页已入库");
				InsuranceTaiAnUserInfo insuranceTaiAnUserInfo=insuranceTaiAnParser.userInfoParser(taskInsurance,html);
				if(null!=insuranceTaiAnUserInfo){
					insuranceTaiAnUserInfoRepository.save(insuranceTaiAnUserInfo);
					tracer.addTag("action.crawler.getUserInfo", "全部个人信息已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
							 200, taskInsurance);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getPension(TaskInsurance taskInsurance, String usersession_uuid) {
		try {
			String url="http://124.130.146.14:8082/hsp/siAd.do";
			String requestBody="method=queryAgedPayHis"
					+ "&_random=0.18689942234048829"
					+ "&__usersession_uuid="+usersession_uuid.trim()+""
					+ "&_laneID=33029a02-19e5-4363-80d5-e6a2f53c437d";
			String chineseDescription="养老保险缴费明细";
			String englishDescription="getPension";
			getDifferentInsurChargeInfo(taskInsurance,url,requestBody,chineseDescription,englishDescription);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPension.e",e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getMedical(TaskInsurance taskInsurance, String usersession_uuid)  {
		try {
			String url="http://124.130.146.14:8082/hsp/siMedi.do";
			String requestBody="method=queryMediPayHis"
					+ "&_random=0.19237104783707304"
					+ "&__usersession_uuid="+usersession_uuid.trim()+""
					+ "&_laneID=33029a02-19e5-4363-80d5-e6a2f53c437d";
			String chineseDescription="医疗保险缴费明细";
			String englishDescription="getMedical";
			getDifferentInsurChargeInfo(taskInsurance,url,requestBody,chineseDescription,englishDescription);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMedical.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getInjury(TaskInsurance taskInsurance, String usersession_uuid) {
		try {
			String url="http://124.130.146.14:8082/hsp/siHarm.do";
			String requestBody="method=queryHarmPayHis"
					+ "&_random=0.4206279338018615"
					+ "&__usersession_uuid="+usersession_uuid.trim()+""
					+ "&_laneID=33029a02-19e5-4363-80d5-e6a2f53c437d";
			String chineseDescription="工伤保险缴费明细";
			String englishDescription="getInjury";
			getDifferentInsurChargeInfo(taskInsurance,url,requestBody,chineseDescription,englishDescription);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getInjury.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getUnemployment(TaskInsurance taskInsurance, String usersession_uuid) {
		try {
			String url="http://124.130.146.14:8082/hsp/siLost.do";
			String requestBody="method=queryLostPayHis"
					+ "&_random=0.6765695566624843"
					+ "&__usersession_uuid="+usersession_uuid.trim()+""
					+ "&_laneID=33029a02-19e5-4363-80d5-e6a2f53c437d";
			String chineseDescription="失业保险缴费明细";
			String englishDescription="getUnemployment";
			getDifferentInsurChargeInfo(taskInsurance,url,requestBody,chineseDescription,englishDescription);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUnemployment.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getBear(TaskInsurance taskInsurance, String usersession_uuid) {
		try {
			String url="http://124.130.146.14:8082/hsp/siBirth.do";
			String requestBody="method=queryBirthPayHis"
					+ "&_random=0.8542002111036315"
					+ "&__usersession_uuid="+usersession_uuid.trim()+""
					+ "&_laneID=33029a02-19e5-4363-80d5-e6a2f53c437d";
			String chineseDescription="生育保险缴费明细";
			String englishDescription="getBear";
			getDifferentInsurChargeInfo(taskInsurance,url,requestBody,chineseDescription,englishDescription);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBear.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public void getDifferentInsurChargeInfo(TaskInsurance taskInsurance,String url,String requestBody,String chineseDescription,String englishDescription) throws Exception {
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			InsuranceTaiAnHtml insuranceTaiAnHtml = new InsuranceTaiAnHtml();  
			insuranceTaiAnHtml.setTaskid(taskInsurance.getTaskid());
			insuranceTaiAnHtml.setType(chineseDescription+"源码页");
			insuranceTaiAnHtml.setPagenumber(1);
			insuranceTaiAnHtml.setUrl(url);
			insuranceTaiAnHtml.setHtml(html);
			insuranceTaiAnHtmlRepository.save(insuranceTaiAnHtml);
			tracer.addTag("action.crawler."+englishDescription+".html", chineseDescription+"源码页已入库");
			if(englishDescription.equals("getPension")){   //养老
				List<InsuranceTaiAnChargeDetail> list=insuranceTaiAnParser.pensionParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceTaiAnChargeDetailRepository.saveAll(list);
					tracer.addTag("action.crawler."+englishDescription+"", chineseDescription+"已入库");
				}
			}else if(englishDescription.equals("getMedical")){   //医疗
				List<InsuranceTaiAnChargeDetail> list=insuranceTaiAnParser.medicalParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceTaiAnChargeDetailRepository.saveAll(list);
					tracer.addTag("action.crawler."+englishDescription+"", chineseDescription+"已入库");
				}
			}else if(englishDescription.equals("getUnemployment")){    //失业 
				List<InsuranceTaiAnChargeDetail> list=insuranceTaiAnParser.unemploymentParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceTaiAnChargeDetailRepository.saveAll(list);
					tracer.addTag("action.crawler."+englishDescription+"", chineseDescription+"已入库");
				}
			}else {     //工伤和生育
				List<InsuranceTaiAnChargeDetail> list=insuranceTaiAnParser.injuryAndBearParser(taskInsurance,html,englishDescription);
				if(null!=list && list.size()>0){
					insuranceTaiAnChargeDetailRepository.saveAll(list);
					tracer.addTag("action.crawler."+englishDescription+"", chineseDescription+"已入库");
				}
			}
		}
	}
}
