package app.service.aop;

import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;

public interface CarInsuranceVerify extends CarInsuranceCrawler{
	
	/**
	 * 信息验证
	 * */
	public TaskCarInsurance verify(CarInsuranceRequestBean carInsuranceRequestBean);

}
