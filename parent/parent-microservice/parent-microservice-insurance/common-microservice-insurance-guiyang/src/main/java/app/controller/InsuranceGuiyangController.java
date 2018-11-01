package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.service.InsuranceGuiyangService;

/**
 * 贵阳社保 Controller
 * 
 * @author qizhongbin
 *
 */
@RestController
@RequestMapping("/insurance/guiyang")
public class InsuranceGuiyangController {
	@Autowired
	private app.commontracerlog.TracerLog tracer;

	@Autowired
	private InsuranceGuiyangService insuranceGuiyangService;

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;

	@Value("${spring.application.name}")
	String appName;

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

		tracer.addTag("parser.login.taskid", parameter.getTaskId());
		tracer.addTag("InsuranceGuiyangController.login:开始登录", parameter.toString());
		// 执行登录业务方法
		insuranceGuiyangService.login(parameter);
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
		tracer.addTag("parser.crawler.taskid", parameter.getTaskId());
		tracer.addTag("InsuranceGuiyangController.crawler:检测Task", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 执行业务
		insuranceGuiyangService.getAllData(parameter);
		return taskInsurance;
	}
}
