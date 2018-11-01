package app.service.aop;

import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;

public interface CarInsuranceCrawler {
	
	/**
	 * 开始爬取
	 * 
	 * 
	 * */
	public TaskCarInsurance getAllData(CarInsuranceRequestBean carInsuranceRequestBean);
	
	/**
	 * 完成爬取
	 * 
	 * 
	 * */
	public TaskCarInsurance getAllDataDone(String taskid);

}
