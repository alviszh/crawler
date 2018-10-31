package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileReportBaseInfo;


public interface MobileReportBaseInfoRepository extends JpaRepository<MobileReportBaseInfo, Long>{

	List<MobileReportBaseInfo> findByTaskId(String taskid);
}
