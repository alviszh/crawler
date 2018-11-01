package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.service.aop.InsuranceLogin;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.nanyang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.nanyang" })
public class InsuranceNanYangCommonService implements InsuranceLogin{

	@Autowired
	private InsuranceNanYangService insuranceNanYangService;
	@Autowired 
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

		insuranceNanYangService.getUserInfo(insuranceRequestParameters,taskInsurance);
		insuranceNanYangService.getEndowment(insuranceRequestParameters,taskInsurance);
		
		insuranceNanYangService.getMedical(insuranceRequestParameters,taskInsurance);
		insuranceNanYangService.getUnemployment(insuranceRequestParameters,taskInsurance);
		insuranceNanYangService.getMaternity(insuranceRequestParameters,taskInsurance);
		insuranceNanYangService.getInjury(insuranceRequestParameters,taskInsurance);
		return taskInsurance;
	}

	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceNanYangService.login(insuranceRequestParameters,taskInsurance);
		return taskInsurance;
		
	}



	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}



}
