package app.service;

import app.client.MiddleClient;
import app.commontracerlog.TracerLog;
import app.service.aop.IAgent;
import app.service.aop.aspect.AspectAgent;
import com.crawler.bank.json.IdleInstance;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
     * @Des POST代理请求的封装   用于登录,可自定义intervalTime（自动关闭时间）长度，默认2分钟。例如：浦发信用卡需要3分钟才能完成爬取
     *
     * 人行征信没有taskid
     */
    @Override
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
     * @param pbccrcJsonBean
     * @param requestPath        登录的请求路径
     * @Des POST代理请求的封装   用于登录
     */
    @Override
    public String postAgent(PbccrcJsonBean pbccrcJsonBean, String requestPath) {
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
            ResponseEntity<String> str = this.restTemplate.postForEntity(uri, pbccrcJsonBean, String.class);
            String result = str.getBody();
            return result;
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
