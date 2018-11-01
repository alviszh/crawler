package app.service;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.crawler.bank.json.IdleInstance;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;

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

    //释放资源  （释放这台电脑 by IP，关闭WebDriver）
    public void releaseInstance(String instanceIpAddr, WebDriver driver) {
        tracerLog.output("releaseInstance", instanceIpAddr);
//        tracerLog.output("taobao=========================================================quit", driver.toString());

        tracerLog.output("releaseInstance.app", appName);
		if(instanceIpAddr==null){
			tracerLog.output("releaseInstance.instanceIpAddr", "IsNull");
			throw new RuntimeException("instanceIpAddr is null");
		}else{
			tracerLog.output("releaseInstance.instanceIpAddr", instanceIpAddr);
		}
		
        if (driver != null) {
            tracerLog.output("releaseInstance", instanceIpAddr);
            tracerLog.output("taobao=====webdriver=====quit=====success", driver.toString());
            try {
            	driver.quit();
			} catch (Exception e) {
				tracerLog.output("releaseInstance.quit.exception", "释放资源失败");
            	tracerLog.output("releaseInstance.quit.e", e.toString());
			}
        } else {
            tracerLog.output("::::::::::::::::::::::::::::::::::::::::::::::::::::::FUCK:::::::::::: webdriver is null!!!!!!!!!!!!!!!!","");
        }
        
        tracerLog.output("releaseInstance", instanceIpAddr); 
        if(middleClient==null){ 
        	tracerLog.output("middleClient", "NullException");
        	tracerLog.output("instanceIpAddr", instanceIpAddr); 
        	tracerLog.output("appName", appName);
        }else{
        	try{
            	middleClient.releaseInstance(appName.toUpperCase(), instanceIpAddr);//注意 appName 要大写一下，默认是小写

        	}catch(Exception e){
        		e.printStackTrace();
        	}
        } 
    }

    /**
     * @param e_commerceJsonBean
     * @param requestPath        登录的请求路径
     * @Des POST代理请求的封装   用于登录,可自定义intervalTime（自动关闭时间）长度，默认2分钟。例如：浦发信用卡需要3分钟才能完成爬取
     */
    public E_CommerceTask postAgent(E_CommerceJsonBean e_commerceJsonBean, String requestPath, Long intervalTime) {
        //
        IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(), requestPath, e_commerceJsonBean.getTaskid(), intervalTime);
        if (idleInstance == null || idleInstance.getIdleInstanceInfo() == null) {
            tracerLog.output("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
            throw new RuntimeException("NoIdleInstance");
        } else {
            String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
            Integer port = idleInstance.getIdleInstanceInfo().getPort();
            String uri = "http://" + ip + ":" + port + requestPath;
            tracerLog.output("uri", uri);
            tracerLog.output("e_commerceJsonBean", e_commerceJsonBean.toString());
            e_commerceJsonBean.setIp(ip);
            e_commerceJsonBean.setPort(port + "");
            
            ResponseEntity<E_CommerceTask> str = this.restTemplate.postForEntity(uri, e_commerceJsonBean, E_CommerceTask.class);
            E_CommerceTask e_CommerceTask = str.getBody();
            
            e_CommerceTask.setCrawlerHost(ip);
            e_CommerceTask.setCrawlerPort(port + "");
            tracerLog.output("e_CommerceTask", e_CommerceTask.toString());
            return e_CommerceTask;
        }
    }


    /**
     * @param e_commerceJsonBean
     * @param requestPath        登录的请求路径
     * @Des POST代理请求的封装   用于登录
     */
    public E_CommerceTask postAgent(E_CommerceJsonBean e_commerceJsonBean, String requestPath) {
        //
        IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(), requestPath, e_commerceJsonBean.getTaskid());
        if (idleInstance == null || idleInstance.getIdleInstanceInfo() == null) {
            tracerLog.output("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
            throw new RuntimeException("NoIdleInstance");
        } else {
            String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
            Integer port = idleInstance.getIdleInstanceInfo().getPort();
            String uri = "http://" + ip + ":" + port + requestPath;
            tracerLog.output("uri", uri);
            tracerLog.output("e_commerceJsonBean", e_commerceJsonBean.toString());
            e_commerceJsonBean.setIp(ip);
            e_commerceJsonBean.setPort(port + "");
            ResponseEntity<E_CommerceTask> str = this.restTemplate.postForEntity(uri, e_commerceJsonBean, E_CommerceTask.class);
            E_CommerceTask task = str.getBody();
            
           /* task.setCrawlerHost(ip);
            task.setCrawlerPort(port + "");*/
            
            return task;
        }
    }

    /**
     * @param e_commerceJsonBean 
     * @param requestPath        登录的请求路径
     * @Des POST代理请求的封装   用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作，获取的ip地址 是之前存放到数据库中的字段
     */
    public E_CommerceTask postAgentCombo(E_CommerceJsonBean e_commerceJsonBean, String requestPath) {
        String ip = e_commerceJsonBean.getIp();
        String port = e_commerceJsonBean.getPort();
        String uri = "http://" + ip + ":" + port + requestPath;
        tracerLog.output("uri", uri);
        tracerLog.output("e_commerceJsonBean", e_commerceJsonBean.toString());
        ResponseEntity<E_CommerceTask> str = this.restTemplate.postForEntity(uri, e_commerceJsonBean, E_CommerceTask.class);
        E_CommerceTask task = str.getBody();
        return task;
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
