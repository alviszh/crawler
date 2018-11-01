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
import app.service.InsuranceXinJiangService;

@RestController
@RequestMapping("/insurance/szxinjiang")
public class InsuranceXinJiangController {
	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceXinJiangService insuranceXinJiangService;

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;


	/**
	 * 登录 接口
	 * 
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.crawler.taskid", parameter.getTaskId());
		tracer.addTag("InsuranceXinJiangController.crawler:开始爬取", parameter.toString());
		// 通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 执行登录业务方法
		insuranceXinJiangService.getAllData(parameter);

		return taskInsurance;
	}

}
