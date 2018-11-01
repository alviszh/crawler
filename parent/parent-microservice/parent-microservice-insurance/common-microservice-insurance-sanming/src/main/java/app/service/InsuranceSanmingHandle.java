package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sanming"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sanming"})
public class InsuranceSanmingHandle {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSanmingService insuranceSanmingService;
	
	@Async
	public TaskInsurance processor(InsuranceRequestParameters insuranceRequestParameters) {
	
		tracer.addTag("insurance.sanming.processor", insuranceRequestParameters.getTaskId());
		
		insuranceSanmingService.login(insuranceRequestParameters);
		
		TaskInsurance taskInsurance = insuranceService.findTaskInsurance(insuranceRequestParameters.getTaskId());
		if(taskInsurance.getPhase().equals(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase())
			&& taskInsurance.getPhase_status().equals(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus())){
			insuranceSanmingService.getAllData(insuranceRequestParameters);			
		}
		return taskInsurance;
	}

}
