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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingHtml;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingPension;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.beijing.InsuranceBeijingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.beijing.InsuranceBeijingPensionRepository;
import com.microservice.dao.repository.crawler.insurance.beijing.InsuranceBeijingUserInfoRepository;

import app.bean.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceBeijingParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.beijing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.beijing"})
public class AsyncBeijingPensionService {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceBeijingParser insuranceBeijingParser;
	@Autowired
	private InsuranceBeijingPensionRepository insuranceBeijingPensionRepository;
	@Autowired
	private InsuranceBeijingHtmlRepository insuranceBeijingHtmlRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceBeijingUserInfoRepository insuranceBeijingUserInfoRepository;
	
	
	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		
		tracer.addTag("parser.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		@SuppressWarnings("rawtypes")
		WebParam webParam = insuranceBeijingParser.getUserInfo(taskInsurance,webClient);
		if(null != webParam){
			insuranceBeijingUserInfoRepository.save(webParam.getInsuranceBeijingUserInfo());
			tracer.addTag("getUserInfo 个人信息","个人信息已入库!");
			
			insuranceService.changeCrawlerStatusUserInfo(taskInsurance,webParam.getCode());
			
			InsuranceBeijingHtml insuranceBeijingHtml = new InsuranceBeijingHtml();
			insuranceBeijingHtml.setPageCount(1);
			insuranceBeijingHtml.setType("userinfo");
			insuranceBeijingHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceBeijingHtml.setUrl(webParam.getUrl());
			insuranceBeijingHtml.setHtml(webParam.getHtml());
			insuranceBeijingHtmlRepository.save(insuranceBeijingHtml);
			tracer.addTag("getUserInfo 个人信息源码","个人信息源码表入库!");		
			
		}
		
	}
	
	
	/**
	 * @Des 获取养老信息
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getPensionMsg(InsuranceRequestParameters insuranceRequestParameters, String searchYear) {
		
		tracer.addTag("parser.crawler.getPension"+searchYear, insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		
		WebParam<InsuranceBeijingPension> webParam = insuranceBeijingParser.getPension(taskInsurance,cookies,searchYear);
		if(null != webParam){
			insuranceBeijingPensionRepository.saveAll(webParam.getList());
			tracer.addTag("getPensionMsg InsuranceBeijingPension "+searchYear+" save!", insuranceRequestParameters.getTaskId());			
			InsuranceBeijingHtml insuranceBeijingHtml = new InsuranceBeijingHtml();
			insuranceBeijingHtml.setHtml(webParam.getHtml());
			insuranceBeijingHtml.setPageCount(Integer.valueOf(searchYear));
			insuranceBeijingHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceBeijingHtml.setType("PENSION");
			insuranceBeijingHtml.setUrl(webParam.getUrl());
			insuranceBeijingHtmlRepository.save(insuranceBeijingHtml);
			tracer.addTag("getPensionMsg InsuranceBeijingHtml "+searchYear+" save!", insuranceRequestParameters.getTaskId());
			
//			System.out.println("养老 状态吗"+searchYear+": "+webParam.getCode());
			insuranceService.changeCrawlerStatus(searchYear+"年【个人社保-养老保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					webParam.getCode(), taskInsurance);
		
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}		
		
	}
	
	

}
