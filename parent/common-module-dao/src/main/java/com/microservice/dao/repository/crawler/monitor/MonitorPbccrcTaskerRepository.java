/**
 * 
 */
package com.microservice.dao.repository.crawler.monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorPbccrcTasker;

@Repository
public interface MonitorPbccrcTaskerRepository extends JpaRepository<MonitorPbccrcTasker, Long> {
	//执行某一个需要定时爬取的人行征信，根据用户名
	@Query("select o from MonitorPbccrcTasker o where o.username like ?1%") 
	MonitorPbccrcTasker executeOnePbccrc(String username);
	//查询所有可以提供爬取的人行征信账号
	@Query("select o from MonitorPbccrcTasker o where o.isneedmonitor=1") 
	List<MonitorPbccrcTasker> findAllNeedMonitorPbccrc();
}
