/**
 * 
 */
package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.entity.system.MonitorEurekaServerInfo;
import app.repository.system.MonitorEurekaServerInfoRepository;


/**
 * @author sln
 * @date 2018年10月29日下午6:03:14
 * @Description: 提供查询服务
 */
@Component
//@Service
//@EntityScan(basePackages = "com.microservice.dao.entity.crawler.monitor")
//@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.monitor")
public class MonitorService {
	@Autowired
	private MonitorEurekaServerInfoRepository eurekaServerInfoRepository;
	public List<MonitorEurekaServerInfo> getEurekaList(){
		List<MonitorEurekaServerInfo> list = eurekaServerInfoRepository.findAllNeedMonitorAppName();
		return list;
	}
}
