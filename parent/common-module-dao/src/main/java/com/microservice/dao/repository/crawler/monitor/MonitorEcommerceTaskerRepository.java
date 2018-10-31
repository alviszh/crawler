/**
 * 
 */
package com.microservice.dao.repository.crawler.monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorEcommerceTasker;


/**
 * @author sln
 * @date 2018年8月23日上午11:54:37
 * @Description: 
 */
@Repository
public interface MonitorEcommerceTaskerRepository extends JpaRepository<MonitorEcommerceTasker, Long> {
	//执行某一个需要监控的电商
	@Query("select o from MonitorEcommerceTasker o where o.webtype = ?1") 
	MonitorEcommerceTasker executeOneWeb(String webtype);
	
	
	//查询所有需要监控的电商
	@Query("select o from MonitorEcommerceTasker o where o.isneedmonitor=1") 
	List<MonitorEcommerceTasker> findAllNeedMonitorWeb();

}
