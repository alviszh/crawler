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
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.haerbin"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.haerbin"})
public class InsuranceHaerbinCommonService{
	
	@Autowired
	private InsuranceHaErBinService insuranceHaErBinService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance  taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceHaErBinService.login(insuranceRequestParameters);
		
		if(taskInsurance.getPhase().equals("LOGIN")&&taskInsurance.getPhase_status().equals("SUCCESS"))
		{
			taskInsurance = insuranceHaErBinService.getAllData(insuranceRequestParameters);
		}
		return taskInsurance;
	}
}
