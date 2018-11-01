package app.service.common;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.crawler.bank.json.IdleInstance;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.client.MiddleClient;
import app.commontracerlog.TracerLog;

@Component
public class AgentService {
	
	@Autowired 
	private RestTemplateBuilder builder; 
	// 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
	@Bean
	public RestTemplate restTemplate() {
		return builder.build();
	}

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
        tracerLog.addTag("releaseInstance", instanceIpAddr);
//        tracerLog.addTag("taobao=========================================================quit", driver.toString());

        if (driver != null) {
            tracerLog.addTag("releaseInstance", instanceIpAddr);
            tracerLog.addTag("taobao=====webdriver=====quit=====success", driver.toString());
            try {
            	driver.quit();
			} catch (Exception e) {
				tracerLog.addTag("releaseInstance.quit.exception", "释放资源失败");
            	tracerLog.addTag("releaseInstance.quit.e", e.toString());
			}
        } else {
            tracerLog.addTag("::::::::::::::::::::::::::::::::::::::::::::::::::::::FUCK:::::::::::: webdriver is null!!!!!!!!!!!!!!!!", driver.toString());
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
     * @param e_commerceJsonBean
     * @param requestPath        登录的请求路径
     * @Des POST代理请求的封装   用于登录,可自定义intervalTime（自动关闭时间）长度，默认2分钟。例如：浦发信用卡需要3分钟才能完成爬取
     */
    public TaskHousing postAgent(MessageLoginForHousing messageLoginForHousing, String requestPath, Long intervalTime) {
        //
        IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(), requestPath, messageLoginForHousing.getTask_id(), intervalTime);
        if (idleInstance == null || idleInstance.getIdleInstanceInfo() == null) {
            tracerLog.addTag("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
            throw new RuntimeException("NoIdleInstance");
        } else {
            String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
            Integer port = idleInstance.getIdleInstanceInfo().getPort();
            String uri = "http://" + ip + ":" + port + requestPath;
            tracerLog.addTag("uri", uri);
            tracerLog.addTag("housingJsonBean", messageLoginForHousing.toString());
            messageLoginForHousing.setIp(ip);
            messageLoginForHousing.setPort(port + "");
            ResponseEntity<TaskHousing> str = this.restTemplate.postForEntity(uri, messageLoginForHousing, TaskHousing.class);
            TaskHousing taskHousing = str.getBody();
            return taskHousing;
        }
    }


    /**
     * @param e_commerceJsonBean
     * @param requestPath        登录的请求路径
     * @Des POST代理请求的封装   用于登录
     */
    public TaskHousing postAgent(MessageLoginForHousing messageLoginForHousing, String requestPath) {
        //
        IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(), requestPath, messageLoginForHousing.getTask_id());
        if (idleInstance == null || idleInstance.getIdleInstanceInfo() == null) {
            tracerLog.addTag("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
            throw new RuntimeException("NoIdleInstance");
        } else {
            String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
            Integer port = idleInstance.getIdleInstanceInfo().getPort();
            String uri = "http://" + ip + ":" + port + requestPath;
            tracerLog.addTag("uri", uri);
            tracerLog.addTag("housingJsonBean", messageLoginForHousing.toString());
            messageLoginForHousing.setIp(ip);
            messageLoginForHousing.setPort(port + "");
            ResponseEntity<TaskHousing> str = this.restTemplate.postForEntity(uri, messageLoginForHousing, TaskHousing.class);
            TaskHousing task = str.getBody();
            return task;
        }
    }

    /**
     * @param e_commerceJsonBean
     * @param requestPath        登录的请求路径
     * @Des POST代理请求的封装   用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作
     */
    public TaskHousing postAgentCombo(MessageLoginForHousing messageLoginForHousing, String requestPath) {
        String ip = messageLoginForHousing.getIp();
        String port = messageLoginForHousing.getPort();
        String uri = "http://" + ip + ":" + port + requestPath;
        tracerLog.addTag("uri", uri);
        tracerLog.addTag("insuranceJsonBean", messageLoginForHousing.toString());
        ResponseEntity<TaskHousing> str = this.restTemplate.postForEntity(uri, messageLoginForHousing, TaskHousing.class);
        TaskHousing task = str.getBody();
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
