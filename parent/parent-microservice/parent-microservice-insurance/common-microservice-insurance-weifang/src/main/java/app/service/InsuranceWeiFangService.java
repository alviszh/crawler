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
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangHtml;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.weifang.InsuranceWeiFangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.weifang.InsuranceWeiFangUserInfoRespository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceWeiFangParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.weifang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.weifang"})
public class InsuranceWeiFangService {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceWeiFangParser insuranceWeiFangParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceWeiFangHtmlRepository insuranceWeiFangHtmlRepository;
	@Autowired
	private InsuranceWeiFangUserInfoRespository insuranceWeiFangUserInfoRespository;
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
			webParam = insuranceWeiFangParser.getUserInfo(taskInsurance,cookies);
			if(null != webParam){
				String html=webParam.getHtml();
				tracer.addTag("getUserInfo",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				InsuranceWeiFangHtml insuranceWeiFangHtml = new InsuranceWeiFangHtml();
				insuranceWeiFangHtml.setPageCount(1);
				insuranceWeiFangHtml.setType("userInfo");
				insuranceWeiFangHtml.setTaskid(insuranceRequestParameters.getTaskId());
				insuranceWeiFangHtml.setUrl(webParam.getUrl());
				insuranceWeiFangHtml.setHtml(html);
				insuranceWeiFangHtmlRepository.save(insuranceWeiFangHtml);	
				InsuranceWeiFangUserInfo  userInfo=webParam.getInsuranceWeiFangUserInfo();
				if (null !=userInfo) {
					insuranceWeiFangUserInfoRespository.save(userInfo);
					tracer.addTag("getUserinfo", "社保个人信息已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance); 
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}else{
					tracer.addTag("getUserinfo", "潍坊社保个人信息未爬取到，无数据");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}										
			}		
		} catch (Exception e) {
			tracer.addTag("getUserinfo.Exception", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			e.printStackTrace();
		}
		
	
	}
}
