package app.service.aop;

import com.crawler.bank.json.BankJsonBean;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

public interface ICrawler { 
	 
	/**
	 * 开始爬取
	 * 
	 * 
	 * */
	public TaskBank getAllData(BankJsonBean bankJsonBean);
	
	
	/**
	 * 完成爬取
	 * 
	 * 
	 * */
	public TaskBank getAllDataDone(String taskId);
}
