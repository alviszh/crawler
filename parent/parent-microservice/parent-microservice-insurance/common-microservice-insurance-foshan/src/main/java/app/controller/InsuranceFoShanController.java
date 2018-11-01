package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.AsyncFoshanGetAllDataService;
import app.service.InsuranceService;

@RestController
@Configuration
@RequestMapping("/insurance")
public class InsuranceFoShanController {

	@Autowired
	private AsyncFoshanGetAllDataService asyncFoshanGetAllDataService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer; 
	
	
	@PostMapping(value="/foshan")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("parser.login",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceService.changeLoginStatusDoing(taskInsurance);
		tracer.addTag("parser.login.taskInsurance", taskInsurance.toString());
		try {
			asyncFoshanGetAllDataService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		return taskInsurance;
	}
	
	@PostMapping(value="/foshan/getData")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		
		tracer.addTag("parser.crawler"," ----crawler----");
		tracer.qryKeyValue("taskid", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = asyncFoshanGetAllDataService.updateTaskInsurance(insuranceRequestParameters);		
		if(asyncFoshanGetAllDataService.isLogin(insuranceRequestParameters)){
			asyncFoshanGetAllDataService.getAllData(insuranceRequestParameters);
		}else{
			taskInsurance = new TaskInsurance();
			taskInsurance.setPhase("CRAWLER");
			taskInsurance.setPhase_status("ERROR");
			taskInsurance.setDescription("网络异常，请您稍候重试");
		}
	
		return taskInsurance;
		
	}
}
