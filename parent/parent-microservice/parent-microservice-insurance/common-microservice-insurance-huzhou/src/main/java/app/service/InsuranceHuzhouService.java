package app.service;

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
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.huzhou.InsuranceHuzhouBasicinfoRepository;
import com.microservice.dao.repository.crawler.insurance.huzhou.InsuranceHuzhouHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.huzhou.InsuranceHuzhouUserInfoRespository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.InsuranceHuzhouHtmlunit;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.huzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.huzhou"})
public class InsuranceHuzhouService {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceHuzhouHtmlunit insuranceHuzhouHtmlunit;
	@Autowired
	private InsuranceHuzhouHtmlRepository insuranceHuzhouHtmlRepository;
	@Autowired
	private InsuranceHuzhouUserInfoRespository insuranceHuzhouUserInfoRespository;
	@Autowired
	private InsuranceHuzhouBasicinfoRepository insuranceHuzhouBasicinfoRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired 
	private TracerLog tracer;
	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("getUserInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		@SuppressWarnings("rawtypes")
		WebParam webParam;
		try {
			webParam = insuranceHuzhouHtmlunit.getUserInfo(taskInsurance, cookies);
			if(null != webParam){
				String html=webParam.getHtml();
				tracer.addTag("getUserInfo",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				InsuranceHuzhouHtml insuranceHuzhouHtml = new InsuranceHuzhouHtml();
				insuranceHuzhouHtml.setPageCount(1);
				insuranceHuzhouHtml.setType("userinfo");
				insuranceHuzhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
				insuranceHuzhouHtml.setUrl(webParam.getUrl());
				insuranceHuzhouHtml.setHtml(webParam.getHtml());
				insuranceHuzhouHtmlRepository.save(insuranceHuzhouHtml);
				if (null !=webParam.getUserInfo()) {
					insuranceHuzhouUserInfoRespository.save(webParam.getUserInfo());
					tracer.addTag("getUserInfo", "社保个人信息已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance); 
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}else{
					tracer.addTag("getUserInfo", "社保个人信息未爬取到，无数据");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}										
			}	
		} catch (Exception e) {
			tracer.addTag("getUserInfo.Exception", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			e.printStackTrace();
		}		
	}
	
	/**
	 * @Des 获取基本信息信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	public void getBasicinfo(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("getBasicinfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		WebParam webParam;
		try {
			webParam = insuranceHuzhouHtmlunit.getBasicinfo(taskInsurance, cookies);
			if(null != webParam){
				String html=webParam.getHtml();
				tracer.addTag("getBasicinfo",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				InsuranceHuzhouHtml insuranceHuzhouHtml = new InsuranceHuzhouHtml();
				insuranceHuzhouHtml.setPageCount(1);
				insuranceHuzhouHtml.setType("basicinfo");
				insuranceHuzhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
				insuranceHuzhouHtml.setUrl(webParam.getUrl());
				insuranceHuzhouHtml.setHtml(webParam.getHtml());
				insuranceHuzhouHtmlRepository.save(insuranceHuzhouHtml);
				if (null !=webParam.getBasicinfoList()) {
					insuranceHuzhouBasicinfoRepository.saveAll(webParam.getBasicinfoList());
					tracer.addTag("getBasicinfo", "社保基本信息已入库");
				}else{
					tracer.addTag("getBasicinfo", "社保个人信息未爬取到，无数据");
				}										
			}	
		} catch (Exception e) {
			tracer.addTag("getBasicinfo.Exception", e.getMessage());
			e.printStackTrace();
		}
			
	}		
}
