package com.microservice.dao.repository.crawler.monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorTelecomTasker;

@Repository
public interface MonitorTelecomTaskerRepository extends JpaRepository<MonitorTelecomTasker, Long> {
	@Query("select o from MonitorTelecomTasker o where o.province like ?1%") 
	MonitorTelecomTasker executeOneWeb(String province);
	
	//如下查询方式，可以查询同样省份下所有卡片，从而统一调用执行
	@Query("select o from MonitorTelecomTasker o where o.province like ?1%") 
	List<MonitorTelecomTasker> executeOneProvince(String province);
	
	
	//查询所有需要监控的电信
	@Query("select o from MonitorTelecomTasker o where o.isneedmonitor=1") 
	List<MonitorTelecomTasker> findAllNeedMonitorTelecom();
}
