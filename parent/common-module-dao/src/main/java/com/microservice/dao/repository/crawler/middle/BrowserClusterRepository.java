package com.microservice.dao.repository.crawler.middle;

import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.dao.entity.crawler.middle.BrowserCluster; 


public interface BrowserClusterRepository  extends JpaRepository<BrowserCluster, Long>{
	
	//@Lock(LockModeType.READ)
	List<BrowserCluster> findByAppName(String appName);
	
	//一个appName和instanceIp 必定只存在一条记录，这里用一个对象接受，如果一个appName和instanceIp返回多个对象逻辑是存在问题的
	BrowserCluster findByAppNameAndInstanceIp(String appName,String instanceIp);
	
	@Transactional
	List<BrowserCluster> removeByAppName(String appName);
	
	@Transactional
	List<BrowserCluster> removeByAppNameLike(String appNameLike);
	
	@Transactional
	BrowserCluster removeByAppNameAndInstanceIp(String appName,String instanceIp);
	
	
	//@Lock(LockModeType.READ)
	List<BrowserCluster> findByInstanceIp(String instanceIp);
	
	//一个windowHandle 必定只存在一条记录，这里用一个对象接受，如果windowHandle返回多个对象逻辑是存在问题的
	BrowserCluster findByWindowHandle(String windowHandle);
	
	List<BrowserCluster> findByInuse(Integer inuse);
	
	@Modifying
	@Transactional
	@Query("update BrowserCluster u set u.inuse = ?1,u.updateTime=?2,u.taskid = null,u.requestPath = null where u.instanceIp = ?3")
	void updateByIp(Integer inuse,Date updatetime, String instanceIp);
	
	@Modifying
	@Transactional
	@Query("update BrowserCluster u set u.inuse = ?1,u.updateTime=?2,u.requestPath=?3,u.taskid=?6 where u.instanceIp = ?4 and u.appName=?5")
	void updateByIpWithPath(Integer inuse,Date updatetime,String requestPath, String instanceIp,String appName,String taskid);
	
	//@Transactional
	//List<BrowserCluster> updateByAppName(String instanceIp);

}
