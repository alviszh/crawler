/**
 * 
 */
package com.microservice.dao.repository.crawler.monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorRancherInfo;
@Repository
public interface MonitorRancherInfoRepository extends JpaRepository<MonitorRancherInfo, Long> {
	//取出所有需要监控网络的主机信息
	@Query("select o from MonitorRancherInfo o where o.ismonitor=1") 
	List<MonitorRancherInfo> findAllNeedMonitor();
}
