package app.service.aop;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

public interface InsuranceSms {

	/**
	 * 发送手机验证码接口
	 * */
	public TaskInsurance sendSms(InsuranceRequestParameters insuranceRequestParameters);
	
	/**
	 * 效验短信接口
	 * */
	public TaskInsurance verifySms(InsuranceRequestParameters insuranceRequestParameters);
}
