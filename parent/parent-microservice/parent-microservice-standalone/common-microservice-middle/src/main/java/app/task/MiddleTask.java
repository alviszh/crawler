package app.task;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.IdleInstance;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.middle.BrowserCluster;
import com.microservice.dao.repository.crawler.middle.BrowserClusterRepository;

import app.commontracerlog.TracerLog;
import app.service.BrowserClusterService;

@Component
public class MiddleTask {

    public final static long ONE_SECOND = 5 * 1000;

    @Autowired
    private BrowserClusterRepository browserClusterRepository;
    
    @Autowired
    private BrowserClusterService browserClusterService; 

    public final static long DEFAULT_INTERVAL_TIME = 2 * 60 * 1000;

    private static final Logger log = LoggerFactory.getLogger(MiddleTask.class);

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired 
	private TracerLog tracer;

    @Scheduled(fixedDelay = ONE_SECOND)
    public void fixedDelayJob() { 
        findTimeoutInstance();
    }

    public List<BrowserCluster> findByInuse() {
        return browserClusterRepository.findByInuse(1);
    }


    public List<BrowserCluster> findTimeoutInstance() {
        List<BrowserCluster> browserClusters = findByInuse();
        tracer.addTag("browserClusters.inuse.size", browserClusters.size()+""); 
        Long currentTime = (new Date()).getTime();
        if (!browserClusters.isEmpty()) {
            for (BrowserCluster browserCluster : browserClusters) {
                Long updateTime = browserCluster.getUpdateTime().getTime();
                long INTERVAL_TIME = browserCluster.getIntervalTime() == null ? DEFAULT_INTERVAL_TIME : browserCluster.getIntervalTime();
                long diff = currentTime - updateTime; 
                log.info("ID----"+browserCluster.getId()+" INTERVAL_TIME----" + INTERVAL_TIME + " diff--------" + diff + "--AppName--" + browserCluster.getAppName()+"---browserCluster---"+browserCluster.toString());
                if (diff > INTERVAL_TIME) {//updatetime 时间超过(默认2分钟)指定时间
                    log.info("browserCluster--------" + browserCluster.toString());
                    if (browserCluster.getInstanceIp() == null) {
                        log.info("ERROR: browserCluster.getInstanceIp()==null ");
                    } else if (browserCluster.getPort() == null) {
                        log.info("ERROR: browserCluster.getPort()==null ");
                    } else if (browserCluster.getRequestPath() == null) {
                        log.info("ERROR: browserCluster.getRequestPath()==null ");
                    } else if (browserCluster.getTaskid() == null) {
                        log.info("ERROR: browserCluster.getTaskid()==null ");
                    } else {
                    	log.info("browserCluster.getAppName()=="+browserCluster.getAppName()+"=========");
                    	if(browserCluster.getAppName().trim().equals("PBCCRC")){
                    		log.info("关闭人行征信"+browserCluster.getAppName());
                    		tracer.addTag("关闭人行征信", browserCluster.getAppName());
                    		//人行征信
                   		 	try {
                                autoClosePbccrc(browserCluster);
                            } catch (Exception e) {
                                log.info("关闭人行征信 autoClosePbccrc excrption:" + e.toString());
                                IdleInstance idleInstance = browserClusterService.findByAppNameAndInstanceIp(browserCluster.getAppName(),browserCluster.getInstanceIp());
                                log.info("关闭人行征信 autoClosePbccrc:" + idleInstance.toString());
                            } 
                    	}else if(browserCluster.getAppName().startsWith("BANK")){
                    		log.info("关闭银行"+browserCluster.getAppName());
                    		tracer.addTag("关闭银行", browserCluster.getAppName());
                    		//银行
                    		try {
                                autoClose(browserCluster);
                            } catch (Exception e) {
                                log.info("关闭银行 autoClose excrption:" + e.toString());
                                IdleInstance idleInstance = browserClusterService.findByAppNameAndInstanceIp(browserCluster.getAppName(),browserCluster.getInstanceIp());
                                log.info("关闭银行 autoClose:" + idleInstance.toString());
                            }
                    	}else if(browserCluster.getAppName().startsWith("ECOMMERCE")){
                    		log.info("关闭电商"+browserCluster.getAppName());
                    		tracer.addTag("关闭电商", browserCluster.getAppName());
                    		//电商
                    		try {
                    			autoCloseE_Commerce(browserCluster);
                            } catch (Exception e) {
                                log.info("关闭电商 autoCloseE_Commerce excrption:" + e.toString());
                                IdleInstance idleInstance = browserClusterService.findByAppNameAndInstanceIp(browserCluster.getAppName(),browserCluster.getInstanceIp());
                                log.info("关闭电商 autoCloseE_Commerce:" + idleInstance.toString());
                            }
                    	}else{
                    		 log.info("无法关闭未知的服务==========" + browserCluster.getAppName()+"==========");
                    	} 
                    }
                }
            }
        }

        return browserClusters;
    }
 
    

