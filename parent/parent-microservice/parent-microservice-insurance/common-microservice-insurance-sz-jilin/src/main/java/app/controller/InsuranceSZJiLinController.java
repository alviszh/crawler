package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.InsuranceSZJiLinService;
import app.service.InsuranceService;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

@RestController
@RequestMapping("/insurance/szjilin")
public class InsuranceSZJiLinController {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZJiLinService insuranceSZJiLinService;
	@Autowired
	private InsuranceLogin insuranceLogin;
	@PostMapping(value = "/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.login.taskid", parameter.getTaskId());
		tracer.addTag("InsurancebaishanController.login:开始登录", parameter.toString());
	//	TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		TaskInsurance taskInsurance = insuranceLogin.login(parameter);
		return taskInsurance;
	}
	
	@PostMapping(value = "/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.crawler.taskid", parameter.getTaskId());
		tracer.addTag("InsurancejilinController.crawler:检测Task", parameter.toString());
	//	TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());

		tracer.addTag("InsurancejilinController.crawler:开始爬取", parameter.toString());
		// 执行业务
		TaskInsurance taskInsurance = insuranceLogin.getAllData(parameter);
		return taskInsurance;
	}
	
}
