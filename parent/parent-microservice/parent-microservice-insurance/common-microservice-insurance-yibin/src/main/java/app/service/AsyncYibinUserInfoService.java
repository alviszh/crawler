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
import com.microservice.dao.entity.crawler.insurance.yibin.InsuranceYibinHtml;
import com.microservice.dao.entity.crawler.insurance.yibin.InsuranceYinbinUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.yibin.InsuranceYibinHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.yibin.InsuranceYinbinUserInfoRespository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceYibinParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.yibin"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.yibin"})
public class AsyncYibinUserInfoService {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceYibinParser insuranceYibinParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceYibinHtmlRepository insurancYibinHtmlRepository;
	@Autowired
	private InsuranceYinbinUserInfoRespository insuranceYinbinUserInfoRespository;
	@Autowired
	private TracerLog tracer;	

	@Async
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		tracer.addTag("getUserInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		@SuppressWarnings("rawtypes")
		WebParam webParam = insuranceYibinParser.getUserInfo(taskInsurance,cookies);
		if(null != webParam){
			String html=webParam.getHtml();
			tracer.addTag("getUserInfo",
					insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
			InsuranceYibinHtml insuranceYibinHtml  = new InsuranceYibinHtml();
			insuranceYibinHtml.setPageCount(1);
			insuranceYibinHtml.setType("userInfo");
			insuranceYibinHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceYibinHtml.setUrl(webParam.getUrl());
			insuranceYibinHtml.setHtml(html);
			insurancYibinHtmlRepository.save(insuranceYibinHtml);	
			InsuranceYinbinUserInfo  userInfo=webParam.getUserInfo();
			if (null !=userInfo) {
				insuranceYinbinUserInfoRespository.save(userInfo);
				tracer.addTag("getUserinfo", "社保个人信息已入库"+userInfo.toString());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance); 			
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			}else{
				tracer.addTag("getUserinfo", "社保个人信息未爬取到，无数据");
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
				taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}										
		}		
	
	}
}
