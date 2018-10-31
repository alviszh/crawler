package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileReportCallStatistics;

public interface MobileReportCallStatisticsRepository extends JpaRepository<MobileReportCallStatistics, Long>{

	List<MobileReportCallStatistics> findByTaskId(String taskid);
}
