package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.InsuranceService;
import app.service.InsuranceXingTaiFutureService;

@RestController
@RequestMapping("/insurance/xingtai") 
public class InsuranceXingTaiController {
	public static final Logger log = LoggerFactory.getLogger(InsuranceXingTaiController.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceXingTaiFutureService insuranceXingTaiFutureService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.qryKeyValue("InsuranceXingTaiController login", insuranceRequestParameters.getTaskId());
		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		try {
			taskInsurance = insuranceXingTaiFutureService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("登陆异常", e.toString());
			
		}

		return taskInsurance;
		
	}
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		
		tracer.qryKeyValue("InsuranceGuiLinController.crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance = insuranceXingTaiFutureService.getAllData(insuranceRequestParameters);
		} catch (Exception e) {
			tracer.addTag("爬取异常", e.toString());
		}
		return taskInsurance;
		
	}
}
