package app.service.aop;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

public interface InsuranceCrawler {

	/**
	 * 开始爬取
	 * 
	 * 
	 * */
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters);
	
	
	/**
	 * 完成爬取
	 * 
	 * 
	 * */
	public TaskInsurance getAllDataDone(String taskId);
}
