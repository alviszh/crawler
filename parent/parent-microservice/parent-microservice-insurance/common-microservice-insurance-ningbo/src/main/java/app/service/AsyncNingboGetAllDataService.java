package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.ningbo" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.ningbo" })
public class AsyncNingboGetAllDataService implements InsuranceLogin{
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceNingBoService insuranceNingBoService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
//	/**
//	 * @Des 爬取总方法
//	 * @param insuranceRequestParameters
//	 */
//	@Async
//	public void getAllData(InsuranceRequestParameters insuranceRequestParameters){
//		tracer.addTag("AsyncNingboGetAllDataService getAllData", insuranceRequestParameters.getTaskId());
//		try {
//			//insuranceNingBoService.getUserInfo(insuranceRequestParameters);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		
//	}
//
//	
//	public void loginCrawler(InsuranceRequestParameters insuranceRequestParameters)throws Exception {
//		insuranceNingBoService.getUserInfo(insuranceRequestParameters);
//		insuranceNingBoService.getMedicalInfo(insuranceRequestParameters);
//		insuranceNingBoService.getEndowmentInfo(insuranceRequestParameters);
//		insuranceNingBoService.getHurtInfo(insuranceRequestParameters);
//		insuranceNingBoService.getBearInfo(insuranceRequestParameters);
//		insuranceNingBoService.getLostInfo(insuranceRequestParameters);
//		
//	}

	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceNingBoService.getUserInfo(insuranceRequestParameters);
		insuranceNingBoService.getMedicalInfo(insuranceRequestParameters);
		insuranceNingBoService.getEndowmentInfo(insuranceRequestParameters);
		insuranceNingBoService.getHurtInfo(insuranceRequestParameters);
		insuranceNingBoService.getBearInfo(insuranceRequestParameters);
		insuranceNingBoService.getLostInfo(insuranceRequestParameters);
		return taskInsurance;
	}


	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceNingBoService.login(insuranceRequestParameters);
		return taskInsurance;
	}

}
