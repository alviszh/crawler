/**
 * 
 */
package com.microservice.dao.repository.crawler.monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorInsuranceTasker;

@Repository
public interface MonitorInsuranceTaskerRepository extends JpaRepository<MonitorInsuranceTasker, Long> {
	//执行某一个需要监控的社保
	@Query("select o from MonitorInsuranceTasker o where o.city like ?1%") 
	MonitorInsuranceTasker executeOneWeb(String city);
	//查询所有需要监控的社保
	@Query("select o from MonitorInsuranceTasker o where o.isneedmonitor=1") 
	List<MonitorInsuranceTasker> findAllNeedMonitorWeb();
}
