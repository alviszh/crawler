package app.service;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.crawler.bank.json.IdleInstance;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.client.MiddleClient;
import app.commontracerlog.TracerLog;
import app.service.aop.IAgent;

@Component
public class AgentService implements IAgent{
	
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
    @Override
    public void releaseInstance(String instanceIpAddr, WebDriver driver) {
        tracerLog.addTag("releaseInstance", instanceIpAddr);

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
            tracerLog.addTag(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: webdriver is null!!!!!!!!!!!!!!!!", driver.toString());
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
    @Override
    public TaskInsurance postAgent(InsuranceRequestParameters insuranceRequestParameters, String requestPath, Long intervalTime) {
        //
        IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(), requestPath, insuranceRequestParameters.getTaskId(), intervalTime);
        if (idleInstance == null || idleInstance.getIdleInstanceInfo() == null) {
            tracerLog.addTag("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
            throw new RuntimeException("NoIdleInstance");
        } else {
            String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
            Integer port = idleInstance.getIdleInstanceInfo().getPort();
            String uri = "http://" + ip + ":" + port + requestPath;
            tracerLog.addTag("uri", uri);
            tracerLog.addTag("insuranceJsonBean", insuranceRequestParameters.toString());
            insuranceRequestParameters.setIp(ip);
            insuranceRequestParameters.setPort(port + "");
            ResponseEntity<TaskInsurance> str = this.restTemplate.postForEntity(uri, insuranceRequestParameters, TaskInsurance.class);
            TaskInsurance taskInsurance = str.getBody();
            return taskInsurance;
        }
    }


    /**
     * @param e_commerceJsonBean
     * @param requestPath        登录的请求路径
     * @Des POST代理请求的封装   用于登录
     */
    @Override
    public TaskInsurance postAgent(InsuranceRequestParameters insuranceRequestParameters, String requestPath) {
        //
        IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(), requestPath, insuranceRequestParameters.getTaskId());
        if (idleInstance == null || idleInstance.getIdleInstanceInfo() == null) {
            tracerLog.addTag("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
            throw new RuntimeException("NoIdleInstance");
        } else {
            String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
            Integer port = idleInstance.getIdleInstanceInfo().getPort();
            String uri = "http://" + ip + ":" + port + requestPath;
            tracerLog.addTag("uri", uri);
            tracerLog.addTag("insuranceJsonBean", insuranceRequestParameters.toString());
            insuranceRequestParameters.setIp(ip);
            insuranceRequestParameters.setPort(port + "");
            ResponseEntity<TaskInsurance> str = this.restTemplate.postForEntity(uri, insuranceRequestParameters, TaskInsurance.class);
            TaskInsurance task = str.getBody();
            return task;
        }
    }

    /**
     * @param e_commerceJsonBean
     * @param requestPath        登录的请求路径
     * @Des POST代理请求的封装   用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作
     */
    @Override
    public TaskInsurance postAgentCombo(InsuranceRequestParameters insuranceRequestParameters, String requestPath) {
        String ip = insuranceRequestParameters.getIp();
        String port = insuranceRequestParameters.getPort();
        String uri = "http://" + ip + ":" + port + requestPath;
        tracerLog.addTag("uri", uri);
        tracerLog.addTag("insuranceJsonBean", insuranceRequestParameters.toString());
        ResponseEntity<TaskInsurance> str = this.restTemplate.postForEntity(uri, insuranceRequestParameters, TaskInsurance.class);
        TaskInsurance task = str.getBody();
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
