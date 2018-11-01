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
import app.service.InsuranceLianYunGangService;
import app.service.InsuranceService;



@RestController
@Configuration
@RequestMapping("/insurance/lianyungang") 
public class InsuranceLianYunGangController extends InsuranceService {

    @Autowired
    private TracerLog tracerLog;
    
    @Autowired
    private InsuranceLianYunGangService insuranceLianYunGangService;
    
    @Autowired
    private AgentService agentService;
    
    @PostMapping(path = "/loginAgent")
  	public TaskInsurance loginAgent(@RequestBody InsuranceRequestParameters insuranceRequestParameters)
  			throws IllegalAccessException, Exception {

  		tracerLog.output("taskid", insuranceRequestParameters.getTaskId());


  		TaskInsurance taskInsurance = findTaskInsurance(insuranceRequestParameters.getTaskId());

  		try {
  			taskInsurance = agentService.postAgent(insuranceRequestParameters, "/insurance/lianyungang/crawler", 300000L);

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

		insuranceLianYunGangService.crawler(insuranceRequestParameters);
		return null;
	}

}
