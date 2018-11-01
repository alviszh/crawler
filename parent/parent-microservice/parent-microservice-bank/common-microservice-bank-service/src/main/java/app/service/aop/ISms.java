package app.service.aop;

import com.crawler.bank.json.BankJsonBean;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

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
	public TaskBank sendSms(BankJsonBean bankJsonBean);
	
	/**
	 * 效验短信接口
	 * 
	 * 
	 * */
	public TaskBank verifySms(BankJsonBean bankJsonBean);

}
