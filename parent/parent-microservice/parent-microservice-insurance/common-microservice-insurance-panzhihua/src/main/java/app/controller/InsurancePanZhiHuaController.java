package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@RestController
@RequestMapping("/insurance/panzhihua")
public class InsurancePanZhiHuaController {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceLogin insuranceLogin;
	@PostMapping(value = "/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.login.taskid", parameter.getTaskId());
		tracer.addTag("InsurancejiyuanController.login:开始登录", parameter.toString());
		// 通过taskid查出对应表中的数据
//		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 执行登录业务方法
		TaskInsurance taskInsurance = insuranceLogin.login(parameter);
		return taskInsurance;
	}
	
	@PostMapping(value = "/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.crawler.taskid", parameter.getTaskId());
		tracer.addTag("InsurancejiyuanController.crawler:检测Task", parameter.toString());
		tracer.addTag("InsurancejiyuanController.crawler:开始爬取", parameter.toString());
		// 执行业务
		TaskInsurance taskInsurance = insuranceLogin.getAllData(parameter);
		return taskInsurance;
	}
	
}
