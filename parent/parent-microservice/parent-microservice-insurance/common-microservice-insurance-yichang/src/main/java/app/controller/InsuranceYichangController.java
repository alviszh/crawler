package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.AsyncYichangGetAllDataService;
import app.service.InsuranceService;
import app.service.InsuranceYichangService;


@RestController
@Configuration
@RequestMapping("/insurance/yichang") 
public class InsuranceYichangController {

	@Autowired
	private InsuranceYichangService insuranceYichangService;
	@Autowired
	private AsyncYichangGetAllDataService asyncYichangGetAllDataService;
	
	@Autowired
	private TracerLog tracer;
	 
	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.qryKeyValue("parser.login.taskid",insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.login.auth",insuranceRequestParameters.getUsername());
		
		try {
			insuranceYichangService.login(insuranceRequestParameters);
		} catch (Exception e) {
			tracer.addTag("InsuranceYichangController.login:" , insuranceRequestParameters.getTaskId()+"---ERROR:"+e);
			e.printStackTrace();
		}

		return null;
		
	}
	
	/**
	 * 获取数据
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.qryKeyValue("parser.crawler.taskid",insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.crawler.auth",insuranceRequestParameters.getUsername());
		
		asyncYichangGetAllDataService.getAllData(insuranceRequestParameters);
		return null;
		
	}
	
}
