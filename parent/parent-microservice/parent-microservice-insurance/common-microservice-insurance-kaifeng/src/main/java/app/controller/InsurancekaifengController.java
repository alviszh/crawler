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
import app.service.InsuranceService;
import app.service.InsurancekaifengService;


@RestController
@RequestMapping("/insurance/kaifeng")
public class InsurancekaifengController {
	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsurancekaifengService insurancekaifengService;

	@Autowired
	private InsuranceService insuranceService;

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	/**
	 * 登录 接口（用户名，密码登录）
	 * 
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/usernameAndPasswordlogin")
	public TaskInsurance usernameAndPasswordlogin(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.login.taskid", parameter.getTaskId());
		tracer.addTag("InsurancekaifengController.login:开始登录", parameter.toString());
		// 通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		taskInsurance.setCity(parameter.getCity());
		// 执行登录业务方法
		insurancekaifengService.login(parameter);
		return taskInsurance;
	}
	/**
	 * 爬取,解析接口
	 * 
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.crawler.taskid", parameter.getTaskId());
		tracer.addTag("InsurancekaifengController.crawler:检测Task", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 执行业务
		insurancekaifengService.getAllData(parameter);
		return taskInsurance;
	}

	/**
	 * 登录验证码初始化接口
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/verificationCode")
	public TaskInsurance verificationCode(@RequestBody InsuranceRequestParameters parameter) {

		tracer.addTag("InsurancekaifengController.verificationCode:开始获取验证码对应的图片流", null);
		// 通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 发送短信验证码（第二种登陆方式（手机号，短信验证码登录））
		insurancekaifengService.sendSms(parameter);
		return taskInsurance;
	}

}
