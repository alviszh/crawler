package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.InsuranceNanchangGetAllDataService;
import app.service.InsuranceNanchangService;
import app.service.InsuranceService;

@RestController
@Configuration
@RequestMapping("/insurance/nanchang")
public class InsuranceNanchangController {

	@Autowired
	private InsuranceNanchangService insuranceNanchangService;
	@Autowired
	private InsuranceNanchangGetAllDataService insuranceNanchangGetAllDataService;

	@Autowired
	private InsuranceService insuranceService;

	@Autowired
	private TracerLog tracer;

	/**
	 * 登录
	 * 
	 * @param insuranceRequestParameters
	 * @return
	 */
	// 使用task表中pid字段传递url
	@PostMapping(value = "/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters) {

		tracer.qryKeyValue("parser.login.taskid", insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.login.auth", insuranceRequestParameters.getUsername());

		insuranceNanchangService.login(insuranceRequestParameters);

		return null;

	}

	/**
	 * 获取数据
	 * 
	 * @param insuranceRequestParameters
	 * @return
	 */

	// 使用task表中pid字段传递url

	@PostMapping(value = "/getAllData")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) {

		tracer.qryKeyValue("parser.crawler.taskid", insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.crawler.auth", insuranceRequestParameters.getUsername());

		insuranceNanchangGetAllDataService.getAllData(insuranceRequestParameters);

		return null;
	}

	@GetMapping(value = "/hystrix")
	public String hystrix() {

		return insuranceNanchangService.hystrix();

	}

}
