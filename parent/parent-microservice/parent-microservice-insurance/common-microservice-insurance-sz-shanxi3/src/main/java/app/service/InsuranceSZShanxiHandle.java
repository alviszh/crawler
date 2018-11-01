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
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.shanxi3"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.shanxi3"})
public class InsuranceSZShanxiHandle {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private AsyncSZShanxiPensionService asyncSZShanxiPensionService;
	
	@Async
	public TaskInsurance processor(InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.output("insurance.sz.shanxi3.processor", insuranceRequestParameters.getTaskId());
		asyncSZShanxiPensionService.login(insuranceRequestParameters);
		
		TaskInsurance taskInsurance = insuranceService.findTaskInsurance(insuranceRequestParameters.getTaskId());
		if(taskInsurance.getPhase().equals(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase())
				&& taskInsurance.getPhase_status().equals(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus())){
			asyncSZShanxiPensionService.getAllData(insuranceRequestParameters);			
			}
		return taskInsurance;
		

	}

}
