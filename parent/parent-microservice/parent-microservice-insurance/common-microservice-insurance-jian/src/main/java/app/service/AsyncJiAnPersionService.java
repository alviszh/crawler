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
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.jian.InsuranceJiAnHtml;
import com.microservice.dao.entity.crawler.insurance.jian.InsuranceJiAnPersion;
import com.microservice.dao.entity.crawler.insurance.jian.InsuranceJiAnUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.jian.InsuranceJiAnHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.jian.InsuranceJiAnPersionRepository;
import com.microservice.dao.repository.crawler.insurance.jian.InsuranceJiAnUserInfoRespository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceJiAnParser;
import app.parser.InsuranceJiAnPersionParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.jian"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.jian"})
public class AsyncJiAnPersionService {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceJiAnPersionParser insuranceJiAnPersionParser;
	@Autowired
	private InsuranceJiAnPersionRepository insuranceJiAnPersionRepository;
	@Autowired
	private InsuranceJiAnUserInfoRespository insuranceJiAnUserInfoRespository;
	@Autowired
	private InsuranceJiAnHtmlRepository insuranceJiAnHtmlRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceJiAnParser  insuranceJiAnParser;
	@Autowired
	private TracerLog tracer;	
	/**
	 * @Des 获取养老信息
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getPersionInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		tracer.addTag("getPersionInfo",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());	
		int temp=0;
		WebParam webParam =insuranceJiAnPersionParser.getPersionInfo(taskInsurance, cookies);
		if (null !=webParam) {
			List<InsuranceJiAnPersion> persionList=webParam.getPersionList();
			if (null !=persionList && !persionList.isEmpty()) {
				insuranceJiAnPersionRepository.saveAll(persionList);	
				temp++;
				InsuranceJiAnHtml insuranceJiAnHtml = new InsuranceJiAnHtml();
				insuranceJiAnHtml.setPagenumber(1);
				insuranceJiAnHtml.setType("persionInfo");
				insuranceJiAnHtml.setTaskid(insuranceRequestParameters.getTaskId());
				insuranceJiAnHtml.setUrl(webParam.getUrl());
				insuranceJiAnHtml.setHtml(webParam.getHtml());
				insuranceJiAnHtmlRepository.save(insuranceJiAnHtml);			
			}else{		
			  tracer.addTag("getPersionInfo",insuranceRequestParameters.getTaskId()+"没有当前条件所对应的养老信息");					
			}									
		}			
	
        if (temp>0) {
        	taskInsurance=insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
        	insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}else{
			taskInsurance=insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
        	insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
	
	}	
	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		tracer.addTag("getUserInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		@SuppressWarnings("rawtypes")
		WebParam webParam = insuranceJiAnParser.getUserInfo(taskInsurance,cookies);
		if(null != webParam){
			String html=webParam.getHtml();
			tracer.addTag("getUserInfo",
					insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
			InsuranceJiAnHtml insuranceJiAnHtml = new InsuranceJiAnHtml();
			insuranceJiAnHtml.setPagenumber(1);
			insuranceJiAnHtml.setType("userInfo");
			insuranceJiAnHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceJiAnHtml.setUrl(webParam.getUrl());
			insuranceJiAnHtml.setHtml(html);
			insuranceJiAnHtmlRepository.save(insuranceJiAnHtml);	
			InsuranceJiAnUserInfo  userInfo=webParam.getInsuranceJiAnUserInfo();
			if (null !=userInfo) {
				insuranceJiAnUserInfoRespository.save(userInfo);
				tracer.addTag("getUserinfo", "吉安社保个人信息已入库");
				taskInsurance=insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance); 
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}else{
				tracer.addTag("getUserinfo", "吉安社保个人信息未爬取到，无数据");
				taskInsurance=insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}										
		}		
	
	}
}
