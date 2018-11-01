package app.service.common.aop.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.commontracerlog.TracerLog;
import app.service.common.aop.ICrawler;

@Component
public class CrawlerImpl implements ICrawler{
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	

	@Override
	public TaskHousing getAllDataDone(String  taskId) {
		tracer.addTag("AbstractCrawlerSmsTwiceTracer getAllDataDone",taskId);
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(taskId); 
		return taskHousing; 
	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

}
