/**
 * 
 */
package com.microservice.dao.repository.crawler.monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorHousingFundTasker;


/**
 * @author sln
 * @date 2018年8月23日上午11:54:37
 * @Description: 
 */
@Repository
public interface MonitorHousingFundTaskerRepository extends JpaRepository<MonitorHousingFundTasker, Long> {
	//执行某一个需要监控的公积金
	@Query("select o from MonitorHousingFundTasker o where o.city like ?1%") 
	MonitorHousingFundTasker executeOneWeb(String city);
	
	
	//查询所有需要监控的公积金
	@Query("select o from MonitorHousingFundTasker o where o.isneedmonitor=1") 
	List<MonitorHousingFundTasker> findAllNeedMonitorWeb();

}
