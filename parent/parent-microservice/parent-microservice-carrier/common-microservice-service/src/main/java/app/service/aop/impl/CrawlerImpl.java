package app.service.aop.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawler;

@Component
public class CrawlerImpl implements ICrawler{
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	
	@Override
	public TaskMobile getAllDataDone(String  taskId) {
		tracer.addTag("AbstractCrawlerSmsTwiceTracer getAllDataDone",taskId);
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(taskId); 
		return taskMobile; 
	}

	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

}
