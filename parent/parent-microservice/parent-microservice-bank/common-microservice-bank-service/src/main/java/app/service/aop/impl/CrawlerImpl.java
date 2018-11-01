package app.service.aop.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.crawler.bank.json.BankJsonBean;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawler;

@Component
@Repository
public class CrawlerImpl implements ICrawler{
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private TaskBankRepository taskBankRepository;
	
	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		return null;
	}

	@Override
	public TaskBank getAllDataDone(String taskId) {
		tracer.addTag("AbstractCrawlerSmsTwiceTracer getAllDataDone",taskId);
		TaskBank taskBank = taskBankRepository.findByTaskid(taskId); 
		return taskBank; 
	}

}
