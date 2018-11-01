package app.service.aop;

import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;

/**
 * 爬取接口，适用于登录就可以直接爬取的情况（例如地区的电信运营商可以跳过短信验证码的情况），可直接实现此接口
 * 
 * 
 * */
public interface ICrawlerLogin extends ICrawler{
	
	/**
	 * 登录接口
	 * 
	 * 
	 * */
	public E_CommerceTask login(E_CommerceJsonBean e_CommerceJsonBean);
	
	/**
	 * 获取二维码接口
	 * */
	public E_CommerceTask getQRcode(E_CommerceJsonBean e_CommerceJsonBean);
	
	/**
	 * 验证二维码接口
	 * */
	public E_CommerceTask checkQRcode(E_CommerceJsonBean e_CommerceJsonBean);

}
