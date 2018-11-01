package app.service;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.IdleInstance;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.client.MiddleClient;
import app.commontracerlog.TracerLog;
import app.service.aop.IAgent;

@Component
public class AgentService implements IAgent{

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
		/*tracerLog.addTag("releaseInstance",instanceIpAddr);
		middleClient.releaseInstance(appName.toUpperCase(), instanceIpAddr);//注意 appName 要大写一下，默认是小写
		if(driver!=null){
			driver.quit();
		}
		*/ 
		
		tracerLog.addTag("releaseInstance.app", appName);
		if(instanceIpAddr==null){
			tracerLog.addTag("releaseInstance.instanceIpAddr", "IsNull");
			throw new RuntimeException("instanceIpAddr is null");
		}else{
			tracerLog.addTag("releaseInstance.instanceIpAddr", instanceIpAddr);
		}
		
		
		if (driver != null) {
			tracerLog.addTag("bank.webDriver.quit", driver.toString());
			tracerLog.addTag("releaseInstance", instanceIpAddr);
			tracerLog.addTag("bank.webdriver.quit.success", driver.toString());
            try {
                driver.quit();
            } catch (Exception e) {
            	tracerLog.addTag("releaseInstance.quit.exception", "释放资源失败");
            	tracerLog.addTag("releaseInstance.quit.e", e.toString());
            } 
        } else {
        	tracerLog.addTag("releaseInstance", "bank.webdriver.quit.null");
        }
    	
		tracerLog.addTag("releaseInstance", instanceIpAddr); 
        if(middleClient==null){ 
        	tracerLog.addTag("middleClient", "NullException");
        	tracerLog.addTag("instanceIpAddr", instanceIpAddr); 
        	tracerLog.addTag("appName", appName);
        }else{
        	middleClient.releaseInstance(appName.toUpperCase(), instanceIpAddr);//注意 appName 要大写一下，默认是小写
        } 
	} 
	
	/**
	 * @Des POST代理请求的封装   用于登录
	 * @param bankJsonBean
	 * @param requestPath 登录的请求路径
	 */
	public TaskBank postAgent(BankJsonBean bankJsonBean,String requestPath){
		//
		IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(),requestPath,bankJsonBean.getTaskid(),INTERVAL_TIME);
		if(idleInstance==null||idleInstance.getIdleInstanceInfo()==null){
			tracerLog.addTag("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
			throw new RuntimeException("NoIdleInstance"); 
		}else{
			String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
			Integer port = idleInstance.getIdleInstanceInfo().getPort(); 
			String uri = "http://" + ip + ":" + port + requestPath;
			tracerLog.addTag("uri", uri);  
			tracerLog.addTag("bankJsonBean", bankJsonBean.toString());  
			bankJsonBean.setIp(ip);
			bankJsonBean.setPort(port+""); 
			ResponseEntity<TaskBank> str = this.restTemplate.postForEntity(uri, bankJsonBean, TaskBank.class);
			TaskBank taskBank = str.getBody();   
			return taskBank;
		} 
	}
	
	/**
	 * @Des POST代理请求的封装   用于登录,可自定义intervalTime（自动关闭时间）长度，默认2分钟。例如：浦发信用卡需要3分钟才能完成爬取
	 * @param bankJsonBean
	 * @param requestPath 登录的请求路径
	 */
	public TaskBank postAgent(BankJsonBean bankJsonBean,String requestPath,Long intervalTime){
		//
		IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(),requestPath,bankJsonBean.getTaskid(),intervalTime); 
		if(idleInstance==null||idleInstance.getIdleInstanceInfo()==null){
			tracerLog.addTag("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
			throw new RuntimeException("NoIdleInstance"); 
		}else{
			String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
			Integer port = idleInstance.getIdleInstanceInfo().getPort(); 
			String uri = "http://" + ip + ":" + port + requestPath;
			System.out.println("返回的服务器地址："+uri);
			tracerLog.addTag("uri", uri);  
			tracerLog.addTag("bankJsonBean", bankJsonBean.toString());  
			bankJsonBean.setIp(ip);
			bankJsonBean.setPort(port+""); 
			ResponseEntity<TaskBank> str = this.restTemplate.postForEntity(uri, bankJsonBean, TaskBank.class);
			TaskBank taskBank = str.getBody();   
			return taskBank;
		} 
	}
	
	/**
	 * @Des POST代理请求的封装   用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作
	 * @param bankJsonBean
	 * @param requestPath 登录的请求路径
	 */
	public TaskBank postAgentCombo(BankJsonBean bankJsonBean,String requestPath){ 
		String ip = bankJsonBean.getIp();
		String port = bankJsonBean.getPort(); 
		String uri = "http://" + ip + ":" + port + requestPath;
		tracerLog.addTag("uri", uri);  
		tracerLog.addTag("bankJsonBean", bankJsonBean.toString());  
		ResponseEntity<TaskBank> str = this.restTemplate.postForEntity(uri, bankJsonBean, TaskBank.class);
		TaskBank taskBank = str.getBody();   
		return taskBank; 
	}
	
	/**
	 * @Des POST代理请求的封装
	 * @param bankJsonBean
	 * @param 请求路径
	 */
	/*public TaskBank postAgentCombo(BankJsonBean bankJsonBean,String requestPath){
		TaskBank	
		 
			String ip = taskBank.getCrawlerHost();
			String port = taskBank.getCrawlerPort(); 
			String uri = "http://" + ip + ":" + port + requestPath;
			tracerLog.addTag("uri", uri);  
 
			ResponseEntity<TaskBank> str = this.restTemplate.postForEntity(uri, bankJsonBean, TaskBank.class);
			TaskBank taskBank = str.getBody();   
			return taskBank;
	 
	}
	*/
	
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
