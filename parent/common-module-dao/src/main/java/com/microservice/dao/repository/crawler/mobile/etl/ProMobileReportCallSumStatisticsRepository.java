package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportCallSumStatistics;


public interface ProMobileReportCallSumStatisticsRepository extends JpaRepository<ProMobileReportCallSumStatistics, Long>{

	List<ProMobileReportCallSumStatistics> findByTaskId(String taskid);
}
