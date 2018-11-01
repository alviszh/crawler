package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.service.aop.InsuranceLogin;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.bengbu" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.bengbu" })
public class InsuranceBengBuCommonService implements InsuranceLogin{

	@Autowired
	private InsuranceBengBuService insuranceBengBuService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceBengBuService.login(insuranceRequestParameters,taskInsurance);
		return taskInsurance;
	}

	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceBengBuService.crawlerUserInfo(insuranceRequestParameters,taskInsurance);
		
		insuranceBengBuService.crawlerMedical(insuranceRequestParameters,taskInsurance);
		insuranceBengBuService.crawlerEndowment(insuranceRequestParameters,taskInsurance);
		insuranceBengBuService.crawlerUnemployment(insuranceRequestParameters,taskInsurance);
		insuranceBengBuService.crawlerInjury(insuranceRequestParameters,taskInsurance);
		insuranceBengBuService.crawlerMaternity(insuranceRequestParameters,taskInsurance);
//		System.out.println("1212121212121212121221212"+taskInsurance.toString());
		return taskInsurance;
	}

	

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		
		return null;
	}






}
