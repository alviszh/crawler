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
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.heilongjiang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.heilongjiang" })
public class InsuranceHeiLongJiangCommonService implements InsuranceLogin{

	@Autowired
	private InsuranceHeiLongJiangService insuranceHeiLongJiangService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

		insuranceHeiLongJiangService.getMedical(insuranceRequestParameters);
		insuranceHeiLongJiangService.getEndowment(insuranceRequestParameters);
		insuranceHeiLongJiangService.getUnemployment(insuranceRequestParameters);
		insuranceHeiLongJiangService.getInjury(insuranceRequestParameters);
		insuranceHeiLongJiangService.getMaternity(insuranceRequestParameters);
		insuranceHeiLongJiangService.getUserInfo(insuranceRequestParameters);
		return taskInsurance;
	}
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

		insuranceHeiLongJiangService.login(insuranceRequestParameters);
		return taskInsurance;
		
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
