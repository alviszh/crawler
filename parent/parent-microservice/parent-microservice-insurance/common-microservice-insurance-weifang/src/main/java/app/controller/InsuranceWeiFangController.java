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
import app.service.AgentService;
import app.service.InsuranceWeiFangService;

/**
 * 济南社保 Controller
 * 
 * @author qizhongbin
 *
 */
@RestController
@RequestMapping("/insurance/weifang")
public class InsuranceWeiFangController {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceWeiFangService insuranceWeiFangService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private AgentService agentService;
	
	@PostMapping(path = "/loginAgent")
	public TaskInsurance loginAgent(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("crawler.insurance.login", insuranceRequestParameters.getTaskId());   
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance =  agentService.postAgent(insuranceRequestParameters, "/insurance/huizhou/crawler"); 
		} catch (RuntimeException e) {
			tracer.qryKeyValue("RuntimeException", e.toString());
		}
		return taskInsurance;
	}
	 /**
	 * 登录 接口
	 * 
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.login.taskid", parameter.getTaskId());
		tracer.addTag("InsuranceWeiFangController.login:开始登录", parameter.toString());
		// 通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		taskInsurance.setCity(parameter.getCity());
		// 执行登录业务方法
		taskInsurance=insuranceWeiFangService.login(parameter);
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
		tracer.addTag("InsuranceWeiFangController.crawler:检测Task", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 执行业务
		insuranceWeiFangService.getAllData(parameter);
		return taskInsurance;
	}
	
	
	@PostMapping(path = "/quit")
	public TaskInsurance quit(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance = insuranceWeiFangService.quitDriver(insuranceRequestParameters);
		return taskInsurance;
	}
	

}
