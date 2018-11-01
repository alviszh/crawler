package app.service.aop.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceCrawler;

@Component
public class InsuranceCrawlerImpl implements InsuranceCrawler{
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		tracer.addTag("AbstractCrawlerTracer getAllDataDone",taskId);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(taskId); 
		return taskInsurance;
	}

}
