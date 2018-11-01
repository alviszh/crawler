package app.service.aop;

import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;

public interface CarInsuranceSms {
	
	/**
	 * 发送手机验证码接口
	 * */
	public TaskCarInsurance sendSms(CarInsuranceRequestBean carInsuranceRequestBean);
	
	/**
	 * 效验短信接口
	 * */
	public TaskCarInsurance verifySms(CarInsuranceRequestBean carInsuranceRequestBean);

}
