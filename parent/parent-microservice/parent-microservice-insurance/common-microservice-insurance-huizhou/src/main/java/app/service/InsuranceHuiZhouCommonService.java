package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.huizhou"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.huizhou"})
public class InsuranceHuiZhouCommonService{
	
	@Autowired
	private InsuranceHuiZhouService insuranceHuiZhouService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance  taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceHuiZhouService.login(insuranceRequestParameters);
		if(taskInsurance.getPhase().equals("LOGIN")&&taskInsurance.getPhase_status().equals("SUCCESS"))
		{
			taskInsurance = insuranceHuiZhouService.getAllData(insuranceRequestParameters);
		}
		return taskInsurance;
	}

//	@Async
//	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
//		TaskInsurance  taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		
//		return taskInsurance;
//	}


}
