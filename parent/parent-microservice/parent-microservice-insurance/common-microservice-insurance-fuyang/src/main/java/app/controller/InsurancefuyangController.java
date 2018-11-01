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
import app.service.InsurancefuyangService;

/**
 * 滨州社保 Controller
 * 
 * @author qizhongbin
 *
 */
@RestController
@RequestMapping("/insurance/fuyang")
public class InsurancefuyangController {
	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsurancefuyangService insurancefuyangService;

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;

	/**
	 * 登录 接口
	 * 
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter) {
		// 通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		taskInsurance.setCity(parameter.getCity());
		tracer.addTag("action.login.taskid", parameter.getTaskId());
		tracer.addTag("InsurancefuyangController.login:开始登录", parameter.toString());
		// 执行登录业务方法
		insurancefuyangService.login(parameter);
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
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		tracer.addTag("action.crawler.taskid", parameter.getTaskId());
		tracer.addTag("InsurancefuyangController.crawler:检测Task", parameter.toString());
		// 执行业务
		insurancefuyangService.getAllData(parameter);
		return taskInsurance;
	}
}
