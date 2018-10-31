package com.microservice.dao.repository.crawler.monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorEurekaServerInfo;
@Repository
public interface MonitorEurekaServerInfoRepository extends JpaRepository<MonitorEurekaServerInfo, Long> {
	@Query("select o from MonitorEurekaServerInfo o where o.isneedmonitor=1") 
	List<MonitorEurekaServerInfo> findAllNeedMonitorAppName();
}
