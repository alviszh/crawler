package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileReportLocationStatistics;

public interface MobileReportLocationStatisticsRepository extends JpaRepository<MobileReportLocationStatistics, Long>{

	List<MobileReportLocationStatistics> findByTaskId(String taskid);
}
