package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportPeriodStatistics;


public interface ProMobileReportPeriodStatisticsRepository extends JpaRepository<ProMobileReportPeriodStatistics, Long>{

	List<ProMobileReportPeriodStatistics> findByTaskId(String taskid);
}
