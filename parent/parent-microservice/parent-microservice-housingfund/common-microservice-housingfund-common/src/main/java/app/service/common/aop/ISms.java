package app.service.common.aop;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

/**
 * 继承登录接口，如果运营商有登录、爬取、短信验证码（无二次短信验证码），可直接实现此接口
 * 
 * 
 * */
public interface ISms {
	
	/**
	 * 发送短信接口
	 * 
	 * 
	 * */
	public TaskHousing sendSms(MessageLoginForHousing messageLoginForHousing);
	
	/**
	 * 效验短信接口
	 * 
	 * 
	 * */
	public TaskHousing verifySms(MessageLoginForHousing messageLoginForHousing);

}
