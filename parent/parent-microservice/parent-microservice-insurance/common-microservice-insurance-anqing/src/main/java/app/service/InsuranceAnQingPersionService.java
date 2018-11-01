package app.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.anqing.InsuranceAnQingHtml;
import com.microservice.dao.entity.crawler.insurance.anqing.InsuranceAnQingInfo;
import com.microservice.dao.entity.crawler.insurance.anqing.InsuranceAnQingPaydetails;
import com.microservice.dao.entity.crawler.insurance.anqing.InsuranceAnQingUserInfo;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.anqing.InsuranceAnQingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.anqing.InsuranceAnQingInfoRepository;
import com.microservice.dao.repository.crawler.insurance.anqing.InsuranceAnQingPaydetailsRepository;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceAnQingParser;
import app.parser.InsuranceAnQingPersionParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.anqing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.anqing"})
public class InsuranceAnQingPersionService {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceAnQingHtmlRepository insuranceAnQingHtmlRepository;
	@Autowired
	private InsuranceAnQingPaydetailsRepository   insuranceAnQingPaydetailsRepository;
	@Autowired
	private InsuranceAnQingPersionParser insuranceAnQingPersionParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceAnQingParser  insuranceAnQingParser;
	@Autowired
	private InsuranceAnQingInfoRepository insuranceAnQingInfoRepository;
	
	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 获取社保缴费信息
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getPaydetails(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("InsuranceAnQingPersionService.getPaydetails",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
			WebParam webParam;
		try {
			webParam = insuranceAnQingPersionParser.getPaydetails(taskInsurance, cookies);
			int temp = 0;
			if (null != webParam) {
				List<InsuranceAnQingPaydetails> paydetails = webParam.getInsuranceAnQingPaydetails();
				if (null != paydetails && !paydetails.isEmpty()) {
					temp++;
					insuranceAnQingPaydetailsRepository.saveAll(paydetails);
					InsuranceAnQingHtml insuranceAnQingHtml = new InsuranceAnQingHtml();
					insuranceAnQingHtml.setPageCount(1);
					insuranceAnQingHtml.setType("paydetails");
					insuranceAnQingHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceAnQingHtml.setUrl(webParam.getUrl());
					insuranceAnQingHtml.setHtml(webParam.getHtml());
					insuranceAnQingHtmlRepository.save(insuranceAnQingHtml);
				} else {
					tracer.addTag("InsuranceAnQingPersionService.getPaydetails",
							insuranceRequestParameters.getTaskId());
				}
			}

			if (temp > 0) {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
		} catch (Exception e) {
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				e.printStackTrace();
			}	
	}	
	@Async
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("getUserInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		WebParam webParam=null;
		try {
			webParam = insuranceAnQingParser.getUserInfo(taskInsurance,cookies);
			if(null != webParam){
				String html=webParam.getHtml();
				tracer.addTag("getUserInfo",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				InsuranceAnQingHtml insuranceAnQingHtml = new InsuranceAnQingHtml();
				insuranceAnQingHtml.setPageCount(1);
				insuranceAnQingHtml.setType("userInfo");
				insuranceAnQingHtml.setTaskid(insuranceRequestParameters.getTaskId());
				insuranceAnQingHtml.setUrl(webParam.getUrl());
				insuranceAnQingHtml.setHtml(html);
				insuranceAnQingHtmlRepository.save(insuranceAnQingHtml);		
				InsuranceAnQingUserInfo  userInfo=webParam.getInsuranceAnQingUserInfo();
				if (null !=userInfo) {
					insuranceAnQingHtmlRepository.save(insuranceAnQingHtml);
					tracer.addTag("getUserinfo", "社保个人信息已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance); 
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}else{
					tracer.addTag("getUserinfo", "社保个人信息未爬取到，无数据");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}										
			}		
		} catch (Exception e) {
			tracer.addTag("InsuranceAnQingPersionService.getUserinfo.Exception", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
			e.printStackTrace();
		}	
	}
	/**
	 * @Des 获取工伤信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	public void getInsuranceInfo(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("getInsuranceInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		@SuppressWarnings("rawtypes")
		WebParam webParam;
		try {
			webParam = insuranceAnQingParser.getInsuranceInfo(taskInsurance, cookies);
			if(null != webParam){
				String html=webParam.getHtml();
				tracer.addTag("getInsuranceInfo",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				InsuranceAnQingHtml insuranceAnQingHtml = new InsuranceAnQingHtml();
				insuranceAnQingHtml.setPageCount(1);
				insuranceAnQingHtml.setType("insuranceInfo");
				insuranceAnQingHtml.setTaskid(insuranceRequestParameters.getTaskId());
				insuranceAnQingHtml.setUrl(webParam.getUrl());
				insuranceAnQingHtml.setHtml(html);
				insuranceAnQingHtmlRepository.save(insuranceAnQingHtml);	
				List<InsuranceAnQingInfo>  insuranceAnQingInfos=webParam.getInsuranceAnQingInfos();
				if (null !=insuranceAnQingInfos && ! insuranceAnQingInfos.isEmpty()) {
					insuranceAnQingInfoRepository.saveAll(insuranceAnQingInfos);
					tracer.addTag("getUserinfo", "社保情况信息已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 200, taskInsurance); 
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}else{
					tracer.addTag("getUserinfo", "社保情况信息未爬取到，无数据");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}										
			}	
		} catch (Exception e) {
			tracer.addTag("getInsuranceInfo.Exception", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			e.printStackTrace();
		}	
	}
}
