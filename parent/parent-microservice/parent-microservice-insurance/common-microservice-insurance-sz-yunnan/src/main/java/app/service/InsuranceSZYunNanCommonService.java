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
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.yunnan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.yunnan" })
public class InsuranceSZYunNanCommonService implements InsuranceLogin{

	@Autowired
	private InsuranceSZYunNanService insuranceSZYunNanService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceSZYunNanService.MedicalLogin(insuranceRequestParameters,taskInsurance);
		insuranceSZYunNanService.EndowmentLogin(insuranceRequestParameters,taskInsurance);
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceSZYunNanService.crawlerUserInfo(insuranceRequestParameters,taskInsurance);
		insuranceSZYunNanService.crawlerMedical(insuranceRequestParameters,taskInsurance);
		
		insuranceSZYunNanService.crawlerInjury(insuranceRequestParameters,taskInsurance);
		insuranceSZYunNanService.crawlerEndowment(insuranceRequestParameters,taskInsurance);
		insuranceSZYunNanService.crawlerMaternity(insuranceRequestParameters,taskInsurance);
		insuranceSZYunNanService.crawlerUnemployment(insuranceRequestParameters,taskInsurance);
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
