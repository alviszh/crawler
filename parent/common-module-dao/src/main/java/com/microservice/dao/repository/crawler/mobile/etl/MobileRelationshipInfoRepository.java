package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileRelationshipInfo;

public interface MobileRelationshipInfoRepository extends JpaRepository<MobileRelationshipInfo, Long>{

	List<MobileRelationshipInfo> findByTaskId(String taskid);

}