    //银行
    public void autoClose(BrowserCluster browserCluster) throws Exception {
        String requestPath = browserCluster.getRequestPath();
        if(requestPath==null){
        	 tracer.addTag("autoCloseERROR", "RequestPathIsNull");
        	 tracer.addTag("browserCluster.getId()", browserCluster.getId()+"");
        	 tracer.addTag("browserCluster.getAppName()", browserCluster.getAppName()+"");
        	 log.info(browserCluster.getAppName()+"-----requestPath is null------ID:" + browserCluster.getId() ); 
        }else{
        	requestPath = requestPath.replace("login", "quit"); 
            BankJsonBean bankJsonBean = new BankJsonBean();
            bankJsonBean.setIp(browserCluster.getInstanceIp());
            bankJsonBean.setPort(browserCluster.getPort());
            bankJsonBean.setTaskid(browserCluster.getTaskid());
            String uri = "http://" + browserCluster.getInstanceIp() + ":" + browserCluster.getPort() + requestPath;
            tracer.addTag("autoClose", "auto");
            tracer.addTag("autoClose", uri);
            log.info("autoClose-----uri------" + uri);
            ResponseEntity<TaskBank> str = this.restTemplate.postForEntity(uri, bankJsonBean, TaskBank.class);
            log.info("autoClose------TaskBank--------" + str);
        }
        

    }

    //电商
    public void autoCloseE_Commerce(BrowserCluster browserCluster) throws Exception {
    	String requestPath = browserCluster.getRequestPath();
    	if(requestPath==null){
    		 tracer.addTag("autoCloseE_CommerceERROR", "RequestPathIsNull");
        	 tracer.addTag("browserCluster.getId()", browserCluster.getId()+"");
        	 tracer.addTag("browserCluster.getAppName()", browserCluster.getAppName()+"");
        	 log.info(browserCluster.getAppName()+"-----requestPath is null------ID:" + browserCluster.getId() );
        }else{
        	 requestPath = requestPath.replace("login", "quit");
             E_CommerceJsonBean e_commerceJsonBean = new E_CommerceJsonBean();
             e_commerceJsonBean.setIp(browserCluster.getInstanceIp());
             e_commerceJsonBean.setPort(browserCluster.getPort());
             e_commerceJsonBean.setTaskid(browserCluster.getTaskid());
             String uri = "http://" + browserCluster.getInstanceIp() + ":" + browserCluster.getPort() + requestPath;
             tracer.addTag("autoCloseE_Commerce", uri);
             tracer.addTag("autoClose", "E_Commerce");
             log.info("autoClose-----uri------" + uri);
             ResponseEntity<E_CommerceTask> str = this.restTemplate.postForEntity(uri, e_commerceJsonBean, E_CommerceTask.class);
             log.info("autoClose------TaskBank--------" + str);
        }
       
       
    }

    //人行征信
    public void autoClosePbccrc(BrowserCluster browserCluster) throws Exception {
        String requestPath = browserCluster.getRequestPath();
        requestPath = requestPath.replace("getCredit", "quit");
        PbccrcJsonBean pbccrcJsonBean = new PbccrcJsonBean();
        pbccrcJsonBean.setIp(browserCluster.getInstanceIp());
        pbccrcJsonBean.setPort(browserCluster.getPort());
        pbccrcJsonBean.setMapping_id(browserCluster.getTaskid());
        String uri = "http://" + browserCluster.getInstanceIp() + ":" + browserCluster.getPort() + requestPath;
        tracer.addTag("autoClose", "Pbccrc");
        tracer.addTag("autoClosePbccrc", uri);
        log.info("autoClose-----uri------" + uri);
        ResponseEntity<String> str = this.restTemplate.postForEntity(uri, pbccrcJsonBean, String.class); 
        log.info("autoClose------pbccrc--------" + str);
    }
}
