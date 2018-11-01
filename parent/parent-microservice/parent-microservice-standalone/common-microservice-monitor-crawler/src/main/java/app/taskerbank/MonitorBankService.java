package app.taskerbank;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.monitor.json.MonitorAllWebBankTempBean;
import com.microservice.dao.entity.crawler.monitor.MonitorAllWebBank;
import com.microservice.dao.repository.crawler.monitor.MonitorAllWebBankRepository;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

/**
 * @author sln
 * 	监测银行微服务是否存在
 * //0表示可用，1表示升级中(eureka上无服务)，2表示维护中(网站无法访问)
 */
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.monitor")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.monitor")
public class MonitorBankService {
	public static final Logger log = LoggerFactory.getLogger(MonitorBankService.class);
	@Autowired
	private MonitorAllWebBankRepository allWebBankRepository;
	@Autowired
	private EurekaClient eurekaClient;
//	@Cacheable(value="mycache" ,key="'proxyIpSet'")
	public List<MonitorAllWebBankTempBean> linkbankUsable() {
		List<MonitorAllWebBankTempBean> tempList=new ArrayList<MonitorAllWebBankTempBean>();
		MonitorAllWebBankTempBean tempBean=null;
		List<MonitorAllWebBank> urlList = allWebBankRepository.findAllBankNeedMonitor(); 
		if(urlList!=null && urlList.size()>0){
			Application app = null;
			String appName="";
			for (MonitorAllWebBank bank : urlList) {
				appName=bank.getAppname().trim();
				app=eurekaClient.getApplication(appName);  //从eureka上找寻指定的微服务  
				if(null!=app){  
					List<InstanceInfo> instancesList = app.getInstances();
					if(instancesList!=null && instancesList.size()>0){
						tempBean=new MonitorAllWebBankTempBean(bank.getBanktype().trim(),bank.getCardtype().trim(),0);
					}else{   //有时候没有微服务，不响应null,但是instancesList集合为空
						tempBean=new MonitorAllWebBankTempBean(bank.getBanktype().trim(),bank.getCardtype().trim(),1);
					}
				}else{   //1  微服务不存在   
					tempBean=new MonitorAllWebBankTempBean(bank.getBanktype().trim(),bank.getCardtype().trim(),1);
				}
				tempList.add(tempBean);
			}
		}
		log.info("本轮Eureka 银行微服务  节点变化监控任务已经执行完毕");
		return tempList;
	}
}
