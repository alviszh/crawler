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
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zhuhai"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zhuhai"})
public class InsuranceZhuhaiHandle {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceZhuhaiService insuranceZhuhaiService;
	@Autowired
	private InsuranceService insuranceService;

	@Async
	public TaskInsurance processor(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("insurance.zhuhai.processor", insuranceRequestParameters.getTaskId());
		
		insuranceZhuhaiService.login(insuranceRequestParameters);
		
		TaskInsurance taskInsurance = insuranceService.findTaskInsurance(insuranceRequestParameters.getTaskId());
		if(taskInsurance.getPhase().equals(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase())
			&& taskInsurance.getPhase_status().equals(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus())){
			insuranceZhuhaiService.getAllData(insuranceRequestParameters);			
		}
		
		
		return taskInsurance;
		
	}
}
