package app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.bank.json.IdleInstance;
import com.microservice.dao.entity.crawler.middle.BrowserCluster;
import com.netflix.appinfo.InstanceInfo;

import app.commontracerlog.TracerLog;
import app.service.BrowserClusterService;

@RestController
@RequestMapping("/middle")
public class MiddleController {
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private BrowserClusterService browserClusterService;
	
	private static final Logger log = LoggerFactory.getLogger(MiddleController.class);
	
	/*@RequestMapping(value = "/initAll", method = { RequestMethod.GET })
	public List<BrowserCluster> initAll() {
		List<BrowserCluster> browserCluster = browserClusterService.initAllBrowserCluster(); 
		return browserCluster;
	}*/
	
	@RequestMapping(value = "/init", method = { RequestMethod.GET })
	public List<BrowserCluster> init(String appName) {
		List<BrowserCluster> browserClusters = browserClusterService.initBrowserCluster(appName);
		return browserClusters;
	}
	
	@RequestMapping(value = "/init/like", method = { RequestMethod.GET })
	public List<BrowserCluster> initLike(String appName) {
		List<BrowserCluster> browserClusters = browserClusterService.initBrowserClusterLike(appName);
		return browserClusters;
	}
	
	//内部接口
	@RequestMapping(value = "/getIdleInstanceInfo", method = { RequestMethod.GET })
	public InstanceInfo getIdleInstanceInfo(String appName) { 
		return browserClusterService.getIdleInstanceInfo(appName);
	}

	//获取一个闲置的实例
	//外部接口
	@RequestMapping(value = "/getIdleInstance", method = { RequestMethod.GET })
	public IdleInstance getIdleInstance(String appName,String requestPath,String taskid,Long intervalTime) { 
		tracer.addTag("@@@@@browserClusterService.getIdleInstance.Controller@@@@@", "appName "+appName+" requestPath "+requestPath+" taskid "+taskid+" intervalTime "+intervalTime); 
		if(appName==null){
			tracer.addTag("getIdleInstanceError", "appNameIsNull"); 
			throw new RuntimeException("appName is null");
		}else if(requestPath==null){
			tracer.addTag("getIdleInstanceError", "requestPathIsNull"); 
			throw new RuntimeException("requestPath is null");
		}else if (taskid==null){
			tracer.addTag("getIdleInstanceError", "taskidIsNull"); 
			throw new RuntimeException("taskid is null");
		}else{
			//获取一个闲置的实例
			tracer.addTag("*****browserClusterService.getIdleInstance.start*****", "appName "+appName+" requestPath "+requestPath+" taskid "+taskid+" intervalTime "+intervalTime); 
			IdleInstance idleInstance = browserClusterService.getIdleInstance(appName,requestPath,taskid,intervalTime);
			tracer.addTag("#####browserClusterService.getIdleInstance.end#####", "idleInstance "+idleInstance.toString()); 
			return idleInstance;
		}
		
	}
	
	//通过Seleunim的 WindowsHandler id 获取该实例上的webdriver 对象继续使用（常用语短信验证码环节）
	//外部接口
	@RequestMapping(value = "/getInstanceByHandler", method = { RequestMethod.GET })
	public IdleInstance getInstanceByHandler(String handler) { 
		IdleInstance idleInstance = browserClusterService.findByWindowHandle(handler);
		return idleInstance;
	}
	
	//释放一个使用完毕的实例
	//外部接口
	@RequestMapping(value = "/releaseInstance", method = { RequestMethod.GET })
	public IdleInstance releaseInstance(String appName,String instanceIpAddr) { 
		if(appName==null){
			tracer.addTag("releaseInstanceError", "appNameIsNull"); 
			tracer.addTag("appName", appName); 
			tracer.addTag("instanceIpAddr", instanceIpAddr); 
			throw new RuntimeException("appName is null");
		}else if(instanceIpAddr==null){
			tracer.addTag("releaseInstanceError", "instanceIpAddrIsNull"); 
			tracer.addTag("appName", appName); 
			tracer.addTag("instanceIpAddr", instanceIpAddr); 
			throw new RuntimeException("instanceIpAddr is null");
		}else{
			tracer.addTag("releaseInstance", "request"); 
			tracer.addTag("appName", appName); 
			tracer.addTag("instanceIpAddr", instanceIpAddr); 
			IdleInstance idleInstance = browserClusterService.findByAppNameAndInstanceIp(appName,instanceIpAddr);
			return idleInstance;
		}
		
	}
		
	
	
    //客户端开始
	/*@RequestMapping(value = "/clientFirst", method = { RequestMethod.POST })
	public ClientBean clientFirst(@RequestBody ClientBean clientBean) {
		//给客户端返回一个可用的ip地址
		ClientBean clientFirst = middleService.clientFirst(clientBean);
		
		return clientFirst;
	}*/
	//客户端结束
	/*@RequestMapping(value = "/clientEnd", method = { RequestMethod.POST })
	public ClientBean clientEnd(@RequestBody ClientBean clientBean) {
		//将该服务从正在使用设置成空闲
		ClientBean clientFirst = middleService.clientEnd(clientBean);
		
		return clientFirst;
	}*/
	//服务器 注册/下线
	/*@RequestMapping(value = "/server", method = { RequestMethod.POST })
	public EurekaListenbean server(@RequestBody EurekaListenbean eurekaListenbean) {
		//更新数据库中服务器的状态
		EurekaListenbean server = middleService.server(eurekaListenbean);
		return server;
	}*/

}
