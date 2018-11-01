package app.service.aop;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

/**
 * 继承登录接口，如果运营商有登录、爬取、短信验证码、二次短信验证码，可直接实现此接口
 * 
 * 
 * */
public interface ISmsTwice extends ISms{
	
	/**
	 * 发送二次短信接口
	 * 
	 * 
	 * */
	public TaskMobile sendSmsTwice(MessageLogin messageLogin);
	
	/**
	 * 效验二次短信接口
	 * 
	 * 
	 * */
	public TaskMobile verifySmsTwice(MessageLogin messageLogin);

}
