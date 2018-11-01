package app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan; 
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.crawler.bank.json.BrowserInfo;
import com.crawler.bank.json.IdleInstance;
import com.crawler.bank.json.IdleInstanceInfo;
import com.microservice.dao.entity.crawler.middle.BrowserCluster;
import com.microservice.dao.repository.crawler.middle.BrowserClusterRepository;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;

import app.commontracerlog.TracerLog;
import app.controller.MiddleController;


@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.middle")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.middle")
public class BrowserClusterService {
	
	@Autowired
	private BrowserClusterRepository browserClusterRepository;
	
	@Autowired
	private EurekaClient eurekaClient;
	
	@Autowired 
	private TracerLog tracer;
	
	private static final Logger log = LoggerFactory.getLogger(BrowserClusterService.class);
	
	private Application getApplicationByName(String appName){
		Application app = eurekaClient.getApplication(appName);
		return app;
	}
	
	//释放一个使用完毕的实例
	//提供严格的事务隔离。它要求事务序列化执行，事务只能一个接着一个地执行，不能并发执行。此隔离级别可以防止更新丢失、脏读、不可重复读、幻读。如果仅仅通过“行级锁”是无法实现事务序列化的，必须通过其他机制保证新插入的数据不会被刚执行查询操作的事务访问到。
	@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	public IdleInstance  findByAppNameAndInstanceIp(String appName,String instanceIp){
		tracer.addTag("findByAppNameAndInstanceIp",appName+"    "+instanceIp); 
		IdleInstance idleInstance = new IdleInstance();
		BrowserCluster browserCluster = browserClusterRepository.findByAppNameAndInstanceIp(appName,instanceIp); 
		if(browserCluster==null){
			tracer.addTag("browserCluster","browserCluster is null");
			idleInstance.setMessageCode("appName:"+appName+"  instanceIp:"+instanceIp+" NoExist");
			idleInstance.setMessage("appName:"+appName+"  instanceIp:"+instanceIp+" 不存在");
		}else{   
			tracer.addTag("browserCluster",browserCluster.toString());
			IdleInstanceInfo idleInstanceInfo = getIdleInstanceInfoFormEureka(appName,instanceIp); 
			idleInstance.setIdleInstanceInfo(idleInstanceInfo);
			//如果Eureka 上不存在这个实例，那么在数据库中删除
			if(idleInstanceInfo==null){
				tracer.addTag("删除数据库中的实例",browserCluster.toString()); 
				browserClusterRepository.delete(browserCluster);
			}
			
		} 
		
		tracer.addTag("changeInstanceStatus",instanceIp); 
		changeInstanceStatus(instanceIp, 0);
		if(idleInstance.getIdleInstanceInfo()!=null){ 
			idleInstance.setMessageCode("ReleaseIInstance");
			idleInstance.setMessage("释放一个已经使用完毕的实例");
		} else{
			idleInstance.setMessageCode("NoExistInstance"); 
			idleInstance.setMessage("该实例在Eureka中已不存在,已经在数据库中被删除");
		}
		
		return idleInstance;
	}
	
	/*private BrowserCluster  removeByAppNameAndInstanceIp(String appName,String instanceIp){
		return browserClusterRepository.removeByAppNameAndInstanceIp(appName,instanceIp);
	}*/
	
