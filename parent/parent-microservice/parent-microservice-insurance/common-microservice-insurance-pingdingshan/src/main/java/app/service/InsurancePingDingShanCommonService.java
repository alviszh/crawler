package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.pingdingshan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.pingdingshan" })
public class InsurancePingDingShanCommonService extends InsuranceNanYangLoginTemplateService implements InsuranceLogin{

	@Autowired
	private InsurancePingDingShanService insurancePingDingShanService;
	
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insurancePingDingShanService.getEndowment(insuranceRequestParameters,taskInsurance);
		insurancePingDingShanService.getUserInfo(insuranceRequestParameters,taskInsurance);
		
		insurancePingDingShanService.getMedical(insuranceRequestParameters,taskInsurance);
		insurancePingDingShanService.getUnemployment(insuranceRequestParameters,taskInsurance);
		insurancePingDingShanService.getMaternity(insuranceRequestParameters,taskInsurance);
		insurancePingDingShanService.getInjury(insuranceRequestParameters,taskInsurance);
		return taskInsurance;
		
	}
	
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		commonLogin(insuranceRequestParameters, taskInsurance);
		return taskInsurance;
	}



	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
