package app.service.aop.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;
import app.commontracerlog.TracerLog;
import app.service.aop.ICrawler;

@Component
public class CrawlerImpl implements ICrawler{
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private E_CommerceTaskRepository e_CommerceTaskRepository;
	
	@Override
	public E_CommerceTask getAllDataDone(String  taskId) {
		tracer.addTag("AbstractCrawlerSmsTwiceTracer getAllDataDone",taskId);
		E_CommerceTask commerceTask = e_CommerceTaskRepository.findByTaskid(taskId); 
		return commerceTask; 
	}

	@Override
	public E_CommerceTask getAllData(E_CommerceJsonBean e_CommerceJsonBean) {
		// TODO Auto-generated method stub
		return null;
	}

}
