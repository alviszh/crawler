package com.microservice.dao.repository.crawler.cmcc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.cmcc.CmccCheckMsgResult;

public interface CmccCheckMsgResultRepository extends JpaRepository<CmccCheckMsgResult, Long>{
	
	List<CmccCheckMsgResult> findByTaskId(String taskId);

}
