package app.eureka;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.monitor.json.MonitorEurekaChange;
import com.microservice.dao.entity.crawler.monitor.MonitorEurekaServerInfo;
import com.microservice.dao.repository.crawler.monitor.MonitorEurekaServerInfoRepository;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;


/**
 * 该类用于检测eureka上的微服务变动情况以及节点变动情况
 * 
 * 要是检测出数据库中没有出现的节点，就将新出现的添加到数据库中
 * 
 * 根据数据库内容，到eureka上进行对比
 * 
 * 用log日志，如果打tracer日志的话，太频繁
 * 
 * @author sln
 *
 */
@Component
public class MonitorEurekaService {
	private static final Logger log = LoggerFactory.getLogger(MonitorEurekaService.class);
	@Autowired
	private EurekaClient eurekaClient;
	@Autowired
	private MonitorEurekaServerInfoRepository eurekaServerInfoRepository;
	@Autowired
	private MonitorEurekaMailService eurekaMailService;
	public void eurekaTasker(){
		List<MonitorEurekaChange> changeList = new ArrayList<MonitorEurekaChange>();
		List<MonitorEurekaServerInfo> list = eurekaServerInfoRepository.findAllNeedMonitorAppName();
		if(null!=list && list.size()>0){
			MonitorEurekaChange monitorEurekaChange= null;
			String appName = "";
			int instanceCount = 0;
			String developer = "";
			Application app = null;
			String servicename="";
			for (MonitorEurekaServerInfo monitorEurekaServerInfo : list) {
				monitorEurekaChange = new MonitorEurekaChange();
				appName = monitorEurekaServerInfo.getAppname().trim();  //从数据库中取出要监控的微服务
				developer = monitorEurekaServerInfo.getDeveloper().trim();
				instanceCount = monitorEurekaServerInfo.getInstancecount(); //从数据库中取出该微服务指定的节点数
				servicename=monitorEurekaServerInfo.getServicename().trim();
				if(instanceCount==0){   //暂时排除在监控范围之外
					log.info(appName+"  暂时排除在监控范围之外");
				}else{
					app = eurekaClient.getApplication(appName);  //从eureka上找寻指定的微服务  
					if(app!=null){ //eureka上存在该服务，取出实际节点数
						List<InstanceInfo> instances = app.getInstances(); 
						int actualInstanceCount = instances.size();  //实际节点数（实例数量增加不用通知）
						if(actualInstanceCount<instanceCount){  //只监测节点数减少
							monitorEurekaChange.setActualInstanceCount(actualInstanceCount);
							monitorEurekaChange.setAppname(appName);
							monitorEurekaChange.setChangeDetail("微服务节点减少,请尽快处理~");
							monitorEurekaChange.setInstancecount(instanceCount);
							monitorEurekaChange.setDeveloper(developer);
							monitorEurekaChange.setServicename(servicename);
							changeList.add(monitorEurekaChange);
						}
					}else{
						log.info(appName+"  在Eureka上未找到相关实例,请检查该微服务启动情况");
						monitorEurekaChange.setActualInstanceCount(0);
						monitorEurekaChange.setAppname(appName);
						monitorEurekaChange.setChangeDetail("微服务被外星人偷走了！！！");
						monitorEurekaChange.setInstancecount(instanceCount);
						monitorEurekaChange.setDeveloper(developer);
						monitorEurekaChange.setServicename(servicename);
						changeList.add(monitorEurekaChange);
					}
				}
			}
			log.info("本轮Eureka微服务节点变化监控任务已经执行完毕");
			//调用邮件通知服务
			if(changeList!=null && changeList.size()>0){
				eurekaMailService.sendResultMail(changeList);
			}
		}
	}
}
