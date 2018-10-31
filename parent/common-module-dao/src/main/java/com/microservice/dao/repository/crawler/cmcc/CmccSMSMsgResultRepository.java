package com.microservice.dao.repository.crawler.cmcc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.cmcc.CmccSMSMsgResult;

public interface CmccSMSMsgResultRepository extends JpaRepository<CmccSMSMsgResult, Long>{

	List<CmccSMSMsgResult> findByTaskId(String taskId);
	
}
