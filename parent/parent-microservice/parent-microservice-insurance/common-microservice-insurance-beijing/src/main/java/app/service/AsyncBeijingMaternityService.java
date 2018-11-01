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
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingHtml;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingMaternity;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.beijing.InsuranceBeijingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.beijing.InsuranceBeijingMaternityRepository;

import app.bean.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceBeijingParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.beijing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.beijing"})
public class AsyncBeijingMaternityService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceBeijingParser insuranceBeijingParser;
	@Autowired
	private InsuranceBeijingMaternityRepository insuranceBeijingMaternityRepository;
	@Autowired
	private InsuranceBeijingHtmlRepository insuranceBeijingHtmlRepository;
	@Autowired
	private InsuranceService insuranceService;
	
	/**
	 * @Des 获取生育信息
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getBear(InsuranceRequestParameters insuranceRequestParameters, String searchYear) {
		
		tracer.addTag("parser.crawler.getBear"+searchYear, insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		
		WebParam<InsuranceBeijingMaternity> webParam = insuranceBeijingParser.getBear(taskInsurance,cookies,searchYear);
		if(null != webParam){
			insuranceBeijingMaternityRepository.saveAll(webParam.getList());
			tracer.addTag("getBear InsuranceBeijingMaternity "+searchYear+" save!", insuranceRequestParameters.getTaskId());			
			InsuranceBeijingHtml insuranceBeijingHtml = new InsuranceBeijingHtml();
			insuranceBeijingHtml.setHtml(webParam.getHtml());
			insuranceBeijingHtml.setPageCount(Integer.valueOf(searchYear));
			insuranceBeijingHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceBeijingHtml.setType("BEAR");
			insuranceBeijingHtml.setUrl(webParam.getUrl());
			insuranceBeijingHtmlRepository.save(insuranceBeijingHtml);
			tracer.addTag("getBear InsuranceBeijingHtml "+searchYear+" save!", insuranceRequestParameters.getTaskId());
			
			insuranceService.changeCrawlerStatus(searchYear+"年【个人社保-生育保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
					webParam.getCode(), taskInsurance);
			
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}		
		
	}

	
	
}
