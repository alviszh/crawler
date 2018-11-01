package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import app.service.InsuranceJiAnForMedicalService;
import app.service.InsuranceJiAnForPersionService;

@RestController
@Configuration
@RequestMapping("/insurance/jian") 
public class InsuranceJiAnController {
public static final Logger log = LoggerFactory.getLogger(InsuranceJiAnController.class);
	
	@Autowired
	private InsuranceJiAnForMedicalService insuranceJiAnForMedicalService;
	@Autowired
	private InsuranceJiAnForPersionService insuranceJiAnForPersionService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/loginForYanglao")
	public TaskInsurance loginForPersion(@RequestBody InsuranceRequestParameters insuranceRequestParameters){		
	    tracer.addTag("InsuranceJiAnController.loginForPersion.login",insuranceRequestParameters.getTaskId());		
		TaskInsurance taskInsurance = null;
		try {
		    taskInsurance=insuranceJiAnForPersionService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}	
	
	@PostMapping(value="/crawlerForYanglao")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("InsuranceJiAnController.crawlerForYanglao.crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance.setShiyeStatus(201);
		taskInsurance.setShengyuStatus(201);
		taskInsurance.setGongshangStatus(201);
		taskInsurance.setYiliaoStatus(201);
		taskInsuranceRepository.save(taskInsurance);
		taskInsurance = insuranceJiAnForPersionService.getAllData(insuranceRequestParameters);
		return taskInsurance;
	}
	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/loginForMedical")
	public TaskInsurance loginForMedical(@RequestBody InsuranceRequestParameters insuranceRequestParameters){		
	    tracer.addTag("InsuranceJiAnController.loginForMedical.login",insuranceRequestParameters.getTaskId());		
		TaskInsurance taskInsurance = null;	
		try {
			taskInsurance=insuranceJiAnForMedicalService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}	
	
	@PostMapping(value="/crawlerForMedical")
	public TaskInsurance crawlerForMedical(@RequestBody InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceJiAnController.crawlerForMedical.crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance.setUserInfoStatus(201);
		taskInsurance.setShiyeStatus(201);
		taskInsurance.setShengyuStatus(201);
		taskInsurance.setGongshangStatus(201);
		taskInsurance.setYanglaoStatus(201);
		taskInsuranceRepository.save(taskInsurance);
		taskInsurance = insuranceJiAnForMedicalService.getAllData(insuranceRequestParameters);
		return taskInsurance;

	}
}
