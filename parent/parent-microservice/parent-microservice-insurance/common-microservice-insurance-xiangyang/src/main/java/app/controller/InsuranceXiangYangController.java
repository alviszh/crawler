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
import app.service.InsuranceXiangYangService;

@RestController
@RequestMapping("/insurance/xiangyang")
public class InsuranceXiangYangController {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceXiangYangService insuranceXiangYangService;
	
	@PostMapping(value = "/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter){
		tracer.addTag("action.login.taskid", parameter.getTaskId());
		tracer.addTag("InsuranceXiangYangController.login:开始登录", parameter.toString());
		// 通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		taskInsurance.setCity(parameter.getCity());
		// 更新task表 为 登录 进行中
		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		// 执行登录业务方法
		insuranceXiangYangService.login(parameter, taskInsurance);
		return taskInsurance;
	}
	
	@PostMapping(value = "/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.crawler.taskid", parameter.getTaskId());
		tracer.addTag("InsuranceXiangYangController.crawler:检测Task", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		if ("CRAWLER".equals(taskInsurance.getPhase()) && "DOING".equals(taskInsurance.getPhase_status())) {
			tracer.addTag("正在进行上次未完成的爬取任务。。。", taskInsurance.toString());
			return taskInsurance;
		}

		tracer.addTag("InsuranceXiangYangController.crawler:开始爬取", parameter.toString());
		// 更新task表 为 爬取 进行中
		taskInsurance = insuranceService.changeCrawlerStatusDoing(parameter);
		// 执行业务
		insuranceXiangYangService.getcrawler(parameter, taskInsurance);
		return taskInsurance;
	}
	
}
