package app.service.aop;

import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;

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
	public E_CommerceTask sendSms(E_CommerceJsonBean e_CommerceJsonBean);
	
	/**
	 * 效验短信接口
	 * 
	 * 
	 * */
	public E_CommerceTask verifySms(E_CommerceJsonBean e_CommerceJsonBean);

}
