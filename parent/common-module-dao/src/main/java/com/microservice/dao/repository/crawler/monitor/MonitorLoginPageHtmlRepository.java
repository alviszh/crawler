package com.microservice.dao.repository.crawler.monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorLoginPageHtml;
@Repository
public interface MonitorLoginPageHtmlRepository extends JpaRepository<MonitorLoginPageHtml, Long> {
	
	MonitorLoginPageHtml findTopByWebtypeOrderByCreatetimeDesc(String webtype);
	
	////////////////////////////////////////////////////////////
	//在最新执行的任务内容中筛选发生变动的内容
	@Query("select o.webtype,o.taskid,o.url,o.jscountchangedetail,o.developer,o.comparetaskid from MonitorLoginPageHtml o where o.taskid= ?1 and o.changeflag = true") 
	List<MonitorLoginPageHtml> findByChangeflag(String recentlyTaskid);
	
	
	//测试文本对比
	@Query("select o from MonitorLoginPageHtml o where o.webtype= ?1 and o.taskid = ?2") 
	MonitorLoginPageHtml test(String webType,String taskid);

	
	//查询js数量变化
	@Query("select o from MonitorLoginPageHtml o where o.taskid= ?1 and o.jscountchangeflag = true") 
	List<MonitorLoginPageHtml> findJsCountChangeByTaskid(String taskid);
}
