package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileReportFamilyInfo;

public interface MobileReportFamilyInfoRepository extends JpaRepository<MobileReportFamilyInfo, Long>{
	
	List<MobileReportFamilyInfo> findByTaskId(String taskid);

}
