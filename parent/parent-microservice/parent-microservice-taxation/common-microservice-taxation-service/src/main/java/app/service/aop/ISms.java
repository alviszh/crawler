package app.service.aop;

import com.crawler.taxation.json.TaxationRequestParameters;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;

public interface ISms extends ICrawler{

	/**
	 * 发送手机验证码接口
	 * */
	public TaskTaxation sendSms(TaxationRequestParameters taxationRequestParameters);
	
	/**
	 * 效验短信接口
	 * */
	public TaskTaxation verifySms(TaxationRequestParameters taxationRequestParameters);
}
