package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.taxation.json.TaxationJsonBean;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;

import app.commontracerlog.TracerLog;
import app.service.TaxationTaskService;

@RestController
@RequestMapping("/taxation")
public class TaxationTaskController {

	public static final Logger log = LoggerFactory.getLogger(TaxationTaskController.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaxationTaskService taxationTaskService;
	
	@PostMapping(path = "/tasks/check")
	public TaskTaxation createTask(@RequestBody TaxationJsonBean taxationJsonBean){
		
		tracer.addTag("TaxationJsonBean =======>>",taxationJsonBean.toString());
		taxationJsonBean = taxationTaskService.checkUser(taxationJsonBean); 
		return taxationTaskService.createTask(taxationJsonBean);			
	
	}
	
	
	@GetMapping(path="/tasks/{taskid}/status")
	public TaskTaxation taskStatus(@PathVariable String taskid){
		
		TaskTaxation taskTaxation = taxationTaskService.getTaskTaxation(taskid);
		tracer.addTag("Request task status", "taskid:"+taskid); 
		return taskTaxation;
		
	}
}
