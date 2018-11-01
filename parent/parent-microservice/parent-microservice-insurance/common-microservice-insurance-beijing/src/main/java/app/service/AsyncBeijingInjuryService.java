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
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingInjury;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.beijing.InsuranceBeijingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.beijing.InsuranceBeijingInjuryRepository;

import app.bean.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceBeijingParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.beijing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.beijing"})
public class AsyncBeijingInjuryService {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceBeijingParser insuranceBeijingParser;
	@Autowired
	private InsuranceBeijingInjuryRepository insuranceBeijingInjuryRepository;
	@Autowired
	private InsuranceBeijingHtmlRepository insuranceBeijingHtmlRepository;
	@Autowired
	private InsuranceService insuranceService;
	
	/**
	 * @Des 获取工伤信息
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getInjury(InsuranceRequestParameters insuranceRequestParameters, String searchYear) {
		
		tracer.addTag("parser.crawler.getInjury"+searchYear, insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		
		WebParam<InsuranceBeijingInjury> webParam = insuranceBeijingParser.getInjury(taskInsurance,cookies,searchYear);
		if(null != webParam){
			insuranceBeijingInjuryRepository.saveAll(webParam.getList());
			tracer.addTag("getInjury InsuranceBeijingInjury "+searchYear+" save!", insuranceRequestParameters.getTaskId());			
			InsuranceBeijingHtml insuranceBeijingHtml = new InsuranceBeijingHtml();
			insuranceBeijingHtml.setHtml(webParam.getHtml());
			insuranceBeijingHtml.setPageCount(Integer.valueOf(searchYear));
			insuranceBeijingHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceBeijingHtml.setType("INJURY");
			insuranceBeijingHtml.setUrl(webParam.getUrl());
			insuranceBeijingHtmlRepository.save(insuranceBeijingHtml);
			tracer.addTag("getInjury InsuranceBeijingHtml "+searchYear+" save!", insuranceRequestParameters.getTaskId());
			
			insuranceService.changeCrawlerStatus(searchYear+"年【个人社保-工伤保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					webParam.getCode(), taskInsurance);
			
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}		
		
	}

}
