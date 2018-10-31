package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileCallInfo;

public interface ProMobileCallInfoRepository  extends JpaRepository<ProMobileCallInfo, Long> {

	List<ProMobileCallInfo> findByTaskId(String taskid);
	
	//勿删 
	int countByTaskId(String taskId); 
}
