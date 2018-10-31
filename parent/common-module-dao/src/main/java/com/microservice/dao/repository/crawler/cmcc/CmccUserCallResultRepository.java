package com.microservice.dao.repository.crawler.cmcc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.cmcc.CmccUserCallResult;

public interface CmccUserCallResultRepository extends JpaRepository<CmccUserCallResult, Long>{

	List<CmccUserCallResult> findByTaskId(String taskId);
	//勿删   2018年4月8日为统计指定taskid下的通话记录总数而添加的方法
	int countByTaskId(String taskId); 
}
