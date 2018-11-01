package app.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.InstanceInfo;

import app.bean.json.EurekaInstanceBean;
import app.service.amqp.outbound.Source;

@Component
@EnableBinding(Source.class)
public class EurekaStateChangeListener {
	
	private static final Logger log = LoggerFactory.getLogger(EurekaStateChangeListener.class);
	
	@Autowired
	private Source eurekaOutput;
	
	//服务下线
	@EventListener
	public void listen(EurekaInstanceCanceledEvent event) {
		// 服务断线事件  
		EurekaInstanceBean cb = new EurekaInstanceBean();
		cb.setEventType("CanceledEvent");
		cb.setAppName(event.getAppName());
		cb.setServerId(event.getServerId());
		cb.setTimestamp(event.getTimestamp());
		System.out.println("======断线事件："+cb.toString()); 
		// 发送开始通知 
		eurekaOutput.output().send(MessageBuilder.withPayload(cb).build()); 
	}

	//服务注册
	@EventListener
	public void listen(EurekaInstanceRegisteredEvent event) {
		// 服务注册事件
		InstanceInfo instanceInfo = event.getInstanceInfo();
		EurekaInstanceBean cb = new EurekaInstanceBean();
		cb.setEventType("RegisteredEvent");
		cb.setAppName(instanceInfo.getAppName());
		cb.setHomePageUrl(instanceInfo.getHomePageUrl());
		cb.setHostName(instanceInfo.getHostName());
		cb.setInstanceId(instanceInfo.getInstanceId());
		cb.setIpAddr(instanceInfo.getIPAddr());
		cb.setTimestamp(instanceInfo.getLastUpdatedTimestamp());
		cb.setVipAddress(instanceInfo.getVIPAddress());
		System.out.println("======注册事件："+cb.toString()); 
		// 发送开始通知 
		eurekaOutput.output().send(MessageBuilder.withPayload(cb).build());
				
		/*
		InstanceInfo instanceInfo = event.getInstanceInfo();
		if(instanceInfo!=null){
			String appName = instanceInfo.getAppName(); 
			String instanceIp = instanceInfo.getIPAddr();
			String bool = instanceInfo.getMetadata().get("winio");
			if (bool!=null) {
				BrowserCluster bc = browserClusterRepository.findByAppNameAndInstanceIp(appName, instanceIp);
				if(bc==null){
					bc = new BrowserCluster();
				}  
				log.info("winio is  "+bool+" appName " + appName + " ip " + instanceInfo.getIPAddr()); 
				bc.setAppName(appName);
				bc.setInstanceIp(instanceIp);
				bc.setPort(instanceInfo.getPort() + "");
				bc.setInuse(0);//这个地方不能一开始就是0  todo 
				BrowserCluster browserCluster = browserClusterRepository.save(bc);
				log.info("初始化完成 : " + browserCluster.toString());
			}else{
				log.info(appName+" winio not exist  "+bool+" appName " + appName + " ip " + instanceInfo.getIPAddr());
			} 
		}
		 */
		
		/*System.out.println("EurekaInstanceRegisteredEvent---->" + event);
		System.out.println("instanceInfo getInstanceId---->" + instanceInfo.getInstanceId());
		System.out.println("instanceInfo getHostName---->" + instanceInfo.getHostName());
		System.out.println("instanceInfo getIPAddr---->" + instanceInfo.getIPAddr());
		System.out.println("instanceInfo getHomePageUrl---->" + instanceInfo.getHomePageUrl());
		System.out.println("instanceInfo getVIPAddress---->" + instanceInfo.getVIPAddress());
		System.out.println("instanceInfo getLastUpdatedTimestamp---->" + instanceInfo.getLastUpdatedTimestamp());
		System.out.println("instanceInfo getLastDirtyTimestamp---->" + instanceInfo.getLastDirtyTimestamp());
		System.out.println("instanceInfo getAppGroupName---->" + instanceInfo.getAppGroupName());
		System.out.println("instanceInfo getAppName---->" + instanceInfo.getAppName());
		System.out.println("instanceInfo getASGName---->" + instanceInfo.getASGName());
		System.out.println("instanceInfo getCountryId---->" + instanceInfo.getCountryId());
		*/

		
	}


	//续约时间很频繁，没30s，所有未付都续约一下
	@EventListener
	public void listen(EurekaInstanceRenewedEvent event) {

		/*
		System.out.println("EurekaInstanceRenewedEvent---->" + event);
		System.out.println("EurekaInstanceRegisteredEvent---->" + event);
		System.out.println("instanceInfo getInstanceId---->" + instanceInfo.getInstanceId());
		System.out.println("instanceInfo getHostName---->" + instanceInfo.getHostName());
		System.out.println("instanceInfo getIPAddr---->" + instanceInfo.getIPAddr());
		System.out.println("instanceInfo getHomePageUrl---->" + instanceInfo.getHomePageUrl());
		System.out.println("instanceInfo getVIPAddress---->" + instanceInfo.getVIPAddress());
		System.out.println("instanceInfo getLastUpdatedTimestamp---->" + instanceInfo.getLastUpdatedTimestamp());
		System.out.println("instanceInfo getLastDirtyTimestamp---->" + instanceInfo.getLastDirtyTimestamp());
		*/
		
	}

	@EventListener
	public void listen(EurekaRegistryAvailableEvent event) {
		System.out.println("EurekaRegistryAvailableEvent---->" + event);
	}

	@EventListener
	public void listen(EurekaServerStartedEvent event) {
		// Server启动
		System.out.println("EurekaServerStartedEvent---->" + event);
	}

}
