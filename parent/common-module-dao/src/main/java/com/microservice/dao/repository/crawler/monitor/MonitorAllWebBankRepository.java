package com.microservice.dao.repository.crawler.monitor;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorAllWebBank;

@Repository
public interface MonitorAllWebBankRepository extends JpaRepository<MonitorAllWebBank, Long> {
	@Query("select DISTINCT url from MonitorAllWebBank") 
	List<String> findAllUrl();
	
	//查询一条，如果相同的记录出现多次
	MonitorAllWebBank findTopByUrl(String url);
	
	
	//根据网站可用性监测结果将可用性字段进行更新
	@Transactional
	@Modifying
	@Query(value = "update MonitorAllWebBank o set o.usablestate = ?1 where o.url = ?2")
	void updateUsableFlag(int flag,String url);
	
	
	//根据微服务可用性监测结果将可用性字段进行更新
	@Transactional
	@Modifying
	@Query(value = "update MonitorAllWebBank o set o.usablestate = ?1 where o.appname = ?2")
	void updateUsableFlagByAppName(int flag,String appname);

	
//	@Query("select DISTINCT appname from MonitorAllWebBank")
	@Query("select appname from MonitorAllWebBank") 
	List<String> findAllAppName();

	@Query("select o from MonitorAllWebBank o where o.logintype not like 'ACCOUNT_NUM'") 
	List<MonitorAllWebBank> findResult();
	
	@Query("select o from MonitorAllWebBank o where o.isneedmonitor=1") 
	List<MonitorAllWebBank> findAllBankNeedMonitor();
	
}
