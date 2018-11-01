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
import app.service.InsuranceShenzhenService;

/**
 * 深圳社保 Controller
 * @author rongshengxu
 *
 */
@RestController
@RequestMapping("/insurance/shenzhen")
public class InsuranceShenzhenController {
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private InsuranceShenzhenService insuranceShenzhenService;
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	/**
	 * 登录 接口
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter){
		tracer.addTag("InsuranceShenzhenController.login:开始登录",parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		insuranceShenzhenService.login(parameter);
		return taskInsurance;
	}
	
	/**
	 * 爬取 接口
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter){
		tracer.addTag("InsuranceShenzhenController.crawler:检测Task",parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		insuranceShenzhenService.getAllData(parameter);
		return taskInsurance;
				
	}
	
}
