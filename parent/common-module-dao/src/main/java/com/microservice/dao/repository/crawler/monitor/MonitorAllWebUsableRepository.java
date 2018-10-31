package com.microservice.dao.repository.crawler.monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorAllWebUsable;

@Repository
public interface MonitorAllWebUsableRepository extends JpaRepository<MonitorAllWebUsable, Long> {
	@Query("select o from MonitorAllWebUsable o where o.taskid= ?1 and o.isusable=false") 
	List<MonitorAllWebUsable> findWebNotUsable(String recentlyTaskid);
	
	
	//查出指定网站最近几次执行对应的状态码
	@Query(value="select * from monitor_allweb_usable o where o.webtype= ?1 order by createtime desc limit 5",nativeQuery=true) 
	List<MonitorAllWebUsable> findWebNotUsableLimitFive(String webtype);
	
	//查出网站近五天中每天的状态，选择每一天执行的最新一条记录（根据网站名称）
	@Query(value="select b.* from ( select a.*, row_number() over(partition by a.webtype,to_char(a.createtime,'yyyy-mm-dd') order by to_char(a.createtime,'yyyy-mm-dd') desc) as rn from monitor_allweb_usable a where a.createtime >= now() - interval '5 days' ) b where b.rn = 1 and b.webtype like ?1%",nativeQuery=true)
	List<MonitorAllWebUsable> findDaysWebUsableTaskerResult(String webtype);
	
	//查询最近一次任务的taskid
	@Query(value="select * from monitor_allweb_usable order by createtime desc limit 1",nativeQuery=true)
	MonitorAllWebUsable findTopOrderByCreatetimeDesc();
	
	
	//根据最近一次任务的taskid，查询该任务下所有网站的可用情况（用于当前页面展示）
	@Query(value="select o from MonitorAllWebUsable o where o.taskid= ?1") 
	List<MonitorAllWebUsable> findAllWebByRecentlyTaskid(String taskid);
}
