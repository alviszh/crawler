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
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.huaian" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.huaian" })
public class InsuranceHuaiAnCommonService implements InsuranceLogin{

	@Autowired
	private InsuranceHuaiAnService insuranceHuaiAnService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceHuaiAnService.login(insuranceRequestParameters,taskInsurance);
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceHuaiAnService.crawlerUserInfo(insuranceRequestParameters,taskInsurance);
		insuranceHuaiAnService.crawlerMedical(insuranceRequestParameters,taskInsurance);
		insuranceHuaiAnService.crawlerEndowment(insuranceRequestParameters,taskInsurance);
		insuranceHuaiAnService.crawlerInjury(insuranceRequestParameters,taskInsurance);
		insuranceHuaiAnService.crawlerUnemployment(insuranceRequestParameters,taskInsurance);
		insuranceHuaiAnService.crawlerMaternity(insuranceRequestParameters,taskInsurance);
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
