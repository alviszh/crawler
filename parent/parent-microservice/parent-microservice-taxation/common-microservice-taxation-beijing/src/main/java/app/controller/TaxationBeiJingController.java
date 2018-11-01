package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.taxation.json.TaxationRequestParameters;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;
import com.microservice.dao.repository.crawler.taxation.basic.TaskTaxationRepository;

import app.commontracerlog.TracerLog;
import app.service.TaxationBeiJingService;

@RestController   
@Configuration
@RequestMapping("/taxation/beijing")
public class TaxationBeiJingController {
      
	@Autowired
	private TaxationBeiJingService taxationBeiJingService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskTaxationRepository taskTaxationRepository;
	  
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TaskTaxation taxationLogin(@RequestBody TaxationRequestParameters taxationRequestParameters){
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());	
		tracer.addTag("action.housingFund.beijing.login.start",taxationRequestParameters.getTaskId());
		taskTaxation = taxationBeiJingService.login(taxationRequestParameters);
		return taskTaxation;
		
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskTaxation taxationCrawler(@RequestBody TaxationRequestParameters taxationRequestParameters){
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());	
		tracer.addTag("action.housingFund.beijing.crawler.start",taxationRequestParameters.getTaskId());
		taskTaxation = taxationBeiJingService.getAllData(taxationRequestParameters);
		return taskTaxation;
		
	}
}
