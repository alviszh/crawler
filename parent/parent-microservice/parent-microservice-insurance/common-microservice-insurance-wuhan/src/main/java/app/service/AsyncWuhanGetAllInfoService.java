package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;

@Component
public class AsyncWuhanGetAllInfoService {
	
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private InsuranceWuhanService insuranceChangchunService;
	
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	
	
	@Async
	public void getAllInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		tracer.addTag("AsyncChangchunGetAllInfoService.crawler.getAllInfo", insuranceRequestParameters.getTaskId());
		
		tracer.addTag("parser.crawler.taskid",insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.crawler.auth",insuranceRequestParameters.getUsername());
		try {
			//获取个人参保信息
			insuranceChangchunService.getPersonalInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//获取五险信息
			insuranceChangchunService.getAllInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//获取个人信息
			insuranceChangchunService.getUserInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
}

}
