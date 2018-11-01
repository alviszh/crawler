package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;

import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.guangxi"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.guangxi"})
public class InsuranceGuangXiAllDateService {

	@Autowired
	private InsuranceGuangXiFatureService InsuranceGuangXiFatureService;
	@Autowired
	private TracerLog tracer;
	@Async
	public void getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("crawler.service.getAllData", insuranceRequestParameters.getTaskId());				   
		InsuranceGuangXiFatureService.getUserInfo(insuranceRequestParameters);
		InsuranceGuangXiFatureService.getBasicinfo(insuranceRequestParameters);		
		InsuranceGuangXiFatureService.getPaydetails(insuranceRequestParameters);	
	}
}