	private List<BrowserCluster>  findByAppName(String appName){
		return browserClusterRepository.findByAppName(appName);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	public IdleInstance  findByWindowHandle(String windowHandle){ 
		IdleInstance idleInstance = new IdleInstance();
		BrowserCluster browserCluster = browserClusterRepository.findByWindowHandle(windowHandle);
		if(browserCluster==null){
			idleInstance.setMessageCode("ThisWindowHandleNoExist");
			idleInstance.setMessage("windowHandle:"+windowHandle+" 不存在");
		}else{
			BrowserInfo browserInfo = new BrowserInfo();
			browserInfo.setHandler(windowHandle);
			idleInstance.setBrowserInfo(browserInfo);
			
			String appName = browserCluster.getAppName();
			String instanceIp = browserCluster.getInstanceIp();
			IdleInstanceInfo idleInstanceInfo = getIdleInstanceInfoFormEureka(appName,instanceIp); 
			idleInstance.setIdleInstanceInfo(idleInstanceInfo);
		} 
		 
		if(idleInstance.getIdleInstanceInfo()!=null){ 
			changeInstanceStatus(idleInstance.getIdleInstanceInfo().getIpAddr(), 1);
		} 
		return idleInstance;
	}
	
	
	private IdleInstanceInfo getIdleInstanceInfoFormEureka(String appName,String instanceIp){
		Application app = getApplicationByName(appName);  
		IdleInstanceInfo idleInstanceInfo = null;
		if(app!=null){
			List<InstanceInfo> instances = app.getInstances();  
			for(InstanceInfo instanceInfo:instances){
				if(instanceInfo.getIPAddr().equals(instanceIp)){ 
					idleInstanceInfo = new IdleInstanceInfo();
					idleInstanceInfo.setApp(instanceInfo.getAppName());
					idleInstanceInfo.setPort(instanceInfo.getPort());
					idleInstanceInfo.setHostName(instanceInfo.getHostName());
					idleInstanceInfo.setInstanceId(instanceInfo.getInstanceId());
					idleInstanceInfo.setIpAddr(instanceInfo.getIPAddr());
					idleInstanceInfo.setLastDirtyTimestamp(instanceInfo.getLastDirtyTimestamp());
					idleInstanceInfo.setLastUpdatedTimestamp(instanceInfo.getLastDirtyTimestamp()); 
					idleInstanceInfo.setMetadata(instanceInfo.getMetadata());
					idleInstanceInfo.setStatus(instanceInfo.getStatus().name());
					idleInstanceInfo.setVipAddress(instanceInfo.getVIPAddress()); 
					break;
				} 
			}
		} 
		return idleInstanceInfo; 
	}
	
	//提供严格的事务隔离。它要求事务序列化执行，事务只能一个接着一个地执行，不能并发执行。此隔离级别可以防止更新丢失、脏读、不可重复读、幻读。如果仅仅通过“行级锁”是无法实现事务序列化的，必须通过其他机制保证新插入的数据不会被刚执行查询操作的事务访问到。
	@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	public IdleInstance getIdleInstance(String appName,String requestPath,String taskid,Long intervalTime){ 
		IdleInstance idleInstance = new IdleInstance();
		InstanceInfo instanceInfo = getIdleInstanceInfo(appName);
		if(instanceInfo==null){ 
			tracer.addTag("getIdleInstance",appName+" 没有可用实例");
			idleInstance.setMessageCode("NoIdleInstance");
			idleInstance.setMessage("无可用的闲置实例");
		}else{
			idleInstance.setMessageCode("ExistIdleInstance");
			idleInstance.setMessage("存在一个可用的闲置实例");
			IdleInstanceInfo idleInstanceInfo = new IdleInstanceInfo();
			idleInstanceInfo.setApp(instanceInfo.getAppName());
			idleInstanceInfo.setPort(instanceInfo.getPort());
			idleInstanceInfo.setHostName(instanceInfo.getHostName());
			idleInstanceInfo.setInstanceId(instanceInfo.getInstanceId());
			idleInstanceInfo.setIpAddr(instanceInfo.getIPAddr());
			idleInstanceInfo.setLastDirtyTimestamp(instanceInfo.getLastDirtyTimestamp());
			idleInstanceInfo.setLastUpdatedTimestamp(instanceInfo.getLastDirtyTimestamp()); 
			idleInstanceInfo.setMetadata(instanceInfo.getMetadata());
			idleInstanceInfo.setStatus(instanceInfo.getStatus().name());
			idleInstanceInfo.setVipAddress(instanceInfo.getVIPAddress());
			idleInstance.setIdleInstanceInfo(idleInstanceInfo); 
			tracer.addTag("getIdleInstance",appName+" 存在一个可用的闲置实例"+idleInstance.toString());
		}
		
		tracer.addTag("BrowserClusterService getIdleInstance getIdleInstance", idleInstance.toString());  
		//如果获取到了一个闲置的实例，更改其状态inuse=1
		if(idleInstance.getIdleInstanceInfo()!=null){
			tracer.addTag("idleInstance.getIdleInstanceInfo()不为空",idleInstance.getIdleInstanceInfo().toString()); 
			changeInstanceStatusWithPath(idleInstance.getIdleInstanceInfo().getIpAddr(),requestPath, 1,appName,taskid,intervalTime); 
		}  
		return idleInstance;
	}
	
 
	//对Eureka 查询该微服务的所有节点
	public InstanceInfo getIdleInstanceInfo(String appName){
		//获取Eureka中该App的所有实例
		Application app = getApplicationByName(appName);  
		InstanceInfo idleInstanceInfo = null; 
		if(app!=null){
			List<InstanceInfo> instances = app.getInstances();  
			//获取数据库中该App的所有实例
			List<BrowserCluster> browserClusters = findByAppName(appName);   
			for(InstanceInfo instanceInfo:instances){  
				for(BrowserCluster bc:browserClusters){
					//如果该App在数据库中的使用状态是否（inuse = 0）则认为是闲置未使用状态
					if(instanceInfo.getIPAddr().equals(bc.getInstanceIp())&&bc.getInuse()==0){ 
						idleInstanceInfo = instanceInfo;
						return idleInstanceInfo; 
					}else{
						continue;
					}
				} 
			}
		}  
		
		return null;
		
	}
	

	private void changeInstanceStatusWithPath(String instanceIp,String requestPath,Integer inUse,String appName,String taskid,Long intervalTime){  
		
		List<BrowserCluster> browserClusters = browserClusterRepository.findByInstanceIp(instanceIp);
		if(!browserClusters.isEmpty()){
			for(BrowserCluster browserCluster:browserClusters){
				if(appName.equals(browserCluster.getAppName())){
					browserCluster.setRequestPath(requestPath);
					browserCluster.setTaskid(taskid);  
				} 
				browserCluster.setIntervalTime(intervalTime);
				browserCluster.setUpdateTime(new Date());
				browserCluster.setInuse(inUse);   
				browserClusterRepository.save(browserCluster);
			}
		} 
		//browserClusterRepository.updateByIpWithPath(inUse, (new Date()), requestPath, instanceIp,appName,taskid);
	}
	
	 
	private void changeInstanceStatus(String instanceIp,Integer inUse){  
		browserClusterRepository.updateByIp(inUse,(new Date()),instanceIp);
	}
	
	//初始化winio 集群 by appName
	//提供严格的事务隔离。它要求事务序列化执行，事务只能一个接着一个地执行，不能并发执行。此隔离级别可以防止更新丢失、脏读、不可重复读、幻读。如果仅仅通过“行级锁”是无法实现事务序列化的，必须通过其他机制保证新插入的数据不会被刚执行查询操作的事务访问到。
	@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	public List<BrowserCluster> initBrowserClusterLike(String appNameLike){ 
		//根据appName删除，
		List<BrowserCluster> bcs = browserClusterRepository.removeByAppNameLike(appNameLike.replace("*", "%"));
		 
		//然后从Eureka中同步
		List<BrowserCluster> browserClusters = new ArrayList<BrowserCluster>();
		Applications apps = eurekaClient.getApplications();
		for(Application app: apps.getRegisteredApplications()){ 
			if (app.getName().contains(appNameLike.replace("*", ""))) { 
				List<InstanceInfo> Instances = app.getInstances();
				//将Eureka中最新的数据添加到数据库中，先删除，再添加达到刷新的目的
				for (InstanceInfo instanceInfo : Instances) {
					String bool = instanceInfo.getMetadata().get("winio");
					if (bool!=null) {
						tracer.addTag("winio is","bool  "+bool+" appName " + app.getName() + " ip " + instanceInfo.getIPAddr());
						BrowserCluster bc = new BrowserCluster();
						bc.setAppName(app.getName());
						bc.setInstanceIp(instanceInfo.getIPAddr());
						bc.setPort(instanceInfo.getPort() + "");
						bc.setInuse(0);//这个地方不能一开始就是0  todo
						//bc.setWindowHandle("a672cfb6-7d5a-4385-8e5b-0c7e86f0ac6b");
						BrowserCluster browserCluster = browserClusterRepository.save(bc);
						browserClusters.add(browserCluster);
					}else{
						tracer.addTag("winio not exist",app.getName()+" bool  "+bool+" appName " + app.getName() + " ip " + instanceInfo.getIPAddr());
					}
				}
			}
		}
		return browserClusters;
	}
	
	//初始化winio 集群 by appName
	//提供严格的事务隔离。它要求事务序列化执行，事务只能一个接着一个地执行，不能并发执行。此隔离级别可以防止更新丢失、脏读、不可重复读、幻读。如果仅仅通过“行级锁”是无法实现事务序列化的，必须通过其他机制保证新插入的数据不会被刚执行查询操作的事务访问到。
	@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	public List<BrowserCluster> initBrowserCluster(String appName){ 
		//根据appName删除，
		List<BrowserCluster> bcs = browserClusterRepository.removeByAppName(appName);
		 
		//然后从Eureka中同步
		List<BrowserCluster> browserClusters = new ArrayList<BrowserCluster>();
		Applications apps = eurekaClient.getApplications();
		for(Application app: apps.getRegisteredApplications()){
			if (app.getName().equals(appName)) { 
				List<InstanceInfo> Instances = app.getInstances();
				//将Eureka中最新的数据添加到数据库中，先删除，再添加达到刷新的目的
				for (InstanceInfo instanceInfo : Instances) {
					String bool = instanceInfo.getMetadata().get("winio");
					if (bool!=null) {
						tracer.addTag("winio is","bool  "+bool+" appName " + app.getName() + " ip " + instanceInfo.getIPAddr());
						BrowserCluster bc = new BrowserCluster();
						bc.setAppName(app.getName());
						bc.setInstanceIp(instanceInfo.getIPAddr());
						bc.setPort(instanceInfo.getPort() + "");
						bc.setInuse(0);//这个地方不能一开始就是0  todo
						//bc.setWindowHandle("a672cfb6-7d5a-4385-8e5b-0c7e86f0ac6b");
						BrowserCluster browserCluster = browserClusterRepository.save(bc);
						browserClusters.add(browserCluster);
					}else{
						tracer.addTag("winio not exist",app.getName()+" bool  "+bool+" appName " + app.getName() + " ip " + instanceInfo.getIPAddr());
					}
				}
			}
		}
		return browserClusters;
	}
	
	//初始化整个winio 集群
	//提供严格的事务隔离。它要求事务序列化执行，事务只能一个接着一个地执行，不能并发执行。此隔离级别可以防止更新丢失、脏读、不可重复读、幻读。如果仅仅通过“行级锁”是无法实现事务序列化的，必须通过其他机制保证新插入的数据不会被刚执行查询操作的事务访问到。
	@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	public List<BrowserCluster> initAllBrowserCluster(){
		//删除全部，
		browserClusterRepository.deleteAll();
		//然后从Eureka中同步
		List<BrowserCluster> browserClusters = new ArrayList<BrowserCluster>();
		Applications apps = eurekaClient.getApplications(); 
		for(Application app:apps.getRegisteredApplications()){ 
			List<InstanceInfo> Instances = app.getInstances();  
			//将Eureka中最新的数据添加到数据库中，先删除，再添加达到刷新的目的
			for(InstanceInfo instanceInfo:Instances){ 
				String bool = instanceInfo.getMetadata().get("winio");
				if("true".equals(bool)){ 
					BrowserCluster bc = new BrowserCluster();
					bc.setAppName(app.getName());
					bc.setInstanceIp(instanceInfo.getIPAddr());
					bc.setPort(instanceInfo.getPort()+"");
					bc.setInuse(0);//这个地方不能一开始就是0  todo
					//bc.setWindowHandle("a672cfb6-7d5a-4385-8e5b-0c7e86f0ac6b");
					BrowserCluster browserCluster = browserClusterRepository.save(bc); 
					browserClusters.add(browserCluster) ; 
				} 
			}  
		}
		return browserClusters;
	}
	
	

}
