package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.AsyncGuangzhouGetAllDataService;
import app.service.InsuranceService;

@RestController
@Configuration
@RequestMapping("/insurance")
public class InsuranceGuangZhouController {

	@Autowired
	private AsyncGuangzhouGetAllDataService asyncGuangzhouGetAllDataService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer; 
	
	@PostMapping(value="/guangzhou")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("parser.login",insuranceRequestParameters.getTaskId());
		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceService.changeLoginStatusDoing(taskInsurance);
		tracer.addTag("parser.login.taskInsurance", taskInsurance.toString());
		try {
			asyncGuangzhouGetAllDataService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.login.Exception", e.toString());
		}
		
		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.login.taskInsurance", taskInsurance.toString());
		
		return taskInsurance;
	}
	
	@PostMapping(value="/guangzhou/sendSMS")
	public TaskInsurance sendSMS(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("parser.sendSMS",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_DOING.getPhase(), InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_DOING.getPhasestatus(), InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_DOING.getDescription(), taskInsurance);
			asyncGuangzhouGetAllDataService.sendSms(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.sendSMS.Exception", e.toString());
		}
		
		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.sendSMS.taskInsurance", taskInsurance.toString());
		return taskInsurance;
	}
	
	@PostMapping(value="/guangzhou/checkSMS")
	public TaskInsurance checkSMS(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("crawler.checkSMS.taskid",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_SMS_VALIDATE_DOING.getPhase(), InsuranceStatusCode.INSURANCE_SMS_VALIDATE_DOING.getPhasestatus(), InsuranceStatusCode.INSURANCE_SMS_VALIDATE_DOING.getDescription(), taskInsurance);
			asyncGuangzhouGetAllDataService.verifySms(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.checkSMS.Exception", e.toString());
		}
		
		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		tracer.addTag("crawler.checkSMS.taskInsurance", taskInsurance.toString());
		return taskInsurance;
	}
	
	@PostMapping(value="/guangzhou/getData")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("parser.crawler"," ----crawler----");
		TaskInsurance taskInsurance = asyncGuangzhouGetAllDataService.updateTaskInsurance(insuranceRequestParameters);		
		try {
			asyncGuangzhouGetAllDataService.getAllData(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
		
	}
}
