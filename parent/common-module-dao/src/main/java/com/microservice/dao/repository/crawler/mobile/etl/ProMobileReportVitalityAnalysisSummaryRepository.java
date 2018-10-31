package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportVitalityAnalysisSummary;


public interface ProMobileReportVitalityAnalysisSummaryRepository extends JpaRepository<ProMobileReportVitalityAnalysisSummary, Long>{

	List<ProMobileReportVitalityAnalysisSummary> findByTaskId(String taskid);
}
