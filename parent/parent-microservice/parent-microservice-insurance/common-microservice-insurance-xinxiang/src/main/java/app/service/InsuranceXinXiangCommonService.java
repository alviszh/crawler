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
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.xinxiang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.xinxiang" })
public class InsuranceXinXiangCommonService implements InsuranceLogin{

	@Autowired
	private InsuranceXinXiangService insuranceXinXiangService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceXinXiangService.login(insuranceRequestParameters,taskInsurance);		
		return taskInsurance;
	}
	
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceXinXiangService.crawlerUserInfo(insuranceRequestParameters,taskInsurance);		
		insuranceXinXiangService.crawlerMedical(insuranceRequestParameters,taskInsurance);		
		insuranceXinXiangService.crawlerEndowment(insuranceRequestParameters,taskInsurance);		
		insuranceXinXiangService.crawlerUnemplyment(insuranceRequestParameters,taskInsurance);		
		
		insuranceXinXiangService.crawlerInjury(insuranceRequestParameters,taskInsurance);	
		insuranceXinXiangService.crawlerMaternity(insuranceRequestParameters,taskInsurance);
		return taskInsurance;
	}


	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}
