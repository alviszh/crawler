package app.service.aop;

import org.openqa.selenium.WebDriver;

import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;

public interface IAgent { 
	 
	/**
	 * 释放资源  （释放这台电脑 by IP，关闭WebDriver）
	 */
	public void releaseInstance(String instanceIpAddr, WebDriver driver);
	
	/**
	 * @Des POST代理请求的封装   用于登录
	 * @param e_CommerceJsonBean
	 * @param requestPath 登录的请求路径
	 */
	public E_CommerceTask postAgent(E_CommerceJsonBean e_CommerceJsonBean,String requestPath);
	
	/**
	 * @Des POST代理请求的封装   用于登录,可自定义intervalTime（自动关闭时间）长度，默认2分钟。
	 * @param e_CommerceJsonBean
	 * @param requestPath 登录的请求路径
	 */
	public E_CommerceTask postAgent(E_CommerceJsonBean e_CommerceJsonBean,String requestPath,Long intervalTime);
	
	/**
	 * @Des POST代理请求的封装   用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作
	 * @param e_CommerceJsonBean
	 * @param requestPath 登录的请求路径
	 */
	public E_CommerceTask postAgentCombo(E_CommerceJsonBean e_CommerceJsonBean,String requestPath);
}
