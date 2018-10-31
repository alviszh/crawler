package com.microservice.dao.repository.crawler.cmcc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.cmcc.CmccPayMsgResult;

public interface CmccPayMsgResultRepository extends JpaRepository<CmccPayMsgResult, Long>{
	
	List<CmccPayMsgResult> findByTaskId(String taskId);

}
