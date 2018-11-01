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
import app.service.InsuranceLiaochengService;


@RestController
@Configuration
@RequestMapping("/insurance/liaocheng") 
public class InsuranceLiaochengController {

	@Autowired
	private InsuranceLiaochengService insuranceLiaochengService;
	
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
			insuranceLiaochengService.getAllData(insuranceRequestParameters);
		} catch (Exception e) {
			tracer.addTag("InsuranceLiaochengController.login:" , insuranceRequestParameters.getTaskId()+"---ERROR:"+e);
			e.printStackTrace();
		}
		return null;
		
	}
	
}
