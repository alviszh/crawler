package app.service.common;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.crawler.bank.json.IdleInstance;
import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.client.MiddleClient;
import app.commontracerlog.TracerLog;

@Component
public class AgentService {

	@Value("${spring.application.name}")
	String appName;
	
	@Autowired
	private RestTemplate restTemplate; 
	
	@Autowired
	private MiddleClient middleClient;
	
	@Autowired
	private TracerLog tracerLog;
	
	public final static long INTERVAL_TIME =  2 * 60 * 1000;
	
	//释放资源  （释放这台电脑 by IP，关闭WebDriver）
	public void releaseInstance(String instanceIpAddr, WebDriver driver){
		tracerLog.addTag("releaseInstance",instanceIpAddr);
		middleClient.releaseInstance(appName.toUpperCase(), instanceIpAddr);//注意 appName 要大写一下，默认是小写
		if(driver!=null){
			driver.quit();
		}
		
	} 
	
	
	/**   
	  *    
	  * 项目名称：common-microservice-telecom-login  
	  * 所属包名：app.service.common
	  * 类描述：   POST代理请求的封装   用于登录
	  * 创建人：hyx 
	  * 创建时间：2018年5月21日 
	  * @version 1  
	  * 返回值    TaskMobile
	  */
	public TaskMobile postAgent(MessageLogin messageLogin,String requestPath){
		//
		IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(),requestPath,messageLogin.getTask_id()); 
		if(idleInstance==null||idleInstance.getIdleInstanceInfo()==null){
			tracerLog.addTag("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
			throw new RuntimeException("NoIdleInstance"); 
		}else{
			String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
			Integer port = idleInstance.getIdleInstanceInfo().getPort(); 
			String uri = "http://" + ip + ":" + port + requestPath;
			tracerLog.addTag("uri", uri);  
			tracerLog.addTag("messageLogin", messageLogin.toString());  
			messageLogin.setIp(ip);
			messageLogin.setPort(port+""); 
			ResponseEntity<TaskMobile> str = this.restTemplate.postForEntity(uri, messageLogin, TaskMobile.class);
			TaskMobile taskMobile = str.getBody();   
			return taskMobile;
		} 
	}
	
	/**
	 * @Des POST代理请求的封装   用于登录,可自定义intervalTime（自动关闭时间）长度，默认2分钟。例如：浦发信用卡需要3分钟才能完成爬取
	 * @param bankJsonBean
	 * @param requestPath 登录的请求路径
	 */
	public TaskMobile postAgent(MessageLogin messageLogin,String requestPath,Long intervalTime){
		//
		IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(),requestPath,messageLogin.getTask_id(),intervalTime); 
		if(idleInstance==null||idleInstance.getIdleInstanceInfo()==null){
			tracerLog.addTag("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
			throw new RuntimeException("NoIdleInstance"); 
		}else{
			String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
			Integer port = idleInstance.getIdleInstanceInfo().getPort(); 
			String uri = "http://" + ip + ":" + port + requestPath;
			tracerLog.addTag("uri", uri);  
			tracerLog.addTag("messageLogin", messageLogin.toString());  
			messageLogin.setIp(ip);
			messageLogin.setPort(port+""); 
			ResponseEntity<TaskMobile> str = this.restTemplate.postForEntity(uri, messageLogin, TaskMobile.class);
			TaskMobile taskMobile = str.getBody();   
			return taskMobile;
		} 
	}
	
	
	public String getAppNameUpperCase() {
		return appName.toUpperCase();
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	
	
}
