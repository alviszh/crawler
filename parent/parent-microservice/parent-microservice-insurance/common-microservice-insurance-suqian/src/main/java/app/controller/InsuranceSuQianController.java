package app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.InsuranceService;
import app.service.InsuranceSuQianService;



@RestController
@Configuration
@RequestMapping("/insurance/suqian") 
public class InsuranceSuQianController extends InsuranceService {

    @Autowired
    private TracerLog tracerLog;
    
    @Autowired
    private InsuranceSuQianService insuranceSuQianService;
    
    @Autowired
    private AgentService agentService;
    
    @PostMapping(path = "/loginAgent")
  	public TaskInsurance loginAgent(@RequestBody InsuranceRequestParameters insuranceRequestParameters)
  			throws IllegalAccessException, Exception {

  		tracerLog.output("taskid", insuranceRequestParameters.getTaskId());


  		TaskInsurance taskInsurance = findTaskInsurance(insuranceRequestParameters.getTaskId());

  		try {
  			taskInsurance = agentService.postAgent(insuranceRequestParameters, "/insurance/suqian/crawler", 300000L);

  		} catch (Exception e) {
  			e.printStackTrace();
  			tracerLog.qryKeyValue("RuntimeException", e.toString());
  		}

  		return taskInsurance;
  	}
	
    
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskInsurance> crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracerLog.output("taskid", insuranceRequestParameters.getTaskId());
		tracerLog.output("insuranceRequestParameters", insuranceRequestParameters.toString());
		insuranceSuQianService.crawler(insuranceRequestParameters);
		return null;
	}

}
