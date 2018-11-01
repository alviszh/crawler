package app.service.aop.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.taxation.json.TaxationRequestParameters;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;
import com.microservice.dao.repository.crawler.taxation.basic.TaskTaxationRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawler;

@Component
public class CrawlerImpl implements ICrawler{
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private TaskTaxationRepository taskTaxationRepository;
	
	@Override
	public TaskTaxation getAllDataDone(String  taskId) {
		tracer.addTag("AbstractCrawlerTracer getAllDataDone",taskId);
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taskId); 
		return taskTaxation; 
	}

	@Override
	public TaskTaxation getAllData(TaxationRequestParameters taxationRequestParameters) {
		// TODO Auto-generated method stub
		return null;
	}




}
