package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileFamilyNetInfo;

public interface MobileFamilyNetInfoRepository extends JpaRepository<MobileFamilyNetInfo, Long>{

	List<MobileFamilyNetInfo> findByTaskId(String taskid);

}
