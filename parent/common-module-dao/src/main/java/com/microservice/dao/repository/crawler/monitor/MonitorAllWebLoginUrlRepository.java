package com.microservice.dao.repository.crawler.monitor;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorAllWebLoginUrl;

/**
 * @description:
 * @author: sln 
 * @date: 2018年1月31日 下午3:55:55 
 */
@Repository
public interface MonitorAllWebLoginUrlRepository extends JpaRepository<MonitorAllWebLoginUrl, Long> {
	//取出所有需要监控的，且可用性为true的网站
	@Query("select o from MonitorAllWebLoginUrl o where o.isusable = true and o.isneedmonitor=1") 
	List<MonitorAllWebLoginUrl> findUrlUsable();
	
	//根据网站可用性监测结果将可用性字段进行实时更新
	@Transactional
	@Modifying
	@Query(value = "update MonitorAllWebLoginUrl o set o.isusable = ?1 where o.id = ?2")
	void updateUsableFlag(boolean flag,long id);
	
	//根据开发者，取出所有需要监控的，且可用性为true的网站
//	@Query("select o from MonitorAllWebLoginUrl o where o.isusable = true and o.isneedmonitor=1 and o.developer=?1 and o.isusable = true") 
	@Query("select o from MonitorAllWebLoginUrl o where o.isusable = true and o.isneedmonitor=1 and o.developer=?1") 
	List<MonitorAllWebLoginUrl> findUrlUsableByDeveloper(String developer);
	
	//取出该开发者名下所有需要监控的网站
	@Query("select o from MonitorAllWebLoginUrl o where o.developer=?1 and o.isneedmonitor=1") 
	List<MonitorAllWebLoginUrl> findUrlByDeveloper(String developer);
	
	//查询所有的需要监控的网站，展示可用性与否
	@Query("select o.webtype,o.isusable,o.developer from MonitorAllWebLoginUrl o where o.isneedmonitor=1") 
	List<MonitorAllWebLoginUrl> findAllWeb();
	
	//检测出数据库中需要监测的网站对应的开发者
	@Query("select distinct o.developer from MonitorAllWebLoginUrl o") 
	List<String> findAllDeveloper();
}
