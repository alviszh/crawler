package com.microservice.dao.repository.crawler.cmcc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.cmcc.CmccUserInfo;

public interface CmccUserInfoRepository extends JpaRepository<CmccUserInfo, Long>{
	
	List<CmccUserInfo> findByTaskId(String taskId);

}
