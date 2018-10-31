package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportStability;


public interface ProMobileReportStabilityRepository extends JpaRepository<ProMobileReportStability, Long>{

	List<ProMobileReportStability> findByTaskId(String taskid);
}
