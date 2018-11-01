package app.service.aop;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

/**
 * 爬取接口，适用于无需登录就可以直接爬取的情况（例如部分地区的社保、公积金），可直接实现此接口
 * 
 * 因为强授权爬虫基本上都是异步爬取，因此不能通过对爬取方法的before、after来判断爬取开始和结束，因此拆分成两个接口，getAllData表示爬取开始，getAllDataDone表示爬取完成
 * */
public interface ICrawler { 
	 
	/**
	 * 开始爬取
	 * 
	 * 
	 * */
	public TaskMobile getAllData(MessageLogin messageLogin);
	
	
	/**
	 * 完成爬取
	 * 
	 * 
	 * */
	public TaskMobile getAllDataDone(String taskId);
}
