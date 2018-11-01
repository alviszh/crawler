package app.service;

import app.client.MiddleClient;
import app.commontracerlog.TracerLog;
import app.service.aop.IAgent;

import com.crawler.bank.json.IdleInstance;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.microservice.dao.entity.crawler.qq.TaskQQ;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AgentService {//implements IAgent{

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MiddleClient middleClient;
    
    @Autowired
    private TracerLog tracerLog;
    
	@Autowired
	private TaskQQStatusService taskQQStatusService;

    //释放资源  （释放这台电脑 by IP，关闭WebDriver）
    @Async
    public void  releaseInstance(String instanceIpAddr, WebDriver driver) {
        tracerLog.qryKeyValue("releaseInstance", instanceIpAddr);
        if (driver != null) {
            tracerLog.addTag("pbccrc.webdriver.quit", driver.toString());
            try {
                driver.quit();
            } catch (Exception e) {
                tracerLog.qryKeyValue("releaseInstance.quit.exception", "释放资源失败");
                tracerLog.addTag("releaseInstance.quit.e", e.toString());
            }

        } else {
            tracerLog.qryKeyValue("releaseInstance", "pbccrc.webdriver.quit.null");
        }

        if(middleClient==null){ 
        	tracerLog.qryKeyValue("middleClient", "NullException");
        	tracerLog.addTag("appName", appName);
        }else{
        	middleClient.releaseInstance(appName.toUpperCase(), instanceIpAddr);//注意 appName 要大写一下，默认是小写
        } 

        
    }

    /**
     * @param pbccrcJsonBean
     * @param requestPath        登录的请求路径
     * @Des POST代理请求的封装   用于登录,可自定义intervalTime（自动关闭时间）长度，默认2分钟。
     *
     */
  //  @Override
    public String postAgent(PbccrcJsonBean pbccrcJsonBean, String requestPath, Long intervalTime) {
        //
        IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(), requestPath, pbccrcJsonBean.getMapping_id(), intervalTime);
        if (idleInstance == null || idleInstance.getIdleInstanceInfo() == null) {
            tracerLog.qryKeyValue("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
            throw new RuntimeException("NoIdleInstance");
        } else {
            String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
            Integer port = idleInstance.getIdleInstanceInfo().getPort();
            String uri = "http://" + ip + ":" + port + requestPath;
            tracerLog.addTag("uri", uri);
            tracerLog.addTag("pbccrcJsonBean", pbccrcJsonBean.toString());
            pbccrcJsonBean.setIp(ip);
            pbccrcJsonBean.setPort(port + "");
            ResponseEntity<String > str = this.restTemplate.postForEntity(uri, pbccrcJsonBean, String .class);
            String  result = str.getBody();
            return result;
        }
    }
    /**
	 * @Des POST代理请求的封装   用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作
	 * @param bankJsonBean
	 * @param requestPath 登录的请求路径
	 */
	public TaskQQ postAgentCombo(PbccrcJsonBean pbccrcJsonBean,String requestPath){ 
		String ip = pbccrcJsonBean.getIp();
		String port = pbccrcJsonBean.getPort(); 
		String uri = "http://" + ip + ":" + port + requestPath;
		tracerLog.addTag("uri", uri);  
		tracerLog.addTag("bankJsonBean", pbccrcJsonBean.toString());  
		ResponseEntity<TaskQQ> str = this.restTemplate.postForEntity(uri, pbccrcJsonBean, TaskQQ.class);
		TaskQQ taskQQ = str.getBody();  
		return taskQQ; 
	}

    /**
     * @param pbccrcJsonBean
     * @param requestPath        登录的请求路径
     * @Des POST代理请求的封装   用于登录
     */
 //   @Override
    public TaskQQ postAgent(PbccrcJsonBean pbccrcJsonBean, String requestPath) {
        IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(), requestPath, pbccrcJsonBean.getMapping_id());
        if (idleInstance == null || idleInstance.getIdleInstanceInfo() == null) {
            tracerLog.qryKeyValue("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
            throw new RuntimeException("NoIdleInstance");
        } else {
            String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
            Integer port = idleInstance.getIdleInstanceInfo().getPort();
            String uri = "http://" + ip + ":" + port + requestPath;
            tracerLog.addTag("uri", uri);
            tracerLog.addTag("pbccrcJsonBean", pbccrcJsonBean.toString());
            pbccrcJsonBean.setIp(ip);
            pbccrcJsonBean.setPort(port + "");
            ResponseEntity<TaskQQ> str = this.restTemplate.postForEntity(uri, pbccrcJsonBean, TaskQQ.class);
            TaskQQ taskQQ = str.getBody();
            taskQQStatusService.changeStatusLogin(pbccrcJsonBean);
            return taskQQ;
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
