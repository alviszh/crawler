package app.service.aop;

import com.crawler.taxation.json.TaxationRequestParameters;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;

public interface ICrawler {
	
	/**
	 * 开始爬取
	 * 
	 * 
	 * */
	public TaskTaxation getAllData(TaxationRequestParameters taxationRequestParameters);
	
	
	/**
	 * 完成爬取
	 * 
	 * 
	 * */
	public TaskTaxation getAllDataDone(String taskId);
	
}
